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
public class ExpFasesBean extends MasterBean {
    
	//Variables
	private Integer idFase;
	private String nombre;
	private String descripcion;	
	private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer diasVencimiento;
	private Integer diasAntelacion;
	
	// Nombre campos de la tabla 
	static public final String C_IDFASE = "IDFASE";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	static public final String C_DIASVENCIMIENTO="DIASVENCIMIENTO";
	static public final String C_DIASANTELACION="DIASANTELACION";
	
	static public final String T_NOMBRETABLA = "EXP_FASES";
	

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public Integer getDiasAntelacion() {
        return diasAntelacion;
    }
    public void setDiasAntelacion(Integer valor) {
        this.diasAntelacion = valor;
    }
    public Integer getDiasVencimiento() {
        return diasVencimiento;
    }
    public void setDiasVencimiento(Integer valor) {
        this.diasVencimiento = valor;
    }
}
