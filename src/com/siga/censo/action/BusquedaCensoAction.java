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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColaCambioLetradoAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.VleLetradosSigaAdm;
import com.siga.censo.form.BusquedaCensoForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DesignaForm;

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
					}else if (accion.equalsIgnoreCase("buscarTodosModal")){
						mapDestino = buscarTodosModal(mapping, miForm, request, response);
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
	
	protected String insertarNoCol (ActionMapping mapping,	BusquedaCensoForm miForm,	HttpServletRequest request,	HttpServletResponse response, Long idpersona) throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {		
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = this.getUserBean(request);

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
   		    
   		    ///////////////////////////////////////////////////////////////////////////
   		 

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
				
				CenDireccionTipoDireccionBean vBeanTipoDir [] = new CenDireccionTipoDireccionBean [1];
				CenDireccionTipoDireccionBean b = new CenDireccionTipoDireccionBean ();
				b.setIdTipoDireccion (new Integer("3"));
				vBeanTipoDir[0] = b;
				
				
				//estableciendo los datos del Historico
				CenHistoricoBean beanHis = new CenHistoricoBean ();
				beanHis.setMotivo ("");
				
			//obteniendo adm de BD de direcciones
				CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean (request));
				
				//insertando la direccion
				if (! direccionesAdm.insertarConHistorico (beanDir, vBeanTipoDir, beanHis, this.getLenguaje (request)))
					throw new SIGAException (direccionesAdm.getError());
				
				
				//insertando en la cola de modificacion de datos para Consejos
				CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (this.getUserBean (request));
				if (! colaAdm.insertarCambioEnCola (ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION, 
						beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
					throw new SIGAException (colaAdm.getError ());
				request.setAttribute("idDireccion",beanDir.getIdDireccion().toString());

   		    
////////////////////////////////////////////////////////////////////////////////////////
			tx.commit();

			//Mandamos los datos para el refresco:
			request.setAttribute("mensaje",mensInformacion);
			request.setAttribute("idInstitucion",beanCli.getIdInstitucion().toString());
			request.setAttribute("idPersona",beanCli.getIdPersona().toString());
			request.setAttribute("nColegiado",miForm.getNumeroColegiado());
			request.setAttribute("nif",miForm.getNif());
			request.setAttribute("nombre",miForm.getNombre());
			request.setAttribute("apellido1",miForm.getApellido1());
			request.setAttribute("apellido2",miForm.getApellido2());
			
			
	   } 	
	   catch (Exception e) {
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
			
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			//Vector vOcultos = miform.getDatosTablaOcultos(0);
			
			
			// obtener idpersona
			String idPersona = miform.getIdPersona();
			// obtener idinstitucion
			String idInstitucion = miform.getIdInstitucion();
			
			// obtener nColegiado
			//String nColegiado = (String)vOcultos.get(2);
			String nColegiado = miform.getNumeroColegiado();
			// obtener nifCif
			//String nifCif = (String)vOcultos.get(3);
			String nifCif = miform.getNif();
			// obtener nombre
			//String nombre = (String)vOcultos.get(4);
			String nombre = miform.getNombre();
			String apellido1 = miform.getApellido1();
			String apellido2 = miform.getApellido2();
			String poblacion = miform.getPoblacion();
			String provincia = miform.getProvincia();
			String direcion = miform.getDireccion();
			String codPostal = miform.getCodPostal();
			String telefono = miform.getTelefono();
			String mail = miform.getMail();
			String sexo = miform.getSexo();
			String tratamiento = miform.getTratamiento();			
			String fax = miform.getFax1();			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
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
			datosCliente.put("idPais","");
			datosCliente.put("mail",mail);
			datosCliente.put("sexo",sexo);
			datosCliente.put("tratamiento",tratamiento);
			datosCliente.put("fax1",fax);			
			
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
	

	


}
