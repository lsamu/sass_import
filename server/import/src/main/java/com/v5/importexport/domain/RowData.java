package com.v5.importexport.domain;

import javax.persistence.Transient;
import java.util.*;

public class RowData {

	private Integer rowIndex;
	private Set<ColumnData> rowData = new HashSet<ColumnData>(0);
	private List<ColumnData> columnDataList;
	
	public RowData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RowData(Integer rowIndex, Set<ColumnData> rowData) {
		super();
		this.rowIndex = rowIndex;
		this.rowData = rowData;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Set<ColumnData> getRowData() {
		return rowData;
	}

	public void setRowData(Set<ColumnData> rowData) {
		this.rowData = rowData;
	}

	@Transient
	public List<ColumnData> getColumnDataList() {
		columnDataList = new ArrayList<ColumnData>(rowData);
    	
    	Collections.sort(columnDataList, new Comparator<ColumnData>() {

            @Override
            public int compare(ColumnData o1, ColumnData o2) {
				int diff = o1.getColumnIndex() - o2.getColumnIndex();
				if (diff > 0) {
					return 1;
				} else if (diff < 0) {
					return -1;
				}
				return 0; //相等为0
            }
            
        });
    	
		return columnDataList;
	}

	public void setColumnDataList(List<ColumnData> columnDataList) {
		this.columnDataList = columnDataList;
	}
	
	
}
