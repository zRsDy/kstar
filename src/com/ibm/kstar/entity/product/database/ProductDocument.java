package com.ibm.kstar.entity.product.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.xsnake.web.utils.BeanUtils;

@Entity
@Table(name = "CRM_T_PRODUCT_DOCUMENT")
public class ProductDocument implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public ProductDocument(){
		
	}
	
	public ProductDocument(ProductDocument pd ,String type){
		BeanUtils.copyProperties(pd, this);
		this.type = type;
	}
	
	@Id
	@GeneratedValue(generator = "crm_t_product_document_id_generator")
	@GenericGenerator(name = "crm_t_product_document_id_generator", strategy = "uuid")
	@Column(name = "ROW_ID")
	private String id;
	
	@Column(name = "CATALOG_ID")
	private String catalogId;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "PATH")
	private String path;
	
	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "FILE_SIZE")
	private long size;

	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	@Column(name = "CREATE_BY")
	private String createBy;
	
	@Column(name = "LAST_UPDATE_BY")
	private String lastUpdateBy;
	
	@Transient
	private String type;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
