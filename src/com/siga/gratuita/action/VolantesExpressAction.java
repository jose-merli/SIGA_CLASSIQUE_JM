package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.AjaxMultipleCollectionXmlBuilder;
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
import com.siga.beans.ScsDelitoAdm;
import com.siga.beans.ScsDelitoBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
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
					ClsLogging.writeFileLog("VOLANTES EXPRESS:accion:"+accion, 10);
					
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxTurnos")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxTurnos", 10);
						getAjaxTurnos(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxGuardias")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxGuardias", 10);
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxColegiados")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiados", 10);
						getAjaxColegiados(mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxSustituidos")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxSustituidos", 10);
						getAjaxSustituidos(mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxColegiado")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiado", 10);
						getAjaxColegiado(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxColegiadoGuardia")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiadoGuardia", 10);
						getAjaxColegiadoGuardia(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaAsistencias")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxBusquedaAsistencias", 10);
						mapDestino = getAjaxBusquedaAsistencias (mapping, miForm, request, response);
						
					}else if ( accion.equalsIgnoreCase("getAjaxPrimeraAsistencia")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxPrimeraAsistencia", 10);
						mapDestino = getAjaxPrimeraAsistencia (mapping, miForm, request, response);
						
					}else if ( accion.equalsIgnoreCase("getAjaxGuardarAsistencias")){
						ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxGuardarAsistencias", 10);
						mapDestino = getAjaxGuardarAsistencias (mapping, miForm, request, response);
					}
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN VOLANTES EXPRES mapping nulo!", 10);
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN VOLANTES EXPRES siga exception"+es.getLiteral(), 10);
			throw es;
		} catch (Exception e) {
			ClsLogging.writeFileLog("!!!!!!!!!!!!!!!ERROR EN VOLANTES EXPRES"+e.toString(), 10);
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
		miForm.setDelito(isDelitosVE);
		
		if(isDelitosVE){
			List<ScsDelitoBean> alDelitos = null;
			ScsDelitoAdm admDelitos = new ScsDelitoAdm(miForm.getUsrBean());
			alDelitos = admDelitos.getDelitos(miForm.getVolanteExpressVo());
			if(alDelitos==null){
				alDelitos = new ArrayList<ScsDelitoBean>();
			
			}
			miForm.setDelitos(alDelitos);
		}
		
		

		//Sacamos los turnos
//		ScsTurnoAdm admTurnos = new ScsTurnoAdm(miForm.getUsrBean());
//		List<ScsTurnoBean> turnos = new ArrayList<ScsTurnoBean>();// admTurnos.getTurnos(miForm.getVolanteExpressVo());
		miForm.setTurnos(new ArrayList<ScsTurnoBean>());
		miForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		miForm.setColegiadosGuardia(new ArrayList<CenPersonaBean>());
		miForm.setColegiadosSustituidos(new ArrayList<CenPersonaBean>());
		
		
		miForm.setComisarias(new ArrayList<ScsComisariaBean>());
		miForm.setJuzgados(new ArrayList<ScsJuzgadoBean>());
		
		
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

	private String getFechaGuardia(String fecha)throws ClsExceptions{
		if(fecha==null ||fecha.equals(""))
			return "";
		String checkstr = "0123456789";
		String fechaADevolver = "";
		String fechaTemp = "";
		String separador = "/";
		String day;
		String month;
		String year;
		int bisiesto = 0;
		
		for (int i = 0; i < fecha.length(); i++) {
			int indice = checkstr.indexOf(fecha.substring(i,i+1)); 
			if ( indice >= 0) {
				fechaTemp = fechaTemp + fecha.substring(i,i+1);
			}else{
				fechaTemp = fechaTemp + separador;
			}
		}
		fecha = fechaTemp;
		int pos1=fecha.indexOf(separador);
		int pos2=fecha.indexOf(separador,pos1+1);
		int pos3=fecha.indexOf(separador,pos2+1); // No deberia existir.
		if ((pos1>pos2) || (pos3>0)){throw new ClsExceptions("");}
		day=fecha.substring(0,pos1);
		month=fecha.substring(pos1+1,pos2);
		year=fecha.substring(pos2+1);
		if (year.length() == 1) {
			year = "200" + year; } // Si el año es un solo digito se asume 200x
		if (year.length() == 2) {
			year = "20" + year; } // Si el año son solo 2 digitos se asume 20xx
		if ((Integer.parseInt(year) < 1900) || (Integer.parseInt(year) > 2999)) {
			throw new ClsExceptions("");
		} // Codigo de error 1 corresponde a año incorrecto
		if ((day=="") || (month=="") || (year=="")){throw new ClsExceptions("");}
		// Validacion del campo mes
		if ((Integer.parseInt(month) < 1) || (Integer.parseInt(month) > 12)) { 
			throw new ClsExceptions("");} // Codigo de error 2 corresponde a mes incorrecto
		// Validacion del campo dia
		if (((Integer.parseInt(day) < 1) || (Integer.parseInt(day) > 31))){ 
			throw new ClsExceptions(""); } // Codigo de error 3 corresponde a dia incorrecto
		// Validacion 29 febrero
		if ((Integer.parseInt(year) % 4 == 0) || (Integer.parseInt(year) % 100 == 0) || (Integer.parseInt(year) % 400 == 0)) { bisiesto = 1; }
		if (((Integer.parseInt(month) == 2) && (bisiesto == 1) && (Integer.parseInt(day) > 29))) {throw new ClsExceptions(""); } // Codigo de error 4 corresponde a fecha no valida
		if (((Integer.parseInt(month) == 2) && (bisiesto != 1) && (Integer.parseInt(day) > 28))) { 
			throw new ClsExceptions(""); }
		// Validacion del resto de meses
		if (((Integer.parseInt(day) > 31) && ((month == "01") || (month == "03") || (month == "05") || (month == "07") || (month == "08") || (month == "10") || (month == "12")))) {
			throw new ClsExceptions(""); }
		if (((Integer.parseInt(day) > 30) && ((month == "04") || (month == "06") || (month == "09") || (month == "11")))) {
			throw new ClsExceptions(""); }
		fechaADevolver = day+separador+month+separador+year;
		return fechaADevolver;
		
		
	} 
	protected void getAjaxTurnos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		
		
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		String fechaGuardia = request.getParameter("fechaGuardia");
		try {
			fechaGuardia = getFechaGuardia(fechaGuardia);	
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Fecha mal formateada.getAjaxTurnos.fechaGuardia:"+fechaGuardia+"/", 10);
			return;
		}
		
		miForm.setFechaGuardia(fechaGuardia);
		
		
		
		
		
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxTurnos.fechaGuardia:"+fechaGuardia+"/", 10);
		
		//Sacamos los turnos
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(miForm.getUsrBean());
		List<ScsTurnoBean> alTurnos = admTurnos.getTurnos(miForm.getVolanteExpressVo());
		ClsLogging.writeFileLog("VOLANTES EXPRESS:Select Turnos", 10);
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
		}else{
			for(ScsTurnoBean turno:alTurnos){
				ClsLogging.writeFileLog("VOLANTES EXPRESS:turno:"+turno.getNombre(), 10);
			}
		}
		ClsLogging.writeFileLog("VOLANTES EXPRESS:Fin Select Turnos", 10);
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
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxGuardias.fechaguardia:"+fechaGuardia+"/", 10);
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxGuardias.idTurno:"+idTurno+"/", 10);
		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsGuardiasTurnoBean> alGuardias = null;
		ClsLogging.writeFileLog("VOLANTES EXPRESS:Select Guardias", 10);
		if(miForm.getIdTurno()!= null && !miForm.getIdTurno().equals("-1")&& !miForm.getIdTurno().equals("")){
			ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(miForm.getUsrBean());
			alGuardias = admGuardias.getGuardiasTurnos(miForm.getVolanteExpressVo());
		}
		if(alGuardias==null){
			alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
			
		}else{
			for(ScsGuardiasTurnoBean guardia:alGuardias){
				ClsLogging.writeFileLog("VOLANTES EXPRESS:guardia:"+guardia.getNombre(), 10);
			}
			
		}
		ClsLogging.writeFileLog("VOLANTES EXPRESS:Select Guardias", 10);
		
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
		
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiados.fechaguardia:"+fechaGuardia+"/", 10);
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiados.idTurno:"+idTurno+"/", 10);
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiados.idGuardia:"+idGuardia+"/", 10);
		
		
		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<CenPersonaBean> alColegiadosGuardias = null;
		List<CenPersonaBean> alColegiadosSustituidos = null;
		
		if(miForm.getIdGuardia()!= null && !miForm.getIdGuardia().equals("")&& !miForm.getIdGuardia().equals("-1")&&miForm.getFechaGuardia()!= null && !miForm.getFechaGuardia().equals("")){
			ScsGuardiasColegiadoAdm admGuardiasCol = new ScsGuardiasColegiadoAdm(miForm.getUsrBean());
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Select alColegiadosGuardias", 10);
			alColegiadosGuardias = admGuardiasCol.getColegiadosGuardia(miForm.getVolanteExpressVo(),true);
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Select fin alColegiadosGuardias", 10);
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Select alColegiadosSustituidos", 10);
			alColegiadosSustituidos = admGuardiasCol.getColegiadosGuardia(miForm.getVolanteExpressVo(),false);
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Select fin alColegiadosSustituidos", 10);
		}
		
		if(alColegiadosGuardias==null){
			alColegiadosGuardias = new ArrayList<CenPersonaBean>();
		}else{
			for(CenPersonaBean persona:alColegiadosGuardias){
				ClsLogging.writeFileLog("VOLANTES EXPRESS:letrado guardia:"+persona.getNombre(), 10);
			}
			
		}
		if(alColegiadosSustituidos==null){
			alColegiadosSustituidos = new ArrayList<CenPersonaBean>();
		}else{
			for(CenPersonaBean persona:alColegiadosSustituidos){
				ClsLogging.writeFileLog("VOLANTES EXPRESS:letrado sustituido:"+persona.getNombre(), 10);
			}
			
		}
		miForm.setColegiadosGuardia(alColegiadosGuardias);
		miForm.setColegiadosSustituidos(alColegiadosSustituidos);
		List listaParametros = new ArrayList();
		listaParametros.add(alColegiadosGuardias);
		listaParametros.add(alColegiadosSustituidos);
		
		respuestaAjax(new AjaxMultipleCollectionXmlBuilder<CenPersonaBean>(), listaParametros,response);
		
		
		
	}
	@SuppressWarnings("unchecked")
	protected void getAjaxSustituidos (ActionMapping mapping, 		
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
		
		
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxSustituidos.fechaguardia:"+fechaGuardia+"/", 10);
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxSustituidos.idTurno:"+idTurno+"/", 10);
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxSustituidos.idGuardia:"+idGuardia+"/", 10);
		

		
		//Sacamos las guardias si hay algo selccionado en el turno
		
		List<CenPersonaBean> alColegiadosSustituidos = null;
		if(miForm.getIdGuardia()!= null && !miForm.getIdGuardia().equals("")&& !miForm.getIdGuardia().equals("-1")){
			ScsGuardiasColegiadoAdm admGuardiasCol = new ScsGuardiasColegiadoAdm(miForm.getUsrBean());
		
			alColegiadosSustituidos = admGuardiasCol.getColegiadosGuardia(miForm.getVolanteExpressVo(),false);
		}
		
		if(alColegiadosSustituidos==null){
			alColegiadosSustituidos = new ArrayList<CenPersonaBean>();
		}
		
		miForm.setColegiadosSustituidos(alColegiadosSustituidos);
		respuestaAjax(new AjaxCollectionXmlBuilder<CenPersonaBean>(), alColegiadosSustituidos,response);

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
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiado.numeroColegiado:"+numeroColegiado+"/", 10);
				
		
		
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
		
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiado.nombreColegiado:"+nombreColegiado+"/", 10);
		
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
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiadoGuardia.idColegiadoGuardia:"+idColegiadoGuardia+"/", 10);

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
		
		ClsLogging.writeFileLog("VOLANTES EXPRESS:getAjaxColegiadoGuardia.nombreColegiado:"+nombreColegiado+"/", 10);
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
			
			if(miForm.getLugar().equals("centro")){
				List<ScsComisariaBean> alComisarias = null;
				ScsComisariaAdm admComisarias = new ScsComisariaAdm(miForm.getUsrBean());
				alComisarias = admComisarias.getComisarias(miForm.getVolanteExpressVo());
				if(alComisarias==null){
					alComisarias = new ArrayList<ScsComisariaBean>();
				
				}
				miForm.setComisarias(alComisarias);
			}else{
				List<ScsJuzgadoBean> alJuzgados = null;
				ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(miForm.getUsrBean());
				alJuzgados = admJuzgados.getJuzgados(miForm.getVolanteExpressVo());
				if(alJuzgados==null){
					alJuzgados = new ArrayList<ScsJuzgadoBean>();
				
				}
				miForm.setJuzgados(alJuzgados);
				
			}
			

			
			
			
			
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
	protected String getAjaxPrimeraAsistencia (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		VolantesExpressForm miForm = (VolantesExpressForm) formulario;
		//Recogemos el parametro enviado por ajax
		
		String lugar = request.getParameter("lugar");
		miForm.setLugar(lugar);
		
		miForm.setMsgAviso("");
		miForm.setMsgError("");
		try {
			
			if(miForm.getLugar().equals("centro")){
				List<ScsComisariaBean> alComisarias = null;
				ScsComisariaAdm admComisarias = new ScsComisariaAdm(miForm.getUsrBean());
				alComisarias = admComisarias.getComisarias(miForm.getVolanteExpressVo());
				if(alComisarias==null){
					alComisarias = new ArrayList<ScsComisariaBean>();
				
				}
				miForm.setComisarias(alComisarias);
			}else{
				List<ScsJuzgadoBean> alJuzgados = null;
				ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(miForm.getUsrBean());
				alJuzgados = admJuzgados.getJuzgados(miForm.getVolanteExpressVo());
				if(alJuzgados==null){
					alJuzgados = new ArrayList<ScsJuzgadoBean>();
				
				}
				miForm.setJuzgados(alJuzgados);
				
			}
			
			List<ScsAsistenciasBean> alAsistencias = new ArrayList<ScsAsistenciasBean>();
			
			ScsAsistenciasBean asistenciaBean = new ScsAsistenciasBean();
    		asistenciaBean.setHora("");
    		asistenciaBean.setMinuto("");
    		asistenciaBean.setAsistidoNif("");
    		asistenciaBean.setAsistidoNombre("");
    		asistenciaBean.setAsistidoApellido1("");
    		asistenciaBean.setAsistidoApellido2("");
			asistenciaBean.setDelitosImputados("");
			asistenciaBean.setIdDelito(new Integer(-1));
			asistenciaBean.setObservaciones("");
			asistenciaBean.setComisaria(new Long(-1));
			asistenciaBean.setJuzgado(new Long(-1));
			asistenciaBean.setNumeroDiligencia("");
			asistenciaBean.setNumero(new Integer(-1));
			asistenciaBean.setAnio(new Integer(-1));
			asistenciaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			asistenciaBean.setEjgAnio(new Integer(-1));
			asistenciaBean.setEjgIdTipoEjg(new Integer(-1));
			asistenciaBean.setEjgNumEjg("");
			asistenciaBean.setEjgNumero(new Long(-1));
			asistenciaBean.setDesignaNumero(new Integer(-1));
			
			alAsistencias.add(asistenciaBean);
			miForm.setAsistencias(alAsistencias);
			
		} catch (ClsExceptions e) {
			miForm.setAsistencias(new ArrayList<ScsAsistenciasBean> ());
			String error = UtilidadesString.getMensajeIdioma(miForm.getUsrBean(),e.getMsg());
			miForm.setMsgError(error);
		}catch (Exception e){
			miForm.setAsistencias(new ArrayList<ScsAsistenciasBean> ());
			String error = UtilidadesString.getMensajeIdioma(miForm.getUsrBean(),"messages.general.errorExcepcion");
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
		//miForm.setFechaJustificacion(GstDate.getApplicationFormatDate("", fechaJustificacion));
		miForm.setFechaJustificacion(fechaJustificacion);
		String datosAsistencias = (String)request.getParameter("datosAsistencias");
		miForm.setDatosAsistencias(datosAsistencias);
		miForm.setMsgAviso("");
		miForm.setMsgError("");
		
	
		VolantesExpressVo volantesExpressVo = miForm.getVolanteExpressVo();
		ArrayList<ScsAsistenciasBean> alAsistenciaOld =  (ArrayList<ScsAsistenciasBean>)volantesExpressVo.getAsistencias(); 
		try {
		ClsLogging.writeFileLog("getAjaxGuardarAsistencias:Antes de BusinessManager:", 10);
			BusinessManager bm = getBusinessManager();
			ClsLogging.writeFileLog("getAjaxGuardarAsistencias:Antes de obtener servicio:", 10);
			VolantesExpressService ves = (VolantesExpressService)bm.getService(VolantesExpressService.class);
			ClsLogging.writeFileLog("getAjaxGuardarAsistencias:Antes de ejecutar servicio:", 10);
			ArrayList<ScsAsistenciasBean> alAsistencias = (ArrayList<ScsAsistenciasBean>) ves.executeService(volantesExpressVo);
			miForm.setAsistencias(alAsistencias);
			if(volantesExpressVo.getMsgAviso()!=null && !volantesExpressVo.getMsgAviso().equals("")){
				miForm.setMsgAviso(volantesExpressVo.getMsgAviso());
			}else{
				miForm.setMsgAviso(UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"messages.updated.success"));
			}
		} catch (SIGAException e) {
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),e.getLiteral());
			miForm.setMsgError(error);
		}catch (ClsExceptions e) {
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),e.getMsg());
			miForm.setMsgError(error);
		}catch (Exception e){
			ClsLogging.writeFileLog("ERRRR VOLANTES EXPRESS:getAjaxGuardarAsistencias:"+e.toString()+"/", 10);
			miForm.setAsistencias(alAsistenciaOld);
			String error = UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"messages.general.errorExcepcion");
			miForm.setMsgError(error);			
		}
		return "listadoAsistencias";
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
		miForm.setColegiadosSustituidos(new ArrayList<CenPersonaBean>());
		
		miForm.setIdTurno(null);
		miForm.setIdGuardia(null);
		miForm.setIdColegiado(null);
		miForm.setIdColegiadoGuardia(null);
		miForm.setIdColegiadoSustituido(null);
		
		return "inicio";
	}
	
}

