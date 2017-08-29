# 自己业余时间写的rpc  目前功能还不够完善 
主要使用技术以及后期开发功能简介：
1、rpc远程调用使用netty作为通信框架，采用netty主从线程模型。
2、目前开发给予tcp协议长链接的rpc远程调用，支持异步回调，超时时间，后期加入接口调用统计,权限。
3、序列化目前支持jdk，hessian，jakson序列化，后面准备支持protobuf，Marshalling等序列化技术。
4、使用zookeeper作为服务的注册与发现中心。
5、整合spring方便使用。
6、目前代理使用jdk，后期支持Javassist，cglib等
7、后期开发负载均衡路由模块.....
