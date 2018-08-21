package com.common.distributedlock;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * 基础测试类，存放一些公操作方法
 *
 * @author lemontree
 * @version 2.0, 2017/8/9
 * @since 2.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public abstract class AbstractTest {

    protected String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
