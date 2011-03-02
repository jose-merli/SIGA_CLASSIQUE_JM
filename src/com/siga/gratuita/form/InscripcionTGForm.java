package com.siga.gratuita.form;

/**
 * @author jorgeta
 */
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsRetencionesBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;
import com.siga.tlds.FilaExtElement;

public class InscripcionTGForm extends MasterForm {
	
	//Campos para la tabla de turnos
	String idInstitucion = null;
	String idPersona = null;
	String idTurno = null;
	String idGuardia = null;
	String  fechaSolicitud = null;
	String  observacionesSolicitud = null;
	String  fechaValidacion = null;
	String  observacionesValidacion = null;
	String  fechaSolicitudBaja = null;
	String  observacionesBaja = null;
	String  fechaBaja = null;
	private String	fechaDenegacion;
	private String  observacionesDenegacion;
	String tipo;
	String clase;
	String estado;
	List<ScsTurnoBean> turnosInscripcion;
	List<ScsGuardiasTurnoBean> guardiasInscripcion;
	String colegiadoNumero;
	String colegiadoNombre;
	String fechaDesde;
	String fechaHasta;
	List<ScsInscripcionTurnoBean> inscripcionesTurno;
	List<ScsInscripcionGuardiaBean> inscripcionesGuardia;
	
	ScsInscripcionTurnoBean inscripcionTurno;
	
	FilaExtElement[] elementosFila;
	String estadoPendientes;
	String tipoGuardias;
	String validarInscripciones;
	String telefono1 = null;
	String telefono2 = null;
	String movil = null;
	String fax1 = null;
	String fax2 = null;
	String idDireccion = null;
	
	List<ScsRetencionesBean> retenciones= null;
	String irpf = null;
	String idRetencion = null;
	
	String guardiasSel;
	String turnosSel;
	
	UsrBean usrBean;
	
	boolean solicitudAlta = false;
	boolean solicitudBaja = false;
	boolean validacionAlta = false;
	boolean validacionBaja = false;
	
	boolean masivo = false;
	
	String  fechaValidacionTurno = null;
	String  fechaBajaTurno = null;
	String  fechaSolicitudTurno = null;
	private String	fechaValorAlta;
	private String	fechaValorBaja;
	
	String fechaActiva = null;
	String  observacionesValBaja = null;
	
	private String porGrupos;
	List<LetradoInscripcion> gruposGuardiaLetrado;
	
	
	private String numeroGrupo;
	private String ordenGrupo;
	
