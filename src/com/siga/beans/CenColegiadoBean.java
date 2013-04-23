/*
 * Created on 14-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenColegiadoBean extends MasterBean {
	private static final long serialVersionUID = -2510899080052901261L;

	/* Variables */
	private Long idPersona;
	private Integer idInstitucion, idTipoSeguro;
	
	private String 	nColegiado, 		comunitario, 		nComunitario, 		indTitulacion,		otrosColegios, 
					jubilacionCuota, 	situacionEjercicio, situacionResidente, situacionEmpresa,  	fechaPresentacion, 
					fechaIncorporacion, fechaJura,			fechaTitulacion, 	fechaDeontologia, 	fechaMovimiento,
					cuentacontableSJCS,	nMutualista, identificadorDS, numSolicitudColegiacion;
	
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_COLEGIADO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA 			= "IDPERSONA";
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_NCOLEGIADO 		= "NCOLEGIADO";
	static public final String C_FECHAPRESENTACION 	= "FECHAPRESENTACION";
	static public final String C_FECHAINCORPORACION = "FECHAINCORPORACION";
	static public final String C_FECHAJURA 			= "FECHAJURA";
	static public final String C_NCOMUNITARIO 		= "NCOMUNITARIO";
	static public final String C_INDTITULACION 		= "INDTITULACION";
	static public final String C_FECHATITULACION 	= "FECHATITULACION";
	static public final String C_OTROSCOLEGIOS		= "OTROSCOLEGIOS";
	static public final String C_JUBILACIONCUOTA	= "JUBILACIONCUOTA";
	static public final String C_FECHADEONTOLOGIA	= "FECHADEONTOLOGIA";
	static public final String C_SITUACIONEJERCICIO	= "SITUACIONEJERCICIO";
	static public final String C_SITUACIONRESIDENTE	= "SITUACIONRESIDENTE";
	static public final String C_SITUACIONEMPRESA	= "SITUACIONEMPRESA";
	static public final String C_FECHAMOVIMIENTO	= "FECHAMOVIMIENTO";
	static public final String C_IDTIPOSSEGURO		= "IDTIPOSSEGURO";
	static public final String C_COMUNITARIO		= "COMUNITARIO";
	static public final String C_CUENTACONTABLESJCS = "CUENTACONTABLESJCS";
	static public final String C_IDENTIFICADORDS    = "IDENTIFICADORDS";
	static public final String C_NMUTUALISTA    	= "NMUTUALISTA";
	static public final String C_NUMSOLICITUDCOLEGIACION = "NUMSOLICITUDCOLEGIACION";
	

	// Metodos SET
	public void setIdPersona (Long id) 			{ this.idPersona = id; }
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdTipoSeguro (Integer tipo)		{ this.idTipoSeguro = tipo; }
	public void setNColegiado (String s)			{ this.nColegiado = s; }
	public void setComunitario (String s)			{ this.comunitario = s; }
	public void setNComunitario (String s)			{ this.nComunitario = s; }
	public void setIndTitulacion (String s)			{ this.indTitulacion = s; }
	public void setOtrosColegios (String s)			{ this.otrosColegios = s; }
	public void setJubilacionCuota (String s)		{ this.jubilacionCuota = s; }
	public void setSituacionEjercicio (String s)	{ this.situacionEjercicio = s; }
	public void setSituacionResidente (String s)	{ this.situacionEmpresa = s; }
	public void setSituacionEmpresa (String s)		{ this.situacionResidente = s; }
	public void setFechaPresentacion (String f) 	{ this.fechaPresentacion = f; }
	public void setFechaIncorporacion (String f) 	{ this.fechaIncorporacion = f; }
	public void setFechaJura (String f) 			{ this.fechaJura = f; }
	public void setFechaTitulacion (String f) 		{ this.fechaTitulacion = f; }
	public void setFechaDeontologia (String f) 		{ this.fechaDeontologia = f; }
	public void setFechaMovimiento (String f) 		{ this.fechaMovimiento = f; }
	public void setCuentaContableSJCS (String f) 	{ this.cuentacontableSJCS = f; }
	public void setIdentificadorDS (String f) 		{ this.identificadorDS = f; }
	public void setNMutualista (String f) 			{ this.nMutualista = f; }
	public void setNumSolicitudColegiacion(String numSolicitudColegiacion) { this.numSolicitudColegiacion = numSolicitudColegiacion; }
		
	// Metodos GET
	public Long getIdPersona 		()	{ return this.idPersona; }
	public Integer getIdInstitucion 	()	{ return this.idInstitucion; }
	public Integer getIdTipoSeguro 		()	{ return this.idTipoSeguro; }
	public String getNColegiado 		()	{ return this.nColegiado; }
	public String getComunitario 		()	{ return this.comunitario; }
	public String getNComunitario 		()	{ return this.nComunitario; }
	public String getIndTitulacion 		()	{ return this.indTitulacion; }
	public String getOtrosColegios 		()	{ return this.otrosColegios; }
	public String getJubilacionCuota 	()	{ return this.jubilacionCuota; }
	public String getSituacionEjercicio ()	{ return this.situacionEjercicio; }
	public String getSituacionResidente ()	{ return this.situacionEmpresa; }
	public String getSituacionEmpresa 	()	{ return this.situacionResidente; }
	public String getFechaPresentacion 	() 	{ return this.fechaPresentacion; }
	public String getFechaIncorporacion () 	{ return this.fechaIncorporacion; }
	public String getFechaJura 			()	{ return this.fechaJura; }
	public String getFechaTitulacion 	()	{ return this.fechaTitulacion; }
	public String getFechaDeontologia 	()	{ return this.fechaDeontologia; }
	public String getFechaMovimiento 	()	{ return this.fechaMovimiento;}
	public String getCuentaContableSJCS ()	{ return this.cuentacontableSJCS;}
	public String getIdentificadorDS    () 	{ return this.identificadorDS; }
	public String getNMutualista    	() 	{ return this.nMutualista; }
	public String getNumSolicitudColegiacion() { return numSolicitudColegiacion; }
}
