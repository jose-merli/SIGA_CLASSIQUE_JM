package com.siga.envios.action;

import java.util.*;
import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.transaction.*;
import javax.servlet.http.*;
import com.siga.envios.form.*;
import org.apache.struts.action.*;
import com.siga.gui.processTree.*;

public class SIGAEnviosDatosGeneralesAction extends MasterAction
{
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try
		{
		    miForm = (MasterForm) formulario;

		    if (miForm != null)
		    {
				String accion = miForm.getModo();

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
				{
					mapDestino = abrir(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("grabar"))
				{
					mapDestino = grabar(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("borrarCampos"))
				{
					mapDestino = borrarCampos(mapping, miForm, request, response);
				}

				else
				{
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

			if (mapDestino == null)
			{
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}

			return mapping.findForward(mapDestino);
		}

		catch (SIGAException es)
		{
			throw es;
		}

		catch (Exception e)
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

        if(request.getParameter("acceso").equalsIgnoreCase("Ver"))
        {
		    userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
        }

        EnvCamposEnviosAdm admCamposEnvio = new EnvCamposEnviosAdm(this.getUserBean(request));

        String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");

        Vector vDatos = admCamposEnvio.obtenerCamposEnvios(idInstitucion, idEnvio, "");

        Hashtable htAux = new Hashtable();

        htAux.put(EnvDestinatariosBean.C_IDINSTITUCION, idInstitucion);
        htAux.put(EnvDestinatariosBean.C_IDENVIO, idEnvio);

        EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));
        Vector vEnvio = admEnvio.selectByPK(htAux);
        EnvEnviosBean envioBean = null; 
        if (vEnvio!=null && vEnvio.size()>0) {
        	envioBean = (EnvEnviosBean)vEnvio.elementAt(0);
        }

        String sDescripcionEnvio = envioBean.getDescripcion();
        String sIdPlantillaEnvio = ""+envioBean.getIdPlantillaEnvios();
        String sIdPlantillaGeneracion = ""+envioBean.getIdPlantilla();

        htAux = new Hashtable();

        htAux.put(EnvTipoEnviosBean.C_IDTIPOENVIOS, envioBean.getIdTipoEnvios());

        EnvTipoEnviosAdm admTipoEnvio = new EnvTipoEnviosAdm(this.getUserBean(request));
        Vector vTipoEnvio = admTipoEnvio.selectByPK(htAux);
        EnvTipoEnviosBean tipoEnvioBean = null;
        if (vTipoEnvio!=null && vTipoEnvio.size()>0) {
        	tipoEnvioBean = (EnvTipoEnviosBean)vTipoEnvio.elementAt(0);
        }
        String sTipo = tipoEnvioBean.getNombre();
        String sIdTipoEnvio = ""+tipoEnvioBean.getIdTipoEnvios();
        
        request.setAttribute("nombreEnv", sDescripcionEnvio);
        request.setAttribute("tipo", sTipo);
        request.setAttribute("idTipoEnvio", sIdTipoEnvio);
        request.setAttribute("idEnvio", idEnvio);
        request.setAttribute("idInstitucion", idInstitucion);
        request.setAttribute("idPlantillaEnvio", sIdPlantillaEnvio);
        request.setAttribute("idPlantillaGeneracion", sIdPlantillaGeneracion);

        request.setAttribute("datos", vDatos);
        
        boolean existePlantilla=admEnvio.existePlantillaEnvio(idInstitucion, idEnvio);
        request.setAttribute("existePlantilla", new Boolean(existePlantilla));

        return "inicio";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
	    return mostrarRegistro(mapping,formulario,request,response,false);
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
	    return mostrarRegistro(mapping,formulario,request,response,true);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
	    SIGAEnviosDatosGeneralesForm form = (SIGAEnviosDatosGeneralesForm)formulario;
	    UserTransaction tx = null;
	    
	    try {
	    
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
		    
		    EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.getUserBean(request));
	
		    String idInstitucion = form.getIdInstitucion();
		    String idEnvio = form.getIdEnvio();
		    String idCampo = form.getIdCampo();
		    String tipoCampo = form.getTipoCampo();
		    
	    	String idFormato = null;
	    	try {
	    		idFormato = form.getIdFormato();
	    	} catch (Exception ef){
	    		idFormato = "";
	    	}
	
		    Hashtable htAux = new Hashtable();
	
		    htAux.put(EnvCamposEnviosBean.C_IDINSTITUCION, idInstitucion);
		    htAux.put(EnvCamposEnviosBean.C_IDENVIO, idEnvio);
		    htAux.put(EnvCamposEnviosBean.C_IDCAMPO, idCampo);
		    htAux.put(EnvCamposEnviosBean.C_TIPOCAMPO, tipoCampo);
	
		    Vector vAux = admCampos.selectByPK(htAux);
	
		    EnvCamposEnviosBean beanCampos = (EnvCamposEnviosBean)vAux.elementAt(0);
	
		    Hashtable htOld = beanCampos.getOriginalHash();
		    Hashtable htNew = (Hashtable)htOld.clone();
	
	    	htNew.put(EnvCamposEnviosBean.C_IDFORMATO, idFormato);
		    htNew.put(EnvCamposEnviosBean.C_VALOR, form.getValor());
	
		    tx.begin();
		    if (admCampos.update(htNew, htOld))
		        request.setAttribute("mensaje", "messages.updated.success");
		    else
		        request.setAttribute("mensaje", "messages.updated.error");
		    tx.commit();
		    
		    request.setAttribute("modal", "1");
	    } catch (Exception e){
	    	throwExcp("messages.general.error",new String[] {"modulo.envios"}, e, tx);
	    }
	    return "exito";
	}

