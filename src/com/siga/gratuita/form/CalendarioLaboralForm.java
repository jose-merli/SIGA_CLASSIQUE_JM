
package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesForm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 20/12/2004
/**
* Maneja el formulario que mantiene la tabla SCS_CALENDARIOLABORAL
*/
 public class CalendarioLaboralForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
 	
 	/**
	 * Almacena en la Hashtable el identificador de una fiesta 
	 * 
	 * @param Identificador fiesta. 
	 * @return void 
	 */
	public void setIdentificativo	(int id) 					{ this.datos.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO, Integer.toString(id));				}
	
	/**
	 * Almacena en la Hashtable el identificador de una institución 
	 * 
	 * @param Identificador institución 
	 * @return void 
	 */
	public void setIdInstitucion	(int idInstitucion) 		{ this.datos.put(ScsCalendarioLaboralBean.C_IDINSTITUCION, Integer.toString(idInstitucion));	}	
	
	/**
	 * Almacena en la Hashtable una fecha de una fiesta 
	 * 
	 * @param Fecha fiesta
	 * @return void 
	 */
	public void setFecha			(String fecha) 				{ this.datos.put(ScsCalendarioLaboralBean.C_FECHA, fecha);										}	
	
	/**
	 * Almacena en la Hashtable el nombre de una fiesta 
	 * 
	 * @param Nombre de una fiesta.
	 * @return void 
	 */
	public void setNombreFiesta 	(String fiesta) 			{ this.datos.put(ScsCalendarioLaboralBean.C_NOMBREFIESTA,fiesta);		 						}
	
	/**
	 * Almacena en la Hashtable el identificador de un partido judidicial. 
	 * 
	 * @param Identificador partido judicial.
	 * @return void 
	 */
//	public void setIdPartido		(int idPartido)				{ this.datos.put(ScsCalendarioLaboralBean.C_IDPARTIDO,Integer.toString(idPartido));	 			}
	public void setIdPartido		(String idPartido)			{ UtilidadesHash.set(this.datos, ScsCalendarioLaboralBean.C_IDPARTIDO, idPartido);	 			}
	
	/**
	 * Almacena en la Hashtable la fecha de la última modificación. 
	 * 
	 * @param Fecha última modificación.
	 * @return void 
	 */
	public void setFechaMod			(String fechaMod)			{ this.datos.put(ScsCalendarioLaboralBean.C_FECHAMODIFICACION,fechaMod); 					}
	
	/**
	 * Almacena en la Hashtable el usuario de la última modificación. 
	 * 
	 * @param Usuario última modificación.
	 * @return void 
	 */
	public void setUsuMod			(int usuMod)				{ this.datos.put(ScsCalendarioLaboralBean.C_USUMODIFICACION,Integer.toString(usuMod));	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	/**
	 * Recupera del Hashtable el identificativo  
	 * 
	 * @return Identificativo del festivo. 
	 */
	public int	  getIdentificativo		() 						{ return Integer.parseInt((String)this.datos.get(ScsCalendarioLaboralBean.C_IDENTIFICATIVO));	}
	
	/**
	 * Recupera del Hashtable el identificador de la institución  
	 * 
	 * @return Identificador institución
	 */
	public int 	  getIdInstitucion		() 						{ return Integer.parseInt((String)this.datos.get(ScsCalendarioLaboralBean.C_IDINSTITUCION));	}	
	
	/**
	 * Recupera del Hashtable la fecha del festivo  
	 * 
	 * @return Fecha del festivo
	 */
	public String getFecha 				() 						{ return ((String)this.datos.get(ScsCalendarioLaboralBean.C_FECHA));							}	
	
	/**
	 * Recupera del Hashtable el nombre del festivo.  
	 * 
	 * @return Nombre del festivo 
	 */
	public String getNombreFiesta 		() 						{ return ((String)this.datos.get(ScsCalendarioLaboralBean.C_NOMBREFIESTA));}
	
	/**
	 * Recupera del Hashtable el identificador del partido judicial.  
	 * 
	 * @return Identificador partido judicial.
	 */
//	public Integer  getIdPartido		()						{ return Integer.parseInt((String)this.datos.get(ScsCalendarioLaboralBean.C_IDPARTIDO));}
	public String  getIdPartido		()						{ return UtilidadesHash.getString(this.datos, ScsCalendarioLaboralBean.C_IDPARTIDO);}
	
	/**
	 * Recupera del Hashtable la fecha de la última modificación hecha en el registro.  
	 * 
	 * @return FechaMod. De tipo "String". 
	 */
	public String getFechaMod			()						{ return ((String)this.datos.get(ScsRetencionesBean.C_FECHAMODIFICACION));}
	
	/**
	 * Recupera del Hashtable el usuario que realizó la última modificación en el registro.  
	 * 
	 * @return UsuMod. De tipo "int". 
	 */
	public int 	  getUsuMod				()						{ return Integer.parseInt((String)this.datos.get(ScsRetencionesBean.C_USUMODIFICACION));}
	
}