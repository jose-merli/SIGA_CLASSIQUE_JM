
package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesForm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 08/04/2005
/**
* Maneja el formulario que mantiene la tabla SCS_GRUPOFACTURACION
*/
 public class DefinirGrupoFacturacionForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
 	
 	/**
	 * 
	 */
	private static final long serialVersionUID = 255831222685103053L;

	/**
	 * Almacena en la Hashtable el identificador de un grupo de facturaci�n 
	 * 
	 * @param Identificador grupo facturaci�n. 
	 * @return void 
	 */
	public void setIdGrupoFacturacion		(String id) 					{ this.datos.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION, id);								}
	
	/**
	 * Almacena en la Hashtable el identificador de una instituci�n 
	 * 
	 * @param Identificador instituci�n 
	 * @return void 
	 */
	public void setIdInstitucion			(String idInstitucion) 		{ this.datos.put(ScsGrupoFacturacionBean.C_IDINSTITUCION, idInstitucion);							}	
	
	
	/**
	 * Almacena en la Hashtable el nombre del grupo de facturaci�n 
	 * 
	 * @param Nombre del grupo de facturaci�n.
	 * @return void 
	 */
	public void setNombre		 			(String nombre) 			{ this.datos.put(ScsGrupoFacturacionBean.C_NOMBRE,nombre);		 									}
			
	
	
	/**
	 * Almacena en la Hashtable la fecha de la �ltima modificaci�n. 
	 * 
	 * @param Fecha �ltima modificaci�n.
	 * @return void 
	 */
	public void setFechaMod			(String fechaMod)			{ this.datos.put(ScsGrupoFacturacionBean.C_FECHAMODIFICACION,fechaMod); 					}
	
	/**
	 * Almacena en la Hashtable el usuario de la �ltima modificaci�n. 
	 * 
	 * @param Usuario �ltima modificaci�n.
	 * @return void 
	 */
	public void setUsuMod			(int usuMod)				{ this.datos.put(ScsGrupoFacturacionBean.C_USUMODIFICACION,Integer.toString(usuMod));	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	/**
	 * Recupera de  la Hashtable el identificador de un grupo de facturaci�n
	 * @return String 
	 */
	public String getIdGrupoFacturacion		() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION).toString();		}
	
	/**
	 * Recupera de  la Hashtable el identificador de una instituci�n 
	 * @return String 
	 */
	public String getIdInstitucion			() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_IDINSTITUCION).toString();			}	
	
	
	/**
	 * Recupera de  la Hashtable el nombre del grupo de facturaci�n 
	 * @return String 
	 */
	public String getNombre		 			() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_NOMBRE).toString();					}
			
	
	/**
	 * Recupera de  la Hashtable la fecha de la �ltima modificaci�n.
	 * @return String 
	 */
	public String getFechaMod				()					{ return this.datos.get(ScsGrupoFacturacionBean.C_FECHAMODIFICACION).toString();		}
	
	/**
	 * Recupera de  la Hashtable el usuario de la �ltima modificaci�n.
	 * @return String 
	 */
	public String getUsuMod					()					{ return this.datos.get(ScsGrupoFacturacionBean.C_USUMODIFICACION).toString();			}
}