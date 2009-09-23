//VERSIONES:
//david.sanchezp Creacion: 17-03-2005 
//

package com.siga.facturacionSJCS.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso CONSULTAR PAGOS
* @author david.sanchezp 17-03-2005
*/
public class MantenimientoPagosSCJSForm extends MasterForm {

	
	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (String dato) {UtilidadesHash.set(this.datos,"IdInstitucion", dato);} 
	public void setIdEstado(String dato) {UtilidadesHash.set(this.datos,"idEstado", dato);} 
	public void setFechaIni (String dato) {UtilidadesHash.set(this.datos,"fechaIni", dato);} 
	public void setFechaFin (String dato) {UtilidadesHash.set(this.datos,"fechaFin", dato);} 
	public void setNombre (String dato) {UtilidadesHash.set(this.datos,"nombre", dato);} 
//	public void setCriterioPago (String dato) {UtilidadesHash.set(this.datos,"criterioPago", dato);}
	public void setBuscar (String dato) {UtilidadesHash.set(this.datos,"buscar", dato);}
	public void setIdPagosJG (String dato) {UtilidadesHash.set(this.datos,"idPagosJG", dato);}
	

	// Metodos Get 1 por campo Formulario (*.jsp)
	public String getIdInstitucion	() 	{return UtilidadesHash.getString(this.datos, "IdInstitucion");}
	public String getIdEstado	() {return UtilidadesHash.getString(this.datos, "idEstado");}
	public String getFechaIni () {return UtilidadesHash.getString(this.datos, "fechaIni");}	
	public String getFechaFin () {return UtilidadesHash.getString(this.datos, "fechaFin");}		
	public String getNombre	() {return UtilidadesHash.getString(this.datos, "nombre");}		
//	public String getCriterioPago () {return UtilidadesHash.getString(this.datos, "criterioPago");}
	public String getBuscar () {return UtilidadesHash.getString(this.datos, "buscar");}
	public String getIdPagosJG () {return UtilidadesHash.getString(this.datos, "idPagosJG");}	

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