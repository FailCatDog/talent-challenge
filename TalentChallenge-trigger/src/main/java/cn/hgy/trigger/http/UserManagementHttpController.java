package cn.hgy.trigger.http;

import cn.hgy.api.IUserManagementHttpService;
import cn.hgy.api.dto.AdminCreateRequestDTO;
import cn.hgy.api.dto.CompanyPendingReviewDTO;
import cn.hgy.api.dto.CompanyProfileUpdateRequestDTO;
import cn.hgy.api.dto.CompanyQualificationSubmitRequestDTO;
import cn.hgy.api.dto.UserLoginRequestDTO;
import cn.hgy.api.dto.UserLoginResponseDTO;
import cn.hgy.api.dto.UserProfileResponseDTO;
import cn.hgy.api.dto.UserProfileUpdateRequestDTO;
import cn.hgy.api.dto.UserRegisterRequestDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;
import cn.hgy.domain.user.service.IUserManagementService;
import cn.hgy.infrastructure.storage.StorageService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final StorageService storageService;

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

    @PostMapping(value = "/update_profile")
    @Override
    public Response<UserProfileResponseDTO> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        log.info("更新用户资料 userId={}", userId);

        UserEntity userEntity = userManagementService.updateUserProfile(
                userId,
                dto.getNickname(),
                dto.getAvatarUrl(),
                dto.getBio(),
                dto.getGender(),
                dto.getBirthday(),
                dto.getRegionCode(),
                dto.getIndustry(),
                dto.getTitle()
        );

        UserProfileResponseDTO responseDTO = UserProfileResponseDTO.builder()
                .userId(userEntity.getUserId())
                .nickname(userEntity.getNickname())
                .avatarUrl(userEntity.getAvatarUrl())
                .bio(userEntity.getBio())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .regionCode(userEntity.getRegionCode())
                .industry(userEntity.getIndustry())
                .title(userEntity.getTitle())
                .lastLoginTime(userEntity.getLastLoginTime())
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    /**
     * 查看用户完整资料
     */
    @GetMapping(value = "/profile/{userId}")
    public Response<UserProfileResponseDTO> getUserProfile(@PathVariable("userId") String userId) {
        log.info("查看用户资料 userId={}", userId);
        UserProfileResponseDTO profileDTO = userManagementService.getUserProfile(userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), profileDTO);
    }

    /**
     * 企业上传资质文件（单文件）
     */
    @PostMapping(value = "/company/upload_qualification")
    public Response<String> uploadQualificationFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        try {
            String url = storageService.storeQualificationFile(userId, file);
            log.info("企业上传资质文件 userId={}, url={}", userId, url);
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), url);
        } catch (IOException e) {
            log.error("企业上传资质文件失败 userId={}", userId, e);
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "文件上传失败");
        }
    }

    /**
     * 企业更新可自由修改字段（简介、标签、官网、地区）
     */
    @PostMapping(value = "/company/update_profile")
    public Response<Boolean> updateCompanyProfile(@RequestBody CompanyProfileUpdateRequestDTO req,
                                                   HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        userManagementService.updateCompanyFreeFields(userId, req.getCompanyIntro(),
                req.getCompanyTags(), req.getCompanyWebsite(), req.getCompanyRegion());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
    }

    /**
     * 企业提交资质审核（含核心字段 + 文件URL）
     * 已认证状态下拒绝修改核心字段
     */
    @PostMapping(value = "/company/submit_qualification")
    public Response<Boolean> submitQualification(@RequestBody CompanyQualificationSubmitRequestDTO req,
                                                  HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        userManagementService.submitCompanyQualification(userId, req.getFileUrls(),
                req.getCreditCode(), req.getCompanyIndustry(), req.getCompanyScale(),
                req.getContactPerson(), req.getContactPosition(),
                req.getContactPhone(), req.getContactEmail());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
    }

    /**
     * 管理员查询待审核企业列表
     */
    @GetMapping(value = "/admin/company/pending_review")
    public Response<Map<String, Object>> getPendingReviewCompanies(
            @RequestParam(value = "status", defaultValue = "pending_review") String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        log.info("管理员查询企业列表 status={}, page={}, pageSize={}", status, page, pageSize);
        List<CompanyProfileEntity> list = userManagementService.queryCompaniesByCertificationStatus(status, page, pageSize);
        long total = userManagementService.countCompaniesByCertificationStatus(status);
        List<CompanyPendingReviewDTO> dtoList = list.stream().map(e -> CompanyPendingReviewDTO.builder()
                .userId(e.getUserId())
                .creditCode(e.getCreditCode())
                .qualificationFiles(e.getQualificationFiles())
                .certificationStatus(e.getCertificationStatus())
                .rejectReason(e.getRejectReason())
                .submitTime(e.getUpdateTime())
                .build()
        ).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("list", dtoList);
        result.put("total", total);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    /**
     * 管理员通过企业认证
     */
    @PostMapping(value = "/admin/company/approve/{userId}")
    public Response<Boolean> approveCompany(@PathVariable("userId") String targetUserId, HttpServletRequest request) {
        log.info("管理员通过企业认证 targetUserId={}", targetUserId);
        userManagementService.approveCompanyCertification(targetUserId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
    }

    /**
     * 管理员拒绝企业认证
     */
    @PostMapping(value = "/admin/company/reject/{userId}")
    public Response<Boolean> rejectCompany(@PathVariable("userId") String targetUserId,
                                            @RequestBody Map<String, String> body,
                                            HttpServletRequest request) {
        String reason = body.getOrDefault("reason", "");
        log.info("管理员拒绝企业认证 targetUserId={}, reason=", targetUserId, reason);
        userManagementService.rejectCompanyCertification(targetUserId, reason);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
    }
}
