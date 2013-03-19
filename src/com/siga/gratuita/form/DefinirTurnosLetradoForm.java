package com.siga.gratuita.form;

/**
 * @author ruben.fernandez
 */
import com.siga.general.*;
import java.util.*;


public class DefinirTurnosLetradoForm extends MasterForm {

	public String incluirRegistrosConBajaLogica = "N";
	public String fechaConsulta;
	
	public String idpersona = "";
	public String idinstitucion = "";
	public String accion = "";
	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	
	public String getIdPersona() {
		return this.idpersona;
	}
	
	public void setIdPersona(String s) {
		this.idpersona = s;
	}
	
	public String getIdInstitucion() {
		return this.incluirRegistrosConBajaLogica;
	}
	
	public void setIdInstitucion(String s) {
		this.idinstitucion = s;
	}

	public String getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
}