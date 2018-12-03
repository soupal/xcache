package com.xquant.platform.component.xcache.inteceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;
import com.xquant.platform.component.xcache.dto.XCacheObjectMetaData;
import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.register.XCacheObjectMetaDataRegistry;
import com.xquant.platform.component.xcache.resolver.XCacheObjectMetaDataResolver;
import com.xquant.platform.component.xcache.resolver.XObjectToCacheObjectResolver;
import com.xquant.platform.component.xcache.resource.XCacheObjectCreatePublisher;

public class DefaultXCacheMapperInterceptor implements XCacheMapperInterceptor {

	private static Logger logger = LoggerFactory.getLogger(DefaultXCacheMapperInterceptor.class);
	
	@Override
	public void intercept(XCacheNotifyCommand command, Object obj) {
		XCacheObjectMetaData cacheObjectMetaData = getMetaData(obj);
		XCacheNotifyObject xCacheNotifyObject = new XCacheNotifyObject(command, cacheObjectMetaData);
		XObjectToCacheObjectResolver.resolveCacheKeyAndProperties(xCacheNotifyObject,obj);
		XCacheObjectCreatePublisher.publish(xCacheNotifyObject);
	}

	private XCacheObjectMetaData getMetaData(Object obj) {
		Class<?> cls = obj.getClass();
		XCacheObjectMetaData cacheObjectMetaData = XCacheObjectMetaDataRegistry.getCacheObjectMetaData(cls);
		if (cacheObjectMetaData == null) {
			synchronized (cls) {
				cacheObjectMetaData = XCacheObjectMetaDataRegistry.getCacheObjectMetaData(cls);
				if (cacheObjectMetaData == null) {
					logger.info("start processing cache metadata for class {}", cls.getName());
					long startTime = System.currentTimeMillis();
					cacheObjectMetaData = XCacheObjectMetaDataResolver.process4MetaData(cls);
					XCacheObjectMetaDataRegistry.register(cls, cacheObjectMetaData);
					long endTime = System.currentTimeMillis();
					logger.info("end processing cache metadata for class {},used time {} ms", cls.getName(), endTime - startTime);
				}
			}
		}
		if (cacheObjectMetaData == null) {
			throw new RuntimeException("无法获取缓存对象注解元数据" + cls.getName());
		}
		return cacheObjectMetaData;
	}
}
