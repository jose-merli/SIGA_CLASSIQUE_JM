package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenTipoSociedadAdm;
import com.siga.beans.CenTipoSociedadBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.beans.ScsRetencionesAdm;
import com.siga.beans.ScsRetencionesBean;
import com.siga.beans.ScsTipoDocumentoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.SolicitudRetencionForm;


//Clase: ScsRetencionesACtion 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 20/12/2004
/**
 * Maneja las acciones que se pueden realizar sobre la tabla SCS_MAESTRORETENCIONES
 */
public class SolicitudRetencionAction extends MasterAction {
	
	/** 
	 * No implementado
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {			
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);		
			ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));						
			/* En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo edición */
			request.getSession().setAttribute("accion",formulario.getModo());
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsRetencionesBean.C_IDRETENCION,(ocultos.get(0)));
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			ScsRetencionesBean retencion = admBean.getRetencion(miHash,this.getUserBean(request).getLanguage());
			
			
			request.getSession().setAttribute("elegido",admBean.beanToHashTable(retencion));
			request.getSession().setAttribute("DATABACKUP",retencion.getOriginalHash());
			ArrayList arraySociedades = new ArrayList();
			arraySociedades.add(retencion.getLetraNifSociedad());
			request.setAttribute("sociedades",arraySociedades);
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}	
		return "modificacionOk";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
	    
		try {
			//Vector visibles = formulario.getDatosTablaVisibles(0);
			Vector ocultos = formulario.getDatosTablaOcultos(0);		
			
			ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));
			/* En la sessión se almacena la acción para abrir el jsp que opera sobre el registro en modo consulta */
			request.getSession().setAttribute("accion",formulario.getModo());
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsRetencionesBean.C_IDRETENCION,(ocultos.get(0)));
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			ScsRetencionesBean retencion = admBean.getRetencion(miHash,this.getUserBean(request).getLanguage());
			
			//Vector resultado = admBean.selectPorClave(miHash);
			//ScsRetencionesBean retencion = (ScsRetencionesBean)resultado.get(0);
					
			request.getSession().setAttribute("elegido",admBean.beanToHashTable(retencion));
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(retencion));
			
			ArrayList arraySociedades = new ArrayList();
			arraySociedades.add(retencion.getLetraNifSociedad());
			request.setAttribute("sociedades",arraySociedades);
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "modificacionOk";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		
		return "insertarRetencion";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		SolicitudRetencionForm miForm = (SolicitudRetencionForm) formulario;		
		ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));
		Hashtable miHash =null;
		
		//Obtenemos la descripcion del formulario
		String descripcion = miForm.getDescripcion();
		
		//Definimos la tabla y la descripcion que luego utilizaremos para sacar el idrecurso de multiidioma
		String nombreTabla = ScsRetencionesBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsRetencionesBean.C_DESCRIPCION;
		
		try {
			
			//Obtenemos el id de la retencion
			Integer newIdRetencion =  admBean.getNuevoIdRetencion();
			miHash = miForm.getDatos();
			miHash.put(ScsRetencionesBean.C_IDRETENCION, newIdRetencion.toString());
			
			//el nuevo id nos servira para sacar el idrecurso multiidioma
			String pkRecursoRetencion = newIdRetencion.toString();
			
			//Obtenemos el idRecurso con el nombreTable el nombre del campo la institucion y el PK de la tabla
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(0), pkRecursoRetencion);
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(0), pkRecursoRetencion);
        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
        	recCatalogoBean.setDescripcion(descripcion);
	        recCatalogoBean.setIdInstitucion(null);
        	recCatalogoBean.setIdRecurso(idRecurso);
        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
        	recCatalogoBean.setNombreTabla(nombreTabla);
			
			tx=usr.getTransaction();		
			tx.begin();
			
			//Insertamos los recursos
			
        	if(!admRecCatalogos.insert(recCatalogoBean, usr.getLanguageInstitucion())) 
        		throw new SIGAException ("messages.inserted.error");
    	
        	miHash.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION,idRecurso);
        	
        	String[] sociedades = miForm.getComboSociedades();
			if(sociedades != null && sociedades.length >0)
			{
				for(int i=0;i<sociedades.length;i++)
				{
					
					miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD,sociedades[i]);
					miHash = admBean.prepararInsert(miHash);
					if(i!=0){
						newIdRetencion =  admBean.getNuevoIdRetencion();
						pkRecursoRetencion = newIdRetencion.toString();
						miHash.put(ScsRetencionesBean.C_IDRETENCION, newIdRetencion.toString());
						idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(0), pkRecursoRetencion);
						idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(0), pkRecursoRetencion);
			        	admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
			        	recCatalogoBean = new GenRecursosCatalogosBean ();
			        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
			        	recCatalogoBean.setDescripcion(descripcion);
				        recCatalogoBean.setIdInstitucion(null);
			        	recCatalogoBean.setIdRecurso(idRecurso);
			        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
			        	recCatalogoBean.setNombreTabla(nombreTabla);
			        	if(!admRecCatalogos.insert(recCatalogoBean, usr.getLanguageInstitucion())) 
			        		throw new SIGAException ("messages.inserted.error");
			        	
			        	miHash.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION,idRecurso);
						
						
					}
					if (!admBean.insert(miHash))
					{
						//throw new ClsExceptions(admBean.getError());
						throw new SIGAException("gratuita.retenciones.sociedadYaIrpf"); 
					}
				}
			}
			else
			{
				miForm.setLetraNifSociedad("");
				miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD,"");
				
				miHash = admBean.prepararInsert(miHash);
				if (!admBean.insert(miHash))
				{
					throw new ClsExceptions(admBean.getError());
				}
			}
        	
        	
        	
			tx.commit();			
			
				
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.inserted.success",request);
		
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		String claves[]={ScsRetencionesBean.C_IDRETENCION};
		String campos[]={ScsRetencionesBean.C_DESCRIPCION,ScsRetencionesBean.C_LETRANIFSOCIEDAD,ScsRetencionesBean.C_RETENCION};
		
		SolicitudRetencionForm miForm = (SolicitudRetencionForm) formulario;		
		ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));
		Hashtable miHash =null;
		
		String descripcion = miForm.getDescripcion();
		String nombreTabla = ScsRetencionesBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsRetencionesBean.C_DESCRIPCION;
		
		try {
			
			miHash = miForm.getDatos();
			String pkRecursoRetencion = String.valueOf(miForm.getIdRetencion());
			
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(0), pkRecursoRetencion);
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			
			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(ScsRetencionesBean.C_IDRETENCION, String.valueOf(miForm.getIdRetencion()));
			
			Hashtable htSignos = new Hashtable();
			htSignos.put(ScsRetencionesBean.C_IDRETENCION, "<>");
			
			
			ScsRetencionesBean beanRetencion = admBean.getRetencion(miHash,usr.getLanguage());
			
			
			boolean isCambioDescripcion = !beanRetencion.getDescripcion().equalsIgnoreCase(descripcion); 
			
			
			
			
			
			
			tx=usr.getTransaction();		
			tx.begin();
			
			//Miramos si hay cambio en la descripcion para ver si tenemos que modificar los recursos.
			if(isCambioDescripcion){
				String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(0), pkRecursoRetencion);
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setDescripcion(descripcion);
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	
	        	if(!admRecCatalogos.update(recCatalogoBean, usr)) { 
	        		throw new SIGAException ("messages.updated.error");
	        	}
				
			}
			miHash.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION,idRecurso);
			
			
			String[] sociedades = miForm.getComboSociedades();
			if (sociedades != null && sociedades.length >0 && !sociedades[0].equals("")) {
				//Si hay seleccion de sociedades en el combo:

				//1.-Update de la primera:
				
				miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD, sociedades[0]);
				if (!admBean.updateDirect(miHash, claves, campos)){
					throw new SIGAException("gratuita.retenciones.sociedadYaIrpf");
				}
				
				//2.-insertamos el registro para la sociedad seleccionada en el combo para los segundos y 
				// posteriores calculando sus nuevos idretencion:
				for (int i=1; i<sociedades.length; i++) {
					miForm.setLetraNifSociedad(sociedades[i]);
					miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD, sociedades[i]);
					
					Integer newIdRetencion =  admBean.getNuevoIdRetencion();
					pkRecursoRetencion = newIdRetencion.toString();
					idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(0), pkRecursoRetencion);
					String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(0), pkRecursoRetencion);
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
		        	recCatalogoBean.setDescripcion(descripcion);
			        recCatalogoBean.setIdInstitucion(null);
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	recCatalogoBean.setNombreTabla(nombreTabla);
		        	
		        	if(!admRecCatalogos.insert(recCatalogoBean, usr.getLanguageInstitucion())){ 
		        		throw new SIGAException ("messages.updated.error");
		        	}
		        	
					miHash.put(ScsRetencionesBean.C_IDRETENCION, newIdRetencion.toString());
					Hashtable hash = admBean.prepararInsert(miHash);	
					miHash.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION,idRecurso);
					
					miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD, sociedades[i]);
					if (!admBean.insert(hash)){
						throw new SIGAException("gratuita.retenciones.sociedadYaIrpf");
					}
				}
			} else {
				
				miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD, "");
				if (!admBean.updateDirect(miHash, claves, campos)) {
					throw new ClsExceptions(admBean.getError());
				}
			}
			
			
			
				        
			tx.commit();	
					
				
			
			
		} catch(Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.updated.success",request);
		
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector visibles = formulario.getDatosTablaVisibles(0);
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		String nombreTabla = ScsRetencionesBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsRetencionesBean.C_DESCRIPCION;
		
		try {		
			String idRetencion = (String)ocultos.get(0);
			miHash.put(ScsRetencionesBean.C_IDRETENCION,idRetencion);
			miHash.put(ScsRetencionesBean.C_USUMODIFICACION,(ocultos.get(1)));
			miHash.put(ScsRetencionesBean.C_FECHAMODIFICACION,(ocultos.get(2)));		
			miHash.put(ScsRetencionesBean.C_DESCRIPCION,(visibles.get(0)));
			miHash.put(ScsRetencionesBean.C_RETENCION,(visibles.get(1)));
			miHash.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD,(visibles.get(2)));
			
			
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(0), idRetencion);
			
			tx=usr.getTransaction();
			tx.begin();
			
			//Eliminamos los recursos
			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        		throw new SIGAException ("error.messages.deleted");
	        	}
			}else
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			if (admBean.delete(miHash))
		    {
				
				forward = "exitoModal";        
		    }		    
		    else
		    {
		    	forward = "exito";
		    }
			tx.commit();
		}catch(Exception e){
			throwExcp("messages.deleted.error",e,tx);
		}
		
		if (forward.equalsIgnoreCase("exitoModal")) 
			return exitoRefresco("messages.deleted.success",request);
		else 
			return exito("messages.deleted.error",request);       
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la consulta a partir de esos datos. Almacena un vector con los resultados
	 * en la sesión con el nombre "resultado"
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		SolicitudRetencionForm miForm = (SolicitudRetencionForm) formulario;		
		ScsRetencionesAdm admBean =  new ScsRetencionesAdm(this.getUserBean(request));
		CenTipoSociedadAdm admTipoSociedad = new CenTipoSociedadAdm(this.getUserBean(request));
		Vector v = new Vector ();
		Vector v1 = new Vector ();
		Hashtable sociedades = new Hashtable();
		

		try {
			v = admBean.select(miForm.getDatos(),this.getUserBean(request).getLanguage());
			
			v1 = admTipoSociedad.select();
			for (int i=0;i<v1.size();i++)
			{
				CenTipoSociedadBean bean=(CenTipoSociedadBean)v1.elementAt(i);
				sociedades.put(bean.getLetraCif(), bean.getDescripcion());
			}
			
			request.getSession().setAttribute("resultado",v);
			request.getSession().setAttribute("tipo",v);
			
			request.setAttribute("sociedades",sociedades);
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}	
		return "listarRetenciones";
	}
	
	
	/** 
	 * No implementado
	 */	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		return null;
	}
}