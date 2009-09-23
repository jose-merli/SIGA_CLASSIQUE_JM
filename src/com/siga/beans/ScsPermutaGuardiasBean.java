package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PERMUTAGUARDIAS
 * 
 * @author david.sanchezp
 * @since 24/1/2005
 */

public class ScsPermutaGuardiasBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer 	idInstitucion;
	private Integer 	numero;
	private Integer 	anulada;
	//Solicitante
	private Integer 	idTurno_Solicitante;
	private Integer 	idGuardia_Solicitante;
	private Integer 	idCalendarioGuardias_Solicitan;
	private Integer 	idPersona_Solicitante;
	private String 		fechaInicio_Solicitante;
	private String 		motivos_Solicitante;
	private String 		fechaSolicitud;
	//Confirmador
	private Integer 	idTurno_Confirmador;
	private Integer 	idGuardia_Confirmador;
	private Integer 	idCalendarioGuardias_Confirmad;
	private Integer 	idPersona_Confirmador;
	private String 		fechaInicio_Confirmador;
	private String 		motivos_Confirmador;
	private String 		fechaConfirmacion;
	

	
	/* Nombre de Tabla */
	
	static public String T_NOMBRETABLA = "SCS_PERMUTAGUARDIAS";
	
	
	/* Nombre de campos de la tabla */
	
	static public final String	C_IDINSTITUCION = 					"IDINSTITUCION";
	static public final String 	C_NUMERO = 							"NUMERO";
	static public final String 	C_ANULADA = 						"ANULADA";
	static public final String 	C_IDTURNO_SOLICITANTE = 			"IDTURNO_SOLICITANTE";
	static public final String 	C_IDGUARDIA_SOLICITANTE =			"IDGUARDIA_SOLICITANTE";
	static public final String 	C_IDCALENDARIOGUARDIAS_SOLICITAN =	"IDCALENDARIOGUARDIAS_SOLICITAN";
	static public final String 	C_IDPERSONA_SOLICITANTE =			"IDPERSONA_SOLICITANTE";
	static public final String 	C_FECHAINICIO_SOLICITANTE =			"FECHAINICIO_SOLICITANTE";
	static public final String 	C_MOTIVOS_SOLICITANTE =				"MOTIVOSSOLICITANTE";
	static public final String 	C_FECHASOLICITUD =					"FECHASOLICITUD";
	static public final String 	C_IDTURNO_CONFIRMADOR = 			"IDTURNO_CONFIRMADOR";
	static public final String 	C_IDGUARDIA_CONFIRMADOR =			"IDGUARDIA_CONFIRMADOR";
	static public final String 	C_IDCALENDARIOGUARDIAS_CONFIRMAD =	"IDCALENDARIOGUARDIAS_CONFIRMAD";
	static public final String 	C_IDPERSONA_CONFIRMADOR =			"IDPERSONA_CONFIRMADOR";
	static public final String 	C_FECHAINICIO_CONFIRMADOR =			"FECHAINICIO_CONFIRMADOR";
	static public final String 	C_MOTIVOS_CONFIRMADOR =				"MOTIVOSCONFIRMADOR";
	static public final String 	C_FECHACONFIRMACION =				"FECHACONFIRMACION";
	
	
	/* Metodos SET */
	
	public void setIdInstitucion (Integer valor) 					{ this.idInstitucion = valor;}
	public void setNumero (Integer valor) 							{ this.numero = valor;}
	public void setAnulada (Integer valor) 							{ this.anulada = valor;}
	public void setIdTurnoSolicitante (Integer valor) 				{ this.idTurno_Solicitante = valor;}
	public void setIdGuardiaSolicitante (Integer valor) 			{ this.idGuardia_Solicitante = valor;}
	public void setIdCalendarioGuardiasSolicitan (Integer valor) 	{ this.idCalendarioGuardias_Solicitan = valor;}
	public void setFechaInicioSolicitante (String  valor)			{ this.fechaInicio_Solicitante = valor;}
	public void setMotivosSolicitante (String  valor)				{ this.motivos_Solicitante = valor;}
	public void setIdPersonaSolicitante (Integer valor) 			{ this.idPersona_Solicitante = valor;}
	public void setFechaSolicitud (String  valor)					{ this.fechaSolicitud = valor;}
	public void setIdTurnoConfirmador (Integer valor) 				{ this.idTurno_Confirmador = valor;}
	public void setIdGuardiaConfirmador(Integer valor) 				{ this.idGuardia_Confirmador = valor;}
	public void setIdCalendarioGuardiasConfirmad (Integer valor) 	{ this.idCalendarioGuardias_Confirmad = valor;}
	public void setFechaInicioConfirmador (String  valor)			{ this.fechaInicio_Confirmador = valor;}
	public void setMotivosConfirmador (String  valor)				{ this.motivos_Confirmador = valor;}
	public void setIdPersonaConfirmador (Integer valor) 			{ this.idPersona_Confirmador = valor;}
	public void setFechaConfirmacion (String  valor)				{ this.fechaConfirmacion = valor;}

	
	/* Metodos GET */
	
	public Integer getIdInstitucion () 					{ return this.idInstitucion;}
	public Integer getNumero () 						{ return this.numero;}
	public Integer getAnulada () 						{ return this.anulada;}
	public Integer getIdTurnoSolicitante () 			{ return this.idTurno_Solicitante;}
	public Integer getIdGuardiaSolicitante () 			{ return this.idGuardia_Solicitante;}
	public Integer getIdCalendarioGuardiasSolicitan () 	{ return this.idCalendarioGuardias_Solicitan;}
	public String  getFechaInicioSolicitante ()			{ return this.fechaInicio_Solicitante;}
	public String  getMotivosSolicitante ()				{ return this.motivos_Solicitante;}
	public Integer getIdPersonaSolicitante () 			{ return this.idPersona_Solicitante;}
	public String  getFechaSolicitud ()					{ return this.fechaSolicitud;}
	public Integer getIdTurnoConfirmador () 			{ return this.idTurno_Confirmador;}
	public Integer getIdGuardiaConfirmador() 			{ return this.idGuardia_Confirmador;}
	public Integer getIdCalendarioGuardiasConfirmad () 	{ return this.idCalendarioGuardias_Confirmad;}
	public String  getFechaInicioConfirmador ()			{ return this.fechaInicio_Confirmador;}
	public String  getMotivosConfirmador ()				{ return this.motivos_Confirmador;}
	public Integer getIdPersonaConfirmador () 			{ return this.idPersona_Confirmador;}
	public String  getFechaConfirmacion ()				{ return this.fechaConfirmacion;}
	
}