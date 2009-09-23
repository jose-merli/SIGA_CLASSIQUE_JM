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

public class TiposProductosForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdTipoProducto (String id) {
 		UtilidadesHash.set(datos, PysTiposProductosBean.C_IDTIPOPRODUCTO, id);
 	}
 	
 	public void setTipoProducto (String dato) { 
 		UtilidadesHash.set(datos, "DESCRIPCIONPRODUCTO", dato);
 	}
 	
 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion	() {
 		return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public String getIdTipoProducto	() {
 		return UtilidadesHash.getString(datos, PysTiposProductosBean.C_IDTIPOPRODUCTO);	
 	}
 	
 	public String getTipoProducto	() 	{ 
 		return UtilidadesHash.getString(datos, "DESCRIPCIONPRODUCTO");
 	}
}
