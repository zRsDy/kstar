package com.ibm.kstar.api.system.lov.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xsnake.web.utils.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.cache.CacheData;

@Entity
@Table(name = "SYS_T_LOV_MEMBER")
public class LovMember implements Serializable{

	private static final long serialVersionUID = 1L;

	public LovMember(){}
	
	public LovMember(String id,String name,String code,String parentId){
		this.id = id;
		this.name = name;
		this.code = code;
		this.parentId = parentId;
	}
	
	public LovMember(String id,String name,String code,String parentId,String memo){
		this.id = id;
		this.name = name;
		this.code = code;
		this.parentId = parentId;
		this.memo = memo;
	}

	@Id
	@GeneratedValue(generator = "sys_t_lov_member_id_generator")
	@GenericGenerator(name="sys_t_lov_member_id_generator", strategy="uuid")
	@Column(name="ROW_ID")
	private String id;
	
	@Column(name="LOV_CODE")
	private String code;
	
	@Column(name="LOV_NAME")
	private String name;
	
	@Column(name="MEMO")
	private String memo;
	
	@Column(name="PARENT_ID")
	private String parentId;
	
	@Column(name="GROUP_ID")
	private String groupId;
	
	@Column(name="GROUP_CODE")
	private String groupCode;
	
	@Column(name="LOV_PATH")
	private String path;
	
	@Column(name="LEAF_FLAG")
	private String leafFlag;
	
	@Column(name="LOV_LEVEL")
	private Integer level;
	
	@Column(name="OPT_TXT1")
	private String optTxt1;
	
	@Column(name="OPT_TXT2")
	private String optTxt2;
	
	@Column(name="OPT_TXT3")
	private String optTxt3;
	
	@Column(name="OPT_TXT4")
	private String optTxt4;
	
	@Column(name="OPT_TXT5")
	private String optTxt5;
	
	@Column(name="OPT_TXT6")
	private String optTxt6;
	
	@Column(name="OPT_TXT7")
	private String optTxt7;
	
	@Column(name="OPT_TXT8")
	private String optTxt8;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="CREATION_BY")
	private String creationBy;
	
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name="DELETE_FLAG")
	private String deleteFlag = "N";
	
	@Column(name="NAME_PATH")
	private String namePath;
	
	@Column(name="CODE_PATH")
	private String codePath;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="NO")
	private Integer no;
	
	@Transient
	private String upBaoBei;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/*
	public String getName() {
		return name;
	}
	*/
	public String getOldName(){
		return this.name;
	}
	public String getName() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale==null)
			return this.name;
		if("en".equals(locale.getLanguage())){
			if(StringUtils.isNotEmpty(this.getOptTxt7()))
				return this.getOptTxt7();
		}else if("tw".equals(locale.getLanguage())){
			if(StringUtils.isNotEmpty(this.getOptTxt8()))
				return this.getOptTxt8();
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(String leafFlag) {
		this.leafFlag = leafFlag;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationBy() {
		return creationBy;
	}

	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getOptTxt1() {
		return optTxt1;
	}

	public void setOptTxt1(String optTxt1) {
		this.optTxt1 = optTxt1;
	}

	public String getOptTxt1Name() {
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember("APPLICATION", optTxt1);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getName();
	}

	public String getOptTxt2() {
		return optTxt2;
	}

	public void setOptTxt2(String optTxt2) {
		this.optTxt2 = optTxt2;
	}

	public String getOptTxt3() {
		return optTxt3;
	}

	public void setOptTxt3(String optTxt3) {
		this.optTxt3 = optTxt3;
	}

	public String getOptTxt4() {
		return optTxt4;
	}

	public void setOptTxt4(String optTxt4) {
		this.optTxt4 = optTxt4;
	}

	public String getOptTxt5() {
		return optTxt5;
	}

	public void setOptTxt5(String optTxt5) {
		this.optTxt5 = optTxt5;
	}
	
	public String getOptTxt6() {
		return optTxt6;
	}

	public void setOptTxt6(String optTxt6) {
		this.optTxt6 = optTxt6;
	}

	public String getOptTxt7() {
		return optTxt7;
	}

	public void setOptTxt7(String optTxt7) {
		this.optTxt7 = optTxt7;
	}

	public String getOptTxt8() {
		return optTxt8;
	}

	public void setOptTxt8(String optTxt8) {
		this.optTxt8 = optTxt8;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getNamePath() {
		return namePath;
	}

	public void setNamePath(String namePath) {
		this.namePath = namePath;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

    public String getUpBaoBei() {
		return upBaoBei;
	}

	public void setUpBaoBei(String upBaoBei) {
		this.upBaoBei = upBaoBei;
	}

	@Override
    public boolean equals(Object obj) {
    	if(obj==null){
    		return false;
    	}
        if(this==obj){
            return true;
        }else if(obj instanceof LovMember)
        {
        	LovMember o=(LovMember)obj;
            if("Y".equalsIgnoreCase(o.getLeafFlag()) && this.getOptTxt5() != null && o.getOptTxt5()!= null
            		 && this.getOptTxt5().equals(o.getOptTxt5()) && "Y".equalsIgnoreCase(this.getLeafFlag()) ){
                return true;
            }
            else return false;
        }
        else return false;
    }
    
    
    public String json(){
    	return JSON.toJSONString(this);
    }
	
	
}
