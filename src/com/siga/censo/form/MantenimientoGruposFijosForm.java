/*
 * Created on 01-sep-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenGruposClienteBean;
import com.siga.general.MasterForm;

import org.apache.struts.upload.FormFile;

/**
 * @author PDM
 * @since 01/09/2006
 * 
 */
public class MantenimientoGruposFijosForm extends MasterForm {

	private static final long serialVersionUID = -1461823896375945763L;
	private FormFile fichero;
	private String nombrefichero;
	private String directorio;
	private String busquedaNombre;

	// METODOS SET
	public void setIdInstitucion(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_IDINSTITUCION, dato);
	}

	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_NOMBRE, dato);
	}

	public void setIdGrupo(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_IDGRUPO, dato);
	}

	public void setFichero(FormFile fichero) {
		this.fichero = fichero;
	}

	// METODOS GET
	public String getIdInstitucion() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_IDINSTITUCION);
	}

	public String getNombre() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_NOMBRE);
	}

	public String getIdGrupo() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_IDGRUPO);
	}

	public FormFile getFichero() {
		return this.fichero;
	}

	public String getNombrefichero() {
		return nombrefichero;
	}

	public void setNombrefichero(String nombrefichero) {
		this.nombrefichero = nombrefichero;
	}

	public String getDirectorio() {
		return directorio;
	}

	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}

	public String getBusquedaNombre() {
		return busquedaNombre;
	}

	public void setBusquedaNombre(String busquedaNombre) {
		this.busquedaNombre = busquedaNombre;
	}

}
