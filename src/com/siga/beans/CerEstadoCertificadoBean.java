package com.siga.beans;

public class CerEstadoCertificadoBean extends MasterBean
{
	/* Variables */
	private Integer idEstadoCertificado;
	private String descripcion;

	/* Nombre campos de la tabla */
	static public final String C_IDESTADOCERTIFICADO = "IDESTADOCERTIFICADO";
	static public final String C_DESCRIPCION = "DESCRIPCION";

	static public final String T_NOMBRETABLA = "CER_ESTADOCERTIFICADO";

    public Integer getIdEstadoCertificado()
    {
        return idEstadoCertificado;
    }

    public void setIdEstadoCertificado(Integer idEstadoCertificado)
    {
        this.idEstadoCertificado = idEstadoCertificado;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
}