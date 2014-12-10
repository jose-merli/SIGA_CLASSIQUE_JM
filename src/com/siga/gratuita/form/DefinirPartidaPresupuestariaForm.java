package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 21/12/2004
/**
 * Maneja el formulario que mantiene la tabla SCS_PARTIDAPRESUPUESTARIA
 */
 public class DefinirPartidaPresupuestariaForm extends MasterForm {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1472733069240658238L;
	// Metodos Set (Formulario (*.jsp))
	/**
	 * Almacena en la Hashtable el identificador de una partida 
	 * 
	 * @param Identificador retenci�n. 
	 * @return void 
	 */
	public void setIdPartidaPresupuestaria	(int id) 				{ this.datos.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA, Integer.toString(id));	}
	
	/**
	 * Almacena en la Hashtable el nombre de una partida 
	 * 
	 * @param Nombre de la partida
	 * @return void 
	 */
	public void setNombrePartida			(String nombre) 		{ this.datos.put(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA, nombre);			}	
	
	/**
	 * Almacena en la Hashtable la descripcion de una partida 
	 * 
	 * @param Descripcion de la partida
	 * @return void 
	 */
	public void setDescripcion				(String descripcion)	{ this.datos.put(ScsPartidaPresupuestariaBean.C_DESCRIPCION, descripcion);						}	
	/**
	 * Almacena en la Hashtable el importe de una partida 
	 * 
	 * @param Importe de la partida
	 * @return void 
	 */
	public void setImportePartida			(String importePartida)	{ this.datos.put(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA, importePartida);				}
	
	/**
	 * Almacena en la Hashtable la fecha de �ltima modificaci�n de una partida 
	 * 
	 * @param Fecha de �ltima modificaci�n de la partida.
	 * @return void 
	 */
	public void setFechaMod					(String fechaMod)		{ this.datos.put(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION,fechaMod); 					}
	
	/**
	 * Almacena en la Hashtable el usuario de la �ltima modificaci�n de una partida 
	 * 
	 * @param Usuario de la �ltima modificaci�n de la partida.
	 * @return void 
	 */
	public void setUsuMod					(int usuMod)			{ this.datos.put(ScsPartidaPresupuestariaBean.C_USUMODIFICACION,Integer.toString(usuMod));		}
	
	/**
	 * Almacena en la Hashtable el identificador de la instituci�n 
	 * 
	 * @param Identificador de la instituci�n
	 * @return void 
	 */
	public void setIdInstitucion			(int idInstitucion)		{ this.datos.put(ScsPartidaPresupuestariaBean.C_IDINSTITUCION,Integer.toString(idInstitucion));	}

	
	// Metodos Get 1 por campo Formulario (*.jsp)
	/**
	 * Recupera del Hashtable el identificador de una partida  
	 * 
	 * @return Identificador de la partida 
	 */
	public int 	  getIdPartidaPresupuestaria	()					{ return Integer.parseInt((String)this.datos.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA));	}
	
	/**
	 * Recupera del Hashtable el nombre de una partida  
	 * 
	 * @return Nombre de la partida 
	 */
	public String getNombrePartida				()		 			{ return ((String)this.datos.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA));							}	
	
	/**
	 * Recupera del Hashtable la descripci�n de una partida  
	 * 
	 * @return Descripci�n de la partida 
	 */
	public String getDescripcion				()					{ return ((String)this.datos.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION));								}	
	
	/**
	 * Recupera del Hashtable la fecha de la �ltima modificaci�n de una partida  
	 * 
	 * @return Fecha de la �ltima modificaci�n de una partida 
	 */
	public String getFechaMod					()					{ return ((String)this.datos.get(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION));						}
	
	/**
	 * Recupera del Hashtable el usuario de la �ltima modificaci�n de una partida  
	 * 
	 * @return Usuario de la �ltima modificaci�n de una partida 
	 */
	public int  getUsuMod						()					{ return Integer.parseInt((String)this.datos.get(ScsPartidaPresupuestariaBean.C_USUMODIFICACION));			}
	
	/**
	 * Recupera del Hashtable el identificador de una instituci�n  
	 * 
	 * @return Identificador de la instituci�n 
	 */
	public int getIdInstitucion				()					{ return Integer.parseInt((String)this.datos.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION));			}
	/**
	 * Recupera de la Hashtable el importe de una partida

	 * @return String 
	 */
	public String getImportePartida				()					{ return this.datos.get(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA).toString();							}
}	
	
