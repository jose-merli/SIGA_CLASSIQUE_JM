/*
 * VERSIONES:
 * 
 * miguel.villegas - 20-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CENHISTORICO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

import java.util.Hashtable;

public class CenHistoricoBean extends MasterBean{

	/* Variables */
	private Long idPersona;
	private Integer idInstitucion;
	private Integer idInstitucionCargo;	




	private Integer idHistorico;	
	private String 	fechaEntrada;
	private String 	fechaEfectiva;
	private String 	motivo;
	private String 	descripcion;
	private Integer idTipoCambio;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_HISTORICO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA		= "IDPERSONA";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_IDINSTITUCIONCARGO	= "IDINSTITUCIONCARGO";	
	static public final String C_IDHISTORICO	= "IDHISTORICO";
	static public final String C_FECHAENTRADA	= "FECHAENTRADA";
	static public final String C_FECHAEFECTIVA	= "FECHAEFECTIVA";
	static public final String C_MOTIVO			= "MOTIVO";
	static public final String C_IDTIPOCAMBIO	= "IDTIPOCAMBIO";		
	static public final String C_DESCRIPCION	= "DESCRIPCION";		

	// Metodos SET
	public void setIdPersona (Long id) 	{ this.idPersona = id; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdHistorico (Integer s)	{ this.idHistorico = s; }
	public void setFechaEntrada (String s) 	{ this.fechaEntrada = s; }
	public void setFechaEfectiva (String s) 	{ this.fechaEfectiva = s; }
	public void setMotivo (String s)	{ this.motivo = s; }
	public void setIdTipoCambio (Integer id) 	{ this.idTipoCambio = id; }	
	public void setIdInstitucionCargo(Integer idInstitucionCargo) {
		this.idInstitucionCargo = idInstitucionCargo;
	}

	// Metodos GET
	public Long getIdPersona () 	{ return this.idPersona; }
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Integer getIdHistorico ()	{ return this.idHistorico; }
	public String getFechaEntrada () 	{ return this.fechaEntrada; }
	public String getFechaEfectiva () 	{ return this.fechaEfectiva; }
	public String getMotivo ()	{ return this.motivo; }
	public Integer getIdTipoCambio () 	{ return this.idTipoCambio; }
	public Integer getIdInstitucionCargo() {
		return this.idInstitucionCargo;
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
	
	public void  setHash(Hashtable beanHist){
		
		setIdPersona ((Long)beanHist.get(C_IDPERSONA));
		setIdInstitucion((Integer)beanHist.get(C_IDINSTITUCION)); 
		setIdHistorico ((Integer)beanHist.get(C_IDHISTORICO));
		setFechaEntrada ((String)beanHist.get(C_FECHAENTRADA));
		setFechaEfectiva ((String)beanHist.get(C_FECHAEFECTIVA));
		setMotivo ((String)beanHist.get(C_MOTIVO));
		setIdTipoCambio ((Integer)beanHist.get(C_IDTIPOCAMBIO));
		setIdInstitucionCargo((Integer)beanHist.get(C_IDINSTITUCIONCARGO));
		
	}
}
