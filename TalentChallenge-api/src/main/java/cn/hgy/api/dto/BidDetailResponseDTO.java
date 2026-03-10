package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidDetailResponseDTO {

    private String bidId;
    private String status;
    private String content;
    private String attachmentUrl;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ProjectInfo project;
    private TeamInfo team;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectInfo {
        private String projectId;
        private String projectTitle;
        private String projectDescription;
        private String publisherId;
        private String publisherName;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamInfo {
        private String teamId;
        private String teamName;
        private List<TeamMemberItemDTO> members;
    }
}
