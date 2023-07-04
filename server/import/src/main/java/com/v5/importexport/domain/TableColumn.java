package com.v5.importexport.domain;

public class TableColumn implements Cloneable {

	private String columnId;
	private String tableName;
	private String columnName;
	private String dataType;
	private Integer length;
	private Integer scale;
	private String isNull;
	
	public TableColumn() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override  
    public Object clone() {  
		TableColumn tc = null;  
        try{  
            tc = (TableColumn)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return tc;  
    }  
	
	public TableColumn(TableColumn tc)
	{
		this.tableName = tc.getTableName();
		this.columnName = tc.getColumnName();
		this.dataType = tc.getDataType();
		this.length = tc.getLength();
		this.scale = tc.getScale();
		this.isNull = tc.getIsNull();
		this.columnId = tc.getColumnId();
	}

	public TableColumn(String tableName, String columnName, String dataType, Integer length, Integer scale, String isNull, String columnId) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
		this.length = length;
		this.scale = scale;
		this.isNull = isNull;
		this.columnId = columnId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
}
