/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */

package com.siga.facturacion.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class InformesFacturasEmitidasForm extends MasterForm
{
	public String getFechaDesde() {
		return UtilidadesHash.getString(this.datos, "FECHA_DESDE");
	}
	public String getFechaHasta() {
		return UtilidadesHash.getString(this.datos, "FECHA_HASTA");
	}
	public void setFechaDesde(String fechaDesde) {
		UtilidadesHash.set(this.datos, "FECHA_DESDE", fechaDesde);
	}
	public void setFechaHasta(String fechaHasta) {
		UtilidadesHash.set(this.datos, "FECHA_HASTA", fechaHasta);
	}
}
