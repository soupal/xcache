package com.xquant.platform.component.xcache.parser;

import java.util.Map;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

public class XCachePlaceHolderParser {

	public static String parse(String str, Map<String, Object> map) {
		VariableTokenHandler handler = new VariableTokenHandler(map);
		GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
		return parser.parse(str);
	}
	
	private static class VariableTokenHandler implements TokenHandler {
		private Map<String, Object> map;

		public VariableTokenHandler(Map<String, Object> map) {
			this.map = map;
		}

		public String handleToken(String content) {
			if (map != null && map.containsKey(content)) {
				return String.valueOf(map.get(content));
			}
			throw new RuntimeException("无法进行占位符信息" + content + "的解析");
			// return "#{" + content + "}";
		}
	}
}