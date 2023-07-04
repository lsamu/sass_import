package com.rainfe.tdm.fasttdm.config.bean;

import com.rainfe.mapletr.view.component.config.DMConditional;
import com.rainfe.mapletr.view.component.config.MySQLConditional;
import com.rainfe.mapletr.view.component.config.OracleConditional;
import com.rainfe.mapletr.view.component.config.UXConditional;

import com.rainfe.tdm.fasttdm.service.datasource.dm.DMImportExportDataSourceServiceImpl;
import com.rainfe.tdm.fasttdm.service.datasource.mysql.MySQLImportExportDataSourceServiceImpl;
import com.rainfe.tdm.fasttdm.service.datasource.oracle.OracleImportExportDataSourceServiceImpl;
import com.rainfe.tdm.fasttdm.service.datasource.ux.UXImportExportDataSourceServiceImpl;
import com.rainfe.tdm.fasttdm.service.importexportextends.ImportExportDataSourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/**
 * 导入导出配置
 * 
 * @author Zhao xiaona
 * @version 1.0.0.0
 */
@Configuration
public class ImportExportBeanConfig {

	// 达梦
    @Bean()
    @Conditional(DMConditional.class)
    public ImportExportDataSourceService dMImportExportDataSourceServiceImpl(){
       return new DMImportExportDataSourceServiceImpl();
    }
    
    // MySQL
    @Bean()
    @Conditional(MySQLConditional.class)
    public ImportExportDataSourceService mySQLImportExportDataSourceImple(){
       return new MySQLImportExportDataSourceServiceImpl();
    }
    
    // 优炫 同达梦
    @Bean()
    @Conditional(UXConditional.class)
    public ImportExportDataSourceService uxImportExportDataSourceImple(){
       return new UXImportExportDataSourceServiceImpl();
    }
    
    // Oracle 同达梦
    @Bean()
    @Conditional(OracleConditional.class)
    public ImportExportDataSourceService oracleImportExportDataSourceImple(){
       return new OracleImportExportDataSourceServiceImpl();
    }
}
