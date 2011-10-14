package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_EJG
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 */

public class ScsEJGBean extends MasterBean{
	
	/*
	 *  Variables */ 
	ScsPersonaJGBean solicitante;
	ScsTipoEJGBean tipoEjg;
	
	private Short idEstadoEjg;
	
	private Integer	anio;
	private Integer numero;
	private String	fechaApertura;
	private String  origenApertura;
	private String	fechaLimitePresentacion;
	private String	fechaPresentacion;
	private Integer	procuradorNecesario;
	private String  calidad;
	private String  tipoLetrado;
	private String	observaciones;
	private String	delitos;
	private String	dictamen;
	private String	fechaDictamen;
	private String	procurador;
	private String	ratificacionDictamen;
	private String	fechaRatificacion;
	private Integer idTipoDictamenEJG;
	private Integer idFundamentoCalif;
	private Integer idPersona;
	private Integer idInstitucion;
	private Integer idTipoEJG;
	private Integer guardiaTurno_idTurno;
	private Integer guardiaTurno_idGuardia;
	private Integer idTipoEJGColegio;
	private Integer idPersonaJG;
//	private Integer asistencia_anio;
//	private Integer asistencia_numero;
//	private Integer SOJ_idTipoSOJ;
//	private Integer SOJ_numero;
//	private Integer SOJ_anio;
//	private Integer designaIdTurno;
//	private Integer designaNumero;
//	private Integer designaAnio;
	private String numeroCAJG;
	private String anioCAJG;
	private String	numEJG;
	private String	idPretension;
	private String	idPretensionInstitucion;
	private String	idDictamen;
	
	private Double facturado;
	private Double pagado;
	private Integer idFacturacion;
	
	private Integer idProcurador;
	private Integer idInstitucionProcurador;
	
	private Integer idTipoRatificacionEJG;

 	private String fechaAuto;
	private String fechaNotificacion;
	private String fechaResolucionCAJG;
	private String turnadoAuto;
	private String turnadoRatificacion;

	private Integer idFundamentoJuridico;
	private Integer idTipoResolAuto;
	private Integer idTipoSentidoAuto;
 
	private String numeroDiligencia;
	private String numeroProcedimiento;
	private Long comisaria;
	private Long juzgado;
	private Integer comisariaInstitucion;
	private Integer juzgadoInstitucion;
	private String refAuto;
	private String fechaDesigProc;
	private String sufijo;
	private String identificadorDS;
	private String idOrigenCAJG;
	private String idPreceptivo;
	private String idSituacion;
	private String idRenuncia;
	private String numeroDesignaProc;
	private Integer idTipoenCalidad;
 
	private Integer calidadidinstitucion;    
	private String docResolucion;
    private String NIG;
    private Long idPonente;
    
    //Nuevos campos David
    private String observacionImpugnacion;
    private String fechaPublicacion;
    private String numeroResolucion;
    private String anioResolucion;
    private String bisResolucion;
   
    private String deTipoEjg;
    private String tipoEjgCol;
	private String estadoEjg;    
	private String descripcionJuzgado;   
	private String descripcionComisaria;
    private String descripcionOrigen;
	private String descripcionPretension;
    
