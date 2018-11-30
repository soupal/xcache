package com.xquant.platform.component.xcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于mapper接口的注解 注解之后该mapper接口才会启动缓存功能
 * 
 * @project xquant-platform-component-cache
 * @author guanglai.zhou
 * @date 2018年11月27日
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface XCacheMapper {

}
