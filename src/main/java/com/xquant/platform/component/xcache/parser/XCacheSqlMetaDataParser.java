package com.xquant.platform.component.xcache.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

import com.xquant.platform.component.xcache.dto.XCacheSqlMetaData;

public class XCacheSqlMetaDataParser {

	private static final ThreadLocal<List<Object>> sqlObjListThreadLocal = new ThreadLocal<List<Object>>();

	private static final String WILD_CARD = "?";

	/**
	 * 进行sql占位符解析 将占位符中的占位替换为"?" 保存参数值以及类型到数组中  
	 * 
	 * @param sql
	 * @param parmMap
	 * @return
	 */
	public static XCacheSqlMetaData parse(String sql, Map<String, Object> parmMap) {
		try {
			XCacheSqlMetaData sqlMetaData = new XCacheSqlMetaData();
			VariableTokenHandler handler = new VariableTokenHandler(parmMap);
			GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
			sqlObjListThreadLocal.set(new ArrayList<Object>());
			String preparedSql = parser.parse(sql);
			sqlMetaData.setSql(preparedSql);
			List<Object> objectList = sqlObjListThreadLocal.get();
			sqlMetaData.setArgs(objectList.toArray(new Object[objectList.size()]));
			return sqlMetaData;
		} finally {
			sqlObjListThreadLocal.remove();
		}

	}

	private static class VariableTokenHandler implements TokenHandler {
		private Map<String, Object> map;

		public VariableTokenHandler(Map<String, Object> map) {
			this.map = map;
		}

		public String handleToken(String content) {
			if (map != null && map.containsKey(content)) {
				sqlObjListThreadLocal.get().add(map.get(content));
				return WILD_CARD;
			}
			throw new RuntimeException("无法进行占位符信息" + content + "的解析");
		}
	}

}
