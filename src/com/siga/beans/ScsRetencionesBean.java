package com.siga.beans;

//Clase: ScsRetencionesBean 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 20/12/2004
/**
 * Implementa las operaciones sobre el bean de la tabla SCS_MAESTRORETENCIONES
 */
public class ScsRetencionesBean extends MasterBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8182100027666309695L;
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
	static public final String C_PORDEFECTO 			= "PORDEFECTO";	
	
	
	
	public static String getPordefecto() {
		return C_PORDEFECTO;
	}
	
	public void setPordefecto (Integer id)			{ this.idRetencion = id; 		}
	

	// Metodos SET
	/**
	 * Almacena en la Bean el identificador de una retenci�n "idretencion" 
	 * 
	 * @param id Identificador retenci�n. De tipo "Integer". 
	 * @return void 
	 */	
	public void setIdRetencion (Integer id)			{ this.idRetencion = id; 		}
	
	/**
	 * Almacena en el Bean el valor de la retenci�n "retencion" 
	 * 
	 * @param ret Valor retenci�n. De tipo "float". 
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
	 * Almacena en la Bean la descripci�n de la rentenci�n  
	 * 
	 * @param des Descripci�n de la retenci�n. De tipo "String" 
	 * @return void 
	 */
	public void setDescripcion (String des) 		{ this.descripcion = des; 		}
	
	/**
	 * Almacena en la Bean el modo en el que se env�o el formulario (inserci�n, busqueda, consulta).  
	 * 
	 * @param modo Modo de env�o. De tipo "String" 
	 * @return void 
	 */
	public void setModo (String modo)				{ this.modo=modo;				}
	
	
	// Metodos GET
	/**
	 * Recupera del Bean el identificador de la retenci�n.  
	 * 
	 * @return Identificador de retenci�n. De tipo "Integer" 
	 */
	public Integer getIdRetencion 		() 			{ return this.idRetencion; 		}
	
	
	/**
	 * Recupera del Bean el valor de la retenci�n.  
	 * 
	 * @return Valor de la retenci�n. De tipo "float" 
	 */
	public float   getRetencion 		()			{ return this.retencion; 		}
	
	/**
	 * Recupera del Bean la letra del NIF/Sociedad de la retenci�n.  
	 * 
	 * @return Letra del NIF/Sociedad. De tipo "String" 
	 */
	public String  getLetraNifSociedad 	() 			{ return this.letraNifSociedad; }
	
	/**
	 * Recupera del Bean la descripci�n de la retenci�n.  
	 * 
	 * @return Descripci�n de la retenci�n. De tipo "String" 
	 */
	public String  getDescripcion 		() 			{ return this.descripcion;		}
	
	/**
	 * Recupera del Bean el modo de env�o del formulario (inserci�n, consulta, b�squeda).  
	 * 
	 * @return Modo de env�o del formulario. De tipo "String" 
	 */
	public String  getModo 				()			{ return this.modo;				}

	
}