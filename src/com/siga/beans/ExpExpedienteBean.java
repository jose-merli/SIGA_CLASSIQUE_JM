/*
 * Created on Dec 27, 2004
 * @author emilio.grau
 *
 *modified by miguel.villegas 18-02-2005 idPersona Integer->Long
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de expedientes
 */
public class ExpExpedienteBean extends MasterBean {

	//Variables
	
	private Integer idInstitucion;
	private Integer idInstitucion_tipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Long idPersona;
	private Integer numExpDisciplinario;
	private Integer anioExpDisciplinario;
	private String fecha;
	private String asunto;
	private String juzgado;
	private Integer procedimiento;
	private String idInstitucionJuzgado;
	private String idInstitucionProcedimiento;
	private String numAsunto;
	private String fechaInicialEstado;
	private String fechaFinalEstado;
	private String fechaProrrogaEstado;
	private String descripcionResolucion;
	private String actuacionesPrescritas;
	private String anotacionesCanceladas;
	private String sancionPrescrita;
	private String sancionFinalizada;
	private String sancionado;
	private String alertaGenerada;
	private String alertaGeneradaCad;
	private String alertaFaseGenerada;
	private String alertaCaducidadGenerada;
	private String alertaFinalGenerada;
	private Integer idClasificacion;
	private String esVisible;
	private String esVisibleEnFicha;
	private Integer idFase;
	private Integer idEstado;
	private String fechaModificacion;
	private String fechaInicialFase;
	private Integer usuModificacion;
	private Integer idArea;
	private Integer idMateria;
	private Integer idPretension;
	private String  otrasPretensiones;
	
	private String  fechaCaducidad;
	private String  fechaResolucion;
	private String  Observaciones;
	private Double  minuta;
	private Double  importeIVA;
	private Double  importeTotal;
	private Double  porcentajeIVA;
	private Double  porcentajeIVAFinal;
	private Double  minutaFinal;
	private Double  importeIVAFinal;
	private Double  importeTotalFinal;
	private Double  derechosColegiales;
	private Integer idTipoIVA;
	private Integer idResultadoJuntaGobierno;
	
	private String identificadorDS;
	private String idDireccion;
	

	// Nombre campos de la tabla 
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_NUMEXPDISCIPLINARIO ="NUMEXPDISCIPLINARIO";
	static public final String C_FECHA ="FECHA";
	static public final String C_ASUNTO ="ASUNTO";
	static public final String C_JUZGADO ="JUZGADO";
	static public final String C_PROCEDIMIENTO ="PROCEDIMIENTO";
	static public final String C_IDINSTITUCION_JUZGADO="IDINSTITUCION_JUZ";
	static public final String C_IDINSTITUCION_PROCEDIMIENTO="IDINSTITUCION_PROC";
	static public final String C_NUMASUNTO ="NUMASUNTO";
	static public final String C_FECHAINICIALESTADO ="FECHAINICIALESTADO";
	static public final String C_FECHAINICIALFASE ="FECHAINICIALFASE";
	static public final String C_FECHAFINALESTADO ="FECHAFINALESTADO";
	static public final String C_FECHAPRORROGAESTADO ="FECHAPRORROGAESTADO";
	static public final String C_DESCRIPCIONRESOLUCION ="DESCRIPCIONRESOLUCION";
	static public final String C_SANCIONPRESCRITA ="SANCIONPRESCRITA";
	static public final String C_ACTUACIONESPRESCRITAS ="ACTUACIONESPRESCRITAS";
	static public final String C_SANCIONFINALIZADA ="SANCIONFINALIZADA";
	static public final String C_ALERTAGENERADA ="ALERTAGENERADA";
	static public final String C_ALERTAGENERADACAD ="ALERTAGENERADACAD";
	static public final String C_ALERTACADUCIDADGENERADA ="ALERTACADUCIDADGENERADA";
	static public final String C_ALERTAFINALGENERADA ="ALERTAFINALGENERADA";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_ANIOEXPDISCIPLINARIO ="ANIOEXPDISCIPLINARIO";
	static public final String C_IDPERSONA ="IDPERSONA";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_ESVISIBLE ="ESVISIBLE";
	static public final String C_ESVISIBLEENFICHA ="ESVISIBLEENFICHA";
	static public final String C_IDCLASIFICACION ="IDCLASIFICACION";
	static public final String C_IDFASE ="IDFASE";
	static public final String C_IDESTADO ="IDESTADO";
	static public final String C_SANCIONADO ="SANCIONADO";
	static public final String C_ANOTACIONESCANCELADAS ="ANOTACIONESCANCELADAS";
	
	static public final String C_IDAREA ="IDAREA";
	static public final String C_IDMATERIA ="IDMATERIA";
	static public final String C_IDPRETENSION ="IDPRETENSION";
	static public final String C_OTRASPRETENSIONES ="OTRASPRETENSIONES";
	
