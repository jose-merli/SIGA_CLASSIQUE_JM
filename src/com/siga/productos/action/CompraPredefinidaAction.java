/**
 * Clase action para la compra directa predefinida.<br/>
 * @version RGG 10/04/2007   
 */

package com.siga.productos.action;


import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.certificados.form.SIGASolicitudCertificadoForm;
import com.siga.general.*;


import java.util.*;


public class CompraPredefinidaAction extends MasterAction {

	
	/**
	 * Redefinicion de la funcion executeInternal para controlar las nuevas acciones confirmar y denegar
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {		
			String mapDestino = "exception";
			MasterForm miForm = null;
		    
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
			
				String accion = miForm.getModo();
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
				} else 
				// abrirMenDiligencia
				if (accion.equalsIgnoreCase("abrirMenDiligencia")){
					mapDestino = abrirMenDiligencia(mapping, miForm, request, response);
					break;
				} else
				// abrirDiligencia
				if (accion.equalsIgnoreCase("abrirDiligencia")){
					mapDestino = abrirDiligencia(mapping, miForm, request, response);
					break;
				} else
				// insertarDiligencia
				if (accion.equalsIgnoreCase("insertarDiligencia")){
					mapDestino = insertarDiligencia(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("abrirCompraColegio")){
					mapDestino = abrirCompraColegio(mapping, miForm, request, response);
					break;
				}
				else if (accion.equalsIgnoreCase("compraColegio")){
					mapDestino = compraColegio(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("insertarCompraColegio")){
					mapDestino = insertarCompraColegio(mapping, miForm, request, response);
					break;
				}else
					return super.executeInternal(mapping, formulario, request, response);
				
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 				
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		}
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			//throw new SIGAException("Error en la Aplicación",e);
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.productos"}); 
		}
	}
	
	/** 
	 *  Funcion que atiende la accion abrir. VA CON INSERTAR
	 *  Realiza la compra de un certificado concreto, indicado en la configuración. Debido a lógica de negocio este botón solo se puede configurar para CGAE y Consejos. Concretamente el problema viene porque la gestión del cobro la hace el colegio de facturación siempre por defecto y además existen limitaciones sobre la información de otros colegios a las que tiene acceso un Consejo y no tiene un colegio.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="mensajes";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
			StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    Hashtable htProducto = new Hashtable();
		    htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucionP );
		    htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPI.select(htProducto);
		    if (vDatos!=null && vDatos.size()>0)
		    {
	            PysProductosInstitucionBean beanPI = (PysProductosInstitucionBean)vDatos.get(0);
	            if (beanPI.getTipoCertificado()==null || beanPI.getTipoCertificado().equals(""))
	            {
	            	throw new SIGAException("certificados.boton.mensaje.productoNoCertificado");
	            }
		    } else {
		    	throw new SIGAException("certificados.boton.mensaje.productoNoExiste");
		    }
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
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
	protected String abrirMenDiligencia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="mensajesDiligencia";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion AbrirDiligencia. VA CON INSERTARDILIGENCIA
	 *  Realiza una solicitud de comunicación y diligencia directa. Debido a lógica de negocio este botón solo se puede configurar para CGAE y Consejos.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirDiligencia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="abrirDiligencia";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			// cosas
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion insertarDiligencia. VA CON ABRIRDILIGENCIA 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertarDiligencia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String salida="";
		UserTransaction tx = null;
		
		try{
			
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			tx = user.getTransaction();
			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}

			String idInstitucionOrigen=request.getParameter("idInstitucionOrigen");
			String idInstitucionDestino=request.getParameter("idInstitucionDestino");
			String descripcion=request.getParameter("descripcion");
			String fechaSolicitud = request.getParameter("fechaSolicitud");
			String metodoSolicitud = request.getParameter("metodoSolicitud");
		    
			// cosas
		    CerSolicitudCertificadosAdm solicitudAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    if (idInstitucionOrigen==null || idInstitucionOrigen.trim().equals("")) {
		    	return exito("certificados.solicitudes.literal.faltaOrigen",request);
		    }
		    if (idInstitucionDestino==null || idInstitucionDestino.trim().equals("")) {
		    	return exito("certificados.solicitudes.literal.faltaDestino",request);
		    }

		    tx.begin();
		    
		    CerSolicitudCertificadosBean sol = solicitudAdm.insertarSolicitudCertificado(idPersonaX,
            								          idInstitucionOrigen,
            								          idInstitucionDestino,
            								          descripcion,
            								          fechaSolicitud,
            								          metodoSolicitud,
            								          user);
			
            tx.commit();
            
	        request.setAttribute("BOT_mensaje","messages.diligencia.updated.success");
	        request.setAttribute("BOT_idInstitucion",sol.getIdInstitucion().toString());
	        request.setAttribute("BOT_idSolicitudCertificado",sol.getIdSolicitud().toString());
	        request.setAttribute("BOT_concepto",sol.getDescripcion());
	        request.setAttribute("BOT_idProducto",sol.getPpn_IdProducto().toString());
	        request.setAttribute("BOT_idTipoProducto",sol.getPpn_IdTipoProducto().toString());
	        request.setAttribute("BOT_idProductoInstitucion",sol.getPpn_IdProductoInstitucion().toString());
	        
	        salida = "exitoDiligencia";

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		} 
			
		return salida;
	}
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada. VA CON MODIFICAR
	 *  Realiza la compra de cualquier certificado del colegio, seleccionado en una ventana posterior. Debido a lógica de negocio este botón solo se puede configurar para CGAE y Consejos. Concretamente el problema viene porque la gestión del cobro la hace el colegio de facturación siempre por defecto y además existen limitaciones sobre la información de otros colegios a las que tiene acceso un Consejo y no tiene un colegio.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="mensajes2";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion editar.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="selPlantilla";


		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
			StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
			
		    Hashtable htPlantilla = new Hashtable();
			htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucionP );
		    htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPlantilla.select(htPlantilla);
		    String porDefecto="";
		    if (vDatos!=null)
		    {
		        for (int i=0; i<vDatos.size(); i++)
		        {
		            CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(i);
		            
		            if (beanPlantilla.getPorDefecto().equals("S"))
		            {
		                porDefecto=""+beanPlantilla.getIdPlantilla();
		                
		                i=vDatos.size();
		            }
		        }
		    }
	
		    request.setAttribute("porDefecto", porDefecto);
			
			request.setAttribute("idInstitucionP",idInstitucionP);
			request.setAttribute("idTipoProducto",idTipoProducto);
			request.setAttribute("idProducto",idProducto);
			request.setAttribute("idProductoInstitucion",idProductoInstitucion);

		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	protected String abrirCompraColegioOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="abrirCompraColegio";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
			StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    Hashtable htProducto = new Hashtable();
		    htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucionP );
		    htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPI.select(htProducto);
		    if (vDatos!=null && vDatos.size()>0)
		    {
	            PysProductosInstitucionBean beanPI = (PysProductosInstitucionBean)vDatos.get(0);
	            if (beanPI.getTipoCertificado()==null || beanPI.getTipoCertificado().equals(""))
	            {
	            	throw new SIGAException("certificados.boton.mensaje.productoNoCertificado");
	            }
		    } else {
		    	throw new SIGAException("certificados.boton.mensaje.productoNoExiste");
		    }
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	protected String abrirCompraColegio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="abrirCompraColegio";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			String tipoParametro = beanBot.getParametro();
			if(tipoParametro.equals("certificado")){
				
				/*SIGAMantenimientoCertificadosForm form = (SIGAMantenimientoCertificadosForm)formulario;
		        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		        PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
		        
		        String institucion = userBean.getLocation();

		        String where = " WHERE ";
		        
		        where += PysProductosInstitucionBean.C_TIPOCERTIFICADO + " IN ('" + PysProductosInstitucionBean.PI_COMUNICACION_CODIGO + "','" +
		        																   PysProductosInstitucionBean.PI_DILIGENCIA_CODIGO + "','" +
		        																   PysProductosInstitucionBean.PI_CERTIFICADO_CODIGO + "')";
		        where += " AND " + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + institucion;
		        
		        Vector datos = productoAdm.selectNLS(where);
				
				*/
				
			}else if(tipoParametro.equals("producto")){
				/*
				StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
				String idInstitucionP="";
				String idTipoProducto="";
				String idProducto="";
				String idProductoInstitucion="";
				try {
					if (st.hasMoreElements()) {
						idInstitucionP=(String)st.nextElement();
						idTipoProducto=(String)st.nextElement();
						idProducto=(String)st.nextElement();
						idProductoInstitucion=(String)st.nextElement();
					}
				} catch (Exception ee) {
					throw new SIGAException("certificados.boton.mensaje.malConfigurado");
				}
			    Hashtable htProducto = new Hashtable();
			    htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucionP );
			    htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
			    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
			    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
			
			    Vector vDatos = admPI.select(htProducto);
			    if (vDatos!=null && vDatos.size()>0)
			    {
		            PysProductosInstitucionBean beanPI = (PysProductosInstitucionBean)vDatos.get(0);
		            if (beanPI.getTipoCertificado()==null || beanPI.getTipoCertificado().equals(""))
		            {
		            	throw new SIGAException("certificados.boton.mensaje.productoNoCertificado");
		            }
			    } else {
			    	throw new SIGAException("certificados.boton.mensaje.productoNoExiste");
			    }
			    */
				
			}else{
				//Dejo abierto esto por si se quiere elegir el tipo de producto. E 
				//Se podria hacer dejando el parametro nulo, y pasariamos a la jsp
				//entonces pasar un Array con el tipo de
				// productos y en la jsp hacer un combo hijo que, recogiendo el valor del tipo,
				//nos mostrara los productos.
				
			} 
			
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
			Vector v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			v2 = admVal.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	protected String compraColegio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		String result="compraColegio";


		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			/*
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			/*
			StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
			/*
		    Hashtable htPlantilla = new Hashtable();
			htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucionP );
		    htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPlantilla.select(htPlantilla);
		    /*String porDefecto="";
		    if (vDatos!=null)
		    {
		        for (int i=0; i<vDatos.size(); i++)
		        {
		            CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(i);
		            
		            if (beanPlantilla.getPorDefecto().equals("S"))
		            {
		                porDefecto=""+beanPlantilla.getIdPlantilla();
		                
		                i=vDatos.size();
		            }
		        }
		    }*/
	
		    /*
		    request.setAttribute("porDefecto", porDefecto);
			
			request.setAttribute("idInstitucionP",idInstitucionP);
			request.setAttribute("idTipoProducto",idTipoProducto);
			request.setAttribute("idProducto",idProducto);
			request.setAttribute("idProductoInstitucion",idProductoInstitucion);
			*/
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion ver.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="selPlantilla2";


		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select("WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			

/*			
		    Hashtable htPlantilla = new Hashtable();
			htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucionP );
		    htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPlantilla.select(htPlantilla);
		    String porDefecto="";
		    if (vDatos!=null)
		    {
		        for (int i=0; i<vDatos.size(); i++)
		        {
		            CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(i);
		            
		            if (beanPlantilla.getPorDefecto().equals("S"))
		            {
		                porDefecto=""+beanPlantilla.getIdPlantilla();
		                
		                i=vDatos.size();
		            }
		        }
		    }
	
		    request.setAttribute("porDefecto", porDefecto);
			
			request.setAttribute("idInstitucionP",idInstitucionP);
			request.setAttribute("idTipoProducto",idTipoProducto);
			request.setAttribute("idProducto",idProducto);
			request.setAttribute("idProductoInstitucion",idProductoInstitucion);
*/
			
		    request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idInstitucionX",idInstitucionX);
			request.setAttribute("idPersonaX",idPersonaX);
			request.setAttribute("idBoton",idBoton);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
		return result;
	}
	
	/** 
	 *  Funcion que atiende la accion insertar. VA CON ABRIR 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String salida = null;
		UserTransaction tx = null;
		String mensaje ="";
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idInstitucionPresentadorX=request.getParameter("idInstitucionPresentador");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			String metodoSolicitud=request.getParameter("metodoSolicitud");
			String fechaSolicitud=request.getParameter("fechaSolicitud");

			//String idPlantilla=request.getParameter("idPlantilla");

			PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
			StringTokenizer st = new StringTokenizer(beanBot.getValorParametro(),"#");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    Hashtable htProducto = new Hashtable();
		    htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucionP );
		    htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPI.select(htProducto);
		    PysProductosInstitucionBean beanPI = null;
		    if (vDatos!=null && vDatos.size()>0)
		    {
	            beanPI = (PysProductosInstitucionBean)vDatos.get(0);
	            if (beanPI.getTipoCertificado()==null || !beanPI.getTipoCertificado().equalsIgnoreCase("C"))
	            {
	            	throw new SIGAException("certificados.boton.mensaje.productoNoCertificado");
	            }
		    } else {
		    	throw new SIGAException("certificados.boton.mensaje.productoNoExiste");
		    }
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			String formaPago=null; 
			String tipoEnvio=null; 
			if (v2!=null && v2.size()>0) {
				beanVal=(AdmValorPreferenteBean)v2.get(0);
				formaPago=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			} 
			
			PysFormaPagoProductoAdm admForm= new PysFormaPagoProductoAdm(this.getUserBean(request));
			PysFormaPagoProductoBean beanForm = null;
			Vector v3 = admForm.select(" WHERE IDINSTITUCION="+beanPI.getIdInstitucion()+" AND IDTIPOPRODUCTO="+beanPI.getIdTipoProducto()+" AND IDPRODUCTO="+beanPI.getIdProducto()+" AND IDPRODUCTOINSTITUCION="+beanPI.getIdProductoInstitucion());
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.formaPagoMAL");
			} 
			
			
			
			v2 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2!=null && v2.size()>0) {
				beanVal=(AdmValorPreferenteBean)v2.get(0);
				tipoEnvio=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}		

			
			tx = user.getTransaction(); 
			tx.begin();
			
			// proceso de compra de certificado
			Articulo a = admPI.realizarCompraPredefinida(new Integer(idInstitucionX),idInstitucionPresentadorX,new Integer(idTipoProducto),new Long(idProducto), new Long(idProductoInstitucion), new Long(idPersonaX),formaPago ,tipoEnvio,false, fechaSolicitud, metodoSolicitud);

			// Insertamos el certificado (esto antes lo hacia un trigger)
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
			// compruebo la persona del colegio origen
		    Vector v4 = admSolicitud.select(" WHERE IDINSTITUCION="+a.getIdInstitucion() +
		    					" AND PPN_IDTIPOPRODUCTO="+a.getIdTipo() +
								" AND PPN_IDPRODUCTO="+ a.getIdArticulo()+
								" AND PPN_IDPRODUCTOINSTITUCION="+ a.getIdArticuloInstitucion()+
								" AND IDPERSONA_DES="+idPersonaX +
								" AND IDPETICIONPRODUCTO="+a.getIdPeticion());
		    
		    CerSolicitudCertificadosBean beanSolic = null;
		    if (v4!=null && v4.size()>0) {
		    	beanSolic = (CerSolicitudCertificadosBean) v4.get(0);
		    } else {
		    	throw new ClsExceptions("No se encuentra la solicitud, es posible que el producto no fuera certificado.");
		    }
		    
		    /* RGG 12/04/2007 NUEVO CAMBIO DE CRITERIO: NO SE GENERA Y DESPUES NOS LLEVA A LA VENTANA DE SOLICITUDES DE CERTIFICADOS
			
	        String fechaSolicitud = beanSolic.getFechaSolicitud();
	        String idSolicitud = beanSolic.getIdSolicitud().toString();
	        String idInstitucionSol = beanSolic.getIdInstitucion_Sol().toString();
		        
	        boolean usarIdInstitucion = false;
			boolean colegiadoEnOrigen = true;
			
			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(new Integer(user.getUserName()));
			if (beanPI.getTipoCertificado().equalsIgnoreCase("C")) {
				// Es un certificado normal 
				// se usa siempre la institucion
				colegiadoEnOrigen=true;
				usarIdInstitucion=true;
			} else if (beanPI.getTipoCertificado().equalsIgnoreCase("M")) {
				// Es un comunicado
				// Compruebo si está en origen. Si no lanzo mensaje e impido seguir.
				colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucionP, idSolicitud);
				// RGG cojo los datos de la institucion en cualquier caso.
				usarIdInstitucion=true;
			} else {
				// Es una diligencia
				boolean esDeConsejo=admCer.esConsejo(idInstitucionP);
				if (esDeConsejo) {
					// Se trata de CGAE
					// Compruebo si está en origen. Si no pregunto si desea continuar utilizando al cliente de CGAE.
					colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucionP, idSolicitud);
					// RGG siempre la institucion aunque exista el cliente en origen
					usarIdInstitucion=true;
					if (!colegiadoEnOrigen) {
						if (request.getParameter("paraConsejo")==null) {
			
							tx.rollback();
							
					        request.setAttribute("PREG_PDF_idInstitucion",idInstitucionX);
					        request.setAttribute("PREG_PDF_idInstitucionPresentador",idInstitucionPresentadorX);
					        request.setAttribute("PREG_PDF_idPersona",idPersonaX);
					        request.setAttribute("PREG_PDF_idBoton",idBoton);
					        request.setAttribute("PREG_PDF_idPantilla",idPlantilla);
					        request.setAttribute("PREG_PDF_existe",new Boolean(colegiadoEnOrigen).toString());
					        
							return "preguntaDireccionGeneraPDFPredefinido";

						} else {
							colegiadoEnOrigen=true;
							usarIdInstitucion=true;
						}
					}
				}
			}

	        
	        String sEstadoCertificado = null;

	        if (colegiadoEnOrigen) {
	        
	        	// proceso de generar 
				admPI.aprobarGenerarPredefinido(beanSolic,beanPI,usarIdInstitucion,idPlantilla);
				mensaje="messages.certificados.updated.success";

	        } else {
	            
	        	// NO ES colegiadoEnOrigen
		        ClsLogging.writeFileLog("Error al genera el certificado PDF: El cliente no es colegiadoEnOrigen, idpersona=" + idPersonaX,3);
	        	mensaje = "messages.error.solicitud.clienteEsColegiado";

	        }
			salida = exito(mensaje,request);
	        */
	        tx.commit();
	        request.setAttribute("BOT_mensaje","messages.certificados.updated.success");
	        request.setAttribute("BOT_idInstitucion",beanSolic.getIdInstitucion().toString());
	        request.setAttribute("BOT_idPeticionProducto",beanSolic.getIdPeticionProducto().toString());
	        request.setAttribute("BOT_concepto",beanSolic.getDescripcion());
	        request.setAttribute("BOT_idProducto",beanSolic.getPpn_IdProducto().toString());
	        request.setAttribute("BOT_idTipoProducto",beanSolic.getPpn_IdTipoProducto().toString());
	        request.setAttribute("BOT_idProductoInstitucion",beanSolic.getPpn_IdProductoInstitucion().toString());
	        
	        salida = "exitoHaciaCertificados";

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		} 
		return salida;
	}
	
	/** 
	 *  Funcion que atiende la accion modificar. VA CON ABRIRAVANZADA
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String salida = null;
		UserTransaction tx = null;
		String mensaje ="";
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idInstitucionPresentadorX=request.getParameter("idInstitucionPresentador");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			String metodoSolicitud=request.getParameter("metodoSolicitud");
			String fechaSolicitud=request.getParameter("fechaSolicitud");

			String idProductoCertificado=request.getParameter("idProductoCertificado");
			//String idPlantilla=request.getParameter("idPlantilla");

			PysProductosInstitucionAdm admPI = new PysProductosInstitucionAdm(this.getUserBean(request));
		    
		    AdmBotonAccionAdm admBot= new AdmBotonAccionAdm(this.getUserBean(request));
		    AdmBotonAccionBean beanBot = null;
			Vector v = admBot.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation());
			if (v!=null && v.size()>0) {
				beanBot = (AdmBotonAccionBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    
			
			StringTokenizer st = new StringTokenizer(idProductoCertificado,"_");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			try {
				if (st.hasMoreElements()) {
					idInstitucionP=(String)st.nextElement();
					idTipoProducto=(String)st.nextElement();
					idProducto=(String)st.nextElement();
					idProductoInstitucion=(String)st.nextElement();
				}
			} catch (Exception ee) {
				throw new SIGAException("certificados.boton.mensaje.malConfigurado");
			}
		    Hashtable htProducto = new Hashtable();
		    htProducto.put(PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucionP );
		    htProducto.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTO, idProducto);
		    htProducto.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		
		    Vector vDatos = admPI.select(htProducto);
		    PysProductosInstitucionBean beanPI = null;
		    if (vDatos!=null && vDatos.size()>0)
		    {
	            beanPI = (PysProductosInstitucionBean)vDatos.get(0);
	            if (beanPI.getTipoCertificado()==null || !beanPI.getTipoCertificado().equalsIgnoreCase("C"))
	            {
	            	throw new SIGAException("certificados.boton.mensaje.productoNoCertificado");
	            }
		    } else {
		    	throw new SIGAException("certificados.boton.mensaje.productoNoExiste");
		    }
	
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			String formaPago=null; 
			String tipoEnvio=null; 
			if (v2!=null && v2.size()>0) {
				beanVal=(AdmValorPreferenteBean)v2.get(0);
				if(beanPI.getnoFacturable()!=null && beanPI.getnoFacturable().trim().equals("1"))
					formaPago=null;
				else
					formaPago=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			} 
			
			PysFormaPagoProductoAdm admForm= new PysFormaPagoProductoAdm(this.getUserBean(request));
			PysFormaPagoProductoBean beanForm = null;
			Vector v3 = admForm.select(" WHERE IDINSTITUCION="+beanPI.getIdInstitucion()+" AND IDTIPOPRODUCTO="+beanPI.getIdTipoProducto()+" AND IDPRODUCTO="+beanPI.getIdProducto()+" AND IDPRODUCTOINSTITUCION="+beanPI.getIdProductoInstitucion());
			if (v2==null || v2.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.formaPagoMAL");
			} 
			
			
			
			v2 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v2!=null && v2.size()>0) {
				beanVal=(AdmValorPreferenteBean)v2.get(0);
				tipoEnvio=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}		

			
			tx = user.getTransaction(); 
			tx.begin();
			
			// proceso de compra de certificado
			Articulo a = admPI.realizarCompraPredefinida(new Integer(idInstitucionX),idInstitucionPresentadorX,new Integer(idTipoProducto),new Long(idProducto), new Long(idProductoInstitucion), new Long(idPersonaX),formaPago ,tipoEnvio,false, fechaSolicitud, metodoSolicitud);

			// Insertamos el certificado (esto antes lo hacia un trigger)
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
			// compruebo la persona del colegio origen
		    Vector v4 = admSolicitud.select(" WHERE IDINSTITUCION="+a.getIdInstitucion() +
		    					" AND PPN_IDTIPOPRODUCTO="+a.getIdTipo() +
								" AND PPN_IDPRODUCTO="+ a.getIdArticulo()+
								" AND PPN_IDPRODUCTOINSTITUCION="+ a.getIdArticuloInstitucion()+
								" AND IDPERSONA_DES="+idPersonaX +
								" AND IDPETICIONPRODUCTO="+a.getIdPeticion());
		    
		    CerSolicitudCertificadosBean beanSolic = null;
		    if (v4!=null && v4.size()>0) {
		    	beanSolic = (CerSolicitudCertificadosBean) v4.get(0);
		    } else {
		    	throw new ClsExceptions("No se encuentra la solicitud, es posible que el producto no fuera certificado.");
		    }
		    
	        tx.commit();
	        request.setAttribute("BOT_mensaje","messages.certificados.updated.success");
	        request.setAttribute("BOT_idInstitucion",beanSolic.getIdInstitucion().toString());
	        request.setAttribute("BOT_idPeticionProducto",beanSolic.getIdPeticionProducto().toString());
	        request.setAttribute("BOT_concepto",beanSolic.getDescripcion());
	        request.setAttribute("BOT_idProducto",beanSolic.getPpn_IdProducto().toString());
	        request.setAttribute("BOT_idTipoProducto",beanSolic.getPpn_IdTipoProducto().toString());
	        request.setAttribute("BOT_idProductoInstitucion",beanSolic.getPpn_IdProductoInstitucion().toString());

	        salida = "exitoHaciaCertificados";

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		} 
		return salida;
	}
	
	
	protected String insertarCompraColegio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String salida = null;
		UserTransaction tx = null;
		String mensaje ="";
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			String idInstitucionX=request.getParameter("idInstitucion");
			String idPersonaX=request.getParameter("idPersona");
			String idBoton=request.getParameter("idBoton");
			String idProductoCertificado = request.getParameter("idProductoCertificado");
			String metodoSolicitud=request.getParameter("metodoSolicitud");
			String fechaSolicitud=request.getParameter("fechaSolicitud");

			//El idProductoCertificado es el id del combo. En combo.properties esta configurado de esta manera
			//IDINSTITUCION||'_'||IDTIPOPRODUCTO||'_'||IDPRODUCTO||'_'||IDPRODUCTOINSTITUCION AS ID
			//por lo que sacamos los atos de ahi
			StringTokenizer st = new StringTokenizer(idProductoCertificado,"_");
			String idInstitucionP="";
			String idTipoProducto="";
			String idProducto="";
			String idProductoInstitucion="";
			
			if (st.hasMoreElements()) {
				idInstitucionP=(String)st.nextElement();
				idTipoProducto=(String)st.nextElement();
				idProducto=(String)st.nextElement();
				idProductoInstitucion=(String)st.nextElement();
			}
			
			//Buscamos la forma de pago preferente para este boton. Por analisis pone que sea siempre banco y que se 
			//ponga el numero de cuenta la del cargo abono(qu esiga el algoritomo para compra rapida de certificado) 
		    AdmValorPreferenteAdm admVal= new AdmValorPreferenteAdm(this.getUserBean(request));
		    AdmValorPreferenteBean beanVal = null;
			Vector v2 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='FORMA_PAGO'");
			String formaPago=null; 
			String tipoEnvio=null; 
			if (v2!=null && v2.size()>0) {
				beanVal=(AdmValorPreferenteBean)v2.get(0);
				formaPago=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			} 
			
			//Verificamos que la forma de pago configurada existe en la forma de pago del producto
			PysFormaPagoProductoAdm admForm= new PysFormaPagoProductoAdm(this.getUserBean(request));
			PysFormaPagoProductoBean beanForm = null;
			Vector v3 = admForm.select(" WHERE IDINSTITUCION="+idInstitucionP+" AND IDTIPOPRODUCTO="+idTipoProducto+" AND IDPRODUCTO="+idProducto+" AND IDPRODUCTOINSTITUCION="+idProductoInstitucion);
			if (v3==null || v3.size()==0) {
				throw new SIGAException("certificados.boton.mensaje.formaPagoMAL");
			} 
			
			
			
			Vector v1 = admVal.select(" WHERE IDBOTON="+idBoton+" AND IDINSTITUCION="+user.getLocation()+" AND CAMPO='TIPO_ENVIO'");
			if (v1!=null && v1.size()>0) {
				beanVal=(AdmValorPreferenteBean)v1.get(0);
				tipoEnvio=beanVal.getValor();
			} else {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			}		

			
			tx = user.getTransaction(); 
			tx.begin();
			
			// proceso de compra de certificado
			PysProductosInstitucionAdm admProductos = new PysProductosInstitucionAdm(this.getUserBean(request));
			
			Articulo a = admProductos.realizarCompraPredefinida(new Integer(idInstitucionX),idInstitucionX,new Integer(idTipoProducto),new Long(idProducto), new Long(idProductoInstitucion), new Long(idPersonaX),formaPago ,tipoEnvio,true, fechaSolicitud, metodoSolicitud);

			// Insertamos el certificado (esto antes lo hacia un trigger)
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));	    
		    // compruebo la persona del colegio origen
		    Vector v4 = admSolicitud.select(" WHERE IDINSTITUCION="+a.getIdInstitucion() +
		    					" AND PPN_IDTIPOPRODUCTO="+a.getIdTipo() +
								" AND PPN_IDPRODUCTO="+ a.getIdArticulo()+
								" AND PPN_IDPRODUCTOINSTITUCION="+ a.getIdArticuloInstitucion()+
								" AND IDPERSONA_DES="+idPersonaX +
								" AND IDPETICIONPRODUCTO="+a.getIdPeticion());
		    
		    CerSolicitudCertificadosBean beanSolic = null;
		    if (v4!=null && v4.size()>0) {
		    	beanSolic = (CerSolicitudCertificadosBean) v4.get(0);
		    } else {
		    	throw new ClsExceptions("No se encuentra la solicitud, es posible que el producto no fuera certificado.");
		    }
		    
		   
	        tx.commit();
	        request.setAttribute("BOT_mensaje","messages.certificados.updated.success");
	        request.setAttribute("BOT_idInstitucion",beanSolic.getIdInstitucion().toString());
	        request.setAttribute("BOT_idPeticionProducto",beanSolic.getIdPeticionProducto().toString());
	        request.setAttribute("BOT_concepto",beanSolic.getDescripcion());
	        request.setAttribute("BOT_idProducto",beanSolic.getPpn_IdProducto().toString());
	        request.setAttribute("BOT_idTipoProducto",beanSolic.getPpn_IdTipoProducto().toString());
	        request.setAttribute("BOT_idProductoInstitucion",beanSolic.getPpn_IdProductoInstitucion().toString());
	        
	        salida = "exitoHaciaCertificados";

		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		} 
		return salida;
	}
	
	
}
