package cn.hgy.domain.team.service;

import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.model.entity.TeamInviteEntity;
import cn.hgy.domain.team.model.valobj.TeamInviteStatusEnum;
import cn.hgy.domain.team.model.valobj.TeamStatusEnum;
import cn.hgy.domain.team.repository.ITeamRepository;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.valobj.UserTypeEnum;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.types.common.TeamCommand;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private Snowflake snowflake;

    @Mock
    private ITeamRepository teamRepository;

    @Mock
    private IUserManagementRepository userManagementRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void shouldRejectCreateWhenStudentHasCreatedTeamBefore() {
        TeamCommand command = TeamCommand.builder()
                .teamName("A")
                .leaderId("u1")
                .build();

        when(teamRepository.countByLeaderId("u1")).thenReturn(1L);

        assertThatThrownBy(() -> teamService.createTeam(command))
                .isInstanceOf(AppException.class);

        verify(teamRepository, never()).save(any());
    }

    @Test
    void shouldRejectCreateWhenStudentAlreadyInActiveTeam() {
        TeamCommand command = TeamCommand.builder()
                .teamName("A")
                .leaderId("u1")
                .build();

        when(teamRepository.countByLeaderId("u1")).thenReturn(0L);
        when(teamRepository.queryActiveTeamByUserId("u1"))
                .thenReturn(Optional.of(TeamEntity.builder().teamId("t1").status(TeamStatusEnum.ACTIVE.getCode()).build()));

        assertThatThrownBy(() -> teamService.createTeam(command))
                .isInstanceOf(AppException.class);

        verify(teamRepository, never()).save(any());
    }

    @Test
    void shouldCreatePendingInviteWhenLeaderInvitesStudent() {
        TeamEntity teamEntity = TeamEntity.builder()
                .teamId("t1")
                .leaderId("leader1")
                .status(TeamStatusEnum.ACTIVE.getCode())
                .memberCount(1)
                .maxMemberCount(5)
                .build();
        UserEntity invitedUser = UserEntity.builder()
                .userId("user2")
                .type(UserTypeEnum.STUDENT.getCode())
                .build();

        when(teamRepository.queryByTeamId("t1")).thenReturn(Optional.of(teamEntity));
        when(userManagementRepository.queryUserBaseByUserId("user2")).thenReturn(invitedUser);
        when(teamRepository.queryActiveTeamByUserId("user2")).thenReturn(Optional.empty());
        when(teamRepository.existsPendingInvite("t1", "user2")).thenReturn(false);
        when(snowflake.nextIdStr()).thenReturn("invite-1");
        when(teamRepository.saveInvite(any(TeamInviteEntity.class))).thenReturn(true);

        boolean result = teamService.inviteMember("t1", "leader1", "user2");

        assertThat(result).isTrue();

        ArgumentCaptor<TeamInviteEntity> captor = ArgumentCaptor.forClass(TeamInviteEntity.class);
        verify(teamRepository).saveInvite(captor.capture());
        TeamInviteEntity inviteEntity = captor.getValue();
        assertThat(inviteEntity.getInviteId()).isEqualTo("invite-1");
        assertThat(inviteEntity.getStatus()).isEqualTo(TeamInviteStatusEnum.PENDING.getCode());
        assertThat(inviteEntity.getTeamId()).isEqualTo("t1");
        assertThat(inviteEntity.getInviterId()).isEqualTo("leader1");
        assertThat(inviteEntity.getInvitedUserId()).isEqualTo("user2");
    }

    @Test
    void shouldAcceptInviteAndAddMemberToTeam() {
        TeamInviteEntity inviteEntity = TeamInviteEntity.builder()
                .inviteId("invite-1")
                .teamId("t1")
                .invitedUserId("user2")
                .status(TeamInviteStatusEnum.PENDING.getCode())
                .build();
        TeamEntity teamEntity = TeamEntity.builder()
                .teamId("t1")
                .leaderId("leader1")
                .status(TeamStatusEnum.ACTIVE.getCode())
                .memberCount(1)
                .maxMemberCount(5)
                .build();

        when(teamRepository.queryInviteById("invite-1")).thenReturn(Optional.of(inviteEntity));
        when(teamRepository.queryByTeamId("t1")).thenReturn(Optional.of(teamEntity));
        when(teamRepository.queryActiveTeamByUserId("user2")).thenReturn(Optional.empty());
        when(teamRepository.addMember("t1", "user2", "member")).thenReturn(true);
        when(teamRepository.update(teamEntity)).thenReturn(true);
        when(teamRepository.updateInvite(any(TeamInviteEntity.class))).thenReturn(true);

        boolean result = teamService.handleInvite("invite-1", "user2", true);

        assertThat(result).isTrue();
        assertThat(teamEntity.getMemberCount()).isEqualTo(2);
        verify(teamRepository).addMember("t1", "user2", "member");

        ArgumentCaptor<TeamInviteEntity> captor = ArgumentCaptor.forClass(TeamInviteEntity.class);
        verify(teamRepository).updateInvite(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(TeamInviteStatusEnum.ACCEPTED.getCode());
        assertThat(captor.getValue().getHandleTime()).isNotNull();
    }

    @Test
    void shouldReturnMyInvites() {
        when(teamRepository.queryInvitesByInvitedUserId("user2", "pending"))
                .thenReturn(List.of(TeamInviteItemDTO.builder().inviteId("i1").status("pending").build()));

        List<TeamInviteItemDTO> invites = teamService.getMyInvites("user2", "pending");

        assertThat(invites).hasSize(1);
        assertThat(invites.get(0).getInviteId()).isEqualTo("i1");
    }
}
