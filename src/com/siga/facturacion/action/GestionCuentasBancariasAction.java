package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
import org.redabogacia.sigaservices.app.autogen.model.FacSeriefacturacionBanco;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.vo.BancoVo;
import org.redabogacia.sigaservices.app.vo.fac.CuentaBancariaVo;
import org.redabogacia.sigaservices.app.vo.fac.SeriesCuentaBancariaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBancosAdm;
import com.siga.beans.CenBancosBean;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPaisBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.FacSufijoAdm;
import com.siga.beans.FacSufijoBean;
import com.siga.comun.VoUiService;
import com.siga.facturacion.form.CuentasBancariasForm;
import com.siga.facturacion.form.service.CuentaBancariaVoService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class GestionCuentasBancariasAction extends MasterAction {
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
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultaBancos")){
						getConsultaBancos (mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("buscar")){
						mapDestino = buscar (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrar")){
						mapDestino = borrar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultar")){
						mapDestino = consultar(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("editar")){
						mapDestino = editar(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("modificar")){
						mapDestino = modificar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("refrescar")){
						mapDestino = refrescar(mapping, miForm, request, response);		
					}else if ( accion.equalsIgnoreCase("seriesDisponibles")){
						mapDestino = seriesDisponibles(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("guardarRelacionSerie")){
						mapDestino = guardarRelacionSerie(mapping, miForm, request, response);
					}else {
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

	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		cuentasBancariasForm.setIdInstitucion(usrBean.getLocation());
		BusinessManager bm = getBusinessManager();
		CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
		List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancosConCuentasBancarias(new Integer(usrBean.getLocation()));
		request.setAttribute("listaBancos", bancosList);

		
		
		return "inicio";
	}
	protected String refrescar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "exito";
	}
	
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		
		String forward = "listado";

		 
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			List<CuentaBancariaVo> cuentaBancariaVos =cuentasBancariasService.getCuentasBancarias(voService.getForm2Vo(cuentasBancariasForm));
			List<CuentasBancariasForm> cuentasBancarias = voService.getVo2FormList(cuentaBancariaVos);
			
			cuentasBancariasForm.setModo("abrir");
			request.setAttribute("cuentasBancarias", cuentasBancarias);

//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos("");
//			request.setAttribute("listaBancos", bancosList);
			
		} catch (Exception e){
			
			throwExcp("messages.general.errorExcepcion", e, null); 
			
		}
		return forward;
	}
	
	protected String nuevo (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		
		
//			BusinessManager bm = getBusinessManager();
//			org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos( new BancoVo());
//			request.setAttribute("listaBancos", bancosList);
		cuentasBancariasForm.setModo("insertar");
		//Combo sufijos
		FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
		Hashtable claves = new Hashtable ();
		UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
		
		Vector vsufijos = sufijoAdm.select(claves);
		Vector vsufijosList = new Vector();
		List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
		for (int vs = 0; vs < vsufijos.size(); vs++){
			
			FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
			sufijosListFinal.add(sufijosBean);
		}

		request.setAttribute("listaSufijos", sufijosListFinal);
		
		//Obtengo las series disponibles para la institución	
		BusinessManager bm = getBusinessManager();
		CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
		VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
		CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
		cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);

		return "editar";
	}
	
	protected String consultar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			request.setAttribute("seriesFacturacion", cuentasBancariasService.getSeriesCuentaBancaria(cuentaBancariaVo));
			cuentasBancariasForm.setIBAN(UtilidadesString.mostrarIBANConAsteriscos(cuentaBancariaVo.getIban()));
			cuentasBancariasForm.setCuentaBanco(UtilidadesString.mostrarNumeroCuentaConAsteriscos(cuentaBancariaVo.getNumerocuenta()));
			cuentasBancariasForm.setDigControlBanco("**");

			//Combos sufijos
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
			Vector vsufijosList = new Vector();
			List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
			for (int vs = 0; vs < vsufijos.size(); vs++){
				
				FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
				sufijosListFinal.add(sufijosBean);
			}

			request.setAttribute("listaSufijos", sufijosListFinal);
			
			cuentasBancariasForm.setModo("abrir");
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
			
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "editar";
	}
	/**
	 * Lo comenntamos porque no se usa
	 */
	
	protected String editar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			cuentasBancariasForm.setIBAN(cuentaBancariaVo.getIban());
			cuentasBancariasForm.setModo("modificar");
			request.setAttribute("seriesFacturacion", cuentasBancariasService.getSeriesCuentaBancaria(cuentaBancariaVo));
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
			
			//Combo sufijos
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
			Vector vsufijosList = new Vector();
			List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
			for (int vs = 0; vs < vsufijos.size(); vs++){
				
				FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
				sufijosListFinal.add(sufijosBean);
			}

			request.setAttribute("listaSufijos", sufijosListFinal);

		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "editar";
	}
	
	protected String borrar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService bts = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo = bts.getCuentaBancaria(cuentaBancariaVo);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			
			//Se comprueba si la cuenta bancaria tiene alguna serie asignada, si tiene no se permite eliminar 
			
			List series = bts.getSeriesCuentaBancaria(cuentaBancariaVo);
			
			//Si no existe ninguna serie relacionada
			if(series.isEmpty()){
				bts.delete(cuentaBancariaVo);
				request.removeAttribute("modal");
				request.removeAttribute("sinrefresco");
				request.setAttribute("mensaje","messages.deleted.success");
				forward = "exito";
			
			}else{
				throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.serie.relacionada"));
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}

	
	protected String insertar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();

			//Rellenamos el codigo del banco e insertamos un banco extranjero
			if(cuentasBancariasForm.getIBAN()!=null && !cuentasBancariasForm.getIBAN().equals("")){
				if(!cuentasBancariasForm.getIBAN().substring(0,2).equals("ES")){
					CenBancosAdm bancosAdm = new CenBancosAdm(usrBean);					
					CenBancosBean bancosBean = bancosAdm.existeBancoExtranjero(cuentasBancariasForm.getBIC());
					if(bancosBean == null){
						CenPaisAdm paisAdm = new CenPaisAdm(usrBean);
						CenPaisBean paisBean = paisAdm.getPaisByCodIso(cuentasBancariasForm.getIBAN().substring(0,2));
						bancosBean = bancosAdm.insertarBancoExtranjero(paisBean.getIdPais(), cuentasBancariasForm.getBIC());
					}					
					cuentasBancariasForm.setCodigoBanco(bancosBean.getCodigo());
				}else{
					cuentasBancariasForm.setCodigoBanco(cuentasBancariasForm.getIBAN().substring(4,8));
					cuentasBancariasForm.setSucursalBanco(cuentasBancariasForm.getIBAN().substring(8,12));
					cuentasBancariasForm.setDigControlBanco(cuentasBancariasForm.getIBAN().substring(12,14));		
					cuentasBancariasForm.setCuentaBanco(cuentasBancariasForm.getIBAN().substring(14));							
				}
			}
			
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			cuentasBancariasService.insert(cuentaBancariaVo);
			forward = exitoModal("messages.updated.success",request);
			
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	protected String modificar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;	
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		
		try {
			BusinessManager bm = getBusinessManager();			
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			cuentasBancariasService.update(cuentaBancariaVo);
			
			//Modificacion del idsufijo de la serie en fac_seriefacturacion_banco
			//listaseries tiene el formato: idserie,idsufijo;idserie,idsufijo;...;
			SeriesCuentaBancariaVo serieBancariaVo = new SeriesCuentaBancariaVo();
			serieBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			serieBancariaVo.setIdinstitucion((short) Integer.parseInt(cuentasBancariasForm.getIdInstitucion()));
			
			String listaseries=cuentasBancariasForm.getListaSeries();
			
			int numSeries=0;
			String puntoComa=";";
			Integer idserie=0;
			Integer idsufijo=0;
			String bancos_codigo;
			String listaseries_aux=listaseries;
			
			while (listaseries_aux.indexOf(puntoComa) > -1) {
		      listaseries_aux = listaseries_aux.substring(listaseries_aux.indexOf(puntoComa)+puntoComa.length(),listaseries_aux.length());
		      numSeries++;  
			}
			
			for(int s=0;s<numSeries;s++){
				
				String datosSerie= listaseries.split(";")[s];
				
				idserie = Integer.parseInt(datosSerie.split(",")[0]);
				bancos_codigo = datosSerie.split(",")[1];
				
				//Si la serie no tiene ningún sufijo asociado
				if(datosSerie.endsWith(","))
					idsufijo=0;
				else
					idsufijo = Integer.parseInt(datosSerie.split(",")[2]);
				
				//Se actualiza el idsufijo de la serie asociada a la cuenta bancaria
				serieBancariaVo.setBancos_codigo(bancos_codigo);
				serieBancariaVo.setIdseriefacturacion(idserie.longValue());
				serieBancariaVo.setIdsufijo(idsufijo);

				cuentasBancariasService.updateSufijoSerieCuentaBancaria(serieBancariaVo);
			}	
			
			forward = exitoModal("messages.updated.success",request);
			
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	protected void getConsultaBancos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			
			BancoVo banco = new BancoVo();
			banco.setNombre(cuentasBancariasForm.getBancoAjaxIn());
			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos(banco);
			JSONObject json = new JSONObject();
			JSONArray lisatBancosJsonArray = new JSONArray();
			for (CenBancos cenBancos : bancosList) {
				JSONObject obj = new JSONObject();
				obj.put("codigobanco", cenBancos.getCodigo());
	            obj.put("descripcionbanco", cenBancos.getNombre());
	            lisatBancosJsonArray.put(obj);
			}
			json.put("listaBancos", lisatBancosJsonArray);
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
		    response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
	}
	
	
	protected String seriesDisponibles (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			request.setAttribute("listaSeriesDisponibles", cuentasBancariasService.getSeriesDisponiblesCuentaBanc(cuentaBancariaVo));
			
			//Combo sufijos
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
			Vector vsufijosList = new Vector();
			List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
			for (int vs = 0; vs < vsufijos.size(); vs++){
				
				FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
				sufijosListFinal.add(sufijosBean);
			}
	
			request.setAttribute("listaSufijos", sufijosListFinal);
			

		}catch (Exception e){
			throw new SIGAException("messages.general.error", e, null); 			
		}
		
		return "relacionarNuevaSerie";
	}
	
	protected String guardarRelacionSerie (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		String forward="exception";
		
		try {

			CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;	
			UsrBean usrBean = this.getUserBean(request);
			
			BusinessManager bm = getBusinessManager();			
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			FacSeriefacturacionBanco serieFacturacionBanco = new FacSeriefacturacionBanco();
			serieFacturacionBanco.setUsumodificacion(new Integer(usrBean.getUserName()));
			serieFacturacionBanco.setIdinstitucion(Short.parseShort(cuentasBancariasForm.getIdInstitucion()));
			serieFacturacionBanco.setIdseriefacturacion(Long.parseLong(cuentasBancariasForm.getIdSerieFacturacion()));
			serieFacturacionBanco.setBancosCodigo(cuentasBancariasForm.getBancosCodigo());
			serieFacturacionBanco.setIdsufijo(cuentasBancariasForm.getIdSufijoSerie());
			cuentasBancariasService.insertRelacionSerie(serieFacturacionBanco);
			
			forward = exitoModal("messages.updated.success",request);
			
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e, null); 			
		}
		
		return forward;
	}

}