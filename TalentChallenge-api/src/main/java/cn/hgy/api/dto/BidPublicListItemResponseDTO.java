package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2026-03-09
 * @Description: 公开揭榜列表项响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPublicListItemResponseDTO {
    /**
     * 揭榜ID
     */
    private String bidId;

    /**
     * 团队ID
     */
    private String teamId;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 揭榜状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
