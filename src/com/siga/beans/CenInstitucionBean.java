package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
* @author ruben.fernandez
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/
@AjaxXMLBuilderAnnotation 
public class CenInstitucionBean extends MasterBean{
	
	/* Variables */ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2541501638618322756L;
	private Integer		idInstitucion;
	private String		nombre;
	private Integer		cuentaContableCaja;
	private Integer 	bbddcpd;
	private String  	idLenguaje;
	private Integer 	idPersona;
	private Integer 	cen_inst_idInstitucion;
	private String	 	abreviatura;
	private String	 	fechaEnProduccion;
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CEN_INSTITUCION";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_CUENTACONTABLECAJA = 		"CUENTACONTABLECAJA";
	static public final String 	C_BBDDCPD = 				"BBDDCPD";
	static public final String 	C_IDLENGUAJE = 				"IDLENGUAJE";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_CEN_INST_IDINSTITUCION = 	"CEN_INST_IDINSTITUCION";
	static public final String 	C_ABREVIATURA = 			"ABREVIATURA";
	static public final String 	C_FECHAENPRODUCCION =    	"FECHAENPRODUCCION";
	
	
	
	/*Metodos SET*/
	
	public void setIdInstitucion			(Integer valor)		{ this.idInstitucion = valor;}
	public void setNombre					(String valor)		{ this.nombre = valor;}
	public void setCuentaContableCaja		(Integer valor)		{ this.cuentaContableCaja = valor;}
	public void setBbddcpd					(Integer valor)		{ this.bbddcpd = valor;}
	public void setIdLenguaje	 			(String  valor) 	{ this.idLenguaje = valor;}
	public void setIdPersona	 			(Integer valor) 	{ this.idPersona = valor;}
	public void setCen_inst_idInstitucion	(Integer valor) 	{ this.cen_inst_idInstitucion = valor;}
	public void setAbreviatura				(String valor)		{ this.abreviatura = valor;}
	public void setFechaEnProduccion		(String valor)		{ this.fechaEnProduccion = valor;}
	
		
	/*Metodos GET*/
	
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Integer getIdInstitucion				()	{ return this.idInstitucion;}	
	public String getNombre						()	{ return this.nombre;}
	public Integer getCuentaContableCaja		()	{ return this.cuentaContableCaja;}
	public Integer getBbddcpd					()  { return this.bbddcpd;}
	public String  getIdLenguaje	 			() 	{ return this.idLenguaje;}
	public Integer getIdPersona	 				() 	{ return this.idPersona;}
	public Integer getCen_inst_idInstitucion	() 	{ return this.cen_inst_idInstitucion;}
	@AjaxXMLBuilderNameAnnotation
	public String getAbreviatura				()	{ return this.abreviatura;}
	public String getFechaEnProduccion			()	{ return this.fechaEnProduccion;}
}