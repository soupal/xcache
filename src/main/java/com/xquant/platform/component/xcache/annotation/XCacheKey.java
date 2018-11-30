package com.xquant.platform.component.xcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在使用{@link XCacheObject}注解的简单java对象中的方法上使用该注解,该方法用于从该类型对象上获取注解name对应字段的值
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface XCacheKey {

	String name();

}
