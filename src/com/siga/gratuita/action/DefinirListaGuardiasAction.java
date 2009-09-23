package com.siga.gratuita.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import com.atos.utils.*;

import org.apache.struts.action.*;

import java.io.File;
import java.util.*;

import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.certificados.Plantilla;

/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_LISTAGUARDIAS Y SCS_INCLUSIONGUARDIASENLISTAS
 * Se centra en el mantenimiento de las listas de guardias
 * Terminado el 12-1-2005 con los ultimos cambios.
 * 
 * @author david.sanchezp
 * @since 27/12/2004 
 * @version 2.0
 */
public class DefinirListaGuardiasAction extends MasterAction {

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
				}else if (accion.equalsIgnoreCase("generarInforme")){
					mapDestino = generarInforme(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("finalizarInforme")){
					mapDestino = finalizarInforme(mapping, miForm, request, response);
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/**
	 * Es el metodo inicial que se ejecuta al entrar a la pantalla de busqueda.
	 * Limpia la sesion de los datos del formulario.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			//Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("DATABACKUP");
			//Borramos el String accion y el IDLISTA:
			request.getSession().removeAttribute("accion");
			request.getSession().removeAttribute("IDLISTA");
		} catch (Exception e){
			throwExcp("messages.select.error",e,null);
		}		
		return "inicio";
	}
	
	/**
	 * Hace una busqueda de las guardias de una lista en la pantalla de mantenimiento.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		ScsListaGuardiasAdm admListaGuardias = new ScsListaGuardiasAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		
		String forward = "error";
		UsrBean usr;
		Vector salida = new Vector();
		String tipoDiaParseado = "";
		Hashtable backupHash = new Hashtable();
		String idlista="", idinstitucion="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//Recupero el idlista del formulario si estoy en editar en la pantalla de mantenimiento y vengo de la pantalla de buscar.
			//Sino, estoy en editar viniendo de nuevo->insertar una nueva lista de guardias.
			//En este caso recupero el idlista de la sesion. Lo almacen� en sesion al insertar.
			if (miForm.getIdLista()!=null && !miForm.getIdLista().equals(""))
				idlista = miForm.getIdLista(); 			
			else {
				backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				idlista =  (String)backupHash.get("IDLISTA");
			}
			
			idinstitucion = (String)usr.getLocation();
			
			//SELECT			
			salida = admListaGuardias.selectGenerico(admListaGuardias.buscarGuardiasEnListaGuardias(idinstitucion,idlista));						
			//Introduzco en el vector losTipoDias pero parseandolos primero.
			for (int i=0; i < salida.size();i++) {
				
				tipoDiaParseado = ScsGuardiasTurnoAdm.obtenerTipoDia((String)((Hashtable)salida.get(i)).get("SELECCIONLABORABLES"),(String)((Hashtable)salida.get(i)).get("SELECCIONFESTIVOS"),usr);
				((Hashtable)salida.get(i)).put("TIPODIASPARSEADO",tipoDiaParseado);
			}			
			request.setAttribute("resultado",salida);

			request.setAttribute("accion",miForm.getAccion());
			request.setAttribute("modo",miForm.getModo());				
			forward = "listar3LG";
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}												
		return forward;		
	}

	/**
	 * Vamos a la pantalla de mantenimiento desde nuevo o editar/ver.
	 * En esta pantalla se consulta en Base de Datos las guardias de una lista de guardias seleccionada o se <br>
	 * crea/modifica una nueva guardia..
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		ScsListaGuardiasAdm admListaGuardias = new ScsListaGuardiasAdm(this.getUserBean(request));
		
		Vector ocultos = new Vector();
		Vector visibles = new Vector();
		String idlista="", idinstitucion="", nombre="", lugar="", observaciones="";
		Hashtable backupHash = new Hashtable();
		
		try {
			//NUEVO desde la pantalla inicial de Busqueda.
			if ((miForm.getAccion() != null)&&(miForm.getAccion().equals("nuevo"))) {
					request.setAttribute("accion","nuevo");
					request.setAttribute("modo",miForm.getModo());					
			}
	        //EDITAR desde la pantalla inicial de Busqueda.
			else {
					//Parametros ocultos y visibles de la pantalla anterior.
					ocultos = miForm.getDatosTablaOcultos(0);
					visibles = miForm.getDatosTablaVisibles(0);
					idinstitucion = (String)ocultos.get(0);
					idlista = (String)ocultos.get(1);
					nombre = (String)visibles.get(0);
					lugar = (String)visibles.get(1);	
					observaciones = (String)ocultos.get(2);				
					request.setAttribute("IDLISTA",idlista);
					request.setAttribute("IDINSTITUCION",idinstitucion);
					request.setAttribute("NOMBRE",nombre);
					request.setAttribute("LUGAR",lugar);
					request.setAttribute("OBSERVACIONES",observaciones);
					
					//Consultamos la fecha y usuario de registro a partir de sus id			
					Vector fechaYUsu = admListaGuardias.selectGenerico(admListaGuardias.getFechaYUsu(idinstitucion,idlista));
					//Almacenamos en sesion el registro de la lista de guardias			
					backupHash.put("NOMBRE",(String)((Hashtable)fechaYUsu.elementAt(0)).get("NOMBRE"));
					backupHash.put("LUGAR",(String)((Hashtable)fechaYUsu.elementAt(0)).get("LUGAR"));
					backupHash.put("OBSERVACIONES",(String)((Hashtable)fechaYUsu.elementAt(0)).get("OBSERVACIONES"));
					backupHash.put("USUMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("USUMODIFICACION"));
					backupHash.put("FECHAMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("FECHAMODIFICACION"));
					backupHash.put("IDINSTITUCION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDINSTITUCION"));
					backupHash.put("IDLISTA",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDLISTA"));
					
					request.getSession().removeAttribute("DATABACKUP");
					request.getSession().setAttribute("DATABACKUP",backupHash);					
					request.setAttribute("accion",miForm.getAccion());
					request.setAttribute("modo",miForm.getModo());									
			}
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);			
		}							
		return "consultarLG";
	}

	/**
	 * Ejecuta el metodo editar().
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return this.editar(mapping,formulario,request,response);	
	}

	/**
	 * Navegaciona a la ventana modal.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		
		try {
			request.setAttribute("modo",miForm.getModo());
			request.setAttribute("IDLISTA",miForm.getIdLista());
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return "modalLG";
	}

	/**
	 * Anhade a una tabla hash los datos necesarios para hacer una insercion en base de datos de una nueva <br> 
	 * Lista de Guardias si estoy en mantenimiento o inserto una nueva guardia con n�mero de orden en una lista <br>
	 * de guardias. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		ScsListaGuardiasAdm admListaGuardiasBean = new ScsListaGuardiasAdm(this.getUserBean(request));
		ScsInclusionGuardiasEnListasAdm admInclusionGuardiasEnListas = new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
		
		String forward = "error";
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		UserTransaction tx = null;
		UsrBean usr;
		
		try {			
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();
			
			//INSERT: en la pantalla Modal, actualiza los datos de una Lista de Guardias con el orden dado		
			if (miForm.getAccion()!= null && miForm.getAccion().equals("modal")) {
				
				
					
				
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,usr.getLocation());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDTURNO,miForm.getIdTurno());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDGUARDIA,miForm.getIdGuardia());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,miForm.getIdLista());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_ORDEN,miForm.getOrden());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_USUMODIFICACION,usr.getUserName());
					miHash.put(ScsInclusionGuardiasEnListasBean.C_FECHAMODIFICACION,"sysdate");
					
					//Compruebo que el orden no exista
					Vector registrosTotal = admInclusionGuardiasEnListas.selectGenerico(admInclusionGuardiasEnListas.comprobarPosicionGuardias(miHash));
					String total = (String)(((Hashtable)registrosTotal.get(0)).get("TOTAL"));
					
					if (total.equals("0")) {				
						//INSERT (INICIO TRANSACCION)
						tx=usr.getTransaction();
			            tx.begin();                 
						if (admInclusionGuardiasEnListas.insert(miHash)) { 
							request.setAttribute("mensaje","messages.inserted.success");
						} else {
							request.setAttribute("mensaje","messages.inserted.error");
						}
			            tx.commit();
			            request.setAttribute("modal", "1");    
					} else { 					
						request.setAttribute("mensaje","gratuita.modalLG.literal.aviso4");	
						request.setAttribute("sinrefresco", "sinrefresco");	
					}
				
				    request.setAttribute("modo",miForm.getModo());						
					request.setAttribute("accion",miForm.getAccion());
				       
					forward = "exito";
			//INSERT: Al pulsar en Nuevo en la pantalla de Busqueda y Guardar en la de Mantenimiento.
			} else {
					miHash.put("NOMBRE",miForm.getListaGuardias());
					miHash.put("LUGAR",miForm.getLugar());
					miHash.put("OBSERVACIONES",miForm.getObservaciones());
					miHash.put("USUMODIFICACION",usr.getUserName());
					miHash.put("FECHAMODIFICACION","sysdate");
					miHash.put("IDINSTITUCION",usr.getLocation());
		
		            //SELECT
					//Obtengo el primer y unico registro de la consulta
					Vector registros = admListaGuardiasBean.selectGenerico(admListaGuardiasBean.getIdLista(usr.getLocation()));
					String idlista = (String)(((Hashtable)registros.get(0)).get("IDLISTA"));
					//Si no hay elementos le asigno el primero: 1
					if (idlista.equals("")) idlista = "1";
					miHash.put("IDLISTA",idlista);
					//INSERT (INICIO TRANSACCION)
		            tx.begin();
		            if (admListaGuardiasBean.insert(miHash)) {
						request.setAttribute("modo","editar");
						request.setAttribute("accion","editar");
						request.getSession().setAttribute("IDLISTA",idlista);
		            	
				        //Consultamos la fecha y usuario de registro a partir de sus id			
						Vector fechaYUsu = admListaGuardiasBean.selectGenerico(admListaGuardiasBean.getFechaYUsu(usr.getLocation(),idlista));
		
						//Almacenamos en sesion el registro de la lista de guardias			
						backupHash.put("NOMBRE",(String)((Hashtable)fechaYUsu.elementAt(0)).get("NOMBRE"));
						backupHash.put("LUGAR",(String)((Hashtable)fechaYUsu.elementAt(0)).get("LUGAR"));
						backupHash.put("OBSERVACIONES",(String)((Hashtable)fechaYUsu.elementAt(0)).get("OBSERVACIONES"));
						backupHash.put("USUMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("USUMODIFICACION"));
						backupHash.put("FECHAMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("FECHAMODIFICACION"));
						backupHash.put("IDINSTITUCION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDINSTITUCION"));
						backupHash.put("IDLISTA",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDLISTA"));							
						request.getSession().setAttribute("DATABACKUP",backupHash);
						
						forward = exitoRefresco("messages.inserted.success",request);
		            } else {
						request.setAttribute("modo","editar");
						request.setAttribute("accion","nuevo");		            	
						forward = exito("messages.inserted.error",request);
		            }
		            tx.commit();            
			}
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;
	}

	/**
	 * Modifica los datos de una lista de guardias.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;					
		ScsListaGuardiasAdm admListaGuardiasBean = new ScsListaGuardiasAdm(this.getUserBean(request));
		
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		UserTransaction tx = null;
		UsrBean usr;		
		String forward = "error";
		String idlista = "";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				
			//UPDATE: Mantenimiento: Actualiza los datos de una Lista de Guardias al pulsar en Guardar		
			backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			//Recupero el idlista del formulario si estoy en editar en la pantalla de mantenimiento y vengo de la pantalla de buscar.
			//Sino, estoy en editar viniendo de nuevo->insertar una nueva lista de guardias.
			//En este caso recupero el idlista de la sesion. Lo almacen� en sesion al insertar
			if (miForm.getIdLista()!=null && !miForm.getIdLista().equals(""))
				idlista = miForm.getIdLista();			
			else 
				idlista = (String)backupHash.get("IDLISTA");
			miHash.put("IDLISTA",idlista);
			miHash.put("NOMBRE",miForm.getListaGuardias());
			miHash.put("LUGAR",miForm.getLugar());
			miHash.put("OBSERVACIONES",miForm.getObservaciones());
			miHash.put("USUMODIFICACION",usr.getUserName());
			miHash.put("FECHAMODIFICACION","sysdate");
			miHash.put("IDINSTITUCION",usr.getLocation());

			//UPDATE (INICIO TRANSACCION)
			tx=usr.getTransaction();
	        tx.begin();                 
			if (admListaGuardiasBean.update(miHash,backupHash)) 
				request.setAttribute("mensaje","messages.updated.success");
			else
				request.setAttribute("mensaje","messages.updated.error");					
	        tx.commit();

	        //Consultamos la fecha y usuario de registro a partir de sus id			
			Vector fechaYUsu = admListaGuardiasBean.selectGenerico(admListaGuardiasBean.getFechaYUsu(usr.getLocation(),idlista));

			//Almacenamos en sesion el registro de la lista de guardias			
			backupHash.put("NOMBRE",(String)((Hashtable)fechaYUsu.elementAt(0)).get("NOMBRE"));
			backupHash.put("LUGAR",(String)((Hashtable)fechaYUsu.elementAt(0)).get("LUGAR"));
			backupHash.put("OBSERVACIONES",(String)((Hashtable)fechaYUsu.elementAt(0)).get("OBSERVACIONES"));
			backupHash.put("USUMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("USUMODIFICACION"));
			backupHash.put("FECHAMODIFICACION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("FECHAMODIFICACION"));
			backupHash.put("IDINSTITUCION",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDINSTITUCION"));
			backupHash.put("IDLISTA",(String)((Hashtable)fechaYUsu.elementAt(0)).get("IDLISTA"));							
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().setAttribute("DATABACKUP",backupHash);

			request.setAttribute("modo",miForm.getModo());
			forward = "exito";				
		    request.setAttribute("hiddenFrame", "1");
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}	
		return forward;
	}		


	/**
	 * Borra una lista de guardias en la pantalla de busqueda o una guardia de una lista en la pantalla de <br>
	 * mantenimiento.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "error";
		Hashtable miHash = new Hashtable();
		ScsListaGuardiasAdm admListaGuardias = new ScsListaGuardiasAdm(this.getUserBean(request));
		ScsInclusionGuardiasEnListasAdm admInclusionGuardiasEnListas = new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request)); 
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		Vector ocultos = new Vector();
		Vector visibles = new Vector();

		try {
			//Borrar: registro de scs_listaguardias en la pantalla de busqueda
			if ((miForm.getAccion() != null)&&(miForm.getAccion().equals("buscarPor"))) {
					ocultos = miForm.getDatosTablaOcultos(0);					
					miHash.clear();							
					miHash.put(ScsListaGuardiasBean.C_IDINSTITUCION,ocultos.get(0));
					miHash.put(ScsListaGuardiasBean.C_IDLISTA,ocultos.get(1));
					//DELETE
					if (admListaGuardias.delete(miHash))
						request.setAttribute("mensaje","messages.deleted.success");
					else
					    request.setAttribute("mensaje","error.messages.deleted");
	
					forward = "exito";				
			        request.setAttribute("hiddenFrame", "1");
			        request.setAttribute("modo",miForm.getModo());
			//Borrar: registro de scs_inclusionguardiasenlistas en la pantalla de mantenimiento
			} else {
					ocultos = miForm.getDatosTablaOcultos(0);					
					visibles = miForm.getDatosTablaVisibles(0);
					miHash.clear();							
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,ocultos.get(0));
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,ocultos.get(1));
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDGUARDIA,ocultos.get(2));
					miHash.put(ScsInclusionGuardiasEnListasBean.C_IDTURNO,ocultos.get(3));
					miHash.put(ScsInclusionGuardiasEnListasBean.C_ORDEN,visibles.get(0));
					//DELETE
					if (admInclusionGuardiasEnListas.delete(miHash))
						request.setAttribute("mensaje","messages.deleted.success");
					else
					    request.setAttribute("mensaje","error.messages.deleted");
	
					forward = "exito";
					request.setAttribute("modo",miForm.getModo());
			        request.setAttribute("hiddenFrame", "1");
			}
		} 
		catch (Exception e){
			throwExcp("messages.deleted.error",e,null);
		}					
		return forward;
	}


	/**
	 * Realiza la busqueda de las listas de guardias.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		ScsListaGuardiasAdm admListaGuardiasBean = new ScsListaGuardiasAdm(this.getUserBean(request)); 
		CenPartidoJudicialAdm admPartidoBean = new CenPartidoJudicialAdm(this.getUserBean(request));
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();
		Hashtable hashFormulario = new Hashtable();
		UsrBean usr;
		String idpartido = "", partido="", consulta="";
		String forward = "error";
		
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			

			//Mantenimiento de la busqueda de la pantalla inicial
			if ((miForm.getAccion() != null) && (miForm.getAccion().equals("buscarPor"))) {
				//Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP
				request.getSession().removeAttribute("DATOSFORMULARIO");
				request.getSession().removeAttribute("DATABACKUP");
				
				//Consulto el idpartido
				if (miForm.getPartido()!=null && !miForm.getPartido().equals("")) {			
					partido = miForm.getPartido();
					consulta = admPartidoBean.obtenerIdPartido(partido);
					idpartido = (String)admPartidoBean.getCampoRegistro(consulta,CenPartidoJudicialBean.C_IDPARTIDO);							
				}			
				
				miHash.put("INSTITUCION",usr.getLocation());
				miHash.put("LISTAGUARDIAS",miForm.getListaGuardias());
				miHash.put("LUGAR",miForm.getLugar());
				miHash.put("ZONA",miForm.getZona());
				miHash.put("SUBZONA",miForm.getSubzona());
				//miHash.put("PARTIDO",miForm.getPartido());			
				miHash.put("IDPARTIDO",idpartido);

				//Guardo en una tabla hash los datos de busqueda.
				hashFormulario.put("LISTAGUARDIAS",miForm.getListaGuardias());
				hashFormulario.put("LUGAR",miForm.getLugar());
				hashFormulario.put("ZONA",miForm.getZona());
				hashFormulario.put("SUBZONA",miForm.getSubzona());
				//hashFormulario.put("PARTIDO",miForm.getPartido());
				hashFormulario.put("INICIARBUSQUEDA","SI");
				request.getSession().setAttribute("DATOSFORMULARIO",hashFormulario);
				
				v = admListaGuardiasBean.selectGenericoNLS(admListaGuardiasBean.buscarListaGuardias(miHash));
				request.setAttribute("resultado",v);
				
				request.setAttribute("modo",miForm.getModo());				
				forward = "listarLG";
			//Mantenimiento de la busqueda de la pantalla modal
			} else {
				miHash.put("FECHAMODIFICACION","sysdate");
				miHash.put("USUMODIFICACION",usr.getUserName());
				//Datos necesarios para la consulta
				miHash.put("IDLISTA",miForm.getIdLista());
				miHash.put("IDINSTITUCION",usr.getLocation());
				miHash.put("ABREVIATURA",miForm.getAbreviatura());
				miHash.put("TURNO",miForm.getTurno());
				//miHash.put("PARTIDO",miForm.getPartido());
				miHash.put("ZONA",miForm.getZona());
				miHash.put("SUBZONA",miForm.getSubzona());
				miHash.put("AREA",miForm.getArea());
				miHash.put("MATERIA",miForm.getMateria());
								
				v = admListaGuardiasBean.selectGenericoNLS(admListaGuardiasBean.buscarGuardias(miHash));
				request.setAttribute("resultado",v);
				
				request.setAttribute("modo",miForm.getModo());				
				forward = "listar2LG";
			}
		} 
		catch (Exception e){
			throwExcp("messages.select.error",e,null);
		}		
		return forward;
	}

	/**
	 * Contemplamos 2 casos:
	 * 1. Si accion es "editarNuevo" vamos a la pantalla de Mantenimiento en modo edicion. <br>
	 * 	  Este caso se da cuando venimos de insertar un nuevo registro y queremos editarlo.
	 * 2. Vamos a la pantalla inicial de busqueda sin borrar datos del formulario de la sesion pero si de los datos de backup.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
		
		UsrBean usr = null;
		String forward = "";
		try {
			//Si venimos de insertar un nuevo registro vamos a editar ese nuevo registro
			if (miForm.getAccion()!=null && miForm.getAccion().equals("editarNuevo")) {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				//Paso los parametro del registro insertado:
				request.setAttribute("IDINSTITUCION",usr.getLocation());
				//request.setAttribute("IDLISTA",miForm.getIdLista());
				request.setAttribute("IDLISTA",(String)request.getSession().getAttribute("IDLISTA"));
				request.setAttribute("NOMBRE",miForm.getListaGuardias());
				request.setAttribute("LUGAR",miForm.getLugar());
				request.setAttribute("OBSERVACIONES",miForm.getObservaciones());
				
				request.setAttribute("accion","editar");
				request.setAttribute("modo","editar");
				forward = "consultarLG";
			} else {
				//Borramos las tablas hash: DATABACKUP
				request.getSession().removeAttribute("DATABACKUP");		
				//Borramos el String accion y el IDLISTA:
				request.getSession().removeAttribute("accion");
				request.getSession().removeAttribute("IDLISTA");
				forward = "inicio";
			}
		} catch (Exception e){
			throwExcp("messages.select.error",e,null);
		}
		return forward;	
	}
	
	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String generarInforme(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  
	{
		try {

		    // Comprobamos si tiene guardias
			DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm)formulario;
			ScsInclusionGuardiasEnListasAdm admIGL = new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
			Hashtable paramBusqueda = new Hashtable();
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,miForm.getIdLista());
			Vector v = admIGL.select(paramBusqueda);
			if (v != null &&  v.size() > 0){
				// Guardo el formulario en sesi�n para poder fijar a quienes hay que enviar la carta
				request.setAttribute("DATOS", miForm.getDatos());
			}
			else {
			    return exitoModalSinRefresco("messages.listaGuardias.definirListaGuardias.generarInforme.sinGuardias.error", request);
			}

		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
        return "recogidaDatos";
	}
	
	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */

	protected String finalizarInforme(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
	    
		String resultado="exito";
		String institucion="";
		File ficFOP=null;
			
		try {
			
			DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Hashtable backupHash = new Hashtable();
			backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			// Obtengo el periodo a consultar
			String fechaInicio=miForm.getFechaInicio();
			String fechaFin=miForm.getFechaFin();
			String nombreLista =UtilidadesHash.getString(backupHash,"NOMBRE");
			String lugar =UtilidadesHash.getString(backupHash,"LUGAR");
			String observaciones =UtilidadesHash.getString(backupHash,"OBSERVACIONES");
			
			
			// Las listas de guardias a analizar
			ScsInclusionGuardiasEnListasAdm admIGL =new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
			Hashtable paramBusqueda=new Hashtable();
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,miForm.getIdLista());
			Vector listasIncluidas=admIGL.select(paramBusqueda);
							
			Enumeration listaResultados=listasIncluidas.elements();
			
			// Generacion de cartas propiamente dichas
			if (listaResultados.hasMoreElements()){

				String nombreFicheroPDF="";
				
				boolean correcto=true;
				institucion=usr.getLocation();
				
				// Gestion de nombres de ficheros
				ReadProperties rp = new ReadProperties("SIGA.properties");			
			    String rutaServidor = rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava");
	    		String barra = "";
	    		String nombreFichero = "";
	    		if (rutaServidor.indexOf("/") > -1){ 
	    			barra = "/";
	    		}
	    		if (rutaServidor.indexOf("\\") > -1){ 
	    			barra = "\\";
	    		}    		
	     		rutaServidor += barra+institucion;
				File rutaFOP=new File(rutaServidor);
				if (!rutaFOP.exists()){
					if(!rutaFOP.mkdirs()){
						throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
					}
				}     		
	     		nombreFichero=rutaServidor+barra+institucion+"_"+this.getUserName(request).toString()+".fo";     		
				ficFOP = new File(nombreFichero);
				if (ficFOP.exists()){
					if(!ficFOP.delete()){
						throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
					}
				}
				
				Plantilla plantilla = new Plantilla(this.getUserBean(request));
		    				
			    String rutaPlantilla = rp.returnProperty("sjcs.directorioFisicoListaGuardiasJava")+rp.returnProperty("sjcs.directorioListaGuardiasJava");
	    		if (rutaPlantilla.indexOf("/") > -1){
	    			barra = "/";
	    		}
	    		if (rutaPlantilla.indexOf("\\") > -1){
	    			barra = "\\";
	    		}
	    		
	    		// Insertamos cabecera del documento
//	    		 Comentado por PDM, todo esta en las plantillas
	    		//correcto=plantilla.insercionCabeceraSimpleDocumentoFOP(ficFOP,"listaGuardias","1","1","1.5","1.2");
	    		
	    		// Obtenemos nombre de la institucion
	    		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request)); 
	    		String nombreInstitucion=admInstitucion.getNombreInstitucion(institucion);
	    		
//	    		 Comentado por PDM,No hace falta poner un numero por defecto de registros
//               porque FOP pagina automaticamente cuando los datos estan en tablas	    		
	    		//int numeroLineas=40;
	    		Vector datos = new Vector();
	    		Vector guardias = new Vector();
				ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(this.getUserBean(request));
	    		
				while((correcto)&&(listaResultados.hasMoreElements())){
					ScsInclusionGuardiasEnListasBean fila=(ScsInclusionGuardiasEnListasBean)listaResultados.nextElement();
					// Recopilacion datos
					/* RGG 08/10/2007 cambio para que obtenga todas las guardias del tiron
					 * Vector datosParciales= admGT.getDatosListaGuardias(fila.getIdInstitucion().toString(),fila.getIdTurno().toString(),fila.getIdGuardia().toString(),fechaInicio,fechaFin);
					int i=0;
					while (i<datosParciales.size()){
						datos.add(datosParciales.elementAt(i));
						i++;
					}
					*/
					Vector guardia = new Vector();
					guardia.add(fila.getIdTurno().toString());
					guardia.add(fila.getIdGuardia().toString());
					
					guardias.add(guardia);
					
				}

				// RGG
				datos = admGT.getDatosListaGuardias(institucion,guardias,fechaInicio,fechaFin);  
				
				
//				 Comentado por PDM
	    		/*if (datos.size()<=numeroLineas){
	    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, datos.size(),rutaPlantilla+barra+institucion+barra,usr.getLanguage());    			
	    		}
	    		else{*/
	    			/*correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, numeroLineas,rutaPlantilla+barra+institucion+barra,usr.getLanguage());
	        		for (int cont=0; cont<numeroLineas; cont++){
	        			datos.removeElementAt(0);
	        		}
	        		while (datos.size()>numeroLineas){
	        			correcto=plantilla.insercionSimplePaginaNuevaDocumentoFOP(ficFOP);
		    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, numeroLineas,rutaPlantilla+barra+institucion+barra,usr.getLanguage());
	            		for (int cont=0; cont<numeroLineas; cont++){
	            			datos.removeElementAt(0);
	            		}
	        	    }*/
        			//correcto=plantilla.insercionSimplePaginaNuevaDocumentoFOP(ficFOP);
	    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, datos.size(),rutaPlantilla+barra+institucion+barra,usr.getLanguage(), nombreLista, lugar, observaciones);
	        		for (int cont=0; cont<datos.size(); cont++){
	        			datos.removeElementAt(0);
	        		}    			
	    		//}
	    		
				// Insertamos fin documento
	        	// Comentado por PDM, todo esta en las plantillas	
	    		//correcto=plantilla.insercionPieSimpleDocumentoFOP(ficFOP);
				
				// Obtencion fichero PDF
				if (correcto){
					nombreFicheroPDF="listaGuardias_"+UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","_").replaceAll(":","_").replaceAll(" ","_")+".pdf";
					File ficPDF=new File(rutaServidor+barra+nombreFicheroPDF); 
					plantilla.convertFO2PDF(ficFOP, ficPDF, rutaPlantilla+File.separator+institucion);
					ficFOP.delete();
				}
				else{
					ficFOP.delete();					
				}
				
			request.setAttribute("nombreFichero", nombreFicheroPDF);
			request.setAttribute("rutaFichero", rutaServidor+barra+nombreFicheroPDF);
			request.setAttribute("generacionOK","OK");
			resultado="descarga";
			
			}
			else{
			    resultado = exito("messages.listaGuardias.definirListaGuardias.generarInforme.sinGuardias.error",request);
				// Por el cambio 2417 Quitar este mensaje: throw new ClsExceptions("Se ha producido un error en la generaci�n de informes.");
			} 
			
		}
		catch (Exception e) 
		{
			ficFOP.delete();
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		} 
        return resultado;
	}	
	
