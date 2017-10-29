package com.ibm.kstar.entity.report;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rpt_t_rank_line")
public class RankLine  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "rpt_t_rank_line_id_generator")
	@GenericGenerator(name = "rpt_t_rank_line_id_generator", strategy = "uuid")
	@Column(name = "row_id")
	private String id;
	
	@Column(name = "header_id")
	private String headerId;

	@Column(name = "org_Id")
	private String orgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
