package codeminimus.jrom.annotation;

import codeminimus.jrom.MappingLifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field annotated will be supplied with a value from an auto-incrementing sequence.  This fieldName of
 * this sequence is modelName:fieldName by default.
 * <p/>
 * The sequence key and value will be created and set to start at 1 if it does not exist.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sequence {

    /**
     * At what event in the lifecycle of the option is the Sequence updated.
     */
    MappingLifecycle[] when() default {MappingLifecycle.BEFORE_SAVE};

    /**
     * Specify the key fieldName for the auto-incrementing sequence.
     */
    String name() default "";
}
