/*
 * Created on Feb 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.CenDatosColegialesEstadoBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.expedientes.form.EjecucionSancionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para las acciones en caso de que el estado tenga ESEJECUCIONSANCION=S
 */
public class EjecucionSancionAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
		return "inicio";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
		UserTransaction tx = null;
		int estado = 0;
		StringBuilder messageLlamadaWebServiceAcaRevisionLetrado = new StringBuilder();
		try {
			
			UsrBean userBean = (UsrBean)request.getSession().getAttribute(("USRBEAN"));
			tx = userBean.getTransactionPesada();		
			EjecucionSancionForm form =  (EjecucionSancionForm)formulario;
			
			Hashtable hEjSancion = (Hashtable)request.getSession().getAttribute("ejecucionSancion");
			String idInstitucion=(String)hEjSancion.get("idInstitucion");
			//String idPersona=(String)hEjSancion.get("idPersona");
			Integer idInstitucion_tipoExpediente = (Integer)hEjSancion.get("IdInstitucion_tipoExpediente");
			Integer IdTipoExpediente = (Integer)hEjSancion.get("IdTipoExpediente");
			Integer numeroExpediente=(Integer)hEjSancion.get("numeroExpediente");
			Integer anioExpediente=(Integer)hEjSancion.get("anioExpediente");
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm(userBean); 
			ExpExpedienteBean expedienteBean =  expedienteAdm.getExpediente(IdTipoExpediente.toString(), idInstitucion, idInstitucion_tipoExpediente.toString(),
					anioExpediente.toString(), numeroExpediente.toString());
			//Comienzo la transaccion:
			tx.begin();
			
			String idPersona;
			ExpExpedienteAdm exp = new ExpExpedienteAdm(this.getUserBean(request));
			Vector vDenunciado = exp.getDenunciados(idInstitucion, idInstitucion_tipoExpediente.toString(), anioExpediente.toString(), numeroExpediente.toString(), IdTipoExpediente.toString());
			
			for (int  j = 0; j < vDenunciado.size(); j++){
				Hashtable h = new Hashtable();
				h = (Hashtable)vDenunciado.get(j);
				idPersona = (String)h.get("IDPERSONA");
				
				if (form.isBajaTurno()){					
					// Obligo a que no use internamente una transaccion.
					ScsInscripcionTurnoAdm tAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
					SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
					
					tAdm.cancelarInscripcionesTurnosPersona(idPersona, idInstitucion, form.getMotivo(), GstDate.getFormatedDateShort(sdf.parse(expedienteBean.getFechaInicialEstado())));
				}
				
				Hashtable<String,String> estadoColegialHashtable = new Hashtable<String, String>();
				estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, idInstitucion);
				estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idPersona);
				estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, expedienteBean.getFechaInicialEstado());
				
				// construyendo el idestado y el motivo
				String motivo = "";
				if (form.isBajaColegial()){
					estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDESTADO, ""+ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL);
					motivo = UtilidadesString.getMensajeIdioma(userBean, "expedientes.alertas.literal.motivo2");
				}
				if (form.isBajaEjercicio()){
					estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDESTADO, ""+ClsConstants.ESTADO_COLEGIAL_SINEJERCER);
					motivo = UtilidadesString.getMensajeIdioma(userBean, "expedientes.alertas.literal.motivo3");
				}
				if (form.isInhabilitacion()){
					estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDESTADO, ""+ClsConstants.ESTADO_COLEGIAL_INHABILITACION);
					motivo = UtilidadesString.getMensajeIdioma(userBean, "expedientes.alertas.literal.motivo1");
				}
				if (form.isSuspension()){
					estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_IDESTADO, ""+ClsConstants.ESTADO_COLEGIAL_SUSPENSION);
					motivo = UtilidadesString.getMensajeIdioma(userBean, "expedientes.alertas.literal.motivo4");
				}
				motivo += " " + form.getMotivo();
				estadoColegialHashtable.put(CenDatosColegialesEstadoBean.C_OBSERVACIONES, motivo);
				
				CenDatosColegialesEstadoAdm cenDatosColegialesAdm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
				boolean bDesdeCGAE = false;
				estado = cenDatosColegialesAdm.insertaEstadoColegial(estadoColegialHashtable, bDesdeCGAE, this.getLenguaje(request));
				
				if (form.isBajaColegial() || form.isInhabilitacion() ||form.isSuspension()){
					if(estado!=2 && estadoColegialHashtable.get("RESPUESTA_ACA")!=null){
						messageLlamadaWebServiceAcaRevisionLetrado.append(estadoColegialHashtable.get("RESPUESTA_ACA"));
						messageLlamadaWebServiceAcaRevisionLetrado.append(" ");
					}
					
				}
			}
			//Fin de la transaccion:
		    tx.commit();
		    
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.expediente"}, e, tx); 
		}
		if(estado==0){
			return exitoModal("messages.updated.success",request);
		}
		else{
			String[] parametros = {"","","",""};
			// informando de fin correcto y de cosas SJCS pendientes
			parametros[0] = "success";
			parametros[1] = UtilidadesString.getMensajeIdioma(this.getUserBean(request), "messages.updated.success");
			if(estado==2){
				parametros[3] =  UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.bajacolegial.errorNotificacionAca");
				parametros[0] = "error";
			}else if(messageLlamadaWebServiceAcaRevisionLetrado.length()>0){
				parametros[3] =  messageLlamadaWebServiceAcaRevisionLetrado.toString();
				parametros[0] = "notice";
				
			}
			request.setAttribute("parametrosArray", parametros);
			request.setAttribute("modal", "");
			return "exitoParametros";
		}
	}
	
	
}
