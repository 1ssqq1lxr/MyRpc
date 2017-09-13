import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.it.netty.rpc.service.Person;
import com.it.netty.rpc.service.PersonService;


public class springClient {
	public static void main(String[] args) throws InterruptedException {
		@SuppressWarnings("resource")

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
		ClassPathXmlApplicationContext applicationContext1 = new ClassPathXmlApplicationContext("consume.xml");
		final PersonService bean = (PersonService) applicationContext.getBean(PersonService.class);
		final PersonService bean1 = (PersonService) applicationContext1.getBean(PersonService.class);
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		for(int j=0;j<100;j++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						countDownLatch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Person name = bean.getName();
					System.out.println(name.getName());
				}
			}).start();
		}
		for(int j=0;j<100;j++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						countDownLatch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Person name1 = bean1.getName();
					System.out.println(name1.getName());
				}
			}).start();
		}

		countDownLatch.countDown();

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
