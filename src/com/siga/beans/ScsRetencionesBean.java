package com.siga.beans;

//Clase: ScsRetencionesBean 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 20/12/2004
/**
 * Implementa las operaciones sobre el bean de la tabla SCS_MAESTRORETENCIONES
 */
public class ScsRetencionesBean extends MasterBean {
	
	/* Variables */
	private String modo;	
	private Integer idRetencion;	
	private float retencion;	
	private String letraNifSociedad,	
					descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_MAESTRORETENCIONES";
	
	
	/* Nombre campos de la tabla */
	static public final String C_IDRETENCION 			= "IDRETENCION";
	static public final String C_DESCRIPCION 			= "DESCRIPCION";
	static public final String C_LETRANIFSOCIEDAD 		= "LETRANIFSOCIEDAD";
	static public final String C_RETENCION 				= "RETENCION";	
	
	
	
	// Metodos SET
	/**
	 * Almacena en la Bean el identificador de una retención "idretencion" 
	 * 
	 * @param id Identificador retención. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdRetencion (Integer id)			{ this.idRetencion = id; 		}
	
	/**
	 * Almacena en el Bean el valor de la retención "retencion" 
	 * 
	 * @param ret Valor retención. De tipo "float". 
	 * @return void 
	 */
	public void setRetencion (float ret) 			{ this.retencion = ret; 		}
	
	/**
	 * Almacena en el Bean el valor de la letra del NIF o sociedad al que se aplica la rentencion  
	 * 
	 * @param nif Letra del NIF o Sociedad. De tipo "String". 
	 * @return void 
	 */
	public void setLetraNifSociedad (String nif) 	{ this.letraNifSociedad = nif; 	}
	
	/**
	 * Almacena en la Bean la descripción de la rentención  
	 * 
	 * @param des Descripción de la retención. De tipo "String" 
	 * @return void 
	 */
	public void setDescripcion (String des) 		{ this.descripcion = des; 		}
	
	/**
	 * Almacena en la Bean el modo en el que se envío el formulario (inserción, busqueda, consulta).  
	 * 
	 * @param modo Modo de envío. De tipo "String" 
	 * @return void 
	 */
	public void setModo (String modo)				{ this.modo=modo;				}
	
	
	// Metodos GET
	/**
	 * Recupera del Bean el identificador de la retención.  
	 * 
	 * @return Identificador de retención. De tipo "Integer" 
	 */
	public Integer getIdRetencion 		() 			{ return this.idRetencion; 		}
	
	
	/**
	 * Recupera del Bean el valor de la retención.  
	 * 
	 * @return Valor de la retención. De tipo "float" 
	 */
	public float   getRetencion 		()			{ return this.retencion; 		}
	
	/**
	 * Recupera del Bean la letra del NIF/Sociedad de la retención.  
	 * 
	 * @return Letra del NIF/Sociedad. De tipo "String" 
	 */
	public String  getLetraNifSociedad 	() 			{ return this.letraNifSociedad; }
	
	/**
	 * Recupera del Bean la descripción de la retención.  
	 * 
	 * @return Descripción de la retención. De tipo "String" 
	 */
	public String  getDescripcion 		() 			{ return this.descripcion;		}
	
	/**
	 * Recupera del Bean el modo de envío del formulario (inserción, consulta, búsqueda).  
	 * 
	 * @return Modo de envío del formulario. De tipo "String" 
	 */
	public String  getModo 				()			{ return this.modo;				}

	
}