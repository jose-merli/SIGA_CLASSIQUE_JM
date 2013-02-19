/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_ABONO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class FacAbonoBean extends MasterBean{

	/* Variables */
	private Long idAbono;
	private Integer idInstitucion;
	private String 	motivos;	
	private String 	fecha;
	private Long idPersona;
	private Integer idCuenta;
	private Long idPersonaDeudor;
	private Integer idCuentaDeudor;
	private Integer idPagosJG;
	private String 	contabilizada;
	private String idFactura;
	private String numeroAbono;
	private String impExcesivo;
	private String observaciones;
	
	private Integer estado;
	private Double impTotalNeto;
	private Double impTotalIva;
	private Double impTotal;
	private Double impTotalAbonadoEfectivo;
	private Double impTotalAbonadoPorBanco;
	private Double impTotalAbonado;
	private Double impPendientePorBanco;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_ABONO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDABONO		= "IDABONO";
	static public final String C_IDPERSONA		= "IDPERSONA";	
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_FECHA			= "FECHA";
	static public final String C_MOTIVOS		= "MOTIVOS";
	static public final String C_IDCUENTA		= "IDCUENTA";
	static public final String C_IDPAGOSJG		= "IDPAGOSJG";
	static public final String C_CONTABILIZADA	= "CONTABILIZADA";
	static public final String C_IDFACTURA		= "IDFACTURA";	
	static public final String C_NUMEROABONO	= "NUMEROABONO";
	static public final String C_IMPEXCESIVO	= "IMPEXCESIVO";
	static public final String C_OBSERVACIONES	= "OBSERVACIONES";
	static public final String C_IDPERSONADEUDOR= "IDPERSONADEUDOR";	
	static public final String C_IDCUENTADEUDOR	= "IDCUENTADEUDOR";

	static public final String C_ESTADO	= "ESTADO";
	static public final String C_IMPTOTALNETO	= "IMPTOTALNETO";
	static public final String C_IMPTOTALIVA	= "IMPTOTALIVA";
	static public final String C_IMPTOTAL	= "IMPTOTAL";
	static public final String C_IMPTOTALABONADOEFECTIVO	= "IMPTOTALABONADOEFECTIVO";
	static public final String C_IMPTOTALABONADOPORBANCO	= "IMPTOTALABONADOPORBANCO";
	static public final String C_IMPTOTALABONADO	= "IMPTOTALABONADO";
	static public final String C_IMPPENDIENTEPORABONAR	= "IMPPENDIENTEPORABONAR";

	// Metodos SET
	public void setIdPersona (Long id) 	{ this.idPersona = id; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdAbono (Long id)	{ this.idAbono = id; }
	public void setFecha (String s) 	{ this.fecha = s; }
	public void setIdCuenta (Integer id) 	{ this.idCuenta = id; }
	public void setIdPagosJG (Integer id) 	{ this.idPagosJG = id; }
	public void setMotivos (String s)	{ this.motivos = s; }
	public void setIdFactura (String id) 	{ this.idFactura = id; }	
	public void setContabilizada (String s) 	{ this.contabilizada = s; }
	public void setNumeroAbono (String id) 	{ this.numeroAbono = id; }
	public void setImpExcesivo (String id) 	{ this.impExcesivo = id; }
	public void setObservaciones (String id) 	{ this.observaciones = id; }

	public void setEstado (Integer id) 	{ this.estado = id; }
	public void setImpTotalNeto (Double id) 	{ this.impTotalNeto = id; }
	public void setImpTotalIva (Double id) 	{ this.impTotalIva = id; }
	public void setImpTotal (Double id) 	{ this.impTotal = id; }
	public void setImpTotalAbonadoEfectivo (Double id) 	{ this.impTotalAbonadoEfectivo = id; }
	public void setImpTotalAbonadoPorBanco (Double id) 	{ this.impTotalAbonadoPorBanco = id; }
	public void setImpTotalAbonado (Double id) 	{ this.impTotalAbonado = id; }
	public void setImpPendientePorAbonar (Double id) 	{ this.impPendientePorBanco = id; }

	// Metodos GET
	public Long getIdPersona () 	{ return this.idPersona; }
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdAbono ()	{ return this.idAbono; }
	public String getFecha () 	{ return this.fecha; }
	public Integer getIdCuenta () 	{ return this.idCuenta; }
	public Integer getIdPagosJG () 	{ return this.idPagosJG; }
	public String getMotivos ()	{ return this.motivos; }
	public String getIdFactura () 	{ return this.idFactura; }	
	public String getContabilizada () 	{ return this.contabilizada; }	
	public String getNumeroAbono () 	{ return this.numeroAbono; }	
	public String getImpExcesivo() 	{ return this.impExcesivo; }	
	public String getObservaciones() 	{ return this.observaciones; }	

	public Integer getEstado () 	{ return this.estado; }
	public Double getImpTotalNeto () 	{ return this.impTotalNeto; }
	public Double getImpTotalIva () 	{ return this.impTotalIva; }
	public Double getImpTotal () 	{ return this.impTotal; }
	public Double getImpTotalAbonadoEfectivo () 	{ return this.impTotalAbonadoEfectivo; }
	public Double getImpTotalAbonadoPorBanco () 	{ return this.impTotalAbonadoPorBanco; }
	public Double getImpTotalAbonado () 	{ return this.impTotalAbonado; }
	public Double getImpPendientePorAbonar () 	{ return this.impPendientePorBanco; }
	
	public Long getIdPersonaDeudor() {
		return idPersonaDeudor;
	}
	public void setIdPersonaDeudor(Long idPersonaDeudor) {
		this.idPersonaDeudor = idPersonaDeudor;
	}
	public Integer getIdCuentaDeudor() {
		return idCuentaDeudor;
	}
	public void setIdCuentaDeudor(Integer idCuentaDeudor) {
		this.idCuentaDeudor = idCuentaDeudor;
	}
}
