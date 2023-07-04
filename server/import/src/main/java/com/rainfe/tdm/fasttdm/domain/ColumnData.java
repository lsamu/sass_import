package com.rainfe.tdm.fasttdm.domain;

/**
 * 列数据对象
 * @author zhaoxiaona
 * @version 1.0.0.0
 */
public class ColumnData {

	private String columnName;
	private String columnField;
	private String columnValue;
	private Integer columnIndex;
	private String dataType;
	
	public ColumnData() {
		super();
	}
	
	public ColumnData(ColumnData cd)
	{
		this.columnName = cd.getColumnName();
		this.columnField = cd.getColumnField();
		this.columnValue = cd.getColumnValue();
		this.columnIndex = cd.getColumnIndex();
		this.dataType = cd.getDataType();
	}

	public ColumnData(String columnName, String columnField, String columnValue, Integer columnIndex, String dataType) {
		super();
		this.columnName = columnName.trim();
		this.columnField = columnField.trim();
		this.columnValue = columnValue.trim();
		this.columnIndex = columnIndex;
		this.dataType = dataType.trim();
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName.trim();
	}

	public String getColumnField() {
		return columnField;
	}

	public void setColumnField(String columnField) {
		this.columnField = columnField.trim();
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue.trim();
	}

	public Integer getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
