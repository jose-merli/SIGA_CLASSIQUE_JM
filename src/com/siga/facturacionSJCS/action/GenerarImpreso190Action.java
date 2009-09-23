package com.siga.facturacionSJCS.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenerarImpreso190Adm;
import com.siga.beans.GenerarImpreso190Bean;
import com.siga.facturacionSJCS.form.GenerarImpreso190Form;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* Clase action del caso de uso GENERAR IMPRESO 190
* @author AtosOrigin 05-04-2005
*/
public class GenerarImpreso190Action extends MasterAction {

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
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("generar")){
						// abrirAvanzadaConParametros
						mapDestino = generar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("download")){
						// abrirAvanzadaConParametros
						mapDestino = download(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("mostrarBoton")){
						// abrirAvanzadaConParametros
						mapDestino = mostrarBoton(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("modificarDatos")){
						// abrirAvanzadaConParametros
						mapDestino = modificarCabeceraInforme(mapping, miForm, request, response);	
					/*} else if (accion.equalsIgnoreCase("insertarDatos")){
						// abrirAvanzadaConParametros
						mapDestino = insertarCabeceraInforme(mapping, miForm, request, response);*/	
					} else {
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacionSJCS"});
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
		String salida = null;
		try {
			GenerarImpreso190Adm impreso190Adm=new GenerarImpreso190Adm(this.getUserBean(request));

			// borro el formulario en session de Avanzada
			//GenerarImpreso190Form miform = (GenerarImpreso190Form)request.getAttribute("generarImpreso190Form");
			
			// calculo los anhos
			Date fecha = new Date();
			int anio = new Integer(GstDate.getYear(fecha)).intValue();
			request.setAttribute("FcsAnioActual",new Integer(anio).toString());
			
			ArrayList anios = new ArrayList();
			anio = anio - 10;
			for (int i=0;i<12;i++) {
				anios.add(new Integer(anio).toString());
				anio++;
			}
			
			Hashtable claveHash=new Hashtable();
			claveHash.put(GenerarImpreso190Bean.C_IDINSTITUCION,this.getUserBean(request).getLocation());
			Vector vCabeceraInforme=impreso190Adm.selectByPK(claveHash);
			
			request.setAttribute("FcsAniosFacturacion",anios);
			request.setAttribute("vCabeceraInforme",vCabeceraInforme);
			
			salida = "inicio";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		// COMUN
		return salida;
	}

	/**
	 * Metodo que implementa el modo generar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String generar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		String salida = null;
		String sNombreFichero = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// borro el formulario en session de Avanzada
			GenerarImpreso190Form miform = (GenerarImpreso190Form)request.getAttribute("generarImpreso190Form");
			
			// realizo la generación
	    	sNombreFichero = miform.getNombreFicheroOriginal();
	    	String sDirectorio = getPathTemporal(user);
    		
	    	// creo el directorio si no existe
	    	File camino = new File (sDirectorio + File.separator + user.getLocation());
    		camino.mkdirs();

	    	String sNombreCompletoFichero=sDirectorio + File.separator + user.getLocation() + File.separator + sNombreFichero;
			File fichero = new File(sNombreCompletoFichero);
			
			FcsFacturacionJGAdm admFac = new FcsFacturacionJGAdm(this.getUserBean(request));
			try {
				request.removeAttribute("mensaje");
				fichero = admFac.generarImpreso190(miform, user.getLocation());
				miform.setNombreFichero(fichero.getName());
				
				//Controlo si se ha generado un zip por tener errores:
				if (fichero.getName().indexOf(".zip")!=-1) {
					request.setAttribute("mensaje", "messages.error.log190");
					request.setAttribute("logError", "SI");
				} else
					request.setAttribute("logError", "NO");
			} catch (SIGAException se) {
				request.setAttribute("mensaje",se.getLiteral(this.getLenguaje(request)));
			}		
			
			salida = "exitoImpreso190";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		// COMUN
		return salida;
	}

	/**
	 * Metodo que implementa el modo download
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String download (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		File fichero = null;
		String sNombreFichero = "";
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// borro el formulario en session de Avanzada
			GenerarImpreso190Form miform = (GenerarImpreso190Form)formulario;//request.getAttribute("generarImpreso190Form");
			
			// realizo la generación
	    	sNombreFichero = miform.getNombreFichero();
	    	String sDirectorio = getPathTemporal(user);
			String sNombreCompletoFichero=sDirectorio + File.separator + user.getLocation() + File.separator + sNombreFichero;
			fichero = new File(sNombreCompletoFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		return "descargaFichero";		
	}

	/**
	 * Metodo que implementa el modo mostrarBoton
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String mostrarBoton (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		String salida = null;
		try {
			String logError = request.getParameter("logError");
			if (logError!=null)
				request.setAttribute("logError", logError);
			else
				request.setAttribute("logError", "NO");
			
			String m = request.getParameter("mensaje"); 
			if (m!=null) {
				request.setAttribute("mensaje",m);
			}
			salida = "generado";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		// COMUN
		return salida;
	}
	/**
	 * Metodo que implementa la grabacion de los datos de cabecera del informe190
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificarCabeceraInforme (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions
    {
		String salida = null;
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			GenerarImpreso190Form miform = (GenerarImpreso190Form)formulario;
			GenerarImpreso190Adm impresoAdm=new GenerarImpreso190Adm(this.getUserBean(request));
			Hashtable hashConsulta=new Hashtable();
			hashConsulta.put(GenerarImpreso190Bean.C_IDINSTITUCION,user.getLocation());
			Vector vCabecInforme=impresoAdm.selectByPK(hashConsulta);
			
			Hashtable hash=new Hashtable();
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_ANIO,miform.getAnio());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_APELLIDO1,miform.getApellido1Contacto());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_APELLIDO2,miform.getApellido2Contacto());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_NOMBREFICHERO,miform.getNombreFicheroOriginal());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_IDPROVINCIA,miform.getCodigoProvincia());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_NOMBRE,miform.getNombreContacto());
			UtilidadesHash.set(hash,GenerarImpreso190Bean.C_TELEFONO,miform.getTelefonoContacto());
			
			if (vCabecInforme!=null && vCabecInforme.size()>0){
				impresoAdm.updateDirect(hash,impresoAdm.getClavesBean(), impresoAdm.getCamposBean());
			}else{	
				impresoAdm.insert(hash);
			}
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}


		return exitoRefresco("messages.updated.success", request);
    }

		private String getPathTemporal(UsrBean usr) throws ClsExceptions {
		GenParametrosAdm admParam = new GenParametrosAdm(usr);
		String valor = admParam.getValor(usr.getLocation(),"FCS",ClsConstants.PATH_IMPRESO190,null);
		return valor;
	}
}
