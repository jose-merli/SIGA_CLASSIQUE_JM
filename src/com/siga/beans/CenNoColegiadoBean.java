/*
 * Created on 25-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.siga.beans;


/**
 * @author daniel.campos
 * 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author pilard
 *
 * Se han añadido tres nuevos campos en la tabla CEN_NOCOLEGIADO: PREFIJO_NUMREG, CONTADOR_NUMREG, SUFIJO_NUMREG
 */
public class CenNoColegiadoBean extends MasterBean {

	/* Variables */
	private Long idPersona;
	private Integer idInstitucion,contadorNumReg,contadorNumRegSP=null;
	private String numeroReg=null, sociedadSJ=null, tipo=null, anotaciones=null, prefijoNumReg=null, sociedadSP=null; 
	private String  sufijoNumReg=null;
	private String numeroRegSP=null,prefijoNumRegSP=null,sufijoNumRegSP=null;
	private String  fecha_constitucion=null;
	private String  fechafin=null;
	private String  resena=null;
	private String  objetoSocial=null;
	private String nopoliza,companiaseg=null;
	private Long idPersonaNotario;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_NOCOLEGIADO";

	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA 			= "IDPERSONA";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";	
	
	static public final String C_SOCIEDADSJ			= "SOCIEDADSJ";
	static public final String C_SOCIEDADSP			= "SOCIEDADPROFESIONAL";
	static public final String C_TIPO	 			= "TIPO";
	static public final String C_ANOTACIONES 		= "ANOTACIONES";
	static public final String C_PREFIJO_NUMREG		= "PREFIJO_NUMREG";
	static public final String C_CONTADOR_NUMREG	= "CONTADOR_NUMREG";
	static public final String C_SUFIJO_NUMREG		= "SUFIJO_NUMREG";
	static public final String C_FECHA_CONSTITUCION	= "FECHA_CONSTITUCION";
	static public final String C_FECHAFIN			= "FECHAFIN";
	static public final String C_RESENA				= "RESENA";
	static public final String C_OBJETOSOCIAL		= "OBJETOSOCIAL";
	static public final String C_IDPERSONANOTARIO	= "IDPERSONANOTARIO";
	static public final String C_PREFIJO_NUMREGSP	= "PREFIJO_NUMSSPP";
	static public final String C_CONTADOR_NUMREGSP	= "CONTADOR_NUMSSPP";
	static public final String C_SUFIJO_NUMREGSP	= "SUFIJO_NUMSSPP";
	static public final String C_NOPOLIZA			= "NOPOLIZA";
	static public final String C_COMPANIASEG		= "COMPANIASEG";
	
	// Metodos SET
	public void setIdInstitucion(Integer idInstitucion) {		this.idInstitucion = idInstitucion;	}
	public void setIdPersona(Long idPersona) 			{		this.idPersona = idPersona;			}
	
	
	// Metodos GET
	public Integer getIdInstitucion() 	{	return idInstitucion;	}
	public Long getIdPersona() 			{	return idPersona;		}
	
	
	public String getAnotaciones() {
		return anotaciones;
	}
	public void setAnotaciones(String anotaciones) {
		this.anotaciones = anotaciones;
	}

	public String getSociedadSJ() {
		return sociedadSJ;
	}
	public void setSociedadSJ(String sociedadSJ) {
		this.sociedadSJ = sociedadSJ;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPrefijoNumReg() {
		return prefijoNumReg;
	}
	public void setPrefijoNumReg(String prefijoNumReg) {
		this.prefijoNumReg = prefijoNumReg;
	}
	public Integer getContadorNumReg() {
		return contadorNumReg;
	}
	public void setContadorNumReg(Integer contadorNumReg) {
		this.contadorNumReg = contadorNumReg;
	}
	public String getSufijoNumReg() {
		return sufijoNumReg;
	}
	public void setSufijoNumReg(String sufijoNumReg) {
		this.sufijoNumReg = sufijoNumReg;
	}
	public String getFechaConstitucion() {
		return fecha_constitucion;
	}
	public void setFechaConstitucion(String fecha_constitucion) {
		this.fecha_constitucion = fecha_constitucion;
	}
	public String getFechaFin() {
		return fechafin;
	}
	public void setFechaFin(String fechafin) {
		this.fechafin = fechafin;
	}
	public String getResena() {
		return resena;
	}
	public void setResena(String resena) {
		this.resena = resena;
	}
	public String getObjetoSocial() {
		return objetoSocial;
	}
	public void setObjetoSocial(String objetoSocial) {
		this.objetoSocial = objetoSocial;
	}
	public String getNoPoliza() {
		return nopoliza;
	}
	public void setNoPoliza(String nopoliza) {
		this.nopoliza = nopoliza;
	}
	public String getCompaniaSeg() {
		return companiaseg;
	}
	public void setCompaniaSeg(String companiaseg) {
		this.companiaseg = companiaseg;
	}
	
	public Long getIdPersonaNotario() {
		return idPersonaNotario;
	}
	public void setIdPersonaNotario(Long idPersonaNotario) {
		this.idPersonaNotario = idPersonaNotario;
	}
	

	public String getSociedadSP() {
		return sociedadSP;
	}
	public void setSociedadSP(String sociedadSP) {
		this.sociedadSP = sociedadSP;
	}
	public String getPrefijoNumRegSP() {
		return prefijoNumRegSP;
	}
	public void setPrefijoNumRegSP(String prefijoNumRegSP) {
		this.prefijoNumRegSP = prefijoNumRegSP;
	}
	public Integer getContadorNumRegSP() {
		return contadorNumRegSP;
	}
	public void setContadorNumRegSP(Integer contadorNumRegSP) {
		this.contadorNumRegSP = contadorNumRegSP;
	}
	public String getSufijoNumRegSP() {
		return sufijoNumRegSP;
	}
	public void setSufijoNumRegSP(String sufijoNumRegSP) {
		this.sufijoNumRegSP = sufijoNumRegSP;
	}
}
