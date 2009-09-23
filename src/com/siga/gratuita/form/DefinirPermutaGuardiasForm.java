package com.siga.gratuita.form;

import com.siga.general.MasterForm;


/**
 * Metodos set y get del formulario del caso de uso de Definir Permuta de Guardias. 
 * 
 * @author david.sanchezp
 * @since 10/2/2005 
 * @version 1.0
 */
 public class DefinirPermutaGuardiasForm extends MasterForm {	
	
	// Metodos Set para escribir en una tabla hash datos los campos del formulario.
 	// Hay 1 metodo set por cada campo del formulario.
 	public void setIdInstitucion (String valor)						{ this.datos.put("IDINSTITUCION",valor); 					}
 	public void setIdTurnoSolicitante (String valor)				{ this.datos.put("IDTURNOSOLICITANTE",valor); 	 			}
 	public void setIdGuardiaSolicitante (String valor)				{ this.datos.put("IDGUARDIASOLICITANTE",valor); 	 		} 
 	public void setIdCalendarioGuardiasSolicitante (String valor)	{ this.datos.put("IDCALENDARIOGUARDIASSOLICITANTE",valor); 	}
 	public void setIdPersonaSolicitante(String valor)				{ this.datos.put("IDPERSONASOLICITANTE",valor); 	 		}
 	public void setFechaInicioSolicitante (String valor)			{ this.datos.put("FECHAINICIOSOLICITANTE",valor); 	 		}
 	public void setFechaFinSolicitante (String valor)				{ this.datos.put("FECHAFINSOLICITANTE",valor); 	 			}
 	public void setNombreSolicitante (String valor)					{ this.datos.put("NOMBRESOLICITANTE",valor); 	 			}
 	public void setNumeroColegiadoSolicitante (String valor)		{ this.datos.put("NUMEROCOLEGIADOSOLICITANTE",valor); 	 	}
 	public void setMotivosSolicitante (String valor)				{ this.datos.put("MOTIVOSSOLICITANTE",valor); 	 			}
 	public void setIdTurnoConfirmador (String valor)				{ this.datos.put("IDTURNOCONFIRMADOR",valor); 	 			}
 	public void setIdGuardiaConfirmador (String valor)				{ this.datos.put("IDGUARDIACONFIRMADOR",valor); 	 		} 
 	public void setIdCalendarioGuardiasConfirmador (String valor)	{ this.datos.put("IDCALENDARIOGUARDIASCONFIRMADOR",valor); 	}
 	public void setIdPersonaConfirmador(String valor)				{ this.datos.put("IDPERSONACONFIRMADOR",valor); 	 		}
 	public void setFechaInicioConfirmador (String valor)			{ this.datos.put("FECHAINICIOCONFIRMADOR",valor); 	 		}
 	public void setFechaFinConfirmador (String valor)				{ this.datos.put("FECHAFINCONFIRMADOR",valor); 	 			}
 	public void setNombreConfirmador (String valor)					{ this.datos.put("NOMBRECONFIRMADOR",valor); 	 			}
 	public void setNumeroColegiadoConfirmador (String valor)		{ this.datos.put("NUMEROCOLEGIADOCONFIRMADOR",valor); 	 	}
 	public void setMotivosConfirmador (String valor)				{ this.datos.put("MOTIVOSCONFIRMADOR",valor); 	 			}
 	public void setAccion (String valor)							{ this.datos.put("ACCION",valor); 	 						}
 	public void setReserva (String valor)							{ this.datos.put("RESERVA",valor); 	 						}
 	public void setDiasGuardia (String valor)						{ this.datos.put("DIASGUARDIA",valor); 	 					}
 	public void setTipoDias (String valor)							{ this.datos.put("TIPODIAS",valor); 	 					}
 	public void setNumero (String valor)							{ this.datos.put("NUMERO",valor); 	 						}
 	public void setFechaSolicitud (String valor)					{ this.datos.put("FECHASOLICITUD",valor); 	 				}
 	public void setAnulada (String valor)							{ this.datos.put("ANULADA",valor); 	 						}
 	public void setOrden (String valor)								{ this.datos.put("ORDEN",valor); 	 						}
 	public void setIdPersona (String valor)							{ this.datos.put("IDPERSONA",valor); 	 					}
 	public void setIdTurno (String valor)							{ this.datos.put("IDTURNO",valor); 	 						}
 	public void setIdGuardia (String valor)							{ this.datos.put("IDGUARDIA",valor); 	 					} 
 	public void setIdCalendarioGuardias (String valor)				{ this.datos.put("IDCALENDARIOGUARDIAS",valor); 			}
 	public void setFechaInicio (String valor)						{ this.datos.put("FECHAINICIO",valor); 	 					}
 	public void setFechaFin (String valor)							{ this.datos.put("FECHAFIN",valor); 	 					}
 	public void setSustituta (String valor)							{ this.datos.put("SUSTITUTA",valor); 	 					}
 	
 	public void setNombreColegiadoPestanha (String valor)			{ this.datos.put("NOMBRECOLEGIADOPESTAÑA",valor); 	 		}
 	public void setNumeroColegiadoPestanha (String valor)			{ this.datos.put("NUMEROCOLEGIADOPESTAÑA",valor); 	 		}
 	/*public void setFlagSalto (String valor)							{ this.datos.put("FLAGSALTO",valor);							}
 	public void setFlagCompensacion (String valor)					{ this.datos.put("FLAGCOMPENSACION",valor);					}
 	public void setCheckSalto (String valor)								{ this.datos.put("SALTO",valor);							}
 	public void setCheckCompensacion (String valor)						{ this.datos.put("COMPENSACION",valor);}*/
	// Metodos Get para leer de la tabla hash datos los campos del formulario.
 	// Hay 1 metodo get por cada campo del formulario.
 	public String getIdInstitucion ()				{ return ((String)this.datos.get("IDINSTITUCION")); 					}
 	public String getIdCalendarioSolicitante ()		{ return ((String)this.datos.get("IDCALENDARIOGUARDIASSOLICITANTE")); 	}
 	public String getIdTurnoSolicitante ()			{ return ((String)this.datos.get("IDTURNOSOLICITANTE")); 		}
 	public String getIdGuardiaSolicitante ()		{ return ((String)this.datos.get("IDGUARDIASOLICITANTE")); 		}
 	public String getIdPersonaSolicitante ()		{ return ((String)this.datos.get("IDPERSONASOLICITANTE")); 				}
 	public String getFechaInicioSolicitante ()		{ return ((String)this.datos.get("FECHAINICIOSOLICITANTE")); 			}
 	public String getFechaFinSolicitante ()			{ return ((String)this.datos.get("FECHAFINSOLICITANTE")); 				}
 	public String getNombreSolicitante ()			{ return ((String)this.datos.get("NOMBRESOLICITANTE")); 				}
 	public String getNumeroColegiadoSolicitante ()	{ return ((String)this.datos.get("NUMEROCOLEGIADOSOLICITANTE")); 		}
 	public String getMotivosSolicitante ()			{ return ((String)this.datos.get("MOTIVOSSOLICITANTE")); 				}
 	public String getIdCalendarioConfirmador ()		{ return ((String)this.datos.get("IDCALENDARIOGUARDIASCONFIRMADOR")); 	}
 	public String getIdTurnoConfirmador ()			{ return ((String)this.datos.get("IDTURNOCONFIRMADOR")); 				}
 	public String getIdGuardiaConfirmador ()		{ return ((String)this.datos.get("IDGUARDIACONFIRMADOR")); 				}
 	public String getIdPersonaConfirmador ()		{ return ((String)this.datos.get("IDPERSONACONFIRMADOR")); 				}
 	public String getFechaInicioConfirmador ()		{ return ((String)this.datos.get("FECHAINICIOCONFIRMADOR"));			}
 	public String getFechaFinConfirmador ()			{ return ((String)this.datos.get("FECHAFINCONFIRMADOR")); 				}
 	public String getNombreConfirmador ()			{ return ((String)this.datos.get("NOMBRECONFIRMADOR")); 				}
 	public String getNumeroColegiadoConfirmador ()	{ return ((String)this.datos.get("NUMEROCOLEGIADOCONFIRMADOR")); 		}
 	public String getMotivosConfirmador ()			{ return ((String)this.datos.get("MOTIVOSCONFIRMADOR")); 				}
 	public String getAccion ()						{ return ((String)this.datos.get("ACCION")); 							}
 	public String getReserva ()						{ return ((String)this.datos.get("RESERVA")); 							}
 	public String getDiasGuardia ()					{ return ((String)this.datos.get("DIASGUARDIA")); 						}
 	public String getTipoDias ()					{ return ((String)this.datos.get("TIPODIAS")); 							}
 	public String getNumero ()						{ return ((String)this.datos.get("NUMERO")); 							}
 	public String getFechaSolicitud ()				{ return ((String)this.datos.get("FECHASOLICITUD")); 					}
 	public String getAnulada ()						{ return ((String)this.datos.get("ANULADA")); 							}
 	public String getOrden ()						{ return ((String)this.datos.get("ORDEN")); 							}
 	public String getIdPersona ()					{ return ((String)this.datos.get("IDPERSONA")); 						}
 	public String getIdTurno ()						{ return ((String)this.datos.get("IDTURNO")); 							}
 	public String getIdGuardia ()					{ return ((String)this.datos.get("IDGUARDIA")); 						}
 	public String getIdCalendarioGuardias ()		{ return ((String)this.datos.get("IDCALENDARIOGUARDIAS")); 				}
 	public String getFechaInicio ()					{ return ((String)this.datos.get("FECHAINICIO")); 						}
 	public String getFechaFin ()					{ return ((String)this.datos.get("FECHAFIN")); 							}
 	public String getSustituta ()					{ return ((String)this.datos.get("SUSTITUTA")); 							}
 	
 	public String getNombreColegiadoPestanha ()		{ return ((String)this.datos.get("NOMBRECOLEGIADOPESTAÑA"));			} 		
 	public String getNumeroColegiadoPestanha ()		{ return ((String)this.datos.get("NUMEROCOLEGIADOPESTAÑA")); 			}
 	/*public String getFlagSalto ()					{ return ((String)this.datos.get("FLAGASALTO")); 							}
 	public String getFlagCompensacion ()			{ return ((String)this.datos.get("FLAGCOMPENSACION")); 							}
 	public String getCheckSalto ()					{ return ((String)this.datos.get("SALTO")); 							}
 	public String getCheckCompensacion ()			{ return ((String)this.datos.get("COMPENSACION")); 							}*/
 	
}