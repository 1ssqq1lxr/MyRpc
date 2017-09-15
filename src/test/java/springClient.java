import java.util.Collection;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.it.netty.rpc.service.Person;
import com.it.netty.rpc.service.PersonService;


public class springClient {
	@Test 
	public  void test() {     
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
		final PersonService bean = (PersonService) applicationContext.getBean(PersonService.class);

		TestRunnable runner = new TestRunnable() { 
			@Override 
			public void runTest() throws Throwable { 
				// 测试内容 

				Person name = bean.getName();
				System.out.println(name.getName());
			} 
		}; 
		int runnerCount = 10000; 
		//Rnner数组，想当于并发多少个。 
		TestRunnable[] trs = new TestRunnable[runnerCount]; 
		for (int i = 0; i < runnerCount; i++) { 
			trs[i] = runner; 
		} 
		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入 

		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs); 
		try { 
			// 开发并发执行数组里定义的内容 
			long start = System.currentTimeMillis();
			mttr.runTestRunnables(); 
			long used = System.currentTimeMillis() - start;
			System.out.printf("%s 调用花费 %s milli-seconds.\n", runnerCount, used);
		} catch (Throwable e) { 
			e.printStackTrace(); 
		} 

	}

	//	public static void main(String[] args) throws InterruptedException {
	//		@SuppressWarnings("resource")
	//
	//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consume.xml");
	////		ClassPathXmlApplicationContext applicationContext1 = new ClassPathXmlApplicationContext("consume.xml");
	//		final PersonService bean = (PersonService) applicationContext.getBean(PersonService.class);
	////		final PersonService bean1 = (PersonService) applicationContext1.getBean(PersonService.class);
	//		final CountDownLatch countDownLatch = new CountDownLatch(1);
	//		for(int j=0;j<15000;j++){
	//			new Thread(new Runnable() {
	//				@Override
	//				public void run() {
	//					// TODO Auto-generated method stub
	//					try {
	////						countDownLatch.await();
	//					} catch (Exception e) {
	//						// TODO Auto-generated catch block
	//						e.printStackTrace();
	//					}
	//					Person name = bean.getName();
	//					System.out.println(name.getName());
	//				}
	//			}).start();
	//		}
	////		for(int j=0;j<100;j++){
	////			new Thread(new Runnable() {
	////				@Override
	////				public void run() {
	////					// TODO Auto-generated method stub
	////					try {
	////						countDownLatch.await();
	////					} catch (InterruptedException e) {
	////						// TODO Auto-generated catch block
	////						e.printStackTrace();
	////					}
	////					Person name1 = bean1.getName();
	////					System.out.println(name1.getName());
	////				}
	////			}).start();
	////		}
	//
	//		countDownLatch.countDown();
	//
	//	}
	//	//		for(int i=0;i<100;i++){
	//	//			new Thread(new Runnable() {
	//	//				@Override
	//	//				public void run() {
	//	//					// TODO Auto-generated method stub
	//	//					bean.get1Name();
	//	//					
	//	//				}
	//	//			}).start();
	//	//
	//	//		}
	//	//		for(int i=0;i<100;i++){
	//	//			new Thread(new Runnable() {
	//	//				@Override
	//	//				public void run() {
	//	//					// TODO Auto-generated method stub
	//	//					System.out.println(bean.setName("ohyeaj"));
	//	//				}
	//	//			}).start();
	//	//
	//	//		}
}
