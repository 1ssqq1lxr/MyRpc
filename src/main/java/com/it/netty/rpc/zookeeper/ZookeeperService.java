package com.it.netty.rpc.zookeeper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.esotericsoftware.minlog.Log;
import com.it.netty.rpc.Config;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.protocol.ProtocolFactory;
import com.it.netty.rpc.protocol.jackson.JacksonProtocolFactory;
import com.it.netty.rpc.zookeeper.base.BaseZookeeperClient;
import com.it.netty.rpc.zookeeper.base.BaseZookeeperService;

public class ZookeeperService implements BaseZookeeperService ,InitializingBean,DisposableBean {

	private int port;

	private String zkAddress;

	private Certificate certificate;

	private String path;

	private NodeEventHandler eventHandler;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getZkAddress() {
		return zkAddress;
	}
	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}


	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	@SuppressWarnings("unused")
	private BaseZookeeperClient baseZookeeperClient;

	@SuppressWarnings("unused")
	private ProtocolFactory factory = new JacksonProtocolFactory();

	@SuppressWarnings("unused")
	private CuratorFramework curatorFramework;

	public ZookeeperService() throws Exception {
		super();
		
	}
	@Override
	public void registNode(String path, URI uri,CreateMode mode,boolean is) {
		path = path.startsWith("/")?path:"/"+path;
		try {
			if(is){
				curatorFramework.create().creatingParentsIfNeeded().withMode(mode).forPath(path, serialObject(uri));
			}
			else{
				curatorFramework.create().withMode(mode).forPath(path, serialObject(uri));
			}
		} catch (Exception e) {
			Log.error(this.getClass().getName()+"创建节点失败", e);
		}


	}
	@Override
	public void removeNode(String path,boolean is) {
		path = path.startsWith("/")?path:"/"+path;
		try {
			if(is)
				curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
			else
				curatorFramework.delete().forPath(path);
		} catch (Exception e) {
			Log.error(this.getClass().getName()+"删除节点失败", e);
		}
	}
	@Override
	public boolean exists(String path) {
		path = path.startsWith("/")?path:"/"+path;
		Stat forPath;
		try {
			forPath = curatorFramework.checkExists().forPath(path);
			if(forPath==null)
				return false;
			else
				return  true;
		} catch (Exception e) {
			Log.error(this.getClass().getName()+"检查节点是否存在失败", e);
		}
		return false;

	}
	@Override
	public URI getData(String path) {
		path = path.startsWith("/")?path:"/"+path;
		if(exists(path)){
			byte[] forPath;
			try {
				forPath = curatorFramework.getData().forPath(path);
				if(forPath!=null){
					return factory.decode(URI.class, forPath);
				}
			} catch (Exception e) {
				Log.error(this.getClass().getName()+"获取节点数据失败", e);
			}
		}
		return null;
	}
	public void closeServer(){
		curatorFramework.close();
	}
	@Override
	public List<URI> getChildNodes(String path) {
		path = path.startsWith("/")?path:"/"+path;
		// TODO Auto-generated method stub
		List<URI> list = new ArrayList<>();
		try {
			List<String> forPath = curatorFramework.getChildren().forPath(path);
			for(String paths :forPath){
				list.add(this.getData(paths));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public byte[] serialObject(Object obj){
		return factory.encode(obj);
	}
	public <T> T byteToobject(byte[] bytes,Class<T> tclass){
		return factory.decode(tclass,bytes);
	}

	@SuppressWarnings("unused")
	private  void setListenter(String path,CuratorFramework client,final NodeEventHandler eventHandler) throws Exception{  
		path = path.startsWith("/")?path:"/"+path;
		//设置节点的cache  
		@SuppressWarnings("resource")
		TreeCache treeCache = new TreeCache(client, path);  
		//设置监听器和处理过程  
		treeCache.getListenable().addListener(new TreeCacheListener() {  
			@Override  
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {  
				ChildData data = event.getData(); 
				if(data !=null){
					URI uri = null;
					if(data.getData()!=null && data.getData().length>0){
						uri = factory.decode(URI.class, data.getData());
					}

					String path = data.getPath();
					switch (event.getType()) {  
					case NODE_ADDED:  
						eventHandler.addNode(path.substring(1,path.length()),uri);
						logger.info("NODE_ADDED : ("+ path +")  数据:"+ uri.toString());  
						break;  
					case NODE_REMOVED: 
						eventHandler.removeNode(path.substring(1,path.length()));
						logger.info("NODE_REMOVED : "+ path);  
						break;  
					case NODE_UPDATED:  
						eventHandler.upateNode(path.substring(1,path.length()),uri);
						logger.info("NODE_UPDATED : ("+ path +")  数据:"+ uri.toString());  
						break;  
					default:  
						break;  
					}  
				}else{  
					logger.info( "data is null : "+ event.getType());  
				}  
			}  
		});  
		//开始监听  
		treeCache.start();  

	} 

	public  interface NodeEventHandler{
		void addNode(String className,URI uri);
		void removeNode(String className);
		void upateNode(String className,URI uri);
	}

	public void  initRegist(ConcurrentHashSet<String> registClassNames)throws Exception{

		Config.rpcPort = this.port;
		if(CollectionUtils.isNotEmpty(registClassNames)){ //注册服务
			InetAddress localHost = Inet4Address.getLocalHost();
			String hostAddress = localHost.getHostAddress();
			URI uri= new URI(null, hostAddress, this.port, null);
			for(String className:registClassNames){
				Class<?> loadClass = this.getClass().getClassLoader().loadClass(className);
				if(!loadClass.isInterface()){
					className = loadClass.getInterfaces()[0].getName();
				}
				registNode(className, uri, CreateMode.EPHEMERAL, false);
				logger.info( "success regist server {} :{} ", className,uri);  
			}
		}
	}
	public void initServer(ConcurrentHashSet<String> getClassNames) throws Exception{
		if(CollectionUtils.isNotEmpty(getClassNames)){ //获取服务信息
	
			for(String className:getClassNames){
				URI data = getData(className);
				if(data!=null){
					Config.uri.addCache(className,data);
					setListenter(className, this.curatorFramework,this.eventHandler);
				}
				else
					logger.info( "not regist server  : {}"+ className);  
			}
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		this.baseZookeeperClient = new ZookeeperClient();
		this.curatorFramework=baseZookeeperClient.init(path, zkAddress, certificate);
		this.eventHandler=new NodeEventHandler() {
			public void upateNode(String path,URI uri) {
				Config.uri.putIfAbsentCache(path, uri);
			}
			@Override
			public void removeNode(String path) {
				Config.uri.remove(path);
			}
			@Override
			public void addNode(String path,URI uri) {
				Config.uri.addCache(path, uri);;
			}
		};
	}
	@Override
	public void destroy() throws Exception {
		this.closeServer();
	}

	

}
