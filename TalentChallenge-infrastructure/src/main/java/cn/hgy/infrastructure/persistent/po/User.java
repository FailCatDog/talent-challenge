package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户PO对象
 * 
 * @author hgy
 */
@Data
public class User {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户唯一标识
     */
    private String userId;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像图片URL
     */
    private String avatarUrl;

    /**
     * 用户个人简介
     */
    private String bio;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 用户出生日期
     */
    private LocalDate birthday;

    /**
     * 用户所在地区编码
     */
    private String regionCode;

    /**
     * 用户所属行业
     */
    private String industry;

    /**
     * 用户头衔/职位
     */
    private String title;

    /**
     * 账号是否活跃（1-活跃，2-禁用）
     */
    private Integer isActive;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updateTime;
}
