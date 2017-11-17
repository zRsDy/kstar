package com.ibm.kstar.entity.channel;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流水记录表(crm_t_serial_number)
 * 
 * @author lhm
 * @version 1.0.0 2017-02-15
 */
@Entity
@Table(name = "crm_t_serial_number")
public class SerialNumber implements java.io.Serializable {
    /** 版本号 */
	private static final long serialVersionUID = 769920659960396481L;

	/** 主键自增 */
    @Id
    @Column(name = "c_pid", unique = true, nullable = false)
    @GeneratedValue(generator = "series_number_c_pid_generator")
   	@GenericGenerator(name="series_number_c_pid_generator", strategy="uuid")
    private String id;
    
    /** 流水模块 */
    @Column(name = "c_module")
    private String module;
    
    /** 组成日期*/
    @Column(name = "c_date")
    private String date;
    
    /** 组成编码 */
    @Column(name = "c_code")
    private String code;
    
    /** 流水号 */
	@Column(name = "c_serial")
	private Long serial;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getSerial() {
		return serial;
	}

	public void setSerial(Long serial) {
		this.serial = serial;
	}

}