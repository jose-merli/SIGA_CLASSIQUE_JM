package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesForm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 20/12/2004
/**
 * Maneja el formulario que mantiene la tabla SCS_MAESTRORETENCIONES
 */
 public class SolicitudRetencionForm extends MasterForm{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6023721924419934297L;

	// Metodos Set (Formulario (*.jsp))
 	/**
	 * Almacena en la Hashtable el identificador de una retenci�n "idretencion" 
	 * 
	 * @param idRetencion Identificador retenci�n. De tipo "int". 
	 * @return void 
	 */
 	public void setIdRetencion 		(int idRetencion) 					{ this.datos.put(ScsRetencionesBean.C_IDRETENCION, Integer.toString(idRetencion));	   }	
 	
 	/**
	 * Almacena en la Hashtable el valor de la retenci�n "retencion" 
	 * 
	 * @param retencion Valor retenci�n. De tipo "float". 
	 * @return void 
	 */
 	public void setRetencion 		(String retencion) 					{ this.datos.put(ScsRetencionesBean.C_RETENCION, retencion);						   }	
	
 	/**
	 * Almacena en la Hashtable el valor de la letra del NIF o sociedad al que se aplica la rentencion  
	 * 
	 * @param letraNifSociedad Letra del NIF o Sociedad. De tipo "String". 
	 * @return void 
	 */
 	public void setLetraNifSociedad (String letraNifSociedad) 			{ this.datos.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD,letraNifSociedad.toUpperCase());	}
	
 	/**
	 * Almacena en la Hashtable la descripci�n de la rentenci�n  
	 * 
	 * @param descripcion Descripci�n de la retenci�n. De tipo "String" 
	 * @return void 
	 */
 	public void setDescripcion 		(String descripcion) 				{ this.datos.put(ScsRetencionesBean.C_DESCRIPCION,descripcion);		   					} 
	
 	/**
	 * Almacena en la Hashtable la fecha de la �ltima modificaci�n del registro.  
	 * 
	 * @param fechaMod Fecha �ltima modificaci�n. De tipo "String" 
	 * @return void 
	 */
 	public void setFechaMod			(String fechaMod)					{ this.datos.put(ScsRetencionesBean.C_FECHAMODIFICACION,fechaMod); 					   }
	
 	/**
	 * Almacena en la Hashtable el usuario que efect�o la �ltima modificaci�n.  
	 * 
	 * @param usuMod Usuario �ltima modificaci�n. De tipo "int" 
	 * @return void 
	 */ 	
 	public void setUsuMod			(int usuMod)						{ this.datos.put(ScsRetencionesBean.C_USUMODIFICACION,Integer.toString(usuMod));	   }

	// Metodos Get 1 por campo Formulario (*.jsp)
 	
 	public void setComboSociedades (String[] inst)     {this.datos.put("SOCIEDADES",inst);               } 
 	
 	/**
	 * Recupera del Hashtable el identificador de la retenci�n.  
	 * 
	 * @return Identificador de retenci�n. De tipo "int" 
	 */ 	
 	public int    getIdRetencion			()  { return Integer.parseInt((String)this.datos.get(ScsRetencionesBean.C_IDRETENCION));		}
	
 	/**
	 * Recupera del Hashtable el valor de la retenci�n.  
	 * 
	 * @return Valor de una retenci�n. De tipo "float". 
	 */
 	public String  getRetencion				() 	{ return (String)this.datos.get(ScsRetencionesBean.C_RETENCION); 							}
	
 	/**
	 * Recupera del Hashtable la letra del NIF o Sociedad.  
	 * 
	 * @return Letra del NIF o Sociedad. De tipo "String". 
	 */
 	public String getLetraNifSociedad		() 	{ return ((String)this.datos.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)); 					}
	
 	/**
	 * Recupera del Hashtable la descripci�n de la retenci�n.  
	 * 
	 * @return Descripcion. De tipo "String". 
	 */
 	public String getDescripcion 			() 	{ return ((String)this.datos.get(ScsRetencionesBean.C_DESCRIPCION));	 					}
	
 	/**
	 * Recupera del Hashtable la fecha de la �ltima modificaci�n hecha en el registro.  
	 * 
	 * @return FechaMod. De tipo "String". 
	 */
 	public String getFechaMod				()	{ return ((String)this.datos.get(ScsRetencionesBean.C_FECHAMODIFICACION)); 					}
	
 	/**
	 * Recupera del Hashtable el usuario que realiz� la �ltima modificaci�n en el registro.  
	 * 
	 * @return UsuMod. De tipo "int". 
	 */
 	public int 	  getUsuMod					()	{ return Integer.parseInt((String)this.datos.get(ScsRetencionesBean.C_USUMODIFICACION)); 	}
 	
 	public String[] getComboSociedades	() 	        { return (String[]) this.datos.get("SOCIEDADES");		
  	}
	
 	public String claveM190;

	public String getClaveM190() {					return (String)this.datos.get(ScsRetencionesBean.C_CLAVEM190); 	}

	public void setClaveM190(String claveM190) {	this.datos.put(ScsRetencionesBean.C_CLAVEM190,claveM190);		}
 	
 	
}