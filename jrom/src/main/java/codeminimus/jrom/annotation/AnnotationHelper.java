package codeminimus.jrom.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationHelper {
    public static final AnnotationHelper HELPER = new AnnotationHelper();

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