package com.siga.beans;

public class EnvCamposBean extends MasterBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7862736172512571894L;
	/* Variables */
	private Integer idCampo;
	private String tipoCampo;
	private String nombre;
	private String nombreSalida;
	private String capturarDatos;

	/* Nombre campos de la tabla */
	static public final String C_IDCAMPO = "IDCAMPO";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_NOMBRESALIDA = "NOMBRESALIDA";
	static public final String C_CAPTURARDATOS = "CAPTURARDATOS";
	
	static public final String T_NOMBRETABLA = "ENV_CAMPOS";
	
	// M�todos SET
    public Integer getIdCampo()
    {
        return idCampo;
    }
    
    public void setIdCampo(Integer idCampo)
    {
        this.idCampo = idCampo;
    }
    
    public String getTipoCampo()
    {
        return tipoCampo;
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        this.tipoCampo = tipoCampo;
    }

    public String getNombre()
    {
        return nombre;
    }
    
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNombreSalida()
    {
        return nombreSalida;
    }
    
    public void setNombreSalida(String nombreSalida)
    {
        this.nombreSalida = nombreSalida;
    }

    public String getCapturarDatos()
    {
        return capturarDatos;
    }
    
    public void setCapturarDatos(String capturarDatos)
    {
        this.capturarDatos = capturarDatos;
    }
}