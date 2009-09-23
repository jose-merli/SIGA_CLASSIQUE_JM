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
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.BusquedaClientesFiltrosAdm;
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
				ScsDesignasLetradoBean.T_NOMBRETABLA+" dp,"+
				CenPersonaBean.T_NOMBRETABLA+" p,"+
				CenColegiadoBean.T_NOMBRETABLA+" c "+
				" where dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"="+instit+
				" and dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+" = c."+CenColegiadoBean.C_IDINSTITUCION+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =c."+CenColegiadoBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_IDPERSONA+" =p."+CenPersonaBean.C_IDPERSONA+
				" and dp."+ScsDesignasLetradoBean.C_ANIO+"="+anio+
				" and dp."+ScsDesignasLetradoBean.C_NUMERO+"="+numero+
				" and dp."+ScsDesignasLetradoBean.C_IDTURNO+"="+turno+
				" and dp."+ScsDesignasLetradoBean.C_FECHARENUNCIA+" is null ";
			
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
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected synchronized String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		CambiosLetradosDesignasForm miform = (CambiosLetradosDesignasForm)formulario;
		UserTransaction tx=null;
		boolean ok=false;
		
		String nombreApellidos = null;
		String numColegiadoAutomatico = null;
		
		Integer user=this.getUserName(request);
		String instit=usr.getLocation();
		String anio=miform.getAnio();
		String numero=miform.getNumero();
		String turno=miform.getIdTurno();
		String idPersona=miform.getIdPersona();
		String fCambio=miform.getAplFechaDesigna();
		String motivo=miform.getIdTipoMotivo();
		String observ=miform.getObservaciones();
		String idPersonaSaliente="";
		String compensacion = null;
		
		//Obtención parametros de la busqueda SJCS 
		String flagSalto = 			request.getParameter("flagSalto");
		String compensacionActual = request.getParameter("compensacionActual");
		boolean bCompensacion  = UtilidadesString.stringToBoolean(compensacionActual);
		if (bCompensacion){
			compensacionActual=ClsConstants.DB_TRUE;
		}else{
			compensacionActual=ClsConstants.DB_FALSE;
		}
		
		String flagCompensacion = 	request.getParameter("flagCompensacion");
		String checkSalto = 		request.getParameter("checkSalto");
		
		String msgOrigen = 			UtilidadesString.getMensajeIdioma(usr,"gratuita.modalCambioLetradoDesigna.titulo"); 
		String motivoSalto = 		UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarSaltoPor") +
									" "+msgOrigen;
		String motivoCompensacion=	UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarCompensacionPor") +
									" "+msgOrigen;
		String cambioMismoDia=request.getParameter("cambioMismoDia");
		
		try{
			
			tx=usr.getTransaction();
			tx.begin();
			BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(this.getUserBean(request));
			if (idPersona == null || idPersona.trim().equals("")) {
				BusquedaClientesFiltrosAdm busquedaClientesFiltrosAdm= new BusquedaClientesFiltrosAdm();
				Row row = busquedaClientesFiltrosAdm.gestionaDesignacionesAutomaticas(usr.getLocation(), miform.getIdTurno());
				idPersona = (String)row.getValue(ScsDesignasLetradoBean.C_IDPERSONA);
				compensacion = (String)row.getValue(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
				numColegiadoAutomatico = (String)row.getValue(CenColegiadoBean.C_NCOLEGIADO);
				nombreApellidos = (String)row.getValue(CenPersonaBean.C_NOMBRE);
				String apellido1 = (String)row.getValue(CenPersonaBean.C_APELLIDOS1);
				if (apellido1 != null) {
					nombreApellidos += " " + apellido1;	
				}
				
				String apellido2 = (String)row.getValue(CenPersonaBean.C_APELLIDOS2);
				if (apellido2 != null) {
					nombreApellidos += " " + apellido2;	
				}
			}
			
			
			ScsDesignasLetradoAdm designaAdm = new ScsDesignasLetradoAdm(this.getUserBean(request));
			Hashtable datos=(Hashtable)ses.getAttribute("DATABACKUP_CLD");
			if(datos!=null){
				idPersonaSaliente=(String)datos.get(ScsDesignasLetradoBean.C_IDPERSONA);
				
				// HASH DE MODIFICACION para el que actualmente estaba asignado
				Hashtable designaActual= (Hashtable)datos.clone();
				designaActual.put(ScsDesignasLetradoBean.C_FECHARENUNCIA,fCambio);
				if (cambioMismoDia!=null && cambioMismoDia.equalsIgnoreCase("1") ){
					//admFiltros.crearSalto(instit,turno,null,idPersonaSaliente,"1", "Salto por cambio de Letrado en el mismo día (Registro Automático)");
					ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
					Hashtable hash = new Hashtable();
					String fecha = UtilidadesBDAdm.getFechaBD("");
					String motivos = UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.inicio_SaltosYCompensaciones.literal.motivo")+" "+fecha;
					hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,usr.getLocation());
					hash.put(ScsSaltosCompensacionesBean.C_IDTURNO,miform.getIdTurno());
					hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,motivos);
					hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idPersonaSaliente);
					hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,ClsConstants.SALTOS);
					hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,adm.getNuevoIdSaltosTurno(usr.getLocation(),miform.getIdTurno()));
					hash.put(ScsSaltosCompensacionesBean.C_FECHA,fCambio);
					hash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,fCambio);
					if (!adm.insert(hash)) {
						throw new ClsExceptions("Error insertando salto: "+ adm.getError());
					}
					ok=designaAdm.delete(designaActual); 			
				}else{
				  ok=designaAdm.updateDirect(designaActual, designaAdm.getClavesBean(), new String[]{ScsDesignasLetradoBean.C_FECHARENUNCIA});
				}
				if (!ok) throw new ClsExceptions(designaAdm.getError());
			
			}
			
			// HASH DE INSERCION para el nuevo
			Hashtable designaNueva = new Hashtable();
			designaNueva.put(ScsDesignasLetradoBean.C_IDINSTITUCION,instit);
			designaNueva.put(ScsDesignasLetradoBean.C_IDTURNO,turno);
			designaNueva.put(ScsDesignasLetradoBean.C_NUMERO,numero);
			designaNueva.put(ScsDesignasLetradoBean.C_ANIO,anio);
			designaNueva.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersona);
			designaNueva.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fCambio);			
			designaNueva.put(ScsDesignasLetradoBean.C_IDTIPOMOTIVO,motivo);
			designaNueva.put(ScsDesignasLetradoBean.C_MANUAL,ClsConstants.DB_FALSE);
			designaNueva.put(ScsDesignasLetradoBean.C_LETRADODELTURNO,ClsConstants.DB_FALSE);
			designaNueva.put(ScsDesignasLetradoBean.C_OBSERVACIONES,observ);
			
			ok=designaAdm.insert(designaNueva);
			if (!ok) throw new ClsExceptions(designaAdm.getError());
			
			 

			//Primero: Actualiza si ha sido automático o manual (Designaciones)
			admFiltros.actualizaManualDesigna(instit,turno,idPersona,anio, numero, flagSalto,flagCompensacion);

			if (compensacion == null || !compensacion.trim().equals(ClsConstants.COMPENSACIONES)) { // si no es por compensacion tratamos ultimo
				//Segundo: Tratamiento de último (Designaciones)
				admFiltros.tratamientoUltimo(instit,turno,idPersona,flagSalto,flagCompensacion);
			}
			
			//Tercero: Generación de salto (Designaciones y asistencias)
			admFiltros.crearSalto(instit,turno,null,idPersona,checkSalto, motivoSalto);
			
			//Cuarto: Generación de compensación (Designaciones NO ALTAS)
			admFiltros.crearCompensacion(instit,turno,null,idPersonaSaliente,compensacionActual,motivoCompensacion);
			

			// Se cierra la transacción
			tx.commit();		
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		String mensaje = "messages.updated.success";
		
		if (numColegiadoAutomatico != null) {
			mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request), mensaje);
			mensaje += "\r\n" + UtilidadesString.getMensajeIdioma(getUserBean(request), "messages.nuevaDesigna.seleccionAutomaticaLetrado", new String[]{numColegiadoAutomatico, nombreApellidos});
		}
		
		return exitoModal(mensaje, request);
		
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