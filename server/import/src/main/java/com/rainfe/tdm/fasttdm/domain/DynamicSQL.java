package com.rainfe.tdm.fasttdm.domain;



import com.rainfe.tdm.fasttdm.enums.SQLType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DynamicSQL {

	private SQLType type;
	private String keyTable;
	private List<NameValue> nameValueList = new LinkedList<NameValue>();
	private List<String> queryFieldList = new LinkedList<String>();
	private Set<Condition> setCondition = new HashSet<Condition>();
	
	public DynamicSQL() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DynamicSQL(SQLType type, String keyTable, List<NameValue> nameValueList, List<String> queryFieldList,
			Set<Condition> setCondition) {
		super();
		this.type = type;
		this.keyTable = keyTable;
		this.nameValueList = nameValueList;
		this.queryFieldList = queryFieldList;
		this.setCondition = setCondition;
	}

	public SQLType getType() {
		return type;
	}

	public void setType(SQLType type) {
		this.type = type;
	}

	public String getKeyTable() {
		return keyTable;
	}

	public void setKeyTable(String keyTable) {
		this.keyTable = keyTable;
	}

	public List<NameValue> getNameValueList() {
		return nameValueList;
	}

	public void setNameValueList(List<NameValue> nameValueList) {
		this.nameValueList = nameValueList;
	}

	public List<String> getQueryFieldList() {
		return queryFieldList;
	}

	public void setQueryFieldList(List<String> queryFieldList) {
		this.queryFieldList = queryFieldList;
	}

	public Set<Condition> getSetCondition() {
		return setCondition;
	}

	public void setSetCondition(Set<Condition> setCondition) {
		this.setCondition = setCondition;
	}
	
}
