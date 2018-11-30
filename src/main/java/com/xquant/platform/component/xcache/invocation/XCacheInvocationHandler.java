package com.xquant.platform.component.xcache.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.inteceptor.DefaultXCacheMapperInterceptor;
import com.xquant.platform.component.xcache.inteceptor.XCacheMapperInterceptor;

public class XCacheInvocationHandler implements InvocationHandler {

	private XCacheMapperInterceptor xCacheMapperInterceptor =new DefaultXCacheMapperInterceptor();

	private Object realObject;
	private Map<String, XCacheNotifyCommand> methodAndCommandMap;

	public XCacheInvocationHandler(Object realObject, Map<String, XCacheNotifyCommand> methodAndCommandMap) {
		this.realObject = realObject;
		this.methodAndCommandMap = methodAndCommandMap;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object obj = method.invoke(realObject, args);
		if (methodAndCommandMap.containsKey(method.getName())) {
			XCacheNotifyCommand xCacheNotifyCommand = methodAndCommandMap.get(method.getName());
			// 默认只处理包含主键的对象 该方法中只有一个参数对象
			xCacheMapperInterceptor.intercept(xCacheNotifyCommand, args[0]);
		}
		return obj;
	}
}
