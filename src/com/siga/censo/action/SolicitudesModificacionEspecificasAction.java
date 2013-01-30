/*
 * VERSIONES:
 * 
 * miguel.villegas - 24-01-2005 - Creacion
 *	
 */

/**
 * Clase action para manejar las solicitudes de modificaciones especificas.<br/>
 * Gestiona la edicion de dichas solicitudes  
 */

package com.siga.censo.action;


import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.censo.form.SolicitudesModificacionEspecificasForm;
import java.util.*;


public class SolicitudesModificacionEspecificasAction extends MasterAction {

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {

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

		String result="";
		String tipoBusqueda="";		

		try{
			Vector ocultos=new Vector();
			Vector original=new Vector();
			Vector modificacion=new Vector();
			Hashtable temporal=new Hashtable();
			Hashtable hashBusqueda=new Hashtable();
			
			// Obtengo el formulario y el tipo de la busqueda
			SolicitudesModificacionEspecificasForm form = (SolicitudesModificacionEspecificasForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);			
			tipoBusqueda=(String)ocultos.get(3);
			
			// Obtengo las entradas correspondientes a la busqueda en funcion del tipo de modificacion (original y modificado)
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))){
				// Obtencion de los datos a modificar
				CenSolicitModifDatosBasicosAdm adminDB = new CenSolicitModifDatosBasicosAdm(this.getUserBean(request));				
				modificacion=adminDB.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				// Obtencion de los datos originales
				CenClienteAdm adminC = new CenClienteAdm(this.getUserBean(request));				
				temporal.put(CenClienteBean.C_IDPERSONA,(String)ocultos.get(0));
				temporal.put(CenClienteBean.C_IDINSTITUCION,(String)ocultos.get(1));				
				Vector temporalV=adminC.select(temporal);
				// Transformo bean en una hastable con los datos que me interesan
				CenClienteBean beanOriginal=(CenClienteBean) temporalV.firstElement();
				Hashtable hashOriginal=new Hashtable();
				hashOriginal.put(CenClienteBean.C_PUBLICIDAD,beanOriginal.getPublicidad());
				hashOriginal.put(CenClienteBean.C_GUIAJUDICIAL,beanOriginal.getGuiaJudicial());
				hashOriginal.put(CenClienteBean.C_ABONOSBANCO,beanOriginal.getAbonosBanco());
				hashOriginal.put(CenClienteBean.C_CARGOSBANCO,beanOriginal.getCargosBanco());
				hashOriginal.put(CenClienteBean.C_IDLENGUAJE,beanOriginal.getIdLenguaje());
				original.addElement(hashOriginal);
				result="verDatosGenerales";				
			}
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES))){
				// Obtencion de los datos a modificar				
				CenSoliModiDireccionesAdm adminD = new CenSoliModiDireccionesAdm(this.getUserBean(request));				
				modificacion=adminD.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				// Obtencion de los datos originales					
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				temporal = clienteAdm.getDirecciones(new Long((String)ocultos.get(0)),new Integer((String)ocultos.get(1)),new Long((String)ocultos.get(4)),true);
				original.addElement(temporal);
				result="verDirecciones";
			}
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXP_FOTO))){
				// Obtencion de los datos a modificar				
				CenSolicModifExportarFotoAdm adminExpFoto = new CenSolicModifExportarFotoAdm(this.getUserBean(request));				
				modificacion=adminExpFoto.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				// Obtencion de los datos originales
				CenClienteAdm adminC = new CenClienteAdm(this.getUserBean(request));				
				temporal.put(CenClienteBean.C_IDPERSONA,(String)ocultos.get(0));
				temporal.put(CenClienteBean.C_IDINSTITUCION,(String)ocultos.get(1));				
				Vector temporalV=adminC.select(temporal);
				// Transformo bean en una hastable con los datos que me interesan
				CenClienteBean beanOriginal=(CenClienteBean) temporalV.firstElement();
				Hashtable hashOriginal=new Hashtable();
				hashOriginal.put(CenClienteBean.C_EXPORTARFOTO,beanOriginal.getExportarFoto());
				original.addElement(hashOriginal);
				result="verExportarFoto";
			}			
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS))){
				// Obtencion de los datos a modificar				
				CenSolicModiCuentasAdm adminCB = new CenSolicModiCuentasAdm(this.getUserBean(request));				
				modificacion=adminCB.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				// Obtencion de los datos originales
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				temporal = clienteAdm.getCuentasBancarias(new Long((String)ocultos.get(0)),new Integer((String)ocultos.get(1)),new Integer((String)ocultos.get(4)));
				original.addElement(temporal);
				result="verCuentasBancarias";				

			}
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))){
				// Obtencion de los datos a modificar				

				CenSolicitudModificacionCVAdm adminCV = new CenSolicitudModificacionCVAdm(this.getUserBean(request));				
				modificacion=adminCV.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				

				// Obtencion de los datos originales si procede (puede ser una incorporacion)
				if (ocultos.size()>5){
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
					temporal = clienteAdm.getDatosCV(new Long((String)ocultos.get(0)),new Integer((String)ocultos.get(1)),new Integer((String)ocultos.get(4)));
					original.addElement(temporal);				
				}	
				result="verDatosCV";				
			}
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))){
				// Obtencion de los datos a modificar, primero la solicitud				
				CenSolModiFacturacionServicioAdm adminFact = new CenSolModiFacturacionServicioAdm(this.getUserBean(request));				
				modificacion=adminFact.obtenerEntradaSolicitudModificacion((String)ocultos.get(2));
				// Obtencion de los datos originales si procede (puede ser una incorporacion)
				PysServiciosSolicitadosAdm solicAdm = new PysServiciosSolicitadosAdm(this.getUserBean(request));
				// Cargo una hashtable con los criterios de busqueda en los que estoy interesado
				temporal=((Row)modificacion.firstElement()).getRow();
				hashBusqueda.put(PysServiciosSolicitadosBean.C_IDINSTITUCION,temporal.get(CenSolModiFacturacionServicioBean.C_IDINSTITUCION));
				hashBusqueda.put(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS,temporal.get(CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS));
				hashBusqueda.put(PysServiciosSolicitadosBean.C_IDSERVICIO,temporal.get(CenSolModiFacturacionServicioBean.C_IDSERVICIO));
				hashBusqueda.put(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION,temporal.get(CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION));
				hashBusqueda.put(PysServiciosSolicitadosBean.C_IDPETICION,temporal.get(CenSolModiFacturacionServicioBean.C_IDPETICION));
				// Realizo la busqueda
				original = solicAdm.getServiciosSolicitados(hashBusqueda,new Integer((String)ocultos.get(1)),false);					
				result="verFacturacion";				
			}
			if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES))){
				// Obtencion de los datos a modificar, primero la solicitud				
				ExpSolicitudBorradoAdm adminExp = new ExpSolicitudBorradoAdm(this.getUserBean(request));
				ExpSolicitudBorradoBean beanExp = new ExpSolicitudBorradoBean();
				beanExp=adminExp.getSolicitud((String)ocultos.get(2));
				modificacion.add(beanExp.getOriginalHash());
				// Obtencion de los datos originales si procede
				ExpExpedienteAdm adminExpII = new ExpExpedienteAdm(this.getUserBean(request));
				temporal=adminExpII.getExpedienteCenso(beanExp.getIdTipoExpediente().toString(),beanExp.getIdInstitucion().toString(),beanExp.getIdInstitucion_tipoExpediente().toString(),beanExp.getAnioExpediente().toString(),beanExp.getNumeroExpediente().toString());				
				ExpExpedienteBean beannn = new ExpExpedienteBean();				
				original.addElement(temporal);					
				result="verExpediente";								
			}
			if (original.isEmpty()){
				original=null;
			}
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", modificacion);			
			request.setAttribute("container_desc", original);			
		}	  
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}			
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
			Integer idPersona=new Integer(user.getUserName());
			
			// Creo manejador para acceder a la BBDD
			CenSolicitudesModificacionAdm admin=new CenSolicitudesModificacionAdm(user);

			// Obtengo los datos del formulario
			SolicitudesModificacionEspecificasForm miForm = (SolicitudesModificacionEspecificasForm)formulario;

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
		String tipoBusqueda="";
		String textoTipo="";

		try{
			Vector vector1=new Vector();
			Vector vector2=new Vector();
			Vector vector3=new Vector();
			Vector vector4=new Vector();
			Vector vector5=new Vector();
			Vector vector6=new Vector();
			Vector vector=new Vector();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");		
		
			// Manejadores para el formulario y el acceso a las BBDDs
			SolicitudesModificacionEspecificasForm form = (SolicitudesModificacionEspecificasForm) formulario;
			CenSolicitModifDatosBasicosAdm adminDB = new CenSolicitModifDatosBasicosAdm(this.getUserBean(request));
			CenSoliModiDireccionesAdm adminD = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			CenSolicModifExportarFotoAdm adminExpFoto = new CenSolicModifExportarFotoAdm(this.getUserBean(request));
			CenSolicModiCuentasAdm adminCB = new CenSolicModiCuentasAdm(this.getUserBean(request));
			CenSolicitudModificacionCVAdm adminCV = new CenSolicitudModificacionCVAdm(this.getUserBean(request));
			CenSolModiFacturacionServicioAdm adminFact = new CenSolModiFacturacionServicioAdm(this.getUserBean(request));
			ExpSolicitudBorradoAdm adminExp = new ExpSolicitudBorradoAdm(this.getUserBean(request));
			tipoBusqueda=form.getTipoModifEspec();
			textoTipo=form.getTextoModificacion();
			if (tipoBusqueda !=null && !tipoBusqueda.equals("")){
			// Obtengo las entradas correspondientes a la busqueda en funcion del tipo de modificacion
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))){
									
					vector=adminDB.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta()); 
				}
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES))){
									
					vector=adminD.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXP_FOTO))){
									
					vector=adminExpFoto.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}				
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS))){
									
					vector=adminCB.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))){
									
					vector=adminCV.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))){
									
					vector=adminFact.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}
				if (tipoBusqueda.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES))){
									
					vector=adminExp.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());				
				}
			}else{
				vector=adminDB.getSolicitudesModifEspecifTotales(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				/*vector1=adminDB.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				vector2=adminD.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				vector3=adminCB.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				vector4=adminCV.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				vector5=adminFact.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				vector6=adminExp.getSolicitudes(form.getIdInstitucion(),form.getEstadoSolicitudModif(),form.getFechaDesde(),form.getFechaHasta());
				Row row=new Row();
				if (vector1!=null && vector1.size()>0){
					Enumeration en = vector1.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
				}
				if (vector2!=null && vector2.size()>0){
					Enumeration en = vector2.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
					
				}
				if (vector3!=null && vector3.size()>0){
					Enumeration en = vector3.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
					
				}
				if (vector4!=null && vector4.size()>0){
					
					Enumeration en = vector4.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
				}
				if (vector5!=null && vector5.size()>0){
					Enumeration en = vector5.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
				}
				if (vector6!=null && vector6.size()>0){
					Enumeration en = vector6.elements();
					while (en.hasMoreElements())
					{
	            		 row = (Row) en.nextElement(); 
					    vector.add(row);
					}
				}*/
				
				
			}
            Vector VectorFinal=new Vector();
            VectorFinal.add(vector);
			// Paso la busqueda como parametro en el request 
			request.setAttribute("container", vector);
			request.setAttribute("TEXTOTIPOMODIF", textoTipo);
			request.setAttribute("TIPOMODIF", tipoBusqueda);			
			request.setAttribute("IDPERSONA", form.getIdPersona());
			request.setAttribute("IDINSTITUCION", form.getIdInstitucion());		

			//Para volver correctamente desde envios:
	        request.getSession().setAttribute("EnvEdicionEnvio","GME");
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
			CenColegiadoBean original;		
			Hashtable hash= new Hashtable(); 		
			Vector camposOcultos=new Vector();

			// Obtengo el userbean
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
 			
			// Obtengo los datos del formulario
			SolicitudesModificacionEspecificasForm miForm = (SolicitudesModificacionEspecificasForm)formulario;

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
		
		try {
			//String tipoModif="";		
			Hashtable hashOriginal = new Hashtable(); 		
			Hashtable hash = new Hashtable();		
			Vector original;
			boolean correcto=true;
			Enumeration listaPeticiones;
			String[] solicitudes;
			String[] solicitudesTipoModif;
			ArrayList noProcesadas=new ArrayList();		
			String peticiones="";
			int i=0;
			int procesados=0;		

			// Obtengo usuario
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			
			
			// Obtengo los datos del formulario
			SolicitudesModificacionEspecificasForm miForm = (SolicitudesModificacionEspecificasForm)formulario;
			

			// Obtengo las solicitudes yl tipo de modificacion
			peticiones=miForm.getSolicitudes();			
			//tipoModif=miForm.getTipoModifEspec();	
			
			// Procedo a realizar la pertinente gestion
			if (peticiones.equalsIgnoreCase("")){
				result="procesarSolicitud";
			}
			else{
				// Obtengo los identificadores de solicitud 
				solicitudes = miForm.getPeticiones();
				solicitudesTipoModif=miForm.getPeticionesTipoModif();
				// Comienzo control de transacciones
				tx = usr.getTransactionPesada();
				
				// Gestiono en funcion del tipo de modificacion
				i=0;	
			  while(i<solicitudesTipoModif.length){
			  	
				  if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))){
					CenSolicitModifDatosBasicosAdm adminDB = new CenSolicitModifDatosBasicosAdm(this.getUserBean(request));					
					
			       	
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminDB.procesarSolicitud(solicitudes[i],this.getUserName(request), this.getLenguaje(request));
						if (correcto){							
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							tx.rollback();
						}
						
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXP_FOTO))){
					CenSolicModifExportarFotoAdm adminExpFoto = new CenSolicModifExportarFotoAdm(this.getUserBean(request));					
					tx.begin();					
					correcto=adminExpFoto.procesarSolicitud(solicitudes[i],this.getUserName(request), this.getLenguaje(request));
					if (correcto){							
						procesados++;
						tx.commit();
					} else{
						noProcesadas.add(solicitudes[i]);
						tx.rollback();
					}						
						
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES))){
					CenSoliModiDireccionesAdm adminDir = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			      // 	i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminDir.procesarSolicitud(solicitudes[i], this.getLenguaje(request));
						if (correcto){
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							
						}
					//	i++;
					//}
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS))){
					CenSolicModiCuentasAdm adminCB = new CenSolicModiCuentasAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminCB.procesarSolicitud(solicitudes[i],this.getUserName(request), this.getUserBean(request), this.getLenguaje(request));
						if (correcto){
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							tx.rollback();
						}
						//i++;
					//}	
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))){
					CenSolicitudModificacionCVAdm adminCV = new CenSolicitudModificacionCVAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminCV.procesarSolicitud(solicitudes[i],this.getUserName(request), this.getLenguaje(request));
						if (correcto){
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							tx.rollback();
						}
						//i++;
					//}
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))){					
					CenSolModiFacturacionServicioAdm adminFact = new CenSolModiFacturacionServicioAdm(this.getUserBean(request));
			       //	i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminFact.procesarSolicitud(solicitudes[i], this.getLenguaje(request));
						if (correcto){
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							tx.rollback();
						}
						//i++;
					//}				
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES))){					
					ExpSolicitudBorradoAdm adminExp = new ExpSolicitudBorradoAdm(this.getUserBean(request));
			       //	i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminExp.procesarSolicitud(solicitudes[i],this.getUserName(request), this.getLenguaje(request));
						if (correcto){
							procesados++;
							tx.commit();
						}
						else{
							noProcesadas.add(solicitudes[i]);
							tx.rollback();
						}
						//i++;
					//}				
				}
				i++;
			  }
			}

			if (correcto) {
				result=exitoRefresco("messages.updated.success", request);
			} else {
				result=exitoRefresco("messages.censo.solicitudesModificacion.error", request);
			}
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}					
		return result;		
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
		String tipoModif="";				
		UserTransaction tx = null;
		
		try {		
			Vector original;
			boolean correcto=true;
			Enumeration listaPeticiones;
			String[] solicitudes;
			String[] solicitudesTipoModif;
			String peticiones="";
			int procesados=0 , i;

			// Obtengo usuario
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
 			
			// Obtengo los datos del formulario
			SolicitudesModificacionEspecificasForm miForm = (SolicitudesModificacionEspecificasForm)formulario;

			// Obtengo las solicitudes yl tipo de modificacion
			peticiones=miForm.getSolicitudes();			
			tipoModif=miForm.getTipoModifEspec();

			// Procedo a realizar la pertinente gestion
			if (peticiones.equalsIgnoreCase("")){
				result="denegarSolicitud";
			}
			else{
				// Obtengo los identificadores de solicitud				
				solicitudes = miForm.getPeticiones();
				solicitudesTipoModif=miForm.getPeticionesTipoModif();
				
				// Comienzo control de transacciones
				tx = usr.getTransaction();
				i=0;	
			 while(i<solicitudesTipoModif.length){	
				// Gestiono en funcion del tipo de modificacion
				if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))){
					CenSolicitModifDatosBasicosAdm adminDB = new CenSolicitModifDatosBasicosAdm(this.getUserBean(request));
			       //	i=0;
					//while(i<solicitudes.length){
						tx.begin();
						correcto=adminDB.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}	
				} else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXP_FOTO))){
					CenSolicModifExportarFotoAdm adminExpFoto  = new CenSolicModifExportarFotoAdm(this.getUserBean(request));
					tx.begin();
					correcto=adminExpFoto.denegarSolicitud(solicitudes[i]);
					if (correcto){
						procesados++;
						tx.commit();
					} else
						tx.rollback();
					
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES))){
					CenSoliModiDireccionesAdm adminDir = new CenSoliModiDireccionesAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminDir.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;							
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}						
					
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS))){
					CenSolicModiCuentasAdm adminCB = new CenSolicModiCuentasAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();									
						correcto=adminCB.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;							
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))){
					CenSolicitudModificacionCVAdm adminCV = new CenSolicitudModificacionCVAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminCV.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))){
					CenSolModiFacturacionServicioAdm adminFact = new CenSolModiFacturacionServicioAdm(this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminFact.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}				
				}else if (solicitudesTipoModif[i].equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES))){
					ExpSolicitudBorradoAdm  adminExp = new ExpSolicitudBorradoAdm (this.getUserBean(request));
			       	//i=0;
					//while(i<solicitudes.length){
						tx.begin();					
						correcto=adminExp.denegarSolicitud(solicitudes[i]);
						if (correcto){
							procesados++;
							tx.commit();
						} else
							tx.rollback();
						//i++;
					//}				
				}
				i++;
			 }	
			}
			if (correcto){
				result=exitoRefresco("messages.updated.success",request);			
			} else {
				result=exitoRefresco("messages.censo.solicitudesModificacion.error", request);
			}						
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}					
		return result;		
	}	
}
