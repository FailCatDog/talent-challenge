package cn.hgy.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法ID生成器配置
 * 统一管理分布式ID生成
 */
@Configuration
public class SnowFlakeConfig {

    @Value("${app.snowflake.data-center-id:1}")
    private long dataCenterId;

    @Value("${app.snowflake.worker-id:1}")
    private long workerId;

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(workerId, dataCenterId);
    }
}
