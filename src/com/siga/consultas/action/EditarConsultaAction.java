/*
 * Created on Mar 10, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenGruposCriteriosAdm;
import com.siga.beans.CenGruposCriteriosBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ConCampoConsultaAdm;
import com.siga.beans.ConCampoConsultaBean;
import com.siga.beans.ConCamposAgregacionAdm;
import com.siga.beans.ConCamposAgregacionBean;
import com.siga.beans.ConCamposOrdenacionAdm;
import com.siga.beans.ConCamposOrdenacionBean;
import com.siga.beans.ConCamposSalidaAdm;
import com.siga.beans.ConCamposSalidaBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.ConCriterioConsultaAdm;
import com.siga.beans.ConCriterioConsultaBean;
import com.siga.beans.ConCriteriosDinamicosAdm;
import com.siga.beans.ConCriteriosDinamicosBean;
import com.siga.beans.ConModuloAdm;
import com.siga.beans.ConModuloBean;
import com.siga.beans.ConOperacionConsultaBean;
import com.siga.beans.ConTablaConsultaAdm;
import com.siga.beans.ConTablaConsultaBean;
import com.siga.beans.ConTipoCampoConsultaBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.consultas.Campo;
import com.siga.consultas.CriterioDinamico;
import com.siga.consultas.form.EditarConsultaForm;
import com.siga.consultas.form.RecuperarConsultasForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la edición de Consultas
 */
public class EditarConsultaAction extends MasterAction {
	
	/** 
	 *  Función que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
						
					} else if (accion.equalsIgnoreCase("tablaPri")){
						mapDestino = tablaPri(mapping, miForm, request, response);
					
					} else if (accion.equalsIgnoreCase("cambiarCriterio")){
						mapDestino = cambiarCriterio(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("operacionValor")){
						mapDestino = operacionValor(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("modificarSolo")){
						mapDestino = modificarSolo(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("modificarEjecutar")){
						mapDestino = modificarEjecutar(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("insertarSolo")){
						mapDestino = insertarSolo(mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("insertarEjecutar")){
						mapDestino = insertarEjecutar(mapping, miForm, request, response);
						
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
	
	
	/** 
	 * Método que obtiene las posibles operaciones y los posibles valores para un campo 
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */		
	protected String operacionValor(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
	
		try {
			//Averiguo el tipo de campo y su longitud
			EditarConsultaForm form = (EditarConsultaForm)formulario;
			ConCampoConsultaAdm ccAdm = new ConCampoConsultaAdm(this.getUserBean(request));
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			StringTokenizer st = new StringTokenizer(form.getCampo(),"#");
			String idTabla = st.nextToken();
			String idCampo = st.nextToken();
			
			Hashtable h = new Hashtable();
			h.put(ConCampoConsultaBean.C_IDCAMPO,idCampo);
			ConCampoConsultaBean ccBean = (ConCampoConsultaBean)ccAdm.selectByPK(h).firstElement();
			
			request.setAttribute("tipocampo",ccBean.getTipoCampo());
			
			Integer longitud = null; 
			Integer escala = null;
			if (ccBean.getLongitud()!=null){
				longitud = ccBean.getLongitud();
				request.setAttribute("longitud",longitud);
				if (ccBean.getEscala()!=null){
					escala = ccBean.getEscala();
					request.setAttribute("escala",escala);					
				}
			}		
			
			//recuperamos las posibles operaciones
			String selectOper = "SELECT ";
			selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
			selectOper+= UtilidadesMultidioma.getCampoMultidiomaSimple(ConOperacionConsultaBean.C_DESCRIPCION,usr.getLanguage())+" AS DESCRIPCION";
			selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
			selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='"+ccBean.getTipoCampo()+"'";
			
			RowsContainer rc1 = null;
			rc1 = new RowsContainer();
			rc1.query(selectOper);
			request.setAttribute("datosOperacion",rc1.getAll());
			
			//recuperamos los posibles valores
			if (ccBean.getSelectAyuda()!=null && !ccBean.getSelectAyuda().equals("")){
				String selectValor = ccBean.getSelectAyuda();
				selectValor=selectValor.replaceAll("%%IDIOMA%%",usr.getLanguage());
				selectValor=selectValor.replaceAll("@IDIOMA@",usr.getLanguage());
				RowsContainer rc2 = null;
		
				rc2 = new RowsContainer();
				rc2.query(selectValor);
				if (!rc2.getAll().isEmpty()){
					request.setAttribute("datosValor",rc2.getAll());
				}
			}
			
		} 
		catch (Exception e) { 	
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return "combos";
	}
	
	/** 
	 * Método que obtiene la lista de tablas elegible como tabla prioritaria
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */		
	protected String tablaPri(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		EditarConsultaForm form = (EditarConsultaForm)formulario;
		
		Vector tablas = new Vector();
		String ids = form.getTablas();
		StringTokenizer st = new StringTokenizer(ids,"#");
		while(st.hasMoreTokens()){			
			boolean existe = false;
			String s = (String)st.nextToken();
			StringTokenizer st2 = new StringTokenizer(s,",");
			String id = st2.nextToken();
			String desc = st2.nextToken();
			for (int i=0;i<tablas.size();i++){
				if(((String)((Vector)tablas.get(i)).get(0)).equals(id)){
					existe=true;
					i=tablas.size();
				}
			}
			if (!existe){
				Vector v = new Vector();
				v.add(id);
				v.add(desc);
				tablas.add(v);
			}
		}
		if (!tablas.isEmpty()){
			request.setAttribute("tablas",tablas);
		}
		
		return "tablaPri";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String consultaExperta="";
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));      
			String accion = (String)request.getParameter("accion");
			String idInstitucion = (String)request.getParameter("idInstitucion");
			accion = idInstitucion.equals(userBean.getLocation())?accion:"consulta";
			request.setAttribute("accion",accion);
			String tipoConsulta = (String)request.getParameter("tipoConsulta");
			request.setAttribute("tipoConsulta",tipoConsulta);
			consultaExperta=(String)request.getParameter("consultaExperta");
			
			//Averiguamos si estamos en el CGAE
			String esCGAE = CenVisibilidad.getNivelInstitucion(userBean.getLocation());
			if (esCGAE.equals("1")) request.setAttribute("esCGAE","true");

			EditarConsultaForm form = (EditarConsultaForm)formulario;
			
