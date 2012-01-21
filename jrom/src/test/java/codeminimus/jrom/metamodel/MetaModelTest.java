package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import codeminimus.jrom.annotation.Unmapped;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MetaModelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private StringJedisConnection connection = mock(StringJedisConnection.class);

    private final MetaModel builder = new MetaModel("modelName", null, ImmutableMap.<String, FieldModel>of());

    @SuppressWarnings("unchecked")
    @Test
    public void construct() {
        MetaModel<BasicModel> metaModel = MetaModel.newMetaModel(BasicModel.class);

        assertThat(metaModel.getModelName(), is("basicModel"));
        assertThat(metaModel.keyModel.fieldName(), is("key"));
        assertThat((Class<Integer>) metaModel.fields.get("field").field.getType(), equalTo(int.class));
    }

    @Test
    public void construct_TwoKeys() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModel.newMetaModel(TwoKeyModel.class);
    }

    @Test
    public void construct_NoKey() throws NoSuchFieldException {
        thrown.expect(KeyValueMappingException.class);

        MetaModel.newMetaModel(NoKeyModel.class);
    }

    @Test
    public void construct_NoModelAnnotation() {
        thrown.expect(KeyValueMappingException.class);

        MetaModel.newMetaModel(NoKeyValueModelAnnot.class);
    }

    @Test
    public void key_String() throws NoSuchFieldException {
        String key = builder.buildKey("apple");

        assertThat(key, is("modelName:apple"));
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

    @Test
    public void save() {
        MetaModel<BasicModel> metaModel = MetaModel.newMetaModel(BasicModel.class);

        when(connection.incr("basicModel:key")).thenReturn(2L);

        BasicModel basicModel = new BasicModel();
        basicModel.field = 12;

        BasicModel newModel = metaModel.create(basicModel, connection);

        verify(connection).hSet("basicModel:2", "field", "12");
        verify(connection).hSet("basicModel:2", "key", "2");

        assertThat(newModel, is(not(sameInstance(basicModel))));
        assertThat(newModel.field, equalTo(12));
        assertThat(newModel.key, equalTo(2));
    }

    @Test
    public void read() {
        MetaModel<BasicModel> metaModel = MetaModel.newMetaModel(BasicModel.class);

        when(connection.hGet("basicModel:1", "field")).thenReturn("2");
        when(connection.hGet("basicModel:1", "key")).thenReturn("1");

        BasicModel newModel = metaModel.read("1", connection);

        assertThat(newModel.field, equalTo(2));
        assertThat(newModel.key, equalTo(1));
    }

    @Test
    public void delete() {
        MetaModel<BasicModel> metaModel = MetaModel.newMetaModel(BasicModel.class);

        when(connection.del("basicModel:1")).thenReturn(1L);

        BasicModel basicModel = new BasicModel();
        basicModel.key = 1;

        boolean delete = metaModel.delete(basicModel, connection);

        assertThat(delete, is(true));
    }

    @Test
    public void key() {
        MetaModel<BasicModel> metaModel = MetaModel.newMetaModel(BasicModel.class);

        BasicModel basicModel = new BasicModel();
        basicModel.key = 1;

        assertThat(metaModel.key(basicModel), is("basicModel:1"));
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
    public static class BasicModel {
        @Key
        @Sequence
        private int key;
        private int field;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BasicModel that = (BasicModel) o;

            return field == that.field && key == that.key;

        }

        @Override
        public int hashCode() {
            int result = key;
            result = 31 * result + field;
            return result;
        }

        @Override
        public String toString() {
            return "BasicModel{" +
                    "keyModel='" + key + '\'' +
                    ", field=" + field +
                    '}';
        }
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

    @SuppressWarnings("UnusedDeclaration")
    public class NoKeyValueModelAnnot {
        @Key
        private int key;
        private int field;
    }
}
