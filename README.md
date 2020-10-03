# wxapi-http-server
搭建自己的微信API服务，原理是HOOK微信PC版并封装成HTTP接口。


[接口文档](https://xiaomujiayou.github.io/wxapi/ "接口文档") （更新中)

### 下载：

最新版 [下载](https://github.com/xiaomujiayou/wxapi-http-server/releases "下载")

对应微信版本：2.9.5.41 [下载](https://share.weiyun.com/egS8Mx3F "下载")


------------

### 开发环境、用到的框架和类库：
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


### 使用：

- 使用现成的服务：
  - 运行控制端.exe - 设置 - 服务器地址 填写：`129.211.37.193:8888` 即可。
- 使用打包好的程序：
  - 安装java 1.8 环境
  - 启动服务：解压 server.zip 进入根目录运行 java -jar wechat_robot-xxx.jar （如有乱码修改控制台编码UTF-8）
  - 启动控制端：运行控制端.exe - 设置 - 服务器地址 填写：127.0.0.1:8888。
- docker：

### 消息回调：

获取用户发来的消息目前有三种方式：

1.设置http回调url

服务收到消息会将参数转发至该地址，设置方式：`控制端 - 设置 - 微信消息回调地址`，该方式适合web项目使用。

2.WebSocket方式(推荐)

接口地址：ws://serverIp:port/action/callback/B3563580-3029-44C3-B132-XXXXXX(机器码)

[参考案例](https://github.com/xiaomujiayou/wx-robot "参考案例")

3.接口轮询(不推荐)

通过`获取用户消息`接口轮番查询 [接口地址](https://xiaomujiayou.github.io/wxapi/#_1_5_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%81%8A%E5%A4%A9%E4%BF%A1%E6%81%AF%E5%8E%86%E5%8F%B2%E8%AE%B0%E5%BD%95 "接口地址")

### 常见问题：

- 控制端启动报错：无法找到指定DLL库文件***。安装openssl 
[下载](https://slproweb.com/download/Win32OpenSSL_Light-1_1_1h.exe "下载")

- 以管理员的身份运行控制端。




------------
联系方式：

![微信扫码](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/my.jpg?x-oss-process=image/resize,h_200,w_200 "微信扫码")
