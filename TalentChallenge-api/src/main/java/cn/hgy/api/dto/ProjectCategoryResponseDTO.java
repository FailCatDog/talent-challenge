package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryResponseDTO {
    private String projectCategoryId;
    private String name;
    private String parentId;
    private String description;
    private String icon;
    private Integer sortOrder;
    private Boolean isVisible;
    private Boolean isActive;
    private List<ProjectCategoryResponseDTO> children;
}

