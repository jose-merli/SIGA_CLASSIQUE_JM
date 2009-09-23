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
public class ExpPlazoEstadoClasificacionBean extends MasterBean {
    
	//Variables
    private Integer idFase;
    private Integer idEstado;	
	private Integer idClasificacion;
	private String plazo;
	private Integer idInstitucion;
	private Integer idTipoExpediente;
	
	// Nombre campos de la tabla 
	static public final String C_IDFASE = "IDFASE";
	static public final String C_IDESTADO = "IDESTADO";	
	static public final String C_IDCLASIFICACION = "IDCLASIFICACION";
	static public final String C_PLAZO = "PLAZO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	
	static public final String T_NOMBRETABLA = "EXP_PLAZOESTADOCLASIFICACION";
	
	//Constantes tipos de plazo
	static public final int DIAS_NATURALES = 0;
	static public final int DIAS_HABILES = 1;
	static public final int MESES = 2;
	static public final int ANIOS = 3;
	
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
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
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
    public String getPlazo() {
        return plazo;
    }
    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }
    
    public int getTipoPlazo(){
    	if (this.plazo.endsWith("dh")){
    		return DIAS_HABILES;
    	}else if (this.plazo.endsWith("m")){
    		return MESES;
    	}else if (this.plazo.endsWith("a")){
    		return ANIOS;
    	}else{
    		return DIAS_NATURALES;
    	}
    }
    
    public int getValorPlazo(){
    	if (this.plazo.endsWith("dh")){    		
    		return Integer.valueOf(plazo.substring(0,plazo.length()-2)).intValue();
    	}else if (this.plazo.endsWith("m")){
    		return Integer.valueOf(plazo.substring(0,plazo.length()-1)).intValue();
    	}else if (this.plazo.endsWith("a")){
    		return Integer.valueOf(plazo.substring(0,plazo.length()-1)).intValue();
    	}else{
    		return Integer.valueOf(plazo).intValue();
    	}
    }
}
