package cn.hgy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Configurable
@EnableRabbit
@MapperScan("cn.hgy.infrastructure.persistent.dao")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
