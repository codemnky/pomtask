package pomtask.core;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pomtask.core.config.EnvironmentConfig;
import pomtask.core.config.RedisConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {EnvironmentConfig.class, RedisConfig.class})
public abstract class RedisTestBase {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    @After
    public void clearDatabase() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
