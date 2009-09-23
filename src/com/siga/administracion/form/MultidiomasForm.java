
/*
 * Created on 26-sep-2007
 *
 */

package com.siga.administracion.form;

import com.siga.Utilidades.UtilidadesString;
import com.siga.general.MasterForm;

public class MultidiomasForm extends MasterForm 
{
	String descripcion = "", idIdioma = "", idIdiomaATraducir = "", datosModificados = "", esCatalogo = "";

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdIdioma() {
		return idIdioma;
	}
	public void setIdIdioma(String idIdioma) {
		this.idIdioma = idIdioma;
	}
	public String getIdIdiomaATraducir() {
		return idIdiomaATraducir;
	}
	public void setIdIdiomaATraducir(String idIdiomaATraducir) {
		this.idIdiomaATraducir = idIdiomaATraducir;
	}
	public String getDatosModificados() {
		return datosModificados;
	}
	public void setDatosModificados(String datosModificados) {
		this.datosModificados = datosModificados;
	}
	public boolean esCatalogo() {
		return UtilidadesString.stringToBoolean(esCatalogo);
	}
	public void setEsCatalogo(String esCatalogo) {
		this.esCatalogo = esCatalogo;
	}
}

