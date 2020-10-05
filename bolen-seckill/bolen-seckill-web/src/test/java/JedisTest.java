import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bolen.seckill.common.util.RedisClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-jedis.xml","classpath:applicationContext-jdbc.xml","classpath:applicationContext.xml"})
public class JedisTest {
	@Autowired
	RedisClient redisClient;
	
	@Test
	public void test01(){
		redisClient.set("hello", "100");
		Integer decrese = redisClient.decrese("hello");
		System.out.println(decrese);
	}


}
