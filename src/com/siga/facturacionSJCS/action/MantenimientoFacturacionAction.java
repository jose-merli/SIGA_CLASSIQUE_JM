//VERSIONES:
//raul.ggonzalez 07-03-2005 Creacion
//

package com.siga.facturacionSJCS.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.MantenimientoFacturacionForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* Clase action del caso de uso BUSCAR FACTURACION
* @author AtosOrigin 07-03-2005
*/
public class MantenimientoFacturacionAction extends MasterAction {
	// Atributos
	/**   */


	
	
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
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// borro el formulario en session de Avanzada
			MantenimientoFacturacionForm miformSession = (MantenimientoFacturacionForm)request.getSession().getAttribute("mantenimientoFacturacionForm");
			
			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
			request.setAttribute("SJCSInstitucionesVisibles",visibilidad);
	
			if (CenVisibilidad.tieneHijos(user.getLocation())) {
				request.setAttribute("SJCSTieneHijos","S");
			} else {
				request.removeAttribute("SJCSTieneHijos");
			}

			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("SJCSBusquedaFacturacionTipo","BFN"); // busqueda normal
			request.setAttribute("strutTrans", user.getStrutsTrans());
	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		// COMUN
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
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String destino = "";
		
		try {
	     	MantenimientoFacturacionForm miform = (MantenimientoFacturacionForm)formulario;
	
			String idFacturacion = "";
//			String idInstitucionRegistro = "";
			String idInstitucionUsuario = "";

	     	// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			
			if ((miform.getDesdeRegularizacion() == null) || (!miform.getDesdeRegularizacion().equalsIgnoreCase("si"))) {
				Vector fila = miform.getDatosTablaOcultos(0);
				if (fila!=null && fila.size()!=0){
				idFacturacion = (String)fila.get(0);
//				idInstitucionRegistro = (String)fila.get(1);
				idInstitucionUsuario = (String)fila.get(2);
				}else{
					idFacturacion = "" + miform.getIdFacturacion();
					idInstitucionUsuario = "" + miform.getIdInstitucion();
				}
			}
			else {
				miform.setDesdeRegularizacion("no");
				idFacturacion = "" + miform.getIdFacturacion();
				idInstitucionUsuario = "" + miform.getIdInstitucion();
			}
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			String modo = "edicion";
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("accion",modo);
			datosFacturacion.put("idFacturacion",idFacturacion);
			datosFacturacion.put("idInstitucion",idInstitucionUsuario);
			request.setAttribute("datosFacturacion", datosFacturacion);		
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
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
		
		try {
  	
	     	MantenimientoFacturacionForm miform = (MantenimientoFacturacionForm)formulario;

			String idFacturacion = "";
			String idInstitucionRegistro = "";
			String idInstitucionUsuario = "";

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector fila = miform.getDatosTablaOcultos(0);
			if (fila != null) {
				idFacturacion = (String)fila.get(0);
				idInstitucionRegistro = (String)fila.get(1);
				idInstitucionUsuario = (String)fila.get(2);
			}
			else {
				idFacturacion = "" + miform.getIdFacturacion();
				idInstitucionRegistro = "" + miform.getIdInstitucion();
			}
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			String modo = "consulta";
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("accion",modo);
			datosFacturacion.put("idFacturacion",idFacturacion);
			datosFacturacion.put("idInstitucion",idInstitucionRegistro);
			request.setAttribute("datosFacturacion", datosFacturacion);		
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
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
			MantenimientoFacturacionForm miform = (MantenimientoFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			//Se elimina el paginador por si estan cacheados datos de consultas que no sean de facturacion.
			//Esto es necesario porque si se pulsa el botón "volver" y los datos del paginador no 
			//se corresponden con los de la página a mostrar, se produce un error.
			request.getSession().removeAttribute("DATAPAGINADOR");
			
			String modo = "nuevo";
	
			//CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			Hashtable datosFacturacion = new Hashtable();
			datosFacturacion.put("accion",modo);
			datosFacturacion.put("idFacturacion","");
			datosFacturacion.put("idInstitucion",user.getLocation());
			request.setAttribute("datosFacturacion", datosFacturacion);		
			destino="administracion";
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
	     return destino;
	}

	/**
	 * Metodo que implementa el modo buscarPor 
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
	
			// casting del formulario
			MantenimientoFacturacionForm miFormulario = (MantenimientoFacturacionForm)formulario;
			
			// busqueda de clientes
			FcsFacturacionJGAdm adm = new FcsFacturacionJGAdm(this.getUserBean(request));
			
		/**/	
			 HashMap databackup=new HashMap();
				
				 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
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
					
						
					
					
			  }else{	
					
			  	    databackup=new HashMap();
					
					//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;
				
				
				
		/**/		
			
			
			
				Hashtable criterios = new Hashtable();
				if (user.getStrutsTrans().equalsIgnoreCase("FCS_MantenimientoPrevisiones")) {
					if (miFormulario.getEstado()!=null) criterios.put("idEstado",miFormulario.getEstado());
					if (miFormulario.getFechaIni()!=null) criterios.put("fechaIni",miFormulario.getFechaIni());
					if (miFormulario.getFechaFin()!=null) criterios.put("fechaFin",miFormulario.getFechaFin());
					if (miFormulario.getNombre()!=null) criterios.put("nombre",miFormulario.getNombre());
					if (miFormulario.getHitos()!=null) criterios.put("hito",miFormulario.getHitos());
					resultado = adm.getFacturaciones(criterios, user.getLocation(), ClsConstants.DB_TRUE);
					
				} else if (user.getStrutsTrans().equalsIgnoreCase("CEN_MantenimientoFacturacion")) {
					if (miFormulario.getNombreInstitucion()!=null) criterios.put("idInstitucion",miFormulario.getNombreInstitucion());
					if (miFormulario.getEstado()!=null) criterios.put("idEstado",miFormulario.getEstado());
					if (miFormulario.getFechaIni()!=null) criterios.put("fechaIni",miFormulario.getFechaIni());
					if (miFormulario.getFechaFin()!=null) criterios.put("fechaFin",miFormulario.getFechaFin());
					if (miFormulario.getNombre()!=null) criterios.put("nombre",miFormulario.getNombre());
					if (miFormulario.getHitos()!=null) criterios.put("hito",miFormulario.getHitos());
					resultado = adm.getFacturaciones(criterios, user.getLocation(), ClsConstants.DB_FALSE);
				}
			
			
			
			databackup.put("paginador",resultado);
			if (resultado!=null){ 
			   datos = resultado.obtenerPagina(1);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			}   
			
			   
			  
			
    		
	   }
		
		 	destino="resultado";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
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
			
			// Obtengo los datos del formulario		
			MantenimientoFacturacionForm miForm = (MantenimientoFacturacionForm)formulario;		

			Vector fila = miForm.getDatosTablaOcultos(0);
			String idFacturacion = (String)fila.get(0);
			String idInstitucionRegistro = (String)fila.get(1);
			String idInstitucionUsuario = (String)fila.get(2);

			FcsFacturacionJGAdm adm = new FcsFacturacionJGAdm(this.getUserBean(request));

			// Recupero el nombre de los ficheros asociados a la facturacion
			Hashtable nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(new Integer(idInstitucionRegistro), new Integer(idFacturacion), this.getUserBean(request));

			// Comienzo control de transacciones 
			tx = usr.getTransactionPesada();			
			tx.begin();

			Object[] param_in = new Object[2];
	 		String resultadoPl[] = new String[2];
	 		//Parametros de entrada del PL
	        HttpSession ses = (HttpSession)request.getSession();
			param_in[0] = idInstitucionRegistro;			
			param_in[1] = idFacturacion;
	 		//Ejecucion del PL
			resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_BORRAR_FACTURACION (?,?,?,?)}", 2, param_in);
			correcto = ((String)resultadoPl[0]).equals("0");
			if (!correcto) 
				throw new SIGAException("messages.deleted.error");

			tx.commit();

			// borrado fisico de ficheros del servidor web
			UtilidadesFacturacionSJCS.borrarFicheros(new Integer(idInstitucionRegistro), nombreFicheros, this.getUserBean(request));

			request.setAttribute("descOperation","messages.deleted.success");				
		 }
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
	   	 }
		 if (correcto) 
		 	return exitoRefresco("messages.deleted.success",request);
		 else 
		 	return exitoRefresco("messages.deleted.error",request);
	}
	


}
