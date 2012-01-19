package pomtask.core.mapper.metamodel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AttributeModelTest {
    private JedisConnection connection = mock(JedisConnection.class);
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

        verify(connection).hSet("modelName".getBytes(), "testField".getBytes(), "help".getBytes());
    }

    @Test
    public void update_NullValue() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = null;

        assertThat((String) attribute.update(dummyModel, connection), is(nullValue()));

        verify(connection, never()).hSet(eq("modelName".getBytes()), eq("testField".getBytes()), Matchers.<byte[]>any());
    }

    @Test
    public void create() throws NoSuchFieldException {
        AttributeModel attribute = new AttributeModel(model, DummyModel.class.getDeclaredField("testField"));

        DummyModel dummyModel = new DummyModel();
        dummyModel.testField = "help";

        assertThat((String) attribute.create(dummyModel, connection), is("help"));

        verify(connection).hSet("modelName".getBytes(), "testField".getBytes(), "help".getBytes());
    }

    private class DummyModel {
        private String testField;
    }
}
