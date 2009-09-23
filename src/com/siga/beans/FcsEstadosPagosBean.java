//VERSIONES:
//Creacion: david.sanchezp 18-03-2005
//
package com.siga.beans;

/**
* Bean de la tabla FCS_ESTADOSPAGOSJG
* 
* @author david.sanchezp 
* 
*/
public class FcsEstadosPagosBean extends MasterBean {
	
	/* Variables */
	private Integer idEstadoPagosJG;
	private String 	descripcion = null;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_ESTADOSPAGOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADOPAGOSJG 		= "IDESTADOPAGOSJG";
	static public final String C_DESCRIPCION			= "DESCRIPCION";

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the idEstadoPagosJG.
	 */
	public Integer getIdEstadoPagosJG() {
		return idEstadoPagosJG;
	}
	/**
	 * @param idEstadoPagosJG The idEstadoPagosJG to set.
	 */
	public void setIdEstadoPagosJG(Integer idEstadoPagosJG) {
		this.idEstadoPagosJG = idEstadoPagosJG;
	}
}
