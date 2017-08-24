import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.framework.ZkBeanService;
import com.it.netty.rpc.service.PersonService;


public class springClient {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
		PersonService bean = (PersonService) applicationContext.getBean("personService");
	}
}
