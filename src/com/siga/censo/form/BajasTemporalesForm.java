package com.siga.censo.form;

/**
 * @author jtacosta
 */
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;

public class BajasTemporalesForm extends MasterForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6545073248803061578L;

	//Campos para la tabla de turnos
	String idInstitucion = null;
	
	String idPersona = null;
	String colegiadoNumero;
	String colegiadoNombre;
	
	String tipo; 
	
	
	String fechaBT;
	String fechaDesde;
	String fechaHasta;
	String fechaAlta;
	String validado;
	String fechaEstado;

	String descripcion;
	String situacion;

	String estadoBaja;
	
	String idTurno;
	String idGuardia;
	
	List<ScsTurnoBean> turnos;
	List<ScsGuardiasTurnoBean> guardias;
	
	
	List<CenBajasTemporalesBean> bajasTemporales;
	List<CenPersonaBean> personasDeBaja;
	List<CenPersonaBean> personasDeGuardia;
	
	String msgError;
	String msgAviso;
	String datosSeleccionados;
	
	boolean fichaColegial=false; 
	
		public String incluirRegistrosConBajaLogica = "N";

	String botones ="";
	UsrBean usrBean;
	//aalg: para saber si entra como consulta o edición
	String accion = ""; 
	
	public String getBotones() {
		botones = "";
		//asi nos aseguramos que se sea el usr bean
		if(usrBean!=null){
			if(fechaDesde!=null && !fechaDesde.equals("")){
				if(usrBean.isLetrado()){
				
					if(validado==null||validado.equals("")){
						botones ="E,B";
					}else{
						botones="";
					}
				}else{
					botones ="E,B";
					
				}
				
				
			}else{
				botones="";
				
			}
		}
		return botones;
	}

	public void setBotones(String botones) {
		this.botones = botones;
	}

	
	public String getDatosSeleccionados() {
		return datosSeleccionados;
	}

	public void setDatosSeleccionados(String datosSeleccionados) {
		this.datosSeleccionados = datosSeleccionados;
	}

	public CenBajasTemporalesBean getBajaTemporalBean() throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporalBean = new CenBajasTemporalesBean();
		bajaTemporalBean.setUsrBean(usrBean);
		//Esta fecha vienen de formulario por lo que se formatea
		if(fechaDesde!=null && !fechaDesde.equals("")){
			bajaTemporalBean.setFechaDesde(GstDate.getApplicationFormatDate("", fechaDesde));
		}
		//Esta fecha vienen de formulario por lo que se formatea
		if(fechaHasta!=null && !fechaHasta.equals("")){
			bajaTemporalBean.setFechaHasta(GstDate.getApplicationFormatDate("", fechaHasta));
		}
		if(idInstitucion!=null && !idInstitucion.equals(""))
			bajaTemporalBean.setIdInstitucion(new Integer(idInstitucion));
		if(idPersona!=null && !idPersona.equals(""))
			bajaTemporalBean.setIdPersona(new Long(idPersona));
		if(fechaBT!=null && !fechaBT.equals(""))
			bajaTemporalBean.setFechaBT(GstDate.getApplicationFormatDate("", fechaBT));
		if(tipo!=null && !tipo.equals(""))
			bajaTemporalBean.setTipo(tipo);
		//esta fecha nunca estan en el formulario como mucho viene hidden pero ya formateada
		if(fechaAlta!=null && !fechaAlta.equals(""))
			bajaTemporalBean.setFechaAlta(fechaAlta);
		//esta fecha nunca estan en el formulario como mucho viene hidden pero ya formateada
		if(fechaEstado!=null && !fechaEstado.equals(""))
			bajaTemporalBean.setFechaEstado(fechaEstado);
		if(validado!=null && !validado.equals(""))
			bajaTemporalBean.setValidado(validado);
		if(descripcion!=null && !descripcion.equals(""))
			bajaTemporalBean.setDescripcion(descripcion);
		
		
		
		
		return bajaTemporalBean;
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


	
	public String getTipo() {
		return tipo;
	}
	public String getTipoDescripcion() {
		String tipoDescripcion = "";
		if(tipo.equals("V")){
			tipoDescripcion=UtilidadesString.getMensajeIdioma(usrBean, "censo.bajastemporales.tipo.vacaciones");
		}else if(tipo.equals("B")){
			tipoDescripcion=UtilidadesString.getMensajeIdioma(usrBean, "censo.bajastemporales.tipo.baja");
		}else if(tipo.equals("M")){
			tipoDescripcion=UtilidadesString.getMensajeIdioma(usrBean, "censo.bajastemporales.tipo.maternidad");
			
		}else if(tipo.equals("S")){
			tipoDescripcion=UtilidadesString.getMensajeIdioma(usrBean, "censo.bajastemporales.tipo.suspension");
			
		}
		
		
		return tipoDescripcion;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFechaBT() {
		return fechaBT;
	}


	public void setFechaBT(String fechaBT) {
		this.fechaBT = fechaBT;
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


	public String getFechaAlta() {
		return fechaAlta;
	}


	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	

	public String getDescripcion() {
		if(descripcion==null)
			descripcion="";
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	

	public List<CenBajasTemporalesBean> getBajasTemporales() {
		return bajasTemporales;
	}

	public void setBajasTemporales(List<CenBajasTemporalesBean> bajasTemporales) {
		this.bajasTemporales = bajasTemporales;
	}

	

	

	public String getEstadoBajaTxt() {
		String estadoBajaTxt = "-";
		if(fechaDesde!=null && !fechaDesde.equals("")){
			if(validado == null || validado.equals("")){
				estadoBajaTxt = "Pendiente";
			}else if(validado.equals("1")){
				estadoBajaTxt = "Aceptado";
			}else if(validado.equals("0")){
				estadoBajaTxt = "Denegado";
			}
		}
		
		return estadoBajaTxt;
	}
	public String getEstadoBaja() {
	
		return estadoBaja;
	}

	public void setEstadoBaja(String estadoBaja) {
		this.estadoBaja = estadoBaja;
	}

	public String getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}

	public String getIdGuardia() {
		return idGuardia;
	}

	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}

	public List<ScsTurnoBean> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<ScsTurnoBean> turnos) {
		this.turnos = turnos;
	}

	public List<ScsGuardiasTurnoBean> getGuardias() {
		return guardias;
	}

	public void setGuardias(List<ScsGuardiasTurnoBean> guardias) {
		this.guardias = guardias;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void clear() {
		idInstitucion = null;
		
		idPersona = null;
		colegiadoNumero = null;
		colegiadoNombre = null;
		
		tipo = null; 
		
		
		fechaBT = null;
		fechaDesde = null;
		fechaHasta = null;
		fechaAlta = null;
		
		situacion=null;
		descripcion ="";
		validado = null;
		
		estadoBaja = null;
		
		idTurno = null;
		idGuardia = null;
		
		turnos = null;
		guardias = null;
		
		botones = "";
		
		bajasTemporales = null;
		personasDeBaja=null;
		personasDeGuardia=null;
		
		msgError = null;
		msgAviso= null;
		
		fichaColegial = false;
		
		incluirRegistrosConBajaLogica = "N";
	}

	

	

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgAviso() {
		return msgAviso;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	

	public String getValidado() {
		return validado;
	}

	public void setValidado(String validado) {
		this.validado = validado;
	}

	public String getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getSituacion() {
		return situacion;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public boolean isFichaColegial() {
		return fichaColegial;
	}

	public void setFichaColegial(boolean fichaColegial) {
		this.fichaColegial = fichaColegial;
	}

	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public List<CenPersonaBean> getPersonasDeBaja() {
		return personasDeBaja;
	}

	public void setPersonasDeBaja(List<CenPersonaBean> personasDeBaja) {
		this.personasDeBaja = personasDeBaja;
	}


	public List<CenPersonaBean> getPersonasDeGuardia() {
		return personasDeGuardia;
	}

	public void setPersonasDeGuardia(List<CenPersonaBean> personasDeGuardia) {
		this.personasDeGuardia = personasDeGuardia;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	
}
