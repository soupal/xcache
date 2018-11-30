package com.xquant.platform.component.xcache.dto;

import java.io.Serializable;
import java.util.Map;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;

/**
 * 缓存通知对象
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XCacheNotifyObject implements Serializable {

	private static final long serialVersionUID = -4306735197490707992L;

	private final XCacheObjectMetaData xCacheObjectMetaData;

	private final XCacheNotifyCommand notifyCommand;

	private Map<String, Object> cacheKeyAndPropertyMap;

	private XCacheSqlMetaData xCacheSqlMetaData;
	/**
	 * 解析占位符生成的cacheKey
	 */
	private String cacheKey;
	/**
	 * map类型的cache真实数据
	 */
	private Map<String, Object> mapCache;

	/**
	 * 对象类型{非map}的真实数据
	 */
	private Object objCache;

	public XCacheNotifyObject(XCacheNotifyCommand notifyCommand, XCacheObjectMetaData xCacheObjectMetaData) {
		Assert.notNull(notifyCommand, "notifyCommand can not be null");
		Assert.notNull(xCacheObjectMetaData, "xCacheObjectMetaData can not be null");
		this.notifyCommand = notifyCommand;
		this.xCacheObjectMetaData = xCacheObjectMetaData;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public XCacheSqlMetaData getxCacheSqlMetaData() {
		return xCacheSqlMetaData;
	}

	public void setxCacheSqlMetaData(XCacheSqlMetaData xCacheSqlMetaData) {
		this.xCacheSqlMetaData = xCacheSqlMetaData;
	}

	public Map<String, Object> getMapCache() {
		return mapCache;
	}

	public void setMapCache(Map<String, Object> mapCache) {
		this.mapCache = mapCache;
	}

	public Object getObjCache() {
		return objCache;
	}

	public void setObjCache(Object objCache) {
		this.objCache = objCache;
	}

	public XCacheNotifyCommand getNotifyCommand() {
		return notifyCommand;
	}

	public XCacheObjectMetaData getxCacheObjectMetaData() {
		return xCacheObjectMetaData;
	}

	public Map<String, Object> getCacheKeyAndPropertyMap() {
		return cacheKeyAndPropertyMap;
	}

	public void setCacheKeyAndPropertyMap(Map<String, Object> cacheKeyAndPropertyMap) {
		this.cacheKeyAndPropertyMap = cacheKeyAndPropertyMap;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
