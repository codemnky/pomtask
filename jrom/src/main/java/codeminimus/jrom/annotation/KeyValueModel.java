package codeminimus.jrom.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface KeyValueModel {
    String name() default "";
}
