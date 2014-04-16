package com.siga.censo.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.GenFichero;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.services.scs.DocumentacionEjgService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo;
import org.redabogacia.sigaservices.app.vo.services.VoDbService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.CenAnexosCuentasBancariasAdm;
import com.siga.beans.CenAnexosCuentasBancariasBean;
import com.siga.beans.CenMandatosCuentasBancariasAdm;
import com.siga.beans.CenMandatosCuentasBancariasBean;
import com.siga.censo.form.MandatosCuentasBancariasForm;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.general.form.FicheroForm;
import com.siga.general.form.service.FicheroVoService;
import com.siga.gratuita.form.DefinirDocumentacionEJGForm;
import com.siga.gratuita.form.service.DocumentacionEjgVoService;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;

public class MandatosCuentasBancariasAction extends MasterAction{

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
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
				
				if (accion.equalsIgnoreCase("gestionarMandatoCuentaBancaria")){
					mapDestino = this.gestionarMandatoCuentaBancaria(mapping, miForm, request, response);					
					
				} else if (accion.equalsIgnoreCase("mandatoCuentaBancaria")){
					mapDestino = this.mandatoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("modificarMandatoCuentaBancaria")){
					mapDestino = this.modificarMandatoCuentaBancaria(mapping, miForm, request, response);					
					
				} else if (accion.equalsIgnoreCase("ficherosMandatoCuentaBancaria")){
					mapDestino = this.ficherosMandatoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("editarFicheroMandatoCuentaBancaria") || accion.equalsIgnoreCase("verFicheroMandatoCuentaBancaria")){
					mapDestino = this.gestionarFicheroMandatoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("modificarFirmaMandatoCuentaBancaria")){
					mapDestino = this.modificarFirmaMandatoCuentaBancaria(mapping, miForm, request, response);															
					
				} else if (accion.equalsIgnoreCase("borrarFichero")){
					mapDestino = this.borrarFichero(mapping, miForm, request, response);															
					
				} else if (accion.equalsIgnoreCase("downloadFichero")){
					mapDestino = this.downloadFichero(mapping, miForm, request, response);															
					
				} else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 							 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
			
		} catch (SIGAException es) {
			throw es;
			
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
	}	
	
	/**
	 * Gestion de mandatos de una cuenta bancaria
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String gestionarMandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String retorno = "gestionarMandatoCuentaBancaria";			
		try {
			// Obtengo los datos del formulario
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;

			// Cargo los datos en un hash para el tratamiento con las pestanas
			Hashtable<String, String> hashMandato = new Hashtable<String, String>();														
			hashMandato.put("idInstitucion", formMandato.getIdInstitucion());
			hashMandato.put("idPersona", formMandato.getIdPersona());
			hashMandato.put("idCuenta", formMandato.getIdCuenta());			
			hashMandato.put("idMandato", formMandato.getIdMandato());
			hashMandato.put("nombrePersona", formMandato.getNombrePersona());
			hashMandato.put("numero", formMandato.getNumero());
			hashMandato.put("modoMandato", formMandato.getModoMandato());
			
			// Indico los datos devuelto en la request
			request.setAttribute("hashMandato", hashMandato);
			request.setAttribute("modosMandato", formMandato.getModosMandato());	
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return retorno;		
	}			
	
	/**
	 * Obtener los datos de un mandato
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String mandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String retorno = "mandatoCuentaBancaria";			
		try {					
			// Recupero los parametros de la request
			String idInstitucion = (String) request.getParameter("idInstitucion");
			String idPersona = (String) request.getParameter("idPersona");
			String idCuenta = (String) request.getParameter("idCuenta");
			String idMandato = (String) request.getParameter("idMandato");
			
			// Compruebo que tenga todos los datos necesarios para consulta un mandato
			if (idInstitucion!=null && !idInstitucion.equals("") && 
				idPersona!=null && !idPersona.equals("") &&
				idCuenta!=null && !idCuenta.equals("") &&
				idMandato!=null && !idMandato.equals("")) { 
				
				// Transformo los datos del formulario en un bean
				CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean();
				beanMandato.setIdInstitucion(idInstitucion);
				beanMandato.setIdPersona(idPersona);
				beanMandato.setIdCuenta(idCuenta);
				beanMandato.setIdMandato(idMandato);
			
				// Consulto los datos del mandato
				UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
				CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usuario);
				beanMandato = mandatosAdm.obtenerMandato(beanMandato); 
				
				// Indico los datos devuelto en la request
				request.setAttribute("beanMandato", beanMandato);	
			}
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return retorno;		
	}		
	
	/**
	 * Modifica los datos de un mandato
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String modificarMandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {			
			// Obtengo los datos del formulario
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			
			// Relleno el bean con los datos del formulario
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean(formMandato);
			
			// Consulta que no exista la referencia del mandato de SEPA para la institucion
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usuario);
			if (mandatosAdm.consultarMandatoSEPA(beanMandato)) {
				return exito("censo.fichaCliente.bancos.mandatos.error.idMandatoExiste", request);
			}	
			
			// Modifico los datos del mandato
			if (!mandatosAdm.modificarMandato(beanMandato)) {
				return exito("messages.updated.error", request);
			}	
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return exito("messages.updated.success", request);
	}			
	
	/**
	 * Obtiene los datos de los ficheros de mandato y anexos
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String ficherosMandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {			
			// Obtengo los datos del formulario
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			
			// Relleno el bean con los datos del formulario
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean(formMandato);
			
			// Obtiene los datos de los ficheros de mandato y anexos
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenAnexosCuentasBancariasAdm AnexosAdm = new CenAnexosCuentasBancariasAdm(usuario);		
			Vector<CenAnexosCuentasBancariasBean> vListadoAnexos = AnexosAdm.obtenerAnexos(beanMandato);
			
			// Indico los datos devuelto en la request
			request.setAttribute("vListadoAnexos", vListadoAnexos);
			request.setAttribute("beanMandato", beanMandato);
				
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}							
		
		return "ficherosMandatoCuentaBancaria";		
	}	
	
	/**
	 * Gestion de los datos de la firma de un mandato
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String gestionarFicheroMandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			// Obtengo los datos del formulario
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			
			// Relleno el bean con los datos del formulario
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean(formMandato);			
			
			// Obtengo los datos del mandato
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usuario);
			beanMandato = mandatosAdm.obtenerMandato(beanMandato);
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/CEN_MandatosCuentasBancarias.do",null,request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}finally{
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			}
			if(beanMandato.getIdFicheroFirma()!=null && !beanMandato.getIdFicheroFirma().equals("")){
				FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
				
				FicheroVo ficheroVo =  new FicheroVo();
				ficheroVo.setIdinstitucion(Short.valueOf(beanMandato.getIdInstitucion()));
				ficheroVo.setIdfichero(Long.valueOf(beanMandato.getIdFicheroFirma()));
				ficheroVo = ficherosService.getFichero(ficheroVo);
				VoUiService<FicheroForm, FicheroVo> voUiService = new FicheroVoService(); 
				FicheroForm ficheroForm = voUiService.getVo2Form(ficheroVo);
				formMandato.setNombreArchivo(ficheroForm.getNombreArchivo());
				formMandato.setIdFichero(ficheroForm.getIdFichero());
				formMandato.setExtensionArchivo(ficheroForm.getExtensionArchivo());
				formMandato.setFechaArchivo(ficheroForm.getFechaArchivo());
				formMandato.setNombreArchivo(ficheroForm.getNombreArchivo());
				formMandato.setDirectorioArchivo(ficheroForm.getDirectorioArchivo());
				formMandato.setDescripcionArchivo(ficheroForm.getDescripcionArchivo());

				request.setAttribute("FicheroForm", formMandato);
				
			}
			
			// Indico los datos devuelto en la request
			request.setAttribute("beanMandato", beanMandato);	
			
			
			 if(formMandato.getModo().equalsIgnoreCase("verFicheroMandatoCuentaBancaria"))
				 request.setAttribute("accionModo", "ver");
			 else
				request.setAttribute("accionModo", "editar");
			
			
			 request.setAttribute("modoFichero", formMandato.getModo());
				
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}							
		
		return "ficheroMandatoCuentaBancaria";		
	}		
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String modificarFirmaMandatoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {		
			// Obtengo los datos del formulario
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_bytes.getValor());
			if(formMandato.getTheFile()!=null && formMandato.getTheFile().getFileSize()>Integer.parseInt(maxsize)){
				throw new SIGAException("messages.general.file.maxsize",new String[] { maxsize });
			}
			// Relleno el bean con los datos del formulario
			CenMandatosCuentasBancariasBean beanMandato = new CenMandatosCuentasBancariasBean(formMandato);
			
			// Modifico los datos de la firma del mandato
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usuario);
			if (!mandatosAdm.modificarFirmaMandato(beanMandato)) {
				return exito("messages.updated.error", request);
			}else{
				if(formMandato.getTheFile()!=null && formMandato.getTheFile().getFileData()!=null && formMandato.getTheFile().getFileData().length>0){
					uploadFile(formMandato, usuario);
					mandatosAdm.asociarFichero(formMandato);
				}
			}
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		
		return exitoModal("messages.updated.success", request);
	}		
	
	public void uploadFile(MandatosCuentasBancariasForm formMandato, UsrBean usrBean ) throws BusinessException
   	{
		FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
		
		FicheroVo ficheroVo =  new FicheroVo();
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFicheros = rp.returnProperty("gen.ficheros.path");
		String directorioFichero = getDirectorioFichero(formMandato.getIdPersona(),formMandato.getIdInstitucion(), pathFicheros);
		ficheroVo.setDirectorio(directorioFichero);
		ficheroVo.setDescripcion(UtilidadesString.getMensajeIdioma(usrBean, "fichero.mandatos.descripcion"));
		ficheroVo.setIdinstitucion(Short.valueOf(formMandato.getIdInstitucion()));
		try {
			ficheroVo.setFichero(formMandato.getTheFile().getFileData());
		} catch (Exception e) {
			throw new BusinessException(e.toString());
		}
		if(formMandato.getTheFile().getFileName().lastIndexOf(".")!=-1)
			ficheroVo.setExtension(formMandato.getTheFile().getFileName().substring(formMandato.getTheFile().getFileName().lastIndexOf(".")+1));
		
		ficheroVo.setUsumodificacion(Integer.valueOf(usrBean.getUserName()));
		ficheroVo.setFechamodificacion(new Date());
		ficherosService.insert(ficheroVo);
		formMandato.setIdFichero(ficheroVo.getIdfichero().toString());
	
   	}
	private String  getDirectorioFichero(String idPersona,String idInstitucion , String pathFicheros){
		StringBuffer directorioFichero = new StringBuffer(pathFicheros);
		directorioFichero.append(idInstitucion);
		directorioFichero.append(File.separator);
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String directorio = rp.returnProperty("fac.ficheros.mandatos");
		directorioFichero.append(directorio);
		directorioFichero.append(File.separator);
		directorioFichero.append(idPersona);
		
		return directorioFichero.toString();
	}
	
	protected String downloadFichero(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String pathFicheros = rp.returnProperty("gen.ficheros.path");
			String directorioFichero = getDirectorioFichero(formMandato.getIdPersona(),formMandato.getIdInstitucion(), pathFicheros);
			FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
			
			FicheroVo ficheroVo =  new FicheroVo();
			ficheroVo.setIdinstitucion(Short.valueOf(formMandato.getIdInstitucion()));
			ficheroVo.setIdfichero(Long.valueOf(formMandato.getIdFichero()));
			ficheroVo = ficherosService.getFichero(ficheroVo);
			
			StringBuffer pathFichero = new StringBuffer(directorioFichero);
			pathFichero.append(File.separator);
			pathFichero.append(formMandato.getIdInstitucion());
			pathFichero.append("_");
			pathFichero.append(formMandato.getIdFichero());
			pathFichero.append(".");
			pathFichero.append(ficheroVo.getExtension());
			File file = new File(pathFichero.toString());
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getPath());
			request.setAttribute("accion", "");
			

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}

		return forward;
		
		
	}
	
	protected String borrarFichero(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			MandatosCuentasBancariasForm formMandato = (MandatosCuentasBancariasForm) formulario;
			FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
			FicheroVo ficheroVo =  new FicheroVo();
			ficheroVo.setIdinstitucion(Short.valueOf(formMandato.getIdInstitucion()));
			ficheroVo.setIdfichero(Long.valueOf(formMandato.getIdFichero()));
			ficheroVo = ficherosService.getFichero(ficheroVo);
			formMandato.setIdFichero("");
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usr);
			//borramos la referencia al fichero de la tabla cen_mandatos
			mandatosAdm.asociarFichero(formMandato);
			//borramos el fichero ficsico y el registyo de BBDD
			ficherosService.delete(ficheroVo);
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.deleted.success", request);
	}

	
	
}