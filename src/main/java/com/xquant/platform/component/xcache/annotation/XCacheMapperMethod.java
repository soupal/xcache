package com.xquant.platform.component.xcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;

/**
 * 用于mapper方法的注解 同时制定操作类型 @XCacheNotifyCommand
 * 
 * 在一个注解{@link XCacheMapper}的mapper接口的方法添加此注解,该方法启用缓存功能
 * 
 * @project xquant-platform-component-cache
 * @author guanglai.zhou
 * @date 2018年11月27日
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface XCacheMapperMethod {

	XCacheNotifyCommand notifyCommand();

}
