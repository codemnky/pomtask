package codeminimus.jrom.metamodel;

import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MetaModelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private final MetaModel builder = new MetaModel();


    @Before
    public void setUp() {
        builder.modelName = "test";
    }

    @Test
    public void addField_Key() throws NoSuchFieldException {
        builder.addField(DummyModel.class.getDeclaredField("key"));

        assertThat(builder.key, instanceOf(KeyModel.class));
    }

    @Test
    public void addField_Key_FailsWhenKeyAlreadySet() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModel builder = new MetaModel();
        builder.key = mock(KeyModel.class);

        builder.addField(DummyModel.class.getDeclaredField("key"));
    }

    @Test
    public void addField_SequencedKey() throws NoSuchFieldException {
        builder.addField(DummyModel.class.getDeclaredField("sequencedKey"));

        assertThat(builder.key, instanceOf(SequencedKeyModel.class));
    }

    @Test
    public void addField_UnannotatedIsAttribute() throws NoSuchFieldException {
        Field unannotatedField = DummyModel.class.getDeclaredField("stringField");

        builder.addField(unannotatedField);

        assertThat(builder.fields.get(0), instanceOf(AttributeModel.class));
    }

    @Test
    public void addField_Transient() throws NoSuchFieldException {
        Field transientField = DummyModel.class.getDeclaredField("transientField");

        builder.addField(transientField);

        assertThat(builder.fields.isEmpty(), is(true));
    }

    //TODO:         if (field == null) {    throw new KeyValueMappingException(String.format("No KeyModel specified for metamodel (%s)", modelClass.getName())); }


    public class DummyModel {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String key;

        @Key
        @Sequence
        private int sequencedKey;

        private String stringField;

        @Unmapped
        private long transientField;
    }
}
