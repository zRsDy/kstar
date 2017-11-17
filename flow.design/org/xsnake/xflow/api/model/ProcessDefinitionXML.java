//package org.xsnake.xflow.api.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//@Table(name = "XFLOW_PROCESS_DEFINITION_XML")
//public class ProcessDefinitionXML implements Serializable {
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GenericGenerator(name = "xflow_process_definition_xml_id_generator", strategy = "assigned")
//	@GeneratedValue(generator = "xflow_process_definition_xml_id_generator")
//	@Column(name = "ROW_ID")
//	String id;
//	
//	@Column(name = "XML")
//	String xml;
//	
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getXml() {
//		return xml;
//	}
//
//	public void setXml(String xml) {
//		this.xml = xml;
//	}
//
//}
