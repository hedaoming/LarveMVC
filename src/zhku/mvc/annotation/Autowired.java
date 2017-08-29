package zhku.mvc.annotation;

import java.lang.annotation.*;

/**
 * Created by ipc on 2017/8/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
