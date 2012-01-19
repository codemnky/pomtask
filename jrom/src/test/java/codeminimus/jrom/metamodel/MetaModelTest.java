package codeminimus.jrom.metamodel;

import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.collect.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MetaModelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private final MetaModel builder = new MetaModel("modelName", null, ImmutableList.<FieldModel>of());

    @Test
    public void construct() {
        MetaModel metaModel = new MetaModel(BasicModel.class);

        assertThat(metaModel.getModelName(), is("basicModel"));
        assertThat(metaModel.key.fieldName(), is("key"));
        assertThat(metaModel.fields.get(0).fieldName(), is("field"));
    }

    @Test
    public void construct_TwoKeys() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModel builder = new MetaModel(TwoKeyModel.class);

        builder.buildFieldModel(DummyModel.class.getDeclaredField("key"));
    }

    @Test
    public void construct_NoKey() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModel builder = new MetaModel(NoKeyModel.class);

        builder.buildFieldModel(DummyModel.class.getDeclaredField("key"));
    }

    @Test
    public void buildFieldModel_Key() throws NoSuchFieldException {
        FieldModel fieldModel = builder.buildFieldModel(DummyModel.class.getDeclaredField("key"));

        assertThat(fieldModel, instanceOf(KeyModel.class));
    }

    @Test
    public void buildFieldModel_SequencedKey() throws NoSuchFieldException {
        FieldModel fieldModel = builder.buildFieldModel(DummyModel.class.getDeclaredField("sequencedKey"));

        assertThat(fieldModel, instanceOf(SequencedKeyModel.class));
    }

    @Test
    public void buildFieldModel_UnannotatedIsAttribute() throws NoSuchFieldException {
        Field unannotatedField = DummyModel.class.getDeclaredField("stringField");

        FieldModel fieldModel = builder.buildFieldModel(unannotatedField);

        assertThat(fieldModel, instanceOf(AttributeModel.class));
    }

    @Test
    public void buildFieldModel_Transient() throws NoSuchFieldException {
        Field transientField = DummyModel.class.getDeclaredField("transientField");

        builder.buildFieldModel(transientField);

        assertThat(builder.fields.isEmpty(), is(true));
    }

    @SuppressWarnings("UnusedDeclaration")
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

    @SuppressWarnings("UnusedDeclaration")
    @KeyValueModel
    public class BasicModel {
        @Key
        private String key;
        private int field;
    }

    @SuppressWarnings("UnusedDeclaration")
    @KeyValueModel
    public class TwoKeyModel {
        @Key
        private String key1;
        @Key
        private String key2;
        private int field;
    }

    @SuppressWarnings("UnusedDeclaration")
    @KeyValueModel
    public class NoKeyModel {
        private int field;
    }
}
