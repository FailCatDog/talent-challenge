package cn.hgy.domain.team.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 团队成员信息值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberVO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 角色：leader / member
     */
    private String role;
}
