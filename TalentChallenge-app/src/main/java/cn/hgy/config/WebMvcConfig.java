package cn.hgy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 配置静态资源访问映射，使上传的文件可以通过 URL 访问
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path.milestones}")
    private String milestonesPath;

    @Value("${file.upload.path.qualifications}")
    private String qualificationsPath;

    @Value("${file.upload.path.bids}")
    private String bidsPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 里程碑文件访问映射
        registry.addResourceHandler("/static/milestones/**")
                .addResourceLocations("file:" + milestonesPath + "/");

        // 企业资质文件访问映射
        registry.addResourceHandler("/static/qualifications/**")
                .addResourceLocations("file:" + qualificationsPath + "/");

        // 揭榜附件访问映射
        registry.addResourceHandler("/static/bids/**")
                .addResourceLocations("file:" + bidsPath + "/");
    }
}
