import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.framework.Test;


public class spring {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("NewFile.xml");
		Test bean = applicationContext.getBean(Test.class);
		System.out.println(bean.getAddress());
	}
}
