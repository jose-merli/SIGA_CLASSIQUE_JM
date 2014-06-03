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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import weblogic.wtc.jatmi.viewjCompiler;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenMandatosAdm;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.MantenimientoMandatosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoMandatosAction extends MasterAction {	
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
						MantenimientoMandatosForm formClientes = (MantenimientoMandatosForm)miForm;
						formClientes.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						formClientes.reset(mapping,request);
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("enviarCliente")){
						// enviarCliente
						mapDestino = enviarCliente(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("enviarPersona")){
						// enviarCliente
						mapDestino = enviarPersona(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarInit")){
						miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = buscarPor(mapping, miForm, request, response); 
					}else if (accion.equalsIgnoreCase("generaExcel")){
						mapDestino = generaExcel(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("firmarSeleccionados")){
						mapDestino = firmarMandatos(mapping, miForm, request, response);
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
			MantenimientoMandatosForm miformSession = (MantenimientoMandatosForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
			if (miformSession!=null) {
				miformSession.reset(mapping,request);
			}
			MantenimientoMandatosForm miformSession2 = (MantenimientoMandatosForm)request.getSession().getAttribute("mantenimientoMandatosForm");
			if (miformSession2!=null) {
				miformSession2.reset(mapping,request);
			}
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;
			miform.reset(mapping,request);
			
			String colegiado = request.getParameter("colegiado");
			miform.setColegiado(colegiado);
	
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
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
		//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
		//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
		//de la super clase(MasterAction)
		String[] clavesBusqueda={"NCOLEGIADO","NIFCIF","APELLIDOS","NOMBRE","TIPOMANDATO","IBAN","REFERENCIA","FECHAFIRMA","LUGARFIRMA"};
	 
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			// casting del formulario
			MantenimientoMandatosForm miFormulario = (MantenimientoMandatosForm)formulario;

			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
				&& !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null &&!isSeleccionarTodos){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
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
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				 			
				com.siga.Utilidades.paginadores.Paginador resultado = null;
				Vector datos = null;

				resultado = mandatoAdm.getClientesMandatos(idInstitucion,miFormulario, user.getLanguage());
				
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)mandatoAdm.selectGenerico(resultado.getQueryInicio()));
						aniadeClavesBusqueda(clavesBusqueda,clavesRegSeleccinados);
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

				request.setAttribute("CenResultadoBusquedaClientes",resultado);				
			}
			destino="resultado";
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
	        CenInstitucionAdm instAdm = new CenInstitucionAdm(user); 
	        request.setAttribute("nombreColegio",instAdm.getNombreInstitucion(idInstitucion));

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
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
			
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;

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
			
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;

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
	
	

	
	
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
        Vector datos = new Vector();
		
		
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
	        MantenimientoMandatosForm form = (MantenimientoMandatosForm)formulario;
	        CenColegiadoAdm colegiadoAdm = null;
			CenNoColegiadoAdm noColegiadoAdm = null;
			Vector datosTabla = null;
			String idTipoPersona = null;
	        for (int i = 0; i < form.getDatosTabla().size(); i++) {
	        	datosTabla = new Vector();
	        	String linea = form.getDatosTablaOcultos(i).toString();
	        	linea=linea.replaceAll("#", " ");
	        	linea=linea.substring(1, linea.length()-1);
				StringTokenizer tokCampos = new StringTokenizer(linea, "||");
	        	String[] vCampos ={"","","","","","","","",""};
	        	vCampos=linea.split("\\|\\|", 9);
	        	/*int j=0;
	        	while(tokCampos.hasMoreElements()){
					vCampos[j]=tokCampos.nextToken();
					j++;
				}*/
	        	
	        	Hashtable ht = new Hashtable();
				ht.put("NCOLEGIADO", 	vCampos[0]);
				ht.put("NIFCIF", 		vCampos[1]);
				ht.put("APELLIDOS", 	vCampos[2]);
				ht.put("NOMBRE", 		vCampos[3]);
				ht.put("TIPOMANDATO", 	vCampos[4]);
				ht.put("IBAN", 			vCampos[5]);
				ht.put("REFERENCIA", 	vCampos[6]);
				ht.put("FECHA FIRMA", 	UtilidadesString.formatoFecha(vCampos[7], ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				ht.put("LUGAR FIRMA",	vCampos[8]);
				datosTabla.add(ht);
				
				if(datosTabla!=null)
					datos.addAll(datosTabla);
				else
					System.out.println("Bingo");
			}
			
			
	        String[] cabeceras = new String[]{"NCOLEGIADO","NIFCIF","APELLIDOS","NOMBRE","TIPOMANDATO","IBAN","REFERENCIA","FECHA FIRMA","LUGAR FIRMA"};
	        String[] campos = new String[]{"NCOLEGIADO","NIFCIF","APELLIDOS","NOMBRE","TIPOMANDATO","IBAN","REFERENCIA","FECHA FIRMA","LUGAR FIRMA"};
	        
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
	
	protected String firmarMandatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
        Vector datos = new Vector();
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
	        MantenimientoMandatosForm form = (MantenimientoMandatosForm)formulario;
			
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			
	        for (int i = 0; i < form.getDatosTabla().size(); i++) {
	        	String linea = form.getDatosTablaOcultos(i).toString();
	        	linea=linea.replaceAll("#", " ");
	        	linea=linea.substring(1, linea.length()-1);
				StringTokenizer tokCampos = new StringTokenizer(linea, "||");
	        	String[] vCampos ={"","","","","","","","",""};
	        	vCampos=linea.split("\\|\\|", 9);
	        	/*int j=0;
	        	while(tokCampos.hasMoreElements()){
					vCampos[j]=tokCampos.nextToken();
					j++;
				}*/
	        	
	        	Hashtable ht = new Hashtable();
				ht.put("NCOLEGIADO", 	vCampos[0]);
				ht.put("NIFCIF", 		vCampos[1]);
				ht.put("APELLIDOS", 	vCampos[2]);
				ht.put("NOMBRE", 		vCampos[3]);
				ht.put("TIPOMANDATO", 	vCampos[4]);
				ht.put("IBAN", 			vCampos[5]);
				ht.put("REFERENCIA", 	vCampos[6]);
				ht.put("FECHA FIRMA", 	vCampos[7]);
				ht.put("LUGAR FIRMA",	vCampos[8]);
				
				mandatoAdm.firmarReferencia(vCampos[6], form.getFechaFirma(), form.getLugarFirma());

			}
			
			/*
	        String[] cabeceras = new String[]{"NCOLEGIADO","NIFCIF","APELLIDOS","NOMBRE","TIPOMANDATO","IBAN","REFERENCIA","FECHA FIRMA","LUGAR FIRMA"};
	        String[] campos = new String[]{"NCOLEGIADO","NIFCIF","APELLIDOS","NOMBRE","TIPOMANDATO","IBAN","REFERENCIA","FECHA FIRMA","LUGAR FIRMA"};
	        
			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", idInstitucion+"_"+this.getUserName(request).toString());
						
			*/
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "exito";
	}


}
