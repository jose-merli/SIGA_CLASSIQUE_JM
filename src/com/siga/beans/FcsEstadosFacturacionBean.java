//VERSIONES:
//raul.ggonzalez 08-03-2005 creacion
//
package com.siga.beans;

/**
* Bean de la tabla FCS_ESTADOSFACTURACION
* 
* @author AtosOrigin 08-03-2005
* 
*/
public class FcsEstadosFacturacionBean extends MasterBean {
	
	/* Variables */
	private Integer idEstadoFacturacion;
	
	private String 	descripcion;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_ESTADOSFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADOFACTURACION  = "IDESTADOFACTURACION";
	static public final String C_DESCRIPCION			= "DESCRIPCION";
	static public final String C_FECHAMODIFICACION			= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION			= "USUMODIFICACION";
	

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
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 */
	public void setDescripcion(String dato) {
		this.descripcion = dato;
	}
}
