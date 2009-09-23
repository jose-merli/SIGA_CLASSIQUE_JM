/*
 * Created on Mar 07, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EnvListaCorreosBean extends MasterBean {
    
	//Variables
	private Integer idListaCorreo;
	private String nombre;
	private Integer idInstitucion;
	private String descripcion;
	private String dinamica;	
	
	// Nombre campos de la tabla 
	static public final String C_IDLISTACORREO = "IDLISTACORREO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_DESCRIPCION="DESCRIPCION";
	static public final String C_DINAMICA = "DINAMICA";	
	
	static public final String T_NOMBRETABLA = "ENV_LISTACORREOS";	

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDinamica() {
        return dinamica;
    }
    public void setDinamica(String dinamica) {
        this.dinamica = dinamica;
    }
    public Integer getIdListaCorreo() {
        return idListaCorreo;
    }
    public void setIdListaCorreo(Integer idListaCorreo) {
        this.idListaCorreo = idListaCorreo;
    }
}
