package com.siga.facturacionSJCS.action;


import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;


import com.siga.beans.FcsCobrosRetencionJudicialAdm;
import com.siga.facturacionSJCS.form.BusquedaRetencionesAplicadasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class BusquedaRetencionesAplicadasAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						
						break;
					}else if (accion.equalsIgnoreCase("generaExcel")) {
						mapDestino = generaExcel(mapping, miForm, request, response);
						break;
					} 
					else if (accion.equalsIgnoreCase("")) {
						mapDestino = "";
					} else {
						return super.executeInternal(mapping, formulario,
								request, response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
	}
	protected String generaExcel(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		BusquedaRetencionesAplicadasForm form = (BusquedaRetencionesAplicadasForm) formulario;
		//Vector vRetencionesAplicadas = form.getRetencionesAplicadas();
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		//Vector datos = new Vector();
		Integer idInstitucion = this.getIDInstitucion(request);
		String letrado = form.getLetrado();
		String fechaDesde = form.getFechaDesde();
		String fechaHasta = form.getFechaHasta();
		String idTipoRetencion = form.getIdTipoRetencion();
		String idDestinatario = form.getIdDestinatario();
		String pagoRelacionado = form.getPagoRelacionado();
		String abonoRelacionado = form.getAbonoRelacionado();
		
		
		try {
			
	        
	        
			
			
			
			
			
			FcsCobrosRetencionJudicialAdm cobrosRetencionAdm = new FcsCobrosRetencionJudicialAdm(user);
			Vector vRetencionesAplicadas = cobrosRetencionAdm.getRetencionesAplicadas(idInstitucion,letrado,
					fechaDesde,fechaHasta,idTipoRetencion,idDestinatario,pagoRelacionado,abonoRelacionado,user,true);
						
			
			String[] cabeceras = new String[]{
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.letrado"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.tipoRetencion"),
				
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.destinatarioRetencion"),
				 UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.fechaDesde"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.fechaHasta"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.fechaRetencion"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.abonoRelacionado"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.pagoRelacionado"),
				UtilidadesString.getMensajeIdioma(user, "factSJCS.busquedaRetAplicadas.literal.importeRetenido")};
			
			
			String[] campos = new String[]{"NOMBRE","DESCTIPORETENCION","NOMBREDESTINATARIO","FECHAINICIO","FECHAFIN",
					"FECHARETENCION","ABONORELACIONADO","PAGORELACIONADO","IMPORTERETENIDO"	};
				//
			request.setAttribute("campos",campos);
			request.setAttribute("datos",vRetencionesAplicadas);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", "RetencionesAplicadas_"+idInstitucion);
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}
	
		return "generaExcel";
		
		
		
		
	}
	
	
	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		BusquedaRetencionesAplicadasForm form = (BusquedaRetencionesAplicadasForm) formulario;

		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Integer idInstitucion = this.getIDInstitucion(request);
		String letrado = form.getLetrado();
		String fechaDesde = form.getFechaDesde();
		String fechaHasta = form.getFechaHasta();
		String idTipoRetencion = form.getIdTipoRetencion();
		String idDestinatario = form.getIdDestinatario();
		String pagoRelacionado = form.getPagoRelacionado();
		String abonoRelacionado = form.getAbonoRelacionado();
		
		
		
		
		FcsCobrosRetencionJudicialAdm cobrosRetencionAdm = new FcsCobrosRetencionJudicialAdm(user);
		Vector vRowsRetencionesAplicadas = cobrosRetencionAdm.getRetencionesAplicadas(idInstitucion,letrado,
				fechaDesde,fechaHasta,idTipoRetencion,idDestinatario,pagoRelacionado,abonoRelacionado,user,false);
		/*FcsCobrosRetencionJudicialBean retencionAplicada = new FcsCobrosRetencionJudicialBean();
		FcsRetencionesJudicialesBean retencionJudicial = new FcsRetencionesJudicialesBean();
		CenPersonaBean persona = new CenPersonaBean();
		persona.setNombre("Juan");
		persona.setApellido1("Garcia");
		persona.setApellido2("Feijoo");
		FcsDestinatariosRetencionesBean destinatarioRetencion = new FcsDestinatariosRetencionesBean();
		destinatarioRetencion.setNombre("CGPJ");
		String abonoRelacionadoReal = "A202020961";
		String pagoRelacionadoReal = "9898928302878";
		String tipoRetencion = "Porcentaje";
		
		
		retencionJudicial.setDestinatarioRetencion(destinatarioRetencion);
		retencionJudicial.setFechaInicio("10-10-2008");
		retencionJudicial.setFechaFin("11-10-2009");
		retencionJudicial.setTipoRetencion(tipoRetencion);
		
		retencionAplicada.setRetencionJudicial(retencionJudicial);
		retencionAplicada.setPersona(persona);
		retencionAplicada.setFechaRetencion("10-02-2009");
		retencionAplicada.setImporteRetenido(new Double("10.87"));
		retencionAplicada.setAbonoRelacionado(abonoRelacionadoReal);
		retencionAplicada.setPagoRelacionado(pagoRelacionadoReal);
		
		
		vRowsRetencionesAplicadas.add(retencionAplicada);*/
		form.setRetencionesAplicadas(vRowsRetencionesAplicadas);
		
	
		
		return "resultado";
	}
	

	
	

}