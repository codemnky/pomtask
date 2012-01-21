package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.base.Throwables;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;

public class KeyModel extends FieldModel {
    public KeyModel(MetaModel model, Field keyField) {
        super(model, keyField);
        this.field.setAccessible(true);
    }

    public KeyModel(KeyModel keyModel) {
        super(keyModel.model, keyModel.field);
    }

    @Override
    public Object update(String key, Object obj, StringJedisConnection connection) {
        return storeValue(obj, connection);
    }

    @Override
    public Object create(String key, Object obj, StringJedisConnection connection) {
        return storeValue(obj, connection);
    }

    private Object storeValue(Object obj, StringJedisConnection connection) {
        try {
            Object value = field.get(obj);
            if (value == null) {
                throw new KeyValueMappingException("@Key fields may never be null.");
            }
            String convertedValue = ConvertUtils.convert(value);

            connection.hSet(model.getKey(convertedValue), fieldName(), convertedValue);
            return value;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}