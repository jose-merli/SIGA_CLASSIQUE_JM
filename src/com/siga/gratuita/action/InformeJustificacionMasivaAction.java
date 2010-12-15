package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AcreditacionForm;
import com.siga.gratuita.form.ActuacionDesignaForm;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;
import com.siga.informes.MasterWords;


public class InformeJustificacionMasivaAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			do {
				miForm = (MasterForm) formulario;
				
				if (miForm != null) {
					String accion = miForm.getModo();
					
					if (accion == null || accion.equalsIgnoreCase("")||accion.equalsIgnoreCase("abrir")) {
						if(mapping.getPath().equals("/JGR_InformeJustificacion")){
							mapDestino = inicioInforme(mapping, miForm, request, response);
							break;
						}else {
							mapDestino = abrir(mapping, miForm, request, response);
							break;
						}
					} else if (accion.equalsIgnoreCase("buscarInit")){
						borrarPaginador(request, paginadorPenstania);
						mapDestino = buscarPor(mapping, miForm, request, response);
						break;
					}else if (accion.equalsIgnoreCase("buscarPor")){
						mapDestino = buscarPor(mapping, miForm, request, response);	
						break;
					}
					else if (accion.equalsIgnoreCase("justificar")) {
						mapDestino = justificar(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("informe")) {
						mapDestino = generaInforme(mapping, miForm, request, response);
						break;
					}else {
						return super.executeInternal(mapping, formulario,
								request, response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
	}
	protected String inicioInforme (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		InformeJustificacionMasivaForm form = (InformeJustificacionMasivaForm) formulario;
		form.clear();
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		GenParametrosAdm paramAdm = new GenParametrosAdm (user);
		//Haria falta meter los parametros en con ClsConstants
		String cod_Fact_ja_2005 = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_FACT_JA_2005, "");
		boolean	aplicarAcreditacionesAnterior2005 = (cod_Fact_ja_2005!=null && cod_Fact_ja_2005.equalsIgnoreCase(ClsConstants.DB_TRUE));
		form.setAplicarAcreditacionesAnterior2005(aplicarAcreditacionesAnterior2005);
		String codPermitirSinResolucionJustfLetrado = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_PERMITIR_SINRESOLUCION_JUSTIF_LETRADO, "");
		form.setPermitirSinResolucionJustifLetrado(codPermitirSinResolucionJustfLetrado!=null && codPermitirSinResolucionJustfLetrado.equalsIgnoreCase(ClsConstants.DB_TRUE));
		
		
		
		return "inicioInforme";
		
	}
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		String modo = (String) request.getSession().getAttribute("modo");
		InformeJustificacionMasivaForm form = (InformeJustificacionMasivaForm) formulario;
		form.clear();
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		
		if(request.getParameter("idPersonaPestanha")!=null){
			Long idPersona = new Long(request.getParameter("idPersonaPestanha"));
			Integer idInstitucion = new Integer(request.getParameter("idInstitucionPestanha"));
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			
			// Obtengo la informacion del colegiado:
			String nombreColegiado = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona,idInstitucion);
			String numColegiado = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			form.setNombreColegiado(nombreColegiado);
			if(numColegiado!=null){
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				String estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucion));
				form.setNumColegiado(numColegiado);
				form.setEstadoColegial(estadoColegial);
			}else{
				form.setNumColegiado(null);
				form.setEstadoColegial(null);
				
			}
			form.setIdInstitucion(""+idInstitucion);
			form.setIdPersona(""+idPersona);
			form.setFichaColegial(true);
			form.setAnio(GstDate.getYear(new Date()));
			
		}
		
		
		
		
		
		return "inicio";
	}
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		return this.abrir(mapping, formulario, request, response);
	}

	
	protected synchronized String justificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions, SIGAException {
//		UserTransaction tx = null;
		StringBuffer msgSinAcreditaciones = new StringBuffer();
		StringBuffer msgAviso = new StringBuffer();
		UsrBean user = (UsrBean) request.getSession().getAttribute(
				"USRBEAN");
		String obsJustificacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.justificacion");
		String obsActuacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.actuacion");
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		final String idAcreditacionPenal = rp3.returnProperty("codigo.general.scsacreditacion.jurisdiccion.penal");
		try {
			InformeJustificacionMasivaForm miForm = (InformeJustificacionMasivaForm) formulario;

			String fechaJustificacion = miForm.getFecha();
			ScsDesignaAdm desginaAdm = new ScsDesignaAdm(this
					.getUserBean(request));
			ScsActuacionDesignaAdm actuacionDesginaAdm = new ScsActuacionDesignaAdm(
					this.getUserBean(request));
			
			String clavesDesigna[] = { ScsDesignaBean.C_ANIO, ScsDesignaBean.C_NUMERO,
					ScsDesignaBean.C_IDINSTITUCION, ScsDesignaBean.C_IDTURNO };
			String camposDesigna[]={ScsDesignaBean.C_FECHAESTADO,ScsDesignaBean.C_ESTADO};

			String clavesActuacion[] = { ScsActuacionDesignaBean.C_ANIO, ScsActuacionDesignaBean.C_NUMERO,
					ScsActuacionDesignaBean.C_IDINSTITUCION, ScsActuacionDesignaBean.C_IDTURNO ,ScsActuacionDesignaBean.C_NUMEROASUNTO};
			
			
			
			String idPersona   =  miForm.getIdPersona();
			String datosJustificacion = miForm.getDatosJustificacion();
			
			if(datosJustificacion.length()>0){
				String[] arrayDatosJustificacion = datosJustificacion.split("#");
				
				for (int i = 0; i < arrayDatosJustificacion.length; i++) {
					String rowJustificacion = arrayDatosJustificacion[i];
					String[] arrayRowsJustificacion = rowJustificacion.split(",");
					String anio  =  arrayRowsJustificacion[0];
					String numero  =  arrayRowsJustificacion[1]; 
					String idInstitucion  =  arrayRowsJustificacion[2];
					String idTurno  =  arrayRowsJustificacion[3];
					String idJuzgado  =  arrayRowsJustificacion[4];
					String idProcedimiento  =  arrayRowsJustificacion[5];
					String numActuacion  =  arrayRowsJustificacion[6];
					String idAcreditacion  =  arrayRowsJustificacion[7];
					String justificado   =  arrayRowsJustificacion[8];
					String idJurisdiccion   =  arrayRowsJustificacion[9];
					String fechaDesigna   =  arrayRowsJustificacion[10];
					String validado   =  arrayRowsJustificacion[11];
					
					
					
					//si la actuacion es x es que es nueva, sino es modificacion(justificacion, validacion o baja)
					Hashtable hashActuacion = new Hashtable();
					if(numActuacion.equals("x")){
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_ANIO, anio);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCION,
								idInstitucion);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_NUMERO, numero);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDTURNO, idTurno);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO,
								idPersona);
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDJUZGADO, idJuzgado);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO,
								idInstitucion);
	//					UtilidadesHash.set(hashActuacion,
	//							ScsActuacionDesignaBean.C_FECHAMODIFICACION,
	//					"sysdate");
	//					UtilidadesHash.set(hashActuacion,
	//							ScsActuacionDesignaBean.C_USUMODIFICACION,
	//							new Long(this.getUserBean(request).getIdPersona()));
	
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,
								idProcedimiento);
						UtilidadesHash.set(
								hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO,
								idInstitucion);
	
						UtilidadesHash.set(
								hashActuacion,
								ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL,
								"0");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_ANULACION, "0");
						 
						if(miForm.isAplicarAcreditacionesAnterior2005()&&idJurisdiccion!=null 
								&& idJurisdiccion.equalsIgnoreCase(idAcreditacionPenal)){
							idAcreditacion = getIdAcreditacion(idAcreditacion,fechaJustificacion!=null?fechaJustificacion:fechaDesigna , user);
							
						}
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDACREDITACION,
								idAcreditacion);
	
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHA,
								GstDate.getApplicationFormatDate("", fechaJustificacion));
					
						UtilidadesHash.set(hashActuacion,
							ScsActuacionDesignaBean.C_OBSERVACIONES,
							obsActuacion);
						
						
						//Es posible que este justificada y no validada
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
								GstDate.getApplicationFormatDate("", fechaJustificacion));
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
								obsJustificacion);
						
						if(Boolean.parseBoolean(validado)){
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_VALIDADA, "1");
						}else{
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_VALIDADA, "0");
							
						}
						//actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
						hashActuacion = actuacionDesginaAdm.prepararInsert(hashActuacion);
						actuacionDesginaAdm.insert(hashActuacion);
						
						
	
											
					}else{
						List<String> camposList = new ArrayList<String>();
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_ANIO, anio);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCION,
								idInstitucion);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_NUMERO, numero);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDTURNO, idTurno);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO,
								idPersona);
						
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_NUMEROASUNTO,
								numActuacion);
						
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHAMODIFICACION,
						"sysdate");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_USUMODIFICACION,user.getUserName());
						
						camposList.add(ScsActuacionDesignaBean.C_FECHAMODIFICACION);
						camposList.add(ScsActuacionDesignaBean.C_USUMODIFICACION);
						
						//si ya estaba justificado no seteamos la frecha de justificacion
						if(justificado.equalsIgnoreCase(ClsConstants.DB_FALSE)){
							camposList.add(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
							camposList.add(ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION);
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
									GstDate.getApplicationFormatDate("", fechaJustificacion));
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
									obsJustificacion);
							if(miForm.isAplicarAcreditacionesAnterior2005()&&idJurisdiccion!=null 
									&& idJurisdiccion.equalsIgnoreCase(idAcreditacionPenal)){
								camposList.add(ScsActuacionDesignaBean.C_IDACREDITACION);
								idAcreditacion = getIdAcreditacion(idAcreditacion,fechaJustificacion, user);
								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_IDACREDITACION,
										idAcreditacion);
								
								
							}
						}
						camposList.add(ScsActuacionDesignaBean.C_VALIDADA);
						if(Boolean.parseBoolean(validado)){
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_VALIDADA, "1");
						}else{
							UtilidadesHash.set(hashActuacion,
									ScsActuacionDesignaBean.C_VALIDADA, "0");
							
						}
						
						
						
						actuacionDesginaAdm.updateDirect(hashActuacion,clavesActuacion,camposList.toArray(new String[camposList.size()]));
						
						
						
					}
				}
			}
			String datosBaja = miForm.getDatosBaja();
			if(datosBaja.length()>0){
				String[] arrayDatosBaja = datosBaja.split("#");
				Hashtable<String, String> htDesigna = null;
				
				for (int i = 0; i < arrayDatosBaja.length; i++) {
					String rowBaja = arrayDatosBaja[i];
					String[] arrayRowsJustificacion = rowBaja.split(",");
					htDesigna = new Hashtable<String, String>();
					String anio  =  arrayRowsJustificacion[0];
					String numero  =  arrayRowsJustificacion[1]; 
					String idInstitucion  =  arrayRowsJustificacion[2];
					String idTurno  =  arrayRowsJustificacion[3];
					htDesigna.put(ScsDesignaBean.C_IDINSTITUCION,idInstitucion);
					htDesigna.put(ScsDesignaBean.C_ANIO,anio);
					htDesigna.put(ScsDesignaBean.C_IDTURNO,idTurno);
					htDesigna.put(ScsDesignaBean.C_NUMERO,numero);
					htDesigna.put(ScsDesignaBean.C_FECHAESTADO,"sysdate");
					htDesigna.put(ScsDesignaBean.C_ESTADO, "F");
					desginaAdm.updateDirect(htDesigna, clavesDesigna, camposDesigna);
					
				}
			}

