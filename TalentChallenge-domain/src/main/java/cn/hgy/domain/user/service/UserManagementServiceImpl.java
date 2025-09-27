package cn.hgy.domain.user.service;

import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;
import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import cn.hgy.domain.user.model.valobj.UserTypeEnum;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-22
 * @Description: 用户管理服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementServiceImpl implements IUserManagementService{

    private final IUserManagementRepository repository;
    private final IJWTService jwtService;

    @Override
    public Boolean userRegister(String nickname, String type, String identityType, String identifier, String credential) {
        // TODO: 高并发场景下，检查和插入之间可能存在竞态条件，需要考虑使用分布式锁或数据库唯一约束来解决

        // 验证是否已注册
        boolean userExists = repository.existsByIdentityTypeAndIdentifier(identityType, identifier);
        if (userExists) {
            throw new AppException(ResponseCode.USER_IS_EXIST.getCode(), ResponseCode.USER_IS_EXIST.getInfo());
        }

        UserEntity userEntity = UserEntity.builder()
                .nickname(nickname)
                .type(UserTypeEnum.getCodeByType(type))
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .build();

        // todo 预验证机制，这里先默认已经验证
        UserAuthCredentialEntity userAuthCredentialEntity = UserAuthCredentialEntity.builder()
                .credential(credential)
                .identifier(identifier)
                .identityType(identityType)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .build();

        try {
            repository.insertNewUserAndUserAuthCredential(userEntity, userAuthCredentialEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResponseCode.USER_REGISTER_FAIL.getCode(), ResponseCode.USER_REGISTER_FAIL.getInfo());
        }

        return true;
    }

    @Override
    public UserLoginEntity userLogin(String identityType, String identifier, String credential) {
        // 1. 查找用户认证信息
        UserAuthCredentialEntity userAuthCredentialEntity;
        try {
            userAuthCredentialEntity = repository.queryUserAuthByIdentifier(identityType, identifier);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ResponseCode.USER_LOGIN_FAIL.getCode(), ResponseCode.USER_LOGIN_FAIL.getInfo());
        }
        if (null == userAuthCredentialEntity) {
            log.error("登录的用户不存在 identityType={}, identifier={}, credential={}", identityType, identifier, credential);
            throw new AppException(ResponseCode.USER_IS_NOT_EXIST.getCode(), ResponseCode.USER_IS_NOT_EXIST.getInfo());
        }

        // 2. 查找用户
        UserEntity userEntity = repository.queryUserBaseByUserId(userAuthCredentialEntity.getUserId());

        // 3. 校验密码
        if (!userAuthCredentialEntity.verifyPassword(credential)) {
            log.error("用户登录凭证不正确 userId:{}", userAuthCredentialEntity.getUserId());
            throw new AppException(ResponseCode.USER_CREDENTIAL_NOT_TRUE.getCode(), ResponseCode.USER_CREDENTIAL_NOT_TRUE.getInfo());
        }

        // 4. 校验用户状态
        if (!userEntity.checkUserStatus()) {
            log.error("用户当前账户不可用 userId:{}", userEntity.getUserId());
            throw new AppException(ResponseCode.USER_IS_NOT_ACTIVE.getCode(), ResponseCode.USER_IS_NOT_ACTIVE.getInfo());
        }

        // 5. 组合实体
        String authentication = jwtService.generateToken(userEntity.getUserId(), null);
        UserLoginEntity userLoginEntity = UserLoginEntity.builder()
                .userId(userEntity.getUserId())
                .type(UserTypeEnum.getTypeByCode(userEntity.getType()))
                .nickname(userEntity.getNickname())
                .avatarUrl(userEntity.getAvatarUrl())
                .isActive(userEntity.getIsActive())
                .userId(userAuthCredentialEntity.getUserId())
                .userAuthCredentialId(userAuthCredentialEntity.getUserAuthCredentialId())
                .identityType(userAuthCredentialEntity.getIdentityType())
                .identifier(userAuthCredentialEntity.getIdentifier())
                .credential(userAuthCredentialEntity.getCredential())
                .isVerified(userAuthCredentialEntity.getIsVerified())
                .verifyTime(userAuthCredentialEntity.getVerifyTime())
                .authentication(authentication)
                .build();

        // 6. 更新最后登录时间
        userEntity.updateLastLoginTime();
        userLoginEntity.setLastLoginTime(userEntity.getLastLoginTime());

        // 7. 更新到数据库
        try {
            repository.updateLastLoginTime(userEntity);
        } catch (Exception e) {
            log.error("更新用户最后登录时间失败：{}", e.getMessage());
        }

        return userLoginEntity;
    }
}
