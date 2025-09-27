package cn.hgy.domain.user.model.entity;

import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-22
 * @Description: 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
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
     * 账号是否激活（1-激活，2-禁用）
     */
    private Integer isActive;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 验证账户是否可用
     * @return 是否成功
     */
    public Boolean checkUserStatus() {
        return Objects.equals(this.isActive, UserStatusEnum.ACTIVE.getStatusCode());
    }

    /**
     * 更新用户最后登录时间
     */
    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
    }

}