//			tx.commit();
		} catch (Exception e) {
			//throwExcp("messages.general.error",	new String[] { "modulo.gratuita" }, e, tx);
		}
		StringBuffer txtADevolver = new StringBuffer("");
		if(msgAviso.toString().equalsIgnoreCase("") && msgSinAcreditaciones.toString().equalsIgnoreCase("")){
			txtADevolver.append("messages.updated.success");
		}else{
			if(!msgAviso.toString().equalsIgnoreCase("")){
				txtADevolver.append(UtilidadesString.getMensajeIdioma(user.getLanguageInstitucion(), "gratuita.informeJustificacionMasiva.message.success.conexcepciones"));
				txtADevolver.append("\n\r");
				txtADevolver.append(msgAviso.toString().substring(1));
			}
			if(!msgSinAcreditaciones.toString().equalsIgnoreCase("")){
				if(!msgAviso.toString().equalsIgnoreCase(""))
					txtADevolver.append("\n\r");
				txtADevolver.append(UtilidadesString.getMensajeIdioma(user.getLanguageInstitucion(), "gratuita.informeJustificacionMasiva.message.success.sinAcreditaciones"));
				txtADevolver.append("\n\r");
				txtADevolver.append(msgSinAcreditaciones.toString().substring(1));
			}

		}
		return exitoRefresco(txtADevolver.toString(), request);
	}
	
	protected String getIdAcreditacion(String idAcreditacion,String fecha,UsrBean usrBean) throws Exception {
		//		Si la fecha es anterior a 01/01/2005 cogera un tipo de
		// acreditacion(2 si es inuicio y 3 si es final)
		//Si es posterior a esa fecha cogera otros tipos de
		// acreditacion(6 si es inuicio y 7 si es final)
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp3 = new ReadProperties("SIGA.properties");
		//Miramos es tipo de jurisdiccion(constante ==1). 
		//Si es de penal va a depender de la fecha de acreditacion 
	    Date dateFecha = null;
		if (fecha != null && !fecha.equalsIgnoreCase(""))
			dateFecha = GstDate.convertirFecha(fecha,"dd/MM/yyyy");
		
		//obtenemos el parametro si es 1 hay que aplicara la logica de las acreditaciones anteriores a 2005
        //si es 0 no es necesario
		if (Integer.parseInt(GstDate.getYear(dateFecha)) < 2005) {
			String idAcreditacionInicioAntes2005 =  rp3.returnProperty("codigo.general.scsacreditacion.inicio.antes2005");
			String idAcreditacionFinAntes2005 =  rp3.returnProperty("codigo.general.scsacreditacion.fin.antes2005");
			String idAcreditacionInicioDespues2005 =  rp3.returnProperty("codigo.general.scsacreditacion.inicio.despues2005");
			String idAcreditacionFinDespues2005 =  rp3.returnProperty("codigo.general.scsacreditacion.fin.despues2005");
			if(idAcreditacion.equalsIgnoreCase(idAcreditacionInicioDespues2005)){
				idAcreditacion = idAcreditacionInicioAntes2005;
				
			}else if(idAcreditacion.equalsIgnoreCase(idAcreditacionFinDespues2005)){
				idAcreditacion = idAcreditacionFinAntes2005;
			} 
		} 
		return idAcreditacion;
	}
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(usrBean);
		f.setIdInstitucion(usrBean.getLocation());
		
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorPenstania);
		boolean permitirBotones = usrBean.getAccessType()!=null && usrBean.getAccessType().equals(SIGAConstants.ACCESS_FULL); 
		request.setAttribute("permitirBotones", permitirBotones);
		
		try {
			HashMap databackup=getPaginador(request, paginadorPenstania);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				List<DesignaForm> designaFormList = new ArrayList<DesignaForm>();
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					 
					//Ahora nos salen designas. Las obtenemos y obtenemos sus listado
					for (int i = 0; i < datos.size(); i++) {
						Row designaRow = (Row)datos.get(i);
						Hashtable designaHashtable = (Hashtable) designaRow.getRow();
						List<DesignaForm> designaList = admDesignas.getDesignaList(f, designaHashtable,null, false);
						designaFormList.addAll(designaList);
					}
					request.setAttribute("designaFormList", designaFormList);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",designaFormList);
				}else{
					databackup.put("datos",new ArrayList<DesignaForm>());
					request.setAttribute("designaFormList", new ArrayList<DesignaForm>());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorPenstania, databackup);
					
				}	
				

			}else{	
				databackup=new HashMap();
				String keyPersona =f.getIdPersona();
				GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
				//Haria falta meter los parametros en con ClsConstants
				String cod_Fact_ja_2005 = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_FACT_JA_2005, "");
				boolean	aplicarAcreditacionesAnterior2005 = (cod_Fact_ja_2005!=null && cod_Fact_ja_2005.equalsIgnoreCase(ClsConstants.DB_TRUE));
				f.setAplicarAcreditacionesAnterior2005(aplicarAcreditacionesAnterior2005);
				String codPermitirSinResolucionJustfLetrado = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_PERMITIR_SINRESOLUCION_JUSTIF_LETRADO, "");
				f.setPermitirSinResolucionJustifLetrado(codPermitirSinResolucionJustfLetrado!=null && codPermitirSinResolucionJustfLetrado.equalsIgnoreCase(ClsConstants.DB_TRUE));
				
				String mensajeResponsabilidadJustificacionLetrado = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_MENSAJE_RESPONSABILIDAD_LETRADO, "");
				f.setMensajeResponsabilidadJustificacionLetrado(mensajeResponsabilidadJustificacionLetrado);
				
				PaginadorBind paginador = admDesignas.getDesignasJustificacionPaginador(f,false);
				
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					
					List<DesignaForm> designaFormList = new ArrayList<DesignaForm>();
					//Ahora nos salen designas. Las obtenemos y obtenemos sus listado
					for (int i = 0; i < datos.size(); i++) {
						Row designaRow = (Row)datos.get(i);
						Hashtable designaHashtable = (Hashtable) designaRow.getRow();
						List<DesignaForm> designaList = admDesignas.getDesignaList(f, designaHashtable,null, false);
						designaFormList.addAll(designaList);
					}
					request.setAttribute("designaFormList", designaFormList);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("datos",designaFormList);
					setPaginador(request, paginadorPenstania, databackup);
				}else{
					databackup.put("datos",new ArrayList<DesignaForm>());
					request.setAttribute("designaFormList", new ArrayList<DesignaForm>());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorPenstania, databackup);
					
				} 	


			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		request.setAttribute("letrado", f.getIdPersona());

		if (f.getMostrarTodas() != null && !f.getMostrarTodas().equals("")) {
			request.setAttribute("mostrarTodas", f.getMostrarTodas());
		}
		if (f.getFechaDesde() != null && !f.getFechaDesde().equals("")) {
			request.setAttribute("fechaDesde", f.getFechaDesde());
		}
		if (f.getFechaHasta() != null && !f.getFechaHasta().equals("")) {
			request.setAttribute("fechaHasta", f.getFechaHasta());
		}
		//metemos al formulario la fecha de hoy por defecto
		GstDate gstDate = new GstDate();
		
		if(f.getFecha()==null || f.getFecha().equalsIgnoreCase("")){
			String fecha = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request)); 
			f.setFecha(fecha);
		}
		return "listadoPaginado";
	}
	
	
	
	protected String generaInforme(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions, SIGAException {
		Date inicio = new Date();
		ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + "==> SIGA: INICIO InformesJustificacion.GeneraInforme",10);
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		UsrBean usr = this.getUserBean(request);
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(usr);
		Integer idInstitucion = this.getIDInstitucion(request);
		f.setIdInstitucion(""+idInstitucion);
		//Ijniciamos esta variable para ir metiendo los juzgados. Se hace para evitar el acceso masivo a BBDD
		
		
		try {
			
			
			String idioma = f.getIdioma();
			GstDate gstDate = new GstDate();
			String hoy = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request));
			if(idioma ==null ||idioma.equalsIgnoreCase("")){
				hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,"dma",usr.getLanguage());
				idioma = usr.getLanguageExt();
			
			}else{
				hoy = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(hoy,"dma",idioma);
				AdmLenguajesAdm admLenguajes = new AdmLenguajesAdm(usr);
				idioma = admLenguajes.getLenguajeExt(idioma);
			
			}
			
			File ficheroSalida = null;
			Vector informesRes = new Vector(); 
			// El id del informe es:
			Vector plantillas = this.obtenerPlantillasFormulario("JUS1",idInstitucion.toString(), usr);
			// Obtiene el Array de las plantillas. En este caso solo es una por lo que sacamos el 
			// elemneto 0 del vector
			AdmInformeBean b = (AdmInformeBean) plantillas.get(0);
			
			// --- acceso a paths y nombres 
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			////////////////////////////////////////////////
			// MODELO DE TIPO WORD: LLAMADA A ASPOSE.WORDS
			String rutaPl = rutaPlantilla + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio()+ClsConstants.FILE_SEP;
			String nombrePlantilla=b.getNombreFisico()+"_"+idioma+".doc";
			String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+b.getDirectorio();
			
			
			File crear = new File(rutaAlm);
			if(!crear.exists())
				crear.mkdirs();
			
			MasterWords words=new MasterWords(rutaPl+nombrePlantilla);
			Hashtable htCabeceraInforme = null;
			Hashtable htPersonas = admDesignas.getPersonasSalidaInformeJustificacion(f,true);
			
			if(htPersonas==null ||htPersonas.size()<1){
				throw new SIGAException("messages.informes.ficheroVacio");
				
			}else{
					//Hashtable htPersonas = getHashPersonaInforme(vRowsInforme,usr,idInstitucion);
					Iterator itePersonas = htPersonas.keySet().iterator();
					int j = 0;
					while (itePersonas.hasNext()) {
						String keyPersona = (String) itePersonas.next();
						TreeMap tmRowsInformePorPersona = (TreeMap) htPersonas.get(keyPersona);
						Vector vRowsInformePorPersona = new Vector();
						Iterator itRowsDesigna = tmRowsInformePorPersona.keySet().iterator();
						 
						while (itRowsDesigna.hasNext()) {
							String keyRowDesigna = (String) itRowsDesigna.next();
							Hashtable htRowDesigna = (Hashtable) tmRowsInformePorPersona.get(keyRowDesigna);
							//esto es de lo viejo lo meto
							htRowDesigna.put("PENDIENTE"," ");
							htRowDesigna.put("ACREDITACION_INI"," ");
							htRowDesigna.put("ACREDITACION_FIN"," ");
							String estado = (String)htRowDesigna.get(ScsDesignaBean.C_ESTADO);
							if (estado != null && estado.equals("F"))
								htRowDesigna.put("BAJA","X");
							else
								htRowDesigna.put("BAJA"," ");
							
							
							
							
							List<DesignaForm> designaList = (List<DesignaForm>) htRowDesigna.get("designaList");
							for(DesignaForm designaForm:designaList){
								
								boolean isPrimero = true;
//								IF DESIGNA.JUZGADO NULL && DESIGNA.ACTUACIONES EMPTY
//								RETURN SIN JUZGADO;
//							IF	DESIGNA.IDPROCEDIMIENTO NULL && DESIGNA.ACTUACIONES EMPTY
//								RETURN SIN PROCEDIMIENTO:
//							IF DESIGNA.ACTUACIONES EMPTY
//									CATEGORIA = DESIGNA.CATEGORIA;
//									IF DESIGNA.ACREDITACIONES EMPTY
//										RETURN MODULO SIN ACREDITACIONES;
//									ELSE 
//										ACREDITACION = DESIGNA.ACREDITACIONES(i).ACREDITACION;
//
//							ELSE
//								CATEGORIA = DESIGNA.ACTUACIONES(i).CATEGORIA;
//								ACTUACION = DESIGNA.ACTUACIONES(i);
//								ACREDITACION = ACTUACION.DESCRIPCION;
//								IF DESIGNA.ACREDITACIONES[KEYACTUACION]
//									ACREDITACION = DESIGNA.ACREDITACIONES[KEYACTUACION](i).ACREDITACION;
								
								if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0){
									Map<String, List<ActuacionDesignaForm>> actuacionesMap = designaForm.getActuaciones();
									Iterator actuacionesIterator = actuacionesMap.keySet().iterator(); 
									while (actuacionesIterator.hasNext()) {
										String idProcedimineto = (String) actuacionesIterator.next();
										List<ActuacionDesignaForm> actuacionesList = actuacionesMap.get(idProcedimineto);
										if(actuacionesList!=null && actuacionesList.size()>0){
											
											for (ActuacionDesignaForm actuacionForm : actuacionesList) {
												String categoria = actuacionForm.getCategoria();
												//String acreditacion = actuacionForm.getDescripcion();
												String acreditacion = actuacionForm.getAcreditacion().getDescripcion();
												String fechaJustificacion ="";
												String validada = "";
												if(actuacionForm.getFechaJustificacion()!=null && !actuacionForm.getFechaJustificacion().equals("")){
													fechaJustificacion =  actuacionForm.getFechaJustificacion();
													if(actuacionForm.getValidada().equals("1")){
														validada = "X";
													}
													
												}												
												String numeroAsunto="";
												if (actuacionForm.getNumero()!=null &&!actuacionForm.getNumero().equals("")){
													numeroAsunto=actuacionForm.getNumero();
												}
												
												String descripcionFacturacion="";
												if (actuacionForm.getDescripcionFacturacion()!=null && !actuacionForm.getDescripcionFacturacion().equals("")){
													descripcionFacturacion=actuacionForm.getDescripcionFacturacion();
												}
												
												Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
												if(isPrimero){
													isPrimero = false;
												}else{
													htRowDesignaClone.put("CODIGODESIGNA", "");
													htRowDesignaClone.put("EXPEDIENTES", "");
													htRowDesignaClone.put("IDJUZGADO", "");
													htRowDesignaClone.put("FECHADESIGNA", "");
													htRowDesignaClone.put("ASUNTO", "");
													htRowDesignaClone.put("CLIENTE", "");
													
												}
												htRowDesignaClone.put("CATEGORIA", categoria);
												htRowDesignaClone.put("ACREDITACION", acreditacion);
												htRowDesignaClone.put("FECHAJUSTIFICACION", fechaJustificacion);
												htRowDesignaClone.put("VALIDADA", validada);
												htRowDesignaClone.put("N_ACTUACION", numeroAsunto);
												htRowDesignaClone.put("DESCRIPCIONFACTURACION", descripcionFacturacion);
												vRowsInformePorPersona.add(htRowDesignaClone);
												
											}
											if(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0){
												Map<String, List<AcreditacionForm>> acreditacionesMap = designaForm.getAcreditaciones();
												List<AcreditacionForm> acreditacionesList = acreditacionesMap.get(idProcedimineto);
												if(acreditacionesList!=null && acreditacionesList.size()>0){
													for (AcreditacionForm acreditacionForm : acreditacionesList) {
														String categoria = "";
														String fechaJustificacion = "";
														String validada = "";
														String numeroAsunto = "";
														String descripcionFacturacion="";
														String acreditacion = acreditacionForm.getDescripcion();
														Hashtable htRowDesignaClone2 = (Hashtable) htRowDesigna.clone();
														if(isPrimero){
															isPrimero = false;
														}else{
															htRowDesignaClone2.put("CODIGODESIGNA", "");
															htRowDesignaClone2.put("EXPEDIENTES", "");
															htRowDesignaClone2.put("IDJUZGADO", "");
															htRowDesignaClone2.put("FECHADESIGNA", "");
															htRowDesignaClone2.put("ASUNTO", "");
															htRowDesignaClone2.put("CLIENTE", "");
															
														}
														htRowDesignaClone2.put("CATEGORIA", categoria);
														htRowDesignaClone2.put("ACREDITACION", acreditacion);
														htRowDesignaClone2.put("FECHAJUSTIFICACION", fechaJustificacion);
														htRowDesignaClone2.put("VALIDADA", validada);
														htRowDesignaClone2.put("N_ACTUACION", numeroAsunto);
														htRowDesignaClone2.put("DESCRIPCIONFACTURACION", descripcionFacturacion);
														vRowsInformePorPersona.add(htRowDesignaClone2);
													}
												}
											}
										}
									}
								}else{
									if(designaForm.getNumEjgResueltosFavorables()==0){
//									if(designaForm.getNumEjgResueltosFavorables()==0 && !f.isPermitirSinResolucionJustifLetrado()){
										String acreditacion = UtilidadesString.getMensajeIdioma(usr,"gratuita.informeJustificacionMasiva.literal.designaSinEjgFavorable");
										Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
										htRowDesignaClone.put("CATEGORIA", "");
										htRowDesignaClone.put("ACREDITACION", acreditacion);
										htRowDesignaClone.put("FECHAJUSTIFICACION", "");
										htRowDesignaClone.put("VALIDADA", "");
										htRowDesignaClone.put("N_ACTUACION", "");
										htRowDesignaClone.put("DESCRIPCIONFACTURACION", "");
										vRowsInformePorPersona.add(htRowDesignaClone);
										
									}else if(designaForm.getIdJuzgado()==null||designaForm.getIdJuzgado().equals("")){
										
										String acreditacion = UtilidadesString.getMensajeIdioma(usr,"gratuita.informeJustificacionMasiva.aviso.sinJuzgado");
										Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
										htRowDesignaClone.put("CATEGORIA", "");
										htRowDesignaClone.put("ACREDITACION", acreditacion);
										htRowDesignaClone.put("FECHAJUSTIFICACION", "");
										htRowDesignaClone.put("VALIDADA", "");
										htRowDesignaClone.put("N_ACTUACION", "");
										htRowDesignaClone.put("DESCRIPCIONFACTURACION", "");
										vRowsInformePorPersona.add(htRowDesignaClone);
										
									}else if(designaForm.getIdProcedimiento()==null||designaForm.getIdProcedimiento().equals("")){
										String acreditacion = UtilidadesString.getMensajeIdioma(usr,"gratuita.informeJustificacionMasiva.aviso.sinModulo");
										Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
										htRowDesignaClone.put("CATEGORIA", "");
										htRowDesignaClone.put("ACREDITACION", acreditacion);
										htRowDesignaClone.put("FECHAJUSTIFICACION", "");
										htRowDesignaClone.put("VALIDADA", "");
										htRowDesignaClone.put("N_ACTUACION", "");
										htRowDesignaClone.put("DESCRIPCIONFACTURACION", "");
										vRowsInformePorPersona.add(htRowDesignaClone);
									}else{
										String categoria = designaForm.getCategoria();
										if(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0){
											Map<String, List<AcreditacionForm>> acreditacionesMap = designaForm.getAcreditaciones();
											Iterator acreditacionesIterator = acreditacionesMap.keySet().iterator(); 
											while (acreditacionesIterator.hasNext()) {
												String idProcedimineto = (String) acreditacionesIterator.next();
												List<AcreditacionForm> acreditacionesList = acreditacionesMap.get(idProcedimineto);
												if(acreditacionesList!=null && acreditacionesList.size()>0){
													for (AcreditacionForm acreditacionForm : acreditacionesList) {
														String acreditacion = acreditacionForm.getDescripcion();
														Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
														if(isPrimero){
															isPrimero = false;
														}else{
															htRowDesignaClone.put("CODIGODESIGNA", "");
															htRowDesignaClone.put("EXPEDIENTES", "");
															htRowDesignaClone.put("IDJUZGADO", "");
															htRowDesignaClone.put("FECHADESIGNA", "");
															htRowDesignaClone.put("ASUNTO", "");
															htRowDesignaClone.put("CLIENTE", "");
															
														}
														htRowDesignaClone.put("CATEGORIA", categoria);
														htRowDesignaClone.put("ACREDITACION", acreditacion);
														htRowDesignaClone.put("FECHAJUSTIFICACION", "");
														htRowDesignaClone.put("VALIDADA", "");
														htRowDesignaClone.put("N_ACTUACION", "");
														htRowDesignaClone.put("DESCRIPCIONFACTURACION", "");
														vRowsInformePorPersona.add(htRowDesignaClone);
														
													}
												}
												
											}
											
										}else{
											
											String acreditacion = UtilidadesString.getMensajeIdioma(usr,"gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones");
											Hashtable htRowDesignaClone = (Hashtable) htRowDesigna.clone();
											htRowDesignaClone.put("CATEGORIA", categoria);
											htRowDesignaClone.put("ACREDITACION", acreditacion);
											htRowDesignaClone.put("FECHAJUSTIFICACION", "");
											htRowDesignaClone.put("VALIDADA", "");
											htRowDesignaClone.put("N_ACTUACION", "");
											htRowDesignaClone.put("DESCRIPCIONFACTURACION", "");
											vRowsInformePorPersona.add(htRowDesignaClone);
										}
									}
									
									
									
								}
								
								
								
								
							}
						}
						//En toda las filas tenemos la descripcion del colegiado asi que cogemos la
						//primera para sacar la cabecera
						Hashtable htRow = (Hashtable)vRowsInformePorPersona.get(0);
						htCabeceraInforme = new Hashtable();
						String nColegiado =  (String)htRow.get(CenColegiadoBean.C_NCOLEGIADO);
						htCabeceraInforme.put("NCOLEGIADO",nColegiado);
						htCabeceraInforme.put("ETIQUETA","Nº Colegiado");
						htCabeceraInforme.put("NOMBRE",(String)htRow.get(CenPersonaBean.C_NOMBRE));
						htCabeceraInforme.put("FECHA",hoy);
			
						String direccion = "";
						String codPostal = "";
						String pobLetrado = "";
						String provLetrado = "";
						
						if (htRow!=null && htRow.size()>0) {
							codPostal = (String)htRow.get("CP_LETRADO");
							pobLetrado = (String)htRow.get("POBLACION_LETRADO");
							direccion = (String)htRow.get("DOMICILIO_LETRADO");
							provLetrado = (String)htRow.get("PROVINCIA_LETRADO");
						}
						htCabeceraInforme.put("DIRECCION",direccion);
						htCabeceraInforme.put("CP",codPostal);
						htCabeceraInforme.put("POBLACION",pobLetrado);
						htCabeceraInforme.put("PROVINCIA",provLetrado);
						htCabeceraInforme.put("CRONOLOGICO",nColegiado);

						Document doc=words.nuevoDocumento();
						doc = words.sustituyeDocumento(doc,htCabeceraInforme);
						doc = words.sustituyeRegionDocumento(doc,"region",vRowsInformePorPersona);

						StringBuffer nombreArchivo = new StringBuffer(); 
						nombreArchivo.append(nColegiado);
						nombreArchivo.append("-");
						
						nombreArchivo.append(b.getNombreSalida());
						nombreArchivo.append("_");
						nombreArchivo.append(idInstitucion);
						nombreArchivo.append("_");
						nombreArchivo.append(j);
						nombreArchivo.append(".doc");
						j++;
						File archivo = words.grabaDocumento(doc,rutaAlm,nombreArchivo.toString());
						informesRes.add(archivo);
					}
				/////////////////////////////////////////////////
				if (informesRes.size()!=0) { 
					if (informesRes.size()<2) {
						ficheroSalida = (File) informesRes.get(0);
					} else {
						AdmTipoInformeAdm admT = new AdmTipoInformeAdm(this.getUserBean(request));
						AdmTipoInformeBean beanT = admT.obtenerTipoInforme("JUSDE");
						ArrayList ficheros= new ArrayList();
						for (int i=0;i<informesRes.size();i++) {
							File file = (File) informesRes.get(i);
							ficheros.add(file);
						}
						String nombreFicheroZIP=beanT.getDescripcion().trim() + "_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
						String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
						rutaServidorDescargasZip += ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+"temp"+ File.separator;
						File ruta = new File(rutaServidorDescargasZip);
						ruta.mkdirs();
						Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficheros);
						ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
					}
					request.setAttribute("nombreFichero", ficheroSalida.getName());
					request.setAttribute("rutaFichero", ficheroSalida.getPath());
					request.setAttribute("borrarFichero", "true");
				}
				else
					throw new SIGAException("messages.informes.ficheroVacio");
			}
		}catch (SIGAException e) {
			// request.setAttribute("mensaje", e.getMessage());
			return exito(e.getLiteral(), request);
//			throwExcp(e.getMessage(),new String[] {"modulo.informes"},e,null);
		}finally{
			Date fin = new Date(); 
			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + "==> SIGA: FIN InformesJustificacion.GeneraInforme",10);
			ClsLogging.writeFileLog(fin.getTime()-inicio.getTime() + " MILISEGUNDOS ==> SIGA: TIEMPO TOTAL",10);	
			
		}
		
		
		

		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
	private Vector obtenerPlantillasFormulario(String idInforme, String idInstitucion, UsrBean usr) throws ClsExceptions {
	    Vector salida = new Vector ();
	    try {
		    AdmInformeAdm adm = new AdmInformeAdm(usr);
		    salida.add(adm.obtenerInforme(idInstitucion,idInforme));
		    
	    } catch (ClsExceptions e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al obtener los objetos plantillas del formulario.");
	    }
	    return salida;
	}
	

}