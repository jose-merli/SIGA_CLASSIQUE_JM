package com.siga.gratuita.util.calendarioSJCS;

import java.util.ArrayList;
import java.util.Map;

import com.atos.utils.ClsExceptions;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoBean;

/**
 * Created on Mar 15, 2006
 */
public class LetradoGuardia implements Cloneable
{
	// Atributos
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Long idPersona;
	private ArrayList periodoGuardias;
	
	private String saltoCompensacion; //Valores = S(Salto)/ N(No)/ C(Compensacion)
	private String idSaltoCompensacion;
	private CenBajasTemporalesBean bajaTemporal;
	private Map<String, CenBajasTemporalesBean> bajasTemporales;
	private String idSaltoCompensacionGrupo;
	
	private CenPersonaBean persona;
	private ScsInscripcionGuardiaBean inscripcionGuardia;
	private ScsInscripcionTurnoBean inscripcionTurno;
	private Integer posicion;
	private Long idGrupoGuardiaColegiado;
	private Integer grupo;
	private Integer ordenGrupo;
	private String numeroGrupo;

	
	// Constructores
	public LetradoGuardia(CenPersonaBean perBean, Integer idInstitucion, Integer idTurno, Integer idGuardia,
			String saltoCompensacion)
	{
		this.persona = perBean;
		this.idPersona = perBean.getIdPersona();
		
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.saltoCompensacion = saltoCompensacion;
	}	
	public LetradoGuardia(ScsInscripcionGuardiaBean inscripcionGuardia,
			Map<String, CenBajasTemporalesBean> bajasTemporales) throws ClsExceptions
	{
		// inicializando letrado
		this.persona = inscripcionGuardia.getPersona();
		this.idPersona = this.persona.getIdPersona();

		this.idInstitucion = inscripcionGuardia.getIdInstitucion();
		this.idTurno = inscripcionGuardia.getIdTurno();
		this.idGuardia = inscripcionGuardia.getIdGuardia();
		this.saltoCompensacion = null;
		this.inscripcionGuardia = inscripcionGuardia;

		// rellenando bajas temporales
		this.bajasTemporales = bajasTemporales;

		// datos de grupo
		this.numeroGrupo = inscripcionGuardia.getNumeroGrupo();
		this.idGrupoGuardiaColegiado = inscripcionGuardia.getIdGrupoGuardiaColegiado();
		this.grupo = inscripcionGuardia.getGrupo();
		this.ordenGrupo = inscripcionGuardia.getOrdenGrupo();
	}	
	public LetradoGuardia(ScsInscripcionTurnoBean inscripcionTurno,
			Map<String, CenBajasTemporalesBean> bajasTemporales) throws ClsExceptions
	{
		// inicializando letrado
		this.persona = inscripcionTurno.getPersona();
		this.idPersona = this.persona.getIdPersona();

		this.idInstitucion = inscripcionTurno.getIdInstitucion();
		this.idTurno = inscripcionTurno.getIdTurno();
		this.saltoCompensacion = null;
		this.inscripcionTurno = inscripcionTurno;

		// rellenando bajas temporales
		this.bajasTemporales = bajasTemporales;
	}	
	

	// Setters
	public void setIdInstitucion(Integer valor) {this.idInstitucion = valor;}
	public void setIdTurno(Integer valor) {this.idTurno = valor;}
	public void setIdGuardia(Integer valor) {this.idGuardia = valor;}
	public void setIdPersona(Long valor) {this.idPersona = valor;}
	public void setPeriodoGuardias(ArrayList valor) {this.periodoGuardias = valor;}
	
	public void setSaltoCompensacion(String valor) {this.saltoCompensacion = valor;}
	public void setIdSaltoCompensacion(String valor) {this.idSaltoCompensacion = valor;}
	public void setBajaTemporal(CenBajasTemporalesBean valor) {this.bajaTemporal = valor;}
	public void setBajasTemporales(Map<String, CenBajasTemporalesBean> valor) {this.bajasTemporales = valor;}
	public void setIdSaltoCompensacionGrupo(String idSaltoCompensacionGrupo) {this.idSaltoCompensacionGrupo = idSaltoCompensacionGrupo;}
	
	public void setPersona(CenPersonaBean valor) {this.persona = valor;}
	public void setInscripcionGuardia(ScsInscripcionGuardiaBean inscripcionGuardia) {this.inscripcionGuardia = inscripcionGuardia;}
	public void setInscripcionTurno(ScsInscripcionTurnoBean inscripcionTurno) {this.inscripcionTurno = inscripcionTurno;}
	public void setPosicion(Integer valor) {this.posicion = valor;}
	public void setIdGrupoGuardiaColegiado(Long valor) {this.idGrupoGuardiaColegiado = valor;}
	public void setGrupo(Integer valor) {this.grupo = valor;}
	public void setOrdenGrupo(Integer valor) {this.ordenGrupo = valor;}
	public void setNumeroGrupo(String valor) {this.numeroGrupo = valor;}
	
	
	// Getters
	public Integer getIdInstitucion() {return this.idInstitucion;}
	public Integer getIdTurno() {return this.idTurno;}
	public Integer getIdGuardia() {return this.idGuardia;}
	public Long getIdPersona() {return this.idPersona;}
	public ArrayList getPeriodoGuardias() {return this.periodoGuardias;}
	
	public String getSaltoCompensacion() {return this.saltoCompensacion;}
	public String getIdSaltoCompensacion() {return this.idSaltoCompensacion;}
	public CenBajasTemporalesBean getBajaTemporal() {return this.bajaTemporal;}
	public Map<String, CenBajasTemporalesBean> getBajasTemporales() {return this.bajasTemporales;}
	public String getIdSaltoCompensacionGrupo() {return idSaltoCompensacionGrupo;}
	
	public CenPersonaBean getPersona() {return this.persona;}
	public ScsInscripcionGuardiaBean getInscripcionGuardia() {return inscripcionGuardia;}
	public ScsInscripcionTurnoBean getInscripcionTurno() {return inscripcionTurno;}
	public Integer getPosicion() {return this.posicion;}
	public Long getIdGrupoGuardiaColegiado() {return this.idGrupoGuardiaColegiado;}
	public Integer getGrupo() {return this.grupo;}
	public Integer getOrdenGrupo() {return this.ordenGrupo;}
	public String getNumeroGrupo() {return this.numeroGrupo;}
	
	// Otros metodos
	public Object clone() throws CloneNotSupportedException {
		LetradoGuardia obj = null;

		try {
			obj = (LetradoGuardia) super.clone();
		} catch (CloneNotSupportedException ex) {
			obj = null;
		}
		if (this.periodoGuardias != null)
			obj.periodoGuardias = (ArrayList) this.periodoGuardias.clone();
		return obj;
	}
	
	public String toString() {
		String grupo;
		if (this.numeroGrupo != null && !this.numeroGrupo.equals(""))
			grupo = "(Grupo " + this.numeroGrupo + ")";
		else
			grupo = "";
		
		if (persona == null)
			return '"' + "-" + '"';
		else
			return '"' + persona.getApellido1() + " " + persona.getApellido2() + ", " + persona.getNombre() + '"' + " " + grupo;
	}
	
}
