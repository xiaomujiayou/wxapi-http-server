package com.xm.wechat_robot;

import com.xm.wechat_robot.config.SqliteConfig;
import com.xm.wechat_robot.message.ClientMsgQueue;
import com.xm.wechat_robot.message.WxMsgQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


@ComponentScan("com.xm")
@MapperScan(basePackages = {"com.xm.wechat_robot.mapper","com.xm.wechat_robot.mapper.custom"})
@SpringBootApplication
public class WechatRobotApplication implements CommandLineRunner {

    public static void main(String[] args) {
    //    SpringApplication.run(WechatRobotApplication.class, args);
        SpringApplication application = new SpringApplication(WechatRobotApplication.class);
        application.addListeners(new SqliteConfig());
        application.run(args);
    }

    @Autowired
    private ClientMsgQueue clientMsgQueue;
    @Autowired
    private WxMsgQueue wxMsgQueue;

    @Override
    public void run(String... args) throws Exception {
        new Thread(wxMsgQueue).start();
        new Thread(clientMsgQueue).start();
    }
}
