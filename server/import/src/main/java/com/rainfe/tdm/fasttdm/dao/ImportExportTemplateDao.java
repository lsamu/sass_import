package com.rainfe.tdm.fasttdm.dao;


import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;

public interface ImportExportTemplateDao {
	/**
	 * 校验是否存在相同编号的Excel模板
	 * 
	 * @param code
	 *            Excel模板编号
	 *            
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean isExistExcelTemplate(String code);
	
	/**
	 * 新建Excel模板
	 * 
	 * @param excelTemplate
	 *            Excel模板主表数据
	 *            
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean insertExcelTemplate(ExcelTemplate excelTemplate);
	
	/**
	 * 编辑Excel模板
	 * 
	 * @param excelTemplate
	 *            Excel模板主表数据
	 *            
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean editExcelTemplate(ExcelTemplate excelTemplate);
	
	/**
	 * 根据Excel模板主键ID删除Excel模板
	 * 
	 * @param id
	 *            Excel模板主表ID          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean deleteExcelTemplateById(String id);
	
	/**
	 * 根据Excel模板主键ID查找Excel模板
	 * 
	 * @param id
	 *            Excel模板主表ID          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	ExcelTemplate findExcelTemplateById(String id);
	
	/**
	 * 根据Excel模板编码查询Excel模板
	 * 
	 * @param code
	 *            Excel模板主表Code         
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	ExcelTemplate findExcelTemplateByCode(String code);
	
	/**
	 * 根据Excel模板主键ID数组删除Excel模板
	 * 
	 * @param ids
	 *            Excel模板主表ID数组          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean deleteExcelTemplateByIds(String[] ids);
	
}
