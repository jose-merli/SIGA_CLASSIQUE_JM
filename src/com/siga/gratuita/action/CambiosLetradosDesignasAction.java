package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.BusquedaClientesFiltrosAdm;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CambiosLetradosDesignasForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005* 
 */

public class CambiosLetradosDesignasAction extends MasterAction {
	
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
			
			
			ScsDesignasLetradoAdm adm = new ScsDesignasLetradoAdm(this.getUserBean(request));
			
			String consultaContrarios = 
				" select "+
				" p."+CenPersonaBean.C_NOMBRE+","+
				" p."+CenPersonaBean.C_APELLIDOS1+","+
				" p."+CenPersonaBean.C_APELLIDOS2+","+
				" F_SIGA_CALCULONCOLEGIADO (c."+CenColegiadoBean.C_IDINSTITUCION+",c."+CenColegiadoBean.C_IDPERSONA+") "+CenColegiadoBean.C_NCOLEGIADO+","+
				" dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+","+
				" dp."+ScsDesignasLetradoBean.C_IDTURNO+","+
				" dp."+ScsDesignasLetradoBean.C_NUMERO+","+
				" dp."+ScsDesignasLetradoBean.C_ANIO+","+
				" dp."+ScsDesignasLetradoBean.C_IDPERSONA+","+
				" dp."+ScsDesignasLetradoBean.C_FECHADESIGNA+","+
				" dp."+ScsDesignasLetradoBean.C_FECHARENUNCIA+
				" from "+ 
				ScsDesignasLetradoBean.T_NOMBRETABLA+" dp,"+
				CenPersonaBean.T_NOMBRETABLA+" p,"+
				CenColegiadoBean.T_NOMBRETABLA+" c "+
				" where dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"=" +(String)usr.getLocation()+
				" and dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+" = c."+CenColegiadoBean.C_IDINSTITUCION+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" = c."+CenColegiadoBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" = p."+CenPersonaBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_ANIO+"="+(String)designaActual.get("ANIO")+
				" and dp."+ScsDesignasLetradoBean.C_NUMERO+"=" +(String)designaActual.get("NUMERO")+
				" and dp."+ScsDesignasLetradoBean.C_IDTURNO+"=" +(String)designaActual.get("IDTURNO")+
				" order by dp."+ScsDesignasLetradoBean.C_FECHADESIGNA+" DESC";
			
			Vector resultado = (Vector)adm.selectGenerico(consultaContrarios);
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
		CambiosLetradosDesignasForm miform = (CambiosLetradosDesignasForm)formulario;
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
				" p."+CenPersonaBean.C_NOMBRE+","+
				" p."+CenPersonaBean.C_APELLIDOS1+","+
				" p."+CenPersonaBean.C_APELLIDOS2+","+
				" F_SIGA_CALCULONCOLEGIADO (c."+CenColegiadoBean.C_IDINSTITUCION+",c."+CenColegiadoBean.C_IDPERSONA+") "+CenColegiadoBean.C_NCOLEGIADO+","+
				" dp."+ ScsDesignasLetradoBean.C_IDINSTITUCION+","+
				" dp."+ ScsDesignasLetradoBean.C_IDTURNO+","+
				" dp."+ ScsDesignasLetradoBean.C_ANIO+","+
				" dp."+ ScsDesignasLetradoBean.C_NUMERO+","+ 
				" dp."+ ScsDesignasLetradoBean.C_IDPERSONA+","+
				" dp."+ ScsDesignasLetradoBean.C_FECHADESIGNA+","+
				" dp."+ ScsDesignasLetradoBean.C_FECHARENUNCIA+", " +
				" dp."+ ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA+", " +
				" dp."+ ScsDesignasLetradoBean.C_MANUAL+", " +
				" dp."+ ScsDesignasLetradoBean.C_LETRADODELTURNO+", " +
				" dp."+ ScsDesignasLetradoBean.C_IDTIPOMOTIVO+", " +
				" dp."+ ScsDesignasLetradoBean.C_OBSERVACIONES+
				" from "+ 
				ScsDesignasLetradoBean.T_NOMBRETABLA+" dp,"+
				CenPersonaBean.T_NOMBRETABLA+" p,"+
				CenColegiadoBean.T_NOMBRETABLA+" c "+
				" where dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"="+instit+
				" and dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+" = c."+CenColegiadoBean.C_IDINSTITUCION+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" ="+idPersona+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =c."+CenColegiadoBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =p."+CenPersonaBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_ANIO+"="+anio+
				" and dp."+ScsDesignasLetradoBean.C_NUMERO+"="+numero+
				" and dp."+ScsDesignasLetradoBean.C_IDTURNO+"="+turno+
				" and dp."+ScsDesignasLetradoBean.C_FECHADESIGNA+" = to_date('"+fechaDesigna+"','" + ClsConstants.DATE_FORMAT_SQL + "')";
			
