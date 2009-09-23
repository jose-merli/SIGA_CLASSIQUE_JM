
package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesForm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 08/04/2005
/**
* Maneja el formulario que mantiene la tabla SCS_GRUPOFACTURACION
*/
 public class DefinirGrupoFacturacionForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
 	
 	/**
	 * Almacena en la Hashtable el identificador de un grupo de facturación 
	 * 
	 * @param Identificador grupo facturación. 
	 * @return void 
	 */
	public void setIdGrupoFacturacion		(String id) 					{ this.datos.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION, id);								}
	
	/**
	 * Almacena en la Hashtable el identificador de una institución 
	 * 
	 * @param Identificador institución 
	 * @return void 
	 */
	public void setIdInstitucion			(String idInstitucion) 		{ this.datos.put(ScsGrupoFacturacionBean.C_IDINSTITUCION, idInstitucion);							}	
	
	
	/**
	 * Almacena en la Hashtable el nombre del grupo de facturación 
	 * 
	 * @param Nombre del grupo de facturación.
	 * @return void 
	 */
	public void setNombre		 			(String nombre) 			{ this.datos.put(ScsGrupoFacturacionBean.C_NOMBRE,nombre);		 									}
			
	
	
	/**
	 * Almacena en la Hashtable la fecha de la última modificación. 
	 * 
	 * @param Fecha última modificación.
	 * @return void 
	 */
	public void setFechaMod			(String fechaMod)			{ this.datos.put(ScsGrupoFacturacionBean.C_FECHAMODIFICACION,fechaMod); 					}
	
	/**
	 * Almacena en la Hashtable el usuario de la última modificación. 
	 * 
	 * @param Usuario última modificación.
	 * @return void 
	 */
	public void setUsuMod			(int usuMod)				{ this.datos.put(ScsGrupoFacturacionBean.C_USUMODIFICACION,Integer.toString(usuMod));	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	/**
	 * Recupera de  la Hashtable el identificador de un grupo de facturación
	 * @return String 
	 */
	public String getIdGrupoFacturacion		() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION).toString();		}
	
	/**
	 * Recupera de  la Hashtable el identificador de una institución 
	 * @return String 
	 */
	public String getIdInstitucion			() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_IDINSTITUCION).toString();			}	
	
	
	/**
	 * Recupera de  la Hashtable el nombre del grupo de facturación 
	 * @return String 
	 */
	public String getNombre		 			() 					{ return this.datos.get(ScsGrupoFacturacionBean.C_NOMBRE).toString();					}
			
	
	/**
	 * Recupera de  la Hashtable la fecha de la última modificación.
	 * @return String 
	 */
	public String getFechaMod				()					{ return this.datos.get(ScsGrupoFacturacionBean.C_FECHAMODIFICACION).toString();		}
	
	/**
	 * Recupera de  la Hashtable el usuario de la última modificación.
	 * @return String 
	 */
	public String getUsuMod					()					{ return this.datos.get(ScsGrupoFacturacionBean.C_USUMODIFICACION).toString();			}
}