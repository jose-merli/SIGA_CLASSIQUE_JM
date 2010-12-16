package com.siga.gratuita.action;

import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TreeMap;
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

import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsAcreditacionBean;
import com.siga.beans.ScsAcreditacionProcedimientoAdm;
import com.siga.beans.ScsAcreditacionProcedimientoBean;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsTurnoBean;

import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;


public class InformeJustificacionMasivaLetradoAction extends InformeJustificacionMasivaAction {
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

					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("buscarInit")){
						// buscarPersona
						borrarPaginador(request, paginadorPenstania);
						mapDestino = buscarPor(mapping, miForm, request, response);	
					}else if (accion.equalsIgnoreCase("")) {
						mapDestino = "";
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

	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		return this.abrir(mapping, formulario, request, response);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		StringBuffer msgAviso = new StringBuffer();
		StringBuffer msgSinAcreditaciones = new StringBuffer();
		UsrBean user = (UsrBean) request.getSession().getAttribute(
		"USRBEAN");
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp3 = new ReadProperties("SIGA.properties");
		final String TIPO_ACREDIT_INIFIN = rp3.returnProperty("codigo.general.scstipoacreditacion.iniciofin");
		final String ACREDITACION_INIFIN = rp3.returnProperty("codigo.general.scsacreditacion.iniciofin");
		String obsJustificacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.justificacion");
		String obsActuacion = UtilidadesString.getMensajeIdioma(user, "gratuita.informeJustificacionMasiva.observaciones.actuacion");
		try {
			InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
			
			GstDate gstDate = new GstDate();
			
			//String fechaDefecto = f.getFecha();
			//Para el letrado ponemos la fecha de hoy. en caso de que la coja del formulario
			/*String fechaDefecto = f.getFecha();
			Date dateFechaDefecto = null;
			if (fechaDefecto != null && !fechaDefecto.equalsIgnoreCase(""))
				dateFechaDefecto = gstDate.parseStringToDate(fechaDefecto,
						"dd/MM/yyyy", new Locale(user.getLanguage()));
				
			*/

			tx = this.getUserBean(request).getTransaction();
			tx.begin(); // Abro aqui la transaccion porque se insertan personas

			ScsDesignaAdm desginaAdm = new ScsDesignaAdm(this
					.getUserBean(request));
			ScsActuacionDesignaAdm actuacionDesginaAdm = new ScsActuacionDesignaAdm(
					this.getUserBean(request));

			String claves[] = { ScsDesignaBean.C_ANIO, ScsDesignaBean.C_NUMERO,
					ScsDesignaBean.C_IDINSTITUCION, ScsDesignaBean.C_IDTURNO };
			Hashtable htAcumulaAcreditacionesProcedimiento = new Hashtable();
			Boolean isFactAnterior2005 = null;
			for (int i = 0; i < f.getDatosTabla().size(); i++) {

				Vector vCampos = f.getDatosTablaOcultos(i);
				String anio = (String) vCampos.get(0);
				String numero = (String) vCampos.get(1);
				String idInstitucion = (String) vCampos.get(2);
				String idTurno = (String) vCampos.get(3);
				String idJuzgado = (String) vCampos.get(4);
				String idInstJuzgado = (String) vCampos.get(5);
				String isCheckFechaIni = (String) vCampos.get(6);
				String isCheckFechaFin = (String) vCampos.get(7);
				String idProcedimiento = (String) vCampos.get(8);
				String fechaIniActuacion = (String) vCampos.get(9);
				String idJurisdiccion = (String) vCampos.get(10);
				String idPersona = (String) vCampos.get(11);
				String codigoDesigna = (String) vCampos.get(12);
				String numeroJustificaciones = (String) vCampos.get(13);
				String baja = (String) vCampos.get(14);
				String validarJustificaciones = (String) vCampos.get(15);

				Hashtable h = new Hashtable();

				UtilidadesHash.set(h, ScsDesignaBean.C_ANIO, anio);
				UtilidadesHash.set(h, ScsDesignaBean.C_IDINSTITUCION,
						idInstitucion);
				
				UtilidadesHash.set(h, ScsDesignaBean.C_NUMERO, numero);
				UtilidadesHash.set(h, ScsDesignaBean.C_IDTURNO, idTurno);

				Vector aux = new Vector();
				/*
				 * if (!esCampoVacio(fechaIni)) {
				 * aux.add(ScsDesignaBean.C_FECHAINI); UtilidadesHash.set (h,
				 * ScsDesignaBean.C_FECHAINI,
				 * GstDate.getApplicationFormatDate("", fechaDefecto)); }
				 */
				boolean isFechaFin = new Boolean(isCheckFechaFin)
						.booleanValue();
				/*if (isFechaFin) {
					aux.add(ScsDesignaBean.C_FECHAFIN);
					UtilidadesHash.set(h, ScsDesignaBean.C_FECHAFIN,"sysdate");
					//Si recogieramos el dato del formulario
					//UtilidadesHash.set(h, ScsDesignaBean.C_FECHAFIN, GstDate
						//	.getApplicationFormatDate("", fechaDefecto));
				}*/
				boolean isBaja = UtilidadesString.stringToBoolean(baja);
				if (isBaja) {
					aux.add(ScsDesignaBean.C_FECHAESTADO);
					aux.add(ScsDesignaBean.C_ESTADO);
					
					UtilidadesHash.set(h, ScsDesignaBean.C_FECHAESTADO,
							"sysdate");
					UtilidadesHash.set(h, ScsDesignaBean.C_ESTADO, "F");
				}

				String campos[] = new String[aux.size()];
				for (int b = 0; b < aux.size(); b++) {
					campos[b] = (String) aux.get(b);
				}

				//si hay cambio de estado de la designa actulizaremos dicho
				// campo

				if (isBaja)
					desginaAdm.updateDirect(h, claves, campos);
				else {
					try{
					//					(si se cambia afinalizado se mete la fechaFin)
						//if (isFechaFin)
							//desginaAdm.updateDirect(h, claves, campos);
	
						//Preparamos el insert. Aqui le mete el numero de asunto
						// que sera el max+1
						Hashtable hashActuacion = new Hashtable();
	
						boolean isFechaInicio = new Boolean(isCheckFechaIni)
								.booleanValue();
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_ANIO, anio);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCION,
								idInstitucion);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO,
								idPersona);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_NUMERO, numero);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDTURNO, idTurno);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDJUZGADO, idJuzgado);
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO,
								idInstJuzgado);
						
						
				
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHAMODIFICACION,
								"sysdate");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_USUMODIFICACION,
								new Long(this.getUserBean(request).getIdPersona()));
	
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,
								idProcedimiento);
	
						UtilidadesHash
								.set(
										hashActuacion,
										ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL,
										"0");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_ANULACION, "0");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_VALIDADA, "1");
	
						UtilidadesHash.set(
										hashActuacion,
										ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO,
										idInstitucion);
						
						UtilidadesHash.set(
								hashActuacion,
								ScsTurnoBean.C_VALIDARJUSTIFICACIONES,
								validarJustificaciones);
	
						Hashtable hashInicial = (Hashtable)hashActuacion.clone();
						
						String idAcreditacion = null;
						//Por defecto la fecha de hoy
						Date dateFechaDefecto = new Date();
						
						//Miramos si estaba metida l acreditacion de inicio
						Date dateFechaInicioActuacion = null;
						if (fechaIniActuacion != null && !fechaIniActuacion.equalsIgnoreCase(""))
							dateFechaInicioActuacion = gstDate.parseStringToDate(fechaIniActuacion,
									"dd/MM/yyyy", new Locale(user.getLanguage()));
						
						
						ArrayList alHashActuacion = actuacionDesginaAdm.existeActuacionesSinJustificar(hashActuacion);
						//Vemos si existen mas de una actuacion dada de alta. si es asi recorremos el array 
						//e insertamos la justificacion de cada una de ellas
						if(alHashActuacion!=null && alHashActuacion.size()>0){
							int sizeHash = alHashActuacion.size();
							if( sizeHash != Integer.parseInt(numeroJustificaciones)){
								msgAviso.append(", ");
								msgAviso.append(anio);
								msgAviso.append(" / ");
								msgAviso.append(codigoDesigna);
							}else {
							  
								String fechaJustificacion = null;
								switch (sizeHash) {
									case 1:
										//Casos posibles:
										//1. Tipo de acreditacion de inicio-fin
										//2. Tipo de acreditacion de inicio + tipo de acreditacion de fin
											//Inicio dada de alta y justificada y fin dad de alta sin justificar
											//Inicio
										hashActuacion = (Hashtable)alHashActuacion.get(0);
										String idAcreditActExistente = (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
										String idTipoAcreditacion =  (String)hashActuacion.get(ScsAcreditacionBean.C_IDTIPOACREDITACION);
										fechaJustificacion = (String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
										
										if(idTipoAcreditacion.equalsIgnoreCase(TIPO_ACREDIT_INIFIN)){
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											//si es de inicio y fin nos da igual lo que haya marcado ya que se va a justificar una sola
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditActExistente);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");
												
											}
											
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}
											
										}else{
											//si no es de este tipo y solo hay una dada de alta, que es la de inicio
											//(POR DEFINICION NO PUEDE EXISTIR ACTUACION DE FIN SIN INICIO) 
											if (isFechaInicio && isFechaFin) {
												//Este caso es que esta insertada la actuacion de inicio sin justificar
												//y nos selecciona los dos check
												//HAY QUE MODIFICAR LA DE INICIO E INSEERTAR LA DE FIN
												//HAY QUE MODIFICAR LA DE INICIO
												
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditActExistente);
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
											
												if(fechaJustificacion==null){
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														"sysdate");
												}else{
												  	
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_VALIDADA, "1");
												  
													
												}
												if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
												actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
												}
												//Preparamos el insert de la acreditacion de fin
												idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
														isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
														idProcedimiento,idJurisdiccion, false, true,user);
												UtilidadesHash.set(hashInicial,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditacion);
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONES,
														obsActuacion);
												
												
												UtilidadesHash.set(hashInicial,
														ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														"sysdate");
												UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_FECHA,
													"sysdate");
												
												//comprobamos si existe la actuacion solo que no esta justificada
												hashInicial = actuacionDesginaAdm.prepararInsert(hashInicial);
												if ((user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
													UtilidadesHash.set(hashInicial,
															ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
															"");
													UtilidadesHash.set(hashInicial,
															ScsActuacionDesignaBean.C_VALIDADA,
															"0");
												}
												actuacionDesginaAdm.insert(hashInicial);
												
											}else if(isFechaInicio||isFechaFin){
												//Este caso es que esta insertada la actuacion de inicio sin justificar o
												// que la de inicio este justificada y la de fin no. 
												//Por lo tanto solo queda un check por marcar que es el de inicio o el de fin
												//HAY QUE MODIFICAR LA DE INICIO O LA DE FIN
												if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){	
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditActExistente);
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
												if(fechaJustificacion==null){
													UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														"sysdate");
												}else{
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_VALIDADA, "1");
													
												}
												actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
												}
												
											}
											
											
										}
										break;
									case 2:
										hashActuacion = (Hashtable)alHashActuacion.get(0);
										fechaJustificacion = (String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
										String idAcreditActExistenteInicio = (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
										
										if (isFechaInicio && isFechaFin) {
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditActExistenteInicio);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
										
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");
												
											}
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											hashActuacion = (Hashtable)alHashActuacion.get(1);
											fechaJustificacion = (String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
											
											String idAcreditActExistenteFin = (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditActExistenteFin);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");
												
											}
											
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}
											
										}else{
											//Solo puede ser que sea de inicio
											//(por progama Si esta seleccionado el fin hay que seleccionar el incio)
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditActExistenteInicio);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");
												
											}
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											
											}
											
										}
										
										break;
		
									default:
										break;
								}
							
							}
						}else{
							
							
							
							if (isFechaInicio && isFechaFin) {
								
								//Si nuestro procedimiento tiene creado acreditacion de inicio fin, metemos esta
								//si NO meteremos una de inicio y otra de fin individuales
								ScsAcreditacionProcedimientoAdm scsAcProc = new ScsAcreditacionProcedimientoAdm(user);
								Hashtable htPKScsAcredProc = new Hashtable();
								htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDINSTITUCION,idInstitucion);
								htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO,idProcedimiento);
								htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDACREDITACION,ACREDITACION_INIFIN);
								Vector vLista = scsAcProc.selectByPK(htPKScsAcredProc);
														
								if(vLista!=null && vLista.size()>0){
									
									idAcreditacion = ACREDITACION_INIFIN;
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONES,
											obsActuacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_IDACREDITACION,
											idAcreditacion);
									//comprobamos si existe la actuacion solo que no esta justificada
									hashActuacion = actuacionDesginaAdm.prepararUpdate(hashActuacion);
									
									
									
									//Si trae numero de asunto metemos la fecha de justificacion, si no seran nuevas actuaciones
									if((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)!=null &&
											!((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)).equalsIgnoreCase("")){
										
										//Si ha habido concurrencia es que la fecha de justificacion no es nula 
										if(hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)!=null
												&&!((String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)).equalsIgnoreCase("")){
											msgAviso.append(", ");
											msgAviso.append(anio);
											msgAviso.append(" / ");
											msgAviso.append(codigoDesigna);
											
											
										}else{
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}
											
											
										}
									
									}else{
									//Si no trae numero de asunto es que no existe registro no justificado.
									//Habra que dar de alta la actuacion
											
											hashActuacion = actuacionDesginaAdm.prepararInsert(hashActuacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONES,
													obsActuacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											UtilidadesHash
												.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHA,
													"sysdate");
											
											if ((user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														"");
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA,
														"0");
											}
											actuacionDesginaAdm.insert(hashActuacion);
										
									}
									
									
									
								}else{
								
									//						Si es de inicio y final metemos una de inicio y otra
									// de fin
									idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
											isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
											idProcedimiento,idJurisdiccion, true, false,user);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONES,
											obsActuacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_IDACREDITACION,
											idAcreditacion);
									//comprobamos si existe la actuacion solo que no esta justificada
									hashActuacion = actuacionDesginaAdm.prepararUpdate(hashActuacion);
									
									
									
									//Si trae numero de asunto metemos la fecha de justificacion, si no seran nuevas actuaciones
									if((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)!=null &&
											!((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)).equalsIgnoreCase("")){
										
										//Si ha habido concurrencia es que la fecha de justificacion no es nula 
										if(hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)!=null
												&&!((String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)).equalsIgnoreCase("")){
											msgAviso.append(", ");
											msgAviso.append(anio);
											msgAviso.append(" / ");
											msgAviso.append(codigoDesigna);
											
											
										}else{
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}
											
											
										}
									
									}else{
									//Si no trae numero de asunto es que no existe registro no justificado.
									//Habra que dar de alta la actuacion
											
										hashActuacion = actuacionDesginaAdm.prepararInsert(hashActuacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
												obsJustificacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONES,
												obsActuacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
												"sysdate");
										UtilidadesHash
											.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHA,
												"sysdate");
										if ((user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"");
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_VALIDADA,
													"0");
										}
										actuacionDesginaAdm.insert(hashActuacion);
										
									}
									idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
											isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
											idProcedimiento,idJurisdiccion, false, true,user);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_IDACREDITACION,
											idAcreditacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONES,
											obsActuacion);
									//Sacamos el asunto de la ultima acreditacion para comprobar que 
									String newNumeroAsunto = (String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO);
									//comprobamos si existe la actuacion solo que no esta justificada
									hashActuacion = actuacionDesginaAdm.prepararUpdate(hashActuacion);
									
									
									
									//Si trae numero de asunto metemos la fecha de justificacion
									if((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)!=null &&
											!((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)).equalsIgnoreCase("")&&
											!((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)).equalsIgnoreCase(newNumeroAsunto)){
										
										//Si ha habido concurrencia es que la fecha de justificacion no es nula 
										if(hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)!=null
												&&!((String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)).equalsIgnoreCase("")){
											msgAviso.append(", ");
											msgAviso.append(anio);
											msgAviso.append(" / ");
											msgAviso.append(codigoDesigna);
											
										}else{
											if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){	
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"sysdate");
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}
											
											
										}
										
									}else{
									//Si no trae numero de asunto es que no existe registro no justificado.
									//Habra que dar de alta la actuacion
										
										hashActuacion = actuacionDesginaAdm.prepararInsert(hashActuacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
												obsJustificacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONES,
												obsActuacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
												"sysdate");
										UtilidadesHash
											.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHA,
												"sysdate");
										if ((user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													"");
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_VALIDADA,
													"0");
										}
										actuacionDesginaAdm.insert(hashActuacion);
										
									}
								
								}
		
							} else {
								//						Creamos una actuacion de inicio o fin.
								idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
										isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
										idProcedimiento,idJurisdiccion, isFechaInicio, isFechaFin,user);
								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
										obsJustificacion);
								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_OBSERVACIONES,
										obsActuacion);
								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_IDACREDITACION,
										idAcreditacion);
								//comprobamos si existe la actuacion solo que no esta justificada
								hashActuacion = actuacionDesginaAdm.prepararUpdate(hashActuacion);
								
								
								
								//Si trae numero de asunto metemos la fecha de justificacion
								if((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)!=null &&
										!((String)hashActuacion.get(ScsActuacionDesignaBean.C_NUMEROASUNTO)).equalsIgnoreCase("")){
									
									//Si ha habido concurrencia es que la fecha de justificacion no es nula 
									if(hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)!=null
											&&!((String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION)).equalsIgnoreCase("")){
										msgAviso.append(", ");
										msgAviso.append(anio);
										msgAviso.append(" / ");
										msgAviso.append(codigoDesigna);
										
									}else{
										if (!(user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
												obsJustificacion);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
												"sysdate");
										actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
										}
										
									}
									
								}else{
								//Si no trae numero de asunto es que no existe registro no justificado.
								//Habra que dar de alta la actuacion
										
									hashActuacion = actuacionDesginaAdm.prepararInsert(hashActuacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONES,
											obsActuacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
											"sysdate");
									UtilidadesHash
										.set(hashActuacion,
											ScsActuacionDesignaBean.C_FECHA,
											"sysdate");
									if ((user.isLetrado() && (hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES)!=null && hashActuacion.get(ScsTurnoBean.C_VALIDARJUSTIFICACIONES).equals("S")))){
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
												"");
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_VALIDADA,
												"0");
									}
									actuacionDesginaAdm.insert(hashActuacion);

								}
		
							}
		
						}
					}catch(ClsExceptions cls){
						msgSinAcreditaciones.append(", ");
						msgSinAcreditaciones.append(anio);
						msgSinAcreditaciones.append(" / ");
						msgSinAcreditaciones.append(codigoDesigna);
						/*msgSinAcreditaciones.append("(");
						msgSinAcreditaciones.append(cls.getMsg());
						msgSinAcreditaciones.append("}");*/
						
						
					}
					
				}

			}

			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.gratuita" }, e, tx);
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

	

	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(this
				.getUserBean(request));
		//Vector v = admDesignas.getDesignasLetrado(this.getIDInstitucion(request),
		// f.getLetrado(), f.getFecha());
		
		/*Vector v = admDesignas.getDesignasLetradoNew(this.getIDInstitucion(request), f
				.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
				.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),false);
		*/
		/*Vector v = admDesignas.getDatosSalidaJustificacion(this.getIDInstitucion(request), f
				.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
				.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),false);*/
		String keyPersona =f.getLetrado();
		Hashtable htPersonas = admDesignas.getPersonasSalidaJustificacion(this.getIDInstitucion(request),keyPersona , f.getFechaDesde(), f.getFechaHasta(), f
				.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),false);
		
		
		Vector vRowsInformePorPersona = null;
		if(htPersonas!=null && htPersonas.size()>0){
			TreeMap tmRowsInformePorPersona = (TreeMap) htPersonas.get(keyPersona);
			
			if(tmRowsInformePorPersona!=null){
				vRowsInformePorPersona = new Vector(tmRowsInformePorPersona.values());
			}else{
				vRowsInformePorPersona = new Vector();
				
			}
		}else{
			vRowsInformePorPersona = new Vector();
			
		}
		request.setAttribute("resultado", vRowsInformePorPersona);
		request.setAttribute("letrado", f.getLetrado());
		request.setAttribute("MODOPESTANA", modoPestanha);
		

		if (f.getMostrarTodas() != null && !f.getMostrarTodas().equals("")) {
			request.setAttribute("mostrarTodas", f.getMostrarTodas());
		}
		if (f.getFechaDesde() != null && !f.getFechaDesde().equals("")) {
			request.setAttribute("fechaDesde", f.getFechaDesde());
		}
		if (f.getFechaHasta() != null && !f.getFechaHasta().equals("")) {
			request.setAttribute("fechaHasta", f.getFechaHasta());
		}

		return "resultado";
	}
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(this
				.getUserBean(request));
		

		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorPenstania);
		try {
			HashMap databackup=getPaginador(request, paginadorPenstania);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					Row fila = (Row)datos.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					String descJuzgado = (String) registro.get("DESC_JUZGADO");
					//miramos si se han actualiozado los datos. Para ello miramos si tiene
					//metida uno de los campos que se actualizan en la actualizacion
					//como es DESC_JUZGADO
					if(descJuzgado==null)
					
						datos = actualizarDesignasLetrado(admDesignas,this.getIDInstitucion(request).toString(),datos);
					
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	
				databackup=new HashMap();
				String keyPersona =f.getLetrado();
				PaginadorBind paginador = admDesignas.getDesignasLetradoPaginador(this.getIDInstitucion(request),keyPersona , f.getFechaDesde(), f.getFechaHasta(), f
						.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),f.getAnio(),false);
				//Paginador paginador = new Paginador(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros==0){					
					paginador =null;
				}
				databackup.put("paginador",paginador);
				if (paginador!=null){ 
					Vector datos = paginador.obtenerPagina(1);
					Row fila = (Row)datos.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					String descJuzgado = (String) registro.get("DESC_JUZGADO");
					//miramos si se han actualiozado los datos. Para ello miramos si tiene
					//metida uno de los campos que se actualizan en la actualizacion
					//como es DESC_JUZGADO
					if(descJuzgado==null)
					
						datos = actualizarDesignasLetrado(admDesignas,this.getIDInstitucion(request).toString(),datos);
					
					databackup.put("datos",datos);
					setPaginador(request, paginadorPenstania, databackup);
				} 	

				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);



			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		request.setAttribute("letrado", f.getLetrado());
		request.setAttribute("MODOPESTANA", modoPestanha);
		if (f.getMostrarTodas() != null && !f.getMostrarTodas().equals("")) {
			request.setAttribute("mostrarTodas", f.getMostrarTodas());
		}
		if (f.getFechaDesde() != null && !f.getFechaDesde().equals("")) {
			request.setAttribute("fechaDesde", f.getFechaDesde());
		}
		if (f.getFechaHasta() != null && !f.getFechaHasta().equals("")) {
			request.setAttribute("fechaHasta", f.getFechaHasta());
		}
		return "resultado";
	}
	/**
	 * Metodo que nos actualiza el vector de justificaciones paginados con 
	 * 1.FechaEfectiva
	 * 2.Estado Compra
	 * @param datos
	 * @return
	 * @throws ClsExceptions 
	 */
	private Vector actualizarDesignasLetrado(ScsDesignasLetradoAdm admDesignas,String idInstitucion,Vector datos) throws ClsExceptions{
		
		
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable htAcumuladorJuzgados = new Hashtable();
		Hashtable htAcumuladorProcedimientos = new Hashtable();
		Hashtable htAcumuladorTurnos = new Hashtable();
		for (int i=0;i<datos.size();i++) 
		{
			Row fila = (Row)datos.get(i);
			
			Hashtable registro = (Hashtable) fila.getRow();

			//String numeroDesigna = (String)registro.get("NUMERO");
			//String codigoDesigna = (String)registro.get("CODIGO");
			//String anioDesigna = (String)registro.get("ANIO");
			String idTurno  = (String)registro.get("IDTURNO");
			String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
			//String fechaOrden  = (String)registro.get("FECHAORDEN");
			
			//helperInformes.completarHashSalida(registro,getNumeroJustificacionesDesignaSalida(idInstitucion.toString(), 
				//	idTurno,anioDesigna,numeroDesigna));
			
			Vector vTurno = admDesignas.geTurno(idInstitucion.toString(), 
					idTurno,htAcumuladorTurnos,helperInformes);
			helperInformes.completarHashSalida(registro,vTurno);
			
			
			if(idProcedimiento!=null && !idProcedimiento.equalsIgnoreCase("")){
				Vector vProcedimiento = admDesignas.getProcedimiento(idInstitucion.toString(), 
						idProcedimiento,htAcumuladorProcedimientos,helperInformes);
				helperInformes.completarHashSalida(registro,vProcedimiento);
			}else{
				registro.put("PROCEDIMIENTO","");
				registro.put("CATEGORIA","");
				registro.put("IDJURISDICCION","");
				
			}
			
			
			String idJuzgado = (String)registro.get(ScsDesignaBean.C_IDJUZGADO);
			String idInstitucionJuzgado  = (String)registro.get("IDINSTITUCION_JUZG");
			if(idJuzgado!=null && !idJuzgado.equalsIgnoreCase("")){
				Vector vJuzgado = admDesignas.getJuzgado(idInstitucionJuzgado.toString(),idJuzgado,htAcumuladorJuzgados, helperInformes);
				helperInformes.completarHashSalida(registro,vJuzgado);
				String descJuzgado = (String)registro.get("JUZGADO"); 
				registro.put("DESC_JUZGADO",descJuzgado);
				
			}else{
				registro.put("DESC_JUZGADO","");
				registro.put("IDJUZGADO","");
				
			}

		}
		
		return datos;
		
		
	}
	
	
	public Hashtable completarHashSalida(Hashtable htSalida, Vector vParcial){
		
		if (vParcial!=null && vParcial.size()>0) {
			Hashtable registro = (Hashtable) vParcial.get(0);
			htSalida.putAll(registro);
		}
	
		return htSalida;
		
		
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
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		InformeJustificacionMasivaForm miForm = (InformeJustificacionMasivaForm)formulario; 
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;		
		
		
		

		try {
			
			
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			miForm.setLetrado(idPersona);
			request.setAttribute("letrado",idPersona);
			
			String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
			
			
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				} catch (Exception e1){
					throw new Exception("Error al obtener los datos de colegiales");
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);
			request.setAttribute("MODOPESTANA", modoPestanha);
			//Recoho el modo en el que esta y lo paso como parametro
			//String accionPestanha = request.getParameter("accion");
			//request.setAttribute("modo",accionPestanha);
			
		}catch(Exception e){
			throwExcp("messages.select.error",e,null);
		}
		return "inicio";
	}

}