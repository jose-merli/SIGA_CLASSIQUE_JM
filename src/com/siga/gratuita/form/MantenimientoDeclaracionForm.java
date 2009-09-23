package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDeclaracionBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoDeclaracionForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdDeclaracion(String dato) {
		UtilidadesHash.set(this.datos, ScsDeclaracionBean.C_IDDECLARACION , dato);
	}
	public void setDescripcion(String dato) {
		UtilidadesHash.set(this.datos, ScsDeclaracionBean.C_DESCRIPCION , dato);
	}
	
	
	// METODOS GET
	public String getDescripcion() {
		return UtilidadesHash.getString(this.datos, ScsDeclaracionBean.C_DESCRIPCION);
	}
	public String getIdDeclaracion() {
		return UtilidadesHash.getString(this.datos, ScsDeclaracionBean.C_IDDECLARACION);
	}

	
}