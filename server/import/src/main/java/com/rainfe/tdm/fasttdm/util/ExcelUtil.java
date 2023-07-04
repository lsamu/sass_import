package com.rainfe.tdm.fasttdm.util;

import com.rainfe.mapletr.dps.webcontext.WebContext;
import com.rainfe.tdm.fasttdm.domain.*;
import com.rainfe.tdm.fasttdm.enums.DataType;
import com.rainfe.tdm.fasttdm.enums.OptionType;
import com.rainfe.tdm.fasttdm.enums.SQLType;
import com.rainfe.tdm.fasttdm.domain.*;
import com.rainfe.tdm.fasttdm.service.ExcelImportExportService;
import com.rainfe.tdm.fasttdm.domain.*;
import com.rainfe.tdm.fasttdm.service.ImportExportTemplateService;
import com.rainfe.tdm.fasttdm.service.importexportextends.ImportExportDataSourceService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ExcelUtil {

	private  final Logger logger = Logger.getLogger(ExcelUtil.class);
	
	@Autowired
	private WebContext webContext;
	
	@Autowired
	private ImportExportDataSourceService dataSourceService;
	
	@Autowired
	private ExcelImportExportService excelService;
	
	@Autowired
	private ImportExportTemplateService templateService;
	
	public  String ImportExcel(InputStream stream, String templateCode, HttpServletRequest request)
	{
		String result = "";
		try
		{		
			Workbook workbook = excelService.createWorkbook(stream);
			Sheet sheet = excelService.getWorkSheet(workbook, 0);
			RowData rowHeader = excelService.getHeader(sheet, 0);
			
			ExcelTemplate excelTemplate = new ExcelTemplate();
			excelTemplate = templateService.findExcelTemplateByCode(templateCode);
				
			String strValidMsg = excelService.ValidExcelColumn(rowHeader, excelTemplate);
			if(StringUtils.isNotBlank(strValidMsg))
			{
				result = "0|" + strValidMsg;
			}
			else
			{
				//模板验证通过，开始入库数据。
				List<RowData> errorRowDataList = new LinkedList<RowData>();
				List<RowData> rowDataList = excelService.getData(sheet, 1);
				errorRowDataList = ImportData(rowHeader, rowDataList, excelTemplate);	
				if(errorRowDataList.size() > 0)
				{
					Workbook workbook_error = excelService.createWorkbook("", new LinkedList<String>());
					Sheet sheet_error = excelService.getWorkSheet(workbook_error, 0);
					ColumnData cd = new ColumnData();
					cd.setColumnIndex(rowHeader.getRowData().size());
					cd.setColumnName("错误信息");
					rowHeader.getRowData().add(cd);
					sheet_error = excelService.writeHeadertoExcel(sheet_error, rowHeader);
					sheet_error = excelService.writeDatatoExcel(sheet_error, errorRowDataList, 0);
					
					String rootDir = request.getRealPath("/");
					String filePath= excelService.getErrorLogPath(rootDir);
					
		            //输出错误日志文件到指定路径  
		            FileOutputStream output= new FileOutputStream(filePath);  
		            workbook_error.write(output);
		            output.close();
		            
		            result = "1|" + filePath;
				}
				else
				{
					result = "2|导入成功！";
				}
			}
		}
		catch(IOException e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			result = "-1|数据导入发生异常！";
		}
		
		return result;
	}
	
	private  List<RowData> ImportData(RowData rowHeader, List<RowData> rowDataList, ExcelTemplate excelTemplate)	
	{
		List<RowData> errorRowData = new LinkedList<RowData>();
		try
		{
			String keyTable = excelTemplate.getKeytable();
			List<ExcelTemplateItem> lstExcelTemplateItem = excelTemplate.getExcelTemplateItemList();
			List<TableColumn> lstTableColumn = dataSourceService.getTableColumn(keyTable);
			
			List<RowData> finalRowData = new LinkedList<RowData>();
			
			//查找指定字段对应到输入文件中的单元格列索引
			Set<ColumnData> setHeaderColumn = rowHeader.getRowData();
			for(ExcelTemplateItem item : lstExcelTemplateItem)
			{
				String excelColumn = item.getExcelcolumn();		
				Iterator<ColumnData> iterator = setHeaderColumn.iterator();
		        while (iterator.hasNext()) 
		        {
		            ColumnData cd = iterator.next();
		            String columnName = cd.getColumnName();
		            int excelColumnIndex = cd.getColumnIndex();
		            if(excelColumn.equals(columnName))
		            {
		            	item.setExcelcolumnindex(excelColumnIndex);
		            	break;
		            }	          
		        }
			}
			
			for(RowData rd : rowDataList)
			{
				String strErrMsg = "";
				Set<ColumnData> setDataColumn = rd.getRowData();
				for(ExcelTemplateItem item : lstExcelTemplateItem)
				{			
					String errMsg = "";
					String dataSourceType = item.getDatasource();
					String dataSourceCode = item.getDatasourcecode();
					String excelColumn = item.getExcelcolumn();
					String tableColumn = item.getTablecolumn();
					String associatedTable = item.getAssociatetable();
					String associatedFieldID = item.getAssociatefieldid();
					int excelColumnIndex = item.getExcelcolumnindex();
					TableColumn tc = excelService.getTableColumn(tableColumn, lstTableColumn);
					
					String value = "";
					ColumnData columnData = null;
			        Iterator<ColumnData> iterator = setDataColumn.iterator();
			        while (iterator.hasNext()) 
			        {
			            ColumnData cd = iterator.next();
			            String columnValue = cd.getColumnValue();
			            int columnIndex = cd.getColumnIndex();
			            if(excelColumnIndex == columnIndex)
			            {		 
			            	if(StringUtils.isNotBlank(dataSourceType) && StringUtils.isNotBlank(dataSourceCode)) 
			            	{
			            		value = getValueByDataSourceType(columnValue,dataSourceType,dataSourceCode);
			            	}
			            	else if(StringUtils.isNotBlank(associatedTable) && StringUtils.isNotBlank(associatedFieldID))
			            	{
			            		value = getAssociatedValue(columnValue, associatedTable, associatedFieldID);
			            	}
			            	else
			            	{
			            		value = columnValue;
			            	}
			            	
			            	cd.setColumnName(excelColumn);
			            	if(StringUtils.isNotBlank(columnValue) && StringUtils.isBlank(value))
			            	{			            		
			            		errMsg += "值无效，系统中不存在;";
			            	}
			            	else
			            	{
				            	cd.setColumnField(tableColumn);
				            	cd.setColumnValue(value);
				            	cd.setDataType(tc.getDataType());
				            	
				            	if(cd != null)
						        {					        	
						        	String result = excelService.CheckData(cd, tc);
						        	if(StringUtils.isNotBlank(result))
						        	{
						        		if(result.indexOf("false|") != -1)
						        		{
						        			errMsg += result.replace("false|", "");
						        		}
						        		else
						        		{
						        			String valueAfterFormat = result.replace("true|", "");
						        			cd.setColumnValue(valueAfterFormat);
						        		}
						        	}
						        }
			            	}
			            	break;
			            }	            
			        }			      			        
			        
			        if(StringUtils.isNotBlank(errMsg))
			        {
			        	strErrMsg += "["+excelColumn+"]列"+errMsg;
			        }
				}
				
				
				if(StringUtils.isNotBlank(strErrMsg))
				{
					ColumnData cd = new ColumnData();
					cd.setColumnName("错误信息");
					cd.setColumnValue(strErrMsg);
					Integer index = setDataColumn.size();
					cd.setColumnIndex(index);
					
					setDataColumn.add(cd);
					RowData errRD = new RowData();
					errRD.setRowData(setDataColumn);
					
					errorRowData.add(rd);
					
					continue;
				}
				
				//当校验不出错的时，判断数据的唯一性
				List<String> fieldList = lstExcelTemplateItem.stream().map(a -> a.getTablecolumn()).collect(Collectors.toList());
				boolean isUnique = dataSourceService.checkRowDataUnique(keyTable, setDataColumn, fieldList);
				if(!isUnique)
				{
					ColumnData cd = new ColumnData();
					cd.setColumnName("错误信息");
					cd.setColumnValue("本行数据与当前表已有数据重复。");
					Integer index = setDataColumn.size();
					cd.setColumnIndex(index);
					
					setDataColumn.add(cd);
					RowData errRD = new RowData();
					errRD.setRowData(setDataColumn);
					
					errorRowData.add(rd);
					continue;
				}
				
				finalRowData.add(rd);
			}
			
			if(finalRowData.size() > 0)
			{
				Map<String,String> mapExtendField = new HashMap<String,String>();
				mapExtendField.put("GID", "sys_guid()");
				
				dataSourceService.addImportData(keyTable, finalRowData, mapExtendField);
			}
			
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			e.printStackTrace(); 	
		}
		
		return errorRowData;
	}
	
	public  void downloadTemplate(String templateCode, HttpServletResponse response) 
	{
		OutputStream out = null;
		try
		{			
			String fileName = "";
			List<String> lstTitle = new LinkedList<String>();
			List<String> lstDataSourceType = Arrays.asList("BusinessCode", "Constant", "CurrentDate", "CurrentUser");
			ExcelTemplate excelTemplate = templateService.findExcelTemplateByCode(templateCode);
			String templateName = excelTemplate.getName();
			fileName = templateName + ".xlsx";
			List<ExcelTemplateItem> lstExcelTemplateItem = excelTemplate.getExcelTemplateItemList();		
			for(ExcelTemplateItem item : lstExcelTemplateItem)
			{
				String dataSourceType = item.getDatasource();
				if(!lstDataSourceType.contains(dataSourceType))
				{
					String columnName = item.getExcelcolumn();
					lstTitle.add(columnName);
				}
			}			
			
			Workbook wb = excelService.createWorkbook("", lstTitle);	
			
			response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setCharacterEncoding("UTF-8");
			
		    out = response.getOutputStream();
			wb.write(out);			
			
			out.flush();
			out.close();
		}
		catch(IOException e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			e.printStackTrace(); 			
		}
	}
	
	/// <summary>
    /// 根据文件路径下载错误日志文件
    /// </summary>
    /// <param name="filePath">文件路径</param>
    /// <param name="isDelete">是否删除</param>
    /// <param name="response">响应</param>
    /// <returns></returns>
	public  void downloadFile(String filePath, boolean isDelete, HttpServletResponse response)
	{
		try
		{
			File file = new File(filePath);
			 
			String fileName = file.getName();			
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
			 
			// 以流的形式下载文件。
			InputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
		
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream output = response.getOutputStream();
			response.setContentType("application/octet-stream");
			output.write(buffer);
			output.flush();
			output.close();
			
			if(isDelete)
			{
				file.delete();
			}
		}
		catch(IOException e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			e.printStackTrace(); 			
		}

	}
	
	/// <summary>
    /// 根据数据源类型获取列显示值对应的目标值
    /// </summary>
    /// <param name="value"></param>
    /// <param name="associatedTable"></param>
    /// <param name="associatedFieldID"></param>
    /// <returns></returns>
	private String getValueByDataSourceType(String cellValue, String dataSourceType, String dataSourceCode)
	{
		String value = "";
		switch (dataSourceType)
        {
            case "BusinessCode":  
                value = "";                          
                break;
            case "Dictionary":          	
            	value = getDictionaryID(cellValue, dataSourceCode);
                break;
            case "Enum":
            	value = excelService.getEnumValue(cellValue, dataSourceCode, "value");
                break;
            case "Constant":
            	value = dataSourceCode;	                          
                break;
            case "CurrentDate":
            	value = "sysdate";	                           
                break;
            case "CurrentUser":                        
            	value = webContext.getCurrentUser().getId();                                         
                break;
            default:
            	value = cellValue;
                break;
        }
		
		return value;
	}
	
	/// <summary>
    /// 根据数据源类型获取列目标值对应的显示值
    /// </summary>
    /// <param name="value">表存储值</param>
    /// <param name="dataSourceType"></param>
    /// <param name="dataSourceCode"></param>
    /// <returns></returns>
	private String getNameByDataSourceType(String value, String dataSourceType, String dataSourceCode)
	{
		String name = "";
		String strSQL = "";
		Map<String,String> mapParameters = new HashMap<String,String>();
		switch (dataSourceType)
        {            
            case "Dictionary":       
            	name = getDictionaryName(value);
                break;
            case "Enum":
            	name = excelService.getEnumValue(value, dataSourceCode, "name");
                break;            
            default:
            	name = value;
                break;
        }
		
		return name;
	}
	
	/// <summary>
    /// 根据字典名称获取其对应的ID值
    /// </summary>
    /// <param name="value">字段显示值</param>
    /// <param name="dataSourceType"></param>
    /// <returns></returns>
	private String getDictionaryID(String value, String dataSourceCode)
	{
		try
		{
			List<String> queryFieldList = new LinkedList<String>();
	    	queryFieldList.add("ID");
	    	
	    	Set<Condition> setCondition_sub = new HashSet<Condition>();
	    	Condition subCon = new Condition("code", OptionType.equals, dataSourceCode, DataType.string);
	    	setCondition_sub.add(subCon);
	    	
	    	DynamicSQL subSQL = new DynamicSQL();
	    	subSQL.setType(SQLType.Retrieve);
	    	subSQL.setKeyTable("c_datadictionary");
	    	subSQL.setQueryFieldList(queryFieldList);
	    	subSQL.setSetCondition(setCondition_sub);
	    	
	    	String strSubSQL = dataSourceService.getSQL(subSQL);
	    	
	    	Set<Condition> setCondition = new HashSet<Condition>();
	    	Condition subCon1 = new Condition("pid", OptionType.in, strSubSQL, DataType.sql);
	    	setCondition.add(subCon1);
	    	Condition subCon2 = new Condition("name", OptionType.equals, value, DataType.string);
	    	setCondition.add(subCon2);
	    	
	    	DynamicSQL mainSQL = new DynamicSQL();	
	    	mainSQL.setType(SQLType.Retrieve);
	    	mainSQL.setKeyTable("c_datadictionary");
	    	mainSQL.setQueryFieldList(queryFieldList);
	    	mainSQL.setSetCondition(setCondition);
	    	
	    	String strSQL = dataSourceService.getSQL(mainSQL);
	    	value = dataSourceService.getValueBySQL(strSQL, null);
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return value;
	}
	
	/// <summary>
    /// 根据字典ID获取其对应的显示值
    /// </summary>
    /// <param name="id">ID值</param>
    /// <returns></returns>
	private String getDictionaryName(String id)
	{
		String name = id;
		try
		{
			List<String> queryFieldList = new LinkedList<String>();
	    	queryFieldList.add("NAME");
	    		    		    	
	    	Set<Condition> setCondition = new HashSet<Condition>();	    	
	    	Condition subCon = new Condition("ID", OptionType.equals, id, DataType.string);
	    	setCondition.add(subCon);
	    	
	    	DynamicSQL mainSQL = new DynamicSQL();	
	    	mainSQL.setType(SQLType.Retrieve);
	    	mainSQL.setKeyTable("c_datadictionary");
	    	mainSQL.setQueryFieldList(queryFieldList);
	    	mainSQL.setSetCondition(setCondition);
	    	
	    	String strSQL = dataSourceService.getSQL(mainSQL);
	    	name = dataSourceService.getValueBySQL(strSQL, null);
		}
		catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return name;
	}
	
	/// <summary>
    /// 根据字段关联信息获取列显示值对应的关联ID
    /// </summary>
    /// <param name="value"></param>
    /// <param name="associatedTable"></param>
    /// <param name="associatedFieldID"></param>
    /// <returns></returns>
	private String getAssociatedValue(String value, String associatedTable, String associatedFieldID)
    {
		try
		{
	        String[] arrTable_FieldDisplayName = associatedTable.split("\\.");
	        String table = arrTable_FieldDisplayName[0];
	        String displayName = arrTable_FieldDisplayName[1];
	
	        List<String> queryFieldList = new LinkedList<String>();
	    	queryFieldList.add(associatedFieldID);
	    		    		    	
	    	Set<Condition> setCondition = new HashSet<Condition>();	    	
	    	Condition subCon = new Condition(displayName, OptionType.equals, value, DataType.string);
	    	setCondition.add(subCon);
	    	
	    	DynamicSQL mainSQL = new DynamicSQL();	
	    	mainSQL.setType(SQLType.Retrieve);
	    	mainSQL.setKeyTable(table);
	    	mainSQL.setQueryFieldList(queryFieldList);
	    	mainSQL.setSetCondition(setCondition);
	    	
	    	String strSQL = dataSourceService.getSQL(mainSQL);
	        String result = dataSourceService.getValueBySQL(strSQL, null);
	        
	        return result;
		}
        catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
    }
	
	/// <summary>
    /// 根据字段关联信息获取列ID值对应的关联显示值
    /// </summary>
    /// <param name="value"></param>
    /// <param name="associatedTable"></param>
    /// <param name="associatedFieldID"></param>
    /// <returns></returns>
	private String getAssociatedName(String value, String associatedTable, String associatedFieldID)
    {
		try
		{
	        String[] arrTable_FieldDisplayName = associatedTable.split("\\.");
	        String table = arrTable_FieldDisplayName[0];
	        String displayName = arrTable_FieldDisplayName[1];
	
	        List<String> queryFieldList = new LinkedList<String>();
	    	queryFieldList.add(displayName);
	    		    		    	
	    	Set<Condition> setCondition = new HashSet<Condition>();	    	
	    	Condition subCon = new Condition(associatedFieldID, OptionType.equals, value, DataType.string);
	    	setCondition.add(subCon);
	    	
	    	DynamicSQL mainSQL = new DynamicSQL();	
	    	mainSQL.setType(SQLType.Retrieve);
	    	mainSQL.setKeyTable(table);
	    	mainSQL.setQueryFieldList(queryFieldList);
	    	mainSQL.setSetCondition(setCondition);
	    	
	    	String strSQL = dataSourceService.getSQL(mainSQL);
	        String result = dataSourceService.getValueBySQL(strSQL, null);
	        
	        return result;
		}
        catch(Exception e)
		{
			logger.error("执行异常"+e.getMessage(),e);
		}
		
		return null;
    }
	
	/// <summary>
    /// 根据字段关联信息获取列ID值对应的关联显示值
    /// </summary>
    /// <param name="templateCode"></param>
    /// <param name="condition"></param>
    /// <param name="exportType"></param>
	/// <param name="response">响应</param>
    /// <returns></returns>
	public void exportData(String templateCode, String condition, String exportType, HttpServletResponse response)
	{
		switch(exportType)
		{
			case "excel":
				exportExcel(templateCode,condition,response);
				break;
			default:
				exportExcel(templateCode,condition,response);
				break;
		}
	}
	
	public void exportExcel(String templateCode, String condition, HttpServletResponse response)
	{
		OutputStream out = null;
		try
		{			
			ExcelTemplate excelTemplate = templateService.findExcelTemplateByCode(templateCode);			
			String templateName = excelTemplate.getName();
			String keyTable = excelTemplate.getKeytable();
			List<ExcelTemplateItem> lstExcelTemplateItem = excelTemplate.getExcelTemplateItemList();
			
			//获取导出数据
			List<String> lstQueryField = lstExcelTemplateItem.stream().map(o->o.getTablecolumn()).collect(Collectors.toList());				
			List<RowData> lstRowData = dataSourceService.getExportData(keyTable,lstQueryField, condition, "");
			
			//写入excel
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
		            	String value = "";
		            	ExcelTemplateItem item = itemOptional.get();
		            	String dataSourceType = item.getDatasource();
		            	String dataSourceCode = item.getDatasourcecode();
		            	String associatedTable = item.getAssociatetable();
		            	String associatedFieldID = item.getAssociatefieldid();
		            	String columnValue = cd.getColumnValue();
		            	if(StringUtils.isNotBlank(dataSourceType) && StringUtils.isNotBlank(dataSourceCode)) 
		            	{
		            		value = getNameByDataSourceType(columnValue,dataSourceType,dataSourceCode);
		            	}
		            	else if(StringUtils.isNotBlank(associatedTable) && StringUtils.isNotBlank(associatedFieldID))
		            	{
		            		value = getAssociatedName(columnValue, associatedTable, associatedFieldID);
		            	}
		            	else
		            	{
		            		value = columnValue;
		            	}
		            	
		            	cd.setColumnValue(value);
		            	String columnName = item.getExcelcolumn();
		            	cd.setColumnName(columnName);
		            }
		        }
			}
			
			Workbook workbook = excelService.createWorkbook(templateName, null);
			Sheet sheet = excelService.getWorkSheet(workbook, 0);
			sheet = excelService.writeHeadertoExcel(sheet, lstRowData.get(0));
			excelService.writeDatatoExcel(sheet, lstRowData, 1);
					
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	        String fileName = templateName + "_" + sdf.format(new Date()) + ".xlsx";        					
			
	        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setCharacterEncoding("UTF-8");
			
		    out = response.getOutputStream();
			workbook.write(out);			
			
			out.flush();
			out.close();
		}
		catch(IOException e)
		{
			logger.error("执行异常"+e.getMessage(),e);
			e.printStackTrace(); 			
		}
	
	}
	
}
