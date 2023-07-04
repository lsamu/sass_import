package com.rainfe.tdm.fasttdm.dao.impl;

import com.rainfe.mapletr.db.dao.BaseDao;
import com.rainfe.tdm.fasttdm.dao.ImportExportTemplateDao;
import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImportExportTemplateDaoImpl implements ImportExportTemplateDao {
	//日志对象
		private static final Logger logger = Logger.getLogger(ImportExportTemplateDaoImpl.class);
			
		@Autowired
		private BaseDao<ExcelTemplate> baseDao;
		
		@Override
		public boolean isExistExcelTemplate(String code)
		{
			try
			{
				ExcelTemplate template = baseDao.get("code", code, ExcelTemplate.class);
				return template == null ? false : true;
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
				baseDao.save(excelTemplate);			
				return true;
			}
			catch(RuntimeException e)
			{
				logger.error("日志添加失败:",e);
				return false;
			}
		}
		
		@Override
		public boolean editExcelTemplate(ExcelTemplate ExcelTemplate) 
		{
			try
			{
				if(null==ExcelTemplate){
					return false;
				}else{
					baseDao.update(ExcelTemplate);
					return true;
				}		
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
				baseDao.deleteById(id, ExcelTemplate.class);
				return true;
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
			ExcelTemplate ExcelTemplate = null;
			try
			{
				ExcelTemplate = baseDao.get(id, ExcelTemplate.class);
				return ExcelTemplate;
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
			ExcelTemplate ExcelTemplate = null;
			try
			{
				ExcelTemplate = baseDao.get("code", code, ExcelTemplate.class);
				return ExcelTemplate;
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
				List<ExcelTemplate> templatelist = baseDao.listByCriterion(ExcelTemplate.class, Restrictions.in("id", ids));
				baseDao.batchDelete(templatelist);
				
				return true;
			}
			catch(RuntimeException e)
			{
				logger.error("日志添加失败:",e);
				return false;
			}
		}
}
