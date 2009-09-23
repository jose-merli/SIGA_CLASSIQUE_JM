package com.siga.beans;

/**
 * Bean de la tabla CEN_SANCION
 * 
 * @author rgg
 * @since 16/03/2007
 */
public class CenSancionBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idSancion;
	private Integer	idPersona;
	private Integer	idTipoSancion;
	private Integer	idInstitucion;
	private String	refColegio;
	private String	refCGAE;
	private String	fechaResolucion;
	private String	fechaImposicion;
	private String	fechaAcuerdo;
	private String	fechaFirmeza;
	private String	chkFirmeza;
	private String	fechaInicio;
	private String	fechaFin;
	private String	fechaRehabilitado;
	private String	chkRehabilitado;
	private String	texto;
	private String	observaciones;
	private Integer	idInstitucionSancion;

	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "CEN_SANCION";
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDSANCION = 		"IDSANCION";
	static public final String 	C_IDPERSONA = 	"IDPERSONA";
	static public final String  C_IDTIPOSANCION = "IDTIPOSANCION";
	static public final String 	C_REFCOLEGIO = 		"REFCOLEGIO";
	static public final String 	C_REFCGAE = 	"REFCGAE";
	static public final String  C_FECHARESOLUCION = "FECHARESOLUCION";
	static public final String  C_FECHAIMPOSICION = "FECHAIMPOSICION";
	static public final String 	C_FECHAACUERDO = 		"FECHAACUERDO";
	static public final String  C_FECHAFIRMEZA = "FECHAFIRMEZA";
	static public final String  C_CHKFIRMEZA = "CHKFIRMEZA";
	static public final String 	C_FECHAINICIO = 		"FECHAINICIO";
	static public final String 	C_FECHAFIN = 	"FECHAFIN";
	static public final String  C_FECHAREHABILITADO = "FECHAREHABILITADO";
	static public final String  C_CHKREHABILITADO = "CHKREHABILITADO";
	static public final String  C_TEXTO = "TEXTO";
	static public final String  C_OBSERVACIONES = "OBSERVACIONES";
	static public final String  C_IDINSTITUCION = "IDINSTITUCION";
	static public final String  C_IDINSTITUCIONSANCION = "IDINSTITUCIONSANCION";


	/**
	 * @return 
	 */
	public Integer getIdSancion() {
		return idSancion;
	}
	/**
	 * @param 
	 */
	public void setIdSancion(Integer valor) {
		this.idSancion = valor;
	}
	/**
	 * @return 
	 */
	public Integer getIdPersona() {
		return idPersona;
	}
	/**
	 * @param 
	 */
	public void setIdPersona(Integer valor) {
		this.idPersona = valor;
	}
	/**
	 * @return
	 */
	public Integer getIdTipoSancion() {
		return idTipoSancion;
	}
	/**
	 * @param 
	 */
	public void setIdTipoSancion(Integer valor) {
		this.idTipoSancion = valor;
	}
	/**
	 * @return 
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param 
	 */
	public void setIdInstitucion(Integer valor) {
		this.idInstitucion = valor;
	}
	/**
	 * @return 
	 */
	public Integer getIdInstitucionSancion() {
		return idInstitucionSancion;
	}
	/**
	 * @param 
	 */
	public void setIdInstitucionSancion(Integer valor) {
		this.idInstitucionSancion = valor;
	}
	/**
	 * @return 
	 */
	public String getRefColegio() {
		return refColegio;
	}
	/**
	 * @param 
	 */
	public void setRefColegio(String descripcion) {
		this.refColegio = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getRefCGAE() {
		return refCGAE;
	}
	/**
	 * @param 
	 */
	public void setRefCGAE(String descripcion) {
		this.refCGAE = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaResolucion() {
		return fechaResolucion;
	}
	/**
	 * @param 
	 */
	public void setFechaResolucion(String descripcion) {
		this.fechaResolucion = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaImposicion() {
		return fechaImposicion;
	}
	/**
	 * @param 
	 */
	public void setFechaImposicion(String descripcion) {
		this.fechaImposicion = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaAcuerdo() {
		return fechaAcuerdo;
	}
	/**
	 * @param 
	 */
	public void setFechaAcuerdo(String descripcion) {
		this.fechaAcuerdo = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaFirmeza() {
		return fechaFirmeza;
	}
	/**
	 * @param 
	 */
	public void setFechaFirmeza(String descripcion) {
		this.fechaFirmeza = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getChkFirmeza() {
		return chkFirmeza;
	}
	/**
	 * @param 
	 */
	public void setChkFirmeza(String descripcion) {
		this.chkFirmeza = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param 
	 */
	public void setFechaInicio(String descripcion) {
		this.fechaInicio = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaFin() {
		return fechaFin;
	}
	/**
	 * @param 
	 */
	public void setFechaFin(String descripcion) {
		this.fechaFin = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getFechaRehabilitado() {
		return fechaRehabilitado;
	}
	/**
	 * @param 
	 */
	public void setFechaRehabilitado(String descripcion) {
		this.fechaRehabilitado = descripcion;
	}
	
	/**
	 * @return 
	 */
	public String getChkRehabilitado() {
		return chkRehabilitado;
	}
	/**
	 * @param 
	 */
	public void setChkRehabilitado(String descripcion) {
		this.chkRehabilitado = descripcion;
	}
	
	
	/**
	 * @return 
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param 
	 */
	public void setTexto(String descripcion) {
		this.texto = descripcion;
	}
	
	
	/**
	 * @return 
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param 
	 */
	public void setObservaciones(String descripcion) {
		this.observaciones = descripcion;
	}
	
	
}