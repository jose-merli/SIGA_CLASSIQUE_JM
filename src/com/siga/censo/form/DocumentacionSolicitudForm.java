//VERSIONES
//raul.ggonzalez 14-02-2005 Creacion
//

package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;
import com.siga.beans.*;

/**
* Clase action form del caso de uso DOCUMENTACION DE SOLICITUD
* @author AtosOrigin 14-02-2005
*/
public class DocumentacionSolicitudForm extends MasterForm {

	private String documentosSolicitados="";
	
	// Metodos Set (Formulario (*.jsp))
 
	public void setTipoColegiacion (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setTipoSolicitud (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}
	
	public void setTipoModalidad (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setDocumentosSolicitados (String dato) { 
		try {
			this.documentosSolicitados = dato;
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	// Metodos Get 1 por campo Formulario (*.jsp)

	

	public String getTipoModalidad	() 	{ 
		return UtilidadesHash.getString(this.datos, CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD);		
	}

	public String getTipoColegiacion	() 	{ 
		return UtilidadesHash.getString(this.datos, CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION);		
	}

	public String getTipoSolicitud	() 	{ 
		return UtilidadesHash.getString(this.datos, CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD);		
	}

	public String[] getDocumentosSolicitados	() 	{ 
		return documentosSolicitados.split("%");		
	}

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