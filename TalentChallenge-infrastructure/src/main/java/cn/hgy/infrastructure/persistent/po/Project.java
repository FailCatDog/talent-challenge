package cn.hgy.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目PO对象
 * 
 * @author hgy
 */
@Data
public class Project {
    
    private Long id;
    private String projectId;
    private String serialNumber;
    private String title;
    private String description;
    private String categoryId;
    private String tags; // JSON字符串
    private String requirements;
    private String skillLevel;
    private Integer teamSizeMin;
    private Integer teamSizeMax;
    private Integer durationDays;
    private String budgetType;
    private BigDecimal budgetAmount;
    private String currency;
    private LocalDateTime publishedAt;
    private LocalDateTime applicationDeadline;
    private LocalDate expectedStartDate;
    private LocalDate expectedEndDate;
    private String status;
    private String rejectReason;
    private Integer progress;
    private String publisherId;
    private String acceptedTeamId;
    private Integer applicationCount;
    private Integer viewCount;
    private Integer favoriteCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long version;
    private String publisherName;
    private String acceptedTeamName;

}
