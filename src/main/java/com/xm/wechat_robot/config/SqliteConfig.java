package com.xm.wechat_robot.config;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import tk.mybatis.mapper.code.IdentityDialect;

import java.lang.reflect.Field;

/**
 * 由于通用Mapper并未支持Sqlite，解决sqlite取回主键异常
 * @throws NoSuchFieldException
 * @throws IllegalAccessException
 */
public class SqliteConfig  implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        try {
            Field field = IdentityDialect.MYSQL.getClass().getDeclaredField("identityRetrievalStatement");
            field.setAccessible(true);
            field.set(IdentityDialect.MYSQL,"select last_insert_rowid()");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
