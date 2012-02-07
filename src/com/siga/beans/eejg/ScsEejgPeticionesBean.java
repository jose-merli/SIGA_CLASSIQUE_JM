package com.siga.beans.eejg;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.tlds.FilaExtElement;
/**
 * 
 * @author jorgeta
 *
 */
public class ScsEejgPeticionesBean extends MasterBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6457973849067549976L;

	public static final int EEJG_ESTADO_INICIAL = 10;  
	public static final int EEJG_ESTADO_INICIAL_ESPERANDO = 15;
	public static final int EEJG_ESTADO_ESPERA = 20;
	public static final int EEJG_ESTADO_ESPERA_ESPERANDO = 25;
	public static final int EEJG_ESTADO_PENDIENTE_INFO = 23;
	public static final int EEJG_ESTADO_FINALIZADO = 30;
	public static final int EEJG_ESTADO_ERROR_SOLICITUD = 40;
	public static final int EEJG_ESTADO_ERROR_CONSULTA_INFO = 50;
	
	public static final String INSTITUCION_PARAMETROS_EEJG = "2000";
	
	Long idPeticion;
	Integer idUsuarioPeticion;
	AdmUsuariosBean usuarioPeticion;
	
	Integer idInstitucion;
	String fechaPeticion;	
	Integer estado;
	String idSolicitud;
	String fechaSolicitud;
	String fechaConsulta;
	Integer idTipoEjg;
	Integer anio;
	Integer numero;
	String idioma;
	Long idPersona;
	ScsUnidadFamiliarEJGBean unidadFamiliar;
	Integer numeroIntentosSolicitud;
	Integer numeroIntentosConsulta;
	Integer numeroIntentosPendienteInfo;
	Integer idXml;
	ScsEejgXmlBean xmlPeticionEejg = null;
	private String	nif;
	private String	nombre;
	private String	apellido1;
	private String	apellido2;
	
	FilaExtElement[] elementosFila;
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_EEJG_PETICIONES";
	
	
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDPETICION = "IDPETICION";
	static public final String 	C_IDUSUARIOPETICION = "IDUSUARIOPETICION";
	static public final String 	C_FECHAPETICION = "FECHAPETICION";
	static public final String 	C_ESTADO = "ESTADO";
	static public final String 	C_IDSOLICITUD = "IDSOLICITUD";
	static public final String 	C_FECHASOLICITUD = "FECHASOLICITUD";
	static public final String 	C_IDINSTITUCION = "IDINSTITUCION";
	static public final String 	C_IDTIPOEJG = "IDTIPOEJG";
	static public final String 	C_ANIO = "ANIO";
	static public final String 	C_NUMERO = "NUMERO";
	static public final String 	C_IDPERSONA = "IDPERSONA";
	static public final String 	C_NUMEROINTENTOSSOLICITUD = "NUMEROINTENTOSSOLICITUD";
	static public final String 	C_NUMEROINTENTOSCONSULTA = "NUMEROINTENTOSCONSULTA";
	static public final String 	C_NUMEROINTENTOSPENDIENTEINFO = "NUMEROINTENTOSPENDIENTEINFO";
	
	static public final String 	C_IDXML = "IDXML";
	static public final String 	C_IDIOMA = "IDIOMA";
	static public final String 	C_FECHACONSULTA = "FECHACONSULTA";
	static public final String 	C_NIF				=				"NIF";
	static public final String 	C_NOMBRE			=				"NOMBRE";
	static public final String 	C_APELLIDO1			=				"APELLIDO1";
	static public final String 	C_APELLIDO2			=				"APELLIDO2";
	
	public Long getIdPeticion() {
		return idPeticion;
	}
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public Integer getIdTipoEjg() {
		return idTipoEjg;
	}
	public void setIdTipoEjg(Integer idTipoEjg) {
		this.idTipoEjg = idTipoEjg;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	
	public void setUnidadFamiliar(ScsUnidadFamiliarEJGBean unidadFamiliar) {
		this.unidadFamiliar = unidadFamiliar;
		this.idInstitucion = unidadFamiliar.getIdInstitucion();
		this.idTipoEjg = unidadFamiliar.getIdTipoEJG();
		this.anio = unidadFamiliar.getAnio();
		this.numero = unidadFamiliar.getNumero();
		this.idPersona = Long.parseLong(unidadFamiliar.getIdPersona().toString());
		
	}
	public ScsUnidadFamiliarEJGBean getUnidadFamiliar() {
		unidadFamiliar.setIdInstitucion(idInstitucion);
		unidadFamiliar.setIdTipoEJG(idTipoEjg);
		unidadFamiliar.setAnio(anio);
		unidadFamiliar.setNumero(numero);
		unidadFamiliar.setIdPersona(Integer.parseInt(idPersona.toString()));
		return unidadFamiliar;
	}
	
	public Integer getNumeroIntentosSolicitud() {
		return numeroIntentosSolicitud;
	}
	public void setNumeroIntentosSolicitud(Integer numeroIntentosSolicitud) {
		this.numeroIntentosSolicitud = numeroIntentosSolicitud;
	}
	public Integer getNumeroIntentosConsulta() {
		return numeroIntentosConsulta;
	}
	public void setNumeroIntentosConsulta(Integer numeroIntentosConsulta) {
		this.numeroIntentosConsulta = numeroIntentosConsulta;
	}
	public Integer getIdXml() {
		return idXml;
	}
	public void setIdXml(Integer idXml) {
		this.idXml = idXml;
	}
	public ScsEejgXmlBean getXmlPeticionEejg() {
		return xmlPeticionEejg;
	}
	public void setXmlPeticionEejg(ScsEejgXmlBean xmlPeticionEejg) {
		this.xmlPeticionEejg = xmlPeticionEejg;
	}
	/**
	 * @return the idUsuarioPeticion
	 */
	public Integer getIdUsuarioPeticion() {
		return idUsuarioPeticion;
	}
	/**
	 * @param idUsuarioPeticion the idUsuarioPeticion to set
	 */
	public void setIdUsuarioPeticion(Integer idUsuarioPeticion) {
		this.idUsuarioPeticion = idUsuarioPeticion;
	}
	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	/**
	 * @return the fechaConsulta
	 */
	public String getFechaConsulta() {
		return fechaConsulta;
	}
	/**
	 * @param fechaConsulta the fechaConsulta to set
	 */
	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	/**
	 * @return the idioma
	 */
	public String getIdioma() {
		return idioma;
	}
	/**
	 * @param idioma the idioma to set
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	/**
	 * @return the numeroIntentosPendienteInfo
	 */
	public Integer getNumeroIntentosPendienteInfo() {
		return numeroIntentosPendienteInfo;
	}
	/**
	 * @param numeroIntentosPendienteInfo the numeroIntentosPendienteInfo to set
	 */
	public void setNumeroIntentosPendienteInfo(Integer numeroIntentosPendienteInfo) {
		this.numeroIntentosPendienteInfo = numeroIntentosPendienteInfo;
	}
	public AdmUsuariosBean getUsuarioPeticion() {
		return usuarioPeticion;
	}
	public void setUsuarioPeticion(AdmUsuariosBean usuarioPeticion) {
		this.usuarioPeticion = usuarioPeticion;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public FilaExtElement[] getElementosFila() {
		FilaExtElement[] elementosFila = null;
		int	estado = this.getEstado();
		switch (estado) {
			case ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL:
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("espera", "esperaEejg","general.boton.esperaEejg",SIGAConstants.ACCESS_READ);
			break;
			case ScsEejgPeticionesBean.EEJG_ESTADO_PENDIENTE_INFO:
				elementosFila = new FilaExtElement[5];
				elementosFila[3] = new FilaExtElement("espera", "avisoEsperaInfoEejg","general.boton.esperaInfoEejg",SIGAConstants.ACCESS_READ);
				elementosFila[4] = new FilaExtElement("download", "esperaInfoEejg","general.boton.descargarEejg",	SIGAConstants.ACCESS_READ);
			break;
			case ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA:case ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA_ESPERANDO:case ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL_ESPERANDO:
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("espera", "esperaAdministracionesEejg","general.boton.esperaAdministracionesEejg",SIGAConstants.ACCESS_READ);
			break;
			case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_SOLICITUD:case ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_CONSULTA_INFO:
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("descargaLog", "errorEejg","general.boton.errorEejg",SIGAConstants.ACCESS_READ);
			break;
			case ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO:
				elementosFila = new FilaExtElement[4];
				elementosFila[3] = new FilaExtElement("download", "descargarEejg","general.boton.descargarEejg",	SIGAConstants.ACCESS_READ);
			break;
		default:
			elementosFila = new FilaExtElement[4];
			elementosFila[3] = new FilaExtElement(null, "solicitarEejg","general.boton.solicitarEejg",	SIGAConstants.ACCESS_READ,"general.boton.solicitudEejg","90");
			break;
		}
				
		
		this.setElementosFila(elementosFila);
		return elementosFila;
	}
	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}
	
	
	

	
}