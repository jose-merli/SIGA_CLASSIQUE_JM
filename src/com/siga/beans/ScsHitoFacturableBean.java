package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_HITOFACTURABLE
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */

public class ScsHitoFacturableBean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	private Integer	idHito;
	private String  nombre;
	private String  descripcion;
	private String	aplicableA;
	private String	sqlSeleccion;
	private String	sqlUpdate;
	
	
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_HITOFACTURABLE";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDHITO	=			"IDHITO";
	static public final String 	C_NOMBRE	=			"NOMBRE";
	static public final String 	C_DESCRIPCION	=		"DESCRIPCON";
	static public final String 	C_APLICABLEA	=		"APLICABLEA";
	static public final String 	C_SQLSELECCION =		"SQLSELECCION";
	static public final String 	C_SQLUPDATE =			"SQLUPDATE";
		
	
	/**
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador del hito de facturacion
	 * 
	 * @param valor Identificador del hito de facturacion. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdHito				(Integer valor)	{ this.idHito	=			valor;}
	/**
	 * Almacena en el Bean el nombre del hito de facturacion
	 * 
	 * @param valor NOmbre del hito de facturacion. De tipo "Integer". 
	 * @return void 
	 */
	public void setNombre				(String  valor)	{ this.nombre =				valor;}
	/**
	 * Almacena en el Bean la descripcion del hito de facturacion
	 * 
	 * @param valor Descripcion del hito de facturacion. De tipo "String". 
	 * @return void 
	 */
	public void setDescripcion			(String  valor)	{ this.descripcion =		valor;}
	/**
	 * Almacena en el Bean el campo aplicable a del hito de facturacion
	 * 
	 * @param valor AplicableA del hito de facturacion. De tipo "String". 
	 * @return void 
	 */
	public void setAplicableA			(String  valor)	{ this.aplicableA = 		valor;}
	/**
	 * Almacena en el Bean el campo sqlSeleccion a del hito de facturacion
	 * 
	 * @param valor SqlSeleccion del hito de facturacion. De tipo "String". 
	 * @return void 
	 */
	public void setSqlSeleccion			(String  valor)	{ this.sqlSeleccion =	 	valor;}
	/**
	 * Almacena en el Bean el campo sqlUpdate a del hito de facturacion
	 * 
	 * @param valor SqlUpdate del hito de facturacion. De tipo "String". 
	 * @return void 
	 */
	public void setSqlUpdate			(String  valor)	{ this.sqlUpdate = 			valor;}
	
	
	/**
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del hito de facturacion
	 * 
	 * @return Identificador del hito de facturacion
	 */
	public Integer getIdHito				()	{ return this.idHito;				}
	/**
	 * Recupera del Bean el nombre del hito de facturacion
	 * 
	 * @return Nombre del hito de facturacion
	 */
	public String  getNombre				()	{ return this.nombre;				}
	/**
	 * Recupera del Bean el descripcion del hito de facturacion
	 * 
	 * @return Descripcion del hito de facturacion
	 */
	public String  getDescripcion			()	{ return this.descripcion;			}
	/**
	 * Recupera del Bean el campo aplicablea del hito de facturacion
	 * 
	 * @return AplicableA del hito de facturacion
	 */
	public String  getAplicableA			()	{ return this.aplicableA;			}
	/**
	 * Recupera del Bean el campo SqlSeleccion del hito de facturacion
	 * 
	 * @return SqlSeleccion del hito de facturacion
	 */
	public String  getSqlSeleccion			()	{ return this.sqlSeleccion;			}
	/**
	 * Recupera del Bean el campo SqlUpdate del hito de facturacion
	 * 
	 * @return SqlUpdate del hito de facturacion
	 */
	public String  getSqlUpdate				()	{ return this.sqlUpdate;			}
	
}