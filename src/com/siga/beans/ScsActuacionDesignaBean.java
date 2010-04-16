package com.siga.beans;



/**
 * Implementa las operaciones sobre el bean de la tabla SCSACTUACIONDESIGNA
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 * @version 27/01/2006 (david.sanchezp): modificaciones para incluir nuevos combos y retoque de codigo.
 */

public class ScsActuacionDesignaBean extends MasterBean{
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_ACTUACIONDESIGNA";
	
	
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 				"IDINSTITUCION";
	static public final String	C_IDTURNO =						"IDTURNO";
	static public final String	C_ANIO =						"ANIO";
	static public final String	C_NUMERO =						"NUMERO";
	static public final String	C_FECHA=						"FECHA";
	static public final String	C_NUMEROASUNTO	=				"NUMEROASUNTO";
	static public final String	C_ACUERDOEXTRAJUDICIAL	=		"ACUERDOEXTRAJUDICIAL";
	static public final String	C_ANULACION	=					"ANULACION";
	static public final String	C_IDPROCEDIMIENTO	=			"IDPROCEDIMIENTO";
	static public final String	C_LUGAR	=						"LUGAR";
	static public final String	C_OBSERVACIONESJUSTIFICACION = 	"OBSERVACIONESJUSTIFICACION";
	static public final String	C_OBSERVACIONES	=				"OBSERVACIONES";
	static public final String	C_FECHAJUSTIFICACION	=		"FECHAJUSTIFICACION";
	static public final String  C_FACTURADO 				  = "FACTURADO";
	static public final String  C_PAGADO 					  = "PAGADO";
	static public final String  C_IDFACTURACION 			  = "IDFACTURACION";
	static public final String  C_IDPRISION 			      = "IDPRISION";
	static public final String  C_IDINSTITUCIONPRISION 		  = "IDINSTITUCION_PRIS";
	static public final String  C_IDCOMISARIA 				  = "IDCOMISARIA";
	static public final String  C_IDINSTITUCIONCOMISARIA 	  = "IDINSTITUCION_COMIS";
	static public final String  C_IDJUZGADO 				  = "IDJUZGADO";
	static public final String  C_IDINSTITUCIONJUZGADO 		  = "IDINSTITUCION_JUZG";
	static public final String  C_IDACREDITACION 			  = "IDACREDITACION";
	static public final String  C_IDINSTITUCIONPROCEDIMIENTO  = "IDINSTITUCION_PROC";
	static public final String  C_VALIDADA 					  = "VALIDADA";
	static public final String  C_IDPERSONACOLEGIADO 		  = "IDPERSONACOLEGIADO";
	static public final String  C_IDPRETENSION      		  = "IDPRETENSION";
	static public final String  C_TALONARIO  	     		  = "TALONARIO";
	static public final String  C_TALON      	     		  = "TALON";
	
	
	/*
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer	anio;
	private Long    numero;
	private String	fecha;
	private Long	numeroAsunto;
	private Integer acuerdoExtraJudicial;
	private Integer	anulacion;
	private String  idProcedimiento;
	private String	lugar;
	private String	observacionesJustificacion;
	private String	observaciones;
	private String  fechaJustificacion;
	private String  facturado;
	private String  pagado;
	private Integer idFacturacion;
	private Integer idInstitucionPrision;
	private Long    idPrision;
	private Integer idInstitucionComisaria;
	private Long    idComisaria;
	private Integer idInstitucionJuzgado;
	private Long    idJuzgado;
	private Integer idAcreditacion;
	private Integer idInstitucionProcedimiento;
	private String  validada;
	private Long    idPersonaColegiado;
	private Integer idPretension;
	private String   talonario;
    private String   talon;
	
    /**
	 * @return Returns the getTalonario.
	 */
    public String getTalonario() {
		return talonario;
	}
    
    /**
	 * @param acuerdoExtraJudicial The Talonario to set.
	 */
	public void setTalonario(String talonario) {
		this.talonario = talonario;
	}
	
	 /**
	 * @return Returns the getTalon.
	 */
	public String getTalon() {
		return talon;
	}
	
	 /**
	 * @param acuerdoExtraJudicial The Talon to set.
	 */
	
