//VERSIONES:
//raul.ggonzalez 08-03-2005 creacion
//
package com.siga.beans;

/**
* Bean de la tabla FCS_PAGO_COLEGIADO
* 
* @author Juan Antonio Saiz 10-09-2009
* 
*/
public class FcsPagoColegiadoBean extends MasterBean {
	
	/* Variables */
	private Integer		idInstitucion;
	private Integer		idPagosJG;
	private Integer		idPerOrigen;
	private Integer		idPerDestino;
	private Double		impOficio;
	private Double		impAsistencia;
	private Double		impSOJ;
	private Double		impEJG;
	private Double		impMovVar;
	private Integer		impIRPF;
	private Double		impRet;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_PAGO_COLEGIADO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDPAGOSJG					= "IDPAGOSJG";
	static public final String C_IDPERORIGEN				= "IDPERORIGEN";
	static public final String C_IDPERDESTINO				= "IDPERDESTINO";
	static public final String C_IMPOFICIO					= "IMPOFICIO";
	static public final String C_IMPASISTENCIA				= "IMPASISTENCIA";
	static public final String C_IMPSOJ						= "IMPSOJ";
	static public final String C_IMPEJG						= "IMPEJG";
	static public final String C_IMPMOVVAR					= "IMPMOVVAR";
	static public final String C_IMPIRPF					= "IMPIRPF";
	static public final String C_IMPRET						= "IMPRET";
	static public final String C_FECHAMODIFICACION			= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION			= "USUMODIFICACION";
	

	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	public Integer getIdPerOrigen() {
		return idPerOrigen;
	}
	public void setIdPerOrigen(Integer idPerOrigen) {
		this.idPerOrigen = idPerOrigen;
	}
	public Integer getIdPerDestino() {
		return idPerDestino;
	}
	public void setIdPerDestino(Integer idPerDestino) {
		this.idPerDestino = idPerDestino;
	}
	public Double getImpOficio() {
		return impOficio;
	}
	public void setImpOficio(Double impOficio) {
		this.impOficio = impOficio;
	}
	public Double getImpAsistencia() {
		return impAsistencia;
	}
	public void setImpAsistencia(Double impAsistencia) {
		this.impAsistencia = impAsistencia;
	}
	public Double getImpSOJ() {
		return impSOJ;
	}
	public void setImpSOJ(Double impSOJ) {
		this.impSOJ = impSOJ;
	}
	public Double getImpEJG() {
		return impEJG;
	}
	public void setImpEJG(Double impEJG) {
		this.impEJG = impEJG;
	}
	public Double getImpMovVar() {
		return impMovVar;
	}
	public void setImpMovVar(Double impMovVar) {
		this.impMovVar = impMovVar;
	}
	public Integer getImpIRPF() {
		return impIRPF;
	}
	public void setImpIRPF(Integer impIRPF) {
		this.impIRPF = impIRPF;
	}
	public Double getImpRet() {
		return impRet;
	}
	public void setImpRet(Double impRet) {
		this.impRet = impRet;
	}
}
