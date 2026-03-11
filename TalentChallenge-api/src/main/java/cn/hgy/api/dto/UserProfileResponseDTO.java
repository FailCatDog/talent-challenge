package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {

    // 基础信息
    private String userId;
    private Integer type;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private Integer gender;
    private LocalDate birthday;
    private String regionCode;
    private String industry;
    private String title;
    private LocalDateTime lastLoginTime;

    // 学生信息（type=1时有效）
    private String realName;
    private String college;
    private String major;
    private String grade;
    private String studentId;
    private String email;
    private String phone;
    private List<String> techTags;
    private String techIntro;

    // 企业信息（type=2时有效）
    private String companyIntro;
    private List<String> companyTags;
    private String creditCode;
    private String companyIndustry;
    private String companyScale;
    private String companyRegion;
    private String companyWebsite;
    private String contactPerson;
    private String contactPosition;
    private String contactPhone;
    private String contactEmail;
    private List<String> qualificationFiles;

    // 企业认证信息
    private String certificationStatus;
    private String rejectReason;
    private LocalDateTime certifiedAt;
}

