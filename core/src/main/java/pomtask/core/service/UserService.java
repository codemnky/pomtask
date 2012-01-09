package pomtask.core.service;

import pomtask.core.domain.User;

public interface UserService {
    User findByEmail(String email);
}
