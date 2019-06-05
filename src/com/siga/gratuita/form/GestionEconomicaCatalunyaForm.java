package com.siga.gratuita.form;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
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
 *       La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class GestionEconomicaCatalunyaForm extends MasterForm {
	private static final long serialVersionUID = 1L;
	String descripcion = null;
	String idPeriodo = null;
	String anio;
	String idJustificacion;
	String idFacturacion = null;
	String facturaciones = null;
	String descripcionFacturaciones = null;
	
	String idDevolucion = null;
	

	String idInstitucion = null;
	String idEstado;
	String descripcionEstado;
	String fechaDesde;
	String fechaHasta;
	String nombrePeriodo;
	Integer usuModificacion;
	String abreviaturaInstitucion;
	String error;
	UsrBean usrBean = null;
	String seleccion;
	List<JustificacionCatalunyaForm> datosJustificacion;
	String pathFile;

	
	String edicionJusticiable;
	String edicionFechaJustificacion;
	String edicionTipoActuacion;
	String edicionLetrado;
	String edicionFecha;
	String edicionId;
	String idColegio;

	public final String getIdColegio() {
		return idColegio;
	}

	public final void setIdColegio(String idColegio) {
		this.idColegio = idColegio;
	}

	public final String getEdicionJusticiable() {
		return edicionJusticiable;
	}

	public final void setEdicionJusticiable(String edicionJusticiable) {
		this.edicionJusticiable = edicionJusticiable;
	}

	public final String getEdicionFechaJustificacion() {
		return edicionFechaJustificacion;
	}

	public final void setEdicionFechaJustificacion(String edicionFechaJustificacion) {
		this.edicionFechaJustificacion = edicionFechaJustificacion;
	}

	public final String getEdicionTipoActuacion() {
		return edicionTipoActuacion;
	}

	public final void setEdicionTipoActuacion(String edicionTipoActuacion) {
		this.edicionTipoActuacion = edicionTipoActuacion;
	}

	public final String getEdicionLetrado() {
		return edicionLetrado;
	}

	public final void setEdicionLetrado(String edicionLetrado) {
		this.edicionLetrado = edicionLetrado;
	}

	public final String getEdicionFecha() {
		return edicionFecha;
	}

	public final void setEdicionFecha(String edicionFecha) {
		this.edicionFecha = edicionFecha;
	}

	public final String getEdicionId() {
		return edicionId;
	}

	public final void setEdicionId(String edicionId) {
		this.edicionId = edicionId;
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

	public String getFacturaciones() {
		return facturaciones;
	}

	public void setFacturaciones(String facturaciones) {
		this.facturaciones = facturaciones;
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
//		VALIDADO_ERRONEO((short)16,"Validado err�neo"),
//		ENVIANDO_CICAC((short)20,"Enviando ICA - CICAC..."),
//		ENVIADO_CICAC((short)30,"Enviado CICAC"),
//		ERROR_CICAC((short)40,"Error CICAC"),
//		FIN_CICAC((short)50,"Fin CICAC"),
//		DEVUELTO_CICAC_ICA_ERRONEO((short)60,"Devuelto CICAC - ICA err�neo"),
//		ENVIANDO_GEN_GENERANDO_XML((short)62,"Enviando CICAC - GEN. Generando xml"),
//		ENVIANDO_GEN_MOVIENDO_XML((short)65,"Enviando CICAC - GEN. Moviendo xml"),
//		ENVIADO_GEN((short)70,"Enviado Gen."),
//		PROCESANDO_GEN((short)75,"Procesando Gen..."),
//		ERROR_GEN_ANTIVIRUS((short)80,"Error GEN antivirus"),
//		ERROR_GEN((short)85,"Error Gen."),
//		DEVUELTO_GEN_CICAC_ERRONEO((short)87,"Devuelto GEN - CICAC err�neo"),
//		DEVUELTO_GEN_CICAC_CORRECTO((short)90,"Devuelto GEN - CICAC correcto."),
//		RESPONDIENDO_ICA((short)92,"Respondiendo ICA..."),
//		DEVUELTO_ICA_CORRECTO((short)95,"Devuelto CICAC - ICA correcto");

		
		
		
		switch (Integer.valueOf(idEstado)) {
		case 10:
			elementosFila = new FilaExtElement[1];
			elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPreviaJustificacion",SIGAConstants.ACCESS_FULL);
			break;
		case 12:
			elementosFila = new FilaExtElement[1];
			elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPreviaJustificacion",SIGAConstants.ACCESS_FULL);
			break;
		case 14:
			elementosFila = new FilaExtElement[3];
			elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPreviaJustificacion",SIGAConstants.ACCESS_FULL);
			elementosFila[1] = new FilaExtElement( "enviardenuevo","revalida", SIGAConstants.ACCESS_FULL);
			elementosFila[2] = new FilaExtElement("enviar", "enviaJustificacionCICAC",SIGAConstants.ACCESS_FULL);
			break;
			
		case 16:
			elementosFila = new FilaExtElement[3];
			elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPreviaJustificacion",SIGAConstants.ACCESS_FULL);
			elementosFila[1] = new FilaExtElement( "enviardenuevo","revalida", SIGAConstants.ACCESS_FULL);
			elementosFila[2] = new FilaExtElement( "descargaLog","descargaLogValidacion", SIGAConstants.ACCESS_FULL);
			
			break;
		case 20:
			elementosFila = new FilaExtElement[1];
			elementosFila[0] = new FilaExtElement("consultar", "consultaVistaPreviaJustificacion",SIGAConstants.ACCESS_FULL);
			break;
		case 30:
			
			
			if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
				elementosFila = new FilaExtElement[5];
				elementosFila[0] = new FilaExtElement("editar", "editaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement("download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
				elementosFila[2] = new FilaExtElement("upload","adjuntaFicheroError", SIGAConstants.ACCESS_FULL);
				elementosFila[3] = new FilaExtElement("denegar","insertaErrorGlobal", SIGAConstants.ACCESS_FULL);
				elementosFila[4] = new FilaExtElement("enviar","enviaJustificacionGEN", SIGAConstants.ACCESS_FULL);
			}else {
				elementosFila = new FilaExtElement[1];
//				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[0] = new FilaExtElement("download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
			}
			break;
		case 40:
			if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
				elementosFila = new FilaExtElement[4];
				elementosFila[0] = new FilaExtElement("editar", "editaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement("upload","adjuntaFicheroError", SIGAConstants.ACCESS_FULL);
				elementosFila[2] = new FilaExtElement( "descargaLog","descargaErroresJustificacion", SIGAConstants.ACCESS_FULL);
				elementosFila[3] = new FilaExtElement( "finalizar","finalizaErrores", SIGAConstants.ACCESS_FULL);
			}else {
				elementosFila = new FilaExtElement[2];
//				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
//				elementosFila[1] = new FilaExtElement("download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
				
			}
			
			break;
		case 50:
			
			elementosFila = new FilaExtElement[0];
//			elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
//			elementosFila[1] = new FilaExtElement( "descargaLog","descargaErroresJustificacion", SIGAConstants.ACCESS_FULL);
			
			break;
			
		case 60:
			elementosFila = new FilaExtElement[1];
//			elementosFila[0] = new FilaExtElement( "consultar","consultaJustificacion", SIGAConstants.ACCESS_FULL);
			elementosFila[0] = new FilaExtElement( "descargaLog","descargaErroresJustificacion", SIGAConstants.ACCESS_FULL);
			break;
		case 70:
			elementosFila = new FilaExtElement[2];
			elementosFila[0] = new FilaExtElement( "consultar","consultaJustificacion", SIGAConstants.ACCESS_FULL);
			elementosFila[1] = new FilaExtElement( "download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
			break;
		
		case 87:
			if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
				elementosFila = new FilaExtElement[3];
				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement( "descargaLog","descargaErroresJustificacion", SIGAConstants.ACCESS_FULL);
				elementosFila[2] = new FilaExtElement("enviar","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
			}else {
				elementosFila = new FilaExtElement[2];
				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement( "descargaLog","descargaErroresJustificacion", SIGAConstants.ACCESS_FULL);
				
			}
			break;
		case 90:
			if(usrBean !=null && usrBean.getLocation()!=null && usrBean.getLocation().equals("3001")) {
				elementosFila = new FilaExtElement[3];
				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement( "download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
				elementosFila[2] = new FilaExtElement("enviar","enviaRespuestaCICAC_ICA", SIGAConstants.ACCESS_FULL);
			}else {
				elementosFila = new FilaExtElement[2];
				elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
				elementosFila[1] = new FilaExtElement( "download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
			}
			break;
		case 95:
			elementosFila = new FilaExtElement[2];
			elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
			elementosFila[1] = new FilaExtElement( "download","descargaJustificacion", SIGAConstants.ACCESS_FULL);
			break;

		default:
			elementosFila = new FilaExtElement[1];
			elementosFila[0] = new FilaExtElement("consultar", "consultaJustificacion",SIGAConstants.ACCESS_FULL);
		
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
		this.idFacturacion = null;
		this.idInstitucion = null;
		this.descripcionEstado = null;
		this.elementosFila = null;
		
		this.anio = null;
		this.nombrePeriodo = null;

	}

	public GestionEconomicaCatalunyaForm clone() {
		GestionEconomicaCatalunyaForm miForm = new GestionEconomicaCatalunyaForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setIdEstado(this.getIdEstado());
		miForm.setFechaDesde(this.getFechaDesde());
		miForm.setFechaHasta(this.getFechaHasta());
		miForm.setDatosPaginador(this.getDatosPaginador());
		return miForm;

	}
	public String getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(String idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public String getIdFacturacion() {
		return idFacturacion;
	}

	public void setIdFacturacion(String idFacturacion) {
		this.idFacturacion = idFacturacion;
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
	public final String getDescripcionFacturaciones() {
		return descripcionFacturaciones;
	}

	public final void setDescripcionFacturaciones(String descripcionFacturaciones) {
		this.descripcionFacturaciones = descripcionFacturaciones;
	}
	public GestionEconomicaCatalunyaVo getForm2Vo(GestionEconomicaCatalunyaForm objectForm) {
		
		GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = new GestionEconomicaCatalunyaVo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			gestionEconomicaCatalunyaVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		
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
		if(objectForm.getIdJustificacion()!=null && !objectForm.getIdJustificacion().equals(""))
			gestionEconomicaCatalunyaVo.setIdJustificacion(Long.valueOf(objectForm.getIdJustificacion()));
		
		if(objectForm.getIdColegio()!=null && !objectForm.getIdColegio().equals(""))
			gestionEconomicaCatalunyaVo.setIdColegio(Short.valueOf(objectForm.getIdColegio()));
		
		if(objectForm.getIdDevolucion()!=null && !objectForm.getIdDevolucion().equals(""))
			gestionEconomicaCatalunyaVo.setIdDevolucion(Long.valueOf(objectForm.getIdDevolucion()));
		
		
		if(objectForm.getIdEstado()!=null && !objectForm.getIdEstado().equals(""))
			gestionEconomicaCatalunyaVo.setIdEstado(Short.valueOf(objectForm.getIdEstado()));
		if(objectForm.getIdPeriodo()!=null && !objectForm.getIdPeriodo().equals(""))
			gestionEconomicaCatalunyaVo.setIdPeriodo(Short.valueOf(objectForm.getIdPeriodo()));
		if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
			gestionEconomicaCatalunyaVo.setAnio(Short.valueOf(objectForm.getAnio()));
		if(objectForm.getIdFacturacion()!=null && !objectForm.getIdFacturacion().equals(""))
			gestionEconomicaCatalunyaVo.setIdFacturacion(Long.valueOf(objectForm.getIdFacturacion()));
		gestionEconomicaCatalunyaVo.setDescripcion(objectForm.getDescripcion());
		
		gestionEconomicaCatalunyaVo.setNombrePeriodo(objectForm.getNombrePeriodo());
		String facturaciones = objectForm.getFacturaciones();
		if(facturaciones!=null && !facturaciones.equalsIgnoreCase("")) {
			String[] registrosFacturaciones = facturaciones.split(",");
			Long[] longNum = new Long[registrosFacturaciones.length];
			for(int i=0; i<registrosFacturaciones.length; i++){
				longNum[i] = Long.parseLong(registrosFacturaciones[i]);
			}
			gestionEconomicaCatalunyaVo.setFacturaciones(longNum);
		}
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
				gestionEconomicaCatalunyaVo.setFileErrorData(SIGAServicesHelper.getBytes(new File(getPathFile())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gestionEconomicaCatalunyaVo.setEdicionFecha(objectForm.getEdicionFecha());
		gestionEconomicaCatalunyaVo.setEdicionFechaJustificacion(objectForm.getEdicionFechaJustificacion());
		gestionEconomicaCatalunyaVo.setEdicionId(objectForm.getEdicionId());
		gestionEconomicaCatalunyaVo.setEdicionJusticiable(objectForm.getEdicionJusticiable());
		gestionEconomicaCatalunyaVo.setEdicionLetrado(objectForm.getEdicionLetrado());
		gestionEconomicaCatalunyaVo.setEdicionTipoActuacion(objectForm.getEdicionTipoActuacion());
		
		
		
		
		
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
		
		
		if(objectVo.getIdJustificacion()!=null)
			objectForm.setIdJustificacion(objectVo.getIdJustificacion().toString());
		if(objectVo.getIdInstitucion()!=null)
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		if(objectVo.getDescripcion()!=null && !objectVo.getDescripcion().equals(""))
			objectForm.setDescripcion(objectVo.getDescripcion());
		if(objectVo.getIdFacturacion()!=null){
			objectForm.setIdFacturacion(objectVo.getIdFacturacion().toString());
		}
		if(objectVo.getAnio()!=null){
			objectForm.setAnio(objectVo.getAnio().toString());
		}
		if(objectVo.getIdPeriodo()!=null)
			objectForm.setIdPeriodo(objectVo.getIdPeriodo().toString());
		if(objectVo.getIdEstado()!=null)
			objectForm.setIdEstado(objectVo.getIdEstado().toString());
		if(objectVo.getDescripcionEstado()!=null && !objectVo.getDescripcionEstado().equals(""))
			objectForm.setDescripcionEstado(objectVo.getDescripcionEstado());
		
		objectForm.setDescripcion(objectVo.getDescripcion());
		objectForm.setDescripcionFacturaciones(objectVo.getDescripcionFacturaciones());
		objectForm.setNombrePeriodo(objectVo.getNombrePeriodo());
		objectForm.setUsuModificacion(objectVo.getUsuModificacion());
		objectForm.setAbreviaturaInstitucion(objectVo.getAbreviaturaInstitucion());
		objectForm.setError(objectVo.getError());
		if(objectVo.getIdDevolucion()!=null)
			objectForm.setIdDevolucion(objectVo.getIdDevolucion().toString());
		if(objectVo.getDatosJustificacion()!=null && objectVo.getDatosJustificacion().size()>0) {
			JustificacionCatalunyaForm justificacionCatalunyaForm = new JustificacionCatalunyaForm();
			List<JustificacionCatalunyaForm>  datosJustificacionList = justificacionCatalunyaForm.getVo2FormList(objectVo.getDatosJustificacion());
			objectForm.setDatosJustificacion(datosJustificacionList);
		}
		
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

	


}