//VERSIONES:
//Creacion: david.sanchezp 18-03-2005
//
package com.siga.beans;

/**
* Bean de la tabla FCS_ESTADOSPAGOS
* 
* @author david.sanchezp 
* 
*/
public class FcsPagosEstadosPagosBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idPagosJG, idEstadoPagosJG;
	private String 	fechaEstado = null;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_PAGOS_ESTADOSPAGOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDPAGOSJG 			= "IDPAGOSJG";
	static public final String C_IDESTADOPAGOSJG 	= "IDESTADOPAGOSJG";
	static public final String C_FECHAESTADO		= "FECHAESTADO";

	/**
	 * @return Returns the fechaEstado.
	 */
	public String getFechaEstado() {
		return fechaEstado;
	}
	/**
	 * @param fechaEstado The fechaEstado to set.
	 */
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
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
	 * @return Returns the idPagosJG.
	 */
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param idPagosJG The idPagosJG to set.
	 */
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
}