	public void setTalon(String talon) {
		this.talon = talon;
	}
	

	
	/**
	 * @return Returns the acuerdoExtraJudicial.
	 */
	public Integer getAcuerdoExtraJudicial() {
		return acuerdoExtraJudicial;
	}
	/**
	 * @param acuerdoExtraJudicial The acuerdoExtraJudicial to set.
	 */
	public void setAcuerdoExtraJudicial(Integer acuerdoExtraJudicial) {
		this.acuerdoExtraJudicial = acuerdoExtraJudicial;
	}
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the anulacion.
	 */
	public Integer getAnulacion() {
		return anulacion;
	}
	/**
	 * @param anulacion The anulacion to set.
	 */
	public void setAnulacion(Integer anulacion) {
		this.anulacion = anulacion;
	}
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
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the fechaJustificacion.
	 */
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	/**
	 * @param fechaJustificacion The fechaJustificacion to set.
	 */
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	/**
	 * @return Returns the idAcreditacion.
	 */
	public Integer getIdAcreditacion() {
		return idAcreditacion;
	}
	/**
	 * @param idAcreditacion The idAcreditacion to set.
	 */
	public void setIdAcreditacion(Integer idAcreditacion) {
		this.idAcreditacion = idAcreditacion;
	}
	/**
	 * @return Returns the idComisaria.
	 */
	public Long getIdComisaria() {
		return idComisaria;
	}
	/**
	 * @param idComisaria The idComisaria to set.
	 */
	public void setIdComisaria(Long idComisaria) {
		this.idComisaria = idComisaria;
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
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idInstitucionComisaria.
	 */
	public Integer getIdInstitucionComisaria() {
		return idInstitucionComisaria;
	}
	/**
	 * @param idInstitucionComisaria The idInstitucionComisaria to set.
	 */
	public void setIdInstitucionComisaria(Integer idInstitucionComisaria) {
		this.idInstitucionComisaria = idInstitucionComisaria;
	}
	/**
	 * @return Returns the idInstitucionJuzgado.
	 */
	public Integer getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	/**
	 * @param idInstitucionJuzgado The idInstitucionJuzgado to set.
	 */
	public void setIdInstitucionJuzgado(Integer idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	/**
	 * @return Returns the idInstitucionPrision.
	 */
	public Integer getIdInstitucionPrision() {
		return idInstitucionPrision;
	}
	/**
	 * @param idInstitucionPrision The idInstitucionPrision to set.
	 */
	public void setIdInstitucionPrision(Integer idInstitucionPrision) {
		this.idInstitucionPrision = idInstitucionPrision;
	}
	/**
	 * @return Returns the idInstitucionProcurador.
	 */
	public Integer getIdInstitucionProcedimiento() {
		return idInstitucionProcedimiento;
	}
	/**
	 * @param idInstitucionProcurador The idInstitucionProcurador to set.
	 */
	public void setIdInstitucionProcedimiento(Integer idInstitucionProcedimiento) {
		this.idInstitucionProcedimiento = idInstitucionProcedimiento;
	}
	/**
	 * @return Returns the idJuzgado.
	 */
	public Long getIdJuzgado() {
		return idJuzgado;
	}
	/**
	 * @param idJuzgado The idJuzgado to set.
	 */
	public void setIdJuzgado(Long idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	/**
	 * @return Returns the idPersonaColegiado.
	 */
	public Long getIdPersonaColegiado() {
		return idPersonaColegiado;
	}
	/**
	 * @param idPersonaColegiado The idPersonaColegiado to set.
	 */
	public void setIdPersonaColegiado(Long idPersonaColegiado) {
		this.idPersonaColegiado = idPersonaColegiado;
	}
	/**
	 * @return Returns the idPrision.
	 */
	public Long getIdPrision() {
		return idPrision;
	}
	/**
	 * @param idPrision The idPrision to set.
	 */
	public void setIdPrision(Long idPrision) {
		this.idPrision = idPrision;
	}
	/**
	 * @return Returns the idProcedimiento.
	 */
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	/**
	 * @param idProcedimiento The idProcedimiento to set.
	 */
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	/**
	 * @return Returns the lugar.
	 */
	public String getLugar() {
		return lugar;
	}
	/**
	 * @param lugar The lugar to set.
	 */
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	/**
	 * @return Returns the numero.
	 */
	public Long getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	/**
	 * @return Returns the numeroAsunto.
	 */
	public Long getNumeroAsunto() {
		return numeroAsunto;
	}
	/**
	 * @param numeroAsunto The numeroAsunto to set.
	 */
	public void setNumeroAsunto(Long numeroAsunto) {
		this.numeroAsunto = numeroAsunto;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return Returns the observacionesJustificacion.
	 */
	public String getObservacionesJustificacion() {
		return observacionesJustificacion;
	}
	/**
	 * @param observacionesJustificacion The observacionesJustificacion to set.
	 */
	public void setObservacionesJustificacion(String observacionesJustificacion) {
		this.observacionesJustificacion = observacionesJustificacion;
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
	
	/**
	 * @return Returns the validada.
	 */
	public String getValidada() {
		return validada;
	}
	/**
	 * @param validada The validada to set.
	 */
	public void setValidada(String validada) {
		this.validada = validada;
	}
	/**
	 * @return Returns the idPretension.
	 */
	public Integer getIdPretension() {
		return idPretension;
	}
	/**
	 * @param idPretension The idPretension to set.
	 */
	public void setIdPretension(Integer idPretension) {
		this.idPretension = idPretension;
	}
}