/*
 * Created on Mar 15, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvEnviosBean extends MasterBean {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4142441368551326593L;
	//Variables
    private Integer idInstitucion;
	private Integer idEnvio;
	private String descripcion;
	private String fechaCreacion;
	private String fechaProgramada;
	private String generarDocumento;
	private String imprimirEtiquetas;
	private Integer idPlantillaEnvios;
	private Integer idPlantilla;
	private Integer idEstado;
	private Integer idImpresora;
	private Integer idTipoEnvios;
	private String consulta;
	private String acuseRecibo;
	private String idTipoIntercambioTelematico;
	private Long idEnvioProgramado;
	private Short comisionAJG;
	private String idSolicitdECOS;
	private String CSV;
	
	public Long getIdEnvioProgramado() {
		return idEnvioProgramado;
	}
	public void setIdEnvioProgramado(Long idEnvioProgramado) {
		this.idEnvioProgramado = idEnvioProgramado;
	}
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_DESCRIPCION="DESCRIPCION";
	static public final String C_FECHACREACION = "FECHA";	
	static public final String C_FECHAPROGRAMADA = "FECHAPROGRAMADA";	
	static public final String C_GENERARDOCUMENTO = "GENERARDOCUMENTO";
	static public final String C_IMPRIMIRETIQUETAS = "IMPRIMIRETIQUETAS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_IDESTADO = "IDESTADO";	
	static public final String C_IDIMPRESORA = "IDIMPRESORA";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_CONSULTA = "CONSULTA";
	static public final String C_ACUSERECIBO = "ACUSERECIBO";
	static public final String C_IDTIPOINTERCAMBIOTELEMATICO = "IDTIPOINTERCAMBIOTELEMATICO";
	static public final String C_COMISIONAJG = "COMISIONAJG";
	static public final String C_IDSOLICITUDECOS = "IDSOLICITUDECOS";
	static public final String C_CSV = "CSV";
	static public final String SEQ_ENV_ENVIOS = "SEQ_ENV_ENVIOS";
	static public final String SEQ_ENV_ENVIOS_MASIVOS = "SEQ_CER_ENVIOS";
	
	static public final String T_NOMBRETABLA = "ENV_ENVIOS";	

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getGenerarDocumento() {
        return generarDocumento;
    }
    public void setGenerarDocumento(String generarDocumento) {
        this.generarDocumento = generarDocumento;
    }
    public Integer getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }
    public Integer getIdEstado() {
        return idEstado;
    }
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
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
    public Integer getIdPlantilla() {
        return idPlantilla;
    }
    public void setIdPlantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }
    public Integer getIdPlantillaEnvios() {
        return idPlantillaEnvios;
    }
    public void setIdPlantillaEnvios(Integer idPlantillaEnvios) {
        this.idPlantillaEnvios = idPlantillaEnvios;
    }
    public Integer getIdTipoEnvios() {
        return idTipoEnvios;
    }
    public void setIdTipoEnvios(Integer idTipoEnvios) {
        this.idTipoEnvios = idTipoEnvios;
    }
    public String getImprimirEtiquetas() {
        return imprimirEtiquetas;
    }
    public void setImprimirEtiquetas(String imprimirEtiquetas) {
        this.imprimirEtiquetas = imprimirEtiquetas;
    }
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public String getFechaProgramada() {
        return fechaProgramada;
    }
    public void setFechaProgramada(String fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }
    public String getConsulta() {
        return consulta;
    }
    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }
	public String getAcuseRecibo() {
		return acuseRecibo;
	}
	public void setAcuseRecibo(String acuseRecibo) {
		this.acuseRecibo = acuseRecibo;
	}
	public String getIdTipoIntercambioTelematico() {
		return idTipoIntercambioTelematico;
	}
	public void setIdTipoIntercambioTelematico(String idTipoIntercambioTelematico) {
		this.idTipoIntercambioTelematico = idTipoIntercambioTelematico;
	}
	public Short getComisionAJG() {
		return comisionAJG;
	}
	public void setComisionAJG(Short comisionAJG) {
		this.comisionAJG = comisionAJG;
	}
	public String getIdSolicitdECOS() {
		return idSolicitdECOS;
	}
	public void setIdSolicitdECOS(String idSolicitdECOS) {
		this.idSolicitdECOS = idSolicitdECOS;
	}
	public String getCSV() {
		return CSV;
	}
	public void setCSV(String cSV) {
		CSV = cSV;
	}
}
