package com.siga.gratuita.form;

import com.siga.general.MasterForm;


/**
 * Metodos set y get del formulario del caso de uso de Definir Lista de Guardias. 
 * 
 * @author david.sanchezp
 * @since 27/12/2004 
 * @version 1.0
 */
 public class DefinirListaGuardiasForm extends MasterForm{	
	
	// Metodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 metodo set por cada campo del formulario.
 	public void setListaGuardias (String lista)			{ this.datos.put("LISTAGUARDIAS",lista); 	 		}
 	public void setLugar (String lugar)					{ this.datos.put("LUGAR",lugar); 	 				}
 	public void setNombre (String valor)				{ this.datos.put("NOMBRE",valor); 	 				}
 	public void setZona (String zona)					{ this.datos.put("ZONA",zona); 	 					}
 	public void setSubzona (String subzona)				{ this.datos.put("SUBZONA",subzona); 	 			}
 	public void setPartido (String partido)				{ this.datos.put("PARTIDO",partido); 	 			}
 	public void setArea (String valor)					{ this.datos.put("AREA",valor); 	 				}
 	public void setMateria (String valor)				{ this.datos.put("MATERIA",valor); 	 				}
 	public void setOrden (String valor)					{ this.datos.put("ORDEN",valor); 	 				}
 	public void setAccion (String valor)				{ this.datos.put("ACCION",valor); 	 				}
 	public void setAbreviatura (String valor)			{ this.datos.put("ABREVIATURA",valor); 	 			}
 	public void setTurno (String valor)					{ this.datos.put("TURNO",valor); 	 				}
 	public void setIdLista (String valor)				{ this.datos.put("IDLISTA",valor); 	 				}
 	public void setObservaciones (String valor)			{ this.datos.put("OBSERVACIONES",valor); 	 		}
 	public void setIdGuardia (String valor)				{ this.datos.put("IDGUARDIA",valor); 	 			}
 	public void setIdTurno (String valor)				{ this.datos.put("IDTURNO",valor); 	 				}
 	public void setOrdenOriginal (String valor)			{ this.datos.put("ORDENORIGINAL",valor); 	 		}
 	public void setIdInstitucion (String valor)			{ this.datos.put("IDINSTITUCION",valor); 	 		}
 	public void setComunicacion (String valor)			{ this.datos.put("COMUNICACION",valor); 	 		}
 	
 	
 	
	// Metodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 metodo get por cada campo del formulario.
 	public String getListaGuardias ()			{ return ((String)this.datos.get("LISTAGUARDIAS")); 		}
 	public String getLugar ()					{ return ((String)this.datos.get("LUGAR")); 				}
 	public String getNombre ()					{ return ((String)this.datos.get("NOMBRE")); 				}
 	public String getZona ()					{ return ((String)this.datos.get("ZONA")); 					}
 	public String getSubzona ()					{ return ((String)this.datos.get("SUBZONA")); 				}
 	public String getPartido ()					{ return ((String)this.datos.get("PARTIDO")); 				}
 	public String getArea ()					{ return ((String)this.datos.get("AREA")); 					}
 	public String getMateria ()					{ return ((String)this.datos.get("MATERIA")); 				}
 	public String getOrden ()					{ return ((String)this.datos.get("ORDEN")); 				}
 	public String getAccion ()					{ return ((String)this.datos.get("ACCION")); 				}
 	public String getAbreviatura ()				{ return ((String)this.datos.get("ABREVIATURA")); 			}
 	public String getTurno ()					{ return ((String)this.datos.get("TURNO")); 				}
 	public String getIdLista ()					{ return ((String)this.datos.get("IDLISTA")); 				}
 	public String getObservaciones ()			{ return ((String)this.datos.get("OBSERVACIONES")); 		}
 	public String getIdGuardia ()				{ return ((String)this.datos.get("IDGUARDIA")); 			}
 	public String getIdTurno ()					{ return ((String)this.datos.get("IDTURNO")); 				}
 	public String getOrdenOriginal ()			{ return ((String)this.datos.get("ORDENORIGINAL")); 		}
 	public String getIdInstitucion ()			{ return ((String)this.datos.get("IDINSTITUCION")); 		}
 	public String getComunicacion ()			{ return ((String)this.datos.get("COMUNICACION"));  		}

 	// Variables para la gestion de informes (Listas de Guardias)
 	
	private String fechaInicio="";
	private String fechaFin="";
 	
	public void setFechaInicio			(String fecha)	{this.fechaInicio=fecha;}
	public void setFechaFin			 	(String fecha)	{this.fechaFin=fecha;}
	
	public String getFechaInicio		()	{return (String)this.fechaInicio;}
	public String getFechaFin			()	{return (String)this.fechaFin;}
	
}