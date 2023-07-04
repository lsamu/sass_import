package com.v5.importexport.controller;

import com.alibaba.fastjson.JSON;

import com.v5.importexport.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value="/importexport")
public class ImportExportController {
	
	@Autowired
	private ExcelUtil excelUtil;
	
	@RequestMapping(value = "/importData/{code}")
    public void importData(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file, @PathVariable String code)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	InputStream stream = file.getInputStream();        	
        	String result = excelUtil.ImportExcel(stream, code, request);
            commonVO.setStatus(0);
    		commonVO.setMsg("");
    		commonVO.setData(result);
    		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (IOException e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("数据导入发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/downloadTemplate/{code}")
    public void downloadTemplate(HttpServletResponse response,  @PathVariable String code)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	excelUtil.downloadTemplate(code, response);
            commonVO.setStatus(0);
    		commonVO.setMsg("");
    		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("下载模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	String filepath = request.getParameter("path");
        	String isdelete = request.getParameter("isdelete");
        	excelUtil.downloadFile(filepath, Boolean.parseBoolean(isdelete), response);
            commonVO.setStatus(0);
    		commonVO.setMsg("");
    		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("下载模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/exportData")
    public void exportData(HttpServletRequest request, HttpServletResponse response)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	String templateCode = request.getParameter("templateCode");
        	String condition = request.getParameter("condition");
        	String exportType = request.getParameter("exportType");
        	excelUtil.exportData(templateCode, condition, exportType, response);
            commonVO.setStatus(0);
    		commonVO.setMsg("");
    		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("导出数据发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
}
