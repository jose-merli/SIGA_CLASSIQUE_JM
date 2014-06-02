package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasProcuradorAdm;
import com.siga.beans.ScsDesignasProcuradorBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CambiosProcuradoresDesignasForm;
import com.siga.ws.CajgConfiguracion;


/**
 * @author ruben.fernandez
 * @since 9/2/2005* 
 */

public class CambiosProcuradoresDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else return super.executeInternal(mapping, formulario, request, response);
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
	}
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN"); 
		HttpSession ses = request.getSession();
		//Recogemos de la pestanha la designa insertada o la que se quiere consultar
		//y los usamos para la consulta y además hacemos una hashtable y lo guardamos en session
		Hashtable designaActual = new Hashtable();
		
		try {
			if((String)request.getParameter("ANIO")!=null){
				UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO, 		(String)request.getParameter("ANIO"));
				UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO, 		(String)request.getParameter("NUMERO"));
				UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,(String)usr.getLocation());
				UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,		(String)request.getParameter("IDTURNO"));
			}else{
				designaActual = (Hashtable)ses.getAttribute("designaActual");
			}
			
			
			ScsDesignasProcuradorAdm adm = new ScsDesignasProcuradorAdm(this.getUserBean(request));
			
			String consultaProcuradores = 
				" select "+
				" p."+ScsProcuradorBean.C_NOMBRE+","+
				" p."+ScsProcuradorBean.C_APELLIDO1+","+
				" p."+ScsProcuradorBean.C_APELLIDO2+","+
				" p."+ScsProcuradorBean.C_NCOLEGIADO+","+
				" dp."+ScsDesignasProcuradorBean.C_IDINSTITUCION+","+
				" dp."+ScsDesignasProcuradorBean.C_IDTURNO+","+
				" dp."+ScsDesignasProcuradorBean.C_NUMERO+","+
				" dp."+ScsDesignasProcuradorBean.C_ANIO+","+
				" dp."+ScsDesignasProcuradorBean.C_IDPROCURADOR+","+
				" dp."+ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC+","+
				" dp."+ScsDesignasProcuradorBean.C_FECHADESIGNA+","+
				" dp."+ScsDesignasProcuradorBean.C_NUMERODESIGNACION+","+
				" dp."+ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA+","+
				" dp."+ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA+","+
				" dp."+ScsDesignasProcuradorBean.C_FECHARENUNCIA+
				" from scs_procurador p, scs_designa d, scs_designaprocurador dp " +
				" where dp.idinstitucion = d.idinstitucion " +
				" and   dp.idturno = d.idturno " +
				" and   dp.numero = d.numero " +
				" and   dp.anio = d.anio " +
				" and   dp.idprocurador = p.idprocurador " +
				" and   dp.idinstitucion_proc = p.idinstitucion " +
				" and dp."+ScsDesignasProcuradorBean.C_ANIO+"="+(String)designaActual.get("ANIO")+
				" and dp."+ScsDesignasProcuradorBean.C_NUMERO+"=" +(String)designaActual.get("NUMERO")+
				" and dp."+ScsDesignasProcuradorBean.C_IDTURNO+"=" +(String)designaActual.get("IDTURNO")+
				" and dp."+ScsDesignasProcuradorBean.C_IDINSTITUCION+"=" +(String)designaActual.get(ScsDesignaBean.C_IDINSTITUCION)+
				" order by dp."+ScsDesignasProcuradorBean.C_FECHADESIGNA+" DESC";
			
			Vector resultado = (Vector)adm.selectGenerico(consultaProcuradores);
			request.setAttribute("resultado",resultado);
			ses.setAttribute("designaActual",designaActual);
			request.setAttribute("modo",(String)request.getParameter("modo"));
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "inicio";
	}
    

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
	
		String result=ver(mapping, formulario,request, response);
		
		// jbd 01/02/2010 Pasamos el valor del pcajg del colegio
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
		request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
		request.setAttribute("accion","editar");
		
		return result;
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {

		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		CambiosProcuradoresDesignasForm miform = (CambiosProcuradoresDesignasForm)formulario;
		HttpSession ses = request.getSession();
		Hashtable hash = (Hashtable)ses.getAttribute("designaActual");
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String instit=usr.getLocation();
		String anio=(String)hash.get("ANIO");
		String numero=(String)hash.get("NUMERO");
		String turno=(String)hash.get("IDTURNO");
		String idPersona=(String)ocultos.get(0);
		String fechaDesigna=(String)ocultos.get(2);
		try {
			miform.setDatos(hash);
			String consultaDesigna = 
				" select"+
				" p."+ScsProcuradorBean.C_NOMBRE+","+
				" p."+ScsProcuradorBean.C_APELLIDO1+","+
				" p."+ScsProcuradorBean.C_APELLIDO2+","+
				" p."+ScsProcuradorBean.C_NCOLEGIADO+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDINSTITUCION+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDTURNO+","+
				" dp."+ ScsDesignasProcuradorBean.C_ANIO+","+
				" dp."+ ScsDesignasProcuradorBean.C_NUMERO+","+ 
				" dp."+ ScsDesignasProcuradorBean.C_IDPROCURADOR+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC+","+
				" dp."+ ScsDesignasProcuradorBean.C_FECHADESIGNA+","+
				" dp."+ ScsDesignasProcuradorBean.C_FECHARENUNCIA+", " +
				" dp."+ ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA+", " +
				" dp."+ ScsDesignasProcuradorBean.C_NUMERODESIGNACION+", " +
				" dp."+ ScsDesignasProcuradorBean.C_IDTIPOMOTIVO+", " +
				" dp."+ ScsDesignasProcuradorBean.C_OBSERVACIONES+
				" from scs_procurador p, scs_designa d, scs_designaprocurador dp " +
				" where dp.idinstitucion = d.idinstitucion " +
				" and   dp.idturno = d.idturno " +
				" and   dp.numero = d.numero " +
				" and   dp.anio = d.anio " +
				" and   dp.idprocurador = p.idprocurador " +
				" and   dp.idinstitucion_proc = p.idinstitucion " +
				" and dp."+ScsDesignasProcuradorBean.C_ANIO+"="+anio+
				" and dp."+ScsDesignasProcuradorBean.C_NUMERO+"="+numero+
				" and dp."+ScsDesignasProcuradorBean.C_IDTURNO+"="+turno+
				" and dp."+ScsDesignasProcuradorBean.C_FECHADESIGNA+" = to_date('"+fechaDesigna+"','" + ClsConstants.DATE_FORMAT_SQL + "')";
			
			ScsDesignasProcuradorAdm designaAdm = new ScsDesignasProcuradorAdm (this.getUserBean(request));
			Vector vDesigna=(Vector)designaAdm.selectGenerico(consultaDesigna);
			if(vDesigna!=null && vDesigna.size()>0){
				Hashtable datos=(Hashtable)vDesigna.get(0);
				ses.setAttribute("DATABACKUP_CLD",datos);
				miform.setDatos(datos);
			}else{
				ses.removeAttribute("DATABACKUP_CLD");
				miform.setDatos(hash);
			}
			request.setAttribute("accion","ver");
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "edicion";

	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();
		Hashtable hash = (Hashtable)ses.getAttribute("designaActual");
		CambiosProcuradoresDesignasForm miform = (CambiosProcuradoresDesignasForm)formulario;
		String instit=usr.getLocation();
		String anio=(String)hash.get("ANIO");
		String numero=(String)hash.get("NUMERO");
		String turno=(String)hash.get("IDTURNO");
		
		// jbd 01/02/2010 Pasamos el valor del pcajg del colegio
		int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
		request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
		
		try {
			String consultaDesigna =
				" select"+
				" p."+ScsProcuradorBean.C_NOMBRE+","+
				" p."+ScsProcuradorBean.C_APELLIDO1+","+
				" p."+ScsProcuradorBean.C_APELLIDO2+","+
				" p."+ScsProcuradorBean.C_NCOLEGIADO+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDINSTITUCION+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDTURNO+","+
				" dp."+ ScsDesignasProcuradorBean.C_ANIO+","+
				" dp."+ ScsDesignasProcuradorBean.C_NUMERO+","+ 
				" dp."+ ScsDesignasProcuradorBean.C_IDPROCURADOR+","+
				" dp."+ ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC+","+
				" dp."+ ScsDesignasProcuradorBean.C_FECHADESIGNA+","+
				" dp."+ ScsDesignasProcuradorBean.C_FECHARENUNCIA+", " +
				" dp."+ ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA+", " +
				" dp."+ ScsDesignasProcuradorBean.C_NUMERODESIGNACION+", " +
				" dp."+ ScsDesignasProcuradorBean.C_IDTIPOMOTIVO+", " +
				" dp."+ ScsDesignasProcuradorBean.C_OBSERVACIONES+
				" from scs_procurador p, scs_designa d, scs_designaprocurador dp " +
				" where dp.idinstitucion = d.idinstitucion " +
				" and   dp.idturno = d.idturno " +
				" and   dp.numero = d.numero " +
				" and   dp.anio = d.anio " +
				" and   dp.idprocurador = p.idprocurador " +
				" and   dp.idinstitucion_proc = p.idinstitucion " +
				" and dp."+ScsDesignasProcuradorBean.C_ANIO+"="+anio+
				" and dp."+ScsDesignasProcuradorBean.C_NUMERO+"="+numero+
				" and dp."+ScsDesignasProcuradorBean.C_IDTURNO+"="+turno+
				" and dp."+ScsDesignasProcuradorBean.C_IDINSTITUCION+"="+instit+//inc-5342 falta filtro por institucion
				" and dp."+ScsDesignasProcuradorBean.C_FECHARENUNCIA+" is null ";
			
			ScsDesignasProcuradorAdm designaAdm = new ScsDesignasProcuradorAdm (usr);
			Vector vDesigna=(Vector)designaAdm.selectGenerico(consultaDesigna);
			if(vDesigna!=null && vDesigna.size()>0){
				Hashtable datos=(Hashtable)vDesigna.get(0);
				ses.setAttribute("DATABACKUP_CLD",datos);
				miform.setDatos(datos);
				request.setAttribute("NUEVOPROC", false);
			}else{
				ses.removeAttribute("DATABACKUP_CLD");
				miform.setDatos(hash);
				request.setAttribute("NUEVOPROC", true);
			}
			miform.setObservaciones("");
			miform.setNumeroDesigna("");
			
			ScsDesignaAdm desAdm = new ScsDesignaAdm(usr);
			Vector datosProcuradoresEJGRel = desAdm.getDatosProcuradoresEJGRelacionados(instit, numero, turno, anio);
			request.setAttribute("datosProcuradoresEJGRel", datosProcuradoresEJGRel);
			
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "nuevo";

		}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		com.siga.gratuita.form.CambiosProcuradoresDesignasForm miform = (CambiosProcuradoresDesignasForm)formulario;
		UserTransaction tx=null;
		boolean ok=false;
		
		Integer user=this.getUserName(request);
		String instit=usr.getLocation();
		String anio=miform.getAnio();
		String numero=miform.getNumero();
		String turno=miform.getIdTurno();
		String idProcurador=miform.getAplIdProcurador();
		String idInstProcurador=miform.getAplInstitProcurador();
		String numeroDesigna=miform.getNumeroDesigna();
		String fCambio=miform.getAplFechaDesigna();
		String motivo=miform.getIdTipoMotivo();
		String observ=miform.getObservaciones();
		String cambioMismoDia = request.getParameter("cambioMismoDia");
		
		try{
			
			tx=usr.getTransaction();
			tx.begin();
			ScsDesignasProcuradorAdm designaAdm = new ScsDesignasProcuradorAdm(this.getUserBean(request));
			Hashtable datos=(Hashtable)ses.getAttribute("DATABACKUP_CLD");
			if(datos!=null){

				// HASH DE MODIFICACION para el que actualmente estaba asignado
				Hashtable designaActual= (Hashtable)datos.clone();
				designaActual.put(ScsDesignasProcuradorBean.C_FECHARENUNCIA,fCambio);
				designaActual.put(ScsDesignasProcuradorBean.C_IDTIPOMOTIVO,motivo);
				
				if (cambioMismoDia != null && cambioMismoDia.equalsIgnoreCase("1")) {		
					if (!designaAdm.delete(designaActual))
						throw new ClsExceptions(designaAdm.getError());
				}else{
					ok=designaAdm.update(designaActual,datos);
					if (!ok) throw new ClsExceptions(designaAdm.getError());
				}
			}
			
			// HASH DE INSERCION para el nuevo
			Hashtable designaNueva = new Hashtable();
			designaNueva.put(ScsDesignasProcuradorBean.C_IDINSTITUCION,instit);
			designaNueva.put(ScsDesignasProcuradorBean.C_IDTURNO,turno);
			designaNueva.put(ScsDesignasProcuradorBean.C_NUMERO,numero);
			designaNueva.put(ScsDesignasProcuradorBean.C_ANIO,anio);
			designaNueva.put(ScsDesignasProcuradorBean.C_IDPROCURADOR,idProcurador);
			designaNueva.put(ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,idInstProcurador);
			designaNueva.put(ScsDesignasProcuradorBean.C_FECHADESIGNA, fCambio);		
			designaNueva.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION,numeroDesigna);
			designaNueva.put(ScsDesignasProcuradorBean.C_OBSERVACIONES,observ);
			
			ok=designaAdm.insert(designaNueva);
			if (!ok) throw new ClsExceptions(designaAdm.getError());
			

			// Se cierra la transacción
			tx.commit();		
			
			//CR7 - Actualizamos el procurador a las designaciones relacionadas
			if(miform.getActualizaProcuradores() != null && miform.getActualizaProcuradores().equals("1")){
				ScsDesignaAdm desAdm = new ScsDesignaAdm(usr);
				ScsEJGAdm ejgAdm = new ScsEJGAdm(usr);
				Vector datosProcuradoresEJGRel = desAdm.getDatosProcuradoresEJGRelacionados(instit, numero, turno, anio);	
				ejgAdm.actualizarProcuradoresEJG(datosProcuradoresEJGRel,miform.getAplIdProcurador(),miform.getAplInstitProcurador());
			}			
			
		
			
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoModal("messages.updated.success",request);
		
	}
	
	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		CambiosProcuradoresDesignasForm miform = (CambiosProcuradoresDesignasForm)formulario;
		UserTransaction tx=null;
		boolean ok=false;
		
		Integer user=this.getUserName(request);
		String fRenuncia=miform.getAplFechaRenunciaSolicita();
		String motivo=miform.getIdTipoMotivo();
		String observ=miform.getObservaciones();
		String numeroDesigna=miform.getNumeroDesigna();
		try{
			tx=usr.getTransaction();
			tx.begin();
			ScsDesignasProcuradorAdm designaAdm = new ScsDesignasProcuradorAdm(this.getUserBean(request));
			Hashtable datos=(Hashtable)ses.getAttribute("DATABACKUP_CLD");
		
			// HASH DE INSERCION para el nuevo
			Hashtable designaNueva = (Hashtable)datos.clone();
			designaNueva.put(ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA, fRenuncia);			
			designaNueva.put(ScsDesignasProcuradorBean.C_IDTIPOMOTIVO,motivo);
			designaNueva.put(ScsDesignasProcuradorBean.C_OBSERVACIONES,observ);
			designaNueva.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION,numeroDesigna);
			
			ok=designaAdm.update(designaNueva,datos);
			if (!ok) throw new ClsExceptions(designaAdm.getError());
			// Se cierra la transacción
			tx.commit();		
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoModal("messages.updated.success",request);
	}



}