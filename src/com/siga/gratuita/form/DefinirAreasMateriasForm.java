/*
 * Fecha creación: 28/12/2004
 * Autor: julio.vicente
 *
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

 public class DefinirAreasMateriasForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
	public void setIdArea			(Integer idarea)				{ this.datos.put(ScsAreaBean.C_IDAREA, idarea);					}
	
	public void setAccion			(String accion)				{ this.datos.put("ACCION", accion);													}
	
	public void setNombreArea		(String nombreArea)			{ this.datos.put("NOMBREAREA", nombreArea);											}	
	public void setContenidoArea	(String contenidoArea)		{ this.datos.put(ScsAreaBean.C_CONTENIDO, contenidoArea);							}	
	public void setIdInstitucion 	(Integer idinstitucion)			{ this.datos.put(ScsAreaBean.C_IDINSTITUCION,idinstitucion);		}
	
	public void setIdMateria		(Integer idmateria)				{ this.datos.put(ScsMateriaBean.C_IDMATERIA, idmateria);				}
	public void setNombreMateria	(String nombreMateria) 		{ this.datos.put("NOMBREMATERIA", nombreMateria);							}
	public void setContenidoMateria	(String contenidoMateria)	{ this.datos.put(ScsMateriaBean.C_CONTENIDO, contenidoMateria);						}
	public void setIdJurisdiccion   (String dato)               { this.datos.put("IDJURISDICCION" , dato);	}
	public void setJurisdiccion     (String dato)                   { this.datos.put("JURISDICCION" , dato);
	}
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	public Integer    	getIdArea			()						{ return (Integer)this.datos.get(ScsAreaBean.C_IDAREA);			}
	
	public String	getAccion			()						{ return ((String)this.datos.get("ACCION"));										}
	
	public String   getNombreArea		() 						{ return ((String)this.datos.get("NOMBREAREA"));									}	
	public String 	getContenidoArea	()						{ return ((String)this.datos.get(ScsAreaBean.C_CONTENIDO));							}	
	public Integer		getIdInstitucion 	()						{ return (Integer)this.datos.get(ScsAreaBean.C_IDINSTITUCION);		}
	
	public Integer    	getIdMateria		()						{ return (Integer)this.datos.get(ScsMateriaBean.C_IDMATERIA);		}
	public String   getNombreMateria	() 						{ return ((String)this.datos.get("NOMBREMATERIA"));									}	
	public String 	getContenidoMateria	()						{ return ((String)this.datos.get(ScsMateriaBean.C_CONTENIDO));						}
	public String   getIdJurisdiccion   ()                      {return  ((String)this.datos.get("IDJURISDICCION"));}	
	public String   getJurisdiccion   ()                      {return  ((String)this.datos.get(" JURISDICCION"));}
}
  