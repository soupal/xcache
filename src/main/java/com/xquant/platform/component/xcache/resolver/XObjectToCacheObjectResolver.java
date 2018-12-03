package com.xquant.platform.component.xcache.resolver;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.xquant.platform.component.xcache.config.XCacheConfig;
import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;
import com.xquant.platform.component.xcache.dto.XCacheObjectMetaData;
import com.xquant.platform.component.xcache.dto.XCacheSqlMetaData;
import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.parser.XCachePlaceHolderParser;
import com.xquant.platform.component.xcache.parser.XCacheSqlMetaDataParser;

/**
 * 缓存对象解析(根据进行数据库操作时的对象 获取对象缓存注解元数据信息 解析成指定类型的对象信息)
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XObjectToCacheObjectResolver {

	private static Logger logger = LoggerFactory.getLogger(XObjectToCacheObjectResolver.class);

	private static final EnumSet<XCacheNotifyCommand> NO_NEED_QUERY_ENUM_SET = EnumSet.of(XCacheNotifyCommand.DELETE, XCacheNotifyCommand.UPDATE_ALL);
	
	public static void resolveCacheKeyAndProperties(XCacheNotifyObject xCacheNotifyObject, Object obj) {
		XCacheObjectMetaData cacheObjectMetaData = xCacheNotifyObject.getxCacheObjectMetaData();
		int initialCapacity = cacheObjectMetaData.getNumOfCacheKey() + cacheObjectMetaData.getNumOfCacheProperty();
		// 解析所有的cacheKey和cacheProperties
		Map<String, Object> cacheKeyAndProValueMap = new HashMap<>(initialCapacity, XCacheConfig.DEFAULT_LOAD_FACTOR);
		processGetterMethod(obj, cacheKeyAndProValueMap, cacheObjectMetaData.getCacheKeyGetterMethods(), true);
		processGetterMethod(obj, cacheKeyAndProValueMap, cacheObjectMetaData.getCachePropertyGetterMthods(), false);
		xCacheNotifyObject.setCacheKeyAndPropertyMap(cacheKeyAndProValueMap);
		// 解析cacheKey
		String cacheKeyExpression = xCacheNotifyObject.getxCacheObjectMetaData().getCacheKeyExpression();
		String cacheKey = XCachePlaceHolderParser.parse(cacheKeyExpression, cacheKeyAndProValueMap);
		xCacheNotifyObject.setCacheKey(cacheKey);
		// 解析sqlMetaData
		if(NO_NEED_QUERY_ENUM_SET.contains(xCacheNotifyObject.getNotifyCommand())) {
			return;
		}
		String sql = xCacheNotifyObject.getxCacheObjectMetaData().getSql();
		if (StringUtils.isNotBlank(sql)) {
			XCacheSqlMetaData cacheSqlMetaData = XCacheSqlMetaDataParser.parse(sql, cacheKeyAndProValueMap);
			xCacheNotifyObject.setxCacheSqlMetaData(cacheSqlMetaData);
		}
	}

	private static void processGetterMethod(Object obj, Map<String, Object> cacheKeyAndProValueMap, Map<String, Method> cacheGetterMethods,
			boolean checknull) {
		for (String key : cacheGetterMethods.keySet()) {
			Method getterMethod = cacheGetterMethods.get(key);
			Object value = ReflectionUtils.invokeMethod(getterMethod, obj);
			if (value == null) {
				if (checknull) {
					throw new RuntimeException(String.format("当前对象[%s]执行方法[%s]获取的值为空或不存在?", obj.toString(), getterMethod.getName()));
				} else {
					logger.warn("当前对象{}执行方法{}获取的值为空或不存在?", obj.toString(), getterMethod.getName());
				}
			}
			cacheKeyAndProValueMap.put(key, value);
		}
	}
}
