package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.model.entity.StudentProfileEntity;
import cn.hgy.domain.user.model.entity.UserAuthCredentialEntity;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.infrastructure.persistent.dao.CompanyProfileDao;
import cn.hgy.infrastructure.persistent.dao.StudentProfileDao;
import cn.hgy.infrastructure.persistent.dao.UserAuthCredentialDao;
import cn.hgy.infrastructure.persistent.dao.UserDao;
import cn.hgy.infrastructure.persistent.po.CompanyProfile;
import cn.hgy.infrastructure.persistent.po.StudentProfile;
import cn.hgy.infrastructure.persistent.po.User;
import cn.hgy.infrastructure.persistent.po.UserAuthCredential;
import cn.hgy.types.enums.CertificationStatusEnum;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final StudentProfileDao studentProfileDao;
    private final CompanyProfileDao companyProfileDao;
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

    @Override
    public void updateUserProfile(UserEntity userEntity) {
        User user = new User();
        user.setUserId(userEntity.getUserId());
        user.setNickname(userEntity.getNickname());
        user.setAvatarUrl(userEntity.getAvatarUrl());
        user.setBio(userEntity.getBio());
        user.setGender(userEntity.getGender());
        user.setBirthday(userEntity.getBirthday());
        user.setRegionCode(userEntity.getRegionCode());
        user.setIndustry(userEntity.getIndustry());
        user.setTitle(userEntity.getTitle());

        userDao.updateProfileByUserId(user);
    }

    @Override
    public StudentProfileEntity queryStudentProfileByUserId(String userId) {
        StudentProfile studentProfile = studentProfileDao.queryByUserId(userId);
        if (studentProfile == null) {
            return null;
        }

        return StudentProfileEntity.builder()
                .userId(studentProfile.getUserId())
                .realName(studentProfile.getRealName())
                .college(studentProfile.getCollege())
                .major(studentProfile.getMajor())
                .grade(studentProfile.getGrade())
                .studentId(studentProfile.getStudentId())
                .email(studentProfile.getEmail())
                .phone(studentProfile.getPhone())
                .techTags(StrUtil.isNotBlank(studentProfile.getTechTags()) ?
                    JSON.parseArray(studentProfile.getTechTags(), String.class) : null)
                .techIntro(studentProfile.getTechIntro())
                .build();
    }

    @Override
    public CompanyProfileEntity queryCompanyProfileByUserId(String userId) {
        CompanyProfile companyProfile = companyProfileDao.queryByUserId(userId);
        if (companyProfile == null) {
            return null;
        }

        return CompanyProfileEntity.builder()
                .userId(companyProfile.getUserId())
                .companyIntro(companyProfile.getCompanyIntro())
                .companyTags(StrUtil.isNotBlank(companyProfile.getCompanyTags()) ?
                    JSON.parseArray(companyProfile.getCompanyTags(), String.class) : null)
                .creditCode(companyProfile.getCreditCode())
                .companyIndustry(companyProfile.getCompanyIndustry())
                .companyScale(companyProfile.getCompanyScale())
                .companyRegion(companyProfile.getCompanyRegion())
                .companyWebsite(companyProfile.getCompanyWebsite())
                .contactPerson(companyProfile.getContactPerson())
                .contactPosition(companyProfile.getContactPosition())
                .contactPhone(companyProfile.getContactPhone())
                .contactEmail(companyProfile.getContactEmail())
                .qualificationFiles(StrUtil.isNotBlank(companyProfile.getQualificationFiles()) ?
                    JSON.parseArray(companyProfile.getQualificationFiles(), String.class) : null)
                .certificationStatus(companyProfile.getCertificationStatus())
                .rejectReason(companyProfile.getRejectReason())
                .certifiedAt(companyProfile.getCertifiedAt())
                .build();
    }

    @Override
    public void updateCompanyQualificationFiles(String userId, List<String> fileUrls) {
        String json = JSON.toJSONString(fileUrls);
        companyProfileDao.updateQualificationFiles(userId, json);
    }

    @Override
    public void updateCompanyCoreFields(String userId, String creditCode, String companyIndustry,
                                         String companyScale, String contactPerson, String contactPosition,
                                         String contactPhone, String contactEmail) {
        companyProfileDao.updateCoreFields(userId, creditCode, companyIndustry, companyScale,
                contactPerson, contactPosition, contactPhone, contactEmail);
    }

    @Override
    public void updateCompanyFreeFields(String userId, String companyIntro, List<String> companyTags,
                                         String companyWebsite, String companyRegion) {
        String tagsJson = companyTags != null ? JSON.toJSONString(companyTags) : null;
        companyProfileDao.updateFreeFields(userId, companyIntro, tagsJson, companyWebsite, companyRegion);
    }

    @Override
    public void updateCompanyCertificationStatus(String userId, String status, String rejectReason) {
        LocalDateTime certifiedAt = CertificationStatusEnum.CERTIFIED.getCode().equals(status)
                ? LocalDateTime.now() : null;
        companyProfileDao.updateCertificationStatus(userId, status, rejectReason, certifiedAt);
    }

    @Override
    public List<CompanyProfileEntity> queryCompaniesByCertificationStatus(String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<CompanyProfile> list = companyProfileDao.selectByCertificationStatus(status, offset, pageSize);
        return list.stream().map(cp -> CompanyProfileEntity.builder()
                .userId(cp.getUserId())
                .creditCode(cp.getCreditCode())
                .qualificationFiles(StrUtil.isNotBlank(cp.getQualificationFiles()) ?
                    JSON.parseArray(cp.getQualificationFiles(), String.class) : null)
                .certificationStatus(cp.getCertificationStatus())
                .rejectReason(cp.getRejectReason())
                .certifiedAt(cp.getCertifiedAt())
                .updateTime(cp.getUpdateTime())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public long countCompaniesByCertificationStatus(String status) {
        return companyProfileDao.countByCertificationStatus(status);
    }
}
