package com.common.distributedlock;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 主启动
 *
 * @author lemontree
 * @version 2.0, 2017/8/9
 * @since 2.0
 */
@SpringBootApplication(scanBasePackages = {"com.common.distributedlock"})
@ImportResource("classpath*:META-INF/dubbo*.xml")
public class ApplicationTest {
}