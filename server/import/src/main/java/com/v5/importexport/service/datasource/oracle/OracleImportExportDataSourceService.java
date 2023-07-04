package com.v5.importexport.service.datasource.oracle;

import com.v5.importexport.domain.ColumnData;
import com.v5.importexport.domain.RowData;
import com.v5.importexport.domain.TableColumn;
import com.v5.importexport.service.importexportextends.ImportExportDataSourceService;
import com.v5.importexport.domain.DynamicSQL;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class OracleImportExportDataSourceService implements ImportExportDataSourceService {

	private static final Logger logger = Logger.getLogger(OracleImportExportDataSourceService.class);
	
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
