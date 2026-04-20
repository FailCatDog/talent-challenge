package cn.hgy.api;

import cn.hgy.api.dto.TeamCreateRequestDTO;
import cn.hgy.api.dto.TeamInviteHandleRequestDTO;
import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamInviteRequestDTO;
import cn.hgy.api.dto.TeamListResponseDTO;
import cn.hgy.api.dto.TeamResponseDTO;
import cn.hgy.api.dto.TeamUpdateRequestDTO;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ITeamHttpService {

    Response<String> createTeam(TeamCreateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> updateTeam(TeamUpdateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> disbandTeam(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getTeamDetail(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getMyTeam(HttpServletRequest request);

    Response<TeamListResponseDTO> getMyTeams(Integer pageNum, Integer pageSize, HttpServletRequest request);

    Response<Boolean> inviteMember(TeamInviteRequestDTO dto, HttpServletRequest request);

    Response<List<TeamInviteItemDTO>> getMyInvites(String status, HttpServletRequest request);

    Response<Boolean> handleInvite(TeamInviteHandleRequestDTO dto, HttpServletRequest request);
}

