package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;


/**
 * Metodos set y get del formulario del caso de uso de Definir Calendario de Guardias. 
 * 
 * @author david.sanchezp
 * @since 19/1/2005 
 * @version 1.0
 */
 public class DefinirCalendarioGuardiaForm extends MasterForm{	
	
	// Metodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 metodo set por cada campo del formulario.
 	public void setIdInstitucionPestanha (String valor)	{ this.datos.put("IDINSTITUCIONPESTAÑA",valor); 	}
 	public void setIdTurnoPestanha (String valor)		{ this.datos.put("IDTURNOPESTAÑA",valor); 	 		}
 	public void setIdGuardiaPestanha (String valor)		{ this.datos.put("IDGUARDIAPESTAÑA",valor); 	 	} 
 	public void setIdCalendarioGuardias (String valor)	{ this.datos.put("IDCALENDARIOGUARDIAS",valor); 	}
 	public void setIdInstitucion (String valor)			{ this.datos.put("IDINSTITUCION",valor); 			}
 	public void setIdTurno (String valor)				{ this.datos.put("IDTURNO",valor); 	 				}
 	public void setIdGuardia(String valor)				{ this.datos.put("IDGUARDIA",valor); 	 			} 
 	public void setIdPersona(String valor)				{ this.datos.put("IDPERSONA",valor); 	 			}
 	public void setAccion (String valor)				{ this.datos.put("ACCION",valor); 	 				}
 	public void setFechaInicio (String valor)			{ this.datos.put("FECHAINICIO",valor); 	 			}
 	public void setFechaFin (String valor)				{ this.datos.put("FECHAFIN",valor); 	 			}
 	public void setObservaciones (String valor)			{ this.datos.put("OBSERVACIONES",valor); 	 		}
 	public void setReserva (String valor)				{ this.datos.put("RESERVA",valor); 	 				}
 	public void setDiasGuardia (String valor)			{ this.datos.put("DIASGUARDIA",valor); 	 			}
 	public void setTipoDias (String valor)				{ this.datos.put("TIPODIAS",valor); 	 			}
 	public void setDiasACobrar (String valor)			{ this.datos.put("DIASACOBRAR",valor); 	 			}
 	public void setContador (String valor)				{ this.datos.put("CONTADOR",valor); 	 			}
 	public void setFechaDesde (String valor)			{ this.datos.put("FECHADESDE",valor); 	 			}
 	public void setFechaHasta (String valor)			{ this.datos.put("FECHAHASTA",valor); 	 			}
 	public void setNumeroLetrados (String valor)		{ this.datos.put("NUMEROLETRADOS",valor); 	 		}
 	public void setNumeroSustitutos (String valor)		{ this.datos.put("NUMEROSUSTITUTOS",valor); 	 	}
 	public void setModoOriginal (String valor)			{ this.datos.put("MODOORIGINAL",valor); 	 		}
 	public void setFechaApertura (String valor)			{ this.datos.put("FECHAAPERTURA",valor); 	 		}
 	public void setNColegiado (String valor)			{ this.datos.put("NCOLEGIADO",valor); 	 			}
 	public void setFlagSalto (String valor)				{ this.datos.put("FLAGSALTO",valor); 	 			}
 	public void setFlagCompensacion (String valor)		{ this.datos.put("FLAGCOMPENSACION",valor); 	 	}
 	public void setPeriodos (String valor)				{ this.datos.put("PERIODOS",valor); 	 			}
 	public void setIndicePeriodo (String valor)			{ this.datos.put("INDICEPERIODO",valor); 	 		}
 	public void setGuardiasPeriodo (String valor)		{ this.datos.put("GUARDIASPERIODO",valor); 	 		}
 	public void setBuscarFechaDesde (String d) 			{UtilidadesHash.set(datos, "FECHA_DESDE", d);		}
	public void setBuscarFechaHasta (String  d) 		{UtilidadesHash.set(datos, "FECHA_HASTA", d); 		}
	public void setBuscarColegiado (String d) 			{UtilidadesHash.set(datos, "BUSCARCOLEGIADO", d);	}
	
 	
	// Metodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 metodo get por cada campo del formulario.
 	public String getIdInstitucionPestanha ()	{ return ((String)this.datos.get("IDINSTITUCIONPESTAÑA")); 	}
 	public String getIdGuardiaPestanha ()		{ return ((String)this.datos.get("IDGUARDIAPESTAÑA")); 		}
 	public String getIdTurnoPestanha ()			{ return ((String)this.datos.get("IDTURNOPESTAÑA")); 		}
 	public String getIdCalendarioGuardias ()	{ return ((String)this.datos.get("IDCALENDARIOGUARDIAS")); 	}
 	public String getIdInstitucion ()			{ return ((String)this.datos.get("IDINSTITUCION")); 		}
 	public String getIdGuardia ()				{ return ((String)this.datos.get("IDGUARDIA")); 			}
 	public String getIdTurno ()					{ return ((String)this.datos.get("IDTURNO")); 				}
 	public String getIdPersona ()				{ return ((String)this.datos.get("IDPERSONA")); 			}
 	public String getAccion ()					{ return ((String)this.datos.get("ACCION")); 				}
 	public String getFechaInicio ()				{ return ((String)this.datos.get("FECHAINICIO")); 			}
 	public String getFechaFin ()				{ return ((String)this.datos.get("FECHAFIN")); 				}
 	public String getObservaciones ()			{ return ((String)this.datos.get("OBSERVACIONES")); 		}
 	public String getReserva ()					{ return ((String)this.datos.get("RESERVA")); 				}
 	public String getDiasGuardia ()				{ return ((String)this.datos.get("DIASGUARDIA")); 			}
 	public String getTipoDias ()				{ return ((String)this.datos.get("TIPODIAS")); 				}
 	public String getDiasACobrar ()				{ return ((String)this.datos.get("DIASACOBRAR")); 			}
 	public String getContador ()				{ return ((String)this.datos.get("CONTADOR")); 				}
 	public String getFechaDesde ()				{ return ((String)this.datos.get("FECHADESDE")); 			}
 	public String getFechaHasta ()				{ return ((String)this.datos.get("FECHAHASTA")); 			}
 	public String getNumeroLetrados ()			{ return ((String)this.datos.get("NUMEROLETRADOS")); 		}
 	public String getNumeroSustitutos ()		{ return ((String)this.datos.get("NUMEROSUSTITUTOS")); 		}
 	public String getModoOriginal ()			{ return ((String)this.datos.get("MODOORIGINAL")); 			} 	
 	public String getFechaApertura ()			{ return ((String)this.datos.get("FECHAAPERTURA")); 		}
 	public String getNColegiado ()				{ return ((String)this.datos.get("NCOLEGIADO")); 			}
 	public String getFlagSalto ()				{ return ((String)this.datos.get("FLAGSALTO")); 			}
 	public String getFlagCompensacion ()		{ return ((String)this.datos.get("FLAGCOMPENSACION")); 		}
 	public String getPeriodos ()				{ return ((String)this.datos.get("PERIODOS")); 				}
 	public String getIndicePeriodo ()			{ return ((String)this.datos.get("INDICEPERIODO")); 		}
 	public String getGuardiasPeriodo ()			{ return ((String)this.datos.get("GUARDIASPERIODO")); 		}
 	public String getBuscarFechaDesde () 		{return UtilidadesHash.getString(datos, "FECHA_DESDE");	}
	public String getBuscarFechaHasta () 		{return UtilidadesHash.getString(datos, "FECHA_HASTA");	}
	public String getBuscarColegiado () 		{return UtilidadesHash.getString(datos, "BUSCARCOLEGIADO");	 	}
 	
}