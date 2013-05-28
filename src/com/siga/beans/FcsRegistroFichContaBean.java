package com.siga.beans;

public class FcsRegistroFichContaBean extends MasterBean{
	
	/* Variables */ 
	
    public static final int ESTADO_PROGRAMADO=1;
    public static final int ESTADO_ENPROCESO=2;
    public static final int ESTADO_TERMINADO=3;
    public static final int ESTADO_ERROR=4;
    
    private Integer	idContabilidad;
	private String	fechaCreacion;
	private String	fechaDesde;
	private String  fechaHasta;
	private Integer numeroAsientos;
	private String	nombreFichero;
	private Integer idInstitucion;
	private Integer estado;
	
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "FAC_REGISTROFICHCONTA";
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDCONTABILIDAD = 				"IDCONTABILIDAD";
	static public final String 	C_FECHACREACION  = 				"FECHACREACION";
	static public final String 	C_FECHADESDE     = 				"FECHADESDE";
	static public final String 	C_FECHAHASTA     = 				"FECHAHASTA";
	static public final String 	C_NUMEROASIENTOS = 				"NUMEROASIENTOS";
	static public final String 	C_NOMBREFICHERO  = 				"NOMBREFICHERO";
	static public final String 	C_IDINSTITUCION  = 				"IDINSTITUCION";
	static public final String 	C_ESTADO         = 				"ESTADO";
	
	public Integer getIdContabilidad() {
		return idContabilidad;
	}
	public void setIdContabilidad(Integer idContabilidad) {
		this.idContabilidad = idContabilidad;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public Integer getNumeroAsientos() {
		return numeroAsientos;
	}
	public void setNumeroAsientos(Integer numeroAsientos) {
		this.numeroAsientos = numeroAsientos;
	}
	public String getNombreFichero() {
		return nombreFichero;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	
	

}
