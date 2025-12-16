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
        Path location;
        if (isWindows) {
            location = Paths.get(System.getProperty("user.dir"), "data", "milestones");
        } else {
            location = Paths.get("/data", "milestones");
        }
        String fileLocation = location.toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/static/milestones/**")
                .addResourceLocations(fileLocation);
    }
}

