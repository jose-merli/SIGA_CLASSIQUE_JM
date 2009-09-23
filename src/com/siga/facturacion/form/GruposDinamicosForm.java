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

public class GruposDinamicosForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdGruposCriterios (String id) {
 		UtilidadesHash.set(datos, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS, id);
 	}
 	
 	public void setGrupoClientesDinamico (String dato) { 
 		UtilidadesHash.set(datos, "NOMBREGRUPOCRITERIO", dato);
 	}
 	
	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion	() {
 		return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public  String getIdGruposCriterios	() {
 		return UtilidadesHash.getString(datos, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS);	
 	}
 	
 	public String getGrupoClientesDinamico	() 	{ 
 		return UtilidadesHash.getString(datos, "NOMBREGRUPOCRITERIO");  
 	}
}
