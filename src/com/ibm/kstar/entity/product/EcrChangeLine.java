package com.ibm.kstar.entity.product;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年08月16日 14:32
 * @LastModifier 黄奇
 */
@Entity
@Table(name = "CRM_T_PRODUCT_ECR_CHANGE_LINE", schema = "KCRM", catalog = "")
public class EcrChangeLine extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "C_ID", nullable = false, length = 32)
    private String id;

    @Basic
    @Column(name = "C_CHANGE_ID", nullable = false, length = 32)
    private String changeId;

    @Basic
    @Column(name = "C_PRODUCT_ID", nullable = false, length = 32)
    private String productId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void fillInit(UserObject userObject) {
        if (this.createdAt == null) {
            this.setCreatedAt(new Date());
        }
        if (this.createdById == null) {
            this.setCreatedById(userObject.getEmployee().getId());
        }
        if (this.getCreatedOrgId() == null) {
            this.setCreatedOrgId(userObject.getOrg().getId());
        }
        if (this.getCreatedPosId() == null) {
            this.setCreatedPosId(userObject.getPosition().getId());
        }
        this.setUpdatedById(userObject.getEmployee().getId());
        this.setUpdatedAt(new Date());
    }
}
