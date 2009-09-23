package com.siga.beans;

/**
 * @author david.sanchezp
 * Bean de la tabla CEN_INSTITUCION_POBLACION.
 * 
 */
public class CenInstitucionPoblacionBean extends MasterBean{

	/* Atributos */
	private Integer idInstitucion;
	private String idPoblacion;
	private Integer UsuMod;
	private String fechaMod;
	
	/* Nombre de la tabla */
	public static final String T_NOMBRETABLA = "CEN_INSTITUCION_POBLACION";
	
	/* Nombre de los campos de la tabla */
	public static final String C_IDINSTITUCION = "IDINSTITUCION";
	public static final String C_IDPOBLACION = "IDPOBLACION";
	public static final String C_USUMODIFICACION = "USUMODIFICACION";
	public static final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	
	/* Metodos set */
	public void setIdInstitucion(Integer idInstitucion) {this.idInstitucion=idInstitucion;}
	public void setIdPoblacion(String idPoblacion) {this.idPoblacion=idPoblacion;}
	public void setUsuMod(Integer usuMod) {this.usuMod=usuMod;}
	public void setFechaMod(String fechaMod) {this.fechaMod=fechaMod;}
	
	/* Metodos get */
	public Integer getIdInstitucion() {return this.idInstitucion;}
	public String getIdPoblacion() {return this.idPoblacion;}
	public Integer getUsuMod() {return this.usuMod;}
	public String getFechaMod() {return this.fechaMod;}
}
