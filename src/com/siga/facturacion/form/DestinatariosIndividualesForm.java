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

public class DestinatariosIndividualesForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdPersona (Long id) {
 		UtilidadesHash.set(datos, CenPersonaBean.C_IDPERSONA, id);
 	}
 	
 	public void setNombre (String dato) { 
 		UtilidadesHash.set(datos, CenPersonaBean.C_NOMBRE, dato);
 	}
 	
 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion () {
 		return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public Long getIdPersona () {
 		return UtilidadesHash.getLong(datos, CenPersonaBean.C_IDPERSONA);
 	}
 	
 	public String getNombre	() 	{ 
 		return UtilidadesHash.getString(datos, CenPersonaBean.C_NOMBRE);
 	}
}
