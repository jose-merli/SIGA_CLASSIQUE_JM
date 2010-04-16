package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;

/**
 * Maneja el formulario que mantiene la tabla SCS_EJGFORM
 */
public class BusquedaCAJG_EJGForm extends DefinirEJGForm {

	public void setSelDefinitivo(String dato) {
		UtilidadesHash.set(this.datos, "SELDEFINITIVO", dato);
	}

	public String getSelDefinitivo() {
		return UtilidadesHash.getString(this.datos, "SELDEFINITIVO");
	}

}