    public String getDescripcionOrigen() {
		return descripcionOrigen;
	}
	public void setDescripcionOrigen(String descripcionOrigen) {
		this.descripcionOrigen = descripcionOrigen;
	}
	public String getDescripcionPretension() {
		return descripcionPretension;
	}
	public void setDescripcionPretension(String descripcionPretension) {
		this.descripcionPretension = descripcionPretension;
	}


    

    
    
	
	public String getDescripcionJuzgado() {
		return descripcionJuzgado;
	}
	public void setDescripcionJuzgado(String descripcionJuzgado) {
		this.descripcionJuzgado = descripcionJuzgado;
	}
	public String getDescripcionComisaria() {
		return descripcionComisaria;
	}
	public void setDescripcionComisaria(String descripcionComisaria) {
		this.descripcionComisaria = descripcionComisaria;
	}
	public String getEstadoEjg() {
		return estadoEjg;
	}
	public void setEstadoEjg(String estadoEjg) {
		this.estadoEjg = estadoEjg;
	}
	/*
	 *  Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_EJG";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_ANIO =						"ANIO";
	static public final String	C_NUMERO	=					"NUMERO";
	static public final String	C_FECHAAPERTURA	=				"FECHAAPERTURA";
	static public final String	C_ORIGENAPERTURA		 =		"ORIGENAPERTURA";
	static public final String	C_FECHALIMITEPRESENTACION	=	"FECHALIMITEPRESENTACION";
	static public final String	C_FECHAPRESENTACION	=			"FECHAPRESENTACION";
	static public final String	C_PROCURADORNECESARIO	=		"PROCURADORNECESARIO";
	static public final String	C_CALIDAD	=					"CALIDAD";
	static public final String	C_TIPOLETRADO	=				"TIPOLETRADO";
	static public final String	C_OBSERVACIONES	=				"OBSERVACIONES";
	static public final String	C_DELITOS	=					"DELITOS";
	static public final String	C_DICTAMEN	=					"DICTAMEN";
	static public final String	C_FECHADICTAMEN	=				"FECHADICTAMEN";
	static public final String	C_PROCURADOR	=				"PROCURADOR";
	static public final String	C_RATIFICACIONDICTAMEN	=		"RATIFICACIONDICTAMEN";
	static public final String	C_FECHARATIFICACION	=			"FECHARATIFICACION";
	static public final String	C_IDTIPODICTAMENEJG	=			"IDTIPODICTAMENEJG";	
	static public final String	C_IDPERSONA	=					"IDPERSONA";
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_IDTIPOEJG	=					"IDTIPOEJG";
	static public final String	C_IDFUNDAMENTOCALIF	=			"IDFUNDAMENTOCALIF";
	static public final String	C_GUARDIATURNO_IDTURNO	=		"GUARDIATURNO_IDTURNO";
	static public final String	C_GUARDIATURNO_IDGUARDIA	=	"GUARDIATURNO_IDGUARDIA";
	static public final String	C_IDTIPOEJGCOLEGIO	=			"IDTIPOEJGCOLEGIO";
	static public final String	C_IDPERSONAJG	=				"IDPERSONAJG";
//	static public final String	C_ASISTENCIA_ANIO	=			"ASISTENCIA_ANIO";
//	static public final String	C_ASISTENCIA_NUMERO	=			"ASISTENCIA_NUMERO";
//	static public final String	C_SOJ_IDTIPOSOJ	=				"SOJ_IDTIPOSOJ";
//	static public final String	C_SOJ_NUMERO	=				"SOJ_NUMERO";
//	static public final String	C_SOJ_ANIO	=					"SOJ_ANIO";
//	static public final String	C_DESIGNA_IDTURNO	=			"DESIGNA_IDTURNO";
//	static public final String	C_DESIGNA_NUMERO	=			"DESIGNA_NUMERO";
//	static public final String	C_DESIGNA_ANIO	=				"DESIGNA_ANIO";
	static public final String	C_NUMERO_CAJG	=				"NUMERO_CAJG";
	static public final String	C_ANIO_CAJG	=					"ANIOCAJG";
	static public final String	C_NUMEJG	=					"NUMEJG";
	static public final String	C_SUFIJO	=					"SUFIJO";	
	static public final String C_FACTURADO = 					"FACTURADO";
	static public final String C_PAGADO = 						"PAGADO";
	static public final String C_IDFACTURACION = 				"IDFACTURACION";
	
	static public final String C_IDPROCURADOR = 				"IDPROCURADOR";
	static public final String C_IDINSTITUCIONPROCURADOR = 		"IDINSTITUCION_PROC";
	
	static public final String C_IDTIPORATIFICACIONEJG = 		"IDTIPORATIFICACIONEJG";
	static public final String C_FECHAAUTO	= 					"FECHAAUTO";
	static public final String C_FECHANOTIFICACION= 			"FECHANOTIFICACION";
	static public final String C_FECHARESOLUCIONCAJG= 			"FECHARESOLUCIONCAJG";
	static public final String C_IDFUNDAMENTOJURIDICO= 			"IDFUNDAMENTOJURIDICO";
	static public final String C_IDTIPORESOLAUTO= 				"IDTIPORESOLAUTO";
	static public final String C_IDTIPOSENTIDOAUTO= 			"IDTIPOSENTIDOAUTO";
	static public final String C_TURNADOAUTO= 					"TURNADOAUTO";
	static public final String C_TURNADORATIFICACION= 			"TURNADORATIFICACION";

	static public final String C_JUZGADO                = "JUZGADO";
	static public final String C_COMISARIA     		    = "COMISARIA";
	static public final String C_NUMERODILIGENCIA       = "NUMERODILIGENCIA";
	static public final String C_NUMEROPROCEDIMIENTO    = "NUMEROPROCEDIMIENTO";
	static public final String C_JUZGADOIDINSTITUCION   = "JUZGADOIDINSTITUCION";
	static public final String C_COMISARIAIDINSTITUCION = "COMISARIAIDINSTITUCION";
	
	static public final String C_IDPRETENSION   = "IDPRETENSION";
	static public final String C_IDDICTAMEN   = "IDDICTAMEN";
	static public final String C_IDPRETENSIONINSTITUCION = "IDPRETENSIONINSTITUCION";
	static public final String C_REFAUTO   = "REFAUTO";
	static public final String C_FECHADESIGPROC   = "FECHA_DES_PROC";
	static public final String C_IDENTIFICADORDS   = "IDENTIFICADORDS";
	static public final String C_IDORIGENCAJG   = "IDORIGENCAJG";
	static public final String C_PRECEPTIVO   = "IDPRECEPTIVO";
	static public final String C_SITUACION   = "IDSITUACION";
	static public final String C_IDRENUNCIA   = "IDRENUNCIA";
	static public final String C_NUMERODESIGNAPROC   = "NUMERODESIGNAPROC";
    static public final String C_IDTIPOENCALIDAD   = "IDTIPOENCALIDAD";
    static public final String C_CALIDADIDINSTITUCION   = "CALIDADIDINSTITUCION";
    static public final String C_DOCRESOLUCION   = "DOCRESOLUCION";
    static public final String C_FECHACREACION  = "FECHACREACION";
    static public final String C_USUCREACION   = "USUCREACION";
    static public final String C_NIG   = "NIG";
    static public final String C_IDPONENTE   = "IDPONENTE";
    //Crear nuevos campos en la pestaña de impugnaciones del EJG
    static public final String C_OBSERVACIONIMPUGNACION = "OBSERVACIONIMPUGNACION";
    static public final String C_FECHAPUBLICACION ="FECHAPUBLICACION";
    static public final String C_NUMERORESOLUCION = "NUMERORESOLUCION";
    static public final String C_ANIORESOLUCION = "ANIORESOLUCION";
    static public final String C_BISRESOLUCION = "BISRESOLUCION";
	
    public String getDeTipoEjg() {
		return deTipoEjg;
	}
	public void setDeTipoEjg(String deTipoEjg) {
		this.deTipoEjg = deTipoEjg;
	}
	public String getTipoEjgCol() {
		return tipoEjgCol;
	}
	public void setTipoEjgCol(String tipoEjgCol) {
		this.tipoEjgCol = tipoEjgCol;
	}		
	public String getIdRenuncia() {
		return idRenuncia;
	}
	public void setIdRenuncia(String idRenuncia) {
		this.idRenuncia = idRenuncia;
	}
	
	public String getIdPretension() {
		return idPretension;
	}
	public void setIdPretension(String idPretension) {
		this.idPretension = idPretension;
	}
	public String getIdPretensionInstitucion() {
		return idPretensionInstitucion;
	}
	public void setIdPretensionInstitucion(String idPretensionInstitucion) {
		this.idPretensionInstitucion = idPretensionInstitucion;
	}
	/**
	 * @return Returns the numEJG.
	 */
	public String getNumEJG() {
		return numEJG;
	}
	/**
	 * @param numEJG The numEJG to set.
	 */
	public void setNumEJG(String numEJG) {
		this.numEJG = numEJG;
	}
	/**
	 * @return Returns the numEJG.
	 */
	public String getSufijo() {
		return numEJG;
	}
	/**
	 * @param numEJG The numEJG to set.
	 */
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
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
//	/**
//	 * @return Returns the asistencia_anio.
//	 */
//	public Integer getAsistencia_anio() {
//		return asistencia_anio;
//	}
//	/**
//	 * @param asistencia_anio The asistencia_anio to set.
//	 */
//	public void setAsistencia_anio(Integer asistencia_anio) {
//		this.asistencia_anio = asistencia_anio;
//	}
//	/**
//	 * @return Returns the asistencia_numero.
//	 */
//	public Integer getAsistencia_numero() {
//		return asistencia_numero;
//	}
//	/**
//	 * @param asistencia_numero The asistencia_numero to set.
//	 */
//	public void setAsistencia_numero(Integer asistencia_numero) {
//		this.asistencia_numero = asistencia_numero;
//	}
	/**
	 * @return Returns the calidad.
	 */
	public String getCalidad() {
		return calidad;
	}
	/**
	 * @param calidad The calidad to set.
	 */
	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}
	/**
	 * @return Returns the delitos.
	 */
	public String getDelitos() {
		return delitos;
	}
	/**
	 * @param delitos The delitos to set.
	 */
	public void setDelitos(String delitos) {
		this.delitos = delitos;
	}
	/**
	 * @return Returns the designaAnio.
	 */
	/*
	public Integer getDesignaAnio() {
		return designaAnio;
	}
	/**
	 * @param designaAnio The designaAnio to set.
	 */
	/*
	public void setDesignaAnio(Integer designaAnio) {
		this.designaAnio = designaAnio;
	}
	/**
	 * @return Returns the designaIdTurno.
	 */
	/*
	public Integer getDesignaIdTurno() {
		return designaIdTurno;
	}
	/**
	 * @param designaIdTurno The designaIdTurno to set.
	 */
	/*
	public void setDesignaIdTurno(Integer designaIdTurno) {
		this.designaIdTurno = designaIdTurno;
	}
	/**
	 * @return Returns the designaNumero.
	 */
	/*
	public Integer getDesignaNumero() {
		return designaNumero;
	}
	/**
	 * @param designaNumero The designaNumero to set.
	 */
	/*
	public void setDesignaNumero(Integer designaNumero) {
		this.designaNumero = designaNumero;
	}
	/**
	 * @return Returns the dictamen.
	 */
	public String getDictamen() {
		return dictamen;
	}
	/**
	 * @param dictamen The dictamen to set.
	 */
	public void setDictamen(String dictamen) {
		this.dictamen = dictamen;
	}
	/**
	 * @return Returns the fechaApertura.
	 */
	public String getFechaApertura() {
		return fechaApertura;
	}
	
	public String getFechaProc() {
		return fechaDesigProc;
	}
	public void setFechaProc(String fechaDesigProc) {
		this.fechaDesigProc = fechaDesigProc;
	}
	/**
	 * @param fechaApertura The fechaApertura to set.
	 */
	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	/**
	 * @return Returns the fechaDictamen.
	 */
	public String getFechaDictamen() {
		return fechaDictamen;
	}
	/**
	 * @param fechaDictamen The fechaDictamen to set.
	 */
	public void setFechaDictamen(String fechaDictamen) {
		this.fechaDictamen = fechaDictamen;
	}
	/**
	 * @return Returns the fechaLimitePresentacion.
	 */
	public String getFechaLimitePresentacion() {
		return fechaLimitePresentacion;
	}
	/**
	 * @param fechaLimitePresentacion The fechaLimitePresentacion to set.
	 */
	public void setFechaLimitePresentacion(String fechaLimitePresentacion) {
		this.fechaLimitePresentacion = fechaLimitePresentacion;
	}
	/**
	 * @return Returns the fechaPresentacion.
	 */
	public String getFechaPresentacion() {
		return fechaPresentacion;
	}
	/**
	 * @param fechaPresentacion The fechaPresentacion to set.
	 */
	public void setFechaPresentacion(String fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}
	/**
	 * @return Returns the fechaRatificacion.
	 */
	public String getFechaRatificacion() {
		return fechaRatificacion;
	}
	/**
	 * @param fechaRatificacion The fechaRatificacion to set.
	 */
	public void setFechaRatificacion(String fechaRatificacion) {
		this.fechaRatificacion = fechaRatificacion;
	}
	/**
	 * @return Returns the guardiaTurno_idGuardia.
	 */
	public Integer getGuardiaTurno_idGuardia() {
		return guardiaTurno_idGuardia;
	}
	/**
	 * @param guardiaTurno_idGuardia The guardiaTurno_idGuardia to set.
	 */
	public void setGuardiaTurno_idGuardia(Integer guardiaTurno_idGuardia) {
		this.guardiaTurno_idGuardia = guardiaTurno_idGuardia;
	}
	/**
	 * @return Returns the guardiaTurno_idTurno.
	 */
	public Integer getGuardiaTurno_idTurno() {
		return guardiaTurno_idTurno;
	}
	/**
	 * @param guardiaTurno_idTurno The guardiaTurno_idTurno to set.
	 */
	public void setGuardiaTurno_idTurno(Integer guardiaTurno_idTurno) {
		this.guardiaTurno_idTurno = guardiaTurno_idTurno;
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
	 * @return Returns the idPersona.
	 */
	public Integer getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idPersonaJG.
	 */
	public Integer getIdPersonaJG() {
		return idPersonaJG;
	}
	/**
	 * @param idPersonaJG The idPersonaJG to set.
	 */
	public void setIdPersonaJG(Integer idPersonaJG) {
		this.idPersonaJG = idPersonaJG;
	}
	/**
	 * @return Returns the idTipoDictamenEJG.
	 */
	public Integer getIdTipoDictamenEJG() {
		return idTipoDictamenEJG;
	}
	/**
	 * @param idTipoDictamenEJG The idTipoDictamenEJG to set.
	 */
	public void setIdTipoDictamenEJG(Integer idTipoDictamenEJG) {
		this.idTipoDictamenEJG = idTipoDictamenEJG;
	}

	public Integer getIdFundamentoCalif() {
		return idFundamentoCalif;
	}
	public void setIdFundamentoCalif(Integer idFundamentoCalif) {
		this.idFundamentoCalif = idFundamentoCalif;
	}
	/**
	 * @return Returns the idTipoEJG.
	 */
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	/**
	 * @param idTipoEJG The idTipoEJG to set.
	 */
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}
	/**
	 * @return Returns the idTipoEJGColegio.
	 */
	public Integer getIdTipoEJGColegio() {
		return idTipoEJGColegio;
	}
	/**
	 * @param idTipoEJGColegio The idTipoEJGColegio to set.
	 */
	public void setIdTipoEJGColegio(Integer idTipoEJGColegio) {
		this.idTipoEJGColegio = idTipoEJGColegio;
	}
	/**
	 * @return Returns the numero.
	 */
	public Integer getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
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
	 * @return Returns the origenApertura.
	 */
	public String getOrigenApertura() {
		return origenApertura;
	}
	/**
	 * @param origenApertura The origenApertura to set.
	 */
	public void setOrigenApertura(String origenApertura) {
		this.origenApertura = origenApertura;
	}
	/**
	 * @return Returns the procurador.
	 */
	public String getProcurador() {
		return procurador;
	}
	/**
	 * @param procurador The procurador to set.
	 */
	public void setProcurador(String procurador) {
		this.procurador = procurador;
	}
	/**
	 * @return Returns the procuradorNecesario.
	 */
	public Integer getProcuradorNecesario() {
		return procuradorNecesario;
	}
	/**
	 * @param procuradorNecesario The procuradorNecesario to set.
	 */
	public void setProcuradorNecesario(Integer procuradorNecesario) {
		this.procuradorNecesario = procuradorNecesario;
	}
	/**
	 * @return Returns the ratificacionDictamen.
	 */
	public String getRatificacionDictamen() {
		return ratificacionDictamen;
	}
	/**
	 * @param ratificacionDictamen The ratificacionDictamen to set.
	 */
	public void setRatificacionDictamen(String ratificacionDictamen) {
		this.ratificacionDictamen = ratificacionDictamen;
	}
