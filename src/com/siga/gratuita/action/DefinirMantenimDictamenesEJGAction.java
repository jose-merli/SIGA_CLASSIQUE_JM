//Clase: DefinirDocumentacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.beans.ScsDictamenEJGAdm;
import com.siga.beans.ScsDictamenEJGBean;
import com.siga.beans.ScsDocumentacionEJGAdm;

import com.siga.beans.ScsDocumentoEJGBean;

import com.siga.beans.ScsTipoDocumentoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirMantenimDictamenesEJGForm;
import com.siga.gratuita.form.DefinirMantenimDocumentacionEJGForm;


/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_DOCUMENTACIONEJG
*/
public class DefinirMantenimDictamenesEJGAction extends MasterAction {	
	
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
		
		//La primera vez que se carga el formulario  borrarDocu
		// Abrir
		if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
			mapDestino = abrir(mapping, miForm, request, response);						
		}else if (accion.equalsIgnoreCase("insertarDictamen")){
			mapDestino = insertarDictamen(mapping, miForm, request, response);
		}else {
			return super.executeInternal(mapping,
				      formulario,
				      request, 
				      response);
		}
		
		// Redireccionamos el flujo a la JSP correspondiente
		if (mapDestino == null) 
		{ 
			//mapDestino = "exception";
			if (miForm.getModal().equalsIgnoreCase("TRUE"))
			{
				request.setAttribute("exceptionTarget", "parent.modal");
			}
			
			//throw new ClsExceptions("El ActionMapping no puede ser nulo");
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
	
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		/*"DATABACKUP" se usa habitualmente así que en primero lugar borramos esta variable*/		
		//request.getSession().removeAttribute("DATABACKUP");	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
		DefinirMantenimDictamenesEJGForm miForm =(DefinirMantenimDictamenesEJGForm) formulario;
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		

		Hashtable busqueda = new Hashtable();		
		String testigo="0";
		
		String sql="select d.idinstitucion as INSTITUCION,d.iddictamen as IDDICTAMEN,d.abreviatura as ABREVIATURA,f_siga_getrecurso (d.DESCRIPCION,1) as DESCRIPCION,d.idtipodictamen as IDTIPODICTAMEN,d.idfundamento as IDFUNDAMENTO,d.codigoext as CODIGOEXT from scs_dictamenejg d where d.idinstitucion="+usr.getLocation();
				
		if(miForm.getCodigoExt()!=null && !miForm.getCodigoExt().toString().equals("") && !miForm.getCodigoExt().toString().equals("null")){
				sql=sql+" where d.codigoext like '%"+miForm.getCodigoExt().toString()+"%' ";
				busqueda.put("CODIGOEXT",miForm.getCodigoExt().toString());
				testigo="1";
		}else{
			busqueda.put("CODIGOEXT"," ");
		}
		if(miForm.getDescripcion()!=null && !miForm.getDescripcion().toString().equals("")){
			if(testigo.equals("1")){
				sql=sql+" and ";				
			}else{
				sql=sql+" where ";
			}
				sql=sql+"  d.descripcion like '%"+miForm.getDescripcion().toString()+"%'"+
				"  where d.idinstitucion="+usr.getLocation();
				busqueda.put("DESCRIPCION",miForm.getDescripcion().toString());
		}else{
			busqueda.put("DESCRIPCION"," ");
		}
		sql=sql+" order by d.abreviatura";
		
		request.getSession().setAttribute("busqueda",busqueda);
		
		try {			
			v = admBean.selectGenerico(sql);
			request.setAttribute("resultado",v);			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}				
		
		return "listarDictamen";
	}

	
	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		try {
			
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirMantenimDictamenesEJGForm miForm = (DefinirMantenimDictamenesEJGForm)formulario;
			//miForm.setAbreviatura((String)ocultos.get(1));
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
			//Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo","ver");
			request.getSession().setAttribute("accionModo","ver");
			
			Hashtable elegidoHash = new Hashtable();

			String sql="Select * from scs_dictamenejg t where t.idinstitucion="+usr.getLocation()+" and t.iddictamen="+(String)ocultos.get(0);
			//String sql2="Select * from scs_documentoejg t where t.idinstitucion="+usr.getLocation()+" and t.idtipodocumentoejg="+(String)ocultos.get(0);
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectGenerico(sql);
			if(resultado!=null)
				elegidoHash=(Hashtable)resultado.get(0);
			
			//Vector resultado2 = admBean.selectGenerico(sql2);
			
			//request.getSession().setAttribute("resultado",resultado2);
			
			request.getSession().setAttribute("elegido",elegidoHash);			

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionTipoOk";
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
			DefinirMantenimDictamenesEJGForm miForm = (DefinirMantenimDictamenesEJGForm)formulario;
			//miForm.setAbreviatura((String)ocultos.get(1));
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
			//Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo","editar");
			request.getSession().setAttribute("accionModo","editar");
			
			Hashtable elegidoHash = new Hashtable();
			String sql="",idDictamen="";
			if(ocultos!=null && ocultos.size()>0){
				sql="Select * from scs_dictamenejg t where t.idinstitucion="+usr.getLocation()+" and t.iddictamen="+(String)ocultos.get(0);
			}else{
				idDictamen=(String)request.getSession().getAttribute("idDictamen");
				request.getSession().removeAttribute("idDictamen");
				if(idDictamen!=null && !idDictamen.equals("")){ //si lo acabamos de crear
					sql="Select * from scs_dictamenejg t where t.idinstitucion="+usr.getLocation()+" and t.iddictamen="+idDictamen;
				}else{
					sql="Select * from scs_dictamenejg t where t.idinstitucion="+usr.getLocation()+" and t.iddictamen="+(String)miForm.getIdDictamen();
				}
			}
			//String sql2="Select * from scs_documentoejg t where t.idinstitucion="+usr.getLocation()+" and t.idtipodocumentoejg="+(String)ocultos.get(0);
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectGenerico(sql);
			if(resultado!=null && resultado.size()>0)
				elegidoHash=(Hashtable)resultado.get(0);
			
			//Vector resultado2 = admBean.selectGenerico(sql2);
			
			//request.getSession().setAttribute("resultado",resultado2);
			
			request.getSession().setAttribute("elegido",elegidoHash);			

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionTipoOk";
	}	
	
	
	/**
	 * Rellena el string que indica la acción a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "insertarDictamen";
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
	protected String insertarDictamen(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirMantenimDictamenesEJGForm miForm = (DefinirMantenimDictamenesEJGForm) formulario;		
		ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
		String forward="";
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();			
			tx=usr.getTransaction();		
			tx.begin();
			miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
			admBean.prepararInsert(miHash);
			request.getSession().setAttribute("idDictamen",miHash.get(ScsDictamenEJGBean.C_IDDICTAMEN));
			admBean.insert(miHash);	        
			tx.commit();			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}    
//		return exitoModal("messages.inserted.success",request);
		return exitoRefresco("messages.inserted.success", request);
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
		
		DefinirMantenimDictamenesEJGForm miForm = (DefinirMantenimDictamenesEJGForm) formulario;		
		ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
		String forward="";
		Hashtable miHash = new Hashtable();		
		String claves[]={ScsDictamenEJGBean.C_IDINSTITUCION,ScsDictamenEJGBean.C_IDDICTAMEN};
		String campos[]={ScsDictamenEJGBean.C_IDINSTITUCION,ScsDictamenEJGBean.C_IDDICTAMEN,ScsDictamenEJGBean.C_ABREVIATURA,ScsDictamenEJGBean.C_DESCRIPCION,ScsDictamenEJGBean.C_CODIGOEXT,ScsDictamenEJGBean.C_IDFUNDAMENTO,ScsDictamenEJGBean.C_IDTIPODICTAMEN,ScsDocumentoEJGBean.C_FECHAMODIFICACION,ScsDocumentoEJGBean.C_USUMODIFICACION};
		try {			
			miHash = miForm.getDatos();			
			tx=usr.getTransaction();		
			tx.begin();
			miHash.put(ScsDictamenEJGBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(ScsDictamenEJGBean.C_IDDICTAMEN,miForm.getIdDictamen());
			miHash.put(ScsDictamenEJGBean.C_CODIGOEXT,miForm.getCodigoExt());
			
			//admBean.prepararInsert(miHash);
			admBean.updateDirect(miHash,claves,campos);	        
			tx.commit();	
		
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoRefresco("messages.updated.success",request);
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
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsDictamenEJGAdm admBean =  new ScsDictamenEJGAdm(this.getUserBean(request));
		DefinirMantenimDictamenesEJGForm miForm = (DefinirMantenimDictamenesEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsDictamenEJGBean.C_IDDICTAMEN,(ocultos.get(0)));
			miHash.put(ScsDictamenEJGBean.C_IDINSTITUCION,usr.getLocation());
			
			tx=usr.getTransaction();
			tx.begin();
			admBean.delete(miHash);		    
			tx.commit();
			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		}
		
		return exitoRefresco("messages.deleted.success",request);
	}
	
	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		return "busqueda";
	}

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		ScsDocumentacionEJGAdm admBean =  new ScsDocumentacionEJGAdm(this.getUserBean(request));
		DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();


		
		request.setAttribute("DATOSBUSQUEDA",miHash);
		
		try {			
			v = admBean.buscar(miHash);			
			request.setAttribute("resultado",v);	
			request.setAttribute("accion",formulario.getModo());		
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "busqueda";
	}
}