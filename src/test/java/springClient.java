import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.framework.ZkBeanService;
import com.it.netty.rpc.service.Person;
import com.it.netty.rpc.service.PersonService;


public class springClient {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
		final PersonService bean = (PersonService) applicationContext.getBean(PersonService.class);
		
		for(int i=0;i<30;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Person name = bean.getName();
					System.out.println(name.getName());
				}
			}).start();
		}

	}
}
