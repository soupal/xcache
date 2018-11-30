package com.xquant.platform.component.xcache.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;
import com.xquant.platform.component.xcache.service.XCacheNotifyService;

/**
 * 缓存资源构造事件发布器 
 * <li>当前存在事务 将缓存对象添加到线程局部变量</li>
 * <li>当前不存在事务 直接调用 {@link XCacheNotifyService} 进行缓存通知</li>
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
public class XCacheObjectCreatePublisher implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(XCacheObjectCreatePublisher.class);

	private static XCacheObjectCreatePublisher resourceManager = new XCacheObjectCreatePublisher();

	private ApplicationContext context;

	public static XCacheObjectCreatePublisher getInstance() {
		return resourceManager;
	}

	/**
	 * 添加缓存对象
	 * 
	 * @param command
	 * @param cacheObj
	 */
	public static void publish(XCacheNotifyObject notifyObject) {
		resourceManager.context.publishEvent(new XCacheObjectCreateApplicationEvent(notifyObject));
		logger.info("构造缓存通知对象并发布事件成功{}",notifyObject);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return context;
	}
}
