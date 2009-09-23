/*
 * VERSIONES:
 * nuria.rgonzalez - 29-03-2005 - Creación
 * jose.barrientos - 28-02-2009 - Añadidos los campos fcs y nlineas
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FacDisqueteAbonosBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	private String 	bancosCodigo, fecha, nombreFichero;	
	private Long 	idDisqueteAbono;
	private String fcs;
	private Integer nlineas;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_DISQUETEABONOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDDISQUETEABONO		= "IDDISQUETEABONO";
	static public final String C_BANCOS_CODIGO		 	= "BANCOS_CODIGO";
	static public final String C_FECHA				 	= "FECHA";
	static public final String C_NOMBREFICHERO		 	= "NOMBREFICHERO";
	static public final String C_FCS		 			= "FCS";
	static public final String C_NUMEROLINEAS		 			= "NUMEROLINEAS";
	
//		METODOS SET
	public void setBancosCodigo(String bancosCodigo) 	{this.bancosCodigo = bancosCodigo;}	
	public void setFecha(String fecha) 					{this.fecha = fecha;}
	public void setIdDisqueteAbono(Long idDisqueteAbono) {this.idDisqueteAbono = idDisqueteAbono;}
	public void setIdInstitucion(Integer idInstitucion) {this.idInstitucion = idInstitucion;}
	public void setNombreFichero(String nombreFichero) 	{this.nombreFichero = nombreFichero;}
	public void setFCS(String fcs) 	{this.fcs = fcs;}
	public void setNumeroLineas(Integer nlineas) {this.nlineas = nlineas;}
	
//		METODOS GET
	public String getBancosCodigo() 	{return bancosCodigo;}
	public String getFecha() 			{return fecha;}
	public Long getIdDisqueteAbono() 	{return idDisqueteAbono;}
	public Integer getIdInstitucion() 	{return idInstitucion;}
	public String getNombreFichero() 	{return nombreFichero;}
	public String getFCS() 	{return fcs;}
	public Integer getNumeroLineas() 	{return nlineas;}	
	
}
