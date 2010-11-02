/*
 * Created on Mar 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.util.ArrayList;
import java.util.Map;

import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsTurnoBean;

/**
 * @author A203486
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LetradoGuardia implements Cloneable {

	private String saltoCompensacion = null; //Valores = S(Salto)/ N(No)/ C(Compensacion)
	private String idSaltoCompensacion = null;
	private Long idPersona;
	private Integer idInstitucion, idTurno, idGuardia;
	private ArrayList periodoGuardias;
	private Map<String,CenBajasTemporalesBean> bajasTemporales;
	CenBajasTemporalesBean bajaTemporal;
	CenPersonaBean persona = null;
	Integer posicion;
	String fechaValidacion;
	String fechaBaja;

	public String getFechaValidacion() {
		return fechaValidacion;
	}

	public void setFechaValidacion(String fechaValidacion) {
		this.fechaValidacion = fechaValidacion;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public CenPersonaBean getPersona() {
		return persona;
	}
	
	public void setPersona(CenPersonaBean persona) {
		this.persona = persona;
	}

	public CenBajasTemporalesBean getBajaTemporal() {
		return bajaTemporal;
	}

	public void setBajaTemporal(CenBajasTemporalesBean bajaTemporal) {
		this.bajaTemporal = bajaTemporal;
	}

	public Object clone() throws CloneNotSupportedException {
		LetradoGuardia  obj = null;
		
		try {
			obj = (LetradoGuardia)super.clone();
        } catch(CloneNotSupportedException ex){
            obj = null;
        }
        if (this.periodoGuardias!=null)
		obj.periodoGuardias = (ArrayList)this.periodoGuardias.clone();
		return obj;
	}
	
	public LetradoGuardia() {
		
	}
	
	public LetradoGuardia(Long idPersona, Integer idInstitucion, Integer idTurno, Integer idGuardia, String saltoCompensacion){
		this.idPersona = idPersona;
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.saltoCompensacion = saltoCompensacion;
	}

	public LetradoGuardia(Long idPersona, Integer idInstitucion, Integer idTurno, Integer idGuardia){
		this.idPersona = idPersona;
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
	}

	public LetradoGuardia(Long idPersona, Integer idInstitucion, Integer idTurno) {
		this.idPersona = idPersona;
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the idGuardia.
	 */
	public Integer getIdGuardia() {
		return idGuardia;
	}
	/**
	 * @param idGuardia The idGuardia to set.
	 */
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	/**
	 * @return Returns the periodoGuardias.
	 */
	public ArrayList getPeriodoGuardias() {
		return periodoGuardias;
	}
	/**
	 * @param periodoGuardias The periodoGuardias to set.
	 */
	public void setPeriodoGuardias(ArrayList periodoGuardias) {
		this.periodoGuardias = periodoGuardias;
	}
	/**
	 * @return Returns the saltoCompensacion.
	 */
	public String getSaltoCompensacion() {
		return saltoCompensacion;
	}
	/**
	 * @param saltoCompensacion The saltoCompensacion to set.
	 */
	public void setSaltoCompensacion(String saltoCompensacion) {
		this.saltoCompensacion = saltoCompensacion;
	}

	public Map<String, CenBajasTemporalesBean> getBajasTemporales() {
		return bajasTemporales;
	}

	public void setBajasTemporales(
			Map<String, CenBajasTemporalesBean> bajasTemporales) {
		this.bajasTemporales = bajasTemporales;
	}

	public String getIdSaltoCompensacion() {
		return idSaltoCompensacion;
	}

	public void setIdSaltoCompensacion(String idSaltoCompensacion) {
		this.idSaltoCompensacion = idSaltoCompensacion;
	}
}
