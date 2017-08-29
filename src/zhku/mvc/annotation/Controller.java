package zhku.mvc.annotation;

import java.lang.annotation.*;

/**
 * Controller注解实现
 *
 * Created by ipc on 2017/8/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    public String value() default "";
}
