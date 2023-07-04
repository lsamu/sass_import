package com.rainfe.tdm.fasttdm.controller;

import com.alibaba.fastjson.JSON;
import com.rainfe.mapletr.common.api.BaseController;
import com.rainfe.mapletr.common.api.FromClientViewModel;
import com.rainfe.mapletr.common.api.ToClientViewModel;
import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;
import com.rainfe.tdm.fasttdm.service.ImportExportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value="/impexptemplate")
public class ImportExportTemplateController extends BaseController {

	@Autowired
	private ImportExportTemplateService impExpTemplateService;
	
	@RequestMapping(value = "/add")
    public void addImpTemplate(HttpServletResponse response, @RequestBody ExcelTemplate excelTemplate)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	//1.校验excelTemplate是都已经存在
        	String code = excelTemplate.getCode();
        	boolean result = impExpTemplateService.isExistExcelTemplate(code);
        	if(result)
        	{
        		commonVO.setStatus(-1);
        		commonVO.setMsg("当前模板编码已存在，不能执行新增操作！");
        	}
        	else
        	{      		
        		result = impExpTemplateService.insertExcelTemplate(excelTemplate);
        		
        		commonVO.setStatus(0);
        		commonVO.setMsg("");
        		commonVO.setData(String.valueOf(result));
        	}
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("新增导入模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/edit")
    public void editImpTemplate(HttpServletResponse response, @RequestBody ExcelTemplate excelTemplate)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
            boolean result = impExpTemplateService.deleteExcelTemplateById(excelTemplate.getId());
            if (result) {
                result = impExpTemplateService.insertExcelTemplate(excelTemplate);
            }

            commonVO.setStatus(0);
            commonVO.setMsg("");
            commonVO.setData(String.valueOf(result));
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("编辑导入模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/remove/{Id}")
    public void deleteImpTemplate(HttpServletResponse response, @PathVariable String id)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	boolean result = impExpTemplateService.deleteExcelTemplateById(id);
        	
        	commonVO.setStatus(0);
    		commonVO.setMsg("");
    		commonVO.setData(String.valueOf(result));
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("删除导入模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/find/{id}")
    public void findImpTemplate(HttpServletResponse response, @PathVariable String id)
	{
		ToClientViewModel<ExcelTemplate> commonVO = new ToClientViewModel<ExcelTemplate>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	ExcelTemplate excelTemplate = impExpTemplateService.findExcelTemplateById(id);
        	
        	commonVO.setStatus(0);
    		commonVO.setMsg("");
    		commonVO.setData(excelTemplate);
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("获取导入模板信息发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/findByCode/{code}")
    public void findImpTemplateByCode(HttpServletResponse response, @PathVariable String code)
	{
		ToClientViewModel<ExcelTemplate> commonVO = new ToClientViewModel<ExcelTemplate>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	ExcelTemplate excelTemplate = impExpTemplateService.findExcelTemplateByCode(code);
        	
        	commonVO.setStatus(0);
    		commonVO.setMsg("");
    		commonVO.setData(excelTemplate);
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("获取模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
	
	@RequestMapping(value = "/del")
    public void deleteImpTemplateByIds(HttpServletResponse response, @ModelAttribute FromClientViewModel fromModel)
	{
		ToClientViewModel<String> commonVO = new ToClientViewModel<String>();
		commonVO.setStatus(-1);
		commonVO.setMsg("");
        
        try 
        {
        	if (null != fromModel.getData() && fromModel.getData().length() > 0) 
        	{
    			String[] Ids = fromModel.getData().split(",");
	        	boolean result = impExpTemplateService.deleteExcelTemplateByIds(Ids);
	        	
	        	commonVO.setStatus(0);
	    		commonVO.setMsg("");
	    		commonVO.setData(String.valueOf(result));
        	}
        		
    		this.printString(response, JSON.toJSONString(commonVO));
        } 
        catch (Exception e) 
        {
        	commonVO.setStatus(-1);
    		commonVO.setMsg("批量删除导入模板发生异常！");
    		this.printString(response, JSON.toJSONString(commonVO));
        }
        
    }
}
