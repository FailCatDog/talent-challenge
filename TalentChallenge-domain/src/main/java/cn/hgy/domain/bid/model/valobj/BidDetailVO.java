package cn.hgy.domain.bid.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 揭榜详情值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidDetailVO {

    /**
     * 揭榜ID
     */
    private String bidId;

    /**
     * 揭榜状态
     */
    private String status;

    /**
     * 揭榜申请内容
     */
    private String content;

    /**
     * 附件URL
     */
    private String attachmentUrl;

    /**
     * 备注（拒绝原因等）
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 关联项目ID
     */
    private String projectId;

    /**
     * 关联项目标题
     */
    private String projectTitle;

    /**
     * 揭榜团队ID
     */
    private String teamId;

    /**
     * 揭榜团队名称
     */
    private String teamName;

    /**
     * 当前请求用户在团队中的角色：leader / member / none
     */
    private String currentUserRole;
}
