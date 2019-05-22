# FunnyServer

> FunnyServer是一个使用Netty编写的简单的HTTP服务器，支持WebSocket


## 使用方法

1. maven中引入依赖

```xml
<dependency>
    <groupId>tech.hongjian</groupId>
    <artifactId>funny-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. 创建server

```java
public class TestServer {
    public static void main(String[] args) {
        FunnyServer.instance(ServerConfig.getPort(), true).start();
    }
}
```

3. 修改`server.properties`配置文件

```properties
# 监听端口
server.port=9000
# 静态资源路径，多个路径用','隔开
server.resource.location=static,public
# 静态资源映射的url前缀
#server.resource.url-prefix=

# controller包路径
web.controller-package=tech.hongjian.funnyserver.controller
# websocket包路径
web.websocket-package=tech.hongjian.funnyserver.controller.ws
```
4. 运行`main`方法
