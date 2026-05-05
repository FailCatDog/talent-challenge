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
public class StudentBidListResponseDTO {

    private String bidId;
    private String projectId;
    private String projectTitle;
    private String publisherName;
    private String teamId;
    private String teamName;
    private String status;
    private String currentMilestone;
    private LocalDateTime createdAt;
}
