package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenTipoSociedadBean extends MasterBean{
	/* Variables */
	private String letraCif;
	private String 	descripcion;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOSOCIEDAD";
	
	/* Nombre campos de la tabla */	
	static public final String C_LETRACIF			= "LETRACIF";	
	static public final String C_DESCRIPCION		= "DESCRIPCION";
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
		
	//	 Metodos SET
	/**
	 * @param descripcion obtiene el descripcion.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @param idCV obtiene el idCV.
	 */
	public void setLetraCif(String letraCif) {
		this.letraCif = letraCif;
	}

	/**
	 * @return Devuelve la descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @return Devuelve el idCV.
	 */
	public String getLetraCif() {
		return letraCif;
	}
	
}
