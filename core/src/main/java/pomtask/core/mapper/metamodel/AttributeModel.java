package pomtask.core.mapper.metamodel;

import com.google.common.base.Throwables;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.lang.reflect.Field;

public class AttributeModel extends FieldModel {
    public AttributeModel(MetaModel model, Field field) {
        super(model, field);
    }

    @Override
    public Object update(Object obj, JedisConnection connection) {
        try {
            Object fieldValue = field.get(obj);

            if (fieldValue != null) {
                String storeValue = ConvertUtils.convert(fieldValue);
                connection.hSet(model.getModelName().getBytes(), fieldName().getBytes(), storeValue.getBytes());
            }

            return fieldValue;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object create(Object obj, JedisConnection connection) {
        return update(obj, connection);
    }
}
