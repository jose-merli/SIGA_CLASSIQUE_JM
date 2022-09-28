package com.siga.gratuita.form;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.scs.GestionEnvioInformacionEconomicaCatalunyaService;
import org.redabogacia.sigaservices.app.vo.scs.GestionEconomicaCatalunyaVo;

import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

/***
 * 
 * @author jorgeta
 * @date 08/03/2018
 *
 *       La imaginación es más importante que el conocimiento
 *
 */
public class GestionEconomicaCatalunyaForm extends MasterForm {
	private static final Logger log = Logger.getLogger(GestionEconomicaCatalunyaForm.class);
	private static final long serialVersionUID = 1L;
	String idTipoIntercambio = null;
	String descripcionTipoIntercambio = null;
	String descripcion = null;
	String idPeriodo = null;
	String anio;
	String idIntercambio;
	String idJustificacion;
	
	String idDevolucion = null;
	String idCertificacion ;
	String idCertificacionAnexo;
	String idTipoCertificacion;
	

	String idInstitucion = null;
	String idEstado;
	String descripcionEstado;
	String descripcionEstadoIca;
	String descripcionEstadoConsell;
	String fechaDesde;
	String fechaCreacion;
	String fechaHasta;
	String nombrePeriodo;
	Integer usuModificacion;
	String abreviaturaInstitucion;
	String error;
	UsrBean usrBean = null;
	String seleccion;
	List<JustificacionCatalunyaForm> datosJustificacion;
	private FormFile theFile;
	String pathFile;
	String idColegio;
	String cantidadAsunto;
	String	importeAsunto;
	String	importeDevoluciones;
	String	valorInteres;
	String	importeFinal;
	String	importeAnticipo;
	String	acumuladoTrimetreActual;
	String	acumuladoTrimetreAnterior;
	
	
	
	
	
	
	
	
	

	public String getCantidadAsunto() {
		return cantidadAsunto;
	}

	public void setCantidadAsunto(String cantidadAsunto) {
		this.cantidadAsunto = cantidadAsunto;
	}

	public String getImporteAsunto() {
		return importeAsunto;
	}

	public void setImporteAsunto(String importeAsunto) {
		this.importeAsunto = importeAsunto;
	}

	public String getImporteDevoluciones() {
		return importeDevoluciones;
	}

	public void setImporteDevoluciones(String importeDevoluciones) {
		this.importeDevoluciones = importeDevoluciones;
	}

	public String getValorInteres() {
		return valorInteres;
	}

	public void setValorInteres(String valorInteres) {
		this.valorInteres = valorInteres;
	}

	public String getImporteFinal() {
		return importeFinal;
	}

	public void setImporteFinal(String importeFinal) {
		this.importeFinal = importeFinal;
	}

	public String getImporteAnticipo() {
		return importeAnticipo;
	}

	public void setImporteAnticipo(String importeAnticipo) {
		this.importeAnticipo = importeAnticipo;
	}

	public String getAcumuladoTrimetreActual() {
		return acumuladoTrimetreActual;
	}

	public void setAcumuladoTrimetreActual(String acumuladoTrimetreActual) {
		this.acumuladoTrimetreActual = acumuladoTrimetreActual;
	}

	public String getAcumuladoTrimetreAnterior() {
		return acumuladoTrimetreAnterior;
	}

	public void setAcumuladoTrimetreAnterior(String acumuladoTrimetreAnterior) {
		this.acumuladoTrimetreAnterior = acumuladoTrimetreAnterior;
	}

	public final String getIdColegio() {
		return idColegio;
	}

	public final void setIdColegio(String idColegio) {
		this.idColegio = idColegio;
	}

	
	public final String getPathFile() {
		return pathFile;
	}

	public final void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public final List<JustificacionCatalunyaForm> getDatosJustificacion() {
		return datosJustificacion;
	}

	public final void setDatosJustificacion(List<JustificacionCatalunyaForm> datosJustificacion) {
		this.datosJustificacion = datosJustificacion;
	}

