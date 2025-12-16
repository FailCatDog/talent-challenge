package cn.hgy.domain.bid.model.entity;

import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidEntity {

    private String bidId;
    private String projectId;
    private String teamId;
    private BidStatusEnum status;
    private String content;
    private String attachmentUrl;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}

