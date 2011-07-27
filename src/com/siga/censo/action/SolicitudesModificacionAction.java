/*
 * VERSIONES:
 * 
 * miguel.villegas - 04-01-2005 - Creacion
 *	
 */

/**
 * Clase action para manejar las solicitudes de modificacion.<br/>
 * Gestiona la edicion de dichas solicitudes  
 */

package com.siga.censo.action;


import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.censo.form.SolicitudesModificacionForm;
import java.util.*;


public class SolicitudesModificacionAction extends MasterAction {

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("procesarSolicitud")){
					mapDestino = procesarSolicitud(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("denegarSolicitud")){
					mapDestino = denegarSolicitud(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("modificarDatos")){
					mapDestino = modificarDatos(mapping, miForm, request, response);					
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} 
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}	
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		String result="abrir";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			//Integer idPersona=this.getUserName(request);
			Long idPersona=new Long(user.getIdPersona());

			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","SMG"); // busqueda normal
			
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			if (buscar==null){
				SolicitudesModificacionForm miformSession = (SolicitudesModificacionForm)request.getSession().getAttribute("SolicitudesModificacionForm");
				if (miformSession!=null) {
					miformSession.reset(mapping,request);
					miformSession.setFechaDesde("");
					miformSession.setFechaHasta("");
				}
				SolicitudesModificacionForm miform = (SolicitudesModificacionForm)formulario;
				miform.reset(mapping,request);
				miform.setFechaDesde("");
				miform.setFechaHasta("");
			}
			request.setAttribute("buscar",buscar);
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona.toString());
			request.setAttribute("IDINSTITUCION", idInstitucion);						
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrir";		
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="editar";
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="ver";
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "nuevo";
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		boolean acceso=true;
		UserTransaction tx=null;

		try{
			
			// Obtengo el UserBean, el identificador de la institucion y el de la persona
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			//Long idPersona=new Long(1000); // Para pruebas
			Long idPersona=new Long(user.getIdPersona());
			
			// Creo manejador para acceder a la BBDD
			CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(user);

			// Obtengo los datos del formulario
			SolicitudesModificacionForm miForm = (SolicitudesModificacionForm)formulario;

			// Cargo la tabla hash con los valores del formulario para insertar en CEN_SOLICITUDESMODIFICACION
			Hashtable hash = formulario.getDatos();
			
			// Anhado valores que faltan
			hash.put("IDPERSONA",idPersona.toString());
			hash.put("IDINSTITUCION",idInstitucion);			
			
			// Obtengo el IDSOLICITUD
			admin.prepararInsert(hash);
			
			// Comienzo control de transacciones
			tx = user.getTransaction();					
			tx.begin();		
			
			// Inserto en CEN_SOLICITUDESMODIFICACION 
			if (admin.insert(hash)){ 
				result="insertar";			
				tx.commit();
			}
			else{
				throw new SIGAException (admin.getError());
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}					

		return (result);
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "modificar";		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="borrar";
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";

		try{
			Vector vector=new Vector();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
			// Manejadores para el formulario y el acceso a las BBDDs
			SolicitudesModificacionForm form = (SolicitudesModificacionForm) formulario;
			CenSolicitudesModificacionAdm admin = new CenSolicitudesModificacionAdm(this.getUserBean(request));			

			// Obtengo las entradas correspondientes a la busqueda
			vector=admin.getSolicitudes(form.getIdInstitucion(),form.getCmbTipoModificacion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());			

			// Paso la busqueda como parametro en el request 
			request.setAttribute("container", vector);
			request.setAttribute("IDPERSONA", form.getIdPersona());
			request.setAttribute("IDINSTITUCION", form.getIdInstitucion());		
			
	        //Para volver correctamente desde envios:
	        request.getSession().setAttribute("EnvEdicionEnvio","GMG");
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}		
		return (result);
	}
	
	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificarDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="modificarDatos";
		String modo="";
		UserTransaction tx = null;
		
		try {		
			Hashtable hash= new Hashtable(); 		
			Vector camposOcultos=new Vector();
			CenColegiadoBean original;		

			// Obtengo el userbean
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
 			
			// Obtengo los datos del formulario
			SolicitudesModificacionForm miForm = (SolicitudesModificacionForm)formulario;

			// Cargo la tabla hash con los valores del formulario
			camposOcultos = miForm.getDatosTablaOcultos(0);			
			modo = "editar";
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",camposOcultos.get(0));
			datosCliente.put("idInstitucion",camposOcultos.get(1));
			datosCliente.put("tipoAcceso",ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			
			// Paso la tabla como parametro
			request.setAttribute("datosCliente", datosCliente);			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);		
	}	
	
	/** 
	 *  Funcion que implementa la accion procesar solicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String procesarSolicitud(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		UserTransaction tx = null;
		CenHistoricoAdm admHistorico = new CenHistoricoAdm(this.getUserBean(request));
		CenHistoricoBean beanHistorico = new CenHistoricoBean();
		String sol="";
		Hashtable hashSol = new Hashtable();
		
		try {		
			Hashtable hashOriginal = new Hashtable(); 		
			Hashtable hash = new Hashtable();		
			Vector original;
			boolean correcto=true;
			Enumeration listaPeticiones;
			String[] solicitudes;
			String peticiones="";
			int i=0, procesados=0;
			ArrayList noProcesadas=new ArrayList();
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			SolicitudesModificacionForm miForm = (SolicitudesModificacionForm)formulario;

			// Obtengo las solicitudes y las gestiono
			peticiones=miForm.getSolicitudes();
			solicitudes = miForm.getPeticiones();			
			if (peticiones.equalsIgnoreCase("")){
				result="procesarSolicitud";
			}
			else{
				// Comienzo control de transacciones
				tx = usr.getTransaction();				
				while(i<solicitudes.length){
					sol=solicitudes[i];
					if (!sol.equalsIgnoreCase("")){
						original=admin.obtenerEntradaSolicitudesModificacion(sol);
						Row row = (Row)original.firstElement();
						hashSol = row.getRow();
					}
					tx.begin();
//					if (!solicitudes[i].equalsIgnoreCase("")){
					
					///MODIFICACION///////////////////////////////////////////////////////////////	
					correcto=admin.procesadoSolicitud(sol);//
					if (correcto){																//
						// Si la modificación se realiza correctamente añadimos el historico del cambio
						beanHistorico = new CenHistoricoBean();
						beanHistorico.setIdInstitucion(Integer.parseInt(miForm.getIdInstitucion()));
						//beanHistorico.setIdPersona(Long.parseLong(miForm.getIdPersona()));
						beanHistorico.setIdPersona(Long.parseLong((String) hashSol.get("IDPERSONA")));
						beanHistorico.setDescripcion(miForm.getDescripcion());
						beanHistorico.setFechaEfectiva(UtilidadesString.formatoFecha(GstDate.getHoyJava(),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_LONG_SPANISH));
						beanHistorico.setFechaEntrada(UtilidadesString.formatoFecha(hashSol.get("FECHAALTA").toString(),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_LONG_SPANISH));
						int tipoCambio = Integer.parseInt(hashSol.get("IDTIPOMODIFICACION").toString());
						if(tipoCambio % 10 != 0) tipoCambio=99;
						beanHistorico.setIdTipoCambio(tipoCambio);
						beanHistorico.setDescripcion(hashSol.get("DESCRIPCION").toString());
						beanHistorico.setMotivo(UtilidadesString.getMensajeIdioma(usr, "censo.solicitudModificacion.motivoGenerico"));
						admHistorico.insertarRegistroHistorico(beanHistorico, usr);
						procesados++;															//
						tx.commit();															//
					}																			//
					else{																		//
						noProcesadas.add(solicitudes[i]);										//
						tx.rollback();															//
					}																			//
					//////////////////////////////////////////////////////////////////////////////
					
//						original=admin.obtenerEntradaSolicitudesModificacion(solicitudes[i]);
//						Row row = (Row)original.firstElement();
//						hashOriginal = row.getRow();
//						hash=(Hashtable)hashOriginal.clone();
//						//hash = row.getRow();
//						hash.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_REALIZADA));						
//						if (!admin.update(hash,hashOriginal)){
//							correcto = false;
//						}	
//						else{
//							tx.commit();
//							procesados++;							
//						}
//					}	
					i++;
				}	
			}		
			if (correcto){
				result=exitoRefresco("messages.updated.success",request);
			}
			else{
				i=0;
				String mensaje="";
				while(i < noProcesadas.size()){
					mensaje=mensaje + "censo.resultadosSolicitudesModificacion.literal.idSolicitud" + ": " +
							((String)noProcesadas.get(0)) + " " + "messages.censo.solicitudesModificacion.error" + "\n.";
					i++;
				}
				result=exitoRefresco(mensaje,request);
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}
		return (result);		
	}
	
	
	/** 
	 *  Funcion que implementa la accion denegarSolicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String denegarSolicitud(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		UserTransaction tx = null;
		
		try {		
			Hashtable hashOriginal = new Hashtable(); 		
			Hashtable hash = new Hashtable();		
			Vector original;
			boolean correcto=true;
			Enumeration listaPeticiones;
			String[] solicitudes;
			String peticiones="";
			int i=0, procesados=0;
			ArrayList noProcesadas=new ArrayList();

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			SolicitudesModificacionForm miForm = (SolicitudesModificacionForm)formulario;

			// Obtengo las solicitudes y las gestiono
			peticiones=miForm.getSolicitudes();
			if (peticiones.equalsIgnoreCase("")){
				result="procesarSolicitud";
			}
			else{
				solicitudes = miForm.getPeticiones();
				// Comienzo control de transacciones
				tx = usr.getTransaction();
				while((correcto) && (i<solicitudes.length)){
					tx.begin();
					///MODIFICACION///////////////////////////////////////////////////////////////	
					correcto=admin.denegacionSolicitud(solicitudes[i]);//
					if (correcto){																//
						procesados++;															//
						tx.commit();															//
					}																			//
					else{																		//
						noProcesadas.add(solicitudes[i]);										//
						tx.rollback();															//
					}																			//
					//////////////////////////////////////////////////////////////////////////////
//					if (!solicitudes[i].equalsIgnoreCase("")){
//						original=admin.obtenerEntradaSolicitudesModificacion(solicitudes[i]);
//						Row row = (Row)original.firstElement();
//						hashOriginal = row.getRow();
//						hash=(Hashtable)hashOriginal.clone();
//						//hash = row.getRow();
//						hash.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC,String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_DENEGADA));
//						if (!admin.update(hash,hashOriginal)){
//							correcto=false;
//						}	
//						else{
//							procesados++;							
//						}
//					}	
					i++;
				}
//				if (correcto){
//					tx.commit();
//				}	
//				else {
//					throw new SIGAException (admin.getError());
//				}
			}
			if (correcto){
				result=exitoRefresco("messages.updated.success",request);
			}
			else{
				i=0;
				String mensaje="";
				while(i < noProcesadas.size()){
					mensaje=mensaje + "censo.resultadosSolicitudesModificacion.literal.idSolicitud" + ": " +
							((String)noProcesadas.get(0)) + " " + "messages.censo.solicitudesModificacion.error" + "\n.";
					i++;
				}
				result=exitoRefresco(mensaje,request);
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}
		return (result);		
	}	
}
