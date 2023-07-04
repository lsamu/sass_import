package com.rainfe.tdm.fasttdm.service.impl.excel;

import cn.hutool.core.date.DateTime;
import com.rainfe.tdm.fasttdm.domain.*;
import com.rainfe.tdm.fasttdm.service.ExcelImportExportService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelImportExportServiceImpl implements ExcelImportExportService
{
	private static final Logger logger = Logger.getLogger(ExcelImportExportServiceImpl.class);
	
	/**
	 * 转换文件输入流为标准workbook
	 * 
	 * @param stream
	 *            文件输入流
	 * @return 标题行数据
	 */	
	@Override
	public Workbook createWorkbook(InputStream stream)
	{
		try
		{
			Workbook workbook = WorkbookFactory.create(stream);
			return workbook;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
	}
	
	/**
	 * 转换文件输入流为标准workbook
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param lstTitle
	 *            标题头          
	 * @return 标题行数据
	 */
	@Override
	public Workbook createWorkbook(String sheetName, List<String> lstTitle)
	{
		Workbook wb = new SXSSFWorkbook();
		try
		{			
		    Sheet sheet = null;
	
		    // 对每个表生成一个新的sheet,并以表名命名
		    if (StringUtils.isBlank(sheetName)) {
		      sheetName = "sheet1";
		    }
		    sheet = wb.createSheet(sheetName);
		    sheet.setDefaultColumnWidth(10 * 256);
		    // 设置表头的说明
		    if(lstTitle != null && lstTitle.size() > 0)
		    {
			    Row topRow = sheet.createRow(0);
			    for (int i = 0; i < lstTitle.size(); i++) {
			    	Cell cell = topRow.createCell(i);
			    	//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellType(CellType.STRING);
			    	cell.setCellValue(lstTitle.get(i));		      
			    	sheet.setColumnWidth(i, lstTitle.get(i).getBytes().length * 258);
			    }
		    }

		}
		catch(Exception e) {
			logger.error("执行异常"+e.getMessage(),e);
		}
	    
	    return wb;
	}
	
	/**
	 * 从workbook中获取指定Sheet内容
	 * 
	 * @param workbook
	 *            标准workbook
	 * @param sheetIndex
	 *            sheet索引
	 * @return 标题行数据
	 */
	@Override
	public Sheet getWorkSheet(Workbook workbook, int sheetIndex)
	{
		try
		{
			Sheet sheet = workbook.getSheetAt(sheetIndex);	
			return sheet;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
	}
	
	/**
	 * 从sheet中获取数据标题行
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param startRowIndex
	 *            起始行索引
	 * @return 标题行数据
	 */	
	@Override
	public RowData getHeader(Sheet sheet, int startRowIndex)
	{
		try
		{
			RowData rowData = new RowData();
			Set<ColumnData> setColumnData = new HashSet<ColumnData>();
									
			Row headerRow = sheet.getRow(startRowIndex);
			
			int columnIndex = 0;
			List<String> lstColumnName = new LinkedList<String>();
			for(Cell cell : headerRow) 
			{
				ColumnData columnData = new ColumnData();
				//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellType(CellType.STRING);
				String cellValue = cell.getStringCellValue();
				if(StringUtils.isNotBlank(cellValue) && !lstColumnName.contains(cellValue))
				{
					lstColumnName.add(cellValue);
					
					columnData.setColumnValue(cellValue);
				    columnData.setColumnName(cellValue);
				    columnData.setColumnIndex(columnIndex);
				    setColumnData.add(columnData);				    
				}
				
				++columnIndex;
			}
			
			rowData.setRowData(setColumnData);
			rowData.setRowIndex(startRowIndex);
			
			return rowData;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
	}
	
	/**
	 * 从sheet中获取所有数据（不含标题行）
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param startRowIndex
	 *            单元格数据
	 * @return 数据集合（自动过滤重复数据）
	 */	
	@Override
	public List<RowData> getData(Sheet sheet, int startRowIndex)
	{
		try
		{
			List<RowData> lstRowData = new LinkedList<RowData>();		
			List<String> lstRowDataStr = new LinkedList<String>();
			int lastRowIndex = sheet.getLastRowNum();
			for(int i = startRowIndex; i <= lastRowIndex; i++)
			{
				Row dataRow = sheet.getRow(i);
		        if (dataRow == null) { 
		        	continue; 
		        }
		        
		        if(IsEmptyRow(dataRow))
		        {
		        	continue;
		        }
		        
				RowData rowData = new RowData();
				Set<ColumnData> setColumnData = new HashSet<ColumnData>();
				//列索引和单元格值的映射
				Map<Integer,String> map = new HashMap<Integer,String>(); 

			//	int preColumnIndex = 0;
				for(Cell cell : dataRow) 
				{
					String cellValue = getCellValue(cell);
					int columnIndex = cell.getColumnIndex();
//					if(columnIndex > preColumnIndex + 1)
//					{
//						for(int k = preColumnIndex +1; k < columnIndex; k++)
//						{
//							ColumnData columnData = new ColumnData();
//							columnData.setColumnValue("");
//							columnData.setColumnIndex(k);
//
//							setColumnData.add(columnData);
//							map.put(k,"");
//						}
//					}

					ColumnData columnData = new ColumnData();
					columnData.setColumnValue(cellValue);
					columnData.setColumnIndex(columnIndex);
					setColumnData.add(columnData);

					map.put(columnIndex,cellValue);

				//	preColumnIndex = columnIndex;
				}
				
				rowData.setRowData(setColumnData);
				rowData.setRowIndex(startRowIndex);
				
				String rowDataStr = map.toString();
				if(!lstRowDataStr.contains(rowDataStr))
				{
					//过滤重复数据
					lstRowDataStr.add(rowDataStr);
					lstRowData.add(rowData);
				}
			}
			
			return lstRowData;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
	}
	
	/**
	 * 校验数据
	 * 
	 * @param columnData
	 *            列数据
	 * @param tableColumn
	 *            列属性信息
	 * @return 返回校验结果及错误信息
	 */	
	@Override
	public String CheckData(ColumnData columnData, TableColumn tableColumn)
	{
		try
		{			
			String fieldValue = columnData.getColumnValue();
			
			String dataType = tableColumn.getDataType();
			Integer dataLength = tableColumn.getLength();			
			Integer dataScale = tableColumn.getScale();
			String isNull = tableColumn.getIsNull();
			
			String result = "true|" + fieldValue;
			switch(dataType)
			{
				case "StringType":
					boolean isBlank = StringUtils.isBlank(fieldValue);
					if(isNull.equals("false") && isBlank)
					{
						result = "false|" + "不允许为空！";
					}
					else
					{
						if(fieldValue.getBytes().length > dataLength)
						{
							//按照一个汉字两个字节计算
							result = "false|"+ "最多只能输入" + dataLength/2 + "个汉字！";
						}
						else
						{
							result = "true|" + fieldValue;
						}
					}
					break;
				case "NumberType":
					if(dataScale == 0)
					{
						//说明数字为整形
						result = ConvertDataToInt(fieldValue);
						if(result.indexOf("true|") != -1)
						{
							result = result.replace("true|", "");
							if(result.length() > dataLength)
							{
								result = "false|" + result + "最大有效数字长度为" + dataLength;
							}
						}
					}
					else
					{
						//说明小数位浮点型
						result = ConvertDataToDouble(fieldValue);
						if(result.indexOf("true|") != -1)
						{
							result = result.replace("true|", "");
							String[] arr = result.split("\\.");
							if(arr[1].length() > dataScale)
							{
								result = "false|" + result + "最大保留小数长度为" + dataScale;
							}
							else if(arr[0].length() > dataLength-dataScale) {
								result = "false|" + result + "最大有效数字长度为" + dataLength;
							}
						}
					}
					break;
				case "DATETIME":
				case "DATE":
				case "DatetimeType":
					result = ConvertDataToDate(fieldValue);					
					break;
			}
			
			return result;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 写数据到excel中
	 * 
	 * @param sheet
	 *            标准sheet
	 * @param lstRowData
	 *            行数据集合
	 * @param startRowIndex
	 *            写入起始位置索引
	 * @return 写数据到excel中
	 */	
	@Override
	public Sheet writeDatatoExcel(Sheet sheet, List<RowData> lstRowData, int startRowIndex)
	{
		try
		{
			Integer columnIndex = 0;
			for(RowData rowData : lstRowData) {
				Row dataRow = sheet.createRow(startRowIndex);
				List<ColumnData> lstColumnData = rowData.getColumnDataList();
				for (ColumnData cd : lstColumnData) {
					String columnName = cd.getColumnName();
					if (StringUtils.isBlank(columnName)) {
						continue;
					}

					String columnValue = cd.getColumnValue();

					columnIndex = cd.getColumnIndex();
					Cell cell = dataRow.createCell(columnIndex);
					//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(columnValue);
				}

				++startRowIndex;
			}
		}
		catch(Exception e)
		{
			logger.error("写Excel执行异常"+e.getMessage(),e);
		}
		
		return sheet;
	}

	/**
	 * 写表头到excel中
	 * @param sheet
	 *            标准sheet
	 * @param rowHeader
	 *            表头行数据
	 * @return
	 */
	@Override
	public Sheet writeHeadertoExcel(Sheet sheet, RowData rowHeader){
		Row headerRow = sheet.createRow(0);
		List<ColumnData> lstColumnData = rowHeader.getColumnDataList();
		for (ColumnData cd : lstColumnData) {
			String columnName = cd.getColumnName();
			if (StringUtils.isBlank(columnName)) {
				continue;
			}

			Integer columnIndex = cd.getColumnIndex();
			Cell cell = headerRow.createCell(columnIndex);
			//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(columnName);
			if (StringUtils.isNotBlank(columnName)) {
				int width = columnName.getBytes().length * 258;
				int minWidth = 15 * 258;
				width = width < minWidth ? minWidth : width;
				sheet.setColumnWidth(columnIndex, width);
			}
		}

		return sheet;
	}
	
	/**
	 * 验证Excel列
	 * 
	 * @param headerRow
	 *            标题行数据
	 * @param excelTemplate
	 *            模板列配置信息
	 * @return 新增入库结果
	 */	
	@Override
	public String ValidExcelColumn(RowData headerRow, ExcelTemplate excelTemplate)
	{
		String result = "";
		try
		{
			String strErrorMsg = "";
			//模板列集合
			List<String> lstHeader = new LinkedList<String>();
			//缺失列集合
			List<String> lstColumn = new LinkedList<String>();
			//重复列集合
			List<String> lstRepeatColumn = new LinkedList<String>();
			Set<ColumnData> setColumnData = headerRow.getRowData();
			
			Iterator<ColumnData> iterator = setColumnData.iterator();
	        while (iterator.hasNext()) 
	        {
	            ColumnData cd = iterator.next();
	            String columnName = cd.getColumnName();
	            
	            if(lstHeader.contains(columnName))
	            {
	            	lstRepeatColumn.add(columnName);
	            	continue;
	            }
	            else
	            {
	            	lstHeader.add(columnName);
	            }
	        }
	        
	        if(lstRepeatColumn.size() > 0)
	        {
	        	strErrorMsg += "模板存在重复列[" +StringUtils.join(lstRepeatColumn, ",")+ "];";
	        }
	        
	        //系统自动生成列
	        List<String> lstDataSourceType = Arrays.asList("BusinessCode", "Constant", "CurrentDate", "CurrentUser");
			List<ExcelTemplateItem> lstExcelTemplateItem = excelTemplate.getExcelTemplateItemList();
			for(ExcelTemplateItem item : lstExcelTemplateItem)
			{
				String excelColumn = item.getExcelcolumn();	
				String dataSource = item.getDatasource();
				if(!lstHeader.contains(excelColumn) && !lstDataSourceType.contains(dataSource))
				{
					lstColumn.add(excelColumn);   
				}
			}
			
			if(lstColumn.size() > 0)
	        {
	        	strErrorMsg += "模板缺失列[" +StringUtils.join(lstColumn, ",")+ "];";
	        }
			
			if(StringUtils.isNotBlank(strErrorMsg))
			{
				result = strErrorMsg;
			}
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			result = "模板列验证异常！";
		}
		
		return result;
	}
	
	/**
	 * 获取指定字段对应信息
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param lstTableColumn
	 *            模型字段集合
	 * @return 指定字段信息
	 */	
	@Override
	public TableColumn getTableColumn(String fieldName, List<TableColumn> lstTableColumn)
	{
		TableColumn tableColumn = null;
		try
		{
			Iterator<TableColumn> iterator = lstTableColumn.iterator();
	        while (iterator.hasNext()) 
	        {
	        	TableColumn tc = iterator.next();
	            String columnName = tc.getColumnName();
	            
	            if(fieldName.equals(columnName))
	            {
	            	//耗性能
	            	//tableColumn = (TableColumn)tc.clone(); 
	            	tableColumn = new TableColumn(tc);
	            	break;
	            }
	        }
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
        
        return tableColumn;
	}
	
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
	@Override
	public String getEnumValue(String cellValue, String enumValueStr, String type)
	{
		String strNameValue = "";
		try
		{
	        if (StringUtils.isNotBlank(enumValueStr))
	        {
	            String[] arrEnumNameValue = enumValueStr.split("\\|");
	            for (String enumNameValue : arrEnumNameValue)
	            {
	            	if(type.equals("value"))
	            	{
		            	String name = enumNameValue.split(";")[0];
		                if (name.trim().equalsIgnoreCase(cellValue))
		                {
		                	strNameValue = enumNameValue.split(";")[1];
		                    break;
		                }
	            	}
	            	else
	            	{
	            		String value = enumNameValue.split(";")[1];
		                if (value.trim().equalsIgnoreCase(cellValue))
		                {
		                	strNameValue = enumNameValue.split(";")[0];
		                    break;
		                }
	            	}
	            }
	        }
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}

        return strNameValue;
	}
	
	/**
	 * 获取错误日志文件路径
	 * 
	 * @param rootDir
	 *        根目录    
	 * @return 返回错误日志文件路径
	 */	
	@Override
	public String getErrorLogPath(String rootDir)
	{
		String filePath = rootDir;
		try
		{
			String tempDir = rootDir + "tempdirectory";
			File file = new File(tempDir);
	        if (!file.exists()) {
	            file.mkdirs();
	        } 
	        
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	        String fileName = "错误日志_"+format.format(new Date())+".xlsx";
		    filePath= tempDir +"\\"+ fileName;
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
       
		return filePath;
	}
	
	/**
	 * 获取导出EXCEL工作簿对象
	 * 
	 * @param lstRowData
	 *            导出数据集合
	 * @param excelTemplate
	 *    excel模板
	 * @return 返回Workbook对象
	 */	
	@Override
	public Workbook getExportWorkbook(List<RowData> lstRowData, ExcelTemplate excelTemplate)
	{
		Workbook workbook = null;
		try
		{
			List<ExcelTemplateItem> lstExcelTemplateItem = excelTemplate.getExcelTemplateItemList();
			for(RowData rowData : lstRowData)
			{
				Set<ColumnData> setColumnData = rowData.getRowData();
				Iterator<ColumnData> iterator = setColumnData.iterator();
		        while (iterator.hasNext()) 
		        {
		            ColumnData cd = iterator.next();
		            String columnField = cd.getColumnField();
		            
		            Optional<ExcelTemplateItem> itemOptional = lstExcelTemplateItem.stream().filter(o->o.getTablecolumn().equals(columnField)).findFirst();
		            if(itemOptional.isPresent())
		            {
		            	ExcelTemplateItem item = itemOptional.get();
		            	String columnName = item.getExcelcolumn();
		            	Integer columnIndex = item.getExcelcolumnindex();
		            	
		            	cd.setColumnName(columnName);
		            	cd.setColumnIndex(columnIndex);
		            }
		        }
			}
			
			String templateName = excelTemplate.getName();
			workbook = createWorkbook(templateName, null);
			Sheet sheet = getWorkSheet(workbook, 0);
			sheet = writeHeadertoExcel(sheet, lstRowData.get(0));
			writeDatatoExcel(sheet, lstRowData, 0);
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return workbook;
	}
	
	/**
	 * 校验数据行是否为空行
	 * 
	 * @param dataRow
	 *            数据行
	 * @return true or false
	 */	
	private boolean IsEmptyRow(Row dataRow) 
	{
		boolean isEmptyRow = true;
		try
		{
			int lastColumnIndex = dataRow.getLastCellNum();
			for(int i = 0; i <= lastColumnIndex; i++)
			{
				Cell currentCell = dataRow.getCell(i);
				if(currentCell != null && StringUtils.isNotBlank(currentCell.toString()))
				{
					isEmptyRow = false;
					break;
				}
			}
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}		
		
		return isEmptyRow;	
	}
	
	/**
	 * 获取单元格值
	 * 
	 * @param cell
	 *            单元格
	 * @return 单元格值
	 */	
	private String getCellValue(Cell cell)
	{
		String value = "";
//		int cellType = cell.getCellType();
		CellType cellType = cell.getCellType();
//		if(cellType == HSSFCell.CELL_TYPE_NUMERIC)
		if(cellType == CellType.NUMERIC)
		{
			// 数字
			if (HSSFDateUtil.isCellDateFormatted(cell)) 
			{      
	             //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
	             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	             value=sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();                                 	             
			} 
			else 
			{
				String cellstr = cell.toString();
//				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellType(CellType.STRING);
				value = cell.getStringCellValue();
				if(value.indexOf(".") > -1) {
					value = cellstr;
				}
			}
		}
//		else if(cellType == HSSFCell.CELL_TYPE_STRING)
		else if(cellType == CellType.STRING)
		{
			// 字符串
			value = cell.getStringCellValue();
		}
//		else if(cellType == HSSFCell.CELL_TYPE_BOOLEAN)
		else if(cellType == CellType.BOOLEAN)
		{
			// Boolean
			value = cell.getBooleanCellValue() + "";
		}
//		else if(cellType == HSSFCell.CELL_TYPE_FORMULA)
		else if(cellType == CellType.FORMULA)
		{
			// 公式
			value = cell.getCellFormula() + "";
		}
//		else if(cellType == HSSFCell.CELL_TYPE_BLANK)
		else if(cellType == CellType.BLANK)
		{
			// 空值
			value = "";
		}
//		else if(cellType == HSSFCell.CELL_TYPE_ERROR)
		else if(cellType == CellType.ERROR)
		{
			// 故障
			value = "非法字符";
		}
		else
		{
			value = "未知类型";
		}
		
		return value;
	}
	
	/**
	 * 转换字符串为整数
	 * 
	 * @param value
	 *            字符串值
	 * @return 转换结果
	 */	
	private String ConvertDataToInt(String value)
	{
		String result = "";
		try
		{
			if (value != null && StringUtils.isNotBlank(value.trim()))
            {
				Integer v = Double.valueOf(value).intValue(); 
				result = v.toString();               
            }
			
			result = "true|" + result;
		}
		catch(Exception e)
		{
			result = "false|" + value + "转换整数失败！";
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return result;
	}
	
	
	/**
	 * 转换字符串为日期
	 * 
	 * @param value
	 *            字符串值
	 * @return 转换结果
	 */	
	private String ConvertDataToDate(String value)
	{
		String result = "";
		try
		{
			if (value != null && StringUtils.isNotBlank(value.trim()))
            {
				result = DateTime.of(value, "yyyy-MM-dd").toString();				            
            }
			
			result = "true|" + result;
		}
		catch(Exception e)
		{
			result = "false|" + value + "转换日期失败！";
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return result;
	}
	
	/**
	 * 转换字符串为小数
	 * 
	 * @param value
	 *            字符串值
	 * @return 转换结果
	 */	
	private String ConvertDataToDouble(String value)
	{
		String result = "";
		try
		{
			if (value != null && StringUtils.isNotBlank(value.trim()))
            {
				Double v = Double.valueOf(value);
				result = v.toString();               
            }
			
			result = "true|" + result;
		}
		catch(Exception e)
		{
			result = "false|" + value + "转换小数失败！";
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return result;
	}
}
