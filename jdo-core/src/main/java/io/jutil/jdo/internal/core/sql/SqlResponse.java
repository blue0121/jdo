package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.EntityMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
public class SqlResponse {
	private final EntityMetadata metadata;
	private final Map<String, Object> paramMap = new HashMap<>();
	private String sql;
	private boolean isForceVersion;
	private final List<String> nameList = new ArrayList<>();
	private final List<Object> paramList = new ArrayList<>();
	private final List<List<?>> batchParamList = new ArrayList<>();
	private final List<SqlParameter> parameterList = new ArrayList<>();
	private final List<List<SqlParameter>> batchParameterList = new ArrayList<>();

	public SqlResponse(EntityMetadata metadata) {
		this.metadata = metadata;
	}

	public EntityMetadata getMetadata() {
		return metadata;
	}

	public void putParam(String field, Object value) {
		paramMap.put(field, value);
	}

	public void removeParam(String field) {
		paramMap.remove(field);
	}

	public Map<String, Object> toParamMap() {
		return paramMap;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isForceVersion() {
		return isForceVersion;
	}

	public void setForceVersion(boolean forceVersion) {
		isForceVersion = forceVersion;
	}

	public void addName(String name) {
		nameList.add(name);
	}

	public List<String> toNameList() {
		return nameList;
	}

	public void addParam(Object param) {
		paramList.add(param);
	}

	public List<Object> toParamList() {
		return paramList;
	}

	public void addParameter(SqlParameter parameter) {
		parameterList.add(parameter);
	}

	public List<SqlParameter> toParameterList() {
		return parameterList;
	}

	public void addBatchParam(List<?> paramList) {
		batchParamList.add(paramList);
	}

	public List<List<?>> toBatchParamList() {
		return batchParamList;
	}

	public void addBatchParameter(List<SqlParameter> parameterList) {
		batchParameterList.add(parameterList);
	}

	public List<List<SqlParameter>> toBatchParameterList() {
		return batchParameterList;
	}

}
