import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.framework.ZkBeanService;


public class spring {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("NewFile.xml");
		ZkBeanService bean = (ZkBeanService) applicationContext.getBean("ss");
		System.out.println(bean.getAddress());
	}
}
