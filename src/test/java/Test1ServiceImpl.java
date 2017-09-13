import com.it.netty.rpc.annocation.RpcService;

@RpcService
public class Test1ServiceImpl  implements Test1Service{

	@Override
	public String say(String str) {
		// TODO Auto-generated method stub
		return str;
	}

}
