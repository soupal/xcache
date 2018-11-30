package com.xquant.platform.component.xcache.enums;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存通知命令类型
 * 
 * @project xquant-platform-component-cache
 * @author guanglai.zhou
 * @date 2018年11月27日
 */
public enum XCacheNotifyCommand {
	/**
	 * 新增对象
	 */
	INSERT("01"),
	/**
	 * 全部更新对象
	 */
	UPDATE_ALL("02"),
	/**
	 * 部分更新对象
	 */
	UPDATE_PART("03"),
	/**
	 * 删除对象
	 */
	DELETE("04");

	private XCacheNotifyCommand(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	private static final Logger logger = LoggerFactory.getLogger(XCacheNotifyCommand.class);

	public static Map<String, XCacheNotifyCommand> map = new HashMap<String, XCacheNotifyCommand>();

	static {
		XCacheNotifyCommand[] _arr = XCacheNotifyCommand.values();
		for (int i = 0, _len = _arr.length; i < _len; i++) {
			XCacheNotifyCommand temp = _arr[i];
			map.put(temp.getValue(), temp);
		}
	}

	public static XCacheNotifyCommand getEnumName(String value) {
		if (StringUtils.isBlank(value)) {
			if (logger.isWarnEnabled()) {
				logger.warn(XCacheNotifyCommand.class.getName() + " pass argument null or \"\" in getEnumName method and ignored ");
			}
			return null;
		}
		if (map.containsKey(value)) {
			return map.get(value);
		}
		throw new IllegalArgumentException(XCacheNotifyCommand.class.getName() + " : 不支持的value值！" + value);
	}
}
