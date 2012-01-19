package pomtask.core.mapper.metamodel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import pomtask.core.mapper.StringJedisConnection;
import pomtask.core.mapper.annotation.Attribute;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AttributeModelTest {
    private StringJedisConnection connection = mock(StringJedisConnection.class);
    private MetaModel model = new MetaModel();

    @Before
    public void setUp() {
        model.modelName = "modelName";
        model.key = mock(KeyModel.class);
    }

    @Test
    public void update() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = "help";

        assertThat((String) attribute.update(dummyModel, connection), is("help"));

        verify(connection).hSet("modelName", "testField", "help");
    }

    @Test
    public void update_NullValue() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = null;

        assertThat(attribute.update(dummyModel, connection), is(nullValue()));

        verify(connection, never()).hSet(eq("modelName"), eq("testField"), Matchers.<String>any());
    }

    @Test
    public void create() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = "help";

        assertThat((String) attribute.create(dummyModel, connection), is("help"));

        verify(connection).hSet("modelName", "testField", "help");
    }

    @Test
    public void update_NamedAttribute() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("namedField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.namedField = 5;

        assertThat((Integer) attribute.update(dummyModel, connection), is(5));

        verify(connection).hSet("modelName", "newName", "5");
    }

    private class DummyModel {
        private String testField;
        @Attribute(name = "newName")
        private int namedField;
    }
}
