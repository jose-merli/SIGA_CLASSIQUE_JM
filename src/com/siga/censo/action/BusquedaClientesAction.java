// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'

/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Collection;
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
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.censo.form.BusquedaClientesForm;
import com.siga.facturacion.form.ConsultaMorososForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class BusquedaClientesAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={CenClienteBean.C_IDINSTITUCION,CenClienteBean.C_IDPERSONA,CenClienteBean.C_NOAPARECERREDABOGACIA};
	
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
						BusquedaClientesForm formClientes = (BusquedaClientesForm)miForm;
						formClientes.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						formClientes.reset(mapping,request);
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("abrirAvanzadaConParametros")){
						// abrirAvanzadaConParametros
						mapDestino = abrirAvanzadaConParametros(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("verFichaColegial")){
						// verFichaColegial
						mapDestino = verFichaColegial(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirBusquedaModal")){
						// abrirBusquedaModal
						mapDestino = abrirBusquedaModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("enviarCliente")){
						// enviarCliente
						mapDestino = enviarCliente(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarModal")){
						// buscarModal
						//borrarPaginador(mapping, miForm, request, response);
						mapDestino = buscarModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarModalInit")){
						String idPaginador = getIdPaginador(super.paginadorModal,getClass().getName());
						borrarPaginador(request, idPaginador);
 						//borrarPaginadorModal(mapping, miForm, request, response);
						mapDestino = buscarModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("recargarEditar")){
						// recargarEditar
						mapDestino = recargarEditar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("abrirBuscarPersona")){
						// abrirBuscarPersona
						mapDestino = abrirBuscarPersonaModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarPersona")){
						// buscarPersona
						mapDestino = buscarPersonasModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarPersonaInit")){
						// buscarPersona
						//borrarPaginador(mapping, miForm, request, response);
						String idPaginador = getIdPaginador(super.paginador,getClass().getName());
						borrarPaginador(request, idPaginador);
						
						mapDestino = buscarPersonasModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("enviarPersona")){
						// enviarCliente
						mapDestino = enviarPersona(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("nuevaSociedad")){
						// nuevaSociedad
						mapDestino = nuevaSociedad(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarInit")){
						miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						request.getSession().removeAttribute("DATAPAGINADOR");
						
						//borrarPaginador(mapping, miForm, request, response);
						//String idPaginador = getIdPaginador(super.paginador,getClass().getName());
						//borrarPaginador(request, idPaginador);
						
						mapDestino = buscarPor(mapping, miForm, request, response); 
					}
					else if (accion.equalsIgnoreCase("abrirBusquedaProcuradorModal")){
						// buscar Procurador
						mapDestino = abrirBusquedaProcuradorModal(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarProcuradorModalInit")){
						// buscar Procurador
						//borrarPaginadorModal(mapping, miForm, request, response);
						String idPaginador = getIdPaginador(super.paginadorModal,getClass().getName());
						borrarPaginador(request, idPaginador);
						
						mapDestino = buscarProcuradorModal(mapping, miForm, request, response);	
					} else if (accion.equalsIgnoreCase("buscarProcuradorModal")){
						// buscar Procurador
						mapDestino = buscarProcuradorModal(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("tagBusquedaPersona")){
						mapDestino = tagBusquedaPersona(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("generaExcel")){
						mapDestino = generaExcel(mapping, miForm, request, response);
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
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			
			// borro el formulario en session de Avanzada
			BusquedaClientesForm miformSession = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
			BusquedaClientesForm miformSession2 = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
			miform.reset(mapping,request);
			
			String colegiado = request.getParameter("colegiado");
			miform.setColegiado(colegiado);
			
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
	     	BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
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
	protected String recargarEditar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";

		try {
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
			// OBTENGO VALORES DEL REQUEST
			String idPersona =  request.getParameter("idPersona");
			// obtener idinstitucion
			String idInstitucion = request.getParameter("idInstitucion");
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = "";
			// Si el tipo es null lo ponemos a personal:
			if (request.getParameter("tipo")==null)
				tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			else
				tipo = request.getParameter("tipo");
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String[] pestanasOcultas=new String [1];
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
		
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} 
			else {
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
/*
				String NIFCIF = beanPer.getNIFCIF();
				String primera="0";
				primera = NIFCIF.substring(0,1);
				try {
					int numero = Integer.parseInt(primera);
					// si no falla tiene CIF, osea, es juridico
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				} catch (NumberFormatException ne) {
					// si no falla tiene NIF, osea, es fisico
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
				}
*/
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
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
			if (request.getParameter("TIPO")!=null && !request.getParameter("TIPO").equals("")){
				request.setAttribute("TIPO",request.getParameter("TIPO"));
			}else{
				request.setAttribute("TIPO",null);
			}

			// RGG NCOLEGIADO Sirve para modificar ncolegiado si tiene el parametro
			String editarNColegiado = request.getParameter("editarNColegiado");
			if (editarNColegiado!=null) {
				datosCliente.put("editarNColegiado","S");
			}
			
			
			request.setAttribute("datosCliente", datosCliente);		
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}

	
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
		 	BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
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

	/**
	 * Metodo que implementa el modo ver para la ficha colegial del userbean 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String verFichaColegial(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
			// OBTENGO VALORES DEL USERBEAN
			// obtener idpersona
			String idPersona = new Long(this.getUserBean(request).getIdPersona()).toString();
			// obtener idinstitucion
			String idInstitucion = this.getUserBean(request).getLocation();
			
			String modo = "ver";
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} 
			else {
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));
/*
				String NIFCIF = beanPer.getNIFCIF();
				String primera="0";
				primera = NIFCIF.substring(0,1);
				try {
					int numero = Integer.parseInt(primera);
					// si no falla tiene CIF, osea, es juridico
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				} catch (NumberFormatException ne) {
					// si no falla tiene NIF, osea, es fisico
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
				}
*/
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
			
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
	
			// con esto aseguramos que no hay boton volver
			//request.getSession().removeAttribute("CenBusquedaClientesTipo"); 
			
			// AHORA si queremos que vuelva. Que vuelva a la "nada"
			request.getSession().setAttribute("CenBusquedaClientesTipo","NADA"); 
			request.setAttribute("datosCliente", datosCliente);		
			destino="administracion";
		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}

	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		String mensaje = "";

	     try {
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
			String modo = "nuevo";
	
			String tipoAcceso = ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO;
			String[] pestanasOcultas=new String [1];
			pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
			request.setAttribute("pestanasOcultas",pestanasOcultas);
			
			//CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona","");
			datosCliente.put("idInstitucion","");
			datosCliente.put("tipoAcceso",tipoAcceso);
			request.setAttribute("datosCliente", datosCliente);		
			destino="administracion";
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
	     return destino;
	}

	/**
	 * Metodo que implementa el modo nuevaSociedad 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevaSociedad(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		String mensaje = "";

	     try {
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
	
			String modo = "nuevaSociedad";
	
			String tipoAcceso = ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO;
			String[] pestanasOcultas=new String [1];
			pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
			request.setAttribute("pestanasOcultas",pestanasOcultas);
			
			//CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona","");
			datosCliente.put("idInstitucion","");
			datosCliente.put("tipoAcceso",tipoAcceso);
			request.setAttribute("datosCliente", datosCliente);	
					
			destino="administracion";	
			
			
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
	     return destino;
	}

	
	
	
	
	
	
	
	
	
	/**
	 * Metodo que implementa el modo buscarPor para realizar la busqueda de un colegiado o no colegiado.
	 * <br> Implementa tanto la busqueda simple como avanzada.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			// casting del formulario
			BusquedaClientesForm miFormulario = (BusquedaClientesForm)formulario;
			String colegiado = miFormulario.getColegiado();
			
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
			
			CenClienteAdm cliente = new CenClienteAdm(user);
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null &&!isSeleccionarTodos){ 
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				datos = cliente.getTelefonosPaginador(datos);
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				 			
				PaginadorBind resultado = null;
				Vector datos = null;
				if (colegiado!=null && colegiado.equals(ClsConstants.DB_TRUE)) {
//					Busqueda de colegiados
					resultado = cliente.getClientesColegiados(idInstitucion,miFormulario, user.getLanguage());
				} else {
//					Busqueda de No colegiados
					if (colegiado!=null && colegiado.equals(ClsConstants.DB_FALSE)) {// Búsqueda de NO colegiados
						resultado = cliente.getClientesNoColegiados(idInstitucion,miFormulario);
					}else{
// 					Búsqueda de Letrados
						request.getSession().setAttribute("chkBusqueda",miFormulario.getChkBusqueda()); // busqueda normal
						resultado = cliente.getClientesLetrados(idInstitucion,miFormulario);	
					}
				}
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)cliente.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					datos = cliente.getTelefonosPaginador(datos);
					databackup.put("datos",datos);
					
					
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
				

				request.setAttribute("CenResultadoBusquedaClientes",resultado);
			}
			if (miFormulario.getAvanzada().equals(ClsConstants.DB_TRUE)){ 
				destino="resultadoAvanzada";
				// para saber en que tipo de busqueda estoy
				request.getSession().setAttribute("CenBusquedaClientesTipo","A"); // busqueda avanzada	
			} else {
				destino="resultado";
				// para saber en que tipo de busqueda estoy
				request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal	
			}

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}
	/*protected ArrayList getClavesLetrado(ArrayList v){
		
		Hashtable aux=new Hashtable();
		
		ArrayList claves= new ArrayList();
		for (int k=0;k<v.size();k++){
			aux=null;
			Hashtable aux2= new Hashtable();
			aux = (Hashtable) v.get(k);
			//aux=(Hashtable)row.getRow();
			String idPersona = (String)aux.get(CenPersonaBean.C_IDPERSONA);
			String idInstitucion = (String)aux.get(CenClienteBean.C_IDINSTITUCION);
			
			StringBuffer keyJsp = new StringBuffer();
			keyJsp.append(CenPersonaBean.C_IDPERSONA);
			keyJsp.append(separador);
			keyJsp.append(CenClienteBean.C_IDINSTITUCION);
			
			StringBuffer valueJsp = new StringBuffer();
			valueJsp.append(idPersona);
			valueJsp.append(separador);
			valueJsp.append(idInstitucion);
			
			
			
			
			aux2.put(CenPersonaBean.C_IDPERSONA,idPersona );
			aux2.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
			String aplicarLOPD = (String) aux
			.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
			String isAplicarLOPD =  aplicarLOPD==null||aplicarLOPD.equals("")||aplicarLOPD.equals(ClsConstants.DB_FALSE) ?ClsConstants.DB_FALSE:aplicarLOPD;
			
			
			aux2.put("APLICARLOPD",isAplicarLOPD);
			aux2.put(keyJsp.toString(), valueJsp.toString());

			aux2.put("SELECCIONADO", "1");
			claves.add(aux2);	
		}
		
		return claves;
	}*/


	/**
	 * Metodo que implementa el modo abrirAvanzada 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario - Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";

		try {
			
			// borro el formulario en session de Avanzada
			BusquedaClientesForm miformSession2 = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
	
			BusquedaClientesForm miformSession = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
	
			
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
			miform.reset(mapping,request);
			
			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
			request.setAttribute("CenInstitucionesVisibles",visibilidad);
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","A"); // busqueda avanzada
	
			destino="avanzada";
		
		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	}
		return destino;
	}
	
	/**
	 * Metodo que implementa el modo abrirAvanzadaConParametros 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirAvanzadaConParametros (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";

     try {
		// obtengo la visibilidad para el user
		String visibilidad = obtenerVisibilidadUsuario(request);
		request.setAttribute("CenInstitucionesVisibles",visibilidad);

		// para saber en que tipo de busqueda estoy
		request.getSession().setAttribute("CenBusquedaClientesTipo","A"); // busqueda avanzada

		destino="avanzada";
		
     } 	catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	 }
		
	 return destino;
	}
	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";		
		boolean correcto=false;		
		Hashtable hash = new Hashtable();		
		Vector camposOcultos = new Vector();
		UserTransaction tx = null;
		
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenClienteAdm admin=new CenClienteAdm(this.getUserBean(request));
 			
			// Obtengo los datos del formulario		
			BusquedaClientesForm miForm = (BusquedaClientesForm)formulario;		
			camposOcultos = miForm.getDatosTablaOcultos(0);
			
			// Comienzo control de transacciones 
			tx = usr.getTransaction();			
			tx.begin();
			
			// Realizo el borrado fisico
			try {
				correcto = admin.eliminarNoColegiado((String)camposOcultos.elementAt(0),(String)camposOcultos.elementAt(1));
			} catch (ClsExceptions ce) {
				throw new SIGAException("messages.deletedCliente.error");
			}
			
			if (!correcto){
				throw new SIGAException("messages.deletedCliente.error");
			}

			tx.commit();
		    result = this.exitoRefresco("messages.deleted.success",request);
			

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
		}

		return result;
		
		/*
		} 
		catch(ClsExceptions ex){
//prepare
			request.setAttribute("descOperation","messages.deleted.error"+"messages.deletedCliente.error");				
			throwExcp("messages.general.error",new String[] {"modulo.censo"},ex,null);
		} 
		catch(Exception ex){
//			prepare
			request.setAttribute("descOperation","messages.deleted.error"+"messages.deletedCliente.error");				
			throwExcp("messages.general.error",new String[] {"modulo.censo"},ex,null);
		}
		*/
        
	}

	/**
	 * Metodo que implementa el modo abrir busqueda en una ventana modal
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirBusquedaModal (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {
			
			// borro el formulario en session de Avanzada
			BusquedaClientesForm miformSession = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
			BusquedaClientesForm miformSession2 = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
			miform.reset(mapping,request);
			
			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
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
		return "inicio";
	}

	/**
	 * Metodo que implementa el modo enviarCliente
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarCliente (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
//			Vector vOcultos = miform.getDatosTablaOcultos(0);
			
			
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
			String nombre = miform.getNombrePersona();
			// obtener apellido1
			//String apellido1 = (String)vOcultos.get(5);
			String apellido1 = miform.getApellido1();
			// obtener apellido2
			//String apellido2 = (String)vOcultos.get(6);
			String apellido2 = miform.getApellido2();
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosCliente = new Hashtable();
			
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("nColegiado",nColegiado);
			datosCliente.put("nifCif",nifCif);
			datosCliente.put("nombre",nombre);
			datosCliente.put("apellido1",apellido1);
			datosCliente.put("apellido2",apellido2);
			
			request.setAttribute("datosClienteModal", datosCliente);		

			try {
				EnvEnviosAdm adm = new EnvEnviosAdm (this.getUserBean(request));
				Vector v = adm.getDirecciones(idInstitucion, idPersona, "-1");
				if (v != null && v.size() == 1)
					request.setAttribute("unicaDireccion", (Hashtable)v.get(0));
			}
			catch (Exception e) {}
			
			destino="seleccion";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 return destino;
	}

	/**
	 * Metodo que implementa el modo buscarModal 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException {
		
		String destino = "";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
	
			// casting del formulario
			BusquedaClientesForm miFormulario = (BusquedaClientesForm)formulario;
			
			// busqueda de clientes
			CenClienteAdm cliente = new CenClienteAdm(this.getUserBean(request));
			
			if (request.getParameter("busquedaSancion")!=null && request.getParameter("busquedaSancion").equals("1")){
				request.setAttribute("busquedaSancion","1");
				
			}
			
			
			String idPaginador = getIdPaginador(super.paginadorModal,getClass().getName());
			request.setAttribute(ClsConstants.PARAM_PAGINACION,idPaginador);
			HashMap databackup=getPaginador(request, idPaginador);
			if (databackup!=null){//Si estamos paginando 
				 		
					     PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
					     Vector datos=new Vector();
					
					
					//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
					String pagina = (String)request.getParameter("pagina");
					
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					if (paginador!=null){
						databackup.put("paginador",paginador);
						databackup.put("datos",datos);
					}

					
			  }else{//si es la primera vez que realizamos la busqueda	
					
			  	    databackup=new HashMap();
					
					//obtengo datos de la consulta 			
				PaginadorBind resultado = null;
				Vector datos = null;
//				 busqueda de colegiados y no colegiados
				//String deudor=(String)request.getParameter("deudor");
				if (request.getParameter("clientes")!=null && request.getParameter("clientes").equals("1")){
					
					  resultado = cliente.getClientesInstitucion(idInstitucion,miFormulario, user.getLanguage());

					
				}else{
					resultado = cliente.getColegiados(idInstitucion,miFormulario, user.getLanguage());
				}
					
					
					if (resultado!=null){
					   databackup.put("paginador",resultado);
					   datos = resultado.obtenerPagina(1);
					   databackup.put("datos",datos);
					   setPaginador(request, idPaginador, databackup);
					}   
					
					   
				
			  }
						
			destino="resultado";

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		 	
	   	 }
		 return destino;
	}
	
	
	/**
	 * Metodo que implementa el modo abrir busqueda de personas en una ventana modal
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirBuscarPersonaModal (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		return "buscarPersona";
	}
	
	/**
	 * Metodo que implementa el modo buscarPersona 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPersonasModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			// casting del formulario
			BusquedaClientesForm form = (BusquedaClientesForm)formulario;
			CenPersonaAdm cliente = new CenPersonaAdm(this.getUserBean(request));



			String idPaginador = getIdPaginador(super.paginador,getClass().getName());
			request.setAttribute(ClsConstants.PARAM_PAGINACION,idPaginador);
			HashMap databackup=getPaginador(request, idPaginador);
			if (databackup!=null){//Si estamos paginando 

				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();


				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");




				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}




				databackup.put("paginador",paginador);
				databackup.put("datos",datos);





			}else{//si es la primera vez que realizamos la busqueda	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;
				int institucion = Integer.parseInt(idInstitucion);
				if (institucion == 2000){ // General
					// Busqueda de colegiados y no colegiados del censo
					// Hay que cambiarlo mas adelante para que busque solo los colegiados del colegio 
					resultado = cliente.getPersonas(idInstitucion,form);
				}else if (institucion > 3000){  // Consejo
					resultado = cliente.getClientesConsejo(idInstitucion, form);
				}else{ // Colegio
					// Busqueda de colegiados y no colegiados del censo
					resultado = cliente.getPersonas(idInstitucion,form);
				}

				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					setPaginador(request, idPaginador, databackup);
				}   


			}

			destino="resultadoPersonas";
		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}
	
	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarPersona (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion = (String)vOcultos.get(1);
			/*// obtener nifCif
			String nifCif = (String)vOcultos.get(2);
			// obtener nombre
			String nombre = (String)vOcultos.get(3);
			// obtener apellido1
			String apellido1 = (String)vOcultos.get(4);
			// obtener apellido2
			String apellido2 = (String)vOcultos.get(5);*/
			

			// obtener nifCif
			//String nifCif = (String)vOcultos.get(3);
			String nifCif = miform.getNif();
			// obtener nombre
			//String nombre = (String)vOcultos.get(4);
			String nombre = miform.getNombrePersona();
			// obtener apellido1
			//String apellido1 = (String)vOcultos.get(5);
			String apellido1 = miform.getApellido1();
			// obtener apellido2
			//String apellido2 = (String)vOcultos.get(6);
			String apellido2 = miform.getApellido2();
			
			
			
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosPersona = new Hashtable();
			
			datosPersona.put("idPersona",idPersona);
			datosPersona.put("idInstitucion",idInstitucion);
			datosPersona.put("nifCif",nifCif);
			datosPersona.put("nombre",nombre);
			datosPersona.put("apellido1",apellido1);
			datosPersona.put("apellido2",apellido2);
			
			request.setAttribute("datosPersona", datosPersona);		

			destino="seleccionPersona";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 return destino;
	}
	
	

	
	
	/**
	 * Metodo que implementa el modo abrir busqueda en una ventana modal
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirBusquedaProcuradorModal (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			
			// borro el formulario en session de Avanzada
			BusquedaClientesForm miformSession = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
			BusquedaClientesForm miformSession2 = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
			BusquedaClientesForm miform = (BusquedaClientesForm)formulario;
			miform.reset(mapping,request);
			
			// obtengo la visibilidad para el user
//			String visibilidad = obtenerVisibilidadUsuario(request);
//			request.setAttribute("CenInstitucionesVisibles",visibilidad);
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }

		return "inicioProcurador";
	}

	
	
	/**
	 * Metodo que implementa el modo buscarProcuradorModal 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarProcuradorModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			String idPaginador = getIdPaginador(super.paginadorModal,getClass().getName());
			request.setAttribute(ClsConstants.PARAM_PAGINACION,idPaginador);

			HashMap databackup=getPaginador(request, idPaginador);
			if (databackup!=null){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();

				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");

				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}

				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
			}
			else {//si es la primera vez que realizamos la busqueda	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;

				BusquedaClientesForm miForm = (BusquedaClientesForm)formulario;

				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
				resultado = procuradorAdm.getProcuradoresModal("" + this.getIDInstitucion(request), miForm.getNombrePersona(), miForm.getApellido1(), miForm.getApellido2(), miForm.getNumeroColegiado(), miForm.getCodigo(), UtilidadesString.stringToBoolean(miForm.getChkBusqueda()));

				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					setPaginador(request, idPaginador, databackup);
				}

			}
			destino = "resultadoProcurador";
		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}

	/**
	 * Metodo que implementa la busqueda para el tag BusquedaPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  Vector  Resultados del Select
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	
	public String tagBusquedaPersona ( ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String select="";
		String tipo="";
		String nif="";
//		String ncolegiado="";
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

		BusquedaClientesForm miForm = (BusquedaClientesForm) formulario;
		CenClienteAdm clienteAdm = new CenClienteAdm(user);
		Vector resultado=new Vector();
		//if (request.getAttribute("tipo")!=null){
        	tipo=miForm.getTipoBus();
        //}
        //if (request.getAttribute("nif")!=null){
        	nif=miForm.getNumeroNif();
        //}
		
		if(tipo.equals("colegiado")){
			select="Select cl.idpersona, p.nombre, p.apellidos1, p.apellidos2, "+
					" decode(f_siga_gettipocliente(cl.idpersona,cl.idinstitucion,sysdate),20,1,0) as ejerciente,"+
					" c.situacionresidente as residente,"+
					" f_siga_calculoncolegiado(cl.idinstitucion,cl.idpersona) as ncolegiado"+
					" from cen_colegiado c, cen_cliente cl, cen_persona p"+
					" where cl.idpersona = p.idpersona"+
					" and cl.idpersona = c.idpersona"+
					" and cl.idinstitucion = c.idinstitucion"+
					" and c.idinstitucion="+user.getLocation()+ 
					" and f_siga_calculoncolegiado(cl.idinstitucion,cl.idpersona) ='"+nif+"'"+ 
					" and rownum<2"+
					" order by residente desc, ejerciente desc";
		}else if(tipo.equals("personas")){
			select="Select cl.idpersona, p.nombre, p.apellidos1, p.apellidos2, " +
					" decode(f_siga_gettipocliente(cl.idpersona,cl.idinstitucion,sysdate),20,1,0) as ejerciente," +
					" c.situacionresidente as residente," +
					" f_siga_calculoncolegiado(cl.idinstitucion,cl.idpersona) as ncolegiado" +
					" from cen_colegiado c, cen_cliente cl, cen_persona p" +
					" where cl.idpersona = p.idpersona" +
					" and cl.idpersona = c.idpersona" +
					//" and cl.idinstitucion = c.idinstitucion" +
					" and cl.idinstitucion="+user.getLocation()+
					" and p.nifcif='"+nif+"'"+
					" and rownum<2" +
					" order by residente desc, ejerciente desc";			
		}
		try {
			resultado = clienteAdm.selectGenerico(select);
			request.setAttribute("RESULTADO",resultado);

		} catch (Exception  e) {
			throwExcp("messages.general.error",new String[] {"tagBusquedaPersonas"},e,null);
		}

		return "buscarPersonas";
	}	
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
        Vector datos = new Vector();
		
		
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
	        BusquedaClientesForm form = (BusquedaClientesForm)formulario;
	        CenColegiadoAdm colegiadoAdm = null;
			CenNoColegiadoAdm noColegiadoAdm = null;
			Vector datosTabla = null;
			String idTipoPersona = null;
	        for (int i = 0; i < form.getDatosTabla().size(); i++) {
				Vector vCampos = form.getDatosTablaOcultos(i);
				String idPersona = (String) vCampos.get(0);
				String idInstitucionPersona = (String) vCampos.get(1);
				idTipoPersona = (String) vCampos.get(2);
				
				
				switch (Integer.parseInt(idTipoPersona)) {
				case 0:
					if(noColegiadoAdm == null)
						noColegiadoAdm = new CenNoColegiadoAdm(user);
					datosTabla = noColegiadoAdm.getInformeNoColegiado(idInstitucionPersona,idPersona,user.getLanguageInstitucion(),false);
					
					break;
				case 1:
					if(colegiadoAdm == null)
						colegiadoAdm = new CenColegiadoAdm(user);
					datosTabla = colegiadoAdm.getInformeColegiado(idInstitucionPersona,idPersona,user.getLanguageInstitucion(),false);	
					break;
				case 2:
					
					if(colegiadoAdm == null)
						colegiadoAdm = new CenColegiadoAdm(user);
					datosTabla = colegiadoAdm.getInformeLetrado(idInstitucionPersona,idPersona,user.getLanguageInstitucion(),false);	
					break;
				
				}
				
				if(datosTabla!=null)
					datos.addAll(datosTabla);
				else
					System.out.println("Bingo");
				
				
				
				
			}
	        String[] cabeceras = null;
	        String[] campos = null;
	        switch (Integer.parseInt(idTipoPersona)) {
			case 0:
				cabeceras = new String[]{"NOMBRE", "APELLIDOS1", "APELLIDOS2",
					       "NIFCIF",  "IDTIPOIDENTIFICACION",  "FECHANACIMIENTO",
					       "IDESTADOCIVIL", "NATURALDE", "FALLECIDO", "SEXO",
					       "DESC_ESTADOCIVIL", "DESC_TIPOIDENTIFICACION", "FECHAALTA",
					      "CARACTER", "PUBLICIDAD", "GUIAJUDICIAL", "ABONOSBANCO",
					      "CARGOSBANCO", "COMISIONES", "IDTRATAMIENTO", "IDLENGUAJE",
					      "FOTOGRAFIA", "ASIENTOCONTABLE", "FECHACARGA", "LETRADO",
					       "FECHAACTUALIZACION", "FECHAEXPORTCENSO", "NOENVIARREVISTA",
					      "NOAPARECERREDABOGACIA", "DESC_TRATAMIENTO", "DESC_LENGUAJE","SERIE","NUMEROREF","SOCIEDADSJ",
					      "TIPO","ANOTACIONES","PREFIJO_NUMREG",
					      "CONTADOR_NUMREG","SUFIJO_NUMREG","FECHAFIN",
					      "IDPERSONANOTARIO","RESENA","OBJETOSOCIAL",
					      "SOCIEDADPROFESIONAL","PREFIJO_NUMSSPP","CONTADOR_NUMSSPP",
					      "SUFIJO_NUMSSPP","NOPOLIZA","COMPANIASEG",
					      "IDINSTITUCION_NOTARIO","FECHAALTA_NOTARIO","CARACTER_NOTARIO",
					      "PUBLICIDAD_NOTARIO","GUIAJUDICIAL_NOTARIO","ABONOSBANCO_NOTARIO",
					      "CARGOSBANCO_NOTARIO","COMISIONES_NOTARIO"," IDTRATAMIENTO_NOTARIO",
					      "IDLENGUAJE_NOTARIO","FOTOGRAFIA_NOTARIO","ASIENTOCONTABLE_NOTARIO", 
					      "FECHACARGA_NOTARIO","LETRADO_NOTARIO","FECHAACTUALIZACION_NOTARIO",
					      "FECHAEXPORTCENSO_NOTARIO","NOENVIARREVISTA_NOTARIO","NOAPARECERREDABOGACIA",
					      "NOMBRE_NOTARIO","APELLIDOS1_NOTARIO","APELLIDOS2_NOTARIO",
					      "NIFCIF_NOTARIO","IDTIPOIDENTIFICACION_NOTARIO","FECHANACIMIENTO_NOTARIO", 
					      "IDESTADOCIVIL_NOTARIO","NATURALDE_NOTARIO","FALLECIDO_NOTARIO","SEXO_NOTARIO",
					      
					       "DOMICILIO","CODIGOPOSTAL","TELEFONO1","TELEFONO2","MOVIL","FAX1","FAX2",
					       "CORREOELECTRONICO","PAGINAWEB","POBLACIONEXTRANJERA","POBLACION","PROVINCIA","PAIS"};	
				campos = new String[]{"NOMBRE", "APELLIDOS1", "APELLIDOS2",
					       "NIFCIF",  "IDTIPOIDENTIFICACION",  "FECHANACIMIENTO",
					       "IDESTADOCIVIL", "NATURALDE", "FALLECIDO", "SEXO",
					       "DESC_ESTADOCIVIL", "DESC_TIPOIDENTIFICACION", "FECHAALTA",
					      "CARACTER", "PUBLICIDAD", "GUIAJUDICIAL", "ABONOSBANCO",
					      "CARGOSBANCO", "COMISIONES", "IDTRATAMIENTO", "IDLENGUAJE",
					      "FOTOGRAFIA", "ASIENTOCONTABLE", "FECHACARGA", "LETRADO",
					       "FECHAACTUALIZACION", "FECHAEXPORTCENSO", "NOENVIARREVISTA",
					      "NOAPARECERREDABOGACIA", "DESC_TRATAMIENTO", "DESC_LENGUAJE","SERIE","NUMEROREF","SOCIEDADSJ",
					      "TIPO","ANOTACIONES","PREFIJO_NUMREG",
					      "CONTADOR_NUMREG","SUFIJO_NUMREG","FECHAFIN",
					      "IDPERSONANOTARIO","RESENA","OBJETOSOCIAL",
					      "SOCIEDADPROFESIONAL","PREFIJO_NUMSSPP","CONTADOR_NUMSSPP",
					      "SUFIJO_NUMSSPP","NOPOLIZA","COMPANIASEG",
					      "IDINSTITUCION_NOTARIO","FECHAALTA_NOTARIO","CARACTER_NOTARIO",
					      "PUBLICIDAD_NOTARIO","GUIAJUDICIAL_NOTARIO","ABONOSBANCO_NOTARIO",
					      "CARGOSBANCO_NOTARIO","COMISIONES_NOTARIO"," IDTRATAMIENTO_NOTARIO",
					      "IDLENGUAJE_NOTARIO","FOTOGRAFIA_NOTARIO","ASIENTOCONTABLE_NOTARIO", 
					      "FECHACARGA_NOTARIO","LETRADO_NOTARIO","FECHAACTUALIZACION_NOTARIO",
					      "FECHAEXPORTCENSO_NOTARIO","NOENVIARREVISTA_NOTARIO","NOAPARECERREDABOGACIA",
					      "NOMBRE_NOTARIO","APELLIDOS1_NOTARIO","APELLIDOS2_NOTARIO",
					      "NIFCIF_NOTARIO","IDTIPOIDENTIFICACION_NOTARIO","FECHANACIMIENTO_NOTARIO", 
					      "IDESTADOCIVIL_NOTARIO","NATURALDE_NOTARIO","FALLECIDO_NOTARIO","SEXO_NOTARIO",
					      
					       "DOMICILIO","CODIGOPOSTAL","TELEFONO1","TELEFONO2","MOVIL","FAX1","FAX2",
					       "CORREOELECTRONICO","PAGINAWEB","POBLACIONEXTRANJERA","POBLACION","PROVINCIA","PAIS"};	
				break;
			case 1:case 2:
				cabeceras = new String[]{"NOMBRE", "APELLIDOS1", "APELLIDOS2",
					       "NIFCIF",  "IDTIPOIDENTIFICACION",  "FECHANACIMIENTO",
					       "IDESTADOCIVIL", "NATURALDE", "FALLECIDO", "SEXO",
					       "DESC_ESTADOCIVIL", "DESC_TIPOIDENTIFICACION", "FECHAALTA",
					      "CARACTER", "PUBLICIDAD", "GUIAJUDICIAL", "ABONOSBANCO",
					      "CARGOSBANCO", "COMISIONES", "IDTRATAMIENTO", "IDLENGUAJE",
					      "FOTOGRAFIA", "ASIENTOCONTABLE", "FECHACARGA", "LETRADO",
					       "FECHAACTUALIZACION", "FECHAEXPORTCENSO", "NOENVIARREVISTA",
					      "NOAPARECERREDABOGACIA", "DESC_TRATAMIENTO", "DESC_LENGUAJE",
					       "FECHAPRESENTACION", "FECHAINCORPORACION", "INDTITULACION",
					       "JUBILACIONCUOTA", "SITUACIONEJERCICIO", "SITUACIONRESIDENTE",
					       "SITUACIONEMPRESA", "COMUNITARIO", "NCOLEGIADO", "FECHAJURA",
					       "NCOMUNITARIO", "FECHATITULACION", "OTROSCOLEGIOS", "FECHADEONTOLOGIA",
					       "FECHAMOVIMIENTO", "IDTIPOSSEGURO", "CUENTACONTABLESJCS", "DESC_TIPOSEGURO","ESTADO_COLEGIAL","FECHA_ESTADO_COLEGIAL",
					       "DOMICILIO","CODIGOPOSTAL","TELEFONO1","TELEFONO2","MOVIL","FAX1","FAX2",
					       "CORREOELECTRONICO","PAGINAWEB","POBLACIONEXTRANJERA","POBLACION","PROVINCIA","PAIS"};	
				campos = new String[]{"NOMBRE", "APELLIDOS1", "APELLIDOS2",
					       "NIFCIF",  "IDTIPOIDENTIFICACION",  "FECHANACIMIENTO",
					       "IDESTADOCIVIL", "NATURALDE", "FALLECIDO", "SEXO",
					       "DESC_ESTADOCIVIL", "DESC_TIPOIDENTIFICACION", "FECHAALTA",
					      "CARACTER", "PUBLICIDAD", "GUIAJUDICIAL", "ABONOSBANCO",
					      "CARGOSBANCO", "COMISIONES", "IDTRATAMIENTO", "IDLENGUAJE",
					      "FOTOGRAFIA", "ASIENTOCONTABLE", "FECHACARGA", "LETRADO",
					       "FECHAACTUALIZACION", "FECHAEXPORTCENSO", "NOENVIARREVISTA",
					      "NOAPARECERREDABOGACIA", "DESC_TRATAMIENTO", "DESC_LENGUAJE",
					       "FECHAPRESENTACION", "FECHAINCORPORACION", "INDTITULACION",
					       "JUBILACIONCUOTA", "SITUACIONEJERCICIO", "SITUACIONRESIDENTE",
					       "SITUACIONEMPRESA", "COMUNITARIO", "NCOLEGIADO", "FECHAJURA",
					       "NCOMUNITARIO", "FECHATITULACION", "OTROSCOLEGIOS", "FECHADEONTOLOGIA",
					       "FECHAMOVIMIENTO", "IDTIPOSSEGURO", "CUENTACONTABLESJCS", "DESC_TIPOSEGURO","ESTADO_COLEGIAL","FECHA_ESTADO_COLEGIAL",
					       "DOMICILIO","CODIGOPOSTAL","TELEFONO1","TELEFONO2","MOVIL","FAX1","FAX2","CORREOELECTRONICO",
					       "PAGINAWEB","POBLACIONEXTRANJERA","POBLACION","PROVINCIA","PAIS"};
				break;
			
			
			}
			
				//,"COMUNICACIONES"};
			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", idInstitucion+"_"+this.getUserName(request).toString());
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "generaExcel";
	}
	



}
