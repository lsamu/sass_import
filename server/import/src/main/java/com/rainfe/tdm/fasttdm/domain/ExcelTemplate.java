package com.rainfe.tdm.fasttdm.domain;


import com.rainfe.mapletr.db.entity.AbstractEntity;

import javax.persistence.*;
import java.util.*;

/**
 * ExcelTemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="SYSCONFIG_EXCELTEMPLATE")
public class ExcelTemplate extends AbstractEntity<String> implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4141643731747396083L;
	// Fields    

     private String id;
     private String code;
     private String name;
     private String keytable;
     private String remark;
     @OrderBy("ordernumber ASC")
     private Set<ExcelTemplateItem> excelTemplateItems = new HashSet<ExcelTemplateItem>(0);

     private List<ExcelTemplateItem> excelTemplateItemList;

    // Constructors
	/** default constructor */
    public ExcelTemplate() {
    }

    
    /** full constructor */
    public ExcelTemplate(String code, String name, String keytable, String remark, Set<ExcelTemplateItem> excelTemplateItems) {
        this.code = code.trim();
        this.name = name.trim();
        this.keytable = keytable.trim();
        this.remark = remark.trim();
        this.excelTemplateItems = excelTemplateItems;
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
    
    @Column(name="CODE", length=100)

    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code.trim();
    }
    
    @Column(name="NAME", length=100)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name.trim();
    }
    
    @Column(name="KEYTABLE", length=100)

    public String getKeytable() {
        return this.keytable;
    }
    
    public void setKeytable(String keytable) {
        this.keytable = keytable.trim();
    }

    @Column(name="REMARK", length=500)
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark.trim();
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="excelTemplate")
    @OrderBy(value="ordernumber asc")
    public Set<ExcelTemplateItem> getExcelTemplateItems() {
        return this.excelTemplateItems;
    }
    
    public void setExcelTemplateItems(Set<ExcelTemplateItem> excelTemplateItems) {
        this.excelTemplateItems = excelTemplateItems;
    }
    
    @Transient
    public List<ExcelTemplateItem> getExcelTemplateItemList() {
    	excelTemplateItemList = new ArrayList<ExcelTemplateItem>(excelTemplateItems);
    	
    	Collections.sort(excelTemplateItemList, new Comparator<ExcelTemplateItem>() {

            @Override
            public int compare(ExcelTemplateItem o1, ExcelTemplateItem o2) {
                return o1.getOrdernumber() < o2.getOrdernumber() ? -1 : 1;
            }
            
        });
    	
		return excelTemplateItemList;
	}


	public void setExcelTemplateItemList(List<ExcelTemplateItem> excelTemplateItemList) {
		this.excelTemplateItemList = excelTemplateItemList;
	}

   
}
