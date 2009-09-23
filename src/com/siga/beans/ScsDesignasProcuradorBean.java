package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DESIGNAPROCURADOR
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 */

public class ScsDesignasProcuradorBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idInstitucionProc;
	private Integer	idInstitucion;
	private Integer	idProcurador;
	private Integer	idTurno;
	private Integer	idTipoMotivo;
	private Integer	anio;
	private Integer numero;
	private Integer numeroDesignacion;
	private String	fechaDesigna;
	private String	observaciones;
	private String	fechaRenuncia;
	private String	fechaRenunciaSolicita;
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_DESIGNAPROCURADOR";
	
	
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_IDTURNO =					"IDTURNO";
	static public final String	C_ANIO =					"ANIO";
	static public final String	C_NUMERO =					"NUMERO";
	static public final String	C_IDPROCURADOR =			"IDPROCURADOR";
	static public final String	C_IDINSTITUCION_PROC = 		"IDINSTITUCION_PROC";
	static public final String	C_FECHADESIGNA =			"FECHADESIGNA";
	static public final String	C_NUMERODESIGNACION =		"NUMERODESIGNACION";
	static public final String	C_FECHARENUNCIASOLICITA =	"FECHARENUNCIASOLICITA";
	static public final String	C_IDTIPOMOTIVO =			"IDTIPOMOTIVO";
	static public final String	C_OBSERVACIONES =			"OBSERVACIONES";
	static public final String	C_FECHARENUNCIA =			"FECHARENUNCIA";
		
	/*
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion de la designa 
	 * 
	 * @param valor Identificador de la institucion de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion (Integer valor)	{ this.idInstitucion = 	valor;}
	/**
	 * Almacena en el Bean el identificador del turno de la designa 
	 * 
	 * @param valor Identificador del turno de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno	(Integer valor)	{ this.idTurno =	valor;}
	
	/**
	 * Almacena en el Bean el anho de la designa 
	 * 
	 * @param valor Anho de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio	(Integer valor)	{ this.anio =	valor;}
	
	/**
	 * Almacena en el Bean el numero de la designa 
	 * 
	 * @param valor Numero de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setNumero(Integer valor)	{ this.numero =	valor;}
	
	
	/**
	 * Almacena en el Bean la fecha de la designa 
	 * 
	 * @param valor Fecha de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setFechaDesigna	(String valor)	{ this.fechaDesigna =	valor;}
	

	/**
	 * Almacena en el Bean las observaciones de la designa 
	 * 
	 * @param valor Observaciones de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones(String valor)	{ this.observaciones =	valor;}

	/**
	 * Almacena en el Bean la fecha de renuncia de la designa 
	 * 
	 * @param valor Fecha de renuncia de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setFechaRenuncia(String valor)	{ this.fechaRenuncia =	valor;}


	public void setFechaRenunciaSolicita(String fechaRenunciaSolicita) 	{this.fechaRenunciaSolicita = fechaRenunciaSolicita;	}
	public void setIdInstitucionProc(Integer idInstitucionProc) 		{this.idInstitucionProc = idInstitucionProc;			}
	public void setIdProcurador(Integer idProcurador) 					{this.idProcurador = idProcurador;						}
	public void setIdTipoMotivo(Integer idTipoMotivo)					{this.idTipoMotivo = idTipoMotivo;						}
	public void setNumeroDesignacion(Integer numeroDesignacion)			{this.numeroDesignacion = numeroDesignacion;			}
	

	
	/*
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la institucion de la designa
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion ()	{return this.idInstitucion;}
	/**
	 * Recupera del Bean el identificador del turno de la designa
	 * 
	 * @return Identificador del turno
	 */
	public Integer getIdTurno	()	{return this.idTurno;}
	
	/**
	 * Recupera del Bean el anho de la designa
	 * 
	 * @return Anho de la institucion
	 */
	public Integer getAnio	()	{return this.anio;}
	
	/**
	 * Recupera del Bean el identificador el numero de la designa
	 * 
	 * @return Numero de la institucion
	 */
	public Integer getNumero()	{return this.numero;}
	
	/**
	 * Recupera del Bean la fecha de la designa
	 * 
	 * @return Fecha de la designa
	 */
	public String getFechaDesigna	()	{return this.fechaDesigna;}
	
	/**
	 * Recupera del Bean las observaciones de la designa
	 * 
	 * @return Observaciones 
	 */
	public String getObservaciones()	{return this.observaciones;}

	/**
	 * Recupera del Bean la fecha de la renuncia de la designa
	 * 
	 * @return Fecha de renuncia de la designa
	 */
	public String getFechaRenuncia()	{return this.fechaRenuncia;}


	public String getFechaRenunciaSolicita()	{return fechaRenunciaSolicita;	}
	public Integer getIdInstitucionProc() 		{return idInstitucionProc;		}
	public Integer getIdProcurador() 			{return idProcurador;			}
	public Integer getIdTipoMotivo() 			{return idTipoMotivo;			}
	public Integer getNumeroDesignacion() 		{return numeroDesignacion;		}
	
	}