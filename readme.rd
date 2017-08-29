# 自己业余时间写的rpc  目前功能还不够完善 
主要使用技术以及后期开发功能简介：
1、rpc远程调用使用netty作为通信框架，采用netty主从线程模型。
2、目前开发给予tcp协议长链接的rpc远程调用，支持异步回调，超时时间，后期加入接口调用统计,权限。
3、序列化目前支持jdk，hessian，jakson序列化，后面准备支持protobuf，Marshalling等序列化技术。
4、使用zookeeper作为服务的注册与发现中心。
5、整合spring方便使用。
6、目前代理使用jdk，后期支持Javassist，cglib等
7、后期开发负载均衡路由模块.....



使用方式:
	注册服务：
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:rpc="http://www.lxr.com/schema/rpc"  
    xsi:schemaLocation="  
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
        http://www.lxr.com/schema/rpc 
        http://www.lxr.com/schema/rpc/rpc-1.0.0.xsd">  
    	<bean id="perservice" class="com.it.netty.rpc.service.PersonServiceImpl"></bean>
 		<rpc:server id="test" serverPort="8096" zkAddress="127.0.0.1:12181">
 				<rpc:serviceRegeist class="com.it.netty.rpc.service.PersonService"></rpc:serviceRegeist>
 		</rpc:server>
	</beans>  
	#在 com.it.netty.rpc.service.PersonServiceImpl类上加上注解@RpcService
	消费服务：
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"  
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
	    xmlns:rpc="http://www.lxr.com/schema/consume"  
	    xsi:schemaLocation="  
	        http://www.springframework.org/schema/beans 
	        http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
	        http://www.lxr.com/schema/consume 
	        http://www.lxr.com/schema/consume/rpc-consume-1.0.0.xsd">  
	        <rpc:serviceBind id="123"  zkAddress="127.0.0.1:12181" protocol="HESSIAN">
	        			<rpc:serviceConsume name="personService" class="com.it.netty.rpc.service.PersonService"></rpc:serviceConsume>
	        </rpc:serviceBind>
	</beans>  
	代码中：
	@Autowired
	PersonService personService;
	即可使用。
	
	
	
	