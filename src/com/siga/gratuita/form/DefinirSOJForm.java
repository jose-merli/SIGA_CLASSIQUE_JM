/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_SOJ
*/
 public class DefinirSOJForm extends MasterForm{	
	
	// Metodos Set (Formulario (*.jsp))

 	
 	/**
	 * Almacena en la Hashtable el identificador de un SOJ 
	 * 
	 * @param Identificador SOJ 
	 * @return void 
	 */
	public void setIdTipoSOJ				(String idTipoSOJ)				{ this.datos.put(ScsSOJBean.C_IDTIPOSOJ, idTipoSOJ);						}
	
	/**
	 * Almacena en la Hashtable el numSOJ de un SOJ de un colegio 
	 * 
	 * @param numSOJ SOJ de un colegio
	 * @return void 
	 */
	public void setNumSOJ			(String numSOJ)			{ this.datos.put(ScsSOJBean.C_NUMSOJ, numSOJ);								}	
	/**
	 * Almacena en la Hashtable el tipo de un SOJ de un colegio 
	 * 
	 * @param Tipo SOJ de un colegio
	 * @return void 
	 */
	public void setIdTipoSOJColegio			(String TipoSOJColegio)			{ this.datos.put(ScsSOJBean.C_IDTIPOSOJCOLEGIO, TipoSOJColegio);								}
	
	/**
	 * Almacena en la Hashtable el anho de un SOJ 
	 * 
	 * @param Anho del SOJ 
	 * @return void 
	 */
	public void setAnio						(String anio)					{ this.datos.put(ScsSOJBean.C_ANIO, anio);									}
	
	/**
	 * Almacena en la Hashtable el número de un SOJ 
	 * 
	 * @param Número del SOJ 
	 * @return void 
	 */
	public void setNumero					(String idNumero)				{ this.datos.put(ScsSOJBean.C_NUMERO, idNumero);							}
	
	/**
	 * Almacena en la Hashtable la fecha de apertura de un SOJ 
	 * 
	 * @param Fecha apertura SOJ 
	 * @return void 
	 */
	public void setFechaAperturaSOJDesde	(String fechaAperturaSOJDesde)	{ this.datos.put("FECHAAPERTURADESDE", fechaAperturaSOJDesde);				}

	/**
	 * Almacena en la Hashtable la fecha de apertura de un SOJ 
	 * 
	 * @param Fecha apertura SOJ 
	 * @return void 
	 */
	public void setFechaAperturaSOJHasta	(String fechaAperturaSOJHasta)	{ this.datos.put("FECHAAPERTURAHASTA", fechaAperturaSOJHasta);				}
	
	/**
	 * Almacena en la Hashtable la fecha de apertura de un SOJ 
	 * 
	 * @param Fecha apertura SOJ 
	 * @return void 
	 */
	public void setFechaAperturaSOJ			(String fechaAperturaSOJ)	{ this.datos.put(ScsSOJBean.C_FECHAAPERTURA, fechaAperturaSOJ);					}
	
	/**
	 * Almacena en la Hashtable el estado de un SOJ 
	 * 
	 * @param Estado SOJ 
	 * @return void 
	 */
	public void setEstadoSOJ				(String estado)		 			{ this.datos.put(ScsSOJBean.C_ESTADO, estado);								}
	
	/**
	 * Almacena en la Hashtable la descripción de la consulta de un SOJ 
	 * 
	 * @param Consulta SOJ 
	 * @return void 
	 */
	public void setDescripcionConsulta		(String consulta)			{ this.datos.put(ScsSOJBean.C_DESCRIPCIONCONSULTA, consulta);					} 
	
	/**
	 * Almacena en la Hashtable la respuesta a una consulta de un SOJ 
	 * 
	 * @param Respuesta consulta SOJ 
	 * @return void 
	 */
	public void setRespuestaLetrado			(String respuestaLetrado)	{ this.datos.put(ScsSOJBean.C_RESPUESTALETRADO, respuestaLetrado);				}
	
	/**
	 * Almacena en la Hashtable el identificador abogado de un SOJ 
	 * 
	 * @param Identificador solicitante SOJ 
	 * @return void 
	 */
	public void setIdPersona				(String idPersona)			{ this.datos.put(ScsSOJBean.C_IDPERSONA, idPersona);							}
	
	/**
	 * Almacena en la Hashtable la institucion de un SOJ 
	 * 
	 * @param Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion	 		(String idInstitucion)		{ this.datos.put(ScsSOJBean.C_IDINSTITUCION, idInstitucion);					}
	
	/**
	 * Almacena en la Hashtable el identificador de un turno de un SOJ º
	 * 
	 * @param Identificador turno SOJ 
	 * @return void 
	 */
	public void setIdTurno	 				(String idTurno)			{ this.datos.put(ScsSOJBean.C_IDTURNO, idTurno);							}
	
	/**
	 * Almacena en la Hashtable el identificador de la actuación de un SOJ 
	 * 
	 * @param Identificador actuación SOJ 
	 * @return void 
	 */
	public void setIdFActuracion	 			(String idFActuracion)		{ this.datos.put(ScsSOJBean.C_IDFACTURACION, idFActuracion);				}
		
	/**
	 * Almacena en la Hashtable la guardia de un SOJ 
	 * 
	 * @param Guardia de un SOJ 
	 * @return void 
	 */
	public void setIdGuardia	 			(String idGuardia)			{ this.datos.put(ScsSOJBean.C_IDGUARDIA, idGuardia);						}
	
	/**
	 * Almacena en la Hashtable el identificador del solicitante de un SOJ 
	 * 
	 * @param Solicitante de un SOJ 
	 * @return void 
	 */
	public void setIdPersonaJG	 			(String idPersonaJG)		{ this.datos.put(ScsSOJBean.C_IDPERSONAJG, idPersonaJG);					}
	
	/**
	 * Almacena en la Hashtable el nombre del solicitante de un SOJ 
	 * 
	 * @param Nombre solicitante de un SOJ 
	 * @return void 
	 */
	public void setNombre		 			(String nombre)			{ this.datos.put(ScsPersonaJGBean.C_NOMBRE, nombre);							}
	
	/**
	 * Almacena en la Hashtable el apellido del solicitante de un SOJ 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido1	 			(String apellido)		{ this.datos.put(ScsPersonaJGBean.C_APELLIDO1, apellido);						}
	
	/**
	 * Almacena en la Hashtable el apellido del solicitante de un SOJ 
	 * 
	 * @param Apellido solicitante de un SOJ 
	 * @return void 
	 */
	public void setApellido2	 			(String apellido)		{ this.datos.put(ScsPersonaJGBean.C_APELLIDO2, apellido);						}
	
	/**
	 * Almacena en la Hashtable el NIF de un solicitante 
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
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	/**
	 * Recupera de la Hashtable el identificador de un SOJ 
	 * @return String
	 */
	public String  getIdTipoSOJ				()							{ return this.datos.get(ScsSOJBean.C_IDTIPOSOJ).toString();						}

	/**
	 * Almacena en la Hashtable el numSOJ de un SOJ de un colegio. 
	 * @return String 
	 */
	public String getNumSOJ		()							{ return this.datos.get(ScsSOJBean.C_NUMSOJ).toString();				}	
	
	/**
	 * Almacena en la Hashtable el tipo de un SOJ de un colegio. 
	 * @return String 
	 */
	public String getIdTipoSOJColeigo		()							{ return this.datos.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString();				}
	
	/**
	 * Recupera de la Hashtable el anho de un SOJ 
	 * @return String
	 */
	public String  getAnio					()							{ return this.datos.get(ScsSOJBean.C_ANIO).toString();							}
	
	/**
	 * Recupera de la Hashtable el número de un SOJ
	 * @return String 
	 */
	public String getNumero					()							{ return this.datos.get(ScsSOJBean.C_NUMERO).toString();						}
	
	/**
	 * Recupera de la Hashtable la fecha de apertura de un SOJ
	 * @return String 
	 */
	public String getFechaAperturaSOJDesde	()							{ return this.datos.get("FECHAAPERTURADESDE").toString();						}
	
	/**
	 * Recupera de la Hashtable la fecha de apertura de un SOJ
	 * @return String 
	 */
	public String getFechaAperturaSOJHasta	()							{ return this.datos.get("FECHAAPERTURAHASTA").toString();						}
	
	/**
	 * Recupera de la Hashtable la fecha de apertura de un SOJ
	 * @return String 
	 */
	public String getFechaAperturaSOJ		()							{ return this.datos.get(ScsSOJBean.C_FECHAAPERTURA).toString();						}
	
	/**
	 * Recupera de la Hashtable el estado de un SOJ 
	 * @return String 
	 */
	public String getEstadoSOJ				()	 						{ return this.datos.get(ScsSOJBean.C_ESTADO).toString();						}
	
	/**
	 * Recupera de la Hashtable la descripción de la consulta de un SOJ 
	 * @return String 
	 */
	public String getDescripcionConsulta	()							{ return this.datos.get(ScsSOJBean.C_DESCRIPCIONCONSULTA).toString();			} 
	
	/**
	 * Recupera de la Hashtable la respuesta a una consulta de un SOJ 
	 * @return String
	 */
	public String getRespuestaLetrado		()							{ return this.datos.get(ScsSOJBean.C_RESPUESTALETRADO).toString();				}
	
	/**
	 * Recupera de la Hashtable el identificador abogado de un SOJ
	 * @return String 
	 */
	public String getIdPersona				()							{ return this.datos.get(ScsSOJBean.C_IDPERSONA).toString();						}
	
	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return String
	 */
	public String getIdInstitucion	 		()							{return this.datos.get(ScsSOJBean.C_IDINSTITUCION).toString();					}
	
	/**
	 * Recupera de la Hashtable el identificador de un turno de un SOJ 
	 * @return String
	 */
	public String getIdTurno	 			()							{return this.datos.get(ScsSOJBean.C_IDTURNO).toString();						}
	
	/**
	 * Recupera de la Hashtable el identificador de la actuación de un SOJ 
	 * @return String 
	 */
	public String getIdFActuracion	 		()							{ return this.datos.get(ScsSOJBean.C_IDFACTURACION).toString();					}
	
	/**
	 * Recupera de la Hashtable la guardia de un SOJ
	 * @return String
	 */
	public String getIdGuardia	 			()						{return this.datos.get(ScsSOJBean.C_IDGUARDIA).toString();							}
	
	/**
	 * Recupera de la Hashtable el identificador del solicitante de un SOJ 
	 * @return String 
	 */
	public String getIdPersonaJG	 		()						{return this.datos.get(ScsSOJBean.C_IDPERSONAJG).toString();						}
	
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
	 * Recupera de la Hashtable el NIF de un solicitante
	 * @return String
	 */
	public String getNif					()						{ return this.datos.get(ScsPersonaJGBean.C_NIF).toString();							}
	
	/**
	 * Recupera de la Hashtable el número de colegiado
	 * @return String 
	 */
	public String getNColegiado				()						{ return this.datos.get("NCOLEGIADO").toString();									}
}