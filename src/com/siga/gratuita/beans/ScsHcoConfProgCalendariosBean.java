package com.siga.gratuita.beans;

import com.atos.utils.UsrBean;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.gratuita.form.HcoConfProgrCalendarioForm;
/**
 * 
 * @author jorgeta
 *
 */
public class ScsHcoConfProgCalendariosBean  extends MasterBean{
	static public final Short estadoProgramado = new Short("0"); 
	static public final Short estadoProcesando = new Short("1");
	static public final Short estadoError = new Short("2");
	static public final Short estadoFinalizado = new Short("3");
	static public final Short estadoCancelado = new Short("4");
	static public final Short estadoReprogramado = new Short("5");

	static public String T_NOMBRETABLA = "SCS_HCO_CONF_PROG_CALENDARIOS";
	UsrBean usrBean;

	// Nombre campos de la tabla
	static public final String C_IDCONJUNTOGUARDIA = "IDCONJUNTOGUARDIA";
	static public final String C_IDPROGCALENDARIO = "IDPROGCALENDARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTURNO = "IDTURNO";
	static public final String C_IDGUARDIA = "IDGUARDIA";
	static public final String C_ORDEN = "ORDEN";
	static public final String C_ESTADO = "ESTADO";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	
	private Long idConjuntoGuardia;
	private Long idProgrCalendario;
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	ScsGuardiasTurnoBean guardia;
	private Short estado;
	private Short orden;
	public Long getIdConjuntoGuardia() {
		return idConjuntoGuardia;
	}
	public void setIdConjuntoGuardia(Long idConjuntoGuardia) {
		this.idConjuntoGuardia = idConjuntoGuardia;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	
	
	public Integer getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	public Integer getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	public Short getOrden() {
		return orden;
	}
	public void setOrden(Short orden) {
		this.orden = orden;
	}
	
	public HcoConfProgrCalendarioForm getHcoConfProgrCalendarioForm() {
		HcoConfProgrCalendarioForm hcoConfProgrCalendarioForm = new HcoConfProgrCalendarioForm();
		if(idConjuntoGuardia!=null)
			hcoConfProgrCalendarioForm.setIdConjuntoGuardia(idConjuntoGuardia.toString());
		if(idProgrCalendario!=null)
			hcoConfProgrCalendarioForm.setIdProgrCalendario(idProgrCalendario.toString());
		if(idInstitucion!=null)
			hcoConfProgrCalendarioForm.setIdInstitucion(idInstitucion.toString());
		if(idTurno!=null)
			hcoConfProgrCalendarioForm.setIdTurno(idTurno.toString());
		if(idGuardia!=null)
			hcoConfProgrCalendarioForm.setIdGuardia(idGuardia.toString());
		if(orden!=null)
			hcoConfProgrCalendarioForm.setOrden(orden.toString());
		if(estado!=null)
			hcoConfProgrCalendarioForm.setEstado(estado.toString());
		
		return hcoConfProgrCalendarioForm;
	}
	public Long getIdProgrCalendario() {
		return idProgrCalendario;
	}
	public void setIdProgrCalendario(Long idProgrCalendario) {
		this.idProgrCalendario = idProgrCalendario;
	}
	public Short getEstado() {
		return estado;
	}
	public void setEstado(Short estado) {
		this.estado = estado;
	}
	public ScsGuardiasTurnoBean getGuardia() {
		return guardia;
	}
	public void setGuardia(ScsGuardiasTurnoBean guardia) {
		this.guardia = guardia;
	}
	

	
}