	static public final String C_FECHACADUCIDAD ="FECHACADUCIDAD";
	static public final String C_FECHARESOLUCION ="FECHARESOLUCION";
	static public final String C_OBSERVACIONES ="OBSERVACIONES";
	static public final String C_MINUTA ="MINUTA";
	static public final String C_IMPORTETOTAL ="IMPORTETOTAL";
	static public final String C_MINUTAFINAL ="MINUTAFINAL";
	static public final String C_IMPORTETOTALFINAL ="IMPORTETOTALFINAL";
	static public final String C_DERECHOSCOLEGIALES ="DERECHOSCOLEGIALES";	
	static public final String C_PORCENTAJEIVA ="PORCENTAJEIVA";
	static public final String C_IDTIPOIVA ="IDTIPOIVA";
	static public final String C_IDRESULTADOJUNTAGOBIERNO ="IDRESULTADOJUNTAGOBIERNO";
	static public final String C_ALERTAFASEGENERADA ="ALERTAFASEGENERADA";
	static public final String C_IDENTIFICADORDS ="IDENTIFICADORDS";
	
	static public final String T_NOMBRETABLA = "EXP_EXPEDIENTE";
	
	static public final String C_IDDIRECCION ="IDDIRECCION";
	
	
	public String getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getFechaResolucion() {
		return fechaResolucion;
	}
	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	public String getObservaciones() {
		return Observaciones;
	}
	public void setObservaciones(String Observaciones) {
		this.Observaciones = Observaciones;
	}
	public Double getMinuta() {
		return minuta;
	}
	public void setMinuta(Double minuta) {
		this.minuta = minuta;
	}
	public Double getImporteIVA() {
		return importeIVA;
	}
	public void setImporteIVA(Double valor) {
		this.importeIVA = valor;
	}
	public Double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(Double valor) {
		this.importeTotal = valor;
	}
	public Double getPorcentajeIVA() {
		return porcentajeIVA;
	}
	public void setPorcentajeIVA(Double valor) {
		this.porcentajeIVA = valor;
	}
	
	public Double getPorcentajeIVAFinal() {
		return porcentajeIVAFinal;
	}
	public void setPorcentajeIVAFinal(Double porcentajeIVAFinal) {
		this.porcentajeIVAFinal = porcentajeIVAFinal;
	}
	public Integer getIdTipoIVA() {
		return idTipoIVA;
	}
	public void setIdTipoIVA(Integer idTipoIVA) {
		this.idTipoIVA = idTipoIVA;
	}
	public Integer getIdResultadoJuntaGobierno() {
		return idResultadoJuntaGobierno;
	}
	public void setIdResultadoJuntaGobierno(Integer idResultadoJuntaGobierno) {
		this.idResultadoJuntaGobierno = idResultadoJuntaGobierno;
	}
	
	
	
