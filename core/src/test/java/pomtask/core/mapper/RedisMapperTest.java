package pomtask.core.mapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pomtask.core.mapper.exception.KeyValueMappingException;

public class RedisMapperTest {
    private RedisMapper mapper = new RedisMapper();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void save_NoModelAnnotation() {
        thrown.expect(KeyValueMappingException.class);

        mapper.save(new NoModelKey());
    }

    public class NoModelKey {
        @SuppressWarnings("UnusedDeclaration")
        private String varString;
    }
}
