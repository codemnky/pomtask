package codeminimus.jrom.metamodel;


import codeminimus.jrom.StringJedisConnection;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;

import java.lang.reflect.Field;

public abstract class FieldModel {
    @VisibleForTesting
    final Field field;
    @VisibleForTesting
    final MetaModel model;

    protected FieldModel(MetaModel model, Field field) {
        this.field = field;
        field.setAccessible(true);
        this.model = model;
    }

    public String fieldName() {
        return field.getName();
    }

    public abstract Object update(String key, Object obj, StringJedisConnection conn);

    public abstract Object create(String key, Object obj, StringJedisConnection connection);

    public final void set(Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
