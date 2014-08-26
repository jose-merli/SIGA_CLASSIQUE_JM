package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_GUARDIASCOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 06/12/2004
 * @version david.sanchezp 21/01/2005: al variar los campos en base de datos.
 * @version david.sanchezp 24/01/2005: para modificar todo los relacionado con el campo IDFACTURACION para <br>
 * sustituirlo por el campo RESERVA.
 * @version david.sanchezp 23/03/2006: para corregir algunos tipos de los datos.
 * @version adrian.ayala 14/05/2008: Limpieza, nuevos campos y eliminacion de campos antiguos
 */

public class ScsGuardiasColegiadoBean extends MasterBean
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_GUARDIASCOLEGIADO";
	
	//Nombres de campos de la tabla
	static public final String C_IDINSTITUCION					= "IDINSTITUCION";
	static public final String C_IDTURNO						= "IDTURNO";
	static public final String C_IDGUARDIA						= "IDGUARDIA";
	static public final String C_IDPERSONA						= "IDPERSONA";
	static public final String C_FECHAINICIO					= "FECHAINICIO";
	static public final String C_FECHAFIN						= "FECHAFIN";
	static public final String C_DIASGUARDIA					= "DIASGUARDIA";
	static public final String C_DIASACOBRAR					= "DIASACOBRAR";
	static public final String C_OBSERVACIONES					= "OBSERVACIONES";
	static public final String C_RESERVA						= "RESERVA";
	static public final String C_FACTURADO						= "FACTURADO";
	static public final String C_PAGADO							= "PAGADO";
	static public final String C_IDFACTURACION					= "IDFACTURACION";
	static public final String C_GUARDIA_SELECCIONLABORABLES	= "GUARDIA_SELECCIONLABORABLES";
	static public final String C_GUARDIA_SELECCIONFESTIVOS		= "GUARDIA_SELECCIONFESTIVOS";
	
	
	
	//////////////////// ATRIBUTOS ////////////////////
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer	idGuardia;
	private Long	idPersona;
	private String	fechaInicio;
	private String	fechaFin;
	private Long	diasGuardia;
	private Long	diasACobrar;
	private String	observaciones;
	private String	reserva;	
	private String	facturado;
	private String	pagado;
	private Integer	idFacturacion;
	private String	guardia_seleccionLaborables;
	private String	guardia_seleccionFestivos;
	
	
	
	//////////////////// GETTERS ////////////////////
	public Long getDiasACobrar() {return diasACobrar;}
	public Long getDiasGuardia() {return diasGuardia;}
	public String getFacturado() {return facturado;}
	public String getFechaFin() {return fechaFin;}
	public String getFechaInicio() {return fechaInicio;}
	public Integer getIdFacturacion() {return idFacturacion;}
	public Integer getIdGuardia() {return idGuardia;}
	public Integer getIdInstitucion() {return idInstitucion;}
	public Long getIdPersona() {return idPersona;}
	public Integer getIdTurno() {return idTurno;}
	public String getObservaciones() {return observaciones;}
	public String getPagado() {return pagado;}
	public String getReserva() {return reserva;}
	public String getGuardia_seleccionLaborables() {return guardia_seleccionLaborables;}
	public String getGuardia_seleccionFestivos() {return guardia_seleccionFestivos;}
	
	
	
	//////////////////// SETTERS ////////////////////
	public void setDiasACobrar(Long valor) {this.diasACobrar = valor;}
	public void setDiasGuardia(Long valor) {this.diasGuardia = valor;}
	public void setFacturado(String valor) {this.facturado = valor;}
	public void setFechaFin(String valor) {this.fechaFin = valor;}
	public void setFechaInicio(String valor) {this.fechaInicio = valor;}
	public void setIdFacturacion(Integer valor) {this.idFacturacion = valor;}
	public void setIdGuardia(Integer valor) {this.idGuardia = valor;}
	public void setIdInstitucion(Integer valor) {this.idInstitucion = valor;}
	public void setIdPersona(Long valor) {this.idPersona = valor;}
	public void setIdTurno(Integer valor) {this.idTurno = valor;}
	public void setObservaciones(String valor) {this.observaciones = valor;}
	public void setPagado(String valor) {this.pagado = valor;}
	public void setReserva(String valor) {this.reserva = valor;}
	public void setGuardia_seleccionLaborables(String valor) {this.guardia_seleccionLaborables = valor;}
	public void setGuardia_seleccionFestivos(String valor) {this.guardia_seleccionFestivos = valor;}

}