package com.distributed.databases.lab2.tests;

import com.distributed.databases.lab2.HzConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksandr Havrylenko
 **/
public class PessimisticLockingCounter implements CounterTest {
    private final static Logger logger = LoggerFactory.getLogger(PessimisticLockingCounter.class);

    @Override
    public String getDescription() {
        return "Test 2 with Distributed Map with Pessimistic Locking";
    }

    @Override
    public void test(int maxCounterVal) {
        try {
            HazelcastInstance hzClient = HzConfig.getClient();
            IMap<String, Integer> hzMap = hzClient.getMap(MY_COUNTER_DISTRIBUTED_MAP);

            for (int i = 0; i < maxCounterVal; i++) {
                hzMap.lock(KEY);
                try {
                    int value = hzMap.get(KEY);
                    value++;
                    hzMap.put(KEY, value);
                } finally {
                    hzMap.unlock(KEY);
                }
            }
            hzClient.shutdown();
        } catch (Exception e) {
            logger.error("Error happened during test invocation", e);
        }

    }
}
