package com.xquant.platform.component.xcache.listener;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;
import com.xquant.platform.component.xcache.resource.XCacheObjectCreateApplicationEvent;
import com.xquant.platform.component.xcache.resource.XCacheResourceSynchronizationManager;
import com.xquant.platform.component.xcache.service.XCacheNotifyService;

/**
 * 接收缓存通知对象创建事件并进行处理
 * 
 * <li>当前存在事务 将缓存对象添加到线程局部变量</li>
 * <li>当前不存在事务 直接调用 {@link XCacheNotifyService} 进行缓存通知</li>
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
@Component
public class XCacheObjectCreateEventListener implements ApplicationListener<XCacheObjectCreateApplicationEvent> {

	private Logger logger = LoggerFactory.getLogger(XCacheObjectCreateEventListener.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Autowired(required = false)
	private XCacheNotifyService xCacheNotifyService;

	@Override
	public void onApplicationEvent(XCacheObjectCreateApplicationEvent event) {
		XCacheNotifyObject notifyObject = event.getxCacheNotifyObject();
		// 执行数据库查询
		if (needSqlQuery(notifyObject)) {
			fillNotifyObjectWithCache(notifyObject);
		}
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			logger.info("Transactin is active!add cache object to XCacheResource succ,{}", notifyObject);
			XCacheResourceSynchronizationManager.addCacheObject(notifyObject);
		} else {
			// 如果当前无事务 则直接进行缓存通知
			if (xCacheNotifyService != null) {
				xCacheNotifyService.notify(notifyObject.getNotifyCommand(), notifyObject.getCacheKey(),
						notifyObject.getxCacheObjectMetaData().getCacheName(), notifyObject.getMapCache(),
						notifyObject.getxCacheObjectMetaData().getFunNum());
			}
		}
	}

	private boolean needSqlQuery(XCacheNotifyObject notifyObject) {
		if (notifyObject.getxCacheSqlMetaData() != null) {
			Assert.notNull(notifyObject.getxCacheSqlMetaData().getSql(), "sql can not be null");
			Assert.notNull(notifyObject.getxCacheSqlMetaData().getArgs(), "args can not be null");
			return true;
		}
		return false;
	}

	private void fillNotifyObjectWithCache(XCacheNotifyObject notifyObject) {
		String sql = notifyObject.getxCacheSqlMetaData().getSql();
		Object[] args = notifyObject.getxCacheSqlMetaData().getArgs();
		int[] argTypes = notifyObject.getxCacheSqlMetaData().getArgTypes();
		List<Map<String, Object>> queryForList = null;
		if (argTypes != null) {
			queryForList = jdbcTemplate.queryForList(sql, args, argTypes);
		} else {
			queryForList = jdbcTemplate.queryForList(sql, args);
		}
		if (CollectionUtils.isEmpty(queryForList)) {
			throw new RuntimeException(String.format("根据主键查询[%s]结果为空?参数为[%s]", sql, args));
		}
		if (queryForList.size() > 1) {
			throw new RuntimeException(String.format("根据主键查询[%s]结果数据为多条?参数为[%s]", sql, args));
		}
		notifyObject.setMapCache(queryForList.get(0));
	}
}
