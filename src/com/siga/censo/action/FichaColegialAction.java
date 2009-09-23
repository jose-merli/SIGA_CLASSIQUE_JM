//VERSIONES:
//raul.ggonzalez 21-02-2005 Creacion
//

package com.siga.censo.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.beans.*;
import com.siga.censo.form.FichaColegialForm;
import com.siga.general.*;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;

/**
* Clase action del caso de uso FICHA COLEGIAL
* @author AtosOrigin 21-02-2005
*/
public class FichaColegialAction extends MasterAction {
	// Atributos
	/**   */

	/**
	 * Metodo que implementa el modo abrir 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";

		try {
			
		FichaColegialForm miform = (FichaColegialForm)formulario;

		// OBTENGO VALORES DEL USERBEAN
		// obtener idpersona
		String idPersona = new Long(this.getUserBean(request).getIdPersona()).toString();
		// obtener idinstitucion
		String idInstitucion = this.getUserBean(request).getLocation();
		
		String modo = "ver";
		Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);

		CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
		String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
		if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
			tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
		} else {
			//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
			CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));
			// esto comprueba por el CIF
/*
			String NIFCIF = beanPer.getNIFCIF();
			String primera="0";
			primera = NIFCIF.substring(0,1);
			try {
				int numero = Integer.parseInt(primera);
				// si no falla tiene CIF, osea, es juridico
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			} catch (NumberFormatException ne) {
				// si no falla tiene NIF, osea, es fisico
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
			}
*/
			// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
			// obtengo los datos de nocolegiado
			Hashtable hashNoCol = new Hashtable();
			hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
			hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
			CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
			Vector v = nocolAdm.selectByPK(hashNoCol);
			if (v!=null && v.size()>0) {
				CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
				if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
					// SOCIEDAD
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
				} else {
					// PERSONAL
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				}
			}			
			
		}
		
		Hashtable datosCliente = new Hashtable();
		datosCliente.put("accion",modo);
		datosCliente.put("idPersona",idPersona);
		datosCliente.put("idInstitucion",idInstitucion);
		datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));

		// con esto aseguramos que no hay boton volver
		//request.getSession().removeAttribute("CenFichaColegialTipo"); 
		
		// AHORA si queremos que vuelva. Que vuelva a la "nada"
		request.getSession().setAttribute("CenBusquedaClientesTipo","NADA"); 
		
		request.setAttribute("datosCliente", datosCliente);		

		destino="administracion";
	 } 	
	catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	}

	 return destino;
	}
}
