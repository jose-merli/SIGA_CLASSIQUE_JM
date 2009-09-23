/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de consultas-perfil
 */
public class ConConsultaPerfilBean extends MasterBean {

	//	Variables
	private Integer idInstitucion;
	private String idPerfil;
	private Integer idInstitucion_Consulta;
	private Integer idConsulta;
	private String fechaModificacion;
	private Integer usuModificacion;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDPERFIL ="IDPERFIL";
	static public final String C_IDINSTITUCION_CONSULTA ="IDINSTITUCION_CONSULTA";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_CONSULTAPERFIL";
	
	
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdInstitucion_Consulta() {
		return idInstitucion_Consulta;
	}
	public void setIdInstitucion_Consulta(Integer idInstitucion_Consulta) {
		this.idInstitucion_Consulta = idInstitucion_Consulta;
	}
	public String getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
