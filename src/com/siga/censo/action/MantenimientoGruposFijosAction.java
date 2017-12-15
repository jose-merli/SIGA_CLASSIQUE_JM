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

import com.atos.utils.ClsConstants;
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
	
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;
		try {

			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if (modo != null)
						accion = modo;
					if (accion != null && accion.equalsIgnoreCase("abrirAlvolver")) {
						mapDestino = abrirAlvolver(mapping, miForm, request, response);
					} else if (accion != null && accion.equalsIgnoreCase("download")) {
						mapDestino = download(mapping, miForm, request, response);
					} else {
						return super.executeInternal(mapping, formulario, request, response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
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

				String nombre = miForm.getBusquedaNombre();
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
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(this.getUserBean(request));

			Hashtable hashGrupoOriginal = (Hashtable) request.getSession().getAttribute("DATABACKUP");
			Hashtable hashGrupoModificado = miForm.getDatos();

			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			hashGrupoModificado.put(CenGruposClienteBean.C_IDINSTITUCION, user.getLocation());

			String idGrupo = UtilidadesHash.getString(hashGrupoOriginal, CenGruposClienteBean.C_IDGRUPO);
			String idInstitucion = UtilidadesHash.getString(hashGrupoOriginal, CenGruposClienteBean.C_IDINSTITUCION);
			String nombreOrig = UtilidadesHash.getString(hashGrupoOriginal, CenGruposClienteBean.C_NOMBRE);
			String nombreModif = UtilidadesHash.getString(hashGrupoModificado, CenGruposClienteBean.C_NOMBRE);

			String nombreTabla = CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = CenGruposClienteBean.C_NOMBRE;

			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), idGrupo.toString());
			if (idRecurso == null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");

			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(CenGruposClienteBean.C_IDGRUPO, idGrupo);
			Hashtable htSignos = new Hashtable();
			htSignos.put(CenGruposClienteBean.C_IDGRUPO, "<>");

			boolean isClaveUnica = true;
			// si alguno de los campos clave ha cambiado, comprobamos que no
			// esté repetido
			if (!nombreOrig.equals(nombreModif)) {
				isClaveUnica = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion, nombreModif, nombreCampoDescripcion, htPkTabl, htSignos, nombreTabla, 4, user.getLanguage());
			}
			if (isClaveUnica) {
				tx.begin();
				if (!nombreOrig.equals(nombreModif)) {
					String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), idGrupo.toString());
					GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
					GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean();
					recCatalogoBean.setDescripcion(nombreModif);
					recCatalogoBean.setIdRecurso(idRecurso);
					recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
					if (!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) {
						throw new SIGAException("messages.updated.error");
					}
				}
				
				tx.commit();
			} else {
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}

		request.setAttribute("modo", "editar");
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
		

		try {
			
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			CenGruposClienteBean beanGrupos = new CenGruposClienteBean();
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm(this.getUserBean(request));
			beanGrupos.setIdInstitucion(new Integer(user.getLocation()));
			beanGrupos.setNombre(miForm.getNombre());
			String crearListaCorreoGrupoFijo = "0";
			if(miForm.getCrearListaCorreoGrupoFijo()!=null && !miForm.getCrearListaCorreoGrupoFijo().equals(""))
				crearListaCorreoGrupoFijo = miForm.getCrearListaCorreoGrupoFijo();
			gruposFijosAdm.insertar(beanGrupos,crearListaCorreoGrupoFijo.equalsIgnoreCase(ClsConstants.DB_TRUE)); 
			Hashtable<String, Object> hashGrupos = new Hashtable<String, Object>();
			hashGrupos.put(CenGruposClienteBean.C_IDGRUPO, beanGrupos.getIdGrupo().toString());
			hashGrupos.put(CenGruposClienteBean.C_IDINSTITUCION, beanGrupos.getIdInstitucion().toString());
			request.getSession().setAttribute("DATABACKUP", hashGrupos);
			request.setAttribute("modo", "editar");
		}catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		
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
		request.setAttribute("buscarVolver", "buscarVolver");
		return "inicio";
	}
	
}