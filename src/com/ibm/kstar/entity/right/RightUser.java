package com.ibm.kstar.entity.right;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//@Entity
//@Table(name = "view_right_user")
public class RightUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "view_right_user_id_generator")
	@GenericGenerator(name = "view_right_user_id_generator", strategy = "uuid")
	@Column(name = "user_id")
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
