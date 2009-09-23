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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;


import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenGruposClienteAdm;
import com.siga.beans.CenGruposClienteBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.censo.form.MantenimientoGruposFijosForm;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoGruposFijosAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			
	        /***** PAGINACION*****/
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
		        Paginador resultado = null;
				Vector datos = null;
				CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm (user);
				
				String nombre = miForm.getNombre();
				String idInstitucion = user.getLocation();
				Paginador ResulGruposFijos = gruposFijosAdm.busquedaGruposFijos(nombre,idInstitucion,user.getLanguage());
				databackup.put("paginador",ResulGruposFijos);
				if (ResulGruposFijos!=null){ 
				   datos = ResulGruposFijos.obtenerPagina(1);
				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADOR",databackup);
				}   
				
				
				
		   }	
		
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 
		return "buscarPor";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		
		try {
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			
			String	idInstitucionGrupos = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idGrupo = (String)miForm.getDatosTablaOcultos(0).get(1);
			UsrBean user= this.getUserBean(request);
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm (user);
			
			Vector vGruposFijos = gruposFijosAdm.busquedaGrupofijo(idInstitucionGrupos, idGrupo,user.getLanguage());
			
			if (vGruposFijos.size() == 1) {
				Hashtable hashGrupos = (Hashtable) vGruposFijos.get(0);
				miForm.setDatos(hashGrupos);
				request.getSession().setAttribute("DATABACKUP", hashGrupos);
				
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		return this.obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
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
			String idInstitucion =  user.getLocation();
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
				
				//gruposFijosAdm.update(hashGrupoModificado, hashGrupoOriginal);
				
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado"); 
			}
		}
		catch (Exception e) { 
			e.printStackTrace();
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		//request.setAttribute("sinrefresco", "1");// De momento no queremos que refresque la consulta porque al haber añadido la paginacion, si estabamos en la pagina 2, al refrescar volvemos a la 1
											     // y queremos que nos siga manteniendo donde estabamos. En un futuro se arreglara esto (que refresque la consulta y permanezca en la pagina de la que 
											     // viniamos.
		return exitoModal("messages.updated.success", request);
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
			
			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoGruposFijosForm miForm = (MantenimientoGruposFijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			CenGruposClienteBean beanGrupos = new CenGruposClienteBean();
			CenGruposClienteAdm gruposFijosAdm = new CenGruposClienteAdm (this.getUserBean(request));
			
			String idInstitucion=user.getLocation();
			String nombre=miForm.getNombre();
			
			beanGrupos.setIdInstitucion(new Integer(idInstitucion));
			
			String nombreTabla = CenGruposClienteBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = CenGruposClienteBean.C_NOMBRE;
			
			Integer pkRecursoGrupo =  gruposFijosAdm.getNuevoIdGrupo(user.getLocation());
			beanGrupos.setIdGrupo(pkRecursoGrupo);
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoGrupo.toString());
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			beanGrupos.setNombre(idRecurso.toString());

			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(CenGruposClienteBean.C_IDGRUPO, pkRecursoGrupo);
			Hashtable htSignos = new Hashtable();
			htSignos.put(CenGruposClienteBean.C_IDGRUPO, "<>");
			
			
			boolean isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,nombre,nombreCampoDescripcion,
					htPkTabl,htSignos,nombreTabla,4,user.getLanguage());
			
			
			if(isClaveUnicaMultiIdioma){
				tx.begin();
				
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), pkRecursoGrupo.toString());
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
	        	recCatalogoBean.setDescripcion(nombre);
		        recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(nombreTabla);
	        	if(!admRecCatalogos.insert(recCatalogoBean, user.getLanguageInstitucion())) 
	        		throw new SIGAException ("messages.inserted.error");
        	
	        	
	        	
				
				gruposFijosAdm.insert(beanGrupos);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.grupoFijoDuplicado"); 
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
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
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
}