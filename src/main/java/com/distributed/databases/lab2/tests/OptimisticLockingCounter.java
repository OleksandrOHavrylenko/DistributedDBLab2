package com.distributed.databases.lab2.tests;

import com.distributed.databases.lab2.HzConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksandr Havrylenko
 **/
public class OptimisticLockingCounter implements CounterTest{
    private final static Logger logger = LoggerFactory.getLogger(OptimisticLockingCounter.class);
    @Override
    public String getDescription() {
        return "Test 3 with Distributed Map with Optimistic Locking.";
    }

    @Override
    public void test(int maxCounterVal) {
        try {
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
        } catch (Exception e) {
            logger.error("Error happened during test invocation", e);
        }
    }
}
