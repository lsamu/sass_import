package com.rainfe.tdm.fasttdm.service;


import com.rainfe.tdm.fasttdm.domain.ExcelTemplate;

public interface ImportExportTemplateService {

	/**
	 * 校验是否存在相同编号的excel模板
	 * 
	 * @param code
	 *            导入模板编号
	 *            
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean isExistExcelTemplate(String code);
	
	/**
	 * 新建excel模板
	 * 
	 * @param excelTemplate
	 *            excel模板主表数据
	 *                  
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean insertExcelTemplate(ExcelTemplate excelTemplate);
	
	/**
	 * 编辑excel模板
	 * 
	 * @param excelTemplate
	 *            excel模板主表数据
	 *                  
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean editExcelTemplate(ExcelTemplate excelTemplate);
	
	/**
	 * 根据excel模板主键ID删除导入模板
	 * 
	 * @param id
	 *            excel模板主表ID          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean deleteExcelTemplateById(String id);
	
	/**
	 * 根据excel模板主键ID查询导入模板
	 * 
	 * @param id
	 *            excel模板主表ID          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	ExcelTemplate findExcelTemplateById(String id);
	
	/**
	 * 根据excel模板编码查询导入模板
	 * 
	 * @param code
	 *         excel模板主表Code         
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	ExcelTemplate findExcelTemplateByCode(String code);
	
	/**
	 * 根据excel模板主键ID数组删除导入模板
	 * 
	 * @param ids
	 *            excel模板主表ID数组          
	 * @return 创建成功则返回 true ，否则返回 false
	 */
	boolean deleteExcelTemplateByIds(String[] ids);
	
}
