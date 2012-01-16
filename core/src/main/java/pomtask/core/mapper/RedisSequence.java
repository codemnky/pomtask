package pomtask.core.mapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.Sequence;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;

public class RedisSequence {
    @VisibleForTesting
    final String sequenceKeyName;

    private final Field field;
    private final ImmutableSet<MappingLifecycle> mappingLifecycles;
    private final Class type;

    public RedisSequence(Field field) {
        this(field, AnnotationHelper.HELPER);
    }

    @VisibleForTesting
    RedisSequence(Field field, AnnotationHelper helper) {
        field.setAccessible(true);
        this.field = field;

        this.type = findFieldType(field.getType());

        Sequence annotation = field.getAnnotation(Sequence.class);
        mappingLifecycles = Sets.immutableEnumSet(annotation.when()[0], annotation.when());

        this.sequenceKeyName = findKeyName(field, helper, annotation);
    }

    private Class findFieldType(Class<?> type) {
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return int.class;
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return long.class;
        }
        throw new KeyValueMappingException("Sequences only support Integer (int) or Long (long) data type.");
    }

    public void increment(MappingLifecycle lifecycle, JedisConnection connection, Object model) {
        if (mappingLifecycles.contains(lifecycle)) {
            try {
                long sequence = connection.incr(sequenceKeyName.getBytes());

                if (type == int.class) {
                    field.setInt(model, (int) sequence);
                } else {
                    field.setLong(model, sequence);
                }
            } catch (IllegalAccessException e) {
                throw Throwables.propagate(e);
            }
        }
    }

    private String findKeyName(Field field, AnnotationHelper helper, Sequence annotation) {
        String sequenceKeyName;
        if (annotation.name().isEmpty()) {
            String modelName = helper.findModelName(field.getDeclaringClass());
            sequenceKeyName = String.format("%s:%s", modelName, field.getName());
        } else {
            sequenceKeyName = annotation.name();
        }
        return sequenceKeyName;
    }
}
