/*
 * Created on Dec 27, 2004
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.ExpPermisosTiposExpedientes;
import com.siga.expedientes.form.BusquedaExpedientesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 */
public class BusquedaExpedientesAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={ExpExpedienteBean.C_IDINSTITUCION,ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
			ExpExpedienteBean.C_IDTIPOEXPEDIENTE,ExpExpedienteBean.C_ANIOEXPEDIENTE,ExpExpedienteBean.C_NUMEROEXPEDIENTE};
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

//			La primera vez que se carga el formulario 
//			Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				// jbd // inc6710 // Esto no se debe de borrar porque al 'volver' se pasa por abrir y no queremos
								  // perder el paginador
				//BusquedaExpedientesForm formExp = (BusquedaExpedientesForm)miForm;
				//formExp.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				//formExp.reset(mapping,request);
				//request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("abrirAvanzada")){
				BusquedaExpedientesForm formExp = (BusquedaExpedientesForm)miForm;
				formExp.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				formExp.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrirAvanzada(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("buscarVolver")){
				//aalg: inc_10284. Se añade opción buscarVolver para refrescar la búsqueda cuando vuelve
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				((BusquedaExpedientesForm)miForm).setDatosPaginador(null);
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarInit")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarPor")){
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarInitAvanzada")){
				miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}else if (accion.equalsIgnoreCase("buscarPorAvanzada")){
				mapDestino = buscarInit(mapping, miForm, request, response); 
			}
			else if (accion.equalsIgnoreCase("nuevoDesdeEjg")){
				mapDestino = nuevoDesdeEjg(mapping, miForm, request, response);
			}
			else if (accion.equalsIgnoreCase("editarDesdeEjg")){
				mapDestino = editarDesdeEjg(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("verDesdeEjg")){
				mapDestino = verDesdeEjg(mapping, miForm, request, response);
			
			}else if (accion.equalsIgnoreCase("nuevoCopia")){
				mapDestino = nuevoCopia(mapping, miForm, request, response);
			}
			else if (accion.equalsIgnoreCase("generaExcel")){
				mapDestino = generaExcel(mapping, miForm, request, response);
			}else {
				return super.executeInternal(mapping,
				formulario,
				request, 
				response);
			}

//			Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}


				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}

		} catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}



	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try{

			//Recuperamos el nombre de la institución local
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			CenInstitucionAdm instAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion=instAdm.getNombreInstitucion(userBean.getLocation());
			request.setAttribute("nombreInstitucion",nombreInstitucion);

			// miro a ver si tengo que ejecutar la busqueda una vez presentada la pagina
			// para la acción de volver
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		//	para saber en que tipo de busqueda estoy
		request.getSession().setAttribute("volverAuditoriaExpedientes","N"); // busqueda normal
		return("inicio");
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try{
			//	para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","A"); // busqueda avanzada

			//Recuperamos el nombre de la institución local
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			CenInstitucionAdm instAdm = new CenInstitucionAdm(this.getUserBean(request));
			String nombreInstitucion=instAdm.getNombreInstitucion(userBean.getLocation());
			request.setAttribute("nombreInstitucion",nombreInstitucion);						

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return "avanzada";
	}

	
	protected String buscarInit(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String forward = "resultado";
		boolean isAvanzada = false;
		try{
			BusquedaExpedientesForm miFormulario = (BusquedaExpedientesForm)formulario; 
			if(miFormulario.getAvanzada()!=null && miFormulario.getAvanzada().equals(ClsConstants.DB_TRUE)){
				//forward = "resultadoAvanzada";
				isAvanzada = true;
				request.setAttribute("isBusquedaAvanzada",ClsConstants.DB_TRUE);
			}
			
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			
			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
				&& !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null &&!isSeleccionarTodos){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();
	
	
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
	
	
	
				if (paginador!=null){	 
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
	
	
	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
	
	
	
	
			}else{
				
				ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request));
				databackup=new HashMap();
	
				//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;
				
				String idMateria = null; 
				String idArea = null;
				if(miFormulario.getIdMateria()!=null && !miFormulario.getIdMateria().equals("")){
					String[] materia= miFormulario.getIdMateria().split(",");
					idMateria = materia[2];
					idArea = materia[1];
					miFormulario.setIdMateria(idMateria);
					miFormulario.setIdArea(idArea);
					

				}
			
				if(miFormulario.getJuzgado()!=null && !miFormulario.getJuzgado().equals("")){
					String[] juzgado = miFormulario.getJuzgado().split(",");
					miFormulario.setJuzgado(juzgado[0]);
					miFormulario.setIdInstJuzgado(juzgado[1]);
				}
				
				
//				String idPretension = miFormulario.getIdPretension();
//				String idProcedimiento = null;
				if(miFormulario.getIdProcedimiento()!=null && !miFormulario.getIdProcedimiento().equals("")){
					String[] procedimiento = miFormulario.getIdProcedimiento().split(",");
					miFormulario.setIdProcedimiento(procedimiento[0]);
					miFormulario.setIdInstProcedimiento(procedimiento[1]);
					
				}
				
				
				resultado = expedienteAdm.getPaginadorAvanzadoExpedientes(miFormulario,user);
				// Paso de parametros empleando la sesion
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)expedienteAdm.selectGenericoNLS(resultado.getQueryInicio()));
						
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
						
					databackup.put("datos",datos);
					
					
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
				

			}
			//para saber en que tipo de busqueda estoy
			if (isAvanzada){
				request.getSession().setAttribute("volverAuditoriaExpedientes","AV"); // busqueda avanzada
			}else{
				request.getSession().setAttribute("volverAuditoriaExpedientes","NB"); // busqueda normal
			}
			
			//obtenemos los permisos a aplicar

			ExpPermisosTiposExpedientes perm=new ExpPermisosTiposExpedientes(user);
			request.setAttribute("permisos",perm);
			
			
			
			
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return forward;
	}

	
	
	

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return mostrarRegistro(mapping,formulario,request,response,true,false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return mostrarRegistro(mapping,formulario,request,response,false,false);
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;

//			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));

			Vector vOcultos = form.getDatosTablaOcultos(0);
//			String idInstitucion = (String)vOcultos.elementAt(0);

			Hashtable hash = new Hashtable();

			hash.put(ExpExpedienteBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));


			/*if (expAdm.delete(hash)){
	        	return exitoRefresco("messages.deleted.success",request);
	        }else{
	        	return exito("messages.deleted.error",request);
	        }*/

			expAdm.delete(hash);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		return exitoRefresco("messages.deleted.success",request);
	}	
	
	protected String verDesdeEjg(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return verEjg(mapping,formulario,request,response,false,false);
	}
	protected String editarDesdeEjg(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return verEjg(mapping,formulario,request,response,true,false);
	}

	protected String nuevoDesdeEjg(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		return verEjg(mapping,formulario,request,response,true,true);
	}

	
	protected String verEjg(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws SIGAException{

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;	
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   						

			if (!bNuevo){
				
				String idInstitucion = form.getInstitucion();
				String idInstitucion_TipoExpediente = form.getInstitucion();
				String idTipoExpediente = null;
				if( request.getParameter("idTipoExpediente")==null ||  request.getParameter("idTipoExpediente").toString().trim().equals(""))
					 idTipoExpediente = (String) form.getIdTipoExpediente();
				else
					 idTipoExpediente = (String) request.getParameter("idTipoExpediente");

				String numExpediente = form.getNumeroExpediente();
				if(numExpediente==null || numExpediente.equals(""))
					numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
				
				String anioExpediente = form.getAnioExpediente();
				if(anioExpediente==null || anioExpediente.equals(""))
					anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");

				
				
				
				String nombreTipoExpediente = UtilidadesString.mostrarDatoJSP(form.getTipoExpediente());

				//Si se intenta editar un expediente de otra institucion,
				//sólo se permitirá modificar las anotaciones (pestanha de seguimiento)
				String soloSeguimiento = "false";
				if (bEditable){
					soloSeguimiento = (!userBean.getLocation().equals(idInstitucion))?"true":"false";	        	
				}

				//Anhadimos parametros para las pestanhas
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion",idInstitucion);
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("numeroExpediente",numExpediente);
				htParametros.put("anioExpediente",anioExpediente);
				htParametros.put("nombreTipoExpediente",nombreTipoExpediente);
				htParametros.put("editable", bEditable ? "1" : "0");
				htParametros.put("accion", bEditable ? "edicion" : "consulta");	
				htParametros.put("soloSeguimiento",soloSeguimiento);

				request.setAttribute("expediente", htParametros);

				//Recuperamos las pestanhas ocultas para no mostrarlas
				ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
				String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas(idInstitucion_TipoExpediente,idTipoExpediente);
				
				request.setAttribute("pestanasOcultas",pestanasOcultas);
				request.setAttribute("idTipoExpediente",idTipoExpediente);
				request.setAttribute("idInstitucionTipoExpediente",idInstitucion_TipoExpediente);
				
				

				// Metemos los datos no editables del expediente en Backup.
				//Los datos particulares se anhadirán a la HashMap en cada caso.
				HashMap datosExpediente = new HashMap();
				Hashtable datosGenerales = new Hashtable();
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				datosExpediente.put("datosGenerales",datosGenerales);
				request.getSession().setAttribute("DATABACKUP",datosExpediente);


			}else{
				String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
				String idTipoExpediente = request.getParameter("idTipoExpediente");
//				String nombreTipoExpediente = request.getParameter("nombreTipo");
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("editable", "1");	
				htParametros.put("accion", "nuevo");
				htParametros.put("soloSeguimiento", "false");

				request.setAttribute("expediente", htParametros);
			}

			String nuevo = bNuevo?"true":"false";
			request.setAttribute("nuevo", nuevo);	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}


		return "editar";
	}

	
	
	
	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @param  bNuevo 
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws SIGAException{

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;	
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   						

			if (!bNuevo){
//				Vector vVisibles = form.getDatosTablaVisibles(0);
				Vector vOcultos = form.getDatosTablaOcultos(0);		

				String idInstitucion = (String)vOcultos.elementAt(0);
				String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
				String idTipoExpediente = (String)vOcultos.elementAt(2);
				String numExpediente = (String)vOcultos.elementAt(3);
				String anioExpediente = (String)vOcultos.elementAt(4);
				String nombreTipoExpediente = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(5));

				//Si se intenta editar un expediente de otra institucion,
				//sólo se permitirá modificar las anotaciones (pestanha de seguimiento)
				String soloSeguimiento = "false";
				if (bEditable){
					soloSeguimiento = (!userBean.getLocation().equals(idInstitucion))?"true":"false";	        	
				}

				//Anhadimos parametros para las pestanhas
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion",idInstitucion);
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("numeroExpediente",numExpediente);
				htParametros.put("anioExpediente",anioExpediente);
				htParametros.put("nombreTipoExpediente",nombreTipoExpediente);
				htParametros.put("editable", bEditable ? "1" : "0");
				htParametros.put("accion", bEditable ? "edicion" : "consulta");	
				htParametros.put("soloSeguimiento",soloSeguimiento);

				request.setAttribute("expediente", htParametros);

				//Recuperamos las pestanhas ocultas para no mostrarlas
				ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
				String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas(idInstitucion_TipoExpediente,idTipoExpediente);
				
				request.setAttribute("pestanasOcultas",pestanasOcultas);
				request.setAttribute("idTipoExpediente",idTipoExpediente);
				request.setAttribute("idInstitucionTipoExpediente",idInstitucion_TipoExpediente);
				
				

				// Metemos los datos no editables del expediente en Backup.
				//Los datos particulares se anhadirán a la HashMap en cada caso.
				HashMap datosExpediente = new HashMap();
				Hashtable datosGenerales = new Hashtable();
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				datosExpediente.put("datosGenerales",datosGenerales);
				request.getSession().setAttribute("DATABACKUP",datosExpediente);


			}else{
				String idInstitucion_TipoExpediente = request.getParameter("idInst");
				String idTipoExpediente = request.getParameter("idTipo");
//				String nombreTipoExpediente = request.getParameter("nombreTipo");
				Hashtable htParametros=new Hashtable();
				htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				htParametros.put("idTipoExpediente",idTipoExpediente);
				htParametros.put("editable", "1");	
				htParametros.put("accion", "nuevo");
				htParametros.put("soloSeguimiento", "false");

				request.setAttribute("expediente", htParametros);
			}

			String nuevo = bNuevo?"true":"false";
			request.setAttribute("nuevo", nuevo);	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}


		return "editar";
	}

	protected String nuevoCopia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{

		try{
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;	
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   						

			
			ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));


			String idInstitucion_TipoExpediente = request.getParameter("idInst");
			String idTipoExpediente = request.getParameter("idTipo");
			String idNombreTipoExpediente = request.getParameter("idNombreTipo");
			String numExp = request.getParameter("numExp");
			String AnioExp = request.getParameter("anioExp");	
			String idTipoExpedienteAnt = request.getParameter("tipoExpAnt");

			String nombreTipoExpediente = request.getParameter("nombreTipo");
			Hashtable htParametros=new Hashtable();
			
			htParametros.put("idInstitucion",idInstitucion_TipoExpediente);
			htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
			htParametros.put("idTipoExpediente",idTipoExpedienteAnt);
			htParametros.put("numeroExpediente",numExp);
			htParametros.put("anioExpediente",AnioExp);
			htParametros.put("nombreTipoExpediente",nombreTipoExpediente);
			htParametros.put("idTipoExpedienteNew",idTipoExpediente);			
			htParametros.put("nombreTipoExpedienteNew",idNombreTipoExpediente);
			htParametros.put("editable", "1");	
			htParametros.put("accion", "edicion");
			htParametros.put("copia", "s");
			htParametros.put("soloSeguimiento", "false");
			request.setAttribute("expediente", htParametros);
			String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas(idInstitucion_TipoExpediente,idTipoExpediente);

			request.setAttribute("pestanasOcultas",pestanasOcultas);
			request.setAttribute("idInstitucionTipoExpediente",idInstitucion_TipoExpediente);
			HashMap datosExpediente = new HashMap();
			Hashtable datosGenerales = new Hashtable();
			datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
			datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpedienteAnt);
			datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExp);
			datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,AnioExp);
			datosGenerales.put("idTipoExpedienteNew",idTipoExpediente);
			datosGenerales.put("nombreTipoExpedienteNew",idNombreTipoExpediente);
			datosExpediente.put("datosGenerales",datosGenerales);
			request.getSession().setAttribute("DATABACKUP",datosExpediente);
			
			request.setAttribute("nuevo", "true");	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}


		return "editar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException { 	


		return mostrarRegistro(mapping,formulario,request,response,true,true);

	}
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
        Vector datos = new Vector();
		
		
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			BusquedaExpedientesForm form = (BusquedaExpedientesForm)formulario;
	        
			
			ExpExpedienteAdm expedienteAdm = new ExpExpedienteAdm (this.getUserBean(request));
			
			
			for (int i = 0; i < form.getDatosTabla().size(); i++) {
				Vector vCampos = form.getDatosTablaOcultos(i);
			
		    	
		    
		    
			    	String idInstitucion = (String) vCampos.get(0);
					String idInstitucionTipoExp =(String) vCampos.get(1);
		    		String idTipoExp = (String) vCampos.get(2);
					String anio = (String) vCampos.get(3);
					String numero = (String) vCampos.get(4);
					datos.addAll(expedienteAdm.getDatosInformeExpediente(idInstitucion, idInstitucionTipoExp, idTipoExp, anio, numero, null,null, false,false));
		    
		    	
		    
	    	}
	        String[] cabeceras = null;
	        String[] campos = null;
	      
				cabeceras = new String[]{"IDINSTITUCION",
						"IDINSTITUCION_TIPOEXPEDIENTE",
						"IDTIPOEXPEDIENTE",
						"ANIOEXPEDIENTE",
						"NUMEROEXPEDIENTE",
						"FECHAINICIO",
						"ASUNTO",
						"ALERTAGENERADA",
						"ESVISIBLE",
						"ESVISIBLEENFICHA",
						"SANCIONADO",
						"SANCIONPRESCRITA",
						"ACTUACIONESPRESCRITAS",
						"SANCIONFINALIZADA",
						"ANOTACIONESCANCELADAS",
						"ANIOEXPDISCIPLINARIO",
						"NUMEXPDISCIPLINARIO",
						"NUMASUNTO",
						"FECHAINICIALESTADO",
						"FECHAFINALESTADO",
						"FECHAPRORROGAESTADO",
						"DESCRIPCIONRESOLUCION",
						"FECHARESOLUCION",
						"FECHACADUCIDAD",
						"OBSERVACIONES",
						"MINUTA",
						"IDPERSONA",
						"PER_NOMBRE",
						"PER_APELLIDOS1",
						"PER_APELLIDOS2",
						"PER_NIFCIF",
						"TE_NOMBRE",
						"TE_ESGENERAL",
						"CLA_NOMBRE",
						"FASE_NOMBRE",
						"EST_NOMBRE",
						"EST_ESEJECUCIONSANCION",
						"EST_ESFINAL",
						"EST_ESAUTOMATICO",
						"EST_DESCRIPCION",
						"EST_IDFASE_SIGUIENTE",
						"EST_IDESTADO_SIGUIENTE",
						"EST_MENSAJE",
						"EST_PRE_SANCIONADO",
						"EST_PRE_VISIBLE",
						"EST_PRE_VISIBLEENFICHA",
						"EST_POST_ACTPRESCRITAS",
						"EST_POST_SANCIONPRESCRITA",
						"EST_POST_SANCIONFINALIZADA",
						"EST_POST_ANOTCANCELADAS",
						"EST_POST_VISIBLE",
						"EST_POST_VISIBLEENFICHA",
						"IVA_DESCRIPCION",
						"RES_DESCRIPCION",
						"RES_CODIGOEXT",
						"RES_BLOQUEADO",
						"JUZ_NOMBRE",
						"JUZ_DOMICILIO",
						"JUZ_CODIGOPOSTAL",
						"JUZ_IDPOBLACION",
						"JUZ_IDPROVINCIA",
						"JUZ_POBLACION",
						"JUZ_PROVINCIA",
						"JUZ_TELEFONO1",
						"JUZ_TELEFONO2",
						"JUZ_FAX1",
						"JUZ_FECHABAJA",
						"JUZ_CODIGOEXT",
						"JUZ_CODIGOPROCURADOR",
						"PROC_NOMBRE",
						"PROC_PRECIO",
						"PROC_IDJURISDICCION",
						"PROC_CODIGO",
						"PROC_COMPLEMENTO",
						"PROC_VIGENTE",
						"PROC_ORDEN",
						"PRETENSION",
						"OTRASPRETENSIONES",
						"CAMPOCONF11",
						"CAMPOCONF12",
						"CAMPOCONF13",
						"CAMPOCONF14",
						"CAMPOCONF15",
						"CAMPOCONF21",
						"CAMPOCONF22",
						"CAMPOCONF23",
						"CAMPOCONF24",
						"CAMPOCONF25"};	
				campos = new String[]{"IDINSTITUCION",
						"IDINSTITUCION_TIPOEXPEDIENTE",
						"IDTIPOEXPEDIENTE",
						"ANIOEXPEDIENTE",
						"NUMEROEXPEDIENTE",
						"FECHAINICIO",
						"ASUNTO",
						"ALERTAGENERADA",
						"ESVISIBLE",
						"ESVISIBLEENFICHA",
						"SANCIONADO",
						"SANCIONPRESCRITA",
						"ACTUACIONESPRESCRITAS",
						"SANCIONFINALIZADA",
						"ANOTACIONESCANCELADAS",
						"ANIOEXPDISCIPLINARIO",
						"NUMEXPDISCIPLINARIO",
						"NUMASUNTO",
						"FECHAINICIALESTADO",
						"FECHAFINALESTADO",
						"FECHAPRORROGAESTADO",
						"DESCRIPCIONRESOLUCION",
						"FECHARESOLUCION",
						"FECHACADUCIDAD",
						"OBSERVACIONES",
						"MINUTA",
						"IDPERSONA",
						"PER_NOMBRE",
						"PER_APELLIDOS1",
						"PER_APELLIDOS2",
						"PER_NIFCIF",
						"TE_NOMBRE",
						"TE_ESGENERAL",
						"CLA_NOMBRE",
						"FASE_NOMBRE",
						"EST_NOMBRE",
						"EST_ESEJECUCIONSANCION",
						"EST_ESFINAL",
						"EST_ESAUTOMATICO",
						"EST_DESCRIPCION",
						"EST_IDFASE_SIGUIENTE",
						"EST_IDESTADO_SIGUIENTE",
						"EST_MENSAJE",
						"EST_PRE_SANCIONADO",
						"EST_PRE_VISIBLE",
						"EST_PRE_VISIBLEENFICHA",
						"EST_POST_ACTPRESCRITAS",
						"EST_POST_SANCIONPRESCRITA",
						"EST_POST_SANCIONFINALIZADA",
						"EST_POST_ANOTCANCELADAS",
						"EST_POST_VISIBLE",
						"EST_POST_VISIBLEENFICHA",
						"IVA_DESCRIPCION",
						"RES_DESCRIPCION",
						"RES_CODIGOEXT",
						"RES_BLOQUEADO",
						"JUZ_NOMBRE",
						"JUZ_DOMICILIO",
						"JUZ_CODIGOPOSTAL",
						"JUZ_IDPOBLACION",
						"JUZ_IDPROVINCIA",
						"JUZ_POBLACION",
						"JUZ_PROVINCIA",
						"JUZ_TELEFONO1",
						"JUZ_TELEFONO2",
						"JUZ_FAX1",
						"JUZ_FECHABAJA",
						"JUZ_CODIGOEXT",
						"JUZ_CODIGOPROCURADOR",
						"PROC_NOMBRE",
						"PROC_PRECIO",
						"PROC_IDJURISDICCION",
						"PROC_CODIGO",
						"PROC_COMPLEMENTO",
						"PROC_VIGENTE",
						"PROC_ORDEN",
						"PRETENSION",
						"OTRASPRETENSIONES",
						"CAMPOCONF11",
						"CAMPOCONF12",
						"CAMPOCONF13",
						"CAMPOCONF14",
						"CAMPOCONF15",
						"CAMPOCONF21",
						"CAMPOCONF22",
						"CAMPOCONF23",
						"CAMPOCONF24",
						"CAMPOCONF25"};
			
			
				//,"COMUNICACIONES"};
			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", user.getLocation()+"_"+this.getUserName(request).toString());
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "generaExcel";
	}
	
	
}

