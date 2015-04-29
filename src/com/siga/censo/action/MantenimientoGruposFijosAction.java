/*
 * Created on 01-sep-2006
 *
 */
package com.siga.censo.action;

/**
 * @author pilard
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenGruposFicheros;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.ExcelHelper;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.cen.CenClienteService;
import org.redabogacia.sigaservices.app.services.cen.CenGruposFicherosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.cen.CenGruposFicherosVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenGruposClienteAdm;
import com.siga.beans.CenGruposClienteBean;
import com.siga.beans.CenGruposClienteClienteAdm;
import com.siga.beans.CenGruposClienteClienteBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.censo.form.MantenimientoGruposFijosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoGruposFijosAction extends MasterAction {
	
	
	private static String COL_NIFCIF = "NIF/CIF";
	private static String COL_NUMCOLEGIADO = "N.COLEGIADO";
	private static String COL_IDGRUPO = "ID.GRUPO";
	private static String COL_RESULTADO = "RESULTADO";
	private static String COL_ACCION = "ALTA(A)/BAJA(B)";
	
	public static final List<String> CAMPOS_MODELO = Arrays.asList(COL_NIFCIF,COL_NUMCOLEGIADO,COL_IDGRUPO,COL_ACCION);
	public static final List<String> CAMPOS_LOG = Arrays.asList(COL_NIFCIF,COL_NUMCOLEGIADO,COL_IDGRUPO,COL_ACCION,COL_RESULTADO);
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					if ((accion!=null)&&(accion.equalsIgnoreCase("abrirAlvolver"))){
						mapDestino = abrirAlvolver(mapping, miForm, request, response);
					}else if ((accion!=null)&& (accion.equalsIgnoreCase("generarPlantilla"))){
						mapDestino = generarPlantillaExcel(mapping, miForm, request, response);
					}else if ((accion!=null)&& (accion.equalsIgnoreCase("procesarFichero"))){
						mapDestino = procesarFichero(mapping, miForm, request, response);
					}else if ((accion!=null)&&(accion.equalsIgnoreCase("download"))){
						mapDestino = download(mapping, miForm, request, response);
					}else{
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
	        /***** PAGINACION*****/
			HashMap databackup = new HashMap();
			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String) request.getParameter("pagina");

				if (pagina != null) {
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				databackup = new HashMap();
				Paginador resultado = null;
				Vector datos = null;
				CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(user);

				String nombre = miForm.getNombre();
				String idInstitucion = user.getLocation();
				Paginador ResulGruposFijos = gruposFijosAdm.busquedaGruposFijos(nombre, idInstitucion, user.getLanguage());
				databackup.put("paginador", ResulGruposFijos);
				if (ResulGruposFijos != null) {
					datos = ResulGruposFijos.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				}
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return "buscarPor";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		try {
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			String idInstitucionGrupos = "";
			String idGrupo = "";

			if (miForm.getDatosTablaOcultos(0) == null) {
				Hashtable hashGrupModif = (Hashtable) request.getSession().getAttribute("DATABACKUP");
				idInstitucionGrupos = (String) hashGrupModif.get("IDINSTITUCION");
				idGrupo = (String) hashGrupModif.get("IDGRUPO");

			} else {
				idInstitucionGrupos = (String) miForm.getDatosTablaOcultos(0).get(0);
				idGrupo = (String) miForm.getDatosTablaOcultos(0).get(1);
			}

			UsrBean user = this.getUserBean(request);
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(user);

			Vector vGruposFijos = gruposFijosAdm.busquedaGrupofijo(idInstitucionGrupos, idGrupo, user.getLanguage());

			if (vGruposFijos.size() == 1) {
				Hashtable hashGrupos = (Hashtable) vGruposFijos.get(0);
				miForm.setDatos(hashGrupos);
				request.getSession().setAttribute("DATABACKUP", hashGrupos);

				// Obtenemos los ficheros de carga masiva del grupo fijo (si
				// existen)
				BusinessManager bm = getBusinessManager();
				CenGruposFicherosService fichgrupserv = (CenGruposFicherosService) bm.getService(CenGruposFicherosService.class);
				CenGruposFicheros obj = new CenGruposFicheros();
				obj.setIdinstitucionGrupo(Short.parseShort(hashGrupos.get("IDINSTITUCION").toString()));
				obj.setIdgrupo(Short.parseShort(hashGrupos.get("IDGRUPO").toString()));
				List<CenGruposFicherosVo> fichGrupoList = fichgrupserv.getList(obj);

				request.setAttribute("ficherosRel", fichGrupoList);
			}

			request.setAttribute("modo", modo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		String modo = "editar";
		return this.obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		String modo = "ver";
		return this.obtenerDatos(modo,formulario,request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm (this.getUserBean(request));

			Hashtable hashGrupoOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashGrupoModificado = miForm.getDatos();

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			hashGrupoModificado.put(CenGruposClienteBean.C_IDINSTITUCION,user.getLocation());

			String idGrupo =  UtilidadesHash.getString(hashGrupoOriginal,CenGruposClienteBean.C_IDGRUPO);
			String idInstitucion =  UtilidadesHash.getString(hashGrupoOriginal,CenGruposClienteBean.C_IDINSTITUCION);
			String nombreOrig=UtilidadesHash.getString(hashGrupoOriginal,CenGruposClienteBean.C_NOMBRE);
			String nombreModif=UtilidadesHash.getString(hashGrupoModificado,CenGruposClienteBean.C_NOMBRE);
			
			String nombreTabla = CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = CenGruposClienteBean.C_NOMBRE;

			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), idGrupo.toString());
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(CenGruposClienteBean.C_IDGRUPO, idGrupo);
			Hashtable htSignos = new Hashtable();
			htSignos.put(CenGruposClienteBean.C_IDGRUPO, "<>");
			
			boolean isClaveUnica=true;
			//si alguno de los campos clave ha cambiado, comprobamos que no esté repetido
			if(!nombreOrig.equals(nombreModif)){
				isClaveUnica=UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,nombreModif,nombreCampoDescripcion,
						htPkTabl,htSignos,nombreTabla,4,user.getLanguage());
			}
			if(isClaveUnica){
				tx.begin();
				if(!nombreOrig.equals(nombreModif)){
					String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), idGrupo.toString());
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setDescripcion(nombreModif);
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	if(!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) { 
		        		throw new SIGAException ("messages.updated.error");
		        	}
				}
				
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado"); 
			}
		}
		catch (Exception e) { 
			e.printStackTrace();
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 

		request.setAttribute("modo","editar");
		return exitoRefresco("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "nuevo";
		try {
			request.setAttribute("modo", modo);
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			miForm.setNombre("");

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;

		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			CenGruposClienteBean beanGrupos = new CenGruposClienteBean();
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(this.getUserBean(request));

			String idInstitucion = user.getLocation();
			String nombre = miForm.getNombre();

			beanGrupos.setIdInstitucion(new Integer(idInstitucion));

			String nombreTabla = CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = CenGruposClienteBean.C_NOMBRE;

			Integer pkRecursoGrupo = gruposFijosAdm.getNuevoIdGrupo(user.getLocation());
			beanGrupos.setIdGrupo(pkRecursoGrupo);
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoGrupo.toString());
			if (idRecurso == null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			beanGrupos.setNombre(idRecurso.toString());

			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(CenGruposClienteBean.C_IDGRUPO, pkRecursoGrupo);
			Hashtable htSignos = new Hashtable();
			htSignos.put(CenGruposClienteBean.C_IDGRUPO, "<>");

			boolean isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion, nombre, nombreCampoDescripcion, htPkTabl, htSignos, nombreTabla, 4, user.getLanguage());

			if (isClaveUnicaMultiIdioma) {
				tx.begin();

				String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoGrupo.toString());
				GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
				GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
				recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
				recCatalogoBean.setDescripcion(nombre);
				recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
				recCatalogoBean.setIdRecurso(idRecurso);
				recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
				recCatalogoBean.setNombreTabla(nombreTabla);
				if (!admRecCatalogos.insert(recCatalogoBean, user.getLanguageInstitucion()))
					throw new SIGAException("messages.inserted.error");

				gruposFijosAdm.insert(beanGrupos);
				tx.commit();

			} else {
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado");
			}

			Hashtable<String, Object> hashGrupos = new Hashtable<String, Object>();
			hashGrupos.put(CenGruposClienteBean.C_IDGRUPO, beanGrupos.getIdGrupo().toString());
			hashGrupos.put(CenGruposClienteBean.C_IDINSTITUCION, beanGrupos.getIdInstitucion().toString());
			request.getSession().setAttribute("DATABACKUP", hashGrupos);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		request.setAttribute("modo", "editar");
		return exitoRefresco("messages.inserted.success", request);

	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;			
			tx = this.getUserBean(request).getTransaction();						
			String	idInstitucionGrupo = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idGrupo = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, CenGruposClienteBean.C_IDGRUPO, idGrupo);
			UtilidadesHash.set (claves, CenGruposClienteBean.C_IDINSTITUCION, idInstitucionGrupo);
			String nombreTabla =  CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion =  CenGruposClienteBean.C_NOMBRE;
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucionGrupo), idGrupo.toString());			
			
			//Comprobamos si tiene colegiados relacionados
			CenGruposClienteClienteAdm gruposClClAdm = new CenGruposClienteClienteAdm(this.getUserBean(request));
			Hashtable grupoClClHash=new Hashtable();
			grupoClClHash.put("IDINSTITUCION_GRUPO",idInstitucionGrupo);
			grupoClClHash.put("IDGRUPO",idGrupo);			
			Vector regGrupoPersonaV=gruposClClAdm.select(grupoClClHash);
			
			if((regGrupoPersonaV!=null)&&(regGrupoPersonaV.size()>0)){
				throw new SIGAException ("messages.gratuita.error.eliminarProcedimiento");
			
			}else{
				//Si tiene documentos relacionados los eliminamos
				BusinessManager bm = getBusinessManager();
				CenGruposFicherosService fichgrupserv= (CenGruposFicherosService) bm.getService(CenGruposFicherosService.class);
				CenGruposFicheros obj=new CenGruposFicheros();
				obj.setIdgrupo(Short.parseShort(idGrupo));
				obj.setIdinstitucionGrupo(Short.parseShort(idInstitucionGrupo));
				fichgrupserv.delete(obj);
				
				//Borramos los ficheros físicamente 
				ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			    String pathFicheros = rp.returnProperty("gen.ficheros.path");
			    StringBuffer directorioFichero = new StringBuffer(pathFicheros);
				directorioFichero.append(this.getUserBean(request).getLocation());
				directorioFichero.append(File.separator);
				directorioFichero.append(rp.returnProperty("cen.ficheros.grupos.fijos"));
				directorioFichero.append(File.separator);
				directorioFichero.append("ficherosCarga");
				directorioFichero.append(File.separator);
				directorioFichero.append(idGrupo); 
				StringBuffer pathFichero = new StringBuffer(directorioFichero);
				
				File path = new File(pathFichero.toString());
				File[] subpath = path.listFiles();
				if(subpath!=null){
					for (int i = 0; i < subpath.length; i++) {
						File[] fichList=subpath[i].listFiles();
						if(fichList!=null){
							for(int f = 0;f<fichList.length;f++){
								fichList[f].delete();
							}
						}
						subpath[i].delete();
					}
				}
				path.delete();
				
				tx.begin();

				if (idRecurso != null) {
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
		        		throw new SIGAException ("error.messages.deleted");
		        	}
				}
				gruposFijosAdm.delete(claves);
				tx.commit();
			}
		}
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
	protected String abrirAlvolver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "inicio";
	}
	

	private String generarPlantillaExcel(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			Vector datos = new Vector();
			File excelPlant = ExcelHelper.createExcelFile(CAMPOS_MODELO, datos);

			StringBuffer nombreFichero = new StringBuffer("PlantillaGruposFijos");
			nombreFichero.append(".xls");
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String rutaPlantilla = rp.returnProperty("cen.ficheros.grupos.fijos.temporal");
			File output = new File(rutaPlantilla + File.separator + nombreFichero.toString());
			excelPlant.renameTo(output);

			request.setAttribute("nombreFichero", output.getName());
			request.setAttribute("rutaFichero", output.getPath());
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK", "OK");
			request.setAttribute("accion", "");

		} catch (BusinessException e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}

		return "descargaFicheroGlobal";
	}
	
	private String procesarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario; 
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Hashtable datosGrupo=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			//Recuperamos el siguiente idfichero
			BusinessManager bm = getBusinessManager();
			CenGruposFicherosService fichgrupserv= (CenGruposFicherosService) bm.getService(CenGruposFicherosService.class);
			Long idFichSig=fichgrupserv.selectSigId(Short.parseShort(user.getLocation()));
			
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    String pathFicheros = rp.returnProperty("gen.ficheros.path");
		    StringBuffer directorioFichero = new StringBuffer(pathFicheros);
			directorioFichero.append(user.getLocation());
			directorioFichero.append(File.separator);
			directorioFichero.append(rp.returnProperty("cen.ficheros.grupos.fijos"));
			directorioFichero.append(File.separator);
			directorioFichero.append("ficherosCarga");
			StringBuffer pathFichero = new StringBuffer(directorioFichero);
			File path = new File(pathFichero.toString());
			if(!path.exists())
				path.mkdirs();
			
			//Guardamos el fichero subido en el directorio correspondiente
			StringBuffer nombreFichero = new StringBuffer(miForm.getFichero().getFileName().substring(0, miForm.getFichero().getFileName().lastIndexOf('.')));
			nombreFichero.append("_");
			nombreFichero.append(datosGrupo.get("IDGRUPO").toString());
			nombreFichero.append("_");
			nombreFichero.append(idFichSig.toString());
			nombreFichero.append(".xls");
			FileOutputStream fileGuardarOut = new FileOutputStream(pathFichero.toString()+File.separator+nombreFichero.toString());
			fileGuardarOut.write(miForm.getFichero().getFileData());
			fileGuardarOut.flush();
			fileGuardarOut.close();			
			
			//Creamos el fichero de log
			StringBuffer nombreFicheroLog = new StringBuffer("LOG_"+miForm.getFichero().getFileName().substring(0, miForm.getFichero().getFileName().lastIndexOf('.')));
			nombreFicheroLog.append("_");
			nombreFicheroLog.append(datosGrupo.get("IDGRUPO").toString());
			nombreFicheroLog.append("_");
			nombreFicheroLog.append(idFichSig.toString());
			nombreFicheroLog.append(".xls");
			File fileOut = new File(pathFichero.toString()+File.separator+nombreFicheroLog.toString());
			
			//Se guarda el registro de los ficheros del grupo en base de datos
			CenGruposFicheros obj=new CenGruposFicheros();
			obj.setIdinstitucion(Short.parseShort(datosGrupo.get("IDINSTITUCION").toString()));
			obj.setDirectorio(pathFichero.toString());
			obj.setIdgrupo(Short.parseShort(datosGrupo.get("IDGRUPO").toString()));
			obj.setIdinstitucionGrupo(Short.parseShort(datosGrupo.get("IDINSTITUCION").toString()));
			obj.setNombrefichero(nombreFichero.toString());
			obj.setNombreficherolog(nombreFicheroLog.toString());
			obj.setUsumodificacion(Integer.parseInt(user.getUserName()));
			obj.setFechamodificacion(new Date());
			fichgrupserv.insert(obj);
			
			Vector datos = ExcelHelper.parseExcelFile(miForm.getFichero().getFileData(),true);
			Vector datosLog=new Vector();

			// Procesamos los datos
			if (datos != null && datos.size() > 0) {
				for (int f = 0; f < datos.size(); f++) {
					Hashtable datosH = (Hashtable) datos.get(f);
					// creamos o eliminamos la relación de la persona con el grupo fijo
					String msg = this.procesarRelGrupoFijo(datosH, datosGrupo, user);
					// Añadimos la columna que informa de como ha ido todo
					datosH.put(COL_RESULTADO, msg);
					// Guardamos los datos del fichero log
					datosLog.add(datosH);
				}
			}

			File excelLog = ExcelHelper.createExcelFile(CAMPOS_LOG, datosLog);
			excelLog.renameTo(fileOut);
			request.setAttribute("modo","editar");

		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.censo"}, e, null);
		}
		
		return exitoRefresco("messages.inserted.success", request);
	}
	
	protected String procesarRelGrupoFijo(Hashtable datos,Hashtable datosGrupo, UsrBean user) {
		String msg="OK";
		String msgErr="KO: ";
		UserTransaction tx = null;
		Long idPersona = null;
		int accion=-1;
		try{

			/** 1. Comprobacion que se ha rellenado y existe el grupo **/
			if((datos.get(COL_IDGRUPO)==null) || ((String)datos.get(COL_IDGRUPO)).equals("")|| (!datosGrupo.get("IDGRUPO").toString().equals(datos.get(COL_IDGRUPO).toString()))){
				msgErr+=UtilidadesString.getMensajeIdioma(user,"censo.mantenimientoGruposFijos.error.fich.idGrupo");
				return msgErr;
			}
			
			/** 2. Comprobacion que se ha rellenado el NIF o NUM_COLEGIADO y existe el colegiado **/
			if ((datos.get(COL_NIFCIF) != null && !((String) datos.get(COL_NIFCIF)).equals("")) || (datos.get(COL_NUMCOLEGIADO) != null && !((String) datos.get(COL_NUMCOLEGIADO)).equals(""))) {
				try {
					CenClienteService cenClienteService = (CenClienteService) BusinessManager.getInstance().getService(CenClienteService.class);
					idPersona = cenClienteService.getIdPersona((String) datos.get(COL_NUMCOLEGIADO), (String) datos.get(COL_NIFCIF), Short.valueOf(user.getLocation()));
					if (idPersona == null) {
						throw new Exception();
					}
				} catch (Exception e) {
					msgErr += UtilidadesString.getMensajeIdioma(user, "censo.mantenimientoGruposFijos.error.fich.NIFCIF");
					return msgErr;
				}
				
			} else {
				msgErr += UtilidadesString.getMensajeIdioma(user, "censo.mantenimientoGruposFijos.error.fich.NIFCIF");
				return msgErr;
			}
			
			/** 3. Comprobacion que se ha rellenado la columna ACCION y se ha rellenado correctamente con A o B **/
			if (datos.get(COL_ACCION) == null || ((String) datos.get(COL_ACCION)).equals("") || (!((String) datos.get(COL_ACCION)).equals("A") && !((String) datos.get(COL_ACCION)).equals("B"))) {
				msgErr += UtilidadesString.getMensajeIdioma(user, "censo.mantenimientoGruposFijos.error.fich.accion");
				return msgErr;
			}			
			
			CenGruposClienteClienteAdm gruposClClAdm = new CenGruposClienteClienteAdm(user);
			Hashtable grupoClClHash=new Hashtable();
			grupoClClHash.put("IDINSTITUCION",user.getLocation());
			grupoClClHash.put("IDPERSONA",idPersona);
			grupoClClHash.put("IDINSTITUCION_GRUPO",datosGrupo.get("IDINSTITUCION").toString());
			grupoClClHash.put("IDGRUPO",datosGrupo.get("IDGRUPO").toString());
			
			/** 4. Comprobacion que la persona no está asignada ya al grupo  **/
			if (datos.get(COL_ACCION).toString().equalsIgnoreCase("A")) {
				Vector regGrupoPersonaV = gruposClClAdm.selectByPK(grupoClClHash);
				if (regGrupoPersonaV != null && regGrupoPersonaV.size() > 0) {
					msgErr += UtilidadesString.getMensajeIdioma(user, "process.usuario.ya.asignado");
					return msgErr;
				}
			}

			/** 5. Comprobacion que la persona existe en el grupo donde se quiere dar de baja  **/
			if (datos.get(COL_ACCION).toString().equalsIgnoreCase("B")) {
				Vector regGrupoPersonaV = gruposClClAdm.selectByPK(grupoClClHash);
				if (regGrupoPersonaV == null || regGrupoPersonaV.size() < 1) {
					msgErr += UtilidadesString.getMensajeIdioma(user, "process.usuario.noexiste");
					return msgErr;
				}
			}
			
			/** 6. Una vez superadas todas las comprobaciones iniciales, se procede a realizar la operacion en cada caso  **/
			tx = user.getTransaction();
			CenGruposClienteClienteBean bean = new CenGruposClienteClienteBean();
			bean.setIdGrupo(Integer.parseInt(datosGrupo.get("IDGRUPO").toString()));
			bean.setIdInstitucionGrupo(Integer.parseInt(datosGrupo.get("IDINSTITUCION").toString()));
			bean.setIdInstitucion(Integer.parseInt(user.getLocation()));
			bean.setIdPersona(idPersona);
			tx.begin();

			try {
				if (datos.get(COL_ACCION).toString().equalsIgnoreCase("A")) {
					accion = CenHistoricoAdm.ACCION_INSERT;
					gruposClClAdm.insert(bean);
				} else if (datos.get(COL_ACCION).toString().equalsIgnoreCase("B")) {
					accion = CenHistoricoAdm.ACCION_DELETE;
					gruposClClAdm.delete(bean);
				}

				try {
					CenHistoricoAdm admHist = new CenHistoricoAdm(user);
					admHist.insertCompleto((CenHistoricoBean) null, bean, accion, user.getUserName());

				} catch (Exception e) {
					if (Status.STATUS_ACTIVE == tx.getStatus()) {
						tx.rollback();
					}
					msgErr += UtilidadesString.getMensajeIdioma(user, "censo.mantenimientoGruposFijos.error.fich.historico");
					return msgErr;
				}

			} catch (Exception e) {
				if (Status.STATUS_ACTIVE == tx.getStatus()) {
					tx.rollback();
				}

				msgErr += UtilidadesString.getMensajeIdioma(user, "messages.inserted.error");
				return msgErr;
			}
			
			/** 7. Lanzamos el proceso de revision de suscripciones del letrado **/
			String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(user.getLocation(), idPersona.toString(), "", "" + user.getUserName());
			if ((resultado == null) || (!resultado[0].equals("0") && !resultado[0].equals("100"))) {
				if (Status.STATUS_ACTIVE == tx.getStatus()) {
					tx.rollback();
				}
				msgErr += UtilidadesString.getMensajeIdioma(user, "censo.mantenimientoGruposFijos.error.fich.revision.suscripcion") + resultado[1];
				return msgErr;
			}

			tx.commit();

		}catch (Exception e) { 
			try {
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			return msgErr+=e.getMessage();
		}

		return msg;
	}
	
	/**
	 * Se va a realizar la descarga del fichero (DOWNLOAD).
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.  
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String ficheroName = "";
		String sRutaFisicaJava = "";
		UsrBean user = null;
		try {
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			MantenimientoGruposFijosForm form = (MantenimientoGruposFijosForm) formulario;
			ficheroName = form.getNombrefichero();
			sRutaFisicaJava = form.getDirectorio();
			File fich = new File(sRutaFisicaJava + File.separator + ficheroName);
			if (fich == null || !fich.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}
			request.setAttribute("nombreFichero", fich.getName());
			request.setAttribute("rutaFichero", fich.getPath());
			request.setAttribute("borrarFichero", "false");
			request.setAttribute("generacionOK", "OK");
			request.setAttribute("accion", "");

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacion.previsionesFacturacion" }, e, null);
		}
		return "descargaFicheroGlobal";
	}
	
}