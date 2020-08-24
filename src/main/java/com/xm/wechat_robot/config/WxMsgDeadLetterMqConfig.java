package com.xm.wechat_robot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信消息死信队列
 */
@Deprecated
//@Configuration
public class WxMsgDeadLetterMqConfig {
    // 声明死信队列
    @Bean
    public Queue wxMsgLetterQueue(){
        return new Queue(WxMsgMqConfig.QUEUE_WX_MSG_FAIL,true);
    }

    // 声明死信队列绑定关系
    @Bean
    public Binding wxMsgLetterBinding(Queue wxMsgLetterQueue){
        return BindingBuilder.bind(wxMsgLetterQueue).to(new DirectExchange(WxMsgMqConfig.EXCHANGE)).with(WxMsgMqConfig.KEY_WX_MSG_FAIL);
    }
}
