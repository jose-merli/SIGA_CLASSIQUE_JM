package com.siga.gratuita.form;

import java.util.Hashtable;
import com.siga.general.MasterForm;

/**
 * @author carlos.vidal
 */
public class RetencionesIRPFForm extends MasterForm
{
	// Atributos
	String FECHAINICIO = null;
	String FECHAFIN = null;
	String LETRA = null;
	String DESCRIPCION = null;
	Integer IDRETENCION = null;
	String RETENCION = null;
	Hashtable BACKUP = null;
	Integer SOCIEDADESCLIENTE = null;
	String idPersona = null;
	String idInstitucion = null;
	Integer periodo = null;
	Integer anyoInformeIRPF = null;
	String idioma;

	
	// GETTERS
	public String getFechaInicio() { return FECHAINICIO; }
	public String getFechaFin() { return FECHAFIN; }
	public String getLetra() { return LETRA; }
	public String getDescripcion() { return DESCRIPCION; }
	public Integer getIdRetencion() { return IDRETENCION; }
	public String getRetencion() { return RETENCION; }
	public Hashtable getBackup() { return BACKUP; }
	public Integer getSociedadesCliente() { return SOCIEDADESCLIENTE; }
	public String getIdPersona() { return idPersona; }
	public String getIdInstitucion() { return idInstitucion; }
	public Integer getAnyoInformeIRPF() { return anyoInformeIRPF; }
	public Integer getPeriodo() { return periodo; }
	public String getIdioma() { return idioma; }
	
	
	// SETTERS
	public void setFechaInicio(String dato) { FECHAINICIO = dato; }
	public void setFechaFin(String dato) { FECHAFIN = dato; }
	public void setLetra(String dato) { LETRA = dato; }
	public void setDescripcion(String dato) { DESCRIPCION = dato; }
	public void setIdRetencion(Integer dato) { IDRETENCION = dato; }
	public void setRetencion(String dato) { RETENCION = dato; }
	public void setBackup(Hashtable dato) { BACKUP = dato; }
	public void setSociedadesCliente(Integer dato) { SOCIEDADESCLIENTE = dato; }
	public void setIdPersona(String idPersona) { this.idPersona = idPersona; }
	public void setIdInstitucion(String idInstitucion) { this.idInstitucion = idInstitucion; }
	public void setPeriodo(Integer periodo) { this.periodo = periodo; }
	public void setAnyoInformeIRPF(Integer anyoInformeIRPF) { this.anyoInformeIRPF = anyoInformeIRPF; }
	public void setIdioma(String idioma) { this.idioma = idioma; }

}