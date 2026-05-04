package cn.hgy.domain.user.service;

import cn.hgy.domain.audit.repository.IAuditReviewRepository;
import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;
import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 用户管理服务单元测试类
 * 使用MockitoExtension来支持Mockito注解
 *
 * @Author: 黄光宇
 * @CreateTime: 2025-09-24
 * @Description: 用户管理服务单元测试
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class UserManagementServiceImplTest {
    @Mock
    private IUserManagementRepository repository; // 模拟用户管理仓库

    @Mock
    private IJWTService jwtService; // 模拟JWT服务

    @Mock
    private IAuditReviewRepository auditReviewRepository;

    @InjectMocks
    private UserManagementServiceImpl userManagementService; // 注入被测试的服务实现

    @Test
    void test_RegisterUserSuccessfullyWhenUserNotExists() {
        log.info("开始测试：当用户不存在时应成功注册用户");

        // Arrange - 准备测试数据
        String nickname = "testUser";
        String type = "学生";
        String identityType = "email";
        String identifier = "test@example.com";
        String credential = "password123";
        log.info("测试数据：nickname={}, type={}, identityType={}, identifier={}", nickname, type, identityType, identifier);

        // 设置Mock行为：模拟用户不存在的情况
        when(repository.existsByIdentityTypeAndIdentifier(identityType, identifier))
                .thenReturn(false);
        log.info("已设置Mock：当查询用户是否存在时返回false");

        // Act - 执行被测试方法
        log.info("执行用户注册方法");
        Boolean result = userManagementService.userRegister(nickname, type, identityType, identifier, credential);

        // Assert - 验证结果
        log.info("验证注册结果是否为true");
        assertThat(result).isTrue();

        // 验证方法调用次数和参数
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).existsByIdentityTypeAndIdentifier(identityType, identifier);
        verify(repository, times(1)).insertNewUserAndUserAuthCredential(
                any(UserEntity.class),
                any(UserAuthCredentialEntity.class)
        );

        log.info("测试完成：用户注册成功测试通过");
    }

    @Test
    void test_ThrowUserExistsExceptionWhenUserAlreadyExists() {
        log.info("开始测试：当用户已存在时应抛出用户已存在异常");

        // Arrange - 准备测试数据
        String nickname = "existingUser";
        String type = "企业";
        String identityType = "phone";
        String identifier = "13800138000";
        String credential = "password456";
        log.info("测试数据：nickname={}, type={}, identityType={}, identifier={}", nickname, type, identityType, identifier);

        // 设置Mock行为：模拟用户已存在的情况
        when(repository.existsByIdentityTypeAndIdentifier(identityType, identifier))
                .thenReturn(true);
        log.info("已设置Mock：当查询用户是否存在时返回true");

        // Act & Assert - 验证抛出异常
        log.info("执行用户注册方法并验证是否抛出AppException");
        AppException exception = assertThrows(AppException.class, () -> {
            userManagementService.userRegister(nickname, type, identityType, identifier, credential);
        });

        // 验证异常信息
        log.info("验证异常代码和信息是否符合预期");
        assertThat(exception.getCode()).isEqualTo(ResponseCode.USER_IS_EXIST.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.USER_IS_EXIST.getInfo());

        // 验证只调用了检查方法，没有调用插入方法
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).existsByIdentityTypeAndIdentifier(identityType, identifier);
        verify(repository, never()).insertNewUserAndUserAuthCredential(any(), any());

        log.info("测试完成：用户已存在异常测试通过");
    }

    @Test
    void test_ThrowRegisterFailExceptionWhenRepositoryThrowsException() {
        log.info("开始测试：当仓库层抛出异常时应抛出注册失败异常");

        // Arrange - 准备测试数据
        String nickname = "testUser";
        String type = "管理员";
        String identityType = "username";
        String identifier = "testuser123";
        String credential = "password789";
        log.info("测试数据：nickname={}, type={}, identityType={}, identifier={}", nickname, type, identityType, identifier);

        // 设置Mock行为：用户不存在，但插入时抛出异常
        when(repository.existsByIdentityTypeAndIdentifier(identityType, identifier))
                .thenReturn(false);
        doThrow(new RuntimeException("Database connection failed"))
                .when(repository).insertNewUserAndUserAuthCredential(any(), any());
        log.info("已设置Mock：用户不存在，但插入操作会抛出运行时异常");

        // Act & Assert - 验证抛出异常
        log.info("执行用户注册方法并验证是否抛出AppException");
        AppException exception = assertThrows(AppException.class, () -> {
            userManagementService.userRegister(nickname, type, identityType, identifier, credential);
        });

        // 验证异常信息
        log.info("验证异常代码和信息是否符合预期");
        assertThat(exception.getCode()).isEqualTo(ResponseCode.USER_REGISTER_FAIL.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.USER_REGISTER_FAIL.getInfo());

        // 验证方法调用
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).existsByIdentityTypeAndIdentifier(identityType, identifier);
        verify(repository, times(1)).insertNewUserAndUserAuthCredential(any(), any());

        log.info("测试完成：注册失败异常测试通过");
    }

    @Test
    void test_RegisterUserWithDifferentIdentityTypes() {
        log.info("开始测试：使用不同身份类型注册用户");

        // Arrange - 测试不同的认证类型
        String nickname = "multiUser";
        String type = "学生";
        String credential = "password000";
        log.info("测试数据：nickname={}, type={}", nickname, type);

        // 测试邮箱类型
        log.info("测试邮箱身份类型");
        when(repository.existsByIdentityTypeAndIdentifier("email", "multi@test.com"))
                .thenReturn(false);

        // Act
        Boolean emailResult = userManagementService.userRegister(nickname, type, "email", "multi@test.com", credential);

        // Assert
        assertThat(emailResult).isTrue();
        verify(repository).existsByIdentityTypeAndIdentifier("email", "multi@test.com");
        verify(repository).insertNewUserAndUserAuthCredential(any(), any());

        // 重置Mock以便下次测试
        reset(repository);
        log.info("已重置Mock对象，准备测试手机身份类型");

        // 测试手机号类型
        log.info("测试手机身份类型");
        when(repository.existsByIdentityTypeAndIdentifier("phone", "13900139000"))
                .thenReturn(false);

        // Act
        Boolean phoneResult = userManagementService.userRegister(nickname, type, "phone", "13900139000", credential);

        // Assert
        assertThat(phoneResult).isTrue();
        verify(repository).existsByIdentityTypeAndIdentifier("phone", "13900139000");
        verify(repository).insertNewUserAndUserAuthCredential(any(), any());

        log.info("测试完成：不同身份类型注册测试通过");
    }

    @Test
    void test_VerifyUserEntityPropertiesWhenRegistering() {
        log.info("开始测试：验证注册时传递给repository的用户实体属性");

        // Arrange
        String nickname = "propertyTestUser";
        String type = "企业";
        String identityType = "email";
        String identifier = "property@test.com";
        String credential = "testPassword";
        log.info("测试数据：nickname={}, type={}, identityType={}, identifier={}", nickname, type, identityType, identifier);

        when(repository.existsByIdentityTypeAndIdentifier(identityType, identifier))
                .thenReturn(false);
        log.info("已设置Mock：当查询用户是否存在时返回false");

        // Act
        log.info("执行用户注册方法");
        userManagementService.userRegister(nickname, type, identityType, identifier, credential);

        // Assert - 验证传递给repository的参数
        log.info("验证传递给repository的用户实体和认证实体属性");
        verify(repository).insertNewUserAndUserAuthCredential(
                argThat(userEntity -> {
                    log.info("验证用户实体属性");
                    assertThat(userEntity.getNickname()).isEqualTo(nickname);
                    assertThat(userEntity.getType()).isEqualTo(2); // UserTypeEnum.getCodeByType("企业") = 2
                    assertThat(userEntity.getIsActive()).isEqualTo(1); // UserStatusEnum.ACTIVE.getStatusCode()
                    return true;
                }),
                argThat(authEntity -> {
                    log.info("验证认证实体属性");
                    assertThat(authEntity.getIdentityType()).isEqualTo(identityType);
                    assertThat(authEntity.getIdentifier()).isEqualTo(identifier);
                    assertThat(authEntity.getCredential()).isEqualTo(credential);
                    assertThat(authEntity.getIsVerified()).isEqualTo(1); // UserStatusEnum.IS_VALIFY.getStatusCode()
                    return true;
                })
        );

        log.info("测试完成：实体属性验证测试通过");
    }

    // ==================== 登录相关测试 ====================

    @Test
    void test_LoginSuccessfullyWhenCredentialsAreCorrect() {
        log.info("开始测试：当凭证正确时应成功登录");

        // Arrange - 准备测试数据
        String identityType = "email";
        String identifier = "test@example.com";
        String credential = "password123";
        String userId = "user123";
        String userAuthCredentialId = "auth123";
        String jwtToken = "jwt.token.here";
        log.info("测试数据：identityType={}, identifier={}, credential={}", identityType, identifier, credential);

        // 创建模拟的用户认证实体
        UserAuthCredentialEntity mockAuthEntity = UserAuthCredentialEntity.builder()
                .userAuthCredentialId(userAuthCredentialId)
                .userId(userId)
                .identityType(identityType)
                .identifier(identifier)
                .credential(credential)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .verifyTime(LocalDateTime.now().minusDays(1))
                .build();

        // 创建模拟的用户实体
        UserEntity mockUserEntity = UserEntity.builder()
                .userId(userId)
                .type(2) // 学生类型
                .nickname("testUser")
                .avatarUrl("http://example.com/avatar.jpg")
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .lastLoginTime(LocalDateTime.now().minusDays(1))
                .build();

        // 设置Mock行为
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(mockAuthEntity);
        when(repository.queryUserBaseByUserId(userId))
                .thenReturn(mockUserEntity);
        when(jwtService.generateToken(userId, null))
                .thenReturn(jwtToken);
        log.info("已设置Mock：用户认证信息存在，用户状态正常，JWT生成成功");

        // Act - 执行登录方法
        log.info("执行用户登录方法");
        UserLoginEntity result = userManagementService.userLogin(identityType, identifier, credential);

        // Assert - 验证结果
        log.info("验证登录结果");
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getNickname()).isEqualTo("testUser");
        assertThat(result.getIdentityType()).isEqualTo(identityType);
        assertThat(result.getIdentifier()).isEqualTo(identifier);
        assertThat(result.getAuthentication()).isEqualTo(jwtToken);
        assertThat(result.getIsActive()).isEqualTo(UserStatusEnum.ACTIVE.getStatusCode());

        // 验证方法调用
        log.info("验证repository和jwtService方法调用情况");
        verify(repository, times(1)).queryUserAuthByIdentifier(identityType, identifier);
        verify(repository, times(1)).queryUserBaseByUserId(userId);
        verify(jwtService, times(1)).generateToken(userId, null);
        verify(repository, times(1)).updateLastLoginTime(any(UserEntity.class));

        log.info("测试完成：用户登录成功测试通过");
    }

    @Test
    void test_ThrowUserNotExistExceptionWhenUserAuthNotFound() {
        log.info("开始测试：当用户认证信息不存在时应抛出用户不存在异常");

        // Arrange - 准备测试数据
        String identityType = "email";
        String identifier = "nonexistent@example.com";
        String credential = "password123";
        log.info("测试数据：identityType={}, identifier={}, credential={}", identityType, identifier, credential);

        // 设置Mock行为：用户认证信息不存在
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(null);
        log.info("已设置Mock：当查询用户认证信息时返回null");

        // Act & Assert - 验证抛出异常
        log.info("执行用户登录方法并验证是否抛出AppException");
        AppException exception = assertThrows(AppException.class, () -> {
            userManagementService.userLogin(identityType, identifier, credential);
        });

        // 验证异常信息
        log.info("验证异常代码和信息是否符合预期");
        assertThat(exception.getCode()).isEqualTo(ResponseCode.USER_IS_NOT_EXIST.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.USER_IS_NOT_EXIST.getInfo());

        // 验证只调用了查询认证信息的方法
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).queryUserAuthByIdentifier(identityType, identifier);
        verify(repository, never()).queryUserBaseByUserId(any());
        verify(jwtService, never()).generateToken(any(), any());

        log.info("测试完成：用户不存在异常测试通过");
    }

    @Test
    void test_ThrowCredentialNotTrueExceptionWhenPasswordIsWrong() {
        log.info("开始测试：当密码错误时应抛出登录凭证不正确异常");

        // Arrange - 准备测试数据
        String identityType = "email";
        String identifier = "test@example.com";
        String wrongCredential = "wrongPassword";
        String correctCredential = "correctPassword";
        String userId = "user123";
        log.info("测试数据：identityType={}, identifier={}, wrongCredential={}", identityType, identifier, wrongCredential);

        // 创建模拟的用户认证实体（注意：由于verifyPassword逻辑是反的，这里需要适配）
        UserAuthCredentialEntity mockAuthEntity = UserAuthCredentialEntity.builder()
                .userAuthCredentialId("auth123")
                .userId(userId)
                .identityType(identityType)
                .identifier(identifier)
                .credential(correctCredential) // 存储的是正确密码
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .build();

        // 创建模拟的用户实体（需要添加这个Mock，否则会出现NullPointerException）
        UserEntity mockUserEntity = UserEntity.builder()
                .userId(userId)
                .type(2)
                .nickname("testUser")
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .build();

        // 设置Mock行为
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(mockAuthEntity);
        when(repository.queryUserBaseByUserId(userId))
                .thenReturn(mockUserEntity);
        log.info("已设置Mock：用户认证信息存在，用户实体存在，但密码不匹配");

        // Act & Assert - 验证抛出异常
        log.info("执行用户登录方法并验证是否抛出AppException");
        AppException exception = assertThrows(AppException.class, () -> {
            userManagementService.userLogin(identityType, identifier, wrongCredential);
        });

        // 验证异常信息
        log.info("验证异常代码和信息是否符合预期");
        assertThat(exception.getCode()).isEqualTo(ResponseCode.USER_CREDENTIAL_NOT_TRUE.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.USER_CREDENTIAL_NOT_TRUE.getInfo());

        // 验证方法调用
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).queryUserAuthByIdentifier(identityType, identifier);
        verify(repository, times(1)).queryUserBaseByUserId(userId); // 现在会调用这个方法
        verify(jwtService, never()).generateToken(any(), any());

        log.info("测试完成：登录凭证不正确异常测试通过");
    }

    @Test
    void test_ThrowUserNotActiveExceptionWhenUserIsDisabled() {
        log.info("开始测试：当用户账户被禁用时应抛出用户账户不可用异常");

        // Arrange - 准备测试数据
        String identityType = "phone";
        String identifier = "13800138000";
        String credential = "password123";
        String userId = "user123";
        log.info("测试数据：identityType={}, identifier={}, credential={}", identityType, identifier, credential);

        // 创建模拟的用户认证实体
        UserAuthCredentialEntity mockAuthEntity = UserAuthCredentialEntity.builder()
                .userAuthCredentialId("auth123")
                .userId(userId)
                .identityType(identityType)
                .identifier(identifier)
                .credential(credential)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .build();

        // 创建模拟的用户实体（账户被禁用）
        UserEntity mockUserEntity = UserEntity.builder()
                .userId(userId)
                .type(2)
                .nickname("disabledUser")
                .isActive(UserStatusEnum.REJECTED.getStatusCode()) // 账户被禁用
                .build();

        // 设置Mock行为
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(mockAuthEntity);
        when(repository.queryUserBaseByUserId(userId))
                .thenReturn(mockUserEntity);
        log.info("已设置Mock：用户认证信息存在，但用户账户被禁用");

        // Act & Assert - 验证抛出异常
        log.info("执行用户登录方法并验证是否抛出AppException");
        AppException exception = assertThrows(AppException.class, () -> {
            userManagementService.userLogin(identityType, identifier, credential);
        });

        // 验证异常信息
        log.info("验证异常代码和信息是否符合预期");
        assertThat(exception.getCode()).isEqualTo(ResponseCode.USER_IS_NOT_ACTIVE.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.USER_IS_NOT_ACTIVE.getInfo());

        // 验证方法调用
        log.info("验证repository方法调用情况");
        verify(repository, times(1)).queryUserAuthByIdentifier(identityType, identifier);
        verify(repository, times(1)).queryUserBaseByUserId(userId);
        verify(jwtService, never()).generateToken(any(), any());
        verify(repository, never()).updateLastLoginTime(any());

        log.info("测试完成：用户账户不可用异常测试通过");
    }

    @Test
    void test_LoginSuccessfullyWithDifferentIdentityTypes() {
        log.info("开始测试：使用不同身份类型登录");

        // 测试邮箱登录
        log.info("测试邮箱身份类型登录");
        test_LoginWithIdentityType("email", "user@test.com", "emailPassword", "emailUser123");

        // 重置Mock以便下次测试
        reset(repository, jwtService);
        log.info("已重置Mock对象，准备测试手机身份类型登录");

        // 测试手机号登录
        log.info("测试手机身份类型登录");
        test_LoginWithIdentityType("phone", "13900139000", "phonePassword", "phoneUser123");

        log.info("测试完成：不同身份类型登录测试通过");
    }

    @Test
    void shouldRecordAuditHistoryWhenAdminRejectsCompanyCertification() {
        CompanyProfileEntity profile = CompanyProfileEntity.builder()
                .userId("company-1")
                .certificationStatus("pending_review")
                .build();
        when(repository.queryCompanyProfileByUserId("company-1")).thenReturn(profile);

        userManagementService.rejectCompanyCertification("company-1", "资料不完整", "admin-1");

        verify(repository).updateCompanyCertificationStatus("company-1", "rejected", "资料不完整");
        verify(auditReviewRepository).save(argThat(record -> {
            assertThat(record.getAuditType()).isEqualTo("company");
            assertThat(record.getTargetId()).isEqualTo("company-1");
            assertThat(record.getAction()).isEqualTo("reject");
            assertThat(record.getFromStatus()).isEqualTo("pending_review");
            assertThat(record.getToStatus()).isEqualTo("rejected");
            assertThat(record.getReason()).isEqualTo("资料不完整");
            assertThat(record.getReviewerId()).isEqualTo("admin-1");
            return true;
        }));
    }

    private void test_LoginWithIdentityType(String identityType, String identifier, String credential, String userId) {
        String userAuthCredentialId = "auth_" + userId;
        String jwtToken = "jwt_token_" + userId;

        // 创建模拟数据
        UserAuthCredentialEntity mockAuthEntity = UserAuthCredentialEntity.builder()
                .userAuthCredentialId(userAuthCredentialId)
                .userId(userId)
                .identityType(identityType)
                .identifier(identifier)
                .credential(credential)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .build();

        UserEntity mockUserEntity = UserEntity.builder()
                .userId(userId)
                .type(2)
                .nickname("testUser_" + identityType)
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .build();

        // 设置Mock行为
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(mockAuthEntity);
        when(repository.queryUserBaseByUserId(userId))
                .thenReturn(mockUserEntity);
        when(jwtService.generateToken(userId, null))
                .thenReturn(jwtToken);

        // 执行登录
        UserLoginEntity result = userManagementService.userLogin(identityType, identifier, credential);

        // 验证结果
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getIdentityType()).isEqualTo(identityType);
        assertThat(result.getIdentifier()).isEqualTo(identifier);
        assertThat(result.getAuthentication()).isEqualTo(jwtToken);

        // 验证方法调用
        verify(repository).queryUserAuthByIdentifier(identityType, identifier);
        verify(repository).queryUserBaseByUserId(userId);
        verify(jwtService).generateToken(userId, null);
        verify(repository).updateLastLoginTime(any(UserEntity.class));
    }

    @Test
    void testVerifyLoginEntityPropertiesWhenLoginSuccessfully() {
        log.info("开始测试：验证登录成功时返回的用户登录实体属性");

        // Arrange - 准备详细的测试数据
        String identityType = "username";
        String identifier = "testuser";
        String credential = "testpassword";
        String userId = "detailed_user_123";
        String userAuthCredentialId = "detailed_auth_123";
        String jwtToken = "detailed.jwt.token";
        LocalDateTime verifyTime = LocalDateTime.now().minusDays(30);
        LocalDateTime lastUseTime = LocalDateTime.now().minusHours(2);
        log.info("测试数据：userId={}, identityType={}, identifier={}", userId, identityType, identifier);

        // 创建详细的模拟数据
        UserAuthCredentialEntity mockAuthEntity = UserAuthCredentialEntity.builder()
                .userAuthCredentialId(userAuthCredentialId)
                .userId(userId)
                .identityType(identityType)
                .identifier(identifier)
                .credential(credential)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .verifyTime(verifyTime)
                .build();

        UserEntity mockUserEntity = UserEntity.builder()
                .userId(userId)
                .type(1) // 企业类型
                .nickname("DetailedTestUser")
                .avatarUrl("http://example.com/detailed-avatar.jpg")
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .lastLoginTime(LocalDateTime.now().minusDays(7))
                .build();

        // 设置Mock行为
        when(repository.queryUserAuthByIdentifier(identityType, identifier))
                .thenReturn(mockAuthEntity);
        when(repository.queryUserBaseByUserId(userId))
                .thenReturn(mockUserEntity);
        when(jwtService.generateToken(userId, null))
                .thenReturn(jwtToken);
        log.info("已设置Mock：详细的用户信息和认证信息");

        // Act - 执行登录
        log.info("执行用户登录方法");
        UserLoginEntity result = userManagementService.userLogin(identityType, identifier, credential);

        // Assert - 详细验证返回实体的所有属性
        log.info("验证登录实体的详细属性");
        assertThat(result).isNotNull();

        // 验证用户基本信息
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getType()).isEqualTo(1);
        assertThat(result.getNickname()).isEqualTo("DetailedTestUser");
        assertThat(result.getAvatarUrl()).isEqualTo("http://example.com/detailed-avatar.jpg");
        assertThat(result.getIsActive()).isEqualTo(UserStatusEnum.ACTIVE.getStatusCode());

        // 验证认证信息
        assertThat(result.getUserAuthCredentialId()).isEqualTo(userAuthCredentialId);
        assertThat(result.getIdentityType()).isEqualTo(identityType);
        assertThat(result.getIdentifier()).isEqualTo(identifier);
        assertThat(result.getCredential()).isEqualTo(credential);
        assertThat(result.getIsVerified()).isEqualTo(UserStatusEnum.IS_VALIFY.getStatusCode());
        assertThat(result.getVerifyTime()).isEqualTo(verifyTime);
        assertThat(result.getLastLoginTime()).isEqualTo(lastUseTime);

        // 验证JWT令牌
        assertThat(result.getAuthentication()).isEqualTo(jwtToken);

        log.info("测试完成：登录实体属性验证测试通过");
    }
}
