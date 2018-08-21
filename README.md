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

======================================================================\n
默认使用redis提供分布式锁，支持SPI方式扩展。
扩展示例：
在自己项目META-INF/services下添加com.common.distributedlock.lock.IDistributedLock文件，并在文件中指定扩展类。
如：com.common.distributedlock.lock.TestDistributedLockImpl
