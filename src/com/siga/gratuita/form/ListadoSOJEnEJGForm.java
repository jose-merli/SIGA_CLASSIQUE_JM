

package com.siga.gratuita.form;

import com.siga.general.MasterForm;


/**
* Maneja el formulario que mantiene la tabla 
*/
 public class ListadoSOJEnEJGForm extends MasterForm{
 	
	protected String ejgidInstitucion = "EJGIDINSTITUCION";
	protected String ejganio = "EJGANIO";
	protected String ejgnumero = "EJGNUMERO";
	protected String ejgidTipoEJG = "EJGIDTIPOEJG";
	protected String sojanio = "SOJANIO";
	protected String sojnumero = "SOJNUMERO";
	protected String sojidTipoSOJ = "SOJIDTIPOSOJ";
 	
 	
 
	
	
	/**
	 * Almacena en la Hashtable el Campo sojanio 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setSojAnio	(String valor)	{
		this.datos.put(this.sojanio, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo sojnumero 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setSojNumero	(String valor)	{
		this.datos.put(this.sojnumero, valor);
	}
	/**
	 * Almacena en la Hashtable el Campo sojidTipoSOJ 
	 * 
	 * @param   
	 * @return void 
	 */
	public void setSojIdTipoSOJ	(String valor)	{
		this.datos.put(this.sojidTipoSOJ, valor);
	}
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
	 * Recupera Hashtable el campo sojanio 
	 * 
	 * @param   
	 * @return  
	 */
	public String getSojAnio	()	{
		return (String)this.datos.get(this.sojanio);
	}	
	/**
	 * Recupera Hashtable el campo sojnumero 
	 * 
	 * @param   
	 * @return  
	 */
	public String getSojNumero	()	{
		return (String)this.datos.get(this.sojnumero);
	}	
	/**
	 * Recupera Hashtable el campo ejg_idTipoEJG 
	 * 
	 * @param   
	 * @return  
	 */
	public String getSojIdTipoSOJ	()	{
		return (String)this.datos.get(this.sojidTipoSOJ);
	}
	

}