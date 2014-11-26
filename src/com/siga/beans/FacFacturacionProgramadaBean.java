/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creaci�n
 */

package com.siga.beans;

//import java.util.Date;


public class FacFacturacionProgramadaBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idEstadoConfirmacion, idEstadoPDF, idEstadoEnvio, idTipoPlantillaMail, idTipoEnvios;
	
	private Long idSerieFacturacion, idProgramacion, idPrevision;
	
	private String 	fechaInicioProductos, fechaFinProductos, fechaInicioServicios, fechaFinServicios, 
					fechaRealGeneracion, fechaConfirmacion, fechaProgramacion, 
					generarPDF,envio,archivarFact,fechaPrevistaConfirmacion,fechaPrevistaGeneracion,
					fechaCargo, confIngresos, confDeudor, ctaIngresos, ctaClientes, visible, descripcion, realizarEnvio;
	
	private String fechaPresentacion, fechaRecibosPrimeros, fechaRecibosRecurrentes, fechaRecibosCOR1, fechaRecibosB2B;

	private String logerror, nombrefichero;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_FACTURACIONPROGRAMADA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION 		= "IDSERIEFACTURACION";
	static public final String C_IDPROGRAMACION 			= "IDPROGRAMACION";
	static public final String C_FECHAINICIOPRODUCTOS 		= "FECHAINICIOPRODUCTOS";
	static public final String C_FECHAFINPRODUCTOS 			= "FECHAFINPRODUCTOS";
	static public final String C_FECHAINICIOSERVICIOS 		= "FECHAINICIOSERVICIOS";
	static public final String C_FECHAFINSERVICIOS 			= "FECHAFINSERVICIOS";
	static public final String C_FECHAPROGRAMACION 			= "FECHAPROGRAMACION";
	static public final String C_FECHAPREVISTAGENERACION 	= "FECHAPREVISTAGENERACION";
	static public final String C_IDPREVISION 				= "IDPREVISION";
	static public final String C_FECHAREALGENERACION 		= "FECHAREALGENERACION";
	static public final String C_FECHACONFIRMACION 			= "FECHACONFIRMACION";

	static public final String C_FECHAPREVISTACONFIRM 		= "FECHAPREVISTACONFIRM";
	static public final String C_IDESTADOCONFIRMACION 		= "IDESTADOCONFIRMACION";
	static public final String C_IDESTADOPDF 				= "IDESTADOPDF";
	static public final String C_IDESTADOENVIO 				= "IDESTADOENVIO";
	static public final String C_GENERAPDF 					= "GENERAPDF";
	static public final String C_ENVIO 						= "ENVIO";
	static public final String C_ARCHIVARFACT 				= "ARCHIVARFACT";
	static public final String C_FECHACARGO					= "FECHACARGO";
	static public final String C_CONFDEUDOR					= "CONFDEUDOR";
	static public final String C_CONFINGRESOS				= "CONFINGRESOS";
	static public final String C_CTAINGRESOS				= "CTAINGRESOS";
	static public final String C_CTACLIENTES				= "CTACLIENTES";
	static public final String C_VISIBLE					= "VISIBLE";
	static public final String C_DESCRIPCION 		    	= "DESCRIPCION";
	static public final String C_IDTIPOPLANTILLAMAIL 		= "IDTIPOPLANTILLAMAIL";
	static public final String C_IDTIPOENVIOS		 		= "IDTIPOENVIOS";
	static public final String C_FECHAPRESENTACION			= "FECHAPRESENTACION";
	static public final String C_FECHARECIBOSPRIMEROS		= "FECHARECIBOSPRIMEROS";
	static public final String C_FECHARECIBOSRECURRENTES	= "FECHARECIBOSRECURRENTES";
	static public final String C_FECHARECIBOSCOR1			= "FECHARECIBOSCOR1";
	static public final String C_FECHARECIBOSB2B			= "FECHARECIBOSB2B";
	static public final String C_NOMBREFICHERO				= "NOMBREFICHERO";
	static public final String C_LOGERROR					= "LOGERROR";

	// Metodos SET
	public void setIdInstitucion (Integer id)			{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)			{ this.idSerieFacturacion = id; }
	public void setIdProgramacion (Long id)				{ this.idProgramacion = id; }
	public void setFechaInicioProductos (String f)		{ this.fechaInicioProductos = f; }
	public void setFechaFinProductos (String f)			{ this.fechaFinProductos = f; }
	public void setFechaInicioServicios (String f)		{ this.fechaInicioServicios = f; }
	public void setFechaFinServicios (String f)			{ this.fechaFinServicios = f; }
	public void setFechaRealGeneracion (String f)		{ this.fechaRealGeneracion = f; }
	public void setFechaConfirmacion (String f)			{ this.fechaConfirmacion = f; }
	public void setFechaProgramacion (String f)			{ this.fechaProgramacion = f; }
	public void setFechaPrevistaGeneracion (String f)	{ this.fechaPrevistaGeneracion = f; }
	public void setIdPrevision (Long id)				{ this.idPrevision = id; }
	
	public void setFechaPrevistaConfirmacion (String id)				{ this.fechaPrevistaConfirmacion = id; }
	public void setIdEstadoConfirmacion (Integer id)				{ this.idEstadoConfirmacion = id; }
	public void setIdEstadoPDF (Integer id)				{ this.idEstadoPDF = id; }
	public void setIdEstadoEnvio (Integer id)				{ this.idEstadoEnvio = id; }
	public void setGenerarPDF (String id)				{ this.generarPDF = id; }
	public void setEnvio (String id)				{ this.envio = id; }
	public void setArchivarFact (String id)				{ this.archivarFact = id; }

	public void setConfDeudor (String id)				{ this.confDeudor = id; }
	public void setConfIngresos (String id)				{ this.confIngresos = id; }
	public void setCtaIngresos (String id)				{ this.ctaIngresos = id; }
	public void setCtaClientes (String id)				{ this.ctaClientes = id; }
	public void setVisible (String id)				{ this.visible = id; }
	public void setDescripcion(String descripcion) 	{ this.descripcion = descripcion; }


	// Metodos GET
	public Integer getIdInstitucion 			()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion			()	{ return this.idSerieFacturacion; }
	public Long getIdProgramacion				()	{ return this.idProgramacion; }
	public String  getFechaInicioProductos 		()	{ return this.fechaInicioProductos; }
	public String  getFechaFinProductos			()	{ return this.fechaFinProductos; }
	public String  getFechaInicioServicios 		()	{ return this.fechaInicioServicios; }
	public String  getFechaFinServicios			()	{ return this.fechaFinServicios; }
	public String  getFechaRealGeneracion		()	{ return this.fechaRealGeneracion; }
	public String  getFechaConfirmacion			()	{ return this.fechaConfirmacion; }
	public String  getFechaProgramacion			()	{ return this.fechaProgramacion; }
	public String  getFechaPrevistaGeneracion	()	{ return this.fechaPrevistaGeneracion; }
	public Long    getIdPrevision				()	{ return this.idPrevision; }
	
	public String getFechaPrevistaConfirmacion  ()				{return  this.fechaPrevistaConfirmacion;}
	public Integer getIdEstadoConfirmacion 		()				{return  this.idEstadoConfirmacion;}
	public Integer getIdEstadoPDF ()				{return  this.idEstadoPDF;}
	public Integer getIdEstadoEnvio ()				{return  this.idEstadoEnvio;}
	public String getGenerarPDF ()				{return  this.generarPDF;}
	public String getEnvio ()				{return  this.envio;}
	public String getArchivarFact ()				{return  this.archivarFact; }

	public String    getConfDeudor				()	{ return this.confDeudor; }
	public String    getConfIngresos				()	{ return this.confIngresos; }
	public String    getCtaIngresos				()	{ return this.ctaIngresos; }
	public String    getCtaClientes				()	{ return this.ctaClientes; }
	public String    getVisible				()	{ return this.visible; }
	public String getDescripcion			() 	{ return descripcion;	}

	
	public String getFechaCargo() {
		return fechaCargo;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	public String getRealizarEnvio() {
		return this.realizarEnvio;
	}
	public void setRealizarEnvio(String realizarEnvio) {
		this.realizarEnvio = realizarEnvio;
	}
	
	public Integer getIdTipoPlantillaMail() {
		return idTipoPlantillaMail;
	}
	public void setIdTipoPlantillaMail(Integer idTipoPlantillaMail) {
		this.idTipoPlantillaMail = idTipoPlantillaMail;
	}
	public Integer getIdTipoEnvios() {
		return idTipoEnvios;
	}
	public void setIdTipoEnvios(Integer idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}
	public String getFechaPresentacion() {
		return fechaPresentacion;
	}
	public void setFechaPresentacion(String fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}
	public String getFechaRecibosRecurrentes() {
		return fechaRecibosRecurrentes;
	}
	public void setFechaRecibosRecurrentes(String fechaRecibosRecurrentes) {
		this.fechaRecibosRecurrentes = fechaRecibosRecurrentes;
	}
	public String getFechaRecibosCOR1() {
		return fechaRecibosCOR1;
	}
	public void setFechaRecibosCOR1(String fechaRecibosCOR1) {
		this.fechaRecibosCOR1 = fechaRecibosCOR1;
	}
	public String getFechaRecibosB2B() {
		return fechaRecibosB2B;
	}
	public void setFechaRecibosB2B(String fechaRecibosB2B) {
		this.fechaRecibosB2B = fechaRecibosB2B;
	}
	public String getFechaRecibosPrimeros() {
		return fechaRecibosPrimeros;
	}
	public void setFechaRecibosPrimeros(String fechaRecibosPrimeros) {
		this.fechaRecibosPrimeros = fechaRecibosPrimeros;
	}
	public String getLogerror() {
		return logerror;
	}
	public void setLogerror(String logerror) {
		this.logerror = logerror;
	}
	public String getNombrefichero() {
		return nombrefichero;
	}
	public void setNombrefichero(String nombrefichero) {
		this.nombrefichero = nombrefichero;
	}
}
