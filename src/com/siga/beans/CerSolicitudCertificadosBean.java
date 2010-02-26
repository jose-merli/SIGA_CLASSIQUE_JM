package com.siga.beans;

public class CerSolicitudCertificadosBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Long idSolicitud;
	private String descripcion;
	private String fechaSolicitud;
	private Integer idEstadoSolicitudCertificado;
	private Integer idInstitucion_Sol;
	private Long idPersona_Des;
	private Long idPersona_Dir;
	private Integer idDireccion_Dir;
	private Integer ppn_idTipoProducto;
	private Long ppn_idProductoInstitucion;
	private Long ppn_idProducto;
	private String fechaEmisionCertificado;
	private String fechaEstado;
	private Integer idEstadoCertificado;
	private Integer idTipoEnvios;
	private Integer idInstitucionOrigen;
	private Integer idInstitucionDestino;
	private String 	metodoSolicitud;
	private Long idPeticionProducto;

	private String contadorCer;
	private String sufijoCer;
	private String prefijoCer;
	private String fechaDescarga;
	private String fechaCobro;
	private String fechaEnvio;
	private String comentario;
	private String fechaEntregaInfo;


	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDSOLICITUD = "IDSOLICITUD";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_FECHASOLICITUD = "FECHASOLICITUD";
	static public final String C_FECHAESTADO = "FECHAESTADO";
	static public final String C_IDESTADOSOLICITUDCERTIFICADO = "IDESTADOSOLICITUDCERTIFICADO";
	static public final String C_IDINSTITUCION_SOL = "IDINSTITUCION_SOL";
	static public final String C_IDPERSONA_DES = "IDPERSONA_DES";
	static public final String C_IDPERSONA_DIR = "IDPERSONA_DIR";
	static public final String C_IDDIRECCION_DIR = "IDDIRECCION_DIR";
	static public final String C_PPN_IDTIPOPRODUCTO = "PPN_IDTIPOPRODUCTO";
	static public final String C_PPN_IDPRODUCTOINSTITUCION = "PPN_IDPRODUCTOINSTITUCION";
	static public final String C_PPN_IDPRODUCTO = "PPN_IDPRODUCTO";
	static public final String C_FECHAEMISIONCERTIFICADO = "FECHAEMISIONCERTIFICADO";
	static public final String C_IDESTADOCERTIFICADO = "IDESTADOCERTIFICADO";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDINSTITUCIONORIGEN = "IDINSTITUCIONORIGEN";
	static public final String C_IDINSTITUCIONDESTINO = "IDINSTITUCIONDESTINO";
	static public final String C_IDPETICIONPRODUCTO = "IDPETICIONPRODUCTO";

	static public final String C_CONTADOR_CER = "CONTADOR_CER";
	static public final String C_SUFIJO_CER = "SUFIJO_CER";
	static public final String C_PREFIJO_CER = "PREFIJO_CER";

	static public final String C_FECHADESCARGA = "FECHADESCARGA";
	static public final String C_FECHACOBRO = "FECHACOBRO";
	static public final String C_FECHAENVIO = "FECHAENVIO";
	static public final String C_COMENTARIO = "COMENTARIO";
	static public final String C_FECHAENTREGAINFO = "FECHAENTREGAINFO";
	static public final String C_IDMETODOSOLICITUD = "IDMETODOSOLICITUD";

	static public final String T_NOMBRETABLA = "CER_SOLICITUDCERTIFICADOS";

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

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getFechaSolicitud()
    {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud)
    {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public String getComentario()
    {
        return comentario;
    }

    public void setComentario(String comentario)
    {
        this.comentario = comentario;
    }


    public Integer getIdEstadoSolicitudCertificado()
    {
        return idEstadoSolicitudCertificado;
    }

    public void setIdEstadoSolicitudCertificado(Integer idEstadoSolicitudCertificado)
    {
        this.idEstadoSolicitudCertificado = idEstadoSolicitudCertificado;
    }

    public Integer getIdInstitucion_Sol()
    {
        return idInstitucion_Sol;
    }

    public void setIdInstitucion_Sol(Integer idInstitucion_Sol)
    {
        this.idInstitucion_Sol = idInstitucion_Sol;
    }

    public Long getIdPersona_Des()
    {
        return idPersona_Des;
    }

    public void setIdPersona_Des(Long idPersona_Des)
    {
        this.idPersona_Des = idPersona_Des;
    }

    public Long getIdPersona_Dir()
    {
        return idPersona_Dir;
    }

    public void setIdPersona_Dir(Long idPersona_Dir)
    {
        this.idPersona_Dir = idPersona_Dir;
    }

    public Integer getIdDireccion_Dir()
    {
        return idDireccion_Dir;
    }

    public void setIdDireccion_Dir(Integer idDireccion_Dir)
    {
        this.idDireccion_Dir = idDireccion_Dir;
    }

    public Integer getPpn_IdTipoProducto()
    {
        return ppn_idTipoProducto;
    }

    public void setPpn_IdTipoProducto(Integer ppn_idTipoProducto)
    {
        this.ppn_idTipoProducto = ppn_idTipoProducto;
    }

    public Long getPpn_IdProductoInstitucion()
    {
        return ppn_idProductoInstitucion;
    }

    public void setPpn_IdProductoInstitucion(Long ppn_idProductoInstitucion)
    {
        this.ppn_idProductoInstitucion = ppn_idProductoInstitucion;
    }

    public Long getPpn_IdProducto()
    {
        return ppn_idProducto;
    }

    public void setPpn_IdProducto(Long ppn_idProducto)
    {
        this.ppn_idProducto = ppn_idProducto;
    }

    public String getFechaEmisionCertificado()
    {
        return fechaEmisionCertificado;
    }

    public void setFechaEmisionCertificado(String fechaEmisionCertificado)
    {
        this.fechaEmisionCertificado = fechaEmisionCertificado;
    }

    public Integer getIdEstadoCertificado()
    {
        return idEstadoCertificado;
    }

    public void setIdEstadoCertificado(Integer idEstadoCertificado)
    {
        this.idEstadoCertificado = idEstadoCertificado;
    }

    public Integer getIdTipoEnvios()
    {
        return idTipoEnvios;
    }

    public void setIdTipoEnvios(Integer idTipoEnvios)
    {
        this.idTipoEnvios = idTipoEnvios;
    }
    public Integer getIdInstitucionDestino() {
        return idInstitucionDestino;
    }
    public void setIdInstitucionDestino(Integer idInstitucionDestino) {
        this.idInstitucionDestino = idInstitucionDestino;
    }
    public Integer getIdInstitucionOrigen() {
        return idInstitucionOrigen;
    }
    public void setIdInstitucionOrigen(Integer idInstitucionOrigen) {
        this.idInstitucionOrigen = idInstitucionOrigen;
    }
    public Long getIdPeticionProducto() {
        return idPeticionProducto;
    }
    public void setIdPeticionProducto(Long idPeticionProducto) {
        this.idPeticionProducto = idPeticionProducto;
    }

    public String getContadorCer() {
        return contadorCer;
    }
    public void setContadorCer(String ContadorCer) {
        this.contadorCer = ContadorCer;
    }
    public String getSufijoCer() {
        return sufijoCer;
    }
    public void setSufijoCer(String SufijoCer) {
        this.sufijoCer = SufijoCer;
    }
    public String getPrefijoCer() {
        return prefijoCer;
    }
    public void setPrefijoCer(String PrefijoCer) {
        this.prefijoCer = PrefijoCer;
    }
    public String getFechaEstado() {
        return fechaEstado;
    }
    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }


    public String getFechaDescarga()
    {
        return fechaDescarga;
    }

    public void setFechaDescarga(String fechaDescarga)
    {
        this.fechaDescarga = fechaDescarga;
    }
    public String getFechaCobro()
    {
        return fechaCobro;
    }

    public void setFechaCobro(String fechaCobro)
    {
        this.fechaCobro = fechaCobro;
    }
    public String getFechaEnvio()
    {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio)
    {
        this.fechaEnvio = fechaEnvio;
    }
    
	public String getFechaEntregaInfo() {
		return fechaEntregaInfo;
	}
	public void setFechaEntregaInfo(String fechaFechaEntregaInfo) {
		this.fechaEntregaInfo = fechaFechaEntregaInfo;
	}
	public String getMetodoSolicitud() {
		return metodoSolicitud;
	}
	public void setMetodoSolicitud(String metodoSolicitud) {
		this.metodoSolicitud = metodoSolicitud;
	}
}