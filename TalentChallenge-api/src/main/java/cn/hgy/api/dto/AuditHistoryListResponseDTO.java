package cn.hgy.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuditHistoryListResponseDTO {

    private List<AuditHistoryResponseDTO> list;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;
}
