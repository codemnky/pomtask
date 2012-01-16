package pomtask.core.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for optimistic locking.  This version is incremented before an update of the model object that it is attached
 * to.  Before an update the store is queried to see if the version of the object is greater than the existing version.
 * If it is not, an OptimisticLockingException will be thrown.  This indicates that another write occurred before the
 * update and the program will need to restart the updates after loading the new information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Version {

}
