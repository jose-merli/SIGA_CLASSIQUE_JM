package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PRECIOHITO
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 * @version 15-12-2008 - adrianag
 */
@AjaxXMLBuilderAnnotation 
public class ScsProcedimientosBean extends MasterBean
{
	//
	//ATRIBUTOS DE CLASE
	//
	//Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_PROCEDIMIENTOS";
	
	//Nombre de campos de la tabla
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPROCEDIMIENTO	= "IDPROCEDIMIENTO";
	static public final String C_NOMBRE				= "NOMBRE";
	static public final String C_PRECIO				= "PRECIO";
	static public final String C_IDJURISDICCION		= "IDJURISDICCION";
	static public final String C_CODIGO				= "CODIGO";
	static public final String C_COMPLEMENTO		= "COMPLEMENTO";
	static public final String C_VIGENTE			= "VIGENTE";
	static public final String C_ORDEN				= "ORDEN";
	static public final String C_PERMITIRANIADIRLETRADO				= "PERMITIRANIADIRLETRADO";
	
	
	//
	//ATRIBUTOS
	//
	private Integer	idInstitucion;
	private String	idProcedimiento;
	private String	nombre;
	private Float	precio;
	private Integer idJurisdiccion;
	private String	codigo;
	private String	complemento;
	private String	vigente;
	private String	permitirAniadirLetrado;
	private Integer	orden;
	
	
	//
	//GETTERS
	//
	public Integer	getIdJurisdiccion()		{return idJurisdiccion;}
	public Integer	getIdInstitucion()		{return idInstitucion;}
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Integer	getIdProcedimiento()	{return Integer.valueOf(idProcedimiento);}
	@AjaxXMLBuilderNameAnnotation
	public String	getNombre()				{return nombre;}
	public Float	getPrecio()				{return precio;}
	public String	getCodigo()				{return codigo;}
    public String	getComplemento()		{return complemento;}
	public String	getVigente()			{return vigente;}
	public String	getPermitirAniadirLetrado()			{return permitirAniadirLetrado;}
	public Integer	getOrden()				{return orden;}
	
	
	//
	//SETTERS
	//
	public void setIdJurisdiccion	(Integer	idJurisdiccion)		{this.idJurisdiccion	= idJurisdiccion;}
	public void setIdInstitucion	(Integer	idInstitucion)		{this.idInstitucion		= idInstitucion;}
	public void setIdProcedimiento	(Integer	idProcedimiento)	{this.idProcedimiento	= idProcedimiento.toString();}
	public void setNombre			(String		nombre)				{this.nombre			= nombre;}
	public void setPrecio			(Float		precio)				{this.precio			= precio;}
	public void setCodigo			(String		codigo)				{this.codigo			= codigo;}
    public void setComplemento		(String		complemento)		{this.complemento		= complemento;}
	public void setVigente			(String		vigente)			{this.vigente			= vigente;}
	public void setOrden			(Integer	orden)				{this.orden				= orden;}
	public void setPermitirAniadirLetrado			(String		permitirAniadirLetrado)			{this.permitirAniadirLetrado			= permitirAniadirLetrado;}

}