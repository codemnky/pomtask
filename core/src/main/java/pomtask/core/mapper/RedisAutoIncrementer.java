package pomtask.core.mapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.AutoIncrement;

import java.lang.reflect.Field;

public class RedisAutoIncrementer {
    @VisibleForTesting
    final String incrementKeyName;

    private final Field field;
    private final ImmutableSet<MappingLifecycle> mappingLifecycles;

    public RedisAutoIncrementer(Field field) {
        this(field, AnnotationHelper.HELPER);
    }

    @VisibleForTesting
    RedisAutoIncrementer(Field field, AnnotationHelper helper) {
        field.setAccessible(true);
        this.field = field;

        AutoIncrement annotation = field.getAnnotation(AutoIncrement.class);
        mappingLifecycles = Sets.immutableEnumSet(annotation.when()[0], annotation.when());

        String modelName = helper.findModelName(field.getDeclaringClass());

        incrementKeyName = String.format("%s:%s", modelName, field.getName());
    }

    public void increment(MappingLifecycle lifecycle, JedisConnection connection, Object model) {
        if (mappingLifecycles.contains(lifecycle)) {

            Long incr = connection.incr(incrementKeyName.getBytes());

            try {
                field.setLong(model, incr);
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }
}
