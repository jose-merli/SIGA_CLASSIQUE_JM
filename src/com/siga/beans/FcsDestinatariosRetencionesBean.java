/**
 * VERSIONES:
 * 
 * jose.barrientos - 29/01/2009 Creacion
 *	
 */
package com.siga.beans;


public class FcsDestinatariosRetencionesBean extends MasterBean {
	
	/* Variables */
	private Integer  idInstitucion, idDestinatario;
	private String   nombre, cuentaContable, codigoExt, orden;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_DESTINATARIOS_RETENCIONES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDDESTINATARIO			= "IDDESTINATARIO";	
	static public final String C_NOMBRE					= "NOMBRE";
	static public final String C_CODIGOEXT		 		= "CODIGOEXT";
	static public final String C_CUENTACONTABLE 		= "CUENTACONTABLE";
	static public final String C_ORDEN					= "ORDEN";
	
	// Métodos GET	
	public Integer getIdDestinatario() {
		return idDestinatario;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public String getNombre() {
		return nombre;
	}
	public String getCuentaContable(){
		return cuentaContable;
	}
	public String getOrden(){
		return orden;
	}
	public String getCodigoExt(){
		return codigoExt;
	}
	
	//	 Métodos SET	
	public void setIdDestinatario(Integer idDestinatario) {
		this.idDestinatario = idDestinatario;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setCuentaContable(String cuentaContable){
		this.cuentaContable = cuentaContable;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
}
