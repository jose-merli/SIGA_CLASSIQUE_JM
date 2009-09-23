package com.siga.facturacionSJCS.form;

/*
 * Created on Jan 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResumenPagosForm extends MasterForm{

	
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, "IDPERSONA", dato);
	}
	
	public void setNumeroColegiado(String dato) {
		UtilidadesHash.set(this.datos, "NUMEROCOLEGIADO", dato);
	}
	
	public void setNombrePersona(String dato) {
		UtilidadesHash.set(this.datos, "NOMBREPERSONA", dato);
	}
	
	public void setNif(String dato) {
		UtilidadesHash.set(this.datos, "NIF", dato);
	}
	
	public void setFacturacion(String dato) {
		UtilidadesHash.set(this.datos, "FACTURACION", dato);
	}
	
	public void setPago(String dato) {
		UtilidadesHash.set(this.datos, "PAGO", dato);
	}
	
	
	
	public Long getIdPersona() {
		return UtilidadesHash.getLong(this.datos, "IDPERSONA");
	}
	
	public String getNumeroColegiado() {
		return UtilidadesHash.getString(this.datos, "NUMEROCOLEGIADO");
	}
		
	public String getNombrePersona() {
		return UtilidadesHash.getString(this.datos, "NOMBREPERSONA");
	}
	
	public String getNif() {
		return UtilidadesHash.getString(this.datos, "NIF");
	}
	
	public String getFacturacion() {
		return UtilidadesHash.getString(this.datos, "FACTURACION");
	}
	
	public String getPago() {
		return UtilidadesHash.getString(this.datos, "PAGO");
	}
		
	

}
