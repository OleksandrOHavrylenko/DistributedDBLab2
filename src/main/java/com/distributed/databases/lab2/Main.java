package com.distributed.databases.lab2;

import com.distributed.databases.lab2.tests.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Havrylenko
 **/
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final int THREADS_10 = 10;
    public static final int COUNTER_10000 = 10000;

    public static void main(String[] args) {
        final CounterTest counterTest = getTest(4);
        counterTest.createData();

        long start = System.nanoTime();

        testDatabaseCounter(THREADS_10, () -> counterTest.test(COUNTER_10000));

        long finish = System.nanoTime();

        logger.info(counterTest.getDescription());
        logger.info("Final result counter = {} per Duration: {} ms;", counterTest.getResult(), (finish - start) / 1_000_000.0);
    }

    private static CounterTest getTest(final int testNumber) {
        return switch (testNumber) {
            case 1 -> new NotLockingCounter();
            case 2 -> new PessimisticLockingCounter();
            case 3 -> new OptimisticLockingCounter();
            case 4 -> new AtomicLongCounter();
            default -> throw new IllegalArgumentException("Invalid testNumber: " + testNumber);
        };
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