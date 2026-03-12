package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.CompanyProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 更新企业认证状态
     */
    int updateCertificationStatus(@Param("userId") String userId,
                                   @Param("status") String status,
                                   @Param("rejectReason") String rejectReason,
                                   @Param("certifiedAt") LocalDateTime certifiedAt);

    /**
     * 更新企业资质文件
     */
    int updateQualificationFiles(@Param("userId") String userId,
                                  @Param("qualificationFiles") String qualificationFiles);

    /**
     * 更新企业认证核心字段（社会信用代码、行业、规模、联系人等）
     */
    int updateCoreFields(@Param("userId") String userId,
                          @Param("creditCode") String creditCode,
                          @Param("companyIndustry") String companyIndustry,
                          @Param("companyScale") String companyScale,
                          @Param("contactPerson") String contactPerson,
                          @Param("contactPosition") String contactPosition,
                          @Param("contactPhone") String contactPhone,
                          @Param("contactEmail") String contactEmail);

    /**
     * 更新企业可自由修改字段（简介、标签、官网、地区）
     */
    int updateFreeFields(@Param("userId") String userId,
                          @Param("companyIntro") String companyIntro,
                          @Param("companyTags") String companyTags,
                          @Param("companyWebsite") String companyWebsite,
                          @Param("companyRegion") String companyRegion);

    /**
     * 按认证状态分页查询企业列表
     */
    List<CompanyProfile> selectByCertificationStatus(@Param("status") String status,
                                                      @Param("offset") int offset,
                                                      @Param("limit") int limit);

    /**
     * 按认证状态统计企业数量
     */
    long countByCertificationStatus(@Param("status") String status);

    /**
     * 按认证状态分组统计
     */
    List<Map<String, Object>> countGroupByCertificationStatus();
}
