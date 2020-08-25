# wxapi-http-server
搭建自己的微信API服务，原理是HOOK微信PC版并封装成HTTP接口。




[接口文档](https://xiaomujiayou.github.io/wxapi/ "接口文档") （更新中)

### 下载：

最新版 [下载](https://github.com/xiaomujiayou/wxapi-http-server/releases "下载")

对应微信版本：2.9.5.41 [下载](https://share.weiyun.com/egS8Mx3F "下载")


### 使用：


- 方式一：
  - 运行控制端.exe - 设置 - 服务器地址 填写：129.211.37.193:8888 即可。
- 方式二：
  - 安装java 1.8 环境
  - 解压 server.zip 进入根目录运行 java -jar wechat_robot-xxx.jar （如有乱码修改控制台编码UTF-8）
  - 运行控制端.exe - 设置 - 服务器地址 填写：127.0.0.1:8888。
- 方式三：

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
联系方式：

![微信扫码](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/my.jpg?x-oss-process=image/resize,h_200,w_200 "微信扫码")
