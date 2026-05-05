package cn.hgy.domain.bid.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公开揭榜列表项值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPublicListItemVO {

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