//	/** 
//	 * @return Returns the sOJ_anio.
//	 */
//	public Integer getSOJ_anio() {
//		return SOJ_anio;
//	}
//	/**
//	 * @param soj_anio The sOJ_anio to set.
//	 */
//	public void setSOJ_anio(Integer soj_anio) {
//		SOJ_anio = soj_anio;
//	}
//	/**
//	 * @return Returns the sOJ_idTipoSOJ.
//	 */
//	public Integer getSOJ_idTipoSOJ() {
//		return SOJ_idTipoSOJ;
//	}
//	/**
//	 * @param tipoSOJ The sOJ_idTipoSOJ to set.
//	 */
//	public void setSOJ_idTipoSOJ(Integer tipoSOJ) {
//		SOJ_idTipoSOJ = tipoSOJ;
//	}
//	/**
//	 * @return Returns the sOJ_numero.
//	 */
//	public Integer getSOJ_numero() {
//		return SOJ_numero;
//	}
//	/**
//	 * @param soj_numero The sOJ_numero to set.
//	 */
//	public void setSOJ_numero(Integer soj_numero) {
//		SOJ_numero = soj_numero;
//	}
	/**
	 * @return Returns the tipoLetrado.
	 */
	public String getTipoLetrado() {
		return tipoLetrado;
	}
	/**
	 * @param tipoLetrado The tipoLetrado to set.
	 */
	public void setTipoLetrado(String tipoLetrado) {
		this.tipoLetrado = tipoLetrado;
	}	
	
	/**
	 * @return Returns the facturado.
	 */
	public Double getFacturado() {
		return facturado;
	}
	/**
	 * @param facturado The facturado to set.
	 */
	public void setFacturado(Double facturado) {
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
	public Double getPagado() {
		return pagado;
	}
	/**
	 * @param pagado The pagado to set.
	 */
	public void setPagado(Double pagado) {
		this.pagado = pagado;
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
	/**
	 * @return Returns the idProcurador.
	 */
	public Integer getIdProcurador() {
		return idProcurador;
	}
	/**
	 * @param idProcurador The idProcurador to set.
	 */
	public void setIdProcurador(Integer idProcurador) {
		this.idProcurador = idProcurador;
	}
	public Integer getIdTipoRatificacionEJG() {
		return idTipoRatificacionEJG;
	}
	public void setIdTipoRatificacionEJG(Integer idTipoRatificacionEJG) {
		this.idTipoRatificacionEJG = idTipoRatificacionEJG;
	}

	public String getNumeroCAJG() {
		return numeroCAJG;
	}
	public void setNumeroCAJG(String num) {
		this.numeroCAJG = num;
	}

	public String getAnioCAJG() {
		return anioCAJG;
	}
	public void setAnioCAJG(String anio) {
		this.anioCAJG = anio;
	}
	public String getFechaAuto() {
		return fechaAuto;
	}
	public void setFechaAuto(String fechaAuto) {
		this.fechaAuto = fechaAuto;
	}
	public String getFechaNotificacion() {
		return fechaNotificacion;
	}
	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}
	public String getFechaResolucionCAJG() {
		return fechaResolucionCAJG;
	}
	public void setFechaResolucionCAJG(String fechaResolucionCAJG) {
		this.fechaResolucionCAJG = fechaResolucionCAJG;
	}
	public Integer getIdFundamentoJuridico() {
		return idFundamentoJuridico;
	}
	public void setIdFundamentoJuridico(Integer idFundamentoJuridico) {
		this.idFundamentoJuridico = idFundamentoJuridico;
	}
	public Integer getIdTipoResolAuto() {
		return idTipoResolAuto;
	}
	public void setIdTipoResolAuto(Integer idTipoResolAuto) {
		this.idTipoResolAuto = idTipoResolAuto;
	}
	public Integer getIdTipoSentidoAuto() {
		return idTipoSentidoAuto;
	}
	public void setIdTipoSentidoAuto(Integer idTipoSentidoAuto) {
		this.idTipoSentidoAuto = idTipoSentidoAuto;
	}
	public String getTurnadoAuto() {
		return turnadoAuto;
	}
	public void setTurnadoAuto(String turnadoAuto) {
		this.turnadoAuto = turnadoAuto;
	}
	public String getTurnadoRatificacion() {
		return turnadoRatificacion;
	}
	public void setTurnadoRatificacion(String turnadoRatificacion) {
		this.turnadoRatificacion = turnadoRatificacion;
	}

	public Long getComisaria() {
		return comisaria;
	}
	public void setComisaria(Long centroDetencion) {
		this.comisaria = centroDetencion;
	}
	public Long getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(Long juzgado) {
		this.juzgado = juzgado;
	}
	public String getNumeroDiligencia() {
		return numeroDiligencia;
	}
	public void setNumeroDiligencia(String numeroDiligencia) {
		this.numeroDiligencia = numeroDiligencia;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public Integer getComisariaInstitucion() {
		return comisariaInstitucion;
	}
	public void setComisariaInstitucion(Integer comisariaInstitucion) {
		this.comisariaInstitucion = comisariaInstitucion;
	}
	public Integer getJuzgadoInstitucion() {
		return juzgadoInstitucion;
	}
	public void setJuzgadoInstitucion(Integer juzgadoInstitucion) {
		this.juzgadoInstitucion = juzgadoInstitucion;
	}
	public String getIdDictamen() {
		return idDictamen;
	}
	public void setIdDictamen(String idDictamen) {
		this.idDictamen = idDictamen;
	}
	public String getRefAuto() {
		return refAuto;
	}
	public void setRefAuto(String refAuto) {
		this.refAuto = refAuto;
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
	
	public String getIdOrigenCAJG() {
		return idOrigenCAJG;
	}
	public void setIdOrigenCAJG(String idOrigenCAJG) {
		this.idOrigenCAJG = idOrigenCAJG;
	}
	public String getIdPreceptivo() {
		return idPreceptivo;
	}
	public void setIdPreceptivo(String idPreceptivo) {
		this.idPreceptivo = idPreceptivo;
	}
	public String getIdSituacion() {
		return idSituacion;
	}
	public void setIdSituacion(String idSituacion) {
		this.idSituacion = idSituacion;
	}
	public ScsPersonaJGBean getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(ScsPersonaJGBean solicitante) {
		this.solicitante = solicitante;
	}
	public ScsTipoEJGBean getTipoEjg() {
		return tipoEjg;
	}
	public void setTipoEjg(ScsTipoEJGBean tipoEjg) {
		this.tipoEjg = tipoEjg;
	}
	public Short getIdEstadoEjg() {
		return idEstadoEjg;
	}
	public void setIdEstadoEjg(Short idEstadoEjg) {
		this.idEstadoEjg = idEstadoEjg;
	}
	public String getNumeroDesignaProc() {
		return numeroDesignaProc;
	}
	public void setNumeroDesignaProc(String numeroDesignaProc) {
		this.numeroDesignaProc = numeroDesignaProc;
	}
	
	
	public Integer getIdTipoenCalidad() {
		return idTipoenCalidad;
	}
	public void setIdTipoenCalidad(Integer idTipoenCalidad) {
		this.idTipoenCalidad = idTipoenCalidad;
	}
	
	public Integer getCalidadidinstitucion() {
		return calidadidinstitucion;
	}
	public void setCalidadidinstitucion(Integer calidadidinstitucion) {
		this.calidadidinstitucion = calidadidinstitucion;
	}
	public String getDocResolucion() {
		return docResolucion;
	}
	public void setDocResolucion(String docResolucion) {
		this.docResolucion = docResolucion;
	}
	
	public String getNIG() {
		return NIG;
	}
	public void setNIG(String nIG) {
		NIG = nIG;
	}
	
	public Long getIdPonente() {
		return idPonente;
	}
	public void setIdPonente(Long idPonente) {
		this.idPonente = idPonente;
	}
	
	public String getObservacionImpugnacion() {
		return observacionImpugnacion;
	}
	public void setObservacionImpugnacion(String observacionImpugnacion) {
		this.observacionImpugnacion = observacionImpugnacion;
	}
	public String getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(String fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getNumeroResolucion() {
		return numeroResolucion;
	}
	public void setNumeroResolucion(String numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}
	public String getAnioResolucion() {
		return anioResolucion;
	}
	public void setAnioResolucion(String anioResolucion) {
		this.anioResolucion = anioResolucion;
	}

	public String getBisResolucion() {
		return bisResolucion;
	}
	public void setBisResolucion(String bisResolucion) {
		this.bisResolucion = bisResolucion;
	}
	
}