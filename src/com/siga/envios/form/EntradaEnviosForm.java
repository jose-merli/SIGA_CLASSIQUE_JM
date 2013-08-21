package com.siga.envios.form;

import org.json.JSONArray;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.EstadosEntradaEnviosEnum;

import com.atos.utils.ClsConstants;
import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

/**
 * Formulario para la definición de envios. 
 * 
 * @author Carlos Ruano
 */
public class EntradaEnviosForm extends MasterForm
{
	// Atributos
	private String fechaDesde;
	private String fechaHasta;
	private String codigoExtJuzgado;
	private String remitente;
	private String idEnvio;
	private String descripcionEstado;
	private String identificador;
	private String identificadorRelacionado;
	private String descripcionRemitente;
	private String idTurnoDesignaNew,idTurnoDesignaSel;	
	private String fechaApertura;
	private String caso;
	private String anioDesignaSel, numeroDesignaSel, codNewDesigna, anioEJGSel, numeroEJGSel, idTipoEJGSel;
	private String anioDesignaNew, numeroDesignaNew, codSelDesigna, anioEJGNew, numeroEJGNew, idTipoEJGNew;
	private String numEJGNew, numEJGSel;
	private String descripcionSolicitante;
	private String procuradorDesignaSel, abogadoDesignaSel;
	private JSONArray ejgSelDesignas;
	private String idTipoDesignacionColegio;
	boolean preceptivoProcurador = false;
	private String comisionAJG;
	private String origen;
	
