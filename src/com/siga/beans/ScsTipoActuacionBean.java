package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_HITOFACTURABLE
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */
 
public class ScsTipoActuacionBean extends MasterBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5027398428104051422L;
	/**
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTipoAsistencia;
	private Integer	idTipoActuacion;
	private String	descripcion;
	private float   importe;
	private float	importeMaximo;
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPOACTUACION";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION	            ="IDINSTITUCION";
	static public final String	C_IDTIPOASISTENCIA       	="IDTIPOASISTENCIA";
	static public final String	C_IDTIPOACTUACION       	="IDTIPOACTUACION";
	static public final String	C_DESCRIPCION	            ="DESCRIPCION";
	static public final String	C_IMPORTE	                ="IMPORTE";
	static public final String 	C_IMPORTEMAXIMO 	        ="IMPORTEMAXIMO";

		
	
	/**
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion del atipo asistencia colegio
	 * 
	 * @param valor Identificador de la institucion del tipo asistencia colegio. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 		(Integer valor)	{ this.idInstitucion =			valor;}
	/**
	 * Almacena en el Bean el identificador del tipo de actuacion del Colegio
	 * 
	 * @param valor Identificador del tipo de actuacion del Colegio. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTipoActuacion				(Integer valor)	{ this.idTipoActuacion	=				valor;}
	
	/**
	 * Almacena en el Bean el identificador del tipo de asistencia del Colegio
	 * 
	 * @param valor Identificador del tipo de asistencia del Colegio. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTipoAsistencia 				(Integer valor)	{ this.idTipoAsistencia	=				valor;}
	
	/**
	 * Almacena en el Bean la descripcion del tipo de asistencia del colegio
	 * 
	 * @param valor la descripcion del tipo de asistencia del colegio. De tipo "String". 
	 * @return void 
	 */
	public void setDescripcion			(String valor)	{ this.descripcion	=			valor;}
	
	/**
	 * Almacena en el Bean el importe
	 * 
	 * @param valor Identificador importe. De tipo "float". 
	 * @return void 
	 */
	public void setImporte				(float valor)	{ this.importe	=				valor;}
	
	/**
	 * Almacena en el Bean el importe maximo
	 * 
	 * @param valor el importe maximo. De tipo "float". 
	 * @return void 
	 */
	public void setImporteMaximo		(float  valor)	{ this.importeMaximo =		valor;}
	
	
	
	
	/**
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la institucion del tipo asistencia colegio
	 * 
	 * @return Identificador de la institucion del tipo asistencia colegio
	 */
	public Integer getIdInstitucion 		()	{ return this.idInstitucion;		}
	/**
	 * Recupera del Bean el identificador del tipo de asistencia del Colegio
	 * 
	 * @return identificador del tipo de asistencia del Colegio
	 */
	public Integer getIdTipoAsistencia				()	{ return this.idTipoAsistencia;				}
	
	/**
	 * Recupera del Bean el identificador del tipo de actuacion del Colegio
	 * 
	 * @return identificador del tipo de actuacion del Colegio
	 */
	public Integer getIdTipoActuacion				()	{ return this.idTipoActuacion;				}
	/**
	 * Recupera del Bean la descripcion del tipo de asistencia del colegio
	 * 
	 * @return la descripcion del tipo de asistencia del colegio
	 */
	public String getDescripcion				()	{ return this.descripcion;			}
	/**
	 * Recupera del Bean el importe
	 * 
	 * @return el importe
	 */
	public float getImporte				()	{ return this.importe;				}
	/**
	 * Recupera del Bean el importe maximo
	 * 
	 * @return el importe maximo
	 */
	public float  getImporteMaximo			()	{ return this.importeMaximo;		}

	
}