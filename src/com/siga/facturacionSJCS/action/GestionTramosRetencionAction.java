package com.siga.facturacionSJCS.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.mail.search.SizeTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsTramosRetencionAdm;
import com.siga.beans.FcsTramosRetencionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.censo.service.BajasTemporalesService;
import com.siga.facturacionSJCS.form.TramosRetencionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionTramosRetencionAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxSmi")){
						getAjaxSmi(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxImporteRetencion")){
						getAjaxImporteRetencion(mapping, miForm, request, response);
						return null;
					}
					
					else if ( accion.equalsIgnoreCase("getAjaxBusquedaTramosRetencion")){
						mapDestino = getAjaxBusquedaTramosRetencion (mapping, miForm, request, response);
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		TramosRetencionForm tramosRetencionForm = (TramosRetencionForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		tramosRetencionForm.setAnio(GstDate.getYear(new Date()));
		GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
		String numMesesPagoTramoLEC = paramAdm.getValor (usr.getLocation(), ClsConstants.MODULO_FACTURACION_SJCS, ClsConstants.GEN_PARAM_NUM_MESES_PAGO_TRAMO_LEC, "1");
		if(Integer.parseInt(numMesesPagoTramoLEC)==0 ||Integer.parseInt(numMesesPagoTramoLEC)>12)
			numMesesPagoTramoLEC = "1";
		tramosRetencionForm.setNumeroMeses(numMesesPagoTramoLEC);
		tramosRetencionForm.setIdInstitucion(usr.getLocation());
		FcsTramosRetencionAdm admFcsTramosRetencionAdm = new FcsTramosRetencionAdm(usr);
		
		Double smi = admFcsTramosRetencionAdm.getSmi(tramosRetencionForm.getAnio());
		tramosRetencionForm.setSmi(UtilidadesNumero.formato(smi));
		tramosRetencionForm.setTramosRetencion(new ArrayList<TramosRetencionForm>());
		
		return "inicio";
	}
	
	
	protected String getAjaxBusquedaTramosRetencion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		TramosRetencionForm tramosRetencionForm = (TramosRetencionForm) formulario;
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String forward = "listadoTramosRetencion";
		
		FcsTramosRetencionAdm admFcsTramosRetencionAdm = new FcsTramosRetencionAdm(usr);
		List<TramosRetencionForm> alTramosRetencion = admFcsTramosRetencionAdm.getTramosRetencion(tramosRetencionForm.getIdInstitucion(),tramosRetencionForm.getSmi(),tramosRetencionForm.getNumeroMeses());
		if(alTramosRetencion==null)
			alTramosRetencion = new ArrayList<TramosRetencionForm>();
		
		tramosRetencionForm.setTramosRetencion(alTramosRetencion);
		return forward;
	}
	protected void getAjaxImporteRetencion(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		TramosRetencionForm miForm = (TramosRetencionForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		//Sacamos las guardias si hay algo selccionado en el turno
		String anio = miForm.getAnio();
		String numMeses = miForm.getNumeroMeses();
		String importe = miForm.getImporte();
		
		FcsTramosRetencionAdm admFcsTramosRetencionAdm = new FcsTramosRetencionAdm(usr);
		try {
			Double importeRetencion = admFcsTramosRetencionAdm.getImporteRetenido(importe,anio,usr.getLocation(),numMeses);
			if(importeRetencion==-1)
				importeRetencion = (double)0;
			miForm.setImporteRetencion(UtilidadesNumero.formato(importeRetencion));
			List listaParametros = new ArrayList();
			listaParametros.add(miForm.getImporteRetencion());
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		} catch (SIGAException e) {
			List listaParametros = new ArrayList();
			listaParametros.add("0");
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		}
	}

	protected void getAjaxSmi(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		TramosRetencionForm miForm = (TramosRetencionForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		//Sacamos las guardias si hay algo selccionado en el turno
		String anio = miForm.getAnio();
		UsrBean usrBean = this.getUserBean(request);
		FcsTramosRetencionAdm admFcsTramosRetencionAdm = new FcsTramosRetencionAdm(usr);
		try {
			Double smi = admFcsTramosRetencionAdm.getSmi(anio);
			miForm.setSmi(UtilidadesNumero.formato(smi));
			List listaParametros = new ArrayList();
			listaParametros.add(miForm.getSmi());
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		} catch (SIGAException e) {
			List listaParametros = new ArrayList();
			listaParametros.add("0");

			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		}
		
		
	}
	
	
}