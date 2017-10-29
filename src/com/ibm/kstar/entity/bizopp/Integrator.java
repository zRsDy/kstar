package com.ibm.kstar.entity.bizopp;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商机变更-授权单位
 * Created by wangchao on 2017/3/22.
 */
@Entity
@Table(name = "CRM_T_BIZOPP_INTEGRATOR")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Integrator implements Serializable {

    @Column(name = "row_id")
    @Id
    @GeneratedValue(generator = "crm_t_bizopp_integrator_id_generator")
    @GenericGenerator(name="crm_t_bizopp_integrator_id_generator", strategy="uuid")
    private String id;

    @Column(name = "INTEGRATOR")
    private String integrator;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "BIZ_OPP_ID")
    private String bizOppId;
    
    @Column(name = "source_id")
	private String sourceId; // 变更前ID 

    public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

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
