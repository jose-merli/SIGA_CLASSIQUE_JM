package com.siga.beans;

/**
 * Bean de la tabla CEN_TIPOSANCION
 * 
 * @author rgg
 * @since 16/03/2007
 */
public class CenTipoSancionBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idTipoSancion;
	private String	descripcion=null;
	
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
	 * @return Returns the idTipoSancion.
	 */
	public Integer getIdTipoSancion() {
		return idTipoSancion;
	}
	/**
	 * @param idDelito The idTipoSancion to set.
	 */
	public void setIdTipoSancion(Integer valor) {
		this.idTipoSancion = valor;
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
	static public String T_NOMBRETABLA = "CEN_TIPOSANCION";
	
	
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDTIPOSANCION = 		"IDTIPOSANCION";
	static public final String 	C_DESCRIPCION = 	"DESCRIPCION";
	

}