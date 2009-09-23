/*
 * Created on Mar 07, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.beans.EnvComponentesListaCorreoAdm;
import com.siga.beans.EnvComponentesListaCorreoBean;
import com.siga.beans.EnvListaCorreoConsultaAdm;
import com.siga.beans.EnvListaCorreoConsultaBean;
import com.siga.beans.EnvListaCorreosAdm;
import com.siga.beans.EnvListaCorreosBean;
import com.siga.envios.form.ListaCorreosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la búsqueda de Listas de correos
 */
public class BusqListaCorreosAction extends MasterAction {
    
    
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
						
					} else if (accion.equalsIgnoreCase("listadoComponentes")){
						mapDestino = listadoComponentes(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("listadoConsultas")){
						mapDestino = listadoConsultas(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("insertarComponente")){
						mapDestino = insertarComponente(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("insertarConsulta")){
						mapDestino = insertarConsulta(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("borrarComponente")){
						mapDestino = borrarComponente(mapping, miForm, request, response);
					
					} else if (accion.equalsIgnoreCase("borrarConsulta")){
						mapDestino = borrarConsulta(mapping, miForm, request, response);
					
					} else if (accion.equalsIgnoreCase("nuevaConsulta")){
						mapDestino = nuevaConsulta(mapping, miForm, request, response);
					
					} else if (accion.equalsIgnoreCase("enviarLista")){
						mapDestino = enviarLista(mapping, miForm, request, response);
					
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}
	
	/**
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */	
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
	    	  
	    return("inicio");
	}
	
	/**
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, 
            				MasterForm formulario, 
            				HttpServletRequest request, 
            				HttpServletResponse response) throws SIGAException
	{
        ListaCorreosForm form = (ListaCorreosForm)formulario;
        HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        EnvListaCorreosAdm listaAdm = new EnvListaCorreosAdm (this.getUserBean(request));
                
        //Tabla ENV_LISTACORREOS
		
		String E_IDINSTITUCION = EnvListaCorreosBean.C_IDINSTITUCION;
		String E_NOMBRE = EnvListaCorreosBean.C_NOMBRE;
		String E_DINAMICA = EnvListaCorreosBean.C_DINAMICA;
		
		
		//Valores recogidos del formulario para la búsqueda
        String nombreLista = form.getCampoLista();
        String esDinamica = form.getCampoDinamica();       
        
        String where = "WHERE ";        
        
        where += E_IDINSTITUCION + " = " + userBean.getLocation();
        
        //campos de búsqueda        
        where += (nombreLista!=null && !nombreLista.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombreLista.trim(),E_NOMBRE) : "";
        where += (esDinamica!=null && !esDinamica.equals("")) ? " AND " + E_DINAMICA + " = '" + esDinamica + "'": ""; 
         
        Vector datos=null;
        try {
            datos = listaAdm.selectNLS(where);
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
             
        request.setAttribute("datos", datos);
        
        
//      Inserto en la session los parámetros de búsqueda
	    
	    Hashtable dataBackup = new Hashtable();
        dataBackup.put("campoLista", form.getCampoLista());
        dataBackup.put("campoDinamica", form.getCampoDinamica());
        
	    request.getSession().setAttribute("DATABACKUP",dataBackup);       
        
	    request.setAttribute("buscar", "buscar");
        
	    if (form.getModal().equalsIgnoreCase("true")){
	        return "resultadoModal";
	    } else {
	        return "resultado";
	    }
	}
    
	/** 
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
        ListaCorreosForm form = (ListaCorreosForm) formulario;
        form.setDinamica("N");
        form.setNombre("");
        
        return "nuevo";
	}
	
	/** 
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
		    
		    EnvListaCorreosAdm listaAdm = new EnvListaCorreosAdm(this.getUserBean(request));
		    
		    ListaCorreosForm form = (ListaCorreosForm) formulario;
		    
		    //Rellenamos el nuevo Bean de lista de correos
		    
		    EnvListaCorreosBean listaBean = new EnvListaCorreosBean();	    
		    Integer id = null;

		    id = listaAdm.getNewIdListaCorreos(userBean);	    
		    listaBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    listaBean.setIdListaCorreo(id);
		    listaBean.setNombre(form.getNombre());
		    String dinamica = form.getDinamica().equalsIgnoreCase("S")?"S":"N";
		    listaBean.setDinamica(dinamica);		    
		    
		    tx.begin();
	        listaAdm.insert(listaBean);
	        tx.commit();
	        
	        Hashtable databackup;
	        if (request.getSession().getAttribute("DATABACKUP")!=null){
	        	try {
	        		databackup = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	        	} catch (Exception e1){
	        		databackup = new Hashtable();	
	        	}
	        } else { 
	            databackup = new Hashtable();
	        }
	        databackup.put(EnvListaCorreosBean.C_IDINSTITUCION,userBean.getLocation());
	        databackup.put(EnvListaCorreosBean.C_IDLISTACORREO,id.toString());
	        databackup.put("editable","1");
	        request.getSession().setAttribute("DATABACKUP",databackup);
	        
	        request.setAttribute("descOperation","messages.inserted.success");
	              
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
        }
        request.setAttribute("modal","1");
	    return "refresh";
	}
	
	
	/** Función que devuelve los componentes de una lista de correos
	 * @see com.siga.envios.BusqListaCorreosAction#listadoComponentes(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	protected String listadoComponentes(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    EnvComponentesListaCorreoAdm listaAdm = new EnvComponentesListaCorreoAdm(this.getUserBean(request));
	    	     
	    //Si el form es de request
	    Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	    Integer idInstitucion = Integer.valueOf((String)dataBackup.get(EnvListaCorreosBean.C_IDINSTITUCION));
	    Integer idListaCorreos = Integer.valueOf((String)dataBackup.get(EnvListaCorreosBean.C_IDLISTACORREO));
	    	    
	    Vector datos=null;
        try {
            datos = listaAdm.obtenerComponentesLista(idInstitucion,idListaCorreos);
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        request.setAttribute("datos", datos);
        return "componentes";
	}
	
	/** Función que devuelve las consultas de una lista de correos
	 * @see com.siga.envios.BusqListaCorreosAction#listadoConsultas(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	protected String listadoConsultas(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    EnvListaCorreoConsultaAdm listaAdm = new EnvListaCorreoConsultaAdm(this.getUserBean(request));
	    	     
	    //Si el form es de request
	    Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	    Integer idInstitucion = Integer.valueOf((String)dataBackup.get(EnvListaCorreosBean.C_IDINSTITUCION));
	    Integer idListaCorreos = Integer.valueOf((String)dataBackup.get(EnvListaCorreosBean.C_IDLISTACORREO));
	    	    
	    Vector datos=null;
        try {
            datos = listaAdm.selectConsultasListas(String.valueOf(idInstitucion),String.valueOf(idListaCorreos));
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        request.setAttribute("datos", datos);
        return "consultas";
	}
	
	
	/** Función que inserta un nuevo componente en una lista
	 * @see com.siga.general.MasterAction#insertarComponente(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertarComponente(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
		    
		    EnvComponentesListaCorreoAdm componenteAdm = new EnvComponentesListaCorreoAdm(this.getUserBean(request));
		    
		    ListaCorreosForm form = (ListaCorreosForm) formulario;
		    
		    Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		    
		    String idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
		    String idListaCorreo = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
		    
		    //Rellenamos el nuevo Bean de componente lista de correos
		    
		    EnvComponentesListaCorreoBean componenteBean = new EnvComponentesListaCorreoBean();	    
		    Integer id = null;

	        componenteBean.setIdInstitucion(Integer.valueOf(idInstitucion));
	        componenteBean.setIdListaCorreo(Integer.valueOf(idListaCorreo));
	        componenteBean.setIdPersona(Integer.valueOf(form.getIdPersona()));
		    
	        //Ahora procedemos a insertarlo si no existe
		    if (!componenteAdm.existeComponente(idListaCorreo,idInstitucion,form.getIdPersona())){
		    	tx.begin();
		        componenteAdm.insert(componenteBean);
		        tx.commit();
		        request.setAttribute("descOperation","messages.inserted.success");	            
		    } else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    }
	           
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
        }
        return exitoRefresco("messages.inserted.success",request);
	}
	
	/** Función que inserta una nueva consultas en una lista dinámica
	 * @see com.siga.general.MasterAction#insertarConsulta(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertarConsulta(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();

		    EnvListaCorreoConsultaAdm consultaAdm = new EnvListaCorreoConsultaAdm(this.getUserBean(request));
		    ListaCorreosForm form = (ListaCorreosForm) formulario;
		    
		    Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		    
	    	String idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
	    	String idListaCorreo = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
		    
		    //2009-CGAE-119-INC-CAT-035
		    //Se separan el idConsulta del idInstitucion.
		    //si no se ha obtenido un idInstitucion se emplea el que hay guardado
		    String[] idConsultaInstitucion = form.getIdConsulta().split("&");
		    String idConsulta = idConsultaInstitucion[0];
		    String idInstitucionCon = idConsultaInstitucion[1];

		    //Rellenamos el nuevo Bean de componente lista de correos
		    EnvListaCorreoConsultaBean consultaBean = new EnvListaCorreoConsultaBean();	    
	    	consultaBean.setIdInstitucion(Integer.valueOf(idInstitucion));
	    	consultaBean.setIdListaCorreo(Integer.valueOf(idListaCorreo));
	    	consultaBean.setIdConsulta(Integer.valueOf(idConsulta));
	    	consultaBean.setIdInstitucionCon(Integer.valueOf(idInstitucionCon));

	    	tx.begin();
	    	consultaAdm.insert(consultaBean);
	    	tx.commit();
		    
	        request.setAttribute("descOperation","messages.inserted.success");
	              
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
        }
        return exitoRefresco("messages.inserted.success",request);
	}
    
	/** 
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    ListaCorreosForm form = (ListaCorreosForm)formulario;
	    String idInstitucion,idListaCorreos,dinamica;
	    Hashtable databackup = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        Vector vOcultos = form.getDatosTablaOcultos(0);
	    if (vOcultos!=null){
	        idInstitucion = (String)vOcultos.elementAt(0);
	        idListaCorreos = (String)vOcultos.elementAt(1);
	    } else {        
	        idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
	        idListaCorreos = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
        }
        databackup.put(EnvListaCorreosBean.C_IDINSTITUCION,idInstitucion);
        databackup.put(EnvListaCorreosBean.C_IDLISTACORREO,idListaCorreos);
        databackup.put("editable","1");
        
        request.getSession().setAttribute("DATABACKUP",databackup);
        
	    
	    return mostrarRegistro(mapping,formulario,request,response);
	}

	/**
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    ListaCorreosForm form = (ListaCorreosForm)formulario;
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);

        String idInstitucion = (String)vOcultos.elementAt(0);
        String idListaCorreos = (String)vOcultos.elementAt(1);
        
        Hashtable databackup;
        if (request.getSession().getAttribute("DATABACKUP")!=null){
            databackup = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        } else { 
            databackup = new Hashtable();
        }
        databackup.put(EnvListaCorreosBean.C_IDINSTITUCION,idInstitucion);
        databackup.put(EnvListaCorreosBean.C_IDLISTACORREO,idListaCorreos);
        databackup.put("editable","0");
        
        request.getSession().setAttribute("DATABACKUP",databackup);        
	    
	    return mostrarRegistro(mapping,formulario,request,response);	
	}
	
	/**
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
	    
		    ListaCorreosForm form = (ListaCorreosForm)formulario;
		    EnvListaCorreosAdm listaAdm = new EnvListaCorreosAdm (this.getUserBean(request));
		    
		    Hashtable dataBackup = (Hashtable)request.getSession().getAttribute(("DATABACKUP"));
		    
		    Hashtable htPk = new Hashtable();
		    htPk.put(EnvListaCorreosBean.C_IDINSTITUCION,dataBackup.get(EnvListaCorreosBean.C_IDINSTITUCION));
		    htPk.put(EnvListaCorreosBean.C_IDLISTACORREO,dataBackup.get(EnvListaCorreosBean.C_IDLISTACORREO));
		    
		    //Recupero el bean de la lista
	        EnvListaCorreosBean listaBean = null;

	        tx.begin();
            listaBean = (EnvListaCorreosBean)listaAdm.selectByPKForUpdate(htPk).firstElement();

            // Modificamos los valores que vienen del formulario
	        // Recordar que el bean guarda en su interior los datos antiguos
	        listaBean.setNombre(form.getNombre());
	        listaBean.setDescripcion(form.getDescripcion());	    
	    
            listaAdm.update(listaBean);
            tx.commit();
	    } catch (Exception exc) {
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},exc,tx);
	    }
	    return exitoRefresco("messages.updated.success",request);
	}
	
	/**
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
	
		    String borrarComponente = (String) request.getParameter("borrarComponente");
		    String borrarConsulta = (String)request.getParameter("borrarConsulta");
		    if (borrarComponente!=null && borrarComponente.equalsIgnoreCase("true")){
		        return borrarComponente(mapping,formulario,request,response);
		    } else if (borrarConsulta!=null && borrarConsulta.equalsIgnoreCase("true")){
		    	return borrarConsulta(mapping,formulario,request,response);
		    } else {		
			    ListaCorreosForm form = (ListaCorreosForm)formulario;
			    EnvListaCorreosAdm listaAdm = new EnvListaCorreosAdm (this.getUserBean(request));
				
				Vector vOcultos = form.getDatosTablaOcultos(0);
				
				Hashtable hash = new Hashtable();
					    
				hash.put(EnvListaCorreosBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
				hash.put(EnvListaCorreosBean.C_IDLISTACORREO, (String)vOcultos.elementAt(1));
			
				tx.begin();
		        listaAdm.delete(hash);
		        tx.commit();
		    } 
        } catch (Exception e) {
        	throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
		return this.exitoRefresco("messages.deleted.success",request);
	}

	/** Función que elimina un componente de una lista de correos
	 * @see com.siga.envios.BusqListaCorreosAction#borrarComponente(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrarComponente(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
	
		    ListaCorreosForm form = (ListaCorreosForm)formulario;
		    
			EnvComponentesListaCorreoAdm componentesAdm = new EnvComponentesListaCorreoAdm (this.getUserBean(request));
			
			Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		    
		    String idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
		    String idListaCorreo = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
			String idPersona = (String)vOcultos.get(1);
			Hashtable hash = new Hashtable();
				    
			hash.put(EnvComponentesListaCorreoBean.C_IDINSTITUCION, idInstitucion);
			hash.put(EnvComponentesListaCorreoBean.C_IDLISTACORREO, idListaCorreo);
			hash.put(EnvComponentesListaCorreoBean.C_IDPERSONA, idPersona);
		
			tx.begin();
	        componentesAdm.delete(hash);
	        tx.commit();
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);  
        }
		return this.exitoRefresco("messages.deleted.success",request);		
	}
	
	/** Función que elimina una consulta de una lista de correos dinámica
	 * @see com.siga.envios.BusqListaCorreosAction#borrarConsulta(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrarConsulta(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
	
		    ListaCorreosForm form = (ListaCorreosForm)formulario;
		    
			EnvListaCorreoConsultaAdm consultaAdm = new EnvListaCorreoConsultaAdm (this.getUserBean(request));
			
			Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		    
		    String idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
		    String idListaCorreo = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
			String idConsulta = (String)vOcultos.get(1);
			String idInstitucionCon = (String)vOcultos.get(3);
			
			Hashtable hash = new Hashtable();
				    
			hash.put(EnvListaCorreoConsultaBean.C_IDINSTITUCION, idInstitucion);
			hash.put(EnvListaCorreoConsultaBean.C_IDCONSULTA, idConsulta);
			hash.put(EnvListaCorreoConsultaBean.C_IDLISTACORREO, idListaCorreo);
			hash.put(EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON, idInstitucionCon);
			
			tx.begin();
			consultaAdm.delete(hash);
			tx.commit();
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);  
        }
		return this.exitoRefresco("messages.deleted.success",request);		
	}
	
	/** 
	 * @see com.siga.envios.BusqListaCorreosAction#mostrarRegistro(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	protected String mostrarRegistro(ActionMapping mapping, 
	        						 MasterForm formulario, 
	        						 HttpServletRequest request, 
	        						 HttpServletResponse response)
			throws SIGAException{
        
	    ListaCorreosForm form = (ListaCorreosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		
		EnvListaCorreosAdm listaAdm = new EnvListaCorreosAdm(this.getUserBean(request));
		
		Hashtable dataBackup = (Hashtable)request.getSession().getAttribute(("DATABACKUP"));
	    String idInstitucion = (String)dataBackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
	    String idListaCorreo = (String)dataBackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
		String editable = (String)dataBackup.get("editable");
		
	    Hashtable htPk = new Hashtable();
		htPk.put(EnvListaCorreosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvListaCorreosBean.C_IDLISTACORREO,idListaCorreo);
		
		EnvListaCorreosBean listaBean = null;
        try {
            listaBean = (EnvListaCorreosBean) listaAdm.selectByPK(htPk).firstElement();
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        //Si el form es de request
		form.setIdInstitucion(idInstitucion);
		form.setIdListaCorreos(idListaCorreo);
        form.setNombre(listaBean.getNombre());
        form.setDescripcion(listaBean.getDescripcion());	
        form.setDinamica(listaBean.getDinamica());
	    
        if (listaBean.getDinamica().equals("S")){
        	return "editarDinamica";
        }else{
        	return "editar";
        }
	}
	
	/**
	 * Metodo que implementa el modo enviarLista
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarLista (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {
			
			ListaCorreosForm form = (ListaCorreosForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector fila = form.getDatosTablaOcultos(0);

			// obtener idListaCorreo
			String idListaCorreo = (String)fila.get(0);
			
			request.setAttribute("idListaCorreo", idListaCorreo);		

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
	   	 }
		 return "seleccion";
	}
	
	/**
	 * MÉtodo que permite seleccionar la nueva consulta que se anhadirá a la lista de correos dinámica
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevaConsulta(ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		ListaCorreosForm form = (ListaCorreosForm)formulario;
	    
		ConConsultaAdm consultaAdm = new ConConsultaAdm (this.getUserBean(request));
		
		Hashtable databackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	    
	    String idInstitucion = (String) databackup.get(EnvListaCorreosBean.C_IDINSTITUCION);
	    String idListaCorreo = (String) databackup.get(EnvListaCorreosBean.C_IDLISTACORREO);
	    
	    try {
	        Vector datos = consultaAdm.selectConsultasListasDinamicas(idInstitucion,idListaCorreo,userBean.getProfile());
	        request.setAttribute("datos",datos);
	    } catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);	        
        }
		return "seleccionarConsulta";		
	}
		
}
