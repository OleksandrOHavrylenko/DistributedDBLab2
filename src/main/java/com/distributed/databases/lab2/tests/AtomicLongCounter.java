package com.distributed.databases.lab2.tests;

import com.distributed.databases.lab2.HzConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksandr Havrylenko
 **/
public class AtomicLongCounter implements CounterTest{
    private final static Logger logger = LoggerFactory.getLogger(AtomicLongCounter.class);
    @Override
    public String getDescription() {
        return "Test 4 with AtomicLong Counter.";
    }

    @Override
    public void createData() {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);
            counter.set(0L);
            hzClient.shutdown();
        } catch (Exception e) {
            logger.error("Error during data creation.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void test(int maxCounterVal) {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);

            for (int i = 0; i < maxCounterVal; i++) {
                counter.incrementAndGet();
            }
            hzClient.shutdown();
        } catch (Exception e) {
            logger.error("Error happened during test invocation", e);
        }
    }

    @Override
    public long getResult() {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IAtomicLong counter = hzClient.getCPSubsystem().getAtomicLong(KEY);
            long l = counter.get();
            hzClient.shutdown();
            return l;
        } catch (Exception e) {
            logger.error("Error during getting result.", e);
            throw new RuntimeException(e);
        }
    }
}
