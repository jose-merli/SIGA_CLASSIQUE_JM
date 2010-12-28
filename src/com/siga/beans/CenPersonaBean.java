package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
 * Created on 14-oct-2004
 */
@AjaxXMLBuilderAnnotation
public class CenPersonaBean extends MasterBean
{
	// Atributos
	private Long idPersona;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String nifcif;
	private Integer idTipoIdentificacion;
	private Integer idEstadoCivil;
	private String fechaNacimiento;
	private String naturalDe;
	private String sexo;  
	private String fallecido;
	
	private boolean existeDatos;
	CenColegiadoBean colegiado;
	
	/** Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_PERSONA";

	// Nombre campos de la tabla
	static public final String C_IDPERSONA 				= "IDPERSONA";
	static public final String C_NOMBRE 				= "NOMBRE";
	static public final String C_APELLIDOS1 			= "APELLIDOS1";
	static public final String C_APELLIDOS2 			= "APELLIDOS2";
	static public final String C_NIFCIF 				= "NIFCIF";
	static public final String C_IDTIPOIDENTIFICACION 	= "IDTIPOIDENTIFICACION";
	static public final String C_IDESTADOCIVIL 			= "IDESTADOCIVIL";
	static public final String C_FECHANACIMIENTO 		= "FECHANACIMIENTO";
	static public final String C_NATURALDE 				= "NATURALDE";
	static public final String C_SEXO				    = "SEXO";
	static public final String C_FALLECIDO				= "FALLECIDO";
	
	
	/**
	 * Constructor por defecto
	 */
	public CenPersonaBean() {
		super();
	}
	/**
	 * Constructor
	 */
	public CenPersonaBean(Long idPersona, String nombre, String apellido1, String apellido2, String nColegiado)
	{
		super();
		this.idPersona = idPersona;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		
		CenColegiadoBean colegiado = new CenColegiadoBean();
		colegiado.setNColegiado(nColegiado);
		this.colegiado = colegiado;
	}
	
	
	// SETTERS
	public void setIdPersona			(Long valor)	{this.idPersona = valor;}
	public void setNombre				(String valor) 	{this.nombre = valor;}
	public void setApellido1			(String valor) 	{this.apellido1 = valor;}
	public void setApellido2			(String valor) 	{this.apellido2 = valor;}
	public void setNIFCIF				(String valor)	{this.nifcif = valor;}
	public void setIdTipoIdentificacion	(Integer valor)	{this.idTipoIdentificacion = valor;}
	public void setIdEstadoCivil		(Integer valor) {this.idEstadoCivil = valor;}
	public void setFechaNacimiento		(String valor) 	{this.fechaNacimiento = valor;}
	public void setNaturalDe			(String valor) 	{this.naturalDe = valor;}
	public void setSexo					(String valor)	{this.sexo = valor;}
	public void setFallecido			(String valor)	{this.fallecido = valor;}
	
	public void setExisteDatos(boolean existeDatos) {this.existeDatos = existeDatos;}
	public void setColegiado(CenColegiadoBean colegiado) {this.colegiado = colegiado;}
	
	
	// GETTERS
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Long getIdPersona 				() {return this.idPersona;}
	public Integer getIdEstadoCivil 		() {return this.idEstadoCivil;}
	public Integer getIdTipoIdentificacion	() {return this.idTipoIdentificacion;}
	@AjaxXMLBuilderNameAnnotation
	public String getNombre 				() {return this.nombre;}
	public String getApellido1 				() {return this.apellido1;}
	public String getApellido2 				() {return this.apellido2;}
	public String getNIFCIF 				() {return this.nifcif;}
	public String getFechaNacimiento 		() {return this.fechaNacimiento;}
	public String getNaturalDe 				() {return this.naturalDe;}
	public String getSexo					() {return this.sexo;}
	public String getFallecido 				() {return this.fallecido;}
	
	public boolean isExisteDatos() {return existeDatos;}
	public CenColegiadoBean getColegiado() {return colegiado;}
	
}
