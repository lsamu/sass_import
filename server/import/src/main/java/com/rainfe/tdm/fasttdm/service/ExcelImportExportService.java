package com.rainfe.tdm.fasttdm.service;


import com.rainfe.tdm.fasttdm.domain.ColumnData;
import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;
import com.rainfe.tdm.fasttdm.domain.RowData;
import com.rainfe.tdm.fasttdm.domain.TableColumn;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

public interface ExcelImportExportService {

	/**
	 * 转换文件输入流为标准workbook
	 * 
	 * @param stream
	 *            文件输入流
	 * @return 标题行数据
	 */	
	Workbook createWorkbook(InputStream stream);
	
	/**
	 * 转换文件输入流为标准workbook
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param lstTitle
	 *            标题头          
	 * @return 标题行数据
	 */	
	Workbook createWorkbook(String sheetName, List<String> lstTitle);
	
	/**
	 * 从workbook中获取指定Sheet内容
	 * 
	 * @param workbook
	 *            标准workbook
	 * @param sheetIndex
	 *            sheet索引
	 * @return 标题行数据
	 */	
	Sheet getWorkSheet(Workbook workbook, int sheetIndex);
	
	/**
	 * 从sheet中获取数据标题行
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param startRowIndex
	 *            起始行索引
	 * @return 标题行数据
	 */	
	RowData getHeader(Sheet sheet, int startRowIndex);
	
	/**
	 * 从sheet中获取所有数据（不含标题行）
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param startRowIndex
	 *            单元格数据
	 * @return 数据集合
	 */	
	List<RowData> getData(Sheet sheet, int startRowIndex);
	
	/**
	 * 校验数据
	 * 
	 * @param columnData
	 *            列数据
	 * @param tableColumn
	 *            列属性信息
	 * @return 返回校验结果及错误信息
	 */	
	String CheckData(ColumnData columnData, TableColumn tableColumn);
	
	/**
	 * 写数据到excel中
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param lstRowData
	 *            行数据集合
	 * @param startRowIndex
	 *            写入起始位置索引
	 * @return 新增入库结果
	 */	
	Sheet writeDatatoExcel(Sheet sheet, List<RowData> lstRowData, int startRowIndex);

	/**
	 * 写数据到excel中
	 *
	 * @param sheet
	 *            标准sheet
	 * @param rowHeader
	 *            表头行数据
	 * @return 新增入库结果
	 */
	Sheet writeHeadertoExcel(Sheet sheet, RowData rowHeader);
	
	/**
	 * 验证Excel列
	 * 
	 * @param headerRow
	 *            标题行数据
	 * @param excelTemplate
	 *            模板列配置信息
	 * @return 新增入库结果
	 */	
	String ValidExcelColumn(RowData headerRow, ExcelTemplate excelTemplate);
	
	/**
	 * 获取指定字段对应信息
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param lstTableColumn
	 *            模型字段集合
	 * @return 指定字段信息
	 */	
	TableColumn getTableColumn(String fieldName, List<TableColumn> lstTableColumn);
	
	/**
	 * 获取单元格值对应的枚举code值
	 * 
	 * @param cellValue
	 *            单元格值
	 * @param enumValueStr
	 *            枚举字符串  name1;value1|name2;value2...
	 * @param type
	 *            返回值类型 name/value
	 * @return 指定字段信息
	 */	
	String getEnumValue(String cellValue, String enumValueStr, String type);
	
	/**
	 * 获取错误日志文件路径
	 * 
	 * @param rootDir
	 *            根目录
	 * @return 返回错误日志文件路径
	 */	
	String getErrorLogPath(String rootDir);
	
	/**
	 * 获取导出EXCEL工作簿对象
	 * 
	 * @param lstRowData
	 *            导出数据集合
	 * @param excelTemplate
	 *    excel模板
	 * @return 返回Workbook对象
	 */	
	Workbook getExportWorkbook(List<RowData> lstRowData, ExcelTemplate excelTemplate);
}
