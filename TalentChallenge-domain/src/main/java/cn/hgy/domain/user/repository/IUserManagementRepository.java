package cn.hgy.domain.user.repository;

import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.StudentProfileEntity;
import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-24
 * @Description: 用户管理仓储接口
 */
public interface IUserManagementRepository {

    /**
     * 插入新用户数据
     * @param userEntity 用户实体
     * @param userAuthCredentialEntity 用户认证实体
     */
    void insertNewUserAndUserAuthCredential(UserEntity userEntity, UserAuthCredentialEntity userAuthCredentialEntity);

    /**
     * 通过认证类型检查用户该账号是否已存在
     *
     * @param identityType 用户账户类型
     * @param identifier 唯一标识符
     * @return 是否存在
     */
    boolean existsByIdentityTypeAndIdentifier(String identityType, String identifier);


    /**
     * 更新用户最后登录时间
     * @param userEntity 用户实体
     */
    void updateLastLoginTime(UserEntity userEntity);

    /**
     * 通过唯一标识符查询用户认证信息
     *
     * @param identityType 用户认证类型
     * @param identifier   唯一标识符
     * @return 用户登陆实体
     */
    UserAuthCredentialEntity queryUserAuthByIdentifier(String identityType, String identifier);

    /**
     * 根据userId查找用户基本信息
     * @param userId 逻辑id
     * @return 用户实体
     */
    UserEntity queryUserBaseByUserId(String userId);

    /**
     * 更新用户资料信息
     * @param userEntity 用户实体
     */
    void updateUserProfile(UserEntity userEntity);

    /**
     * 根据userId查询学生资料
     * @param userId 用户ID
     * @return 学生资料实体
     */
    StudentProfileEntity queryStudentProfileByUserId(String userId);

    /**
     * 根据userId查询企业资料
     * @param userId 用户ID
     * @return 企业资料实体
     */
    CompanyProfileEntity queryCompanyProfileByUserId(String userId);
}
