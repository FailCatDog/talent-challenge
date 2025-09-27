package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.infrastructure.persistent.dao.UserAuthCredentialDao;
import cn.hgy.infrastructure.persistent.dao.UserDao;
import cn.hgy.infrastructure.persistent.po.User;
import cn.hgy.infrastructure.persistent.po.UserAuthCredential;
import cn.hutool.core.lang.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-24
 * @Description: 用户管理仓储实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementRepository implements IUserManagementRepository {

    private final UserDao userDao;
    private final UserAuthCredentialDao userAuthCredentialDao;
    private final Snowflake snowflake;

    @Override
    @Transactional
    public void insertNewUserAndUserAuthCredential(UserEntity userEntity, UserAuthCredentialEntity userAuthCredentialEntity) {
        // 构建po对象
        String userId = snowflake.nextIdStr();
        User user = new User();
        user.setNickname(userEntity.getNickname());
        user.setIsActive(userEntity.getIsActive());
        user.setUserId(userId);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setType(userEntity.getType());

        String userAuthCredentialId = snowflake.nextIdStr();
        UserAuthCredential userAuthCredential = new UserAuthCredential();
        userAuthCredential.setUserAuthCredentialId(userAuthCredentialId);
        userAuthCredential.setIdentityType(userAuthCredentialEntity.getIdentityType());
        userAuthCredential.setUserId(user.getUserId());
        userAuthCredential.setIdentifier(userAuthCredentialEntity.getIdentifier());
        userAuthCredential.setCredential(userAuthCredentialEntity.getCredential());
        userAuthCredential.setIsVerified(userAuthCredentialEntity.getIsVerified());
        userAuthCredential.setCreateTime(LocalDateTime.now());
        userAuthCredential.setUpdateTime(LocalDateTime.now());

        // 插入数据库
        userDao.insert(user);
        userAuthCredentialDao.insert(userAuthCredential);
    }

    @Override
    public boolean existsByIdentityTypeAndIdentifier(String identityType, String identifier) {
        int count = userAuthCredentialDao.existsByIdentityTypeAndIdentifier(identityType, identifier);
        return count > 0;
    }

    @Override
    public void updateLastLoginTime(UserEntity userEntity) {
        userDao.updateUserLastLoginTime(userEntity.getUserId() ,userEntity.getLastLoginTime());
    }

    @Override
    public UserAuthCredentialEntity queryUserAuthByIdentifier(String identityType, String identifier) {
        UserAuthCredential userAuthCredential = userAuthCredentialDao.queryUserByIdentityTypeAndIdentifier(identityType, identifier);

        // 转换对象
        return UserAuthCredentialEntity.builder()
                .userAuthCredentialId(userAuthCredential.getUserAuthCredentialId())
                .userId(userAuthCredential.getUserId())
                .identityType(userAuthCredential.getIdentityType())
                .identifier(userAuthCredential.getIdentifier())
                .credential(userAuthCredential.getCredential())
                .isVerified(userAuthCredential.getIsVerified())
                .verifyTime(userAuthCredential.getVerifyTime())
                .build();
    }

    @Override
    public UserEntity queryUserBaseByUserId(String userId) {
        User user = userDao.queryByUserId(userId);

        // 转换对象
        return UserEntity.builder()
                .userId(user.getUserId())
                .type(user.getType())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .regionCode(user.getRegionCode())
                .industry(user.getIndustry())
                .title(user.getTitle())
                .isActive(user.getIsActive())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }
}
