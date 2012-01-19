package codeminimus.jrom.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationHelper {
    public static final AnnotationHelper HELPER = new AnnotationHelper();

    public String findModelName(Class<?> modelClass) {
        KeyValueModel annotation = modelClass.getAnnotation(KeyValueModel.class);
        return annotation.name().isEmpty() ? StringUtils.uncapitalize(modelClass.getSimpleName()) : annotation.name();
    }

    public Field findFieldWithAnnotation(Class<?> modelClass, Class<? extends Annotation> annotationClass) {
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field;
            }
        }
        return null;
    }
}