package pomtask.core.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void build_FromMap() {
        Map<String, Object> map = ImmutableMap.<String, Object>of(User.USER_NAME_KEY, "test", User.EMAIL_KEY, "test@test.cc");

        assertThat(new User(map), is(new User("test", "test@test.cc")));
    }

    @Test
    public void key() {
        String key = User.key("test@email.com");

        assertThat(key, is("user:test@email.com"));
    }

    @Test
    public void key_NoEmail() {
        thrown.expect(IllegalArgumentException.class);

        String key = User.key(null);

        assertThat(key, is(""));
    }
}
