/*
 * Created on Apr 11, 2005
 * @author juan.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de consultas
 */
public class ConCamposSalidaBean extends MasterBean {
	
	//	Variables
	private Integer idInstitucion;
	private Integer idConsulta;
	private String cabecera;
	private Integer orden;
	private Integer idCampo;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_CABECERA ="CABECERA";
	static public final String C_ORDEN ="ORDEN";
	static public final String C_IDCAMPO ="IDCAMPO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_CAMPOSSALIDA";
	
	
    public Integer getIdCampo() {
        return idCampo;
    }
    public void setIdCampo(Integer idCampo) {
        this.idCampo = idCampo;
    }
    public Integer getIdConsulta() {
        return idConsulta;
    }
    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }
    public String getCabecera() {
        return cabecera;
    }
    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getOrden() {
        return orden;
    }
    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
