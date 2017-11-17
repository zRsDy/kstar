package com.ibm.kstar.api.system.permission.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月08日 18:06
 * @LastModifier 黄奇
 */
@Entity
@Table(name = "CRM_T_verification_Code")
public class VerificationCode {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid")
    @Column(name = "id")
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "is_deleted")
    private boolean deleted;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
