package pomtask.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class EnvironmentConfig {
    @Configuration
    @Profile("dev")
    @PropertySource("classpath:/dev-redis.properties")
    static class Dev {
    }

    @Configuration
    @Profile("test")
    @PropertySource("classpath:/test-redis.properties")
    static class Test {
    }

    @Configuration
    @Profile("prod")
    @PropertySource("classpath:/prod-redis.properties")
    static class Prod {
    }
}
