package com.it.netty.rpc.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.zookeeper.base.BaseZookeeperClient;

public class ZookeeperClient  implements BaseZookeeperClient{

	@Override
	public CuratorFramework init(String path,String connectString,final Certificate certificate) {
		// TODO Auto-generated method stub
		ACLProvider aclProvider = new ACLProvider() {  
            private List<ACL> acl ;  
            @Override  
            public List<ACL> getDefaultAcl() {  
                if(acl ==null){  
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;  
                    acl.clear();  
                    acl.add(new ACL(Perms.ALL, new Id("auth", certificate.toString()) ));  
                    this.acl = acl;  
                }  
                return acl;  
            }  
            @Override  
            public List<ACL> getAclForPath(String path) {  
                return acl;  
            }  
        };  
        String scheme = certificate.getScheme();  
        byte[] auth = certificate.toString().getBytes();  
        int connectionTimeoutMs = Const.ZOOKEEPER_CONNECTION_TIME_OUT;  
        String namespace = path;  
        CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).  
        authorization(scheme, auth).  
        connectionTimeoutMs(connectionTimeoutMs).  
        connectString(connectString).  
        namespace(namespace).  
        retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();  
        client.start();  
        return client;  
	}
		
}
