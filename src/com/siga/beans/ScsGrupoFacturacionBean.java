package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_GRUPOFACTURACION
 * 
 * @author ruben.fernandez
 * @since 06/12/2004 
 */
 
public class ScsGrupoFacturacionBean extends MasterBean{
	
	/**
	 *  Variables 
	 * */ 
	
	private Integer		idGrupoFacturacion;
	private String		nombre;
	private Integer 	idInstitucion;
		
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "SCS_GRUPOFACTURACION";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDGRUPOFACTURACION 		= 		"IDGRUPOFACTURACION";
	static public final String 	C_NOMBRE 					= 		"NOMBRE";
	static public final String 	C_IDINSTITUCION 			= 		"IDINSTITUCION";
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////
		
	// Métodos GET
		
	public Integer getIdGrupoFacturacion() {
		return idGrupoFacturacion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public String getNombre() {
		return nombre;
	}
	
	//	 Métodos SET
	
	public void setIdGrupoFacturacion(Integer idGrupoFacturacion) {
		this.idGrupoFacturacion = idGrupoFacturacion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}