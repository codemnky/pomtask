package pomtask.core.mapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import pomtask.core.mapper.annotation.AnnotationHelper;
import pomtask.core.mapper.annotation.Key;
import pomtask.core.mapper.annotation.KeyValueModel;
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
        KeyValueModel annotation = modelClass.getAnnotation(KeyValueModel.class);
        modelName = annotation.name().isEmpty() ? StringUtils.uncapitalize(modelClass.getSimpleName()) : annotation.name();

        keyField = helper.findFieldWithAnnotation(modelClass, Key.class);
        if (keyField == null) {
            throw new KeyValueMappingException(String.format("No Key specified for model (%s)", modelClass.getName()));
        }
        keyField.setAccessible(true);
    }

    public String key(T model) {
        try {
            return String.format("%s:%s", modelName, keyField.get(model));
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}