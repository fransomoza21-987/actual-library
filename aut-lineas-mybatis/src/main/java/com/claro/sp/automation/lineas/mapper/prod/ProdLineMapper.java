package com.claro.sp.automation.lineas.mapper.prod;

import com.claro.sp.automation.lineas.domain.line.AssignSimRequest;
import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.SimData;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProdLineMapper {
    String findRandomNimAr(CreationContext context);
    String findRandomNimArNode(CreationContext context);
    String findRandomNimPy(CreationContext context);
    String findRandomNimUy(CreationContext context);
    int countCellular(@Param("cellularNumber") String cellularNumber);
    String findSubId(@Param("billNumber") String billNumber, @Param("cellularNumber") String cellularNumber);
    String findRatePlanId(@Param("profileId") String profileId);
    String nextSequenceId(@Param("sequenceName") String sequenceName);
    int countCellularAccountId(@Param("id") String id);
    int countCallRestrictionId(@Param("id") String id);
    void insertActivationRange(CreationContext context);
    void insertCellular(CreationContext context);
    void updateBillNumber(CreationContext context);
    void insertBusinessHistory(CreationContext context);
    void insertCellularAccount(@Param("id") String id, @Param("context") CreationContext context);
    void createSim(@Param("country") String country, @Param("subId") String subId);
    SimData findSim(@Param("subId") String subId);
    void updateCellularEsn(@Param("esn") String esn, @Param("cellularNumber") String cellularNumber);
    void assignSimToCellular(AssignSimRequest request);
    void insertServiceStatus(CreationContext context);
    void insertCellularCallRestriction(@Param("id") String id, @Param("context") CreationContext context);
    void insertRatePlan(CreationContext context);
    List<String> findBasicPackIds(@Param("cellularNumber") String cellularNumber);
    void insertCellularPackage(@Param("cellularNumber") String cellularNumber, @Param("packId") String packId);
    void insertCreditLimit(CreationContext context);
    void deleteSubscriber(@Param("cellularNumber") String cellularNumber, @Param("user") String user);
}
