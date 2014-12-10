/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;


public class ExpCampoConfBean extends MasterBean {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8196460649759131308L;
	//Variables
    private Integer idInstitucion;
	private Integer idTipoExpediente;
	private Integer idCampo;
	private Integer idPestanaConf;
	private Integer idCampoConf;
	private Integer seleccionado;
	private Integer maxLong;
	private Integer tipo;
	private String nombre;
	private Integer orden;
	private Integer general;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE = "IDTIPOEXPEDIENTE";
	static public final String C_IDPESTANACONF = "IDPESTANACONF";
	static public final String C_IDCAMPOCONF="IDCAMPOCONF";
	static public final String C_SELECCIONADO = "SELECCIONADO";
	static public final String C_MAXLONG = "MAXLONG";
	static public final String C_TIPO = "TIPO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_ORDEN = "ORDEN";
	static public final String C_IDCAMPO = "IDCAMPO";
	static public final String C_GENERAL = "GENERAL";
	
	static public final String T_NOMBRETABLA = "EXP_CAMPOCONF";
	
	
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer valor) {
        this.idInstitucion = valor;
    }

    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer valor) {
        this.idTipoExpediente = valor;
    }

    public Integer getIdCampo() {
        return idCampo;
    }
    public void setIdCampo(Integer valor) {
        this.idCampo = valor;
    }

    public Integer getIdPestanaConf() {
        return idPestanaConf;
    }
    public void setIdPestanaConf(Integer valor) {
        this.idPestanaConf = valor;
    }

    public Integer getIdCampoConf() {
        return idCampoConf;
    }
    public void setIdCampoConf(Integer valor) {
        this.idCampoConf = valor;
    }

    public Integer getSeleccionado() {
        return seleccionado;
    }
    public void setSeleccionado(Integer valor) {
        this.seleccionado = valor;
    }

    public Integer getOrden() {
        return orden;
    }
    public void setOrden(Integer valor) {
        this.orden = valor;
    }

    public Integer getTipo() {
        return tipo;
    }
    public void setTipo(Integer valor) {
        this.tipo = valor;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String valor) {
        this.nombre = valor;
    }

    public Integer getGeneral() {
		return general;
	}
	public void setGeneral(Integer valor) {
		this.general = valor;
	}
	public Integer getMaxLong() {
		return maxLong;
	}
	public void setMaxLong(Integer maxLong) {
		this.maxLong = maxLong;
	}
	
}
