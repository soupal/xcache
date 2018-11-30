package com.xquant.platform.component.xcache.dto;

public class XCacheSqlMetaData {

	private String sql;

	private Object[] args;

	private int[] argTypes;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public int[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(int[] argTypes) {
		this.argTypes = argTypes;
	}
}
