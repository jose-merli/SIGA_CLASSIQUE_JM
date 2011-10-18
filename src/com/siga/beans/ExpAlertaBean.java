/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;


public class ExpAlertaBean extends MasterBean {
    
	//Variables
    private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idAlerta;
	private String texto;
	private String fechaAlerta;
	private Integer idInstitucionTipoExpediente;
	private Integer idFase;
	private Integer idEstado;
	private String borrado;	
	ExpFasesBean fase =null;
	ExpEstadosBean estado = null;
	ExpTipoExpedienteBean tipoExpediente;
	String fechaAlertaTxt;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE = "IDTIPOEXPEDIENTE";
	static public final String C_NUMEROEXPEDIENTE = "NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE="ANIOEXPEDIENTE";
	static public final String C_IDALERTA = "IDALERTA";
	static public final String C_TEXTO = "TEXTO";
	static public final String C_FECHAALERTA = "FECHAALERTA";
	static public final String C_IDINSTITUCIONTIPOEXPEDIENTE = "IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDFASE = "IDFASE";
	static public final String C_IDESTADO = "IDESTADO";
	static public final String C_BORRADO = "BORRADO";
	
	static public final String T_NOMBRETABLA = "EXP_ALERTA";
	
	
    public Integer getAnioExpediente() {
        return anioExpediente;
    }
    public void setAnioExpediente(Integer anioExpediente) {
        this.anioExpediente = anioExpediente;
    }
    public String getBorrado() {
        return borrado;
    }
    public void setBorrado(String borrado) {
        this.borrado = borrado;
    }
    public String getFechaAlerta() {
        return fechaAlerta;
    }
    public void setFechaAlerta(String fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }
    public Integer getIdAlerta() {
        return idAlerta;
    }
    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }
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
    public Integer getIdInstitucionTipoExpediente() {
        return idInstitucionTipoExpediente;
    }
    public void setIdInstitucionTipoExpediente(
            Integer idInstitucionTipoExpediente) {
        this.idInstitucionTipoExpediente = idInstitucionTipoExpediente;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public Integer getNumeroExpediente() {
        return numeroExpediente;
    }
    public void setNumeroExpediente(Integer numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
	public ExpFasesBean getFase() {
		return fase;
	}
	public void setFase(ExpFasesBean fase) {
		this.fase = fase;
	}
	public ExpEstadosBean getEstado() {
		return estado;
	}
	public void setEstado(ExpEstadosBean estado) {
		this.estado = estado;
	}
	public ExpTipoExpedienteBean getTipoExpediente() {
		return tipoExpediente;
	}
	public void setTipoExpediente(ExpTipoExpedienteBean tipoExpediente) {
		this.tipoExpediente = tipoExpediente;
	}
	public String getFechaAlertaTxt() {
		return fechaAlertaTxt;
	}
	public void setFechaAlertaTxt(String fechaAlertaTxt) {
		this.fechaAlertaTxt = fechaAlertaTxt;
	}
}
