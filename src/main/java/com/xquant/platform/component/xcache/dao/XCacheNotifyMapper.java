package com.xquant.platform.component.xcache.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("xcc_xcachenotifymapper")
public interface XCacheNotifyMapper {

	int insert(@Param("command") String command, @Param("cacheKey") String cacheKey, @Param("cacheName") String cacheName, @Param("funNum") String funNum,
			@Param("cacheObj") String cacheObj);

	int deleteAll();
	
	int countByKey(@Param("command") String command, @Param("cacheKey") String cacheKey);
}
