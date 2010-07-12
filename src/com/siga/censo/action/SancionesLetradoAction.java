package com.siga.censo.action;

import java.util.Date;
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
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenSancionAdm;
import com.siga.beans.CenSancionBean;
import com.siga.censo.form.SancionesLetradoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso SANCIONES DEL LETRADO
 * @author RGG
 */
public class SancionesLetradoAction extends MasterAction 
{
	
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

			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("archivar")){
				mapDestino = archivar(mapping, miForm, request, response);			
			}else if (accion.equalsIgnoreCase("fecha")){ 
				mapDestino = fecha(mapping, miForm, request, response);	
			}else{
				return super.executeInternal(mapping,
						formulario,
						request, 
						response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}
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
		UsrBean user = null;
		UserTransaction tx = null;
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		    String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
            String tienepermisoArchivo= admSancion.getTienePermisoArchivación(idinstitucion,username);							
			request.setAttribute("tienepermiso",tienepermisoArchivo);
			miform.reset(mapping,request);


	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		return "inicio";
	}

	/**
	 * Metodo que implementa el modo buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String accion = (String)request.getParameter("accion");
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));
			Vector resultado = admSancion.getSancionesLetrado(miform.getIdPersona(), miform.getIdInstitucionAlta());
			String idinstitucion= user.getLocation();
            String username=user.getUserName();
			String tienepermisoArchivo=admSancion.getTienePermisoArchivación(idinstitucion,username);
			request.setAttribute("tienepermisoArchivo",tienepermisoArchivo);
		
			
			
			// RGG indico que estamos en la pestaña de letrado (Datos de colegiacion)
			request.setAttribute("datosColegiacion","1");			
			request.setAttribute("personaColegiacion",miform.getIdPersona());			
			request.setAttribute("institucionColegiacion",miform.getIdInstitucionAlta());			
			request.setAttribute("ACCION", accion);
			request.setAttribute("resultado",resultado);
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		return "resultados";
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
	protected String buscarPor (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;		
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion = new CenSancionAdm(this.getUserBean(request));
			
			HashMap databackup=new HashMap();
			
			String tienepermisoArchivo=admSancion.getTienePermisoArchivación(idinstitucion,username);
			request.setAttribute("tienepermisoArchivo",tienepermisoArchivo);
			databackup.put("tienepermisoArchivo", tienepermisoArchivo);
			 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
			 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
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
				
		  	    databackup=new HashMap();
				
				//obtengo datos de la consulta 			
			Paginador resultado = null;
			Vector datos = null;
			
			String tipobusqueda = "";
			if (miform.getMostrarSanciones()!=null){
				tipobusqueda = ClsConstants.COMBO_MOSTRAR_ARCHIVADAS;				
			}else tipobusqueda=ClsConstants.COMBO_MOSTRAR_SINARCHIVAR;		
			
			//se Obtienen los datos de las consulta para recuperar los datos de las sanciones archivadas o sin archivar.
			resultado = admSancion.getSancionesBuscar(miform, user.getLocation(),tipobusqueda);
			
			
			
			 databackup.put("paginador",resultado);
				if (resultado!=null){ 
				   datos = resultado.obtenerPagina(1);
				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADOR",databackup);
				} 
		  }	
				
			//request.setAttribute("resultado",resultado);
			request.setAttribute("activarFilaSel","true");
	    } 
		catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		return "resultados";
	}	
	
	/**
	 * Metodo que implementa el modo modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		String salida = "";
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= usr.getLocation();
            String username=usr.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));			
			// Obtengo los datos del formulario
			SancionesLetradoForm miForm = (SancionesLetradoForm)formulario;
			boolean checkFirmeza  = UtilidadesString.stringToBoolean(miForm.getChkFirmeza());			
			boolean checkRehabilitado  = UtilidadesString.stringToBoolean(miForm.getChkRehabilitado());
			boolean checkArchivada  = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
		    
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			/*
			CenSancionBean beanSan = new CenSancionBean();
			beanSan.setIdSancion(new Integer(miForm.getIdSancion()));
			beanSan.setIdTipoSancion(new Integer(miForm.getTipoSancion()));
			beanSan.setIdPersona(new Integer(miForm.getIdPersona()));
			beanSan.setIdInstitucion(new Integer(usr.getLocation()));
			beanSan.setIdInstitucionSancion(new Integer(miForm.getNombreInstitucion()));
			*/
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, miForm.getIdSancion());						
			UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, miForm.getTipoSancion());
			UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, usr.getLocation());						
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, miForm.getNombreInstitucion());						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaAcuerdo()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaFin()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, (miForm.getFirmeza()!=null && !miForm.getFirmeza().equals(""))?GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFirmeza()):"");						
			//UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA, (miForm.getChkFirmeza()==null)?"0":"1");						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicio()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, (miForm.getRehabilitado()!=null && !miForm.getRehabilitado().equals(""))?GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getRehabilitado()):"");						
			//UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO, (miForm.getChkRehabilitado()==null)?"0":"1");
			UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaResolucion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaImposicion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, miForm.getObservaciones());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, miForm.getRefCGAE());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, miForm.getRefColegio());						
			UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, miForm.getTexto());
			
			String tienepermisoArchivo=admSancion.getTienePermisoArchivación(idinstitucion,username);
			
			if (tienepermisoArchivo.equals("1")){
				boolean checkArchivada1  = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaArchivada()));
				if  (checkArchivada1){
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"1");	
				}else{
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
				}
			}else {
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
			}
			
			
			
			if (checkFirmeza){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"0");
			}
			if (checkRehabilitado){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"0");
			}
			
			

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			

			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	

		
			if (!admSancion.updateDirect(hash,admSancion.getClavesBean(),admSancion.getCamposBean())) {
				throw new ClsExceptions("Error al realizar el update: " + admSancion.getError());
			}

			tx.commit();

			//salida = this.exitoModal("messages.updated.success",request);
			salida = this.exitoModalSinRefresco("messages.updated.success",request);
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return salida;	   
	   
	}

	/**
	 * Metodo que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		String salida = "";
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= usr.getLocation();
            String username=usr.getUserName();

			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
			// Obtengo los datos del formulario
			SancionesLetradoForm miForm = (SancionesLetradoForm)formulario;
			boolean checkFirmeza  = UtilidadesString.stringToBoolean(miForm.getChkFirmeza());
			boolean checkRehabilitado  = UtilidadesString.stringToBoolean(miForm.getChkRehabilitado());
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, admSancion.getNuevoId(usr.getLocation()));						
			UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, miForm.getTipoSancion());
			UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, usr.getLocation());						
			UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, miForm.getNombreInstitucion());						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaAcuerdo()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaFin()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFirmeza()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicio()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getRehabilitado()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaResolucion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaImposicion()));						
			UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, miForm.getObservaciones());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, miForm.getRefCGAE());						
			UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, miForm.getRefColegio());	
			UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, miForm.getTexto());	
			
			String tienepermisoArchivo=admSancion.getTienePermisoArchivación(idinstitucion,username);
			
			if (tienepermisoArchivo.equals("1")){
				boolean checkArchivada  = UtilidadesString.stringToBoolean(miForm.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaArchivada()));
				if  (checkArchivada){
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"1");	
				}else{
					UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
				}
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA,"0");
			}
			
			
			if (checkFirmeza){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA,"0");
			}
			if (checkRehabilitado){
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"1");	
			}else{
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO,"0");
			}
			
				
			

			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
		
			if (!admSancion.insert(hash)) {
				throw new ClsExceptions("Error al realizar el insert: " + admSancion.getError());
			}

			tx.commit();

			salida = this.exitoModal("messages.updated.success",request);
			
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return salida;	   
	   
	}
	
	/**
	 * Metodo que implementa el modo borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable(); 		
		UserTransaction tx = null;
		String salida = "";
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
 			
			// Obtengo los datos del formulario
			SancionesLetradoForm miForm = (SancionesLetradoForm)formulario;
		    
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, (String)miForm.getDatosTablaOcultos(0).get(0));
			UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, (String)miForm.getDatosTablaOcultos(0).get(1)); 						
			Vector v = admSancion.selectByPK(hash);
			CenSancionBean b = null;
			if (v!=null && v.size()>0) {
				b = (CenSancionBean) v.get(0);
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}
				
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
		
			if (!admSancion.delete(b)) {
				throw new ClsExceptions("Error al realizar el borrado: " + admSancion.getError());
			}

			tx.commit();

			salida = this.exitoRefresco("messages.updated.success",request);
			
	   } catch (Exception e) {
	   		throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
	   return salida;	   
	   
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
	protected String nuevo (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
            String tienepermisoArchivo= admSancion.getTienePermisoArchivación(idinstitucion,username);
			// RGG indico si estamos en pestaña de datos de colegiacion o no
			request.setAttribute("pestanaColegiacion",request.getParameter("pestanaColegiacion"));
			request.setAttribute("personaColegiacion",request.getParameter("personaColegiacion"));
			request.setAttribute("institucionColegiacion",request.getParameter("institucionColegiacion"));							
			request.setAttribute("tienepermiso",tienepermisoArchivo);
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		
	    return "editar";
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
	protected String editar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
            String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hashF = new Hashtable();
			hashF.put(CenSancionBean.C_IDPERSONA, (String)miform.getDatosTablaOcultos(0).get(0));
			hashF.put(CenSancionBean.C_IDSANCION, (String)miform.getDatosTablaOcultos(0).get(1)); 						
			Vector v = admSancion.select(hashF);
			CenSancionBean b = null;
			if (v!=null && v.size()>0) {
				b = (CenSancionBean) v.get(0);
				Hashtable hash = new Hashtable();
				UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, String.valueOf(b.getIdSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, String.valueOf(b.getIdInstitucionSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, b.getRefColegio());
				UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, b.getRefCGAE());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, b.getFechaAcuerdo());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, b.getFechaFin());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, b.getFechaFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, b.getFechaInicio());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, b.getFechaRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, b.getFechaResolucion());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, b.getFechaImposicion());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA, b.getChkFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO, b.getChkRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, b.getTexto());
				UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, b.getObservaciones());				
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA, b.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, b.getFechaArchivada());
				
				String tienepermisoArchivo= admSancion.getTienePermisoArchivación(idinstitucion,username);				
				request.getSession().setAttribute("DATABACKUP",hash);				
				request.setAttribute("tienepermiso",tienepermisoArchivo);
				request.setAttribute("registro",hash);
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		
	    return "editar";
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
	protected String ver (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;
		
		try {
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			 String idinstitucion= user.getLocation();
            String username=user.getUserName();

			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hashF = new Hashtable();
			hashF.put(CenSancionBean.C_IDPERSONA, (String)miform.getDatosTablaOcultos(0).get(0));
			hashF.put(CenSancionBean.C_IDSANCION, (String)miform.getDatosTablaOcultos(0).get(1)); 						
			Vector v = admSancion.select(hashF);
			CenSancionBean b = null;
			if (v!=null && v.size()>0) {
				b = (CenSancionBean) v.get(0);
				Hashtable hash = new Hashtable();
				UtilidadesHash.set(hash,CenSancionBean.C_IDSANCION, String.valueOf(b.getIdSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
				UtilidadesHash.set(hash,CenSancionBean.C_IDINSTITUCIONSANCION, String.valueOf(b.getIdInstitucionSancion()));
				UtilidadesHash.set(hash,CenSancionBean.C_REFCOLEGIO, b.getRefColegio());
				UtilidadesHash.set(hash,CenSancionBean.C_REFCGAE, b.getRefCGAE());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAACUERDO, b.getFechaAcuerdo());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIN, b.getFechaFin());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAFIRMEZA, b.getFechaFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAINICIO, b.getFechaInicio());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAREHABILITADO, b.getFechaRehabilitado());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHARESOLUCION, b.getFechaResolucion());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAIMPOSICION, b.getFechaImposicion());
				UtilidadesHash.set(hash,CenSancionBean.C_TEXTO, b.getTexto());
				UtilidadesHash.set(hash,CenSancionBean.C_OBSERVACIONES, b.getObservaciones());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKARCHIVADA, b.getChkArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_FECHAARCHIVADA, b.getFechaArchivada());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKFIRMEZA, b.getChkFirmeza());
				UtilidadesHash.set(hash,CenSancionBean.C_CHKREHABILITADO, b.getChkRehabilitado());
				
				String tienepermisoArchivo= admSancion.getTienePermisoArchivación(idinstitucion,username);							
				request.setAttribute("tienepermiso",tienepermisoArchivo);
				
				request.setAttribute("registro",hash);
			} else {
				throw new ClsExceptions("Error al obtener el elemento para borrarlo: NO EXISTE");
			}
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}
 		
	    return "editar";
	}
	
	
	/**
	 * Metodo que implementa el modo insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String archivar(ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean user = null;
		UserTransaction tx = null;
		String salida = "";
		
		try {
		
			SancionesLetradoForm miform = (SancionesLetradoForm)formulario;
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idinstitucion= user.getLocation();
            String username=user.getUserName();
			CenSancionAdm admSancion=new CenSancionAdm(this.getUserBean(request));
            String tienepermisoArchivo= admSancion.getTienePermisoArchivación(idinstitucion,username);		
			request.setAttribute("institucionColegiacion",request.getParameter("institucionColegiacion"));			
			request.setAttribute("tienepermiso",tienepermisoArchivo);
		
			String fechaarchivada= miform.getFechaArchivada();
			
			int nsanciones=admSancion.getArchivar(idinstitucion,fechaarchivada);		
			String message=(UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.Archivos.success"));
			
			message+=" "+ nsanciones+" "+UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.ArchivosSanciones.success");
			
			salida= this.exitoModal(message, request);
			
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
    	}   
	     
	    return salida;
 	}
	
	
	/**
	 * Devuelve los datos necesarios para la insercion de un nuevo estado
	 */
	protected String fecha(ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		request.setAttribute("modo","archivar");
		return "archivar";
	} 
	


}