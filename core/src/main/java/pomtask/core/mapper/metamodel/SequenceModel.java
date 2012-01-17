package pomtask.core.mapper.metamodel;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.MappingLifecycle;
import pomtask.core.mapper.annotation.Sequence;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;
import java.util.Set;

public class SequenceModel {
    private Set<Class> SUPPORTED_TYPES = ImmutableSet.<Class>of(Integer.class, int.class, long.class, Long.class);
    @VisibleForTesting
    final String sequenceKeyName;

    private final ImmutableSet<MappingLifecycle> mappingLifecycles;

    public SequenceModel(String modelName, Field field) {
        checkFieldType(field.getType());

        Sequence annotation = field.getAnnotation(Sequence.class);
        mappingLifecycles = Sets.immutableEnumSet(annotation.when()[0], annotation.when());

        this.sequenceKeyName = findKeyName(field, annotation, modelName);
    }

    public long next(JedisConnection connection) {
        return connection.incr(sequenceKeyName.getBytes());
    }

    public ImmutableSet<MappingLifecycle> getMappingLifecycles() {
        return mappingLifecycles;
    }


    private void checkFieldType(Class<?> type) {
        if (!SUPPORTED_TYPES.contains(type)) {
            throw new KeyValueMappingException("Sequences only support Integer (int) or Long (long) data type.");
        }
    }

    private String findKeyName(Field field, Sequence annotation, String modelName) {
        String sequenceKeyName;
        if (annotation.name().isEmpty()) {
            sequenceKeyName = String.format("%s:%s", modelName, field.getName());
        } else {
            sequenceKeyName = annotation.name();
        }
        return sequenceKeyName;
    }
}
