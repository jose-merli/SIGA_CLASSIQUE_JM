package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsComisariaAdm;
import com.siga.beans.ScsComisariaBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTipoAsistenciaColegioAdm;
import com.siga.beans.ScsTipoAsistenciaColegioBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.VolantesExpressForm;
import com.siga.gratuita.service.VolantesExpressService;
import com.siga.gratuita.vos.VolantesExpressVo;

import es.satec.businessManager.BusinessManager;


public class VolantesExpressAction extends MasterAction 
{
	private static BusinessManager businessManager=null;
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			if(getBusinessManager()==null)
				businessManager = BusinessManager.getInstance(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.ATOS_BUSINESS_CONFIG));
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					
					if(modo!=null)
						accion = modo;
//					System.out.println("ACCION:"+accion);
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxTurnos")){
						getAjaxTurnos(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxGuardias")){
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxColegiados")){
						getAjaxColegiados(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxColegiado")){
						getAjaxColegiado(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxColegiadoGuardia")){
						getAjaxColegiadoGuardia(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaAsistencias")){
						mapDestino = getAjaxBusquedaAsistencias (mapping, miForm, request, response);
						
					}else if ( accion.equalsIgnoreCase("getAjaxGuardarAsistencias")){
						mapDestino = getAjaxGuardarAsistencias (mapping, miForm, request, response);
					}
					else {
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
	
	@SuppressWarnings("unchecked")
	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setUsrBean(usrBean);
		miForm.setIdInstitucion(usrBean.getLocation());
		Date hoy = new Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String sHoy = sdf2.format(hoy);
		miForm.setFechaJustificacion(sHoy);

		ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String idTipoAsistencia = rp3.returnProperty("codigo.general.scs_tipoasistencia.volanteExpres");
		miForm.setIdTipoAsistencia(idTipoAsistencia);
		
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		String delitos_VE = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DELITOS_VE, "");
		Boolean isDelitosVE = new Boolean((delitos_VE!=null && delitos_VE.equalsIgnoreCase(ClsConstants.DB_TRUE)));
		miForm.setDelitos(isDelitosVE);

		//Sacamos los turnos
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(miForm.getUsrBean());
		List<ScsTurnoBean> turnos = admTurnos.getTurnos(miForm.getVolanteExpressVo());
		miForm.setTurnos(turnos);
		miForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		miForm.setColegiadosGuardia(new ArrayList<CenPersonaBean>());
		miForm.setLugar("centro");
		miForm.setMsgError("");
		miForm.setMsgAviso("");
		
		ScsTipoAsistenciaColegioAdm admTiposAsis = new ScsTipoAsistenciaColegioAdm(miForm.getUsrBean());
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put(ScsTipoAsistenciaColegioBean.C_IDINSTITUCION, miForm.getIdInstitucion());
		Vector<ScsTipoAsistenciaColegioBean> vTiposAsistenciaColegio = (Vector<ScsTipoAsistenciaColegioBean>)admTiposAsis.selectCombo(ht,true);
		miForm.setTiposAsistenciaColegio(vTiposAsistenciaColegio);
		// Por defecto marcamos "Guardia 24h. Asistencia al detenido. Procedimiento general"
		miForm.setIdTipoAsistenciaColegio(String.valueOf(ScsTipoAsistenciaColegioBean.TIPO_ASISTENCIA_DETENIDO_PROC_GENERAL));
		 
		return "inicio";
	}

	protected String getTurnos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Sacamos los turnos
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(miForm.getUsrBean());
		List<ScsTurnoBean> alTurnos = admTurnos.getTurnos(miForm.getVolanteExpressVo());
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
			
		}
		miForm.setTurnos(alTurnos);
		miForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		miForm.setColegiadosGuardia(new ArrayList<CenPersonaBean>());
		
		miForm.setIdTurno(null);
		miForm.setIdGuardia(null);
		miForm.setIdColegiado(null);
		miForm.setIdColegiadoGuardia(null);
		miForm.setIdColegiadoSustituido(null);
		
		return "inicio";
	}
	protected void getAjaxTurnos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		miForm.setFechaGuardia(fechaGuardia);
		
		//Sacamos los turnos
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(miForm.getUsrBean());
		Collection<ScsTurnoBean> alTurnos = admTurnos.getTurnos(miForm.getVolanteExpressVo());
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
			
		}
	    respuestaAjax(new AjaxCollectionXmlBuilder<ScsTurnoBean>(), alTurnos,response);
	    
		
	}
	
	protected void getAjaxGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		miForm.setFechaGuardia(fechaGuardia);
		String idTurno = request.getParameter("idTurno");
		miForm.setIdTurno(idTurno);
		//Sacamos las guardias si hay algo selccionado en el turno
		Collection<ScsGuardiasTurnoBean> alGuardias = null;
		if(miForm.getIdTurno()!= null && !miForm.getIdTurno().equals("-1")&& !miForm.getIdTurno().equals("")){
			ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(miForm.getUsrBean());
			alGuardias = admGuardias.getGuariasTurnos(miForm.getVolanteExpressVo());
		}
		if(alGuardias==null){
			alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), alGuardias,response);
	}
	
	protected void getAjaxCentros (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		String idTurno = request.getParameter("idTurno");
		miForm.setIdTurno(idTurno);
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsComisariaBean> alComisarias = null;
		if(miForm.getIdTurno()!= null && !miForm.getIdTurno().equals("-1")&& !miForm.getIdTurno().equals("")){
			ScsComisariaAdm admComisarias = new ScsComisariaAdm(miForm.getUsrBean());
			alComisarias = admComisarias.getComisarias(miForm.getVolanteExpressVo());
		}
		if(alComisarias==null){
			alComisarias = new ArrayList<ScsComisariaBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), alComisarias,response);
	}
	
	
	@SuppressWarnings("unchecked")
	protected void getAjaxColegiados (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		miForm.setFechaGuardia(fechaGuardia);
		String idTurno = request.getParameter("idTurno");
		miForm.setIdTurno(idTurno);
		String idGuardia = request.getParameter("idGuardia");
		miForm.setIdGuardia(idGuardia);

		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<CenPersonaBean> alColegiadosGuardias = null;
		if(miForm.getIdGuardia()!= null && !miForm.getIdGuardia().equals("")&& !miForm.getIdGuardia().equals("-1")){
			ScsGuardiasColegiadoAdm admGuardiasCol = new ScsGuardiasColegiadoAdm(miForm.getUsrBean());
			alColegiadosGuardias = admGuardiasCol.getColegiadosGuardia(miForm.getVolanteExpressVo());
		}
		if(alColegiadosGuardias==null){
			alColegiadosGuardias = new ArrayList<CenPersonaBean>();
			
		}
		miForm.setColegiadosGuardia(alColegiadosGuardias);

		respuestaAjax(new AjaxCollectionXmlBuilder<CenPersonaBean>(), alColegiadosGuardias,response);

	}
	@SuppressWarnings("unchecked")
	protected void getAjaxColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		String numeroColegiado = request.getParameter("numeroColegiado");
		miForm.setNumeroColegiado(numeroColegiado);
		
		if(numeroColegiado!= null && !numeroColegiado.equals("")){
			CenClienteAdm admCli = new CenClienteAdm(miForm.getUsrBean());
			Vector<Hashtable<String, Object>> vClientes = admCli.getClientePorNColegiado(miForm.getIdInstitucion(),miForm.getNumeroColegiado());
			if(vClientes!=null &&vClientes.size()>0)
				htCliente = vClientes.get(0);
		}
		String nombreColegiado = null;
		String idColegiado = null;
		
		if(htCliente!=null){
			nombreColegiado = (String)htCliente.get("NOMCOLEGIADO");
			idColegiado = (String)htCliente.get("IDPERSONA");
			
		}else{
			nombreColegiado = "";
			idColegiado = "";
			numeroColegiado = "";
			
		}
		
		List listaParametros = new ArrayList();
		listaParametros.add(idColegiado);
		listaParametros.add(numeroColegiado);
		listaParametros.add(nombreColegiado);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		
	}
	

	@SuppressWarnings("unchecked")
	protected void getAjaxColegiadoGuardia (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		String idColegiadoGuardia = request.getParameter("idColegiadoGuardia");

		if(idColegiadoGuardia!= null && !idColegiadoGuardia.equals("-1") && !idColegiadoGuardia.equals("")){
			CenPersonaAdm admPer = new CenPersonaAdm(miForm.getUsrBean());
			htCliente = admPer.getPersonYnColegiado(idColegiadoGuardia,new Integer(miForm.getIdInstitucion()));
			
		}
		String nombreColegiado = null;
		String idColegiado = null;
		String numeroColegiado = null;		
		if(htCliente!=null){
			nombreColegiado = (String)htCliente.get("NOMCOLEGIADO");
			idColegiado = (String)htCliente.get("IDPERSONA");
			numeroColegiado = (String)htCliente.get("NCOLEGIADO");
			
		}else{
			nombreColegiado = "";
			idColegiado = "";
			numeroColegiado = "";
			
		}
		
		miForm.setNumeroColegiado(numeroColegiado);
		miForm.setNombreColegiado(nombreColegiado);
		miForm.setIdColegiado(idColegiado);

		List listaParametros = new ArrayList();
		listaParametros.add(idColegiado);
		listaParametros.add(numeroColegiado);
		listaParametros.add(nombreColegiado);
		
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		
	}
	protected String getAjaxBusquedaAsistencias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		miForm.setFechaGuardia(fechaGuardia);
		String idTurno = request.getParameter("idTurno");
		miForm.setIdTurno(idTurno);
		String idGuardia = request.getParameter("idGuardia");
		miForm.setIdGuardia(idGuardia);
		String lugar = request.getParameter("lugar");
		miForm.setLugar(lugar);
		String idTipoAsistenciaColegio = request.getParameter("idTipoAsistenciaColegio");
		miForm.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio);
		String idColegiado = request.getParameter("idColegiado");
		miForm.setIdColegiado(idColegiado);
		String idColegiadoGuardia = request.getParameter("idColegiadoGuardia");
		miForm.setIdColegiadoGuardia(idColegiadoGuardia);
		
		miForm.setMsgAviso("");
		miForm.setMsgError("");
		
		ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm (this.getUserBean(request));
		VolantesExpressVo volantesExpressVo = miForm.getVolanteExpressVo();
		 
		try {
			
			List<ScsAsistenciasBean> alAsistencias = admAsistencias.getAsistenciasVolantesExpres(volantesExpressVo);
			miForm.setAsistencias(alAsistencias);
			
		} catch (ClsExceptions e) {
			miForm.setAsistencias(new ArrayList<ScsAsistenciasBean> ());
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),e.getMsg());
			miForm.setMsgError(error);
		}catch (Exception e){
			miForm.setAsistencias(new ArrayList<ScsAsistenciasBean> ());
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"messages.general.errorExcepcion");
			miForm.setMsgError(error);
			
		}
		return "listadoAsistencias";
	}
	@SuppressWarnings("unchecked")
	protected String getAjaxGuardarAsistencias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException, Exception 
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		miForm.setFechaGuardia(fechaGuardia);
		String idTurno = request.getParameter("idTurno");
		miForm.setIdTurno(idTurno);
		String idGuardia = request.getParameter("idGuardia");
		miForm.setIdGuardia(idGuardia);
		String lugar = request.getParameter("lugar");
		miForm.setLugar(lugar);
		String idTipoAsistenciaColegio = request.getParameter("idTipoAsistenciaColegio");
		miForm.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio);
		String idColegiado = request.getParameter("idColegiado");
		miForm.setIdColegiado(idColegiado);
		String idColegiadoGuardia = request.getParameter("idColegiadoGuardia");
		miForm.setIdColegiadoGuardia(idColegiadoGuardia);
		String idColegiadoSustituido = request.getParameter("idColegiadoSustituido");
		miForm.setIdColegiadoSustituido(idColegiadoSustituido);
		String fechaJustificacion = request.getParameter("fechaJustificacion");
		miForm.setFechaJustificacion(GstDate.getApplicationFormatDate("", fechaJustificacion));
		String datosAsistencias = (String)request.getParameter("datosAsistencias");
		miForm.setDatosAsistencias(datosAsistencias);
		miForm.setMsgAviso("");
		miForm.setMsgError("");
		
	
		VolantesExpressVo volantesExpressVo = miForm.getVolanteExpressVo();
		ArrayList<ScsAsistenciasBean> alAsistenciaOld =  (ArrayList<ScsAsistenciasBean>)volantesExpressVo.getAsistencias(); 
		try {
			BusinessManager bm = getBusinessManager();
			VolantesExpressService ves = (VolantesExpressService)bm.getService(VolantesExpressService.class);
			ArrayList<ScsAsistenciasBean> alAsistencias = (ArrayList<ScsAsistenciasBean>) ves.executeService(volantesExpressVo);
			miForm.setAsistencias(alAsistencias);
			miForm.setMsgAviso(UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"messages.updated.success"));
		} catch (SIGAException e) {
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),e.getLiteral());
			miForm.setMsgError(error);
		}catch (ClsExceptions e) {
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),e.getMsg());
			miForm.setMsgError(error);
		}catch (Exception e){
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"messages.general.errorExcepcion");
			miForm.setMsgError(error);			
		}
		return "listadoAsistencias";
	}

	public static BusinessManager getBusinessManager() {
		return businessManager;
	}
}

