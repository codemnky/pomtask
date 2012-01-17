package pomtask.core.mapper.metamodel;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class FieldModelTest {
    private MetaModel model = new MetaModel();
    private Field field;
    private FieldModelClass propertyClass;

    @Before
    public void setUp() throws NoSuchFieldException {
        field = FieldModelClass.class.getDeclaredField("testField");
        propertyClass = new FieldModelClass(model, field);
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

    private class FieldModelClass extends FieldModel {
        private String testField;

        private FieldModelClass(MetaModel model, Field field) {
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

