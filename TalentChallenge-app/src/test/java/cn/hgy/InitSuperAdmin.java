package cn.hgy;

import cn.hgy.domain.user.service.IUserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 一键初始化超级管理员
 * 使用方式：在IDE中运行此测试方法，或执行 mvn test -Dtest=InitSuperAdmin#initSuperAdmin
 */
@Slf4j
@SpringBootTest
public class InitSuperAdmin {

    @Autowired
    private IUserManagementService userManagementService;

    @Test
    public void initSuperAdmin() {
        String nickname = "超级管理员";
        String identityType = "username";
        String identifier = "admin";
        String credential = "Admin@123456";

        log.info("==========================================");
        log.info("开始初始化超级管理员账号...");
        log.info("==========================================");

        try {
            userManagementService.createAdminAccount(nickname, identityType, identifier, credential);
            
            log.info("✅ 超级管理员创建成功！");
            log.info("------------------------------------------");
            log.info("登录信息：");
            log.info("  用户名: {}", identifier);
            log.info("  密码: {}", credential);
            log.info("------------------------------------------");
            log.info("⚠️  请在首次登录后立即修改密码");
            log.info("==========================================");
            
        } catch (Exception e) {
            log.error("❌ 超级管理员创建失败", e);
            log.error("原因: {}", e.getMessage());
            
            if (e.getMessage().contains("该用户已存在")) {
                log.warn("------------------------------------------");
                log.warn("超级管理员账号已存在，无需重复创建");
                log.warn("如需重置，请先在数据库中删除旧账号：");
                log.warn("  DELETE FROM user_auth_credential WHERE identifier = 'admin';");
                log.warn("  DELETE FROM user WHERE user_id = (SELECT user_id FROM user_auth_credential WHERE identifier = 'admin');");
                log.warn("------------------------------------------");
            }
        }
    }
}

