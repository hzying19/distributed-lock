package com.common.distributedlock.test;

import com.common.distributedlock.annot.Distributedlock;
import org.springframework.stereotype.Component;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 14:36
 */
@Component
public class TestService {

    @Distributedlock("#param.name")
    public void test(TestParam param) {
        System.out.println(param.getName()+"------------------------------------------");
    }

}
