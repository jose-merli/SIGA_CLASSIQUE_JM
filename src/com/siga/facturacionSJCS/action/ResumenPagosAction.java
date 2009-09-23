package com.siga.facturacionSJCS.action;



import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.ResumenPagosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
;

/**
 * Clase action para la solicitud de Compra de Productos y Servicios .<br/>
 * Gestiona la abrir, borrar, solicitar, borrarCarrito, continuar, modificarCuenta, desglosePago, finalizarCompra, insertProducto y servicio  
 */
public class ResumenPagosAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
				// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);	
					
				}else if (accion.equalsIgnoreCase("actualizarCliente")){
					mapDestino = actualizarCliente(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("descargaFicheros")){
					mapDestino = descargaFicheros(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("onloadDescargaFicheros")){
					mapDestino = onloadDescargaFicheros(mapping, miForm, request, response);
				}else {
					return super.executeInternal(mapping, formulario, request, response);
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
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.productos"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}

	/** 
	 *  Funcion que atiende la accion abrir. Controla la primera vez que se entra en la funcionalidad.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{	
			UsrBean user = this.getUserBean(request);
			ResumenPagosForm form = (ResumenPagosForm) formulario;
			Integer idInstitucion=this.getIDInstitucion(request);
			Long idPersona 	= null;
			String nombre 	= "";
			String numero 	= "";	
			String nif 		= "";
			
			// Si el usuario es letrado buscamos sus datos sino no mostramos información poruq debe seleccionar un letrado
			if (user.isLetrado()){					
				idPersona = new Long (user.getIdPersona());
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean personaBean = personaAdm.getIdentificador(idPersona);
				
				// Existen datos de la persona
				if(personaBean != null){	
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
					CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucion);			
					numero 	= colegiadoAdm.getIdentificadorColegiado(bean);			
					nombre 	= personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
					nif 	= personaBean.getNIFCIF();
				}
				else {
					idPersona = null;
				}
			}
				

			request.setAttribute("idPersona", idPersona);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("nif", nif);
			request.setAttribute("ResumenPagosForm",form);
					
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e, null); 
		} 
		return "abrir";
	}
	
	private String descargaFicheros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String result = "error";
		UsrBean user = this.getUserBean(request);
		try {
			ResumenPagosForm miForm = (ResumenPagosForm) formulario;
			StringTokenizer st = new StringTokenizer(miForm.getFacturacion(),",");
			String [] valor = new String[2];
			int i=0;
			while(st.hasMoreTokens())
			{
				valor[i] = (String)st.nextElement();
				i++;
			}
			miForm.setFacturacion(valor[1]);
			miForm.setPago(miForm.getPago());
			Long idPersona = miForm.getIdPersona();
			miForm.setIdPersona(idPersona);
			Hashtable nombresFichero = UtilidadesFacturacionSJCS.getNombreFicherosPago(new Integer(user.getLocation()), new Integer(miForm.getFacturacion()), new Integer(miForm.getPago()), idPersona, user);
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(user);
			String pathFichero = paramAdm.getValor("" + user.getLocation(), "FCS", "PATH_PREVISIONES_BD", null);
			
			// Llamada a los pl's que exportan los ficheros de turnos de oficio, guardias, soj y ejg
			
			// TURNOS DE OFICIO
			String nombreFichero = UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_TURNO);
			String cabecera = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_TURNO, ClsConstants.PAGOS_SJCS,user);
			String resultadoPl[] = EjecucionPLs.ejecutarPLExportarTurnosOficio(user.getLocation(),miForm.getFacturacion(),miForm.getPago(), String.valueOf(idPersona), pathFichero, nombreFichero,cabecera,user.getLanguageInstitucion());
			if(!resultadoPl[0].equals("0"))
				throw new ClsExceptions("Error al ejecutar el pl PROC_FCS_EXPORTAR_TURNOS_OFI :"+ resultadoPl[1]);
			
			// GUARDIAS
			nombreFichero = UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_GUARDIA);
			cabecera = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_GUARDIA, ClsConstants.PAGOS_SJCS,user);
			String resultadoPl2[] = EjecucionPLs.ejecutarPLExportarGuardias(idPersona.toString(), user.getLocation(),miForm.getFacturacion(), miForm.getPago(), pathFichero, nombreFichero,cabecera, user.getLanguageInstitucion());
			if(!resultadoPl2[0].equals("0"))
				throw new ClsExceptions("Error al ejecutar el pl PROC_FCS_EXPORTAR_GUARDIAS :"+ resultadoPl2[1]);
			
			//SOJ
			nombreFichero = UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_SOJ);
			cabecera = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_SOJ, ClsConstants.PAGOS_SJCS,user);
			String resultadoPl3[] = EjecucionPLs.ejecutarPLExportarSoj(user.getLocation(),miForm.getFacturacion(),miForm.getPago(), String.valueOf(idPersona), pathFichero, nombreFichero,cabecera);
			if(!resultadoPl3[0].equals("0"))
				throw new ClsExceptions("Error al ejecutar el pl PROC_FCS_EXPORTAR_SOJ :"+ resultadoPl3[1]);
			
			//EJG
			nombreFichero = UtilidadesHash.getString(nombresFichero, "" + ClsConstants.HITO_GENERAL_EJG);
			cabecera = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_EJG, ClsConstants.PAGOS_SJCS,user);
			String resultadoPl4[] = EjecucionPLs.ejecutarPLExportarEjg(user.getLocation(),miForm.getFacturacion(),miForm.getPago(), String.valueOf(idPersona), pathFichero, nombreFichero,cabecera);
			if(!resultadoPl4[0].equals("0"))
				throw new ClsExceptions("Error al ejecutar el pl PROC_FCS_EXPORTAR_EJG :"+ resultadoPl4[1]);
			
			
			result = "onLoadDescargas";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return result;
	}
			
	private String onloadDescargaFicheros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String result = "error";		
		try {
			UsrBean user = this.getUserBean(request);
			ResumenPagosForm miForm = (ResumenPagosForm) formulario;
			request.setAttribute("idFacturacion",miForm.getFacturacion());
			request.setAttribute("idInstitucion",user.getLocation());
			request.setAttribute("idPersona",miForm.getIdPersona().toString());
			request.setAttribute("idPago",miForm.getPago().toString());
			result = "descargas";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return result;
	}
	
	
	/** 
	 * Método que atiende la accion actualizarCliente. Actualiza el Cliente con los datos del mismo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String actualizarCliente(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			request.setAttribute("existeCarro", "S");
			
			ResumenPagosForm form = (ResumenPagosForm) formulario;
			
			//Datos del Cliente seleccionado:
			request.setAttribute("idPersona", form.getIdPersona());
			request.setAttribute("nombrePersona", form.getNombrePersona());
			request.setAttribute("numero", form.getNumeroColegiado());
			request.setAttribute("nif", form.getNif());
			request.setAttribute("idInstitucion", this.getIDInstitucion(request));
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		// return modo;
		return "abrir"; 
	 }

	
	
	
	
	
	

	
	
	
	
	
	
	
}
