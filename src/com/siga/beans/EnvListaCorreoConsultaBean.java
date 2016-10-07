/*
 * Created on May 19, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de listas correos - consultas
 */
public class EnvListaCorreoConsultaBean extends MasterBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6644423401818614124L;
	//Variables
	private Integer idInstitucion;
	private Long idConsulta;
	private Integer idListaCorreo;
	private Integer idInstitucionCon;
	
	// Nombre campos de la tabla 
	static public final String C_IDLISTACORREO = "IDLISTACORREO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDCONSULTA = "IDCONSULTA";
	static public final String C_IDINSTITUCION_CON = "IDINSTITUCION_CON";
	
	static public final String T_NOMBRETABLA = "ENV_LISTACORREOCONSULTA";
	
	
	public Long getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(Long idConsulta) {
		this.idConsulta = idConsulta;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdListaCorreo() {
		return idListaCorreo;
	}
	public void setIdListaCorreo(Integer idListaCorreo) {
		this.idListaCorreo = idListaCorreo;
	}
	public Integer getIdInstitucionCon() {
		return idInstitucionCon;
	}
	public void setIdInstitucionCon(Integer idInstitucionCon) {
		this.idInstitucionCon = idInstitucionCon;
	}

}
