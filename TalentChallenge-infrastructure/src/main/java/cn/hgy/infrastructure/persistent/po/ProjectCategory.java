package cn.hgy.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 项目分类PO对象
 * 
 * @author hgy
 */
@Data
public class ProjectCategory {
    
    private Long id;
    private String projectCategoryId;
    private String name;
    private String parentId;
    private String description;
    private String icon;
    private Integer sortOrder;
    private Boolean isVisible;
    private Boolean isActive;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