	public Integer getUsuModificacion() {
		return usuModificacion;
	}

	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}

	
	public String getNombrePeriodo() {
		return nombrePeriodo;
	}

	public void setNombrePeriodo(String nombrePeriodo) {
		this.nombrePeriodo = nombrePeriodo;
	}

	
	FilaExtElement[] elementosFila;

	public FilaExtElement[] getElementosFila() {

		//		ESTADOS JUSTIFICACION
		//		INICIAL((short)10,"Inicial"),
		//		VALIDANDO((short)12,"Validando"),
		//		VALIDADO_CORRECTO((short)14,"Validado correcto"),
		//		VALIDADO_ERRONEO((short)16,"Validado erróneo"),
		//		ENVIANDO_CICAC((short)20,"Enviando ICA - CICAC..."),
		//		ENVIADO_CICAC((short)30,"Enviado CICAC"),
		//		ERROR_CICAC((short)40,"Error CICAC"),
		//		FIN_CICAC((short)50,"Fin CICAC"),
		//		DEVUELTO_CICAC_ICA_ERRONEO((short)60,"Devuelto CICAC - ICA erróneo"),
		//		ENVIANDO_GEN_GENERANDO_XML((short)62,"Enviando CICAC - GEN. Generando xml"),
		//		ENVIANDO_GEN_MOVIENDO_XML((short)65,"Enviando CICAC - GEN. Moviendo xml"),
		//		ENVIADO_GEN((short)70,"Enviado Gen."),
		//		PROCESANDO_GEN((short)75,"Procesando Gen..."),
		//		ERROR_GEN_ANTIVIRUS((short)80,"Error GEN antivirus"),
		//		ERROR_GEN((short)85,"Error Gen."),
		//		DEVUELTO_GEN_CICAC_ERRONEO((short)87,"Devuelto GEN - CICAC erróneo"),
		//		DEVUELTO_GEN_CICAC_CORRECTO((short)90,"Devuelto GEN - CICAC correcto."),
		//		RESPONDIENDO_ICA((short)92,"Respondiendo ICA..."),
		//		DEVUELTO_ICA_CORRECTO((short)95,"Devuelto CICAC - ICA correcto");


		if(getIdTipoIntercambio()==null) {
			
			
			elementosFila = new FilaExtElement[2];
			
			switch (Integer.valueOf(idEstado)) {
			case 14:
				
				if(usrBean !=null && usrBean.getLocation() !=null && usrBean.getLocation().equals("3001")) {

					
				}else {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement("enviar","enviaIntercambiosCICAC", SIGAConstants.ACCESS_FULL);
				}
				break;
				
			case 50:
				if(usrBean !=null && usrBean.getLocation() !=null && usrBean.getLocation().equals("3001")) {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement("enviar","enviaIntercambiosGEN", SIGAConstants.ACCESS_FULL);
					//elementosFila[0] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
//					elementosFila[1] = new FilaExtElement("enviar","enviaIntercambiosGEN", SIGAConstants.ACCESS_FULL);
					
				}else {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
				}
				break;
			case 70:
				if(usrBean !=null && usrBean.getLocation() !=null && usrBean.getLocation().equals("3001")) {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
//					elementosFila[0] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					
				}else {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
				}
				break;
				
			case 87:
				if(usrBean !=null && usrBean.getLocation () !=null && usrBean.getLocation().equals("3001")) {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement("responderko","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
					
				}
				break;
			case 90:
				
				if(usrBean !=null && usrBean.getLocation () !=null && usrBean.getLocation().equals("3001")) {
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					if(!getIdInstitucion().equals("3001"))
						elementosFila[1] = new FilaExtElement("responderok","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
					
				}
				break;
			case 30:
				elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
				if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {

					elementosFila[1] = new FilaExtElement("enviar","enviaIntercambiosGEN", SIGAConstants.ACCESS_FULL);
				}
				break;
			default:
				elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
				break;
			}

		}else {
			GestionEnvioInformacionEconomicaCatalunyaService.TIPOINTERCAMBIO tipo = GestionEnvioInformacionEconomicaCatalunyaService.TIPOINTERCAMBIO.getEnum(getIdTipoIntercambio());
			log.info("tipo"+tipo);
//			log.info("idEstado"+idEstado);
			elementosFila = new FilaExtElement[0];
			switch (tipo) {


			case Justificaciones:
				switch (Integer.valueOf(idEstado)) {
				case 10:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 12:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 14:
					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					// elementosFila[1] = new FilaExtElement( "enviardenuevo","revalida", SIGAConstants.ACCESS_FULL);
					break;

				case 16:
					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "enviardenuevo","revalida", SIGAConstants.ACCESS_FULL);
					elementosFila[2] = new FilaExtElement( "descargaLog","descargaLogValidacion", SIGAConstants.ACCESS_FULL);

					break;
				case 20:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 30:


					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[4];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement("upload","adjuntaFicheroError", SIGAConstants.ACCESS_FULL);
						elementosFila[3] = new FilaExtElement("denegar","insertaErrorGlobal", SIGAConstants.ACCESS_FULL);

					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);

					}
					break;
				case 40:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[5];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement("upload","adjuntaFicheroError", SIGAConstants.ACCESS_FULL);
						elementosFila[3] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
						elementosFila[4] = new FilaExtElement( "finalizar","finalizaErrores", SIGAConstants.ACCESS_FULL);

					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download", "descarga",SIGAConstants.ACCESS_NONE);

					}

					break;
				case 50:

					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_NONE);
					elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);

					break;

				case 60:
					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_NONE);
					elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
					break;
				case 70:
					elementosFila = new FilaExtElement[2];
					elementosFila[0] = new FilaExtElement( "consultar","consulta", SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					break;

				case 87:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
						//						elementosFila[2] = new FilaExtElement("enviar","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);

					}
					break;
				case 90:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
						//						elementosFila[2] = new FilaExtElement("enviar","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					}
					break;
				case 95:
					elementosFila = new FilaExtElement[2];
					elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					break;

				default:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					break;
				}
				break;
				
			case Devoluciones: case Certificaciones:	case Anexos:
				switch (Integer.valueOf(idEstado)) {
				case 10:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 12:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 14:
					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;

				case 16:
					elementosFila = new FilaExtElement[3];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "enviardenuevo","revalida", SIGAConstants.ACCESS_FULL);
					elementosFila[2] = new FilaExtElement( "descargaLog","descargaLogValidacion", SIGAConstants.ACCESS_FULL);

					break;
				case 20:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
					break;
				case 30:


					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement("denegar","insertaErrorGlobal", SIGAConstants.ACCESS_FULL);


					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar","consulta", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					}
					break;
				case 40:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					}

					break;
				case 50:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPrevia",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
