package com.siga.beans;

public class ScsDocumentacionEJGExtendedBean {
	private String idPresentador;
	private String descPresentador;
	private String documentoAbreviatura;
	private ScsDocumentacionEJGBean documentacionEJGBean;
	private String comisionAJG;
	private Short numIntercambiosOk;
	public String getIdPresentador() {
		return idPresentador;
	}

	public void setIdPresentador(String idPresentador) {
		this.idPresentador = idPresentador;
	}

	public String getDescPresentador() {
		return descPresentador;
	}

	public void setDescPresentador(String descPresentador) {
		this.descPresentador = descPresentador;
	}

	public String getDocumentoAbreviatura() {
		return documentoAbreviatura;
	}

	public void setDocumentoAbreviatura(String documentoAbreviatura) {
		this.documentoAbreviatura = documentoAbreviatura;
	}

	public ScsDocumentacionEJGBean getDocumentacionEJGBean() {
		return documentacionEJGBean;
	}

	public void setDocumentacionEJGBean(ScsDocumentacionEJGBean documentacionEJGBean) {
		this.documentacionEJGBean = documentacionEJGBean;
	}

	public String getComisionAJG() {
		return comisionAJG;
	}

	public void setComisionAJG(String comisionAJG) {
		this.comisionAJG = comisionAJG;
	}

	public Short getNumIntercambiosOk() {
		return numIntercambiosOk;
	}

	public void setNumIntercambiosOk(Short numIntercambiosOk) {
		this.numIntercambiosOk = numIntercambiosOk;
	}
	
}
