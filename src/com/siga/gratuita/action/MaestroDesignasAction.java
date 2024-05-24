package com.siga.gratuita.action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.gen.SelectDataService;
import org.redabogacia.sigaservices.app.services.scs.ScsDesignaService;
import org.redabogacia.sigaservices.app.util.KeyValue;
import org.redabogacia.sigaservices.app.vo.scs.EjgsDesignaVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsTipoDesignaColegioAdm;
import com.siga.beans.ScsTipoDesignaColegioBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BuscarDesignasForm;
import com.siga.gratuita.form.MaestroDesignasForm;
import com.siga.ws.CajgConfiguracion;

import es.satec.businessManager.BusinessManager;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 * @version 26/01/2006 (david.sanchezp): arreglos varios y nuevos combos.
 */

public class MaestroDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else if ( miForm.getModo().equalsIgnoreCase("actualizarDesigna")){
				return mapping.findForward(actualizarDesigna(mapping, miForm, request, response));
				
			}else if ( miForm.getModo().equalsIgnoreCase("actualizaDesigna")){
				return mapping.findForward(actualizaDesigna(mapping, miForm, request, response));
				
			}else if (miForm.getModo().equalsIgnoreCase("getModulosJuzgado")){
				try {
					getModulosJuzgado(mapping, formulario, request, response);
				} catch (Exception e) {
					throw new SIGAException(e);
				}
				return null;
			}else if (miForm.getModo().equalsIgnoreCase("getPretensionesJuzgado")){
				try {
					getPretensionesJuzgado(mapping, formulario, request, response);
				} catch (Exception e) {
					throw new SIGAException(e);
				}
				return null;
			}else 
				return super.executeInternal(mapping, formulario, request, response);
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}
	private void getPretensionesJuzgado(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws JSONException, IOException {
		//Declaraci�n de atributos
				HashMap<String, String> params = new HashMap<String, String>();
				JSONObject objetoJSON = new JSONObject();
				JSONArray listaArrayJSON = new JSONArray();
		    	
				//Obtenemos el usuario de sesi�n
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
				//Recogemos el parametro enviado por ajax
				String idJuzgado = request.getParameter("idJuzgado");
				String comboPretensiones = request.getParameter("comboPretensiones");
				
				
				//Introducimos los parametros necesarios para la query
				params.put("idinstitucion", user.getLocation());
				params.put("idioma", user.getLanguage());
				JSONObject paraJsonObject = new JSONObject(idJuzgado);
				params.put("idjuzgado", (String) paraJsonObject.get("idjuzgado"));
				params.put("fechadesdevigor", (String) paraJsonObject.get("fechadesdevigor"));
				params.put("fechahastavigor", (String) paraJsonObject.get("fechahastavigor"));
				
				if(idJuzgado.contains("idprocedimiento")) 
					params.put("idprocedimiento", (String) paraJsonObject.get("idprocedimiento"));
				else
					params.put("idprocedimiento", "");
				
				if(idJuzgado.contains("idpretension")) 
					params.put("idpretension", (String) paraJsonObject.get("idpretension"));
				else
					params.put("idpretension", "");
				
				
				String idPretension = request.getParameter("idPretension");
				if(idPretension!=null && idPretension.contains("idpretension")) { 
					paraJsonObject = new JSONObject(idPretension);
					params.put("idpretension", (String) paraJsonObject.get("idpretension"));
				}else if(idPretension!=null && !idPretension.contains("idpretension")){
					params.put("idpretension", idPretension);
				}else {
					//por aqui no entrara nunca
					params.put("idpretension", "");
				}
					
				
				
				
				
				//Llamada al servicio
				SelectDataService service = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
				List<KeyValue> pretensiones = null;
				if(comboPretensiones.equalsIgnoreCase("getPretensionesAlcala")) {
					pretensiones = service.getPretensionesAlcala(params);
				}else if(comboPretensiones.equalsIgnoreCase("getPretensionesEjisModulosFiltros")) {
					pretensiones = service.getPretensionesEjisModulosFiltros(params);
				}else {
					pretensiones = service.getPretensiones(params);
				}
				//Obtenemos los datos y lo convertimos en objeto json
				for(int i=0;i<pretensiones.size();i++){
					KeyValue tratamientoAux = (KeyValue) pretensiones.get(i);
					objetoJSON.put("id",tratamientoAux.getKey());
					objetoJSON.put("descripcion",tratamientoAux.getValue());
				
					listaArrayJSON.put(objetoJSON);
					objetoJSON = new JSONObject();
					
				}
				
				 response.setHeader("Cache-Control", "no-cache");
				 response.setHeader("Content-Type", "application/json;charset=utf-8"); 
			     response.setHeader("X-JSON", listaArrayJSON.toString());
				 response.getWriter().write(listaArrayJSON.toString());
	}
	private void getModulosJuzgado(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws JSONException, IOException {
		//Declaraci�n de atributos
				HashMap<String, String> params = new HashMap<String, String>();
				JSONObject objetoJSON = new JSONObject();
				JSONArray listaArrayJSON = new JSONArray();
		    	
				//Obtenemos el usuario de sesi�n
				UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
				//Recogemos el parametro enviado por ajax
				String idJuzgado = request.getParameter("idJuzgado");
				String comboModulos = request.getParameter("comboModulos");

				
				JSONObject paraJsonObject = new JSONObject(idJuzgado);
				params.put("idjuzgado", (String) paraJsonObject.get("idjuzgado"));
				params.put("fechadesdevigor", (String) paraJsonObject.get("fechadesdevigor"));
				params.put("fechahastavigor", (String) paraJsonObject.get("fechahastavigor"));
				if(idJuzgado.contains("idprocedimiento")) 
					params.put("idprocedimiento", (String) paraJsonObject.get("idprocedimiento"));
				else
					params.put("idprocedimiento", "");
				
				
				
				String idModulo = request.getParameter("idModulo");
				if(idModulo!=null && idModulo.equals("")) 
					params.put("idprocedimiento", "");
				else {
					if(idModulo.contains("idprocedimiento")) {
						paraJsonObject = new JSONObject(idModulo);
						params.put("idprocedimiento", (String) paraJsonObject.get("idprocedimiento"));

					} 
					
				}

				if(idJuzgado.contains("idpretension")) 
					params.put("idpretension", (String) paraJsonObject.get("idpretension"));
				else
					params.put("idpretension", "");
				
				
				String idPretension = request.getParameter("idPretension");
				if(idPretension!=null && idPretension.contains("idpretension")) { 
					paraJsonObject = new JSONObject(idPretension);
					params.put("idpretension", (String) paraJsonObject.get("idpretension"));
				}else if(idPretension!=null && !idPretension.contains("idpretension")){
					params.put("idpretension", idPretension);
				}
				if(!params.containsKey("idpretension"))
					params.put("idpretension", "");
				
					
				
				
				//Introducimos los parametros necesarios para la query
				params.put("idioma", user.getLanguage());
				params.put("idinstitucion", user.getLocation());
				
				SelectDataService service = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
				List<KeyValue> modulos = null;
				if(comboModulos.equalsIgnoreCase("getProcedimientosEnVigencia")) {
					modulos = service.getProcedimientosEnVigencia(params);
				
				}else if(comboModulos.equalsIgnoreCase("getProcedimientosEnVigenciaAlcala")) {
					modulos = service.getProcedimientosEnVigenciaAlcala(params);
				
				} else {
					modulos = service.getProcedimientosEnVigencia(params);
				}
				
				
				
				
				
				
				//Obtenemos los datos y lo convertimos en objeto json
				for(int i=0;i<modulos.size();i++){
					KeyValue tratamientoAux = (KeyValue) modulos.get(i);
					objetoJSON.put("id",tratamientoAux.getKey());
					objetoJSON.put("descripcion",tratamientoAux.getValue());
				
					listaArrayJSON.put(objetoJSON);
					objetoJSON = new JSONObject();
					
				}
				
				 response.setHeader("Cache-Control", "no-cache");
				 response.setHeader("Content-Type", "application/json;charset=utf-8"); 
			     response.setHeader("X-JSON", listaArrayJSON.toString());
				 response.getWriter().write(listaArrayJSON.toString());
	}


	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");		
		String forward="inicio";
		String consultaTipoDesigna=null, consultaTurno=null, nombreTurno="", nombreTipoDesigna = "", nombreTurnoAsistencia="", nombreGuardiaAsistencia="", consultaTurnoAsistencia="", consultaGuardiaAsistencia="";
		ScsTipoDesignaColegioAdm tipodesigna = null;
		ScsTurnoAdm turno = null;
		ScsGuardiasTurnoAdm guardia = null;
		ScsDesignaAdm admDesigna = null; 
		Hashtable resultado = new Hashtable();
		ScsDesignaBean beanDesigna = null;
		String idJuzgado = "" ;
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		String idtipoencalidad="";
		String idInstitucion = null;
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);
		try {
			turno = new ScsTurnoAdm(this.getUserBean(request));
			guardia = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			tipodesigna = new ScsTipoDesignaColegioAdm(this.getUserBean(request));
			admDesigna = new ScsDesignaAdm(this.getUserBean(request));

			//Recogemos de la pestanha la designa insertada o la que se quiere consultar
			//y los metemos en un hashtable para el jsp		
			
			if(miform.getAnio()!=null && !miform.getAnio().equals("")&&
					miform.getIdTurno()!=null && !miform.getIdTurno().equals("")&&
					miform.getNumero()!=null && !miform.getNumero().equals("")){
				UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				miform.getAnio());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				miform.getNumero());
				idInstitucion = miform.getIdInstitucion();
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,		idInstitucion);
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				miform.getIdTurno());
					
			}else{
				idInstitucion = (String)request.getParameter("IDINSTITUCION");
				UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,	idInstitucion	);
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));
			}
			
			
			// jbd 01/02/2010 Pasamos el valor del pcajg del colegio
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(idInstitucion));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			
			
		
			String ejisActivo = admParametros.getValor(idInstitucion, "ECOM", "EJIS_ACTIVO", "0");
			request.setAttribute("EJIS_ACTIVO", ejisActivo);			
			
			String filtrarModulos = admParametros.getValor(idInstitucion,"SCS",ClsConstants.GEN_PARAM_FILTRAR_MODULOS_PORFECHA, "");
			request.setAttribute("filtrarModulos", filtrarModulos);
			
			String filtroJuzgadoModuloEspecial = admParametros.getValor(idInstitucion,"SCS",ClsConstants.GEN_PARAM_FILTRAR_JUZGADO_MODULO_ESPECIAL, "0");
			request.setAttribute("filtroJuzgadoModuloEspecial", filtroJuzgadoModuloEspecial);
			
			
			// Consulto la designa:					
			Vector vDesignas = admDesigna.select(resultado);
			beanDesigna = (ScsDesignaBean)vDesignas.get(0);
			request.setAttribute("beanDesigna",beanDesigna);
			
			boolean isAlgunaActuacionFacturada = admDesigna.isAlgunaActuacionFacturada(beanDesigna.getIdInstitucion(), beanDesigna.getNumero(), beanDesigna.getIdTurno(), beanDesigna.getAnio());
			request.setAttribute("isAlgunaActuacionFacturada",isAlgunaActuacionFacturada?AppConstants.DB_TRUE:AppConstants.DB_FALSE);
			
			
			
			if ((beanDesigna.getIdTurno()!=null)&&(!(beanDesigna.getIdTurno()).equals(""))){
				consultaTurno=" where idTurno = " + beanDesigna.getIdTurno() + " and idinstitucion="+idInstitucion+" ";
				nombreTurno = ((ScsTurnoBean)((Vector)turno.select(consultaTurno)).get(0)).getAbreviatura();
			}
				
			//Se recupera el campo calidad, si tiene para el interesado de la designa.
			//para que se pase el dato a la jsp,y para cuando le damos al bot�n volver
			//vuelva con los datos de la busqueda que se realizo.
			ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(usr);
			idtipoencalidad= defendidosAdm.getidTipoEnCalidad(resultado);
			
			//recuperando el idJuzgado.
			if(beanDesigna.getIdJuzgado()!= null){
				 idJuzgado = beanDesigna.getIdJuzgado().toString();
			}
			
			if ((beanDesigna.getIdTipoDesignaColegio()!=null)&&(!(beanDesigna.getIdTipoDesignaColegio()).equals(""))){
				consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + beanDesigna.getIdTipoDesignaColegio() + " and idinstitucion ="+ idInstitucion+" ";
				nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
			}
		} catch(Exception e){
			if ((beanDesigna.getIdTipoDesignaColegio()!=null)&&(!(beanDesigna.getIdTipoDesignaColegio()).equals(""))){
				consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + beanDesigna.getIdTipoDesignaColegio() + " and idinstitucion ="+ idInstitucion+" ";
				nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
			}
		}
		try {
			resultado = beanDesigna.getOriginalHash();
			UtilidadesHash.set(resultado,"TURNO", 								nombreTurno);
			UtilidadesHash.set(resultado,"TIPODESIGNA",							nombreTipoDesigna);
			
			if ((idtipoencalidad!=null)&&(idtipoencalidad.equals(""))){
				UtilidadesHash.set(resultado,"CALIDAD", idtipoencalidad);
			}else{
				UtilidadesHash.set(resultado,"CALIDAD", "");
			}
				
			ses.setAttribute("resultado",resultado);	
			
			ScsAsistenciasBean asistenciaBean = null;
			
			// DCG 
			// Obtenemos los datos de la asistencia asociada, si la tiene
			{
				String miWhere = " WHERE " + ScsAsistenciasBean.C_DESIGNA_ANIO + " = " + beanDesigna.getAnio() +
								   " AND " + ScsAsistenciasBean.C_DESIGNA_NUMERO + " = " + beanDesigna.getNumero() +
								   " AND " + ScsAsistenciasBean.C_DESIGNA_TURNO + " = " + beanDesigna.getIdTurno() +
								   " AND " + ScsAsistenciasBean.C_IDINSTITUCION + " = " + idInstitucion;
				ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
				Vector vA = asistenciaAdm.select(miWhere);
				if ((vA != null) && (vA.size() == 1)) {
					asistenciaBean = (ScsAsistenciasBean) vA.get(0);
					request.setAttribute("asistenciaBean", asistenciaBean);
				}
			}
			if (asistenciaBean!=null) {
				consultaTurnoAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idinstitucion="+idInstitucion+" ";
				consultaGuardiaAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idGuardia = " + asistenciaBean.getIdGuardia() + " and idinstitucion="+idInstitucion+" ";
	
				nombreTurnoAsistencia = ((ScsTurnoBean)((Vector)turno.select(consultaTurnoAsistencia)).get(0)).getNombre();
				nombreGuardiaAsistencia = ((ScsGuardiasTurnoBean)((Vector)guardia.select(consultaGuardiaAsistencia)).get(0)).getNombre();
				request.setAttribute("nombreTurnoAsistencia",nombreTurnoAsistencia);
				request.setAttribute("nombreGuardiaAsistencia",nombreGuardiaAsistencia);				
			}
			
			//Se obtienen los EJG relacionados con la designa
			EjgsDesignaVo designaEjg = new EjgsDesignaVo();
			BusinessManager bm = getBusinessManager();
			designaEjg.setAniodesigna(beanDesigna.getAnio().shortValue());
			designaEjg.setNumerodesigna(beanDesigna.getNumero());
			designaEjg.setIdinstitucion(beanDesigna.getIdInstitucion().shortValue());
			designaEjg.setIdturno(beanDesigna.getIdTurno());
			String longitudEJG = admParametros.getValor (usr.getLocation (), "SCS", "LONGITUD_CODEJG", "5");
			designaEjg.setLongitudNumEjg(Short.valueOf(longitudEJG));
			
		
			ScsDesignaService ejsDesignaService = (ScsDesignaService) bm.getService(ScsDesignaService.class);	
			
			request.setAttribute("EJGS", ejsDesignaService.getEJGrelacionados(designaEjg));
			
			
			ses.setAttribute("ModoAction","editar");
			miform.setConvenio(beanDesigna.getFactConvenio()!=null?beanDesigna.getFactConvenio():"");
		}		
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
		}
		return forward;
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "nuevoRecarga";
	}
	
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
	}
	
	/** 
	 *  Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			  MasterForm formulario, 
			  HttpServletRequest request, 
			  HttpServletResponse response) throws ClsExceptions, SIGAException{
    
		        return "listado";
	}

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		try {
			HttpSession ses = (HttpSession)request.getSession();
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
			
		
			Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			String idturno ="", anio="",numero="";
			String estado= "";			
			String desdeEjg=(String)request.getParameter("desdeEjg");
			String desdeEJG=(String)request.getParameter("desdeEJG");
			if ((desdeEjg!=null && desdeEjg.equalsIgnoreCase("si"))||(desdeEJG!=null && desdeEJG.equalsIgnoreCase("si"))){
				ses.removeAttribute("DATAPAGINADOR");
			}
			
			
			
			
			boolean hayDatos = false;
			if (ocultos==null){
				hayDatos = true;
				idturno = (String) miform.getIdTurno();
				anio = (String)miform.getAnio();
				numero = (String)miform.getNumero();
				estado= miform.getEstado();
			}else{
				miform.setOrigen("");	
			}
			
		
			
			Hashtable elegido = new Hashtable();
			elegido.put(ScsDesignaBean.C_IDINSTITUCION, usr.getLocation());
			elegido.put(ScsDesignaBean.C_IDTURNO, (hayDatos?idturno:(String)ocultos.get(0)));
			elegido.put(ScsDesignaBean.C_ANIO, (hayDatos?anio:(String)ocultos.get(3)));
			elegido.put(ScsDesignaBean.C_NUMERO, (hayDatos?numero:(String)ocultos.get(2)));
			
			String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_sufijo="";
			ScsDesignaAdm adm = new ScsDesignaAdm(usr);
			Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(),
					(String)elegido.get(ScsDesignaBean.C_ANIO), (String)elegido.get(ScsDesignaBean.C_NUMERO), (String)elegido.get(ScsDesignaBean.C_IDTURNO));

			if (hTitulo != null) {
				t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
				t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
				t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
				t_anio = (String) hTitulo.get(ScsDesignaBean.C_ANIO);
				t_numero = (String) hTitulo.get(ScsDesignaBean.C_CODIGO);
				t_sufijo = (String) hTitulo.get(ScsDesignaBean.C_SUFIJO);
				if (t_sufijo!=null && !t_sufijo.equals("")){
					t_numero=t_numero+"-"+t_sufijo;
				}

			}
			StringBuffer solicitante = new StringBuffer();
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE));
			solicitante.append(" ");
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1));
			solicitante.append(" ");
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2));
			elegido.put("solicitante", solicitante.toString());
			elegido.put("designaCodigo", t_numero);
			elegido.put("codigoDesignaNumEJG", t_numero);
			
			
			if (hayDatos==true){				 
				 estado = miform.getEstado();				 
			}else {
				estado= (String)visibles.get(4);
			}			

			String modoaction="";
			 if (estado!=null&&(estado.equalsIgnoreCase("FINALIZADO")|| estado.equalsIgnoreCase("F"))){				 
				  ses.setAttribute("ModoAction","editar");
				 ses.setAttribute("Modo","ver");
			}else{					
				ses.setAttribute("Modo","editar");	
			}		 
			 if(miform.getOrigen()!=null &&miform.getOrigen().equalsIgnoreCase("/JGR_ComunicacionDesigna")){
		 			
		 			request.setAttribute("elementoActivo","12");
		 		}
			 
			 
			 
			request.setAttribute("idDesigna",elegido);
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
		}		
		return "edicion";
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  {

	    String consultaDesigna = "";
		UsrBean usr = this.getUserBean(request);
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		String desdeEjg=(String)request.getParameter("desdeEjg");
		String desdeEJG=(String)request.getParameter("desdeEJG");
		if ((desdeEjg!=null && desdeEjg.equalsIgnoreCase("si"))||(desdeEJG!=null && desdeEJG.equalsIgnoreCase("si"))){
			request.getSession().removeAttribute("DATAPAGINADOR");
		}
		miform.setOrigen("");
		String idInstitucion = null;
		String idTurno = null;
		String anio = null;
		String numero = null;
		
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		
		try {
			if ((miform.getDesdeEjg() != null) && (miform.getDesdeEjg().equalsIgnoreCase("si"))) {				
				idInstitucion = miform.getIdInstitucion();
				idTurno = miform.getIdTurno();
				anio = miform.getAnio();
				numero = ((ocultos == null)?miform.getNumero():(String)ocultos.get(2));			
			} else {
				idInstitucion = (String)ocultos.get(4);
				idTurno = (String)ocultos.get(0);
				anio = (String)ocultos.get(3);
				numero = ((ocultos == null)?miform.getNumero():(String)ocultos.get(2));			
			}
			
			Hashtable elegido = new Hashtable();
			elegido.put(ScsDesignaBean.C_IDINSTITUCION, idInstitucion);
			elegido.put(ScsDesignaBean.C_IDTURNO, idTurno);
			elegido.put(ScsDesignaBean.C_ANIO, anio);
			elegido.put(ScsDesignaBean.C_NUMERO, numero);
			
			String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_sufijo="";
			ScsDesignaAdm adm = new ScsDesignaAdm(usr);
			Hashtable hTitulo = adm.getTituloPantallaDesigna(idInstitucion,
					(String)elegido.get(ScsDesignaBean.C_ANIO), (String)elegido.get(ScsDesignaBean.C_NUMERO), (String)elegido.get(ScsDesignaBean.C_IDTURNO));

			if (hTitulo != null) {
				t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
				t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
				t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
				t_anio = (String) hTitulo.get(ScsDesignaBean.C_ANIO);
				t_numero = (String) hTitulo.get(ScsDesignaBean.C_CODIGO);
				t_sufijo = (String) hTitulo.get(ScsDesignaBean.C_SUFIJO);
				if (t_sufijo!=null && !t_sufijo.equals("")){
					t_numero=t_numero+"-"+t_sufijo;
				}

			}
			StringBuffer solicitante = new StringBuffer();
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE));
			solicitante.append(" ");
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1));
			solicitante.append(" ");
			solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2));
			elegido.put("solicitante", solicitante.toString());
			elegido.put("designaCodigo", t_numero);
			elegido.put("codigoDesignaNumEJG", t_numero);
			
			
			HttpSession ses = (HttpSession)request.getSession();
			ses.setAttribute("Modo","Ver");
			ses.setAttribute("ModoAction","Ver");
			if(miform.getOrigen()!=null &&miform.getOrigen().equalsIgnoreCase("/JGR_ComunicacionDesigna")){
	 			
	 			request.setAttribute("elementoActivo","11");
	 		}
			
			request.setAttribute("idDesigna",elegido);
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
		}
		return "edicion";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
		UserTransaction tx = null;
		UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
		tx=usr.getTransaction();
	    BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
		Hashtable hash = (Hashtable)miform.getDatos();
		Hashtable nuevaDesigna = new Hashtable();
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		
		try{
			tx.begin();
		    nuevaDesigna = designaAdm.prepararInsert(hash);
			
			Hashtable datosEntrada = miform.getDatos();
			String fechaJuicio = (String)datosEntrada.get("FechaJuicio");
			String horasJuicio = (String)datosEntrada.get("HorasJuicio");
			String minutosJuicio = (String)datosEntrada.get("MinutosJuicio");
			if (fechaJuicio!=null && !fechaJuicio.equals("")) {
				String aux = fechaJuicio.substring(0,fechaJuicio.length()-9) + " " + ((horasJuicio.trim().equals(""))?"00":horasJuicio)+":"+((minutosJuicio.trim().equals(""))?"00":minutosJuicio)+":00";
				nuevaDesigna.put(ScsDesignaBean.C_FECHAJUICIO,aux);		
			}
			
			hash.put(ScsDesignaBean.C_FECHAALTA,"SYSDATE");
			
			if (!designaAdm.insert(hash)) {
			    throw new  ClsExceptions("Error al insertar designaci�n: "+designaAdm.getError());
			}
			
			tx.commit();
			
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, tx); 
		}
		return "mantenimiento";
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		Hashtable datosEntrada = null;
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		ScsDesignasLetradoAdm admDesignasLetrado = new ScsDesignasLetradoAdm(this.getUserBean(request));
		ScsActuacionDesignaAdm admActuacionesDesignas = new ScsActuacionDesignaAdm(this.getUserBean(request));
		boolean ok=false;
		UserTransaction tx = null;
		String aux2 = "";
		String Fechaoficiojuzgado="";
		boolean actualizarFechaLetrado = false;
		Hashtable hsDesignaLetrado = new Hashtable();
		Hashtable hsDesignaLetradoNew = new Hashtable();

		try {
			tx = usr.getTransaction();
			datosEntrada = (Hashtable)miform.getDatos();
			
			boolean bExisteDesigna = ValidacionExisteDesigna(miform,request);
			
		    if ((!bExisteDesigna)|| (bExisteDesigna&& request.getParameter("modificarDesigna")!=null && request.getParameter("modificarDesigna").equals("1"))){
		    	
		    
						String consultaDesigna = " where " +ScsDesignaBean.C_ANIO+"="+(String)datosEntrada.get("ANIO")+ 
												 " and "+ScsDesignaBean.C_NUMERO+"="+(String)datosEntrada.get("NUMERO")+
												 // La fecha pasa a ser modificable, si la sacamos del form puede ser erronea
												 // " and "+GstDate.dateBetween0and24h(ScsDesignaBean.C_FECHAENTRADA,(String)datosEntrada.get("FECHA"))+
												 " and "+ScsDesignaBean.C_IDTURNO+"="+(String)datosEntrada.get("IDTURNO")+" "+
												 " and "+ScsDesignaBean.C_IDINSTITUCION+"="+usr.getLocation()+" "; 
						
						ScsDesignaBean designaAntigua = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
						
						Hashtable designaNueva = (Hashtable)(designaAntigua.getOriginalHash().clone());
						String lengua = (String)usr.getLanguage();
						try{
							aux2 = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHACIERRE"));
							if (aux2==null)
								aux2 = "";
						}catch(Exception e){aux2="";}
						designaNueva.put(ScsDesignaBean.C_FECHAFIN,aux2);					
						String tipo=(String)datosEntrada.get("TIPO");
						if (tipo!=null){
						 designaNueva.put(ScsDesignaBean.C_IDTIPODESIGNACOLEGIO,(String)datosEntrada.get("TIPO"));	
						}
						
						String estado = (String)datosEntrada.get("ESTADO");//estado seleccionado
						String estadoOriginal = miform.getEstadoOriginal();//estado original
						boolean anular = false;
						boolean desAnular=false;
						//Control del estado:
						if (estadoOriginal!=null && !estadoOriginal.equalsIgnoreCase("A") && !estadoOriginal.equalsIgnoreCase(estado)) {
							designaNueva.put(ScsDesignaBean.C_ESTADO, estado);
							designaNueva.put(ScsDesignaBean.C_FECHAESTADO, "SYSDATE");
							
						    if (estado!=null && estado.equalsIgnoreCase("A") && estadoOriginal!=null && !estadoOriginal.equalsIgnoreCase(estado)) {
						    	designaNueva.put(ScsDesignaBean.C_FECHAESTADO,"SYSDATE");
						    	designaNueva.put(ScsDesignaBean.C_FECHA_ANULACION,"SYSDATE");
							    anular = true;
						    }
						}else{
							if (estadoOriginal!=null && estadoOriginal.equalsIgnoreCase("A") && !estadoOriginal.equalsIgnoreCase(estado)) {
								designaNueva.put(ScsDesignaBean.C_ESTADO, estado);
								designaNueva.put(ScsDesignaBean.C_FECHA_ANULACION,"");
								designaNueva.put(ScsDesignaBean.C_FECHAESTADO, "SYSDATE");
								desAnular = true;
							}
						}
						
						// JBD INC-5682-SIGA Actualizamos la fecha de entrada 
						// si el usuario la ha modificado mete la nueva, si no, vuelve a meter la original
						// jbd // Agregamos un control para que la fecha sea correcta y ademas tenemos que cambiar 
						       // la fecha de designacion del primer letrado
						String fechaApertura = GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHA"));
						if (fechaApertura!=null && !fechaApertura.equals("")) {
							//String aux = fechaApertura.substring(0,fechaApertura.length()-9);
							designaNueva.put(ScsDesignaBean.C_FECHAENTRADA,fechaApertura);
							if(!fechaApertura.equalsIgnoreCase(designaAntigua.getFechaEntrada())){
								Date dtFechaApertura = UtilidadesFecha.getDate(fechaApertura,ClsConstants.DATE_FORMAT_JAVA);
								// Recogemos las fechas de renuncia y actuacion que marcan el limite de la fecha apertura
								// Si es posterior a esas fechas no dejamos que se cambie la fecha
								hsDesignaLetrado = admDesignasLetrado.getPrimerLetrado(designaAntigua);
								String stFechaPrimeraRenuncia = (String)hsDesignaLetrado.get(ScsDesignasLetradoBean.C_FECHARENUNCIA);
								Date dtFechaPrimeraRenuncia=null;
								if(stFechaPrimeraRenuncia!=null && !stFechaPrimeraRenuncia.equalsIgnoreCase("")){							
									dtFechaPrimeraRenuncia = UtilidadesFecha.getDate(stFechaPrimeraRenuncia,ClsConstants.DATE_FORMAT_JAVA);
								}
								String stFechaPrimeraActuacion = admActuacionesDesignas.getFechaPrimeraActuacion(designaAntigua);
								Date dtFechaPrimeraActuacion = null;
								if(stFechaPrimeraActuacion!=null && !stFechaPrimeraActuacion.equalsIgnoreCase("")){
									dtFechaPrimeraActuacion = UtilidadesFecha.getDate(stFechaPrimeraActuacion,ClsConstants.DATE_FORMAT_JAVA);
								}
								// Ambas fechas pueden ser null
								String stMotivo="";
								Date dtFechaCorte=null;
								if(dtFechaPrimeraActuacion==null && dtFechaPrimeraRenuncia==null){
									// Si no hay fecha de renuncia ni fecha de actuacion actualizamos la fecha de designa del letrado 
									actualizarFechaLetrado = true;
									hsDesignaLetradoNew.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fechaApertura);
								}else{
									// Si una es nula cogemos la otra
									if(dtFechaPrimeraActuacion==null){
										dtFechaCorte = dtFechaPrimeraRenuncia;
										stMotivo="messages.designa.fechaDesigna.anterior.renuncia";
									}else if(dtFechaPrimeraRenuncia==null){
										dtFechaCorte = dtFechaPrimeraActuacion;
										stMotivo="messages.designa.fechaDesigna.anterior.actuacion";
									}else{
										// Ninguna es nula
										if(dtFechaPrimeraActuacion.before(dtFechaPrimeraRenuncia)){
											dtFechaCorte = dtFechaPrimeraActuacion;
											stMotivo="messages.designa.fechaDesigna.anterior.actuacion";
										}else{
											dtFechaCorte = dtFechaPrimeraRenuncia;
											stMotivo="messages.designa.fechaDesigna.anterior.renuncia";
										}
									}
									if(dtFechaCorte.before(dtFechaApertura)){
										String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.designa.fechaDesigna.anterior");
										mensaje += UtilidadesString.getMensajeIdioma(this.getUserBean(request),stMotivo);
										mensaje += " (" +UtilidadesString.formatoFecha(dtFechaCorte, ClsConstants.DATE_FORMAT_SHORT_SPANISH)+")";
										return exitoRefresco(mensaje, request);
										
									} else if(dtFechaCorte.compareTo(dtFechaApertura) == 0){
										String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.designa.fechaDesigna.anterior");
										mensaje += UtilidadesString.getMensajeIdioma(this.getUserBean(request),stMotivo);
										mensaje += " (" +UtilidadesString.formatoFecha(dtFechaCorte, ClsConstants.DATE_FORMAT_SHORT_SPANISH)+")";
										return exitoRefresco(mensaje, request);
										
									}else{
										actualizarFechaLetrado = true;
										hsDesignaLetradoNew.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fechaApertura);
									}
								}
								
							}
						}
						
						String fechaJuicio = GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FechaJuicio"));
						String horasJuicio = (String)datosEntrada.get("HorasJuicio");
						String minutosJuicio = (String)datosEntrada.get("MinutosJuicio");
						if (fechaJuicio!=null && !fechaJuicio.equals("")) {
							String aux = fechaJuicio.substring(0,fechaJuicio.length()-9) + " " + ((horasJuicio.trim().equals(""))?"00":horasJuicio)+":"+((minutosJuicio.trim().equals(""))?"00":minutosJuicio)+":00";
							designaNueva.put(ScsDesignaBean.C_FECHAJUICIO,aux);		
						} else { 
							designaNueva.put(ScsDesignaBean.C_FECHAJUICIO,"");		
						}
						
						designaNueva.put(ScsDesignaBean.C_RESUMENASUNTO,(String)datosEntrada.get("ASUNTO"));
						designaNueva.put(ScsDesignaBean.C_OBSERVACIONES,(String)datosEntrada.get("OBSERVACIONES"));
						designaNueva.put(ScsDesignaBean.C_DELITOS,(String)datosEntrada.get("DELITOS"));
			
						// Obtengo el idProcurador y la idInstitucion del procurador:
						Integer idProcurador, idInstitucionProcurador;
						idProcurador = null;
						idInstitucionProcurador = null;			
						String procurador = (String)datosEntrada.get(ScsDesignaBean.C_PROCURADOR);
						if (procurador!=null && !procurador.equals("")){
							idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
							idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
							designaNueva.put(ScsDesignaBean.C_IDPROCURADOR, idProcurador);
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
						} else {
							designaNueva.put(ScsDesignaBean.C_IDPROCURADOR, "");
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONPROCURADOR, "");
						}			
			
						// Obtengo el idJuzgado y la idInstitucion del Juzgado:
						Integer idJuzgado, idInstitucionJuzgado;
						idJuzgado = null;
						idInstitucionJuzgado = null;			
						String sJuzgado=((String)datosEntrada.get("JUZGADO"));
						if (sJuzgado!=null) {
							String sIdJuzgado = "";
							String sIdInstitucionJuzgado = "";
							if (sJuzgado.startsWith("{")){
								// ES UN JSON
								HashMap<String, String> hmIdJuzgadoObtenido = new ObjectMapper().readValue(sJuzgado, HashMap.class);
								sIdJuzgado = hmIdJuzgadoObtenido.get("idjuzgado");
								sIdInstitucionJuzgado = hmIdJuzgadoObtenido.get("idinstitucion");
							} else if (!sJuzgado.equals("")){
								String[] juzgado =sJuzgado.split(",");
								sIdJuzgado = juzgado[0];
								sIdInstitucionJuzgado = juzgado[1];
							}
							if (sIdJuzgado!=null && !sIdJuzgado.equals("")){
								idJuzgado = new Integer(sIdJuzgado);
								idInstitucionJuzgado = new Integer(sIdInstitucionJuzgado);
								designaNueva.put(ScsDesignaBean.C_IDJUZGADO, idJuzgado);
								designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, idInstitucionJuzgado);
							} else {
								designaNueva.put(ScsDesignaBean.C_IDJUZGADO, "");
								designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, "");
							}	
						} else {
							designaNueva.put(ScsDesignaBean.C_IDJUZGADO, "");
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, "");
						}
						
						if (miform.getNumeroProcedimiento() != null) {
						    UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NUMPROCEDIMIENTO, miform.getNumeroProcedimiento());
						}else{
							 UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NUMPROCEDIMIENTO, "");
						}	
						
						if (miform.getAnioProcedimiento() != null) {
						    UtilidadesHash.set(designaNueva, ScsDesignaBean.C_ANIOPROCEDIMIENTO, miform.getAnioProcedimiento());
						}else{
							 UtilidadesHash.set(designaNueva, ScsDesignaBean.C_ANIOPROCEDIMIENTO, "");
						}							
						
						if (miform.getNig() != null) {
						    UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NIG, miform.getNig());
						}else{
							 UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NIG, "");
						}
						
						String procedimientoSel=(String)datosEntrada.get("IDPROCEDIMIENTO");
						if (procedimientoSel!=null){							
							if(procedimientoSel.equals("")&& designaAntigua.getEstado().equals("F")){
								if (!designaAntigua.getProcedimiento().equals("")){
									designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, designaAntigua.getProcedimiento());
								}else{
									designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, "");
								}
							}else{
								String sIdprocedimiento = "";
								if (procedimientoSel.startsWith("{")){
									// ES UN JSON
									HashMap<String, String> hmProcedimientoSel = new ObjectMapper().readValue(procedimientoSel, HashMap.class);
									sIdprocedimiento = hmProcedimientoSel.get("idprocedimiento");
								} else if (!procedimientoSel.equals("")){
									String procedimiento[] = procedimientoSel.split(",");
									sIdprocedimiento = procedimiento[0];
								}
								designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, sIdprocedimiento);
							}							
						}else {
							designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, "");
						}
						// JBD 16/2/2009 INC-5739-SIGA
						// Obtenemos el idPretension
//						int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
						String pretensionSel=(String)datosEntrada.get("IDPRETENSION");
						GenParametrosAdm admParametros = new GenParametrosAdm(usr);		
						String filtroJuzgadoModuloEspecial = admParametros.getValor(usr.getLocation(),"SCS",ClsConstants.GEN_PARAM_FILTRAR_JUZGADO_MODULO_ESPECIAL, "0");
						if(filtroJuzgadoModuloEspecial!=null && filtroJuzgadoModuloEspecial.equals("1")){
							if(pretensionSel!=null && !pretensionSel.equals("")){
								HashMap<String, String> hmPretensionSel = new ObjectMapper().readValue(pretensionSel, HashMap.class);
								String idPretension = hmPretensionSel.get("idpretension");
								designaNueva.put(ScsDesignaBean.C_IDPRETENSION, idPretension);
							}else {
								designaNueva.put(ScsDesignaBean.C_IDPRETENSION, "");
							}
						}else{
						
							if (pretensionSel!=null){
								if(pretensionSel.equals("")&& designaAntigua.getEstado().equals("F")){							
									if (designaAntigua.getIdPretension()!=null){
										if (!designaAntigua.getIdPretension().equals("")){
											designaNueva.put(ScsDesignaBean.C_IDPRETENSION, designaAntigua.getIdPretension());
										}else{
											designaNueva.put(ScsDesignaBean.C_IDPRETENSION, "");
										}
									}						
								}else{
									String pretenciaon[] = pretensionSel.split(",");
									designaNueva.put(ScsDesignaBean.C_IDPRETENSION, pretenciaon[0]);
								}							
							}else {
								designaNueva.put(ScsDesignaBean.C_IDPRETENSION, "");
							}
						}
						
						// jbd 8/3/2010 inc-6876						
						String fechaoficiojuzgado = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHAOFICIOJUZGADO"));
						if (fechaoficiojuzgado!=null){
							if(fechaoficiojuzgado.equals("")&& designaAntigua.getEstado().equals("F")){
									if (!designaAntigua.getFechaOficioJuzgado().equals("")){
										designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO,designaAntigua.getFechaOficioJuzgado());
									}else{
										designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO, "");
									}
							}else{
								designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO,fechaoficiojuzgado);
							}							
						}else if (!designaAntigua.getEstado().equals("F")){
							designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO, "");
						}		
						
						String fechaRecepcionColegio = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHARECEPCIONCOLEGIO"));
						if (fechaRecepcionColegio!=null){
							if(fechaRecepcionColegio.equals("")&& designaAntigua.getEstado().equals("F")){
									if (!designaAntigua.getFechaRecepcionColegio().equals("")){
										designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO,designaAntigua.getFechaRecepcionColegio());
									}else{
										designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO, "");
									}
							}else{
								designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO,fechaRecepcionColegio);
							}
							
						}else if (!designaAntigua.getEstado().equals("F")){
							designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO, "");
						}
						
						if(miform.getConvenio()!=null)
							designaNueva.put(ScsDesignaBean.C_FACTCONVENIO, miform.getConvenio());
						
						
						
						
						tx.begin();
						designaAdm.update(designaNueva,designaAntigua.getOriginalHash());	
						if(actualizarFechaLetrado){
							admDesignasLetrado.updateFechaDesigna(hsDesignaLetrado, fechaApertura);
						}
						if (anular)
							designaAdm.anularDesigna(mapping, formulario, request, response);
						if (desAnular)
							designaAdm.desAnularDesigna(mapping, formulario, request, response);						
						tx.commit();
						
						
		    }else{
		    	 return "PreguntaDesignaDuplicada";
		    	
		    }
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoRefresco("messages.updated.success",request);
	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
			
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;		
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Hashtable hash = new Hashtable();
		//recoger la designa actual, los datos que hacen falta
		hash.put(ScsDesignaBean.C_ANIO,(String)ocultos.get(3));
		hash.put(ScsDesignaBean.C_NUMERO,(String)ocultos.get(2));
		hash.put(ScsDesignaBean.C_IDINSTITUCION,usr.getLocation());
		hash.put(ScsDesignaBean.C_IDTURNO,(String)ocultos.get(0));
		hash.put(ScsDesignasLetradoBean.C_IDPERSONA,(String)ocultos.get(1));
		boolean ok = false, ok2 = false;
		UserTransaction tx=null;
		String sqlDel=" delete "+ScsDesignasLetradoBean.T_NOMBRETABLA+" where "+ScsDesignasLetradoBean.C_ANIO+"="+(String)ocultos.get(3)+
        " and "+ScsDesignasLetradoBean.C_NUMERO+"="+(String)ocultos.get(2)+
        " and "+ScsDesignasLetradoBean.C_IDINSTITUCION+"="+usr.getLocation()+
        " and "+ScsDesignasLetradoBean.C_IDTURNO+"="+(String)ocultos.get(0);
       
		
		//recoger la designa actual, los datos que hacen falta, para la llamada de la funcion donde introduce los 
		// datos en la tabla SCS_SALTOSCOMPENSACIONES
		String anio=(String)ocultos.get(3);
		String numero=(String)ocultos.get(2);
		String idinstitucion=usr.getLocation();
		String idTurno=(String)ocultos.get(0);
		String codigo= (String)visibles.get(3);		
	
		try{
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			ScsDesignasLetradoAdm desletAdm = new ScsDesignasLetradoAdm(this.getUserBean(request));
			tx=usr.getTransaction();
			tx.begin();						  
			// Comprobamos que se quiera compensar o no por el borrado de la designacion
			String compensar = request.getParameter("compensar");
			if (compensar.equalsIgnoreCase("1")){//En caso de que SI se quiera compensar al letrado
				designaAdm.compensacionDesigna(request,anio,codigo, numero, idTurno, idinstitucion);						
			}								
			if (!desletAdm.deleteSQL(sqlDel)){
			    throw new ClsExceptions("Error al borrar designas letrado: "+desletAdm.getError());
			}
			if (!designaAdm.delete(hash)) {
			    throw new ClsExceptions("Error al borrar designas: "+designaAdm.getError());
			}			
			tx.commit();			
		}catch(Exception e){
			throwExcp("gratuita.listadoDesignas.message.error1",new String[] {"modulo.gratuita"},e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		return null;
	}
	
	
	protected boolean ValidacionExisteDesigna(MaestroDesignasForm formulario,HttpServletRequest request) throws ClsExceptions,SIGAException 
	{
		UsrBean usr = this.getUserBean(request);
		if (formulario == null)
			return false;
		String idJuzgadoFormulario = formulario.getJuzgado();
		String numeroProcedimientoFormulario = formulario.getNumeroProcedimiento();
		if (idJuzgadoFormulario == null || idJuzgadoFormulario.isEmpty() || numeroProcedimientoFormulario == null || numeroProcedimientoFormulario.isEmpty()) 
			return false;
		
		try{
			String idJuzgado = "";
			String idinstitucionJuzgado="";
			if (idJuzgadoFormulario.startsWith("{")){
				// ES UN JSON
				HashMap<String, String> hmIdJuzgadoObtenido = new ObjectMapper().readValue(idJuzgadoFormulario, HashMap.class);
				idJuzgado = hmIdJuzgadoObtenido.get("idjuzgado");
				idinstitucionJuzgado = hmIdJuzgadoObtenido.get("idinstitucion");
			} else if (!idJuzgadoFormulario.equals("")) {
				// MANTENEMOS LA FORMA ANTIG�A 
				String cadena[]=idJuzgadoFormulario.split(",");
				idJuzgado=cadena[0];
				idinstitucionJuzgado=cadena[1];
			}
		         
			StringBuilder consultaDesigna = new StringBuilder();
			consultaDesigna.append(" where /*SIGA-845 Optimizada original 6nyp2h0hza0d3*/ 1=1 ");
			consultaDesigna.append("   and "+ScsDesignaBean.C_IDJUZGADO+"="+idJuzgado);
			consultaDesigna.append("   and "+ScsDesignaBean.C_IDINSTITUCIONJUZGADO+"="+idinstitucionJuzgado);
			consultaDesigna.append("   and upper("+ScsDesignaBean.C_NUMPROCEDIMIENTO+")=upper('"+numeroProcedimientoFormulario+"')");
			consultaDesigna.append("   and ("+ScsDesignaBean.C_NUMERO+","+ScsDesignaBean.C_IDINSTITUCION+","+ScsDesignaBean.C_IDTURNO+","+ScsDesignaBean.C_ANIO+") not in (");
			consultaDesigna.append("       select "+ScsDesignaBean.C_NUMERO+","+ScsDesignaBean.C_IDINSTITUCION+","+ScsDesignaBean.C_IDTURNO+","+ScsDesignaBean.C_ANIO);
			consultaDesigna.append("         from "+ ScsDesignaBean.T_NOMBRETABLA);
			consultaDesigna.append("        where "+ScsDesignaBean.C_IDINSTITUCIONJUZGADO+"="+idinstitucionJuzgado);
			consultaDesigna.append("          and "+ScsDesignaBean.C_IDJUZGADO+"="+idJuzgado);
			consultaDesigna.append("          and upper("+ScsDesignaBean.C_NUMPROCEDIMIENTO+")=upper('"+numeroProcedimientoFormulario+"')");
			consultaDesigna.append("          and "+ScsDesignaBean.C_NUMERO+"="+formulario.getNumero());
			consultaDesigna.append("          and "+ScsDesignaBean.C_IDINSTITUCION+"="+usr.getLocation());
			consultaDesigna.append("          and "+ScsDesignaBean.C_IDTURNO+"="+formulario.getIdTurno());
			consultaDesigna.append("          and "+ScsDesignaBean.C_ANIO+"="+formulario.getAnio()+")");
			consultaDesigna.append("   and "+ScsDesignaBean.C_IDINSTITUCION+"="+usr.getLocation());
			
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (usr);
			Vector existeDesigna=designaAdm.select(consultaDesigna.toString());
			if (existeDesigna!=null && existeDesigna.size()>0){
				return true;
			}
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		return false;
	}
	protected String actualizarDesigna(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		HttpSession ses = request.getSession();
		UsrBean usr = this.getUserBean(request);
		String forward="actualizarDesigna";
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
		ScsDesignaAdm admDesigna  = new ScsDesignaAdm(usr);
		ScsTurnoAdm admTurno = new ScsTurnoAdm(usr);
		
		Hashtable filtro = new Hashtable();

		UtilidadesHash.set(filtro,ScsDesignaBean.C_ANIO, 				miform.getAnio());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_NUMERO, 				miform.getNumero());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDTURNO,				miform.getIdTurno());
		///Calculo de EJGs
		Vector datos = admDesigna.getDatosEJG((String)usr.getLocation(), miform.getNumero(), miform.getIdTurno(), miform.getAnio(),longitudNumEjg);
		// Recorrer los defendidos
		List<ScsEJGBean> ejgList = new ArrayList<ScsEJGBean>();
		for (int i = 0; i < datos.size(); i++) {
				ScsEJGBean ejg = new ScsEJGBean();
				String anio = (String) ((Hashtable) datos.get(i)).get("ANIO_EJG");
				String numero = (String) ((Hashtable) datos.get(i)).get("NUMERO_EJG");
				String tipo =(String) ((Hashtable) datos.get(i)).get("TIPO_EJG");
				ejg = abrir(request, anio, numero, tipo);
				 //Damos formato a la fecha de ratificacion
				  if(ejg.getFechaResolucionCAJG()!= null && !"".equalsIgnoreCase(ejg.getFechaResolucionCAJG())){ 
					  ejg.setFechaResolucionCAJG(GstDate.getFormatedDateShort("",ejg.getFechaResolucionCAJG()));
				  }
				ejgList.add(ejg);
		}
		///Calculo de EJGs
		miform.setEjgs(ejgList);
	
		Vector vDesignas = admDesigna.select(filtro);
		
		Hashtable letradoHashtable = admDesigna.obtenerLetradoDesigna((String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), miform.getNumero());
		miform.setLetrado(UtilidadesHash.getString(letradoHashtable, "NCOLEGIADO")+" "+UtilidadesHash.getString(letradoHashtable, "NOMBRE"));
		miform.setIdLetradoDesignado(UtilidadesHash.getString(letradoHashtable, "IDPERSONA"));
		Hashtable pkTurnoHashtable = new Hashtable();
		pkTurnoHashtable.put(ScsTurnoBean.C_IDTURNO, miform.getIdTurno());
		pkTurnoHashtable.put(ScsTurnoBean.C_IDINSTITUCION, (String)usr.getLocation());
		Vector turnoVector = admTurno.selectByPK(pkTurnoHashtable);
		ScsTurnoBean turnoBean = (ScsTurnoBean) turnoVector.get(0) ;
		miform.setTurno(turnoBean.getAbreviatura());
		ScsDesignaBean beanDesigna = (ScsDesignaBean)vDesignas.get(0);
		miform.setNig(beanDesigna.getNIG());
		miform.setNumeroProcedimiento(beanDesigna.getNumProcedimiento());
		if (beanDesigna.getAnioProcedimiento() == null)
			miform.setAnioProcedimiento("");
		else
			miform.setAnioProcedimiento(beanDesigna.getAnioProcedimiento().toString());
		
		List<ScsJuzgadoBean> alJuzgados = null;
		ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(usr);
		String idJuzgado = "";
		if(miform.getJuzgado()!=null)
			idJuzgado = miform.getJuzgado();
		
		alJuzgados = admJuzgados.getJuzgadosActualizar((String)usr.getLocation(),String.valueOf(turnoBean.getIdTurno()),usr,true, false, idJuzgado);
		if(alJuzgados==null){
			alJuzgados = new ArrayList<ScsJuzgadoBean>();
		
		}
		miform.setJuzgados(alJuzgados);
		miform.setModulos(new ArrayList<ScsProcedimientosBean>());
		miform.setFormulario(beanDesigna);
		if(beanDesigna.getIdPretension()!=null)
			miform.setIdPretension(beanDesigna.getIdPretension().toString());
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);		
		String ejisActivo = admParametros.getValor(usr.getLocation(), "ECOM", "EJIS_ACTIVO", "0");
		request.setAttribute("EJIS_ACTIVO", ejisActivo);
		String prefijoExpedienteCajg = admParametros.getValor (usr.getLocation(), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
		request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);

		return "actualizarDesigna";
	}
	protected String actualizaDesigna(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		
		try {
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			String clavesDesigna[] = { ScsDesignaBean.C_ANIO, ScsDesignaBean.C_NUMERO,
					ScsDesignaBean.C_IDINSTITUCION, ScsDesignaBean.C_IDTURNO };
			String camposDesigna[]={ScsDesignaBean.C_IDINSTITUCIONJUZGADO,ScsDesignaBean.C_IDJUZGADO,ScsDesignaBean.C_IDPROCEDIMIENTO,ScsDesignaBean.C_NUMPROCEDIMIENTO,ScsDesignaBean.C_ANIOPROCEDIMIENTO,ScsDesignaBean.C_NIG};
			Hashtable<String, Object> htDesigna = new Hashtable<String, Object>();
			htDesigna.put(ScsDesignaBean.C_IDINSTITUCION,usr.getLocation());
			htDesigna.put(ScsDesignaBean.C_ANIO,miform.getAnio());
			htDesigna.put(ScsDesignaBean.C_IDTURNO,miform.getIdTurno());
			htDesigna.put(ScsDesignaBean.C_NUMERO,miform.getNumero());
			htDesigna.put(ScsDesignaBean.C_NUMPROCEDIMIENTO,miform.getNumeroProcedimiento());
			Map<String,Hashtable<String, Object>> fksDesignaMap = new HashMap<String, Hashtable<String,Object>>(); 
			Hashtable<String, Object> fksDesignaHashtable = null;
			if (miform.getAnioProcedimiento() == null)
				htDesigna.put(ScsDesignaBean.C_ANIOPROCEDIMIENTO,"");
			else
				htDesigna.put(ScsDesignaBean.C_ANIOPROCEDIMIENTO,miform.getAnioProcedimiento());
			
			if(miform.getIdProcedimiento()!=null && !miform.getIdProcedimiento().equals("")&& !miform.getIdProcedimiento().equals("-1")){
				htDesigna.put(ScsDesignaBean.C_IDPROCEDIMIENTO,miform.getIdProcedimiento());
//				if(usr.isLetrado()){
					fksDesignaHashtable = new Hashtable<String, Object>();
					fksDesignaHashtable.put("TABLA_FK", ScsProcedimientosBean.T_NOMBRETABLA);
					fksDesignaHashtable.put("SALIDA_FK", ScsProcedimientosBean.C_NOMBRE);
					fksDesignaHashtable.put(ScsProcedimientosBean.C_IDPROCEDIMIENTO, miform.getIdProcedimiento());
					fksDesignaHashtable.put(ScsProcedimientosBean.C_IDINSTITUCION, usr.getLocation());
					
					
					fksDesignaMap.put(ScsDesignaBean.C_IDPROCEDIMIENTO,fksDesignaHashtable);
//				}
			}
			if(miform.getIdJuzgado()!=null && !miform.getIdJuzgado().equals("")&& !miform.getIdJuzgado().equals("-1")){
				htDesigna.put(ScsDesignaBean.C_IDJUZGADO, miform.getIdJuzgado());
				htDesigna.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, usr.getLocation());
//				if(usr.isLetrado()){
					fksDesignaHashtable = new Hashtable<String, Object>();
					fksDesignaHashtable.put("TABLA_FK", ScsJuzgadoBean.T_NOMBRETABLA);
					fksDesignaHashtable.put("SALIDA_FK", ScsJuzgadoBean.C_NOMBRE);
					fksDesignaHashtable.put(ScsJuzgadoBean.C_IDINSTITUCION, usr.getLocation());
					fksDesignaHashtable.put(ScsJuzgadoBean.C_IDJUZGADO, miform.getIdJuzgado());
					fksDesignaMap.put(ScsDesignaBean.C_IDJUZGADO,fksDesignaHashtable);
//				}
				
			}
			if (miform.getNig() == null)
				htDesigna.put(ScsDesignaBean.C_NIG,"");
			else
				htDesigna.put(ScsDesignaBean.C_NIG,miform.getNig());
//			if(usr.isLetrado()){
				Vector designaPKVector =  designaAdm.selectByPK(htDesigna);
				ScsDesignaBean scsDesignaBean = (ScsDesignaBean) designaPKVector.get(0);
				Hashtable designaOriginalHashtable = scsDesignaBean.getOriginalHash();
				Map<String,Hashtable<String, Object>> fksDesignaOriginalMap = new HashMap<String, Hashtable<String,Object>>();
				if(designaOriginalHashtable.get(ScsDesignaBean.C_IDJUZGADO)!=null && !designaOriginalHashtable.get(ScsDesignaBean.C_IDJUZGADO).equals("")){
					fksDesignaHashtable = new Hashtable<String, Object>();
					fksDesignaHashtable.put("TABLA_FK", ScsJuzgadoBean.T_NOMBRETABLA);
					fksDesignaHashtable.put("SALIDA_FK", ScsJuzgadoBean.C_NOMBRE);
					fksDesignaHashtable.put(ScsJuzgadoBean.C_IDINSTITUCION, designaOriginalHashtable.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO));
					fksDesignaHashtable.put(ScsJuzgadoBean.C_IDJUZGADO, designaOriginalHashtable.get(ScsDesignaBean.C_IDJUZGADO));
					fksDesignaOriginalMap.put(ScsDesignaBean.C_IDJUZGADO,fksDesignaHashtable);
				}
				if(designaOriginalHashtable.get(ScsDesignaBean.C_IDPROCEDIMIENTO)!=null && !designaOriginalHashtable.get(ScsDesignaBean.C_IDPROCEDIMIENTO).equals("")){
					fksDesignaHashtable = new Hashtable<String, Object>();
					fksDesignaHashtable.put("TABLA_FK", ScsProcedimientosBean.T_NOMBRETABLA);
					fksDesignaHashtable.put("SALIDA_FK", ScsProcedimientosBean.C_NOMBRE);
					fksDesignaHashtable.put(ScsProcedimientosBean.C_IDPROCEDIMIENTO, designaOriginalHashtable.get(ScsDesignaBean.C_IDPROCEDIMIENTO));
					fksDesignaHashtable.put(ScsProcedimientosBean.C_IDINSTITUCION, designaOriginalHashtable.get(ScsDesignaBean.C_IDINSTITUCION));
					fksDesignaOriginalMap.put(ScsDesignaBean.C_IDPROCEDIMIENTO,fksDesignaHashtable);
				}
				designaOriginalHashtable.put("fks", fksDesignaOriginalMap);
				htDesigna.put("fks", fksDesignaMap);
				List<String> ocultarClaveList = new ArrayList<String>();
				ocultarClaveList.add(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
				if (miform.getAnioProcedimiento() == null)
					ocultarClaveList.add(ScsDesignaBean.C_ANIOPROCEDIMIENTO);
				Hashtable<String, String> cambiarNombreSalidaHashtable = new Hashtable<String, String>();
				cambiarNombreSalidaHashtable.put(ScsDesignaBean.C_IDPROCEDIMIENTO,"IDMODULO");
				
				designaAdm.updateDirectHistorico(new Long(miform.getIdLetradoDesignado()), htDesigna, clavesDesigna, camposDesigna,scsDesignaBean.getOriginalHash(),ocultarClaveList,cambiarNombreSalidaHashtable);
//			}
//			else
//				designaAdm.updateDirect(htDesigna, clavesDesigna, camposDesigna);

		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		}
		return exitoModal("messages.updated.success",request);
	}
	
	
	protected ScsEJGBean abrir(HttpServletRequest request, String anio, String numero, String idtipoEjg ) throws SIGAException {
		
		
		Hashtable ejg = null;
		
		ScsEJGBean ejgBean=null;
		try {
			ejgBean = new ScsEJGBean();
			String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
			Hashtable miHash = new Hashtable();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String nombreTurnoAsistencia="", nombreGuardiaAsistencia="", consultaTurnoAsistencia="", consultaGuardiaAsistencia="";
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			//TEMPORAL!!!
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);		
			String ejisActivo = admParametros.getValor(usr.getLocation(), "ECOM", "EJIS_ACTIVO", "0");
			request.setAttribute("EJIS_ACTIVO", ejisActivo);			
			
			// Recuperamos los datos de la clave del EJG. Pueden venir de la request si accedemos por primera vez a esa pestanha
				miHash.put(ScsEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsEJGBean.C_NUMERO,numero);
				miHash.put(ScsEJGBean.C_ANIO,anio);
				miHash.put(ScsEJGBean.C_IDTIPOEJG,idtipoEjg);
			
			
			// Ahora realizamos la consulta. Primero cogemos los campos que queremos recuperar 
			String consulta = "select ejg.ANIO, lpad(ejg.NUMEJG,"+longitudNumEjg+",0) NUMEJG,designa.ESTADO,ejg.IDTIPOEJG AS IDTIPOEJG,ejg.NUMERO_CAJG AS NUMERO_CAJG, ejg.NUMERO, turno.ABREVIATURA AS NOMBRETURNO, guardia.NOMBRE AS NOMBREGUARDIA, guardia.IDGUARDIA AS IDGUARDIA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejg.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJG, ejg.IDTIPOEJGCOLEGIO AS IDTIPOEJGCOLEGIO," +
							  "decode(ejg.ORIGENAPERTURA,'M','Manual','S','SOJ','A','ASISTENCIA','DESIGNA'), ejg.IDPRETENSION as IDPRETENSION, ejg.IDINSTITUCION as IDINSTITUCION, ejg.idtipodictamenejg as IDTIPODICTAMENEJG, " + 
							  "ejg.FECHAAPERTURA AS FECHAAPERTURA,ejg.FECHARESOLUCIONCAJG, personajg.NIF AS NIFASISTIDO, personajg.NOMBRE AS NOMBREASISTIDO, personajg.APELLIDO1 AS APELLIDO1ASISTIDO, personajg.APELLIDO2 AS APELLIDO2ASISTIDO, " +
							  " (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion,"+ this.getUserBean(request).getLanguage() + ")) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = Ejg.Idtipoencalidad "+
                              "  And Tipcal.Idinstitucion = Ejg.Calidadidinstitucion) as calidad, Ejg.Idtipoencalidad, Ejg.Calidadidinstitucion, "+							  
                              "colegiado.NCOLEGIADO AS NCOLEGIADO, PERSONA.IDPERSONA AS IDPERSONA, persona.NOMBRE AS NOMBRELETRADO, " +
							  "persona.APELLIDOS1 AS APELLIDO1LETRADO, persona.APELLIDOS2 AS APELLIDO2LETRADO, soj.ANIO AS ANIOSOJ, soj.NUMERO AS NUMEROSOJ, soj.NUMSOJ AS CODIGOSOJ, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tiposoj.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOSOJ, tiposoj.IDTIPOSOJ AS IDTIPOSOJ, " +
							  "soj.FECHAAPERTURA AS FECHAAPERTURASOJ, asistencia.ANIO AS ANIOASISTENCIA, asistencia.NUMERO AS ASISTENCIANUMERO, asistencia.FECHAHORA AS ASISTENCIAFECHA, " +
							  " ejgd.aniodesigna AS DESIGNA_ANIO,ejgd.idturno AS DESIGNA_IDTURNO,ejgd.numerodesigna AS DESIGNA_NUMERO," +
							  "(SELECT ABREVIATURA FROM scs_turno t WHERE t.idturno = ejgd.IDTURNO and t.IDINSTITUCION = ejg.IDINSTITUCION) DESIGNA_TURNO_NOMBRE, " +
							  "designa.FECHAENTRADA AS FECHAENTRADADESIGNA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejgcolegio.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJGCOLEGIO," +
							  "ejg.FECHAPRESENTACION, ejg.FECHALIMITEPRESENTACION, ejg.PROCURADORNECESARIO, ejg.OBSERVACIONES, ejg.DELITOS"+
							  ",ejg."+ScsEJGBean.C_IDPROCURADOR+
							  ",ejg."+ScsEJGBean.C_IDINSTITUCIONPROCURADOR+
							  ",ejg."+ScsEJGBean.C_NUMERO_CAJG+
							  ",ejg."+ScsEJGBean.C_ANIO_CAJG +
							  ",ejg."+ScsEJGBean.C_NUMERODILIGENCIA + " " + ScsEJGBean.C_NUMERODILIGENCIA +
							  ",ejg."+ScsEJGBean.C_NUMEROPROCEDIMIENTO + " " + ScsEJGBean.C_NUMEROPROCEDIMIENTO +
							  ",ejg."+ScsEJGBean.C_ANIOPROCEDIMIENTO + " " + ScsEJGBean.C_ANIOPROCEDIMIENTO +
							  ",ejg."+ScsEJGBean.C_JUZGADO + " " + ScsEJGBean.C_JUZGADO +
							  ",ejg."+ScsEJGBean.C_JUZGADOIDINSTITUCION + " " + ScsEJGBean.C_JUZGADOIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_COMISARIA + " " + ScsEJGBean.C_COMISARIA +
							  ",ejg."+ScsEJGBean.C_COMISARIAIDINSTITUCION + " " + ScsEJGBean.C_COMISARIAIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_GUARDIATURNO_IDTURNO + " IDTURNO " +
							  ",ejg."+ScsEJGBean.C_SUFIJO + " SUFIJO " + 
							  ",ejg."+ScsEJGBean.C_IDORIGENCAJG + " IDORIGENCAJG " + 
							  ",ejg.IDTIPORATIFICACIONEJG"+
							  ",ejg.fechaRatificacion"+
							  ",designa.codigo codigo";
			// Ahora las tablas de donde se sacan los campos
			consulta += " from scs_ejg ejg, scs_personajg personajg, cen_colegiado colegiado, scs_turno turno, scs_guardiasturno guardia, " +
					   "scs_soj soj, scs_designa designa, scs_tipoejg tipoejg, scs_tiposoj tiposoj, scs_asistencia asistencia, scs_tipoejgcolegio tipoejgcolegio, " +
					   "cen_persona persona,scs_ejgdesigna ejgd";
			// Y por �ltimo efectuamos la join
			consulta += " where ejg.idinstitucion             = turno.idinstitucion(+) and " +
					      " ejg.guardiaturno_idturno      = turno.idturno(+) and " +     
					      " ejg.IDTIPOEJG                 = tipoejg.IDTIPOEJG and "+
						  "ejg.idinstitucion             = guardia.idinstitucion(+) and " +
						  "ejg.guardiaturno_idturno      = guardia.idturno(+) and " +
						  "ejg.guardiaturno_idguardia    = guardia.idguardia(+) and " +   
						  "personajg.idinstitucion    (+)= ejg.idinstitucion and " +       
						  "personajg.idpersona        (+)= ejg.idpersonajg and " +         
						  "ejg.idinstitucion             = colegiado.idinstitucion(+) and " +    
						  "ejg.idpersona                 = colegiado.idpersona(+) and " +        
						  "soj.idinstitucion (+)= ejg.idinstitucion and " +
						  "soj.ejgidtipoejg	 (+)= ejg.idtipoejg and " +
						  "soj.ejganio (+)         = ejg.anio and " +
						  "soj.ejgnumero(+)        = ejg.numero and " +
						  "asistencia.idinstitucion (+)= ejg.idinstitucion and " +
						  "asistencia.ejganio (+)= ejg.anio and " +
						  "asistencia.ejgnumero (+)= ejg.numero " +
						 
						  "and designa.idinstitucion(+) = ejgd.idinstitucion " +
						  "and designa.anio(+) = ejgd.aniodesigna " +
						  "and designa.numero(+) = ejgd.numerodesigna " +
						  "and designa.idturno(+) = ejgd.idturno " +
						  
						  "and ejg.idinstitucion=ejgd.idinstitucion(+) " +
						  "and ejg.anio=ejgd.anioejg(+) " +
						  "and ejg.numero=ejgd.numeroejg(+) " +
						  "and ejg.idtipoejg=ejgd.idtipoejg(+) and " +

						  "tipoejgcolegio.idinstitucion (+)= ejg.idinstitucion and " +
						  "tipoejgcolegio.idtipoejgcolegio (+)= ejg.idtipoejgcolegio and "+
						  "tiposoj.idtiposoj (+)= soj.idtiposoj and "+
						  "ejg.idpersona = persona.idpersona(+) and ";
			
			
			// Y por �ltimo filtramos por la clave principal de ejg
			consulta += " ejg.idtipoejg = " + miHash.get("IDTIPOEJG") + " and ejg.idinstitucion = " + miHash.get("IDINSTITUCION") + " and ejg.anio = " + miHash.get("ANIO") + " and ejg.numero = " + miHash.get("NUMERO");
			
			// jbd inc-6803 Ordenamos para quedarnos solo con la mas moderna
			consulta += " order by designa.fechaentrada desc";
			
			
			// Volvemos a obtener de base de datos la informaci�n, para que se la m�s act�al que hay en la base de datos			
			Vector resultado = admBean.selectGenerico(consulta);
			String fechaapertura="", tipoejg="",dictamen="",fechapresen="", fechalimite="",idorigen="", ano="",num="", sufijo="",estadoEjg="",observaciones="",fechaResolucionCAJG="";
			String lenguaje = this.getUserBean(request).getLanguage();
			String idpreten= "";
			String idcomi= "";
			String idjuzgado= "";
			String idcomiInsti= "";
			String idjuzgadoInsti= "";
			String idInsti= "";
			String descripcionDictamen="";
		
			try{
				ejg = (Hashtable)resultado.get(0);
			}catch (Exception e) {
				throwExcp("error.general.yanoexiste",e,null);
			}
			String estado = "", nombre="",apellido1="",apellido2="",nombreSolicita="",observa = "",calidad="",nproc="",numDili="", anioproc ="", idTipoRatificacionEJG="",fechaRatificacion ="";
			if (!resultado.isEmpty()) {apellido1 =(String) ((Hashtable)resultado.get(0)).get("APELLIDO1ASISTIDO");}		
			if (!resultado.isEmpty()) {apellido2 = (String)((Hashtable)resultado.get(0)).get("APELLIDO2ASISTIDO");}		
			if (!resultado.isEmpty()) {nombre = (String) ((Hashtable)resultado.get(0)).get("NOMBREASISTIDO");}	
			if (!resultado.isEmpty()) {observa = (String) ((Hashtable)resultado.get(0)).get("TIPOEJGCOLEGIO");}
			if (!resultado.isEmpty()) {fechaapertura = (String)((Hashtable)resultado.get(0)).get("FECHAAPERTURA");}				
			if (!resultado.isEmpty()) {tipoejg = (String)((Hashtable)resultado.get(0)).get("TIPOEJG");}	
			if (!resultado.isEmpty()) {dictamen = (String)((Hashtable)resultado.get(0)).get("IDTIPODICTAMENEJG");}
			if (!resultado.isEmpty()) {fechapresen = (String)((Hashtable)resultado.get(0)).get("FECHAPRESENTACION");}	
			if (!resultado.isEmpty()) {fechalimite = (String)((Hashtable)resultado.get(0)).get("FECHALIMITEPRESENTACION");}
			if (!resultado.isEmpty()) {ano = (String)((Hashtable)resultado.get(0)).get("ANIOCAJG");}
			if (!resultado.isEmpty()) {num = (String)((Hashtable)resultado.get(0)).get("NUMERO_CAJG");}	
			if (!resultado.isEmpty()) {idorigen = (String)((Hashtable)resultado.get(0)).get("IDORIGENCAJG");}
			if (!resultado.isEmpty()) {sufijo = (String)((Hashtable)resultado.get(0)).get("SUFIJO");}	
			if (!resultado.isEmpty()) {calidad = (String)((Hashtable)resultado.get(0)).get("CALIDAD");}			
			if (!resultado.isEmpty()) {nproc = (String)((Hashtable)resultado.get(0)).get("NUMEROPROCEDIMIENTO");}
			if (!resultado.isEmpty()) {anioproc = (String)((Hashtable)resultado.get(0)).get("ANIOPROCEDIMIENTO");}
			if (!resultado.isEmpty()) {numDili = (String)((Hashtable)resultado.get(0)).get("NUMERODILIGENCIA");}
			if (!resultado.isEmpty()) {observaciones = (String)((Hashtable)resultado.get(0)).get("OBSERVACIONES");}
			if (!resultado.isEmpty()) {idpreten = (String)((Hashtable)resultado.get(0)).get("IDPRETENSION");}			
			if (!resultado.isEmpty()) {idjuzgado = (String)((Hashtable)resultado.get(0)).get("JUZGADO");}
			if (!resultado.isEmpty()) {idcomi = (String)((Hashtable)resultado.get(0)).get("COMISARIA");}
			if (!resultado.isEmpty()) {idjuzgadoInsti = (String)((Hashtable)resultado.get(0)).get("JUZGADOIDINSTITUCION");}
			if (!resultado.isEmpty()) {idcomiInsti = (String)((Hashtable)resultado.get(0)).get("COMISARIAIDINSTITUCION");}						
			if (!resultado.isEmpty()) {idInsti = (String)((Hashtable)resultado.get(0)).get("IDINSTITUCION");}
			if (!resultado.isEmpty()) {idTipoRatificacionEJG = (String)((Hashtable)resultado.get(0)).get("IDTIPORATIFICACIONEJG");}
			if (!resultado.isEmpty()) {fechaRatificacion = (String)((Hashtable)resultado.get(0)).get("FECHARATIFICACION");}
			if (!resultado.isEmpty()) {fechaResolucionCAJG = (String)((Hashtable)resultado.get(0)).get("FECHARESOLUCIONCAJG");}
			
			
			
			String pretension = "", comisaria = "",origen ="", juzgado="";
			if(idpreten!=null && !idpreten.trim().equals("")){
			
				String comboPretensiones="SELECT IDPRETENSION AS ID, f_siga_getrecurso (DESCRIPCION,"+lenguaje+") AS DESCRIPCION "+ 
				"FROM SCS_PRETENSION WHERE IDINSTITUCION = "+idInsti+" AND IDPRETENSION = "+idpreten;
				resultado.clear();
				resultado = admBean.selectGenerico(comboPretensiones);
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { pretension = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			if(idcomi!=null && !idcomi.trim().equals("")){
				String comboComisariasTurno="SELECT C.IDCOMISARIA || ',' || C.IDINSTITUCION AS ID, c.NOMBRE || ' (' || po.nombre || ')' AS DESCRIPCION " +
						" FROM SCS_COMISARIA c, cen_poblaciones po where c.idpoblacion = po.idpoblacion and c.IDINSTITUCION = "+idcomiInsti+" AND " +
						" c.idcomisaria = "+idcomi;
				resultado.clear();
				resultado = admBean.selectGenerico(comboComisariasTurno);
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { comisaria = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			if(idorigen!=null && !idorigen.trim().equals("")){
				String origenCAJG="select F_SIGA_GETRECURSO(DESCRIPCION, "+lenguaje+") as DESCRIPCION from scs_origencajg where idorigencajg="+idorigen;
				resultado.clear();
				resultado = admBean.selectGenerico(origenCAJG);
				
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { origen = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			
			if(dictamen != null && !"".equalsIgnoreCase(dictamen)){
				String descripcionDictamenQuery= "select f_siga_getrecurso(descripcion,1) as descripcion from scs_tipodictamenejg  where idinstitucion ="+idInsti+" and idtipodictamenejg="+dictamen;
				resultado.clear();
				resultado = admBean.selectGenerico(descripcionDictamenQuery);
				
				if (!resultado.isEmpty()) { descripcionDictamen = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
				
			}
			if(idjuzgado!=null && !idjuzgado.trim().equals("")){
				String comboJuzgados="SELECT IDJUZGADO || ',' || IDINSTITUCION AS ID,decode(j.fechabaja, null ,j.NOMBRE || ' (' || p.nombre || ')'," +
						"j.NOMBRE || ' (' || p.nombre || ') (BAJA)') AS DESCRIPCION FROM SCS_JUZGADO j, cen_poblaciones p WHERE " +
						"j.idpoblacion = p.idpoblacion(+) AND (IDINSTITUCION = "+idjuzgadoInsti+" OR IDINSTITUCION = 2000) and IDJUZGADO=" +idjuzgado;
						
				resultado.clear();
				resultado = admBean.selectGenerico(comboJuzgados);
				
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { juzgado = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
				
			}
			
			consulta = "SELECT F_SIGA_GETRECURSO(F_SIGA_GET_ULTIMOESTADOEJG(" +
					miHash.get("IDINSTITUCION") +
					", " + miHash.get("IDTIPOEJG") +
					", " + miHash.get("ANIO") +
					", " + miHash.get("NUMERO") + "), " + this.getUserBean(request).getLanguage() + ") as DESCRIPCION FROM DUAL";
			
			resultado.clear();
			resultado = admBean.selectGenerico(consulta);
			
			//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
			if (!resultado.isEmpty()) {estado = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}		
			
			
			
			
			if(nombre!=null  && !nombre.trim().equals(""))
				nombreSolicita=nombre;
			if(apellido1!=null && !apellido1.trim().equals(""))
				nombreSolicita+=" "+apellido1;
			if(apellido2!=null)
				nombreSolicita+=" "+apellido2;
			
			
			// Obtenemos el procurador seleccionado:
			// Obtengo el idProcurador y la idInstitucion del procurador:
			String idProcurador = (String)ejg.get(ScsEJGBean.C_IDPROCURADOR);
			String idInstitucionProcurador = (String)ejg.get(ScsEJGBean.C_IDINSTITUCIONPROCURADOR);
			
			try {
				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
				ScsProcuradorBean b = (ScsProcuradorBean)(procuradorAdm.select(h)).get(0);
				}
			catch (Exception e) {}
			
//			String procurador = idProcurador+","+idInstitucionProcurador;
			/*int i = new Integer( (String) miHash.get("NUMERO")).intValue();
			if(sufijo!=null && !sufijo.trim().equals("")){
				ejg.put(ScsEJGBean.C_SUFIJO,sufijo);
				i= i+new Integer(sufijo).intValue();
				ejg.put(ScsEJGBean.C_NUMERO,""+i);
			}	*/
			if(ano!=null)
				ejg.put(ScsEJGBean.C_ANIO_CAJG,ano);
			if(num!=null)
				ejg.put(ScsEJGBean.C_NUMERO_CAJG,num);
			if(origen!=null)
				ejg.put(ScsEJGBean.C_ORIGENAPERTURA,origen);
			if(fechapresen!=null  && !fechapresen.trim().equals("")){
				ejg.put(ScsEJGBean.C_FECHAPRESENTACION,GstDate.getFormatedDateShort("",fechapresen));
			}
			if(fechalimite!=null&& !fechalimite.trim().equals("")){
				ejg.put(ScsEJGBean.C_FECHALIMITEPRESENTACION,GstDate.getFormatedDateShort("",fechalimite));
			}
			if(dictamen!=null)
				ejg.put(ScsEJGBean.C_DICTAMEN,dictamen);
		
			if(fechaapertura!=null && !fechaapertura.trim().equals("")){
				ejg.put(ScsEJGBean.C_FECHAAPERTURA,GstDate.getFormatedDateShort("",fechaapertura));
			}
			if(nombreSolicita!=null)
				ejg.put(ScsEJGBean.C_TIPOLETRADO,nombreSolicita);

//			if(procurador!=null)	
//				ejg.put(ScsEJGBean.C_PROCURADOR,procurador);
			if(calidad!=null)
				ejg.put(ScsEJGBean.C_CALIDAD,calidad);
			if(nproc!=null)
				ejg.put(ScsEJGBean.C_NUMEROPROCEDIMIENTO,nproc);
			
			if(anioproc!=null)
				ejg.put(ScsEJGBean.C_ANIOPROCEDIMIENTO,anioproc);			
			
			if(numDili!=null)
				ejg.put(ScsEJGBean.C_NUMERODILIGENCIA,numDili);
			
			if(observaciones!=null)
				ejg.put(ScsEJGBean.C_OBSERVACIONES,observaciones);
			ejgBean =(ScsEJGBean) admBean.hashTableToBean(ejg);
			if(tipoejg!=null)
				ejgBean.setDeTipoEjg(tipoejg);
			if(estado!=null)
				ejgBean.setEstadoEjg(estado);
			if(observa!=null)
				ejgBean.setTipoEjgCol(observa);
			if(origen!=null)
				ejgBean.setDescripcionOrigen(origen);
			if(comisaria!=null)
				ejgBean.setDescripcionComisaria(comisaria);
			if(juzgado!=null)
				ejgBean.setDescripcionJuzgado(juzgado);
			if(pretension!=null)
				ejgBean.setDescripcionPretension(pretension);
			if(idTipoRatificacionEJG!=null && !"".equalsIgnoreCase(idTipoRatificacionEJG))
				ejgBean.setIdTipoRatificacionEJG(Integer.parseInt(idTipoRatificacionEJG));
			if(fechaRatificacion!=null && !"".equalsIgnoreCase(fechaRatificacion))
				ejgBean.setFechaRatificacion(fechaRatificacion);
			if(fechaResolucionCAJG!=null && !"".equalsIgnoreCase(fechaResolucionCAJG))
				ejgBean.setFechaResolucionCAJG(fechaResolucionCAJG);
	
			if(descripcionDictamen !=null && !"".equalsIgnoreCase(descripcionDictamen))
				ejgBean.setDescripcionDictamen(descripcionDictamen); 

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}			
		return ejgBean;	
	}


	
}