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

    public static void main(String[] args) {
        createHzMap();

        long start = System.nanoTime();

//        logger.info("Test 1 with Distributed Map without locking");
//        testDatabaseCounter(RUN_10_THREADS, () -> HzCounterTester.test1(UP_TO_10_000_COUNTER));
//        logger.info("Test 2 with Distributed Map with Pessimistic Locking");
//        testDatabaseCounter(RUN_10_THREADS, () -> HzCounterTester.test2PessimisticLocking(UP_TO_10_000_COUNTER));
        logger.info("Test 3 with Distributed Map with Optimistic Locking");
        testDatabaseCounter(RUN_10_THREADS, () -> HzCounterTester.test3OptimisticLocking(UP_TO_10_000_COUNTER));

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