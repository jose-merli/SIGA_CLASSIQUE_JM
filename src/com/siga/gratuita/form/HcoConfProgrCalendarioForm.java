package com.siga.gratuita.form;

import com.siga.general.MasterForm;
import com.siga.gratuita.beans.ScsHcoConfProgCalendariosBean;
import com.siga.tlds.FilaExtElement;

/**
 * 
 * @author jorgeta
 *
 */
public class HcoConfProgrCalendarioForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idProgrCalendario;
	private String idConjuntoGuardia;
	private String idInstitucion;
	private String idTurno;
	private String idGuardia;
	private String orden;
	private String estado;
	DefinirTurnosForm turno;
	DefinirGuardiasTurnosForm guardia;
	
	
	
	private FilaExtElement[] elementosFila;
	private String idCalendarioGuardias;
	
	public String getIdConjuntoGuardia() {
		return idConjuntoGuardia;
	}
	public void setIdConjuntoGuardia(String idConjuntoGuardia) {
		this.idConjuntoGuardia = idConjuntoGuardia;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	
	public ScsHcoConfProgCalendariosBean getHcoConfProgCalendariosVO() {
		ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = new ScsHcoConfProgCalendariosBean();
		if(idConjuntoGuardia!=null)
			hcoConfProgCalendariosBean.setIdConjuntoGuardia(new Long(idConjuntoGuardia));
		if(idProgrCalendario!=null)
			hcoConfProgCalendariosBean.setIdProgrCalendario(new Long(idProgrCalendario));
		
		if(idInstitucion!=null)
			hcoConfProgCalendariosBean.setIdInstitucion(new Integer(idInstitucion));
		
		if(idTurno!=null)
			hcoConfProgCalendariosBean.setIdTurno(new Integer(idTurno));
		if(idGuardia!=null)
			hcoConfProgCalendariosBean.setIdGuardia(new Integer(idGuardia));
		if(orden!=null)
			hcoConfProgCalendariosBean.setOrden(new Short(orden));
		if(estado!=null)
			hcoConfProgCalendariosBean.setEstado(new Short(estado));
		
		return hcoConfProgCalendariosBean;
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
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public DefinirTurnosForm getTurno() {
		return turno;
	}
	public void setTurno(DefinirTurnosForm turno) {
		this.turno = turno;
	}
	public DefinirGuardiasTurnosForm getGuardia() {
		return guardia;
	}
	public void setGuardia(DefinirGuardiasTurnosForm guardia) {
		this.guardia = guardia;
	}
	
	public String getIdProgrCalendario() {
		return idProgrCalendario;
	}
	public void setIdProgrCalendario(String idProgrCalendario) {
		this.idProgrCalendario = idProgrCalendario;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public FilaExtElement[] getElementosFila() {
		return elementosFila;
	}
	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}
	public String getIdCalendarioGuardias() {
		return idCalendarioGuardias;
	}
	public void setIdCalendarioGuardias(String idCalendarioGuardias) {
		this.idCalendarioGuardias = idCalendarioGuardias;
	}
	
	
}
