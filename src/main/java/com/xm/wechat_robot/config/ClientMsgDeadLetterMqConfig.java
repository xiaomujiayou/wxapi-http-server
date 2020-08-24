package com.xm.wechat_robot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端消息死信队列的配置
 */
@Deprecated
//@Configuration
public class ClientMsgDeadLetterMqConfig {

    // 声明死信队列
    @Bean
    public Queue clientMsgLetterQueue(){
        return new Queue(ClientMsgMqConfig.QUEUE_CLIENT_MSG_FAIL,true);
    }

    // 声明死信队列绑定关系
    @Bean
    public Binding clientMsgLetterBinding(Queue clientMsgLetterQueue){
        return BindingBuilder.bind(clientMsgLetterQueue).to(new DirectExchange(ClientMsgMqConfig.EXCHANGE)).with(ClientMsgMqConfig.KEY_CLIENT_MSG_FAIL);
    }
}