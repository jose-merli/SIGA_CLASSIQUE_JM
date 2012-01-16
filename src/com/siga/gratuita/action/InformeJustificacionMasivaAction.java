package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;
import com.siga.gratuita.pcajg.resoluciones.ResolucionesFicheroAbstract;


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
					} else {
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
		
		String codIncluirEjgNoFavorable = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_NOFAVORABLE, "");
		String codIncluirSinEjg = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_SIN_EJG, "");
		String codIncluirEjgSinResolucion = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_SIN_RESOLUCION, "");
		String codIncluirEjgPteCAJG = paramAdm.getValor (user.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_PTECAJG, "");
		form.setIncluirEjgPteCAJG(codIncluirEjgPteCAJG);
		form.setIncluirSinEJG(codIncluirSinEjg);
		form.setIncluirEjgNoFavorable(codIncluirEjgNoFavorable);
		form.setIncluirEjgSinResolucion(codIncluirEjgSinResolucion);
		form.setIdInstitucion(user.getLocation());
		
		
		
		
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
		if(form.getFichaColegial()){
			GenParametrosAdm paramAdm = new GenParametrosAdm (user);
			String activarMensaje = paramAdm.getValor (user.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.ACTIVAR_MENSAJE_DOCRESOLUCION_COLEGIADO, "");
			String mensaje = "";
			if (activarMensaje != null && activarMensaje.trim().equals("1")) {
				mensaje = "mensaje.documentoResolucionExpediente";				
			}
			request.setAttribute("MENSAJE_DOCRESOLUCION", mensaje);
			
		}else{
			
			request.setAttribute("MENSAJE_DOCRESOLUCION","");
			
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
		UserTransaction tx = null;
		StringBuffer msgSinAcreditaciones = new StringBuffer();
		StringBuffer msgAviso = new StringBuffer();
		UsrBean user = (UsrBean) request.getSession().getAttribute(
				"USRBEAN");
		String obsJustificacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.justificacion");
		String obsActuacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.actuacion");
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		final String idAcreditacionPenal = rp3.returnProperty("codigo.general.scsacreditacion.jurisdiccion.penal");
		List<String> designasList = new ArrayList<String>();
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
				tx = user.getTransactionPesada();
				tx.begin();
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
					String actuacionRestriccionesActiva   =  arrayRowsJustificacion[12];
					if(actuacionRestriccionesActiva.equals(ClsConstants.DB_TRUE)){
						
						String pkDesigna = anio+","+numero+","+idInstitucion+","+idTurno+","+idProcedimiento;
						if(!designasList.contains(pkDesigna))
							designasList.add(pkDesigna);
					}
					
					
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
			for (String pkiDesigna:designasList) {
				String[] pksDesigna = pkiDesigna.split(",");
				String anioDesigna = pksDesigna[0];
				String numeroDesigna = pksDesigna[1];
				String idInstitucionDesigna = pksDesigna[2];
				String idTurnoDesigna = pksDesigna[3];
				String idProcedimientoDesigna = pksDesigna[4];
				Hashtable designaHashtable = new Hashtable();
				designaHashtable.put(ScsActuacionDesignaBean.C_ANIO, anioDesigna);
				designaHashtable.put(ScsActuacionDesignaBean.C_NUMERO, numeroDesigna);
				designaHashtable.put(ScsActuacionDesignaBean.C_IDINSTITUCION, idInstitucionDesigna);
				designaHashtable.put(ScsActuacionDesignaBean.C_IDTURNO, idTurnoDesigna);
				designaHashtable.put(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO, idProcedimientoDesigna);
				Vector actuacionesDesigna =  actuacionDesginaAdm.select(designaHashtable);
				compruebaRestriccionesAcreditacion(actuacionesDesigna);
				
				
			}
			

			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error",	new String[] { "modulo.gratuita" }, e, tx);
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
	private void compruebaRestriccionesAcreditacion(Vector actuacionesDesigna)throws SIGAException{
		int numAcreditInicio = 0;
		int numAcreditFin = 0;
		
		for (int i = 0; i < actuacionesDesigna.size(); i++) {
			ScsActuacionDesignaBean actuacionDesignaBean = (ScsActuacionDesignaBean)actuacionesDesigna.get(i);
			if(actuacionDesignaBean.getIdAcreditacion().intValue()==2){
				numAcreditInicio++;
			}
			if(actuacionDesignaBean.getIdAcreditacion().intValue()==3){
				numAcreditFin++;
			}
				
			
		}
		if(numAcreditFin>numAcreditInicio)
			throw new SIGAException("messages.error.acreditacionNoValida");
		
		
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
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		if(f.getFichaColegial()){
			f.setActivarRestriccionesFicha(true);
			request.setAttribute("CAMBIAR_COLOR",false);
		}else{
			f.setAnio(null);
			String cambiarColorEjgs = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DESTACAR_EJG_NO_FAVORABLES, "");
			
			request.setAttribute("CAMBIAR_COLOR", (cambiarColorEjgs!=null && cambiarColorEjgs.equalsIgnoreCase(ClsConstants.DB_TRUE)));
		}
		
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
				
				//Haria falta meter los parametros en con ClsConstants
				String cod_Fact_ja_2005 = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_FACT_JA_2005, "");
				boolean	aplicarAcreditacionesAnterior2005 = (cod_Fact_ja_2005!=null && cod_Fact_ja_2005.equalsIgnoreCase(ClsConstants.DB_TRUE));
				f.setAplicarAcreditacionesAnterior2005(aplicarAcreditacionesAnterior2005);
				
				String mensajeResponsabilidadJustificacionLetrado = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_MENSAJE_RESPONSABILIDAD_LETRADO, "");
				f.setMensajeResponsabilidadJustificacionLetrado(mensajeResponsabilidadJustificacionLetrado);
				
				String codIncluirEjgNoFavorable = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_NOFAVORABLE, "");
				String codIncluirSinEjg = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_SIN_EJG, "");
				String codIncluirEjgSinResolucion = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_SIN_RESOLUCION, "");
				String codIncluirEjgPteCAJG = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_INCLUIR_EJG_PTECAJG, "");
				f.setIncluirEjgPteCAJG(codIncluirEjgPteCAJG);
				f.setIncluirSinEJG(codIncluirSinEjg);
				f.setIncluirEjgNoFavorable(codIncluirEjgNoFavorable);
				f.setIncluirEjgSinResolucion(codIncluirEjgSinResolucion);
				
				
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
	
	private String getExpediente(List<DefinirEJGForm> ejgList){
		StringBuffer expedientes = new StringBuffer("");
		if(ejgList!=null && ejgList.size()>0){
			for(DefinirEJGForm ejgForm:ejgList){
				expedientes.append(ejgForm.getNombre());
				expedientes.append(", ");
				
			}
			//quitamos la ltima coma
			expedientes.delete(expedientes.lastIndexOf(","), expedientes.length());
			
		}
		
		
		return expedientes.toString();
		
	}
	
	protected String download (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		InformeJustificacionMasivaForm miForm = (InformeJustificacionMasivaForm) formulario;
		File file = getFicheroPDF(getIDInstitucion(request).toString(), miForm.getDocResolucion());

		if (file == null) {								
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}				
		
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}	
	
	private File getFicheroPDF(String idInstitucion, String docResolucion) {
		File file = new File(ResolucionesFicheroAbstract.getDirectorioArchivos(idInstitucion));
		file = new File(file, docResolucion + "." + ResolucionesFicheroAbstract.getExtension(idInstitucion));
		if (!file.exists()) {
			file = null;
		}		
		return file;
	}


}