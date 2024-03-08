package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DESIGNA_DATOSADICIONALES
 * 
 * @since 07/03/2024
 */
public class ScsDesignaDatosAdicionalesBean extends MasterBean {
	
	private static final long serialVersionUID = 8607899948216476084L;
	// Variables 
	private Integer	idInstitucion ;
	private Integer	idTurno ;
	private Integer	anio ;
	private Long	numero ;
	private Long	numeroAsunto ;
	private String	fechaModificacion ;
	private Integer	usuModificacion ;
	private Integer	tipoAuto ;
	private String	fechaResolucionJudicialOposicion ;
	private String	fechaResolucionSentenciaFirme ;
	private Integer	numeroVistasAdicionales ;
	private String	fechaVista ;
	private Integer	numeroPersonadosMacrocausa ;
	private String	esVictima ;
	private String	esSustitucion ;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_DESIGNA_DATOSADICIONALES";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION 						=  "IDINSTITUCION";
	static public final String	C_IDTURNO                               =  "IDTURNO";
	static public final String	C_ANIO                                  =  "ANIO";
	static public final String	C_NUMERO                                =  "NUMERO";
	static public final String	C_NUMEROASUNTO                          =  "NUMEROASUNTO";
	static public final String	C_FECHAMODIFICACION                     =  "FECHAMODIFICACION";
	static public final String	C_USUMODIFICACION                       =  "USUMODIFICACION";
	static public final String	C_TIPO_AUTO                             =  "TIPO_AUTO";
	static public final String	C_FECHA_RESOLUCION_JUDICIAL_OPOSICION   =  "FECHA_RESOLUCION_JUDICIAL_OPOSICION";
	static public final String	C_FECHA_RESOLUCION_SENTENCIA_FIRME      =  "FECHA_RESOLUCION_SENTENCIA_FIRME";
	static public final String	C_NUMERO_VISTAS_ADICIONALES             =  "NUMERO_VISTAS_ADICIONALES";
	static public final String	C_FECHA_VISTA                           =  "FECHA_VISTA";
	static public final String	C_NUMERO_PERSONADOS_MACROCAUSA          =  "NUMERO_PERSONADOS_MACROCAUSA";
	static public final String	C_ESVICTIMA                             =  "ESVICTIMA";
	static public final String	C_ESSUSTITUCION                         =  "ESSUSTITUCION";
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public Long getNumeroAsunto() {
		return numeroAsunto;
	}
	public void setNumeroAsunto(Long numeroAsunto) {
		this.numeroAsunto = numeroAsunto;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public Integer getTipoAuto() {
		return tipoAuto;
	}
	public void setTipoAuto(Integer tipoAuto) {
		this.tipoAuto = tipoAuto;
	}
	public String getFechaResolucionJudicialOposicion() {
		return fechaResolucionJudicialOposicion;
	}
	public void setFechaResolucionJudicialOposicion(String fechaResolucionJudicialOposicion) {
		this.fechaResolucionJudicialOposicion = fechaResolucionJudicialOposicion;
	}
	public String getFechaResolucionSentenciaFirme() {
		return fechaResolucionSentenciaFirme;
	}
	public void setFechaResolucionSentenciaFirme(String fechaResolucionSentenciaFirme) {
		this.fechaResolucionSentenciaFirme = fechaResolucionSentenciaFirme;
	}
	public Integer getNumeroVistasAdicionales() {
		return numeroVistasAdicionales;
	}
	public void setNumeroVistasAdicionales(Integer numeroVistasAdicionales) {
		this.numeroVistasAdicionales = numeroVistasAdicionales;
	}
	public String getFechaVista() {
		return fechaVista;
	}
	public void setFechaVista(String fechaVista) {
		this.fechaVista = fechaVista;
	}
	public Integer getNumeroPersonadosMacrocausa() {
		return numeroPersonadosMacrocausa;
	}
	public void setNumeroPersonadosMacrocausa(Integer numeroPersonadosMacrocausa) {
		this.numeroPersonadosMacrocausa = numeroPersonadosMacrocausa;
	}
	public String getEsVictima() {
		return esVictima;
	}
	public void setEsVictima(String esVictima) {
		this.esVictima = esVictima;
	}
	public String getEsSustitucion() {
		return esSustitucion;
	}
	public void setEsSustitucion(String esSustitucion) {
		this.esSustitucion = esSustitucion;
	}
	
}