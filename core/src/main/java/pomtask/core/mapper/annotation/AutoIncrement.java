package pomtask.core.mapper.annotation;

import pomtask.core.mapper.MappingLifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoIncrement {
    MappingLifecycle[] when() default {MappingLifecycle.BEFORE_SAVE};
}
