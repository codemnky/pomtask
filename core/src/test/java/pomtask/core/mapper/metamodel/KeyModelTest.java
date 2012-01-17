package pomtask.core.mapper.metamodel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.annotation.KeyValueModel;
import pomtask.core.mapper.exception.KeyValueMappingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class KeyModelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private final JedisConnection mockConnection = mock(JedisConnection.class);

    @Test
    public void key_String() throws NoSuchFieldException {
        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        String key = model.key(new StringKeyedType("apple"));

        assertThat(key, is("modelName:apple"));
    }

    @Test
    public void fieldName() throws NoSuchFieldException {
        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        assertThat(model.fieldName(), is("key"));
    }

    @Test
    public void valueForUpdate() throws NoSuchFieldException {
        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        assertThat((String) model.valueForUpdate(new StringKeyedType("Test"), mockConnection), is("Test"));
    }

    @Test
    public void valueForUpdate_NullKeyValue() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        model.valueForUpdate(new StringKeyedType(null), mockConnection);
    }

    @Test
    public void valueForCreate() throws NoSuchFieldException {
        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        assertThat((String) model.valueForCreate(new StringKeyedType("Test"), mockConnection), is("Test"));
    }

    @Test
    public void valueForCreate_NullKeyValue() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        KeyModel model = new KeyModel("modelName", StringKeyedType.class.getDeclaredField("key"));

        model.valueForCreate(new StringKeyedType(null), mockConnection);
    }

    @KeyValueModel
    public class StringKeyedType {
        @SuppressWarnings("UnusedDeclaration")
        @pomtask.core.mapper.annotation.Key
        private String key;

        public StringKeyedType(String key) {
            this.key = key;
        }
    }
}
