package com.v5.importexport.domain;


import javax.persistence.*;


/**
 * ImportTemplateItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="SYSCONFIG_EXCELTEMPLATE_F")
public class ExcelTemplateItem extends AbstractEntity<String> implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	 private static final long serialVersionUID = -8284381737605250361L;
	
	 private String id;
	 private ExcelTemplate excelTemplate;	 
	 private String excelcolumn;
	 private String tablecolumn;
	 private Integer isunique;
	 private String associatetable;
	 private String associatefieldid;
	 private String datasource;
	 private String datasourcecode;
	 private Integer ordernumber;
	 
	 private int excelcolumnindex;


    // Constructors

    

	/** default constructor */
    public ExcelTemplateItem() {
    }

    
    /** full constructor */
    public ExcelTemplateItem(ExcelTemplate excelTemplate, String excelcolumn, String tablecolumn, Integer isunique, String associatetable, String associatefieldid, String datasource, String datasourcecode, Integer ordernumber) {
        this.excelTemplate = excelTemplate;
        this.excelcolumn = excelcolumn.trim();
        this.tablecolumn = tablecolumn.trim();
        this.associatetable = associatetable.trim();
        this.associatefieldid = associatefieldid.trim();
        this.datasource = datasource.trim();
        this.datasourcecode = datasourcecode.trim();
        this.ordernumber = ordernumber;
        this.isunique = isunique;
    }

   
    // Property accessors
    @Id 
    //@GeneratedValue    
    @Column(name="ID", unique=true, nullable=false, length=50)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PID")
    public ExcelTemplate getExcelTemplate() {
        return this.excelTemplate;
    }
    
    public void setExcelTemplate(ExcelTemplate excelTemplate) {
        this.excelTemplate = excelTemplate;
    }
    
    @Column(name="EXCELCOLUMN", length=100)

    public String getExcelcolumn() {
        return this.excelcolumn;
    }
    
    public void setExcelcolumn(String excelcolumn) {
        this.excelcolumn = excelcolumn.trim();
    }
    
    @Column(name="TABLECOLUMN", length=100)

    public String getTablecolumn() {
        return this.tablecolumn;
    }
    
    public void setTablecolumn(String tablecolumn) {
        this.tablecolumn = tablecolumn.trim();
    }
    
    @Transient
    public int getExcelcolumnindex() {
		return excelcolumnindex;
	}

	public void setExcelcolumnindex(int excelcolumnindex) {
		this.excelcolumnindex = excelcolumnindex;
	}
    
    @Column(name="ASSOCIATETABLE", length=200)

    public String getAssociatetable() {
        return this.associatetable;
    }
    
    public void setAssociatetable(String associatetable) {
        this.associatetable = associatetable.trim();
    }
    
    @Column(name="ASSOCIATEFIELDID", length=100)

    public String getAssociatefieldid() {
        return this.associatefieldid;
    }
    
    public void setAssociatefieldid(String associatefieldid) {
        this.associatefieldid = associatefieldid.trim();
    }
    
    @Column(name="DATASOURCE", length=100)

    public String getDatasource() {
        return this.datasource;
    }
    
    public void setDatasource(String datasource) {
        this.datasource = datasource.trim();
    }
    
    @Column(name="DATASOURCECODE", length=100)

    public String getDatasourcecode() {
        return this.datasourcecode;
    }
    
    public void setDatasourcecode(String datasourcecode) {
        this.datasourcecode = datasourcecode.trim();
    }
    
    @Column(name="ORDERNUMBER")

    public Integer getOrdernumber() {
        return this.ordernumber;
    }
    
    public void setOrdernumber(Integer ordernumber) {
        this.ordernumber = ordernumber;
    }

    @Column(name="ISUNIQUE")

    public Integer getIsunique() {
        return this.isunique;
    }

    public void setIsunique(Integer isunique) {
        this.isunique = isunique;
    }


}