			if (!accion.equals("nuevo")){
			
				//Recupero el idConsulta
				HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
				Hashtable datosGenerales = (Hashtable)databackup.get("datosGenerales");
				String idConsulta = (String)datosGenerales.get(ConConsultaBean.C_IDCONSULTA);
				idInstitucion = (String)datosGenerales.get(ConConsultaBean.C_IDINSTITUCION);
				
				//Recupero los datos de la consulta y hago los set del formulario
				ConConsultaAdm cAdm = new ConConsultaAdm(this.getUserBean(request));
				Hashtable h = new Hashtable();
				h.put(ConConsultaBean.C_IDINSTITUCION,idInstitucion);
				h.put(ConConsultaBean.C_IDCONSULTA,idConsulta);				
				ConConsultaBean cBean = (ConConsultaBean)cAdm.selectByPKForUpdate(h).firstElement();
				
				form.setDescripcion(cBean.getDescripcion()!=null?cBean.getDescripcion():"");
				form.setGeneral(cBean.getGeneral().equals(ConConsultaAdm.CONS_GENERAL_SI)?true:false);
				if (cBean.getIdModulo()!=null){
					request.setAttribute("idModulo",String.valueOf(cBean.getIdModulo()));
				}
				if (cBean.getSentencia()!=null){
					request.setAttribute("sentenciaSelect",String.valueOf(cBean.getSentencia()));
				}
				
				//Hago el set de la tabla prioritaria, si existe
				if (cBean.getIdTabla()!=null && !cBean.getIdTabla().equals("")){
					ConTablaConsultaAdm ctcAdm = new ConTablaConsultaAdm(this.getUserBean(request));
					Hashtable ht = new Hashtable();
					ht.put(ConTablaConsultaBean.C_IDTABLA,cBean.getIdTabla());
					Vector v = ctcAdm.selectByPK(ht);
					ConTablaConsultaBean ctcBean = (ConTablaConsultaBean)v.firstElement();				
					request.setAttribute("idTablaPri",String.valueOf(ctcBean.getIdTabla()));
					request.setAttribute("descTablaPri",ctcBean.getDescripcion());
				}
				
				
				if (accion.equals("consulta")){
					ConModuloAdm cmAdm = new ConModuloAdm(this.getUserBean(request));
					Hashtable h2 = new Hashtable();
					h2.put(ConModuloBean.C_IDMODULO,cBean.getIdModulo());
					Vector v2 = cmAdm.selectByPK(h2);
					ConModuloBean cmBean = (ConModuloBean)v2.firstElement();
					form.setModuloSel(cmBean.getNombre());
				}else{
					databackup.put("datosParticulares",cBean);
				}
				
				request.setAttribute("datos",databackup); 
				String tablas ="";
				String nombreTabla="";
				if (tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){
					//Recupero los vectores de datos y los paso a la jsp
					ConCamposSalidaAdm csAdm = new ConCamposSalidaAdm(this.getUserBean(request));
					Vector vcs = csAdm.getCamposSalida(idInstitucion,idConsulta);
					if (!vcs.isEmpty()){
						request.setAttribute("camposSalida",vcs);
					}
					
					ConCamposOrdenacionAdm coAdm = new ConCamposOrdenacionAdm(this.getUserBean(request));
					Vector vco = coAdm.getCamposOrdenacion(idInstitucion,idConsulta);
					if (!vco.isEmpty()){
						request.setAttribute("camposOrdenacion",vco);
					}
					
					ConCamposAgregacionAdm caAdm = new ConCamposAgregacionAdm(this.getUserBean(request));			
					Vector vca = caAdm.getCamposAgregacion(idInstitucion,idConsulta);
					if (!vca.isEmpty()){
						request.setAttribute("camposAgregacion",vca);
					}
					
					ConCriteriosDinamicosAdm cdAdm = new ConCriteriosDinamicosAdm(this.getUserBean(request));
					Vector vcd = cdAdm.getCriteriosDinamicos(idInstitucion,idConsulta);
					if (!vcd.isEmpty()){
						request.setAttribute("criteriosDinamicos",vcd);
					}
					
					//Completo un listado con las tablas que forman parte de la consulta
					
					for (int i=0;i<vcs.size();i++){
						Row fila=(Row)vcs.elementAt(i);
						nombreTabla = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION);
						if (tablas.indexOf(nombreTabla+"#")==-1){
							tablas = tablas+nombreTabla+"#";
						}
					}
					for (int i=0;i<vcd.size();i++){
						Row fila=(Row)vcd.elementAt(i);
						nombreTabla = fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION);
						if (tablas.indexOf(nombreTabla+"#")==-1){
							tablas = tablas+nombreTabla+"#";
						}
					}
				}
				
				//Los criterios son comunes al tipo de consulta genérico y al tipo 'envíos'
				ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
				Vector vc = ccAdm.getCriteriosConsulta(idInstitucion,idConsulta);
				if (!vc.isEmpty()){
					request.setAttribute("criteriosConsulta",vc);
				}
				
				for (int i=0;i<vc.size();i++){
					Row fila=(Row)vc.elementAt(i);
					nombreTabla = fila.getString("DESCRIPCIONTIPOCAMPO");
					if (tablas.indexOf(nombreTabla+"#")==-1){
						tablas = tablas+nombreTabla+"#";
					}
				}
				
				request.setAttribute("tablas",tablas);
				
			}else{
				form.setTipoConsulta(tipoConsulta);
				form.setSelectExperta("<SELECT>\r\n\r\n</SELECT>\r\n\r\n\r\n<FROM>\r\n\r\n</FROM>\r\n\r\n\r\n<WHERE>\r\n\r\n</WHERE>\r\n\r\n\r\n<GROUPBY>\r\n\r\n</GROUPBY>\r\n\r\n\r\n<ORDERBY>\r\n\r\n</ORDERBY>\r\n");
			}
			 
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		
		
		
		if (consultaExperta!=null && consultaExperta.equals(ClsConstants.DB_TRUE)){	
			
			return "inicioExperta";
		}else{
		 	return "inicio";
		}
	
		
	}
	
	/** 
	 * Funcion que actualiza una consulta existente 
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */		
	protected String modificarSolo(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		modificarConsulta (mapping,formulario,request,response);
		return exitoRefresco("messages.updated.success",request);		
		
	}
	
	/** 
	 * Funcion que actualiza una consulta existente y la ejecuta, mostrando sus resultados
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */	
	protected String modificarEjecutar(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		modificarConsulta (mapping,formulario,request,response);
		EditarConsultaForm form = (EditarConsultaForm)formulario;
		//Recupero el idConsulta
		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP"); 
		Hashtable datosGenerales = (Hashtable)databackup.get("datosGenerales");
		String idConsulta = (String)datosGenerales.get(ConConsultaBean.C_IDCONSULTA);
		String idInstitucion = (String)datosGenerales.get(ConConsultaBean.C_IDINSTITUCION);
		
		request.setAttribute("idInstitucion",idInstitucion);
		request.setAttribute("idConsulta",idConsulta);
		request.setAttribute("tipoConsulta",form.getTipoConsulta());
		request.setAttribute("mensaje","messages.updated.success");
		
		return "exitoEjecucion";		
		
	}
	
	/** 
	 * Método que permite el cambio de la operación y el valor de un criterio
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String cambiarCriterio(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		EditarConsultaForm form = (EditarConsultaForm)formulario;
		String aTrocear = form.getCriterioModif();
		boolean haySeparadorIni = aTrocear.startsWith("#")?false:true;							
		boolean haySeparadorFin = aTrocear.endsWith("#")?false:true;
		String idCampo = "";
		String separadorIni = "";
		String separadorFin = "";
		String operacion = "";
		String valor = "";
		String idTabla="";
		
		StringTokenizer st = new StringTokenizer(aTrocear,"#");					
		if (haySeparadorIni){
			separadorIni = st.nextToken();
			idTabla=st.nextToken(); //tabla
			idCampo=st.nextToken();	
			operacion=st.nextToken(); //operacion
			try {
				valor=st.nextToken();	//valor	
			} catch (NoSuchElementException e) {
			}
		}else{
			idTabla=st.nextToken(); //tabla
			idCampo=st.nextToken();	
			operacion=st.nextToken(); //operacion
			try {
				valor=st.nextToken();	//valor
			} catch (NoSuchElementException e) {
			}
		}
		
		if (haySeparadorFin){
			separadorFin=st.nextToken();
		}
		
		try {
			//Averiguo el tipo de campo y su longitud
			ConCampoConsultaAdm ccAdm = new ConCampoConsultaAdm(this.getUserBean(request));
			Hashtable h = new Hashtable();
			h.put(ConCampoConsultaBean.C_IDCAMPO,idCampo);
			ConCampoConsultaBean ccBean = (ConCampoConsultaBean)ccAdm.selectByPK(h).firstElement();
			
			request.setAttribute("tipocampo",ccBean.getTipoCampo());
			request.setAttribute("idCampo",String.valueOf(ccBean.getIdCampo()));
			request.setAttribute("nombreCampo",ccBean.getNombreReal());
			request.setAttribute("separadorIni",separadorIni);
			request.setAttribute("separadorFin",separadorFin);
			request.setAttribute("operacion",operacion);
			request.setAttribute("valor",valor);
			request.setAttribute("idTabla",idTabla);
			
			Integer longitud = null; 
			Integer escala = null;
			if (ccBean.getLongitud()!=null){
				longitud = ccBean.getLongitud();
				request.setAttribute("longitud",longitud);
				if (ccBean.getEscala()!=null){
					escala = ccBean.getEscala();
					request.setAttribute("escala",escala);					
				}
			}		
			
			//recuperamos las posibles operaciones
			String selectOper = "SELECT ";
			selectOper+= ConOperacionConsultaBean.C_IDOPERACION+" AS ID,";
			selectOper+= ConOperacionConsultaBean.C_DESCRIPCION+" AS DESCRIPCION";
			selectOper+= " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
			selectOper+= " WHERE "+ConOperacionConsultaBean.C_TIPOOPERADOR+"='"+ccBean.getTipoCampo()+"'";
			
			RowsContainer rc1 = null;
			rc1 = new RowsContainer();
			rc1.query(selectOper);
			request.setAttribute("datosOperacion",rc1.getAll());
			
			//recuperamos los posibles valores
			if (ccBean.getSelectAyuda()!=null && !ccBean.getSelectAyuda().equals("")){
				String selectValor = ccBean.getSelectAyuda();
				RowsContainer rc2 = null;
		
				rc2 = new RowsContainer();
				rc2.query(selectValor);
				if (!rc2.getAll().isEmpty()){
					request.setAttribute("datosValor",rc2.getAll());
				}
			}
			
		} 
		catch (Exception e) { 	
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return "cambiarCriterio";
	}


	/** 
	 * Funcion que actualiza una consulta existente 
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected void modificarConsulta(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));       
		EditarConsultaForm form = (EditarConsultaForm)formulario;
		
		//Recupero el idConsulta
		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP"); 
		Hashtable datosGenerales = (Hashtable)databackup.get("datosGenerales");
		String idConsulta = (String)datosGenerales.get(ConConsultaBean.C_IDCONSULTA);
		String idInstitucion = (String)datosGenerales.get(ConConsultaBean.C_IDINSTITUCION);
		
		//Recupero el Bean de consulta para actualizarlo
		ConConsultaBean cBean = (ConConsultaBean)databackup.get("datosParticulares");	
		String esExperta=request.getParameter("experta");
		try{
			String sentencia = "";
			if (esExperta!=null && esExperta.equals(ClsConstants.DB_TRUE)){
				
				sentencia=form.getSelectExperta();
				if(analizarSentenciaExperta(sentencia,request,formulario)){
				  form.setConsultaExperta(ClsConstants.DB_TRUE);
				}
			}else{
				form.setConsultaExperta(ClsConstants.DB_FALSE);
				if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){
					sentencia = getSentencia(form,idInstitucion,idConsulta,request);
				}else if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV)||form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
					sentencia = getSentenciaListas(form,idInstitucion,idConsulta,request);
				}
			}
				
			//Iniciamos la transacción
			UserTransaction tx = userBean.getTransactionPesada();
			
			try {
			    tx.begin();   
			    
			    //Datos de la consulta
			    ConConsultaAdm cAdm = new ConConsultaAdm(this.getUserBean(request));
			    cBean.setDescripcion(form.getDescripcion());
			    cBean.setGeneral(form.isGeneral()?ConConsultaAdm.CONS_GENERAL_SI:ConConsultaAdm.CONS_GENERAL_NO);
			    if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){
			    	cBean.setIdModulo(Integer.valueOf(form.getModulo()));
			    }
			    if (form.getTablaPrioritaria()!=null && !form.getTablaPrioritaria().equals("")){
			    	cBean.setIdTabla(Integer.valueOf(form.getTablaPrioritaria()));
			    }else{
			    	cBean.setIdTabla(null);
			    }
			    cBean.setSentencia(sentencia);
			    cAdm.update(cBean);
			  if (esExperta!=null && !esExperta.equals("1")){// si no es consulta Experta 
			    if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){
				    //Borramos los datos anteriores e insertamos los campos nuevos
				    //****Campos salida
				    Campo[] camposSalida = (Campo[])form.getCamposSalida();
				    ConCamposSalidaAdm csAdm = new ConCamposSalidaAdm(this.getUserBean(request));
				    ConCamposSalidaBean csBean = new ConCamposSalidaBean();
					csAdm = new ConCamposSalidaAdm(this.getUserBean(request));
					Hashtable hcs = new Hashtable();
					hcs.put(ConCamposSalidaBean.C_IDINSTITUCION,idInstitucion);
					hcs.put(ConCamposSalidaBean.C_IDCONSULTA,idConsulta);
					String [] ccs = new String[]{ConCamposSalidaBean.C_IDINSTITUCION,ConCamposSalidaBean.C_IDCONSULTA};
					csAdm.deleteDirect(hcs,ccs);
								
					for (int i=0;i<camposSalida.length;i++){
						if (camposSalida[i]==null || camposSalida[i].getTc().equals("null")){
							break;
						}else{					
							csBean.setIdConsulta(Integer.valueOf(idConsulta));
							csBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							csBean.setCabecera(camposSalida[i].getCab());
							csBean.setOrden(new Integer(i+1));
							csBean.setIdCampo(Integer.valueOf(camposSalida[i].getIdC()));
							csAdm.insert(csBean);
						}
					}
				
				
				//****Campos ordenación
					ConCamposOrdenacionBean coBean = new ConCamposOrdenacionBean();
					ConCamposOrdenacionAdm coAdm = new ConCamposOrdenacionAdm(this.getUserBean(request));
					Hashtable hco = new Hashtable();
					hco.put(ConCamposOrdenacionBean.C_IDINSTITUCION,idInstitucion);
					hco.put(ConCamposOrdenacionBean.C_IDCONSULTA,idConsulta);
					String [] cco = new String[]{ConCamposOrdenacionBean.C_IDINSTITUCION,ConCamposOrdenacionBean.C_IDCONSULTA};
					coAdm.deleteDirect(hco,cco);
					
					Campo[] camposOrden = (Campo[])form.getCamposOrden();
					for (int i=0;i<camposOrden.length;i++){
						if (camposOrden[i]==null || camposOrden[i].getTc().equals("null")){
							break;
						}else{					
							coBean.setIdConsulta(Integer.valueOf(idConsulta));
							coBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							coBean.setOrden(new Integer(i+1));
							coBean.setIdCampo(Integer.valueOf(camposOrden[i].getIdC()));
							coAdm.insert(coBean);
						}
					}
				
				
				//****Campos agregación
					Campo[] camposAgregacion = (Campo[])form.getCamposAgregacion();
					ConCamposAgregacionBean caBean = new ConCamposAgregacionBean();
					ConCamposAgregacionAdm caAdm = new ConCamposAgregacionAdm(this.getUserBean(request));
					Hashtable hca = new Hashtable();
					hca.put(ConCamposAgregacionBean.C_IDINSTITUCION,idInstitucion);
					hca.put(ConCamposAgregacionBean.C_IDCONSULTA,idConsulta);
					String [] cca = new String[]{ConCamposAgregacionBean.C_IDINSTITUCION,ConCamposAgregacionBean.C_IDCONSULTA};
					caAdm.deleteDirect(hca,cca);
					
					//Campo[] camposAgregacion = (Campo[])form.getCamposAgregacion();
					for (int i=0;i<camposAgregacion.length;i++){
						if (camposAgregacion[i]==null || camposAgregacion[i].getTc().equals("null")){
							break;
						}else{					
							caBean.setIdConsulta(Integer.valueOf(idConsulta));
							caBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							caBean.setOrden(new Integer(i+1));
							caBean.setIdCampo(Integer.valueOf(camposAgregacion[i].getIdC()));
							caAdm.insert(caBean);
						}
					}
					
					//****Criterios Dinámicos
					Campo[] criteriosDinamicos = (Campo[])form.getCriteriosDinamicos();
					ConCriteriosDinamicosBean cdBean = new ConCriteriosDinamicosBean();
					ConCriteriosDinamicosAdm cdAdm = new ConCriteriosDinamicosAdm(this.getUserBean(request));
					Hashtable hcd = new Hashtable();
					hcd.put(ConCriteriosDinamicosBean.C_IDINSTITUCION,idInstitucion);
					hcd.put(ConCriteriosDinamicosBean.C_IDCONSULTA,idConsulta);
					String [] ccd = new String[]{ConCriteriosDinamicosBean.C_IDINSTITUCION,ConCriteriosDinamicosBean.C_IDCONSULTA};
					cdAdm.deleteDirect(hcd,ccd);
					
					//Campo[] criteriosDinamicos = (Campo[])form.getCriteriosDinamicos();
					for (int i=0;i<criteriosDinamicos.length;i++){
						if (criteriosDinamicos[i]==null || criteriosDinamicos[i].getTc().equals("null")){
							break;
						}else{					
							cdBean.setIdConsulta(Integer.valueOf(idConsulta));
							cdBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							cdBean.setOrden(new Integer(i+1));
							cdBean.setIdCampo(Integer.valueOf(criteriosDinamicos[i].getIdC()));
							cdAdm.insert(cdBean);
						}
					}
			    }
			
			//****Criterios 
				String[] criterios = (String[])form.getCriterios();
				ConCriterioConsultaBean ccBean = new ConCriterioConsultaBean();
				ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm(this.getUserBean(request));		
				Hashtable hcc = new Hashtable();
				hcc.put(ConCriterioConsultaBean.C_IDINSTITUCION,idInstitucion);
				hcc.put(ConCriterioConsultaBean.C_IDCONSULTA,idConsulta);
				String [] ccc = new String[]{ConCriterioConsultaBean.C_IDINSTITUCION,ConCriterioConsultaBean.C_IDCONSULTA};
				ccAdm.deleteDirect(hcc,ccc);
							
				for(int i=0;i<criterios.length;i++){
					if (criterios[i]==null||criterios[i].equals("null")){
						break;
					}else{													
						String aTrocear = criterios[i];
						boolean hayParentesisIni = aTrocear.startsWith("#")?false:true;					
						boolean acabaEnY = aTrocear.endsWith(".Y.")?true:false;
						boolean acabaEnO = aTrocear.endsWith(".O.")?true:false;
						boolean acabaEnNo = aTrocear.endsWith(".NO.")?true:false;
						
						if (acabaEnY){
							aTrocear = aTrocear.substring(0,aTrocear.length()-3);
							ccBean.setOperador(ConConsultaAdm.CONS_Y);
						}else if (acabaEnO){
							aTrocear = aTrocear.substring(0,aTrocear.length()-3);
							ccBean.setOperador(ConConsultaAdm.CONS_O);
						}else if (acabaEnNo){
							aTrocear = aTrocear.substring(0,aTrocear.length()-4);
							ccBean.setOperador(ConConsultaAdm.CONS_NO);
						}else{
							ccBean.setOperador(null);
						}
						
						boolean hayParentesisFin = aTrocear.endsWith("#")?false:true;
						
						StringTokenizer st = new StringTokenizer(aTrocear,"#");					
						if (hayParentesisIni){
							ccBean.setSeparadorInicio(st.nextToken());
							st.nextToken(); //tipocampo
							ccBean.setIdCampo(Integer.valueOf(st.nextToken()));
							ccBean.setIdOperacion(Integer.valueOf(st.nextToken()));
							try {
								ccBean.setValor(st.nextToken());
							} catch (java.util.NoSuchElementException ne) {
								// pongo un 1 porque no se puede insertar nulo??
								ccBean.setValor(" ");
							}
						}else{
							ccBean.setSeparadorInicio(null);
							st.nextToken(); //tipocampo
							ccBean.setIdCampo(Integer.valueOf(st.nextToken()));
							ccBean.setIdOperacion(Integer.valueOf(st.nextToken()));
							try {
								ccBean.setValor(st.nextToken());
							} catch (java.util.NoSuchElementException ne) {
								// pongo un 1 porque no se puede insertar nulo??
								ccBean.setValor(" ");
							}
						}
						if (hayParentesisFin){
							ccBean.setSeparadorFin(st.nextToken());
						}else{
							ccBean.setSeparadorFin(null);
						}
						ccBean.setOrden(String.valueOf(i+1));
						ccBean.setIdConsulta(Integer.valueOf(idConsulta));
						ccBean.setIdInstitucion(Integer.valueOf(idInstitucion));
						
						ccAdm.insert(ccBean);
					}
				}
				
				updateGrupoCriterio(request, userBean, form, idConsulta,
						idInstitucion, sentencia);
			  }	
			  
			  //2009-CGAE-119-INC-CAT-035
			  //si es experta y de tipo F también se actualiza el GrupoCriterio para poder usarlo
			  //en la facturación. 
			  else if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC))	{
					ConConsultaAdm conAdm = new ConConsultaAdm(this.getUserBean(request));
					ConConsultaBean conBean = new ConConsultaBean();
					conBean.setEsExperta("1");
					conBean.setSentencia(sentencia);
					conBean.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_FAC);
					conBean.setIdInstitucion(Integer.valueOf(idInstitucion));
					conBean.setIdConsulta(Integer.valueOf(idConsulta));
					//La sentencia se debe insertar con los parámetros resueltos.
					//Como es una consulta de facturacion, el parametro idTipoEnvio es innecesario y se pasa como null
					//Tampoco son necesarios los criterios dinamicos y se pasa un array vacio.
					Hashtable ht = conAdm.procesarEjecutarConsulta(null, conBean, new CriterioDinamico[0], false);
					String sql = (String) ht.get("sentencia");
					Hashtable codigosOrdenados = (Hashtable) ht.get("codigosOrdenados");
					//sustituir los parametros
					for(int i = 1;  i<=codigosOrdenados.size(); i++){
						sql = sql.replaceFirst(":"+i, (String)codigosOrdenados.get(new Integer(i)));
					}
					updateGrupoCriterio(request, userBean, form, idConsulta, idInstitucion, sql);
			  }

			tx.commit();
				
			} catch (Exception e) {
				throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,tx); 
			}	
		}
		catch (SIGAException e) {
			throw e;	
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		} 
	}


	private void updateGrupoCriterio(HttpServletRequest request,
			UsrBean userBean, EditarConsultaForm form, String idConsulta,
			String idInstitucion, String sentencia) throws ClsExceptions {
		if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
			
			CenGruposCriteriosAdm grAdm = new CenGruposCriteriosAdm(this.getUserBean(request));
			CenGruposCriteriosBean grBean = new CenGruposCriteriosBean();
			
			Hashtable h = new Hashtable();
			h.put(CenGruposCriteriosBean.C_IDINSTITUCION,idInstitucion);
			h.put(CenGruposCriteriosBean.C_IDCONSULTA,idConsulta);
			Vector v = grAdm.selectForUpdate(h);
								
			if (v!=null && v.size()>0) {
				grBean = (CenGruposCriteriosBean)v.firstElement();
				
				grBean.setNombre(form.getDescripcion());
				grBean.setSentencia(sentencia);
				
				grAdm.update(grBean);
				}
				else{ //no se guardo bien en el alta, insertamos ahora
									
					grBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
					grBean.setIdGruposCriterios(grAdm.getNewIdGruposCriterios(userBean.getLocation()));
					grBean.setNombre(form.getDescripcion());
					grBean.setSentencia(sentencia);
					grBean.setIdConsulta(Integer.valueOf(idConsulta));
					grAdm.insert(grBean);
				}
		}
	}
	
	/** 
	 * Funcion que inserta una nueva consulta  
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */	
	protected String insertarSolo(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		insertarConsulta (mapping,formulario,request,response);
		return "exitoInsercion";	
		
	}
	
	/** 
	 * Funcion que inserta una nueva consulta y la ejecuta, mostrando sus resultados
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */	
	protected String insertarEjecutar(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		insertarConsulta (mapping,formulario,request,response);
		request.setAttribute("ejecucion","ejecucion");
		return "exitoInsercion";	
		
	}

	/** 
	 * Funcion que inserta una nueva consulta 
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected void insertarConsulta(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		try {
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));       
			EditarConsultaForm form = (EditarConsultaForm)formulario;
			
			ConConsultaAdm cAdm = new ConConsultaAdm(this.getUserBean(request));
			ConConsultaBean cBean = new ConConsultaBean();
			
			String idInstitucion = userBean.getLocation();
			String idConsulta = String.valueOf(cAdm.getNewIdConsulta(idInstitucion));
						
			String sentencia = "";
			String esExperta=request.getParameter("experta");
			
			if (esExperta!=null && esExperta.equals(ClsConstants.DB_TRUE)){
				
				sentencia=form.getSelectExperta();
				if(analizarSentenciaExperta(sentencia,request,formulario)){
				  form.setConsultaExperta(ClsConstants.DB_TRUE);
				}
			}
			else{
				form.setConsultaExperta(ClsConstants.DB_FALSE);
				if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN)){
					sentencia = getSentencia(form,idInstitucion,idConsulta,request);
				}else if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV)||form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
					sentencia = getSentenciaListas(form,idInstitucion,idConsulta,request);
				}
		    }	
			
			//Iniciamos la transacción
			tx = userBean.getTransactionPesada();
			
			
			    tx.begin();   
			    
			    //Datos de la consulta
			    
			    cBean.setDescripcion(form.getDescripcion());
			    cBean.setGeneral(form.isGeneral()?ConConsultaAdm.CONS_GENERAL_SI:ConConsultaAdm.CONS_GENERAL_NO);
			    if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV)){
			    	cBean.setIdModulo(new Integer(4));//módulo envíos a grupos
			    }else if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
			    	cBean.setIdModulo(new Integer(6));//módulo facturación
			    }else{
			    	cBean.setIdModulo(Integer.valueOf(form.getModulo()));
			    }
			    
			    if (form.getTablaPrioritaria()!=null && !form.getTablaPrioritaria().equals("")){
			    	cBean.setIdTabla(Integer.valueOf(form.getTablaPrioritaria()));
			    }else{
			    	cBean.setIdTabla(null);
			    }
			    cBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			    cBean.setIdConsulta(Integer.valueOf(idConsulta));
			    cBean.setTipoConsulta(form.getTipoConsulta());
			    cBean.setSentencia(sentencia);
			    cBean.setEsExperta(form.getConsultaExperta());
			    cAdm.insert(cBean);
			    
			  if (esExperta!=null && !esExperta.equals("1")){// si no es consulta Experta
			    if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_GEN) ){
				    //****Campos salida
				    Campo[] camposSalida = (Campo[])form.getCamposSalida();
				    ConCamposSalidaBean csBean = new ConCamposSalidaBean();
					ConCamposSalidaAdm csAdm = new ConCamposSalidaAdm(this.getUserBean(request));
					for (int i=0;i<camposSalida.length;i++){
						if (camposSalida[i]==null || camposSalida[i].getTc().equals("null")){
							break;
						}else{					
							csBean.setIdConsulta(Integer.valueOf(idConsulta));
							csBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							csBean.setCabecera(camposSalida[i].getCab());
							csBean.setOrden(new Integer(i+1));
							csBean.setIdCampo(Integer.valueOf(camposSalida[i].getIdC()));
							csAdm.insert(csBean);
						}
					}
				
				
				//****Campos ordenación
					ConCamposOrdenacionBean coBean = new ConCamposOrdenacionBean();
					ConCamposOrdenacionAdm coAdm = new ConCamposOrdenacionAdm(this.getUserBean(request));
					Campo[] camposOrden = (Campo[])form.getCamposOrden();
					for (int i=0;i<camposOrden.length;i++){
						if (camposOrden[i]==null || camposOrden[i].getTc().equals("null")){
							break;
						}else{					
							coBean.setIdConsulta(Integer.valueOf(idConsulta));
							coBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							coBean.setOrden(new Integer(i+1));
							coBean.setIdCampo(Integer.valueOf(camposOrden[i].getIdC()));
							coAdm.insert(coBean);
						}
					}
				
				
				//****Campos agregación
					ConCamposAgregacionBean caBean = new ConCamposAgregacionBean();
					ConCamposAgregacionAdm caAdm = new ConCamposAgregacionAdm(this.getUserBean(request));
					Campo[] camposAgregacion = (Campo[])form.getCamposAgregacion();
					for (int i=0;i<camposAgregacion.length;i++){
						if (camposAgregacion[i]==null || camposAgregacion[i].getTc().equals("null")){
							break;
						}
						else{					
							caBean.setIdConsulta(Integer.valueOf(idConsulta));
							caBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							caBean.setOrden(new Integer(i+1));
							caBean.setIdCampo(Integer.valueOf(camposAgregacion[i].getIdC()));
							caAdm.insert(caBean);
						}
					}
					
					//****Criterios Dinámicos
					ConCriteriosDinamicosBean cdBean = new ConCriteriosDinamicosBean();
					ConCriteriosDinamicosAdm cdAdm = new ConCriteriosDinamicosAdm(this.getUserBean(request));
					Campo[] criteriosDinamicos = (Campo[])form.getCriteriosDinamicos();
					for (int i=0;i<criteriosDinamicos.length;i++){
						if (criteriosDinamicos[i]==null || criteriosDinamicos[i].getTc().equals("null")){
							break;
						}else{					
							cdBean.setIdConsulta(Integer.valueOf(idConsulta));
							cdBean.setIdInstitucion(Integer.valueOf(idInstitucion));
							cdBean.setOrden(new Integer(i+1));
							cdBean.setIdCampo(Integer.valueOf(criteriosDinamicos[i].getIdC()));
							cdAdm.insert(cdBean);
						}
					}
			    }
			
			//****Criterios 
				String[] criterios = (String[])form.getCriterios();
				ConCriterioConsultaBean ccBean = new ConCriterioConsultaBean();
				ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm(this.getUserBean(request));		
				for(int i=0;i<criterios.length;i++){
					if (criterios[i]==null ||criterios[i].equals("null")){
						break;
					}else{													
						String aTrocear = criterios[i];
						boolean hayParentesisIni = aTrocear.startsWith("#")?false:true;					
						boolean acabaEnY = aTrocear.endsWith(".Y.")?true:false;
						boolean acabaEnO = aTrocear.endsWith(".O.")?true:false;
						boolean acabaEnNo = aTrocear.endsWith(".NO.")?true:false;
						
						if (acabaEnY){
							aTrocear = aTrocear.substring(0,aTrocear.length()-3);
							ccBean.setOperador(ConConsultaAdm.CONS_Y);
						}else if (acabaEnO){
							aTrocear = aTrocear.substring(0,aTrocear.length()-3);
							ccBean.setOperador(ConConsultaAdm.CONS_O);
						}else if (acabaEnNo){
							aTrocear = aTrocear.substring(0,aTrocear.length()-4);
							ccBean.setOperador(ConConsultaAdm.CONS_NO);
						}else{
							ccBean.setOperador(null);
						}
						
						boolean hayParentesisFin = aTrocear.endsWith("#")?false:true;
						
						StringTokenizer st = new StringTokenizer(aTrocear,"#");					
						if (hayParentesisIni){
							ccBean.setSeparadorInicio(st.nextToken());
							st.nextToken(); //tipocampo
							ccBean.setIdCampo(Integer.valueOf(st.nextToken()));
							ccBean.setIdOperacion(Integer.valueOf(st.nextToken()));
							try {
								ccBean.setValor(st.nextToken());
							} catch (java.util.NoSuchElementException ne) {
								// pongo un 1 porque no se puede insertar nulo??
								ccBean.setValor(" ");
							}
						}else{
							ccBean.setSeparadorInicio(null);
							st.nextToken(); //tipocampo
							ccBean.setIdCampo(Integer.valueOf(st.nextToken()));
							ccBean.setIdOperacion(Integer.valueOf(st.nextToken()));
							try {
								ccBean.setValor(st.nextToken());
							} catch (java.util.NoSuchElementException ne) {
								// pongo un 1 porque no se puede insertar nulo??
								ccBean.setValor(" ");
							}
						}
						if (hayParentesisFin){
							ccBean.setSeparadorFin(st.nextToken());
						}else{
							ccBean.setSeparadorFin(null);
						}
						ccBean.setOrden(String.valueOf(i+1));
						ccBean.setIdConsulta(Integer.valueOf(idConsulta));
						ccBean.setIdInstitucion(Integer.valueOf(idInstitucion));
						
						ccAdm.insert(ccBean);
						
					}
				}
				
				insertarGrupoCriterio(request, userBean, form, idConsulta, sentencia);

			  }
			  
			  //2009-CGAE-119-INC-CAT-035
			  //si es experta y de tipo F también se inserta el GrupoCriterio para poder usarlo
			  //en la facturación. 
			  else if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC))	{
					ConConsultaAdm conAdm = new ConConsultaAdm(this.getUserBean(request));
					ConConsultaBean conBean = new ConConsultaBean();
					conBean.setEsExperta("1");
					conBean.setSentencia(sentencia);
					conBean.setTipoConsulta(ConConsultaAdm.TIPO_CONSULTA_FAC);
					conBean.setIdInstitucion(Integer.valueOf(idInstitucion));
					conBean.setIdConsulta(Integer.valueOf(idConsulta));
					//La sentencia se debe insertar con los parámetros resueltos.
					//Como es una consulta de facturacion, el parametro idTipoEnvio es innecesario y se pasa como null
					//Tampoco son necesarios los criterios dinamicos y se pasa un array vacio.
					Hashtable ht = conAdm.procesarEjecutarConsulta(null, conBean, new CriterioDinamico[0], false);
					String sql = (String) ht.get("sentencia");
					Hashtable codigosOrdenados = (Hashtable) ht.get("codigosOrdenados");
					//sustituir los parametros
					for(int i = 1;  i<=codigosOrdenados.size(); i++){
						sql = sql.replaceFirst(":"+i, (String)codigosOrdenados.get(new Integer(i)));
					}
					insertarGrupoCriterio(request, userBean, form, idConsulta, sql);
			  }
			  
			  tx.commit();
			

			
			request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idConsulta",idConsulta);
			request.setAttribute("tipoConsulta",form.getTipoConsulta());
			request.setAttribute("consultaExperta",form.getConsultaExperta());
			request.setAttribute("mensaje","messages.updated.success");
						
			

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,tx); 
		
		} 		
		
	}


	private void insertarGrupoCriterio(HttpServletRequest request,
			UsrBean userBean, EditarConsultaForm form, String idConsulta,
			String sentencia) throws ClsExceptions {
		if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
			
			CenGruposCriteriosAdm grAdm = new CenGruposCriteriosAdm(this.getUserBean(request));
			CenGruposCriteriosBean grBean = new CenGruposCriteriosBean();
			
			grBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
			grBean.setIdGruposCriterios(grAdm.getNewIdGruposCriterios(userBean.getLocation()));
			grBean.setNombre(form.getDescripcion());
			grBean.setSentencia(sentencia);
			grBean.setIdConsulta(Integer.valueOf(idConsulta));
			
			grAdm.insert(grBean);
		}
	}
	
	
	/** 
	 * Funcion que devuelve la sentencia completa a partir de los campos, criterios, etc. 
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected String getSentencia(EditarConsultaForm form, String idInstitucion, String idConsulta, HttpServletRequest request) throws SIGAException {
		

		String select = "";
		String from = "";
		String where = "";
		String groupBy = "";
		String orderBy = "";		
		boolean casoCenPersona = false; //Para saber si estamos en el caso de consultar de la tabla cen_persona debo hacer posteriormente un join con cen_cliente por idinstitucion.
		
		try {
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));     
			
			Campo[] camposSalida = (Campo[])form.getCamposSalida();
			String[] criterios = (String[])form.getCriterios();
			Campo[] criteriosDinamicos = (Campo[])form.getCriteriosDinamicos();
			
			//Debe existir algún campo de salida
			if (camposSalida[0]==null || camposSalida[0].getTc().equals("null")){
				throw new SIGAException("messages.consultas.error.SalidaVacia");			
			}
			
			//Si existe tabla prioritaria, debe haber algún campo de salida de esa tabla
			if (form.getTablaPrioritaria()!=null && !form.getTablaPrioritaria().equals("")){
				boolean correcto = false;
				for (int i=0;camposSalida[i]!=null && !camposSalida[i].getTc().equals("null");i++){
					String tc = camposSalida[i].getTc();
					if (form.getTablaPrioritaria().equals(tc)){
						correcto = true;
						break;
					}					
				}
				if (!correcto){
					throw new SIGAException("messages.consultas.error.tablaIncorrecta");	
				}
			}
						
			//Debe haber unión entre tablas
			Vector t = new Vector();
			for (int i=0;camposSalida[i]!=null && !camposSalida[i].getTc().equals("null");i++){
				String tc = camposSalida[i].getTc();
				boolean incluido = false;
				for (int j=0;j<t.size()&&!incluido;j++){
					incluido = false;
					if (((String)t.get(j)).equals(tc)){
						incluido=true;
					}
				}
				if (!incluido){
					t.add(tc);
				}
			}
			for (int i=0;criteriosDinamicos[i]!=null && !criteriosDinamicos[i].getTc().equals("null");i++){
				String tc = criteriosDinamicos[i].getTc();
				boolean incluido = false;
				for (int j=0;j<t.size()&&!incluido;j++){
					incluido = false;
					if (((String)t.get(j)).equals(tc)){
						incluido=true;
					}
				}
				if (!incluido){
					t.add(tc);
				}
			}
			for (int i=0;criterios[i]!=null && !criterios[i].equals("null");i++){
				boolean hayParentIni = !criterios[i].startsWith("#");
				StringTokenizer st = new StringTokenizer(criterios[i],"#");
				String tc = "";
				if (hayParentIni){
					st.nextToken(); //parentesis
					tc = st.nextToken();
				}else{
					tc=st.nextToken();
				}
				boolean incluido = false;
				for (int j=0;j<t.size()&&!incluido;j++){
					incluido = false;
					if (((String)t.get(j)).equals(tc)){
						incluido=true;
					}
				}
				if (!incluido){
					t.add(tc);
				}
			}
			
			//En la siguiente variable advierto si he usado la tabla de Cen_Persona:
			boolean usoDeCenPersona = false;
			
			if (!t.isEmpty()){
				String[] tablas = new String[t.size()];
				ConTablaConsultaAdm tcAdm = new ConTablaConsultaAdm(this.getUserBean(request));
				for (int i=0;i<t.size();i++){					
					Hashtable h = new Hashtable();
					h.put(ConTablaConsultaBean.C_IDTABLA,(String)t.get(i));
					ConTablaConsultaBean tcBean = (ConTablaConsultaBean)tcAdm.selectByPK(h).firstElement();
					tablas[i]=tcBean.getDescripcion();
					
					//Controlo si se ha consultado Cen_Persona:
					if (tcBean.getDescripcion().equalsIgnoreCase(CenPersonaBean.T_NOMBRETABLA))
						usoDeCenPersona = true;
				}
				
				//Controlo si solo se ha consultado Cen_Persona:
				if (usoDeCenPersona && tablas.length==1)
					casoCenPersona = true;
				
				//Si existe un objeto ConsultasBD en sesión, lo uso. Si no, lo creo y lo guardo en la sesión.
				ConsultasBD consultasBD = null;
				consultasBD = (ConsultasBD)request.getSession().getAttribute("consultasBD");
				if (consultasBD==null){
					consultasBD = new ConsultasBD();
					request.getSession().setAttribute("consultasBD",consultasBD);
				}
				
				ClsLogging.writeFileLog("-------------------------------------------------", 10); 
				ClsLogging.writeFileLog("  idConsulta: " + idConsulta, 10); 
				ClsLogging.writeFileLog("-------------------------------------------------", 10);

				Hashtable h = consultasBD.getFromWhere(tablas);				
				if (h==null || !h.get(Grafo.ESTADO).equals(Grafo.OK_HAY_CAMINO)){
					throw new SIGAException("messages.consultas.error.UnionTablas");
				}else{
					from = " FROM "+h.get(Grafo.FROM);
					
					//Para el caso de tener unicamente la tabla cen_persona hago posteriormente un join para filtrar por institucion:
					if (casoCenPersona)
						from += ","+CenClienteBean.T_NOMBRETABLA;
					
					if (!((String)h.get(Grafo.WHERE)).equals("")){
						where = " WHERE "+h.get(Grafo.WHERE);
					}
				}
				
				//Comprobamos si alguna de las tablas contiene un campo idinstitucion.
				//Si lo contiene, filtramos por la institucion actual.
				ConCampoConsultaAdm ccAdm = new ConCampoConsultaAdm(this.getUserBean(request));
				String inst = ccAdm.getTablaConInstitucion(t);
				if (!inst.equals("")){
					if (where.equals("")){
						if (form.isGeneral()){// si la consulta es general, es decir, que es visible para todos los colegios, el idinstitucion
							                  // es el de cada colegio en particular y no el del CGAE que es el que hace la
							                  // consulta.
							where += " WHERE "+inst+"=%%idinstitucion%%";	
						}else{
						where += " WHERE "+inst+"="+userBean.getLocation();
						}
					}else{
						if (form.isGeneral()){
							where += " AND "+inst+"=%%idinstitucion%%";	
						}else{		
						    where += " AND "+inst+"="+userBean.getLocation();
						}
					}
				}

				
				
				//Ver si aqui es donde anhado join con cen_cliente para filtrar por idinstitucion:				
				if (casoCenPersona){
					if (where.equals("")) {
						if (form.isGeneral()){
						where += " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"=%%idinstitucion%%"+
								 " AND "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;
						}else{
							where += " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation()+
							 " AND "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;
						}
					} else {
						if (form.isGeneral()){	
						where += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"=%%idinstitucion%%"+
							     " AND "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;
						}else{
							where += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation()+
						     " AND "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"="+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA;	
						}
					}
				}
				
			}
			
			
			//Concuerdan los paréntesis abiertos y cerrados			
			String criteriosConcat="";
			for (int i=0;i<criterios.length;i++){
				if (criterios[i]==null || criterios[i].equals("null")){
					if (i>0){
						if (criterios[i-1]!=null && (criterios[i-1].endsWith(".Y.") || criterios[i-1].endsWith(".O.") || criterios[i-1].endsWith(".NO."))){
							//El último criterio no puede acabar con un operador lógico
							throw new SIGAException("messages.consultas.error.operador2");
						}
					}
					i=criterios.length;
				}else{
					criteriosConcat += criterios[i];
				}
				
			}				
			
			int posC = 0;
			int posA = 0;
			
			if ((posC = criteriosConcat.indexOf(")"))>=0){
				while ((posC = criteriosConcat.indexOf(")"))>=0){//existe algún paréntesis cerrado
					//posC = criteriosConcat.indexOf(")");
					criteriosConcat = criteriosConcat.substring(0,posC)+criteriosConcat.substring(posC+1,criteriosConcat.length());
					posA = criteriosConcat.lastIndexOf("(",posC-1);
					if(posA > -1){//existe el correspondiente paréntesis abierto
						if (posA ==0){
							criteriosConcat = criteriosConcat.substring(1);
						}else{
							criteriosConcat = criteriosConcat.substring(0,posA)+criteriosConcat.substring(posA+1,criteriosConcat.length());	
						}			
					}else{
						throw new SIGAException("messages.consultas.error.Parentesis");
					}
				}
				if ((posA = criteriosConcat.lastIndexOf("("))>=0){//Existe un paréntesis abierto pero no cerrado
					throw new SIGAException("messages.consultas.error.Parentesis");
				}
			}else{
				if ((posA = criteriosConcat.lastIndexOf("("))>=0){//Existe un paréntesis abierto pero no cerrado
					throw new SIGAException("messages.consultas.error.Parentesis");
				}
			}
		
		
		
			//CONSTRUYO LA SELECT
				
			//Los campos de agregación tienen que estar entre los campos de salida			
			Campo[] camposAgregacion = (Campo[])form.getCamposAgregacion();
			if (camposAgregacion[0]!=null && !camposAgregacion[0].getTc().equals("null")){			
				
				for(int i=0;i<SIGAConstants.TAMANYO_ARRAY_CONSULTA&&camposAgregacion[i]!=null&&!camposAgregacion[i].getTc().equals("null");i++){
					boolean existe = false;
					String agregacion = camposAgregacion[i].getIdC();
					for (int j=0;j<SIGAConstants.TAMANYO_ARRAY_CONSULTA&&!existe&&camposSalida[j]!=null&&!camposSalida[j].getTc().equals("null");j++){
						String salida = camposSalida[j].getIdC();
						if (salida.equals(agregacion)){
							existe=true;
						}else{
							existe=false;
						}
					}
					if (!existe){
						throw new SIGAException("messages.consultas.error.CamposAgregacion");	
					}
				}	
				
				ConCamposAgregacionAdm caAdm = new ConCamposAgregacionAdm(this.getUserBean(request));
				groupBy = caAdm.getGroupBy(camposAgregacion);
								
			}
			
			// Si existen campos de agregacion, los campos de ordenacion deben estar entre los campos de salida
			Campo[] camposOrdenacion = (Campo[])form.getCamposOrden();
			if (camposOrdenacion[0]!=null && !camposOrdenacion[0].getTc().equals("null")){
				if (camposAgregacion[0]!=null && !camposAgregacion[0].getTc().equals("null")){
					for(int i=0;i<SIGAConstants.TAMANYO_ARRAY_CONSULTA&&camposOrdenacion[i]!=null&&!camposOrdenacion[i].getTc().equals("null");i++){
						boolean existe2 = false;
						String ordenacion = camposOrdenacion[i].getIdC();
						for (int j=0;j<SIGAConstants.TAMANYO_ARRAY_CONSULTA&&!existe2&&camposSalida[j]!=null&&!camposSalida[j].getTc().equals("null");j++){
							String salida = camposSalida[j].getIdC();
							if (salida.equals(ordenacion)){
								existe2=true;
							}else{
								existe2=false;
							}
						}
						if (!existe2){
							throw new SIGAException("messages.consultas.error.CamposOrdenacion");	
						}
					}	
				}
				
				ConCamposOrdenacionAdm coAdm = new ConCamposOrdenacionAdm(this.getUserBean(request));
				orderBy = coAdm.getOrderBy(camposOrdenacion);
			}	
			
			ConCamposSalidaAdm csAdm = new ConCamposSalidaAdm(this.getUserBean(request));
			//SELECT
			select += csAdm.getCamposSalidaParaQuery(camposSalida);
			
			//FROM
			select += from;
			
			//WHERE						
			if (criteriosDinamicos[0]!=null && !criteriosDinamicos[0].getTc().equals("null")){
				if (where.equals("")){
					where += " WHERE "+ConCriteriosDinamicosAdm.CONS_DINAMICOS;
				}else{
					where += " AND "+ConCriteriosDinamicosAdm.CONS_DINAMICOS;
				}
			}	
			
			if (criterios[0]!=null && !criterios[0].equals("") && !criterios[0].equals("null")){
				if (where.equals("")){
					where += " WHERE "+ConCriterioConsultaAdm.CONS_CRITERIOS;
				}else{
					where += " AND ("+ConCriterioConsultaAdm.CONS_CRITERIOS+")";
				}
			}
			select += where;
			
			//GROUP BY
			select += " "+groupBy;	
		
			//ORDER BY
			select += " "+orderBy;				
			
			// RGG INC_3269 21/02/2007 Se intentan sustituir los parametros de las funciones de cen_colegiado
			ConCriterioConsultaAdm concritcon = new ConCriterioConsultaAdm(this.getUserBean(request));
			select = concritcon.sustituirParametrosColegiado(select);
			
			
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		
		return select;
	}
	
	/** 
	 * Funcion que devuelve la sentencia completa de la consulta de listas dinámicas.
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected String getSentenciaListas(EditarConsultaForm form, String idInstitucion, String idConsulta, HttpServletRequest request) throws SIGAException {
		

		String select = "";
		String from = "";
		String where = "";
		boolean isEnvios = form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV)?true:false;
		
		try {
			
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String[] criterios = (String[])form.getCriterios();
									
			//Debe haber unión entre tablas
			Vector t = new Vector();
			String idTablaClientes="27";
			String idTablaDirecciones="39";
			t.add(idTablaClientes);
			if (isEnvios) t.add(idTablaDirecciones);
			
			for (int i=0;criterios[i]!=null && !criterios[i].equals("null");i++){
				boolean hayParentIni = !criterios[i].startsWith("#");
				StringTokenizer st = new StringTokenizer(criterios[i],"#");
				String tc = "";
				if (hayParentIni){
					st.nextToken(); //parentesis
					tc = st.nextToken();
				}else{
					tc=st.nextToken();
				}
				boolean incluido = false;
				for (int j=0;j<t.size()&&!incluido;j++){
					incluido = false;
					if (((String)t.get(j)).equals(tc)){
						incluido=true;
					}
				}
				if (!incluido){
					t.add(tc);
				}
			}
			
			if (!t.isEmpty()){
				String[] tablas = new String[t.size()];
				for (int i=0;i<t.size();i++){
					ConTablaConsultaAdm tcAdm = new ConTablaConsultaAdm(this.getUserBean(request));
					Hashtable h = new Hashtable();
					h.put(ConTablaConsultaBean.C_IDTABLA,(String)t.get(i));
					ConTablaConsultaBean tcBean = (ConTablaConsultaBean)tcAdm.selectByPK(h).firstElement();
					tablas[i]=tcBean.getDescripcion();
				}
				
				//Si existe un objeto ConsultasBD en sesión, lo uso. Si no, lo creo y lo guardo en la sesión.
				ConsultasBD consultasBD = null;
				consultasBD = (ConsultasBD)request.getSession().getAttribute("consultasBD");
				if (consultasBD==null){
					consultasBD = new ConsultasBD();
					request.getSession().setAttribute("consultasBD",consultasBD);
				}
				
				ClsLogging.writeFileLog("-------------------------------------------------", 10); 
				ClsLogging.writeFileLog("  idConsulta: " + idConsulta, 10); 
				ClsLogging.writeFileLog("-------------------------------------------------", 10);

				Hashtable h = consultasBD.getFromWhere(tablas);
				if (h==null || !h.get(Grafo.ESTADO).equals(Grafo.OK_HAY_CAMINO)){
					throw new SIGAException("messages.consultas.error.UnionTablas");
				}else{
					from = " FROM "+h.get(Grafo.FROM);
					if (!((String)h.get(Grafo.WHERE)).equals("")){
						where = " WHERE "+h.get(Grafo.WHERE);
					}
				}
			}
			
			//Concuerdan los paréntesis abiertos y cerrados			
			String criteriosConcat="";
			for (int i=0;i<criterios.length;i++){
				if (criterios[i]==null || criterios[i].equals("null")){
					if (i>0){
						if (criterios[i-1]!=null && (criterios[i-1].endsWith(".Y.") || criterios[i-1].endsWith(".O.") || criterios[i-1].endsWith(".NO."))){
							//El último criterio no puede acabar con un operador lógico
							throw new SIGAException("messages.consultas.error.Operador");
						}
					}
					i=criterios.length;
				}else{
					criteriosConcat += criterios[i];
				}
			}				
			
			int posC = 0;
			int posA = 0;
			
			if ((posC = criteriosConcat.indexOf(")"))>=0){
				while ((posC = criteriosConcat.indexOf(")"))>=0){//existe algún paréntesis cerrado
					//posC = criteriosConcat.indexOf(")");
					criteriosConcat = criteriosConcat.substring(0,posC)+criteriosConcat.substring(posC+1,criteriosConcat.length());
					posA = criteriosConcat.lastIndexOf("(",posC-1);
					if(posA > -1){//existe el correspondiente paréntesis abierto
						if (posA ==0){
							criteriosConcat = criteriosConcat.substring(1);
						}else{
							criteriosConcat = criteriosConcat.substring(0,posA)+criteriosConcat.substring(posA+1,criteriosConcat.length());	
						}			
					}else{
						throw new SIGAException("messages.consultas.error.Parentesis");
					}
				}
				if ((posA = criteriosConcat.lastIndexOf("("))>=0){//Existe un paréntesis abierto pero no cerrado
					throw new SIGAException("messages.consultas.error.Parentesis");
				}
			}else{
				if ((posA = criteriosConcat.lastIndexOf("("))>=0){//Existe un paréntesis abierto pero no cerrado
					throw new SIGAException("messages.consultas.error.Parentesis");
				}
			}
			
			//CONSTRUYO LA SELECT
			
			if (isEnvios){
				//SELECT
				select = "SELECT "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+", ";
				select += CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CODIGOPOSTAL+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_CORREOELECTRONICO+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_DOMICILIO+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_MOVIL+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX1+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FAX2+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPAIS+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPROVINCIA+", ";
				select += CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPOBLACION+" ";
				
				//FROM
				select += from;
				
				//WHERE									
				if (criterios[0]!=null && !criterios[0].equals("") && !criterios[0].equals("null")){
					ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
					String cr=ccAdm.getWhere(criterios);
					
					where += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation();
					where += " AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDDIRECCION+"=";
					where += "f_siga_getdireccion("+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+","+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+","+EnvTipoEnviosAdm.CONS_TIPOENVIO+")";
					where += " AND ("+cr+")";
//					 Se vuelve a poner las restriccion de que solo muestre direcciones que no se hayan dado de baja		  
					where += " AND " + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_FECHABAJA + " IS NULL";

				}
				
				//Comprobamos si alguna de las tablas contiene un campo idinstitucion.
				//Si lo contiene, filtramos por la institucion actual.
				ConCampoConsultaAdm cacAdm = new ConCampoConsultaAdm(this.getUserBean(request));
				String inst = cacAdm.getTablaConInstitucion(t);
				if (!inst.equals("")){
					if (where.equals("")){
						where += " WHERE "+inst+"="+userBean.getLocation();
					}else{
						where += " AND "+inst+"="+userBean.getLocation();
					}
				}

				
			}else{
				select = "SELECT "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+", ";
				select += CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+" ";
				
				//FROM
				select += from;
				
				//WHERE									
				if (criterios[0]!=null && !criterios[0].equals("") && !criterios[0].equals("null")){
					ConCriterioConsultaAdm ccAdm = new ConCriterioConsultaAdm(this.getUserBean(request));
					String cr=ccAdm.getWhere(criterios);
					if (where.equals("")){
						where += " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation();
					}else{
						where += " AND "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation();
					}
					where += " AND ("+cr+")";
				}else{
					where += " WHERE "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION+"="+userBean.getLocation();					
				}
			}
			
			select += where;
			
			// RGG INC_3269 21/02/2007 Se intentan sustituir los parametros de las funciones de cen_colegiado
			ConCriterioConsultaAdm concritcon = new ConCriterioConsultaAdm(this.getUserBean(request));
			select = concritcon.sustituirParametrosColegiado(select);
		} 
		catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		return select;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirConParametros(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirConParametros(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		try {
			EditarConsultaForm form = (EditarConsultaForm)formulario;
			String idInstitucion = form.getIdInstitucion();
			String idConsulta = form.getIdConsulta();
			String tipoConsulta = form.getTipoConsulta();
			
			
			//Hago lo mismo que si editara una consulta
			Hashtable htParametros=new Hashtable();
			htParametros.put("idInstitucion",idInstitucion);
			htParametros.put("idConsulta",idConsulta);
			htParametros.put("tipoConsulta",tipoConsulta);
			htParametros.put("editable",  "1");
			htParametros.put("accion",  "edicion");
			if (form.getConsultaExperta().equals(ClsConstants.DB_TRUE)){
				htParametros.put("consultaExperta", ClsConstants.DB_TRUE);		
			  
			}else{
				htParametros.put("consultaExperta", ClsConstants.DB_FALSE);
			}
			
			
			//Metemos los datos  de la consulta en Backup.
			//Si existe en backup un objeto RecuperarConsultasForm, es que hemos realizado una búsqueda previa
			HashMap datosConsulta = null;
			if (request.getSession().getAttribute("DATABACKUP")!=null){
		    	if (request.getSession().getAttribute("DATABACKUP").getClass().getName().equals("java.util.HashMap")){
		    		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    		RecuperarConsultasForm f = (RecuperarConsultasForm)databackup.get("RecuperarConsultasForm");
		    		if (f!=null){
		    			datosConsulta = databackup;
		    			htParametros.put("buscar","true");
		    		}else{
		    			datosConsulta = new HashMap();
		    		}
		    	}else{
		    		datosConsulta = new HashMap();
		    	}
			}else{
				datosConsulta = new HashMap();
			}
			
			//HashMap datosConsulta = new HashMap();
			Hashtable datosGenerales = new Hashtable();
			datosGenerales.put(ConConsultaBean.C_IDINSTITUCION,idInstitucion);
			datosGenerales.put(ConConsultaBean.C_IDCONSULTA,idConsulta);		    
			datosConsulta.put("datosGenerales",datosGenerales);			
			
			ConConsultaAdm cAdm = new ConConsultaAdm(this.getUserBean(request));
			Vector v = cAdm.selectByPK(datosGenerales);
			ConConsultaBean cBean = (ConConsultaBean)v.firstElement();
			datosConsulta.put("datosParticulares",cBean);
			
			request.getSession().setAttribute("DATABACKUP",datosConsulta);
			request.setAttribute("consulta", htParametros);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.consultas"},e,null); 
		}
		
		return "pestanas";
	}
	
	/** 
	 * Funcion que devuelve la sentencia completa de la consulta de listas dinámicas.
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected boolean analizarSentenciaExperta(String select,HttpServletRequest request, MasterForm formulario) throws SIGAException {
		

		boolean correcta=false;
		//String where =select.substring(select.indexOf(" \" where"));
		String selectTratada=select.replaceAll("\r\n"," ").replaceAll(" ","");
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		Vector camposSalida=new Vector();
		String selectAEjecutar="";
		boolean existeCondicionEnWhere=false;
		
		String selectOriginal=select;
		String selectDespuesWhere="";
		//UserTransaction tx = null;
		UserTransaction tx1 = null;
		
		try {
		
//      0.- Validacion de etiquetas de clausulas
			//SELECT
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTOPEN))<0){
					
					throw new SIGAException("messages.consultas.error.etiquetaSelectOpen");
					
				}else{
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaSelectClose");		
					}
			    }
			//FROM
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAFROMOPEN))<0){
					throw new SIGAException("messages.consultas.error.etiquetaFromOpen");
				}else{
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASFROMCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaFromClose");
					}
				}
			//WHERE
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAWHEREOPEN))>=0){
					
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASWHERECLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaWhereClose");
					}
				 if(	select.toUpperCase().substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETAWHEREOPEN),select.toUpperCase().indexOf(ClsConstants.ETIQUETASWHERECLOSE)).indexOf("WHERE ")>=0){
				 	existeCondicionEnWhere=true;
				 }
				}	
			//UNION	
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONOPEN))>=0){					
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONOPEN))<0){
						throw new SIGAException("messages.consultas.error.etiquetaUnionOpen");
					}		
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaUnionClose");
					}							 
				}	
				
				
			//UNION	ALL
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONALLOPEN))>=0){					
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONALLOPEN))<0){
						throw new SIGAException("messages.consultas.error.etiquetaUnionAllOpen");
					}		
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONALLCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaUnionAllClose");
					}							 
				}		
					
					
				
			//ORDER BY
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAORDERBYOPEN))>=0){
					
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASORDERBYCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaOrderByClose");
					}
				}	
			//GROUP BY	
				if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETAGROUPBYOPEN))>=0){
					
					if ((select.toUpperCase().indexOf(ClsConstants.ETIQUETASGROUPBYCLOSE))<0){
						throw new SIGAException("messages.consultas.error.etiquetaGroupByClose");
					}
				}	
					
//      1.- La consulta debe comenzar por la cláusula SELECT
			String selectAux=select.trim().substring(ClsConstants.ETIQUETASELECTOPEN.length());
			if (!selectAux.toUpperCase().trim().startsWith("SELECT ")){
				throw new SIGAException("messages.consultas.error.select");
			}
		
	           if(selectTratada.toLowerCase().indexOf("=%%idinstitucion%%")<0){
//	          2.- La consulta no tiene clausula IDINSTITUCION=%%idinstitucion%%
	    				throw new SIGAException("messages.consultas.error.idinstitucion");
	    			}
	           else if (selectTratada.toLowerCase().trim().indexOf("%%idinstitucion%%oridinstitucion")>=0){
//	          3.- No se debe poder buscar por instituciones a las que no se tenga a acceso
//	        	  Ej: No se debe poder ejecutar idinstitucion=%%idinstitucion%% and idinstitucion=2050 desde la institucion %%idinstitucion%%
	    				throw new SIGAException("messages.consultas.error.operador");
	    			}

	           
//	  	      5.- Comprobacion de Criterios Dinamicos
//	    			 Para ver si están bien metidos los Alias hacemos una sustitucion del idinstitucion introducido
				if (select.toUpperCase().indexOf("%%IDINSTITUCION%%")>=0){
					select=select.toUpperCase().replaceAll("%%IDINSTITUCION%%",userBean.getLocation());
				}	
				//Es necesario sustituir el posible parámetro TIPOENVIO porque la consulta se va a ejecutar
				//para comprobar que es correcta. En este caso, al tratarse sólo de una prueba, 
				//el valor por el que se sustituye es indiferente
				if (select.toUpperCase().indexOf(EnvTipoEnviosAdm.CONS_TIPOENVIO)>=0){
					select=select.toUpperCase().replaceAll(
							EnvTipoEnviosAdm.CONS_TIPOENVIO,
							String.valueOf(EnvEnviosAdm.TIPO_CORREO_ELECTRONICO));
				}	
				if (select.toUpperCase().indexOf("F_SIGA_GETRECURSO")>=0){
					select=select.toUpperCase().replaceAll("%%IDIOMA%%",userBean.getLanguage());
					select=select.toUpperCase().replaceAll("@IDIOMA@",userBean.getLanguage());
				}	
	    		 	select=ComprobacionCriteriosDinamicos(select,request);
	    		 	if (selectOriginal.indexOf(ClsConstants.ETIQUETAGROUPBYOPEN)>=0){
	    		 		if (selectOriginal.indexOf(ClsConstants.ETIQUETAORDERBYOPEN)<0){
	    		 			selectDespuesWhere=selectOriginal.substring(selectOriginal.indexOf(ClsConstants.ETIQUETAGROUPBYOPEN));
	    		 		}else{
	    		 			selectDespuesWhere=selectOriginal.substring(selectOriginal.indexOf(ClsConstants.ETIQUETAGROUPBYOPEN),selectOriginal.indexOf(ClsConstants.ETIQUETAORDERBYOPEN));
	    		 		}
	    		 	}
	    		 	if (existeCondicionEnWhere){
	  				 
	  				    select=select+" and rownum<2 "+selectDespuesWhere;
	  				}else{
	  					select=select+" where rownum<2 "+selectDespuesWhere;
	  				}
	    		
	    		 	
	    		 	
//            4.- Todos los campos de salida tienen que tener ALIAS	(Obtenemos los campos de salida que están dentro de las
//                etiquetas <SELECT> </SELECT>	 
	    		  camposSalida=ObtenerCamposSalida(select);	

			
	  	    	//2009-CGAE-119-INC-CAT-035
			    	//Si el tipo de consulta es E (Experta Envíos a grupos) o F (Experta Facturación) comprueba que
			    	//los campos de salida son los requeridos para ser usados en envíos o en facturación.
			        //Además, no puede tener criterios dinamicos
			     	EditarConsultaForm form = (EditarConsultaForm)formulario; 
			     	if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV) ||
			     		form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_FAC)){
		 	    	
			   			//Define los campos requeridos para la facturación
			   			//Se definen los campos requeridos, estos dos son comunes a envios y facturacion
			   			ArrayList camposRequeridos = new ArrayList();  
			  	    	camposRequeridos.add(CenClienteBean.C_IDPERSONA);
			  	    	camposRequeridos.add(CenClienteBean.C_IDINSTITUCION);
			  	    	
			  	    	if (form.getTipoConsulta().equals(ConConsultaAdm.TIPO_CONSULTA_ENV)){
				  	    	//Añade los campos requeridos para los envíos
					    	camposRequeridos.add(CenDireccionesBean.C_CODIGOPOSTAL);
					    	camposRequeridos.add(CenDireccionesBean.C_CORREOELECTRONICO);
					    	camposRequeridos.add(CenDireccionesBean.C_DOMICILIO);
					    	camposRequeridos.add(CenDireccionesBean.C_MOVIL);
					    	camposRequeridos.add(CenDireccionesBean.C_FAX1);
					    	camposRequeridos.add(CenDireccionesBean.C_FAX2);
					    	camposRequeridos.add(CenDireccionesBean.C_IDPAIS);
					    	camposRequeridos.add(CenDireccionesBean.C_IDPROVINCIA);
					    	camposRequeridos.add(CenDireccionesBean.C_IDPOBLACION);
			  	    	}
				    	//Comparar los campos requeridos para los envíos
			  	    	String camposNoPresentes = buscarItem(camposSalida, camposRequeridos);
				    	if (camposNoPresentes != null){
							throw new SIGAException("messages.consultas.error.camposRequeridos", new String[]{camposNoPresentes});
				    	}
				    	
				    	//comprueba que no existan criterios dinánicos
//				    	List operadoresList = new ArrayList<String>();
//						operadoresList.add("%=%");
//						operadoresList.add("%!=%");
//						operadoresList.add("%>%");
//						operadoresList.add("%>=%");
//						operadoresList.add("%<%");
//						operadoresList.add("%<=%");
//						operadoresList.add("%IS NULL%");
//						operadoresList.add("%LIKE%");
//						operadoresList.add("%OPERADOR%");
//				    	//comprueba que no existan criterios dinánicos
//						String operadores[] = select.toUpperCase().split("%");
//		 				for (int j = operadores.length-1; j >= 0 ; j--) {
//							String operador = operadores[j];
//							if(operadoresList.contains(operador)){
//								throw new SIGAException("messages.consultas.error.criteriosDinamicosNoPermitidos");
//							}
//						}
				    	if(select.toUpperCase().indexOf("%%OPERADOR%%") != -1){
							throw new SIGAException("messages.consultas.error.criteriosDinamicosNoPermitidos");
				    	}
			     	}
	    	
	     	
			int numeroCampos=0;
			// RGG PRUEBA DE TIEMPOS
		  //  tx = userBean.getTransactionLigera();
		   // tx.begin();
			try{
			 Connection con=ClsMngBBDD.getConnection();
			 
			 // Antes de ejecutar la consulta eliminamos todas las etiquetas introducidas y la nueva consulta la metemos en la variable selectAEjecutar
			 selectAEjecutar=ConConsultaAdm.eliminarEtiquetas(select);
			 selectAEjecutar=selectAEjecutar.replaceAll(ClsConstants.ETIQUETAOPERADOR,"=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%<%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%<=%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%>=%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%LIKE%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%=%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%!=%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%>%%","=");
			 selectAEjecutar=selectAEjecutar.replaceAll("%%IS NULL%%","=");
			 PreparedStatement ps=con.prepareStatement(selectAEjecutar);
		     ResultSet rs=ps.executeQuery();
		      ResultSetMetaData rs1=rs.getMetaData();
		      numeroCampos=rs1.getColumnCount();
		      con.close();
		     
			}catch (Exception e){
				
				throw new SIGAException ("Error al ejecutar la sentencia select en B.D. "+e.getMessage());
			}    
			//tx.commit();
		     
			   
			
		      
			  if(camposSalida.size()!=numeroCampos){
			  	throw new SIGAException("messages.consultas.error.alias"); 
			  }
			
			  
			
			
 

			
			  tx1 = userBean.getTransactionLigera();
			  tx1.begin();
			try {
			// Hacemos una ejecucion previa de la select para ver si tiene errores sintacticos
				if (selectAEjecutar.toUpperCase().indexOf("%%IDINSTITUCION%%")>=0){
					selectAEjecutar=selectAEjecutar.toUpperCase().replaceAll("%%IDINSTITUCION%%",userBean.getLocation());
				}
				if (selectAEjecutar.toUpperCase().indexOf("%%F_SIGA_GETRECURSO%%")>=0){
					selectAEjecutar=selectAEjecutar.toUpperCase().replaceAll("%%IDIOMA%%",userBean.getLanguage());
					selectAEjecutar=selectAEjecutar.toUpperCase().replaceAll("@IDIOMA@",userBean.getLanguage());
				}
				
				RowsContainer rc = new RowsContainer();
					
				rc.query(selectAEjecutar);
				
				 
				
				
				
				
			
			}catch (Exception sqle) {
				
		        String mensaje = sqle.getMessage();
			    if (mensaje.indexOf("TimedOutException")!=-1 || mensaje.indexOf("timed out")!=-1) {
			    	tx1.rollback();
			        throw new SIGAException("messages.transaccion.timeout",sqle);
			    } else {
			        if (sqle.toString().indexOf("ORA-")!=-1) {
			        	tx1.rollback();
			            throw new SIGAException("messages.general.sql", sqle, new String[] {sqle.toString()});
			        }
			        tx1.rollback();
			        throw sqle;
			    }
			    
			}		
			
			correcta=true;
			tx1.commit();
	
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			throwExcp("messages.general.error "+e.getMessage(),new String[] {"modulo.consultas"},e,tx1); 
		
		} 
		
		return correcta;
	}


	/**
	 * Comprueba si todos los elementos de <code>camposRequeridos</code> están en <code>camposSalida</code>.
	 * @param camposSalida
	 * @param camposRequeridos
	 * @return Devuelve <code>true</code> si todos los elementos de <code>camposRequeridos</code> están en 
	 * <code>camposSalida</code>, <code>false</code> en caso contrario
	 * 
	 */
	private String buscarItem(Vector camposSalida, ArrayList camposRequeridos) {
		StringBuffer  aux = new StringBuffer();
		for (int i = 0; i < camposRequeridos.size(); i++){
			boolean itemEncontrado = false; 
			String campoRequerido = (String) camposRequeridos.get(i);
			for (int j = 0; j < camposSalida.size(); j++){
				itemEncontrado = itemEncontrado || campoRequerido.equals(camposSalida.get(j));
			}
			if (!itemEncontrado){
				aux.append(campoRequerido + ", ");
			}
		}
		if(aux.length()>0)
			return aux.substring(0,aux.length()-2);
		else 
			return null;
	}
	
	protected String ObtenerSelectAyuda(String select, HttpServletRequest request) throws SIGAException {
		select=select.toUpperCase().replaceAll(ClsConstants.ETIQUETAOPERADOR,"");
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String select1="";
		int posCritMulti;
		String selectCritMulti="";
		if(select.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR)>=0){
		  select1=select.substring(select.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR));
		   posCritMulti=ClsConstants.TIPOMULTIVALOR.length();
		   selectCritMulti=select1.substring(posCritMulti);
		}else{
		  
		  selectCritMulti=select;
		}
		
			
			if (selectCritMulti.toUpperCase().indexOf("%%")>=0){
				 String selectCritMulti1="";
				 selectCritMulti1=selectCritMulti.toUpperCase();
			    selectCritMulti1=selectCritMulti1.replaceAll("%%IDIOMA%%",usr.getLanguage());
			    selectCritMulti1=selectCritMulti1.replaceAll("@IDIOMA@",usr.getLanguage());
			    selectCritMulti1=selectCritMulti1.substring(0,selectCritMulti1.toUpperCase().indexOf("%%"));
			    
			    return selectCritMulti1;
			}else{
				//return selectCritMulti;
				//eSTO ES QUE YA ESTAN RESUELTOS quitamos la excepcion
				throw new SIGAException("messages.consultas.error.CriterioDinamico");
			}    
			    
	}

	protected String EliminarEtiquetaOperador(String select) throws SIGAException {
		select=select.toUpperCase();
		
		select=select.replaceAll(ClsConstants.ETIQUETAOPERADOR,"");
		return select;
			    
	}
	protected Vector ObtenerCamposSalida(String select) throws SIGAException {
		 String busquedaAlias=" AS ";
		 int posFromUltimo=0;
		 int posFromPenultimo=0;
		 String selectAux="";
		 int posFrom=0;
		 String selectNew="";
		 String alias="";
		 Vector camposSalida=new Vector();
		 String selectCampos=select.toUpperCase().substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTOPEN),select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTCLOSE));
		 int longitudSelectCampos=selectCampos.length();
		
		 for (int i=0; i<longitudSelectCampos;i++){
			int aliasIni=selectCampos.toUpperCase().indexOf(busquedaAlias);
			if (aliasIni<0){
				break;
			}
			selectNew=selectCampos.substring(aliasIni+busquedaAlias.length()).trim();
			int aliasFin=selectNew.substring(1).indexOf("\"");
			
			if (aliasIni>=0){
				if (aliasFin<0){
					throw new SIGAException("messages.consultas.error.alias"); 
				}
				alias=selectNew.substring(1,aliasFin+1);
				camposSalida.add(alias);
				
				if ((i>0) && (camposSalida.get(i).toString().equals(camposSalida.get(i-1).toString()))){
					throw new SIGAException("messages.consultas.error.aliasRepetidos"); 
				}else{
					selectCampos=selectNew.substring(aliasFin+1);
				}
				
				
			}else{
				break;
			}
			
		}
		 return camposSalida;
			    
	}
	
	protected String ComprobacionCriteriosDinamicos(String select, HttpServletRequest request) throws SIGAException {
		// buscamos todos los criterios dinamicos introducidos en la consulta
		
		String critCampoSalida=select.substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTOPEN),select.toUpperCase().indexOf(ClsConstants.ETIQUETASELECTCLOSE));
		critCampoSalida=critCampoSalida.replaceAll("\r\n"," ");
		String selectFrom=select.substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETAFROMOPEN),select.toUpperCase().indexOf(ClsConstants.ETIQUETASFROMCLOSE));
		String critCampoWhere="";
		String  selectAux="";
		String  selectAuxunion="";
		String aliasSustituir="";
		String aliasSustituirFinal="";
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
		List operadoresListTexto = new ArrayList<String>();
		operadoresListTexto.add("=");
		operadoresListTexto.add("!=");
		operadoresListTexto.add("IS NULL");
		operadoresListTexto.add("LIKE");
		operadoresListTexto.add("OPERADOR");
		 
		
		
		//comprobamos si se han metido criterios pero no se ha metido la etiqueta <where>
		selectAux=select.substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETASFROMCLOSE));
	if 	(select.toUpperCase().indexOf("WHERE")>=0){
		
		if (select.toUpperCase().indexOf(ClsConstants.ETIQUETAWHEREOPEN)<0){
			throw new SIGAException("messages.consultas.error.etiquetaWhereOpen");
		}else{
		  critCampoWhere=select.substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETAWHEREOPEN),select.toUpperCase().indexOf(ClsConstants.ETIQUETASWHERECLOSE));
		}
	  }
	
	//comprobamos si se han metido criterios pero no se ha metido la etiqueta <UNION> o <UNION ALL>
	selectAuxunion=select.substring(select.toUpperCase().indexOf(ClsConstants.ETIQUETASWHERECLOSE));
	if (	(selectAuxunion.toUpperCase().indexOf("UNION")>=0)||(selectAuxunion.toUpperCase().indexOf("UNION ALL")>=0)){
		 if (selectAuxunion.toUpperCase().indexOf("UNION ALL")>=0){
			 if (selectAuxunion.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONALLOPEN)<0){
			  throw new SIGAException("messages.consultas.error.etiquetaUnionAllOpen");
			 }		 
		}else  if (selectAuxunion.toUpperCase().indexOf(ClsConstants.ETIQUETAUNIONOPEN)<0){
			throw new SIGAException("messages.consultas.error.etiquetaUnionOpen");
		}
	  }	
	
	// Sustituimos los criterios dinamicos que pueda haber en los campos de salida
		 while ((critCampoSalida.indexOf(ClsConstants.TIPONUMERO))>=0 || (critCampoSalida.indexOf(ClsConstants.TIPOTEXTO))>=0 || (critCampoSalida.indexOf(ClsConstants.TIPOFECHA)>=0 ||(critCampoSalida.indexOf(ClsConstants.TIPOMULTIVALOR))>=0 ) ){
		 	aliasSustituir="";
		 	aliasSustituirFinal="";
		 	if (critCampoSalida.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
				throw new SIGAException("messages.consultas.error.etiquetaOperador");
			}	
		 	if (critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPONUMERO)>=0 ){
		 		String existeOperador=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPONUMERO));
				if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
					throw new SIGAException("messages.consultas.error.etiquetaOperador");
			    }
		 		aliasSustituir=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPONUMERO));
		 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("WHERE"));
		 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
		 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
		 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
		 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
		 			}else{
		 			aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
		 			}
			 		 
			    }
		 		critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(ClsConstants.TIPONUMERO,"1");
		 		//critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(aliasSustituirFinal,"");
		 		critCampoSalida=critCampoSalida.substring(0,critCampoSalida.indexOf(aliasSustituirFinal))+critCampoSalida.substring(critCampoSalida.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
		 		
		 		
		
			}else if (critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOTEXTO)>=0 ){
				String existeOperador=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOTEXTO));
				if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
					throw new SIGAException("messages.consultas.error.etiquetaOperador");
			    }	
				aliasSustituir=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOTEXTO));
		 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("WHERE"));
		 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
		 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
		 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
		 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
		 			}else{
		 			aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
		 			}
			 		 
			    }
		 		critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(ClsConstants.TIPOTEXTO,"'1'");
		 		//critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(aliasSustituirFinal,"");
		 		critCampoSalida=critCampoSalida.substring(0,critCampoSalida.indexOf(aliasSustituirFinal))+critCampoSalida.substring(critCampoSalida.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
				
		 		
		
			}else if (critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOFECHA)>=0 ){
				String existeOperador=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOFECHA));
				if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
					throw new SIGAException("messages.consultas.error.etiquetaOperador");
			    }
				aliasSustituir=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOFECHA));
		 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("WHERE"));
		 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
		 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
		 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
		 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
		 			}else{
		 			aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
		 			}
			 		 
			    }
		 		critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(ClsConstants.TIPOFECHA,"sysdate");
		 		//critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(aliasSustituirFinal,"");
		 		critCampoSalida=critCampoSalida.substring(0,critCampoSalida.indexOf(aliasSustituirFinal))+critCampoSalida.substring(critCampoSalida.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
		    	
		
			}else if (critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR)>=0 ){
				String existeOperador=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR));
				if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
					throw new SIGAException("messages.consultas.error.etiquetaOperador");
			    }	
				aliasSustituir=critCampoSalida.substring(0,critCampoSalida.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR));
		 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("WHERE"));
		 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
		 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
		 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
		 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
		 			}else{
		 			aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
		 			}
			 		 
			    }
				    String valor=ClsConstants.TIPOMULTIVALOR+ObtenerSelectAyuda(critCampoSalida,request )+"%%";
		  // Vemos si esta bien montada la select de ayuda introducida
				    String selectAyudaTratada=ObtenerSelectAyuda(critCampoSalida,request).toUpperCase().replaceAll("\r\n"," ").replaceAll(" ","");
				    if (selectAyudaTratada.indexOf("AS\"ID\"")<0 || selectAyudaTratada.indexOf("AS\"DESCRIPCION\"")<0){
				    	throw new SIGAException("messages.consultas.error.SelectAyuda");
				    }
				    try{
				    RowsContainer rc = new RowsContainer();
					rc.query(ObtenerSelectAyuda(critCampoSalida,request));
					int pos =critCampoSalida.indexOf(valor);
					
					critCampoSalida=critCampoSalida.substring(0,pos)+"1"+critCampoSalida.substring(pos+valor.length());
					//critCampoSalida=critCampoSalida.toUpperCase().replaceFirst(aliasSustituirFinal,"");
					critCampoSalida=critCampoSalida.substring(0,critCampoSalida.indexOf(aliasSustituirFinal))+critCampoSalida.substring(critCampoSalida.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
					
				    } catch (ClsExceptions e) {
						throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
					} catch (Exception e){
						throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
					}
				    
					
				
				
			 
				
		
			}else{
			
				throw new SIGAException("messages.consultas.error.CriterioDinamico");
			}
		 }	
				
