package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla FAC_REGISTROFICHCONTA
 * 
 * @author carlos.vidal
 * @since 26/10/2004
 */



public class FacRegistroFichContaBean extends MasterBean{
	
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
	static public final String 	C_ESTADO  = 				"ESTADO";
	/*Metodos SET & GET*/
	public void   setIdContabilidad	 	(Integer valor){ this.idContabilidad = valor;}
	public void   setFechaCreacion 	 	(String  valor){ this.fechaCreacion  = valor;}
	public void   setFechaDesde    	 	(String  valor){ this.fechaDesde     = valor;}
	public void   setFechaHasta    	 	(String  valor){ this.fechaHasta     = valor;}
	public void   setNumeroAsientos	 	(Integer valor){ this.numeroAsientos = valor;}
	public void   setNombreFichero 	 	(String  valor){ this.nombreFichero  = valor;}
	public void   setIdInstitucion 	 	(Integer valor){ this.idInstitucion  = valor;}
	public void   setEstado 	 	(Integer valor){ this.estado   = valor;}
	public Integer getIdContabilidad	()			{ return this.idContabilidad;}	
	public String getFechaCreacion 		()			{ return this.fechaCreacion ;}	
	public String getFechaDesde    		()			{ return this.fechaDesde    ;}	
	public String getFechaHasta    		()			{ return this.fechaHasta    ;}	
	public Integer getNumeroAsientos	()			{ return this.numeroAsientos;}	
	public String getNombreFichero 		()			{ return this.nombreFichero ;}	
	public Integer getIdInstitucion 	()			{ return this.idInstitucion ;}	
	public Integer getEstado 	()			{ return this.estado ;}	
}