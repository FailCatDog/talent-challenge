package cn.hgy.config;

import cn.hgy.domain.project.service.IMilestoneTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板初始化组件（系统启动时自动初始化默认模板）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MilestoneTemplateInitializer implements CommandLineRunner {
    
    private final IMilestoneTemplateService templateService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化默认里程碑模板...");
        try {
            boolean success = templateService.initializeDefaultTemplates();
            if (success) {
                log.info("默认里程碑模板初始化成功");
            } else {
                log.warn("默认里程碑模板初始化失败或模板已存在");
            }
        } catch (Exception e) {
            log.error("初始化默认里程碑模板时发生异常", e);
        }
    }
}