//			  Sustituimos los criterios dinamicos que pueda haber en el where
			 String operadorWhere = null;
				 while ((critCampoWhere.indexOf(ClsConstants.TIPONUMERO))>=0 || (critCampoWhere.indexOf(ClsConstants.TIPOTEXTO))>=0 || (critCampoWhere.indexOf(ClsConstants.TIPOFECHA)>=0 ||(critCampoWhere.indexOf(ClsConstants.TIPOMULTIVALOR))>=0 )){
				 	aliasSustituir="";
				 	aliasSustituirFinal="";
				 	String operadorEstudio = null;
				 	if(critCampoWhere.toUpperCase().indexOf(ClsConstants.TIPONUMERO)>=0)
				 		operadorEstudio = ClsConstants.TIPONUMERO;
				 	else if(critCampoWhere.toUpperCase().indexOf(ClsConstants.TIPOTEXTO)>=0)
				 		operadorEstudio = ClsConstants.TIPOTEXTO;
				 	else if(critCampoWhere.toUpperCase().indexOf(ClsConstants.TIPOFECHA)>=0)
				 		operadorEstudio = ClsConstants.TIPOFECHA;
				 	else if(critCampoWhere.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR)>=0)
				 		operadorEstudio = ClsConstants.TIPOMULTIVALOR;

				 	
				 		String sentenciaA=critCampoWhere.substring(0,critCampoWhere.toUpperCase().indexOf(operadorEstudio));
				 		int indiceAndIni =  sentenciaA.lastIndexOf("AND");
				 		String lineaEstudio = critCampoWhere.substring(indiceAndIni+3);
				 		if(operadorEstudio.equals(ClsConstants.TIPOMULTIVALOR)){
				 			int indexArroba=  lineaEstudio.indexOf("@"); 
				 			String p1 = lineaEstudio.substring(0,indexArroba);
				 			String p2 = lineaEstudio.substring(indexArroba);
				 			String p3 = p2.substring(0,p2.indexOf("%%"))+"%%";
				 			
				 			String auxi = p2.substring(p2.indexOf("%%")+2);
				 			int findIt = auxi.indexOf("AND");
				 			String p4 = auxi;
				 			if(findIt>-1)
				 				p4 = auxi.substring(0,findIt);
				 							 				
				 			lineaEstudio = p1+p3+p4;
				 			//lineaEstudio = lineaEstudio+"%%";
				 		}else{
					 		int indiceAndFin = lineaEstudio.indexOf("AND");
					 		if(indiceAndFin>-1)
					 			lineaEstudio = lineaEstudio.substring(0,indiceAndFin);
				 		}
				 		lineaEstudio = "AND"+lineaEstudio;
				 		critCampoWhere= critCampoWhere.substring(0,indiceAndIni)+critCampoWhere.substring(indiceAndIni+lineaEstudio.length());
				 		String operadores[] = sentenciaA.split("%%");
				 		for (int j = operadores.length-1; j >= 0 ; j--) {
							String operador = operadores[j];
							if(operadoresList.contains(operador)){
								operadorWhere ="%%"+operador+"%%";
								break;
								
							}
								
						}
				 		
				 		if (operadorWhere==null){
							throw new SIGAException("messages.consultas.error.etiquetaOperador");
					    }
				 		aliasSustituir=lineaEstudio.substring(0,lineaEstudio.toUpperCase().indexOf(operadorEstudio));
				 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("AND"));
				 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
				 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
				 			if (aux.toUpperCase().lastIndexOf(operadorWhere)<=0)		
			 			{
				 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
				 			}else{
				 			 aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(operadorWhere));
				 			}
					 		 
					    }
				 		
				 		int posicionDefecto = lineaEstudio.toUpperCase().indexOf(" DEFECTO ");
				 		String valorDefecto = null;
				 		if (posicionDefecto>=0){
				 			int inicio=lineaEstudio.substring(posicionDefecto).indexOf("\"");
				 			String auxiliar =lineaEstudio.substring(posicionDefecto).substring(inicio+1) ;
							int fin=auxiliar.indexOf("\"");
							if(inicio!=-1 && fin!=-1){
								valorDefecto=auxiliar.substring(0,fin);
							}
					    }
				 		String valor = null;
				 		if (operadorEstudio.equals(ClsConstants.TIPOMULTIVALOR)){
							
					 		
					 		String selectAyuda = ObtenerSelectAyuda(lineaEstudio,request);
						    valor=ClsConstants.TIPOMULTIVALOR+selectAyuda+"%%";
				  // Vemos si esta bien montada la select de ayuda introducida
						    String selectAyudaTratada=selectAyuda.toUpperCase().replaceAll("\r\n"," ").replaceAll(" ","");
						    if (selectAyudaTratada.indexOf("AS\"ID\"")<0 || selectAyudaTratada.indexOf("AS\"DESCRIPCION\"")<0){
						    	throw new SIGAException("messages.consultas.error.SelectAyuda");
						    }
						    try{
						    RowsContainer rc = new RowsContainer();
							rc.query(selectAyuda);
							int pos =lineaEstudio.indexOf(valor);
//							if(!operadorWhere.equals("%%IS NULL%%")){
								if(valorDefecto==null)
					 				lineaEstudio=lineaEstudio.substring(0,pos)+"1"+lineaEstudio.substring(pos+valor.length());
					 			
					 			else{
					 				lineaEstudio= lineaEstudio.substring(0,pos)+valorDefecto+lineaEstudio.substring(pos+valor.length());
					 			}

								
								
							
//							}
//							else{
//								lineaEstudio = lineaEstudio.substring(0,lineaEstudio.indexOf("%%IS NULL%%"))+" IS NULL";
//
//							}
							
						    } catch (ClsExceptions e) {
								throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
							} catch (Exception e){
								throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
							}
					}else{
				 		
				 	
				 		
				 		
//				 		if(!operadorWhere.equals("%%IS NULL%%")){
				 			if(valorDefecto==null){
				 				String valorAplicar = "1";
				 				if(operadorEstudio.equals(ClsConstants.TIPOFECHA))
				 					valorAplicar = "sysdate";
				 					
				 				String defecto = operadorEstudio.equals(ClsConstants.TIPOTEXTO)||(operadorEstudio.equals(ClsConstants.TIPOFECHA)&&!valorAplicar.equalsIgnoreCase("sysdate"))?"'"+valorAplicar+"'":valorAplicar; 
				 				lineaEstudio=lineaEstudio.toUpperCase().replaceFirst(operadorEstudio,defecto);
				 			
				 			}else{
				 				valorDefecto = operadorEstudio.equals(ClsConstants.TIPOTEXTO)||(operadorEstudio.equals(ClsConstants.TIPOFECHA)&&!valorDefecto.equalsIgnoreCase("sysdate"))?"'"+valorDefecto+"'":valorDefecto;
				 				lineaEstudio=lineaEstudio.toUpperCase().replaceFirst(operadorEstudio,""+valorDefecto+"");
				 			}
				 		/*}
				 		else{
				 					 			
				 			//lineaEstudio=lineaEstudio.toUpperCase().replaceFirst("%%IS NULL%%"," IS NULL");
				 			lineaEstudio = lineaEstudio.substring(0,lineaEstudio.indexOf("%%IS NULL%%"))+" IS NULL";
				 		}*/
					}
				 		lineaEstudio=lineaEstudio.substring(0,lineaEstudio.indexOf(aliasSustituirFinal))+lineaEstudio.substring(lineaEstudio.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
				 		
				 		if(valorDefecto!=null){
				 			
				 			lineaEstudio = lineaEstudio.substring(0,lineaEstudio.toUpperCase().indexOf("DEFECTO"));
				 		}
				 		int posicionNulo = lineaEstudio.toUpperCase().indexOf(" NULO ");
				 		if (posicionNulo>=0){
				 			lineaEstudio = lineaEstudio.substring(0,posicionNulo);

				 		}
				 		
				 		
					 
					 critCampoWhere = critCampoWhere.trim()+" "+lineaEstudio;
				 	
			 }
				 while ((selectFrom.indexOf(ClsConstants.TIPONUMERO))>=0 || (selectFrom.indexOf(ClsConstants.TIPOTEXTO))>=0 || selectFrom.indexOf(ClsConstants.TIPOFECHA)>=0 || (selectFrom.indexOf(ClsConstants.TIPOMULTIVALOR))>=0 ){
					  
					  aliasSustituir="";
				      aliasSustituirFinal="";
					  
				      if (selectFrom.toUpperCase().indexOf(ClsConstants.TIPOTEXTO)>=0 ){
					  String existeOperador=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOTEXTO));
						if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
							throw new SIGAException("messages.consultas.error.etiquetaOperador");
					    }	
						aliasSustituir=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOTEXTO));
				 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("FROM"));
				 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
				 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
				 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
				 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
				 			}else{
				 			aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
				 			}
					 		 
					    }
						selectFrom=selectFrom.toUpperCase().replaceFirst(ClsConstants.TIPOTEXTO,"'1'");						
						selectFrom=selectFrom.substring(0,selectFrom.indexOf(aliasSustituirFinal))+selectFrom.substring(selectFrom.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
										  
				 
				  
				  } else if (selectFrom.toUpperCase().indexOf(ClsConstants.TIPONUMERO)>=0){
				 		String existeOperador=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPONUMERO));
						if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
							throw new SIGAException("messages.consultas.error.etiquetaOperador");
					    }
				 		aliasSustituir=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPONUMERO));
				 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("FROM"));
				 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
				 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
				 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
				 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
				 			}else{
				 			 aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
				 			}
					 		 
					    }
				 		selectFrom=selectFrom.toUpperCase().replaceFirst(ClsConstants.TIPONUMERO,"1");				 	
				 		selectFrom=selectFrom.substring(0,selectFrom.indexOf(aliasSustituirFinal))+selectFrom.substring(selectFrom.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());				 		
					
				   }else if (selectFrom.toUpperCase().indexOf(ClsConstants.TIPOFECHA)>=0 ){
						String existeOperador=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOFECHA));
						if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
							throw new SIGAException("messages.consultas.error.etiquetaOperador");
					    }
						aliasSustituir=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOFECHA));
				 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("FROM"));
				 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
				 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
				 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
				 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
				 			}else{
				 			  aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
				 			}
					 		 
					    }
						selectFrom=selectFrom.toUpperCase().replaceFirst(ClsConstants.TIPOFECHA,"sysdate");
						selectFrom=selectFrom.substring(0,selectFrom.indexOf(aliasSustituirFinal))+selectFrom.substring(selectFrom.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
						String final1=selectFrom;
						
				   }else if (selectFrom.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR)>=0 ){
						String existeOperador=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR));
						if (existeOperador.toUpperCase().indexOf(ClsConstants.ETIQUETAOPERADOR)<0){
							throw new SIGAException("messages.consultas.error.etiquetaOperador");
					    }	
						aliasSustituir=selectFrom.substring(0,selectFrom.toUpperCase().indexOf(ClsConstants.TIPOMULTIVALOR));
				 		aliasSustituir=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf("FROM"));
				 		if (aliasSustituir.toUpperCase().lastIndexOf(" AS ")>=0){
				 			String aux=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "));
				 			if (aux.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR)<=0){
				 				throw new SIGAException("messages.consultas.error.etiquetaOperador");
				 			}else{
				 			   aliasSustituirFinal=aliasSustituir.substring(aliasSustituir.toUpperCase().lastIndexOf(" AS "),aliasSustituir.toUpperCase().lastIndexOf(ClsConstants.ETIQUETAOPERADOR));
				 			}
					 		 
					    }
						    String valor=ClsConstants.TIPOMULTIVALOR+ObtenerSelectAyuda(selectFrom,request)+"%%";			
						    String selectAyudaTratada=ObtenerSelectAyuda(selectFrom,request).toUpperCase().replaceAll("\r\n"," ").replaceAll(" ","");
						    if (selectAyudaTratada.indexOf("AS\"ID\"")<0 || selectAyudaTratada.indexOf("AS\"DESCRIPCION\"")<0){
						    	throw new SIGAException("messages.consultas.error.SelectAyuda");
						    }
						    try{
						    RowsContainer rc = new RowsContainer();
							rc.query(ObtenerSelectAyuda(selectFrom,request));
							int pos =selectFrom.indexOf(valor);					
							selectFrom=selectFrom.substring(0,pos)+"1"+selectFrom.substring(pos+valor.length());
							selectFrom=selectFrom.substring(0,selectFrom.indexOf(aliasSustituirFinal))+selectFrom.substring(selectFrom.indexOf(aliasSustituirFinal)+aliasSustituirFinal.length());
							
						    } catch (ClsExceptions e) {
								throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
							} catch (Exception e){
								throw new SIGAException ("Error al construir la consulta del multivalor. "+e.getMessage());
							}
				
					}else{
					
						throw new SIGAException("messages.consultas.error.CriterioDinamico");
					}
				  }
				
				 
				 if (critCampoWhere!=null && !critCampoWhere.equals("")){
				 	select=critCampoSalida+ClsConstants.ETIQUETASELECTCLOSE+selectFrom+ClsConstants.ETIQUETASFROMCLOSE+critCampoWhere+ClsConstants.ETIQUETASWHERECLOSE;
				 	
				 }else{
				 	select=critCampoSalida+ClsConstants.ETIQUETASELECTCLOSE+selectFrom+ClsConstants.ETIQUETASFROMCLOSE;
				 }
			return select;	
				
			
		 
			    
	}

}