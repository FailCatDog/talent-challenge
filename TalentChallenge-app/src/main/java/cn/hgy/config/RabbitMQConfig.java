package cn.hgy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    public static final String BID_EXCHANGE = "bid.exchange";
    
    public static final String PROJECT_SYNC_QUEUE = "bid.confirmed.project.sync";
    public static final String NOTIFICATION_QUEUE = "bid.confirmed.notification";
    
    public static final String ROUTING_KEY_BID_CONFIRMED = "bid.confirmed";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送成功 correlationData={}", correlationData);
            } else {
                log.error("消息发送失败 correlationData={}, cause={}", correlationData, cause);
            }
        });
        
        template.setReturnsCallback(returned -> {
            log.error("消息返回, exchange={}, routingKey={}, replyCode={}, replyText={}, message={}",
                    returned.getExchange(), returned.getRoutingKey(), returned.getReplyCode(),
                    returned.getReplyText(), returned.getMessage());
        });
        
        return template;
    }

    @Bean
    public TopicExchange bidExchange() {
        return ExchangeBuilder.topicExchange(BID_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue projectSyncQueue() {
        return QueueBuilder.durable(PROJECT_SYNC_QUEUE)
                .withArgument("x-dead-letter-exchange", "bid.dlx")
                .withArgument("x-dead-letter-routing-key", PROJECT_SYNC_QUEUE + ".dlq")
                .build();
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", "bid.dlx")
                .withArgument("x-dead-letter-routing-key", NOTIFICATION_QUEUE + ".dlq")
                .build();
    }

    @Bean
    public Binding projectSyncBinding() {
        return BindingBuilder
                .bind(projectSyncQueue())
                .to(bidExchange())
                .with(ROUTING_KEY_BID_CONFIRMED);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(bidExchange())
                .with(ROUTING_KEY_BID_CONFIRMED);
    }
}

