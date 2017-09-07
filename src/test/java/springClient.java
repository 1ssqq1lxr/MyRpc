import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.it.netty.rpc.service.Person;
import com.it.netty.rpc.service.PersonService;


public class springClient {
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
		final PersonService bean = (PersonService) applicationContext.getBean(PersonService.class);
		for(int i=0;i<100;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Person name = bean.getName();
					System.out.println(name.getName());
				}
			}).start();

		}
//		for(int i=0;i<100;i++){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					bean.get1Name();
//					
//				}
//			}).start();
//
//		}
//		for(int i=0;i<100;i++){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					System.out.println(bean.setName("ohyeaj"));
//				}
//			}).start();
//
//		}
	}
}
