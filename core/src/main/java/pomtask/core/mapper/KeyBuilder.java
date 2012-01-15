package pomtask.core.mapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.exception.KeyValueMappingException;

import java.lang.reflect.Field;

public class KeyBuilder<T> {
    private final Field keyField;
    private final String modelName;

    public KeyBuilder(Class<T> modelClass) {
        this(modelClass, AnnotationHelper.HELPER);
    }

    @VisibleForTesting
    KeyBuilder(Class<T> modelClass, AnnotationHelper helper) {
        String modelName = helper.findModelName(modelClass);

        this.modelName = modelName;
        this.keyField = findKeyField(modelClass, helper);
    }

    private Field findKeyField(Class<T> modelClass, AnnotationHelper helper) {
        Field keyField = helper.findFieldWithAnnotation(modelClass, Key.class);
        if (keyField == null) {
            throw new KeyValueMappingException(String.format("No Key specified for model (%s)", modelClass.getName()));
        }

        keyField.setAccessible(true);
        return keyField;
    }

    public String key(T model) {
        try {
            return String.format("%s:%s", modelName, keyField.get(model));
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}