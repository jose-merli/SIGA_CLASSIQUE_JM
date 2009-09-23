/*
 * Fecha creación: 07/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_TELEFONOSPERSONAJG
*/
 public class DefinirTelefonosPersonaForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
 	/**
	 * Almacena en la Hashtable el identificador de la persona 
	 * 
	 * @param Identificador persona
	 * @return void 
	 */
	public void setIdPersona			(String idPersona)				{ this.datos.put(ScsTelefonosPersonaBean.C_IDPERSONA, idPersona);					}

	/**
	 * Almacena en la Hashtable la institucion 
	 * 
	 * @param Institucion 
	 * @return void 
	 */
	public void setIdInstitucion	 	(String idInstitucion)			{ this.datos.put(ScsTelefonosPersonaBean.C_IDINSTITUCION, idInstitucion);			}
	
	/**
	 * Almacena en la Hashtable el identificador del teléfono 
	 * 
	 * @param Identificador teléfono
	 * @return void 
	 */
	public void setIdTelefono		 	(String idTelefono)				{ this.datos.put(ScsTelefonosPersonaBean.C_IDTELEFONO, idTelefono);					}
	
	/**
	 * Almacena en la Hashtable el nombre del teléfono 
	 * 
	 * @param Nombre teléfono
	 * @return void 
	 */
	public void setNombreTelefono		(String nombreTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NOMBRETELEFONO, nombreTelefono);			}
	
	/**
	 * Almacena en la Hashtable el número de teléfono 
	 * 
	 * @param Número teléfono
	 * @return void 
	 */
	public void setNumeroTelefono		(String numeroTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NUMEROTELEFONO, numeroTelefono);			}
	
		
	// Metodos Get 1 por campo Formulario (*.jsp)
 	/**
	 * Recupera de la Hashtable el identificador abogado de un SOJ 
	 * @return int 
	 */
	public String getIdPersona			()								{ return this.datos.get(ScsTelefonosPersonaBean.C_IDPERSONA).toString();							}

	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return int 
	 */
	public String getIdInstitucion	 	()								{ return this.datos.get(ScsTelefonosPersonaBean.C_IDINSTITUCION).toString();						}

	/**
	 * Recupera de la Hashtable el identificador del teléfono
	 * @return Integer 
	 */
	public String getIdTelefono			()								{ return this.datos.get(ScsTelefonosPersonaBean.C_IDTELEFONO).toString();							}
	
	/**
	 * Recupera de la Hashtable el nombre del teléfono
	 * @return String 
	 */
	public String getNombreTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NOMBRETELEFONO).toString();						}
	
	/**
	 * Recupera de la Hashtable el número de teléfono
	 * @return String 
	 */
	public String getNumeroTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NUMEROTELEFONO).toString();						}

		
}
  