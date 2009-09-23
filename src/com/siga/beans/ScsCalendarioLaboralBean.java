package com.siga.beans;

//Clase: ScsCalendarioLaboralBean
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 22/12/2004

public class ScsCalendarioLaboralBean extends MasterBean {

	/* Variables */
	private String modo;
	
	private Integer identificativo,
				    idinstitucion,
				    idpartido;
	
	private String nombrefiesta,
				   fecha;
	
	
	/* Nombre tabla*/
	static public String T_NOMBRETABLA = "SCS_CALENDARIOLABORAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDENTIFICATIVO			= "IDENTIFICATIVO";
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDPARTIDO				= "IDPARTIDO";
	static public final String C_FECHA					= "FECHA";	
	static public final String C_NOMBREFIESTA			= "NOMBREFIESTA";
	
	/* Metodos SET */
	/**
	 * Almacena en el Bean el identificador de una fiesta 
	 * 
	 * @param id Identificador retención. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdentificativo (Integer id) 					{ this.identificativo = id; 		 }
	
	/**
	 * Almacena en el Bean la fecha de una fiesta 
	 * 
	 * @param fecha Fecha de una fiesta. De tipo "String". 
	 * @return void 
	 */
	public void setFecha 		  (String fecha)				{ this.fecha = fecha;		 		 }	
	
	/**
	 * Almacena en el Bean el nombre de una fiesta 
	 * 
	 * @param fiesta Nombre de una fiesta. De tipo "String". 
	 * @return void 
	 */
	public void setNombreFiesta   (String fiesta) 				{ this.nombrefiesta = fiesta; 		 }
	
	/**
	 * Almacena en el Bean el identificador de una institución 
	 * 
	 * @param idinstitucion Nombre de una fiesta. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion  (Integer idinstitucion)	 	{ this.idinstitucion = idinstitucion;}
	
	/**
	 * Almacena en el Bean el identificador de un partido judicial
	 * 
	 * @param idpartido Identificador de un partido judicial. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdPartido	  (Integer idpartido)		 	{ this.idpartido = idpartido;		 }
	
	/* Metodos GET */
	/**
	 * Recupera del Bean el identificador de una fiesta  
	 * 
	 * @return Identificador de una fiesta. De tipo "Integer" 
	 */
	public Integer 	getIdentificativo   ()						{return this.identificativo;		 }
	
	/**
	 * Recupera del Bean la fecha de una fiesta  
	 * 
	 * @return Fecha de una fiesta. De tipo "String" 
	 */
	public String 	getFecha			()						{return this.fecha;					 }	
	
	/**
	 * Recupera del Bean el nombre de una fiesta  
	 * 
	 * @return Nombre de una fiesta. De tipo "String" 
	 */
	public String 	getNombreFiesta	 	()						{return this.nombrefiesta;			 }
	
	/**
	 * Recupera del Bean el identificador de la institución asociada a la fiesta.  
	 * 
	 * @return Identificador de una institución. De tipo "Integer" 
	 */
	public Integer 	getIdInstitucion   	()				 		{return this.idinstitucion;			 }
	
	/**
	 * Recupera del Bean el identificador del partido judicial.  
	 * 
	 * @return Identificador de un partido judicial. De tipo "Integer" 
	 */
	public Integer 	getIdPartido	  	()		 				{return this.idpartido;		 		 }
	
}