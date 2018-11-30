package com.xquant.platform.component.xcache.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;

public class XCacheMethodUtil {

	public static final String GETTER_MEHOTD_PREFIX_GET = "get";
	public static final String GETTER_MEHOTD_PREFIX_IS = "is";

	/**
	 * 获取一个类属性的getter方法
	 * 
	 * @param field
	 *            类属性
	 * @param cls
	 *            类类型
	 * @return 执行类属性getter方法
	 */
	public static Method acquireGetterMethod(Field field, Class<?> cls) {
		String fieldName = field.getName();
		Set<String> supportedName = new HashSet<String>(10);
		supportedName.add(GETTER_MEHOTD_PREFIX_GET + fieldName);
		supportedName.add(GETTER_MEHOTD_PREFIX_IS + fieldName);
		fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		supportedName.add(GETTER_MEHOTD_PREFIX_GET + fieldName);
		supportedName.add(GETTER_MEHOTD_PREFIX_IS + fieldName);
		for (String methodName : supportedName) {
			Method method = MethodUtils.getAccessibleMethod(cls, methodName);
			if (method != null) {
				return method;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		Method accessibleMethod = MethodUtils.getAccessibleMethod(XCacheNotifyObject.class, "getSql");
		String error = String.format("类 [%s] 中方法 [%s] 包含冲突的注解",XCacheNotifyObject.class.getName(),accessibleMethod.getName());
		System.out.println(error);
		
	}

}
