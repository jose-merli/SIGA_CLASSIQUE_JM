/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpClasificacionesBean extends MasterBean {
    
	//Variables
	private Integer idClasificacion;
	private String nombre;
	private Integer idInstitucion;
	private Integer idTipoExpediente;
	
	// Nombre campos de la tabla 
	static public final String C_IDCLASIFICACION = "IDCLASIFICACION";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	
	static public final String T_NOMBRETABLA = "EXP_CLASIFICACION";
	
	public Integer getIdClasificacion() {
        return idClasificacion;
    }
    public void setIdClasificacion(Integer idClasificacion) {
        this.idClasificacion = idClasificacion;
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
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
}
