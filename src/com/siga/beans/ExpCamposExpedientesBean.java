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
public class ExpCamposExpedientesBean extends MasterBean {
    
	//Variables
    private Integer idCampo;
	private String nombre;
	private String TipoCampo;
	private String fechaModificacion;
	private Integer usuModificacion;
	private String proceso;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDCAMPO="IDCAMPO";
	static public final String C_NOMBRE="NOMBRE";
	static public final String C_TIPOCAMPO="TIPOCAMPO";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_PROCESO = "PROCESO";
	
	static public final String T_NOMBRETABLA = "EXP_CAMPOSEXPEDIENTES";
	

	
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    public Integer getIdCampo() {
        return idCampo;
    }
    public void setIdCampo(Integer idCampo) {
        this.idCampo = idCampo;
    }
    
    public Integer getUsuModificacion() {
        return usuModificacion;
    }
    public void setUsuModificacion(Integer usuModificacion) {
        this.usuModificacion = usuModificacion;
    }
    
    public String getTipoCampo() {
        return TipoCampo;
    }
    public void setTipoCampo(String TipoCampo) {
        this.TipoCampo = TipoCampo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
