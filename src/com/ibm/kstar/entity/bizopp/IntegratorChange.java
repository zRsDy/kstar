package com.ibm.kstar.entity.bizopp;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by wangchao on 2017/3/22.
 */
@Entity
@Table(name = "CRM_T_BIZOPP_INTEGRATOR_CHANGE")
public class IntegratorChange implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "row_id")
    @Id
    @GeneratedValue(generator = "crm_t_bizopp_integrator_id_generator")
    @GenericGenerator(name="crm_t_bizopp_integrator_id_generator", strategy="assigned")
    private String id;

    @Column(name = "INTEGRATOR")
    private String integrator;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "BIZ_OPP_ID")
    private String bizOppId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntegrator() {
        return integrator;
    }

    public void setIntegrator(String integrator) {
        this.integrator = integrator;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBizOppId() {
        return bizOppId;
    }

    public void setBizOppId(String bizOppId) {
        this.bizOppId = bizOppId;
    }
}
