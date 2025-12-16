package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidResponseDTO {

    private String bidId;
    private String projectId;
    private String teamId;
    private String status;
    private String content;
    private String attachmentUrl;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

