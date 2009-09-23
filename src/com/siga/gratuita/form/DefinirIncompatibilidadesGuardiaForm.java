package com.siga.gratuita.form;

import com.siga.general.MasterForm;


/**
 * Metodos set y get del formulario del caso de uso de Definir Incompatibilidades de Guardia. 
 * 
 * @author david.sanchezp
 * @since 13/1/2005 
 * @version 1.0
 */
 public class DefinirIncompatibilidadesGuardiaForm extends MasterForm{	
	
	// Metodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 metodo set por cada campo del formulario.
 	public void setIdInstitucionPestanha (String valor)	{ this.datos.put("IDINSTITUCIONPESTAÑA",valor); 	}
 	public void setIdTurnoPestanha (String valor)		{ this.datos.put("IDTURNOPESTAÑA",valor); 	 		}
 	public void setIdGuardiaPestanha (String valor)		{ this.datos.put("IDGUARDIAPESTAÑA",valor); 	 	} 
 	public void setIdIncompatibilidad (String valor)	{ this.datos.put("IDINCOMPATIBILIDAD",valor); 	 	}
 	public void setMotivos (String valor)				{ this.datos.put("MOTIVOS",valor); 	 				}
 	public void setIdTurnoIncompatible (String valor)	{ this.datos.put("IDTURNOINCOMPATIBLE",valor); 	 	}
 	public void setIdGuardiaIncompatible (String valor)	{ this.datos.put("IDGUARDIAINCOMPATIBLE",valor); 	}
 	public void setLugar (String lugar)					{ this.datos.put("LUGAR",lugar); 	 				}
 	public void setZona (String zona)					{ this.datos.put("ZONA",zona); 	 					}
 	public void setSubzona (String subzona)				{ this.datos.put("SUBZONA",subzona); 	 			}
 	public void setPartido (String partido)				{ this.datos.put("PARTIDO",partido); 	 			}
 	public void setArea (String valor)					{ this.datos.put("AREA",valor); 	 				}
 	public void setMateria (String valor)				{ this.datos.put("MATERIA",valor); 	 				}
 	public void setAbreviatura (String valor)			{ this.datos.put("ABREVIATURA",valor); 	 			}
 	public void setTurno (String valor)					{ this.datos.put("TURNO",valor); 	 				}
 	public void setAccion (String valor)				{ this.datos.put("ACCION",valor); 	 				}
 	
 	
	// Metodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 metodo get por cada campo del formulario.
 	public String getIdInstitucionPestanha ()	{ return ((String)this.datos.get("IDINSTITUCIONPESTAÑA")); 	}
 	public String getIdGuardiaPestanha ()		{ return ((String)this.datos.get("IDGUARDIAPESTAÑA")); 		}
 	public String getIdTurnoPestanha ()			{ return ((String)this.datos.get("IDTURNOPESTAÑA")); 		}
 	public String getIdIncompatibilidad ()		{ return ((String)this.datos.get("IDINCOMPATIBILIDAD")); 	}
 	public String getMotivos ()					{ return ((String)this.datos.get("MOTIVOS")); 				}
 	public String getIdTurnoIncompatible ()		{ return ((String)this.datos.get("IDTURNOINCOMPATIBLE")); 	}
 	public String getIdGuardiaIncompatible ()	{ return ((String)this.datos.get("IDGUARDIAINCOMPATIBLE")); }
 	public String getLugar ()					{ return ((String)this.datos.get("LUGAR")); 				}
 	public String getZona ()					{ return ((String)this.datos.get("ZONA")); 					}
 	public String getSubzona ()					{ return ((String)this.datos.get("SUBZONA")); 				}
 	public String getPartido ()					{ return ((String)this.datos.get("PARTIDO")); 				}
 	public String getArea ()					{ return ((String)this.datos.get("AREA")); 					}
 	public String getMateria ()					{ return ((String)this.datos.get("MATERIA")); 				}
 	public String getAbreviatura ()				{ return ((String)this.datos.get("ABREVIATURA")); 			}
 	public String getTurno ()					{ return ((String)this.datos.get("TURNO")); 				}
 	public String getAccion ()					{ return ((String)this.datos.get("ACCION")); 				}

}