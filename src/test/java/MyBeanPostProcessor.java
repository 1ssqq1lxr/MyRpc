import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


public class MyBeanPostProcessor implements BeanPostProcessor{
    public MyBeanPostProcessor() {  
        super();  
        System.out.println("这是BeanPostProcessor实现类构造器！！");          
     }  
   
     @Override  
     public Object postProcessAfterInitialization(Object bean, String arg1)  
             throws BeansException {  
         System.out.println("bean处理器：bean创建之后..");  
         return bean;  
     }  
   
     @Override  
     public Object postProcessBeforeInitialization(Object bean, String arg1)  
             throws BeansException {  
         System.out.println("bean处理器：bean创建之前..");  
       
         return bean;  
     }  
}
