/*
 * Created on 07-mar-2005
 *
 */
package com.siga.facturacion.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenPersonaBean;
import com.siga.general.MasterForm;

/**
 * @author daniel.campos
 *
 */
public class BusquedaFacturaForm extends MasterForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3831777869399250036L;
	private String incluirRegistrosConBajaLogica;
	
	
	public void setIdPersona (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDPERSONA, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}
	
	public void setBuscarNumeroFactura (String d) {
		UtilidadesHash.set(datos, "_NUMERO_FACTURA_", d);
 	}
	public void setBuscarFechaDesde (String d) {
		UtilidadesHash.set(datos, "_FECHA_DESDE_", d);
 	}
	public void setBuscarFechaHasta (String  d) {
		UtilidadesHash.set(datos, "_FECHA_HASTA_", d);
 	}
	public void setBuscarIdSerieFacturacion (Long d) {
		UtilidadesHash.set(datos, "_SERIE_FACTURACION_", d);
 	}
	public void setBuscarFechaGeneracion (String d) {
		UtilidadesHash.set(datos, "_FECHA_GENERACION_", d);
 	}
	public void setBuscarIdEstado (Integer d) {
		UtilidadesHash.set(datos, "_IDESTADO_", d);
 	}
	public void setBuscarFormaPago (String d) {
		UtilidadesHash.set(datos, "_FORMA_PAGO_", d);
 	}
	public void setBuscarConfirmada (String d) {
		UtilidadesHash.set(datos, "_CONFIRMADA_", d);
 	}
	public void setBuscarContabilizada (String d) {
		UtilidadesHash.set(datos, "_CONTABILIZADA_", d);
 	}

	public String getBuscarNumeroFactura() {
		return UtilidadesHash.getString(datos, "_NUMERO_FACTURA_");
 	}
	public String getBuscarFechaDesde () {
		return UtilidadesHash.getString(datos, "_FECHA_DESDE_");
 	}
	public String getBuscarFechaHasta () {
		return UtilidadesHash.getString(datos, "_FECHA_HASTA_");
 	}
	public Long getBuscarIdSerieFacturacion () {
		return UtilidadesHash.getLong(datos, "_SERIE_FACTURACION_");
 	}
	public String getBuscarFechaGeneracion () {
		return UtilidadesHash.getString(datos, "_FECHA_GENERACION_");
 	}
	public Integer getBuscarIdEstado () {
		return UtilidadesHash.getInteger(datos, "_IDESTADO_");
 	}
	public String getBuscarFormaPago () {
		return UtilidadesHash.getString(datos, "_FORMA_PAGO_");
 	}
	public String getBuscarConfirmada () {
		return UtilidadesHash.getString(datos, "_CONFIRMADA_");
 	}
	public String getBuscarContabilizada () {
		return UtilidadesHash.getString(datos, "_CONTABILIZADA_");
 	}
	
	public void setDeudor (String d) {
		UtilidadesHash.set(datos, "_DEUDOR_", d);
 	}
	public String getDeudor () {
		return UtilidadesHash.getString(datos, "_DEUDOR_");
 	}
	
	public void setTitular (String dato) {
		UtilidadesHash.set(datos, "Titular", dato);
 	}
	public String getTitular() {
 		return UtilidadesHash.getString(datos, "Titular");
 	}
 		
	public void setNombreTitular (String dato) {
		UtilidadesHash.set(datos, "NombreTitular", dato);
 	}
	public String getNombreTitular() {
 		return UtilidadesHash.getString(datos, "NombreTitular");
 	}
	
	public void setIdentificacionTitular (String dato) {
		UtilidadesHash.set(datos, "IdentificacionTitular", dato);
 	}
	public String getIdentificacionTitular() {
 		return UtilidadesHash.getString(datos, "IdentificacionTitular");
 	}			
	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
}


