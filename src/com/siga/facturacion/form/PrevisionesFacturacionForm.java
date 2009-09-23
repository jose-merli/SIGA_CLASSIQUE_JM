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

public class PrevisionesFacturacionForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdPrevision (Long id) { 
		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_IDPREVISION, id);
  	}
 	
 	public void setDescripcion (String dato) { 
 		UtilidadesHash.set(datos, "DESCRIPCIONSERIEFAC", dato);
 	}
 	
 	public void setFechaInicioProductos (String dato) { 
 		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS, dato);
 	}
 	
 	public void setFechaFinProductos (String dato) { 
 		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS, dato);
 	}
 	
 	public void setFechaInicioServicios (String dato) { 
 		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS, dato);
 	}
 	
 	public void setFechaFinServicios (String dato) { 
 		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS, dato);
 	}
 	
 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacPrevisionFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion	() {
 		return UtilidadesHash.getLong(datos, FacPrevisionFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public Long getIdPrevision	() {
 		return UtilidadesHash.getLong(datos, FacPrevisionFacturacionBean.C_IDPREVISION);
 	}
 	
 	public String getDescripcion	() 	{ 
 		return UtilidadesHash.getString(datos, "DESCRIPCIONSERIEFAC");
 	}
 	
 	public String getFechaInicioProductos	() 	{ 
 		return UtilidadesHash.getString(datos, FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS);
 	}
 	
 	public String getFechaFinProductos	() 	{ 
 		return UtilidadesHash.getString(datos, FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS);
 	}
 	
 	public String getFechaInicioServicios	() 	{ 
 		return UtilidadesHash.getString(datos, FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS);
 	}
 	
 	public String getFechaFinServicios	() 	{ 
 		return UtilidadesHash.getString(datos, FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS);
 	}
 	
 	public String getDescripcionPrevision () 	{ 
 		return UtilidadesHash.getString(datos, FacPrevisionFacturacionBean.C_DESCRIPCION);
 	}
 	public void setDescripcionPrevision (String s) 	{ 
 		UtilidadesHash.set(datos, FacPrevisionFacturacionBean.C_DESCRIPCION, s);
 	}

}