	public boolean isPreceptivoProcurador() {
		return preceptivoProcurador;
	}
	public void setPreceptivoProcurador(boolean preceptivoProcurador) {
		this.preceptivoProcurador = preceptivoProcurador;
	}
	public String getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public String getIdTipoDesignacionColegio() {
		return idTipoDesignacionColegio;
	}
	public void setIdTipoDesignacionColegio(String idTipoDesignacionColegio) {
		this.idTipoDesignacionColegio = idTipoDesignacionColegio;
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getIdentificadorRelacionado() {
		return identificadorRelacionado;
	}
	public void setIdentificadorRelacionado(String identificadorRelacionado) {
		this.identificadorRelacionado = identificadorRelacionado;
	}
	public String getDescripcionRemitente() {
		return descripcionRemitente;
	}
	public void setDescripcionRemitente(String descripcionRemitente) {
		this.descripcionRemitente = descripcionRemitente;
	}
	public String getDescripcionEstado() {
		return descripcionEstado;
	}
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	public String getAnioDesignaSel() {
		return anioDesignaSel;
	}
	public void setAnioDesignaSel(String anioDesignaSel) {
		this.anioDesignaSel = anioDesignaSel;
	}
	public String getNumeroDesignaSel() {
		return numeroDesignaSel;
	}
	public void setNumeroDesignaSel(String numeroDesignaSel) {
		this.numeroDesignaSel = numeroDesignaSel;
	}
	public String getAnioEJGSel() {
		return anioEJGSel;
	}
	public void setAnioEJGSel(String anioEJGSel) {
		this.anioEJGSel = anioEJGSel;
	}
	public String getNumeroEJGSel() {
		return numeroEJGSel;
	}
	public void setNumeroEJGSel(String numeroEJGSel) {
		this.numeroEJGSel = numeroEJGSel;
	}
	public String getIdTurnoDesignaNew() {
		return idTurnoDesignaNew;
	}
	public void setIdTurnoDesignaNew(String idTurnoDesignaNew) {
		this.idTurnoDesignaNew = idTurnoDesignaNew;
	}
	public String getIdTurnoDesignaSel() {
		return idTurnoDesignaSel;
	}
	public void setIdTurnoDesignaSel(String idTurnoDesignaSel) {
		this.idTurnoDesignaSel = idTurnoDesignaSel;
	}
	public String getIdTipoEJGSel() {
		return idTipoEJGSel;
	}
	public void setIdTipoEJGSel(String idTipoEJGSel) {
		this.idTipoEJGSel = idTipoEJGSel;
	}
	public String getIdTipoEJGNew() {
		return idTipoEJGNew;
	}
	public void setIdTipoEJGNew(String idTipoEJGNew) {
		this.idTipoEJGNew = idTipoEJGNew;
	}

	private String idEnvioRelacionado;
	private String idEstado;
	private String idTipoIntercambioTelematico;
	private String idInstitucion;
	private String fechaPeticion;
	private String fechaRespuesta;
	private String necesitaRespuesta;
	public String getNecesitaRespuesta() {
		return necesitaRespuesta;
	}
	public void setNecesitaRespuesta(String necesitaRespuesta) {
		this.necesitaRespuesta = necesitaRespuesta;
	}

	private FilaExtElement[] elementosFila;
	private String botonesFila;
	private String asunto;
	private String botonesDetalle;
	
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
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
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getCodigoExtJuzgado() {
		return codigoExtJuzgado;
	}
	public void setCodigoExtJuzgado(String codigoExtJuzgado) {
		this.codigoExtJuzgado = codigoExtJuzgado;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getIdEnvio() {
		return idEnvio;
	}
	public void setIdEnvio(String identificador) {
		this.idEnvio = identificador;
	}
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	public String getIdTipoIntercambioTelematico() {
		return idTipoIntercambioTelematico;
	}
	public void setIdTipoIntercambioTelematico(String idTipoIntercambioTelematico) {
		this.idTipoIntercambioTelematico = idTipoIntercambioTelematico;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdEnvioRelacionado() {
		return idEnvioRelacionado;
	}
	public void setIdEnvioRelacionado(String idEnvioRelacionado) {
		this.idEnvioRelacionado = idEnvioRelacionado;
	}
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}
	
	public String getBotonesFila() {
		if(getIdEstado().equals(""+EstadosEntradaEnviosEnum.ESTADO_ERROR.getCodigo())){
			this.botonesFila = null;
		}else{
			this.botonesFila = "C";
			setBotones(this.botonesFila);
		}
		return this.botonesFila;
	}

	public void setBotones(String botonesFila) {
		this.botonesFila = botonesFila;
	}
	
	public String getBotonesDetalle() {
		//caso solicitud ejg 
		//if(necesitaRespuesta!=null && necesitaRespuesta.equals(ClsConstants.DB_TRUE)){
			
			if(idTipoIntercambioTelematico.equals("06")){ // SOLICITUD DESIGNACION PROVISIONAL
				if(idEstado.equals("1") || idEstado.equals("2")){
					// Botones tramitar datos y seleccionar ejg
					botonesDetalle = "v,d,g"; 
				}else if(idEstado.equals("3")){
					//si es incompleto de ejg pondremos el boton tramitar datos
					botonesDetalle = "v,d";
				}else if(idEstado.equals("4")){
					//si el tramitado ha sido por seleccion de ejg pondremos el boton tramitar datos
					if(!preceptivoProcurador || (this.getProcuradorDesignaSel() != null && !this.getProcuradorDesignaSel().equals(""))){
						botonesDetalle = "v,d,g,COM";
					}else{
						botonesDetalle = "v,d,g";
					}
				}else if(idEstado.equals("5")){
					botonesDetalle = "v,d";
				 }
				
			}else if(idTipoIntercambioTelematico.equals("05")){ // RESPUESTA SUSPENSION PROCEDIMIENTO
				if(idEstado.equals("2") && idEnvioRelacionado != null && !idEnvioRelacionado.equals("")){
					botonesDetalle = "v,d,g"; 
				}else {
					botonesDetalle = "v,d";
				}	
				
			}else if(idTipoIntercambioTelematico.equals("08")){ // RESPUESTA SUSPENSION PROCEDIMIENTO
				if(idEstado.equals("2")){
					botonesDetalle = "v,d,p"; 
				}else {
					botonesDetalle = "v,d";
				}			
			}
		
		/*}else{
			if(idEstado.equals("1")){
				// Botones tramitar datos
				botonesDetalle = "v,d,g"; 
			 }else if(idEstado.equals("2")){
				 botonesDetalle = "v,d";
			 }else if(idEstado.equals("3")){
				 //No Aplica este estadp
			 }else if(idEstado.equals("4")){
				 //No aplica este estadp
				 botonesDetalle = "v,d";
			 }
		}*/
		
		return botonesDetalle;
	}
	public void setBotonesDetalle(String botonesDetalle) {
		this.botonesDetalle = botonesDetalle;
	}
	public FilaExtElement[] getElementosFila() {
		FilaExtElement[] elementosFila = null;
		if(getIdEstado().equals(""+EstadosEntradaEnviosEnum.ESTADO_ERROR.getCodigo())){
			if(getIdTipoIntercambioTelematico() != null && getIdTipoIntercambioTelematico().equals(AppConstants.TipoIntercambioEnum.SGP_CAJG_RES_SOL_IMP.getCodigo())){
				elementosFila = new FilaExtElement[3];
				elementosFila[1] = new FilaExtElement("download", "download","general.boton.download",	SIGAConstants.ACCESS_READ);
				elementosFila[2] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
			}else{
				elementosFila = null;
			}
		}else if(getIdEstado().equals(""+EstadosEntradaEnviosEnum.ESTADO_PENDIENTE_ENVIAR.getCodigo()) && (!preceptivoProcurador ||( this.getProcuradorDesignaSel() != null && !this.getProcuradorDesignaSel().equals("")))){
			elementosFila = new FilaExtElement[3];
			elementosFila[1] = new FilaExtElement("download", "download","general.boton.download",	SIGAConstants.ACCESS_READ);
			elementosFila[2] = new FilaExtElement("comunicar", "comunicar",	SIGAConstants.ACCESS_READ);
		}else{
			elementosFila = new FilaExtElement[2];
			elementosFila[1] = new FilaExtElement("download", "download","general.boton.download",	SIGAConstants.ACCESS_READ);
		}
		this.setElementosFila(elementosFila);
		return elementosFila;
	}

	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}
	public String getCaso() {
		return caso;
	}
	public void setCaso(String caso) {
		this.caso = caso;
	}
	public String getAnioDesignaNew() {
		return anioDesignaNew;
	}
	public void setAnioDesignaNew(String anioDesignaNew) {
		this.anioDesignaNew = anioDesignaNew;
	}
	public String getNumeroDesignaNew() {
		return numeroDesignaNew;
	}
	public void setNumeroDesignaNew(String numeroDesignaNew) {
		this.numeroDesignaNew = numeroDesignaNew;
	}
	public String getAnioEJGNew() {
		return anioEJGNew;
	}
	public void setAnioEJGNew(String anioEJGNew) {
		this.anioEJGNew = anioEJGNew;
	}
	public String getNumeroEJGNew() {
		return numeroEJGNew;
	}
	public void setNumeroEJGNew(String numeroEJGNew) {
		this.numeroEJGNew = numeroEJGNew;
	}
	public String getProcuradorDesignaSel() {
		return procuradorDesignaSel;
	}
	public void setProcuradorDesignaSel(String procuradorDesignaSel) {
		this.procuradorDesignaSel = procuradorDesignaSel;
	}
	public String getAbogadoDesignaSel() {
		return abogadoDesignaSel;
	}
	public void setAbogadoDesignaSel(String abogadoDesignaSel) {
		this.abogadoDesignaSel = abogadoDesignaSel;
	}
	public JSONArray getEjgSelDesignas() {
		return ejgSelDesignas;
	}
	public void setEjgSelDesignas(JSONArray ejgSelDesignas) {
		this.ejgSelDesignas = ejgSelDesignas;
	}
	public String getDescripcionSolicitante() {
		return descripcionSolicitante;
	}
	public void setDescripcionSolicitante(String descripcionSolicitante) {
		this.descripcionSolicitante = descripcionSolicitante;
	}
	public String getCodNewDesigna() {
		return codNewDesigna;
	}
	public void setCodNewDesigna(String codNewDesigna) {
		this.codNewDesigna = codNewDesigna;
	}
	public String getCodSelDesigna() {
		return codSelDesigna;
	}
	public void setCodSelDesigna(String codSelDesigna) {
		this.codSelDesigna = codSelDesigna;
	}
	public String getNumEJGNew() {
		return numEJGNew;
	}
	public void setNumEJGNew(String numEJGNew) {
		this.numEJGNew = numEJGNew;
	}
	public String getNumEJGSel() {
		return numEJGSel;
	}
	public void setNumEJGSel(String numEJGSel) {
		this.numEJGSel = numEJGSel;
	}
	public String getComisionAJG() {
		return comisionAJG;
	}
	public void setComisionAJG(String comisionAJG) {
		this.comisionAJG = comisionAJG;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	/**
	 * 
	 */
	public void reset() {
		this.setAnioDesignaNew("");
		this.setAnioDesignaSel("");
		this.setNumeroDesignaNew("");
		this.setNumeroDesignaSel("");
		this.setIdTurnoDesignaNew("");
		this.setIdTurnoDesignaSel("");
		this.setAnioEJGNew("");
		this.setAnioEJGSel("");
		this.setAnioEJGNew("");
		this.setNumeroEJGNew("");
		this.setNumeroEJGSel("");
		this.setIdTipoEJGNew("");
		this.setIdTipoEJGSel("");
		this.setOrigen("");
		
		
	}
}
