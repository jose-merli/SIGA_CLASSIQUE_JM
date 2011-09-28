package com.siga.beans;

import es.satec.siga.util.SigaSequence;


public class GenWebServiceLogBean extends MasterBean {
	
	/* Variables */
	private SigaSequence idWebServiceLog = new SigaSequence("SEQ_GEN_WEBSERVICE_LOG");
	private Integer idInstitucion;
		
	private String 	rqRs, xmlSoap, descripcion;

	/* Nombre tabla */
	static public String T_NOMBRETABLA 					= "GEN_WEBSERVICE_LOG";
	
	/* Nombre campos de la tabla */
	
	static public final String C_IDWEBSERVICELOG = "IDWEBSERVICELOG";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";	
	static public final String C_RQ_RS = "RQ_RS";
	static public final String C_XML_SOAP = "XML_SOAP";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	public String getRqRs() {
		return rqRs;
	}
	public void setRqRs(String rqRs) {
		this.rqRs = rqRs;
	}
	public String getXmlSoap() {
		return xmlSoap;
	}
	public void setXmlSoap(String xmlSoap) {
		this.xmlSoap = xmlSoap;
	}
	public SigaSequence getIdWebServiceLog() {
		return idWebServiceLog;
	}
	public void setIdWebServiceLog(SigaSequence idWebServiceLog) {
		this.idWebServiceLog = idWebServiceLog;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}

