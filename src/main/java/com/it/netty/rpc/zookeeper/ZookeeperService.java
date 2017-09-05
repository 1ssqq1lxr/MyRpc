package com.it.netty.rpc.zookeeper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.framework.HandlerService;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.protocol.ProtocolFactory;
import com.it.netty.rpc.protocol.jackson.JacksonProtocolFactory;
import com.it.netty.rpc.zookeeper.base.BaseZookeeperClient;
import com.it.netty.rpc.zookeeper.base.BaseZookeeperService;

public class ZookeeperService implements BaseZookeeperService ,InitializingBean,DisposableBean {
	protected static final Logger log = LoggerFactory.getLogger(ZookeeperService.class.getSimpleName());
	private String DEV_S="/";
	private  String NODE_NAME="node_";
	public  static Cache<String,RemoteAddress[]>  cache_uri = new CacheFactory<>(); // channel
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
		path = buildPath(path);
		try {
			if(is){
				curatorFramework.create().creatingParentsIfNeeded().withMode(mode).forPath(path, serialObject(uri));
			}
			else{
				curatorFramework.create().withMode(mode).forPath(path, serialObject(uri));
			}
		} catch (Exception e) {
			log.error(this.getClass().getName()+"创建节点失败", e);
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
			log.error(this.getClass().getName()+"删除节点失败", e);
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
			log.error(this.getClass().getName()+"检查节点是否存在失败", e);
		}
		return false;

	}
	@Override
	public URI getData(String path) {
		if(exists(path)){
			byte[] forPath;
			try {
				forPath = curatorFramework.getData().forPath(path);
				if(forPath!=null){
					return factory.decode(URI.class, forPath);
				}
			} catch (Exception e) {
				log.error(this.getClass().getName()+"获取节点数据失败", e);
			}
		}
		return null;
	}
	public void closeServer(){
		curatorFramework.close();
	}
	@Override
	public RemoteAddress[] getChildNodes(String path) {
		path = path.startsWith(DEV_S)?path:DEV_S+path;
		// TODO Auto-generated method stub
		try {
			List<String> forPath = curatorFramework.getChildren().forPath(path);
			RemoteAddress[] addresses = new RemoteAddress[forPath.size()];
			StringBuilder sb = new StringBuilder();
			int num=0;
			for(String paths :forPath){
				String final_path = sb.append(path).append(DEV_S).append(paths).toString();
				URI data = this.getData(final_path);
				if(data!=null){
					RemoteAddress address = new RemoteAddress(paths,data);
					addresses[num]=address;
					num++;
				}
				sb.delete(0, sb.length());
			}
			return addresses;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public byte[] serialObject(Object obj){
		return factory.encode(obj);
	}
	public <T> T byteToobject(byte[] bytes,Class<T> tclass){
		return factory.decode(tclass,bytes);
	}

	public void  setPathChildrenListenter(String path){
		path = path.startsWith(DEV_S)?path:DEV_S+path;
		PathChildrenCache childrenCache = new PathChildrenCache(this.curatorFramework, path, true);  
		PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {  
			@Override  
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {  
				ChildData data = event.getData(); 
				if(data !=null){
					URI uri = null;
					if(data.getData()!=null && data.getData().length>0){
						uri = factory.decode(URI.class, data.getData());
					}

					String path = data.getPath(); 
					switch (event.getType()) {  
					case CHILD_ADDED:  
						eventHandler.addNode(path,uri);
						logger.info("NODE_ADDED : ("+ path +")  数据:"+ uri.toString());  
						break;  
					case CHILD_REMOVED:  
						eventHandler.removeNode(path);
						logger.info("NODE_REMOVED : "+ path);  
						break;  
					case CHILD_UPDATED:  
						eventHandler.upateNode(path,uri);
						logger.info("NODE_UPDATED : ("+ path +")  数据:"+ uri.toString());  
						break;   
					default:  
						break;  
					}  
				}
			}  
		};  
		childrenCache.getListenable().addListener(childrenCacheListener);  
		System.out.println("Register zk watcher successfully!");  
		try {
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}  
	}
	public  void setTreeListenter(String path) throws Exception{  
		path = path.startsWith(DEV_S)?path:DEV_S+path;
		//设置节点的cache 
		@SuppressWarnings("resource")
		TreeCache treeCache = new TreeCache(this.curatorFramework, path);  
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
						eventHandler.addNode(path,uri);
						logger.info("NODE_ADDED : ("+ path +")  数据:"+ uri.toString());  
						break;  
					case NODE_REMOVED: 
						eventHandler.removeNode(path);
						logger.info("NODE_REMOVED : "+ path);  
						break;  
					case NODE_UPDATED:  
						eventHandler.upateNode(path,uri);
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
		if(CollectionUtils.isNotEmpty(registClassNames)){ //注册服务
			InetAddress localHost = Inet4Address.getLocalHost();
			String hostAddress = localHost.getHostAddress();

			for(String className:registClassNames){
				Long timeout = HandlerService.timeouts.get(className);
				Class<?> loadClass = this.getClass().getClassLoader().loadClass(className);
				if(!loadClass.isInterface()){
					className = loadClass.getInterfaces()[0].getName();
				}
				URI uri= new URI(null, hostAddress, this.port,null,timeout);
				registNode(className, uri, CreateMode.EPHEMERAL_SEQUENTIAL, true);
				logger.info( "success regist server {} :{} ", className,uri);  
			}
		}
	}
	public void initServer(ConcurrentHashSet<String> getClassNames) throws Exception{
		if(CollectionUtils.isNotEmpty(getClassNames)){ //获取服务信息
			for(String className:getClassNames){
				RemoteAddress[] data = getChildNodes(className);
				if(data!=null){
					cache_uri.addCache(className,data);
					setPathChildrenListenter(className);
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
				if(uri!=null){
					String matchPath = matchPath(path);
					RemoteAddress[] addresses = cache_uri.getCache(matchPath);
					if(addresses!=null){
						for(int i=0;i<addresses.length;i++){
							if(path.contains(addresses[i].getNodeName())){
								addresses[i].setUri(uri);
								break;
							}
						}
						cache_uri.addCache(matchPath, addresses);
					}
				}
			}
			@Override
			public void removeNode(String path) {
				String matchPath = matchPath(path);
				RemoteAddress[] addresses  = cache_uri.getCache(matchPath);
				if(addresses!=null ){
					RemoteAddress[] addresses_new =new RemoteAddress[addresses.length-1]; 
					int num =0;
					for(int i=0;i<addresses.length;i++){
						if(!path.contains(addresses[i].getNodeName())){
							addresses_new[num]=addresses[i];
							num++;
						}
					}
					cache_uri.addCache(matchPath, addresses_new);
				}
			}
			@Override
			public void addNode(String path,URI uri) {
				String matchPath = matchPath(path);
				String final_path=path.substring(path.lastIndexOf('/')+1,path.length());
				RemoteAddress[] addresses  = cache_uri.getCache(matchPath);
				if(uri!=null&&!isExists(addresses,path)){
					RemoteAddress[] addresses_new =new RemoteAddress[addresses.length+1];
					for(int i=0;i<addresses.length;i++){
						addresses_new[i]=addresses[i];
					}
					RemoteAddress address = new RemoteAddress(final_path,uri);
					addresses_new[addresses.length]=address;
					cache_uri.addCache(matchPath, addresses_new);
				}
			}
		};
	}
	@Override
	public void destroy() throws Exception {
		this.closeServer();
	}
	private String buildPath(String path){
		path = path.startsWith(DEV_S)?path:DEV_S+path;
		StringBuilder builder = new StringBuilder(path);
		builder.append(DEV_S).append(NODE_NAME);
		return builder.toString();

	}
	private String matchPath(String path){
		String rgex = "/(.*?)/";
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
		Matcher m = pattern.matcher(path);  
		if(m.find()){
			return m.group(1);
		}
		return null;
	}
	
	public boolean isExists(RemoteAddress[] addresses,String path){
		boolean flag=false;
		for(RemoteAddress r:addresses){
			if(path.contains(r.getNodeName())){
				flag=true;
				break;
			}
		}
		return flag;
	}

}