//						elementosFila[2] = new FilaExtElement("enviar","enviaIntercambiosGEN", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[2];
						
						elementosFila[0] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
						//elementosFila[1] = new FilaExtElement("enviar","enviaIntercambiosGEN", SIGAConstants.ACCESS_FULL);
					}

					break;

				case 60:
					elementosFila = new FilaExtElement[2];
					elementosFila[0] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement("download","descarga", SIGAConstants.ACCESS_FULL);
					break;
				case 70:
					elementosFila = new FilaExtElement[2];
					elementosFila[0] = new FilaExtElement( "consultar","consulta", SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					break;

				case 87:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[3];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
						elementosFila[2] = new FilaExtElement( "descargaLog","descargaErrores", SIGAConstants.ACCESS_FULL);

					}
					break;
				case 90:
					if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					}else {
						elementosFila = new FilaExtElement[2];
						elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
						elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					}
					break;
				case 95:
					elementosFila = new FilaExtElement[2];
					elementosFila[0] = new FilaExtElement("consultar", "consulta",SIGAConstants.ACCESS_FULL);
					elementosFila[1] = new FilaExtElement( "download","descarga", SIGAConstants.ACCESS_FULL);
					break;

				default:
					elementosFila = new FilaExtElement[1];
					elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
					break;

				}
				


				break;
			default:
				elementosFila = new FilaExtElement[1];
				elementosFila[0] = new FilaExtElement("editar", "editaIntercambio",SIGAConstants.ACCESS_FULL);
				break;
			}
			
		}
		if(elementosFila==null) {
			log.info("Viene nulos");
			elementosFila = new FilaExtElement[0];
			
		}
		return elementosFila;
	}

	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;

	}

	public void clear() {

		this.fechaDesde = null;
		this.fechaHasta = null;
		this.idEstado = null;
		this.descripcion = null;
		this.idPeriodo = null;
		this.idInstitucion = null;
		this.descripcionEstado = null;
		this.elementosFila = null;
		
		this.anio = null;
		this.nombrePeriodo = null;

	}

	public GestionEconomicaCatalunyaForm clone() {
		GestionEconomicaCatalunyaForm miForm = new GestionEconomicaCatalunyaForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setIdColegio(this.getIdColegio());
		miForm.setIdEstado(this.getIdEstado());
		miForm.setFechaDesde(this.getFechaDesde());
		miForm.setFechaHasta(this.getFechaHasta());
		miForm.setDatosPaginador(this.getDatosPaginador());
		miForm.setAnio(this.getAnio());
		miForm.setIdPeriodo(this.getIdPeriodo());
		miForm.setDescripcion(this.getDescripcion());
		return miForm;

	}
	public String getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(String idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	
	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	public String getIdJustificacion() {
		return idJustificacion;
	}

	public void setIdJustificacion(String idJustificacion) {
		this.idJustificacion = idJustificacion;
	}
	
	public GestionEconomicaCatalunyaVo getForm2Vo(GestionEconomicaCatalunyaForm objectForm) {
		
		GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = new GestionEconomicaCatalunyaVo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getIdTipoIntercambio()!=null)
			gestionEconomicaCatalunyaVo.setIdTipoIntercambio(objectForm.getIdTipoIntercambio());
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			gestionEconomicaCatalunyaVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		if(objectForm.getFechaCreacion()!=null && !objectForm.getFechaCreacion().equals(""))
			try {
				gestionEconomicaCatalunyaVo.setFechaCreacion(sdf.parse(objectForm.getFechaCreacion()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(objectForm.getFechaDesde()!=null && !objectForm.getFechaDesde().equals(""))
			try {
				gestionEconomicaCatalunyaVo.setFechaDesde(sdf.parse(objectForm.getFechaDesde()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(objectForm.getFechaHasta()!=null && !objectForm.getFechaHasta().equals(""))
			try {
				gestionEconomicaCatalunyaVo.setFechaHasta(sdf.parse(objectForm.getFechaHasta()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(objectForm.getIdIntercambio()!=null && !objectForm.getIdIntercambio().equals(""))
			gestionEconomicaCatalunyaVo.setIdIntercambio(Long.valueOf(objectForm.getIdIntercambio()));
		if(objectForm.getIdJustificacion()!=null && !objectForm.getIdJustificacion().equals(""))
			gestionEconomicaCatalunyaVo.setIdJustificacion(Long.valueOf(objectForm.getIdJustificacion()));
		
		if(objectForm.getIdColegio()!=null && !objectForm.getIdColegio().equals(""))
			gestionEconomicaCatalunyaVo.setIdColegio(Short.valueOf(objectForm.getIdColegio()));
		
		if(objectForm.getIdDevolucion()!=null && !objectForm.getIdDevolucion().equals(""))
			gestionEconomicaCatalunyaVo.setIdDevolucion(Long.valueOf(objectForm.getIdDevolucion()));
		
		if(objectForm.getIdCertificacion()!=null && !objectForm.getIdCertificacion().equals(""))
			gestionEconomicaCatalunyaVo.setIdCertificacion(Long.valueOf(objectForm.getIdCertificacion()));
		if(objectForm.getIdCertificacionAnexo()!=null && !objectForm.getIdCertificacionAnexo().equals(""))
			gestionEconomicaCatalunyaVo.setIdCertificacionAnexo(Long.valueOf(objectForm.getIdCertificacionAnexo()));
		if(objectForm.getIdTipoCertificacion()!=null && !objectForm.getIdTipoCertificacion().equals(""))
			gestionEconomicaCatalunyaVo.setIdTipoCertificacion(Short.valueOf(objectForm.getIdTipoCertificacion()));
		
		
		if(objectForm.getIdEstado()!=null && !objectForm.getIdEstado().equals(""))
			gestionEconomicaCatalunyaVo.setIdEstado(Short.valueOf(objectForm.getIdEstado()));
		if(objectForm.getIdPeriodo()!=null && !objectForm.getIdPeriodo().equals(""))
			gestionEconomicaCatalunyaVo.setIdPeriodo(Short.valueOf(objectForm.getIdPeriodo()));
		if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
			gestionEconomicaCatalunyaVo.setAnio(Short.valueOf(objectForm.getAnio()));
		
		gestionEconomicaCatalunyaVo.setDescripcion(objectForm.getDescripcion());
		gestionEconomicaCatalunyaVo.setDescripcionTipoIntercambio(objectForm.getDescripcionTipoIntercambio());
		
		gestionEconomicaCatalunyaVo.setNombrePeriodo(objectForm.getNombrePeriodo());
		
		gestionEconomicaCatalunyaVo.setUsuModificacion(objectForm.getUsuModificacion());
		gestionEconomicaCatalunyaVo.setAbreviaturaInstitucion(objectForm.getAbreviaturaInstitucion());
		gestionEconomicaCatalunyaVo.setError(objectForm.getError());
		String seleccion = objectForm.getSeleccion();
		if(seleccion!=null) {
			String[] idLineasJustificacion = seleccion.split("##");
			gestionEconomicaCatalunyaVo.setIdLineasJustificacion(idLineasJustificacion);
		}
		try {
			if(getPathFile()!=null && !getPathFile().equals("")) {
				gestionEconomicaCatalunyaVo.setFileErrorData(SIGAServicesHelper.getBytes(getPathFile()));
			}
			if(getTheFile()!=null) {
				gestionEconomicaCatalunyaVo.setFileErrorData((getTheFile().getFileData()));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(objectForm.getImporteDevoluciones()!=null  && !objectForm.getImporteDevoluciones().equals(""))
			gestionEconomicaCatalunyaVo.setImporteDevoluciones(new BigDecimal(objectForm.getImporteDevoluciones()));
		if(objectForm.getCantidadAsunto()!=null && !objectForm.getCantidadAsunto().equals(""))
			gestionEconomicaCatalunyaVo.setCantidadAsunto(Integer.parseInt(objectForm.getCantidadAsunto()));
		if(objectForm.getImporteAsunto()!=null && !objectForm.getImporteAsunto().equals(""))
			gestionEconomicaCatalunyaVo.setImporteAsunto(new BigDecimal(objectForm.getImporteAsunto()));
		if(objectForm.getAcumuladoTrimetreActual()!=null && !objectForm.getAcumuladoTrimetreActual().equals(""))
			gestionEconomicaCatalunyaVo.setAcumuladoTrimetreActual(new BigDecimal(objectForm.getAcumuladoTrimetreActual()));
		if(objectForm.getAcumuladoTrimetreAnterior()!=null && !objectForm.getAcumuladoTrimetreAnterior().equals(""))
			gestionEconomicaCatalunyaVo.setAcumuladoTrimetreAnterior(new BigDecimal(objectForm.getAcumuladoTrimetreAnterior()));
		if(objectForm.getImporteAnticipo()!=null && !objectForm.getImporteAnticipo().equals(""))
			gestionEconomicaCatalunyaVo.setImporteAnticipo(new BigDecimal(objectForm.getImporteAnticipo()));
		if(objectForm.getImporteFinal()!=null && !objectForm.getImporteFinal().equals(""))
			gestionEconomicaCatalunyaVo.setImporteFinal(new BigDecimal(objectForm.getImporteFinal()));
		if(objectForm.getValorInteres()!=null && !objectForm.getValorInteres().equals(""))
			gestionEconomicaCatalunyaVo.setValorInteres(new BigDecimal(objectForm.getValorInteres()));
		
		
		
		
		return gestionEconomicaCatalunyaVo;
	}
	public List<GestionEconomicaCatalunyaForm> getVo2FormList(
			List<GestionEconomicaCatalunyaVo> voList) {
		List<GestionEconomicaCatalunyaForm> list = new ArrayList<GestionEconomicaCatalunyaForm>();
		GestionEconomicaCatalunyaForm gestionEconomicaForm   = null;
		for (GestionEconomicaCatalunyaVo objectVo : voList) {
			gestionEconomicaForm = getVo2Form(objectVo);
			gestionEconomicaForm.setUsrBean(getUsrBean());
			list.add(gestionEconomicaForm);
			
		}
		return list;
	}
	public GestionEconomicaCatalunyaForm getVo2Form(GestionEconomicaCatalunyaVo objectVo) {
		return getVo2Form(objectVo,new GestionEconomicaCatalunyaForm());
	}
	public GestionEconomicaCatalunyaForm getVo2Form(GestionEconomicaCatalunyaVo objectVo, GestionEconomicaCatalunyaForm objectForm) {
		
		
		if(objectVo.getIdTipoIntercambio()!=null)
			objectForm.setIdTipoIntercambio(objectVo.getIdTipoIntercambio());
		if(objectVo.getIdJustificacion()!=null)
			objectForm.setIdJustificacion(objectVo.getIdJustificacion().toString());
		if(objectVo.getIdIntercambio()!=null)
			objectForm.setIdIntercambio(objectVo.getIdIntercambio().toString());
		if(objectVo.getIdInstitucion()!=null)
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		if(objectVo.getDescripcion()!=null && !objectVo.getDescripcion().equals(""))
			objectForm.setDescripcion(objectVo.getDescripcion());
		if(objectVo.getDescripcionTipoIntercambio()!=null && !objectVo.getDescripcionTipoIntercambio().equals(""))
			objectForm.setDescripcionTipoIntercambio(objectVo.getDescripcionTipoIntercambio());
		
		if(objectVo.getAnio()!=null){
			objectForm.setAnio(objectVo.getAnio().toString());
		}
		if(objectVo.getIdPeriodo()!=null)
			objectForm.setIdPeriodo(objectVo.getIdPeriodo().toString());
		if(objectVo.getIdEstado()!=null)
			objectForm.setIdEstado(objectVo.getIdEstado().toString());
		if(objectVo.getDescripcionEstado()!=null && !objectVo.getDescripcionEstado().equals(""))
			objectForm.setDescripcionEstado(objectVo.getDescripcionEstado());
		if(objectVo.getDescripcionEstadoIca()!=null && !objectVo.getDescripcionEstadoIca().equals(""))
			objectForm.setDescripcionEstadoIca(objectVo.getDescripcionEstadoIca());
		if(objectVo.getDescripcionEstadoConsell()!=null && !objectVo.getDescripcionEstadoConsell().equals(""))
			objectForm.setDescripcionEstadoConsell(objectVo.getDescripcionEstadoConsell());
		
		
		
		objectForm.setNombrePeriodo(objectVo.getNombrePeriodo());
		objectForm.setUsuModificacion(objectVo.getUsuModificacion());
		objectForm.setAbreviaturaInstitucion(objectVo.getAbreviaturaInstitucion());
		objectForm.setError(objectVo.getError());
		if(objectVo.getIdDevolucion()!=null)
			objectForm.setIdDevolucion(objectVo.getIdDevolucion().toString());
		if(objectVo.getIdCertificacion()!=null)
			objectForm.setIdCertificacion(objectVo.getIdCertificacion().toString());
		if(objectVo.getIdCertificacionAnexo()!=null)
			objectForm.setIdCertificacionAnexo(objectVo.getIdCertificacionAnexo().toString());
		if(objectVo.getIdTipoCertificacion()!=null)
			objectForm.setIdTipoCertificacion(objectVo.getIdTipoCertificacion().toString());
		
		if(objectVo.getDatosJustificacion()!=null && objectVo.getDatosJustificacion().size()>0) {
			JustificacionCatalunyaForm justificacionCatalunyaForm = new JustificacionCatalunyaForm();
			List<JustificacionCatalunyaForm>  datosJustificacionList = justificacionCatalunyaForm.getVo2FormList(objectVo.getDatosJustificacion());
			objectForm.setDatosJustificacion(datosJustificacionList);
		}
		if(objectVo.getDatosDevolucion()!=null && objectVo.getDatosDevolucion().size()>0) {
			JustificacionCatalunyaForm justificacionCatalunyaForm = new JustificacionCatalunyaForm();
			List<JustificacionCatalunyaForm>  datosJustificacionList = justificacionCatalunyaForm.getVoDev2FormList(objectVo.getDatosDevolucion());
			objectForm.setDatosJustificacion(datosJustificacionList);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		if(objectVo.getFechaCreacion()!=null)
			objectForm.setFechaCreacion(sdf.format(objectVo.getFechaCreacion()));
		
		return objectForm;
	}

	public final String getAbreviaturaInstitucion() {
		return abreviaturaInstitucion;
	}

	public final void setAbreviaturaInstitucion(String abreviaturaInstitucion) {
		this.abreviaturaInstitucion = abreviaturaInstitucion;
	}

	

	public final String getError() {
		return error;
	}

	public final void setError(String error) {
		this.error = error;
	}

	public final UsrBean getUsrBean() {
		return usrBean;
	}

	public final void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public final String getSeleccion() {
		return seleccion;
	}

	public final void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}
	public final String getIdDevolucion() {
		return idDevolucion;
	}

	public final void setIdDevolucion(String idDevolucion) {
		this.idDevolucion = idDevolucion;
	}

	public String getIdTipoIntercambio() {
		return idTipoIntercambio;
	}

	public void setIdTipoIntercambio(String idTipoIntercambio) {
		this.idTipoIntercambio = idTipoIntercambio;
	}

	public String getIdIntercambio() {
		return idIntercambio;
	}

	public void setIdIntercambio(String idIntercambio) {
		this.idIntercambio = idIntercambio;
	}

	public String getDescripcionTipoIntercambio() {
		return descripcionTipoIntercambio;
	}

	public void setDescripcionTipoIntercambio(String descripcionTipoIntercambio) {
		this.descripcionTipoIntercambio = descripcionTipoIntercambio;
	}

	public String getDescripcionEstadoIca() {
		return descripcionEstadoIca;
	}

	public void setDescripcionEstadoIca(String descripcionEstadoIca) {
		this.descripcionEstadoIca = descripcionEstadoIca;
	}

	public String getDescripcionEstadoConsell() {
		return descripcionEstadoConsell;
	}

	public void setDescripcionEstadoConsell(String descripcionEstadoConsell) {
		this.descripcionEstadoConsell = descripcionEstadoConsell;
	}

	public String getIdCertificacion() {
		return idCertificacion;
	}

	public void setIdCertificacion(String idCertificacion) {
		this.idCertificacion = idCertificacion;
	}

	

	public String getIdTipoCertificacion() {
		return idTipoCertificacion;
	}

	public void setIdTipoCertificacion(String idTipoCertificacion) {
		this.idTipoCertificacion = idTipoCertificacion;
	}

	public String getIdCertificacionAnexo() {
		return idCertificacionAnexo;
	}

	public void setIdCertificacionAnexo(String idCertificacionAnexo) {
		this.idCertificacionAnexo = idCertificacionAnexo;
	}

	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}