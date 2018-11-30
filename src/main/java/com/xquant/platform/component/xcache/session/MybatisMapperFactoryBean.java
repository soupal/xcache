package com.xquant.platform.component.xcache.session;

import static org.springframework.util.Assert.notNull;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;

import com.xquant.platform.component.xcache.annotation.XCacheMapper;
import com.xquant.platform.component.xcache.annotation.XCacheMapperMethod;
import com.xquant.platform.component.xcache.enums.XCacheNotifyCommand;
import com.xquant.platform.component.xcache.invocation.XCacheInvocationHandler;

public class MybatisMapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

	private Class<T> mapperInterface;

	private boolean addToConfig = true;

	public MybatisMapperFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	/**
	 * If addToConfig is false the mapper will not be added to MyBatis. This means
	 * it must have been included in mybatis-config.xml.
	 * <p>
	 * If it is true, the mapper will be added to MyBatis in the case it is not
	 * already registered.
	 * <p>
	 * By default addToCofig is true.
	 *
	 * @param addToConfig
	 */
	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();

		notNull(this.mapperInterface, "Property 'mapperInterface' is required");

		Configuration configuration = getSqlSession().getConfiguration();
		if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
			try {
				configuration.addMapper(this.mapperInterface);
			} catch (Throwable t) {
				logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", t);
				throw new IllegalArgumentException(t);
			} finally {
				ErrorContext.instance().reset();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T getObject() throws Exception {
		XCacheMapper xcacheMapper = this.mapperInterface.getAnnotation(XCacheMapper.class);
		T ret = getSqlSession().getMapper(this.mapperInterface);
		if (xcacheMapper == null) {
			return ret;
		} else {
			Map<String, XCacheNotifyCommand> methodAndCommandMap = new HashMap<String, XCacheNotifyCommand>();
			Method[] methods = this.mapperInterface.getMethods();
			for (Method method : methods) {
				XCacheMapperMethod xCacheMapperMethod = method.getAnnotation(XCacheMapperMethod.class);
				if(xCacheMapperMethod!=null) {
					methodAndCommandMap.put(method.getName(), xCacheMapperMethod.notifyCommand());
				}
			}
			if(methodAndCommandMap.size() <= 0) {
				return ret;
			}
			return (T) Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{mapperInterface},
					new XCacheInvocationHandler(ret,methodAndCommandMap));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getObjectType() {
		return this.mapperInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSingleton() {
		return true;
	}

}
