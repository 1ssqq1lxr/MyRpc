import java.util.ArrayList;
import java.util.LinkedList;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.el.util.ConcurrentCache;
import org.junit.Test;

import com.it.netty.rpc.service.Person;


public class TestThread {
		@Test
		public void testListQuery(){
				// 单线程
				final LinkedList<Integer> lists = new LinkedList<>();
				final ArrayList<Integer> lists1 = new ArrayList<>();
				for(int i=0;i<10000;i++){
					lists.add(i);
				}
				lists.size();
				lists1.size();
				TestRunnable runner = new TestRunnable() { 
			
					@Override 
					public void runTest() throws Throwable { 
						// 测试内容 

//						for(int i=0;i<lists.size();i++){
//							lists.get(i);
//						}
						for(Integer s:lists){
							
						}
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
					long sum =0;
					int k=100;
					for(int i=0;i<k;i++){
						long start = System.currentTimeMillis();
						mttr.runTestRunnables(); 
						sum+= System.currentTimeMillis() - start;
						
					}
					System.out.printf("%s 次 调用    调用总花费 %s  平均时间  %s milli-seconds.\n",k, sum, sum/k);
				
				} catch (Throwable e) { 
					e.printStackTrace(); 
				} 
		}
}
