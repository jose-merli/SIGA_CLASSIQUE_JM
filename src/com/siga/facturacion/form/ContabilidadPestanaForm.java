
package com.siga.facturacion.form;

import com.siga.general.MasterForm;

public class ContabilidadPestanaForm extends MasterForm
{
	String 	cuentaClienteConfiguracion = "", 
			cuentaClienteGenerica = "", 
			cuentaIngresosConfiguracion = "", 
			cuentaIngresosGenerica = "";

	public String getCuentaClienteConfiguracion() {
		return cuentaClienteConfiguracion;
	}
	public String getCuentaClienteGenerica() {
		return cuentaClienteGenerica;
	}
	public String getCuentaIngresosConfiguracion() {
		return cuentaIngresosConfiguracion;
	}
	public String getCuentaIngresosGenerica() {
		return cuentaIngresosGenerica;
	}
	
	public void setCuentaClienteConfiguracion(String cuentaClienteConfiguracion) {
		this.cuentaClienteConfiguracion = cuentaClienteConfiguracion;
	}
	public void setCuentaClienteGenerica(String cuentaClienteGenerica) {
		this.cuentaClienteGenerica = cuentaClienteGenerica;
	}
	public void setCuentaIngresosConfiguracion(String cuentaIngresosConfiguracion) {
		this.cuentaIngresosConfiguracion = cuentaIngresosConfiguracion;
	}
	public void setCuentaIngresosGenerica(String cuentaIngresosGenerica) {
		this.cuentaIngresosGenerica = cuentaIngresosGenerica;
	}
}
