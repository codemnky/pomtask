package pomtask.core.mapper.metamodel;

import com.google.common.base.Throwables;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;

public class KeyModel extends FieldModel {
    public KeyModel(MetaModel model, Field keyField) {
        super(model, keyField);
        this.field.setAccessible(true);
    }

    public KeyModel(KeyModel keyModel) {
        super(keyModel.model, keyModel.field);
    }

    public String key(Object object) {
        try {
            return String.format("%s:%s", model.modelName, field.get(object));
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object update(Object obj, JedisConnection connection) {
        try {
            Object value = field.get(obj);
            if (value == null) {
                throw new KeyValueMappingException("@Key fields may never be null.");
            }
            return value;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object create(Object obj, JedisConnection connection) {
        return update(obj, connection);
    }
}