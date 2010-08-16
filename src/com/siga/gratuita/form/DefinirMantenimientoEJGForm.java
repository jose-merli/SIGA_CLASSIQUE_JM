/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_EJG
*/
 public class DefinirMantenimientoEJGForm extends MasterForm
 {
	String numeroDilegencia = "";
	String numeroProcedimiento = "";
	String comisaria = "";
	String juzgado = ""; 
	String designa_anio= "";
	String designa_numero = "";
	String designa_turno = "";
	String designa_idInstitucion = "";
	String idTurnoEJG="";
	String idGuardiaEJG="";
	String pretension="";
	String idOrigenCAJG="";
	String idPreceptivo="";
	String idSituacion="" ;
	String idRenuncia="";

	

	public void setPretension(String pretension) {
		this.pretension = pretension;
	}
	
	public String getIdOrigenCAJG() {
		return idOrigenCAJG;
	}
	public void setIdOrigenCAJG(String idOrigenCAJG) {
		this.idOrigenCAJG = idOrigenCAJG;
	}
	
	public String getIdPreceptivo() {
		return idPreceptivo;
	}
	public void setIdPreceptivo(String idPreceptivo) {
		this.idPreceptivo = idPreceptivo;
	}

	public String getIdSituacion() {
		return idSituacion;
	}
	public void setIdSituacion(String idSituacion) {
		this.idSituacion = idSituacion;
	}

	/*
	 * Metodos SET
	 */
	
	/**
	 * Almacena en la Hashtable el idrenuncia del EJG 
	 * 
	 * @param valor idrenuncia de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	
		public void setidRenuncia(String idRenuncia) {
		this.idRenuncia = idRenuncia;
	}
		
		
	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio 					(String anio)					{ this.datos.put(ScsEJGBean.C_ANIO, anio);									}
	
	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnioCAJG 					(String anioCAJ)					{ this.datos.put(ScsEJGBean.C_ANIO_CAJG, anioCAJ);									}
	/**
	 * Almacena en la Hashtable el numero_CAJ del EJG 
	 * 
	 * @param valor Numero de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumeroCAJG 					(String numeroCAJ)					{ this.datos.put(ScsEJGBean.C_NUMERO_CAJG, numeroCAJ);								}
	
	/**
	 * Almacena en la Hashtable el numero del EJG 
	 * 
	 * @param valor Numero de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumero 					(String numero)					{ this.datos.put(ScsEJGBean.C_NUMERO, numero);								}
	
	/**
	 * Almacena en la Hashtable el identificador de la institucion del dictamen del EJG 
	 * 
	 * @param valor Identificador de la institucion del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdInstitucion			(String idInstitucion)			{ this.datos.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);		}
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del tipo de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJG				(String idTipoEJG)				{ this.datos.put(ScsEJGBean.C_IDTIPOEJG, idTipoEJG);				}
	public void setFechaAperturaEJG			(String fechaApertura)				{ this.datos.put(ScsEJGBean.C_FECHAAPERTURA, fechaApertura);				}
	public void setFechaProc			(String fechaProc)				{ UtilidadesHash.set(this.datos,ScsEJGBean.C_FECHADESIGPROC, fechaProc);				}
	
	/**
	 * Almacena en la Hashtable la fecha limite de presentacion del EJG 
	 * 
	 * @param valor Fecha limite de presentacion de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setFechaLimitePresentacion	(String  fechaLimitePresentacion)	{ this.datos.put(ScsEJGBean.C_FECHALIMITEPRESENTACION, fechaLimitePresentacion);}
		
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG de un colegio 
	 * 
	 * @param valor Identificador del tipo de EJG de un colegio. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJGColegio			(String tipoEJGColegio)			{ this.datos.put(ScsEJGBean.C_IDTIPOEJGCOLEGIO, tipoEJGColegio);	}
	
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG de un colegio 
	 * 
	 * @param valor Identificador del tipo de EJG de un colegio. De tipo "String". 
	 * @return void 
	 */
	public void setFechaPresentacion		(String fechaPresentacion)		{ this.datos.put(ScsEJGBean.C_FECHAPRESENTACION, fechaPresentacion);}
	
	/**
	 * Almacena en la Hashtable si es necesario procurador o no 
	 * 
	 * @param valor Si es necesario procurador o no 
	 * @return void 
	 */
	public void setProcuradorNecesario		(String procuradorNecesario)	{ this.datos.put(ScsEJGBean.C_PROCURADORNECESARIO, procuradorNecesario);}
	
	/**
	 * Almacena en la Hashtable el nombre del procurador 
	 * 
	 * @param valor Nombre del procurador. De tipo "String". 
	 * @return void 
	 */
	public void setProcurador				(String procurador)				{ this.datos.put(ScsEJGBean.C_PROCURADOR, procurador);}
	
	/**
	 * Almacena en la Hashtable las observaciones del EJG 
	 * 
	 * @param valor Observaciones de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones			(String  observaciones)				{ this.datos.put(ScsEJGBean.C_OBSERVACIONES, observaciones);}
	/**
	 * Almacena en la Hashtable los delitos del EJG 
	 * 
	 * @param valor Delitos del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setDelitos					(String  delitos)					{ this.datos.put(ScsEJGBean.C_DELITOS, delitos);}	

	public void setCalidad					(String  valor)					{ this.datos.put(ScsEJGBean.C_CALIDAD, valor);}	

	public void setIdTurnoEJG               (String idTurnoEJG)             { this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDTURNO, idTurnoEJG);	}
	/**
	 * @param idGuardiaEJG The idGuardiaEJG to set.
	 */
	public void setIdGuardiaEJG             (String idGuardiaEJG)           { this.datos.put(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA, idGuardiaEJG);}
	public void setTipoLetrado             (String tipoLetrado)           { this.datos.put(ScsEJGBean.C_TIPOLETRADO, tipoLetrado);}
	
	/*
	 * Metodos GET*/
	
	/**
	 * Recupera de la Hashtable el idPretenciones de la EJG
	 * 
	 * @return idPretenciones de la EJG
	 */
	public String getPretension() {
		return pretension;
	}
	
	/**
	 * Recupera de la Hashtable el idrenuncia de la EJG
	 * 
	 * @return idrenuncia de la EJG
	 */
	public String getidRenuncia() {
		return idRenuncia;
	}
	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio 					()		{ return this.datos.get(ScsEJGBean.C_ANIO).toString();}
	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * @return Numero de la EJG
	 */
	public String getNumero 				()		{ return this.datos.get(ScsEJGBean.C_NUMERO).toString();}
	
	/**
	 * Recupera de la Hashtable el anho_CAJ de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnioCAJG 					()		{ return this.datos.get(ScsEJGBean.C_ANIO_CAJG).toString();}
	/**
	 * Recupera de la Hashtable el numero_CAJ de la EJG
	 * @return Numero de la EJG
	 */
	public String getNumeroCAJG 				()		{ return this.datos.get(ScsEJGBean.C_NUMERO_CAJG).toString();}
	/**
	 * Recupera de la Hashtable el identificador de la institucion de la EJG
	 * @return Identificador de la institucion  de la EJG
	 */
	public String getIdInstitucion			()		{ return this.datos.get(ScsEJGBean.C_IDINSTITUCION).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG				()		{ return this.datos.get(ScsEJGBean.C_IDTIPOEJG).toString();}	

	/**
	 * Recupera de la Hashtable el identificador del tipo de EJG de un colegio 
	 * @return void 
	 */
	public String getIdTipoEJGColegio			()		{ return this.datos.get(ScsEJGBean.C_IDTIPOEJGCOLEGIO).toString();	}
	
	/**
	 * Recupera de la Hashtable el campo ProcuradorNecesario 
	 * @return void 
	 */
	public String getProcuradorNecesario	()		{ return this.datos.get(ScsEJGBean.C_PROCURADORNECESARIO).toString();	}
	/**
	 * Almacena en la Hashtable el nombre del procurador 
	 * @return String 
	 */
	public String getProcurador				()		{ return this.datos.get(ScsEJGBean.C_PROCURADOR).toString();}
	/**
	 * Recupera de la Hashtable observaciones de la EJG 
	 * @return Observaciones de la EJG
	 */
	public String getObservaciones			()		{ return this.datos.get(ScsEJGBean.C_OBSERVACIONES).toString();}
	/**
	 * Recupera de la Hashtable los delitos de la EJG 
	 * @return Delitos de la EJG
	 */
	public String getDelitos				()		{ return this.datos.get(ScsEJGBean.C_DELITOS).toString();}
	
	public String getCalidad				()		{ return this.datos.get(ScsEJGBean.C_CALIDAD).toString();}
	
	/**
	 * Recupera de la Hashtable la fecha limite de presentacion de la EJG
	 * 
	 * @return Fecha limite de presentacion  de la EJG
	 */
	public String getFechaLimitePresentacion()		{ return this.datos.get(ScsEJGBean.C_FECHALIMITEPRESENTACION).toString();}
	/**
	 * Recupera de la Hashtable el dictamen de la EJG
	 * 
	 * @return Dictamen de la EJG
	 */
	public String getFechaPresentacion			()	{ return this.datos.get(ScsEJGBean.C_FECHAPRESENTACION).toString();}
	public String getFechaProc() 	    { return UtilidadesHash.getString(this.datos,ScsEJGBean.C_FECHADESIGPROC);}
	

	public String getComisaria() {
		return comisaria;
	}
	public void setComisaria(String comisaria) {
		this.comisaria = comisaria;
	}
	public String getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}
	public String getNumeroDilegencia() {
		return numeroDilegencia;
	}
	public void setNumeroDilegencia(String numeroDilegencia) {
		this.numeroDilegencia = numeroDilegencia;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public String getDesigna_anio() {
		return designa_anio;
	}
	public void setDesigna_anio(String designa_anio) {
		this.designa_anio = designa_anio;
	}
	public String getDesigna_idInstitucion() {
		return designa_idInstitucion;
	}
	public void setDesigna_idInstitucion(String designa_idInstitucion) {
		this.designa_idInstitucion = designa_idInstitucion;
	}
	public String getDesigna_numero() {
		return designa_numero;
	}
	public void setDesigna_numero(String designa_numero) {
		this.designa_numero = designa_numero;
	}
	public String getDesigna_turno() {
		return designa_turno;
	}
	public void setDesigna_turno(String designa_turno) {
		this.designa_turno = designa_turno;
	}
	
	/**
	 * @return Returns the idGuardiaEJG.
	 */

	public String getIdGuardiaEJG				()		{ return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA).toString();}
	public String getIdTurnoEJG     			()	    { return this.datos.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO).toString();}
	public String getTipoLetrado     			()	    { return this.datos.get(ScsEJGBean.C_TIPOLETRADO).toString();}
	public String getFechaAperturaEJG     			()	    { return this.datos.get(ScsEJGBean.C_FECHAAPERTURA).toString();}
	
	
}