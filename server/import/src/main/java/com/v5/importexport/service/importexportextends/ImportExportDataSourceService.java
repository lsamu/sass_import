package com.v5.importexport.service.importexportextends;

import com.v5.importexport.domain.ColumnData;
import com.v5.importexport.domain.RowData;
import com.v5.importexport.domain.TableColumn;
import com.v5.importexport.domain.DynamicSQL;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ImportExportDataSourceService {

	/**
	 * 导入数据入库
	 * 
	 * @param keyTable
	 *            主表
	 * @param lstRowData
	 *            待入库数据集合
	 * @param mapExtendField
	 *            表扩展字段及值的集合（如创建人，数据状态等）
	 * @return 新增入库结果
	 */	
	boolean addImportData(String keyTable, List<RowData> lstRowData, Map<String,String> mapExtendField);
	
	/**
	 * 根据表名获取各个列属性信息
	 * 
	 * @param keyTable
	 *            数据表
	 * @return 表列信息集合
	 */	
	List<TableColumn> getTableColumn(String keyTable);
	
	/**
	 * 校验数据唯一性
	 * 
	 * @param keyTable
	 *            主表
	 * @param setColumnData
	 *            行数据
	 * @param fieldList
	 * 			  数据录入有效字段集合
	 * @return 新增入库结果
	 */	
	boolean checkRowDataUnique(String keyTable, Set<ColumnData> setColumnData, List<String> fieldList);
	
	/**
	 * 根据数据表字段类型校验数据有效性
	 * 
	 * @param keyTable
	 *            主表
	 * @param columnData
	 *            单元格数据
	 * @return 新增入库结果
	 */	
	String checkCellData(String keyTable, ColumnData columnData);
	
	/**
	 * 获取导出数据集合
	 * 
	 * @param keyTable
	 *            表名
	 * @param lstQueryField
	 *            查询字段集合
	 * @param condition
	 *            过滤条件（不带where关键字）
	 * @param orderby
	 *            结果集排序                                
	 * @return 行数据集合
	 */	
	List<RowData> getExportData(String keyTable, List<String> lstQueryField, String condition, String orderby);
	
	/**
	 * 根据动态SQL获取目标值
	 * 
	 * @param sql
	 *            动态sql
	 * @param mapParameters
	 *            参数【字段名和值映射】
	 * @return 行数据集合
	 */	
	String getValueBySQL(String sql, Map<String,String> mapParameters);
	
	/**
	 * 动态生成sql语句
	 * 
	 * @param dynamicSQL
	 *         动态sql对象
	 * @return sql语句
	 */	
	String getSQL(DynamicSQL dynamicSQL);

	/**
	 * 动态生成sql语句
	 *
	 * @param tableName
	 *         表名
	 * @param fieldName
	 *         字段显示名
	 * @param fieldValue
	 *         字段值
	 * @return sql语句
	 */
	boolean CheckTableColumnUnique(String tableName, String fieldName, String fieldValue);

	/**
	 * 通过关键字段查询是否存在重复数据
	 *
	 * @param keyTable
	 *            主表
	 * @param setColumnData
	 *            行数据
	 * @param keyFieldList
	 *            关键字段列表
	 * @return 是否存在重复数据
	 */
	boolean checkRowDataCrux(String keyTable, Set<ColumnData> setColumnData, List<String> keyFieldList);


	/**
	 * 获取指定表关键字段
	 *
	 * @param keyTable
	 *            主表
	 * @return 关键字段集合
	 */
	List<String> getCruxBysql(String keyTable);
}
