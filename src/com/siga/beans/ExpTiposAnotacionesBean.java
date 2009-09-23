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
public class ExpTiposAnotacionesBean extends MasterBean {
    
	//Variables
	private Integer idTipoAnotacion;
	private String nombre;
	private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer idEstado;
	private Integer idFase;
	private String mensaje;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDTIPOANOTACION = "IDTIPOANOTACION";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	static public final String C_IDESTADO = "IDESTADO";
	static public final String C_IDFASE = "IDFASE";
	static public final String C_MENSAJE = "MENSAJE";
	
	static public final String T_NOMBRETABLA = "EXP_TIPOANOTACION";
	
    public Integer getIdEstado() {
        return idEstado;
    }
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
    public Integer getIdFase() {
        return idFase;
    }
    public void setIdFase(Integer idFase) {
        this.idFase = idFase;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdTipoAnotacion() {
        return idTipoAnotacion;
    }
    public void setIdTipoAnotacion(Integer idTipoAnotacion) {
        this.idTipoAnotacion = idTipoAnotacion;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }        
}
