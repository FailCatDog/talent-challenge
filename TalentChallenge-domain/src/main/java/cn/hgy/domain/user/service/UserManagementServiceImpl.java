package cn.hgy.domain.user.service;

import cn.hgy.api.dto.UserProfileResponseDTO;
import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.StudentProfileEntity;
import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.entity.UserLoginEntity;
import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import cn.hgy.domain.user.model.valobj.UserTypeEnum;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.types.enums.CertificationStatusEnum;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (UserTypeEnum.ADMIN.getType().equals(type)) {
            throw new AppException(ResponseCode.USER_TYPE_NOT_ALLOWED.getCode(), ResponseCode.USER_TYPE_NOT_ALLOWED.getInfo());
        }

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
                .credential(UserAuthCredentialEntity.encryptPassword(credential))
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
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("type", userEntity.getType());
        String authentication = jwtService.generateToken(userEntity.getUserId(), additionalClaims);
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

    @Override
    public Boolean createAdminAccount(String nickname, String identityType, String identifier, String credential) {
        boolean userExists = repository.existsByIdentityTypeAndIdentifier(identityType, identifier);
        if (userExists) {
            throw new AppException(ResponseCode.USER_IS_EXIST.getCode(), ResponseCode.USER_IS_EXIST.getInfo());
        }

        UserEntity userEntity = UserEntity.builder()
                .nickname(nickname)
                .type(UserTypeEnum.ADMIN.getCode())
                .isActive(UserStatusEnum.ACTIVE.getStatusCode())
                .build();

        UserAuthCredentialEntity userAuthCredentialEntity = UserAuthCredentialEntity.builder()
                .credential(UserAuthCredentialEntity.encryptPassword(credential))
                .identifier(identifier)
                .identityType(identityType)
                .isVerified(UserStatusEnum.IS_VALIFY.getStatusCode())
                .build();

        repository.insertNewUserAndUserAuthCredential(userEntity, userAuthCredentialEntity);
        return true;
    }

    @Override
    public UserEntity updateUserProfile(String userId, String nickname, String avatarUrl, String bio,
                                         Integer gender, java.time.LocalDate birthday, String regionCode,
                                         String industry, String title) {
        UserEntity userEntity = repository.queryUserBaseByUserId(userId);
        if (userEntity == null) {
            throw new AppException(ResponseCode.USER_IS_NOT_EXIST.getCode(), ResponseCode.USER_IS_NOT_EXIST.getInfo());
        }

        if (!userEntity.checkUserStatus()) {
            throw new AppException(ResponseCode.USER_IS_NOT_ACTIVE.getCode(), ResponseCode.USER_IS_NOT_ACTIVE.getInfo());
        }

        if (nickname != null) {
            userEntity.setNickname(nickname);
        }
        if (avatarUrl != null) {
            userEntity.setAvatarUrl(avatarUrl);
        }
        if (bio != null) {
            userEntity.setBio(bio);
        }
        if (gender != null) {
            userEntity.setGender(gender);
        }
        if (birthday != null) {
            userEntity.setBirthday(birthday);
        }
        if (regionCode != null) {
            userEntity.setRegionCode(regionCode);
        }
        if (industry != null) {
            userEntity.setIndustry(industry);
        }
        if (title != null) {
            userEntity.setTitle(title);
        }

        repository.updateUserProfile(userEntity);

        return repository.queryUserBaseByUserId(userId);
    }

    @Override
    public UserProfileResponseDTO getUserProfile(String userId) {
        // 查询用户基础信息
        UserEntity userEntity = repository.queryUserBaseByUserId(userId);
        if (userEntity == null) {
            throw new AppException(ResponseCode.USER_IS_NOT_EXIST.getCode(), ResponseCode.USER_IS_NOT_EXIST.getInfo());
        }

        // 构建基础DTO
        UserProfileResponseDTO dto = UserProfileResponseDTO.builder()
                .userId(userEntity.getUserId())
                .type(userEntity.getType())
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

        // 根据用户类型查询扩展信息
        if (UserTypeEnum.STUDENT.getCode() == userEntity.getType()) {
            // 学生用户
            StudentProfileEntity studentProfile = repository.queryStudentProfileByUserId(userId);
            if (studentProfile != null) {
                dto.setRealName(studentProfile.getRealName());
                dto.setCollege(studentProfile.getCollege());
                dto.setMajor(studentProfile.getMajor());
                dto.setGrade(studentProfile.getGrade());
                dto.setStudentId(studentProfile.getStudentId());
                dto.setEmail(studentProfile.getEmail());
                dto.setPhone(studentProfile.getPhone());
                dto.setTechTags(studentProfile.getTechTags());
                dto.setTechIntro(studentProfile.getTechIntro());
            }
        } else if (UserTypeEnum.COMPANY.getCode() == userEntity.getType()) {
            // 企业用户
            CompanyProfileEntity companyProfile = repository.queryCompanyProfileByUserId(userId);
            if (companyProfile != null) {
                dto.setCompanyIntro(companyProfile.getCompanyIntro());
                dto.setCompanyTags(companyProfile.getCompanyTags());
                dto.setCreditCode(companyProfile.getCreditCode());
                dto.setCompanyIndustry(companyProfile.getCompanyIndustry());
                dto.setCompanyScale(companyProfile.getCompanyScale());
                dto.setCompanyRegion(companyProfile.getCompanyRegion());
                dto.setCompanyWebsite(companyProfile.getCompanyWebsite());
                dto.setContactPerson(companyProfile.getContactPerson());
                dto.setContactPosition(companyProfile.getContactPosition());
                dto.setContactPhone(companyProfile.getContactPhone());
                dto.setContactEmail(companyProfile.getContactEmail());
                dto.setQualificationFiles(companyProfile.getQualificationFiles());
                dto.setCertificationStatus(companyProfile.getCertificationStatus());
                dto.setRejectReason(companyProfile.getRejectReason());
                dto.setCertifiedAt(companyProfile.getCertifiedAt());
            }
        }

        return dto;
    }

    @Override
    public void submitCompanyQualification(String userId, List<String> fileUrls) {
        // 更新资质文件并将状态改为待审核
        repository.updateCompanyQualificationFiles(userId, fileUrls);
        repository.updateCompanyCertificationStatus(userId, CertificationStatusEnum.PENDING_REVIEW.getCode(), null);
        log.info("企业提交资质审核 userId={}", userId);
    }

    @Override
    public void approveCompanyCertification(String targetUserId) {
        repository.updateCompanyCertificationStatus(targetUserId, CertificationStatusEnum.CERTIFIED.getCode(), null);
        log.info("管理员通过企业认证 targetUserId={}", targetUserId);
    }

    @Override
    public void rejectCompanyCertification(String targetUserId, String reason) {
        repository.updateCompanyCertificationStatus(targetUserId, CertificationStatusEnum.REJECTED.getCode(), reason);
        log.info("管理员拒绝企业认证 targetUserId={}, reason={}", targetUserId, reason);
    }

    @Override
    public List<CompanyProfileEntity> queryCompaniesByCertificationStatus(String status, int page, int pageSize) {
        return repository.queryCompaniesByCertificationStatus(status, page, pageSize);
    }

    @Override
    public long countCompaniesByCertificationStatus(String status) {
        return repository.countCompaniesByCertificationStatus(status);
    }
}
