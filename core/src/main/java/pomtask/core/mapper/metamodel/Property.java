package pomtask.core.mapper.metamodel;


import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.lang.reflect.Field;

public abstract class Property {
    @VisibleForTesting
    final Field field;
    @VisibleForTesting
    final MetaModel model;

    protected Property(MetaModel model, Field field) {
        this.field = field;
        field.setAccessible(true);
        this.model = model;
    }

    public final String fieldName() {
        return field.getName();
    }

    public abstract Object update(Object obj, JedisConnection connection);

    public abstract Object create(Object obj, JedisConnection connection);
}
