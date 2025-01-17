/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.ConCampoConsultaBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.ConCriteriosDinamicosAdm;
import com.siga.beans.ConEjecucionAdm;
import com.siga.beans.ConOperacionConsultaAdm;
import com.siga.beans.ConOperacionConsultaBean;
import com.siga.consultas.CriterioDinamico;
import com.siga.consultas.form.RecuperarConsultasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformePersonalizable;

import es.satec.businessManager.BusinessManager;



/**
 * Action del proceso Recuperar Consultas
 */
public class RecuperarConsultasAction extends MasterAction {

    
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
						
					}else if (accion.equalsIgnoreCase("inicio")){
						mapDestino = inicio(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("ejecutarConsulta")){
						mapDestino = ejecutarConsulta(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("criteriosDinamicos")){
						mapDestino = criteriosDinamicos(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("tipoEnvio")){
						mapDestino = seleccionarTipoEnvio(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("download")){
						mapDestino = download(mapping, miForm, request, response);
						
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.consultas"});
		}
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
    	String tipoConsulta = null;
    	if(mapping.getParameter() != null){
    		tipoConsulta = "listas";
    	} else if (request.getParameter("tipoConsulta") != null){
    		tipoConsulta = request.getParameter("tipoConsulta");
    	}
    	
    	RecuperarConsultasForm form = (RecuperarConsultasForm)formulario;
    	
    	try{   		
    		
	    	if (request.getParameter("noReset")!=null){
		    	if (request.getSession().getAttribute("DATABACKUP")!=null){
			    	if (request.getSession().getAttribute("DATABACKUP").getClass().getName().equals("java.util.HashMap")){
			    		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
			    		RecuperarConsultasForm f = (RecuperarConsultasForm)databackup.get("RecuperarConsultasForm");
			    		if (f!=null){			    			
			    			if (tipoConsulta!=null && tipoConsulta.equals("listas")){ //consulta de tipo lista din�mica 
			    				//request.setAttribute("tipoConsulta","listas");
			    				form.setTipoConsulta(f.getTipoConsulta());
			    				form.setDescripcion(f.getDescripcion());
			    				form.setTodos(f.isTodos());
			    			}else{ // consulta de tipo gen�rico
			    				form.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_GEN);
				    			form.setDescripcion(f.getDescripcion());
				    			form.setTodos(f.isTodos());
				    			request.setAttribute("idModulo",f.getIdModulo());
			    			}
			    		}else{
			    			if (tipoConsulta!=null && tipoConsulta.equals("listas")){
			    				form.setTipoConsulta("");
			    			}else{
			    				form.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_GEN);
			    			}
			    		}
			    		
			    	}
		    	}
	    	}else{
	    		if (tipoConsulta!=null && tipoConsulta.equals("listas")){ //consulta de tipo lista din�mica 
	    			form.setTipoConsulta("");
	    			//request.setAttribute("tipoConsulta","listas");
	    		}else{
	    			form.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_GEN);
	    		}
	    	}
    	}catch (Exception e){
    		throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
    	}
    	
    	return "inicio";
    	    	
	}
    protected String inicio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
    	RecuperarConsultasForm form = (RecuperarConsultasForm)formulario;
    	
    	try{   		
	    	form.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_GEN);
	    	request.setAttribute("accionAnterior",form.getAccionAnterior());
	    	request.setAttribute("idModulo",form.getIdModulo());
	    	
    	}catch (Exception e){
    		throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
    	}
    	
    	return "inicio";
    	    	
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
    	try{
	        RecuperarConsultasForm form = (RecuperarConsultasForm)formulario;        
	        ConConsultaAdm consAdm = new ConConsultaAdm (this.getUserBean(request));
	        UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        Vector datos = new Vector();
	        
	        if (form.isTodos()){ //est� activado el check todos
	        	datos = consAdm.selectTodasConsultas(user,form);
	        	request.setAttribute("todos", "true");
	        }else{
	        	datos = consAdm.selectBusqConsultas(user,form);
	        }        
	        
	        request.setAttribute("datos", datos);
	        
	        //pongo el formulario en backup para la vuelta desde las pestanhas
	        HashMap hm = new HashMap();
	        hm.put("RecuperarConsultasForm",form);
	        hm.put("buscar","true");
	        request.getSession().setAttribute("DATABACKUP",hm);
	        
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
        
        return "resultado";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		return mostrarRegistro(mapping,formulario,request,response,true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	
		return mostrarRegistro(mapping,formulario,request,response,false);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	
		try{
			
		    ConConsultaAdm conAdm = new ConConsultaAdm (this.getUserBean(request));
		    
		    Vector vOcultos = formulario.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
			    	    
		    hash.put(ConConsultaBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ConConsultaBean.C_IDCONSULTA, (String)vOcultos.elementAt(1));
		    
		    conAdm.delete(hash);
		    
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
	    
		return exitoRefresco("messages.deleted.success",request);
	}	
	
	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException{
        
		try{
			
			RecuperarConsultasForm form = (RecuperarConsultasForm) formulario;
			
			Vector vOcultos = formulario.getDatosTablaOcultos(0);		
	
	        String idInstitucion = (String)vOcultos.elementAt(0);
	        String idConsulta = (String)vOcultos.elementAt(1);
	        String tipoConsulta = (String)vOcultos.elementAt(2);
	        String esExperta = (String)vOcultos.elementAt(3);
	        		        	        
		    //Anhadimos parametros para las pestanhas
		    Hashtable htParametros=new Hashtable();
		    htParametros.put("idInstitucion",idInstitucion);
		    htParametros.put("idConsulta",idConsulta);
		    htParametros.put("tipoConsulta",tipoConsulta);
		    htParametros.put("editable", bEditable ? "1" : "0");
		    htParametros.put("accion", bEditable ? "edicion" : "consulta");
		    htParametros.put("buscar","true");
		    htParametros.put("consultaExperta",esExperta);
		    request.setAttribute("consulta", htParametros);
		    
		    // Metemos los datos  de la consulta en Backup.
		    HashMap datosConsulta = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable datosGenerales = new Hashtable();
		    datosGenerales.put(ConConsultaBean.C_IDINSTITUCION,idInstitucion);
		    datosGenerales.put(ConConsultaBean.C_IDCONSULTA,idConsulta);		    
			datosConsulta.put("datosGenerales",datosGenerales);			
			
			/*
			ConConsultaAdm cAdm = new ConConsultaAdm(this.getUserName(request));
			Vector v = cAdm.selectByPK(datosGenerales);
			ConConsultaBean cBean = (ConConsultaBean)v.firstElement();
			datosConsulta.put("datosParticulares",cBean);*/
			
			
			request.getSession().setAttribute("DATABACKUP",datosConsulta);

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
	    
		return "editar";
	}
	
	protected String ejecutarConsulta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
			throws SIGAException 
	{
		// Controles generales
		UsrBean userBean = this.getUserBean(request);
		RecuperarConsultasForm form = (RecuperarConsultasForm) formulario;
		ConConsultaAdm conAdm = new ConConsultaAdm(userBean);
		ConEjecucionAdm conEjeAdm = new ConEjecucionAdm(userBean);
		UserTransaction tx = null;

		try {

			if (request.getParameter("pagina") == null) {
				// Si es la primera llamada, hay que obtener la query a ejecutar, crear el paginador, y obtener la primera pagina
				HashMap databackup = (HashMap) request.getSession().getAttribute("DATABACKUP");

				// obteniendo datos de la consulta
				ConConsultaBean conBean = (ConConsultaBean) databackup.get("datosParticulares");
				String sentencia = conBean.getSentencia().toUpperCase();
				String tipoEnvio = form.getTipoEnvio();
				CriterioDinamico[] criteriosDinamicos = form.getCriteriosDinamicos();

				// registrando estadistica ejecucion - inicio
				tx = userBean.getTransactionLigera();
				tx.begin();
				conEjeAdm.registrarInicioEjecucion(conBean, "~~ Ejecuci�n de consulta");
				tx.commit();

				// preparando la ejecucion
				Hashtable ht = conAdm.procesarEjecutarConsulta(tipoEnvio, conBean, criteriosDinamicos, true);

				// A�adimos comprobaci�n de que el �ltimo order by no es parte de una subquery buscando por el par�ntesis. 
				// Esto falla cuando dentro del propio order by existe una subquery pero lo mantenemos por compatibilidad y 
				//  porque ya est� funcionando en todos los casos excepto en consultas expertas.
				// Para que no se produzcan errores en la consulta experta se da la posibilidad de suministrar la query del count sin el order by 
				// que se elimina antes de tratar las etiquetas de la consulta experta
				if (sentencia.indexOf(ClsConstants.ETIQUETASORDERBYCLOSE) != -1) {
					conBean.setSentencia(sentencia.substring(0,	sentencia.indexOf(ClsConstants.ETIQUETAORDERBYOPEN)
									+ ClsConstants.ETIQUETAORDERBYOPEN.length())
							+ sentencia.substring(sentencia.indexOf(ClsConstants.ETIQUETASORDERBYCLOSE)));
				} else {
					conBean.setSentencia(sentencia);
				}
				Hashtable htSinOrder = conAdm.procesarEjecutarConsulta(tipoEnvio, conBean, criteriosDinamicos, true);
				String sentenciaSinOrder = (String) htSinOrder.get("sentencia");
				conBean.setSentencia(sentencia);

				sentencia = (String) ht.get("sentencia");
				String[] cabeceras = (String[]) ht.get("cabeceras");
				Hashtable codigosOrdenados = (Hashtable) ht.get("codigosOrdenados");

				// registrando estadistica ejecucion - sentencia
				tx = userBean.getTransactionLigera();
				tx.begin();
				conEjeAdm.registrarSentenciaEjecucion(sentencia.replaceAll("\\r\\n|\\r|\\n", " "));
				tx.commit();

				// Ya se ha obtenido todo dependiendo del tipo de consulta.
				// Ahora se ejecuta la consulta y se obtiene la primera pagina
				tx = userBean.getTransactionLigera();
				tx.begin();

				try {
					PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(sentencia, sentenciaSinOrder,	cabeceras, codigosOrdenados);
					// END BNS

					int totalRegistros = paginador.getNumeroTotalRegistros();
					request.setAttribute("descripcion", conBean.getDescripcion());
					if (totalRegistros > 0) {
						Vector datos = paginador.obtenerPagina(1);
						databackup.put("paginador", paginador);
						databackup.put("datos", datos);
						databackup.put("cabeceras", cabeceras);
					}

					try {
						tx.commit();
					} catch (Exception e2) {
						;
					}
					// registrando estadistica ejecucion - fin
					tx = userBean.getTransactionLigera();
					tx.begin();
					conEjeAdm.registrarFinEjecucion(null);
					tx.commit();
					
					if (totalRegistros == 0) {
						return "vacia";
					}
				} catch (Exception sqle) {
					try {
						tx.rollback();
					} catch (Exception e2) {
						;
					}

					String mensaje = sqle.getMessage();

					// registrando estadistica ejecucion - fin
					tx = userBean.getTransactionLigera();
					tx.begin();
					conEjeAdm.registrarFinEjecucion(mensaje);
					tx.commit();

					if (mensaje.indexOf("TimedOutException") != -1 || mensaje.indexOf("timed out") != -1) {
						throw new SIGAException("messages.transaccion.timeout", sqle);
					} else {
						if (sqle.toString().indexOf("ORA-") != -1) {
							throw new SIGAException("messages.general.sql", sqle, new String[] { sqle.toString() });
						}
						throw sqle;
					}
				}
			} else {
				// Si no es la primera llamada, hay que obtener la pagina del request y devolver los datos correspondientes

				// obteniendo la pagina del request
				String pagina = (String) request.getParameter("pagina");
				HashMap databackup = (HashMap) request.getSession().getAttribute("DATABACKUP");
				ConConsultaBean conBean = (ConConsultaBean) databackup.get("datosParticulares");

				tx = userBean.getTransactionLigera();
				tx.begin();
				try {
					PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind) databackup.get("paginador");
					// PaginadorBind paginador = (PaginadorBind) databackup.get ("paginador");
					// Utilizamos este paginador para optimizar la respuesta de las consultas, se ha visto que utilizando el paginador
					// que no es sensitivo a mayusculas, acentos, etc. el tiempo de respuesta es de m�s del doble

					// obteniendo la pagina y devolviendola
					Vector datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					databackup.put("paginador", paginador);
					databackup.put("datos", datos);
					request.setAttribute("descripcion", conBean.getDescripcion());
				} catch (Exception sqle) {
					String mensaje = sqle.getMessage();
					if (mensaje.indexOf("TimedOutException") != -1 || mensaje.indexOf("timed out") != -1) {
						throw new SIGAException("messages.transaccion.timeout", sqle);
					} else {
						if (sqle.toString().indexOf("ORA-") != -1) {
							throw new SIGAException("messages.general.sql", sqle, new String[] { sqle.toString() });
						}
						throw sqle;
					}

				}
				tx.commit();

			}

		} catch (Exception e) {
			// registrando estadistica ejecucion - fin
			try {
				String mensaje = e.getMessage();

				tx = userBean.getTransactionLigera();
				tx.begin();
				conEjeAdm.registrarFinEjecucion(mensaje);
				tx.commit();
			} catch (Exception e2) {
				throw new SIGAException("messages.general.sql", e2, new String[] { e2.toString() });
			}

			throw new SIGAException("messages.general.sql", e, new String[] { e.toString() });
		}

		return "ejecucion";
	}	


	
	/** 
	 * Descarga el resultado de la consulta en un fichero
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		UsrBean userBean = this.getUserBean (request);
		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
		ConEjecucionAdm conEjeAdm = new ConEjecucionAdm(userBean);
		UserTransaction tx = null;
		
		try {
			ConConsultaBean conBean = (ConConsultaBean)databackup.get("datosParticulares");
			PaginadorCaseSensitiveBind p = (PaginadorCaseSensitiveBind)databackup.get("paginador");
			
			// registrando estadistica ejecucion - inicio
			tx = userBean.getTransactionLigera();
			tx.begin();
		    conEjeAdm.registrarInicioEjecucion(conBean, "~~ Descarga de consulta", p.getQueryOriginal().replaceAll("\\r\\n|\\r|\\n", " "));
		    tx.commit();
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			InformeForm informeForm = new InformeForm();
			informeForm.setIdInstitucion(userBean.getLocation());
			informeForm.setIdTipoInforme(AdmTipoInformeBean.TIPOINFORME_CONSULTAS);
			List<InformeForm> informesForms = informeService.getInformesConsulta(conBean,informeForm,userBean);
			
			if(informesForms!=null && informesForms.size()>0){
				ArrayList<File> listaFicheros = new ArrayList<File>();
				AdmInformeAdm informeAdm = new AdmInformeAdm(userBean);
				
				Vector datos = informeAdm.selectGenericoBind(p.getQueryOriginal(), p.getCodigos());
				String[] columnas = (String[]) databackup.get("cabeceras");
				
				InformePersonalizable informePersonalizable = new InformePersonalizable();
				File ficheroSalida = informePersonalizable.getFicheroGenerado(informesForms, datos,columnas, userBean);
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK","OK");
			}else{
				RowsContainer rc = new RowsContainer();
				rc.queryBind(p.getQueryOriginal(), p.getCodigos());
				request.setAttribute("datos",rc.getAll());
				request.setAttribute("descripcion",conBean.getDescripcion());
				request.setAttribute("cabeceras",databackup.get("cabeceras"));
			}
			
			// registrando estadistica ejecucion - fin
			tx = userBean.getTransactionLigera();
			tx.begin();
		    conEjeAdm.registrarFinEjecucion(null);
		    tx.commit();
			
			if(informesForms!=null && informesForms.size()>0){
				return "descarga";	
			}else{
				return "export";
			}
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch(Exception e2) {
				;
			}

			// registrando estadistica ejecucion - fin
	        try {
		        tx = userBean.getTransactionLigera();
				tx.begin();
			    conEjeAdm.registrarFinEjecucion(e.getMessage());
			    tx.commit();
			} catch (Exception e1) {
				throwExcp("messages.general.error",new String[] {"modulo.consultas"},e1,null); 
			}
			
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return "export";
	}
	
	/** 
	 * Recupera los criterios din�micos de la consulta para solicitar operaci�n y valor
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String criteriosDinamicos(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		String retorno="";
		Vector criterioReal=new Vector();
		
		try {
			
			RecuperarConsultasForm form = (RecuperarConsultasForm)formulario;
			
			//pongo datos de la consulta en backup
			HashMap databackup = null;
			if(request.getSession().getAttribute("DATABACKUP") != null){
				databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
			}
			ConConsultaAdm conAdm = new ConConsultaAdm(this.getUserBean(request));
			Hashtable h = new Hashtable();
			h.put(ConConsultaBean.C_IDINSTITUCION,form.getIdInstitucion());
			h.put(ConConsultaBean.C_IDCONSULTA,form.getIdConsulta());
			ConConsultaBean conBean = (ConConsultaBean)conAdm.selectByPK(h).firstElement();
			Vector vcd=new Vector();
			Vector operaciones=new Vector();
			Vector valores=new Vector();
			Vector tipo=new Vector();
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			
			
			if (databackup==null){
				databackup =new HashMap();
			}
			databackup.put("datosParticulares",conBean);
			
			//En funci�n del tipo de consulta, solicitamos:
			// - Si es de tipo gen�rico y existen criterios din�micos, solicitamos operaci�n y valor para los mismos.
			
			if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN) ||
					(conBean.getEsExperta().equals("1") && 
							(form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV) ||
							 form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)) )){
				String selectExperta=conBean.getSentencia().toUpperCase().replaceAll("\r\n"," ");
				String selectOriginal = conBean.getSentencia().replaceAll("\r\n"," ");
			  if (conBean.getEsExperta().equals("1")){
			  	// Vemos si la consulta experta tiene criterios dinamicos.
			  	if ((selectExperta.indexOf(ClsConstants.TIPONUMERO))>=0 || (selectExperta.indexOf(ClsConstants.TIPOTEXTO))>=0 || (selectExperta.indexOf(ClsConstants.TIPOFECHA)>=0 ||(selectExperta.indexOf(ClsConstants.TIPOMULTIVALOR))>=0 ) ){
			  		
			  		if (selectExperta.toUpperCase().indexOf("%%IDINSTITUCION%%")>=0){
			  			selectExperta=selectExperta.toUpperCase().replaceAll("%%IDINSTITUCION%%",userBean.getLocation());
			  			selectOriginal=selectOriginal.replaceAll("%%IDINSTITUCION%%",userBean.getLocation());
			  			
					}
			  		
			  		obtenerCriteriosCamposSalida(selectExperta,selectOriginal,vcd,operaciones,valores,tipo,criterioReal,request, response);
			  		
		
			  	  
					retorno="criteriosDinamicosExperta";
			  	}else{
			  	  retorno="consultaSinCriterios";
			  	}
			  }else{// no es consulta experta
				ConCriteriosDinamicosAdm cdAdm = new ConCriteriosDinamicosAdm(this.getUserBean(request));
				 vcd = cdAdm.getCriteriosDinamicos(form.getIdInstitucion(),form.getIdConsulta());			
				if (!vcd.isEmpty()){
					 operaciones = new Vector();
					 valores = new Vector();
					
					for (int i=0;i<vcd.size();i++){
						Row fila = (Row)vcd.get(i);
						String tipoCampo = fila.getString(ConCampoConsultaBean.C_TIPOCAMPO);
						
						//OPERACIONES PARA ESTE CAMPO
						String selectOper = "SELECT ";
						selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
						selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,userBean.getLanguage())+" AS DESCRIPCION";
						selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
						selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='"+tipoCampo+"'";
						
						RowsContainer rc1 = null;
						rc1 = new RowsContainer();
						rc1.query(selectOper);
						operaciones.add(rc1.getAll());
						
						//VALORES PARA ESTE CAMPO
						String selectAyuda = fila.getString(ConCampoConsultaBean.C_SELECTAYUDA);
						if (selectAyuda!=null && !selectAyuda.equals("")){
							RowsContainer rc2 = null;
							rc2 = new RowsContainer();
							rc2.query(selectAyuda);
							valores.add(rc2.getAll());
						}else{
							valores.add(null);
						}
							
					}
				
					request.setAttribute("operaciones",operaciones);
					request.setAttribute("valores",valores);
					request.setAttribute("criterios",vcd);
					retorno="criteriosDinamicos";
				}else{
					retorno="consultaSinCriterios";
				}
			  }	
			}else{
				retorno="consultaSinCriterios";
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirConParametros(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirConParametros(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		RecuperarConsultasForm form = (RecuperarConsultasForm)formulario;
		
		
		try {
			CriterioDinamico[] cDinamicos = form.getCriteriosDinamicos();
			for (int i=0;i<cDinamicos.length && cDinamicos[i]!=null;i++){
	
				// RGG Error INC_3099 Obtenemos el 'simbolo' del operador para ver si el 'is null'
				ConOperacionConsultaAdm conadm= new ConOperacionConsultaAdm(this.getUserBean(request));
				CriterioDinamico criterioDinamico = cDinamicos[i];
				
				Hashtable ht = new Hashtable();
				ht.put(ConOperacionConsultaBean.C_IDOPERACION,(String)criterioDinamico.getOp());
				Vector v = conadm.selectByPK(ht);
				ConOperacionConsultaBean  conbean = null;
				String simbolo="";
				if (v!=null && v.size()>0) {
					conbean = (ConOperacionConsultaBean) v.get(0);
					simbolo = conbean.getSimbolo();
				}
				
	
//				if (!simbolo.trim().equalsIgnoreCase("is null") && (cDinamicos[i].getVal()==null || cDinamicos[i].getVal().equals(""))){
//					throw new SIGAException("messages.consultas.error.ValoresVacios");
//				}
				
				if (criterioDinamico.getTc().equals(SIGAConstants.TYPE_NUMERIC)){
					String message = "";
					try{
						if (criterioDinamico.getVal().startsWith(".") || criterioDinamico.getVal().endsWith(".")){
							message = "messages.consultas.error.punto";
							throw new SIGAException(message);							
						}
						if (!criterioDinamico.getDc().equals("") && !criterioDinamico.getDc().equals("0")){
							message = "messages.consultas.error.CriteriosNumericos";
							Float.parseFloat(criterioDinamico.getVal());
						}else if (!criterioDinamico.getDc().equals("") && criterioDinamico.getDc().equals("0")){
							if (criterioDinamico.getVal().indexOf(".")!=-1){
								message = "messages.consultas.error.noDecimales";
								throw new SIGAException(message);							
							}
							message = "messages.consultas.error.CriteriosNumericos";
							Long.parseLong(criterioDinamico.getVal());
						}
					} catch (SIGAException e) {
						throw e;
					} catch (Exception e) {
						throw new SIGAException(message);
					}
					
				}
			}
			request.setAttribute("criterios",cDinamicos);
		
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return "exitoCriterios";
	}
	
	protected String seleccionarTipoEnvio (ActionMapping mapping,
										   MasterForm formulario,
										   HttpServletRequest request,
										   HttpServletResponse response)
			throws SIGAException
	{
		try {
			//Controles generales
			UsrBean userBean = this.getUserBean (request);
			RecuperarConsultasForm form = (RecuperarConsultasForm) formulario;
    		ConConsultaAdm conAdm = new ConConsultaAdm (userBean);
			HashMap databackup;
			
			//obteniendo datos particulares
			Hashtable h = new Hashtable ();
			h.put (ConConsultaBean.C_IDINSTITUCION, form.getIdInstitucion());
			h.put (ConConsultaBean.C_IDCONSULTA, form.getIdConsulta());
			ConConsultaBean conBean =
				(ConConsultaBean) conAdm.selectByPK (h).firstElement();
			
			Object o_databackup = request.getSession().getAttribute ("DATABACKUP");
			if (o_databackup != null) {
				if (o_databackup.getClass().getName().equals ("java.util.HashMap")) {
		    		databackup = (HashMap) o_databackup;
					databackup.put ("datosParticulares", conBean);
					request.getSession().setAttribute ("DATABACKUP", databackup);
				}
				else {
		    		databackup = new HashMap();
					databackup.put ("datosParticulares",conBean);
					request.getSession().setAttribute ("DATABACKUP", databackup);
				}
			}
			else {
	    		databackup = new HashMap();
				databackup.put ("datosParticulares",conBean);
				request.getSession().setAttribute ("DATABACKUP", databackup);
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.consultas"}, e, null); 
		}
		
		return "tipoEnvio";
	} //seleccionarTipoEnvio ()
	
	protected void obtenerCriteriosCamposSalida(String selectExperta,String selectOriginal ,Vector vcd,Vector operaciones,Vector valores,Vector tipo,Vector criterioReal,HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		Vector v_tipoDatos=new Vector();
		Vector valorDefectoVector=new Vector();
		Vector valorNuloVector=new Vector();
		String campo="";
		String alias="";
		Vector valias=new Vector();
		// Cargamos el vector de tipo de los tipos de datos de los criterios dinamicos
		v_tipoDatos.add(ClsConstants.TIPONUMERO);
		v_tipoDatos.add(ClsConstants.TIPOTEXTO);
		v_tipoDatos.add(ClsConstants.TIPOFECHA);
		v_tipoDatos.add(ClsConstants.TIPOMULTIVALOR);
		String sentencia_aux="";
		String sentenciaOriginalAux="";
		
		boolean continuar=true;
		Vector ayuda=new Vector();
		 UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		List operadoresList = new ArrayList<String>();
		operadoresList.add("=");
		operadoresList.add("!=");
		operadoresList.add(">");
		operadoresList.add(">=");
		operadoresList.add("<");
		operadoresList.add("<=");
		operadoresList.add("IS NULL");
		operadoresList.add("LIKE");
		operadoresList.add("OPERADOR");
	 try{	
//      Buscamos los criterios dinamicos que pueda haber en la sentencia select construida	 	
	 	String critCampoSalida=selectExperta;
	 	 sentencia_aux=critCampoSalida;
	 	sentenciaOriginalAux = selectOriginal;
	 	 continuar=true;
	 	for (int i=0;i<v_tipoDatos.size();i++){// Para cada tipo de datos
	 		continuar=true;
	 		sentencia_aux=critCampoSalida;
	 		sentenciaOriginalAux = selectOriginal;
	 		String operadorEncontrado = "";
	 		while ((continuar)&& (sentencia_aux.length()>0)){
	 			operadorEncontrado = "";
	 			//int pos_ini2 =sentencia_aux.lastIndexOf(v_tipoDatos.get(i).toString());
	 			int pos_ini=sentencia_aux.indexOf(v_tipoDatos.get(i).toString());
	 			if (pos_ini>=0){
	 				String sentenciaA=sentencia_aux.substring(0,pos_ini);
	 				
	 				String operadores[] = sentenciaA.split("%%");
	 				for (int j = operadores.length-1; j >= 0 ; j--) {
						String operador = operadores[j];
						if(operadoresList.contains(operador)){
							operadorEncontrado = operador;
							break;
							
						}
							
					}
	 				 
	 				//--int pos_fin = sentencia_aux.indexOf("AND");
	 				String sentenciaAyuda=sentencia_aux.substring(pos_ini);
	 				int pos_fin = sentenciaAyuda.indexOf("AND");
	 				String sentenciaAyudaOriginal = null;
	 				if(pos_fin==-1)
	 					sentenciaAyudaOriginal = sentenciaOriginalAux.substring(pos_ini);
	 				else
	 					sentenciaAyudaOriginal = sentenciaOriginalAux.substring(pos_ini,pos_ini+pos_fin);
	 				campo=getAliasMostrar(sentenciaA);
	 				vcd.add(campo);
	 				alias=getAliasCompleto(sentenciaA);
	 				valias.add(alias);
	 				int posicionValue = sentenciaAyudaOriginal.toUpperCase().indexOf(" DEFECTO ");
			 		String valorDefecto = null;
			 		if (posicionValue>=0){
			 			String valueDefecto = sentenciaAyudaOriginal.substring(posicionValue);
			 			int inicio=valueDefecto.indexOf("\"");
			 			String auxiliar =valueDefecto.substring(inicio+1) ;
						int fin=auxiliar.indexOf("\"");
						String retorno=null;
						if(inicio!=-1 && fin!=-1){
							valorDefecto=auxiliar.substring(0,fin);
						}
				    }
			 		valorDefectoVector.add(valorDefecto==null?"":valorDefecto);
			 		
			 		
			 		int posicionNulo = sentenciaAyudaOriginal.toUpperCase().indexOf(" NULO ");
			 		String valorNulo = null;
			 		if (posicionNulo>=0){
			 			String valueNulo = sentenciaAyudaOriginal.substring(posicionNulo);
			 			int inicio=valueNulo.indexOf("\"");
			 			String auxiliar =valueNulo.substring(inicio+1) ;
						int fin=auxiliar.indexOf("\"");
						String retorno=null;
						if(inicio!=-1 && fin!=-1){
							valorNulo=auxiliar.substring(0,fin);
						}
				    }
			 		valorNuloVector.add(valorNulo==null?Boolean.FALSE:valorNulo.toLowerCase().equals("si")?Boolean.TRUE:Boolean.FALSE);
			 		
			 		
	 			 if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPONUMERO)){	
	 				
	 				String selectOper = "SELECT ";
	  				selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
	  				selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,user.getLanguage())+" AS DESCRIPCION";
	  				selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
	  				selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='N'";
	  				if(!operadorEncontrado.equalsIgnoreCase("OPERADOR"))
	   					selectOper+= " AND UPPER(SIMBOLO)='"+operadorEncontrado+"' ";
	  				RowsContainer rc1 = null;
	  				rc1 = new RowsContainer();
	  				rc1.query(selectOper);
	  				operaciones.add(rc1.getAll());
	  				valores.add(null);
	  				tipo.add("N");
	  				ayuda.add("-1");
	 			 }else if(v_tipoDatos.get(i).toString().equals(ClsConstants.TIPOTEXTO)){
	 				
	 			 	 String selectOper = "SELECT ";
	   				selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
	   				selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,user.getLanguage())+" AS DESCRIPCION";
	   				selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
	   				selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='A'";
	   				if(!operadorEncontrado.equalsIgnoreCase("OPERADOR"))
	   					selectOper+= " AND UPPER(SIMBOLO)='"+operadorEncontrado+"' ";
	   				
	   				
	   				RowsContainer rc1 = null;
	   				rc1 = new RowsContainer();
	   				rc1.query(selectOper);
	   				operaciones.add(rc1.getAll());
	   				valores.add(null);
	   				tipo.add("A");
	   				ayuda.add("-1");
	 			 }else if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPOFECHA)){
	 			 	String selectOper = "SELECT ";
	  				selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
	  				selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,user.getLanguage())+" AS DESCRIPCION";
	  				selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
	  				selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='D'";
	  				if(!operadorEncontrado.equalsIgnoreCase("OPERADOR"))
	   					selectOper+= " AND UPPER(SIMBOLO)='"+operadorEncontrado+"' ";
	  				RowsContainer rc1 = null;
	  				rc1 = new RowsContainer();
	  				rc1.query(selectOper);
	  				operaciones.add(rc1.getAll());
	  				valores.add(null);
	  				if(operadorEncontrado.equalsIgnoreCase("IS NULL"))
	  					tipo.add("X");
	  				else
	  					tipo.add("D");
	  				ayuda.add("-1");
	  				if(valorDefecto!=null && valorDefecto.equalsIgnoreCase("sysdate")){
	  					valorDefectoVector.set(valorDefectoVector.size()-1, GstDate.getHoyJsp());
	  				}
	 			 }else if (v_tipoDatos.get(i).toString().equals(ClsConstants.TIPOMULTIVALOR)){
	 			 	String selectOper = "SELECT ";
	 				selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
	 				selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,user.getLanguage())+" AS DESCRIPCION";
	 				selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
	 				selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='N'";
	 				if(!operadorEncontrado.equalsIgnoreCase("OPERADOR"))
	   					selectOper+= " AND UPPER(SIMBOLO)='"+operadorEncontrado+"' ";
	 				RowsContainer rc1 = null;
	 				rc1 = new RowsContainer();
	 				rc1.query(selectOper);
	 				operaciones.add(rc1.getAll());
	 				EditarConsultaAction editarConsulta= new EditarConsultaAction();
	 				String selectAyuda =editarConsulta.ObtenerSelectAyuda(sentenciaAyuda,request);
	 				ayuda.add(selectAyuda+"%%");
	 				if (selectAyuda!=null && !selectAyuda.equals("")){
	 					RowsContainer rc2 = null;
	 					rc2 = new RowsContainer();
	 					rc2.query(selectAyuda);
	 					valores.add(rc2.getAll());
	 					tipo.add("MV");
	 				
	 				
	 			
	 				}	
	 			
	 			 }
	 				
	 			}else{
	 				continuar=false;
	 			}
	 			sentencia_aux=sentencia_aux.substring(pos_ini+v_tipoDatos.get(i).toString().length());
	 			sentenciaOriginalAux=sentenciaOriginalAux.substring(pos_ini+v_tipoDatos.get(i).toString().length());
	 		}
	 		
	 		
	 	}
	 	

	 	
  
  		request.setAttribute("operaciones",operaciones);
		request.setAttribute("valores",valores);
		request.setAttribute("valoresDefecto",valorDefectoVector);
		request.setAttribute("valoresNulo",valorNuloVector);
		request.setAttribute("criterios",vcd);
		request.setAttribute("tipo",tipo);
		request.setAttribute("alias",valias);
		request.setAttribute("ayuda",ayuda);

	} catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
	}
	}
	protected String getAliasMostrar(String sentencia)throws SIGAException {
		String operador="";
		sentencia=sentencia.toUpperCase();
		int pos_AND=sentencia.lastIndexOf(" AND ");
		int pos_OR=sentencia.lastIndexOf(" OR ");
		int pos_WHERE=sentencia.lastIndexOf("WHERE");
		int posicion=-1;
		if (pos_AND <0 && pos_OR<0){
			posicion=sentencia.toUpperCase().lastIndexOf("WHERE");
			sentencia=sentencia.substring(posicion+"WHERE".length());
			if (sentencia.toUpperCase().lastIndexOf(" AS ")>=0){// Existe Alias
				int posAs=sentencia.toUpperCase().lastIndexOf(" AS ");
				int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}

				sentencia=sentencia.substring(posAs+" AS ".length(),posEtiquetaOperador).replaceAll("\"","");



			}else{// no hay alias
				int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}
				sentencia=sentencia.substring(0,posEtiquetaOperador);

				if (sentencia.indexOf(".")>=0){
					sentencia=sentencia.substring(sentencia.indexOf(".")+1);
				}


			}

		}else{ 
			if ((pos_AND>pos_OR)&& (pos_AND>pos_WHERE)){
				operador=" AND ";
				posicion=pos_AND;
			}else if ((pos_OR>pos_AND)&& (pos_OR>pos_WHERE)){
				operador=" OR ";
				posicion=pos_OR;
			}else if ((pos_WHERE>pos_AND)&& (pos_WHERE>pos_OR)){
				operador="WHERE";
				posicion=pos_WHERE;
			}
			sentencia=sentencia.substring(posicion+operador.length());

			if (sentencia.toUpperCase().lastIndexOf(" AS ")>=0){// Existe Alias
				int posAs=sentencia.toUpperCase().lastIndexOf(" AS ");
				int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}


				sentencia=sentencia.substring(posAs+" AS ".length(),posEtiquetaOperador).replaceAll("\"","");



			}else{// no hay alias
				int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}

				sentencia=sentencia.substring(0,posEtiquetaOperador);

				if (sentencia.indexOf(".")>=0){
					sentencia=sentencia.substring(sentencia.indexOf(".")+1);
				}


			}
		}	

		return sentencia;
	}
	
	protected String getAliasCompleto(String sentencia)throws SIGAException {
		String operador="";
		int pos_AND=sentencia.lastIndexOf(" AND ");
		int pos_OR=sentencia.lastIndexOf(" OR ");
		int pos_WHERE=sentencia.lastIndexOf(" WHERE ");
		int posicion=-1;
		
		if (pos_AND <0 && pos_OR<0){
			posicion=sentencia.toUpperCase().lastIndexOf("WHERE");
			sentencia=sentencia.substring(posicion+"WHERE".length());
			if (sentencia.toUpperCase().lastIndexOf(" AS ")>=0){// Existe Alias
	            int posAs=sentencia.toUpperCase().lastIndexOf(" AS ");
	            int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}

              
       
            sentencia=sentencia.substring(posAs,posEtiquetaOperador);
       
        
		  	
		    }else{// no hay alias
		  
		     /*sentencia=sentencia.substring(0,sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR));
			
		     if (sentencia.indexOf(".")>=0){
		  	  sentencia=sentencia.substring(sentencia.indexOf(".")+1);
		     }*/
		    	sentencia="-1";
		  
			
		   }
		}else{ 
			if ((pos_AND>pos_OR)&& (pos_AND>pos_WHERE)){
				operador=" AND ";
				posicion=pos_AND;
			  }else if ((pos_OR>pos_AND)&& (pos_OR>pos_WHERE)){
			  	operador=" OR ";
			  	posicion=pos_OR;
			  }else if ((pos_WHERE>pos_AND)&& (pos_WHERE>pos_OR)){
			  	operador="WHERE";
			  	posicion=pos_WHERE;
			  }
		    sentencia=sentencia.substring(posicion+operador.length());
			
			if (sentencia.toUpperCase().lastIndexOf(" AS ")>=0){// Existe Alias
	            int posAs=sentencia.toUpperCase().lastIndexOf(" AS ");
	            int posEtiquetaOperador=sentencia.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR);
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%!=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%>=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%<=%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%IS NULL%%");
				}
				if(posEtiquetaOperador==-1){
					posEtiquetaOperador=sentencia.toUpperCase().indexOf("%%LIKE%%");
				}

              
       
             sentencia=sentencia.substring(posAs,posEtiquetaOperador);
       
        
		  	
		    }else{
		    	sentencia="-1";
		    }
		  }	
		
	 return sentencia;
	}
//	boolean flag = true;
//    Vector vIndices = new Vector();
//    while (flag) {
//        int beginIndex = textoAux.indexOf(marca)+2;
//        if(beginIndex==-1){
//            flag= false;
//            continue;
//        }
//        cadena = textoAux.substring(beginIndex);
//        int endIndex = cadena.indexOf(marca);
//        if(endIndex==-1){
//            flag= false;
//            continue;
//        }
//        int[] indices = {beginIndex,beginIndex+endIndex};
//        vIndices.add(indices);
//        textoAux = textoAux.substring(beginIndex+endIndex+2);
//        beginIndex = textoAux.indexOf(marca);
//        if(beginIndex==-1)
//            flag= false;
//    }
}
