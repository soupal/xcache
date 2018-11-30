package com.xquant.platform.component.xcache.dto;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.xquant.platform.component.xcache.annotation.XCacheKey;
import com.xquant.platform.component.xcache.annotation.XCacheObject;
import com.xquant.platform.component.xcache.annotation.XCacheProperty;

/**
 * 解析 标注{@link XCacheObject}的类的元数据信息
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XCacheObjectMetaData implements Serializable {

	private static final long serialVersionUID = 3348825697285900467L;

	/**
	 * 当前metaData所属的类
	 */
	private Class<?> cacheObjectClass;

	/**
	 * 功能号
	 */
	private int funNum;

	/**
	 * 缓存名称
	 */
	private String cacheName;

	/**
	 * 缓存key表达式
	 */
	private String cacheKeyExpression;

	/**
	 * 执行sql
	 */
	private String sql;

	/**
	 * key为类中方法标注注解 {@link XCacheKey}的name属性值 value为该属性名称对应的 getter方法
	 */
	@JSONField(serialize = false)
	private Map<String, Method> cacheKeyGetterMethods;
	/**
	 * key为类中方法标注注解{@link XCacheProperty}的name属性值value为该属性名称对应的 getter方法
	 */
	@JSONField(serialize = false)
	private Map<String, Method> cachePropertyGetterMthods;

	/**
	 * 缓存key个数
	 */
	private int numOfCacheKey;

	/**
	 * 普通缓存属性个数
	 */
	private int numOfCacheProperty;

	public int getNumOfCacheKey() {
		return numOfCacheKey;
	}

	public void setNumOfCacheKey(int numOfCacheKey) {
		this.numOfCacheKey = numOfCacheKey;
	}

	public int getNumOfCacheProperty() {
		return numOfCacheProperty;
	}

	public void setNumOfCacheProperty(int numOfCacheProperty) {
		this.numOfCacheProperty = numOfCacheProperty;
	}

	public String getCacheNameExpression() {
		return cacheKeyExpression;
	}

	public void setCacheNameExpression(String cacheNameExpression) {
		this.cacheKeyExpression = cacheNameExpression;
	}

	public Map<String, Method> getCacheKeyGetterMethods() {
		return cacheKeyGetterMethods;
	}

	public void setCacheKeyGetterMethods(Map<String, Method> cacheKeyGetterMethods) {
		this.cacheKeyGetterMethods = cacheKeyGetterMethods;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Method> getCachePropertyGetterMthods() {
		return cachePropertyGetterMthods;
	}

	public void setCachePropertyGetterMthods(Map<String, Method> cachePropertyGetterMthods) {
		this.cachePropertyGetterMthods = cachePropertyGetterMthods;
	}

	public Class<?> getCacheObjectClass() {
		return cacheObjectClass;
	}

	public void setCacheObjectClass(Class<?> cacheObjectClass) {
		this.cacheObjectClass = cacheObjectClass;
	}

	public int getFunNum() {
		return funNum;
	}

	public void setFunNum(int funNum) {
		this.funNum = funNum;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getCacheKeyExpression() {
		return cacheKeyExpression;
	}

	public void setCacheKeyExpression(String cacheKeyExpression) {
		this.cacheKeyExpression = cacheKeyExpression;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
