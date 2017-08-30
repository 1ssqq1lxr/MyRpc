import com.it.netty.rpc.romote.DeafultNettyServerRemoteConnection;



public class TestServer {
	public static void main(String[] args) {
		DeafultNettyServerRemoteConnection connection = new DeafultNettyServerRemoteConnection();
		connection.start();
	}
}
