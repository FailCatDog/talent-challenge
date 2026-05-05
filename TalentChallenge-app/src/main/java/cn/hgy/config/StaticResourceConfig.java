package cn.hgy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 静态资源映射：/static/milestones/** -> 本地文件目录
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private static final String WINDOWS_PREFIX = "win";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String osName = System.getProperty("os.name", "").toLowerCase();
        boolean isWindows = osName.startsWith(WINDOWS_PREFIX);

        // 里程碑文件映射
        Path milestonesLocation = isWindows
                ? Paths.get(System.getProperty("user.dir"), "data", "milestones")
                : Paths.get("/data", "milestones");
        registry.addResourceHandler("/static/milestones/**")
                .addResourceLocations(milestonesLocation.toAbsolutePath().toUri().toString());

        // 企业资质文件映射
        Path qualificationsLocation = isWindows
                ? Paths.get(System.getProperty("user.dir"), "data", "qualifications")
                : Paths.get("/data", "qualifications");
        registry.addResourceHandler("/static/qualifications/**")
                .addResourceLocations(qualificationsLocation.toAbsolutePath().toUri().toString());
    }
}

