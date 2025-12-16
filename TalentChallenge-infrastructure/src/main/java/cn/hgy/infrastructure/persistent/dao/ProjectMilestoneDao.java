package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.ProjectMilestone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 里程碑Mapper
 */
@Mapper
public interface ProjectMilestoneDao {
    
    int insert(ProjectMilestone milestone);

    int batchInsert(@Param("list") List<ProjectMilestone> list);

    int update(ProjectMilestone milestone);

    ProjectMilestone selectByMilestoneId(@Param("milestoneId") String milestoneId);

    List<ProjectMilestone> selectByProjectId(@Param("projectId") String projectId);

    ProjectMilestone selectByProjectIdAndCode(@Param("projectId") String projectId,
                                              @Param("milestoneCode") String milestoneCode);

    List<ProjectMilestone> selectByProjectIdAndStatus(@Param("projectId") String projectId,
                                                      @Param("status") String status);

    int updateStatus(@Param("milestoneId") String milestoneId, @Param("status") String status);

    int updateApprovalStatus(@Param("milestoneId") String milestoneId,
                             @Param("approvalStatus") String approvalStatus);

    int updateDeadline(@Param("milestoneId") String milestoneId,
                       @Param("plannedStartTime") java.time.LocalDateTime plannedStartTime,
                       @Param("plannedEndTime") java.time.LocalDateTime plannedEndTime,
                       @Param("remindBeforeHours") Integer remindBeforeHours);

    long countByProjectId(@Param("projectId") String projectId);
}

