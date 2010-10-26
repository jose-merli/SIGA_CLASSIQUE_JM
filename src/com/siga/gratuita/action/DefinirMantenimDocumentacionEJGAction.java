//Clase: DefinirDocumentacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaciÛn: 14/02/2005

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

import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesMultidioma;

import com.siga.beans.ScsDocumentacionEJGAdm;

import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.beans.ScsDocumentoEJGAdm;
import com.siga.beans.ScsDocumentoEJGBean;
import com.siga.beans.ScsTipoDocumentoEJGAdm;
import com.siga.beans.ScsTipoDocumentoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirMantenimDocumentacionEJGForm;
import com.siga.ws.CajgConfiguracion;


/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_DOCUMENTACIONEJG
*/
public class DefinirMantenimDocumentacionEJGAction extends MasterAction {	
	
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
		}else if (accion.equalsIgnoreCase("insertarTipo")){
			mapDestino = insertarTipo(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("insertarDocumento")){
			mapDestino = insertarDocumento(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("nuevo2")){
			mapDestino = nuevo2(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("editarDocu")){
			mapDestino = editarDocu(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("verDocu")){
			mapDestino = verDocu(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("modificarDocu")){
			mapDestino = modificarDocu(mapping, miForm, request, response);
		}else if (accion.equalsIgnoreCase("borrarDocu")){
				mapDestino = borrarDocu(mapping, miForm, request, response);
		
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
		
		/*"DATABACKUP" se usa habitualmente asÌ que en primero lugar borramos esta variable*/		
		request.getSession().removeAttribute("DATABACKUP");	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		ScsDocumentacionEJGAdm admBean =  new ScsDocumentacionEJGAdm(this.getUserBean(request));
		DefinirMantenimDocumentacionEJGForm miForm =(DefinirMantenimDocumentacionEJGForm) formulario;
				
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		

		Hashtable busqueda = new Hashtable();		
		
		
		String sql="select distinct f_siga_getrecurso(t.descripcion,"+usr.getLanguage()+") as DESCRIPCIONTIPO,t.abreviatura as ABREVIATURATIPO, F_SIGA_NOMBRE_DOCUMENTOS(t.idtipodocumentoejg,t.idinstitucion) as ABREVIATURADOCU,"+"F_SIGA_DESCRIPCION_DOCUMENTOS(t.idtipodocumentoejg,t.idinstitucion,"+usr.getLanguage()+") as DESCRIPCIONDOCU,t.idtipodocumentoejg as IDTIPO " +
				"from Scs_Tipodocumentoejg t, Scs_Documentoejg d where ";
		String v1= miForm.getDocumentoAbreviado();
		String v2= miForm.getDoc();
		String v3= miForm.getAbreviatura();
		String v4= miForm.getDescripcion();
		
		
		if(miForm.getAbreviatura().trim()!=null && !miForm.getAbreviatura().toString().trim().equals("")){
				sql+=ComodinBusquedas.prepararSentenciaNLS(miForm.getAbreviatura().trim(),"t.ABREVIATURA")+" and ";
				busqueda.put("ABREVIATURA",miForm.getAbreviatura().toString());
		}else{
			busqueda.put("ABREVIATURA"," ");
			
		}
		if(miForm.getDescripcion().trim()!=null && !miForm.getDescripcion().toString().trim().equals("")){
			sql+=ComodinBusquedas.prepararSentenciaNLS(miForm.getDescripcion().trim(),UtilidadesMultidioma.getCampoMultidiomaSimple("t.DESCRIPCION",usr.getLanguage()))+" and ";
			busqueda.put("DESCRIPCION",miForm.getDescripcion().toString());
		}else{
		busqueda.put("DESCRIPCION"," ");
		
		}
		if(miForm.getDocumentoAbreviado().trim()!=null && !miForm.getDocumentoAbreviado().toString().trim().equals("")){
			sql+=ComodinBusquedas.prepararSentenciaNLS(miForm.getDocumentoAbreviado().trim(), "d.ABREVIATURA") +"and ";
			busqueda.put("DOCUMENTOABREVIADO",miForm.getDocumentoAbreviado().toString());
		}else{
		busqueda.put("DOCUMENTOABREVIADO"," ");
		
		}
		if(miForm.getDoc().trim()!=null && !miForm.getDoc().toString().trim().equals("")){
			sql+=ComodinBusquedas.prepararSentenciaNLS(miForm.getDoc().trim(),UtilidadesMultidioma.getCampoMultidiomaSimple("d.DESCRIPCION",usr.getLanguage()))+" and ";
			busqueda.put("DESCRIPCIONDOC",miForm.getDoc().toString());
		}else{
		busqueda.put("DESCRIPCIONDOC"," ");
		
		}

		
		sql=sql+"   t.idinstitucion = "+usr.getLocation()+
				"  and t.idinstitucion = d.idinstitucion (+)"+
				" and t.idtipodocumentoejg = d.idtipodocumentoejg (+)" +
				" order by translate(upper(ABREVIATURATIPO),'¡…Õ”⁄¿»Ã“ŸƒÀœ÷‹','AEIOUAEIOUAEIOU'),translate(upper(descripciontipo),'¡…Õ”⁄¿»Ã“ŸƒÀœ÷‹','AEIOUAEIOUAEIOU'),translate(upper(ABREVIATURADOCU),'¡…Õ”⁄¿»Ã“ŸƒÀœ÷‹','AEIOUAEIOUAEIOU'),translate(upper(DESCRIPCIONDOCU),'¡…Õ”⁄¿»Ã“ŸƒÀœ÷‹','AEIOUAEIOUAEIOU')";
		
		request.getSession().setAttribute("busqueda",busqueda);
		
		try {			
			v = admBean.selectGenerico(sql);
			request.setAttribute("resultado",v);	
			request.getSession().setAttribute("busqueda",busqueda);
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}				
		
		return "listarDocumento";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesiÛn con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaciÛn. De tipo MasterForm.
	 * @param request InformaciÛn de sesiÛn. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar· la siguiente acciÛn a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			ScsTipoDocumentoEJGAdm admBean =  new ScsTipoDocumentoEJGAdm(this.getUserBean(request));
			ScsDocumentoEJGAdm docejgadm = new ScsDocumentoEJGAdm(this.getUserBean(request));
			DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm)formulario;
			ScsTipoDocumentoEJGBean beanTipoDocumento=new ScsTipoDocumentoEJGBean();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String sql="";
			String sql2="";
			Hashtable miHash = new Hashtable();
			miHash = miForm.getDatos();	
			String idInstitucion=usr.getLocation();
			String idioma=usr.getLanguage();			
			Vector resultado2=new Vector();		
			String tipoDocumento= miForm.getTipoDocumento();
			
			if(ocultos!=null){				
				miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
				miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)ocultos.get(0));	
			    beanTipoDocumento = admBean.getTipoDocumento(miHash,idioma);			
			    resultado2 =  docejgadm.getListaDocumentoEjg(miHash,idioma);				
			}else{
				miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
				miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)request.getSession().getAttribute("idTipoDoc"));				
				beanTipoDocumento = admBean.getTipoDocumento(miHash,idioma);
			    resultado2 =  docejgadm.getListaDocumentoEjg(miHash,idioma);				
				miForm.setTipoDocumento(" ");
			    //request.getSession().removeAttribute("idTipoDoc");
			}			
		
			//Entramos al formulario en modo 'modificaciÛn'
			request.setAttribute("accionModo","editar");
			request.getSession().setAttribute("accionModo","editar");	
			
			// recuperamos los valores de el tipo documento y lo mostramos en la jsp.
			if(beanTipoDocumento!=null){
				String  descripcion = beanTipoDocumento.getDescripcion(); 
				String  abreviatura = beanTipoDocumento.getAbreviatura();
				String  codigoExt   = beanTipoDocumento.getCodigoExt();
				String  idtipoDocumentoejg  = beanTipoDocumento.getIdTipoDocumentoEJG();	
				miForm.setDescripcion(descripcion);
				miForm.setAbreviatura(abreviatura);
				miForm.setCodigoExt(codigoExt);			
				miForm.setTipoDocumento(idtipoDocumentoejg);
				
			}						
			request.getSession().setAttribute("resultado",resultado2);				
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion));			
			request.setAttribute("pcajgActivo", tipoCAJG);
		
		

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionTipoOk";
	
	}

	
	protected String editarDocu(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm)formulario;
			ScsDocumentoEJGAdm docejgadm = new ScsDocumentoEJGAdm(this.getUserBean(request));			
			ScsDocumentoEJGBean beanDocumento=new ScsDocumentoEJGBean();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Hashtable miHash = new Hashtable();			
			String idInstitucion=usr.getLocation();
			String idioma=usr.getLanguage();	
			
			if(ocultos!=null){
				miHash.put(ScsDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
				miHash.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)ocultos.get(0));
				miHash.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,(String)ocultos.get(1));	
				beanDocumento = docejgadm.getDocumento(miHash,idioma);					
			}
			// recuperamos los valores de el tipo documento y lo mostramos en la jsp.
			if(beanDocumento!=null){
				String  descripcion = beanDocumento.getDescripcion(); 
				String  abreviatura = beanDocumento.getAbreviatura();
				String  codigoExt   = beanDocumento.getCodigoExt();
				String  idtipoDocumentoejg  = beanDocumento.getIdTipoDocumentoEJG();
				String  iddocumentoejg= beanDocumento.getIdDocumentoEJG();
				miForm.setDescripcion(descripcion);
				miForm.setAbreviatura(abreviatura);
				miForm.setCodigoExt(codigoExt);
				miForm.setTipoDocumento(idtipoDocumentoejg);
				miForm.setDocumento(iddocumentoejg);
			}	
			//Entramos al formulario en modo 'modificacion'
			request.setAttribute("accionModo","editar");
			request.getSession().setAttribute("accionModo","editar");			
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion));		
			request.setAttribute("pcajgActivo", tipoCAJG);
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionDocumentoOk";
	
	}
	
	protected String verDocu(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm)formulario;
			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String sql="";
			String sql2="";
			ScsDocumentoEJGAdm docejgadm = new ScsDocumentoEJGAdm(this.getUserBean(request));
			ScsDocumentoEJGBean beanDocumento=new ScsDocumentoEJGBean();
			Hashtable miHash = new Hashtable();			
			String idInstitucion=usr.getLocation();
			String idioma=usr.getLanguage();	
			
			if(ocultos!=null){
				miHash.put(ScsDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
				miHash.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)ocultos.get(0));
				miHash.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,(String)ocultos.get(1));	
				beanDocumento = docejgadm.getDocumento(miHash,idioma);					
			}
			
			// recuperamos los valores de el tipo documento y lo mostramos en la jsp.
			if(beanDocumento!=null){
				String  descripcion = beanDocumento.getDescripcion(); 
				String  abreviatura = beanDocumento.getAbreviatura();
				String  codigoExt   = beanDocumento.getCodigoExt();
				String  idtipoDocumentoejg  = beanDocumento.getIdTipoDocumentoEJG();
				String  iddocumentoejg= beanDocumento.getIdDocumentoEJG();
				miForm.setDescripcion(descripcion);
				miForm.setAbreviatura(abreviatura);
				miForm.setCodigoExt(codigoExt);
				miForm.setTipoDocumento(idtipoDocumentoejg);
				miForm.setDocumento(iddocumentoejg);
			}	
							
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion));			
			request.setAttribute("pcajgActivo", tipoCAJG);
			//Entramos al formulario en modo 'ver'
			request.setAttribute("accionModo","ver");
			request.getSession().setAttribute("accionModo","ver");
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionDocumentoOk";
	
	}
	
	
	
	
	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		try {
			
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm)formulario;			
			//miForm.setDocumento((String)ocultos.get(1));
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String lenguaje=usr.getLanguage();
			String idInstitucion=usr.getLocation();
			ScsDocumentacionEJGAdm admBean =  new ScsDocumentacionEJGAdm(this.getUserBean(request));
			ScsTipoDocumentoEJGAdm admtBean =  new ScsTipoDocumentoEJGAdm(this.getUserBean(request));
			ScsDocumentoEJGAdm docejgadm = new ScsDocumentoEJGAdm(this.getUserBean(request));
			ScsTipoDocumentoEJGBean beanTipoDocumento=new ScsTipoDocumentoEJGBean();
			Vector resultado2=new Vector();
			//Entramos al formulario en modo 'modificaciÛn'
			request.setAttribute("accionModo","ver");
			request.getSession().setAttribute("accionModo","ver");
			
			Hashtable elegidoHash = new Hashtable();
			Hashtable miHash = new Hashtable();

			miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
			miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)ocultos.get(0));
			beanTipoDocumento = admtBean.getTipoDocumento(miHash,lenguaje);			
			resultado2 =  docejgadm.getListaDocumentoEjg(miHash,lenguaje);
			
		if(beanTipoDocumento!=null){			
				miForm.setDescripcion(beanTipoDocumento.getDescripcion());
				miForm.setAbreviatura(beanTipoDocumento.getAbreviatura());
				miForm.setCodigoExt(beanTipoDocumento.getCodigoExt());
				miForm.setTipoDocumento(beanTipoDocumento.getIdTipoDocumentoEJG());
			}				
		
			
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion));			
			request.setAttribute("pcajgActivo", tipoCAJG);
			request.getSession().setAttribute("resultado",resultado2);					

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "modificacionTipoOk";
	}

	/**
	 * Rellena el string que indica la acciÛn a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserciÛn. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaciÛn. De tipo MasterForm.
	 * @param request InformaciÛn de sesiÛn. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar· la siguiente acciÛn a llevar a cabo. 
	 * @throws ClsExceptions 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			int idInstitucion=Integer.parseInt(usr.getLocation());
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(idInstitucion);		
			request.setAttribute("pcajgActivo", tipoCAJG);
		return "insertarTipo";
	}

	protected String nuevo2(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {		
		DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm)formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		int idInstitucion=Integer.parseInt(usr.getLocation());
		int tipoCAJG = CajgConfiguracion.getTipoCAJG(idInstitucion);
		String idTipo=miForm.getTipoDocumento();		
		miForm.setDocumento(idTipo);	
		request.setAttribute("pcajgActivo", tipoCAJG);
		
		return "insertarDocu";
	}
	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaciÛn. De tipo MasterForm.
	 * @param request InformaciÛn de sesiÛn. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar· la siguiente acciÛn a llevar a cabo. 
	 */
	protected String insertarTipo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario;		
		ScsTipoDocumentoEJGAdm admBean =  new ScsTipoDocumentoEJGAdm(this.getUserBean(request));
		
		
		String idInstitucion=usr.getLocation();
		String abreviatura = miForm.getAbreviatura();
		String descripcion = miForm.getDescripcion();
		String codigoExt = miForm.getCodigoExt();		
		String nombreTabla = ScsTipoDocumentoEJGBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsTipoDocumentoEJGBean.C_DESCRIPCION;
		String nombreCampoAbreviatura = ScsTipoDocumentoEJGBean.C_ABREVIATURA;
		
		Hashtable miHash = new Hashtable();		
		miHash = miForm.getDatos();
		miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
		
		try {					
			admBean.prepararInsert(miHash);
			String newTipoIdDocumentoEjg = (String)miHash.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG);
			String pkRecursoTipoDocumento = newTipoIdDocumentoEjg;
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoTipoDocumento);
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			
			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, newTipoIdDocumentoEjg);
			Hashtable htSignos = new Hashtable();
			
			htSignos.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, "<>");
			
			boolean isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnica(idInstitucion,abreviatura,nombreCampoAbreviatura,
					htPkTabl,htSignos,nombreTabla,3);
			if(isClaveUnicaMultiIdioma){
				isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,descripcion,nombreCampoDescripcion,
						htPkTabl,htSignos,nombreTabla,3,usr.getLanguage());
				
				if(isClaveUnicaMultiIdioma){
					
					tx=usr.getTransaction();		
					tx.begin();
					
					
					String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), pkRecursoTipoDocumento);
		        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
		        	recCatalogoBean.setDescripcion(descripcion);
			        recCatalogoBean.setIdInstitucion(new Integer(idInstitucion));
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	recCatalogoBean.setNombreTabla(nombreTabla);
		        	if(!admRecCatalogos.insert(recCatalogoBean, usr.getLanguageInstitucion())) 
		        		throw new SIGAException ("messages.inserted.error");
	        	
					
		        	miHash.put(ScsTipoDocumentoEJGBean.C_ABREVIATURA,abreviatura);
		        	miHash.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION,idRecurso);
		        	miHash.put(ScsTipoDocumentoEJGBean.C_CODIGOEXT,codigoExt);
		        	//miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,miHash.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG));
		        	String idtipodocumentoejg=newTipoIdDocumentoEjg;
		        	 miForm.setDocumento(miHash.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG).toString());
		        	request.getSession().setAttribute("idTipoDoc",miHash.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG));
					admBean.insert(miHash);	        
					tx.commit();			
				
				
				}else{
					throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.descripcionDuplicada");	
				}
				
			}else{
				throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.abreviaturaDuplicada");
				
			}
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}    
//		return exitoModal("messages.inserted.success",request);
		return exitoRefresco("messages.inserted.success", request);
	}

	protected String insertarDocumento(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario;		
		ScsDocumentoEJGAdm admBean =  new ScsDocumentoEJGAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		miHash = miForm.getDatos();
		miHash.put(ScsDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
		miHash.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,miForm.getTipoDocumento());
		String idInstitucion=usr.getLocation();
		String abreviatura = miForm.getAbreviatura();
		String descripcion = miForm.getDescripcion();
		String codigoExt = miForm.getCodigoExt();
		
		String nombreTabla = ScsDocumentoEJGBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsDocumentoEJGBean.C_DESCRIPCION;
		String nombreCampoAbreviatura = ScsDocumentoEJGBean.C_ABREVIATURA;
		
		
		try {					
			admBean.prepararInsert(miHash);
			String newIdDocumentoEjg = (String)miHash.get(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG); 
			//°°°°°°°°°°°°°°°°°°°°ATENCION!!!!!!!!!!!!
			//Para formar es id del recurso hay que incluir 2 campos que son los que forman parte de la Pk. Lo separaremos con _
			//pra diferenciarlos
			String pkRecursoDocumento = miForm.getTipoDocumento()+"_"+newIdDocumentoEjg;
			
			
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoDocumento);
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			
			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, miForm.getTipoDocumento());
			htPkTabl.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG, newIdDocumentoEjg);
			Hashtable htSignos = new Hashtable();
			htSignos.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, "=");
			htSignos.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG, "<>");
			boolean isClaveUnicaMultiIdioma =  UtilidadesBDAdm.isClaveUnica(idInstitucion,abreviatura,nombreCampoAbreviatura,
					htPkTabl,htSignos,nombreTabla,3);
			if(isClaveUnicaMultiIdioma){
				isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,descripcion,nombreCampoDescripcion,
						htPkTabl,htSignos,nombreTabla,3,usr.getLanguage());
				
				if(isClaveUnicaMultiIdioma){
					
					tx=usr.getTransaction();		
					tx.begin();
					
					
					String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), pkRecursoDocumento.toString());
		        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setCampoTabla(nombreCampoDescripcion);
		        	recCatalogoBean.setDescripcion(descripcion);
		        	recCatalogoBean.setIdInstitucion(new Integer(idInstitucion));
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	recCatalogoBean.setNombreTabla(nombreTabla);
		        	if(!admRecCatalogos.insert(recCatalogoBean, usr.getLanguageInstitucion())) 
		        		throw new SIGAException ("messages.inserted.error");
	        	
					
		        	miHash.put(ScsDocumentoEJGBean.C_ABREVIATURA,abreviatura);
		        	miHash.put(ScsDocumentoEJGBean.C_DESCRIPCION,idRecurso);
		        	miHash.put(ScsDocumentoEJGBean.C_CODIGOEXT,codigoExt);
					admBean.insert(miHash);	        
					tx.commit();	
				
				
				}else{
					throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.descripcionDuplicada");	
				}
				
			}else{
				throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.abreviaturaDuplicada");
				
			}
			
			
			
			
					
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}    
		return exitoModal("messages.inserted.success",request);
	}	
	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaciÛn. De tipo MasterForm.
	 * @param request InformaciÛn de sesiÛn. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar· la siguiente acciÛn a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario;		
		ScsTipoDocumentoEJGAdm admBean =  new ScsTipoDocumentoEJGAdm(this.getUserBean(request));
		
				
		String claves[]={ScsTipoDocumentoEJGBean.C_IDINSTITUCION,ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG};
		String campos[]={ScsTipoDocumentoEJGBean.C_IDINSTITUCION,ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,
				         ScsTipoDocumentoEJGBean.C_ABREVIATURA,ScsTipoDocumentoEJGBean.C_DESCRIPCION,
				         ScsTipoDocumentoEJGBean.C_CODIGOEXT};
		String nombreTabla = ScsTipoDocumentoEJGBean.T_NOMBRETABLA;
		String nombreCampoDescripcion = ScsTipoDocumentoEJGBean.C_DESCRIPCION;
		String nombreCampoAbreviatura = ScsTipoDocumentoEJGBean.C_ABREVIATURA;
		String idInstitucion=usr.getLocation();
		String abreviatura = miForm.getAbreviatura();
		String descripcion = miForm.getDescripcion();
		String codigoExt = miForm.getCodigoExt();
		
		Hashtable miHash = new Hashtable();
		
		
		try {			
			miHash = miForm.getDatos();	
			miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,idInstitucion);
			miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,miForm.getTipoDocumento());
			String pkRecursoTipoDocumento = miForm.getTipoDocumento();
			
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoTipoDocumento);
			if(idRecurso==null)
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			
			
			Hashtable htPkTabl = new Hashtable();
			htPkTabl.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, miForm.getTipoDocumento());
			
			Hashtable htSignos = new Hashtable();
			htSignos.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, "<>");
			
			
			ScsTipoDocumentoEJGBean beanTipoDocumento = admBean.getTipoDocumento(miHash,usr.getLanguage());
			
			
			boolean isCambioDescripcion = !beanTipoDocumento.getDescripcion().equalsIgnoreCase(descripcion); 
			boolean isCambioAbreviatura = !beanTipoDocumento.getAbreviatura().equalsIgnoreCase(abreviatura);
			
			
			
			boolean isClaveUnicaMultiIdioma = true;
			if(isCambioAbreviatura){
				isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnica(idInstitucion,abreviatura,nombreCampoAbreviatura,
					htPkTabl,htSignos,nombreTabla,3);
			}
			
			
			if(isClaveUnicaMultiIdioma){
				if(isCambioDescripcion){
					isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,descripcion,nombreCampoDescripcion,
						htPkTabl,htSignos,nombreTabla,3,usr.getLanguage());
				}
				
				if(isClaveUnicaMultiIdioma){
					
					tx=usr.getTransaction();		
					tx.begin();
					if(isCambioDescripcion){
						String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), pkRecursoTipoDocumento);
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
					miHash.put(ScsTipoDocumentoEJGBean.C_ABREVIATURA,abreviatura);
					miHash.put(ScsTipoDocumentoEJGBean.C_CODIGOEXT,codigoExt);					
					admBean.updateDirect(miHash,claves,campos);	        
					tx.commit();	
					
				}else{
					throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.descripcionDuplicada");
					
				}
			}else{
				throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.abreviaturaDuplicada");
				
			}
			
		
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	
	protected String modificarDocu(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		

			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			UserTransaction tx=null;
			
			DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario;		
			ScsDocumentoEJGAdm admBean =  new ScsDocumentoEJGAdm(this.getUserBean(request));
			Hashtable miHash = new Hashtable();		
			String claves[]={ScsDocumentoEJGBean.C_IDINSTITUCION,ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,ScsDocumentoEJGBean.C_IDDOCUMENTOEJG};
			String campos[]={ScsDocumentoEJGBean.C_IDINSTITUCION,ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,
					         ScsDocumentoEJGBean.C_ABREVIATURA,ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,
					         ScsDocumentoEJGBean.C_DESCRIPCION,ScsDocumentoEJGBean.C_CODIGOEXT};		
			String nombreTabla = ScsDocumentoEJGBean.T_NOMBRETABLA;
			String nombreCampoDescripcion = ScsDocumentoEJGBean.C_DESCRIPCION;
			String nombreCampoAbreviatura = ScsDocumentoEJGBean.C_ABREVIATURA;
			String idInstitucion=usr.getLocation();
			String abreviatura = miForm.getAbreviatura();
			String descripcion = miForm.getDescripcion();
			String codigoExt = miForm.getCodigoExt();	
			try {			
				miHash = miForm.getDatos();		
				miHash.put(ScsDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,miForm.getTipoDocumento());
				miHash.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,miForm.getDocumento());
				
				String pkRecursoDocumento = miForm.getTipoDocumento()+"_"+miForm.getDocumento();
					
				
				String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(idInstitucion), pkRecursoDocumento);
				if(idRecurso==null)
					throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
				ScsDocumentoEJGBean beanDocumento = admBean.getDocumento(miHash,usr.getLanguage());
				
				boolean isCambioDescripcion = !beanDocumento.getDescripcion().equals(descripcion); 
				boolean isCambioAbreviatura = !beanDocumento.getAbreviatura().equals(abreviatura);
				
				
				boolean isClaveUnicaMultiIdioma = true;
				Hashtable htPkTabl = new Hashtable();
				
				htPkTabl.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,miForm.getTipoDocumento());
				htPkTabl.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,miForm.getDocumento());
				Hashtable htSignos = new Hashtable();
				htSignos.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, "=");
				htSignos.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG, "<>");
				if(isCambioAbreviatura){
					isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnica(idInstitucion,abreviatura,nombreCampoAbreviatura,
						htPkTabl,htSignos,nombreTabla,3);
				}
				
				if(isClaveUnicaMultiIdioma){
					if(isCambioDescripcion){
						isClaveUnicaMultiIdioma = UtilidadesBDAdm.isClaveUnicaMultiIdioma(idInstitucion,descripcion,nombreCampoDescripcion,
							htPkTabl,htSignos,nombreTabla,3,usr.getLanguage());
					}
					
					if(isClaveUnicaMultiIdioma){
						
						tx=usr.getTransaction();		
						tx.begin();
						if(isCambioDescripcion){
							String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(nombreTabla, nombreCampoDescripcion,  new Integer(idInstitucion), pkRecursoDocumento);
			    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
				        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
				        	recCatalogoBean.setDescripcion(descripcion);
				        	recCatalogoBean.setIdRecurso(idRecurso);
				        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
				        	if(!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) { 
				        		throw new SIGAException ("messages.updated.error");
				        	}
							
						}
						
						miHash.put(ScsDocumentoEJGBean.C_DESCRIPCION,idRecurso);
						miHash.put(ScsDocumentoEJGBean.C_ABREVIATURA,abreviatura);
						miHash.put(ScsDocumentoEJGBean.C_CODIGOEXT,codigoExt);						
						admBean.updateDirect(miHash,claves,campos);	        
						tx.commit();	
						
					}else{
						throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.descripcionDuplicada");
						
					}
				}else{
					throw new SIGAException("gratuita.mantenimientoDocumentacionEJG.mensaje.abreviaturaDuplicada");
				}
				
			
			
			} catch (Exception e) {
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
			}
			
			return exitoModal("messages.updated.success",request);
		}	

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaciÛn. De tipo MasterForm.
	 * @param request InformaciÛn de sesiÛn. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar· la siguiente acciÛn a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsTipoDocumentoEJGAdm admBean =  new ScsTipoDocumentoEJGAdm(this.getUserBean(request));
		ScsDocumentoEJGAdm admDocuBean= new ScsDocumentoEJGAdm(this.getUserBean(request));
		//DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		Hashtable miHashDocu = new Hashtable();
		
		try {	
			tx=usr.getTransaction();
			tx.begin();
			// Borramos primero los documentos asociados al tipo de documento que vamos a borrar
			String idTipoDocumento = (String)ocultos.get(0);
			miHashDocu.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,idTipoDocumento);
			miHashDocu.put(ScsDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
			
			String claves[] = new String[2];
			claves[0] = ScsDocumentoEJGBean.C_IDINSTITUCION;
			claves[1] = ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG;
			String nombreTablaDoc =  ScsDocumentoEJGBean.T_NOMBRETABLA;
			String nombreCampoDescripcionDoc =  ScsDocumentoEJGBean.C_DESCRIPCION;	
			ScsDocumentoEJGAdm admDocumentoEjg = new ScsDocumentoEJGAdm(usr);
			Vector listaDocumentos = admDocumentoEjg.select(miHashDocu);
			
			
			//Recorremos los documento para borrar sus recursos
			if(listaDocumentos!=null && listaDocumentos.size()>0){
				int sizeListaDocumentos = listaDocumentos.size();
			
				for (int i = 0; i < sizeListaDocumentos; i++) {
					ScsDocumentoEJGBean beanDocumento =  (ScsDocumentoEJGBean) listaDocumentos.get(i);
					String idDocumento = beanDocumento.getIdDocumentoEJG();
					String pkRecursoDocumento = idTipoDocumento+idDocumento;
					
					String idRecursoDoc = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTablaDoc, nombreCampoDescripcionDoc, new Integer(usr.getLocation()), pkRecursoDocumento);
					
					if (idRecursoDoc != null) {
		    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
			        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
			        	recCatalogoBean.setIdRecurso(idRecursoDoc);
			        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
			        		throw new ClsExceptions (admRecCatalogos.getError());
			        	}
					}else {
						throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
					}
					
				}
				admDocuBean.deleteDirect(miHashDocu, claves);
				
				
			}
		// Borramos el tipo de documento
			String nombreTabla =  ScsTipoDocumentoEJGBean.T_NOMBRETABLA;
			
			String nombreCampoDescripcion =  ScsTipoDocumentoEJGBean.C_DESCRIPCION;
			String pkRecursoTipoDocumento = idTipoDocumento;
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(usr.getLocation()), pkRecursoTipoDocumento);
			
			miHash.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(ocultos.get(0)));
			miHash.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
			
			
			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        	    throw new ClsExceptions (admRecCatalogos.getError());
	        	}
			}else {
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			}
			
			admBean.delete(miHash);		    
			
			tx.commit();
				
				
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
   	   }
	   
		return exitoRefresco("messages.deleted.success",request);
	}
	
	protected String borrarDocu(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsDocumentoEJGAdm admBean =  new ScsDocumentoEJGAdm(this.getUserBean(request));
		//DefinirMantenimDocumentacionEJGForm miForm = (DefinirMantenimDocumentacionEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		
		try {			
			String idTipoDoc = (String)ocultos.get(0);
			String idDoc = (String)ocultos.get(1);
			miHash.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,idTipoDoc);
			miHash.put(ScsDocumentoEJGBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,idDoc);
			
			tx=usr.getTransaction();
			String nombreTabla =  ScsDocumentoEJGBean.T_NOMBRETABLA;
			String nombreCampoDescripcion =  ScsDocumentoEJGBean.C_DESCRIPCION;
			String pkRecursoDocumento = idTipoDoc+"_"+idDoc;
			
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(nombreTabla, nombreCampoDescripcion, new Integer(usr.getLocation()), pkRecursoDocumento);			
			
			tx.begin();

			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        		throw new SIGAException ("error.messages.deleted");
	        	}
			}else
				throw new SIGAException("error.messages.sinConfiguracionMultiIdioma");
			admBean.delete(miHash);		    
			tx.commit();
			
		} catch (Exception e) {
			throw new SIGAException("messages.deleted.documentacion.error");
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
		//String volver=request.getSession().getAttribute("esVolver").toString();
		request.getSession().removeAttribute("esVolver");
		request.getSession().removeAttribute("busqueda");
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