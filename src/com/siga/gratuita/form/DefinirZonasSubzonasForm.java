/*
 * Fecha creación: 14/01/2004
 * Autor: julio.vicente
 *
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

 public class DefinirZonasSubzonasForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))
	public void setIdZona				(int idzona)				{ this.datos.put(ScsZonaBean.C_IDZONA, Integer.toString(idzona));					}
	
	public void setAccion				(String accion)				{ this.datos.put("ACCION", accion);													}
	
	public void setnombrePartido		(String nombrePartido)		{ this.datos.put(CenPartidoJudicialBean.C_NOMBRE, nombrePartido);					}
	
	public void setNombreZona			(String nombreZona)			{ this.datos.put("NOMBREZONA", nombreZona);											}	
	public void setMunicipiosZona		(String municipiosZona)		{ this.datos.put(ScsZonaBean.C_MUNICIPIOS, municipiosZona);							}	
	public void setIdInstitucionZona	(int idinstitucionZona)		{ this.datos.put("INSTITUCIONZONA",Integer.toString(idinstitucionZona));	}
	
	public void setIdSubzona			(int idsubzona)				{ this.datos.put(ScsSubzonaBean.C_IDSUBZONA, Integer.toString(idsubzona));			}
	public void setNombreSubzona		(String nombreSubzona) 		{ this.datos.put("NOMBRESUBZONA", nombreSubzona);									}
	public void setMunicipiosSubzona	(String municipiosSubzona)	{ this.datos.put(ScsSubzonaBean.C_MUNICIPIOS, municipiosSubzona);					}
	public void setIdInstitucionSubzona	(int idinstitucionSubzona)	{ this.datos.put("INSTITUCIONSUBZONA",Integer.toString(idinstitucionSubzona));		}
//	public void setIdPartido			(int idPartido)				{ this.datos.put(ScsSubzonaBean.C_IDPARTIDO,Integer.toString(idPartido));			}
	public void setPartidosJudiciales	(String[] partidos)				{ this.datos.put("PARTIDOS_JUDICIALES",partidos);			}
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	public int    	getIdZona				()						{ return Integer.parseInt((String)this.datos.get(ScsZonaBean.C_IDZONA));			}
	
	public String	getAccion				()						{ return ((String)this.datos.get("ACCION"));										}
	
	public String 	getnombrePartido		()						{ return (String)this.datos.get(CenPartidoJudicialBean.C_NOMBRE);					}
	
	public String   getNombreZona			() 						{ return (String)this.datos.get("NOMBREZONA");										}	
	public String 	getMunicipiosZona		()						{ return ((String)this.datos.get(ScsZonaBean.C_MUNICIPIOS));						}	
	public int		getIdInstitucionZona	()						{ return Integer.parseInt((String)this.datos.get(ScsZonaBean.C_IDINSTITUCION));		}
	
	public int    	getIdSubzona			()						{ return Integer.parseInt((String)this.datos.get(ScsSubzonaBean.C_IDSUBZONA));		}
	public String   getNombreSubzona		() 						{ return ((String)this.datos.get("NOMBRESUBZONA"));									}	
	public String 	getMunicipiosSubzona	()						{ return ((String)this.datos.get(ScsSubzonaBean.C_MUNICIPIOS));						}
	public int 		getIdInstitucionSubzona	()						{ return Integer.parseInt((String)this.datos.get("INSTITUCIONSUBZONA"));			}
//	public int		getIdPartido			()						{ return Integer.parseInt((String)this.datos.get(ScsSubzonaBean.C_IDPARTIDO));		}
	public String[]	getPartidosJudiciales	()						{ return (String[])this.datos.get("PARTIDOS_JUDICIALES");		}
	
	
}