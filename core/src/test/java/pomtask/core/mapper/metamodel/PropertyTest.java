package pomtask.core.mapper.metamodel;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class PropertyTest {
    private MetaModel model = new MetaModel();
    private Field field;
    private PropertyClass propertyClass;

    @Before
    public void setUp() throws NoSuchFieldException {
        field = PropertyClass.class.getDeclaredField("testField");
        propertyClass = new PropertyClass(model, field);
    }

    @Test
    public void contstruct() {
        assertThat(propertyClass.model, sameInstance(model));
        assertThat(propertyClass.field, sameInstance(field));
    }

    @Test
    public void fieldName() throws Exception {
        assertThat(propertyClass.fieldName(), is("testField"));
    }

    private class PropertyClass extends Property {
        private String testField;

        private PropertyClass(MetaModel model, Field field) {
            super(model, field);
        }

        @Override
        public Object update(Object obj, JedisConnection connection) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object create(Object obj, JedisConnection connection) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}

