

package com.siga.gratuita.action;


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsSOJBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class RelacionadoConSJCSAction extends MasterAction 
{
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{		
		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					return super.executeInternal(mapping,formulario,request,response);
				}
			} while (false);
			
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			String relacionesDe = (String)request.getParameter("conceptoE");

			Vector v = new Vector();
			Vector v2 = new Vector();			
			String anio = "", numero = "", idTipo = "", idInstitucion ="", modo = "";

			// Pestana en Designa
			if (relacionesDe == null || relacionesDe.equals("") || relacionesDe.equalsIgnoreCase("null")) {
				
				anio          = request.getParameter("ANIO");
				numero        = request.getParameter("NUMERO");
				idTipo        = request.getParameter("IDTURNO");
				idInstitucion = request.getParameter("IDINSTITUCION");
				
				ScsDesignaAdm designa = new ScsDesignaAdm (this.getUserBean(request));
				v = designa.getRelacionadoCon(idInstitucion, anio, numero, idTipo);
				
				// JBD INC_CAT_5 >>
				// Para recuperar el nombre del letrado de cada relacion hemos a�adido el IDLETRADO a getRelacionadoCon
				CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
				for (int i = 0; i < v.size(); i++){
					Hashtable h = new Hashtable();
					Vector vPersona = new Vector();
					String nombreLetrado = "";
					h = (Hashtable)v.get(i);
					// Recorremos el vector de relaciones y recuperamos el tipo
					String tipoSJCS = UtilidadesHash.getString(h, "SJCS");
					if (tipoSJCS.equalsIgnoreCase("DESIGNA")){
						// Si se trata de una designa sacamos el idTurno y el a�o de la designa para poder recuperar el letrado
						String idTurnoDes = UtilidadesHash.getString(h, "IDTURNO");
						String anioDes = UtilidadesHash.getString(h, "ANIO");
						ScsDesignaAdm desAdm = new ScsDesignaAdm(this.getUserBean(request));
						nombreLetrado = desAdm.getApeNomDesig(idInstitucion, idTurnoDes, anioDes, numero);
					}else{
						// Si se trata de una asistencia o SOJ buscamos al letrado en el censo
						String idLetrado = UtilidadesHash.getString(h, "IDLETRADO");
						if (idLetrado != null){
							vPersona = perAdm.getDatosPersonaTag(idInstitucion, idLetrado);
							if (vPersona.size()>0){
								nombreLetrado = UtilidadesHash.getString((Hashtable)vPersona.get(0),"NOMBRE");
							}
						}
					}
					UtilidadesHash.set(h, "NOMBRELETRADO", nombreLetrado);
					v.set(i, h);
				}
				// << JBD INC_CAT_5
				
				modo = (String)request.getSession().getAttribute("Modo");
			}
			
			// Pestana en EJG 
			else  if (relacionesDe.equalsIgnoreCase("EJG")) {
				
				anio          = request.getParameter("anioEJG");
				numero        = request.getParameter("numeroEJG");
				idTipo        = request.getParameter("idTipoEJG");
				idInstitucion = request.getParameter("idInstitucionEJG");
				int i = 0;
				ScsEJGAdm ejg = new ScsEJGAdm (this.getUserBean(request));
				ExpExpedienteAdm exp = new ExpExpedienteAdm (this.getUserBean(request));
				v = ejg.getRelacionadoCon(idInstitucion, anio, numero, idTipo);
				// JBD INC_CAT_5 >>
				// Para recuperar el nombre del letrado de cada relacion hemos a�adido el IDLETRADO a getRelacionadoCon
				CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
				for ( i = 0; i < v.size(); i++){
					Hashtable h = new Hashtable();
					Vector vPersona = new Vector();
					String nombreLetrado = "";
					h = (Hashtable)v.get(i);
					// Recorremos el vector de relaciones y recuperamos el tipo
					String tipoSJCS = UtilidadesHash.getString(h, "SJCS");
					if (tipoSJCS.equalsIgnoreCase("DESIGNA")){
						// Si se trata de una designa sacamos el idTurno y el a�o de la designa para poder recuperar el letrado
						String idTurnoDes = UtilidadesHash.getString(h, "IDTURNO");
						String anioDes = UtilidadesHash.getString(h, "ANIO");
						String numDes = UtilidadesHash.getString(h, "NUMERO"); 
						ScsDesignaAdm desAdm = new ScsDesignaAdm(this.getUserBean(request));
						nombreLetrado = desAdm.getApeNomDesig(idInstitucion, idTurnoDes, anioDes, numDes);
					}else{
						// Si se trata de una asistencia o SOJ buscamos al letrado en el censo
						String idLetrado = UtilidadesHash.getString(h, "IDLETRADO");
						if (idLetrado != null){
							vPersona = perAdm.getDatosPersonaTag(idInstitucion, idLetrado);
							if (vPersona.size()>0){
								nombreLetrado = UtilidadesHash.getString((Hashtable)vPersona.get(0),"NOMBRE");
							}
						}
					}
					UtilidadesHash.set(h, "NOMBRELETRADO", nombreLetrado);
					v.set(i, h);
				}

				v2 = exp.getRelacionadoConEjg(idInstitucion, anio, numero, idTipo);
				for (int  j = 0; j < v2.size(); j++){
					Hashtable h2 = new Hashtable();
					h2 = (Hashtable)v2.get(j);
					v.add(h2);
					i++;
				}
				// << JBD INC_CAT_5

				modo = (String)request.getSession().getAttribute("accion");
				GenParametrosAdm paramAdm = new GenParametrosAdm (this.getUserBean(request));
				String prefijoExpedienteCajg = paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
				request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			}
			
			request.setAttribute("modo", modo);			
			request.setAttribute("resultado", v);
		} 
		catch (SIGAException e1) {
			e1.printStackTrace();
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
		}
		return "inicio";
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UserTransaction tx = null;

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			
			String tipoSJCS      = (String)ocultos.get(0);
			String idInstitucion = (String)ocultos.get(1);
			String anio          = (String)ocultos.get(2);
			String numero        = (String)ocultos.get(3);
			String idTipo        = (String)ocultos.get(4);
			String idTurno        = (String)ocultos.get(5);
			String idTurnoDesigna = (String)ocultos.get(6);
			String numExp = (String)ocultos.get(7);
			String relacionesDe  = (String)request.getParameter("conceptoE");
	
			if (tipoSJCS == null || tipoSJCS.equals("")) {
				return error("messages.deleted.error",new ClsExceptions("messages.deleted.error"), request);
			}
			
			Hashtable h = new Hashtable ();

			tx = this.getUserBean(request).getTransaction();
			tx.begin();

			// Pestana en Designa
			if (relacionesDe == null || relacionesDe.equals("") || relacionesDe.equalsIgnoreCase("null")) {
				
				if (tipoSJCS.equalsIgnoreCase("ASISTENCIA")) {
					UtilidadesHash.set(h, ScsAsistenciasBean.C_ANIO, anio);
					UtilidadesHash.set(h, ScsAsistenciasBean.C_NUMERO, numero);
					UtilidadesHash.set(h, ScsAsistenciasBean.C_IDINSTITUCION, idInstitucion);
	
					UtilidadesHash.set(h, ScsAsistenciasBean.C_DESIGNA_ANIO, "");
					UtilidadesHash.set(h, ScsAsistenciasBean.C_DESIGNA_TURNO, "");
					UtilidadesHash.set(h, ScsAsistenciasBean.C_DESIGNA_NUMERO, "");
	
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (this.getUserBean(request));
					adm.updateDirect(h, null, new String [] {ScsAsistenciasBean.C_DESIGNA_ANIO, ScsAsistenciasBean.C_DESIGNA_TURNO, ScsAsistenciasBean.C_DESIGNA_NUMERO});
				}
				else if (tipoSJCS.equalsIgnoreCase("EJG")) {
										
					
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_ANIOEJG, anio);
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_NUMEROEJG, numero);
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDTIPOEJG, idTipo);
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDINSTITUCION, idInstitucion);
	
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_ANIODESIGNA, (String)request.getParameter("anio"));
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_NUMERODESIGNA, (String)request.getParameter("numero"));
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDTURNO, idTurnoDesigna);
	
					ScsEJGDESIGNAAdm adm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
					adm.delete(h);
	
				}
			}
			
			// Pestana en EJG 
			else if (relacionesDe.equalsIgnoreCase("EJG")) {

				if (tipoSJCS.equalsIgnoreCase("ASISTENCIA")) {
					UtilidadesHash.set(h, ScsAsistenciasBean.C_ANIO, anio);
					UtilidadesHash.set(h, ScsAsistenciasBean.C_NUMERO, numero);
					UtilidadesHash.set(h, ScsAsistenciasBean.C_IDINSTITUCION, idInstitucion);
	
					UtilidadesHash.set(h, ScsAsistenciasBean.C_EJGANIO, "");
					UtilidadesHash.set(h, ScsAsistenciasBean.C_EJGNUMERO, "");
					UtilidadesHash.set(h, ScsAsistenciasBean.C_EJGIDTIPOEJG, "");
	
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (this.getUserBean(request));
					adm.updateDirect(h, null, new String [] {ScsAsistenciasBean.C_EJGANIO, ScsAsistenciasBean.C_EJGNUMERO, ScsAsistenciasBean.C_EJGIDTIPOEJG});
				}
				else if (tipoSJCS.equalsIgnoreCase("DESIGNA")) {
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_ANIOEJG, (String)request.getParameter("anio"));
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_NUMEROEJG, (String)request.getParameter("numero"));
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDTIPOEJG, (String)request.getParameter("idTipo"));
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDINSTITUCION, idInstitucion);
	
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_ANIODESIGNA, anio);
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_NUMERODESIGNA, numero);
					UtilidadesHash.set(h, ScsEJGDESIGNABean.C_IDTURNO, idTurno);
	
					ScsEJGDESIGNAAdm adm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
					adm.delete(h);
					//adm.updateDirect(h, null, new String [] {ScsEJGBean.C_DESIGNA_ANIO, ScsEJGBean.C_DESIGNA_IDTURNO, ScsEJGBean.C_DESIGNA_NUMERO});
				}
				else if (tipoSJCS.equalsIgnoreCase("SOJ")) {
					UtilidadesHash.set(h, ScsSOJBean.C_ANIO, anio);
					UtilidadesHash.set(h, ScsSOJBean.C_NUMERO, numero);
					UtilidadesHash.set(h, ScsSOJBean.C_IDTIPOSOJ, idTipo);
					UtilidadesHash.set(h, ScsSOJBean.C_IDINSTITUCION, idInstitucion);
	
					UtilidadesHash.set(h, ScsSOJBean.C_EJGANIO, "");
					UtilidadesHash.set(h, ScsSOJBean.C_EJGNUMERO, "");
					UtilidadesHash.set(h, ScsSOJBean.C_EJGIDTIPOEJG, "");
	
					ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm (this.getUserBean(request));
					adm.updateDirect(h, null, new String [] {ScsSOJBean.C_EJGANIO, ScsSOJBean.C_EJGNUMERO, ScsSOJBean.C_EJGIDTIPOEJG});
				}else if (tipoSJCS.equalsIgnoreCase("EXPEDIENTE")) {
					ExpExpedienteAdm exp = new ExpExpedienteAdm (this.getUserBean(request));
					UtilidadesHash.set(h, ExpExpedienteBean.C_ANIOEXPEDIENTE, anio);
					UtilidadesHash.set(h, ExpExpedienteBean.C_NUMEROEXPEDIENTE, numExp);
					UtilidadesHash.set(h, ExpExpedienteBean.C_IDTIPOEXPEDIENTE, idTipo);
					UtilidadesHash.set(h, ExpExpedienteBean.C_IDINSTITUCION, idInstitucion);
					UtilidadesHash.set(h, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, idInstitucion);
					UtilidadesHash.set(h, ExpExpedienteBean.C_ANIOEJG, "");
					UtilidadesHash.set(h, ExpExpedienteBean.C_NUMEROEJG, "");
					UtilidadesHash.set(h, ExpExpedienteBean.C_IDTIPOEJG, "");
	
					exp.updateDirect(h, null, new String [] {ExpExpedienteBean.C_ANIOEJG, ExpExpedienteBean.C_NUMEROEJG, ExpExpedienteBean.C_IDTIPOEJG});
				}
	
				
				
			}

			tx.commit();
			return exitoRefresco("messages.deleted.success",request);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",e,tx);
			return error("messages.deleted.error",new ClsExceptions("messages.deleted.error"), request);
		}
	}
}
