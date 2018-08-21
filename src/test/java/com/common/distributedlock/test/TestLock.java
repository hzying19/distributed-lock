package com.common.distributedlock.test;

import com.common.distributedlock.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 15:01
 */
public class TestLock extends AbstractTest {
    @Autowired
    private TestService testService;

    @Test
    public void testLock() {
        TestParam testParam = new TestParam();
        testParam.setName("haha`~~");

        testService.test(testParam);
    }

    @Test
    public void testMutiLock() throws InterruptedException {
        TestParam testParam = new TestParam();
        testParam.setName("haha`~~");

        int count = 10;
        final CyclicBarrier barrier  = new CyclicBarrier(count);
        for (int i=0;i<count;i++){
            Thread thread1 = new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                System.out.println("所有线程到达同一时刻，开始并发执行..."+System.currentTimeMillis());

                testService.test(testParam);
            });
            thread1.start();
        }

        Thread.sleep(15*1000L);
    }
}
