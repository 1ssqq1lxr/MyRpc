import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.framework.ZkBean;


public class spring {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("NewFile.xml");
		ZkBean bean = (ZkBean) applicationContext.getBean("ss");
		System.out.println(bean.getAddress());
	}
}
