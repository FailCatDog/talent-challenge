package cn.hgy.api;

import cn.hgy.api.dto.TeamCreateRequestDTO;
import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamInviteRequestDTO;
import cn.hgy.api.dto.TeamMemberRemoveRequestDTO;
import cn.hgy.api.dto.TeamResponseDTO;
import cn.hgy.api.dto.TeamSummaryResponseDTO;
import cn.hgy.api.dto.TeamUpdateRequestDTO;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ITeamHttpService {

    Response<String> createTeam(TeamCreateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> updateTeam(TeamUpdateRequestDTO dto, HttpServletRequest request);

    Response<Boolean> disbandTeam(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getTeamDetail(String teamId, HttpServletRequest request);

    Response<TeamResponseDTO> getMyOwnedTeam(HttpServletRequest request);

    Response<List<TeamSummaryResponseDTO>> getMyJoinedTeams(HttpServletRequest request);

    Response<TeamResponseDTO> getMyJoinedTeamDetail(String teamId, HttpServletRequest request);

    Response<Boolean> removeMember(TeamMemberRemoveRequestDTO dto, HttpServletRequest request);

    Response<Boolean> inviteMember(TeamInviteRequestDTO dto, HttpServletRequest request);

    Response<List<TeamInviteItemDTO>> getMyPendingInvites(HttpServletRequest request);

    Response<Boolean> agreeInvite(String inviteId, HttpServletRequest request);

    Response<Boolean> rejectInvite(String inviteId, HttpServletRequest request);
}

