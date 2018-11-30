package com.xquant.platform.component.xcache.service;

import java.util.Map;

import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;

/**
 * 缓存通知服务
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月27日
 */
public interface XCacheNotifyService {

	/**
	 * 缓存通知
	 * 
	 * @param command
	 *            缓存通知类型
	 * @param cacheName
	 *            缓存名称
	 * @param cacheMap
	 *            实际缓存map 包含某个对象的属性和值
	 * @param funNum
	 *            功能号
	 */
	void notify(XCacheNotifyCommand command, String cacheName, Map<String, Object> cacheMap, int funNum);

	/**
	 * 缓存通知
	 * 
	 * @param command
	 *            缓存通知类型
	 * @param cacheName
	 *            缓存名称
	 * @param cacheObj
	 *            缓存对象
	 * @param funNum
	 *            功能号
	 */
	void notify(XCacheNotifyCommand command, String cacheName, Object cacheObj, int funNum);
}
