package com.it.netty.rpc.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.RetryNTimes;

public class CuratorWatcherTest {
	 /** Zookeeper info */
    private static final String ZK_ADDRESS = "192.168.1.100:2181";
    private static final String ZK_PATH = "/zktest";

    public static void main(String[] args) throws Exception {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // 2.Register watcher
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                true    // if cache data
        );
//        watcher.getListenable().addListener((client1, event) -> {
//            ChildData data = event.getData();
//            if (data == null) {
//                System.out.println("No data in event[" + event + "]");
//            } else {
//                System.out.println("Receive event: "
//                        + "type=[" + event.getType() + "]"
//                        + ", path=[" + data.getPath() + "]"
//                        + ", data=[" + new String(data.getData()) + "]"
//                        + ", stat=[" + data.getStat() + "]");
//            }
//        });
        watcher.start(StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }

}
