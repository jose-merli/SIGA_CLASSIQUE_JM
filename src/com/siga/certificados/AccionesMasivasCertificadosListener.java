package com.siga.certificados;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.CerEnvios;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificados;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudesAccion;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosAccionService;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosEnviosService;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.LogFileWriter;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CerEstadoSoliCertifiAdm;
import com.siga.beans.CerPlantillasAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.certificados.action.SIGASolicitudesCertificadosAction;
import com.siga.envios.action.DefinirEnviosAction;
import com.siga.facturacion.Facturacion;
import com.siga.general.SIGAException;
import com.siga.general.SIGAListenerPorMinutosAbstract;

import es.satec.businessManager.BusinessManager;

public class AccionesMasivasCertificadosListener extends SIGAListenerPorMinutosAbstract 
{	
	@Override
	protected void execute(Short idInstitucion)
	{
		try {
			procesoMasivo();
			envioMasivo();
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al ejecutar el demonio de acciones masivas", e, 3);
		}
	}

	@Override
	protected PARAMETRO getActivoParam()
	{
		return PARAMETRO.CER_ACCIONES_MASIVAS_ACTIVO;
	}

	@Override
	protected PARAMETRO getMinutosIntervaloParam()
	{
		return PARAMETRO.CER_ACCIONES_MASIVAS_INTERVALO_MINUTOS;
	}

	@Override
	protected MODULO getModulo()
	{
		return MODULO.CER;
	}
	
	
	public void procesoMasivo(){
		
		
		 CerSolicitudCertificadosAccionService  cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance().getService(CerSolicitudCertificadosAccionService.class);
		 CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
		 List <CerSolicitudesAccion> listaCerSolicitudesAccion = new ArrayList<CerSolicitudesAccion>();
		 CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados ();
		 CerSolicitudesAccion cerSolicitudesAccion = new CerSolicitudesAccion ();
	      HttpServletRequest request = null;
		
		
		 //Obtenemos la información de los certificados sobre los que se tiene que realizar una acción.
		 listaCerSolicitudesAccion = cerSolicitudCertificadosAccionService.obtenerListadoCertificadoAcciones();
		 if(listaCerSolicitudesAccion != null && listaCerSolicitudesAccion.size()>0){
				Iterator iter = listaCerSolicitudesAccion.iterator();
				while (iter.hasNext()) {
					//Obtenemos la accion
					cerSolicitudesAccion = (CerSolicitudesAccion)iter.next();
					cerSolicitudcertificados =cerSolicitudCertificadosService.obtenerCertificados(cerSolicitudesAccion.getIdinstitucion(),cerSolicitudesAccion.getIdsolicitud());
					//Obtenemos la información del certificado 
					switch (cerSolicitudesAccion.getAccion().intValue()) {
				        case 1:
				        	aprobarYgenerar(cerSolicitudcertificados);
				        break;
				 
				        case 2:
				             finalizar (cerSolicitudcertificados);
				        break;
				        case 3:
				        	 facturar (cerSolicitudcertificados,cerSolicitudesAccion.getIdseriefacturacionseleccionada(),request);
				        break;   	
					}
					
					cerSolicitudesAccion = new CerSolicitudesAccion ();
				}
				  
		 } else {
		    	ClsLogging.writeFileLog("NO HAY SOLICITUDES PARA APROBAR Y GENERAR",4);
		}	
	}
	
