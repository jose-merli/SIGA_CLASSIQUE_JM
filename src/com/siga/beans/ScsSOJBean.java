/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsSOJBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoSOJ;
	private Integer	anio;
	private Integer	numero;
	private String	fechaApertura;
	private String	estado;
	private String 	descripcionConsulta;
	private String 	respuestaLetrado;
	private Long	idPersona;
	private Integer	idTurno;
	private Integer	idFActuracion;
	private Integer idInstitucion;
	private Integer idTipoSOJColegio;
	private Integer idGuardia;
	private Integer idPersonaJG;
	private String numSOJ;
	private String sufijo;
	private String facturado;
	private String pagado;
	private Integer idFacturacion;
	private Integer idTipoConsulta;
	private Integer idTipoRespuesta;

	private Integer ejgIdTipoEJG, ejgAnio, ejgNumero;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_SOJ";
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOSOJ = 					"IDTIPOSOJ";
	static public final String 	C_ANIO = 						"ANIO";
	static public final String 	C_NUMERO = 						"NUMERO";
	static public final String  C_FECHAAPERTURA = 				"FECHAAPERTURA";
	static public final String  C_ESTADO = 						"ESTADO"; 
	static public final String  C_DESCRIPCIONCONSULTA = 		"DESCRIPCIONCONSULTA";
	static public final String  C_RESPUESTALETRADO = 			"RESPUESTALETRADO";
	static public final String  C_IDPERSONA = 					"IDPERSONA";
	static public final String  C_IDINSTITUCION = 				"IDINSTITUCION";
	static public final String  C_IDTURNO = 					"IDTURNO";
	static public final String  C_IDFACTURACION = 				"IDFACTURACION";	
	static public final String  C_IDTIPOSOJCOLEGIO = 			"IDTIPOSOJCOLEGIO";
	static public final String  C_IDGUARDIA = 					"IDGUARDIA";
	static public final String  C_IDPERSONAJG = 				"IDPERSONAJG";
	static public final String  C_NUMSOJ = 						"NUMSOJ";
	static public final String  C_IDTIPOCONSULTA = 				"IDTIPOCONSULTA";
	static public final String  C_IDTIPORESPUESTA = 			"IDTIPORESPUESTA";

	static public final String  C_EJGIDTIPOEJG = 				"EJGIDTIPOEJG";
	static public final String  C_EJGANIO = 					"EJGANIO";;
	static public final String  C_EJGNUMERO = 					"EJGNUMERO";
	static public final String  C_SUFIJO = 					    "SUFIJO";
	
	static public final String C_FACTURADO = "FACTURADO";
	static public final String C_PAGADO = "PAGADO";

	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el identificador de un SOJ
	 * 
	 * @param valor Identificador SOJ. 
	 * @return void 
	 */
	public void setIdTipoSOJ					(Integer valor)	{ this.idTipoSOJ = valor;}
	
	/**
	 * Almacena en el Bean el anho de un SOJ
	 * 
	 * @param valor Anho SOJ  
	 * @return void 
	 */
	public void setAnio							(Integer valor)	{ this.anio = valor;}
	
	/**
	 * Almacena en el Bean el número de un SOJ
	 * 
	 * @param valor Número SOJ 
	 * @return void 
	 */
	public void setNumero						(Integer valor)	{ this.numero = valor;}
	
	/**
	 * Almacena en el Bean la fecha de apertura de un SOJ
	 * 
	 * @param valor Fecha apertura SOJ 
	 * @return void 
	 */
	public void setFechaApertura				(String valor)	{ this.fechaApertura = valor;}
	
	/**
	 * Almacena en el Bean el estado de un SOJ
	 * 
	 * @param valor Estado SOJ 
	 * @return void 
	 */
	public void setEstado						(String valor)	{ this.estado = valor;}
	
	public void setSufijo						(String valor)	{ this.sufijo = valor;}
	
	/**
	 * Almacena en el Bean la descipción de la consulta de un SOJ
	 * 
	 * @param valor Consulta del SOJ
	 * @return void 
	 */
	public void setDescripcionConsulta			(String valor)	{ this.descripcionConsulta = valor;}
	
	/**
	 * Almacena en el Bean la respuesta al a consulta de un SOJ
	 * 
	 * @param valor Respuesta consulta SOJ 
	 * @return void 
	 */
	public void setRespuestaLetrado				(String valor)	{ this.respuestaLetrado = valor;}
	
	/**
	 * Almacena en el Bean el identificador del abogado del SOJ
	 * 
	 * @param valor Identificador abogado SOJ 
	 * @return void 
	 */
	public void setIdPersona					(Long valor)	{ this.idPersona = valor;}
	
	/**
	 * Almacena en el Bean el identificador del turno del SOJ
	 * 
	 * @param valor Identificador turno SOJ 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)	{ this.idTurno = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la actuación del SOJ
	 * 
	 * @param valor Identificador actuación SOJ 
	 * @return void 
	 */
	public void setIdFActuracion				(Integer valor)	{ this.idFActuracion = valor;}
	
	/**
	 * Almacena en el Bean el identificador del colegio del SOJ
	 * 
	 * @param valor Identificador colegio SOJ 
	 * @return void 
	 */
	public void setIdTipoSOJColegio			(Integer valor)	{ this.idTipoSOJColegio = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la guardia del SOJ
	 * 
	 * @param valor Identificador guardia SOJ 
	 * @return void 
	 */
	public void setIdGuardia				(Integer valor)	{ this.idGuardia = valor;}
	
	/**
	 * Almacena en el Bean el identificador del solicitante del SOJ
	 * 
	 * @param valor Identificador solicitante SOJ 
	 * @return void 
	 */
	public void setIdPersonaJG				(Integer valor)	{ this.idPersonaJG = valor;}
	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un SOJ
	 * 
	 * @param valor Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un SOJ  
	 * 
	 * @return Identificador de un area. De tipo "Integer" 
	 */
	public Integer getIdTipoSOJ				()	{ return this.idTipoSOJ;}
	
	/**
	 * Recupera del Bean el anho de un SOJ
	 * @return Integer
	 */
	public Integer getAnio					()	{ return this.anio;}
	
	/**
	 * Recupera del Bean el número de un SOJreturn 
	 * @return Integer
	 */
	public Integer getNumero				()	{ return this.numero;}
	
	/**
	 * Recupera del Bean la fecha de apertura de un SOJ
	 * @return String 
	 */
	public String getFechaApertura			()	{ return this.fechaApertura;}
	
	/**
	 * Recupera del Bean el estado de un SOJ 
	 * @return char
	 */
	public String getEstado					()	{ return this.estado;}
	public String getSufijo					()	{ return this.sufijo;}
	
	/**
	 * Recupera del Bean la descipción de la consulta de un SOJ
	 * 
	 * @param valor Consulta del SOJ
	 * @return String
	 */
	public String getDescripcionConsulta	()	{ return this.descripcionConsulta;}
	
	/**
	 * Recupera del Bean la respuesta al a consulta de un SOJ 
	 * @return String
	 */
	public String getRespuestaLetrado		()	{ return this.respuestaLetrado;}
	
	/**
	 * Recupera del Bean el identificador del abogado del SOJ 
	 * @return Integer
	 */
	public Long getIdPersona				()	{ return this.idPersona;}
	
	/**
	 * Recupera del Bean el identificador del turno del SOJ 
	 * @return Integer 
	 */
	public Integer getIdTurno				()	{ return this.idTurno;}
	
	/**
	 * Recupera del Bean el identificador de la actuación del SOJ 
	 * @return Integer 
	 */
	public Integer getIdFActuracion			()	{return this.idFActuracion;}
	
	/**
	 * Recupera del Bean el identificador del colegio del SOJ 
	 * @return Integer 
	 */
	public Integer getIdTipoSOJColegio		()	{ return this.idTipoSOJColegio;}
	
	/**
	 * Recupera del Bean el identificador de la guardia del SOJ
	 * @return Integer
	 */
	public Integer getIdGuardia				()	{ return this.idGuardia;}
	
	/**
	 * Recupera del Bean el identificador del solicitante del SOJ 
	 * @return Integer
	 */
	public Integer getIdPersonaJG			()	{ return this.idPersonaJG;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece un SOJ 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;}
	
	/**
	 * @return Returns the facturado.
	 */
	public String getFacturado() {
		return facturado;
	}
	/**
	 * @param facturado The facturado to set.
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	/**
	 * @return Returns the pagado.
	 */
	public String getPagado() {
		return pagado;
	}
	/**
	 * @param pagado The pagado to set.
	 */
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}

	public String getNumSOJ() {
		return numSOJ;
	}
	public void setNumSOJ(String numSOJ) {
		this.numSOJ = numSOJ;
	}
	
	
	/**
	 * @return Returns the idTipoConsulta.
	 */
	public Integer getIdTipoConsulta() {
		return idTipoConsulta;
	}
	/**
	 * @param idTipoConsulta The idTipoConsulta to set.
	 */
	public void setIdTipoConsulta(Integer idTipoConsulta) {
		this.idTipoConsulta = idTipoConsulta;
	}
	
	/**
	 * @return Returns the idTipoRespuesta.
	 */
	public Integer getIdTipoRespuesta() {
		return idTipoRespuesta;
	}
	/**
	 * @param idTipoRespuesta The idTipoRespuesta to set.
	 */
	public void setIdTipoRespuesta(Integer idTipoRespuesta) {
		this.idTipoRespuesta = idTipoRespuesta;
	}

	public Integer getEjgAnio() {
		return ejgAnio;
	}

	public void setEjgAnio(Integer ejgAnio) {
		this.ejgAnio = ejgAnio;
	}
	
	public Integer getEjgIdTipoEJG() {
		return ejgIdTipoEJG;
	}
	
	public void setEjgIdTipoEJG(Integer ejgIdTipoEJG) {
		this.ejgIdTipoEJG = ejgIdTipoEJG;
	}
	
	public Integer getEjgNumero() {
		return ejgNumero;
	}
	
	public void setEjgNumero(Integer ejgNumero) {
		this.ejgNumero = ejgNumero;
	}
}