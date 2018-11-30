package com.xquant.platform.component.xcache.inteceptor;

import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;

public interface XCacheMapperInterceptor {
	
	void intercept(XCacheNotifyCommand command,Object obj);
}
