package zhku.mvc.annotation;

import java.lang.annotation.*;

/**
 * Created by ipc on 2017/8/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