	public String getFechaValorAlta() {
		return fechaValorAlta;
	}
	public void setFechaValorAlta(String fechaValorAlta) {
		this.fechaValorAlta = fechaValorAlta;
	}
	public String getFechaValorBaja() {
		return fechaValorBaja;
	}
	public void setFechaValorBaja(String fechaValorBaja) {
		this.fechaValorBaja = fechaValorBaja;
	}
	public UsrBean getUsrBean() {
		return usrBean;
	}
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}
	public String getIrpf() {
		return irpf;
	}
	public void setIrpf(String irpf) {
		this.irpf = irpf;
	}
	public List<ScsRetencionesBean> getRetenciones() {
		return retenciones;
	}
	public void setRetenciones(List<ScsRetencionesBean> retenciones) {
		this.retenciones = retenciones;
	}
	public String getTelefono1() {
		return telefono1;
	}
	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}
	public String getTelefono2() {
		return telefono2;
	}
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public String getFax1() {
		return fax1;
	}
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}
	public String getEstadoPendientes() {
		return estadoPendientes;
	}
	public void setEstadoPendientes(String estadoPendientes) {
		this.estadoPendientes = estadoPendientes;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public String getFechaSolicitudJsp() {
		if(fechaSolicitud!=null){
			try {
				return GstDate.getFormatedDateMedium("", fechaSolicitud);
			} catch (ClsExceptions e) {// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}else return "";
		
	}
	public String getFechaDenegacionJsp() {
		if(fechaDenegacion!=null){
			try {
				return GstDate.getFormatedDateShort("", fechaDenegacion);
			} catch (ClsExceptions e) {// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}else return "";
		
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public String getObservacionesSolicitud() {
		return observacionesSolicitud;
	}
	public void setObservacionesSolicitud(String observacionesSolicitud) {
		this.observacionesSolicitud = observacionesSolicitud;
	}
	public String getFechaValidacion() {
		return fechaValidacion;
	}
	public String getFechaValidacionJsp() {
		if(fechaValidacion!=null){
			try {
				return GstDate.getFormatedDateShort("", fechaValidacion);
			} catch (ClsExceptions e) {
				e.printStackTrace();
				return "";
			}
		}else return "";
		
	}
	public void setFechaValidacion(String fechaValidacion) {
		this.fechaValidacion = fechaValidacion;
	}
	public String getObservacionesValidacion() {
		return observacionesValidacion;
	}
	public void setObservacionesValidacion(String observacionesValidacion) {
		this.observacionesValidacion = observacionesValidacion;
	}
	public String getFechaSolicitudBaja() {
		return fechaSolicitudBaja;
	}
	public void setFechaSolicitudBaja(String fechaSolicitudBaja) {
		this.fechaSolicitudBaja = fechaSolicitudBaja;
	}
	public String getFechaSolicitudBajaJsp() {
		if(fechaSolicitudBaja!=null){
			try {
				return GstDate.getFormatedDateMedium("", fechaSolicitudBaja);
			} catch (ClsExceptions e) {
				e.printStackTrace();
				return "";
			}
		}else return "";
		
	}
	public String getObservacionesBaja() {
		return observacionesBaja;
	}
	public void setObservacionesBaja(String observacionesBaja) {
		this.observacionesBaja = observacionesBaja;
	}
	public String getFechaBaja() {
		return fechaBaja;
	}
	public String getFechaBajaJsp() {
		if(fechaBaja!=null){
			try {
				return GstDate.getFormatedDateShort("", fechaBaja);
			} catch (ClsExceptions e) {
				e.printStackTrace();
				return "";
			}
		}else return "";
		
	}
	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public String getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	public String getColegiadoNumero() {
		return colegiadoNumero;
	}
	public void setColegiadoNumero(String colegiadoNumero) {
		this.colegiadoNumero = colegiadoNumero;
	}
	public String getColegiadoNombre() {
		return colegiadoNombre;
	}
	public void setColegiadoNombre(String colegiadoNombre) {
		this.colegiadoNombre = colegiadoNombre;
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
	
	public void clear() {
		idInstitucion = null;
		idPersona = null;
		idTurno = null;
		idGuardia = null;
		fechaSolicitud = null;
		observacionesSolicitud = null;
		fechaValidacion = null;
		observacionesValidacion = null;
		fechaSolicitudBaja = null;
		observacionesBaja = null;
		fechaBaja = null;
		fechaDenegacion = null;
		observacionesDenegacion = null;
		observacionesValBaja = null;
		
		tipo = null;
		clase = null;
		turnosInscripcion = null;
		guardiasInscripcion = null;
		colegiadoNumero = null;
		colegiadoNombre = null;
		fechaDesde = null;
		fechaHasta = null;
		inscripcionesGuardia = null;
		inscripcionesTurno = null;
		estado = null;
		estadoPendientes= null;
		tipoGuardias= null;
		validarInscripciones= null;
		telefono1 = null;
		telefono2 = null;
		movil = null;
		fax1 = null;
		fax2 = null;
		idDireccion = null;
		
		retenciones= null;
		irpf = null;
		idRetencion = null;
		
		guardiasSel= null;
		setAbreviatura("");
		setNombre("");
		setArea("");
		setMateria("");
		setZona("");
		setSubzona("");
		gruposGuardiaLetrado = null;
		numeroGrupo = null;
		ordenGrupo = null;
		

	}
	public void reset(boolean isResetFechaSolicitud,boolean isResetFechaBaja) {
		
		if(isResetFechaSolicitud){
			fechaSolicitud = null;
			observacionesSolicitud = null;
		}
		if(isResetFechaBaja){
			fechaSolicitudBaja = null;
			observacionesBaja = null;
		}
		
		
		estadoPendientes = null;
		fechaDenegacion = null;
		observacionesDenegacion = null;
		fechaValidacion = null;
		observacionesValidacion = null;
		fechaBaja = null;
	
				

	}
	public List<ScsInscripcionTurnoBean> getInscripcionesTurno() {
		return inscripcionesTurno;
	}
	public void setInscripcionesTurno(
			List<ScsInscripcionTurnoBean> inscripcionesTurno) {
		this.inscripcionesTurno = inscripcionesTurno;
	}
	public List<ScsInscripcionGuardiaBean> getInscripcionesGuardia() {
		return inscripcionesGuardia;
	}
	public void setInscripcionesGuardia(
			List<ScsInscripcionGuardiaBean> inscripcionesGuardia) {
		this.inscripcionesGuardia = inscripcionesGuardia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public FilaExtElement[] getElementosFila() {
		//si las guardia no son obligatorias
		if((tipoGuardias==null || Integer.parseInt(tipoGuardias)!= ScsTurnoBean.TURNO_GUARDIAS_OBLIGATORIAS)){
			if(fechaBaja!=null && !fechaBaja.equals("")){
				elementosFila = new FilaExtElement[2];
				//cambiar fecha efectiva de baja
				elementosFila[1]=new FilaExtElement("cambiarFechaEfectiva","cambiarFechaEfectivaBaja",SIGAConstants.ACCESS_FULL);
				elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
			}else{
				if(fechaSolicitudBaja!=null&& !fechaSolicitudBaja.equals("")){
					elementosFila = new FilaExtElement[2];
					elementosFila[1]=new FilaExtElement("validar","validarBaja",SIGAConstants.ACCESS_FULL);
					elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
					//validar baja
					
				}else{
					if(fechaValidacion!=null&& !fechaValidacion.equals("")){
						elementosFila = new FilaExtElement[2];
						//cambiar fecha efectiva de alta
						elementosFila[1]=new FilaExtElement("cambiarFechaEfectiva","cambiarFechaEfectivaValidacion",SIGAConstants.ACCESS_FULL);
						elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
						
					}else{
						elementosFila = new FilaExtElement[2];
						if(fechaDenegacion!=null && !fechaDenegacion.equalsIgnoreCase("")){
							elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
						}else{
							elementosFila[1]=new FilaExtElement("validar","validar",SIGAConstants.ACCESS_FULL);
							elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);	
						}
						
					}
				}
				
			}
		}else{
			elementosFila =  new FilaExtElement[1];
			elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
		}
		return elementosFila;
	}
	
	
	public void setElementosFila(FilaExtElement[]elementosFila ) {
		this.elementosFila = elementosFila;
		
		
			
		
	}
	public List<ScsTurnoBean> getTurnosInscripcion() {
		return turnosInscripcion;
	}
	public void setTurnosInscripcion(List<ScsTurnoBean> turnosInscripcion) {
		this.turnosInscripcion = turnosInscripcion;
	}
	public List<ScsGuardiasTurnoBean> getGuardiasInscripcion() {
		return guardiasInscripcion;
	}
	public void setGuardiasInscripcion(
			List<ScsGuardiasTurnoBean> guardiasInscripcion) {
		this.guardiasInscripcion = guardiasInscripcion;
	}
	public ScsInscripcionTurnoBean getInscripcionTurno() {
		return inscripcionTurno;
	}
	public void setInscripcionTurno(ScsInscripcionTurnoBean inscripcionTurno) {
		this.inscripcionTurno = inscripcionTurno;
	}
	public String getTipoGuardias() {
		return tipoGuardias;
	}
	public void setTipoGuardias(String tipoGuardias) {
		this.tipoGuardias = tipoGuardias;
	}
	public String getValidarInscripciones() {
		return validarInscripciones;
	}
	public void setValidarInscripciones(String validarInscripciones) {
		this.validarInscripciones = validarInscripciones;
	}
	public String getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}
	public String getIdRetencion() {
		return idRetencion;
	}
	public void setIdRetencion(String idRetencion) {
		this.idRetencion = idRetencion;
	}
	public String getGuardiasSel() {
		return guardiasSel;
	}
	public void setGuardiasSel(String guardiasSel) {
		this.guardiasSel = guardiasSel;
	}
	public String getTurnosSel() {
		return turnosSel;
	}
	public void setTurnosSel(String turnosSel) {
		this.turnosSel = turnosSel;
	}
	
	//datos del formulario de turnos disponibles...
	public String getArea(){
		return (String)this.datos.get("IDAREA");
	}
	
	public String getMateria(){
		return (String)this.datos.get("IDMATERIA");
	}
	
	public String getZona(){
		return (String)this.datos.get("IDZONA");
	}
	public String getSubzona(){
		return (String)this.datos.get("IDSUBZONA");
	}
	public void setAbreviatura (String valor){ 
		this.datos.put("ABREVIATURA", valor.toUpperCase());
	}
	
	public void setNombre (String valor){ 
		this.datos.put("NOMBRE", valor.toUpperCase());
	}

	public void setArea (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDAREA", aux);
		}
		else this.datos.put("IDAREA",valor);
	}
	
	public void setMateria (String valor){ 
		this.datos.put("IDMATERIA", valor);
	}
	
	public void setZona (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDZONA", aux);
		}
		else this.datos.put("IDZONA",valor);
	}
	public void setSubzona (String valor){ 
		this.datos.put("IDSUBZONA", valor);
	}
	public boolean isSolicitudAlta() {
		return solicitudAlta;
	}
	public void setSolicitudAlta(boolean solicitudAlta) {
		this.solicitudAlta = solicitudAlta;
	}
	public boolean isSolicitudBaja() {
		return solicitudBaja;
	}
	public void setSolicitudBaja(boolean solicitudBaja) {
		this.solicitudBaja = solicitudBaja;
	}
	public boolean isValidacionAlta() {
		return validacionAlta;
	}
	public void setValidacionAlta(boolean validacionAlta) {
		this.validacionAlta = validacionAlta;
	}
	public boolean isValidacionBaja() {
		return validacionBaja;
	}
	public void setValidacionBaja(boolean validacionBaja) {
		this.validacionBaja = validacionBaja;
	}
	public boolean isMasivo() {
		return masivo;
	}
	public void setMasivo(boolean masivo) {
		this.masivo = masivo;
	}
	public String getFechaValidacionTurno() {
		return fechaValidacionTurno;
	}
	public void setFechaValidacionTurno(String fechaValidacionTurno) {
		this.fechaValidacionTurno = fechaValidacionTurno;
	}
	public String getFechaBajaTurno() {
		return fechaBajaTurno;
	}
	public void setFechaBajaTurno(String fechaBajaTurno) {
		this.fechaBajaTurno = fechaBajaTurno;
	}
	public String getFechaDenegacion() {
		return fechaDenegacion;
	}
	public void setFechaDenegacion(String fechaDenegacion) {
		this.fechaDenegacion = fechaDenegacion;
	}
	public String getObservacionesDenegacion() {
		return observacionesDenegacion;
	}
	public void setObservacionesDenegacion(String observacionesDenegacion) {
		this.observacionesDenegacion = observacionesDenegacion;
	}
	public String getFechaActiva() {
		return fechaActiva;
	}
	public void setFechaActiva(String fechaActiva) {
		this.fechaActiva = fechaActiva;
	}
	public String getFechaSolicitudTurno() {
		return fechaSolicitudTurno;
	}
	public void setFechaSolicitudTurno(String fechaSolicitudTurno) {
		this.fechaSolicitudTurno = fechaSolicitudTurno;
	}
	public String getObservacionesValBaja() {
		return observacionesValBaja;
	}
	public void setObservacionesValBaja(String observacionesValBaja) {
		this.observacionesValBaja = observacionesValBaja;
	}
	public List<LetradoInscripcion> getGruposGuardiaLetrado() {
		return gruposGuardiaLetrado;
	}
	public void setGruposGuardiaLetrado(List<LetradoInscripcion> gruposGuardiaLetrado) {
		this.gruposGuardiaLetrado = gruposGuardiaLetrado;
	}
	public String getNumeroGrupo() {
		return numeroGrupo;
	}
	public void setNumeroGrupo(String numeroGrupo) {
		this.numeroGrupo = numeroGrupo;
	}
	public String getOrdenGrupo() {
		return ordenGrupo;
	}
	public void setOrdenGrupo(String ordenGrupo) {
		this.ordenGrupo = ordenGrupo;
	}
	public String getPorGrupos() {
		return porGrupos;
	}
	public void setPorGrupos(String porGrupos) {
		this.porGrupos = porGrupos;
	}
	
}