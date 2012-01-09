package pomtask.core.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.test.context.ContextConfiguration;
import pomtask.core.RedisTestBase;
import pomtask.core.config.ServiceConfig;
import pomtask.core.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {ServiceConfig.class})
public class RedisUserServiceTest extends RedisTestBase {
    public static final String GOOD_EMAIL = "happy@email.com";
    public static final String GOOD_USER_NAME = "happy";
    @Autowired
    private RedisUserService userService;

    @Before
    public void createData() {
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps("user:" + GOOD_EMAIL);
        operations.put("userName", GOOD_USER_NAME);
        operations.put("email", GOOD_EMAIL);
    }

    @Test
    public void findByEmail() {
        User user = userService.findByEmail(GOOD_EMAIL);

        assertThat(user, is(new User(GOOD_USER_NAME, GOOD_EMAIL)));
    }

    @Test
    public void findByEmail_BadEmail() throws Exception {
        User user = userService.findByEmail("bad@email.com");

        assertThat(user, is(User.INVALID));
    }

}