			ScsDesignasLetradoAdm designaAdm = new ScsDesignasLetradoAdm (this.getUserBean(request));
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
		CambiosLetradosDesignasForm miform = (CambiosLetradosDesignasForm)formulario;
		String instit=usr.getLocation();
		String anio=(String)hash.get("ANIO");
		String numero=(String)hash.get("NUMERO");
		String turno=(String)hash.get("IDTURNO");
		try {
			String consultaDesigna =
				" select"+
				" sd."+ ScsDesignaBean.C_CODIGO+","+
				" sd."+ ScsDesignaBean.C_SUFIJO+","+
				" p."+CenPersonaBean.C_NOMBRE+","+
				" p."+CenPersonaBean.C_APELLIDOS1+","+
				" p."+CenPersonaBean.C_APELLIDOS2+","+
				" F_SIGA_CALCULONCOLEGIADO (c."+CenColegiadoBean.C_IDINSTITUCION+",c."+CenColegiadoBean.C_IDPERSONA+") "+CenColegiadoBean.C_NCOLEGIADO+","+
				" dp."+ ScsDesignasLetradoBean.C_IDINSTITUCION+","+
				" dp."+ ScsDesignasLetradoBean.C_IDTURNO+","+
				" dp."+ ScsDesignasLetradoBean.C_ANIO+","+
				" dp."+ ScsDesignasLetradoBean.C_NUMERO+","+ 
				" dp."+ ScsDesignasLetradoBean.C_IDPERSONA+","+
				" dp."+ ScsDesignasLetradoBean.C_FECHADESIGNA+
				" from "+ 
				ScsDesignasLetradoBean.T_NOMBRETABLA+" dp, "+
				ScsDesignaBean.T_NOMBRETABLA+" sd, "+				
				CenPersonaBean.T_NOMBRETABLA+" p,"+
				CenColegiadoBean.T_NOMBRETABLA+" c "+
				" where dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"="+instit+
				" and dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+" = c."+CenColegiadoBean.C_IDINSTITUCION+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =c."+CenColegiadoBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =p."+CenPersonaBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_ANIO+"="+anio+
				" and dp."+ScsDesignasLetradoBean.C_NUMERO+"="+numero+
				" and dp."+ScsDesignasLetradoBean.C_IDTURNO+"="+turno+
				" and dp."+ScsDesignasLetradoBean.C_FECHARENUNCIA+" is null "+
			    " and dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"=sd."+ScsDesignaBean.C_IDINSTITUCION+
			    " and dp."+ScsDesignasLetradoBean.C_IDTURNO+"=sd."+ScsDesignaBean.C_IDTURNO+
			    " and dp."+ScsDesignasLetradoBean.C_ANIO+"=sd."+ScsDesignaBean.C_ANIO+
			    " and dp."+ScsDesignasLetradoBean.C_NUMERO+"=sd."+ScsDesignaBean.C_NUMERO;
			
			ScsDesignasLetradoAdm designaAdm = new ScsDesignasLetradoAdm (this.getUserBean(request));
			Vector vDesigna=(Vector)designaAdm.selectGenerico(consultaDesigna);
			if(vDesigna!=null && vDesigna.size()>0){
				Hashtable datos=(Hashtable)vDesigna.get(0);
				ses.setAttribute("DATABACKUP_CLD",datos);
				miform.setDatos(datos);
				//Controlo este valor que debe ir en la hash de sesion (se usa en un tag <bean:define> del JSP):
				if (hash.containsKey("IDTIPOMOTIVO"))
					miform.setIdTipoMotivo((String)hash.get("IDTIPOMOTIVO"));
				else
					miform.setIdTipoMotivo("");
			}else{
				ses.removeAttribute("DATABACKUP_CLD");
				miform.setDatos(hash);
			}
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "nuevo";

		}
	
