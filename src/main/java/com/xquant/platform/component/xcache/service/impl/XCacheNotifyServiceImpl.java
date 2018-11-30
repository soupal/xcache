package com.xquant.platform.component.xcache.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.service.XCacheNotifyService;

@Component
public class XCacheNotifyServiceImpl implements XCacheNotifyService {

	private Logger logger = LoggerFactory.getLogger(XCacheNotifyServiceImpl.class);

	@Override
	public void notify(XCacheNotifyCommand command, String cacheName, Map<String, Object> cacheMap, int funNum) {
		logger.info(
				" \r\n\r\n =====================notify service=================\r\n  command:{},cacheName:{},funNum:{},cacheObj:{} \r\n ================================================\r\n",
				command, cacheName, funNum, cacheMap);
	}

	@Override
	public void notify(XCacheNotifyCommand command, String cacheName, Object cacheObj, int funNum) {
		logger.info(
				" \r\n\r\n =====================notify service=================\r\n  command:{},cacheName:{},funNum:{},cacheObj:{} \r\n ================================================\r\n",
				command, cacheName, funNum, cacheObj);
	}

}
