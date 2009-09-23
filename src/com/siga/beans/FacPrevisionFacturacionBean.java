/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

package com.siga.beans;


public class FacPrevisionFacturacionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	
	private Long idSerieFacturacion, idPrevision;
	
	private String 	fechaInicioProductos, fechaFinProductos, fechaInicioServicios, fechaFinServicios, nombreFichero, descripcion;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_PREVISIONFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION 	= "IDSERIEFACTURACION";
	static public final String C_IDPREVISION 			= "IDPREVISION";
	static public final String C_FECHAINICIOPRODUCTOS 	= "FECHAINICIOPRODUCTOS";
	static public final String C_FECHAFINPRODUCTOS 		= "FECHAFINPRODUCTOS";
	static public final String C_FECHAINICIOSERVICIOS 	= "FECHAINICIOSERVICIOS";
	static public final String C_FECHAFINSERVICIOS 		= "FECHAFINSERVICIOS";
	static public final String C_NOMBREFICHERO 		    = "NOMBREFICHERO";
	static public final String C_DESCRIPCION 		    = "DESCRIPCION";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
	public void setIdPrevision (Long id)			{ this.idPrevision = id; }
	public void setFechaInicioProductos (String f)	{ this.fechaInicioProductos = f; }
	public void setFechaFinProductos (String f)		{ this.fechaFinProductos = f; }
	public void setFechaInicioServicios (String f)	{ this.fechaInicioServicios = f; }
	public void setFechaFinServicios (String f)		{ this.fechaFinServicios = f; }
	public void setNombreFichero (String n)			{ this.nombreFichero = n; }
	public void setDescripcion(String descripcion) 	{ this.descripcion = descripcion; }
	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
	public Long getIdPrevision				()	{ return this.idPrevision; }
	public String  getFechaInicioProductos 	()	{ return this.fechaInicioProductos; }
	public String  getFechaFinProductos		()	{ return this.fechaFinProductos; }
	public String  getFechaInicioServicios 	()	{ return this.fechaInicioServicios; }
	public String  getFechaFinServicios		()	{ return this.fechaFinServicios; }
	public String  getNombreFichero			()	{ return this.nombreFichero; }
	public String getDescripcion			() 	{ return descripcion;	}
}
