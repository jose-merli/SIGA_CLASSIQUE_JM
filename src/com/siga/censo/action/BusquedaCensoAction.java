// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'

/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.VleLetradosSigaAdm;
import com.siga.censo.form.BusquedaCensoForm;
import com.siga.censo.form.DireccionesForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class BusquedaCensoAction extends MasterAction {	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						BusquedaCensoForm formClientes = (BusquedaCensoForm)miForm;
						formClientes.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						formClientes.reset(mapping,request);
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("abrirBusquedaCensoModal")){
						// abrirBusquedaModal
						mapDestino = abrirBusquedaCensoModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("enviarClienteCenso")){
						// enviarCliente
						mapDestino = enviarClienteCenso(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarModal")){
						// buscarModal
						//borrarPaginador(mapping, miForm, request, response);
						//mapDestino = buscarModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarModalInit")){
						String idPaginador = getIdPaginador(super.paginadorModal,getClass().getName());
						borrarPaginador(request, idPaginador);
 						//borrarPaginadorModal(mapping, miForm, request, response);
						//mapDestino = buscarModal(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("buscarTodosModalInit")){
						borrarPaginador(request, paginadorModal);
												
						mapDestino = buscarTodosModal(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("buscarTodosArt27Modal")){
						borrarPaginador(request, paginadorModal);
												
						mapDestino = buscarTodosArt27Modal(mapping, miForm, request, response);						
					}else if (accion.equalsIgnoreCase("buscarTodosModal")){
						mapDestino = buscarTodosModal(mapping, miForm, request, response);
					
					} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxComboDirecciones")){
						ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxComboDirecciones", 10);
						getAjaxComboDirecciones(mapping, miForm, request, response);
						return null;
						
					} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxDirecciones")){
						ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxDirecciones", 10);
						getAjaxDirecciones(mapping, miForm, request, response);
						return null;
					} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxBusquedaNIF")){
						ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxBusquedaNIF", 10);
						getAjaxBusquedaNIF(mapping, miForm, request, response);
						return null;
					}
					
					else if (accion.equalsIgnoreCase("buscarPersonaInit")){
						// buscarPersona
						//borrarPaginador(mapping, miForm, request, response);
						String idPaginador = getIdPaginador(super.paginador,getClass().getName());
						borrarPaginador(request, idPaginador);
						
						//mapDestino = buscarPersonasModal(mapping, miForm, request, response);
					}  else if (accion.equalsIgnoreCase("buscarInit")){
						miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						request.getSession().removeAttribute("DATAPAGINADOR");
						
						//borrarPaginador(mapping, miForm, request, response);
						//String idPaginador = getIdPaginador(super.paginador,getClass().getName());
						//borrarPaginador(request, idPaginador);
						
						mapDestino = buscarPor(mapping, miForm, request, response); 
					}else if (accion.equalsIgnoreCase("insertarNoColegiado")){
						mapDestino = insertarNoColegiado(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("insertarNoColegiadoArticulo27")){
						mapDestino = insertarNoColegiadoArt27(mapping, miForm, request, response);						
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}

	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarNoColegiado (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
    	String forward ="exception";
		try {
			// Obtengo los datos del formulario
	     	BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
			//BusquedaCensoForm miForm = (BusquedaCensoForm)request.getSession().getAttribute("BusquedaCensoForm");
			UsrBean usr = this.getUserBean(request);
			
			CenPersonaAdm adminPer=new CenPersonaAdm(usr);
//			String dni = miForm.getNif();
			//Comprobamos si es cliente nuestro, si no es lo introducimos en cenNOcolegiado
   			CenClienteAdm clienteAdm = new CenClienteAdm(usr);
   			
   			CenClienteBean cli = clienteAdm.existeCliente(new Long(miForm.getIdPersona()),new Integer(usr.getLocation()));
   				if (cli==null) {
   					forward =  insertarNoCol(mapping, miForm, request, response, new Long(miForm.getIdPersona()));
   						//insertNoColegiado (CenPersonaBean beanPer, CenClienteBean beanCli, HttpServletRequest request, String continuar);
   				}else{
   						//El letrado seleccionado ya se encuentra registrado en el Colegio, pulse en el botón “Seleccionar” del Expediente
   						//miForm.setAccion("messages.censo.existeColegiado");
   						//miForm.setIdInstitucion(cli.getIdInstitucion().toString());
						miForm.setAccion("messages.fichaCliente.clienteExiste");
   						
   						forward = "validarNoColegiado";
   		    			

   						//return exitoModal("messages.censo.existeColegiado",request);

   				}	
		

	   } catch (Exception e) {
		   //throw new SIGAException (e);
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
	}
	
	protected String insertarNoColegiadoArt27 (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
    	String forward ="exception";
		try {
			// Obtengo los datos del formulario
	     	BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
			UsrBean usr = this.getUserBean(request);
			CenPersonaAdm adminPer=new CenPersonaAdm(usr);
   			CenClienteAdm clienteAdm = new CenClienteAdm(usr);   			
   			CenClienteBean cli = null;
   			UserTransaction t = null; 
   			
   			if(miForm.getIdPersona()!=null && !miForm.getIdPersona().equals("")){
   				cli = clienteAdm.existeCliente(new Long(miForm.getIdPersona()),new Integer(usr.getLocation()));
   			}
   			
   			//Se mira si existe ya en el sistema o no
			if (cli==null) {
				forward =  insertarNoColArticulo27(mapping, miForm, request, response, new Long(miForm.getIdPersona()));

			}else{	
				
				Direccion direccion = new Direccion();
				t = usr.getTransactionPesada();
				t.begin ();
				
				CenDireccionesBean beanDir = new CenDireccionesBean ();
				beanDir.setCodigoPostal (miForm.getCodPostal());
				beanDir.setCorreoElectronico (miForm.getMail());
				beanDir.setDomicilio (miForm.getDireccion());
				beanDir.setFax1 (miForm.getFax1 ());
				beanDir.setFax2 (miForm.getFax2 ());
				beanDir.setIdInstitucion(new Integer(usr.getLocation()));
				beanDir.setIdPais (miForm.getPais ());
				if (miForm.getPais ().equals ("")) {
					miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
				}
				if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
					beanDir.setIdPoblacion (miForm.getPoblacion ());
					beanDir.setIdProvincia (miForm.getProvincia ());
					beanDir.setPoblacionExtranjera ("");
				} else {
					beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
					beanDir.setIdPoblacion ("");
					beanDir.setIdProvincia ("");
				}
				
				beanDir.setIdPersona (new Long(miForm.getIdPersona()));
				beanDir.setMovil (miForm.getMovil ());
				beanDir.setPaginaweb (miForm.getPaginaWeb ());
				beanDir.setTelefono1 (miForm.getTelefono ());
				beanDir.setTelefono2 (miForm.getTelefono2 ());
				beanDir.setPreferente(miForm.getPreferente());
				
				//estableciendo los datos del tipo de direccion
				String tiposDir = "";
				if(miForm.getTipoDireccion()!= null && !miForm.getTipoDireccion().equals("")){
					tiposDir = miForm.getTipoDireccion();
				}
				
				//estableciendo los datos del Historico
				CenHistoricoBean beanHis = new CenHistoricoBean ();
				beanHis.setMotivo ("");
				
				//Si la dirección es nueva se añade para este no colegiado se inserta en el sistema
				Direccion dirAux;
				if(miForm.getDirecciones()!= null && miForm.getDirecciones().equals("-1")){
					// Se llama a la interfaz Direccion para insertar una nueva direccion
					dirAux = direccion.insertar(beanDir, tiposDir, beanHis, null, usr);
				}else{					
					// Se llama a la interfaz Direccion para insertar una nueva direccion
					beanDir.setIdDireccion(new Long(miForm.getIdDireccion()));
					beanDir.setOriginalHash ((Hashtable) request.getSession ().getAttribute ("ORIGINALDIR"));
					dirAux = direccion.actualizarDireccion(beanDir, tiposDir, beanHis, null, usr);
				}
				
				//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
				if(dirAux.isFallo()){
					t.rollback();
					return exito(dirAux.getMsgError(), request);
				}
				
				//confirmando las modificaciones de BD
				t.commit();				
				
				//Mandamos los datos para el refresco:
				request.setAttribute("idPersona",miForm.getIdPersona());
				request.setAttribute("nombre",miForm.getNombre());
				request.setAttribute("apellido1",miForm.getApellido1());
				request.setAttribute("apellido2",miForm.getApellido2());
				String ncol = "No Colegiado";
				if(miForm.getIdInstitucion().equals(usr.getLocation())){
					ncol = miForm.getNumeroColegiado();
				}
				
				request.setAttribute("nColegiado",ncol);
				forward = "exitoInsercionNoColegiadoArt27";
			}	
			
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, null);	
	   
	    } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	    }
		return forward;
	}	
	
	protected String insertarNoColArticulo27 (ActionMapping mapping,BusquedaCensoForm miForm,HttpServletRequest request,HttpServletResponse response, Long idpersona) throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {		
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = this.getUserBean(request);
			Direccion direccion = new Direccion();
			CenClienteAdm adminCli=new CenClienteAdm(usr);
			CenClienteBean beanCli = new  CenClienteBean();
			String institucion =  miForm.getIdInstitucion();
			beanCli =  adminCli.insertNoColegiadoCenso ( request, new Long(miForm.getIdPersona()),institucion);
			String mensInformacion = "messages.inserted.success"; 
			if (!adminCli.getError().equals("")) {
				mensInformacion = adminCli.getError();
			}

			tx = usr.getTransaction();			
			tx.begin();
						
			// Inserto los datos del no colegiado en CenNoColegiado:
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCli.getIdInstitucion());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCli.getIdPersona());
			//El tipo sera siempre Personal:
			hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,ClsConstants.COMBO_TIPO_PERSONAL);
			//No es sociedad SJ:
			hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_FALSE);
			hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,usr.getUserName());
			hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
			
   		    admNoColegiado.insert(hashNoColegiado);
   		    
   		    /////////  INSERTAMOS LA INFO DE LA DIRECCION //////////

   		    CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setCodigoPostal (miForm.getCodPostal());
			beanDir.setCorreoElectronico (miForm.getMail());
			beanDir.setDomicilio (miForm.getDireccion());
			beanDir.setFax1 (miForm.getFax1 ());
			beanDir.setFax2 (miForm.getFax2 ());
			beanDir.setIdInstitucion(new Integer(usr.getLocation()));
			beanDir.setIdPais (miForm.getPais ());
			if (miForm.getPais ().equals ("")) {
				miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdPoblacion (miForm.getPoblacion ());
				beanDir.setIdProvincia (miForm.getProvincia ());
				beanDir.setPoblacionExtranjera ("");
			} else {
				beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
				beanDir.setIdPoblacion ("");
				beanDir.setIdProvincia ("");
			}
			beanDir.setIdPersona (beanCli.getIdPersona());
			beanDir.setMovil (miForm.getMovil ());
			beanDir.setPaginaweb (miForm.getPaginaWeb ());
			beanDir.setTelefono1 (miForm.getTelefono ());
			beanDir.setTelefono2 (miForm.getTelefono2 ());
			beanDir.setPreferente(miForm.getPreferente());
			
			//estableciendo los datos del tipo de direccion
			String tiposDir = "";
			if(miForm.getTipoDireccion()!= null && !miForm.getTipoDireccion().equals("")){
				tiposDir = miForm.getTipoDireccion();
			}
				
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo ("");
				
			// Se llama a la interfaz Direccion para insertar una nueva direccion
			Direccion dirAux = direccion.insertar(beanDir, tiposDir, beanHis, null, usr);
				
			//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
			if(dirAux.isFallo()){
				tx.rollback();
				return exito(dirAux.getMsgError(), request);
			}
				
			//confirmando las modificaciones de BD
			tx.commit();	

			//Mandamos los datos para el refresco:
			request.setAttribute("idPersona",miForm.getIdPersona());
			request.setAttribute("nombre",miForm.getNombre());
			request.setAttribute("apellido1",miForm.getApellido1());
			request.setAttribute("apellido2",miForm.getApellido2());
			String ncol = "No Colegiado";
			if(miForm.getIdInstitucion().equals(usr.getLocation())){
				ncol = miForm.getNumeroColegiado();
			}
			
			request.setAttribute("nColegiado",ncol);
		
		} catch (SIGAException e) {
			throw e;		
	    } catch (Exception e) {
		   throw new SIGAException (e);
		   //throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return "exitoInsercionNoColegiadoArt27";			
	}
		
	protected String insertarNoCol (ActionMapping mapping,	BusquedaCensoForm miForm,	HttpServletRequest request,	HttpServletResponse response, Long idpersona) throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {		
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = this.getUserBean(request);
			Direccion direccion = new Direccion();
			CenClienteAdm adminCli=new CenClienteAdm(usr);
			CenClienteBean beanCli = new  CenClienteBean();
			String institucion =  miForm.getIdInstitucion();
			beanCli =  adminCli.insertNoColegiadoCenso ( request, new Long(miForm.getIdPersona()),institucion);
			String mensInformacion = "messages.inserted.success"; 
			if (!adminCli.getError().equals("")) {
				mensInformacion = adminCli.getError();
			}

			tx = usr.getTransaction();			
			tx.begin();
						
			// Inserto los datos del no colegiado en CenNoColegiado:
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,beanCli.getIdInstitucion());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,beanCli.getIdPersona());
			//El tipo sera siempre Personal:
			hashNoColegiado.put(CenNoColegiadoBean.C_TIPO,ClsConstants.COMBO_TIPO_PERSONAL);
			//No es sociedad SJ:
			hashNoColegiado.put(CenNoColegiadoBean.C_SOCIEDADSJ,ClsConstants.DB_FALSE);
			hashNoColegiado.put(CenNoColegiadoBean.C_USUMODIFICACION,usr.getUserName());
			hashNoColegiado.put(CenNoColegiadoBean.C_FECHAMODIFICACION,"SYSDATE");
			
   		    admNoColegiado.insert(hashNoColegiado);
   		    
   		    /////////  INSERTAMOS LA INFO DE LA DIRECCION //////////

   		    CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setCodigoPostal (miForm.getCodPostal());
			beanDir.setCorreoElectronico (miForm.getMail());
			beanDir.setDomicilio (miForm.getDireccion());
			beanDir.setFax1 (miForm.getFax1 ());
			beanDir.setFax2 (miForm.getFax2 ());
			beanDir.setIdInstitucion(new Integer(usr.getLocation()));
			beanDir.setIdPais (miForm.getPais ());
			if (miForm.getPais ().equals ("")) {
				miForm.setPais (ClsConstants.ID_PAIS_ESPANA);
			}
			if (miForm.getPais().equals (ClsConstants.ID_PAIS_ESPANA)) {
				beanDir.setIdPoblacion (miForm.getPoblacion ());
				beanDir.setIdProvincia (miForm.getProvincia ());
				beanDir.setPoblacionExtranjera ("");
			} else {
				beanDir.setPoblacionExtranjera (miForm.getPoblacionExt ());
				beanDir.setIdPoblacion ("");
				beanDir.setIdProvincia ("");
			}
			beanDir.setIdPersona (beanCli.getIdPersona());
			beanDir.setMovil (miForm.getMovil ());
			beanDir.setPaginaweb (miForm.getPaginaWeb ());
			beanDir.setTelefono1 (miForm.getTelefono ());
			beanDir.setTelefono2 (miForm.getTelefono2 ());
				
			//estableciendo los datos del tipo de direccion
			String tiposDir = "3";
				
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo ("");
				
			// Se llama a la interfaz Direccion para insertar una nueva direccion
			Direccion dirAux = direccion.insertar(beanDir, tiposDir, beanHis, request, usr);
				
			//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
			if(dirAux.isFallo()){
				tx.rollback();
				return exito(dirAux.getMsgError(), request);
			}
			
			request.setAttribute("idDireccion",beanDir.getIdDireccion().toString());
			
			//confirmando las modificaciones de BD
			tx.commit();	
   		    
