package pomtask.core.mapper.metamodel;


import com.google.common.annotations.VisibleForTesting;
import pomtask.core.mapper.StringJedisConnection;

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

    public abstract Object update(Object obj, StringJedisConnection conn);

    public abstract Object create(Object obj, StringJedisConnection connection);
}
