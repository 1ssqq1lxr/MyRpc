package com.it.netty.rpc.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZKConnector implements Watcher{
	 private static final Logger logger =Logger.getLogger(ZKConnector.class);
	    
	    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	    private ZooKeeper zk =null;
	    
	    /**
	     * 释放zookeeper连接
	     */
	    public void releaseConnection() { 
	        if (this.zk!=null) { 
	            try { 
	                this.zk.close(); 
	            } catch ( InterruptedException e ) { 
	                e.printStackTrace(); 
	            } 
	        } 
	    } 
	    
	    
	    /**
	     * 创建zookeeper的连接
	     * @param connectString zookeeper服务器地址列表
	     * @param sessionTimeout Session超时时间
	     */
	     public void createConnection(String connectString, int sessionTimeout) { 
	             //先释放zookeeper连接
	            this.releaseConnection(); 
	            try {
	                zk = new ZooKeeper( connectString, sessionTimeout, this); 
	                connectedSemaphore.await(); 
	            } catch ( InterruptedException e ) { 
	                logger.info( "连接创建失败，发生 InterruptedException"); 
	                e.printStackTrace(); 
	            } catch (IOException e ) { 
	                logger.info( "连接创建失败，发生 IOException" ); 
	                e.printStackTrace(); 
	            } 
	        } 
	    
	     /**
	      * 检查Znode是否为空
	      */
	     public boolean check(String zNode){
	        try {
	            return this.zk.exists(zNode, false).getDataLength()>0;
	        } catch (KeeperException | InterruptedException e) {
	            e.printStackTrace();
	        }
	        return false;
	     }
	     
	     /**
	      * 检查zNode是否存在
	      * 不为空 返回true
	      * 为空，则返回false 
	      */
	     public boolean exist(String zNode){
	        try {
	                Stat stat =this.zk.exists(zNode, false);
	                return stat==null?false:true;
	            } catch (KeeperException | InterruptedException e) {
	                e.printStackTrace();
	            }
	            return true;
	     }
	     
	     /**
	      * 读取zNode的数据
	      */
	     public String readData(String path){
	         try { 
	                if(this.zk.exists(path, false) == null){
	                    return "";
	                }
	                return new String( this.zk.getData(path, false, null)); 
	            } catch ( KeeperException e){ 
	                logger.info("读取数据失败，发生KeeperException，path: " + path); 
	                e.printStackTrace(); 
	                return ""; 
	            } catch ( InterruptedException e){ 
	                logger.info("读取数据失败，发生 InterruptedException，path: " + path); 
	                e.printStackTrace(); 
	                return ""; 
	            } 
	     }
	     
	     /**
	      * 更新zNode的数据
	      */
	     public boolean writeData(String path,String data){
	         try { 
	                if(this.zk.exists(path, false) == null){
	                    return createPersistNode(path,data);
	                }else{
	                    deleteNode(path);
	                    createPersistNode(path,data);
	                }
	            } catch ( KeeperException e ) { 
	                logger.info( "更新数据失败，发生KeeperException，path: " + path  ); 
	                e.printStackTrace(); 
	            } catch ( InterruptedException e ) { 
	                logger.info( "更新数据失败，发生 InterruptedException，path: " + path  ); 
	                e.printStackTrace(); 
	            } 
	            return false; 
	     }
	     
	     /**
	      * 获取子节点数据
	      */
	     public List<String> getChildren(String node){
	         try {
	            List<String> subNodes = this.zk.getChildren(node, false);
	            return subNodes;
	        } catch (KeeperException | InterruptedException e) {
	            e.printStackTrace();
	        }
	        return null; 
	     }
	     
	    /**
	     * 创建持久化节点
	     * @param path 节点path
	     * @param data    初始数据内容
	     * @return
	     */
	    public  boolean createPersistNode(String path, String data) {
	         try { 
	                String createpath =this.zk.create(path,data.getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT ); 
	                logger.info("节点创建成功, Path: " 
	                        + createpath
	                        + ", content: " + data );
	                return true;
	            } catch ( KeeperException e ) { 
	                logger.info( "节点创建失败，发生KeeperException" ); 
	                e.printStackTrace(); 
	            } catch ( InterruptedException e ) { 
	                logger.info( "节点创建失败，发生 InterruptedException" ); 
	                e.printStackTrace(); 
	            } 
	            return false; 
	    }
	    
	    /**
	     * 创建短暂序列化节点
	     * @param path 节点path
	     * @param data 初始数据内容
	     * @param needWatch
	     * @return
	     */
	    public  String createEsquentialNode(String path, String data) {
	         String esquentialNode = null;
	         try { 
	                 esquentialNode =this.zk.create(path,data.getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL); 
	                logger.info("节点创建成功, Path: " 
	                        + esquentialNode
	                        + ", content: " + data );
	                return esquentialNode;
	            } catch ( KeeperException e ) { 
	                logger.info( "节点创建失败，发生KeeperException" ); 
	                e.printStackTrace(); 
	            } catch ( InterruptedException e ) { 
	                logger.info( "节点创建失败，发生 InterruptedException" ); 
	                e.printStackTrace(); 
	            } 
	            return null; 
	    }

	    
	    /**
	     * 删除节点
	     */
	     public void deleteNode(String path) { 
	        try { 
	            if(this.zk.exists(path, false) == null){
	                logger.info("该节点不存在！不做任何操作" ); 
	            }else{
	                this.zk.delete(path, -1); 
	                logger.info("删除节点成功，path："+ path); 
	            }
	        } catch ( KeeperException e ) { 
	            logger.info("删除节点失败，发生KeeperException，path: " + path); 
	            e.printStackTrace(); 
	        } catch ( InterruptedException e ) { 
	            logger.info("删除节点失败，发生 InterruptedException，path: " + path); 
	            e.printStackTrace(); 
	        } 
	    } 
	    
	    
	    public void process(WatchedEvent event) {
	        logger.info( "收到事件通知：" + event.getState() +"\n"  ); 
	        if ( KeeperState.SyncConnected == event.getState() ) { 
	            connectedSemaphore.countDown(); 
	        } 
	    }
	    
	    
	    public static void main(String[] args) {
	    	ZKConnector zkConnector = new ZKConnector();
			zkConnector.createConnection("127.0.0.1:2181", 5000);
			zkConnector.createEsquentialNode("/test","123");
		}
}
