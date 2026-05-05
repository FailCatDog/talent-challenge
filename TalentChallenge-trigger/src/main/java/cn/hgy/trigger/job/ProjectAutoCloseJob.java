package cn.hgy.trigger.job;

import cn.hgy.domain.project.service.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectAutoCloseJob {

    private final IProjectService projectService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void autoCloseCompletedProjects() {
        int closedCount = projectService.autoCloseCompletedProjects();
        if (closedCount > 0) {
            log.info("自动关闭已完成两周的项目 count={}", closedCount);
        }
    }
}
