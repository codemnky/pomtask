package pomtask.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pomtask.core.domain.User;

import java.util.Map;

@Service
public class RedisUserService implements UserService {
    @Autowired
    private StringRedisTemplate template;

    @Override
    public User findByEmail(String email) {
        HashOperations<String, String, Object> operations = template.opsForHash();
        Map<String, Object> entries = operations.entries(User.key(email));
        User user = User.INVALID;
        if (!entries.isEmpty()) {
            user = new User(entries);
        }
        return user;
    }
}
