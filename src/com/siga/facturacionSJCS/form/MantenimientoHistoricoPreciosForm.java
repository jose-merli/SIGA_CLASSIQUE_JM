//VERSIONES:
//julio.vicente Creacion: 15-04-2005 
//

package com.siga.facturacionSJCS.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import com.siga.Utilidades.*;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso MANTENIMIENTO RETENCIONES JUDICIALES
* @author julio.vicente 31-03-2005
*/
public class MantenimientoHistoricoPreciosForm extends MasterForm {
	
	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (String dato) {UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);} 
	public void setIdFacturacion(String dato) {UtilidadesHash.set(this.datos,"IDFACTURACION", dato);}
	public void setIdGuardia(String dato) {UtilidadesHash.set(this.datos,"IDGUARDIA", dato);}
	public void setIdTurno(String dato) {UtilidadesHash.set(this.datos,"IDTURNO", dato);}
	public void setIdHito(String dato) {UtilidadesHash.set(this.datos,"IDHITO", dato);}
	public void setPagoFacturacion (String dato) {UtilidadesHash.set(this.datos,"PAGOFACTURACION", dato);}
	public void setPrecioHito (String dato) {UtilidadesHash.set(this.datos,"PRECIOHITO", dato);}
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	public String getIdInstitucion	() 	{return UtilidadesHash.getString(this.datos, "IDINSTITUCION");}
	public String getIdFacturacion	() {return UtilidadesHash.getString(this.datos, "IDFACTURACION");}
	public String getIdGuardia () {return UtilidadesHash.getString(this.datos, "IDGUARDIA");}
	public String getIdTurno () {return UtilidadesHash.getString(this.datos, "IDTURNO");}
	public String getIdHito () {return UtilidadesHash.getString(this.datos, "IDHITO");}
	public String getPagoFacturacion () {return UtilidadesHash.getString(this.datos, "PAGOFACTURACION");}
	public String getPrecioHito () {return UtilidadesHash.getString(this.datos, "PRECIOHITO");}	

	// OTRAS FUNCIONES 
	/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}