	protected String borrarCampos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
	    UserTransaction tx = null;

	    try
	    {
		    SIGAEnviosDatosGeneralesForm form = (SIGAEnviosDatosGeneralesForm)formulario;
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));

		    String idInstitucion = form.getIdInstitucion();
		    String idEnvio = form.getIdEnvio();
		    String idTipoEnvio = form.getIdTipoEnvio();
		    String idPlantilla = form.getIdPlantillaEnvio();
		    String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

		    Hashtable htAux = new Hashtable();

		    htAux.put(EnvEnviosBean.C_IDINSTITUCION, idInstitucion);
		    htAux.put(EnvEnviosBean.C_IDENVIO, idEnvio);

		    Vector vAux = admEnvio.selectByPK(htAux);

		    EnvEnviosBean beanEnvio = (EnvEnviosBean)vAux.elementAt(0);

		    Hashtable htOld = beanEnvio.getOriginalHash();
		    Hashtable htNew = (Hashtable)htOld.clone();

		    htNew.put(EnvEnviosBean.C_IDPLANTILLAENVIOS, idPlantilla);
		    htNew.put(EnvEnviosBean.C_IDPLANTILLA, "");

		    tx = userBean.getTransaction();
		    tx.begin();

		    admEnvio.copiarCamposPlantilla(Integer.valueOf(idInstitucion), Integer.valueOf(idEnvio), Integer.valueOf(idTipoEnvio),Integer.valueOf(idPlantilla));

		    if (admEnvio.update(htNew, htOld))
		    {
		        tx.commit();

		        request.setAttribute("mensaje", "messages.updated.success");
		    }

		    else
		    {
		        tx.rollback();

		        request.setAttribute("mensaje", "messages.updated.error");
		    }
	    }

	    catch(Exception e)
	    {
	        try
	        {
	            tx.rollback();
	        }

	        catch(Exception ex)
	        {
	            throw new SIGAException(ex);
	        }

	        throw new SIGAException(e);
	    }

	    return "exito";
	}

	protected String grabar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions
	{
	    SIGAEnviosDatosGeneralesForm form = (SIGAEnviosDatosGeneralesForm)formulario;
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    EnvEnviosAdm admEnvio = new EnvEnviosAdm(this.getUserBean(request));

	    String idInstitucion = form.getIdInstitucion();
	    String idEnvio = form.getIdEnvio();
	    String descEnvio = form.getDescripcionEnvio();
	    String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

	    Hashtable htEnvio = new Hashtable();

	    htEnvio.put(EnvEnviosBean.C_IDINSTITUCION, idInstitucion);
	    htEnvio.put(EnvEnviosBean.C_IDENVIO, idEnvio);

	    Vector vAux = admEnvio.selectByPK(htEnvio);

	    EnvEnviosBean beanEnvio = (EnvEnviosBean)vAux.elementAt(0);

	    Hashtable htOld = beanEnvio.getOriginalHash();
	    Hashtable htNew = (Hashtable)htOld.clone();

	    htNew.put(EnvEnviosBean.C_DESCRIPCION, descEnvio);
	    htNew.put(EnvEnviosBean.C_IDPLANTILLA, idPlantillaGeneracion);

	    if (admEnvio.update(htNew, htOld))
	    {
	        request.setAttribute("mensaje", "messages.updated.success");
	    }

	    else
	    {
	        request.setAttribute("mensaje", "messages.updated.error");
	    }

	    return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException, ClsExceptions
	{
	    SIGAEnviosDatosGeneralesForm form = (SIGAEnviosDatosGeneralesForm)formulario;

	    Vector vOcultos = form.getDatosTablaOcultos(0);

	    String idInstitucion = form.getIdInstitucion();
	    String idEnvio = form.getIdEnvio();
	    String idPlantilla = form.getIdPlantillaEnvio();

	    String idCampo = (String)vOcultos.elementAt(0);
	    String descCampo = (String)vOcultos.elementAt(1);
	    String idFormato = null;
	    String descFormato = null;
	    String idTipoCampo = null;
	    
	    //
	    //Control de campos vacios:
	    //
	    //Si no tenemos los datos del tipo de formato (es opcional y puede venir vacio)
	    if (vOcultos.size()==3){
	    	idTipoCampo = (String)vOcultos.elementAt(2);
	    	idFormato = "0";
	    	descFormato = "";
	    } else {
	    	idFormato = (String)vOcultos.elementAt(2);
	    	descFormato = (String)vOcultos.elementAt(3);
	    	idTipoCampo = (String)vOcultos.elementAt(4);
	    }

	    Hashtable htAux = new Hashtable();
	    htAux.put(EnvCamposEnviosBean.C_IDINSTITUCION, idInstitucion);
	    htAux.put(EnvCamposEnviosBean.C_IDENVIO, idEnvio);
	    htAux.put(EnvCamposEnviosBean.C_IDCAMPO, idCampo);
    	htAux.put(EnvCamposEnviosBean.C_TIPOCAMPO, idTipoCampo);

	    EnvCamposEnviosAdm admCampos = new EnvCamposEnviosAdm(this.getUserBean(request));

        Vector vEnvio = admCampos.selectByPK(htAux);

        EnvCamposEnviosBean envioBean = (EnvCamposEnviosBean)vEnvio.elementAt(0);

        Vector vDatos = new Vector();

        vDatos.add(envioBean);
        
        //Comprobamos si se puede editar el valor del campo
        Hashtable htValor = new Hashtable();
        htValor.put(EnvCamposEnviosBean.C_IDCAMPO, idCampo);
        htValor.put(EnvCamposEnviosBean.C_TIPOCAMPO, idTipoCampo);

	    EnvCamposAdm camposAdm = new EnvCamposAdm(this.getUserBean(request));
	    EnvCamposBean campoBean = (EnvCamposBean)camposAdm.selectByPK(htValor).firstElement();

	    String sCapturar = campoBean.getCapturarDatos();

        String editable = bEditable ? "1" : "0";

        request.setAttribute("descCampo", descCampo);
        request.setAttribute("descFormato", descFormato);
        request.setAttribute("idInstitucion", idInstitucion);
        request.setAttribute("idEnvio", idEnvio);
        //request.setAttribute("datos", vDatos);
        request.setAttribute("bean", envioBean);
        request.setAttribute("sCapturar", sCapturar);
        request.setAttribute("editable", editable);

	    return "editar";
	}
}