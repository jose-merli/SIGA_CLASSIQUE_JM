package com.siga.beans;

//Clase: ScsRetencionesIRPFBean 
//Autor: carlos.vidal@atosorigin.com
//Ultima modificación: 20/12/2004
/**
 * Implementa las operaciones sobre el bean de la tabla SCS_RETENCIONESIRPF
 */
public class ScsRetencionesIRPFBean extends MasterBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3407674679050323295L;
	/* Variables */
	private Integer idInstitucion;	
	private Long idPersona;	
	private Integer idRetencion;	
	private String 	fechaInicio,fechaFin;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_RETENCIONESIRPF";
	
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDPERSONA 				= "IDPERSONA";
	static public final String C_IDRETENCION 			= "IDRETENCION";
	static public final String C_FECHAINICIO			= "FECHAINICIO";	
	static public final String C_FECHAFIN				= "FECHAFIN";
	
	// Metodos SET y GET
	public void setIdInstitucion (Integer d)	{ this.idInstitucion = d; 		}
	public Integer getIdInstitucion()			{ return this.idInstitucion; 		}

	public void setIdPersona (Long d)	{ this.idPersona = d; 		}
	public Long getIdPersona()			{ return this.idPersona; 		}
	
	public void setIdRetencion (Integer d)	{ this.idRetencion = d; 		}
	public Integer getIdRetencion()			{ return this.idRetencion; 		}
	
	public void setFechaInicio (String d)	{ this.fechaInicio = d; 		}
	public String getFechaInicion()			{ return this.fechaInicio; 		}

	public void setFechaFin (String d)	{ this.fechaFin = d; 		}
	public String getFechaFin()				{ return this.fechaFin; 		}
	
}