	/** 
	 * Atiende la accion insertar: inserta el cambio de letrado y 
	 * realiza todas las operaciones asociadas
	 */
	protected synchronized String insertar (ActionMapping mapping,
											MasterForm formulario,
											HttpServletRequest request,
											HttpServletResponse response)
		throws ClsExceptions, SIGAException
	{
		//Controles generales
		HttpSession ses = request.getSession();
		UsrBean usr = this.getUserBean(request);
		ScsDesignasLetradoAdm designaAdm = new ScsDesignasLetradoAdm(usr);
		BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(usr);
		
		//Variables de salida
		String nombreColAutomatico = null;
		String numeroColAutomatico = null;		
		String mensaje = "";
		
		//Variables generales
		UserTransaction tx = null;
		
		try {
			//Datos de entrada
			CambiosLetradosDesignasForm miform = (CambiosLetradosDesignasForm) formulario;
			String idInstitucion = usr.getLocation();
			String anio = miform.getAnio();
			String numero = miform.getNumero();
			String idTurno = miform.getIdTurno();
			String idPersona = miform.getIdPersona();
		
			String fCambio = miform.getAplFechaDesigna();
			
			String motivo = miform.getIdTipoMotivo();
		
			String observaciones = miform.getObservaciones();
			String codigo =miform.getCodigo();	
			String sufijo =miform.getsufijo();	
			String mensajes="";
			 if (sufijo!=null && !sufijo.equals("")){ 
					 mensajes=codigo+"-"+sufijo;
			 }else{
				 	 mensajes=codigo;
			 }			
			String idPersonaSaliente = "";
			String compensacion = null;
			String flagSalto = request.getParameter("flagSalto");
			String compensacionActual = UtilidadesString
					.stringToBoolean(request.getParameter("compensacionActual")) ?
							ClsConstants.DB_TRUE :
							ClsConstants.DB_FALSE;
			String flagCompensacion = request.getParameter("flagCompensacion");
			String checkSalto = request.getParameter("checkSalto");
			String msgOrigen = UtilidadesString.getMensajeIdioma(usr,
					"gratuita.modalCambioLetradoDesigna.titulo");
			String motivoSalto = UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.insertarSaltoPor")
					+ " " + msgOrigen+".\n"+UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.numeroDesignacion")+": "+ mensajes;
			String motivoCompensacion = UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.insertarCompensacionPor")
					+ " " + msgOrigen+".\n"+UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.numeroDesignacion")+": "+ mensajes;
			String cambioMismoDia = request.getParameter("cambioMismoDia");
			//gratuita.literal.numeroDesignacion
			
			//iniciando transaccion
			tx = usr.getTransaction();
			tx.begin();
			
			
			//calculando letrado automatico si no ha sido seleccionado manualmente
			if (idPersona == null || idPersona.trim().equals("")) {
				Row row = new BusquedaClientesFiltrosAdm().gestionaDesignacionesAutomaticas(idInstitucion, idTurno, GstDate.getFormatedDateShort("", miform.getAplFechaDesigna()));
				idPersona = (String) row.getValue(ScsDesignasLetradoBean.C_IDPERSONA);
				compensacion = (String) row.getValue(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
				String nombre = (String) row.getValue(CenPersonaBean.C_NOMBRE);
				String apellido1 = (String) row.getValue(CenPersonaBean.C_APELLIDOS1);
				String apellido2 = (String) row.getValue(CenPersonaBean.C_APELLIDOS2);

				numeroColAutomatico = (String) row.getValue(CenColegiadoBean.C_NCOLEGIADO);
				if (apellido1 != null)
					nombreColAutomatico = apellido1;
				if (apellido2 != null) {
					if (nombreColAutomatico != null)
						nombreColAutomatico += " ";
					nombreColAutomatico += apellido2;
				}
				if (nombre != null) {
					if (nombreColAutomatico != null)
						nombreColAutomatico += ", ";
					nombreColAutomatico += nombre;
				}
			}
			
			//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
			CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
			Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersona), new Integer(idInstitucion));
			if(mBajasTemporalesConfirmador.containsKey(GstDate.getFormatedDateShort("", miform.getAplFechaDesigna()) ))
				throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
			
			//modificando designacion letrado anterior
			Hashtable<String, Object> datos = (Hashtable<String, Object>) ses
					.getAttribute("DATABACKUP_CLD");
			if (datos != null) {
				idPersonaSaliente = (String) datos.get(ScsDesignasLetradoBean.C_IDPERSONA);
				Hashtable<String, Object> designaActual = (Hashtable<String, Object>) datos.clone();
				designaActual.put(ScsDesignasLetradoBean.C_FECHARENUNCIA, fCambio);
				if (cambioMismoDia != null && cambioMismoDia.equalsIgnoreCase("1")) {
					ScsSaltosCompensacionesAdm saltosCompenAdm = new ScsSaltosCompensacionesAdm(usr);
					Hashtable<String, Object> saltosCompenHash = new Hashtable<String, Object>();
					String fecha = UtilidadesBDAdm.getFechaBD("");
					String motivos = UtilidadesString.getMensajeIdioma(usr,
							"gratuita.inicio_SaltosYCompensaciones.literal.motivo")
							+ " " + fecha+".\n"+UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.numeroDesignacion")+": "+mensajes;
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_MOTIVOS, motivos);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_IDPERSONA, idPersonaSaliente);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, ClsConstants.SALTOS);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,
							saltosCompenAdm.getNuevoIdSaltosTurno(idInstitucion, idTurno));
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_FECHA, fCambio);
					saltosCompenHash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, fCambio);
					if (!saltosCompenAdm.insert(saltosCompenHash))
						throw new ClsExceptions("Error insertando salto: " + saltosCompenAdm.getError());
					if (!designaAdm.delete(designaActual))
						throw new ClsExceptions(designaAdm.getError());
				}
				else {
					if (!designaAdm.updateDirect(
								designaActual,
								designaAdm.getClavesBean(),
								new String[] { ScsDesignasLetradoBean.C_FECHARENUNCIA }))
						throw new ClsExceptions(designaAdm.getError());
				}
			}
			
			//insertando designacion letrado nuevo
			Hashtable<String, Object> designaNueva = new Hashtable<String, Object>();
			designaNueva.put(ScsDesignasLetradoBean.C_IDINSTITUCION, idInstitucion);
			designaNueva.put(ScsDesignasLetradoBean.C_IDTURNO, idTurno);
			designaNueva.put(ScsDesignasLetradoBean.C_NUMERO, numero);
			designaNueva.put(ScsDesignasLetradoBean.C_ANIO, anio);
			designaNueva.put(ScsDesignasLetradoBean.C_IDPERSONA, idPersona);
			designaNueva.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fCambio);
			designaNueva.put(ScsDesignasLetradoBean.C_IDTIPOMOTIVO, motivo);
			designaNueva.put(ScsDesignasLetradoBean.C_MANUAL, ClsConstants.DB_FALSE);
			designaNueva.put(ScsDesignasLetradoBean.C_LETRADODELTURNO, ClsConstants.DB_FALSE);
			designaNueva.put(ScsDesignasLetradoBean.C_OBSERVACIONES, observaciones);
			if (!designaAdm.insert(designaNueva))
				throw new ClsExceptions(designaAdm.getError());
			
			//actualizando si ha sido un cambio manual o automatico
			admFiltros.actualizaManualDesigna(idInstitucion, idTurno,
					idPersona, anio, numero, flagSalto, flagCompensacion);
			
			//cambiando el orden de la cola si no es por compensacion
			//y solo si el cambio es automatico
			if ((compensacion == null || !compensacion.trim().equals(ClsConstants.COMPENSACIONES))
					&& numeroColAutomatico != null) {
				admFiltros.tratamientoUltimo(idInstitucion, idTurno, idPersona,
						flagSalto, flagCompensacion);
			}
			
			//generando salto
			admFiltros.crearSalto(idInstitucion, idTurno, null, idPersona,
					checkSalto, motivoSalto);
			
			//generando compensacion
			admFiltros.crearCompensacion(idInstitucion, idTurno, null,
					idPersonaSaliente, compensacionActual, motivoCompensacion);			
			
			//finalizando transaccion
			tx.commit();		
			
			
			//preparando mensaje de salida
			mensaje = "messages.updated.success";
			if (numeroColAutomatico != null) {
				mensaje = UtilidadesString.getMensajeIdioma(usr, mensaje);
				mensaje += "\r\n"
						+ UtilidadesString.getMensajeIdioma(
								usr,
								"messages.nuevaDesigna.seleccionAutomaticaLetrado",
								new String[] { numeroColAutomatico,	nombreColAutomatico });
			}
		}catch (SIGAException e) {
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoModal(mensaje, request);
	} //insertar()
	
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
		CambiosLetradosDesignasForm miform = (CambiosLetradosDesignasForm)formulario;
		UserTransaction tx=null;
		boolean ok=false;
		
		Integer user=this.getUserName(request);
		String fRenuncia=miform.getAplFechaRenunciaSolicita();
		String motivo=miform.getIdTipoMotivo();
		String observ=miform.getObservaciones();
		
		try{
			tx=usr.getTransaction();
			tx.begin();
			ScsDesignasLetradoAdm designaAdm = new ScsDesignasLetradoAdm(this.getUserBean(request));
			Hashtable datos=(Hashtable)ses.getAttribute("DATABACKUP_CLD");
		
			// HASH DE INSERCION para el nuevo
			Hashtable designaNueva = (Hashtable)datos.clone();
			designaNueva.put(ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA, fRenuncia);			
			designaNueva.put(ScsDesignasLetradoBean.C_IDTIPOMOTIVO,motivo);
			designaNueva.put(ScsDesignasLetradoBean.C_OBSERVACIONES,observ);
			
			
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