////////////////////////////////////////////////////////////////////////////////////////

			//Mandamos los datos para el refresco:
			request.setAttribute("mensaje",mensInformacion);
			request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
			request.setAttribute("idPersona",beanCli.getIdPersona().toString());
			request.setAttribute("nColegiado",miForm.getNumeroColegiado());
			request.setAttribute("nif",miForm.getNif());
			request.setAttribute("nombre",miForm.getNombre());
			request.setAttribute("apellido1",miForm.getApellido1());
			request.setAttribute("apellido2",miForm.getApellido2());
	       
		} catch (SIGAException e) {
			throw e;		
	   
		} catch (Exception e) {
		   throw new SIGAException (e);
		   //throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return "exitoInsercionNoColegiado";			
	}

	
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			
			// borro el formulario en session de Avanzada
			BusquedaCensoForm miformSession = (BusquedaCensoForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
			BusquedaCensoForm miformSession2 = (BusquedaCensoForm)request.getSession().getAttribute("BusquedaCensoForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
			BusquedaCensoForm miform = (BusquedaCensoForm)formulario;
			miform.reset(mapping,request);
			
			String colegiado = request.getParameter("colegiado");
			miform.setNumeroColegiado(colegiado);
			
			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
			request.setAttribute("CenInstitucionesVisibles",visibilidad);
	
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
			// Debemos borrar los datos del paginador que esta en sesion por si tuviera informacion de consultas hechas en otras opciones de menu
			//request.getSession().removeAttribute("DATAPAGINADOR");
			
		/** Cuando venimos de la pestaña de datos de colegiacion**/	
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			String nColegiado="";
			String idInstitucion="";
			String nifcif="";
	       
	        if (request.getParameter("nColegiado")!=null){
	        	nColegiado=(String)request.getParameter("nColegiado");
	        }
	        if (request.getParameter("idInstitucion")!=null){
	        	idInstitucion=(String)request.getParameter("idInstitucion");
	        }
	        if (request.getParameter("nifcif")!=null){
	        	nifcif=(String)request.getParameter("nifcif");
	        }
	        request.setAttribute("nColegiado",nColegiado);
	        request.setAttribute("idInstitucion",idInstitucion);
	        request.setAttribute("nifcif",nifcif);
	      /***************************************************/  
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}

	/*
	 * obtienen la visibilidad para el usuario 
	 */
	private String obtenerVisibilidadUsuario(HttpServletRequest req) throws SIGAException {
		try {
			UsrBean user = (UsrBean) req.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			return CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
	}

	/**
	 * Metodo que implementa el modo abrirConParametros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action 
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
			request.setAttribute("CenInstitucionesVisibles",visibilidad);

			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
			
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return "inicio";
	}



	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String elementoActivo="1";
		try {
	     	BusquedaCensoForm miform = (BusquedaCensoForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion = (String)vOcultos.get(1);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String[] pestanasOcultas=new String [1];
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String verFichaLetrado = request.getParameter("verFichaLetrado");
			if (verFichaLetrado == null){
				verFichaLetrado = miform.getVerFichaLetrado();
			}
			if (verFichaLetrado!=null && verFichaLetrado.equals("1")){
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				elementoActivo="3";
				
			}else{
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} else {
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));

				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){//si es letrado 
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{//si no es letrado
					// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
					// obtengo los datos de nocolegiado
					Hashtable hashNoCol = new Hashtable();
					hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
					hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
					CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
					Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							if (nocolBean.getSociedadSJ().equals("1") || (nocolBean.getSociedadSP().equals("1")) ){
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
							}else{
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
							}
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}
			  }	
				
			}
			}
			
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			String modo = "editar";
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			request.setAttribute("elementoActivo",elementoActivo);    
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
	
			request.setAttribute("datosCliente", datosCliente);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}	
		return "administracion";
	}

	/**
	 * Metodo que implementa el modo recargarEditar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */

	
	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		
		try{	
		 	BusquedaCensoForm miform = (BusquedaCensoForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion = (String)vOcultos.get(1);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			String modo = "ver";
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			
			String[] pestanasOcultas=new String [1];
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			}else{
				
			  
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));
				
				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{

				// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
				// obtengo los datos de nocolegiado
				Hashtable hashNoCol = new Hashtable();
				hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
				hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
				CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
				Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}			
			   }	
			 }
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
          
			request.setAttribute("datosCliente", datosCliente);		
			destino="administracion";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	 	 }
		 return destino;
    }

	protected String abrirBusquedaCensoModal (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
	try {
		BusquedaCensoForm miFormulario = (BusquedaCensoForm)formulario;
		miFormulario.setNif("");
		// obtengo la visibilidad para el user
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String visibilidad = CenVisibilidad.getVisibilidadCenso(user.getLocation());
		request.setAttribute("CenInstitucionesVisibles",visibilidad);
		request.setAttribute("clientes",request.getParameter("clientes"));
		request.setAttribute("deudor",request.getParameter("deudor"));
		if (request.getParameter("busquedaSancion")!=null && request.getParameter("busquedaSancion").equals("1")){
		request.setAttribute("busquedaSancion","1");
	}
	} 	
	catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	}
	
	// COMUN
	return "inicioCenso";
	}

	
	protected String enviarClienteCenso (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		String destino = "";
		Vector v = new Vector();
		try {
		
			BusquedaCensoForm miform = (BusquedaCensoForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			//Vector vOcultos = miform.getDatosTablaOcultos(0);
			
			
			// obtener idpersona
			String idPersona = miform.getIdPersona();
			// obtener idinstitucion
			String idInstitucion = miform.getIdInstitucion();
			
			CenPersonaAdm perAdm = new CenPersonaAdm(user);
			CenPersonaBean perBean = new CenPersonaBean();
			perBean = perAdm.getPersonaPorId(idPersona);
			
			// obtener nColegiado
			//String nColegiado = (String)vOcultos.get(2);
			String nColegiado = miform.getNumeroColegiado();
			// obtener nifCif
			//String nifCif = (String)vOcultos.get(3);
			String nifCif = miform.getNif();
			// obtener nombre
			//String nombre = (String)vOcultos.get(4);
			String nombre = miform.getNombre().replace("\u00a0"," ").trim();
			String apellido1 = miform.getApellido1().replace("\u00a0"," ").trim();
			String apellido2 = miform.getApellido2().replace("\u00a0"," ").trim();
			String poblacion = miform.getPoblacion().replace("\u00a0"," ").trim();
			String provincia = miform.getProvincia().replace("\u00a0"," ").trim();
			String direcion = miform.getDireccion().replace("\u00a0"," ").trim();
			String codPostal = miform.getCodPostal().replace("\u00a0"," ").trim();
			String telefono = miform.getTelefono().replace("\u00a0"," ").trim();
			String mail = miform.getMail().replace("\u00a0"," ").trim();
			String sexo = miform.getSexo().replace("\u00a0"," ").trim();
			String tratamiento = miform.getTratamiento().replace("\u00a0"," ").trim();			
			String fax = miform.getFax1().replace("\u00a0"," ").trim();		
			String pais = miform.getPais().replace("\u00a0"," ").trim();		
			
			Hashtable datosCliente = new Hashtable();
			
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("nColegiado",nColegiado);
			datosCliente.put("nifCif",nifCif);
			datosCliente.put("nombre",nombre);
			datosCliente.put("apellido1",apellido1);
			datosCliente.put("apellido2",apellido2);
			datosCliente.put("poblacion",poblacion);
			datosCliente.put("provincia",provincia);
			datosCliente.put("direcion",direcion);
			datosCliente.put("codPostal",codPostal);
			datosCliente.put("telefono",telefono);
			datosCliente.put("idDireccion","");
			datosCliente.put("pais",pais);
			datosCliente.put("mail",mail);
			datosCliente.put("sexo",sexo);
			datosCliente.put("tratamiento",tratamiento);
			datosCliente.put("fax1",fax);
			datosCliente.put("FechaNacimiento",perBean.getFechaNacimiento());
			datosCliente.put("LugarNacimiento",perBean.getNaturalDe());
			
			request.setAttribute("datosCensoModal", datosCliente);	

			request.setAttribute("resultado", datosCliente);
		
			destino="seleccionCenso";
			} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}


	protected String buscarTodosModal(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// casting del formulario
		BusquedaCensoForm miFormulario = (BusquedaCensoForm)formulario;
		String idInstitucion=	(String)request.getParameter("nombreInstitucion");	
		// busqueda de clientes
		VleLetradosSigaAdm cliente = new VleLetradosSigaAdm(this.getUserBean(request));
		
		if (request.getParameter("busquedaSancion")!=null && request.getParameter("busquedaSancion").equals("1")){
			request.setAttribute("busquedaSancion","1");			
		}
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorModal);
		request.setAttribute("si",UtilidadesString.getMensajeIdioma(user, "general.yes"));
		request.setAttribute("no",UtilidadesString.getMensajeIdioma(user, "general.no"));
	
		
		
		try {
			HashMap databackup=getPaginador(request, paginadorModal);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					 
					request.setAttribute("letradoList", datos);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
				}else{
					request.setAttribute("letradoList", new Vector());
					databackup.put("datos",new Vector());
					
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorModal, databackup);
					
				}	
				

			}else{	
				databackup=new HashMap();
				//Haria falta meter los parametros en con ClsConstants

				PaginadorBind paginador = cliente.getClientesCenso ( idInstitucion,user.getLocation(),miFormulario, user.getLanguage());
				
				
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					request.setAttribute("letradoList", datos);
					databackup.put("datos",datos);
					
					setPaginador(request, paginadorModal, databackup);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					request.setAttribute("letradoList", new Vector());
					setPaginador(request, paginadorModal, databackup);
					
				} 	


			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "resultadoCenso";
	}
	
	protected String buscarTodosArt27Modal(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// casting del formulario
		BusquedaCensoForm miFormulario = (BusquedaCensoForm)formulario;
		String idInstitucion = miFormulario.getColegiadoen();	
		// busqueda de clientes
		VleLetradosSigaAdm cliente = new VleLetradosSigaAdm(this.getUserBean(request));
		
		if (request.getParameter("busquedaSancion")!=null && request.getParameter("busquedaSancion").equals("1")){
			request.setAttribute("busquedaSancion","1");
			
		}
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorModal);
		request.setAttribute("si",UtilidadesString.getMensajeIdioma(user, "general.yes"));
		request.setAttribute("no",UtilidadesString.getMensajeIdioma(user, "general.no"));
	
		
		
		try {
			HashMap databackup=getPaginador(request, paginadorModal);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					 
					request.setAttribute("letradoList", datos);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
				}else{
					request.setAttribute("letradoList", new Vector());
					databackup.put("datos",new Vector());
					
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorModal, databackup);
					
				}	
				

			}else{	
				databackup=new HashMap();
				//Haria falta meter los parametros en con ClsConstants

				PaginadorBind paginador = cliente.getClientesCensoArticulo27(idInstitucion,user.getLocation(),miFormulario, user.getLanguage());
				
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					request.setAttribute("letradoList", datos);
					databackup.put("datos",datos);
					
					setPaginador(request, paginadorModal, databackup);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					request.setAttribute("letradoList", new Vector());
					setPaginador(request, paginadorModal, databackup);
				} 	
			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "resultadoCenso";
	}
	
	@SuppressWarnings("unchecked")
	protected void getAjaxDirecciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");
		String idPersona = request.getParameter("idPersona");
		String idDireccion = request.getParameter("idDireccion");
		
		if(idDireccion.equals("-1")){
			miForm.setFax1("");
			miForm.setFax2("");
			miForm.setMail("");
			miForm.setPaginaWeb("");
			miForm.setMovil("");
			miForm.setTelefono("");
			miForm.setTelefono2("");
			miForm.setPoblacion("");
			miForm.setProvincia("");
			miForm.setPais("");
			miForm.setDireccion("");
			miForm.setCodPostal("");
			miForm.setPreferente ("");
			miForm.setTipoDireccion("");
			miForm.setPoblacionExt("");
			
		}else{
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(user);
			Hashtable direccion = dirAdm.selectDirecciones(new Long(idPersona), new Integer(idInstitucion), new Long(idDireccion));
			
			miForm.setFax1((String)direccion.get("FAX1"));
			miForm.setFax2((String)direccion.get("FAX2"));
			miForm.setMail((String)direccion.get("CORREOELECTRONICO"));
			miForm.setPaginaWeb((String)direccion.get("PAGINAWEB"));
			miForm.setMovil((String)direccion.get("MOVIL"));
			miForm.setTelefono((String)direccion.get("TELEFONO1"));
			miForm.setTelefono2((String)direccion.get("TELEFONO2"));
			miForm.setPoblacion((String)direccion.get("IDPOBLACION"));
			miForm.setProvincia((String)direccion.get("IDPROVINCIA"));
			miForm.setPais((String)direccion.get("IDPAIS"));
			miForm.setDireccion((String)direccion.get("DOMICILIO"));
			miForm.setCodPostal((String)direccion.get("CODIGOPOSTAL"));
			miForm.setPreferente ((String)direccion.get("PREFERENTE"));
			miForm.setTipoDireccion ((String)direccion.get("IDTIPODIRECCION"));
			miForm.setPoblacionExt((String)direccion.get("POBLACIONEXTRANJERA"));
			miForm.setIdDireccion((String)direccion.get("IDDIRECCION"));
			request.getSession().setAttribute("ORIGINALDIR", direccion);
		}

		List listaParametros = new ArrayList();
		listaParametros.add(miForm.getFax1());
		listaParametros.add(miForm.getFax2());
		listaParametros.add(miForm.getMail());
		listaParametros.add(miForm.getPaginaWeb());
		listaParametros.add(miForm.getMovil());
		listaParametros.add(miForm.getTelefono());
		listaParametros.add(miForm.getTelefono2());
		listaParametros.add(miForm.getPoblacion());
		listaParametros.add(miForm.getProvincia());
		listaParametros.add(miForm.getPais());
		listaParametros.add(miForm.getDireccion());
		listaParametros.add(miForm.getPreferente());
		listaParametros.add(miForm.getTipoDireccion());
		listaParametros.add(miForm.getCodPostal());
		listaParametros.add(miForm.getPoblacionExt());
		listaParametros.add(miForm.getIdDireccion());

		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
	}	
	
	@SuppressWarnings("unchecked")
	protected void getAjaxComboDirecciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");
		String idPersona = request.getParameter("idPersona");		
		List alDirecciones = null;
		
		if((idPersona!= null && !idPersona.equals("")) && (idInstitucion != null && !idInstitucion.equals(""))){
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(user);
			alDirecciones = dirAdm.geDireccionesLetrado(idPersona, idInstitucion);
		}else{
			alDirecciones = new ArrayList<DireccionesForm>();
			DireccionesForm dirForm = new DireccionesForm();
			dirForm.setNombre("-- Nueva");
			dirForm.setIdDireccion(new Long(-1));
			alDirecciones.add(dirForm);		
		}
		
		respuestaAjax(new AjaxCollectionXmlBuilder(), alDirecciones,response);

	}	
	
	@SuppressWarnings("unchecked")
	protected void getAjaxBusquedaNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		BusquedaCensoForm miForm = (BusquedaCensoForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String tipoIden = request.getParameter("tipoIdentificacion").trim();
		String nif = request.getParameter("numIdentificacion").trim();
		String nombre = request.getParameter("nombre").trim();
		String ape1 = request.getParameter("apellido1").trim();
		String ape2 = request.getParameter("apellido2");
		String colegiadoen = request.getParameter("colegiadoen").trim();
		String ncol = request.getParameter("nColegiado").trim();
		String idInstitucion = user.getLocation();
		miForm.setTextoAlerta("");
		List listaParametros = new ArrayList();
	
		if(nif!=null && !nif.equals("")){
			VleLetradosSigaAdm cliente = new VleLetradosSigaAdm(this.getUserBean(request));
			Hashtable infoCliente = cliente.getBusquedPorNIF(nif, idInstitucion);
			CenPersonaAdm perAdm = new CenPersonaAdm(user);
			CenPersonaBean perBean = new CenPersonaBean();
			
			if(infoCliente != null){
				perBean = perAdm.getPersona(nif);
				miForm.setIdPersona((String)infoCliente.get("ID_LETRADO"));
				miForm.setColegiadoen((String)infoCliente.get("ID_COLEGIO"));
				miForm.setNumeroColegiado((String)infoCliente.get("NUM_COLEGIADO"));
				miForm.setApellido2((String)infoCliente.get("APELLIDO2"));
				miForm.setApellido1((String)infoCliente.get("APELLIDO1"));
				miForm.setNombre((String)infoCliente.get("NOMBRE"));
				miForm.setNif(nif);
				miForm.setIdInstitucion((String)infoCliente.get("ID_COLEGIO"));
				miForm.setFax1((String)infoCliente.get("FAX"));
				miForm.setMail((String)infoCliente.get("MAIL"));
				miForm.setTelefono((String)infoCliente.get("TELEFONO"));
				
				miForm.setProvincia((String)infoCliente.get("IDPROVINCIA"));
				miForm.setPais((String)infoCliente.get("IDPAIS"));
				miForm.setPoblacion((String)infoCliente.get("IDPOBLACION"));
				miForm.setPoblacionExt((String)infoCliente.get("POBLACION"));
				miForm.setDireccion((String)infoCliente.get("DIR_PROFESIONAL"));
				miForm.setCodPostal((String)infoCliente.get("COD_POSTAL"));
				miForm.setSexo((String)infoCliente.get("SEXO"));
				if(!perBean.getFechaNacimiento().equals("")){
					miForm.setFechaNacimiento(UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(user.getLanguage(),perBean.getFechaNacimiento())));
				}else{
					miForm.setFechaNacimiento("");
				}
				miForm.setLugarNacimiento(perBean.getNaturalDe());
				miForm.setTratamiento((String)infoCliente.get("TRATAMIENTO"));
				miForm.setTextoAlerta("");

			}else{
				miForm.setIdPersona("");
				miForm.setColegiadoen("");
				miForm.setNumeroColegiado("");
				miForm.setApellido2("");
				miForm.setApellido1("");
				miForm.setNombre("");
				miForm.setNif("");
				miForm.setIdInstitucion("");
				miForm.setFax1("");
				miForm.setMail("");
				miForm.setTelefono("");
				miForm.setPoblacion("");
				miForm.setProvincia("");
				miForm.setPais("");
				miForm.setDireccion("");
				miForm.setCodPostal("");
				miForm.setSexo("");
				miForm.setFechaNacimiento("");
				miForm.setLugarNacimiento("");
				miForm.setTratamiento("");
				perBean = perAdm.getPersona(nif);
				if(perBean != null){
					Long idPersona = perBean.getIdPersona();
					CenColegiadoAdm colAdm = new CenColegiadoAdm(user);
					CenColegiadoBean col = null;
					col = colAdm.existeColegiado(idPersona, new Integer(idInstitucion));
					
					if(col != null){
						Hashtable estadoColegial = colAdm.getEstadoColegial(idPersona, new Integer(idInstitucion));						
						if(((String)estadoColegial.get("IDESTADO")).equals(""+ClsConstants.ESTADO_COLEGIAL_EJERCIENTE)){//COLEGIADOS EJERCIENTES
							miForm.setTextoAlerta("Esta identificación pertenece a un colegiado ("+perBean.getNombre() + " " + perBean.getApellido1() +") ejerciente en este colegio. Realice la búsqueda pulsando en Búsq. Manual");
							miForm.setNif("");
							
						}else if(((String)estadoColegial.get("IDESTADO")).equals(""+ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL) ||
								 ((String)estadoColegial.get("IDESTADO")).equals(""+ClsConstants.ESTADO_COLEGIAL_SUSPENSION)   ||
								 ((String)estadoColegial.get("IDESTADO")).equals(""+ClsConstants.ESTADO_COLEGIAL_INHABILITACION)){//COLEGIADOS DADOS DE BAJA
							if(idInstitucion.equals(""+col.getIdInstitucion())){
								//COLEGIADO DE NUESTRO COLEGIO
								miForm.setIdPersona(""+perBean.getIdPersona());
								miForm.setNumeroColegiado(col.getNColegiado());
								miForm.setColegiadoen(""+col.getIdInstitucion());
								miForm.setApellido2(perBean.getApellido2());
								miForm.setApellido1(perBean.getApellido1());
								miForm.setNombre(perBean.getNombre());
								miForm.setNif(nif);	
								miForm.setIdInstitucion(""+col.getIdInstitucion());
								miForm.setSexo(perBean.getSexo());
								if(!perBean.getFechaNacimiento().equals("")){
									miForm.setFechaNacimiento(UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(user.getLanguage(),perBean.getFechaNacimiento())));
								}else{
									miForm.setFechaNacimiento("");
								}
								miForm.setLugarNacimiento(perBean.getNaturalDe());
								
							}else{
								//COLEGIADO DE OTRO COLEGIO
								miForm.setIdPersona(""+perBean.getIdPersona());
								miForm.setNumeroColegiado(col.getNColegiado());
								miForm.setColegiadoen(""+col.getIdInstitucion());
								miForm.setApellido2(perBean.getApellido2());
								miForm.setApellido1(perBean.getApellido1());
								miForm.setNombre(perBean.getNombre());
								miForm.setNif(nif);									
								miForm.setIdInstitucion(""+col.getIdInstitucion());
							}
							miForm.setTextoAlerta("Esta identificación pertenece a un colegiado ejerciente ("+perBean.getNombre() + " " + perBean.getApellido1() +") dado de baja en su colegio.");
						}						
					}else{
						CenNoColegiadoAdm nColAdm = new CenNoColegiadoAdm(user);
						CenNoColegiadoBean nCol = null;
						nCol = nColAdm.existeNoColegiado(idPersona);
						
						if(nCol!=null){						
							if(!nCol.getTipo().equals("1")){	//NO COLEGIADOS TIPO SOCIEDADES
								miForm.setTextoAlerta("Esta identificación pertenece a una Sociedad. No se puede designar.");
								miForm.setNif("");
								
							}else{	//NO COLEGIADOS DE OTROS COLEGIOS
								miForm.setTextoAlerta("Esta persona ya existe en el sistema. Se reuperarán sus datos de identificación.");
								miForm.setIdPersona(""+perBean.getIdPersona());
								miForm.setNumeroColegiado("No Colegiado");
								miForm.setApellido2(perBean.getApellido2());
								miForm.setApellido1(perBean.getApellido1());
								miForm.setNombre(perBean.getNombre());
								miForm.setNif(nif);
								miForm.setIdInstitucion(""+nCol.getIdInstitucion());
							}
						}
					}					
				}else{
					miForm.setNumeroColegiado(ncol);
					miForm.setApellido2(ape2);
					miForm.setApellido1(ape1);
					miForm.setNombre(nombre);
					miForm.setNif(nif);
					miForm.setColegiadoen(colegiadoen);					
				}
			}
			
			listaParametros.add(miForm.getTextoAlerta());
			listaParametros.add(miForm.getIdPersona());
			listaParametros.add(miForm.getColegiadoen());
			listaParametros.add(miForm.getNumeroColegiado());
			listaParametros.add(miForm.getApellido1());
			listaParametros.add(miForm.getApellido2());
			listaParametros.add(miForm.getNombre());
			listaParametros.add(miForm.getNif());
			listaParametros.add(miForm.getIdInstitucion());
			listaParametros.add(miForm.getFax1());
			listaParametros.add(miForm.getMail());
			listaParametros.add(miForm.getTelefono());
			listaParametros.add(miForm.getPoblacion());
			listaParametros.add(miForm.getPoblacionExt());
			listaParametros.add(miForm.getProvincia());
			listaParametros.add(miForm.getPais());
			listaParametros.add(miForm.getDireccion());
			listaParametros.add(miForm.getCodPostal());
			listaParametros.add(miForm.getSexo());
			listaParametros.add(miForm.getFechaNacimiento());
			listaParametros.add(miForm.getLugarNacimiento());
			listaParametros.add(miForm.getTratamiento());
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
		}
	}	
}
