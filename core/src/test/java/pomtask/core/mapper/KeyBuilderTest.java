package pomtask.core.mapper;

import org.junit.Test;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.KeyValueModel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KeyBuilderTest {
    @Test
    public void key_String() {
        KeyBuilder<StringKeyedType> builder = new KeyBuilder<StringKeyedType>(StringKeyedType.class);

        String key = builder.key(new StringKeyedType("apple"));

        assertThat(key, is("stringKeyedType:apple"));
    }

    @Test
    public void key_ModelNameSet() {
        KeyBuilder<NamedStringKeyedType> builder = new KeyBuilder<NamedStringKeyedType>(NamedStringKeyedType.class);

        String key = builder.key(new NamedStringKeyedType("apple"));

        assertThat(key, is("happy:apple"));
    }

    @KeyValueModel
    public class StringKeyedType {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String key;

        public StringKeyedType(String key) {
            this.key = key;
        }
    }

    @KeyValueModel(name = "happy")
    public class NamedStringKeyedType {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String key;

        public NamedStringKeyedType(String key) {
            this.key = key;
        }
    }
}
