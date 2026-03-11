package cn.hgy.domain.user.service;

import cn.hgy.api.dto.UserProfileResponseDTO;
import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-22
 * @Description: 用户管理服务接口
 */
public interface IUserManagementService {

    /**
     * 用户注册业务
     * @return 注册是否成功
     */
    Boolean userRegister(String nickname, String type,  String identityType, String identifier, String credential);

    /**
     * 用户登录业务
     *
     * @param identityType 登录类型
     * @param identifier   登录凭证
     * @param credential   唯一凭证
     * @return 用户聚合体
     */
    UserLoginEntity userLogin(String identityType, String identifier, String credential);

    /**
     * 管理员创建新管理员账号
     * @param nickname 昵称
     * @param identityType 身份类型
     * @param identifier 标识符
     * @param credential 凭证
     * @return 创建是否成功
     */
    Boolean createAdminAccount(String nickname, String identityType, String identifier, String credential);

    /**
     * 更新用户资料
     * @param userId 用户ID
     * @param nickname 昵称
     * @param avatarUrl 头像URL
     * @param bio 个人简介
     * @param gender 性别
     * @param birthday 生日
     * @param regionCode 地区编码
     * @param industry 行业
     * @param title 职位
     * @return 更新后的用户实体
     */
    UserEntity updateUserProfile(String userId, String nickname, String avatarUrl, String bio,
                                  Integer gender, java.time.LocalDate birthday, String regionCode,
                                  String industry, String title);

    /**
     * 查询用户完整资料（包括扩展信息）
     * @param userId 用户ID
     * @return 用户完整资料DTO
     */
    UserProfileResponseDTO getUserProfile(String userId);

    /**
     * 企业提交资质文件，状态变为 pending_review
     * @param userId 企业用户ID
     * @param fileUrls 资质文件URL列表
     */
    void submitCompanyQualification(String userId, List<String> fileUrls);

    /**
     * 管理员通过企业认证
     * @param targetUserId 目标企业用户ID
     */
    void approveCompanyCertification(String targetUserId);

    /**
     * 管理员拒绝企业认证
     * @param targetUserId 目标企业用户ID
     * @param reason 拒绝原因
     */
    void rejectCompanyCertification(String targetUserId, String reason);

    /**
     * 管理员分页查询待审核企业列表
     * @param status 认证状态
     * @param page 页码
     * @param pageSize 每页数量
     * @return 企业资料列表
     */
    List<CompanyProfileEntity> queryCompaniesByCertificationStatus(String status, int page, int pageSize);

    /**
     * 统计指定认证状态的企业数量
     */
    long countCompaniesByCertificationStatus(String status);

}
