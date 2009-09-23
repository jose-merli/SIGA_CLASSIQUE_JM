package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_ORDENACIONCOLAS
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */

public class ScsOrdenacionColasBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer		idOrdenacionColas;
	private Integer		alfabeticoApellidos;
	private Integer		fechaNacimiento;
	private Integer 	numeroColegiado;
	private Integer 	antiguedadCola;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_ORDENACIONCOLAS";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDORDENACIONCOLAS = 		"IDORDENACIONCOLAS";
	static public final String	C_ALFABETICOAPELLIDOS = 	"ALFABETICOAPELLIDOS";
	static public final String	C_FECHANACIMIENTO = 		"FECHANACIMIENTO";
	static public final String	C_NUMEROCOLEGIADO = 		"NUMEROCOLEGIADO";
	static public final String	C_ANTIGUEDADCOLA = 			"ANTIGUEDADCOLA";
	
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la ordenacion de colas 
	 * 
	 * @param valor Identificador de la ordenacion de colas. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdOrdenacionColas		(Integer valor) 	{ this.idOrdenacionColas = valor;}
	/**
	 * Almacena en el Bean el campo alfabetico apellidos
	 * 
	 * @param valor Alfabetico apellidos de la tabla . De tipo "Integer". 
	 * @return void 
	 */
	public void setAlfabeticoApellidos		(Integer valor)		{ this.alfabeticoApellidos = valor;}
	/**
	 * Almacena en el Bean el campo fecha nacimiento
	 * 
	 * @param valor Fecha nacimiento de la tabla . De tipo "Integer". 
	 * @return void 
	 */
	public void setFechaNacimiento			(Integer valor)		{ this.fechaNacimiento = valor;}
	/**
	 * Almacena en el Bean el campo numero de colegiado
	 * 
	 * @param valor Numero de colegiado. De tipo "Integer". 
	 * @return void 
	 */
	public void setNumeroColegiado			(Integer valor)		{ this.numeroColegiado = valor;}
	/**
	 * Almacena en el Bean el campo antiguedad de cola
	 * 
	 * @param valor Antiguedad de cola. De tipo "Integer". 
	 * @return void 
	 */
	public void setAntiguedadCola			(Integer valor)		{ this.antiguedadCola = valor;}
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la ordenacion de colas
	 * 
	 * @return Identificador de la ordenacion de colas
	 */
	public Integer getIdOrdenacionColas			()	{ return this.idOrdenacionColas;}
	/**
	 * Recupera del Bean el campo alfabetico apellidos
	 * 
	 * @return Alfabetico apellidos
	 */
	public Integer getAlfabeticoApellidos		()	{ return this.alfabeticoApellidos;}
	/**
	 * Recupera del Bean el campo fecha nacimiento
	 * 
	 * @return Fecha nacimiento
	 */
	public Integer getFechaNacimiento			()	{ return this.fechaNacimiento;}
	/**
	 * Recupera del Bean el campo numero de colegiado
	 * 
	 * @return Numero de colegiado
	 */
	public Integer getNumeroColegiado			()	{ return this.numeroColegiado;}
	/**
	 * Recupera del Bean el campo antiguedad de cola
	 * 
	 * @return Antiguedad de cola
	 */
	public Integer getAntiguedadCola			()	{ return this.antiguedadCola;}
	
	
}