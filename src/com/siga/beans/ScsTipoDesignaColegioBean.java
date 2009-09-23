package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_TIPODESIGNACOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 */

public class ScsTipoDesignaColegioBean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTipoDesignaColegiado;
	private String	descripcion;
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPODESIGNACOLEGIO";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDTIPODESIGNACOLEGIADO = 	"IDTIPODESIGNACOLEGIO";
	static public final String  C_DESCRIPCION =				"DESCRIPCION";	
		
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////
	
	/**
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion del tipo de designa 
	 * 
	 * @param valor Identificador de la institucion del tipo de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion = 	valor;}
	/**
	 * Almacena en el Bean el identificador del tipo de designa 
	 * 
	 * @param valor Identificador del tipo de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTipoDesignaColegiado	(Integer valor)	{ this.idTipoDesignaColegiado =	valor;}
	/**
	 * Almacena en el Bean la descripcion del tipo de designa
	 * 
	 * @param valor Descripcion del tipo de designa. De tipo "String". 
	 * @return void 
	 */
	public void setDescripcion				(String valor)	{ this.descripcion = valor;}
	
	
	/**
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la institucion de la designa
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion		()	{ return this.idInstitucion;	}
	/**
	 * Recupera del Bean el identificador del tipo de designa
	 * 
	 * @return Identificador del tipo de la designa
	 */
	public Integer  getIdTipoDesignaColegiado	()	{ return this.idTipoDesignaColegiado;}
	/**
	 * Recupera del Bean la descripcion del tipo de la designa
	 * 
	 * @return Descrpicion del tipo de la designa
	 */
	public String getDescripcion 				() 	{ return this.descripcion;			}	
	}