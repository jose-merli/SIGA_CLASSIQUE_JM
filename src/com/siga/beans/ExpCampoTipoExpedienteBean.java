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
public class ExpCampoTipoExpedienteBean extends MasterBean {
    
	public static final String si= "S";
	public static final String no= "N";
	public static final String DENUNCIANTE = "pestana.auditoriaexp.denunciante";
	public static final String  IMPUGNANTE = "pestana.auditoriaexp.impugnante";
	public static final String DENUNCIADO = "pestana.auditoriaexp.denunciado";
	public static final String  IMPUGNADO = "pestana.auditoriaexp.impugnado";
	public static final String NUMEXPEDIENTE = "expedientes.auditoria.literal.nexpdisciplinario";
	public static final String  NUMEJG = "expedientes.auditoria.literal.numejg";
	//Variables
    private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer idCampo;
	private String visible;
	private String fechaModificacion;
	private Integer usuModificacion;
	private String nombre;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	static public final String C_IDCAMPO="IDCAMPO";
	static public final String C_VISIBLE="VISIBLE";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_NOMBRE = "NOMBRE";
	
	static public final String T_NOMBRETABLA = "EXP_CAMPOTIPOEXPEDIENTE";
	

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
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public Integer getUsuModificacion() {
        return usuModificacion;
    }
    public void setUsuModificacion(Integer usuModificacion) {
        this.usuModificacion = usuModificacion;
    }
    public String getVisible() {
        return visible;
    }
    public void setVisible(String visible) {
        this.visible = visible;
    }
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
