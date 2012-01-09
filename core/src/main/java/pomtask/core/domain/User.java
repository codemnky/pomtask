package pomtask.core.domain;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Map;

public class User {
    public static final User INVALID = new User("NO_USER_NAME", "NO_USER_EMAIL");

    @VisibleForTesting
    static final String USER_NAME_KEY = "userName";
    @VisibleForTesting
    static final String EMAIL_KEY = "email";

    private final String userName;
    private final String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User(Map<String, Object> entries) {
        this((String) entries.get(USER_NAME_KEY), (String) entries.get(EMAIL_KEY));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static String key(String email) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(email), "Email for key must not be null or empty.");
        return String.format("user:%s", email);
    }
}
