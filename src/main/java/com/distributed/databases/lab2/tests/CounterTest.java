package com.distributed.databases.lab2.tests;

import com.distributed.databases.lab2.HzConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksandr Havrylenko
 **/
public interface CounterTest {
    Logger logger = LoggerFactory.getLogger(CounterTest.class);
    String MY_COUNTER_DISTRIBUTED_MAP = "counter-distributed-map";
    String KEY = "counter";

    String getDescription();

    default void createData() {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
            hzMap.put(KEY, 0);
            hzClient.shutdown();
        } catch (Exception e) {
            logger.error("Error during data creation.", e);
            throw new RuntimeException(e);
        }
    }

    void test(final int maxCounterVal);

    default long getResult() {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
            int i = hzMap.get(KEY);
            hzClient.shutdown();
            return i;
        } catch (Exception e) {
            logger.error("Error during getting result.", e);
            throw new RuntimeException(e);
        }
    }
}
