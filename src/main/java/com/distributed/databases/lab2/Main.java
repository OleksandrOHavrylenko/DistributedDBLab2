package com.distributed.databases.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.distributed.databases.lab2.HzCounterTester.*;

/**
 * @author Oleksandr Havrylenko
 **/
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final int THREADS_10 = 10;
    public static final int COUNTER_10000 = 10000;

    public static void main(String[] args) {
//        createHzMap();
        createHzAtomicLong();

        long start = System.nanoTime();

//        logger.info("Test 1 with Distributed Map without locking");
//        testDatabaseCounter(THREADS_10, () -> HzCounterTester.test1(COUNTER_10000));
//        logger.info("Test 2 with Distributed Map with Pessimistic Locking");
//        testDatabaseCounter(THREADS_10, () -> HzCounterTester.test2PessimisticLocking(COUNTER_10000));
//        logger.info("Test 3 with Distributed Map with Optimistic Locking");
//        testDatabaseCounter(THREADS_10, () -> HzCounterTester.test3OptimisticLocking(COUNTER_10000));
        logger.info("Test 4 with Atomic Long");
        testDatabaseCounter(THREADS_10, () -> HzCounterTester.test4AtomicLong(COUNTER_10000));

        long finish = System.nanoTime();
        logger.info("Final result counter = {} per Duration: {} ms;", getCounter(), (finish - start) / 1_000_000.0);
//        logger.info("Final result counter = {} per Duration: {} ms;", getFinalCounter(), (finish - start) / 1_000_000.0);
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