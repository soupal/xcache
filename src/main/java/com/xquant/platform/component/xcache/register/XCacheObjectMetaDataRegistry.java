package com.xquant.platform.component.xcache.register;

import java.util.concurrent.ConcurrentHashMap;

import com.xquant.platform.component.xcache.dto.XCacheObjectMetaData;

/**
 * 用于注册和获取一个对象的缓存元数据信息
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XCacheObjectMetaDataRegistry {

	private static ConcurrentHashMap<String, XCacheObjectMetaData> objMetaDataCache = new ConcurrentHashMap<String, XCacheObjectMetaData>(256);

	public static void register(Class<?> cls, XCacheObjectMetaData cacheObjectMetaData) {
		objMetaDataCache.putIfAbsent(cls.getName(), cacheObjectMetaData);
	}

	public static XCacheObjectMetaData getCacheObjectMetaData(Class<?> cls) {
		return objMetaDataCache.get(cls.getName());
	}

	public static void clear() {
		objMetaDataCache.clear();
	}
}
