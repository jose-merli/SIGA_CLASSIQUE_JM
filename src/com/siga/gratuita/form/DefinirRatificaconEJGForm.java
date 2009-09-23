/*
 * Fecha creación: 17/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_EJGFORM
*/
 public class DefinirRatificaconEJGForm extends MasterForm{

	/*
	 * Metodos SET
	 */
	
	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio 					(String anio)					{ this.datos.put(ScsEJGBean.C_ANIO, anio);									}
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
	public void setIdPersona				(String idPersona)				{ this.datos.put(ScsEJGBean.C_FECHADICTAMEN, idPersona);			}
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
	public void setGuardiaTurno_IdTurno		(String guardiaTurno_IdTurno)	{ this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDTURNO, guardiaTurno_IdTurno);}
	/**
	 * Almacena en la Hashtable el identificador de la guardia de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del guardia de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setGuardiaTurno_IdGuardia	(String guardiaTurno_IdGuardia)	{ this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA, guardiaTurno_IdGuardia);}
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
	public void setAsistencia_Anio			(String asistencia_Anio)		{ this.datos.put("asistencia_Anio", asistencia_Anio);			}
	/**
	 * Almacena en la Hashtable el valor asistencia numero del EJG
	 * 
	 * @param valor el valor asistencia numero del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setAsistencia_Numero		(String asistencia_Numero)		{ this.datos.put("asistencia_Numero", asistencia_Numero);		}
	/**
	 * Almacena en la Hashtable el identificador del tipo del SOJ
	 * 
	 * @param valor Identificador del tipo del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJ_IdTipoSOJ			(String SOJ_IdTipoSOJ)			{ this.datos.put("SOJ_IdTipoSOJ", SOJ_IdTipoSOJ);				}
	/**
	 * Almacena en la Hashtable el numero del SOJ
	 * 
	 * @param valor Numero del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJ_Numero				(String SOJ_Numero)				{ this.datos.put("SOJ_Numero", SOJ_Numero);						}
	/**
	 * Almacena en la Hashtable el anho del SOJ
	 * 
	 * @param valor Anho del SOJ. De tipo "String". 
	 * @return void 
	 */
	public void setSOJ_Anio					(String SOJ_Anio)				{ this.datos.put("SOJ_Anio", SOJ_Anio);							}
	
	/*
	 * Metodos GET*/
	
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
	 * Recupera de la Hashtable la fecha limite de presentacion de la EJG
	 * 
	 * @return Fecha limite de presentacion  de la EJG
	 */
	public String getFechaLimitePresentacion()		{ return this.datos.get(ScsEJGBean.C_FECHALIMITEPRESENTACION).toString();}
	/**
	 * Recupera de la Hashtable la fecha  de presentacion de la EJG
	 * 
	 * @return Fecha de presentacion  de la EJG
	 */
	public String getFechaPresentacion		()		{ return this.datos.get(ScsEJGBean.C_FECHAPRESENTACION).toString();}
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
	 * Recupera de la Hashtable el identificador de la persona de la EJG
	 * 
	 * @return Identificador de la persona  de la EJG
	 */
	public String getIdPersona				()		{ return this.datos.get(ScsEJGBean.C_IDPERSONA).toString();}
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
	public String getGuardiaTurno_IdTurno	()		{ return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO).toString();}
	/**
	 * Recupera de la Hashtable el identificador de la guardia  de la EJG
	 * 
	 * @return Identificador de la guardia de la EJG
	 */
	public String getGuardiaTurno_IdGuardia()		{ return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA).toString();}
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
	public String getAsistencia_Anio		()		{ return this.datos.get("asistencia_Anio").toString();}
	/**
	 * Recupera de la Hashtable el numero de asistencias de la EJG
	 * 
	 * @return Numero de asistencias al anho de la EJG
	 */
	public String getAsistencia_Numero		()		{ return this.datos.get("asistencia_Numero").toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo de SOJ
	 * 
	 * @return Identificador del tipo de SOJ
	 */
	public String getSOJ_IdTipoSOJ			()		{ return this.datos.get("SOJ_idTipoSOJ").toString();}
	/**
	 * Recupera de la Hashtable el numero del tipo de SOJ
	 * 
	 * @return Numero del tipo de SOJ
	 */
	public String getSOJ_Numero			()			{ return this.datos.get("SOJ_Numero").toString();}
	/**
	 * Recupera de la Hashtable el anho del tipo de SOJ
	 * 
	 * @return Anho del tipo de SOJ
	 */
	public String getSOJ_Anio				()		{ return this.datos.get("SOJ_Anio").toString();}
	
}