package com.siga.beans;

//Clase: ScsRetencionesBean 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 21/12/2004
/**
ScsPartidaPresupuestariaBean.java
* Implementa las operaciones sobre el bean de la tabla SCS_PARTIDAPRESUPUESTARIA
*/

public class ScsPartidaPresupuestariaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer		idPartidaPresupuestaria;
	private String		nombrePartida;
	private String		descripcion;
	private Integer 	idInstitucion;
	private Float	 	importePartida;
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PARTIDAPRESUPUESTARIA";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDPARTIDAPRESUPUESTARIA =	"IDPARTIDAPRESUPUESTARIA";
	static public final String 	C_NOMBREPARTIDA = 			"NOMBREPARTIDA";
	static public final String	C_DESCRIPCION = 			"DESCRIPCION";
	static public final String  C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String  C_IMPORTEPARTIDA = 			"IMPORTEPARTIDA";
	
	/*Metodos SET*/
	
	/**
	 * Almacena en la Bean el identificador de una partida presupuestaria 
	 * 
	 * @param valor Identificador partida presupuestaria. 
	 * @return void 
	 */

	public void setIdPartidaPresupuestaria	(Integer valor) { this.idPartidaPresupuestaria = valor;}
	
	/**
	 * Almacena en el Bean el nombre de una partida 
	 * 
	 * @param valor Nombre partida. 
	 * @return void 
	 */
	public void setNombrePartida			(String valor)	{ this.nombrePartida = valor;}
	/**
	 * Almacena en el Bean la descripcion de la partida presupuestaria
	 * 
	 * @param valor Descripcion de la partida presupuestaria. De tipo "Integer". 
	 * @return void 
	 */

	public void setDescripcion				(String valor)	{ this.descripcion = valor;}

	
	/**
	 * Almacena en el Bean el identificador de la institución 
	 * 
	 * @param valor Identificador institución. 
	 * @return void 
	 */

	public void setIdInstitucion			(Integer valor) { this.idInstitucion = valor;}
		
	/**
	 * Almacena en el Bean el identificador de la institución 
	 * 
	 * @param valor Importe de la partida presupuestaria. 
	 * @return void 
	 */

	public void setImportePartida			(Float valor) { this.importePartida = valor;}

	/*Metodos GET*/
	

	/**
	 * Recupera del Bean el identificador de la partida presupuestaria  
	 * 
	 * @return Identficador de la partida 
	 */

	public Integer getIdPartidaPresupuestaria	()	{ return this.idPartidaPresupuestaria;}
	
	/**
	 * Recupera del Bean el nombre de la partida presupuestaria  
	 * 
	 * @return Nombre de la partida 
	 */
	public String  getNombrePartida				()	{ return this.nombrePartida;}
	
	/**
	 * Recupera del Bean la descripción de la partida presupuestaria  
	 * 
	 * @return Descripción de la partida 
	 */
	public String  getDescripcion				()	{ return this.descripcion;}
	
	/**
	 * Recupera del Bean el identificador de la institución  
	 * 
	 * @return Identficador de la institución 
	 */
	public Integer getIdInstitucion				()	{ return this.idInstitucion;}
	
	/**
	 * Recupera del Bean el identificador de la institución  
	 * 
	 * @return Importe de la partida 
	 */
	public Float getImportePartida				()	{ return this.importePartida;}
	
}