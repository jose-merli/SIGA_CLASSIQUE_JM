package com.siga.gratuita.form;

import java.util.List;

import com.siga.general.MasterForm;
import com.siga.gratuita.beans.ScsConfConjuntoGuardiasBean;

/**
 * 
 * @author jorgeta
 *
 */
public class ConfConjuntoGuardiasForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idConjuntoGuardia;
	private String idInstitucion;
	private String idTurno;
	private String idGuardia;
	private String orden;
	DefinirTurnosForm turno;
	DefinirGuardiasTurnosForm guardia;
	private String guardiasInsertar;
	private String guardiasBorrar;
	private String mostrarSoloGuardiasConfiguradas;
	
	
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
	
	
	public ScsConfConjuntoGuardiasBean getConfConjuntoGuardiaVO() {
		ScsConfConjuntoGuardiasBean confConjuntoGuardiasBean = new ScsConfConjuntoGuardiasBean();
		if(idConjuntoGuardia!=null)
			confConjuntoGuardiasBean.setIdConjuntoGuardia(new Long(idConjuntoGuardia));
		
		if(idInstitucion!=null)
			confConjuntoGuardiasBean.setIdInstitucion(new Integer(idInstitucion));
		
		if(idTurno!=null)
			confConjuntoGuardiasBean.setIdTurno(new Integer(idTurno));
		if(idGuardia!=null)
			confConjuntoGuardiasBean.setIdGuardia(new Integer(idGuardia));
		if(orden!=null)
			confConjuntoGuardiasBean.setOrden(new Short(orden));
		
		return confConjuntoGuardiasBean;
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
	public String getGuardiasInsertar() {
		return guardiasInsertar;
	}
	public void setGuardiasInsertar(String guardiasInsertar) {
		this.guardiasInsertar = guardiasInsertar;
	}
	public String getGuardiasBorrar() {
		return guardiasBorrar;
	}
	public void setGuardiasBorrar(String guardiasBorrar) {
		this.guardiasBorrar = guardiasBorrar;
	}
	public String getMostrarSoloGuardiasConfiguradas() {
		return mostrarSoloGuardiasConfiguradas;
	}
	public void setMostrarSoloGuardiasConfiguradas(
			String mostrarSoloGuardiasConfiguradas) {
		this.mostrarSoloGuardiasConfiguradas = mostrarSoloGuardiasConfiguradas;
	}
	
	
}
