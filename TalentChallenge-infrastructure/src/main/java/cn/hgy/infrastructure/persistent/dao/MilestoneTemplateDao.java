package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.MilestoneTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 里程碑模板DAO
 *
 * @author hgy
 */
@Mapper
public interface MilestoneTemplateDao {
    
    void insert(MilestoneTemplate template);
    
    MilestoneTemplate selectByTemplateId(@Param("templateId") String templateId);
    
    MilestoneTemplate selectByMilestoneCode(@Param("milestoneCode") String milestoneCode);
    
    List<MilestoneTemplate> selectAllEnabled();
    
    List<MilestoneTemplate> selectByType(@Param("milestoneType") String milestoneType);
    
    void batchInsert(List<MilestoneTemplate> templates);
}

