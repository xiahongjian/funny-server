package tech.hongjian.funnyserver.annnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.hongjian.funnyserver.route.HttpMethod;



/**
 * @author xiahongjian 
 * @time   2019-03-22 10:39:02
 *
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
	String[] value();
	
	HttpMethod[] method() default {};
}
