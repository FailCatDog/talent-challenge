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
public class TeamListResponseDTO {

    private List<TeamResponseDTO> teams;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
}

