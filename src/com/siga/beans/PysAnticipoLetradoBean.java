/**
 * VERSIONES:
 * 
 * jose.barrientos	- 28-11-2008 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSSERVICIOANTICIPO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;


public class PysAnticipoLetradoBean extends MasterBean{
	
	/* Variables */
	private Long idPersona;
	private Integer idInstitucion, 	
					idAnticipo;
	private Double	importeInicial;
	private String	descripcion,
					fecha,
					ctaContable,
					contabilizado;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_ANTICIPOLETRADO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 	= "IDINSTITUCION";
	static public final String C_IDPERSONA 		= "IDPERSONA";
	static public final String C_IDANTICIPO 	= "IDANTICIPO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_FECHA 			= "FECHA";
	static public final String C_IMPORTEINICIAL = "IMPORTEINICIAL";
	static public final String C_CTACONTABLE = "CTACONTABLE";
	static public final String C_CONTABILIZADO = "CONTABILIZADO";
	
	/* Metodos SET */
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion= id; }
	public void setIdPersona (Long id) 		{ this.idPersona= id; }
	public void setIdAnticipo (Integer id) 		{ this.idAnticipo= id; }
	public void setDescripcion (String desc) 	{ this.descripcion= desc; }
	public void setCtaContable (String valor) 	{ this.ctaContable= valor; }
	public void setFecha (String fc) 			{ this.fecha= fc; }
	public void setContabilizado (String fc) 			{ this.contabilizado= fc; }
	public void setImporteInicial (Double imp) 	{ this.importeInicial= imp; }
	
	/* Metodos GET */
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdPersona () 		{ return this.idPersona; }
	public Integer getIdAnticipo () 	{ return this.idAnticipo; }
	public String  getDescripcion () 	{ return this.descripcion; }
	public String  getCtaContable () 	{ return this.ctaContable; }
	public String  getFecha () 			{ return this.fecha; }
	public String  getContabilizado () 			{ return this.contabilizado; }
	public Double  getImporteInicial ()	{ return this.importeInicial; }
}