//	protected String finalizarInforme(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
//	    
//		String resultado="exito";
//		String institucion="";
//		File ficFOP=null;
//			
//		try {
//			
//			DefinirListaGuardiasForm miForm = (DefinirListaGuardiasForm) formulario;
//			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
//			
//			// Obtengo el periodo a consultar
//			String fechaInicio=miForm.getFechaInicio();
//			String fechaFin=miForm.getFechaFin();
//			
//			// Las listas de guardias a analizar
//			ScsInclusionGuardiasEnListasAdm admIGL =new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
//			Hashtable paramBusqueda=new Hashtable();
//			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
//			paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,miForm.getIdLista());
//			Vector listasIncluidas=admIGL.select(paramBusqueda);
//							
//			Enumeration listaResultados=listasIncluidas.elements();
//			
//			// Generacion de cartas propiamente dichas
//			if (listaResultados.hasMoreElements()){
///*
//				String nombreFicheroPDF="";
//				
//				boolean correcto=true;
//				institucion=usr.getLocation();
//				
//				// Gestion de nombres de ficheros
//				ReadProperties rp = new ReadProperties("SIGA.properties");			
//			    String rutaServidor = rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava");
//	    		String barra = "";
//	    		String nombreFichero = "";
//	    		if (rutaServidor.indexOf("/") > -1){ 
//	    			barra = "/";
//	    		}
//	    		if (rutaServidor.indexOf("\\") > -1){ 
//	    			barra = "\\";
//	    		}    		
//	     		rutaServidor += barra+institucion;
//				File rutaFOP=new File(rutaServidor);
//				if (!rutaFOP.exists()){
//					if(!rutaFOP.mkdirs()){
//						throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
//					}
//				}     		
//	     		nombreFichero=rutaServidor+barra+institucion+"_"+this.getUserName(request).toString()+".fo";     		
//				ficFOP = new File(nombreFichero);
//				if (ficFOP.exists()){
//					if(!ficFOP.delete()){
//						throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
//					}
//				}
//				
//				Plantilla plantilla = new Plantilla();
//		    				
//			    String rutaPlantilla = rp.returnProperty("sjcs.directorioFisicoListaGuardiasJava")+rp.returnProperty("sjcs.directorioListaGuardiasJava");
//	    		if (rutaPlantilla.indexOf("/") > -1){
//	    			barra = "/";
//	    		}
//	    		if (rutaPlantilla.indexOf("\\") > -1){
//	    			barra = "\\";
//	    		}
//	    		
//	    		// Insertamos cabecera del documento
////	    		 Comentado por PDM, todo esta en las plantillas
//	    		//correcto=plantilla.insercionCabeceraSimpleDocumentoFOP(ficFOP,"listaGuardias","1","1","1.5","1.2");
//	    		
//	    		// Obtenemos nombre de la institucion
//	    		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request)); 
//	    		String nombreInstitucion=admInstitucion.getNombreInstitucion(institucion);
//	    		
////	    		 Comentado por PDM,No hace falta poner un numero por defecto de registros
////               porque FOP pagina automaticamente cuando los datos estan en tablas	    		
//	    		//int numeroLineas=40;
//	    		Vector datos = new Vector();
//	    		Vector guardias = new Vector();
//
//				ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(this.getUserBean(request));
//*/	    		
//				while(listaResultados.hasMoreElements()){
//					ScsInclusionGuardiasEnListasBean fila=(ScsInclusionGuardiasEnListasBean)listaResultados.nextElement();
//					// Recopilacion datos
//					/* RGG 08/10/2007 cambio para que obtenga todas las guardias del tiron
//					 * Vector datosParciales= admGT.getDatosListaGuardias(fila.getIdInstitucion().toString(),fila.getIdTurno().toString(),fila.getIdGuardia().toString(),fechaInicio,fechaFin);
//					int i=0;
//					while (i<datosParciales.size()){
//						datos.add(datosParciales.elementAt(i));
//						i++;
//					}
//					*/
//					Vector guardia = new Vector();
//					guardia.add(fila.getIdTurno().toString());
//					guardia.add(fila.getIdGuardia().toString());
//
//					
//				}
///*
//				// RGG
//				datos = admGT.getDatosListaGuardias(institucion,guardias,fechaInicio,fechaFin);  
//*/				
////				 Comentado por PDM
//	    		/*if (datos.size()<=numeroLineas){
//	    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, datos.size(),rutaPlantilla+barra+institucion+barra,usr.getLanguage());    			
//	    		}
//	    		else{*/
//	    			/*correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, numeroLineas,rutaPlantilla+barra+institucion+barra,usr.getLanguage());
//	        		for (int cont=0; cont<numeroLineas; cont++){
//	        			datos.removeElementAt(0);
//	        		}
//	        		while (datos.size()>numeroLineas){
//	        			correcto=plantilla.insercionSimplePaginaNuevaDocumentoFOP(ficFOP);
//		    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, numeroLineas,rutaPlantilla+barra+institucion+barra,usr.getLanguage());
//	            		for (int cont=0; cont<numeroLineas; cont++){
//	            			datos.removeElementAt(0);
//	            		}
//	        	    }*/
//        			//correcto=plantilla.insercionSimplePaginaNuevaDocumentoFOP(ficFOP);
///*	    			correcto=plantilla.obtencionPaginaInformeListaGuardias(institucion, ficFOP, datos, nombreInstitucion, fechaInicio, fechaFin, datos.size(),rutaPlantilla+barra+institucion+barra,usr.getLanguage());
// *          		for (int cont=0; cont<datos.size(); cont++){
//	        			datos.removeElementAt(0);
//	        		}
//*/	        		    			
//	    		//}
//	    		
//				// Insertamos fin documento
//	        	// Comentado por PDM, todo esta en las plantillas	
//	    		//correcto=plantilla.insercionPieSimpleDocumentoFOP(ficFOP);
///*				
//				// Obtencion fichero PDF
//				if (correcto){
//					nombreFicheroPDF="listaGuardias_"+UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","-").replaceAll(":","#").replaceAll(" ","_")+".pdf";
//					File ficPDF=new File(rutaServidor+barra+nombreFicheroPDF); 
//					plantilla.convertFO2PDF(ficFOP, ficPDF, rutaPlantilla+File.separator+institucion);
//					ficFOP.delete();
//				}
//				else{
//					ficFOP.delete();					
//				}
//				
//			request.setAttribute("nombreFichero", nombreFicheroPDF);
//			request.setAttribute("rutaFichero", rutaServidor+barra+nombreFicheroPDF);
//			resultado="descargaFichero";
//*/			
//			}
//			else{
//			    resultado = exito("messages.listaGuardias.definirListaGuardias.generarInforme.sinGuardias.error",request);
//				// Por el cambio 2417 Quitar este mensaje: throw new ClsExceptions("Se ha producido un error en la generaci�n de informes.");
//			} 
//			
//		}
//		catch (Exception e) 
//		{
//			ficFOP.delete();
//			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
//		} 
//        return resultado;
//	}
	
}