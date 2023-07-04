package com.rainfe.tdm.fasttdm.service.impl;


import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;
import com.rainfe.tdm.fasttdm.dao.ImportExportTemplateDao;
import com.rainfe.tdm.fasttdm.service.ImportExportTemplateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportExportTemplateServiceImpl implements ImportExportTemplateService {

	//日志对象
    private static final Logger logger = Logger.getLogger(ImportExportTemplateServiceImpl.class);
    
	@Autowired
	public ImportExportTemplateDao impExpTemplateDao;
	
	@Override
	public boolean isExistExcelTemplate(String code)
	{
		try
		{
			boolean result = impExpTemplateDao.isExistExcelTemplate(code);
			return result;
		}
		catch(RuntimeException e) 
		{
			logger.error("日志添加失败:",e);
			return false;
		}
	}
	
	@Override
	public boolean insertExcelTemplate(ExcelTemplate excelTemplate)
	{
		try
		{
			boolean result = impExpTemplateDao.insertExcelTemplate(excelTemplate);
			return result;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return false;
		}
	}
	
	@Override
	public boolean editExcelTemplate(ExcelTemplate excelTemplate) 
	{
		try
		{
			boolean result = impExpTemplateDao.editExcelTemplate(excelTemplate);
			return result;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return false;
		}
	}
	
	@Override
	public boolean deleteExcelTemplateById(String id) 
	{
		try
		{
			boolean result = impExpTemplateDao.deleteExcelTemplateById(id);
			return result;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return false;
		}
	}
	
	@Override
	public ExcelTemplate findExcelTemplateById(String id) 
	{
		try
		{
			ExcelTemplate importTemplate = impExpTemplateDao.findExcelTemplateById(id);
			return importTemplate;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return null;
		}
	}
	
	@Override
	public ExcelTemplate findExcelTemplateByCode(String code)
	{
		try
		{
			ExcelTemplate importTemplate = impExpTemplateDao.findExcelTemplateByCode(code);
			return importTemplate;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return null;
		}
	}
	
	@Override
	public boolean deleteExcelTemplateByIds(String[] ids)
	{
		try
		{
			boolean result = impExpTemplateDao.deleteExcelTemplateByIds(ids);
			
			return result;
		}
		catch(RuntimeException e)
		{
			logger.error("日志添加失败:",e);
			return false;
		}
	}
	
}
