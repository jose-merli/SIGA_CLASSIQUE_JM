package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesForm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 20/12/2004
/**
 * Maneja el formulario que mantiene la tabla SCS_MAESTRORETENCIONES
 */
 public class SolicitudRetencionForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
 	/**
	 * Almacena en la Hashtable el identificador de una retención "idretencion" 
	 * 
	 * @param idRetencion Identificador retención. De tipo "int". 
	 * @return void 
	 */
 	public void setIdRetencion 		(int idRetencion) 					{ this.datos.put(ScsRetencionesBean.C_IDRETENCION, Integer.toString(idRetencion));	   }	
 	
 	/**
	 * Almacena en la Hashtable el valor de la retención "retencion" 
	 * 
	 * @param retencion Valor retención. De tipo "float". 
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
	 * Almacena en la Hashtable la descripción de la rentención  
	 * 
	 * @param descripcion Descripción de la retención. De tipo "String" 
	 * @return void 
	 */
 	public void setDescripcion 		(String descripcion) 				{ this.datos.put(ScsRetencionesBean.C_DESCRIPCION,descripcion);		   					} 
	
 	/**
	 * Almacena en la Hashtable la fecha de la última modificación del registro.  
	 * 
	 * @param fechaMod Fecha última modificación. De tipo "String" 
	 * @return void 
	 */
 	public void setFechaMod			(String fechaMod)					{ this.datos.put(ScsRetencionesBean.C_FECHAMODIFICACION,fechaMod); 					   }
	
 	/**
	 * Almacena en la Hashtable el usuario que efectúo la última modificación.  
	 * 
	 * @param usuMod Usuario última modificación. De tipo "int" 
	 * @return void 
	 */ 	
 	public void setUsuMod			(int usuMod)						{ this.datos.put(ScsRetencionesBean.C_USUMODIFICACION,Integer.toString(usuMod));	   }

	// Metodos Get 1 por campo Formulario (*.jsp)
 	
 	public void setComboSociedades (String[] inst)     {this.datos.put("SOCIEDADES",inst);               } 
 	
 	/**
	 * Recupera del Hashtable el identificador de la retención.  
	 * 
	 * @return Identificador de retención. De tipo "int" 
	 */ 	
 	public int    getIdRetencion			()  { return Integer.parseInt((String)this.datos.get(ScsRetencionesBean.C_IDRETENCION));		}
	
 	/**
	 * Recupera del Hashtable el valor de la retención.  
	 * 
	 * @return Valor de una retención. De tipo "float". 
	 */
 	public String  getRetencion				() 	{ return (String)this.datos.get(ScsRetencionesBean.C_RETENCION); 							}
	
 	/**
	 * Recupera del Hashtable la letra del NIF o Sociedad.  
	 * 
	 * @return Letra del NIF o Sociedad. De tipo "String". 
	 */
 	public String getLetraNifSociedad		() 	{ return ((String)this.datos.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)); 					}
	
 	/**
	 * Recupera del Hashtable la descripción de la retención.  
	 * 
	 * @return Descripcion. De tipo "String". 
	 */
 	public String getDescripcion 			() 	{ return ((String)this.datos.get(ScsRetencionesBean.C_DESCRIPCION));	 					}
	
 	/**
	 * Recupera del Hashtable la fecha de la última modificación hecha en el registro.  
	 * 
	 * @return FechaMod. De tipo "String". 
	 */
 	public String getFechaMod				()	{ return ((String)this.datos.get(ScsRetencionesBean.C_FECHAMODIFICACION)); 					}
	
 	/**
	 * Recupera del Hashtable el usuario que realizó la última modificación en el registro.  
	 * 
	 * @return UsuMod. De tipo "int". 
	 */
 	public int 	  getUsuMod					()	{ return Integer.parseInt((String)this.datos.get(ScsRetencionesBean.C_USUMODIFICACION)); 	}
 	
 	public String[] getComboSociedades	() 	        { return (String[]) this.datos.get("SOCIEDADES");		
  	}
	
}