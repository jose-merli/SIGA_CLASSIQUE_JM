/*
 * Created on 05-ene-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author pilard
 *
 * Clase que recoge y establece los valores del bean ADM_CONTADOR (almacena la configuracion de contadores) <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario
 */
/*
 * pilar.duran - 05-01-2007 - Creacion
 *	
 */


public class AdmContadorBean extends MasterBean {

	/* Variables */	
	private String 	idcontador, nombre, descripcion, modificableContador, general ;
	private Integer idinstitucion, modoContador, longitudContador, idModulo; 
	private String  prefijo, sufijo, fechaReconfiguracion, reconfiguracionContador, reconfiguracionPrefijo;
	private String  reconfiguracionSufijo, idTabla, idCampoContador, idCampoPrefijo, idCampoSufijo;
	private Long contador;	
	private Integer usucreacion;	
	private String	fechacreacion = "";
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "ADM_CONTADOR";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDCONTADOR    = "IDCONTADOR";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_DESCRIPCION    = "DESCRIPCION";
	static public final String C_MODIFICABLECONTADOR = "MODIFICABLECONTADOR";
	static public final String C_MODO    = "MODO";
	static public final String C_CONTADOR = "CONTADOR";
	static public final String C_PREFIJO    = "PREFIJO";
	static public final String C_SUFIJO = "SUFIJO";
	static public final String C_LONGITUDCONTADOR    = "LONGITUDCONTADOR";
	static public final String C_FECHARECONFIGURACION = "FECHARECONFIGURACION";
	static public final String C_RECONFIGURACIONCONTADOR    = "RECONFIGURACIONCONTADOR";
	static public final String C_RECONFIGURACIONPREFIJO = "RECONFIGURACIONPREFIJO";
	static public final String C_RECONFIGURACIONSUFIJO    = "RECONFIGURACIONSUFIJO";
	static public final String C_IDTABLA = "IDTABLA";
	static public final String C_IDCAMPOCONTADOR    = "IDCAMPOCONTADOR";
	static public final String C_IDCAMPOPREFIJO = "IDCAMPOPREFIJO";
	static public final String C_IDCAMPOSUFIJO    = "IDCAMPOSUFIJO";
	static public final String C_IDMODULO    = "IDMODULO";
	static public final String C_GENERAL    = "GENERAL";
	static public final String C_FECHACREACION ="FECHACREACION";
	static public final String C_USUCREACION="USUCREACION";

	// Metodos SET
	public void setIdContador(String idcontador) {this.idcontador = idcontador;}	
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setGeneral(String valor) {this.general = valor;}
	public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
	public void setModificableContador(String modificableContador) {this.modificableContador = modificableContador;}
	public void setLongitudContador(Integer longitudContador) {this.longitudContador = longitudContador;}
	public void setPrefijo(String prefijo) {this.prefijo = prefijo;}
	public void setSufijo(String sufijo) {this.sufijo = sufijo;}
	public void setFechaReconfiguracion(String fechaReconfiguracion) {this.fechaReconfiguracion = fechaReconfiguracion;}
	public void setReconfiguracionContador(String reconfiguracionContador) {this.reconfiguracionContador = reconfiguracionContador;}
	public void setReconfiguracionPrefijo(String reconfiguracionPrefijo) {this.reconfiguracionPrefijo = reconfiguracionPrefijo;}
	public void setReconfiguracionSufijo(String reconfiguracionSufijo) {this.reconfiguracionSufijo = reconfiguracionSufijo;}
	public void setIdTabla(String idTabla) {this.idTabla = idTabla;}
	public void setIdCampoContador(String idCampoContador) {this.idCampoContador = idCampoContador;}
	public void setIdCampoPrefijo(String idCampoPrefijo) {this.idCampoPrefijo = idCampoPrefijo;}
	public void setIdCampoSufijo(String idCampoSufijo) {this.idCampoSufijo = idCampoSufijo;}
	public void setIdinstitucion(Integer idinstitucion) {this.idinstitucion = idinstitucion;}
	public void setModoContador(Integer modoContador) {this.modoContador = modoContador;}
	public void setContador(Long contador) {this.contador = contador;}
	public void setIdModulo(Integer idModulo) {this.idModulo = idModulo;}
	public void setUsucreacion(Integer usucreacion) {this.usucreacion = usucreacion;}
	public void setFechacreacion(String fechacreacion) {this.fechacreacion = fechacreacion;}
	
	//Metodos GET
	
	public String getIdContador() {return idcontador;}	
	public String getNombre() {return nombre;}
	public String getGeneral() {return general;}
	public String getDescripcion() {return descripcion;}
	public String getModificableContador() {return modificableContador;}
	public Integer getLongitudContador() {return longitudContador;}
	public String getPrefijo() {return prefijo;}
	public String getSufijo() {return sufijo;}
	public String getFechaReconfiguracion(){return fechaReconfiguracion;}
	public String getReconfiguracionContador() {return reconfiguracionContador;}
	public String getReconfiguracionPrefijo() {return reconfiguracionPrefijo;}
	public String getReconfiguracionSufijo() {return reconfiguracionSufijo;}
	public String getIdTabla() {return idTabla;}
	public String getIdCampoContador() {return idCampoContador;}
	public String getIdCampoPrefijo() {return idCampoPrefijo;}
	public String getIdCampoSufijo() {return idCampoSufijo;}
	public Integer getIdinstitucion() {return idinstitucion;}
	public Integer getModoContador() {return modoContador;}
	public Long getContador() {return contador;}
	public Integer getIdModulo() {return idModulo;}
	public Integer getUsucreacion() {return usucreacion;}	
	public String getFechacreacion() {return fechacreacion;}
	
	
	
}

