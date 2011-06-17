package com.siga.gratuita.form;

import com.siga.general.MasterForm;


/**
 * Metodos set y get del formulario del caso de uso de Saltos Y Compensaciones. 
 * 
 * @author david.sanchezp
 * @since 23/2/2005 
 * @version 1.0
 */
 public class SaltosYCompensacionesForm extends MasterForm{	

	// Metodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 metodo set por cada campo del formulario.
 	public void setIdInstitucion (String valor)			{ this.datos.put("IDINSTITUCION",valor); 			}
 	public void setIdTurno (String valor)				{ this.datos.put("IDTURNO",valor); 	 				}
 	public void setIdGuardia(String valor)				{ this.datos.put("IDGUARDIA",valor); 	 			} 
 	public void setIdGrupoGuardia(String valor)			{ this.datos.put("IDGRUPOGUARDIA",valor); 	 		} 
 	public void setIdPersona(String valor)				{ this.datos.put("IDPERSONA",valor); 	 			}
 	public void setFecha (String valor)					{ this.datos.put("FECHA",valor); 	 				}
 	public void setFechaCumplimiento (String valor)		{ this.datos.put("FECHACUMPLIMIENTO",valor); 	 	} 
 	public void setFechaDesde (String valor)			{ this.datos.put("FECHADESDE",valor); 	 			}
 	public void setFechaHasta (String valor)			{ this.datos.put("FECHAHASTA",valor); 	 			}
 	public void setNumeroLetrado (String valor)			{ this.datos.put("NUMEROLETRADO",valor); 	 		}
 	public void setLetrado (String valor)				{ this.datos.put("LETRADO",valor); 	 				} 	
 	public void setSalto (String valor)					{ this.datos.put("SALTO",valor); 	 				}
 	public void setCompensado (String valor)			{ this.datos.put("COMPENSADO",valor); 	 			}
 	public void setMotivos (String valor)				{ this.datos.put("MOTIVOS",valor); 	 				}
 	public void setIdSaltosTurno (String valor)			{ this.datos.put("IDSALTOSTURNO",valor); 	 		}
 	public void setIdSaltoCompensacionGrupo(String valor){ this.datos.put("IDSALTOCOMPENSACIONGRUPO",valor);} 	
 	
 	
	// Metodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 metodo get por cada campo del formulario.
 	public String getIdInstitucion ()			{ return ((String)this.datos.get("IDINSTITUCION")); 		}
 	public String getIdTurno ()					{ return ((String)this.datos.get("IDTURNO")); 				}
 	public String getIdGuardia ()				{ return ((String)this.datos.get("IDGUARDIA")); 			}
 	public String getIdGrupoGuardia ()			{ return ((String)this.datos.get("IDGRUPOGUARDIA")); 		}
 	public String getIdPersona ()				{ return ((String)this.datos.get("IDPERSONA")); 			}
 	public String getFecha ()					{ return ((String)this.datos.get("FECHA")); 				}
 	public String getFechaCumplimiento ()		{ return ((String)this.datos.get("FECHACUMPLIMIENTO")); 	}
 	public String getFechaDesde ()				{ return ((String)this.datos.get("FECHADESDE")); 			}
 	public String getFechaHasta ()				{ return ((String)this.datos.get("FECHAHASTA")); 			}
 	public String getNumeroLetrado ()			{ return ((String)this.datos.get("NUMEROLETRADO")); 		}
 	public String getLetrado ()					{ return ((String)this.datos.get("LETRADO")); 				} 	
 	public String getSalto ()					{ return ((String)this.datos.get("SALTO")); 				}
 	public String getCompensado ()				{ return ((String)this.datos.get("COMPENSADO")); 			}
 	public String getMotivos ()					{ return ((String)this.datos.get("MOTIVOS")); 				}
 	public String getIdSaltosTurno ()			{ return ((String)this.datos.get("IDSALTOSTURNO")); 		}
 	public String getIdSaltoCompensacionGrupo()	{ return ((String)this.datos.get("IDSALTOCOMPENSACIONGRUPO")); 		} 	
 	
}