package cn.hgy.domain.bid.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生揭榜列表项值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentBidListItemVO {

    /**
     * 揭榜ID
     */
    private String bidId;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目标题
     */
    private String projectTitle;

    /**
     * 发布者名称
     */
    private String publisherName;

    /**
     * 团队ID
     */
    private String teamId;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
