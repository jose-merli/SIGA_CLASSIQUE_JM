package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PRECIOHITO
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */

public class ScsPrecioHitoBean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	private Integer	idHitoFacturable;
	private String	fechaEntradaVigor;
	private Integer precio;
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PRECIOHITO";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDHITOFACTURABLE = 	"IDHITOFACTURABLE";
	static public final String 	C_FECHAENTRADAVIGOR = 	"FECHAENTRADAVIGOR";
	static public final String  C_PRECIO = 				"PRECIO";	
		
	
	/**
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador del hito facturable 
	 * 
	 * @param valor Identificador del hito facturable. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdHitoFacturable			(Integer valor)	{ this.idHitoFacturable = 	valor;}
	/**
	 * Almacena en el Bean la fecha de entrada en vigor del hito facturable 
	 * 
	 * @param valor Fecha de entrada en vigor del hito facturable. De tipo "String". 
	 * @return void 
	 */
	public void setFechaEntradaVigor		(String  valor)	{ this.fechaEntradaVigor = 	valor;}
	/**
	 * Almacena en el Bean el precio del hito facturable 
	 * 
	 * @param valor Precio del hito facturable. De tipo "Integer". 
	 * @return void 
	 */
	public void setPrecio					(Integer valor)	{ this.precio = 			valor;}
	
	
	/**
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del hito facturable
	 * 
	 * @return Identificador del hito facturable
	 */
	public Integer getIdHitoFacturable		()	{ return this.idHitoFacturable;	}
	/**
	 * Recupera del Bean la fecha de netrada en vigor del hito facturable
	 * 
	 * @return Fecha de entrada en vigor del hito facturable
	 */
	public String  getFechaEntradaVigor		()	{ return this.fechaEntradaVigor;}
	/**
	 * Recupera del Bean el precio del hito facturable
	 * 
	 * @return Precio del hito facturable
	 */
	public Integer getPrecio 				() 	{ return this.precio;			}	
	}