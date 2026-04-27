package com.claro.sp.automation.lineas.repository;

import com.claro.sp.automation.lineas.domain.line.AssignSimRequest;
import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.LineOperationType;
import com.claro.sp.automation.lineas.domain.line.LineResult;
import com.claro.sp.automation.lineas.domain.line.SimData;
import com.claro.sp.automation.lineas.exception.LineasException;
import com.claro.sp.automation.lineas.mapper.ccard.CcardLineMapper;
import com.claro.sp.automation.lineas.mapper.prod.ProdLineMapper;
import java.security.SecureRandom;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MyBatisLineRepository implements LineRepository {
    private final SqlSessionFactory prodSessionFactory;
    private final SqlSessionFactory ccardSessionFactory;
    private final SecureRandom random = new SecureRandom();

    public MyBatisLineRepository(SqlSessionFactory prodSessionFactory, SqlSessionFactory ccardSessionFactory) {
        this.prodSessionFactory = prodSessionFactory;
        this.ccardSessionFactory = ccardSessionFactory;
    }

    @Override
    public boolean isCellularAvailable(String cellularNumber) {
        try (SqlSession session = prodSessionFactory.openSession()) {
            return session.getMapper(ProdLineMapper.class).countCellular(cellularNumber) == 0;
        }
    }

    @Override
    public boolean isHandleAvailable(String handle) {
        try (SqlSession session = ccardSessionFactory.openSession()) {
            return session.getMapper(CcardLineMapper.class).countHandle(handle) == 0;
        }
    }

    @Override
    public String findCcardPrefix() {
        try (SqlSession session = ccardSessionFactory.openSession()) {
            return session.getMapper(CcardLineMapper.class).findPrefix();
        }
    }

    @Override
    public String findAvailableNumber(CreationContext context) {
        try (SqlSession session = prodSessionFactory.openSession()) {
            ProdLineMapper mapper = session.getMapper(ProdLineMapper.class);
            String nim;
            if (context.isArgentina() && !"N".equalsIgnoreCase(context.getNode())) {
                nim = mapper.findRandomNimArNode(context);
            } else if (context.isArgentina()) {
                nim = mapper.findRandomNimAr(context);
            } else if (context.isParaguay()) {
                nim = mapper.findRandomNimPy(context);
            } else if (context.isUruguay()) {
                nim = mapper.findRandomNimUy(context);
            } else {
                throw new LineasException("Pais no soportado: " + context.getCountryCode());
            }
            return calibrateLength(nim, context.getExpectedLineLength());
        }
    }

    @Override
    public String findNextHandle() {
        try (SqlSession session = ccardSessionFactory.openSession()) {
            return session.getMapper(CcardLineMapper.class).nextHandle();
        }
    }

    @Override
    public String findRatePlanId(String profileId) {
        try (SqlSession session = prodSessionFactory.openSession()) {
            return session.getMapper(ProdLineMapper.class).findRatePlanId(profileId);
        }
    }

    @Override
    public void createLine(CreationContext context) {
        try (SqlSession prodSession = prodSessionFactory.openSession(false);
             SqlSession ccardSession = ccardSessionFactory.openSession(false)) {
            ProdLineMapper prod = prodSession.getMapper(ProdLineMapper.class);
            CcardLineMapper ccard = ccardSession.getMapper(CcardLineMapper.class);

            insertBaseProdData(context, prod);
            insertBaseCcardData(context, ccard);
            if (context.isGenerateSim()) {
                createAndAssignSim(context, prod);
            } else {
                context.setImsi(context.getSubId());
            }
            insertStatusAndPlan(context, prod, ccard);

            prodSession.commit();
            ccardSession.commit();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void rollbackCreatedLine(CreationContext context) {
        try (SqlSession prodSession = prodSessionFactory.openSession(false);
             SqlSession ccardSession = ccardSessionFactory.openSession(false)) {
            if (context.getCellularNumber() != null) {
                prodSession.getMapper(ProdLineMapper.class).deleteSubscriber(context.getCellularNumber(), "WEB");
                ccardSession.getMapper(CcardLineMapper.class).deleteSubscriber(context.getCellularNumber(), context.getCcardOwner());
            }
            prodSession.commit();
            ccardSession.commit();
        }
    }

    @Override
    public LineResult toResult(CreationContext context) {
        LineResult result = new LineResult();
        result.setOperationType(LineOperationType.CREATION);
        result.setCellularNumber(context.getCellularNumber());
        result.setBillNumber(context.getBillNumber());
        result.setSubId(context.getSubId());
        result.setHandle(context.getHandle());
        result.setImsi(context.getImsi());
        result.setIccid(context.getIccid());
        return result;
    }

    private void insertBaseProdData(CreationContext context, ProdLineMapper prod) {
        context.setRatePlanSth(prod.findRatePlanId(context.getProfileId()));
        if (context.getRatePlanSth() == null) {
            throw new LineasException("No se encontro rate plan STH para profileId " + context.getProfileId());
        }
        prod.insertActivationRange(context);
        prod.insertCellular(context);
        if (context.isDistinctBillNumber()) {
            prod.updateBillNumber(context);
        }
        context.setSubId(prod.findSubId(context.getBillNumber(), context.getCellularNumber()));
        if (context.getSubId() == null) {
            throw new LineasException("No se pudo obtener subId para la linea " + context.getCellularNumber());
        }
        prod.insertBusinessHistory(context);
        prod.insertCellularAccount(nextFreeSequence(prod, "SEQ_CELLULAR_ACCOUNTS", SequenceTarget.ACCOUNT), context);
    }

    private void insertBaseCcardData(CreationContext context, CcardLineMapper ccard) {
        ccard.insertPrepayCellular(context);
        ccard.insertNumberChange(context);
    }

    private void createAndAssignSim(CreationContext context, ProdLineMapper prod) {
        prod.createSim(context.getCountryCode().name(), context.getSubId());
        SimData sim = prod.findSim(context.getSubId());
        if (sim == null) {
            throw new LineasException("No hay SIM registrada para el subId " + context.getSubId());
        }
        context.setImsi(sim.getImsi());
        context.setIccid(sim.getIccid());
        prod.updateCellularEsn(trimTrailingF(sim.getIccid()), context.getCellularNumber());

        AssignSimRequest request = new AssignSimRequest();
        request.setCellularNumber(context.getCellularNumber());
        request.setIccid(sim.getIccid());
        request.setDealerId(context.getDealerId());
        request.setSubId(context.getSubId());
        request.setCetId(randomCetId());
        prod.assignSimToCellular(request);
    }

    private void insertStatusAndPlan(CreationContext context, ProdLineMapper prod, CcardLineMapper ccard) {
        prod.insertServiceStatus(context);
        prod.insertCellularCallRestriction(nextFreeSequence(prod, "CCR_SEQ", SequenceTarget.CALL_RESTRICTION), context);
        ccard.insertPpServiceStatus(context);
        prod.insertRatePlan(context);
        List<String> basicPackIds = prod.findBasicPackIds(context.getCellularNumber());
        for (String packId : basicPackIds) {
            prod.insertCellularPackage(context.getCellularNumber(), packId);
        }
        if ("CR".equalsIgnoreCase(context.getBusinessType())) {
            prod.insertCreditLimit(context);
        }
    }

    private String nextFreeSequence(ProdLineMapper prod, String sequenceName, SequenceTarget target) {
        for (int i = 0; i < 20; i++) {
            String id = prod.nextSequenceId(sequenceName);
            boolean exists = switch (target) {
                case ACCOUNT -> prod.countCellularAccountId(id) > 0;
                case CALL_RESTRICTION -> prod.countCallRestrictionId(id) > 0;
            };
            if (!exists) {
                return id;
            }
        }
        throw new LineasException("No se pudo reservar secuencia libre para " + sequenceName);
    }

    private String calibrateLength(String nim, int length) {
        if (nim == null) {
            return null;
        }
        if (nim.length() != length) {
            String base = nim.substring(0, nim.length() - 1).concat("0");
            return base.concat(nim.substring(nim.length() - 1));
        }
        return nim;
    }

    private String trimTrailingF(String iccid) {
        if (iccid == null) {
            return null;
        }
        return iccid.endsWith("F") ? iccid.substring(0, iccid.length() - 1) : iccid;
    }

    private String randomCetId() {
        return "5497" + (10000000 + random.nextInt(90000000));
    }

    private enum SequenceTarget {
        ACCOUNT,
        CALL_RESTRICTION
    }
}


