package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDelitoBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoDelitoForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdDelito(String dato) {
		UtilidadesHash.set(this.datos, ScsDelitoBean.C_IDDELITO , dato);
	}
	public void setDescripcion(String dato) {
		UtilidadesHash.set(this.datos, ScsDelitoBean.C_DESCRIPCION , dato);
	}
	
	
	// METODOS GET
	public String getDescripcion() {
		return UtilidadesHash.getString(this.datos, ScsDelitoBean.C_DESCRIPCION);
	}
	public String getIdDelito() {
		return UtilidadesHash.getString(this.datos, ScsDelitoBean.C_IDDELITO);
	}

	
}