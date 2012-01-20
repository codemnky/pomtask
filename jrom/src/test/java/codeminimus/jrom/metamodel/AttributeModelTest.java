package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.Attribute;
import org.junit.Test;
import org.mockito.Matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AttributeModelTest {
    public static final String MODEL_KEY = "modelKey";
    private StringJedisConnection connection = mock(StringJedisConnection.class);
    private MetaModel model = mock(MetaModel.class);

    @Test
    public void update() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = "help";

        assertThat((String) attribute.update(MODEL_KEY, dummyModel, connection), is("help"));

        verify(connection).hSet(MODEL_KEY, "testField", "help");
    }

    @Test
    public void update_NullValue() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = null;

        assertThat(attribute.update(MODEL_KEY, dummyModel, connection), is(nullValue()));

        verify(connection, never()).hSet(eq("modelName"), eq("testField"), Matchers.<String>any());
    }

    @Test
    public void create() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = "help";

        assertThat((String) attribute.create(MODEL_KEY, dummyModel, connection), is("help"));

        verify(connection).hSet(MODEL_KEY, "testField", "help");
    }

    @Test
    public void update_NamedAttribute() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("namedField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.namedField = 5;

        assertThat((Integer) attribute.update(MODEL_KEY, dummyModel, connection), is(5));

        verify(connection).hSet(MODEL_KEY, "newName", "5");
    }

    @SuppressWarnings("UnusedDeclaration")
    private class DummyModel {
        private String testField;
        @Attribute(name = "newName")
        private int namedField;
    }
}
