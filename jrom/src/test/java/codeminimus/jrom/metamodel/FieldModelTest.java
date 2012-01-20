package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class FieldModelTest {
    private MetaModel model = new MetaModel("modelName", null, ImmutableMap.<String, FieldModel>of());
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

    @SuppressWarnings("UnusedDeclaration")
    private class FieldModelClass extends FieldModel {
        private String testField;

        private FieldModelClass(MetaModel model, Field field) {
            super(model, field);
        }

        @Override
        public Object update(String key, Object obj, StringJedisConnection connection) {
            return null;
        }

        @Override
        public Object create(String key, Object obj, StringJedisConnection connection) {
            return null;
        }
    }
}

