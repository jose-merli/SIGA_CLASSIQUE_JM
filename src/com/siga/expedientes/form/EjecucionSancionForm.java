/*
 * Created on Feb 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Form del action EjecucionSancionAction
 */
public class EjecucionSancionForm extends MasterForm {
	
	private boolean bajaTurno;
	private boolean bajaColegial;
	private boolean bajaEjercicio;
	private boolean inhabilitacion;
	private boolean suspension;
	private String motivo="";

	
	
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public boolean isBajaColegial() {
		return bajaColegial;
	}
	public void setBajaColegial(boolean bajaColegial) {
		this.bajaColegial = bajaColegial;
	}
	public boolean isBajaEjercicio() {
		return bajaEjercicio;
	}
	public void setBajaEjercicio(boolean bajaEjercicio) {
		this.bajaEjercicio = bajaEjercicio;
	}
	public boolean isBajaTurno() {
		return bajaTurno;
	}
	public void setBajaTurno(boolean bajaTurno) {
		this.bajaTurno = bajaTurno;
	}
	public boolean isInhabilitacion() {
		return inhabilitacion;
	}
	public void setInhabilitacion(boolean inhabilitacion) {
		this.inhabilitacion = inhabilitacion;
	}
	public boolean isSuspension() {
		return suspension;
	}
	public void setSuspension(boolean suspension) {
		this.suspension = suspension;
	}
}
