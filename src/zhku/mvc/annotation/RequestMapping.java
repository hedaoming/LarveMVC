package zhku.mvc.annotation;

import java.lang.annotation.*;

/**
 * ReqeustMapping注解的实现
 * Created by ipc on 2017/8/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
