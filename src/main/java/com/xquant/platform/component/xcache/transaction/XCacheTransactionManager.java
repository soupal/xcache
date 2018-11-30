package com.xquant.platform.component.xcache.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;
import com.xquant.platform.component.xcache.resource.XCacheResourceSynchronizationManager;
import com.xquant.platform.component.xcache.service.XCacheNotifyService;

/**
 * 事务管理器 继承{@link DataSourceTransactionManager} 增加缓存逻辑
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
public class XCacheTransactionManager extends DataSourceTransactionManager {

	private static final long serialVersionUID = 2788392542508818566L;

	@Autowired(required = false)
	private XCacheNotifyService xCacheNotifyService;

	@Override
	protected Object doSuspend(Object transaction) {
		Object obj = super.doSuspend(transaction);
		XCacheResourceSynchronizationManager.suspend();
		return obj;
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		super.doResume(transaction, suspendedResources);
		XCacheResourceSynchronizationManager.resume();
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {

		List<XCacheNotifyObject> allCacheNotifyObjects = XCacheResourceSynchronizationManager.getAllCacheNotifyObjects();
		if (allCacheNotifyObjects.size() >= 1 && xCacheNotifyService != null) {
			for (XCacheNotifyObject notifyObject : allCacheNotifyObjects) {
				xCacheNotifyService.notify(notifyObject.getNotifyCommand(), notifyObject.getxCacheObjectMetaData().getCacheName(),
						notifyObject.getMapCache(), notifyObject.getxCacheObjectMetaData().getFunNum());
			}
		}

		super.doCommit(status);
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		XCacheResourceSynchronizationManager.clear();
	}

}
