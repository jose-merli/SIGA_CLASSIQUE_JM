/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_DOCUMENTACIONSOJ
*/
 public class DatosDocumentacionSOJForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))

 	/**
	 * Almacena en la Hashtable el registro de entrada de un SOJ 
	 * 
	 * @param reg. entrada SOJ 
	 * @return void 
	 */
	public void setRegEntrada		(String regEntrada)				{ this.datos.put(ScsDocumentacionSOJBean.C_REGENTRADA, regEntrada);				} 	

 	/**
	 * Almacena en la Hashtable el registro de salida de un SOJ 
	 * 
	 * @param reg. entrada SOJ 
	 * @return void 
	 */
	public void setRegSalida		(String regSalida)				{ this.datos.put(ScsDocumentacionSOJBean.C_REGSALIDA, regSalida);				}
 	
 	/**
	 * Almacena en la Hashtable el identificador de un SOJ 
	 * 
	 * @param Identificador SOJ 
	 * @return void 
	 */
	public void setIdTipoSOJ		(String idTipoSOJ)				{ this.datos.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ, idTipoSOJ);				}
	
	/**
	 * Almacena en la Hashtable el anho de un SOJ 
	 * 
	 * @param Anho del SOJ 
	 * @return void 
	 */
	public void setAnio				(String anio)					{ this.datos.put(ScsDocumentacionSOJBean.C_ANIO, anio);							}
	
	/**
	 * Almacena en la Hashtable el número de un SOJ 
	 * 
	 * @param Número del SOJ 
	 * @return void 
	 */
	public void setNumero			(String idNumero)				{ this.datos.put(ScsDocumentacionSOJBean.C_NUMERO, idNumero);					}
	
	
	/**
	 * Almacena en la Hashtable la fecha de entrega de las documentación de un SOJ 
	 * 
	 * @param Fecha entrega documentacion SOJ 
	 * @return void 
	 */
	public void setFechaEntrega		(String fechaEntrega)		{ this.datos.put(ScsDocumentacionSOJBean.C_FECHAENTREGA, fechaEntrega);				}

	/**
	 * Almacena en la Hashtable la fecha límite de entrega de las documentación de un SOJ 
	 * 
	 * @param Fecha límite entrega documentacion SOJ 
	 * @return void 
	 */
	public void setFechaLimite		(String fechaLimite)		{ this.datos.put(ScsDocumentacionSOJBean.C_FECHALIMITE, fechaLimite);				}
	
	/**
	 * Almacena en la Hashtable la institucion de un SOJ 
	 * 
	 * @param Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion	 	(String idInstitucion)		{ this.datos.put(ScsDocumentacionSOJBean.C_IDINSTITUCION, idInstitucion);		}
	
	/**
	 * Almacena en la Hashtable el identificador de la documentación de un SOJ 
	 * 
	 * @param Identificador documentación SOJ 
	 * @return void 
	 */
	public void setIdDocumentacion	 	(String idDocumentacion)	{ this.datos.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION, idDocumentacion);	}
	
	/**
	 * Almacena en la Hashtable la documentacion de un SOJ 
	 * 
	 * @param Documentación SOJ 
	 * @return void 
	 */
	public void setDocumentacion	 	(String documentacion)		{ this.datos.put(ScsDocumentacionSOJBean.C_DOCUMENTACION, documentacion);	}
	
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	
 	/**
	 * Almacena en la Hashtable el registro de entrada de un SOJ 
	 * 
	 * @param reg. entrada SOJ 
	 * @return void 
	 */
	public String getRegEntrada		()				{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_REGENTRADA);				} 	

 	/**
	 * Almacena en la Hashtable el registro de salida de un SOJ 
	 * 
	 * @param reg. entrada SOJ 
	 * @return void 
	 */
	public String getRegSalida		()				{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_REGSALIDA);				}
	/**
	 * Recupera de la Hashtable el identificador de un SOJ 
	 * @return Integer
	 */
	public String  getIdTipoSOJ			()						{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_IDTIPOSOJ);				}
	
	/**
	 * Recupera de la Hashtable el anho de un SOJ 
	 * @return String
	 */
	public String  getAnio					()					{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_ANIO);					}
	
	/**
	 * Recupera de la Hashtable el número de un SOJ
	 * @return String 
	 */
	public String getNumero				()						{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_NUMERO);					}
		
	/**
	 * Recupera de la Hashtable la fecha de entrega de la documentación de un SOJ
	 * @return String 
	 */
	public String getFechaEntrega		()						{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_FECHAENTREGA);			}
	
	/**
	 * Recupera de la Hashtable la fecha límite de entrega de la documentación de un SOJ
	 * @return String 
	 */
	public String getFechaLimite		()						{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_FECHALIMITE);				}
	
	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return String
	 */
	public String getIdInstitucion	 	()						{return (String)this.datos.get(ScsDocumentacionSOJBean.C_IDINSTITUCION);			}
	
	/**
	 * Recupera de la Hashtable el identificador de la documentacion de un SOJ 
	 * @return String
	 */
	public String getIdDocumentacion 	()						{return (String)this.datos.get(ScsDocumentacionSOJBean.C_IDDOCUMENTACION);			}
	
	/**
	 * Recupera la Hashtable la documentacion de un SOJ 
	 * @return String 
	 */
	public String getDocumentacion	 	()						{ return (String)this.datos.get(ScsDocumentacionSOJBean.C_DOCUMENTACION);			}

}