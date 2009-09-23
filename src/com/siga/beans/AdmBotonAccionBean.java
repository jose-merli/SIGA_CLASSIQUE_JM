package com.siga.beans;

public class AdmBotonAccionBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idBoton;
	private String descripcion;
	private String nombreBoton;
	private String transaccion;
	private String modo;
	private String parametro;
	private String valorParametro;
	private String activo;
	private String modal;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUION = "IDINSTITUCION";
	static public final String C_IDBOTON = "IDBOTON";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_NOMBREBOTON = "NOMBREBOTON";
	static public final String C_TRANSACCION = "TRANSACCION";
	static public final String C_MODO = "MODO";
	static public final String C_NOMBREPARAMETRO = "NOMBREPARAMETRO";
	static public final String C_VALORPARAMETRO = "VALORPARAMETRO";
	static public final String C_ACTIVO = "ACTIVO";
	static public final String C_MODAL = "MODAL";
	

	static public final String T_NOMBRETABLA = "ADM_BOTONACCION";
	
    public Integer getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer valor) 
    {
        this.idInstitucion = valor;
    }
    
    public Integer getidBoton() 
    {
        return idBoton;
    }
    
    public void setIdBoton(Integer valor) 
    {
        this.idBoton = valor;
    }
    
    public String getDescripcion() 
    {
        return descripcion;
    }
    
    public void setDescripcion(String valor) 
    {
        this.descripcion = valor;
    }
    
    public String getNombreBoton() 
    {
        return nombreBoton;
    }
    
    public void setNombreBoton(String valor) 
    {
        this.nombreBoton = valor;
    }
    
    public String getTransaccion() 
    {
        return transaccion;
    }
    
    public void setTransaccion(String valor) 
    {
        this.transaccion = valor;
    }
    
    public String getModo() 
    {
        return modo;
    }
    
    public void setModo(String valor) 
    {
        this.modo = valor;
    }
    
    public String getValorParametro() 
    {
        return valorParametro;
    }
    
    public void setValorParametro(String valor) 
    {
        this.valorParametro = valor;
    }
    
    public String getParametro() 
    {
        return parametro;
    }
    
    public void setParametro(String valor) 
    {
        this.parametro = valor;
    }
    
    public String getModal() 
    {
        return modal;
    }
    
    public void setModal(String valor) 
    {
        this.modal = valor;
    }
    
    public String getActivo() 
    {
        return activo;
    }
    
    public void setActivo(String valor) 
    {
        this.activo = valor;
    }
    
}
