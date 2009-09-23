package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 */



public class DefensaJuridicaDesignasForm extends MasterForm {
	
	protected String defensaJuridica= "DEFENSAJURIDICA";
	protected String idturno = "IDTURNO";
	protected String anio = "ANIO";
	protected String numero = "NUMERO";
	
	//Metodos set de los campos del formulario


	/**
	 * Almacena en la Hashtable el Campo defensa Juridica de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setDefensaJuridica	(String valor)	{
		this.datos.put(this.defensaJuridica, valor);
	}
	/**
	 * Almacena en la Hashtable el anho de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setAnio	(String anio)	{
		this.datos.put(this.anio, anio);
	}
	/**
	 * Almacena en la Hashtable el numero de la designa  
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setNumero	(String valor)	{
		this.datos.put(this.numero, valor);
	}
	/**
	 * Almacena en la Hashtable idturno de la designa 
	 * 
	 * @param Anio  
	 * @return void 
	 */
	public void setIdTurno	(String valor)	{
		this.datos.put(this.idturno, valor);
	}
	
	//	Metodos get de los campos del formulario


	/**
	 * Recupera Hashtable el campo designa juridica de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getDefensaJuridica	()	{
		return (String)this.datos.get(this.defensaJuridica);
	}
	/**
	 * Recupera Hashtable el anho de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getAnio	()	{
		return (String)this.datos.get(this.anio);
	}
	/**
	 * Recupera Hashtable el numero de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getNumero	()	{
		return (String)this.datos.get(this.numero);
	}
	/**
	 * Recupera Hashtable el idTunro de la designa 
	 * 
	 * @param Anio  
	 * @return  
	 */
	public String getIdTurno	()	{
		return (String)this.datos.get(this.idturno);
	}
	
	
}