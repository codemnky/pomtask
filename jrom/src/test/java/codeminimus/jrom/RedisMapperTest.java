package codeminimus.jrom;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;

public class RedisMapperTest {
    private RedisMapper mapper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void buildMapper() {
        JedisConnection connection = new JedisConnection(new Jedis("localhost"));
        mapper = new RedisMapper(connection);
    }

    @Test
    public void nothing() {

    }
}
