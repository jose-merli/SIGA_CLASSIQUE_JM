package com.siga.beans;

public class ConImpresoraBean extends MasterBean
{
	/* Variables */
	private Integer idImpresora;
	private Integer idInstitucion;
	private String nombreImpresora;
	
	/* Nombre campos de la tabla */
	static public final String C_IDIMPRESORA = "IDIMPRESORA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_NOMBREIMPRESORA = "NOMBREIMPRESORA";
	
	static public final String T_NOMBRETABLA = "CON_IMPRESORA";
	

    public Integer getIdImpresora() {
        return idImpresora;
    }
    public void setIdImpresora(Integer idImpresora) {
        this.idImpresora = idImpresora;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public String getNombreImpresora() {
        return nombreImpresora;
    }
    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }
}