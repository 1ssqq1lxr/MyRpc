# MyRPC
主要使用技术以及后期开发功能简介：
<br>
1、rpc远程调用使用netty作为通信框架，采用netty主从线程模型。
2、目前开发给予tcp协议长链接的rpc远程调用，支持异步回调，超时时间，后期加入接口调用统计,权限。
3、序列化目前支持jdk，hessian，jakson序列化，后面准备支持protobuf，Marshalling等序列化技术。
4、使用zookeeper作为服务的注册与发现中心。
5、整合spring方便使用。
6、目前代理默认使用jdk，支持Javassist，cglib等
7、支持轮询，随机，权重随机，一致性hash 负载均衡算法
<br><br>
## 服务注册端
<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:rpc="http://www.lxr.com/schema/rpc"  
    xsi:schemaLocation="  
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
        http://www.lxr.com/schema/rpc 
        http://www.lxr.com/schema/rpc/rpc-1.0.0.xsd">  
    	<bean id="perservice" class="*.*ServiceImpl"/>
 		<rpc:server id="test" serverPort="8096" zkAddress="127.0.0.1:12181">
			<rpc:serviceRegeist class="*.*.*Service" timeout="5000"/>
 		</rpc:server>
	</beans>  
	serverPort:默认开启tcp端口;
	class:	提供服务的接口类（可写接口或者实现类）
			并且在 com.it.netty.rpc.service.PersonServiceImpl类上加上注解@RpcService
	zkAddress: zookeeper地址;
	timeout：默认请求超时时间为5000毫秒，可设置2000-5000范围内;
<br><br>
## 服务消费端
	<beans xmlns="http://www.springframework.org/schema/beans"  
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
		xmlns:rpc="http://www.lxr.com/schema/consume"  
		xsi:schemaLocation="  
		http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
		http://www.lxr.com/schema/consume 
		http://www.lxr.com/schema/consume/rpc-consume-1.0.0.xsd">  
		<rpc:serviceBind id="123"  zkAddress="127.0.0.1:12181" 
			proxy="cglib" clientGroup-thread-nums="50" protocol="HESSIAN">
		    <rpc:serviceConsume name="*Service"interface="*.*Service"/>
		</rpc:serviceBind>
	</beans>  
		interface:代理接口全限定名;
		zkAddress: zookeeper地址;
		proxy:动态代理模式 目前支持jdk,cglib 默认jdk;
		clientGroup-thread-nums:客户端event线程数;
		protocol:序列化,目前支持 jdk，jakson，hessian，默认hessian;
<br>
<br>
## 交流1074455781
<br>
<br>
<br>

 
