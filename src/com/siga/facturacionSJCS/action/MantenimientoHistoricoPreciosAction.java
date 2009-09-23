//VERSIONES:
//julio.vicente Creacion: 15-04-2005 
//Modificado por david.sanchez 13-5-2005 para modificar la consulta del buscarPor que no filtraba bien.

package com.siga.facturacionSJCS.action;

import java.util.Hashtable;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.*;
import com.siga.facturacionSJCS.form.*;
import com.siga.general.*;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

import java.util.Vector;

/**
* Clase action form del caso de uso MANTENIMIENTO HISTORICO HITOS FACTURACIÓN
* @author julio.vicente 15-04-2005
*/
public class MantenimientoHistoricoPreciosAction extends MasterAction {

	public ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();		
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
						break;					
					} else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacionSJCS"});
		}
	}


		
	/**
	 * Metodo que implementa el modo abrir. Con este metodo entramos a la primera pantalla del caso de uso.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		return "inicio";
	}

	/**
	 * Not implemented
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}


	/**
	 * Not implemented
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		return null;
	}


	/**
	 * Not implemented
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		return null;
	}

	/**
	 * Not implemented
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
	     return null;
	}

	/**
	 * Método que implementa el modo buscarPor. Realiza la busqueda de los pagos teniendo en cuenta la fecha <br>
	 * última del estado del pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String destino="";
		Vector resultado = new Vector();
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// casting del formulario
			MantenimientoHistoricoPreciosForm miFormulario = (MantenimientoHistoricoPreciosForm)formulario;
			FcsHistoricoHitoFactAdm miAdm = new FcsHistoricoHitoFactAdm(this.getUserBean(request));
			
			Hashtable miHash = miFormulario.getDatos();

			// Primera parte de la consulta (las tablas que necesitamos)			
			String consulta = " SELECT turnos." + ScsTurnoBean.C_NOMBRE + " AS turno, guardias." + ScsGuardiasTurnoBean.C_NOMBRE + " AS guardia, " + UtilidadesMultidioma.getCampoMultidiomaSimple("hitos." + ScsHitoFacturableBean.C_DESCRIPCION, this.getUserBean(request).getLanguage()) + " AS hito," + 
							  " historico." + FcsHistoricoHitoFactBean.C_PRECIOHITO +  " AS importe FROM " + FcsHistoricoHitoFactBean.T_NOMBRETABLA + " historico, " + ScsGuardiasTurnoBean.T_NOMBRETABLA + " guardias," + 
							  ScsTurnoBean.T_NOMBRETABLA + " turnos, " + FcsFacturacionJGBean.T_NOMBRETABLA + " facturacion, " + ScsHitoFacturableBean.T_NOMBRETABLA + " hitos WHERE historico." + FcsHistoricoHitoFactBean.C_IDINSTITUCION +
							  " = guardias." + ScsGuardiasTurnoBean.C_IDINSTITUCION + " AND historico." + FcsHistoricoHitoFactBean.C_IDTURNO + " = guardias." + ScsGuardiasTurnoBean.C_IDTURNO + " AND historico." + FcsHistoricoHitoFactBean.C_IDGUARDIA +
							  " = guardias." + ScsGuardiasTurnoBean.C_IDGUARDIA + " AND guardias." + ScsGuardiasTurnoBean.C_IDINSTITUCION + " = turnos." + ScsTurnoBean.C_IDINSTITUCION + " and guardias." + ScsGuardiasTurnoBean.C_IDTURNO + 
							  " = turnos." + ScsTurnoBean.C_IDTURNO + " AND historico." + FcsHistoricoHitoFactBean.C_IDHITO + " = hitos." + ScsHitoFacturableBean.C_IDHITO + " AND historico." + FcsHistoricoHitoFactBean.C_IDINSTITUCION + 
							  " = facturacion." + FcsFacturacionJGBean.C_IDINSTITUCION + " AND historico." + FcsHistoricoHitoFactBean.C_IDFACTURACION + " = facturacion." + FcsFacturacionJGBean.C_IDFACTURACION + " AND historico." + 
							  FcsHistoricoHitoFactBean.C_IDINSTITUCION + " = " + user.getLocation() + " AND historico." + FcsHistoricoHitoFactBean.C_PAGOFACTURACION + " = '" + ClsConstants.PAGOS_SJCS + "'";						  												
			
			// Segunda parte de la consulta (con los criterios de búsqueda seleccionados)			 
			if ((miHash.containsKey(ScsHitoFacturableBean.C_IDHITO)) && (!miHash.get(ScsHitoFacturableBean.C_IDHITO).toString().equals("")))
				consulta += " AND hitos." + ScsHitoFacturableBean.C_IDHITO + " = " + miHash.get(ScsHitoFacturableBean.C_IDHITO).toString();

			consulta += " AND facturacion." + FcsFacturacionJGBean.C_IDFACTURACION + " = " + miHash.get(FcsFacturacionJGBean.C_IDFACTURACION);
						
			// Y por último se anhaden los criterios de ordenación			
			consulta += " ORDER BY turnos." + ScsTurnoBean.C_NOMBRE + ", guardias." + ScsGuardiasTurnoBean.C_NOMBRE + ", historico." + FcsHistoricoHitoFactBean.C_PRECIOHITO;
			
			resultado = miAdm.selectGenerico(consulta);	
			
			request.setAttribute("resultado",resultado);
			
			destino = "listar";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}

	
	/**
	 * Not implemented
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}
	
	/**
	 * Not implemented
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		return null;
	}	
}
