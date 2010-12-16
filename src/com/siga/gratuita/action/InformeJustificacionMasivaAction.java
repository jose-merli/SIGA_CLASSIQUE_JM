package com.siga.gratuita.action;

import java.util.Date;

import java.util.ArrayList;
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
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;


import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAcreditacionBean;
import com.siga.beans.ScsAcreditacionProcedimientoAdm;
import com.siga.beans.ScsAcreditacionProcedimientoBean;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsActuacionDesignaBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;


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

					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("")) {
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

	protected synchronized String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		StringBuffer msgSinAcreditaciones = new StringBuffer();
		StringBuffer msgAviso = new StringBuffer();
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
			String fechaDefecto = f.getFecha();
			Date dateFechaDefecto = null;
			if (fechaDefecto != null && !fechaDefecto.equalsIgnoreCase(""))
				dateFechaDefecto = gstDate.parseStringToDate(fechaDefecto,
						"dd/MM/yyyy", new Locale(user.getLanguage()));

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
					UtilidadesHash.set(h, ScsDesignaBean.C_FECHAFIN, GstDate
							.getApplicationFormatDate("", fechaDefecto));
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

						/*UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
								GstDate.getApplicationFormatDate("", fechaDefecto));
						UtilidadesHash
								.set(hashActuacion,
										ScsActuacionDesignaBean.C_FECHA, 
										GstDate.getApplicationFormatDate("", fechaDefecto));
						 */
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_FECHAMODIFICACION,
						"sysdate");
						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_USUMODIFICACION,
								new Long(this.getUserBean(request).getIdPersona()));

						UtilidadesHash.set(hashActuacion,
								ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,
								idProcedimiento);

						UtilidadesHash.set(
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

						Hashtable hashInicial = (Hashtable)hashActuacion.clone();



						String idAcreditacion = null;
						//					Miramos si estaba metida l acreditacion de inicio
						Date dateFechaInicioActuacion = null;
						if (fechaIniActuacion != null && !fechaIniActuacion.equalsIgnoreCase(""))
							dateFechaInicioActuacion = gstDate.parseStringToDate(fechaIniActuacion,
									"dd/MM/yyyy", new Locale(user.getLanguage()));


						//try{


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
										//si es de inicio y fin nos da igual lo que haya marcado ya que se va a justificar una sola
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_IDACREDITACION,
												idAcreditActExistente);
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
												obsJustificacion);								
										//Es posible que este justificada y no validada
										if(fechaJustificacion==null){
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													GstDate.getApplicationFormatDate("", fechaDefecto));
										}else{
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_VALIDADA, "1");

										}
										actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
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
											//Es posible que este justificada y no validada
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														GstDate.getApplicationFormatDate("", fechaDefecto));
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");

											}
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));

											//Preparamos el insert de la acreditacion de fin
											idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
													isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
													idProcedimiento,idJurisdiccion, false, true,user);
											UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditacion);

											UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													GstDate.getApplicationFormatDate("", fechaDefecto));
											UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_FECHA,
													GstDate.getApplicationFormatDate("", fechaDefecto));

											//comprobamos si existe la actuacion solo que no esta justificada
											hashInicial = actuacionDesginaAdm.prepararInsert(hashInicial);
											UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashInicial,
													ScsActuacionDesignaBean.C_OBSERVACIONES,
													obsActuacion);
											actuacionDesginaAdm.insert(hashInicial);

										}else if(isFechaInicio||isFechaFin){
											//Este caso es que esta insertada la actuacion de inicio sin justificar o
											// que la de inicio este justificada y la de fin no. 
											//Por lo tanto solo queda un check por marcar que es el de inicio o el de fin
											//HAY QUE MODIFICAR LA DE INICIO O LA DE FIN
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_IDACREDITACION,
													idAcreditActExistente);

											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											//Es posible que este justificada y no validada
											if(fechaJustificacion==null){
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
														GstDate.getApplicationFormatDate("", fechaDefecto));
											}else{
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_VALIDADA, "1");

											}
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));

										}


									}
									break;
								case 2: 


									if (isFechaInicio && isFechaFin) {

										for (int j=0; j<alHashActuacion.size(); j++){
											hashActuacion = (Hashtable)alHashActuacion.get(j);
											idAcreditActExistente = (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
											fechaJustificacion = (String)hashActuacion.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
											if (Integer.parseInt(hashActuacion.get("IDTIPOACREDITACION").toString())==(ClsConstants.ESTADO_ACREDITACION_INICIO)){




												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditActExistente);

												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
												if(fechaJustificacion==null){
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
															GstDate.getApplicationFormatDate("", fechaDefecto));
												}else{
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_VALIDADA, "1");

												}
												actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}else{

												//Atencion cogemos la fecha justificacion de la segunda por si acaso

												
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditActExistente);

												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
												//Es posible que este justificada y no validada
												if(fechaJustificacion==null){
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
															GstDate.getApplicationFormatDate("", fechaDefecto));
												}else{
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_VALIDADA, "1");

												}
												actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));
											}	
										}
									}else{
										//Solo puede ser que sea de inicio
										//(por progama Si esta seleccionado el fin hay que seleccionar el incio)

										for (int j=0; j<alHashActuacion.size(); j++){
											hashActuacion = (Hashtable)alHashActuacion.get(j);
											String idAcreditActExistenteInicio = (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
											if (Integer.parseInt(hashActuacion.get("IDTIPOACREDITACION").toString())==(ClsConstants.ESTADO_ACREDITACION_INICIO)){
												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_IDACREDITACION,
														idAcreditActExistenteInicio);

												UtilidadesHash.set(hashActuacion,
														ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
														obsJustificacion);
												//Es posible que este justificada y no validada
												if(fechaJustificacion==null){
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
															GstDate.getApplicationFormatDate("", fechaDefecto));
												}else{
													UtilidadesHash.set(hashActuacion,
															ScsActuacionDesignaBean.C_VALIDADA, "1");

												}
												actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));

											}
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
											ScsActuacionDesignaBean.C_IDACREDITACION,
											idAcreditacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
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
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													GstDate.getApplicationFormatDate("", fechaDefecto));
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));


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
												GstDate.getApplicationFormatDate("", fechaDefecto));
										UtilidadesHash
										.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHA,
												GstDate.getApplicationFormatDate("", fechaDefecto));
										actuacionDesginaAdm.insert(hashActuacion);
									}


								}else{



									//						Si es de inicio y final metemos una de inicio y otra
									// de fin
									idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
											isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
											idProcedimiento,idJurisdiccion, true, false,user);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_IDACREDITACION,
											idAcreditacion);
									UtilidadesHash.set(hashActuacion,
											ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
											obsJustificacion);
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
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													GstDate.getApplicationFormatDate("", fechaDefecto));
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));


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
												GstDate.getApplicationFormatDate("", fechaDefecto));
										UtilidadesHash
										.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHA,
												GstDate.getApplicationFormatDate("", fechaDefecto));
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

											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
													obsJustificacion);
											UtilidadesHash.set(hashActuacion,
													ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
													GstDate.getApplicationFormatDate("", fechaDefecto));
											actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));


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
												GstDate.getApplicationFormatDate("", fechaDefecto));
										UtilidadesHash
										.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHA,
												GstDate.getApplicationFormatDate("", fechaDefecto));
										actuacionDesginaAdm.insert(hashActuacion);
									}

								}

							} else {
								//						Creamos una actuacion de inicio o fin.
								idAcreditacion = getAcreditacion(htAcumulaAcreditacionesProcedimiento,
										isFactAnterior2005,dateFechaDefecto,dateFechaInicioActuacion,
										idProcedimiento,idJurisdiccion, isFechaInicio, isFechaFin,user);
								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_IDACREDITACION,
										idAcreditacion);
								//comprobamos si existe la actuacion solo que no esta justificada


								UtilidadesHash.set(hashActuacion,
										ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
										obsJustificacion);
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
										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,
												obsJustificacion);

										UtilidadesHash.set(hashActuacion,
												ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,
												GstDate.getApplicationFormatDate("", fechaDefecto));
										actuacionDesginaAdm.updateDirect(actuacionDesginaAdm.hashTableToBean(hashActuacion));


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
											GstDate.getApplicationFormatDate("", fechaDefecto));
									UtilidadesHash
									.set(hashActuacion,
											ScsActuacionDesignaBean.C_FECHA,
											GstDate.getApplicationFormatDate("", fechaDefecto));
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

	protected String getAcreditacion(Hashtable htAcumulaAcreditacionesProcedimiento,
			Boolean isFactAnterior2005,Date fechaJustificacion,Date dateFechaInicioActuacion,
			String idProcedimiento, String idJurisdiccion,
			boolean isAcreditacionInicio,boolean isAcreditacionFin,UsrBean usrBean) throws ClsExceptions {
		//		Si la fecha es anterior a 01/01/2005 cogera un tipo de
		// acreditacion(2 si es inuicio y 3 si es final)
		//Si es posterior a esa fecha cogera otros tipos de
		// acreditacion(6 si es inuicio y 7 si es final)
		String idAcreditacion = "";
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp3 = new ReadProperties("SIGA.properties");
		//Miramos es tipo de jurisdiccion(constante ==1). 
		//Si es de penal va a depender de la fecha de acreditacion 
		
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		//Haria falta meter los parametros en con ClsConstants
		if(isFactAnterior2005 == null){
			String cod_Fact_ja_2005 = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_FACT_JA_2005, "");
			isFactAnterior2005 = new Boolean((cod_Fact_ja_2005!=null && cod_Fact_ja_2005.equalsIgnoreCase(ClsConstants.DB_TRUE)));
        
		}
		//obtenemos el parametro si es 1 hay que aplicara la logica de las acreditaciones anteriores a 2005
        //si es 0 no es necesario
        String acreditacion = "";
		final String idAcreditacionPenal = rp3.returnProperty("codigo.general.scsacreditacion.jurisdiccion.penal");
		
		if(idJurisdiccion!=null && idJurisdiccion.equalsIgnoreCase(idAcreditacionPenal) && isFactAnterior2005.booleanValue()){
		
			if(dateFechaInicioActuacion!=null){
				if (Integer.parseInt(GstDate.getYear(dateFechaInicioActuacion)) < 2005) {
					 if (!isAcreditacionInicio && isAcreditacionFin) {
						//creamos una actuacion de inicio
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.fin.antes2005");
						acreditacion = "codigo.general.scsacreditacion.fin.antes2005";
	
					} else{
						throw new ClsExceptions("Algo Falla.No deberia venir!!");
					}
				} else {
					 if (!isAcreditacionInicio && isAcreditacionFin) {
						//creamos una actuacion de inicio
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.fin.despues2005");
						acreditacion = "codigo.general.scsacreditacion.fin.despues2005";
	
					} else {
						throw new ClsExceptions("Algo Falla.No deberia venir!!");
					}
	
				}
				
			
			}else{
				if (Integer.parseInt(GstDate.getYear(fechaJustificacion)) < 2005) {
					if (isAcreditacionInicio && !isAcreditacionFin) {
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.inicio.antes2005");
						acreditacion = "codigo.general.scsacreditacion.inicio.antes2005";
	
					} else if (!isAcreditacionInicio && isAcreditacionFin) {
						//creamos una actuacion de inicio
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.fin.antes2005");
						acreditacion = "codigo.general.scsacreditacion.fin.antes2005";
	
					} else if (!isAcreditacionInicio && !isAcreditacionFin) {
						throw new ClsExceptions("No deberia venir!!");
					}
				} else {
					if (isAcreditacionInicio && !isAcreditacionFin) {
						//Creamos una actuacion de fin
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.inicio.despues2005");
						acreditacion = "codigo.general.scsacreditacion.inicio.despues2005";
	
					} else if (!isAcreditacionInicio && isAcreditacionFin) {
						//creamos una actuacion de inicio
						idAcreditacion = rp3
								.returnProperty("codigo.general.scsacreditacion.fin.despues2005");
						acreditacion = "codigo.general.scsacreditacion.fin.despues2005";
	
					} else if (!isAcreditacionInicio && !isAcreditacionFin) {
						throw new ClsExceptions("No deberia venir!!");
					}
	
				}
			
			}
		}else{
			if (isAcreditacionInicio && !isAcreditacionFin) {
				//Creamos una actuacion de fin
				idAcreditacion = rp3
						.returnProperty("codigo.general.scsacreditacion.inicio.despues2005");
				acreditacion = "codigo.general.scsacreditacion.inicio.despues2005";

			} else if (!isAcreditacionInicio && isAcreditacionFin) {
				//creamos una actuacion de inicio
				idAcreditacion = rp3
						.returnProperty("codigo.general.scsacreditacion.fin.despues2005");
				acreditacion = "codigo.general.scsacreditacion.fin.despues2005";

			} else if (!isAcreditacionInicio && !isAcreditacionFin) {
				throw new ClsExceptions("No deberia venir!!");
			}	
		}
		
		ScsAcreditacionProcedimientoAdm scsAcProc = new ScsAcreditacionProcedimientoAdm(usrBean);
		Hashtable htPKScsAcredProc = new Hashtable();
		String idInstitucion = usrBean.getLocation ();
		String keyAcumulaAcreditaciones = idInstitucion+"||"+idProcedimiento+"||"+idAcreditacion;
		htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDINSTITUCION,idInstitucion);
		htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO,idProcedimiento);
		htPKScsAcredProc.put(ScsAcreditacionProcedimientoBean.C_IDACREDITACION,idAcreditacion);
		Vector vLista = null;
		if(htAcumulaAcreditacionesProcedimiento.containsKey(keyAcumulaAcreditaciones))
			vLista = (Vector)htAcumulaAcreditacionesProcedimiento.get(keyAcumulaAcreditaciones);
		else
			vLista = scsAcProc.selectByPK(htPKScsAcredProc);
		if(vLista == null)
			vLista = new Vector();
		htAcumulaAcreditacionesProcedimiento.put(keyAcumulaAcreditaciones, vLista);
		if(vLista==null || vLista.size()==0){
			throw new ClsExceptions("Error no existe la acreditacion:"+acreditacion+":"+idAcreditacion+" del procedimineto:"+idProcedimiento);
		}
		
		
		
		
		return idAcreditacion;

	}
	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		InformeJustificacionMasivaForm f = (InformeJustificacionMasivaForm) formulario;
		ScsDesignasLetradoAdm admDesignas = new ScsDesignasLetradoAdm(this
				.getUserBean(request));
		//Vector v = adm.getDesignasLetrado(this.getIDInstitucion(request),
		// f.getLetrado(), f.getFecha());
		
		/*Vector v = admDesignas.getDesignasLetradoNew(this.getIDInstitucion(request), f
				.getLetrado(), f.getFechaDesde(), f.getFechaHasta(), f
				.getMostrarTodas(),f.getInteresadoNombre(),f.getInteresadoApellidos(),false);*/
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
		//Vector vRowsInformePorPersona = (Vector) htPersonas.get(keyPersona);
		request.setAttribute("resultado", vRowsInformePorPersona);
		request.setAttribute("letrado", f.getLetrado());

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

		return "resultado";
	}
	

	

}