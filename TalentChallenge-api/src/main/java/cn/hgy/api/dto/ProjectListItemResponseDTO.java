package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-19
 * @Description: 项目列表项响应传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectListItemResponseDTO {
    private String projectId;
    private String serialNumber;
    private String title;
    private Integer applicationCount;
    private String acceptedTeamId;
    private String acceptedTeamName;
    private String status;
    private BigDecimal budgetAmount;
    private String currency;
    private String publisherName;
    private Integer viewCount;
    private String currentMilestone;
    private LocalDate expectedEndDate;
    private LocalDateTime submittedAt;
}

