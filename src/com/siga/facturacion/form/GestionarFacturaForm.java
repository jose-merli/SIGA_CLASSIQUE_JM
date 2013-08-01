/*
 * Created on 15-mar-2005
 *
 */
package com.siga.facturacion.form;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author daniel.campos
 *
 */
public class GestionarFacturaForm extends MasterForm{
	private String datosFacturas;
	private String idCuentaUnica;
	
	public String getDatosFacturas() {
		return datosFacturas;
	}
	public void setDatosFacturas(String datosFacturas) {
		this.datosFacturas = datosFacturas;
	}
	public void setIdInstitucion (Integer a) {
		UtilidadesHash.set(this.datos,"_IDINSTITUCION_", a);
	}
	public void setIdFactura (String a) {
		UtilidadesHash.set(this.datos,"_IDFACTURA_", a);
	}
	public void setNumeroFactura (String a) {
		UtilidadesHash.set(this.datos,"_NUMEROFACTURA_", a);
	}
	public void setTarjeta (String a) {
		UtilidadesHash.set(this.datos,"_TARJETA_", a);
	}
	public void setCaducidad (String a) {
		UtilidadesHash.set(this.datos,"_CADUCIDAD_", a);
	}
	public void setIdPagoPorCaja (String a) {
		UtilidadesHash.set(this.datos,"_IDPAGOPORCAJA_", a);
	}
	public void setFecha (String a) {
		UtilidadesHash.set(this.datos,"_FECHA_", a);
	}

	public Integer getIdInstitucion () {
		return UtilidadesHash.getInteger(this.datos,"_IDINSTITUCION_");
	}
	public String getIdFactura () {
		return UtilidadesHash.getString(this.datos,"_IDFACTURA_");
	}
	public String getNumeroFactura () {
		return UtilidadesHash.getString(this.datos,"_NUMEROFACTURA_");
	}
	public String getTarjeta () {
		return UtilidadesHash.getString(this.datos,"_TARJETA_");
	}
	public String getCaducidad () {
		return UtilidadesHash.getString(this.datos,"_CADUCIDAD_");
	}
	public String getIdPagoPorCaja () {
		return UtilidadesHash.getString(this.datos,"_IDPAGOPORCAJA_");
	}
	public String getFecha () {
		return UtilidadesHash.getString(this.datos,"_FECHA_");
	}
	
	
	// DATOS GENERALES ///////////////////////////////////////////////////////
	public void setDatosGeneralesObservaciones (String a) {
		UtilidadesHash.set(this.datos,"_datosGenerales_OBSERVACIONES_", a);		
	}
	public String getDatosGeneralesObservaciones () {
		return UtilidadesHash.getString(this.datos,"_datosGenerales_OBSERVACIONES_");		
	}
	public void setDatosGeneralesObservinforme (String a) {
		UtilidadesHash.set(this.datos,"_datosGenerales_OBSERVINFORME_", a);		
	}
	public String getDatosGeneralesObservinforme () {
		return UtilidadesHash.getString(this.datos,"_datosGenerales_OBSERVINFORME_");		
	}

	
	// PAGOS /////////////////////////////////////////////////////////////////
	// Caja
		public void setDatosPagosCajaFecha (String a) {
			try {
				a = GstDate.getApplicationFormatDate("", a);
				UtilidadesHash.set(this.datos,"_pagosCaja_FECHA_", a);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void setDatosPagosCajaImporteCobrado (Double a) {
			UtilidadesHash.set(this.datos,"_pagosCaja_IMPORTECOBRADO_", a);
		}
		public void setDatosPagosCajaImportePendiente (Double a) {
			UtilidadesHash.set(this.datos,"_pagosCaja_IMPORTEPENDIENTE_", a);
		}
		public void setDatosPagosCajaObservaciones (String a) {
			UtilidadesHash.set(this.datos,"_pagosCaja_OBSERVACIONES_", a);		
		}
		public String getDatosPagosCajaFecha () {
			return UtilidadesHash.getString(this.datos,"_pagosCaja_FECHA_");		
		}
		public Double getDatosPagosCajaImporteCobrado () {
			return UtilidadesHash.getDouble(this.datos,"_pagosCaja_IMPORTECOBRADO_");
		}
		public Double getDatosPagosCajaImportePendiente () {
			return UtilidadesHash.getDouble(this.datos,"_pagosCaja_IMPORTEPENDIENTE_");
		}
		public String getDatosPagosCajaObservaciones () {
			return UtilidadesHash.getString(this.datos,"_pagosCaja_OBSERVACIONES_");		
		}
		
	// Renegociar
		public void setDatosPagosRenegociarEstadoFactura (Integer a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_ESTADOFACTURA_", a);		
		}
		public void setDatosPagosRenegociarDevuelta (Integer a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_DEVUELTA_", a);		
		}		
		public void setDatosPagosRenegociarImportePendiente (Double a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_IMPORTEPENDIENTE_", a);		
		}
		public void setDatosPagosRenegociarNuevaFormaPago (String a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_NUEVAFORMAPAGO_", a);		
		}
		public void  setDatosPagosRenegociarIdCuenta (Integer a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_IDCUENTA_", a);		
		}
		public void setDatosPagosRenegociarObservaciones (String a) {
			UtilidadesHash.set(this.datos,"_pagosRenegociar_OBSERVACIONES_", a);		
		}
		public void setIdCuentaUnica (String a) {
			this.idCuentaUnica = a;		
		}		
		

		public Integer getDatosPagosRenegociarEstadoFactura () {
			return UtilidadesHash.getInteger(this.datos,"_pagosRenegociar_ESTADOFACTURA_");		
		}
		public Integer getDatosPagosRenegociarDevuelta () {
			return UtilidadesHash.getInteger(this.datos,"_pagosRenegociar_DEVUELTA_");		
		}		
		public Double getDatosPagosRenegociarImportePendiente () {
			return UtilidadesHash.getDouble(this.datos,"_pagosRenegociar_IMPORTEPENDIENTE_");		
		}
		public String getDatosPagosRenegociarNuevaFormaPago () {
			return UtilidadesHash.getString(this.datos,"_pagosRenegociar_NUEVAFORMAPAGO_");		
		}
		public Integer getDatosPagosRenegociarIdCuenta () {
			return UtilidadesHash.getInteger(this.datos,"_pagosRenegociar_IDCUENTA_");		
		}
		public String getIdCuentaUnica () {
			return this.idCuentaUnica;		
		}		
		public String getDatosPagosRenegociarObservaciones () {
			return UtilidadesHash.getString(this.datos,"_pagosRenegociar_OBSERVACIONES_");		
		}
	
	// LINEAS ////////////////////////////////////////////////////////////////
	public void setDatosLineaDescripcion (String a) {
		UtilidadesHash.set(this.datos,"_lineas_DESCRIPCION_", a);		
	}
	public void setDatosLineaPrecio (Double a) {
		UtilidadesHash.set(this.datos,"_lineas_PRECIO_", a);		
	}
	public void setDatosLineaIVA (Float a) {
		UtilidadesHash.set(this.datos,"_lineas_IVA_", a);		
	}
	public String getDatosLineaDescripcion () {
		return UtilidadesHash.getString(this.datos,"_lineas_DESCRIPCION_");		
	}
	public Double getDatosLineaPrecio () {
		return UtilidadesHash.getDouble(this.datos,"_lineas_PRECIO_");		
	}
	public Float getDatosLineaIVA () {
		return UtilidadesHash.getFloat(this.datos,"_lineas_IVA_");		
	}
}
