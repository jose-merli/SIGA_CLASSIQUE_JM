/*
 * Created on Apr 13, 2005
 * @author juan.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla ConTipoCampoConsulta
 */
public class ConTipoCampoConsultaBean extends MasterBean {

	private Integer idTipoCampo;
	private String descripcion;
	private Integer idBase;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDTIPOCAMPO ="IDTIPOCAMPO";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_IDBASE ="IDBASE";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";	
	
	static public final String T_NOMBRETABLA = "CON_TIPOCAMPOCONSULTA";	
	
    public Integer getIdTipoCampo() {
        return idTipoCampo;
    }
    public void setIdTipoCampo(Integer idTipoCampo) {
        this.idTipoCampo = idTipoCampo;
    }
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	public Integer getIdBase() {
		return idBase;
	}
	public void setIdBase(Integer idBase) {
		this.idBase = idBase;
	}	
}
