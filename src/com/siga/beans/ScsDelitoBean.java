package com.siga.beans;

/**
 * Bean de la tabla SCS_DELITO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsDelitoBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idDelito;
	private String	descripcion=null;
	private Integer idInstitucion;
	
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

	public Integer getIdInstitucion()
	{
		return idInstitucion;
	}
	
	public void setIdInstitucion(Integer idInstitucion)
	{
		this.idInstitucion=idInstitucion;
	}
	
	/**
	 * @return Returns the idDelito.
	 */
	public Integer getIdDelito() {
		return idDelito;
	}
	/**
	 * @param idDelito The idDelito to set.
	 */
	public void setIdDelito(Integer idDelito) {
		this.idDelito = idDelito;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_DELITO";
	
	
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDDELITO = 		"IDDELITO";
	static public final String 	C_DESCRIPCION = 	"DESCRIPCION";
	static public final String  C_IDINSTITUCION =   "IDINSTITUCION";
	

}