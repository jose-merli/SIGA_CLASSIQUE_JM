//VERSIONES:
//raul.ggonzalez 08-03-2005 creacion
//
package com.siga.beans;

/**
* Bean de la tabla FCS_FACT_ESTADOSFACTURACION
* 
* @author AtosOrigin 08-03-2005
* 
*/
public class FcsFactEstadosFacturacionBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idEstadoFacturacion, idFacturacion,idOrdenEstado;	

	private String 	fechaEstado;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_FACT_ESTADOSFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDFACTURACION	= "IDFACTURACION";
	static public final String C_IDESTADOFACTURACION  = "IDESTADOFACTURACION";
	static public final String C_FECHAESTADO			= "FECHAESTADO";
	static public final String C_FECHAMODIFICACION			= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION			= "USUMODIFICACION";
	static public final String C_IDORDENESTADO			= "IDORDENESTADO";
	

	/**
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 */
	public void setIdInstitucion(Integer dato) {
		this.idInstitucion = dato;
	}
	/**
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 */
	public void setIdFacturacion(Integer dato) {
		this.idFacturacion = dato;
	}
	/**
	 */
	public Integer getIdEstadoFacturacion() {
		return idEstadoFacturacion;
	}
	/**
	 */
	public void setIdEstadoFacturacion(Integer dato) {
		this.idEstadoFacturacion = dato;
	}
	/**
	 */
	public String getFechaEstado() {
		return fechaEstado;
	}
	/**
	 */
	public void setFechaEstado(String dato) {
		this.fechaEstado = dato;
	}
	
	public Integer getIdOrdenEstado() {
		return idOrdenEstado;
	}
	public void setIdOrdenEstado(Integer idOrdenEstado) {
		this.idOrdenEstado = idOrdenEstado;
	}
}
