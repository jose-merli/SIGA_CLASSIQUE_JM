/**
 * Form que representa los campos de la ventana de busqueda modal de persona JG  
 * @author AtosOrigin SAE - S233735 
 * @since 31-04-2006
 */
package com.siga.gratuita.form;

import java.util.List;


import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;


public class BusquedaPersonaJGForm extends MasterForm {
			
	// Datos de entrada de la ventana
	public void setNIdentificacion(String dato) {
		UtilidadesHash.set(this.datos,"NIdentificacion",dato);
	}
	public String getNIdentificacion() {
		return UtilidadesHash.getString(this.datos,"NIdentificacion");
	}
	
	public void setIdInstitucion(String dato) {
		UtilidadesHash.set(this.datos,"IdInstitucion",dato);
	}
	public String getIdInstitucion() {
		return UtilidadesHash.getString(this.datos,"IdInstitucion");
	}
	
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos,"Nombre",dato);
	}
	public String getNombre() {
		return UtilidadesHash.getString(this.datos,"Nombre");
	}
	
	public void setApellido1(String dato) {
		UtilidadesHash.set(this.datos,"Apellido1",dato);
	}
	public String getApellido1() {
		return UtilidadesHash.getString(this.datos,"Apellido1");
	}
	
	public void setApellido2(String dato) {
		UtilidadesHash.set(this.datos,"Apellido2",dato);
	}
	public String getApellido2() {
		return UtilidadesHash.getString(this.datos,"Apellido2");
	}
	
	public void setConceptoE(String dato) {
		UtilidadesHash.set(this.datos,"ConceptoE",dato);
	}
	public String getConceptoE() {
		return UtilidadesHash.getString(this.datos,"ConceptoE");
	}
	
	List<ScsTelefonosPersonaJGBean> telefonos; 
	public List<ScsTelefonosPersonaJGBean> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<ScsTelefonosPersonaJGBean> telefonos) {
		this.telefonos = telefonos;
	}

	
	
	
}
