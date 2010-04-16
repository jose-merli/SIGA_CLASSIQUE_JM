package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DESIGNALETRADO
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 * @version 07/02/2006 (david.sachezp): nuevos campos idProcurador e idInstitucionProcurador
 */

public class ScsContrariosDesignaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer idProcurador;
	private Integer idInstitucionProcurador;
	private Integer	anio;
	private Integer numero;
	private Integer idPersona;
	private String	observaciones;
	private String	nombreRepresentande;
	private String  idRepresentanteLegal;
	private String  idAbogadoContrario;
	private String  nombreAbogadoContrario;
	
	
	public String getIdAbogadoContrario() {
		return idAbogadoContrario;
	}
	public void setIdAbogadoContrario(String idAbogadoContrario) {
		this.idAbogadoContrario = idAbogadoContrario;
	}
	public String getnombreAbogadoContrario() {
		return nombreAbogadoContrario;
	}
	public void setnombreAbogadoContrario(String nombreAbogadoContrario) {
		this.nombreAbogadoContrario = nombreAbogadoContrario;
	}

	
	

	/**
	 * @return Returns the idInstitucionProcurador.
	 */
	public Integer getIdInstitucionProcurador() {
		return idInstitucionProcurador;
	}
	/**
	 * @param idInstitucionProcurador The idInstitucionProcurador to set.
	 */
	public void setIdInstitucionProcurador(Integer idInstitucionProcurador) {
		this.idInstitucionProcurador = idInstitucionProcurador;
	}
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_CONTRARIOSDESIGNA";
	
	
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_IDTURNO =					"IDTURNO";
	static public final String	C_IDPROCURADOR =			"IDPROCURADOR";
	static public final String	C_IDINSTITUCIONPROCURADOR =	"IDINSTITUCION_PROCU";
	static public final String	C_ANIO =					"ANIO";
	static public final String	C_NUMERO =					"NUMERO";
	static public final String	C_IDPERSONA	=			 	"IDPERSONA";
	static public final String	C_NOMBREREPRESENTANTE	=	"NOMBREREPRESENTANTE";
	static public final String	C_OBSERVACIONES	=			"OBSERVACIONES";
	static public final String	C_IDREPRESENTANTELEGAL	=	"IDREPRESENTANTELEGAL";
	static public final String	C_NOMBREABOGADOCONTRARIO=	"NOMBREABOGADOCONTRARIO";
	static public final String	C_IDABOGADOCONTRARIO	=	"IDABOGADOCONTRARIO";
		
	
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
	public void setNumero(Integer valor)				{ this.numero =	valor;}
	/**
	 * Almacena en el Bean la fecha de entrada de la designa 
	 * 
	 * @param valor Fecha de entrada de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setIdPersona(Integer valor)			{ this.idPersona =	valor;}
	/**
	 * Almacena en el Bean la fecha de fin de la designa 
	 * 
	 * @param valor Fecha de fin de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setNombreRepresentante(String valor)				{ this.nombreRepresentande =	valor;}
	/** Almacena en el Bean el Observaciones de la designa 
	 * 
	 * @param valor Observaciones del IdTipoDesignaColegiado de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones(String valor)			{ this.observaciones =	valor;}
	
	/**
	 * @param idProcurador The idProcurador to set.
	 */
	public void setIdProcurador(Integer idProcurador) {
		this.idProcurador = idProcurador;
	}
	/**
	 * @param nombreRepresentande The nombreRepresentande to set.
	 */
	public void setNombreRepresentande(String nombreRepresentande) {
		this.nombreRepresentande = nombreRepresentande;
	}

	/** Almacena en el Bean el idrepresentantelegal 
	 * 
	 * @param valor  idrepresentantelegal
	 * @return void 
	 */
	public void setIdRepresentanteLegal(String valor)			{ this.idRepresentanteLegal =	valor;}
	
	
	
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
	 * @return Numero
	 */
	public Integer getNumero()	{return this.numero;}
	/**
	 * Recupera del Bean la fecha de entrada de la designa
	 * 
	 * @return FechaEntrada 
	 */
	public Integer getIdPersona()			{ return this.idPersona;}
	/**
	 * Recupera del Bean la fecha de fin de la designa
	 * 
	 * @return FechaFin 
	 */
	public String getNombreRepresentante()				{ return this.nombreRepresentande;}
	/**
	 * Recupera del Bean las observaciones de la designa
	 * 
	 * @return Observaciones de la institucion
	 */
	public String getObservaciones()			{ return this.observaciones;}
	/**
	 * @return Returns the idProcurador.
	 */
	public Integer getIdProcurador() {
		return idProcurador;
	}
	/**
	 * @return Returns the nombreRepresentande.
	 */
	public String getNombreRepresentande() {
		return nombreRepresentande;
	}
	/**
	 * @return Returns the idRepresentanteLegal.
	 */
	public String getIdRepresentanteLegal() {
		return idRepresentanteLegal;
	}

}