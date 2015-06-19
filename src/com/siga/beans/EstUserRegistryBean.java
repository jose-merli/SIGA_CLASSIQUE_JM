/*
 * @author Carlos Ruano
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de est_user_registry
 */
public class EstUserRegistryBean extends MasterBean {

	private static final long serialVersionUID = 7035160563301031628L;
	
	// Variables
	private Integer idUsuario;
	private Integer idInstitucion;
	private String fechaRegistro;
	private String idPerfil;

	// Nombre campos de la tabla
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_FECHAREGISTRO = "FECHA_REGISTRO";
	static public final String C_IDPERFIL = "IDPERFIL";

	static public final String T_NOMBRETABLA = "EST_USER_REGISTRY";

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}

}
