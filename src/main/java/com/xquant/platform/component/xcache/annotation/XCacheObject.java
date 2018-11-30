package com.xquant.platform.component.xcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加此注解表示当前简单java对象开启缓存功能 添加时必须指定cacheNameExpression和sql属性
 * <li>cacheNameExpression 生成缓存名称的表达式</li>
 * <li>sql 执行数据库查询生成该对象的sql</li>
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface XCacheObject {

	/**
	 * 功能号
	 * @return
	 */
	int funNum();
	
	
	String cacheName();
	/**
	 * 生成缓存名称的表达式
	 * 
	 * @return
	 */
	String cacheKeyExpression();

	/**
	 * 执行该sql查询数据库数据用于生成当前类对象
	 * 
	 * @return
	 */
	String sql() default "";

}
