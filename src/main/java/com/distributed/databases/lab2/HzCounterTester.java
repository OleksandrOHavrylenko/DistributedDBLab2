package com.distributed.databases.lab2;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
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
        hzClient.shutdown();
    }

    public static void test2PessimisticLocking(final int maxCounterVal) {
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
        hzClient.shutdown();
    }

    public static void test3OptimisticLocking(final int maxCounterVal) {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);

        for (int i = 0; i < maxCounterVal; i++) {
            for(;;) {
                int oldVal = hzMap.get(KEY);
                int newVal = oldVal + 1;
                if (hzMap.replace(KEY, oldVal, newVal)) {
                    break;
                }
            }
        }
        hzClient.shutdown();
    }

    public static void test4AtomicLong(final int maxCounterVal) {
        HazelcastInstance hzClient = HzConfig.getClient();
        IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);

        for (int i = 0; i < maxCounterVal; i++) {
            counter.incrementAndGet();
        }
        hzClient.shutdown();
    }

    private static void incrementCounter(IMap<String, Integer> hzMap) {
        int value = hzMap.get(KEY);
        value++;
        hzMap.put(KEY, value);
    }

    public static int getFinalCounter() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
        int i = hzMap.get(KEY);
        hzClient.shutdown();
        return i;
    }

    public static long getCounter() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);
        long l = counter.get();
        hzClient.shutdown();
        return l;
    }


    public static void createHzMap() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);
        hzMap.put(KEY, 0);
    }

    public static void createHzAtomicLong() {
        HazelcastInstance hzClient = HzConfig.getClient();
        IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);
        counter.set(0L);
        hzClient.shutdown();
    }

}
