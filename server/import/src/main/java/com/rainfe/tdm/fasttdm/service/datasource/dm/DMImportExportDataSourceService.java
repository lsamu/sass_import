package com.rainfe.tdm.fasttdm.service.datasource.dm;


import com.rainfe.tdm.fasttdm.domain.ColumnData;
import com.rainfe.tdm.fasttdm.domain.DynamicSQL;
import com.rainfe.tdm.fasttdm.domain.RowData;
import com.rainfe.tdm.fasttdm.domain.TableColumn;
import com.rainfe.tdm.fasttdm.service.importexportextends.ImportExportDataSourceService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DMImportExportDataSourceService implements ImportExportDataSourceService {

	private static final Logger logger = Logger.getLogger(DMImportExportDataSourceService.class);
	
	@Override
	public abstract boolean addImportData(String keyTable, List<RowData> lstRowData, Map<String,String> mapExtendField);
	
	@Override
	public abstract List<TableColumn> getTableColumn(String keyTable);
	
	@Override
	public abstract boolean checkRowDataUnique(String keyTable, Set<ColumnData> setColumnData, List<String> fieldList);
	
	@Override
	public abstract String checkCellData(String keyTable, ColumnData columnData);
	
	@Override
	public abstract List<RowData> getExportData(String keyTable, List<String> lstQueryField, String condition, String orderby);
	
	@Override
	public abstract String getValueBySQL(String sql, Map<String,String> mapParameters);
	
	@Override
	public abstract String getSQL(DynamicSQL dynamicSQL);

	@Override
	public abstract boolean CheckTableColumnUnique(String tableName, String fieldName, String fieldValue);

	@Override
	public abstract List<String> getCruxBysql(String keyTable);
}
