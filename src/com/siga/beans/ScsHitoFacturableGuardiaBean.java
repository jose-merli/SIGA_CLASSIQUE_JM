package com.siga.beans;


/**
 * Implementa las operaciones sobre el bean de la tabla SCS_HITOFACTURABLE
 * 
 * @author ruben.fernandez
 * @since 06-12-2004
 * @version adrian.ayala - 18-07-2008: adicion de nuevos campos
 */
public class ScsHitoFacturableGuardiaBean extends MasterBean
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	//Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_HITOFACTURABLEGUARDIA";
	
	//Nombres de los campos de la Tabla
	static public final String	C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String	C_IDTURNO			= "IDTURNO";
	static public final String	C_IDGUARDIA			= "IDGUARDIA";
	static public final String	C_IDHITO			= "IDHITO";
	static public final String 	C_PAGOFACTURACION	= "PAGOOFACTURACION";
	static public final String 	C_PRECIOHITO		= "PRECIOHITO";
	static public final String 	C_DIASAPLICABLES	= "DIASAPLICABLES";
	static public final String 	C_AGRUPAR			= "AGRUPAR";
	
	
	
	//////////////////// ATRIBUTOS ////////////////////
	//Valores de los campos
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer	idGuardia;
	private Integer	idHito;
	private String	pagoFacturacion;
	private float	precioHito;
	private String	diasAplicables;
	private boolean	agrupar;
	
	
	
	//////////////////// SETTERS ////////////////////
	public void setIdInstitucion 	(Integer	valor) {this.idInstitucion		= valor;}
	public void setIdTurno 			(Integer	valor) {this.idTurno			= valor;}
	public void setIdGuardia		(Integer	valor) {this.idGuardia			= valor;}
	public void setIdHito			(Integer	valor) {this.idHito				= valor;}
	public void setPagoFacturacion	(String		valor) {this.pagoFacturacion	= valor;}
	public void setPrecioHito		(float		valor) {this.precioHito			= valor;}
	public void setDiasAplicables	(String		valor) {this.diasAplicables		= valor;}
	public void setAgrupar			(boolean	valor) {this.agrupar			= valor;}
	
	
	
	//////////////////// GETTERS ////////////////////
	public Integer	getIdInstitucion 	() {return this.idInstitucion;		}
	public Integer	getIdTurno 			() {return this.idTurno;			}
	public Integer	getIdGuardia		() {return this.idGuardia;			}
	public Integer	getIdHito			() {return this.idHito;				}
	public String	getPagoFacturacion	() {return this.pagoFacturacion;	}
	public float	getPrecioHito		() {return this.precioHito;			}
	public String	getDiasAplicables	() {return this.diasAplicables;		}
	public boolean	getAgrupar			() {return this.agrupar;			}
	
}