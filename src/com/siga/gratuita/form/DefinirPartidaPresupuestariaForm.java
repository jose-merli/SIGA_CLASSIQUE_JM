package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

//Clase: ScsRetencionesAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 21/12/2004
/**
 * Maneja el formulario que mantiene la tabla SCS_PARTIDAPRESUPUESTARIA
 */
 public class DefinirPartidaPresupuestariaForm extends MasterForm {	
	
	// Metodos Set (Formulario (*.jsp))
	/**
	 * Almacena en la Hashtable el identificador de una partida 
	 * 
	 * @param Identificador retención. 
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
	 * Almacena en la Hashtable la fecha de última modificación de una partida 
	 * 
	 * @param Fecha de última modificación de la partida.
	 * @return void 
	 */
	public void setFechaMod					(String fechaMod)		{ this.datos.put(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION,fechaMod); 					}
	
	/**
	 * Almacena en la Hashtable el usuario de la última modificación de una partida 
	 * 
	 * @param Usuario de la última modificación de la partida.
	 * @return void 
	 */
	public void setUsuMod					(int usuMod)			{ this.datos.put(ScsPartidaPresupuestariaBean.C_USUMODIFICACION,Integer.toString(usuMod));		}
	
	/**
	 * Almacena en la Hashtable el identificador de la institución 
	 * 
	 * @param Identificador de la institución
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
	 * Recupera del Hashtable la descripción de una partida  
	 * 
	 * @return Descripción de la partida 
	 */
	public String getDescripcion				()					{ return ((String)this.datos.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION));								}	
	
	/**
	 * Recupera del Hashtable la fecha de la última modificación de una partida  
	 * 
	 * @return Fecha de la última modificación de una partida 
	 */
	public String getFechaMod					()					{ return ((String)this.datos.get(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION));						}
	
	/**
	 * Recupera del Hashtable el usuario de la última modificación de una partida  
	 * 
	 * @return Usuario de la última modificación de una partida 
	 */
	public int  getUsuMod						()					{ return Integer.parseInt((String)this.datos.get(ScsPartidaPresupuestariaBean.C_USUMODIFICACION));			}
	
	/**
	 * Recupera del Hashtable el identificador de una institución  
	 * 
	 * @return Identificador de la institución 
	 */
	public int getIdInstitucion				()					{ return Integer.parseInt((String)this.datos.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION));			}
	/**
	 * Recupera de la Hashtable el importe de una partida

	 * @return String 
	 */
	public String getImportePartida				()					{ return this.datos.get(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA).toString();							}
}	
	
