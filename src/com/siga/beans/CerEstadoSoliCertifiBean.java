package com.siga.beans;

public class CerEstadoSoliCertifiBean extends MasterBean
{
	/* Variables */
	private Integer idEstadoSolicitudCertificado;
	private String descripcion;

	/* Nombre campos de la tabla */
	static public final String C_IDESTADOSOLICITUDCERTIFICADO = "IDESTADOSOLICITUDCERTIFICADO";
	static public final String C_DESCRIPCION = "DESCRIPCION";

	static public final String T_NOMBRETABLA = "CER_ESTADOSOLICERTIFI";

    public Integer getIdEstadoSolicitudCertificado()
    {
        return idEstadoSolicitudCertificado;
    }

    public void setIdEstadoSolicitudCertificado(Integer idEstadoSolicitudCertificado)
    {
        this.idEstadoSolicitudCertificado = idEstadoSolicitudCertificado;
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