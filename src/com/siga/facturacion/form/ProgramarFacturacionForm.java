/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 07-03-2005 - Inicio
 */
package com.siga.facturacion.form;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProgramarFacturacionForm extends MasterForm{
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
	 * @param serieFacturacion The serieFacturacion to set.
	 */
	public void setSerieFacturacion(Long dato) {
		UtilidadesHash.set(this.datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, dato);		
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
	 * @return Returns the serieFacturacion.
	 */
	public Long getSerieFacturacion() {
		return UtilidadesHash.getLong(this.datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
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

	
}
