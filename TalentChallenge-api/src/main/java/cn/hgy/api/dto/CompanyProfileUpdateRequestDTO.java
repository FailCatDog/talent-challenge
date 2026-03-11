package cn.hgy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 企业更新可自由修改字段的请求DTO
 * 这些字段不受认证状态限制，随时可改
 */
@Data
public class CompanyProfileUpdateRequestDTO {

    /** 企业简介 */
    private String companyIntro;

    /** 企业标签 */
    private List<String> companyTags;

    /** 企业官网 */
    private String companyWebsite;

    /** 企业地区 */
    private String companyRegion;
}
