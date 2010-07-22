//VERSIONES:
//julio.vicente Creacion: 31-03-2005 
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
public class MantenimientoRetencionesJudicialesForm extends MasterForm {
	String checkEsDeTurno;
	public String getCheckEsDeTurno() {
		return checkEsDeTurno;
	}
	public void setCheckEsDeTurno(String checkEsDeTurno) {
		this.checkEsDeTurno = checkEsDeTurno;
	}
	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (String dato) {UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);} 
	public void setIdPersona(String dato) {UtilidadesHash.set(this.datos,"IDPERSONA", dato);}
	public void setIdRetencion(String dato) {UtilidadesHash.set(this.datos,"IDRETENCION", dato);}
	public void setIdDestinatario(String dato) {UtilidadesHash.set(this.datos,"IDDESTINATARIO", dato);}
	public void setFechaAlta (String dato) {UtilidadesHash.set(this.datos,"FECHAALTA", dato);}
	public void setFechaInicio (String dato) {UtilidadesHash.set(this.datos,"FECHAINICIO", dato);} 
	public void setFechaNotificacionDesde (String dato) {UtilidadesHash.set(this.datos,"FECHANOTIFICACIONDESDE", dato);} 
	public void setFechaNotificacionHasta (String dato) {UtilidadesHash.set(this.datos,"FECHANOTIFICACIONHASTA", dato);}
	public void setFechaFin (String dato) {UtilidadesHash.set(this.datos,"FECHAFIN", dato);}
	public void setTipoRetencion (String dato) {UtilidadesHash.set(this.datos,"TIPORETENCION", dato);}
	public void setImporte (String dato) {UtilidadesHash.set(this.datos,"IMPORTE", dato);}
	public void setObservaciones (String dato) {UtilidadesHash.set(this.datos,"OBSERVACIONES", dato);}
	public void setNcolegiado (String dato) {UtilidadesHash.set(this.datos,"NCOLEGIADO", dato);}
//	public void setCheckEsDeTurno (String dato) {UtilidadesHash.set(this.datos,"ESDETURNO", dato);}
	public void setValorRetencion (String dato) {UtilidadesHash.set(this.datos,"VALORRETENCION", dato);}
	public void setDescDestinatario  (String dato) {UtilidadesHash.set(this.datos,"DESCDESTINATARIO", dato);}
	public void setCheckHistorico  (String dato) {UtilidadesHash.set(this.datos,"CHECKHISTORICO", dato);}
	
	
	

	// RGG 04-04-2005 para el listado de retenciones
	public void setNombreDestinatario(String dato) {UtilidadesHash.set(this.datos,"nombreDestinatario", dato);}
	public void setFechaIniListado(String dato) {UtilidadesHash.set(this.datos,"fechaIniListado", dato);}
	public void setFechaFinListado(String dato) {UtilidadesHash.set(this.datos,"fechaFinListado", dato);}


	// Metodos Get 1 por campo Formulario (*.jsp)
	public String getIdInstitucion	() 	{return UtilidadesHash.getString(this.datos, "IDINSTITUCION");}
	public String getIdPersona	() {return UtilidadesHash.getString(this.datos, "IDPERSONA");}
	public String getIdRetencion () {return UtilidadesHash.getString(this.datos, "IDRETENCION");}
	public String getIdDestinatario () {return UtilidadesHash.getString(this.datos, "IDDESTINATARIO");}
	public String getFechaAlta () {return UtilidadesHash.getString(this.datos, "FECHAALTA");}
	public String getFechaInicio () {return UtilidadesHash.getString(this.datos, "FECHAINICIO");}	
	public String getFechaNotificacionDesde () {return UtilidadesHash.getString(this.datos, "FECHANOTIFICACIONDESDE");}
	public String getFechaNotificacionHasta () {return UtilidadesHash.getString(this.datos, "FECHANOTIFICACIONHASTA");}	
	public String getFechaFin () {return UtilidadesHash.getString(this.datos, "FECHAFIN");}
	public String getTipoRetencion () {return UtilidadesHash.getString(this.datos, "TIPORETENCION");}
	public String getImporte () {return UtilidadesHash.getString(this.datos, "IMPORTE");}
	public String getObservaciones () {return UtilidadesHash.getString(this.datos, "OBSERVACIONES");}
	public String getNcolegiado () {return UtilidadesHash.getString(this.datos, "NCOLEGIADO");}
//	public String getCheckEsDeTurno () {return UtilidadesHash.getString(this.datos, "ESDETURNO");}
	public String getValorRetencion () {return UtilidadesHash.getString(this.datos, "VALORRETENCION");}
	public String getDescDestinatario () {return UtilidadesHash.getString(this.datos, "DESCDESTINATARIO");}
	
	// RGG 04-04-2005 para el listado de retenciones
	public String getNombreDestinatario () {return UtilidadesHash.getString(this.datos, "nombreDestinatario");}
	public String getFechaIniListado () {return UtilidadesHash.getString(this.datos, "fechaIniListado");}
	public String getFechaFinListado () {return UtilidadesHash.getString(this.datos, "fechaFinListado");}
	public String getCheckHistorico  () {return UtilidadesHash.getString(this.datos, "CHECKHISTORICO");}
	
	
	


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