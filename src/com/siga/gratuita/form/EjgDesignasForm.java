package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 */



public class EjgDesignasForm extends MasterForm {
	
	protected String defensaJuridica = "DEFENSAJURIDICA";
	protected String idturno = "IDTURNO";
	protected String anio = "ANIO";
	protected String numero = "NUMERO";
	protected String ejgidInstitucion = "EJGIDINSTITUCION";
	protected String ejganio = "EJGANIO";
	protected String ejgnumero = "EJGNUMERO";
	protected String ejgidTipoEJG = "EJGIDTIPOEJG";
	protected String procedimiento = "PROCEDIMIENTO";
	protected String juzgado = "JUZGADO";
	
	//Metodos set de los campos del formulario


//	/**
//	 * Almacena en la Hashtable el Campo procedimiento 
//	 * 
//	 * @param   
//	 * @return void 
//	 */
//	public void setProcedimiento	(String valor)	{
//		this.datos.put(this.procedimiento, valor);
//	}
//	/**
//	 * Almacena en la Hashtable el Campo juzgado 
//	 * 
//	 * @param   
//	 * @return void 
//	 */
//	public void setJuzgado	(String valor)	{
//		this.datos.put(this.juzgado, valor);
//	}
	
	
	/**
	 * Almacena en la Hashtable el Campo EJG_idinstitucion 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setEjgIdInstitucion	(String valor)	{
		this.datos.put(this.ejgidInstitucion, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo EJG_anio 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setEjgAnio	(String valor)	{
		this.datos.put(this.ejganio, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo EJG_idinstitucion 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setEjgNumero	(String valor)	{
		this.datos.put(this.ejgnumero, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo EJG_idTipoEJG 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setEjgIdTipoEjg	(String valor)	{
		this.datos.put(this.ejgidTipoEJG, valor);
	}
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
	public void setAnio	(String valor)	{
		this.datos.put(this.anio, valor);
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

	
	
//	/**
//	 * Recupera Hashtable el campo procedimiento 
//	 * 
//	 * @param   
//	 * @return  
//	 */
//	public String getProcedimiento	()	{
//		return (String)this.datos.get(this.procedimiento);
//	}	
//	/**
//	 * Recupera Hashtable el campo juzgado 
//	 * 
//	 * @param   
//	 * @return  
//	 */
//	public String getJuzgado	()	{
//		return (String)this.datos.get(this.juzgado);
//	}	
//	
//	
	/**
	 * Recupera Hashtable el campo ejg_idInstitucion 
	 * 
	 * @param   
	 * @return  
	 */
	public String getEjgIdInstitucion	()	{
		return (String)this.datos.get(this.ejgidInstitucion);
	}	
	/**
	 * Recupera Hashtable el campo ejg_anio 
	 * 
	 * @param   
	 * @return  
	 */
	public String getEjgAnio	()	{
		return (String)this.datos.get(this.ejganio);
	}	
	/**
	 * Recupera Hashtable el campo ejg_idTipoEJG 
	 * 
	 * @param   
	 * @return  
	 */
	public String getEjgIdTipoEjg	()	{
		return (String)this.datos.get(this.ejgidTipoEJG);
	}
	/**
	 * Recupera Hashtable el campo ejg_numero 
	 * 
	 * @param   
	 * @return  
	 */
	public String getEjgNumero	()	{
		return (String)this.datos.get(this.ejgnumero);
	}
	

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