package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;

import com.siga.general.*;


public class ListadoSOJEnEJGAction extends MasterAction {

	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
	}

	public String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException 
	{		
		//DefinirSOJForm miForm = (DefinirSOJForm) formulario;		
// 		UsrBean usr= this.getUserBean(request);
// 		ScsEJGBean scsEJGBean = new ScsEJGBean ();
// 		
// 		String numeroEJG="";
// 	
// 		numeroEJG=scsEJGBean.getNumEJG();
// 		if(numeroEJG==null){
// 			Hashtable miHash = (Hashtable)request.getAttribute("DATOSEJG");
// 			numeroEJG = miHash.get("NUMERO").toString();
// 		}
 		
 		String idInstitucion = "" + this.getIDInstitucion(request);
		String anio = request.getParameter("ANIO").toString();
		String numero = request.getParameter("NUMERO").toString();
		String idTipoEJG = request.getParameter("IDTIPOEJG").toString();	

 		Hashtable h = new Hashtable();
 		UtilidadesHash.set(h, ScsEJGBean.C_ANIO, anio);
 		UtilidadesHash.set(h, ScsEJGBean.C_NUMERO, numero);
 		UtilidadesHash.set(h, ScsEJGBean.C_IDTIPOEJG, idTipoEJG);
 		UtilidadesHash.set(h, ScsEJGBean.C_IDINSTITUCION, idInstitucion);
		
 		ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
		Vector v = admBean.selectByPK(h);
		if (v != null)
			request.setAttribute("resultado",v);

		return "inicio";

		
//		//Hashtable miHash = new Hashtable();
//		//miHash = miForm.getDatos();		
//		
//		try{
//			String consulta = "select SOJ_IDTIPOSOJ A, SOJ_NUMERO B,SOJ_ANIO C from scs_ejg t where NUMERO LIKE '"+numeroEJG+"'";
//			
//		try {
//				v = admBean.selectGenerico(consulta);
//				request.setAttribute("resultado",v);			
//				
//			} catch (ClsExceptions e){
//				e.printStackTrace();
//			}		
//			return "inicio";
//		}catch (Exception e) { 	
//				throw new ClsExceptions(e,"Error obteniendo Expedientes SOJ"); 
//			}
//		
//		//return null;
	}
}
