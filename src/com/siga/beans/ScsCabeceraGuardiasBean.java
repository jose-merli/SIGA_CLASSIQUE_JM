package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_CABECERAGUARDIAS
 * 
 * @author cristina.santos
 * @since 14/03/2006
 * @version 22/03/2006: david.sanchezp: modificacion para poner el idpersona como Long y el texto de idcalendario por idcalendario guardias.
 */

public class ScsCabeceraGuardiasBean extends MasterBean{
	
	/* Variables */ 
	
	public Integer	idInstitucion;
	public Integer	idTurno ;
	public Integer	idGuardia;
	public Integer	idCalendario ;
	public Long		idPersona;
	public String	fechaInicio;
	public String	fechaFin ;
	public String	sustituto;
	public String	facturado;
	public String	pagado;
	public String	validado;
	public Long letradoSustituido;
	public String	fechaSustitucion;
	public String  comenSustitucion;
	public String  fechaAlta;
	private Integer posicion; 
	
	/* Nombre de Tabla */
	
	static public String T_NOMBRETABLA = "SCS_CABECERAGUARDIAS";
	
	
	/* Nombre de campos de la tabla */
	
	static public final String	C_IDINSTITUCION = 		"IDINSTITUCION";
	static public final String	C_IDTURNO = 			"IDTURNO";
	static public final String	C_IDGUARDIA = 			"IDGUARDIA";
	static public final String	C_IDCALENDARIOGUARDIAS ="IDCALENDARIOGUARDIAS";
	static public final String	C_IDPERSONA = 			"IDPERSONA";
	static public final String	C_FECHA_INICIO = 		"FECHAINICIO";
	static public final String	C_FECHA_FIN = 			"FECHA_FIN";
	static public final String	C_SUSTITUTO = 			"SUSTITUTO";
	static public final String	C_FACTURADO = 			"FACTURADO";
	static public final String	C_PAGADO = 				"PAGADO";
	static public final String	C_VALIDADO = 				"VALIDADO";
	static public final String 	C_LETRADOSUSTITUIDO =		"LETRADOSUSTITUIDO";
	static public final String  C_FECHASUSTITUCION = 		"FECHASUSTITUCION";
	static public final String  C_COMENSUSTITUCION = 		"COMENSUSTITUCION";
	static public final String  C_FECHAALTA = 		"FECHAALTA";
	static public final String  C_POSICION = 		"POSICION";
	
	public String getComenSustitucion() {
		return comenSustitucion;
	}
	public void setComenSustitucion(String comenSustitucion) {
		this.comenSustitucion = comenSustitucion;
	}
	public String getFechaSustitucion() {
		return fechaSustitucion;
	}
	public void setFechaSustitucion(String fechaSustitucion) {
		this.fechaSustitucion = fechaSustitucion;
	}
	public Long getLetradoSustituido() {
		return letradoSustituido;
	}
	public void setLetradoSustituido(Long letradoSustituido) {
		this.letradoSustituido = letradoSustituido;
	}
	/* Metodos GET y SET */
	
	public void setIdInstitucion (Integer valor) 		{ this.idInstitucion = valor;}
	
	
	public Integer getIdInstitucion () 				{ return this.idInstitucion;}
	
	
	public String getFacturado() {
		return facturado;
	}
	
	
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	
	
	public String getFechaFin() {
		return fechaFin;
	}
	
	
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	public String getFechaInicio() {
		return fechaInicio;
	}
	
	
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	
	public Integer getIdCalendario() {
		return idCalendario;
	}
	
	
	public void setIdCalendario(Integer idCalendario) {
		this.idCalendario = idCalendario;
	}
	
	
	public Integer getIdGuardia() {
		return idGuardia;
	}
	
	
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	
	
	public Long getIdPersona() {
		return idPersona;
	}
	
	
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	
	
	public Integer getIdTurno() {
		return idTurno;
	}
	
	
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	
	
	public String getPagado() {
		return pagado;
	}
	
	
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}
	
	
	public String getValidado() {
		return validado;
	}
	
	
	public void setValidado(String validado) {
		this.validado = validado;
	}
	
	
	public String getSustituto() {
		return sustituto;
	}
	
	
	public void setSustituto(String sustituto) {
		this.sustituto = sustituto;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Integer getPosicion() {
		return posicion;
	}
	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	
	
}