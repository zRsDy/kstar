package com.ibm.kstar.entity.product;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * CatelogMatchBean.java
 * 销售目录映射实体类
 *
 * @author Neil Wan
 * 2017年2月23日 下午15:24:39
 */

@Entity
@Table(name = "CRM_T_PRODUCT_CATALOG_MATCH")
public class CatelogMatchBean extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "kstarcatelog_match_id_generator")
    @GenericGenerator(name = "kstarcatelog_match_id_generator", strategy = "uuid")
    @Column(name = "C_PID")
    private String id;

    //目录匹配类型
    @Column(name = "C_MATCH_TYPE")
    private String matchType;

    @Column(name = "C_CRM_CATEGORY")
    private String crmCategory;

    //部门
    @Column(name = "C_DEPARTMENT")
    private String department;

    // 目录ID
    @Column(name = "C_DIRECT_ID")
    private String directID;

    // 目录名称
    @Column(name = "C_DIRECT_NAME")
    private String directName;

    // 产品分类
    @Column(name = "C_PRO_CATEGORY")
    private String cproCategory;

    // 产品型号
    @Column(name = "C_PRO_MODEL")
    private String proModel;

    // 有效
    @Column(name = "C_EFFECTIVE")
    private String effective;

    // 有效期开始时间
    @Column(name = "DT_START_TIME")
    private Date startTime;

    // 有效期结束时间
    @Column(name = "DT_END_TIME")
    private Date endTime;

    // 说明
    @Column(name = "C_COMMENT")
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCproCategory() {
        return cproCategory;
    }

    public void setCproCategory(String cproCategory) {
        this.cproCategory = cproCategory;
    }

    public String getProModel() {
        return proModel;
    }

    public void setProModel(String proModel) {
        this.proModel = proModel;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDirectID() {
        return directID;
    }

    public void setDirectID(String directID) {
        this.directID = directID;
    }

    public String getDirectName() {
        return directName;
    }

    public void setDirectName(String directName) {
        this.directName = directName;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getCrmCategory() {
        return crmCategory;
    }

    public void setCrmCategory(String crmCategory) {
        this.crmCategory = crmCategory;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Transient
    private String crmCategoryLabel;
    @Transient
    private String matchTypeLabel;

    @Transient
    private String departmentName;

    public String getDepartmentName() {
        if (StringUtils.isNotEmpty(department) && departmentName == null) {
            LovMember lov = (LovMember) CacheData.getInstance().get(department);
            if (lov != null) {
                departmentName = lov.getName();
            }
        }
        return departmentName;
    }

    public String getCrmCategoryLabel() {
        return crmCategoryLabel;
    }

    public String getMatchTypeLabel() {
        return matchTypeLabel;
    }

    public void setCrmCategoryLabel(String crmCategoryLabel) {
        this.crmCategoryLabel = crmCategoryLabel;
    }

    public void setMatchTypeLabel(String matchTypeLabel) {
        this.matchTypeLabel = matchTypeLabel;
    }
}
