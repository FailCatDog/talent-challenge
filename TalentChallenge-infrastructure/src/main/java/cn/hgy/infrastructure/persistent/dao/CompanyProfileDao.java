package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.CompanyProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业资料Dao接口
 *
 * @author hgy
 */
@Mapper
public interface CompanyProfileDao {

    /**
     * 根据用户ID查询企业资料
     */
    CompanyProfile queryByUserId(@Param("userId") String userId);

    /**
     * 插入企业资料
     */
    int insert(CompanyProfile companyProfile);

    /**
     * 根据用户ID更新企业资料
     */
    int updateByUserId(CompanyProfile companyProfile);
}
