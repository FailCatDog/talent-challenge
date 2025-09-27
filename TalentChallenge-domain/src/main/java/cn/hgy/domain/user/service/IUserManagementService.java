package cn.hgy.domain.user.service;

import cn.hgy.domain.user.model.entity.UserLoginEntity;

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

}
