package com.xquant.platform.component.xcache.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;

/**
 * 保存缓存通知对象{@link XCacheNotifyObject}的局部线程变量
 * 
 * <li>当前事务缓存数据的保存和获取</li>
 * <li>当前事务挂起或恢复时,需要调用相应的事务挂起或恢复进行事务缓存数据的挂起或恢复操作</li>
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XCacheResourceSynchronizationManager {

	private static final Logger logger = LoggerFactory.getLogger(XCacheResourceSynchronizationManager.class);
	/**
	 * 当前事务缓存通知数据
	 */
	private static ThreadLocal<List<XCacheNotifyObject>> NOTIFY_OBJECTS_CURRENT = new ThreadLocal<List<XCacheNotifyObject>>();

	private static final String BOND_SOURCE_KEY_PREFIX = "XCACHE_SOURCE_";

	private static ThreadLocal<Integer> SUSPEND_TIME = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	/**
	 * 挂起事务处理
	 */
	public static void suspend() {
		List<XCacheNotifyObject> allCacheNotifyObjects = getAllCacheNotifyObjects();
		String bindKey = BOND_SOURCE_KEY_PREFIX + SUSPEND_TIME.get();
		TransactionSynchronizationManager.bindResource(bindKey, allCacheNotifyObjects);
		SUSPEND_TIME.set(SUSPEND_TIME.get() + 1);
		clear();
	}

	/**
	 * 恢复事务处理
	 */
	public static void resume() {
		SUSPEND_TIME.set(SUSPEND_TIME.get() - 1);
		String bindKey = BOND_SOURCE_KEY_PREFIX + (SUSPEND_TIME.get());
		Object unbindResource = TransactionSynchronizationManager.unbindResource(bindKey);
		List<XCacheNotifyObject> allCacheNotifyObjects = (List<XCacheNotifyObject>) unbindResource;
		addCacheObjectList(allCacheNotifyObjects);
	}

	/**
	 * 添加一个缓存对象
	 * 
	 * @param xCacheNotifyObject
	 */
	public static void addCacheObject(XCacheNotifyObject xCacheNotifyObject) {
		if (NOTIFY_OBJECTS_CURRENT.get() == null) {
			NOTIFY_OBJECTS_CURRENT.set(new ArrayList<XCacheNotifyObject>());
		}
		NOTIFY_OBJECTS_CURRENT.get().add(xCacheNotifyObject);
		logger.info("add current transaction cached notify object succ ! {}", xCacheNotifyObject);
	}

	/**
	 * 批量添加缓存对象
	 * 
	 * @param xCacheNotifyObjectList
	 */
	public static void addCacheObjectList(List<XCacheNotifyObject> xCacheNotifyObjectList) {
		if (NOTIFY_OBJECTS_CURRENT.get() == null) {
			NOTIFY_OBJECTS_CURRENT.set(new ArrayList<XCacheNotifyObject>());
		}
		NOTIFY_OBJECTS_CURRENT.get().addAll(xCacheNotifyObjectList);
		logger.info("add batch current transaction cached notify object succ ! {}", xCacheNotifyObjectList);
	}

	/**
	 * 获取所有的缓存对象
	 * 
	 * @return
	 */
	public static List<XCacheNotifyObject> getAllCacheNotifyObjects() {
		if (NOTIFY_OBJECTS_CURRENT.get() == null) {
			return new ArrayList<XCacheNotifyObject>();
		}
		logger.info("get all current transaction cached notify object :{}", NOTIFY_OBJECTS_CURRENT.get());
		return NOTIFY_OBJECTS_CURRENT.get();
	}

	/**
	 * 清空缓存对象
	 */
	public static void clear() {
		NOTIFY_OBJECTS_CURRENT.remove();
		logger.info("clear all current transaction cached notify objects");
	}
}
