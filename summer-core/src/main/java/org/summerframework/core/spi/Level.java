package org.summerframework.core.spi;

import java.lang.annotation.*;

/**
 *
 * @author cjshen
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Level {

    /**
     *
     * @return 级别,值越小,越早加载
     */
    int value() default 0;

}
