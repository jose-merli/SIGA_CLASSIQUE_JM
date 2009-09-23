/*
 * Created on 03-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * Modificado por david.sanchezp el 10/03/2005 para incluir los 3 nuevos campos de la tabla (para el TPV).
 * 
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysPeticionCompraSuscripcionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idEstadoPeticion;
	private Long 	idPeticion, idPeticionAlta, idPersona;
	private String 	tipoPeticion=null, fecha=null, numAut=null, numOperacion=null, referencia=null;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PETICIONCOMPRASUSCRIPCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDESTADOPETICION	= "IDESTADOPETICION";
	static public final String C_IDPETICION			= "IDPETICION";
	static public final String C_IDPETICIONALTA		= "IDPETICIONALTA";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_TIPOPETICION		= "TIPOPETICION";
	static public final String C_FECHA				= "FECHA";
	static public final String C_NUM_AUT			= "NUM_AUT";
	static public final String C_NUM_OPERACION		= "NUM_OPERACION";
	static public final String C_REFERENCIA			= "REFERENCIA";	

	
	/**
	 * @return Returns the idEstadoPeticion.
	 */
	public Integer getIdEstadoPeticion() {
		return idEstadoPeticion;
	}
	/**
	 * @param idEstadoPeticion The idEstadoPeticion to set.
	 */
	public void setIdEstadoPeticion(Integer idEstadoPeticion) {
		this.idEstadoPeticion = idEstadoPeticion;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idPeticion.
	 */
	public Long getIdPeticion() {
		return idPeticion;
	}
	/**
	 * @param idPeticion The idPeticion to set.
	 */
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	/**
	 * @return Returns the idPeticionAlta.
	 */
	public Long getIdPeticionAlta() {
		return idPeticionAlta;
	}
	/**
	 * @param idPeticionAlta The idPeticionAlta to set.
	 */
	public void setIdPeticionAlta(Long idPeticionAlta) {
		this.idPeticionAlta = idPeticionAlta;
	}
	/**
	 * @return Returns the tipoPeticion.
	 */
	public String getTipoPeticion() {
		return tipoPeticion;
	}
	/**
	 * @param tipoPeticion The tipoPeticion to set.
	 */
	public void setTipoPeticion(String tipoPeticion) {
		this.tipoPeticion = tipoPeticion;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Returns the numAut.
	 */
	public String getNumAut() {
		return numAut;
	}
	/**
	 * @param numAut The numAut to set.
	 */
	public void setNumAut(String numAut) {
		this.numAut = numAut;
	}
	/**
	 * @return Returns the numOperacion.
	 */
	public String getNumOperacion() {
		return numOperacion;
	}
	/**
	 * @param numOperacion The numOperacion to set.
	 */
	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}
	/**
	 * @return Returns the referencia.
	 */
	public String getReferencia() {
		return referencia;
	}
	/**
	 * @param referencia The referencia to set.
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
