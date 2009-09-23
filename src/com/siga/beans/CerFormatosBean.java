package com.siga.beans;

public class CerFormatosBean extends MasterBean
{
	/* Variables */
	private Integer idFormato;
	private String descripcion;
	private String tipoCampo;
	private String formato;
	
	/* Nombre campos de la tabla */
	static public final String C_IDFORMATO = "IDFORMATO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_FORMATO = "FORMATO";
	
	static public final String T_NOMBRETABLA = "CER_FORMATOS";
	
	// Métodos SET
    public Integer getIdFormato()
    {
        return idFormato;
    }
    
    public void setIdFormato(Integer idFormato)
    {
        this.idFormato = idFormato;
    }
    
    public String getDescripcion()
    {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public String getTipoCampo()
    {
        return tipoCampo;
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        this.tipoCampo = tipoCampo;
    }
    public String getFormato() {
        return formato;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
}