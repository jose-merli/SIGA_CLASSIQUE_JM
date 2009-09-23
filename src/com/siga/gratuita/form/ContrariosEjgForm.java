package com.siga.gratuita.form;


import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 * @version 07/02/2006 (david.sachezp): nuevos campos Procurador
 */



public class ContrariosEjgForm extends MasterForm {
			
	private Integer idDelito, anio, numero, idTipoEJG;
	private String idPersona;
	private String nif="nif";
	private String nombre="nombre";
	private String apellido1="apellido1";
	private String apellido2="apellido2";
	private String codigoPostal="codigoPostal";
	private String direccion="direccion";
	private String idPais="idPais";
	private String idProvincia="idProvincia";
	private String idPoblacion="idPoblacion";
	private String fechaNacimiento="fechaNacimiento";
	private String idEstadoCivil="idEstadoCivil";
	private String observaciones="observaciones";
	private String nuevo 			="nuevo";
	private String modoPestanha=null;
	
	public String getModoPestanha() {
		return modoPestanha;
	}
	/**
	 * @param modoPestanha The modoPestanha to set.
	 */
	public void setModoPestanha(String modoPestanha) {
		this.modoPestanha = modoPestanha;
	}
	/**
	 * @return Returns the idTipoEJG.
	 */
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	/**
	 * @param idTipoEJG The idTipoEJG to set.
	 */
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}
	/**
	 * @return Returns the idDelito.
	 */
	public String getIdPersona() {
		return idPersona;
	}
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public void    setNif          			 (String valor)		{ this.datos.put(this.nif, valor);}
	/**
	 * @return Returns the numero.
	 */
	public Integer getNumero() {
		return numero;
	}
	public void    setNombre                 (String	valor)	{ this.datos.put(this.nombre, valor);}
	public void    setApellido1      		 (String	valor)	{ this.datos.put(this.apellido1, valor);}
	public void    setApellido2        		 (String	valor)	{ this.datos.put(this.apellido2, valor);}
	public void    setCodigoPostal			 (String	valor)	{ this.datos.put(this.codigoPostal, valor);}
	public void    setDireccion				 (String	valor)	{ this.datos.put(this.direccion, valor);}
	public void    setIdPais				 (String	valor)	{ this.datos.put(this.idPais, valor);}
	public void    setIdProvincia			 (String	valor)	{ this.datos.put(this.idProvincia, valor);}
	public void    setIdPoblacion			 (String	valor)	{ this.datos.put(this.idPoblacion, valor);}
	public void    setFechaNacimiento		 (String	valor)	{ this.datos.put(this.fechaNacimiento, valor);}
	public void    setIdEstadoCivil			 (String	valor)	{ this.datos.put(this.idEstadoCivil, valor);}
	public void    setObservaciones          (String	valor)	{ this.datos.put(this.observaciones, valor);}
	public void    setNuevo			 		 (String	valor)	{ this.datos.put(this.nuevo, valor);}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	/**
	 * @param idDelito The idDelito to set.
	 */
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getNif          			()	{return (String)this.datos.get(this.nif);}
	public String getNombre                 ()	{return (String)this.datos.get(this.nombre);}
	public String getApellido1      		()	{return (String)this.datos.get(this.apellido1);}
	public String getApellido2        		()	{return (String)this.datos.get(this.apellido2);}
	public String getCodigoPostal	 		()	{return (String)this.datos.get(this.codigoPostal);}
	public String getDireccion		 		()	{return (String)this.datos.get(this.direccion);}
	public String getIdPais		 			()	{return (String)this.datos.get(this.idPais);}
	public String getIdProvincia		 	()	{return (String)this.datos.get(this.idProvincia);}
	public String getIdPoblacion		 	()	{return (String)this.datos.get(this.idPoblacion);}
	public String getFechaNacimiento	 	()	{return (String)this.datos.get(this.fechaNacimiento);}
	public String getIdEstadoCivil		 	()	{return (String)this.datos.get(this.idEstadoCivil);}
	public String getObservaciones          ()	{return (String)this.datos.get(this.observaciones);}
	public String getNuevo				 	()	{return (String)this.datos.get(this.nuevo);}
}