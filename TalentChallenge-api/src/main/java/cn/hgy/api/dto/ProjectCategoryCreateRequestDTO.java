package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryCreateRequestDTO {
    private String name;
    private String parentId;
    private String description;
    private String icon;
    private Integer sortOrder;
}

