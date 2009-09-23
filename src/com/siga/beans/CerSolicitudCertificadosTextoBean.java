package com.siga.beans;

public class CerSolicitudCertificadosTextoBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion, idTexto;
	private Long idSolicitud;
	private String texto;
	private String incluirSanciones;
    private String incluirDeudas;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDSOLICITUD = "IDSOLICITUD";
	static public final String C_IDTEXTO = "IDTEXTO";
	static public final String C_TEXTO = "TEXTO";
	static public final String C_INCLUIRSANCIONES = "INCLUIRSANCIONES";
	static public final String C_INCLUIRDEUDAS = "INCLUIRDEUDAS";
	
	static public final String T_NOMBRETABLA = "CER_SOLICITUDCERTIFICADOSTEXTO";
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public Long getIdSolicitud()
    {
        return idSolicitud;
    }
    
    public void setIdSolicitud(Long idSolicitud)
    {
        this.idSolicitud = idSolicitud;
    }

    public Integer getIdTexto()
    {
        return idTexto;
    }
    
    public void setIdTexto(Integer idTexto)
    {
        this.idTexto = idTexto;
    }

    public String getTexto()
    {
        return texto;
    }
    
    public void setTexto(String texto)
    {
        this.texto = texto;
    }

	public String getIncluirSanciones() {
		return incluirSanciones;
	}

	public void setIncluirSanciones(String incluirSanciones) {
		this.incluirSanciones = incluirSanciones;
	}

	public String getIncluirDeudas() {
		return incluirDeudas;
	}

	public void setIncluirDeudas(String incluirDeudas) {
		this.incluirDeudas = incluirDeudas;
	}


}