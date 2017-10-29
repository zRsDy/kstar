package com.ibm.kstar.entity.product;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年10月09日 09:10
 * @LastModifier 黄奇
 */
@Entity
@Table(name = "CRM_INT_ECR_REQM_PRODUCT")
public class IEcrReqmProductEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "C_ID", nullable = false, length = 32)
    private String id;

    @Basic
    @Column(name = "C_ecr_reqm_id", nullable = false, length = 32)
    private String changeId;

    @Basic
    @Column(name = "C_MATER_CODE", nullable = false, length = 50)
    private String materCode;

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

    public String getMaterCode() {
        return materCode;
    }

    public void setMaterCode(String materCode) {
        this.materCode = materCode;
    }
}
