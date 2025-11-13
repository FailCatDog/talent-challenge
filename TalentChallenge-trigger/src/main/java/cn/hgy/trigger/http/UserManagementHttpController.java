package cn.hgy.trigger.http;

import cn.hgy.api.IUserManagementHttpService;
import cn.hgy.api.dto.AdminCreateRequestDTO;
import cn.hgy.api.dto.UserLoginRequestDTO;
import cn.hgy.api.dto.UserLoginResponseDTO;
import cn.hgy.api.dto.UserRegisterRequestDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.user.model.entity.UserLoginEntity;
import cn.hgy.domain.user.service.IUserManagementService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-27
 * @Description: 用户管理http接口实现类
 */
@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping(value = "/api/v1/user_management")
public class UserManagementHttpController implements IUserManagementHttpService {

    private final IUserManagementService userManagementService;

    @PostMapping(value = "/user_register")
    @Override
    public Response<Boolean> userRegister(@Valid @RequestBody UserRegisterRequestDTO dto) {
        log.info("用户注册");

        boolean success = userManagementService.userRegister(dto.getNickname(), dto.getType(), dto.getIdentityType(), dto.getIdentifier(), dto.getCredential());

        return new Response<Boolean>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), success);
    }

    @PostMapping(value = "user_login")
    @Override
    public Response<UserLoginResponseDTO> userLogin(@Valid @RequestBody UserLoginRequestDTO dto) {
        log.info("用户登录");

        UserLoginEntity userLoginEntity = userManagementService.userLogin(dto.getIdentityType(), dto.getIdentifier(), dto.getCredential());

        UserLoginResponseDTO loginResponseDTO = UserLoginResponseDTO.builder()
        .userId(userLoginEntity.getUserId())
        .type(userLoginEntity.getType())
        .nickname(userLoginEntity.getNickname())
        .avatarUrl(userLoginEntity.getAvatarUrl())
        .userAuthCredentialId(userLoginEntity.getUserAuthCredentialId())
        .identityType(userLoginEntity.getIdentityType())
        .identifier(userLoginEntity.getIdentifier())
        .credential(userLoginEntity.getCredential())
        .isActive(userLoginEntity.getIsActive())
        .isVerified(userLoginEntity.getIsVerified())
        .verifyTime(userLoginEntity.getVerifyTime())
        .lastLoginTime(userLoginEntity.getLastLoginTime())
        .authentication(userLoginEntity.getAuthentication())
        .build();

        log.info("用户登录完成 userId={} userType={}", userLoginEntity.getUserId(), userLoginEntity.getType());

        return new Response<UserLoginResponseDTO>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), loginResponseDTO);
    }

    @PostMapping(value = "/admin/create_admin")
    @Override
    public Response<Boolean> createAdminAccount(@Valid @RequestBody AdminCreateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("管理员创建新管理员账号 操作者userId={}", userId);

        boolean success = userManagementService.createAdminAccount(dto.getNickname(), dto.getIdentityType(), dto.getIdentifier(), dto.getCredential());

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), success);
    }
}
