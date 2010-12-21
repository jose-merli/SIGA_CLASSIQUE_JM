package com.siga.gratuita.form;

import com.siga.general.MasterForm;

public class ColaGuardiasForm extends MasterForm
{
	//Campos para la tabla de turnos
	String idPersona = "";
	String fechaSuscripcion = "";
	private String fechaConsulta;
	
	
	// SETTERS
	public void setDefGuardia				(String valor) { if(valor==null) datos.remove("DEFGUARDIA");		else datos.put("DEFGUARDIA",valor); }
	public void setIdGuardia				(String valor) { if(valor==null) datos.remove("IDGUARDIA");			else datos.put("IDGUARDIA",valor); }
	public void setNColegiado				(String valor) { if(valor==null) datos.remove("NCOLEGIADO");		else datos.put("NCOLEGIADO",valor); }
	public void setNombre					(String valor) { if(valor==null) datos.remove("NOMBRE");			else datos.put("NOMBRE",valor); }
	public void setApellido1				(String valor) { if(valor==null) datos.remove("APELLIDO1");			else datos.put("APELLIDO1",valor); }
	public void setApellido2				(String valor) { if(valor==null) datos.remove("APELLIDO2");			else datos.put("APELLIDO2",valor); }
	public void setDatosModificados			(String valor) { if(valor==null) datos.remove("DATOSMODIFICADOS");	else datos.put("DATOSMODIFICADOS",valor); }
	public void setIdInstitucion			(String valor) { if(valor==null) datos.remove("IDINSTITUCION");		else datos.put("IDINSTITUCION",valor); }
	public void setIdTurno					(String valor) { if(valor==null) datos.remove("IDTURNO");			else datos.put("IDTURNO",valor); }
	public void setIdGrupoGuardiaColegiado	(String valor) { if(valor==null) datos.remove("IDGRUPOGUARDIACOLEGIADO"); else datos.put("IDGRUPOGUARDIACOLEGIADO",valor); }
	
	public void setIdPersona				(String valor) { this.idPersona = valor; }
	public void setFechaSuscripcion			(String valor) { this.fechaSuscripcion = valor; }
	public void setFechaConsulta			(String valor) { this.fechaConsulta = valor; }

	
	// GETTERS
	public String	getDefGuardia() 				{ return (String)datos.get("DEFGUARDIA"); }
	public String	getIdGuardia() 					{ return (String)datos.get("IDGUARDIA"); }
	public String	getNColegiado() 				{ return (String)datos.get("NCOLEGIADO"); }
	public String	getNombre()     				{ return (String)datos.get("NOMBRE"); }
	public String	getApellido1()    				{ return (String)datos.get("APELLIDO1"); }
	public String	getApellido2()     				{ return (String)datos.get("APELLIDO2"); }
	public String	getDatosModificados()     		{ return (String)datos.get("DATOSMODIFICADOS"); }
	public String	getIdInstitucion()     			{ return (String)datos.get("IDINSTITUCION"); }
	public String	getIdTurno()     				{ return (String)datos.get("IDTURNO"); }
	public String	getIdGrupoGuardiaColegiado()    { return (String)datos.get("IDGRUPOGUARDIACOLEGIADO"); }
	
	public String	getIdPersona()					{ return idPersona; }
	public String	getFechaSuscripcion()			{ return fechaSuscripcion; }
	public String	getFechaConsulta()				{ return fechaConsulta; }

}