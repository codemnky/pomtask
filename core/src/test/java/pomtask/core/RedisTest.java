package pomtask.core;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pomtask.core.config.EnvironmentConfig;
import pomtask.core.config.RedisConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {EnvironmentConfig.class, RedisConfig.class})
public class RedisTest {
    public static final String TEST_KEY = "testkey";
    @Autowired
    StringRedisTemplate redisTemplate;

    @After
    public void clearDatabase() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    public void verifyRedis() {
        BoundValueOperations<String, String> operations = redisTemplate.boundValueOps(TEST_KEY);

        operations.set("help!");

        assertThat(operations.get(), is("help!"));
    }
}
