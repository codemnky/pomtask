package pomtask.core.config;

import com.google.common.base.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;

@Configuration
public class RedisConfig {
    @Inject
    private Environment env;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redis = new JedisConnectionFactory();
        redis.setHostName(env.getProperty("redis.host"));
        redis.setPort(env.getProperty("redis.port", Integer.class));
        redis.setDatabase(env.getProperty("redis.database", Integer.class));

        String redisPassword = env.getProperty("redis.password");
        if (!Strings.isNullOrEmpty(redisPassword)) {
            redis.setPassword(redisPassword);
        }
        return redis;
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }
}
