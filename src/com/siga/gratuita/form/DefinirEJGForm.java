/*
 * Fecha creación: 14/02/2005 
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import java.util.List;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;


/**
* Maneja el formulario que mantiene la tabla SCS_EJGFORM
*/
 public class DefinirEJGForm extends MasterForm{
 	
	 	
	/*
	 * Metodos SET
	 */
 	
	 String anioCAJG;
	 String numeroCAJG;
	 
	 //Crear nuevos campos en la pestaña de impugnaciones del EJG
	 String numeroResolucion;
	 String anioResolucion;
	 //String bisResolucion;
	 
	 
 	public void setRefAuto 					(String refAuto)					{ this.datos.put(ScsEJGBean.C_REFAUTO, refAuto);									} 	
 	
	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio 					(String anio)					{ this.datos.put(ScsEJGBean.C_ANIO, anio);									}
	/**
	 * Almacena en la Hashtable el numEJG del EJG 
	 * 
	 * @param valor numEJG de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumEJG 					(String numEJG)					{ this.datos.put(ScsEJGBean.C_NUMEJG, numEJG);                              }	
	/**
	 * Almacena en la Hashtable el numero del EJG 
	 * 
	 * @param valor Numero de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumero 					(String numero)					{ this.datos.put(ScsEJGBean.C_NUMERO, numero);								}
	/**
	 * Almacena en la Hashtable la fecha de apretura del EJG 
	 * 
	 * @param valor Fecha de apertura de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaApertura			(String	 fechaApertura)			{ this.datos.put(ScsEJGBean.C_FECHAAPERTURA, fechaApertura);				}
	/**
	 * Almacena en la Hashtable el origen de apretura del EJG 
	 * 
	 * @param valor Origen de apertura de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setOrigenApertura			(String origenApertura)			{ this.datos.put(ScsEJGBean.C_ORIGENAPERTURA, origenApertura);				}
	/**
	 * Almacena en la Hashtable desde donde se ha creado el EJG 
	 * 
	 * @param valor Origen de apertura de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setCreadoDesde				(String creado)					{ this.datos.put("CREADODESDE", creado);									}
	/**
	 * Almacena en la Hashtable el estadodel EJG 
	 * 
	 * @param valor Estado del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setEstadoEJG				(String estado)					{ this.datos.put("ESTADOEJG", estado);										}
	/**
	 * Almacena en la Hashtable si está dictaminado 
	 * 
	 * @param valor Si está dictaminado. De tipo "String". 
	 * @return void 
	 */
	public void setDictaminado				(String dictaminado)			{ this.datos.put("DICTAMINADO", dictaminado);								}
	/**
	 * Almacena en la Hashtable la fecha limite de presentacion del EJG 
	 * 
	 * @param valor Fecha limite de presentacion de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaLimitePresentacion	(String  fechaLimitePresentacion)	{ this.datos.put(ScsEJGBean.C_FECHALIMITEPRESENTACION, fechaLimitePresentacion);}
	/**
	 * Almacena en la Hashtable la fecha de presentacion del EJG 
	 * 
	 * @param valor Fecha de presentacion de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaPresentacion		(String  fechaPresentacion)			{ this.datos.put(ScsEJGBean.C_FECHAPRESENTACION, fechaPresentacion);	}
	/**
	 * Almacena en la Hashtable si el procurador es necesario del EJG 
	 * 
	 * @param valor Procurador necesario de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setProcuradorNecesario		(String  procuradorNecesario) 		{ this.datos.put(ScsEJGBean.C_PROCURADORNECESARIO, procuradorNecesario);}
	/**
	 * Almacena en la Hashtable la Calidad del EJG 
	 * 
	 * @param valor Calidad de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setCalidad					(String calidad)					{ this.datos.put(ScsEJGBean.C_CALIDAD, calidad);}
	/**
	 * Almacena en la Hashtable el tipo de letrado del EJG 
	 * 
	 * @param valor Tipo de letrado de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setTipoLetrado				(String tipoLetrado)				{ this.datos.put(ScsEJGBean.C_TIPOLETRADO, tipoLetrado);}
	/**
	 * Almacena en la Hashtable las observaciones del EJG 
	 * 
	 * @param valor Observaciones de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones			(String  observaciones)				{ this.datos.put(ScsEJGBean.C_OBSERVACIONES, observaciones);}
	/**
	 * Almacena en la Hashtable los delitos del EJG 
	 * 
	 * @param valor Delitos del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setDelitos					(String  delitos)					{ this.datos.put(ScsEJGBean.C_DELITOS, delitos);}	
	/**
	 * Almacena en la Hashtable el dictamen del EJG 
	 * 
	 * @param valor Dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setDictamen					(String  dictamen)		{ this.datos.put(ScsEJGBean.C_DICTAMEN, dictamen);}
	/**
	 * Almacena en la Hashtable la fecha del dictamen del EJG 
	 * 
	 * @param valor Fecha del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaDictamen			(String  fechaDictamen)	{ this.datos.put(ScsEJGBean.C_FECHADICTAMEN, fechaDictamen);}
	/**
	 * Almacena en la Hashtable el procurador del EJG 
	 * 
	 * @param valor Procurador de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setProcurador				(String  procurador)		{ this.datos.put(ScsEJGBean.C_PROCURADOR, procurador);}
	/**
	 * Almacena en la Hashtable la ratifiacion del dictamen del EJG 
	 * 
	 * @param valor Ratifiacion del dictamen de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setRatificacionDictamen		(String  ratificacionDictamen)	{ this.datos.put(ScsEJGBean.C_RATIFICACIONDICTAMEN, ratificacionDictamen);}
	/**
	 * Almacena en la Hashtable la la fecha de ratifiacion del EJG 
	 * 
	 * @param valor Fecha de ratifiacion de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaRatificacion		(String  fechaRatificacion)		{ this.datos.put(ScsEJGBean.C_FECHARATIFICACION, fechaRatificacion);}
	/**
	 * Almacena en la Hashtable el identificador del tipo de ratificación del EJG 
	 * 
	 * @param valor Identificador del tipo de ratificacion del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoRatificacionEJG		(String idTipoRatificacionEJG)		{ this.datos.put(ScsEJGBean.C_IDTIPORATIFICACIONEJG, idTipoRatificacionEJG);}
	
	/**
	 * Almacena en la Hashtable el identificador del ponente del EJG 
	 * 
	 * @param valor Identificador del ponente del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdPonente		(String idPonente)		{ this.datos.put(ScsEJGBean.C_IDPONENTE, idPonente);}

	
	/**
	 * Almacena en la Hashtable el identificador del tipo del dictamen del EJG 
	 * 
	 * @param valor Identificador del tipo del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoDictamenEJG		(String idTipoDictamenEJG)		{ this.datos.put(ScsEJGBean.C_IDTIPODICTAMENEJG, idTipoDictamenEJG);}
	
	/**
	 * Almacena en la Hashtable el identificador de la persona del dictamen del EJG 
	 * 
	 * @param valor Identificador de la persona del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdPersona				(String idPersona)				{ this.datos.put(ScsEJGBean.C_IDPERSONA, idPersona);			}
	/**
	 * Almacena en la Hashtable el identificador de la institucion del dictamen del EJG 
	 * 
	 * @param valor Identificador de la institucion del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdInstitucion			(String idInstitucion)			{ this.datos.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);		}
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del tipo de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJG				(String idTipoEJG)				{ this.datos.put(ScsEJGBean.C_IDTIPOEJG, idTipoEJG);				}
	/**
	 * Almacena en la Hashtable el identificador del turno de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del turno de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setGuardiaTurnoIdTurno		(String guardiaTurno_IdTurno)	{ this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDTURNO, guardiaTurno_IdTurno);}
	/**
	 * Almacena en la Hashtable el identificador de la guardia de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del guardia de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setGuardiaTurnoIdGuardia	(String guardiaTurno_IdGuardia)	{ this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA, guardiaTurno_IdGuardia);}
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del colegio del EJG 
	 * 
	 * @param valor Identificador del tipo de EJG del colegio del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJGColegio			(String idTipoEJGColegio)		{ this.datos.put(ScsEJGBean.C_IDTIPOEJGCOLEGIO, idTipoEJGColegio);			}
	/**
	 * Almacena en la Hashtable el identificador de la persona de JG 
	 * 
	 * @param valor Identificador de la persona de JG. De tipo "String". 
	 * @return void 
	 */
	public void setIdPersonaJG				(String idPersonaJG)			{ this.datos.put(ScsEJGBean.C_IDPERSONAJG, idPersonaJG);					}
	/**
	 * Almacena en la Hashtable el valor asistencia anho del EJG
	 * 
	 * @param valor el valor asistencia anho del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setAsistenciaAnio			(String asistencia_Anio)		{ this.datos.put(ScsAsistenciasBean.C_EJGANIO, asistencia_Anio);			}
	/**
	 * Almacena en la Hashtable el valor asistencia numero del EJG
	 * 
	 * @param valor el valor asistencia numero del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setAsistenciaNumero		(String asistencia_Numero)		{ this.datos.put(ScsAsistenciasBean.C_EJGNUMERO, asistencia_Numero);		}
	/**
	 * Almacena en la Hashtable el identificador del tipo del SOJ
	 * 
	 * @param valor Identificador del tipo del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJIdTipoSOJ			(String SOJ_IdTipoSOJ)			{ this.datos.put("SOJ_IdTipoSOJ", SOJ_IdTipoSOJ);				}
	/**
	 * Almacena en la Hashtable el numero del SOJ
	 * 
	 * @param valor Numero del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJNumero				(String SOJ_Numero)				{ this.datos.put("SOJ_Numero", SOJ_Numero);						}
	/**
	 * Almacena en la Hashtable el anho del SOJ
	 * 
	 * @param valor Anho del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJAnio					(String SOJ_Anio)				{ this.datos.put("SOJ_Anio", SOJ_Anio);							}
	/**
	 * Almacena en la Hashtable el anho de la designa
	 * 
	 * @param valor Anho del SOJ. De tipo "String". 
	 * @return void 
	 */
	
	public void setDesignaAnio				(String Designa_Anio)			{ this.datos.put(ScsEJGDESIGNABean.C_ANIODESIGNA, Designa_Anio);					}	
	/**
	 * Almacena en la Hashtable el numero de la designa
	 * 
	 * @param valor Anho del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setDesignaNumero			(String Designa_Numero)			{ this.datos.put(ScsEJGDESIGNABean.C_NUMERODESIGNA, Designa_Numero);				}
	/**
	 * Almacena en la Hashtable el idTurno de la designa
	 * 
	 * @param valor Anho del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setDesignaIdTurno			(String Designa_IdTurno)		{ this.datos.put(ScsEJGDESIGNABean.C_IDTURNO, Designa_IdTurno);				}
	
	/**
	 * Almacena en la Hashtable el nombre de la persona 
	 * 
	 * @param Nombre solicitante de un SOJ 
	 * @return void 
	 */
	public void setNombre		 			(String nombre)				{ this.datos.put(ScsPersonaJGBean.C_NOMBRE, nombre);							}
	
	/**
	 * Almacena en la Hashtable el apellido de la persona 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido1	 			(String apellido)			{ this.datos.put(ScsPersonaJGBean.C_APELLIDO1, apellido);						}
	
	/**
	 * Almacena en la Hashtable el apellido de la persona 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido2	 			(String apellido)			{ this.datos.put(ScsPersonaJGBean.C_APELLIDO2, apellido);						}
	
	/**
	 * Almacena en la Hashtable el NIF de la persona 
	 * 
	 * @param Nif solicitante
	 * @return void 
	 */
	public void setNif						(String nif)				{ this.datos.put(ScsPersonaJGBean.C_NIF, nif);								}
	
	/**
	 * Almacena en la Hashtable el número de colegiado 
	 * 
	 * @param Número de colegiado
	 * @return void 
	 */
	public void setNColegiado				(String ncolegiado)			{ this.datos.put("NCOLEGIADO", ncolegiado);									}

	/**
	 * Almacena en la Hashtable la descrición del estado 
	 * 
	 * @param Descripción del estado
	 * @return void 
	 */
	public void setDescripcionEstado		(String descripcion)			{ this.datos.put("DESCRIPCIONESTADO", descripcion);						}
	
	
	/*
	 * Metodos GET*/
	
	public String getRefAuto 					()		{ return this.datos.get(ScsEJGBean.C_REFAUTO).toString();}
	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio 					()		{ return this.datos.get(ScsEJGBean.C_ANIO).toString();}
	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * 
	 * @return Numero de la EJG
	 */
	public String getNumero 				()		{ return this.datos.get(ScsEJGBean.C_NUMERO).toString();}
	/**
	 * Recupera de la Hashtable el numEJG de la EJG
	 * 
	 * @return numEJG de la EJG
	 */
	public String getNumEJG 				()		{ return this.datos.get(ScsEJGBean.C_NUMEJG).toString();}
	/**
	 * Recupera de la Hashtable la fecha de apertura de la EJG
	 * 
	 * @return Fecha de apertura  de la EJG
	 */
	public String getFechaApertura			()		{ return this.datos.get(ScsEJGBean.C_FECHAAPERTURA).toString();}
	/**
	 * Recupera de la Hashtable la origen de apertura de la EJG
	 * 
	 * @return Origen de apertura  de la EJG
	 */
	public String getOrigenApertura		()			{ return this.datos.get(ScsEJGBean.C_ORIGENAPERTURA).toString();}
	/**
	 * Almacena en la Hashtable el estadodel EJG 
	 * @return String 
	 */
	public String getEstadoEJG			()			{ return this.datos.get("ESTADOEJG").toString();				}	
	/**
	 * Recupera de la Hashtable la fecha limite de presentacion de la EJG
	 * 
	 * @return Fecha limite de presentacion  de la EJG
	 */
	public String getFechaLimitePresentacion()		{ return this.datos.get(ScsEJGBean.C_FECHALIMITEPRESENTACION).toString();}
	
	/**
	 * Recupera de la Hashtable si el procurador es necesario de la EJG
	 * 
	 * @return Procurador necesario  de la EJG
	 */
	public String getProcuradorNecesario	()		{ return this.datos.get(ScsEJGBean.C_PROCURADORNECESARIO).toString();}
	/**
	 * Recupera de la Hashtable la calidad de la EJG
	 * 
	 * @return Calidad  de la EJG
	 */
	public String getCalidad				()		{ return this.datos.get(ScsEJGBean.C_CALIDAD).toString();}
	/**
	 * Recupera de la Hashtable el tipo de letrado de la EJG
	 * 
	 * @return Tipo de letrado de la EJG
	 */
	public String getTipoLetrado			()		{ return this.datos.get(ScsEJGBean.C_TIPOLETRADO).toString();}
	/**
	 * Recupera de la Hashtable observaciones de la EJG
	 * 
	 * @return Observaciones de la EJG
	 */
	public String getObservaciones			()		{ return this.datos.get(ScsEJGBean.C_OBSERVACIONES).toString();}
	/**.
	 * Recupera de la Hashtable los delitos de la EJG
	 * 
	 * @return Delitos de la EJG
	 */
	public String getDelitos				()		{ return this.datos.get(ScsEJGBean.C_DELITOS).toString();}	
	/**
	 * Almacena en la Hashtable el dictamen del EJG 
	 * 
	 * @param valor Dictamen del EJG. De tipo "String". 
	 * @return String 
	 */
	public String getDictamen				()		{ return this.datos.get(ScsEJGBean.C_DICTAMEN).toString();}
	/**
	 * Recupera de la Hashtable el dictamen de la EJG
	 * 
	 * @return Dictamen de la EJG
	 */
	public String getFechaDictamen			()		{ return this.datos.get(ScsEJGBean.C_FECHADICTAMEN).toString();}
	
	/**
	 * Recupera de la Hashtable el dictamen de la EJG
	 * 
	 * @return Dictamen de la EJG
	 */
	public String getFechaPresentacion			()	{ return this.datos.get(ScsEJGBean.C_FECHAPRESENTACION).toString();}
	
	/**
	 * Recupera de la Hashtable el procurador de la EJG
	 * 
	 * @return Procurador de la EJG
	 */
	public String getProcurador				()		{ return this.datos.get(ScsEJGBean.C_PROCURADOR).toString();}
	/**
	 * Recupera de la Hashtable la ratifiacion del dictamen de la EJG
	 * 
	 * @return Ratificacion del dictamen de la EJG
	 */
	public String getRatificacionDictamen	()		{ return this.datos.get(ScsEJGBean.C_RATIFICACIONDICTAMEN).toString();}
	/**
	 * Recupera de la Hashtable la fecha de ratificacion del dictamen de la EJG
	 * 
	 * @return FEcha ratificacion del dictamen de la EJG
	 */
	public String getFechaRatificacion		()		{ return this.datos.get(ScsEJGBean.C_FECHARATIFICACION).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo del dictamen de la EJG
	 * 
	 * @return Observaciones de la EJG
	 */
	public String getIdTipoDictamenEJG		()		{ return this.datos.get(ScsEJGBean.C_IDTIPODICTAMENEJG).toString();}
	
	/**
	 * Recupera de la Hashtable el identificador del tipo del dictamen de la EJG
	 * 
	 * @return Observaciones de la EJG
	 */
	public String getIdTipoRatificacionEJG		()		{ return this.datos.get(ScsEJGBean.C_IDTIPORATIFICACIONEJG).toString();}

	/**
	 * Recupera de la Hashtable el identificador del ponente de la EJG
	 * 
	 * @return Ponente de la EJG
	 */
	public String getIdPonente				()		{ return this.datos.get(ScsEJGBean.C_IDPONENTE).toString();}
	
	/**
	 * Recupera de la Hashtable el identificador de la persona de la EJG
	 * 
	 * @return Identificador de la persona  de la EJG
	 */
	public String getIdPersona				()		{ return this.datos.get(ScsEJGBean.C_IDPERSONA).toString();}
	
	/**
	 * Recupera de la Hashtable si está dictaminado
	 * @return String 
	 */
	public String getDictaminado			()		{ return this.datos.get("DICTAMINADO").toString();			}
	/**
	 * Recupera de la Hashtable el identificador de la institucion de la EJG
	 * 
	 * @return Identificador de la institucion  de la EJG
	 */
	public String getIdInstitucion			()		{ return this.datos.get(ScsEJGBean.C_IDINSTITUCION).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG
	 * 
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG				()		{ return this.datos.get(ScsEJGBean.C_IDTIPOEJG).toString();}
	/**
	 * Recupera de la Hashtable el identificador del turno  de la EJG
	 * 
	 * @return Identificador del turno de la EJG
	 */
	public String getGuardiaTurnoIdTurno	()		{ return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO).toString();}
	/**
	 * Recupera de la Hashtable el identificador de la guardia  de la EJG
	 * 
	 * @return Identificador de la guardia de la EJG
	 */
	public String getGuardiaTurnoIdGuardia()		{ return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG del colegio
	 * 
	 * @return Identificador del tipo de la EJG del colegio
	 */
	public String getIdTipoEJGColegio		()		{ return this.datos.get(ScsEJGBean.C_IDTIPOEJGCOLEGIO).toString();}
	/**
	 * Recupera de la Hashtable el identificador de la persona de la EJG
	 * 
	 * @return Identificador de la persona de la EJG
	 */
	public String getIdPersonaJG			()		{ return this.datos.get(ScsEJGBean.C_IDPERSONAJG).toString();}
	/**
	 * Recupera de la Hashtable la asistencias al anho de la EJG
	 * 
	 * @return Asistencias al anho de la EJG
	 */
	public String getAsistenciaAnio		()		{ return UtilidadesHash.getString(this.datos, ScsAsistenciasBean.C_EJGANIO);}
	/**
	 * Recupera de la Hashtable e	 * l numero de asistencias de la EJG
	 * 
	 * @return Numero de asistencias al anho de la EJG
	 */
	public String getAsistenciaNumero		()		{ return this.datos.get(ScsAsistenciasBean.C_EJGNUMERO).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo de SOJ
	 * 
	 * @return Identificador del tipo de SOJ
	 */
	public String getSOJIdTipoSOJ			()		{ return UtilidadesHash.getString(this.datos,"SOJ_IdTipoSOJ");}
	/**
	 * Recupera de la Hashtable el numero del tipo de SOJ
	 * 
	 * @return Numero del tipo de SOJ
	 */
	public String getSOJNumero				()		{ return UtilidadesHash.getString(this.datos,"SOJ_Numero");}
	/**
	 * Recupera de la Hashtable el anho del tipo de SOJ
	 * 
	 * @return Anho del tipo de SOJ
	 */
	public String getSOJAnio				()		{ return UtilidadesHash.getString(this.datos,"SOJ_Anio");}
	
	/**
	 * Recupera de la Hashtable el anho de la designa
	 * @return String 
	 */
	public String getDesignaAnio			()		{ return UtilidadesHash.getString(this.datos, ScsEJGDESIGNABean.C_ANIODESIGNA);}	
	/**
	 * Recupera de la Hashtable el numero de la designa 
	 * @return String 
	 */
	public String getDesignaNumero			()		{ return UtilidadesHash.getString(this.datos, ScsEJGDESIGNABean.C_NUMERODESIGNA);}
	/**
	 * Recupera de la Hashtable el idTurno de la designa 
	 * @return String 
	 */
	public String getDesignaIdTurno			()			{ return UtilidadesHash.getString(this.datos, ScsEJGDESIGNABean.C_IDTURNO).toString();}
	/**
	 * Recupera de la Hashtable el nombre del solicitante de un SOJ
	 * @return String 
	 */
	public String getNombre		 			()						{ return this.datos.get(ScsPersonaJGBean.C_NOMBRE).toString();						}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ
	 * @return String 
	 */
	public String getApellido1	 			()						{ return this.datos.get(ScsPersonaJGBean.C_APELLIDO1).toString();					}
	
	/**
	 * Recupera de la Hashtable el apellido del solicitante de un SOJ 
	 * @return String 
	 */
	public String getApellido2	 			()						{ return this.datos.get(ScsPersonaJGBean.C_APELLIDO2).toString();					}
	
	/**
	 * Recupera de la Hashtable el NIF de la persona
	 * @return String
	 */
	public String getNif					()						{ return this.datos.get(ScsPersonaJGBean.C_NIF).toString();							}
	/**
	 * Recupera de la Hashtable desde donde se ha creado el EJG 
	 * @return String
	 */
	public String getCreadoDesde			()						{ return this.datos.get("CREADODESDE").toString();									}
	
	/**
	 * Recupera de la Hashtable el número de colegiado
	 * @return String 
	 */
	public String getNColegiado				()						{ return this.datos.get("NCOLEGIADO").toString();									}
	
	/**
	 * Recupera de la Hashtable el número de colegiado
	 * @return String
	 */
	public String getDescripcionEstado 		()						{ return this.datos.get("NCOLEGIADO").toString();									}
	
	
	private String cabeceraCarta="";
	private String motivoCarta="";
	private String pieCarta="";
	private String desdeDesigna="";
	
	
	public void setCabeceraCarta(String id){
		this.cabeceraCarta=id;
	}
	
	public void setMotivoCarta(String id){
		this.motivoCarta=id;
	}
	
	public void setPieCarta(String id){
		this.pieCarta=id;		
	}
	
	public String getCabeceraCarta(){
		return this.cabeceraCarta;
	}
	
	public String getMotivoCarta(){
		return this.motivoCarta;
	}
	
	public String getPieCarta(){
		return this.pieCarta;		
	}
	
	/**
	 * @return Returns the desdeDesigna.
	 */
	public String getDesdeDesigna() {
		return desdeDesigna;
	}
	/**
	 * @param desdeDesigna The desdeDesigna to set.
	 */
	public void setDesdeDesigna(String desdeDesigna) {
		this.desdeDesigna = desdeDesigna;
	}

	public String getFechaAuto() 						{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_FECHAAUTO);				}
	public void setFechaAuto(String dato)				{this.datos.put(ScsEJGBean.C_FECHAAUTO, dato);										}

	public String getFechaNotificacion() 				{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_FECHANOTIFICACION);		}
	public void setFechaNotificacion(String dato)		{this.datos.put(ScsEJGBean.C_FECHANOTIFICACION, dato);								}
	
	public String getFechaResolucionCAJG() 				{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_FECHARESOLUCIONCAJG);		}
	public void setFechaResolucionCAJG(String dato)		{this.datos.put(ScsEJGBean.C_FECHARESOLUCIONCAJG, dato);							}
	
	public String getIdFundamentoJuridico() 			{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_IDFUNDAMENTOJURIDICO);	}
	public void setIdFundamentoJuridico(String dato)	{this.datos.put(ScsEJGBean.C_IDFUNDAMENTOJURIDICO, dato);							}
	
	
	public String getIdTipoResolAuto() 				{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_IDTIPORESOLAUTO);		}
	public void setIdTipoResolAuto(String dato)		{this.datos.put(ScsEJGBean.C_IDTIPORESOLAUTO, dato);								}
	
	public String getIdRenuncia() {
		return idRenuncia;
	}
	public void setIdRenuncia(String idRenuncia) {
		this.idRenuncia = idRenuncia;
	}

	public String getIdTipoSentidoAuto() 				{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_IDTIPOSENTIDOAUTO);		}
	public void setIdTipoSentidoAuto(String dato)		{this.datos.put(ScsEJGBean.C_IDTIPOSENTIDOAUTO, dato);								}
	
	
	public boolean getTurnadoAuto() 					{return UtilidadesHash.getBoolean(this.datos, ScsEJGBean.C_TURNADOAUTO).booleanValue();					}
	public void setTurnadoAuto(boolean dato)			{this.datos.put(ScsEJGBean.C_TURNADOAUTO, (dato?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE));			}
	
	public boolean getTurnadoRatificacion() 			{return UtilidadesHash.getBoolean(this.datos, ScsEJGBean.C_TURNADORATIFICACION).booleanValue(); 	 	}
	public void setTurnadoRatificacion(boolean dato)	{this.datos.put(ScsEJGBean.C_TURNADORATIFICACION, (dato?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE));	}
	
	String rutaFicheroDownload, ficheroDownload, borrarFicheroDownload, idTipoTurno, idTurno, idGuardia;
	String fechaAperturaDesde, fechaAperturaHasta, fechaLimitePresentacionDesde, fechaLimitePresentacionHasta, fechaEstadoDesde, fechaEstadoHasta, fechaDictamenDesde, fechaDictamenHasta;
	String idRenuncia="";
	List<ScsTurnoBean> turnos;
	List<ScsGuardiasTurnoBean> guardias;
	

    public String getBorrarFicheroDownload() {
        return borrarFicheroDownload;
    }
    public void setBorrarFicheroDownload(String borrarFicheroDownload) {
        this.borrarFicheroDownload = borrarFicheroDownload;
    }
    public String getFicheroDownload() {
        return ficheroDownload;
    }
    public void setFicheroDownload(String ficheroDownload) {
        this.ficheroDownload = ficheroDownload;
    }
    public String getRutaFicheroDownload() {
        return rutaFicheroDownload;
    }
    public void setRutaFicheroDownload(String rutaFicheroDownload) {
        this.rutaFicheroDownload = rutaFicheroDownload;
    }
    public String getFechaAperturaDesde() {
		return fechaAperturaDesde;
	}
	public void setFechaAperturaDesde(String fechaAperturaDesde) {
		this.fechaAperturaDesde = fechaAperturaDesde;
	}
	public String getFechaAperturaHasta() {
		return fechaAperturaHasta;
	}
	public void setFechaAperturaHasta(String fechaAperturaHasta) {
		this.fechaAperturaHasta = fechaAperturaHasta;
	}
	
	public String getFechaLimitePresentacionDesde() {
		return fechaLimitePresentacionDesde;
	}
	public void setFechaLimitePresentacionDesde(
			String fechaLimitePresentacionDesde) {
		this.fechaLimitePresentacionDesde = fechaLimitePresentacionDesde;
	}
	public String getFechaLimitePresentacionHasta() {
		return fechaLimitePresentacionHasta;
	}
	public void setFechaLimitePresentacionHasta(
			String fechaLimitePresentacionHasta) {
		this.fechaLimitePresentacionHasta = fechaLimitePresentacionHasta;
	}
	public String getfechaEstadoDesde() {
		return fechaEstadoDesde;
	}
	public void setfechaEstadoDesde(String fechaEstadoDesde) {
		this.fechaEstadoDesde = fechaEstadoDesde;
	}
	public String getfechaEstadoHasta() {
		return fechaEstadoHasta;
	}
	public void setfechaEstadoHasta(String fechaEstadoHasta) {
		this.fechaEstadoHasta = fechaEstadoHasta;
	}
	public String getfechaDictamenDesde() {
		return fechaDictamenDesde;
	}
	public void setfechaDictamenDesde(String fechaDictamenDesde) {
		this.fechaDictamenDesde = fechaDictamenDesde;
	}
	public String getfechaDictamenHasta() {
		return fechaDictamenHasta;
	}
	public void setfechaDictamenHasta(String fechaDictamenHasta) {
		this.fechaDictamenHasta = fechaDictamenHasta;
	}
	
	public String getIdTipoTurno() {
		return idTipoTurno;
	}

	public void setIdTipoTurno(String idTipoTurno) {
		this.idTipoTurno = idTipoTurno;
	}
	
	public List<ScsTurnoBean> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<ScsTurnoBean> turnos) {
		this.turnos = turnos;
	}

	public String getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	
	public String getIdGuardia() {
		return idGuardia;
	}

	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}

	public List<ScsGuardiasTurnoBean> getGuardias() {
		return guardias;
	}

	public void setGuardias(List<ScsGuardiasTurnoBean> guardias) {
		this.guardias = guardias;
	}

	public String  getChkDocumentacion(){
 		return UtilidadesHash.getString(this.datos, "ChkDocumentacion");
	 }
	 public void setChkDocumentacion(String valor){
	 	UtilidadesHash.set(this.datos, "ChkDocumentacion", valor);
	 }
		/**
		 * Almacena en la Hashtable el numero de diligencia de la designa que se quiere buscar 
		 * 
		 * @param diligencia  
		 * @return void 
		 */
		public void setDiligencia	(String diligencia)	{
			this.datos.put("DILIGENCIA", diligencia);
		}
		/**
		 * Almacena en la Hashtable el numero de procedimiento de la designa que se quiere buscar 
		 * 
		 * @param procedimiento  
		 * @return void 
		 */
		public void setProcedimiento	(String procedimiento)	{
			this.datos.put("PROCEDIMIENTO", procedimiento);
		}
		/**
		 * Almacena en la Hashtable el asunto de la defensa
		 * 
		 * @param asunto  
		 * @return void 
		 */
		public void setAsunto	(String asunto)	{
			this.datos.put("ASUNTO", asunto);
		}
		/**
		 * Almacena en la Hashtable el numero de comisaria de la designa que se quiere buscar 
		 * 
		 * @param comisaria  
		 * @return void 
		 */
		public void setComisaria	(String comisaria)	{
			this.datos.put("COMISARIA", comisaria);
		}
	 
	 /**
		 * Recupera en la Hashtable el numero de diligencia de la designa que se quiere buscar 
		 * 
		 * @param diligencia  
		 * @return void 
		 */
		public String getDiligencia	()	{
			return (String)this.datos.get("DILIGENCIA");
		}
		/**
		 * Recupera en la Hashtable el numero de procedimiento de la designa que se quiere buscar 
		 * 
		 * @param procedimiento  
		 * @return void 
		 */
		public String getProcedimiento	()	{
			return (String)this.datos.get("PROCEDIMIENTO");
		}
		/**
		 * Recupera en la Hashtable el numero de comisaria de la designa que se quiere buscar 
		 * 
		 * @param comisaria  
		 * @return void 
		 */
		public String getComisaria	()	{
			return (String)this.datos.get("COMISARIA");
		}	
		public String getAsunto	()	{
			return (String)this.datos.get("ASUNTO");
		}	
		public void setJuzgado(String a)	{
			UtilidadesHash.set(datos, "JUZGADO", a);
		}
		public String getJuzgado()	{
			return UtilidadesHash.getString(datos,"JUZGADO");
		}
		
		public String getIdDictamen(){return UtilidadesHash.getString(datos,"IDDICTAMEN");}
		
		public void setIdDictamen(String d){UtilidadesHash.set(datos, "IDDICTAMEN", d);}
		//Genero esto para en volantes expres sane
		String origen;


		public String getOrigen() {
			return origen;
		}
		public void setOrigen(String origen) {
			this.origen = origen;
		}
		
		public String getIdOrigenCAJG() {
			//return origen;
			return UtilidadesHash.getString(datos, ScsEJGBean.C_IDORIGENCAJG);
		}
		public void setIdOrigenCAJG(String origen) {
			//this.origen = origen;
			UtilidadesHash.set(this.datos, ScsEJGBean.C_IDORIGENCAJG, origen);
		}
	
		private String urlDocumentacionDS;


		/**
		 * @return the urlDocumentacionDS
		 */
		public String getUrlDocumentacionDS() {
			return urlDocumentacionDS;
		}
		/**
		 * @param urlDocumentacionDS the urlDocumentacionDS to set
		 */
		public void setUrlDocumentacionDS(String urlDocumentacionDS) {
			this.urlDocumentacionDS = urlDocumentacionDS;
		}
		public String getNumeroCAJG() {
			return UtilidadesHash.getString(datos, ScsEJGBean.C_NUMERO_CAJG);
		}
		public void setNumeroCAJG(String numeroCAJG) {
			UtilidadesHash.set(this.datos, ScsEJGBean.C_NUMERO_CAJG, numeroCAJG);
		}
		public String getAnioCAJG() {
			return UtilidadesHash.getString(datos, ScsEJGBean.C_ANIO_CAJG);
		}
		public void setAnioCAJG(String anioCAJG) {
			UtilidadesHash.set(this.datos, ScsEJGBean.C_ANIO_CAJG, anioCAJG);
		}
		
		public String getDocResolucion() {
			return UtilidadesHash.getString(datos, ScsEJGBean.C_DOCRESOLUCION);
		}
		
		public void setDocResolucion(String docResolucion) {
			UtilidadesHash.set(this.datos, ScsEJGBean.C_DOCRESOLUCION, docResolucion);
		}
		
		//Crear nuevos campos en la pestaña de impugnaciones del EJG
		
		public String getObservacionImpugnacion() 	{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_OBSERVACIONIMPUGNACION);}
		public void setObservacionImpugnacion(String dato)	{this.datos.put(ScsEJGBean.C_OBSERVACIONIMPUGNACION, dato);}
		
		public String getFechaPublicacion() 	{return UtilidadesHash.getString(this.datos, ScsEJGBean.C_FECHAPUBLICACION);}
		public void setFechaPublicacion(String dato)	{this.datos.put(ScsEJGBean.C_FECHAPUBLICACION, dato);}
						
		public String getNumeroResolucion() {return UtilidadesHash.getString(datos, ScsEJGBean.C_NUMERORESOLUCION);}
		public void setNumeroResolucion(String numeroResolucion){UtilidadesHash.set(this.datos, ScsEJGBean.C_NUMERORESOLUCION, numeroResolucion);}
		
		public String getAnioResolucion() {return UtilidadesHash.getString(datos, ScsEJGBean.C_ANIORESOLUCION);}
		public void setAnioResolucion(String anioResolucion){UtilidadesHash.set(this.datos, ScsEJGBean.C_ANIORESOLUCION, anioResolucion);}
								
		public boolean getBisResolucion() 	{return UtilidadesHash.getBoolean(this.datos, ScsEJGBean.C_BISRESOLUCION).booleanValue();}
		public void setBisResolucion(boolean bisResolucion)	{this.datos.put(ScsEJGBean.C_BISRESOLUCION, (bisResolucion?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE));}						
}