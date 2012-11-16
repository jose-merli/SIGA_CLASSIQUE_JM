/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;
import java.util.Hashtable;

/**
* Maneja el formulario que mantiene la tabla SCS_SOJ
*/
 public class DatosGeneralesSOJForm extends MasterForm{	
	
	protected Hashtable ejg= new Hashtable();
	String anio="",num="",tipo=""; // Variables para el EJG
	
	
	
	public void  setAnioEJG				(String anioEJG)							{ this.ejg.put(ScsEJGBean.C_ANIO, anioEJG);	anio=anioEJG;																			}

	/**
	 * @return Numero EJG
	 */
	public void  setNumeroEJG				(String numeroEJG)							{ this.ejg.put(ScsEJGBean.C_NUMEJG, numeroEJG);	num=numeroEJG;	
	
	}
	

	/**
	 * @return Tipo EJG
	 */
	public void  setTipoEJG				(String tipoEJG)							{this.ejg.put(ScsEJGBean.C_IDTIPOEJG, tipoEJG);	tipo=tipoEJG;					}
	// Metodos Set (Formulario (*.jsp))
 	
 	/**
	 * Almacena en la Hashtable el identificador de un SOJ 
	 * 
	 * @param Identificador SOJ 
	 * @return void 
	 */
	public void setIdTipoSOJ				(String idTipoSOJ)				{ this.datos.put(ScsSOJBean.C_IDTIPOSOJ, idTipoSOJ);						}
	
 	/**
	 * Almacena en la Hashtable el numero colegiado tramitador 
	 * 
	 * @param Identificador SOJ 
	 * @return void 
	 */
	public void setNumColegiado				(String numColegiado)				{ this.datos.put("NCOLEGIADO", numColegiado);						}
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
	 * Almacena en la Hashtable el código de un SOJ 
	 * 
	 * @param Número del SOJ 
	 * @return void 
	 */
	public void setNumSOJ					(String idNumeroSOJ)				{ this.datos.put(ScsSOJBean.C_NUMSOJ, idNumeroSOJ);							}
	
	/**
	 * Almacena en la Hashtable el identificador de una persona 
	 * 
	 * @param Identificador persona 
	 * @return void 
	 */
	public void setIdPersonaJG				(String idPersonaJG)				{ this.datos.put(ScsSOJBean.C_IDPERSONAJG, idPersonaJG);				}
	
	
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
	 * @param ejg objeto cargado con los datos de EJG (Anio, Numero, Tipo y Fecha Apertura)
	 */
	public void setEJG	 				(Hashtable ejg)		{ 
		if(ejg==null){
			this.ejg.clear();
		}else{
			this.ejg=ejg;
		}	
	}
	
	public void setTipoConsulta						(String tipoConsulta)					{ this.datos.put(ScsSOJBean.C_IDTIPOCONSULTA, tipoConsulta);}
	public void setTipoRespuesta					(String tipoRespuesta)					{ this.datos.put(ScsSOJBean.C_IDTIPORESPUESTA, tipoRespuesta);}
	public void setIdTurno      					(String idTurno    )					{ this.datos.put(ScsSOJBean.C_IDTURNO, idTurno);}
	public void setIdGuardia      					(String idGuardia   )					{ this.datos.put(ScsSOJBean.C_IDGUARDIA, idGuardia);}
	
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	/**
	 * Recupera de la Hashtable el identificador de un SOJ 
	 * @return String
	 */
	public String  getIdTipoSOJ				()							{ return this.datos.get(ScsSOJBean.C_IDTIPOSOJ).toString();						}

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
	 * Recupera de la Hashtable código de un SOJ
	 * @return String 
	 */
	public String getNumSOJ					()							{ return this.datos.get(ScsSOJBean.C_NUMSOJ).toString();						}

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
	 * Recupera de la Hashtable el identificador de una persona 
	 * @return String 
	 */
	public String getIdPersonaJG			()							{ return this.datos.get(ScsSOJBean.C_IDPERSONAJG).toString();										}
	
	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return String 
	 */
	public String getIdInstitucion	 		()							{ return this.datos.get(ScsSOJBean.C_IDINSTITUCION).toString();										}
	

	/**
	 * @return El numero de atributos que tiene el objeto EJG
	 */
	public String  getSizeEJG				()							{ return Integer.toString(ejg.size());																}

	/**
	 * @return Anio EJG
	 */
	public String  getAnioEJG				()							{if((String) ejg.get(ScsEJGBean.C_ANIO)==null)		{return anio;}		else{return (String) ejg.get(ScsEJGBean.C_ANIO);
	}}														

	/**
	 * @return Numero EJG
	 */
	public String  getNumeroEJG				()							{ if((String) ejg.get(ScsEJGBean.C_NUMERO)==null){return num;}else{return (String) ejg.get(ScsEJGBean.C_NUMERO);}														}

	/**
	 * @return Tipo EJG
	 */
	public String  getTipoEJG				()							{ if( (String) ejg.get(ScsEJGBean.C_IDTIPOEJG)==null){return tipo;}else{return (String) ejg.get(ScsEJGBean.C_IDTIPOEJG);}													}

	/**
	 * @return Tipo EJG
	 */
	public String  getDescTipoEJG				()						{ return (String) ejg.get(ScsTipoEJGBean.C_DESCRIPCION);											}

	/**
	 * @return Fecha Apertura EJG
	 */
	public String  getFechaAperturaEJG		()							{ return (String) ejg.get(ScsEJGBean.C_FECHAAPERTURA);												}
	
	/**
	 * @return Numero EJG
	 */
	public String  getCodigoEJG				()							{ return (String) ejg.get(ScsEJGBean.C_NUMEJG);														}
	
	public String  getTipoConsulta					()					{ return this.datos.get(ScsSOJBean.C_IDTIPOCONSULTA).toString();							}
	public String  getTipoRespuesta			()							{ return this.datos.get(ScsSOJBean.C_IDTIPORESPUESTA).toString();							}
	public String  getIdTurno   			()							{ return this.datos.get(ScsSOJBean.C_IDTURNO).toString();							}
	public String  getidGuardia 			()							{ return this.datos.get(ScsSOJBean.C_IDGUARDIA).toString();							}
	

}