	public String getAnotacionesCanceladas() {
		return anotacionesCanceladas;
	}
	public void setAnotacionesCanceladas(String anotacionesCanceladas) {
		this.anotacionesCanceladas = anotacionesCanceladas;
	}
	public String getSancionado() {
		return sancionado;
	}
	public void setSancionado(String sancionado) {
		this.sancionado = sancionado;
	}
	public String getSancionFinalizada() {
		return sancionFinalizada;
	}
	public void setSancionFinalizada(String sancionFinalizada) {
		this.sancionFinalizada = sancionFinalizada;
	}
	public String getAlertaGenerada() {
		return alertaGenerada;
	}
	public void setAlertaGenerada(String alertaFaseGenerada) {
		this.alertaGenerada = alertaFaseGenerada;
	}
	public String getAlertaGeneradaCad() {
		return alertaGeneradaCad;
	}
	public void setAlertaGeneradaCad(String valor) {
		this.alertaGeneradaCad = valor;
	}
	public String getAlertaCaducidadGenerada() {
		return alertaCaducidadGenerada;
	}
	public void setAlertaCaducidadGenerada(String valor) {
		this.alertaCaducidadGenerada = valor;
	}
	public String getAlertaFinalGenerada() {
		return alertaFinalGenerada;
	}
	public void setAlertaFinalGenerada(String valor) {
		this.alertaFinalGenerada = valor;
	}
	public String getAlertaFaseGenerada() {
		return alertaFaseGenerada;
	}
	public void setAlertaFaseGenerada(String alertaFaseGenerada) {
		this.alertaFaseGenerada = alertaFaseGenerada;
	}
	public Integer getAnioExpDisciplinario() {
		return anioExpDisciplinario;
	}
	public void setAnioExpDisciplinario(Integer anioExpDisciplinario) {
		this.anioExpDisciplinario = anioExpDisciplinario;
	}
	public Integer getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(Integer anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getDescripcionResolucion() {
		return descripcionResolucion;
	}
	public void setDescripcionResolucion(String descripcionResolucion) {
		this.descripcionResolucion = descripcionResolucion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFechaFinalEstado() {
		return fechaFinalEstado;
	}
	public void setFechaFinalEstado(String fechaFinalEstado) {
		this.fechaFinalEstado = fechaFinalEstado;
	}
	public String getFechaInicialEstado() {
		return fechaInicialEstado;
	}
	public void setFechaInicialEstado(String fechaInicialEstado) {
		this.fechaInicialEstado = fechaInicialEstado;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getFechaProrrogaEstado() {
		return fechaProrrogaEstado;
	}
	public void setFechaProrrogaEstado(String fechaProrrogaEstado) {
		this.fechaProrrogaEstado = fechaProrrogaEstado;
	}
	public String getFechaInicialFase() {
		return fechaInicialFase;
	}
	public void setFechaInicialFase(String valor) {
		this.fechaInicialFase = valor;
	}
	public Integer getIdClasificacion() {
		return idClasificacion;
	}
	public void setIdClasificacion(Integer idClasificacion) {
		this.idClasificacion = idClasificacion;
	}
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public Integer getIdFase() {
		return idFase;
	}
	public void setIdFase(Integer idFase) {
		this.idFase = idFase;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdInstitucion_tipoExpediente() {
		return idInstitucion_tipoExpediente;
	}
	public void setIdInstitucion_tipoExpediente(
			Integer idInstitucion_tipoExpediente) {
		this.idInstitucion_tipoExpediente = idInstitucion_tipoExpediente;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public String getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}
	public String getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	public void setIdInstitucionJuzgado(String idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	public String getNumAsunto() {
		return numAsunto;
	}
	public void setNumAsunto(String numAsunto) {
		this.numAsunto = numAsunto;
	}
	public Integer getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(Integer numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public Integer getNumExpDisciplinario() {
		return numExpDisciplinario;
	}
	public void setNumExpDisciplinario(Integer numExpDisciplinario) {
		this.numExpDisciplinario = numExpDisciplinario;
	}
	public Integer getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Integer procedimiento) {
		this.procedimiento = procedimiento;
	}
	public String getIdInstitucionProcedimiento() {
		return idInstitucionProcedimiento;
	}
	public void setIdInstitucionProcedimiento(String idInstitucionProcedimiento) {
		this.idInstitucionProcedimiento = idInstitucionProcedimiento;
	}
	public String getActuacionesPrescritas() {
		return actuacionesPrescritas;
	}
	public void setActuacionesPrescritas(String actuacionesPrescritas) {
		this.actuacionesPrescritas = actuacionesPrescritas;
	}
	public String getEsVisible() {
		return esVisible;
	}
	public void setEsVisible(String esVisible) {
		this.esVisible = esVisible;
	}
	public String getEsVisibleEnFicha() {
		return esVisibleEnFicha;
	}
	public void setEsVisibleEnFicha(String esVisibleEnFicha) {
		this.esVisibleEnFicha = esVisibleEnFicha;
	}
	public String getSancionPrescrita() {
		return sancionPrescrita;
	}
	public void setSancionPrescrita(String sancionPrescrita) {
		this.sancionPrescrita = sancionPrescrita;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	
	public void setIdArea(Integer valor) {
		this.idArea = valor;
	}
	public Integer getIdArea() {
		return this.idArea;
	}

	public void setIdMateria(Integer valor) {
		this.idMateria = valor;
	}
	public Integer getIdMateria() {
		return this.idMateria;
	}

	public void setIdPretension(Integer valor) {
		this.idPretension = valor;
	}
	public Integer getIdPretension() {
		return this.idPretension;
	}

	public void setOtrasPretensiones(String valor) {
		this.otrasPretensiones = valor;
	}
	public String getOtrasPretensiones() {
		return this.otrasPretensiones;
	}
	/**
	 * @return the identificadorDS
	 */
	public String getIdentificadorDS() {
		return identificadorDS;
	}
	/**
	 * @param identificadorDS the identificadorDS to set
	 */
	public void setIdentificadorDS(String identificadorDS) {
		this.identificadorDS = identificadorDS;
	}
	public String getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}
	public Double getMinutaFinal() {
		return minutaFinal;
	}
	public void setMinutaFinal(Double minutaFinal) {
		this.minutaFinal = minutaFinal;
	}
	public Double getImporteIVAFinal() {
		return importeIVAFinal;
	}
	public void setImporteIVAFinal(Double importeIVAFinal) {
		this.importeIVAFinal = importeIVAFinal;
	}
	public Double getImporteTotalFinal() {
		return importeTotalFinal;
	}
	public void setImporteTotalFinal(Double importeTotalFinal) {
		this.importeTotalFinal = importeTotalFinal;
	}
	public Double getDerechosColegiales() {
		return derechosColegiales;
	}
	public void setDerechosColegiales(Double derechosColegiales) {
		this.derechosColegiales = derechosColegiales;
	}
}
