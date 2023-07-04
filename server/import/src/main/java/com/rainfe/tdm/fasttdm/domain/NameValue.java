package com.rainfe.tdm.fasttdm.domain;


import com.rainfe.tdm.fasttdm.enums.DataType;

public class NameValue {

	private String name;
	private String value;
	private DataType dataType;
	
	public NameValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NameValue(String name, String value, DataType dataType) {
		super();
		this.name = name;
		this.value = value;
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	
}
