package com.xquant.platform.component.xcache.resolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.xquant.platform.component.xcache.annotation.XCacheKey;
import com.xquant.platform.component.xcache.annotation.XCacheObject;
import com.xquant.platform.component.xcache.annotation.XCacheProperty;
import com.xquant.platform.component.xcache.config.XCacheConfig;
import com.xquant.platform.component.xcache.dto.XCacheObjectMetaData;

/**
 * 缓存对象元数据解析器
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月28日
 */
public class XCacheObjectMetaDataResolver {

	private static Logger logger = LoggerFactory.getLogger(XCacheObjectMetaDataResolver.class);

	/**
	 * <li>进行指定类的缓存元注解信息的解析操作</li>
	 * <li>类必须包含注解{@link XCacheObject} 的信息 缺少重要信息则会抛出异常</li>
	 * <li>指定类方法中必须包含 {@link XCacheKey}注解 否则抛出异常</li>
	 * <li>执行类方法中必须包含 {@link XCacheProperty}注解 不包含仅仅日志提示</li>
	 * 
	 * @param cls
	 * @return
	 */
	public static XCacheObjectMetaData process4MetaData(Class<?> cls) {
		XCacheObject xCacheObject = cls.getAnnotation(XCacheObject.class);
		beforeCheck(xCacheObject, cls);
		Method[] methods = cls.getMethods();
		int mapSize = Math.max((int) (methods.length / XCacheConfig.DEFAULT_LOAD_FACTOR), 16);
		XCacheObjectMetaData metaData = newCacheObjectMetaData(cls, xCacheObject, mapSize);
		int numOfCacheKey = 0;
		int numOfCacheProperty = 0;
		Set<String> keyOrPropertyNameCache = new HashSet<String>();
		for (Method method : methods) {
			XCacheKey cacheKey = AnnotationUtils.findAnnotation(method, XCacheKey.class);
			XCacheProperty cacheProperty = AnnotationUtils.findAnnotation(method, XCacheProperty.class);
			if (cacheKey != null && cacheProperty != null) {
				throw new RuntimeException(String.format("类 [%s] 中方法 [%s] 包含冲突的注解,[%s] 与 [%s] 不可重复使用", cls.getName(), method.getName(),
						XCacheKey.class.getName(), XCacheProperty.class.getName()));
			}
			if (cacheKey != null) {
				checkAnnoName(cls, keyOrPropertyNameCache, cacheKey.name());
				metaData.getCacheKeyGetterMethods().put(cacheKey.name(), method);
				numOfCacheKey++;
			}
			if (cacheProperty != null) {
				checkAnnoName(cls, keyOrPropertyNameCache, cacheProperty.name());
				metaData.getCachePropertyGetterMthods().put(cacheProperty.name(), method);
				numOfCacheProperty++;
			}
		}
		keyOrPropertyNameCache.clear();
		metaData.setNumOfCacheKey(numOfCacheKey);
		metaData.setNumOfCacheProperty(numOfCacheProperty);
		afterCheck(cls, metaData);
		return metaData;
	}

	private static void beforeCheck(XCacheObject xCacheObject, Class<?> cls) {
		Assert.notNull(xCacheObject, String.format("类 [%s] 不包含注解 [%s] ,不需要进行缓存对象元数据解析", cls.getName(), XCacheObject.class.getName()));
		Assert.notNull(xCacheObject.cacheKeyExpression(),
				String.format("类 [%s] 中注解 [%s] 的参数cacheKeyExpression不允许为空", cls.getName(), XCacheObject.class.getName()));
		Assert.notNull(xCacheObject.cacheName(), String.format("类 [%s] 中注解 [%s] 的参数cacheName不允许为空", cls.getName(), XCacheObject.class.getName()));
		Assert.notNull(xCacheObject.funNum(), String.format("类 [%s] 中注解 [%s] 的参数funNum不允许为空", cls.getName(), XCacheObject.class.getName()));
		if (StringUtils.isBlank(xCacheObject.sql())) {
			logger.warn("annotation {} on type {} lack of value of property [sql], use default null", XCacheObject.class.getName(), cls.getName());
		}
	}

	private static XCacheObjectMetaData newCacheObjectMetaData(Class<?> cls, XCacheObject xCacheObject, int mapSize) {
		XCacheObjectMetaData metaData = new XCacheObjectMetaData();
		metaData.setCacheObjectClass(cls);
		metaData.setSql(StringUtils.isBlank(xCacheObject.sql()) ? null : xCacheObject.sql());
		metaData.setCacheKeyExpression(xCacheObject.cacheKeyExpression());
		metaData.setCacheName(xCacheObject.cacheName());
		metaData.setFunNum(xCacheObject.funNum());
		metaData.setCacheKeyGetterMethods(new HashMap<String, Method>(mapSize, XCacheConfig.DEFAULT_LOAD_FACTOR));
		metaData.setCachePropertyGetterMthods(new HashMap<String, Method>(mapSize, XCacheConfig.DEFAULT_LOAD_FACTOR));
		return metaData;
	}

	private static void checkAnnoName(Class<?> cls, Set<String> keyOrPropertyNameCache, String cacheKeyName) {
		if (keyOrPropertyNameCache.contains(cacheKeyName)) {
			throw new RuntimeException(String.format("类 [%s] 上注解 [%s] 和 [%s] 有重复属性值 [%s]", cls.getName(), XCacheKey.class.getName(),
					XCacheProperty.class.getName(), cacheKeyName));
		}
		keyOrPropertyNameCache.add(cacheKeyName);
	}

	private static void afterCheck(Class<?> cls, XCacheObjectMetaData metaData) {
		if (metaData.getNumOfCacheKey() <= 1) {
			throw new RuntimeException(String.format("类 [%s] 方法中不存在注解 [%s]?", cls.getName(), XCacheKey.class.getName()));
		}
		if (metaData.getNumOfCacheProperty() <= 1) {
			logger.warn("类{}不存在注解为{}的属性", cls.getName(), XCacheProperty.class.getName());
		}
	}
}
