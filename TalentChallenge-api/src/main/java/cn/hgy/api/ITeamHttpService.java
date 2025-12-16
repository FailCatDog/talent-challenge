package cn.hgy.api;

import cn.hgy.api.dto.TeamCreateRequestDTO;
import cn.hgy.api.dto.TeamListResponseDTO;
import cn.hgy.api.dto.TeamResponseDTO;
import cn.hgy.api.dto.TeamUpdateRequestDTO;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

public interface ITeamHttpService {

    Response<String> createTeam(TeamCreateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> updateTeam(TeamUpdateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> disbandTeam(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getTeamDetail(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getMyTeam(HttpServletRequest request);

    Response<TeamListResponseDTO> getMyTeams(Integer pageNum, Integer pageSize, HttpServletRequest request);
}

