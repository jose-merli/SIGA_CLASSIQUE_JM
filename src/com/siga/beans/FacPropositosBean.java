/*
 * VERSIONES:
 * MJM 15/07/2014 Creacion
 */
package com.siga.beans;

/**
 * Bean de la tabla FAC_MOTIVODEVOLUCION
 * @author MJM
 *
 */
public class FacPropositosBean extends MasterBean {

	/* Variables */	
	private Integer 	idProposito,tipoSEPA;
	private String      codigo, nombre;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_PROPOSITOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPROPOSITO= "IDPROPOSITO";
	static public final String C_CODIGO		= "CODIGO";
	static public final String C_TIPOSEPA	= "TIPOSEPA";
	static public final String C_NOMBRE		= "NOMBRE";

	// Metodos SET
	public void setIdProposito(Integer idProposito) {this.idProposito = idProposito;}	
	public void setTipoSEPA(Integer tipoSEPA) 		{this.tipoSEPA = tipoSEPA;}	
	public void setCodigo(String codigo) 			{this.codigo = codigo;}	
	public void setNombre(String nombre) 			{this.nombre = nombre;}
	
	//Metodos GET
	public Integer getIdProposito() {return idProposito;}	
	public Integer getTipoSEPA() {return tipoSEPA;}	
	public String getCodigo() {return codigo;}	
	public String getNombre() {return nombre;}
	
}
