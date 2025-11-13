package cn.hgy.api;

import cn.hgy.api.dto.BidApplyRequestDTO;
import cn.hgy.api.dto.BidDecisionRequestDTO;
import cn.hgy.api.dto.BidListResponseDTO;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

public interface IBidHttpService {

    Response<Boolean> apply(BidApplyRequestDTO dto, HttpServletRequest request);

    Response<BidListResponseDTO> getProjectBids(String projectId, String status, Integer pageNum, Integer pageSize, HttpServletRequest request);

    Response<Boolean> shortlist(BidDecisionRequestDTO dto, HttpServletRequest request);

    Response<Boolean> confirm(BidDecisionRequestDTO dto, HttpServletRequest request);

    Response<Boolean> reject(BidDecisionRequestDTO dto, HttpServletRequest request);

    Response<Boolean> cancel(String bidId, HttpServletRequest request);
}

