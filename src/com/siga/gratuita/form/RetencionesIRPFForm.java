package com.siga.gratuita.form;

/**
 * @author carlos.vidal
 */
import java.util.Hashtable;

import com.siga.general.MasterForm;

public class RetencionesIRPFForm extends MasterForm {
	String FECHAINICIO 	= null;
	String FECHAFIN 	= null;
	String LETRA 		= null;
	String DESCRIPCION 	= null;
	Integer IDRETENCION = null;
	String RETENCION 	= null;
	Integer SOCIEDADESCLIENTE	= null;
	Hashtable BACKUP 	= null;
	String idPersona = null;
	String idInstitucion = null;
	Integer anyoInformeIRPF = null;
	String idioma;
	
	//Campos para la tabla de retenciones IRPF
	public void setFechaInicio			(String dato)		{ FECHAINICIO = dato;	}	
	public void setFechaFin				(String dato)		{ FECHAFIN = dato;	}	
	public void setLetra				(String dato)		{ LETRA = dato;	}	
	public void setDescripcion			(String dato)		{ DESCRIPCION = dato;	}	
	public void setIdRetencion			(Integer dato)		{ IDRETENCION = dato;	}	
	public void setRetencion			(String dato)		{ RETENCION = dato;	}	
	public void setBackup				(Hashtable dato)		{ BACKUP = dato;	}	
	
	public String	  getFechaInicio() 		{ return FECHAINICIO; }
	public String	  getFechaFin() 		{ return FECHAFIN; }
	public String	  getLetra() 			{ return LETRA; }
	public String	  getDescripcion() 		{ return DESCRIPCION; }
	public Integer	  getIdRetencion() 		{ return IDRETENCION; }
	public String	  getRetencion() 		{ return RETENCION; }
	public Hashtable  getBackup() 			{ return BACKUP; }
	public Integer getSociedadesCliente() {
		return SOCIEDADESCLIENTE;
	}
	public void setSociedadesCliente(Integer dato) {
		SOCIEDADESCLIENTE = dato;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getAnyoInformeIRPF() {
		return anyoInformeIRPF;
	}
	public void setAnyoInformeIRPF(Integer anyoInformeIRPF) {
		this.anyoInformeIRPF = anyoInformeIRPF;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
		
}