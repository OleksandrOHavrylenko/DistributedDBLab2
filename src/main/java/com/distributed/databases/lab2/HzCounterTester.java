package com.distributed.databases.lab2;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * @author Oleksandr Havrylenko
 **/
public class HzCounterTester {


    public static final String MY_COUNTER_DISTRIBUTED_MAP = "counter-distributed-map";
    public static final String KEY = "counter";

    public static void test1(final int maxCounterVal) {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);

        for (int i = 0; i < maxCounterVal; i++) {
            incrementCounter(hzMap);
        }
    }

    public static void test2(final int maxCounterVal) {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);

        for (int i = 0; i < maxCounterVal; i++) {
            hzMap.lock(KEY);
            try {
                incrementCounter(hzMap);
            } finally {
               hzMap.unlock(KEY);
            }
        }
    }

    private static void incrementCounter(IMap<String, Integer> hzMap) {
        int value = hzMap.get(KEY);
        value++;
        hzMap.put(KEY, value);
    }

    public static int getFinalCounter() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
        return hzMap.get(KEY);
    }


    public static void createHzMap() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
        hzMap.put(KEY, 0);
    }

}
