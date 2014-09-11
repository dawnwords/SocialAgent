package edu.fudan.sa.annotation;

import edu.fudan.sa.IRemotableService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ming
 * @since 2014-04-08 13:32.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceStub {
    Class<? extends IRemotableService> service();
}
