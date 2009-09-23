/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;

public class TiposServiciosForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdTipoServicios (String id) {
 		UtilidadesHash.set(datos, PysTipoServiciosBean.C_IDTIPOSERVICIOS, id);
 	}
 	
 	public void setTipoServicios (String dato) {
 		UtilidadesHash.set(datos, "DESCRIPCIONSERVICIO", dato);
 	}
 	

 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion	() {
 		return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public String getIdTipoServicios	() {
 		return UtilidadesHash.getString(datos, PysTipoServiciosBean.C_IDTIPOSERVICIOS);	
 	}
 	
 	public String getTipoServicios	() 	{ 
 		return UtilidadesHash.getString(datos, "DESCRIPCIONSERVICIO"); 
 	}
}
