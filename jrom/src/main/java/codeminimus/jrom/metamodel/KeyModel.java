package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.exception.KeyValueMappingException;
import com.google.common.base.Throwables;

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
            return String.format("%s:%s", model.getModelName(), field.get(object));
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object update(Object obj, StringJedisConnection connection) {
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
    public Object create(Object obj, StringJedisConnection connection) {
        return update(obj, connection);
    }
}