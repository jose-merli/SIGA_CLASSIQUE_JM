package com.siga.beans;

public class CaracteristicasAsistenciasBean extends MasterBean
{
	
	/**
	 *  Variables 
	 * 
	 * */ 	
	private Integer		idInstitucion;
	private Integer		anio;
	private Double		numero;
	private Integer		idInstitucionJuzgado;		
	private Integer		idOrigenContacto;
	private	String		descripcionContacto;
	private String		otroDescripcionOrigenContacto;
	private	String		malosTratos;
	private String		contraLibertadSexual;
	private String		judicial;
	private String		civil;
	private String		penal;
	private String		interposicionDenuncia;
	private String		solicitudMedidasCautelares;
	private String		asistenciaDeclaracion;
	private String		medidasProvisionales;
	private String		ordenProteccion;
	private String		otras;
	private	String		otrasDescripcion;
	private String		asesoramiento;
	private String		derivaActuacionesJudiciales;
	private String		interposicionMinistFiscal;
	private String		intervencionMedicoForense;
	private String		derechosJusticiaGratuita;
	private String		obligadaDesalojoDomicilio;
	private String		entrevistaLetradoDemandante;
	private	String		letradoDesignadoContiActuJudi;
	private	String		civilesPenales;
	private	String		victimaLetradoAnterioridad;
	private	Long		idPersona;
	private String		numeroProcedimiento;
	private	Long		idJuzgado;
	private	String		descripcionJuzgado;
	private	String		nig;
	private Integer		idPretension;
	private	String		descripcionPretension;
	
	
	static public final String T_NOMBRETABLA = "SCS_CARACTASISTENCIA";
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_ANIO = 								"ANIO";
	static public final String 	C_NUMERO = 								"NUMERO";
	static public final String 	C_IDORIGENCONTACTO =			 		"IDORIGENCONTACTO";
	static public final String 	C_DESCRIPCIONCONTACTO =			 		"DESCRIPCIONCONTACTO";
	static public final String 	C_IDINSTITUCION = 						"IDINSTITUCION";
	static public final String 	C_IDINSTITUCION_JUZGADO = 				"IDINSTITUCION_JUZGADO";
	static public final String 	C_OTRODESCRIPCIONORIGENCONTACTO =		"OTRODESCRIPCIONORIGENCONTACTO";
	static public final String 	C_MALOSTRATOS =							"MALOSTRATOS";
	static public final String 	C_CONTRALIBERTADSEXUAL =				"CONTRALIBERTADSEXUAL";
	static public final String 	C_JUDICIAL =							"JUDICIAL";
	static public final String 	C_CIVIL =								"CIVIL";
	static public final String 	C_INTERPOSICIONDENUNCIA =				"INTERPOSICIONDENUNCIA";
	static public final String 	C_PENAL =								"PENAL";
	static public final String 	C_ASISTENCIADECLARACION = 				"ASISTENCIADECLARACION";
	static public final String 	C_MEDIDASPROVISIONALES =				"MEDIDASPROVISIONALES";
	static public final String 	C_SOLICITUDMEDIDASCAUTELARES =			"SOLICITUDMEDIDASCAUTELARES";
	static public final String 	C_ORDENPROTECCION =						"ORDENPROTECCION";		
	static public final String 	C_OTRAS = 								"OTRAS";
	static public final String 	C_OTRASDESCRIPCION = 					"OTRASDESCRIPCION";
	static public final String 	C_ASESORAMIENTO =						"ASESORAMIENTO";
	static public final String 	C_DERIVARACTUACIONESJUDICIALES =		"DERIVARACTUACIONESJUDICIALES";
	static public final String 	C_INTERPOSICIONMINISTFISCAL = 			"INTERPOSICIONMINISTFISCAL";
	static public final String 	C_INTERVENCIONMEDICOFORENSE = 			"INTERVENCIONMEDICOFORENSE";
	static public final String 	C_DERECHOSJUSTICIAGRATUITA =			"DERECHOSJUSTICIAGRATUITA";
	static public final String 	C_OBLIGADADESALOJODOMICILIO =			"OBLIGADADESALOJODOMICILIO";
	static public final String 	C_LETRADODESIGNADOCONTIACTUJUDI =		"LETRADODESIGNADOCONTIACTUJUDI";
	static public final String 	C_ENTREVISTALETRADODEMANDANTE =			"ENTREVISTALETRADODEMANDANTE";
	static public final String 	C_CIVILESPENALES =						"CIVILESPENALES";
	static public final String 	C_VICTIMALETRADOANTERIORIDAD =			"VICTIMALETRADOANTERIORIDAD";
	static public final String 	C_IDPERSONA = 							"IDPERSONA";
	static public final String 	C_NUMEROPROCEDIMIENTO =					"NUMEROPROCEDIMIENTO";
	static public final String 	C_IDJUZGADO =							"IDJUZGADO";
	static public final String 	C_DESCRIPCIONJUZGADO =			 		"DESCRIPCIONJUZGADO";
	static public final String 	C_IDPRETENSION =						"IDPRETENSION";
	static public final String 	C_DESCRIPCIONPRETENSION =			 	"DESCRIPCIONPRETENSION";
	static public final String 	C_NIG =									"NIG";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Double getNumero() {
		return numero;
	}
	public void setNumero(Double numero) {
		this.numero = numero;
	}
	public Integer getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	public void setIdInstitucionJuzgado(Integer idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	public Integer getIdOrigenContacto() {
		return idOrigenContacto;
	}
	public void setIdOrigenContacto(Integer idOrigenContacto) {
		this.idOrigenContacto = idOrigenContacto;
	}
	public String getOtroDescripcionOrigenContacto() {
		return otroDescripcionOrigenContacto;
	}
	public void setOtroDescripcionOrigenContacto(
			String otroDescripcionOrigenContacto) {
		this.otroDescripcionOrigenContacto = otroDescripcionOrigenContacto;
	}
	public String getMalosTratos() {
		return malosTratos;
	}
	public void setMalosTratos(String malosTratos) {
		this.malosTratos = malosTratos;
	}
	public String getContraLibertadSexual() {
		return contraLibertadSexual;
	}
	public void setContraLibertadSexual(String contraLibertadSexual) {
		this.contraLibertadSexual = contraLibertadSexual;
	}
	public String getCivil() {
		return civil;
	}
	public void setCivil(String civil) {
		this.civil = civil;
	}
	public String getPenal() {
		return penal;
	}
	public void setPenal(String penal) {
		this.penal = penal;
	}
	public String getInterposicionDenuncia() {
		return interposicionDenuncia;
	}
	public void setInterposicionDenuncia(String interposicionDenuncia) {
		this.interposicionDenuncia = interposicionDenuncia;
	}
	public String getSolicitudMedidasCautelares() {
		return solicitudMedidasCautelares;
	}
	public void setSolicitudMedidasCautelares(String solicitudMedidasCautelares) {
		this.solicitudMedidasCautelares = solicitudMedidasCautelares;
	}
	public String getAsistenciaDeclaracion() {
		return asistenciaDeclaracion;
	}
	public void setAsistenciaDeclaracion(String asistenciaDeclaracion) {
		this.asistenciaDeclaracion = asistenciaDeclaracion;
	}
	public String getMedidasProvisionales() {
		return medidasProvisionales;
	}
	public void setMedidasProvisionales(String medidasProvisionales) {
		this.medidasProvisionales = medidasProvisionales;
	}
	public String getOrdenProteccion() {
		return ordenProteccion;
	}
	public void setOrdenProteccion(String ordenProteccion) {
		this.ordenProteccion = ordenProteccion;
	}
	public String getOtras() {
		return otras;
	}
	public void setOtras(String otras) {
		this.otras = otras;
	}
	public String getOtrasDescripcion() {
		return otrasDescripcion;
	}
	public void setOtrasDescripcion(String otrasDescripcion) {
		this.otrasDescripcion = otrasDescripcion;
	}
	public String getDerivaActuacionesJudiciales() {
		return derivaActuacionesJudiciales;
	}
	public void setDerivaActuacionesJudiciales(String derivaActuacionesJudiciales) {
		this.derivaActuacionesJudiciales = derivaActuacionesJudiciales;
	}
	public String getInterposicionMinistFiscal() {
		return interposicionMinistFiscal;
	}
	public void setInterposicionMinistFiscal(String interposicionMinistFiscal) {
		this.interposicionMinistFiscal = interposicionMinistFiscal;
	}
	public String getIntervencionMedicoForense() {
		return intervencionMedicoForense;
	}
	public void setIntervencionMedicoForense(String intervencionMedicoForense) {
		this.intervencionMedicoForense = intervencionMedicoForense;
	}
	public String getDerechosJusticiaGratuita() {
		return derechosJusticiaGratuita;
	}
	public void setDerechosJusticiaGratuita(String derechosJusticiaGratuita) {
		this.derechosJusticiaGratuita = derechosJusticiaGratuita;
	}
	public String getObligadaDesalojoDomicilio() {
		return obligadaDesalojoDomicilio;
	}
	public void setObligadaDesalojoDomicilio(String obligadaDesalojoDomicilio) {
		this.obligadaDesalojoDomicilio = obligadaDesalojoDomicilio;
	}
	public String getEntrevistaLetradoDemandante() {
		return entrevistaLetradoDemandante;
	}
	public void setEntrevistaLetradoDemandante(String entrevistaLetradoDemandante) {
		this.entrevistaLetradoDemandante = entrevistaLetradoDemandante;
	}
	public String getLetradoDesignadoContiActuJudi() {
		return letradoDesignadoContiActuJudi;
	}
	public void setLetradoDesignadoContiActuJudi(
			String letradoDesignadoContiActuJudi) {
		this.letradoDesignadoContiActuJudi = letradoDesignadoContiActuJudi;
	}
	public String getCivilesPenales() {
		return civilesPenales;
	}
	public void setCivilesPenales(String civilesPenales) {
		this.civilesPenales = civilesPenales;
	}
	public String getVictimaLetradoAnterioridad() {
		return victimaLetradoAnterioridad;
	}
	public void setVictimaLetradoAnterioridad(String victimaLetradoAnterioridad) {
		this.victimaLetradoAnterioridad = victimaLetradoAnterioridad;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public Long getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(Long idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	public Integer getIdPretension() {
		return idPretension;
	}
	public void setIdPretension(Integer idPretension) {
		this.idPretension = idPretension;
	}
	public String getJudicial() {
		return judicial;
	}
	public void setJudicial(String judicial) {
		this.judicial = judicial;
	}
	public String getAsesoramiento() {
		return asesoramiento;
	}
	public void setAsesoramiento(String asesoramiento) {
		this.asesoramiento = asesoramiento;
	}
	public String getDescripcionContacto() {
		return descripcionContacto;
	}
	public void setDescripcionContacto(String descripcionContacto) {
		this.descripcionContacto = descripcionContacto;
	}
	public String getDescripcionJuzgado() {
		return descripcionJuzgado;
	}
	public void setDescripcionJuzgado(String descripcionJuzgado) {
		this.descripcionJuzgado = descripcionJuzgado;
	}
	public String getDescripcionPretension() {
		return descripcionPretension;
	}
	public void setDescripcionPretension(String descripcionPretension) {
		this.descripcionPretension = descripcionPretension;
	}
}

