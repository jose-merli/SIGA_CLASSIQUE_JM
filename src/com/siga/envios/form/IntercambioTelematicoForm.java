package com.siga.envios.form;

import com.siga.general.MasterForm;

public class IntercambioTelematicoForm extends MasterForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2926799333670533100L;
	private String idIntercambio;
    
    private String idEstado;
    private String estadoComunicacion;
    
    private String tipoComunicacion;
    private String idEnvio;
    private String idInstitucion;
    private String idEcomCola;
    
    private String idPlantilla;
    private String idInstitucionPlantilla;
    
    private String xml;
    private String logError;
    private String plantilla;
    
    private String	fechaPeticion;
    private String	fechaRespuesta;
    private String	idAcuse;
    
    public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public String getIdAcuse() {
		return idAcuse;
	}

	public void setIdAcuse(String idAcuse) {
		this.idAcuse = idAcuse;
	}

	private String envioTipo;
    private String envioNombre;

    DesignaProvisionalForm designaProvisional;
	SolSusProcedimientoForm solSusProcedimiento;

	

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	

	

	public String getIdEnvio() {
		return idEnvio;
	}

	public void setIdEnvio(String idEnvio) {
		this.idEnvio = idEnvio;
	}

	public String getIdEcomCola() {
		return idEcomCola;
	}

	public void setIdEcomCola(String idEcomCola) {
		this.idEcomCola = idEcomCola;
	}

	public String getIdPlantilla() {
		return idPlantilla;
	}

	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	public String getIdInstitucionPlantilla() {
		return idInstitucionPlantilla;
	}

	public void setIdInstitucionPlantilla(String idInstitucionPlantilla) {
		this.idInstitucionPlantilla = idInstitucionPlantilla;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getLogError() {
		return logError;
	}

	public void setLogError(String logError) {
		this.logError = logError;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getFechaPeticion() {
		return fechaPeticion;
	}

	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}

	public String getEnvioTipo() {
		return envioTipo;
	}

	public void setEnvioTipo(String envioTipo) {
		this.envioTipo = envioTipo;
	}

	public String getEnvioNombre() {
		return envioNombre;
	}

	public void setEnvioNombre(String envioNombre) {
		this.envioNombre = envioNombre;
	}

	public String getEstadoComunicacion() {
		return estadoComunicacion;
	}

	public void setEstadoComunicacion(String estadoComunicacion) {
		this.estadoComunicacion = estadoComunicacion;
	}

	public String getTipoComunicacion() {
		return tipoComunicacion;
	}

	public void setTipoComunicacion(String tipoComunicacion) {
		this.tipoComunicacion = tipoComunicacion;
	}

	public DesignaProvisionalForm getDesignaProvisional() {
		return designaProvisional;
	}

	public void setDesignaProvisional(DesignaProvisionalForm designaProvisional) {
		this.designaProvisional = designaProvisional;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdIntercambio() {
		return idIntercambio;
	}

	public void setIdIntercambio(String idIntercambio) {
		this.idIntercambio = idIntercambio;
	}

	public SolSusProcedimientoForm getSolSusProcedimiento() {
		return solSusProcedimiento;
	}

	public void setSolSusProcedimiento(SolSusProcedimientoForm solSusProcedimiento) {
		this.solSusProcedimiento = solSusProcedimiento;
	}

	

	

	
	
}