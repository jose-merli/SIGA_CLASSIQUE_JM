/*
 * VERSIONES:
 * jose.barrientos 		4-6-2009		Creacion	
 *
 */

/**
 * Clase encargada del manejo de plantillas de sms  
 */

package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gui.processTree.SIGAPTConstants;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.envios.form.*;

import org.apache.struts.action.*;

public class TextoEnviosSMSAction extends MasterAction
{

	public ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 

			do {
				UsrBean usrbean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
				//Si venimos de Productos y Servicios para la seleccion de la direccion del envio, damos acceso total:
				if (usrbean.isLetrado())
					usrbean.setAccessType(SIGAConstants.ACCESS_READ);

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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

        if(request.getParameter("acceso").equalsIgnoreCase("Ver"))
        {
		    userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
        }

        EnvCamposEnviosAdm admCamposEnvio = new EnvCamposEnviosAdm(this.getUserBean(request));

        String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");

        Vector vDatos = admCamposEnvio.obtenerCamposEnvios(idInstitucion, idEnvio, "S");

        Hashtable htAux = new Hashtable();

        htAux.put(EnvDestinatariosBean.C_IDINSTITUCION, idInstitucion);
        htAux.put(EnvDestinatariosBean.C_IDENVIO, idEnvio);

        EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));
        Vector vEnvio = admEnvio.selectByPK(htAux);
        EnvEnviosBean envioBean = (EnvEnviosBean)vEnvio.elementAt(0);

        String sDescripcionEnvio = envioBean.getDescripcion();
        String sIdPlantillaEnvio = ""+envioBean.getIdPlantillaEnvios();
        String sIdPlantillaGeneracion = ""+envioBean.getIdPlantilla();

        String sCuerpo = admEnvio.getCuerpoSMS(new Integer(idInstitucion), new Integer(idEnvio));
        if (sCuerpo==null) sCuerpo="";	


        htAux = new Hashtable();

        htAux.put(EnvTipoEnviosBean.C_IDTIPOENVIOS, envioBean.getIdTipoEnvios());

        EnvTipoEnviosAdm admTipoEnvio = new EnvTipoEnviosAdm(this.getUserBean(request));
        Vector vTipoEnvio = admTipoEnvio.selectByPK(htAux);
        EnvTipoEnviosBean tipoEnvioBean = (EnvTipoEnviosBean)vTipoEnvio.elementAt(0);

        String sTipo = tipoEnvioBean.getNombre();
        String sIdTipoEnvio = ""+tipoEnvioBean.getIdTipoEnvios();

        request.setAttribute("nombreEnv", sDescripcionEnvio);
        request.setAttribute("tipo", sTipo);
        request.setAttribute("idTipoEnvio", sIdTipoEnvio);
        request.setAttribute("idEnvio", idEnvio);
        request.setAttribute("idInstitucion", idInstitucion);
        request.setAttribute("idPlantillaEnvio", sIdPlantillaEnvio);
        request.setAttribute("idPlantillaGeneracion", sIdPlantillaGeneracion);
        request.setAttribute("sCuerpo", sCuerpo);

        request.setAttribute("datos", vDatos);

        return "inicio";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UserTransaction tx = null;

	    try
	    {
	    	TextoEnviosSMSForm form = (TextoEnviosSMSForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

	        EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));

	        String idInstitucion = form.getIdInstitucion();
	        String idEnvio = form.getIdEnvio();
	        String sCuerpo = form.getCuerpo();

	        tx = userBean.getTransaction();
	        tx.begin();

	        admEnvio.setTextoSms(new Integer(idInstitucion), new Integer(idEnvio), sCuerpo);

	        tx.commit();

	        request.setAttribute("mensaje", "messages.updated.success");
	    }

	    catch (Exception e)
	    {
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, tx); 
	    }

	    return "exito";
	}

}