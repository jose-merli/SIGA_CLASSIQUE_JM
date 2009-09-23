package com.siga.beans;

public class CerCamposCertificadosBean extends MasterBean
{
	/* Variables */
	private Integer idCampoCertificado;
	private String tipoCampo;
	private String nombre;
	private String nombreSalida;
	private String capturarDatos;

	/* Nombre campos de la tabla */
	static public final String C_IDCAMPOCERTIFICADO = "IDCAMPOCERTIFICADO";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_NOMBRESALIDA = "NOMBRESALIDA";
	static public final String C_CAPTURARDATOS = "CAPTURARDATOS";
	
	static public final String T_NOMBRETABLA = "CER_CAMPOSCERTIFICADOS";
	
	// Métodos SET
    public Integer getIdCampoCertificado()
    {
        return idCampoCertificado;
    }
    
    public void setIdCampoCertificado(Integer idCampoCertificado)
    {
        this.idCampoCertificado = idCampoCertificado;
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