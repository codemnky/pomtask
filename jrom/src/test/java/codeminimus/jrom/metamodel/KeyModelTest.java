package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class KeyModelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final StringJedisConnection mockConnection = mock(StringJedisConnection.class);
    private final MetaModel model = new MetaModel("modelName", null, ImmutableList.<FieldModel>of());
    private Field keyField;

    @Before
    public void setUp() throws NoSuchFieldException {
        keyField = StringKeyedType.class.getDeclaredField("key");
    }

    @Test
    public void key_String() throws NoSuchFieldException {
        KeyModel keyModel = new KeyModel(model, keyField);

        String key = keyModel.key(new StringKeyedType("apple"));

        assertThat(key, is("modelName:apple"));
    }

    @Test
    public void fieldName() throws NoSuchFieldException {
        KeyModel keymodel = new KeyModel(model, keyField);

        assertThat(keymodel.fieldName(), is("key"));
    }

    @Test
    public void valueForUpdate() throws NoSuchFieldException {
        KeyModel model = new KeyModel(this.model, keyField);

        assertThat((String) model.update(new StringKeyedType("Test"), mockConnection), is("Test"));
    }

    @Test
    public void valueForUpdate_NullKeyValue() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        KeyModel model = new KeyModel(this.model, keyField);

        model.update(new StringKeyedType(null), mockConnection);
    }

    @Test
    public void valueForCreate() throws NoSuchFieldException {
        KeyModel model = new KeyModel(this.model, keyField);

        assertThat((String) model.create(new StringKeyedType("Test"), mockConnection), is("Test"));
    }

    @Test
    public void valueForCreate_NullKeyValue() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        KeyModel model = new KeyModel(this.model, keyField);

        model.create(new StringKeyedType(null), mockConnection);
    }

    @KeyValueModel
    public class StringKeyedType {
        @SuppressWarnings("UnusedDeclaration")
        @codeminimus.jrom.annotation.Key
        private String key;

        public StringKeyedType(String key) {
            this.key = key;
        }
    }
}
