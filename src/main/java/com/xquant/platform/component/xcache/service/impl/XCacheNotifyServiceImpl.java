package com.xquant.platform.component.xcache.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xquant.platform.component.xcache.dao.XCacheNotifyMapper;
import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.service.XCacheNotifyService;

@Component
public class XCacheNotifyServiceImpl implements XCacheNotifyService {

	private Logger logger = LoggerFactory.getLogger(XCacheNotifyServiceImpl.class);

	@Autowired
	private XCacheNotifyMapper xCacheNotifyMapper;

	@Override
	public void notify(XCacheNotifyCommand command, String cacheKey, String cacheName, Map<String, Object> cacheMap, int funNum) {
		logger.info(
				" \r\n\r\n =====================notify service=================\r\n command:{} \r\n cacheKey:{} \r\n  cacheName:{} \r\n funNum:{} \r\n cacheObj:{} \r\n ====================================================\r\n",
				command, cacheName, funNum, cacheMap);
		xCacheNotifyMapper.insert(command.getValue(), cacheKey,cacheName, String.valueOf(funNum), JSON.toJSONString(cacheMap));
	}

	@Override
	public void notify(XCacheNotifyCommand command, String cacheKey, String cacheName, Object cacheObj, int funNum) {
		logger.info(
				" \r\n\r\n =====================notify service=================\r\n  command:{},cacheName:{},funNum:{},cacheObj:{} \r\n ====================================================\r\n",
				command, cacheName, funNum, cacheObj);
	}

}
