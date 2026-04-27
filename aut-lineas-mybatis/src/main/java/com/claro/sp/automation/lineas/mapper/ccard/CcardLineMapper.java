package com.claro.sp.automation.lineas.mapper.ccard;

import com.claro.sp.automation.lineas.domain.line.CreationContext;
import org.apache.ibatis.annotations.Param;

public interface CcardLineMapper {
    String findPrefix();
    int countHandle(@Param("handle") String handle);
    String nextHandle();
    void insertPrepayCellular(CreationContext context);
    void insertNumberChange(CreationContext context);
    void insertPpServiceStatus(CreationContext context);
    void deleteSubscriber(@Param("cellularNumber") String cellularNumber, @Param("owner") String owner);
}
