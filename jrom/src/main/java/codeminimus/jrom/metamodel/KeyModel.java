package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.base.Throwables;
import org.joda.convert.StringConvert;

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
        Object value = value(obj);
        if (value == null) {
            throw new KeyValueMappingException("@Key fields may never be null.");
        }
        return value;
    }

    @Override
    public Object create(String key, Object obj, StringJedisConnection connection) {
        Object value = value(obj);
        if (value == null) {
            throw new KeyValueMappingException("@Key fields may never be null.");
        }
        String convertedValue = StringConvert.INSTANCE.convertToString(value);

        connection.hSet(model.buildKey(convertedValue), fieldName(), convertedValue);

        return value;
    }

    public Object value(Object model) {
        try {
            return field.get(model);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}