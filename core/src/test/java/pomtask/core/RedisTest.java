package pomtask.core;

import org.junit.Test;
import org.springframework.data.redis.core.BoundValueOperations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RedisTest extends RedisTestBase {
    public static final String TEST_KEY = "testkey";

    @Test
    public void verifyRedis() {
        BoundValueOperations<String, String> operations = redisTemplate.boundValueOps(TEST_KEY);

        operations.set("help!");

        assertThat(operations.get(), is("help!"));
    }
}
