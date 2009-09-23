/*
 * Created on 01-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author s230298
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsAcreditacionProcedimientoBean extends MasterBean {

	/* Variables */	
	private String 	idProcedimiento;
	private Integer idInstitucion, idAcreditacion, porcentaje;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_ACREDITACIONPROCEDIMIENTO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPROCEDIMIENTO	= "IDPROCEDIMIENTO";
	static public final String C_IDACREDITACION		= "IDACREDITACION";
	static public final String C_PORCENTAJE			= "PORCENTAJE";

	
	/**
	 * @return Returns the idAcreditacion.
	 */
	public Integer getIdAcreditacion() {
		return idAcreditacion;
	}
	/**
	 * @param idAcreditacion The idAcreditacion to set.
	 */
	public void setIdAcreditacion(Integer idAcreditacion) {
		this.idAcreditacion = idAcreditacion;
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
	 * @return Returns the idProcedimiento.
	 */
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	/**
	 * @param idProcedimiento The idProcedimiento to set.
	 */
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	/**
	 * @return Returns the porcentaje.
	 */
	public Integer getPorcentaje() {
		return porcentaje;
	}
	/**
	 * @param porcentaje The porcentaje to set.
	 */
	public void setPorcentaje(Integer porcentaje) {
		this.porcentaje = porcentaje;
	}
}
