package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import com.siga.gui.processTree.SIGAPTConstants;
import com.siga.Utilidades.UtilidadesString;

import com.siga.envios.form.*;
import org.apache.struts.action.*;

public class SIGAEnviosCorreoElectronicoAction extends MasterAction
{
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

        Vector vDatos = admCamposEnvio.obtenerCamposEnvios(idInstitucion, idEnvio, "E");

        Hashtable htAux = new Hashtable();

        htAux.put(EnvDestinatariosBean.C_IDINSTITUCION, idInstitucion);
        htAux.put(EnvDestinatariosBean.C_IDENVIO, idEnvio);

        EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));
        Vector vEnvio = admEnvio.selectByPK(htAux);
        EnvEnviosBean envioBean = (EnvEnviosBean)vEnvio.elementAt(0);

        String sDescripcionEnvio = envioBean.getDescripcion();
        String sIdPlantillaEnvio = ""+envioBean.getIdPlantillaEnvios();
        String sIdPlantillaGeneracion = ""+envioBean.getIdPlantilla();

        String sAsunto = admEnvio.getAsunto(new Integer(idInstitucion), new Integer(idEnvio));
        String sCuerpo = admEnvio.getCuerpo(new Integer(idInstitucion), new Integer(idEnvio));
        if (sAsunto==null) sAsunto="";	
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
        request.setAttribute("sAsunto", sAsunto);
        request.setAttribute("sCuerpo", sCuerpo);

        request.setAttribute("datos", vDatos);

        return "inicio";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UserTransaction tx = null;

	    try
	    {
		    SIGAEnviosCorreoElectronicoForm form = (SIGAEnviosCorreoElectronicoForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

	        EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));

	        String idInstitucion = form.getIdInstitucion();
	        String idEnvio = form.getIdEnvio();
	        String sAsunto = form.getAsunto();
	        String sCuerpo =UtilidadesString.reemplazarTextoEntreMarca(form.getCuerpo(),"%%"); 

	        tx = userBean.getTransaction();
	        tx.begin();

	        admEnvio.setAsunto(new Integer(idInstitucion), new Integer(idEnvio), sAsunto);
	        admEnvio.setCuerpo(new Integer(idInstitucion), new Integer(idEnvio), sCuerpo);

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