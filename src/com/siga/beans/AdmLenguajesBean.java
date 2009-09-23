package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdmLenguajesBean extends MasterBean {
    
	//Variables
	private String idLenguaje;
	private String descripcion;
	private String codigoext;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDLENGUAJE = "IDLENGUAJE";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_CODIGOEXT = "CODIGOEXT";
	
	static public final String T_NOMBRETABLA = "ADM_LENGUAJES";
	

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getIdLenguaje() {
        return idLenguaje;
    }
    public void setIdLenguaje(String IdLenguaje) {
        this.idLenguaje = IdLenguaje;
    }
    public String getCodigoExt() {
        return codigoext;
    }
    public void setCodigoExt(String CodigoExt) {
        this.codigoext = CodigoExt;
    }
}
