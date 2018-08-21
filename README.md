项目说明：
提供轻量级分布式锁，目前使用redis提供的set(lockKey, requestId, "NX", "EX", expireTime)功能以实现分布式锁。<br/>
支持使用其它锁，如使用zookeeper节点功能，以实现锁。
======================================================================<br/>
1、添加maven依赖(本地仓)
<dependency>
  <groupId>com.wtoip.pay</groupId>
	<artifactId>distributed-lock</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>


2、在需要添加分布式锁的对象上添加@Distributedlock注解，并指定锁key表达式，如按用户名加分布式锁，此处支持spring el表达式。
@Component
public class TestService {

    @Distributedlock("#param.userName")
    public void test(TestParam param) {
        System.out.println(param.getName()+"------------------------------------------");
    }
}

======================================================================<br/>
默认使用redis提供分布式锁，支持SPI方式扩展。<br/>
扩展示例：<br/>
在自己项目META-INF/services下添加com.common.distributedlock.lock.IDistributedLock文件，并在文件中指定扩展类。
如：com.common.distributedlock.lock.TestDistributedLockImpl
