package com.distributed.databases.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.distributed.databases.lab2.HzCounterTester.createHzMap;
import static com.distributed.databases.lab2.HzCounterTester.getFinalCounter;

/**
 * @author Oleksandr Havrylenko
 **/
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final int RUN_10_THREADS = 10;
    public static final int UP_TO_10_000_COUNTER = 10_000;
//    public static void main(String[] args) {
//
////        Config hazelcastConfig = new Config();
////        hazelcastConfig.setClusterName("hazelcast_lab2");
//        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.setClusterName("hello-world");
//        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
//        networkConfig.addAddress(/*"127.0.0.1",*/ "192.168.1.54");
//
//        HazelcastInstance hz1 = HazelcastClient.newHazelcastClient(clientConfig);
////        HazelcastInstance hz1 = Hazelcast.newHazelcastInstance(hazelcastConfig);
////        HazelcastInstance hz2 = Hazelcast.newHazelcastInstance(hazelcastConfig);
////        HazelcastInstance hz3 = Hazelcast.newHazelcastInstance(hazelcastConfig);
//
//        IMap<String, String> hzMap = hz1.getMap("my-distributed-map");
//        hzMap.clear();
////        hzMap.put("1", "John");
////        hzMap.put("2", "Mary");
////        hzMap.put("3", "Jane");
//
//
////        System.out.println(hzMap.get("1"));
////        System.out.println(hzMap.get("2"));
////        System.out.println(hzMap.get("3"));
//    }

    public static void main(String[] args) {
        createHzMap();

        long start = System.nanoTime();

        logger.info("Test 1 Lost-Update with default Transaction Isolation level");
        testDatabaseCounter(1, () -> HzCounterTester.test1(UP_TO_10_000_COUNTER));

        long finish = System.nanoTime();
        int finalCounter = getFinalCounter();
        logger.info("Final result counter = {} per Duration: {} ms;", finalCounter, (finish - start) / 1_000_000.0);
    }

    private static void testDatabaseCounter(final int threadsNum, Runnable task) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsNum; i++) {
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for thread completion.", e);
            }
        });
    }
}