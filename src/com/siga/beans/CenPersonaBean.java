/*
 * Created on 14-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@AjaxXMLBuilderAnnotation
public class CenPersonaBean extends MasterBean {

	/* Variables */
	private Long idPersona;
	private Integer idTipoIdentificacion, idEstadoCivil;
	private String nombre, apellido1, apellido2, nifcif, naturalDe,sexo;  
	private String fechaNacimiento,fallecido;
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_PERSONA";

	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA 				= "IDPERSONA";
	static public final String C_NOMBRE 				= "NOMBRE";
	static public final String C_APELLIDOS1 			= "APELLIDOS1";
	static public final String C_APELLIDOS2 			= "APELLIDOS2";
	static public final String C_NIFCIF 				= "NIFCIF";
	static public final String C_FECHANACIMIENTO 		= "FECHANACIMIENTO";
	static public final String C_IDESTADOCIVIL 			= "IDESTADOCIVIL";
	static public final String C_IDTIPOIDENTIFICACION 	= "IDTIPOIDENTIFICACION";
	static public final String C_NATURALDE 				= "NATURALDE";
	static public final String C_SEXO				    = "SEXO";
	static public final String C_FALLECIDO				= "FALLECIDO";
	
	// Metodos SET
	public void setIdPersona (Long id) 			{ this.idPersona = id; }
	public void setIdEstadoCivil (Integer id) 		{ this.idEstadoCivil = id; }
	public void setIdTipoIdentificacion (Integer id){ this.idTipoIdentificacion = id; }
	public void setNombre (String n) 				{ this.nombre = n; }
	public void setApellido1 (String a) 			{ this.apellido1 = a; }
	public void setApellido2 (String a) 			{ this.apellido2 = a; }
	public void setNIFCIF (String nif)				{ this.nifcif = nif; }
	public void setFechaNacimiento (String f) 		{ this.fechaNacimiento = f; }
	public void setNaturalDe (String s) 			{ this.naturalDe = s; }
	public void setFallecido (String fallecido) 			{ this.fallecido = fallecido; }

	// Metodos GET
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Long getIdPersona 			()	{ return this.idPersona; }
	public Integer getIdEstadoCivil 		()	{ return this.idEstadoCivil; }
	public Integer getIdTipoIdentificacion	() 	{ return this.idTipoIdentificacion; }
	@AjaxXMLBuilderNameAnnotation
	public String getNombre 			()	{ return this.nombre; }
	public String getApellido1 			()	{ return this.apellido1; }
	public String getApellido2 			() 	{ return this.apellido2; }
	public String getNIFCIF 			() 	{ return this.nifcif; }
	public String getNaturalDe 			() 	{ return this.naturalDe; }
	public String getFechaNacimiento 		() 	{ return this.fechaNacimiento; }
	public String getFallecido 			() 	{ return this.fallecido; }
	/**
	 * @return Returns the sexo.
	 */
	public String getSexo() {
		return sexo;
	}
	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	CenColegiadoBean colegiado;

	public CenColegiadoBean getColegiado() {
		return colegiado;
	}
	public void setColegiado(CenColegiadoBean colegiado) {
		this.colegiado = colegiado;
	}
	
}
