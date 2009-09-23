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

public class ComprobarPoblacionForm extends MasterForm{

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setNombreAbreviado (String dato) {
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO, dato.toUpperCase());
 	}
 	
 	public void setDescripcion (String dato) { 
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_DESCRIPCION, dato);
 	}
 	
 	public void setIdTipoProducto (Integer id) {
 		UtilidadesHash.set(datos, PysTiposProductosBean.C_IDTIPOPRODUCTO, id);
 	}
 	
 	public void setTipoProducto (String dato) { 
 		UtilidadesHash.set(datos, "DESCRIPCIONPRODUCTO", dato);
 	}
 	
 	public void setIdTipoServicios (Integer id) {
 		UtilidadesHash.set(datos, PysTipoServiciosBean.C_IDTIPOSERVICIOS, id);
 	}
 	
 	public void setTipoServicio (String dato) {
 		UtilidadesHash.set(datos, "DESCRIPCIONSERVICIO", dato);
 	}
 	
 	public void setIdGrupo (Integer id) {
 		UtilidadesHash.set(datos, CenGruposClienteBean.C_IDGRUPO, id);
 	}
 	
 	public void setGrupoClienteFijo (String dato) { 
 		UtilidadesHash.set(datos, "NOMBREGRUPOCLIENTE", dato);
 	}
 	
 	public void setIdGruposCriterios (Integer id) {
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
 	
 	public String getNombreAbreviado	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO);
 	}
 	
 	public String getDescripcion	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_DESCRIPCION);
 	}
 	
 	public Integer getIdTipoProducto	() {
 		return UtilidadesHash.getInteger(datos, PysTiposProductosBean.C_IDTIPOPRODUCTO);	
 	}
 	
 	public String getTipoProducto	() 	{ 
 		return UtilidadesHash.getString(datos, "DESCRIPCIONPRODUCTO");
 	}
 	
 	public Integer getIdTipoServicios	() {
 		return UtilidadesHash.getInteger(datos, PysTipoServiciosBean.C_IDTIPOSERVICIOS);	
 	}
 	
 	public String getTipoServicio	() 	{ 
 		return UtilidadesHash.getString(datos, "DESCRIPCIONSERVICIO"); 
 	}
 	
 	public Integer getIdGrupo	() {
 		return UtilidadesHash.getInteger(datos, CenGruposClienteBean.C_IDGRUPO);	
 	}
 	
 	public String getGrupoClienteFijo	() 	{ 
 		return UtilidadesHash.getString(datos, "NOMBREGRUPOCLIENTE"); 
 	}
 	
 	public Integer getIdGruposCriterios	() {
 		return UtilidadesHash.getInteger(datos, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS);	
 	}
 	
 	public String getGrupoClientesDinamico	() 	{ 
 		return UtilidadesHash.getString(datos, "NOMBREGRUPOCRITERIO");  
 	}
}
