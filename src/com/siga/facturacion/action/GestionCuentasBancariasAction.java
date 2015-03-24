package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
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
import com.siga.beans.FacSerieFacturacionBancoAdm;
import com.siga.beans.FacSerieFacturacionBancoBean;
import com.siga.beans.FacSufijoAdm;
import com.siga.beans.FacSufijoBean;
import com.siga.beans.GenParametrosAdm;
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
 * La imaginaci�n es m�s importante que el conocimiento
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
	
	protected String nuevo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		
		UsrBean user = this.getUserBean(request);
		
		
//			BusinessManager bm = getBusinessManager();
//			org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos( new BancoVo());
//			request.setAttribute("listaBancos", bancosList);
		cuentasBancariasForm.setModo("insertar");
		//Combo sufijos
		FacSufijoAdm sufijoAdm = new FacSufijoAdm (user);
		Hashtable<String,String> claves = new Hashtable<String,String>();
		UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
		
		Vector vsufijos = sufijoAdm.select(claves);
		List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
		for (int vs = 0; vs < vsufijos.size(); vs++){
			
			FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
			sufijosListFinal.add(sufijosBean);
		}

		request.setAttribute("listaSufijos", sufijosListFinal);
		
		//Obtengo las series disponibles para la instituci�n	
		BusinessManager bm = getBusinessManager();
		CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
		VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
		CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
		cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
		
		// obtengo el parametro general 'SEPA_TIPO_FICHEROS_ADEUDO
		GenParametrosAdm admParametros = new GenParametrosAdm(user);
		String tiposFicherosAdeudo = admParametros.getValor(user.getLocation(), "FAC", "SEPA_TIPO_FICHEROS_ADEUDO", "0"); // Por defecto solo n1914
		request.setAttribute("tiposFicherosAdeudo", tiposFicherosAdeudo);		

		return "editar";
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String consultar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			UsrBean user = this.getUserBean(request);
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
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (user);
			Hashtable<String,String> claves = new Hashtable<String,String>();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
			List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
			for (int vs = 0; vs < vsufijos.size(); vs++){
				
				FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
				sufijosListFinal.add(sufijosBean);
			}

			request.setAttribute("listaSufijos", sufijosListFinal);
			
			cuentasBancariasForm.setModo("abrir");
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
			
			// obtengo el parametro general 'SEPA_TIPO_FICHEROS_ADEUDO
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			String tiposFicherosAdeudo = admParametros.getValor(user.getLocation(), "FAC", "SEPA_TIPO_FICHEROS_ADEUDO", "0"); // Por defecto solo n1914
			request.setAttribute("tiposFicherosAdeudo", tiposFicherosAdeudo);
			
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "editar";
	}
	
	protected String editar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			UsrBean user = this.getUserBean(request);
			
			String Uso = cuentasBancariasForm.getUso();
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			cuentasBancariasForm.setIBAN(cuentaBancariaVo.getIban());
			cuentasBancariasForm.setModo("modificar");
			cuentasBancariasForm.setUso(Uso);
			request.setAttribute("seriesFacturacion", cuentasBancariasService.getSeriesCuentaBancaria(cuentaBancariaVo));
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
			
			//Combo sufijos
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (user);
			Hashtable<String,String> claves = new Hashtable<String,String>();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
			List <FacSufijoBean> sufijosListFinal= new ArrayList<FacSufijoBean>();
			for (int vs = 0; vs < vsufijos.size(); vs++){
				
				FacSufijoBean sufijosBean = (FacSufijoBean) vsufijos.get(vs);
				sufijosListFinal.add(sufijosBean);
			}

			request.setAttribute("listaSufijos", sufijosListFinal);
			
			// obtengo el parametro general 'SEPA_TIPO_FICHEROS_ADEUDO
			GenParametrosAdm admParametros = new GenParametrosAdm(user);
			String tiposFicherosAdeudo = admParametros.getValor(user.getLocation(), "FAC", "SEPA_TIPO_FICHEROS_ADEUDO", "0"); // Por defecto solo n1914
			request.setAttribute("tiposFicherosAdeudo", tiposFicherosAdeudo);		

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
			List<SeriesCuentaBancariaVo> series = bts.getSeriesCuentaBancaria(cuentaBancariaVo);
			
			//Si no existe ninguna serie relacionada
			if(!series.isEmpty())
				throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.serie.relacionada"));
			
			
			//Se comprueba si la cuenta tiene relacionado alg�n Pago SJCS pendiente de cerrar o cerrado y que no se ha generado el fichero de transferecia bancaria todav�a
			if(bts.getPagosSJCSPendientesActuaciones(cuentasBancariasForm.getIdInstitucion(),cuentasBancariasForm.getIdCuentaBancaria())>0)		
				throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.pagoSJCS.relacionado"));

			bts.delete(cuentaBancariaVo);
			request.removeAttribute("modal");
			request.removeAttribute("sinrefresco");
			request.setAttribute("mensaje","messages.deleted.success");
			forward = "exito";
		

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
			
			//Sino est� marcado el check SJCS se comprueba si existe una cuenta SJCS para la instituci�n, sino existe 
			//se muestra un mensaje para que se d� de alta
			if((cuentasBancariasForm.getSjcs() == null)||(cuentasBancariasForm.getSjcs().isEmpty()))
			{

				CuentaBancariaVo cuentaBancariaSJCSVo = new CuentaBancariaVo();
				cuentaBancariaSJCSVo.setIdinstitucion(Short.parseShort(cuentasBancariasForm.getIdInstitucion()));
				cuentaBancariaSJCSVo.setSjcs("1");
				
				List<CuentaBancariaVo> cuentasBancSJCS=cuentasBancariasService.getCuentasBancarias(cuentaBancariaSJCSVo);
				
				
				if(cuentasBancSJCS.isEmpty()){
				
					throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.SJCS.no.existe"));
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
			
			
			//Sino est� marcado el check SJCS se comprueba si existe una cuenta SJCS para la instituci�n, sino existe 
			//se muestra un mensaje para que se d� de alta
			if((cuentasBancariasForm.getSjcs() == null)||(cuentasBancariasForm.getSjcs().isEmpty()))
			{
				CuentaBancariaVo cuentaBancariaSJCSVo = new CuentaBancariaVo();
				cuentaBancariaSJCSVo.setIdinstitucion(Short.parseShort(cuentasBancariasForm.getIdInstitucion()));
				cuentaBancariaSJCSVo.setSjcs("1");
				
				List<CuentaBancariaVo> cuentasBancSJCS=cuentasBancariasService.getCuentasBancarias(cuentaBancariaSJCSVo);
				
				if(cuentasBancSJCS.isEmpty()){
				
					throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.SJCS.no.existe"));
				}
				
				//Se comprueba si la cuenta tiene relacionado alg�n Pago SJCS pendiente de cerrar o cerrado y que no se ha generado el fichero de transferecia bancaria todav�a
				if(cuentasBancariasService.getPagosSJCSPendientesActuaciones(cuentasBancariasForm.getIdInstitucion(),cuentasBancariasForm.getBancosCodigo())>0)
					throw new SIGAException(UtilidadesString.getMensajeIdioma(usrBean,"facturacion.message.error.cuenta.pagoSJCS.relacionado"));

			}

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
				
				//Si la serie no tiene ning�n sufijo asociado
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
			Hashtable<String,String> claves = new Hashtable<String,String>();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			
			Vector vsufijos = sufijoAdm.select(claves);
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

			UserTransaction tx = null;
			
			CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;	
			FacSerieFacturacionBancoAdm serieFacBancoAdm = new FacSerieFacturacionBancoAdm(this.getUserBean(request));

			//Se borra la relaci�n con las cuentas que existan
			tx = this.getUserBean(request).getTransaction();
			Hashtable<String,String> claves = new Hashtable<String,String>();
			UtilidadesHash.set (claves, FacSerieFacturacionBancoBean.C_IDINSTITUCION, cuentasBancariasForm.getIdInstitucion());
			UtilidadesHash.set (claves, FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION, cuentasBancariasForm.getIdSerieFacturacion());
			
			tx.begin();
			serieFacBancoAdm.delete(claves);

			//Se inserta la nueva relacion
			FacSerieFacturacionBancoBean serieFacturacionBancoBean = new FacSerieFacturacionBancoBean();
			serieFacturacionBancoBean.setIdInstitucion(Integer.parseInt(cuentasBancariasForm.getIdInstitucion()));
			serieFacturacionBancoBean.setIdSerieFacturacion(Integer.parseInt(cuentasBancariasForm.getIdSerieFacturacion()));
			serieFacturacionBancoBean.setBancos_codigo(cuentasBancariasForm.getBancosCodigo());
			serieFacturacionBancoBean.setIdSufijo(cuentasBancariasForm.getIdSufijoSerie());
			serieFacBancoAdm.insert(serieFacturacionBancoBean);
			tx.commit();
			forward = exitoModal("messages.updated.success",request);
						
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e, null); 			
		}
		
		return forward;
	}

}