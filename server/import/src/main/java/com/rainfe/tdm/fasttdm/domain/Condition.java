package com.rainfe.tdm.fasttdm.domain;


import com.rainfe.tdm.fasttdm.enums.DataType;
import com.rainfe.tdm.fasttdm.enums.OptionType;

public class Condition {

	private String fieldName;
	private OptionType option;
	private String fieldValue;
	private DataType dataType;
	
	public Condition() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Condition(String fieldName, OptionType option, String fieldValue, DataType dataType) {
		super();
		this.fieldName = fieldName;
		this.option = option;
		this.fieldValue = fieldValue;
		this.dataType = dataType;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public OptionType getOption() {
		return option;
	}
	public void setOption(OptionType option) {
		this.option = option;
	}
	
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}	
}
