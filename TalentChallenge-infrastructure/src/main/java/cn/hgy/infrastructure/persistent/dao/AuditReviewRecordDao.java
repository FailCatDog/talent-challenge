package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.AuditReviewRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditReviewRecordDao {

    int insert(AuditReviewRecord record);

    List<AuditReviewRecord> selectHistory(@Param("auditType") String auditType,
                                           @Param("targetId") String targetId,
                                           @Param("action") String action,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countHistory(@Param("auditType") String auditType,
                      @Param("targetId") String targetId,
                      @Param("action") String action);
}
