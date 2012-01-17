package pomtask.core.mapper.metamodel;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;

public class KeyModel implements Property {
    @VisibleForTesting
    final Field keyField;
    @VisibleForTesting
    final String modelName;

    public KeyModel(String modelName, Field keyField) {
        this.modelName = modelName;
        this.keyField = keyField;
        this.keyField.setAccessible(true);
    }

    public KeyModel(KeyModel keyModel) {
        this.keyField = keyModel.keyField;
        this.modelName = keyModel.modelName;
    }

    public String key(Object object) {
        try {
            return String.format("%s:%s", modelName, keyField.get(object));
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String fieldName() {
        return keyField.getName();
    }

    @Override
    public Object valueForUpdate(Object obj, JedisConnection connection) {
        try {
            Object value = keyField.get(obj);
            if (value == null) {
                throw new KeyValueMappingException("@Key fields may never be null.");
            }
            return value;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object valueForCreate(Object obj, JedisConnection connection) {
        return valueForUpdate(obj, connection);
    }
}