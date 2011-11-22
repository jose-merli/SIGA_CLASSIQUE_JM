/*
 * Created on Jan 20, 2005
 * @author emilio.grau
 */
package com.siga.expedientes.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.form.ExpResolucionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la resolución de un expediente
 */
public class ExpResolucionAction extends MasterAction {
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			ExpResolucionForm form = (ExpResolucionForm)formulario;
			HttpSession ses = request.getSession();
			
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			
			if(numExpediente==null || numExpediente.equals(""))
	           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	   
			if(anioExpediente==null || anioExpediente.equals(""))
	           anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");			
			
			String editable = request.getParameter("editable");
			boolean edit = editable.equals("1")?true:false;
			
			//vectores para almacenar el resultado de las select concretas
			Vector datosExp = null;
			
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));	
					
			Hashtable hashExp = new Hashtable();		
			hashExp.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			hashExp.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hashExp.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hashExp.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
			hashExp.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
			datosExp =expAdm.select(hashExp);
			ExpExpedienteBean bean = (ExpExpedienteBean)datosExp.elementAt(0);
			
			//Si estamos en edición, recuperamos el bean para poner en backup
//			HashMap datosExpediente=new HashMap();
//			
//			if (edit){			
//				if (ses.getAttribute("DATABACKUP")!=null){
//					 datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
//				}
//				datosExpediente.put("datosParticulares",bean);
//				ses.setAttribute("DATABACKUP",datosExpediente);
//			}		
//			
			//Recuperamos el nombre del denunciado        
	        CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
	        Hashtable hashIdPers = new Hashtable();		
			hashIdPers.put(CenPersonaBean.C_IDPERSONA,bean.getIdPersona());
	        Vector vPersona = personaAdm.selectByPK(hashIdPers);
	        CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);
	        String nombrePersona = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
	        request.setAttribute("denunciado", nombrePersona);
			
			//Hacemos los sets del formulario
			form.setResDescripcion(bean.getDescripcionResolucion()!=null ? bean.getDescripcionResolucion():"");
			form.setSancionado(bean.getSancionado().equals("S")?true:false);
			form.setSancionPrescrita(bean.getSancionPrescrita()!=null?GstDate.getFormatedDateShort("",bean.getSancionPrescrita()):"");
			form.setSancionFinalizada(bean.getSancionFinalizada()!=null?GstDate.getFormatedDateShort("",bean.getSancionFinalizada()):"");
			form.setActuacionesPrescritas(bean.getActuacionesPrescritas()!=null?GstDate.getFormatedDateShort("",bean.getActuacionesPrescritas()):"");
			form.setAnotacionesCanceladas(bean.getAnotacionesCanceladas()!=null?GstDate.getFormatedDateShort("",bean.getAnotacionesCanceladas()):"");
			
			form.setVisible(bean.getEsVisible().equals(ExpCampoTipoExpedienteBean.si)?true:false);
			form.setVisibleEnFicha(bean.getEsVisibleEnFicha().equals("S")?true:false);
			
			form.setFechaResolucion(bean.getFechaResolucion()!=null?GstDate.getFormatedDateShort("",bean.getFechaResolucion()):"");
			if (bean.getIdResultadoJuntaGobierno() != null)
				request.setAttribute("resultadoInforme", ""+bean.getIdResultadoJuntaGobierno());
			
			// Aplicar visibilidad a campos
			ExpCampoTipoExpedienteAdm adm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_RESULTADO_INFORME)); // Resultado Informe
			Vector v = adm.select(h);
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("mostrarResultadoInforme", b.getVisible());
			}

	        String denunciado="";
			//Chapuza para identificar si se llama denunciante o impugnante
			StringBuffer sbWhere= new StringBuffer("where ");
			sbWhere.append(ExpCampoTipoExpedienteBean.C_IDINSTITUCION).append("=").append(idInstitucion)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE).append("=").append(idTipoExpediente)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDCAMPO).append("=").append(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_NOMBRE).append(" like ").append("'%impugnante%'");
			ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
			Vector resultado=campoTipoExpedienteAdm.select(sbWhere.toString());
	        if (resultado!=null && resultado.size()>0) {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.impugnado");
	    	} else {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.denunciado");
	        }

	        StringBuffer tituloVentana = new StringBuffer();
	        tituloVentana.append(denunciado).append(": ").append(nombrePersona).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.tipo"))
	        			 .append(": ").append((String)request.getParameter("nombreTipoExpediente")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.nexpediente"))
	        			 .append(": ").append((String)request.getParameter("anioExpediente")).append(" / ").append((String)request.getParameter("numeroExpediente"));

	        form.setTituloVentana(tituloVentana.toString());
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return("inicio");
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try{
		    // Recuperamos los datos anteriores de la sesión
		    HttpSession ses=request.getSession();
		    HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
		    ExpExpedienteBean expBean=(ExpExpedienteBean)datosExpediente.get("datosParticulares");	     
		    
		    ExpResolucionForm form = (ExpResolucionForm)formulario;
	        ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));
	        
	        // Actualizamos los datos del expediente
	        expBean.setDescripcionResolucion(form.getResDescripcion());
	        expBean.setSancionPrescrita(form.getSancionPrescrita().equals("")?"":GstDate.getApplicationFormatDate("",form.getSancionPrescrita()));
	        expBean.setActuacionesPrescritas(form.getActuacionesPrescritas().equals("")?"":GstDate.getApplicationFormatDate("",form.getActuacionesPrescritas()));
	        expBean.setSancionFinalizada(form.getSancionFinalizada().equals("")?"":GstDate.getApplicationFormatDate("",form.getSancionFinalizada()));
	        expBean.setEsVisible(form.isVisible()?"S":"N");
	        expBean.setEsVisibleEnFicha(form.isVisibleEnFicha()?"S":"N");
	        expBean.setSancionado(form.isSancionado()?"S":"N");
	        expBean.setAnotacionesCanceladas(form.getAnotacionesCanceladas().equals("")?"":GstDate.getApplicationFormatDate("",form.getAnotacionesCanceladas()));
	        expBean.setFechaResolucion(form.getFechaResolucion().equals("")?"":GstDate.getApplicationFormatDate("",form.getFechaResolucion()));
			if (form.getResultadoInforme()!= null && !form.getResultadoInforme().equals(""))
				expBean.setIdResultadoJuntaGobierno(new Integer(form.getResultadoInforme()));  

	        /*if (expAdm.update(expBean)){
	        	return exitoRefresco("messages.updated.success",request);
	        }else{
	        	return exito("messages.updated.error",request);
	        }*/
	        expAdm.update(expBean);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoRefresco("messages.updated.success",request);
	}
}
