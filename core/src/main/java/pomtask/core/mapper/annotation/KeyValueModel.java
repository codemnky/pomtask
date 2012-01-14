package pomtask.core.mapper.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface KeyValueModel {
    String name() default "";
}
