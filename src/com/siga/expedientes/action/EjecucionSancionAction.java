/*
 * Created on Feb 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.ExpExpedienteAdm;
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
		
			//Comienzo la transaccion:
			tx.begin();
			/*//aalg:se modifica primero el estado del denunciado que est� grabado en exp_expediente
			if (form.isBajaTurno()){
				ScsInscripcionTurnoAdm tAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
				//Obligo a que no use internamente una transaccion.
				 tAdm.insertarBajaEnTurnos(idPersona,idInstitucion,form.getMotivo());
			}
			if (form.isBajaColegial()){
				CenDatosColegialesEstadoAdm c1Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
				c1Adm.insertarBajaColegial(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
			}
			if (form.isBajaEjercicio()){
				CenDatosColegialesEstadoAdm c2Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
				c2Adm.insertarBajaEnEjercicio(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
			}
			if (form.isInhabilitacion()){
				CenDatosColegialesEstadoAdm c3Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
				c3Adm.insertarInhabilitacion(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
			}
			if (form.isSuspension()){
				CenDatosColegialesEstadoAdm c3Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
				c3Adm.insertarSuspension(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
			}*/
			//aalg: se buscan los denunciados para modificarles el estado
			String idPersona;
			ExpExpedienteAdm exp = new ExpExpedienteAdm(this.getUserBean(request));
			Vector vDenunciado = exp.getDenunciados(idInstitucion, idInstitucion_tipoExpediente.toString(), anioExpediente.toString(), numeroExpediente.toString(), IdTipoExpediente.toString());
				
			for (int  j = 0; j < vDenunciado.size(); j++){
				Hashtable h = new Hashtable();
				h = (Hashtable)vDenunciado.get(j);
				idPersona = (String)h.get("IDPERSONA");
				if (form.isBajaTurno()){
					ScsInscripcionTurnoAdm tAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
					//Obligo a que no use internamente una transaccion.
					 tAdm.insertarBajaEnTurnos(idPersona,idInstitucion,form.getMotivo());
				}
				if (form.isBajaColegial()){
					CenDatosColegialesEstadoAdm c1Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
					c1Adm.insertarBajaColegial(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
				}
				if (form.isBajaEjercicio()){
					CenDatosColegialesEstadoAdm c2Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
					c2Adm.insertarBajaEnEjercicio(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
				}
				if (form.isInhabilitacion()){
					CenDatosColegialesEstadoAdm c3Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
					c3Adm.insertarInhabilitacion(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
				}
				if (form.isSuspension()){
					CenDatosColegialesEstadoAdm c3Adm = new CenDatosColegialesEstadoAdm(this.getUserBean(request));
					c3Adm.insertarSuspension(idPersona,idInstitucion,form.getMotivo(),this.getLenguaje(request));
				}
				
			}
			//Fin de la transaccion:
		    tx.commit();
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx); 
		}
		
		return exitoModal("messages.updated.success",request);
	}
}
