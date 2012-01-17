package pomtask.core.mapper.metamodel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.Sequence;
import pomtask.core.mapper.exception.KeyValueMappingException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MetaModelBuilderTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addProperty_Key() throws NoSuchFieldException {
        MetaModelBuilder builder = new MetaModelBuilder();
        builder.modelName = "test";

        builder.addProperty(DummyModel.class.getDeclaredField("key"));

        assertThat(builder.key, instanceOf(KeyModel.class));
    }

    @Test
    public void addProperty_Key_FailsWhenKeyAlreadySet() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModelBuilder builder = new MetaModelBuilder();
        builder.key = mock(KeyModel.class);

        builder.addProperty(DummyModel.class.getDeclaredField("key"));
    }

    @Test
    public void addProperty_SequencedKey() throws NoSuchFieldException {
        MetaModelBuilder builder = new MetaModelBuilder();
        builder.modelName = "test";

        builder.addProperty(DummyModel.class.getDeclaredField("sequencedKey"));

        assertThat(builder.key, instanceOf(SequencedKeyModel.class));
    }

    //TODO:         if (keyField == null) {    throw new KeyValueMappingException(String.format("No KeyModel specified for metamodel (%s)", modelClass.getName())); }


    public class DummyModel {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String key;

        @Key
        @Sequence
        private int sequencedKey;
    }
}
