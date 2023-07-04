package com.rainfe.tdm.fasttdm.service.datasource.ux;


import com.rainfe.tdm.fasttdm.domain.ColumnData;
import com.rainfe.tdm.fasttdm.domain.DynamicSQL;
import com.rainfe.tdm.fasttdm.domain.RowData;
import com.rainfe.tdm.fasttdm.domain.TableColumn;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UXImportExportDataSourceServiceImpl extends UXImportExportDataSourceService {

	private static final Logger logger = Logger.getLogger(UXImportExportDataSourceServiceImpl.class);
	
	@Override
	public boolean addImportData(String keyTable, List<RowData> lstRowData, Map<String,String> mapExtendField)
	{
		return true;
	}	
	
	@Override
	public List<TableColumn> getTableColumn(String keyTable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean checkRowDataUnique(String keyTable, Set<ColumnData> setColumnData, List<String> fieldList)
	{
		return true;
	}

	@Override
	public String checkCellData(String keyTable, ColumnData columnData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RowData> getExportData(String keyTable, List<String> lstQueryField, String condition, String orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueBySQL(String sql, Map<String, String> mapParameters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSQL(DynamicSQL dynamicSQL){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean CheckTableColumnUnique(String tableName, String fieldName, String fieldValue){
		return true;
	}

	@Override
	public boolean checkRowDataCrux(String keyTable, Set<ColumnData> setColumnData, List<String> fieldList) {
		return false;
	}

	/***
	 * 根据表名查询关键字段集合
	 * @return
	 */
	@Override
	public List<String> getCruxBysql(String keyTable) {

		return null;
	}
}
