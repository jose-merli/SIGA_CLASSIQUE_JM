/*
 * Created on 
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysLineaAnticipoBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idAnticipo, idLinea,idPersona;
	private Long NumeroLinea;
	private String 	idFactura, liquidacion, fechaEfectiva;	
	private Double  importeAnticipado; 
	
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA 						= "PYS_LINEAANTICIPO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION        		= "IDINSTITUCION";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_IDANTICIPO       			= "IDANTICIPO";
	static public final String C_IDLINEA        			= "IDLINEA";
	static public final String C_IDFACTURA        			= "IDFACTURA";
	static public final String C_NUMEROLINEA        			= "NUMEROLINEA";
	static public final String C_IMPORTEANTICIPADO        	= "IMPORTEANTICIPADO";
	static public final String C_LIQUIDACION        	= "LIQUIDACION";
	static public final String C_FECHAEFECTIVA        	= "FECHAEFECTIVA";
	/**
	 * @return the idInstitucion
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return the idAnticipo
	 */
	public Integer getIdAnticipo() {
		return idAnticipo;
	}
	/**
	 * @param idAnticipo the idAnticipo to set
	 */
	public void setIdAnticipo(Integer idAnticipo) {
		this.idAnticipo = idAnticipo;
	}
	/**
	 * @return the idLinea
	 */
	public Integer getIdLinea() {
		return idLinea;
	}
	/**
	 * @param idLinea the idLinea to set
	 */
	public void setIdLinea(Integer idLinea) {
		this.idLinea = idLinea;
	}
	/**
	 * @return the idPersona
	 */
	public Integer getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona the idPersona to set
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return the idPersona
	 */
	public String getFechaEfectiva() {
		return fechaEfectiva;
	}
	/**
	 * @param idPersona the idPersona to set
	 */
	public void setFechaEfectiva(String valor) {
		this.fechaEfectiva = valor;
	}
	/**
	 * @param Liquidacion the idPersona to set
	 */
	public void setLiquidacion(String Liquidacion) {
		this.liquidacion = Liquidacion;
	}
	/**
	 * @return the idFactura
	 */
	public String getIdFactura() {
		return idFactura;
	}
	/**
	 * @param idFactura the idFactura to set
	 */
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}
	/**
	 * @return the idFactura
	 */
	public Long getNumeroLinea() {
		return NumeroLinea;
	}
	/**
	 * @param idFactura the idFactura to set
	 */
	public void setNumeroLinea(Long NumeroLinea) {
		this.NumeroLinea = NumeroLinea;
	}
	/**
	 * @return the importeAnticipado
	 */
	public Double getImporteAnticipado() {
		return importeAnticipado;
	}
	/**
	 * @param importeAnticipado the importeAnticipado to set
	 */
	public void setImporteAnticipado(Double importeAnticipado) {
		this.importeAnticipado = importeAnticipado;
	}
	/**
	 * @param Liquidacion the importeAnticipado to set
	 */
	public String getLiquidacion() {
		return this.liquidacion;
	}
	

}