	/**
	 * Método encargado de realizar la aprobación y generación de un certificado
	 * 
	 * @param cerSolicitudcertificados
	 */
	private void aprobarYgenerar(CerSolicitudcertificados cerSolicitudcertificados)
	{

		CerSolicitudCertificadosAccionService cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance()
				.getService(CerSolicitudCertificadosAccionService.class);
		CerSolicitudCertificadosService cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(
				CerSolicitudCertificadosService.class);
		UsrBean usr = null;
		CerPlantillasAdm admPlantillas = null;
		LogFileWriter log = null;

		// Obtenemos el UsrBean
		usr = new UsrBean(String.valueOf(cerSolicitudcertificados.getUsumodificacion()), String.valueOf(cerSolicitudcertificados.getIdinstitucion()), "1");
		GenParametrosAdm parametrosAdm = new GenParametrosAdm(usr);

		try {

			admPlantillas = new CerPlantillasAdm(usr);
			// Construimos el nombre de la solicitud
			String nombreSolicitud = "[Institucion:" + cerSolicitudcertificados.getIdinstitucion() + "][Solicitud:" + cerSolicitudcertificados.getIdsolicitud()
					+ "][fecha:" + cerSolicitudcertificados.getFechasolicitud() + "]";

			// Obtenemos el CerSolicitudCertificadosBean a partir del cerSolicitudcertificados
			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, cerSolicitudcertificados.getIdinstitucion());
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, cerSolicitudcertificados.getIdsolicitud());
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			// Creamos el log de error
			log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(beanSolicitud), cerSolicitudcertificados.getIdsolicitud() + "-LogError");
			log.clear();

			// Obetenemos el id de plantilla de cada uno de los certificados
			String idPlantilla = admPlantillas.obtenerPlantillaDefecto(String.valueOf(cerSolicitudcertificados.getIdinstitucion()),
					String.valueOf(cerSolicitudcertificados.getPpnIdtipoproducto()), String.valueOf(cerSolicitudcertificados.getPpnIdproducto()),
					String.valueOf(cerSolicitudcertificados.getPpnIdproductoinstitucion()));

			// Obtenemos los valores de colegiadoEnOrigen, usarIdInstitucion, beanProd
			HashMap<String, Object> listadoColegiado = SIGASolicitudesCertificadosAction.obtenerPersonaCertificado(usr,
					String.valueOf(cerSolicitudcertificados.getIdinstitucion()), String.valueOf(cerSolicitudcertificados.getPpnIdtipoproducto()),
					String.valueOf(cerSolicitudcertificados.getPpnIdproducto()), String.valueOf(cerSolicitudcertificados.getPpnIdproductoinstitucion()),
					String.valueOf(cerSolicitudcertificados.getIdsolicitud()));

			if (beanSolicitud.getIdEstadoSolicitudCertificado() != CerEstadoSoliCertifiAdm.C_ESTADO_SOL_DENEGADO
					&& beanSolicitud.getIdEstadoSolicitudCertificado() != CerEstadoSoliCertifiAdm.C_ESTADO_SOL_ANULADO) {

				if ((Boolean) listadoColegiado.get("colegiadoEnOrigen")) {
					// Para los certificados normales, este valor siempre es verdadero
					// Para las comunicaciones/diligencias, entra por aqui cuando ES colegiadoEnOrigen

					GenParametrosAdm admParametros = new GenParametrosAdm(usr);
					HashMap<String, Object> listaDeObjetos = SIGASolicitudesCertificadosAction.obtenerPathBD(admParametros,
							String.valueOf(cerSolicitudcertificados.getIdinstitucion()), String.valueOf(cerSolicitudcertificados.getIdsolicitud()));

					try {
						if (! (SIGASolicitudesCertificadosAction.comprobarCompatibilidadNuevoCertificado(usr, beanSolicitud) || beanSolicitud.getIdMotivoSolicitud() != null)) {
							throw new SIGAException("certificados.solicitudes.mensaje.certificadoIncompatible");
						}
						
						// comprobando que la fecha de solicitud es anterior al dia de hoy
						Date fechaSolicitudReal = UtilidadesFecha.getDate(beanSolicitud.getFechaSolicitud(), ClsConstants.DATE_FORMAT_JAVA);
						Date fechaHoy = UtilidadesFecha.getToday();
						if (fechaSolicitudReal.after(fechaHoy)) {
							throw new SIGAException("certificados.solicitudes.mensaje.fechaSolicitudFutura");
						}
						// comprobando limite de fecha de solicitud
						int maximoDiasAntelacionSolicitud = Integer.parseInt(parametrosAdm.getValor(
								String.valueOf(ClsConstants.INSTITUCION_CGAE),
								ClsConstants.MODULO_CERTIFICADOS, 
								"MAXIMO_DIAS_ANTELACION_SOLICITUD", 
								"365"));
						Date fechaLimiteSolicitud = UtilidadesFecha.subDays(fechaHoy, maximoDiasAntelacionSolicitud);
						if (fechaSolicitudReal.before(fechaLimiteSolicitud)) {
							String [] parametrosMensaje = new String[1];
							parametrosMensaje[0] = Integer.toString(maximoDiasAntelacionSolicitud);
							throw new SIGAException(UtilidadesString.getMensaje("certificados.solicitudes.mensaje.fechaSolicitudFueraDePlazo", parametrosMensaje, usr.getLanguage()));
						}

						
						// Se realiza el proceso de aprobar y generar o de sólamente generar
						SIGASolicitudesCertificadosAction.almacenarCertificado(String.valueOf(cerSolicitudcertificados.getIdinstitucion()),
								String.valueOf(cerSolicitudcertificados.getIdsolicitud()), usr, (PysProductosInstitucionBean) listadoColegiado.get("beanProd"),
								idPlantilla, (Boolean) listadoColegiado.get("usarIdInstitucion"), listaDeObjetos);

					} catch (Exception e) {
						ClsLogging.writeFileLogError(
								"ERROR EN APROBAR Y GENERAR PDF MASIVO. SOLICITUD:" + nombreSolicitud + " Error:"
										+ ((SIGAException) e).getLiteral(usr.getLanguage()), e, 3);
						log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
								((SIGAException) e).getLiteral(usr.getLanguage()) });
						/** Escribiendo fichero de log **/
						if (log != null)
							log.flush();

					}

					((File) listaDeObjetos.get("fIn")).delete(); // comprobar si se podría borrar esta linea ya que los ficheros temporales de obtenerPathBD se
																	// deberian haber autoborrado ya o cuando termine el proceso
				} else {

					// Para las comunicaciones/diligencias, entra por aqui cuando NO ES colegiadoEnOrigen
					ClsLogging.writeFileLog("ERROR EN APROBAR Y GENERAR PDF MASIVO. NO ES colegiadoEnOrigen:" + nombreSolicitud
							+ " Error: El cliente no es colegiadoEnOrigen, idpersona=" + String.valueOf(cerSolicitudcertificados.getIdpersonaDes()), 3);
					log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error: El cliente no es colegiadoEnOrigen" });
					/** Escribiendo fichero de log **/
					if (log != null)
						log.flush();

				}
			} else {
				ClsLogging.writeFileLog("----- ERROR APROBAR Y GENERAR PDF -----", 4);
				CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(usr);
				log.addLog(new String[] {
						new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
						"El certificado está " + estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))
								+ ": no es " + "posible procesarlo. Tendrá que crear otro nuevo certificado si es necesario." });
				/** Escribiendo fichero de log **/
				if (log != null)
					log.flush();

			}

		} catch (Exception e) {
			ClsLogging.writeFileLog("-----ERROR APROBAR Y GENERAR PDF  -----", 4);
			e.printStackTrace();
		} finally {
			// Si se produce fallo volvemos a ponerle en el estado anterior al producirse un fallo
			List<CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			cerSolicitudCertificadosService.updateMasivoCasoDeError(listaCerSolicitudcertificados);

			// Una vez realizada la acción se da de baja en base de datos.
			cerSolicitudCertificadosAccionService.darDeBajaAccionMasiva(String.valueOf(cerSolicitudcertificados.getIdinstitucion()),
					String.valueOf(cerSolicitudcertificados.getIdsolicitud()), CerSolicitudCertificadosAdm.A_ABROBAR_GENERAR, usr.getUserName());
		}

	}
	
	/**
	 * Método encargado de realizar la finalización
	 * @param cerSolicitudcertificados
	 */
	
	private void finalizar(CerSolicitudcertificados cerSolicitudcertificados)
	{

		LogFileWriter log = null;
		CerSolicitudCertificadosAccionService cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance()
				.getService(CerSolicitudCertificadosAccionService.class);
		CerSolicitudCertificadosService cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(
				CerSolicitudCertificadosService.class);
		UsrBean usr = null;
		// Obtenemos el UsrBean
		usr = new UsrBean(String.valueOf(cerSolicitudcertificados.getUsumodificacion()), String.valueOf(cerSolicitudcertificados.getIdinstitucion()), "1");
		try {

			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, String.valueOf(cerSolicitudcertificados.getIdinstitucion()));
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, String.valueOf(cerSolicitudcertificados.getIdsolicitud()));

			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

			// Creamos la ruta del log de error
			String formattedDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(cerSolicitudcertificados.getFechasolicitud());
			beanSolicitud.setFechaSolicitud(formattedDate);
			log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(beanSolicitud), cerSolicitudcertificados.getIdsolicitud() + "-LogError");
			log.clear();

			if (beanSolicitud.getIdEstadoSolicitudCertificado() != CerEstadoSoliCertifiAdm.C_ESTADO_SOL_DENEGADO
					&& beanSolicitud.getIdEstadoSolicitudCertificado() != CerEstadoSoliCertifiAdm.C_ESTADO_SOL_ANULADO) {

				// LLamamos al método finalizar
				SIGASolicitudesCertificadosAction.finalizarCertificado(htSolicitud, null, null, 0, usr);

			} else {
				ClsLogging.writeFileLog("----- ERROR AL FINALIZAR PDF -----", 4);
				CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(usr);
				log.addLog(new String[] {
						new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
						"El certificado está " + estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))
								+ ": no es " + "posible procesarlo. Tendrá que crear otro nuevo certificado si es necesario." });
				if (log != null)
					log.flush();

			}

		} catch (Exception e) {
			try {
				log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), ((SIGAException) e).getLiteral(usr.getLanguage()) });
				/** Escribiendo fichero de log **/
				if (log != null)
					log.flush();
				ClsLogging.writeFileLog("-----ERROR AL FINALIZAR PDF: Institución:" + cerSolicitudcertificados.getIdinstitucion() + ",idSolicitud:"
						+ cerSolicitudcertificados.getIdsolicitud(), 4);

			} catch (SIGAException e1) {
				e1.printStackTrace();
			}

		} finally {
			// Volvemos a ponerle en el estado anterior, solo en el caso de producirse un fallo
			List<CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			cerSolicitudCertificadosService.updateMasivoCasoDeError(listaCerSolicitudcertificados);

			// Una vez realizada la acción se da de baja en base de datos.
			cerSolicitudCertificadosAccionService.darDeBajaAccionMasiva(String.valueOf(cerSolicitudcertificados.getIdinstitucion()),
					String.valueOf(cerSolicitudcertificados.getIdsolicitud()), CerSolicitudCertificadosAdm.A_FINALIZAR, usr.getUserName());
		}
	}

	private void facturar (CerSolicitudcertificados cerSolicitudcertificados, String idSerieFacturacionSeleccionada,HttpServletRequest request){
		LogFileWriter log = null;
		CerSolicitudCertificadosAccionService  cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance().getService(CerSolicitudCertificadosAccionService.class);
		CerSolicitudCertificadosService cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(
					CerSolicitudCertificadosService.class);
		 UsrBean usr = null;
		//Obtenemos el UsrBean 
		 usr = new UsrBean(String.valueOf(cerSolicitudcertificados.getUsumodificacion()), 
					String.valueOf(cerSolicitudcertificados.getIdinstitucion()),"1");
		 try{
				String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};
				String[] campos = {CerSolicitudCertificadosBean.C_FECHAESTADO,CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
			  	CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			  	FacFacturaAdm admFactura = new FacFacturaAdm(usr);	
				
				//Creamos el log
				CerSolicitudCertificadosBean cerSolicitudCertificadosBean = new CerSolicitudCertificadosBean();
				cerSolicitudCertificadosBean.setIdSolicitud(cerSolicitudcertificados.getIdsolicitud());
				cerSolicitudCertificadosBean.setIdInstitucion(cerSolicitudcertificados.getIdinstitucion().intValue());
				String formattedDate = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(cerSolicitudcertificados.getFechasolicitud());
				cerSolicitudCertificadosBean.setFechaSolicitud(formattedDate);
				log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(cerSolicitudCertificadosBean),cerSolicitudcertificados.getIdsolicitud()+"-LogError");
				log.clear();
				
				
				 Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
				 htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, String.valueOf(cerSolicitudcertificados.getIdinstitucion()));
				 htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, String.valueOf(cerSolicitudcertificados.getIdsolicitud()));
				 
				 Vector vDatos = admSolicitud.selectByPK(htSolicitud);
				 CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
				  Hashtable<String,Object> htNew = beanSolicitud.getOriginalHash();	
				 Facturacion facturacion = new Facturacion(usr);
				 //Si el estado es facturando se realizará la facturación, se comprueba antes porque en el método facturación rápida no se realiza
				 if(cerSolicitudcertificados.getIdestadosolicitudcertificado().equals(Short.valueOf(""+CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FACTURANDO)) || 
					cerSolicitudcertificados.getIdestadosolicitudcertificado().equals(Short.valueOf(""+CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR))) {
					 try{
						 Vector<Hashtable<String,Object>> vFacturas = admFactura.obtenerFacturasFacturacionRapida(String.valueOf(cerSolicitudcertificados.getIdinstitucion()), null, String.valueOf(cerSolicitudcertificados.getIdsolicitud()));	
			    		 if (vFacturas==null ||  vFacturas.size()==0) { // No esta facturado 
							 facturacion.facturacionRapidaProductosCertificados(String.valueOf(cerSolicitudcertificados.getIdinstitucion()), null, idSerieFacturacionSeleccionada,String.valueOf(cerSolicitudcertificados.getIdsolicitud()), request);
							//Pasamos a facturado
							    htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
						    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO);
						    	admSolicitud.updateDirect(htNew, claves, campos );
			    		 }else{
			    			 //Está facturado, se comprueba si la factura está en revisión o no
				    			// Obtengo los datos de la factura
					        	Hashtable<String,Object> hFactura = vFacturas.get(0);
					        	String estadoFacturacion=UtilidadesHash.getString(hFactura, FacFacturaBean.C_ESTADO);
					        	if(estadoFacturacion != null && !"".equalsIgnoreCase(estadoFacturacion)){
					        		if(Integer.parseInt(estadoFacturacion) != 7){
					        			//Pasamos a finalizado, la factura está confirmada
								    	htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
								    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO);
								    	admSolicitud.updateDirect(htNew, claves, campos );
					        		}else{
					        			//La factura está en revisión introducimos mensaje 
					        			log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado ya está facturado, pero la factura está En revisión. "+
					        						"Si quiere continuar con la facturación rápida de este certificado, hay que eliminar previamente la facturación de Certificados " +
					        						"NO confirmada en Facturación > Mantenimiento Facturación."});
					        			  /** Escribiendo fichero de log **/
										if (log != null)
											log.flush();
										
										String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
							    		htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR);
							    		admSolicitud.updateDirect(htNew, claves, camposAux );
					        		}
					        		
					        	}
			    		 }
						}catch(SIGAException se){
				    		if((se.getErrorCode() != null && !"".equalsIgnoreCase(se.getErrorCode())) && (se.getErrorCode().equalsIgnoreCase("1")||se.getErrorCode().equalsIgnoreCase("2"))){
								String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO);
								admSolicitud.updateDirect(htNew, claves, camposAux);
								log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), (se.getLiteral(usr.getLanguage()))});
							
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();
								
							}else{
								String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR);
								admSolicitud.updateDirect(htNew, claves, camposAux);
								
								log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), (se.getLiteral(usr.getLanguage()))});
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();

							}
				    	}catch(ClsExceptions se){
				    		if((se.getErrorCode() != null && !"".equalsIgnoreCase(se.getErrorCode())) && (se.getErrorCode().equalsIgnoreCase("1")||se.getErrorCode().equalsIgnoreCase("2"))){
								String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO);
								admSolicitud.updateDirect(htNew, claves, camposAux);
								log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), se.getMessage()});
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();
								
							}else{
								String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, ""+ CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND_FACTURAR);
								admSolicitud.updateDirect(htNew, claves, camposAux);
								
								log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), se.getMessage()});
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();
								
							}
				    	}
				 }else{
					 CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(usr);
						switch(beanSolicitud.getIdEstadoSolicitudCertificado()){
						case 1:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": es " +
				    				"necesario aprobarlo y generarlo, y finalizarlo antes de facturar."});
				
						case 4:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": ya " +
				    				"no se puede facturar."});
						case 7:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
				    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
						
						case 11:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
				    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
						
						case 8:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
									"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
						
						case 12:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
				    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
							
						case 9:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
				    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
							
						case 13:
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
				    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
					if (log != null)
						log.flush();
				 }
			}
				    
		}catch (Exception e) { 
			 try {
				log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error en la facturación del certificado"});
				
				  /** Escribiendo fichero de log **/
				if (log != null)
					log.flush();
				
				ClsLogging.writeFileLog("-----ERROR AL FACTURAR PDF: Institución:"+cerSolicitudcertificados.getIdinstitucion()
						+",idSolicitud:"+ cerSolicitudcertificados.getIdsolicitud(),4);
				
			} catch (SIGAException e1) {
				e1.printStackTrace();
			}
		}finally{
			// Volvemos a ponerle en el estado anterior, solo en el caso de producirse un fallo
			List<CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			cerSolicitudCertificadosService.updateMasivoCasoDeError(listaCerSolicitudcertificados);

			// Una vez realizada la acción se da de baja en base de datos.
	        cerSolicitudCertificadosAccionService.darDeBajaAccionMasiva(String.valueOf(cerSolicitudcertificados.getIdinstitucion()), String.valueOf(cerSolicitudcertificados.getIdsolicitud()), 
	        		CerSolicitudCertificadosAdm.A_FACTURAR, usr.getUserName());
		}
	}
	
	public void envioMasivo(){
		//Obtenemos las peticiones agrupadas por idPeticionesDeEnvio
		//Cuando es más de 20 se ejecuta el proceso automático cada min
		CerSolicitudCertificadosEnviosService cerSolicitudCertificadosEnviosService = (CerSolicitudCertificadosEnviosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosEnviosService.class);
		List<CerEnvios> listaCerEnvios=cerSolicitudCertificadosEnviosService.obtenerEnvios();
		List<CerEnvios> listaCerEnviosAux;
		String [] lineas = null;
		if(listaCerEnvios.size() >0){
			for(int i =0; i<listaCerEnvios.size();i++){
				listaCerEnviosAux = new ArrayList<CerEnvios>();
				CerEnvios CerEnviosAux = listaCerEnvios.get(i);
				listaCerEnviosAux.add(CerEnviosAux);
				listaCerEnvios.remove(i);
				
				Iterator<CerEnvios>nombreIterator = listaCerEnvios.iterator();
				while(nombreIterator.hasNext()){
					CerEnvios elemento = nombreIterator.next();
					if(elemento.getIdpeticiondeenvio().equals(CerEnviosAux.getIdpeticiondeenvio())){
						listaCerEnviosAux.add(elemento);
						nombreIterator.remove();	
					}
				}
				//A continuación realizamos el envio
				if (listaCerEnviosAux.size()>0){
					 UsrBean usr = null;
					//Obtenemos el UsrBean 
					 usr = new UsrBean(String.valueOf(listaCerEnviosAux.get(0).getUsumodificacion()), 
								String.valueOf(listaCerEnviosAux.get(0).getIdinstitucion()),"1");
					 lineas= new String[listaCerEnviosAux.size()];
					//Creamos el array de String lineas
					 int j = 0;
					 Iterator<CerEnvios>listaCerEnviosAuxIterator = listaCerEnviosAux.iterator();
						while(listaCerEnviosAuxIterator.hasNext()){
							CerEnvios elemento = listaCerEnviosAuxIterator.next();
							String cadenaTexto = elemento.getIdinstitucion()+"||"+elemento.getIdsolicitud();
							lineas[j]=cadenaTexto;
							j++;
						}
						 
						 
					try {
						String plantilla;
						String fechaProg;
						if(listaCerEnviosAux.get(0).getPlantilla() != null){
							plantilla = String.valueOf(listaCerEnviosAux.get(0).getPlantilla());
						}else{
							plantilla = null;
						}
						if(listaCerEnviosAux.get(0).getFechaprogramada() != null){
							DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
							fechaProg = df.format(listaCerEnviosAux.get(0).getFechaprogramada());
						}else{
							fechaProg = null;
						}
						//Insertamos los envios
						DefinirEnviosAction.insertarEnvioModalMasivoCertificado(String.valueOf(listaCerEnviosAux.get(0).getTipoenvio()),String.valueOf(listaCerEnviosAux.get(0).getTipoplantilla()), 
								plantilla , String.valueOf(listaCerEnviosAux.get(0).getAcuselectura()), 
								fechaProg,  lineas, 
								String.valueOf(listaCerEnviosAux.get(0).getCertificados()), String.valueOf(listaCerEnviosAux.get(0).getFacturas()) , usr, null, 
								String.valueOf(listaCerEnviosAux.get(0).getDestinatario()), String.valueOf(listaCerEnviosAux.get(0).getAsunto()),"SI");
					
					} catch (SIGAException e) {
						e.printStackTrace();
					}
					
					//Una vez enviados borramos la lista
					cerSolicitudCertificadosEnviosService.darBajaEnvios(listaCerEnviosAux.get(0).getIdpeticiondeenvio());
					
				}
				i=0;
				
			}		
			
		}else{
			ClsLogging.writeFileLog("NO HAY SOLICITUDES PARA ENVIAR",4);
		}
		
	}
}
