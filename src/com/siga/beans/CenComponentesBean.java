/*
 * nuria.rgonzalez - 14-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_COMPONENTES <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenComponentesBean extends MasterBean {

	/* Variables */
	private Integer	idInstitucion, idComponente, cen_Cliente_IdInstitucion, idCuenta, idInstitucionTipoColegio; 
	private Long	idPersona;
	private Float capitalSocial;
	
	private String 	cargo, fechaCargo, sociedad,idProvincia,idTipoColegio,numColegiado, cen_Cliente_IdPersona,idCargo;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_COMPONENTES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_IDCOMPONENTE				= "IDCOMPONENTE";
	static public final String C_CARGO						= "CARGO";
	static public final String C_FECHACARGO					= "FECHACARGO";
	static public final String C_CEN_CLIENTE_IDPERSONA		= "CEN_CLIENTE_IDPERSONA";
	static public final String C_CEN_CLIENTE_IDINSTITUCION	= "CEN_CLIENTE_IDINSTITUCION";
	static public final String C_SOCIEDAD					= "SOCIEDAD";
	static public final String C_IDCUENTA					= "IDCUENTA";
	static public final String C_IDTIPOCOLEGIO				= "IDTIPOCOLEGIO";
	static public final String C_NUMCOLEGIADO				= "NUMCOLEGIADO";
	static public final String C_CAPITALSOCIAL				= "CAPITALSOCIAL";
	static public final String C_IDCARGO					= "IDCARGO";
	static public final String C_IDPROVINCIA				= "IDPROVINCIA";
	static public final String C_IDINSTITUCION_TIPOCOLEGIO	= "IDINSTITUCION_TIPOCOLEGIO";
	
//	 Metodos GET
	public String getCargo() {	return cargo;}	
	public Integer getCen_Cliente_IdInstitucion() {return cen_Cliente_IdInstitucion;}
	public String getCen_Cliente_IdPersona() {return cen_Cliente_IdPersona;}
	public String getFechaCargo() {return fechaCargo;}
	public Integer getIdComponente() {return idComponente;}
	public Integer getIdInstitucion() {return idInstitucion;	}
	public Long getIdPersona() {return idPersona;}
	public String getSociedad() {return sociedad;}
	public Integer getIdCuenta() {return idCuenta;}
	public String getIdTipoColegio() {return idTipoColegio;	}
	public String getNumColegiado() {return numColegiado;	}
	public Float getCapitalSocial() {return capitalSocial;}
	public String getIdCargo() {return idCargo;}
	public String getIdProvincia() {return idProvincia;}
	public Integer getIdInstitucionTipoColegio() {return idInstitucionTipoColegio;	}
	
	//	 Metodos SET
	public void setCargo(String cargo) {this.cargo = cargo;	}
	public void setCen_Cliente_IdInstitucion(Integer cen_Cliente_IdInstitucion) {	this.cen_Cliente_IdInstitucion = cen_Cliente_IdInstitucion;	}
	public void setCen_Cliente_IdPersona(String cen_Cliente_IdPersona) {this.cen_Cliente_IdPersona = cen_Cliente_IdPersona;}
	public void setFechaCargo(String fechaCargo) {this.fechaCargo = fechaCargo;	}
	public void setIdComponente(Integer idComponente) {this.idComponente = idComponente;}
	public void setIdInstitucion(Integer idInstitucion) {this.idInstitucion = idInstitucion;}
	public void setIdPersona(Long idPersona) {this.idPersona = idPersona;}
	public void setSociedad(String sociedad) {this.sociedad = sociedad;}
	public void setIdCuenta(Integer dato) {this.idCuenta = dato;}
	public void setIdTipoColegio(String idTipoColegio){this.idTipoColegio=idTipoColegio;}
	public void setNumColegiado(String numColegiado){this.numColegiado=numColegiado;}
	public void setCapitalSocial(Float capitalSocial){this.capitalSocial=capitalSocial;}
	public void setIdCargo(String idCargo) {this.idCargo = idCargo;}
	public void setIdProvincia(String idProvincia) {this.idProvincia = idProvincia;}
	public void setIdInstitucionTipoColegio(Integer idInstitucionTipoColegio) {this.idInstitucionTipoColegio = idInstitucionTipoColegio;}
}
