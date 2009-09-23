/*
 * Created on Apr 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de criterios dinámicos de una consulta
 */
public class ConCriteriosDinamicosBean extends MasterBean {

//	Variables
	private Integer idInstitucion;
	private Integer idConsulta;
	private Integer orden;
	private Integer idCampo;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_ORDEN ="ORDEN";
	static public final String C_IDCAMPO ="IDCAMPO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_CRITERIOSDINAMICOS";
	
	
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
