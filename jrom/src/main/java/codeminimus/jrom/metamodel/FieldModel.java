package codeminimus.jrom.metamodel;


import codeminimus.jrom.StringJedisConnection;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;

public abstract class FieldModel {
    @VisibleForTesting
    final Field field;
    @VisibleForTesting
    final MetaModel model;

    public abstract Object update(String key, Object obj, StringJedisConnection conn);

    protected FieldModel(MetaModel model, Field field) {
        this.field = field;
        field.setAccessible(true);
        this.model = model;
    }

    public abstract Object create(String key, Object obj, StringJedisConnection connection);

    public String fieldName() {
        return field.getName();
    }

    public Object read(String key, StringJedisConnection connection) {
        String value = connection.hGet(key, fieldName());
        return ConvertUtils.convert(value, field.getType());
    }

    public final void set(Object object, Object value) {
        try {
            Object convertedValue = ConvertUtils.convert(value, field.getType());
            field.set(object, convertedValue);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
