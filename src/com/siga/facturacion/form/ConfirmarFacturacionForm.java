/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 14-03-2005 - Inicio
 */
package com.siga.facturacion.form;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfirmarFacturacionForm extends MasterForm{
	private String fechaCargo, estadoConfirmacion, estadoPDF, estadoEnvios, archivadas;
	private String facturacionRapida, idSerieFacturacion, idProgramacion;
	private String fechaEntrega="", fechaFRST="", fechaRCUR="", fechaCOR1="", fechaB2B="";
	private String fechaDesdeConfirmacion,fechaHastaConfirmacion,fechaDesdeGeneracion,fechaHastaGeneracion,fechaDesdePrevistaGeneracion,fechaHastaPrevistaGeneracion;
	private String idTipoPlantillaMail;
	private String fechaPresentacion;
	private String fechaRecibosPrimeros;
	private String fechaRecibosRecurrentes;
	private String fechaRecibosCOR1;
	private String fechaRecibosB2B;
	
	/**
	 * @return the fechaDesdeConfirmacion
	 */
	public String getFechaDesdeConfirmacion() {
		return fechaDesdeConfirmacion;
	}
	/**
	 * @param fechaDesdeConfirmacion the fechaDesdeConfirmacion to set
	 */
	public void setFechaDesdeConfirmacion(String fechaDesdeConfirmacion) {
		this.fechaDesdeConfirmacion = fechaDesdeConfirmacion;
	}
	/**
	 * @return the fechaHastaConfirmacion
	 */
	public String getFechaHastaConfirmacion() {
		return fechaHastaConfirmacion;
	}
	/**
	 * @param fechaHastaConfirmacion the fechaHastaConfirmacion to set
	 */
	public void setFechaHastaConfirmacion(String fechaHastaConfirmacion) {
		this.fechaHastaConfirmacion = fechaHastaConfirmacion;
	}
	/**
	 * @return the fechaDesdeGeneracion
	 */
	public String getFechaDesdeGeneracion() {
		return fechaDesdeGeneracion;
	}
	/**
	 * @param fechaDesdeGeneracion the fechaDesdeGeneracion to set
	 */
	public void setFechaDesdeGeneracion(String fechaDesdeGeneracion) {
		this.fechaDesdeGeneracion = fechaDesdeGeneracion;
	}
	/**
	 * @return the fechaHastaGeneracion
	 */
	public String getFechaHastaGeneracion() {
		return fechaHastaGeneracion;
	}
	/**
	 * @param fechaHastaGeneracion the fechaHastaGeneracion to set
	 */
	public void setFechaHastaGeneracion(String fechaHastaGeneracion) {
		this.fechaHastaGeneracion = fechaHastaGeneracion;
	}
	public String getFechaCargo() {
		return fechaCargo;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	
	public void setEstadoConfirmacion(String dato) {
		this.estadoConfirmacion = dato;
	}
	public void setEstadoPDF(String dato) {
		this.estadoPDF = dato;
	}
	public void setEstadoEnvios(String dato) {
		this.estadoEnvios = dato;
	}
	public void setArchivadas(String dato) {
		this.archivadas = dato;
	}

	public String getEstadoConfirmacion() {
		return estadoConfirmacion;
	}
	public String getEstadoPDF() {
		return estadoPDF;
	}
	public String getEstadoEnvios() {
		return estadoEnvios;
	}
	public String getArchivadas() {
		return archivadas;
	}
	public String getFacturacionRapida() {
		return facturacionRapida;
	}
	public void setFacturacionRapida(String facturacionRapida) {
		this.facturacionRapida = facturacionRapida;
	}
	public String getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(String idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	public String getIdProgramacion() {
		return idProgramacion;
	}
	public void setIdProgramacion(String idProgramacion) {
		this.idProgramacion = idProgramacion;
	}
	public String getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public String getFechaFRST() {
		return fechaFRST;
	}
	public void setFechaFRST(String fechaFRST) {
		this.fechaFRST = fechaFRST;
	}
	public String getFechaRCUR() {
		return fechaRCUR;
	}
	public void setFechaRCUR(String fechaRCUR) {
		this.fechaRCUR = fechaRCUR;
	}
	public String getFechaCOR1() {
		return fechaCOR1;
	}
	public void setFechaCOR1(String fechaCOR1) {
		this.fechaCOR1 = fechaCOR1;
	}
	public String getFechaB2B() {
		return fechaB2B;
	}
	public void setFechaB2B(String fechaB2B) {
		this.fechaB2B = fechaB2B;
	}
	
	/**
	 * @param finalProducto The fFinalProducto to set.
	 */
	public void setFechaFinalProducto(String dato) {
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}		
	}
	/**
	 * @param finalServicio The fFinalServicio to set.
	 */
	public void setFechaFinalServicio(String dato) {		
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	/**
	 * @param inicialProducto The fInicialProducto to set.
	 */
	public void setFechaInicialProducto(String dato) {		
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	/**
	 * @param inicialServicio The fInicialServicio to set.
	 */
	public void setFechaInicialServicio(String dato) {		
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	/**
	 * @param previstaGeneracion The fPrevistaGeneracion to set.
	 */
	public void setFechaPrevistaGeneracion(String dato) {		
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	/**
	 * @param 
	 */
	public void setFechaPrevistaConfirmacion(String dato) {		
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	/**
	 * @param programacion The fProgramacion to set.
	 */
	public void setFechaProgramacion(String dato) {				
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}

	/**
	 * @param 
	 */
	public void setHorasConfirmacion(String dato) {				
		try {
			this.datos.put("horasConfirmacion", dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}

	/**
	 * @param 
	 */
	public void setHorasGeneracion(String dato) {				
		try {
			this.datos.put("horasGeneracion", dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}

	/**
	 * @param 
	 */
	public void setMinutosConfirmacion(String dato) {				
		try {
			this.datos.put("minutosConfirmacion", dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}



	/**
	 * @param 
	 */
	public void setMinutosGeneracion(String dato) {				
		try {
			this.datos.put("minutosGeneracion", dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}
	
	
	
	/**
	 * @param 
	 */
	public void setGenerarPDF(String dato) {				
		try {
			this.datos.put(FacFacturacionProgramadaBean.C_GENERAPDF, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}	

	/**
	 * @param 
	 */
	public void setEnviarFacturas(String dato) {				
		try {
			this.datos.put(FacFacturacionProgramadaBean.C_ENVIO, dato);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}	
	}	

		
	/**
	 * @return Returns the fFinalProducto.
	 */
	public String getFechaFinalProducto() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS);
	}
	/**
	 * @return Returns the fFinalServicio.
	 */
	public String getFechaFinalServicio() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS);
	}
	/**
	 * @return Returns the fInicialProducto.
	 */
	public String getFechaInicialProducto() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS);
	}
	/**
	 * @return Returns the fInicialServicio.
	 */
	public String getFechaInicialServicio() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS);
	}
	/**
	 * @return Returns the fPrevistaGeneracion.
	 */
	public String getFechaPrevistaGeneracion() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION);
	}
	/**
	 * @return Returns the fPrevistaConfirmacion.
	 */
	public String getFechaPrevistaConfirmacion() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM);
	}
	/**
	 * @return Returns the fProgramacion.
	 */
	public String getFechaProgramacion() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION);
	}
	
	/**
	 * @return 
	 */
	public String getHorasGeneracion() {
		return UtilidadesHash.getString(this.datos, "horasGeneracion");
	}
	
	/**
	 * @return 
	 */
	public String getHorasConfirmacion() {
		return UtilidadesHash.getString(this.datos, "horasConfirmacion");
	}
	
	/**
	 * @return 
	 */
	public String getMinutosGeneracion() {
		return UtilidadesHash.getString(this.datos, "minutosGeneracion");
	}
	
	/**
	 * @return 
	 */
	public String getMinutosConfirmacion() {
		return UtilidadesHash.getString(this.datos, "minutosConfirmacion");
	}
	
	/**
	 * @return 
	 */
	public String getGenerarPDF() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_GENERAPDF);
	}
	
	
	/**
	 * @return 
	 */
	public String getEnviarFacturas() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_ENVIO);
	}
	

	public String getDescripcionProgramacion() {
		return UtilidadesHash.getString(this.datos, FacFacturacionProgramadaBean.C_DESCRIPCION);
	}
	public void setDescripcionProgramacion(String s) {
		UtilidadesHash.set(this.datos, FacFacturacionProgramadaBean.C_DESCRIPCION, s);
	}

	
	
	public String getIdTipoPlantillaMail() {
		return idTipoPlantillaMail;
	}

	public void setIdTipoPlantillaMail(String idTipoPlantillaMail) {
		this.idTipoPlantillaMail = idTipoPlantillaMail;
	}
	public String getFechaPresentacion() {
		return fechaPresentacion;
	}
	public void setFechaPresentacion(String fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}
	public String getFechaRecibosPrimeros() {
		return fechaRecibosPrimeros;
	}
	public void setFechaRecibosPrimeros(String fechaRecibosPrimeros) {
		this.fechaRecibosPrimeros = fechaRecibosPrimeros;
	}
	public String getFechaRecibosRecurrentes() {
		return fechaRecibosRecurrentes;
	}
	public void setFechaRecibosRecurrentes(String fechaRecibosRecurrentes) {
		this.fechaRecibosRecurrentes = fechaRecibosRecurrentes;
	}
	public String getFechaRecibosCOR1() {
		return fechaRecibosCOR1;
	}
	public void setFechaRecibosCOR1(String fechaRecibosCOR1) {
		this.fechaRecibosCOR1 = fechaRecibosCOR1;
	}
	public String getFechaRecibosB2B() {
		return fechaRecibosB2B;
	}
	public void setFechaRecibosB2B(String fechaRecibosB2B) {
		this.fechaRecibosB2B = fechaRecibosB2B;
	}
	public String getFechaDesdePrevistaGeneracion() {
		return fechaDesdePrevistaGeneracion;
	}
	public void setFechaDesdePrevistaGeneracion(String fechaDesdePrevistaGeneracion) {
		this.fechaDesdePrevistaGeneracion = fechaDesdePrevistaGeneracion;
	}
	public String getFechaHastaPrevistaGeneracion() {
		return fechaHastaPrevistaGeneracion;
	}
	public void setFechaHastaPrevistaGeneracion(String fechaHastaPrevistaGeneracion) {
		this.fechaHastaPrevistaGeneracion = fechaHastaPrevistaGeneracion;
	}

}