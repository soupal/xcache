package com.xquant.platform.component.xcache.resource;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEvent;

import com.xquant.platform.component.xcache.dto.XCacheNotifyObject;

/**
 * 当生成一个XCacheNotifyObject进行事件发布 然后根据当前是否存在事务进行相应的处理
 * 
 * @project xquant-platform-component-xcache
 * @author guanglai.zhou
 * @date 2018年11月29日
 */
public class XCacheObjectCreateApplicationEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8138182484552254109L;

	private XCacheNotifyObject xCacheNotifyObject;

	public XCacheObjectCreateApplicationEvent(XCacheNotifyObject xCacheNotifyObject) {
		super(xCacheNotifyObject);
		this.xCacheNotifyObject = xCacheNotifyObject;
	}

	public XCacheNotifyObject getxCacheNotifyObject() {
		return xCacheNotifyObject;
	}
}
