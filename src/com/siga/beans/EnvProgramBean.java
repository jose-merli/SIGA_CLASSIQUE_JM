/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvProgramBean extends MasterBean {
    
	//Variables
    private Integer idInstitucion;
	private Integer idEnvio;
	private Integer idProgram;
	private String estado;
	
	
	
	private Integer idioma;
	
	private String idiomaCodigoExt;
	EnvEnvioProgramadoBean envioProgramado;
	
	// Nombre campos de la tabla 
	static public final String C_IDPROGRAM = "IDPROGRAM";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_ESTADO="ESTADO";
	
	static public final String C_IDIOMA = "IDIOMA";
	


	public Integer getIdInstitucion() {
		return idInstitucion;
	}


	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}


	public Integer getIdEnvio() {
		return idEnvio;
	}


	public void setIdEnvio(Integer idEnvio) {
		this.idEnvio = idEnvio;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	


	

	public Integer getIdProgram() {
		return idProgram;
	}


	public void setIdProgram(Integer idProgram) {
		this.idProgram = idProgram;
	}


	

	
	public Integer getIdioma() {
		return idioma;
	}


	public void setIdioma(Integer idioma) {
		this.idioma = idioma;
	}


	public String getIdiomaCodigoExt() {
		return idiomaCodigoExt;
	}


	public void setIdiomaCodigoExt(String idiomaCodigoExt) {
		this.idiomaCodigoExt = idiomaCodigoExt;
	}


	public EnvEnvioProgramadoBean getEnvioProgramado() {
		return envioProgramado;
	}


	public void setEnvioProgramado(EnvEnvioProgramadoBean envioProgramado) {
		this.envioProgramado = envioProgramado;
	}	


}
