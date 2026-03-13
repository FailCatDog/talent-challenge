package cn.hgy.trigger.http;

import cn.hgy.api.IUserManagementHttpService;
import cn.hgy.api.dto.AdminCreateRequestDTO;
import cn.hgy.api.dto.CompanyPendingReviewDTO;
import cn.hgy.api.dto.CompanyProfileUpdateRequestDTO;
import cn.hgy.api.dto.CompanyQualificationDetailDTO;
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
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.domain.user.service.IUserManagementService;
import cn.hgy.infrastructure.storage.StorageService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
    private final IUserManagementRepository userManagementRepository;
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
     * 企业上传资质文件（支持多文件，自动追加到JSON数组）
     */
    @PostMapping(value = "/company/upload_qualification")
    public Response<String> uploadQualificationFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            String userId = UserContextDynamic.getUserContextDynamic();
            if (StrUtil.isBlank(userId)) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            String fileUrl = storageService.storeQualificationFile(userId, file);

            CompanyProfileEntity companyProfile = userManagementRepository.queryCompanyProfileByUserId(userId);
            if (companyProfile == null) {
                return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "企业资料不存在", null);
            }

            JSONArray fileList;
            String existingFiles = companyProfile.getQualificationFiles();
            if (StrUtil.isBlank(existingFiles)) {
                fileList = new JSONArray();
            } else {
                fileList = JSON.parseArray(existingFiles);
            }

            JSONObject fileInfo = new JSONObject();
            fileInfo.put("fileName", file.getOriginalFilename());
            fileInfo.put("fileUrl", fileUrl);
            fileInfo.put("fileSize", file.getSize());
            fileInfo.put("uploadTime", java.time.LocalDateTime.now().toString());
            fileList.add(fileInfo);

            companyProfile.setQualificationFiles(fileList.toJSONString());
            // 更新资质文件字段（JSON字符串）
            userManagementRepository.updateCompanyQualificationFilesJson(userId, fileList.toJSONString());

            log.info("企业上传资质文件成功 userId=, fileUrl={}, 当前文件数={}", userId, fileUrl, fileList.size());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), fileUrl);
        } catch (Exception e) {
            log.error("企业上传资质文件失败", e);
            return new Response<>(ResponseCode.UN_ERROR.getCode(), "上传失败：" + e.getMessage(), null);
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
     * 管理员查询企业列表（可选状态筛选，默认查全部）
     */
    @GetMapping(value = "/admin/company/pending_review")
    public Response<Map<String, Object>> getPendingReviewCompanies(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        log.info("管理员查询企业列表 status={}, page={}, pageSize={}", status, page, pageSize);
        List<Map<String, Object>> list = userManagementRepository.queryCompanyList(status, page, pageSize);
        long total = userManagementRepository.countCompanyList(status);
        List<CompanyPendingReviewDTO> dtoList = list.stream().map(item -> CompanyPendingReviewDTO.builder()
                .userId((String) item.get("userId"))
                .companyName((String) item.get("companyName"))
                .creditCode((String) item.get("creditCode"))
                .companyIndustry((String) item.get("companyIndustry"))
                .certificationStatus((String) item.get("certificationStatus"))
                .updateTime((java.time.LocalDateTime) item.get("updateTime"))
                .build()
        ).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("list", dtoList);
        result.put("total", total);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    /**
     * 管理员查看企业资质认证详情
     */
    @GetMapping(value = "/admin/company/{userId}/qualification_detail")
    public Response<CompanyQualificationDetailDTO> getCompanyQualificationDetail(
            @PathVariable("userId") String targetUserId, HttpServletRequest request) {
        log.info("管理员查看企业资质详情 targetUserId={}", targetUserId);
        CompanyProfileEntity profile = userManagementRepository.queryCompanyProfileByUserId(targetUserId);
        if (profile == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "企业资料不存在");
        }
        // 查询企业名称
        UserEntity user = userManagementRepository.queryUserBaseByUserId(targetUserId);
        String companyName = user != null ? user.getNickname() : null;
        CompanyQualificationDetailDTO dto = CompanyQualificationDetailDTO.builder()
                .userId(profile.getUserId())
                .companyName(companyName)
                .companyIntro(profile.getCompanyIntro())
                .companyTags(profile.getCompanyTags())
                .creditCode(profile.getCreditCode())
                .companyIndustry(profile.getCompanyIndustry())
                .companyScale(profile.getCompanyScale())
                .companyRegion(profile.getCompanyRegion())
                .companyWebsite(profile.getCompanyWebsite())
                .contactPerson(profile.getContactPerson())
                .contactPosition(profile.getContactPosition())
                .contactPhone(profile.getContactPhone())
                .contactEmail(profile.getContactEmail())
                .qualificationFiles(profile.getQualificationFiles())
                .certificationStatus(profile.getCertificationStatus())
                .rejectReason(profile.getRejectReason())
                .certifiedAt(profile.getCertifiedAt())
                .updateTime(profile.getUpdateTime())
                .build();
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
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
