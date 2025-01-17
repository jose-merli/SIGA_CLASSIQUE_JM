package com.siga.censo.action;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenAnexosCuentasBancariasAdm;
import com.siga.beans.CenAnexosCuentasBancariasBean;
import com.siga.censo.form.AnexosCuentasBancariasForm;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.general.form.FicheroForm;
import com.siga.general.form.service.FicheroVoService;

import es.satec.businessManager.BusinessException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnexosCuentasBancariasAction extends MasterAction{

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
				
				if (accion.equalsIgnoreCase("editarFicheroAnexoCuentaBancaria") || accion.equalsIgnoreCase("verFicheroAnexoCuentaBancaria") || accion.equalsIgnoreCase("nuevoFicheroAnexoCuentaBancaria")){
					mapDestino = this.gestionarFicheroMandatoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("modificarFirmaAnexoCuentaBancaria")){
					mapDestino = this.modificarFirmaAnexoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("insertarFirmaAnexoCuentaBancaria")){
					mapDestino = this.insertarFirmaAnexoCuentaBancaria(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("borrarFirmaAnexoCuentaBancaria")){
					mapDestino = this.borrarFirmaAnexoCuentaBancaria(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("borrarFichero")){
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
	 * Gestion de ficheros de un mandato
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
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			
			// Transformo los datos del formulario en un bean
			CenAnexosCuentasBancariasBean beanAnexo = new CenAnexosCuentasBancariasBean(formAnexo);			
			
			// Si tiene anexo consulto sus datos, en otro caso es anexo nuevo
			if (formAnexo.getIdAnexo()!=null && !formAnexo.getIdAnexo().equals("")) {
				UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
				CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usuario);
				beanAnexo = anexosAdm.obtenerAnexo(beanAnexo);				
			}
			//pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", false);
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/CEN_MandatosCuentasBancarias.do",null,request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}finally{
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			}
			
			if(beanAnexo.getIdFicheroFirma()!=null && !beanAnexo.getIdFicheroFirma().equals("")){
				FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
				FicheroVo ficheroVo =  new FicheroVo();
				ficheroVo.setIdinstitucion(Short.valueOf(beanAnexo.getIdInstitucion()));
				ficheroVo.setIdfichero(Long.valueOf(beanAnexo.getIdFicheroFirma()));
				ficheroVo = ficherosService.getFichero(ficheroVo);
				VoUiService<FicheroForm, FicheroVo> voUiService = new FicheroVoService(); 
				FicheroForm ficheroForm = voUiService.getVo2Form(ficheroVo);
				formAnexo.setNombreArchivo(ficheroForm.getNombreArchivo());
				formAnexo.setIdFichero(ficheroForm.getIdFichero());
				formAnexo.setExtensionArchivo(ficheroForm.getExtensionArchivo());
				formAnexo.setFechaArchivo(ficheroForm.getFechaArchivo());
				formAnexo.setNombreArchivo(ficheroForm.getNombreArchivo());
				formAnexo.setDirectorioArchivo(ficheroForm.getDirectorioArchivo());
				formAnexo.setDescripcionArchivo(ficheroForm.getDescripcionArchivo());

				request.setAttribute("FicheroForm", formAnexo);
				
			}
			// Indico los datos devuelto en la request
			request.setAttribute("beanAnexo", beanAnexo);			
			request.setAttribute("modoFichero", formAnexo.getModo());
			if(formAnexo.getModo().equalsIgnoreCase("verFicheroAnexoCuentaBancaria"))
				request.setAttribute("accionModo", "ver");
			else
				request.setAttribute("accionModo", "editar");
			
				
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}							
		
		return "ficheroAnexoCuentaBancaria";		
	}		
	
	/**
	 * Modifica un anexo
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String modificarFirmaAnexoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Obtengo los datos del formulario
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			
			boolean bGestionarFichero = false;
			if (formAnexo.getTheFile()!=null && formAnexo.getTheFile().getFileData()!=null && formAnexo.getTheFile().getFileData().length>0){
				ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);			
				String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
				int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
				if (formAnexo.getTheFile().getFileSize() > maxSizebytes){				
					StringBuilder error = new StringBuilder();
					error.append(UtilidadesString.getMensajeIdioma(usuario,"messages.general.file.maxsize"));
					error.replace(error.indexOf("{"), error.indexOf("}")+1, maxsize);
					throw new SIGAException(error.toString());
				}	
				
				this.uploadFile(formAnexo, usuario);
				bGestionarFichero = true;
			}
			
			// Transformo los datos del formulario en un bean
			CenAnexosCuentasBancariasBean beanAnexo = new CenAnexosCuentasBancariasBean(formAnexo);
						
			tx = usuario.getTransaction();
			tx.begin();
			
			// Modifico los datos de la firma del mandato
			CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usuario);
			if (!anexosAdm.modificarFirmaAnexo(beanAnexo)) {
				return exito("messages.updated.error", request);
			}
			
			if (bGestionarFichero) {				
				anexosAdm.asociarFichero(formAnexo);
			}
			
			tx.commit();
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, tx);
		}
		
		return exitoModal("messages.updated.success", request);
	}
	
	/**
	 * Inserta un nuevo anexo
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String insertarFirmaAnexoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {			
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Obtengo los datos del formulario
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			
			boolean bGestionarFichero = false;
			if (formAnexo.getTheFile()!=null && formAnexo.getTheFile().getFileData()!=null && formAnexo.getTheFile().getFileData().length>0){
				ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
				int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
				if (formAnexo.getTheFile().getFileSize() > maxSizebytes){				
					StringBuilder error = new StringBuilder();
					error.append(UtilidadesString.getMensajeIdioma(usuario,"messages.general.file.maxsize"));
					error.replace(error.indexOf("{"), error.indexOf("}")+1, maxsize);
					throw new SIGAException(error.toString());
				}
				
				this.uploadFile(formAnexo, usuario);
				bGestionarFichero = true;
			}
			
			// Transformo los datos del formulario en un bean
			CenAnexosCuentasBancariasBean beanAnexo = new CenAnexosCuentasBancariasBean(formAnexo);
			
			tx = usuario.getTransaction();
			tx.begin();

			// Genero un nuevo anexos			
			CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usuario);
			anexosAdm.insertarFirmaAnexo(beanAnexo);

			if (bGestionarFichero) {
				formAnexo.setIdAnexo(beanAnexo.getIdAnexo());
				anexosAdm.asociarFichero(formAnexo);
			}
			
			tx.commit();
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, tx);
		}
		
		return exitoModal("messages.inserted.success", request);
	}
	
	/**
	 * Borrar un anexo
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String borrarFirmaAnexoCuentaBancaria(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {			
			// Obtengo los datos del formulario
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			String idFichero = formAnexo.getIdFichero();
			
			// Transformo los datos del formulario en un bean
			CenAnexosCuentasBancariasBean beanAnexo = new CenAnexosCuentasBancariasBean(formAnexo);
			
			// Elimino un anexo
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usuario);
			
			tx = usuario.getTransaction();
			tx.begin();
			
			// Borramos el registro
			if (!anexosAdm.borrarFirmaAnexo(beanAnexo)) {
				return exitoRefresco("messages.deleted.error", request);
			}		
			
			tx.commit();
			
			if (idFichero!=null && !idFichero.equals("")) {
				FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
				FicheroVo ficheroVo =  new FicheroVo();
				ficheroVo.setIdinstitucion(Short.valueOf(formAnexo.getIdInstitucion()));
				ficheroVo.setIdfichero(Long.valueOf(idFichero));
				ficheroVo = ficherosService.getFichero(ficheroVo);					
				
				//borramos el fichero fisico			
				ficherosService.delete(ficheroVo);
			}
		
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, tx);
		}
		
		return exitoRefresco("messages.deleted.success", request);
	}	
	
	private void uploadFile(AnexosCuentasBancariasForm formAnexosMandato, UsrBean usrBean ) throws BusinessException {
		FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
		
		FicheroVo ficheroVo =  new FicheroVo();
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFicheros = rp.returnProperty("gen.ficheros.path");
		String directorioFichero = this.getDirectorioFichero(formAnexosMandato.getIdPersona(),formAnexosMandato.getIdInstitucion(), pathFicheros);
		ficheroVo.setDirectorio(directorioFichero);
		ficheroVo.setDescripcion(UtilidadesString.getMensajeIdioma(usrBean, "fichero.mandatos.anexos.descripcion"));
		ficheroVo.setIdinstitucion(Short.valueOf(formAnexosMandato.getIdInstitucion()));
		try {
			ficheroVo.setFichero(formAnexosMandato.getTheFile().getFileData());
		} catch (Exception e) {
			throw new BusinessException(e.toString());
		}
		
		if(formAnexosMandato.getTheFile().getFileName().lastIndexOf(".")!=-1)
			ficheroVo.setExtension(formAnexosMandato.getTheFile().getFileName().substring(formAnexosMandato.getTheFile().getFileName().lastIndexOf(".")+1));
		
		ficheroVo.setUsumodificacion(Integer.valueOf(usrBean.getUserName()));
		ficheroVo.setFechamodificacion(new Date());
		ficherosService.insert(ficheroVo);
		SIGAServicesHelper.uploadFichero(ficheroVo.getDirectorio(),ficheroVo.getNombre(),ficheroVo.getFichero());
		formAnexosMandato.setIdFichero(ficheroVo.getIdfichero().toString());
   	}
	
	private String getDirectorioFichero(String idPersona, String idInstitucion, String pathFicheros) {
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
	
	private String downloadFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String pathFicheros = rp.returnProperty("gen.ficheros.path");
			String directorioFichero = this.getDirectorioFichero(formAnexo.getIdPersona(), formAnexo.getIdInstitucion(), pathFicheros);
			FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
			
			FicheroVo ficheroVo =  new FicheroVo();
			ficheroVo.setIdinstitucion(Short.valueOf(formAnexo.getIdInstitucion()));
			ficheroVo.setIdfichero(Long.valueOf(formAnexo.getIdFichero()));
			ficheroVo = ficherosService.getFichero(ficheroVo);
			
			StringBuffer pathFichero = new StringBuffer(directorioFichero);
			pathFichero.append(File.separator);
			pathFichero.append(formAnexo.getIdInstitucion());
			pathFichero.append("_");
			pathFichero.append(formAnexo.getIdFichero());
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
	
	private String borrarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {
			UsrBean usuario = (UsrBean) request.getSession().getAttribute("USRBEAN");
			AnexosCuentasBancariasForm formAnexo = (AnexosCuentasBancariasForm) formulario;
			String idFichero = formAnexo.getIdFichero();
			
			tx = usuario.getTransaction();
			tx.begin();
			
			//borramos la referencia al fichero de la tabla cen_mandatos
			CenAnexosCuentasBancariasAdm anexosAdm = new CenAnexosCuentasBancariasAdm(usuario);
			formAnexo.setIdFichero("");
			anexosAdm.asociarFichero(formAnexo);
			
			tx.commit();
			
			FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
			FicheroVo ficheroVo =  new FicheroVo();
			ficheroVo.setIdinstitucion(Short.valueOf(formAnexo.getIdInstitucion()));
			ficheroVo.setIdfichero(Long.valueOf(idFichero));
			ficheroVo = ficherosService.getFichero(ficheroVo);					
			
			//borramos el fichero ficsico	
			ficherosService.delete(ficheroVo);			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}

		return exitoModal("messages.deleted.success", request);
	}
}			