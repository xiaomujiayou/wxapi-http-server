# wxapi-http-server
搭建自己的微信API服务，原理是HOOK微信PC版并封装成HTTP接口。

支持微信版本：2.9.5.41 [下载](https://share.weiyun.com/egS8Mx3F "下载")

[接口文档](https://xiaomujiayou.github.io/wxapi/ "接口文档") （更新中)


------------

#### 开发环境、用到的框架和类库：
- 开发环境：
  - IDEA
  - JDK-1.8
  - maven-3.6.1
  - Sqlite
- 用到的框架：
  - SpringBoot-2.3.2
  - MyBatis-3.4.6
  - [通用Mapper](https://github.com/abel533/Mapper "通用Mapper")
- 用到的类库：
  - [hutool](https://github.com/looly/hutool "hutool")
  - [PageHelper：MyBatis分页插件](https://github.com/pagehelper/Mybatis-PageHelper "PageHelper：MyBatis分页插件")

------------

使用：


### 使用前的准备
- 导入sql文件，生成表，修改数据库配置。默认支持sqlite，也可自行切换mysql,只需引入依赖，修改配置即可。
- 改好配置文件后删除文件名中的“-demo”
- 启动服务
- 启动控制端,修改服务器地址，连接服务器。
- 根据

1. 修改seata Server模块配置文件file.conf

```
store {
  ## store mode: file、db
  mode = "db"
  ## file store property
  file {
    ## store location dir
    dir = "sessionStore"
    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    maxBranchSessionSize = 16384
    # globe session size , if exceeded throws exceptions
    maxGlobalSessionSize = 512
    # file buffer size , if exceeded allocate new buffer
    fileWriteBufferCacheSize = 16384
    # when recover batch read size
    sessionReloadReadSize = 100
    # async, sync
    flushDiskMode = async
  }
  ## database store property
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "druid"
    ## mysql/oracle/h2/oceanbase etc.
    dbType = "mysql"
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://数据库连接/wechat_seata"
    user = "账号"
    password = "密码"
    minConn = 1
    maxConn = 10
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
  }
}
```

2. 修改seata Server模块配置文件registry.conf

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "eureka"
#  eureka {
#    serviceUrl = "http://eureka-service:8101/eureka/"
#    application = "seata-server"
#    weight = "1"
#  }
  eureka {
    serviceUrl = "http://你的eureka服务器地址/eureka/"
    application = "seata-server"
    weight = "1"
  }
}
config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "file"
  nacos {
    serverAddr = "localhost"
    namespace = ""
    group = "SEATA_GROUP"
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  apollo {
    app.id = "seata-server"
    apollo.meta = "http://192.168.1.204:8801"
    namespace = "application"
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  etcd3 {
    serverAddr = "http://localhost:2379"
  }
  file {
    name = "file.conf"
  }
}
```

3. 修改完成后在seata项目根目录运行 `mvn clean package`，打包完成后项目根目录会生成distribution\bin\seata-server，启动seata-server后会注册到配置的eureka服务，所以在此之前应启动eureak服务，如果一切正常eureka控制台会出现seata-server服务（如需帮助请联系）

#### 启动服务
以上配置修改完成后可按如下顺序启动服务
1. cloud_eureka
2. seata-server
3. cloud_gateway
4. api_mall
5. api_user
6. api_activite
7. api_pay
8. api_mini
9. cron_service
10. wind_control

#### 部分依赖可能无法下载：

```
    <!--淘宝sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.tb</groupId>
        <artifactId>taobao-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <!--唯品会sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>osp-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>vop-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--蘑菇街sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.mgj</groupId>
        <artifactId>openapi-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--拼多多sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.pdd</groupId>
        <artifactId>pop-sdk</artifactId>
        <classifier>all</classifier>
        <version>1.6.1</version>
    </dependency>
```

依赖已打包：

`链接：https://pan.baidu.com/s/1VDY0n9SEIK3T0VY_hsFYyw 提取码：ug5y`

也可自行去官网下载。
       

------------
联系我获取前端代码：

![微信扫码](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/my.jpg?x-oss-process=image/resize,h_200,w_200 "微信扫码")
