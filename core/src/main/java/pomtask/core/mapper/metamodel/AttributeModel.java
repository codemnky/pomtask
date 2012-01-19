package pomtask.core.mapper.metamodel;

import com.google.common.base.Throwables;
import org.apache.commons.beanutils.ConvertUtils;
import pomtask.core.mapper.StringJedisConnection;
import pomtask.core.mapper.annotation.Attribute;

import java.lang.reflect.Field;

public class AttributeModel extends FieldModel {
    private final String fieldName;

    public AttributeModel(MetaModel model, Field field) {
        super(model, field);
        Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
        this.fieldName = (attributeAnnotation == null || attributeAnnotation.name().isEmpty()) ? field.getName() : attributeAnnotation.name();
    }

    @Override
    public Object update(Object obj, StringJedisConnection conn) {
        try {
            Object fieldValue = field.get(obj);

            if (fieldValue != null) {
                String storeValue = ConvertUtils.convert(fieldValue);
                conn.hSet(model.getModelName(), fieldName(), storeValue);
            }

            return fieldValue;
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object create(Object obj, StringJedisConnection connection) {
        return update(obj, connection);
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
