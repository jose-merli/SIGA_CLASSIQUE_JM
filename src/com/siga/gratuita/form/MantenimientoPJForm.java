package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * Métodos set y get del formulario del caso de uso del Mantenimiento de Partidos Judiciales. 
 * 
 * @author david.sanchezp
 * @since 2/12/2004 
 * @version 1.0
 */
 public class MantenimientoPJForm extends MasterForm
 {	
	// Métodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 método set por cada campo del formulario.
 	public void setPartidoJudicial (String pj)			{ this.datos.put("PARTIDOJUDICIAL",pj); 				}
	public void setPoblacion (String poblacion)			{ this.datos.put("POBLACION",poblacion); 				}
	public void setProvincia (String provincia)			{ this.datos.put("PROVINCIA",provincia); 	 			}
 	public void setIdPartido (String idPartido)			{ this.datos.put("IDPARTIDO",idPartido); 	 			}
 	public void setIdPoblacion (String idPoblacion)		{ this.datos.put("IDPOBLACION",idPoblacion); 	 		}
 	public void setIdProvincia (String idProvincia)		{ this.datos.put("IDPROVINCIA",idProvincia); 	 		}
 	public void setUsuMod (String usuMod)				{ this.datos.put("USUMOD",usuMod); 	 					}
 	public void setFechaMod (String fechaMod)			{ this.datos.put("FECHAMOD",fechaMod); 	 				}
 	public void setCambiar (String cambiar)				{ this.datos.put("CAMBIAR",cambiar); 	 				}
 	public void setComboPoblaciones (String[] poblac)     {this.datos.put("LISTAPOBLACIONES",poblac);           }
 	public void setComboInstituciones (String[] inst)     {this.datos.put("LISTAINSTITUCIONES",inst);           }
 	
	// Métodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 método get por cada campo del formulario.
	public String getPartidoJudicial ()		  		{ return ((String)this.datos.get("PARTIDOJUDICIAL")); 		}
	public String getPoblacion ()					{ return ((String)this.datos.get("POBLACION")); 			}
	public String getProvincia () 				 	{ return ((String)this.datos.get("PROVINCIA")); 			}
	public String getIdPartido () 				 	{ return ((String)this.datos.get("IDPARTIDO")); 			}
	public String getIdProvincia () 				{ return ((String)this.datos.get("IDPROVINCIA")); 			}
	public String getIdPoblacion () 				{ return ((String)this.datos.get("IDPOBLACION")); 			}
	public String getUsuMod () 						{ return ((String)this.datos.get("USUMOD")); 				}
	public String getFechaMod () 					{ return ((String)this.datos.get("FECHAMOD")); 				}
	public String getCambiar () 					{ return ((String)this.datos.get("CAMBIAR")); 				}
	public String[] getComboPoblaciones	() 	        { return (String[]) this.datos.get("LISTAPOBLACIONES"); 	}
	public String[] getComboInstituciones	() 	    { return (String[]) this.datos.get("LISTAINSTITUCIONES");  	}
}