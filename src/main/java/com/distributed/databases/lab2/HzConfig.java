package com.distributed.databases.lab2;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author Oleksandr Havrylenko
 **/
public class HzConfig {

    public static final String CLUSTER_NAME = "hazelcast-lab2";
    public static final String HZ_CLUSTER_IP = "192.168.1.54";

    private static ClientConfig getClientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(CLUSTER_NAME);
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress( HZ_CLUSTER_IP);
        networkConfig.setRedoOperation(true);

        return clientConfig;
    }

    public static HazelcastInstance  getClient() {
        return HazelcastClient.newHazelcastClient(getClientConfig());
    }
}
