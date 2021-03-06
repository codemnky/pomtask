package codeminimus.jrom.metamodel;

import codeminimus.jrom.StringJedisConnection;
import codeminimus.jrom.annotation.Attribute;
import com.google.common.base.Throwables;
import org.joda.convert.StringConvert;

import java.lang.reflect.Field;

public class AttributeModel extends FieldModel {
    private final String fieldName;

    public AttributeModel(MetaModel model, Field field) {
        super(model, field);
        Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
        this.fieldName = (attributeAnnotation == null || attributeAnnotation.name().isEmpty()) ? field.getName() : attributeAnnotation.name();
    }

    @Override
    public Object update(String key, Object obj, StringJedisConnection conn) {
        try {
            Object fieldValue = field.get(obj);

            if (fieldValue != null) {
                String storeValue = StringConvert.INSTANCE.convertToString(fieldValue);
                conn.hSet(key, fieldName(), storeValue);
            }

            return fieldValue;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object create(String key, Object obj, StringJedisConnection connection) {
        return update(key, obj, connection);
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
