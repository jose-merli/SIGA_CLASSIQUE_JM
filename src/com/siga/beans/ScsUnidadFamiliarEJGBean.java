/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;

public class ScsUnidadFamiliarEJGBean extends MasterBean{
	
	/* Variables */ 
	ScsPersonaJGBean personaJG;
	ScsParentescoBean parentesco;
	ScsEejgPeticionesBean peticionEejg;
	
	private Integer idTipoEJG;
	private Integer idInstitucion;
	private Integer anio;
	private Integer numero;
	private Integer tipoGrupoLab;
	private Integer idPersona;
	private String  observaciones;
	private String  enCalidadDe;
	private Integer solicitante;
	private Double ingresosAnuales;
	private String  bienesInmuebles;	
	private Double importeBienesInmuebles;
	private String  bienesMuebles;	
	private Double importeBienesMuebles;
	private String  otrosBienes;	
	private Double importeOtrosBienes;
	private String  descripcionIngresosAnuales;
	private Integer idParentesco;
	private Integer tipoIngreso;
		
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_UNIDADFAMILIAREJG";
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOEJG 				= 					"IDTIPOEJG";	 
	static public final String  C_OBSERVACIONES				= 					"OBSERVACIONES";
	static public final String  C_IDINSTITUCION				= 					"IDINSTITUCION";
	static public final String  C_ANIO						= 					"ANIO";
	static public final String  C_NUMERO					= 					"NUMERO";
	static public final String  C_IDPERSONA					= 					"IDPERSONA";
	static public final String  C_ENCALIDADDE				= 					"ENCALIDADDE";
	static public final String  C_SOLICITANTE				= 					"SOLICITANTE";
	static public final String  C_IMPORTEINGRESOSANUALES			= 					"IMPORTEINGRESOSANUALES";
	static public final String  C_BIENESINMUEBLES			= 					"BIENESINMUEBLES";
	static public final String  C_IMPORTEBIENESINMUEBLES	= 					"IMPORTEBIENESINMUEBLES";
	static public final String  C_BIENESMUEBLES				=					"BIENESMUEBLES";
	static public final String  C_IMPORTEBIENESMUEBLES		= 					"IMPORTEBIENESMUEBLES";
	static public final String  C_OTROSBIENES				= 					"OTROSBIENES";
	static public final String  C_IMPORTEOTROSBIENES		= 					"IMPORTEOTROSBIENES";
	static public final String  C_DESCRIPCIONINGRESOSANUALES= 					"DESCRIPCIONINGRESOSANUALES";
	static public final String  C_TIPOGRUPOLAB= 	             				"IDTIPOGRUPOLAB";
	static public final String  C_IDPARENTESCO= 	             				"IDPARENTESCO";
	static public final String  C_TIPOINGRESO= 	             				"IDTIPOINGRESO";
	
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el identificador de un EJG
	 * 
	 * @param valor Identificador EJG. 
	 * @return void 
	 */
	public void setIdTipoEJG				(Integer valor)	{ this.idTipoEJG = valor;}

	/**
	 * Almacena en el Bean la institución
	 * 
	 * @param valor Institucion EJG. 
	 * @return void 
	 */
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion = valor;}
	
	/**
	 * Almacena en el Bean el anho de un EJG
	 * 
	 * @param valor Anho 
	 * @return void 
	 */
	public void setAnio						(Integer valor)	{ this.anio = valor;}
	
	/**
	 * Almacena en el Bean el número de un EJG
	 * 
	 * @param valor Número EJG. 
	 * @return void 
	 */
	public void setNumero					(Integer valor)	{ this.numero = valor;}
	
	
	
	/**
	 * Almacena en el Bean el identificador de una persona de la unidad familiar asociada a un EJG
	 * 
	 * @param valor Identificador persona 
	 * @return void 
	 */
	public void setIdPersona				(Integer valor)	{ this.idPersona = valor;}
	
	/**
	 * Almacena en el Bean las observaciones
	 * 
	 * @param valor Observaciones
	 * @return void 
	 */
	public void setObservaciones			(String valor)	{ this.observaciones = valor;}
	
	/**
	 * Almacena en el Bean en calidad de que está relacinada la persona con el EJG
	 * 
	 * @param Calidad en la que está relacionada con el EJG la persona  
	 * @return void 
	 */
	public void setEnCalidadDe				(String valor)	{ this.enCalidadDe = valor;}
	
	/**
	 * Almacena en el Bean el identificador del solicitante del EJG
	 * 
	 * @param Solicitante del EJG  
	 * @return void 
	 */
	public void setSolicitante				(Integer valor)	{ this.solicitante = valor;}
	
	/**
	 * Almacena en el Bean los ingresos anuales de la persona
	 * 
	 * @param Ingresos anuales 
	 * @return void 
	 */
	public void setIngresosAnuales			(Double valor)	{ this.ingresosAnuales = valor;}
	
	/**
	 * Almacena en el Bean los bienes inmuebles de la persona
	 * 
	 * @param Bienes inmuebles
	 * @return void 
	 */
	public void setBienesInmuebles			(String valor)	{ this.bienesInmuebles = valor;}
	
	/**
	 * Almacena en el Bean el importe de los bienes inmuebles de la persona
	 * 
	 * @param Imoporte bienes inmuebles
	 * @return void 
	 */
	public void setImoporteBienesInmuebles	(Double valor)	{ this.importeBienesInmuebles = valor;}
	
	/**
	 * Almacena en el Bean los bienes muebles de la persona
	 * 
	 * @param Bienes inmuebles
	 * @return void 
	 */
	public void setBienesMuebles			(String valor)	{ this.bienesMuebles = valor;}
	
	/**
	 * Almacena en el Bean el importe de los bienes muebles de la persona
	 * 
	 * @param Imoporte bienes muebles
	 * @return void 
	 */
	public void setImoporteBienesMuebles	(Double valor)	{ this.importeBienesMuebles = valor;}
	
	/**
	 * Almacena en el Bean otros bienes de la persona
	 * 
	 * @param Otros bienes
	 * @return void 
	 */
	public void setOtrosBienes			(String valor)	{ this.otrosBienes = valor;}
	
	/**
	 * Almacena en el Bean el importe de los bienes muebles de la persona
	 * 
	 * @param Imoporte bienes muebles
	 * @return void 
	 */
	public void setImporteOtrosBienes	(Double valor)	{ this.importeOtrosBienes = valor;}
	
	/**
	 * Almacena en el Bean la descripción de los ingresos anuales
	 * 
	 * @param Descripción ingresos anuales. De tipo String
	 * @return void 
	 */
	public void setDescripcionIngresosAnuales (String valor)	{ this.descripcionIngresosAnuales = valor;}
	
	/**
	 * Almacena en el Bean el identificador del tipo de ingreso del EJG
	 * 
	 * @param tipo de ingreso del EJG  
	 * @return void 
	 */
	public void setTipoIngreso				(Integer valor)	{ this.tipoIngreso = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un EJG 
	 * @return Integer. 
	 */
	public Integer getIdTipoEJG				()	{ return this.idTipoEJG;}
	
	/**
	 * Recupera del Bean las observaciones
	 * @return String
	 */
	public String getObservaciones			()	{ return this.observaciones;}

	/**
	 * Recupera del Bean la institución 
	 * @return Integer 
	 */
	public Integer getIdInstitucion			()	{ return this.idInstitucion;}
	
	/**
	 * Recupera del Bean el anho de un EJG
	 * @return Integer 
	 */
	public Integer getAnio					()	{ return this.anio;}
	
	/**
	 * Recupera del Bean el número de un EJG 
	 * @return Integer
	 */
	public Integer getNumero				()	{ return this.numero;}
	
	/**
	 * Recupera del Bean el identificador de una persona de la unidad familiar asociada a un EJG
	 * @return Integer 
	 */
	public Integer getIdPersona				()	{ return this.idPersona;}
			
	/**
	 * Recupera del Bean en calidad de que está relacinada la persona con el EJG 
	 * @return String 
	 */
	public String getEnCalidadDe			()	{ return this.enCalidadDe;}
	
	/**
	 * Recupera del Bean el identificador del solicitante del EJG  
	 * @return Integer 
	 */
	public Integer getSolicitante			()	{ return this.solicitante;}
	
	/**
	 * Recupera del Bean los ingresos anuales de la persona
	 * @return Integer 
	 */
	public Double getIngresosAnuales		()	{ return this.ingresosAnuales;}
	
	/**
	 * Recupera del Bean los bienes inmuebles de la persona
	 * @return String 
	 */
	public String getBienesInmuebles		()	{ return this.bienesInmuebles;}
	
	/**
	 * Recupera del Bean el importe de los bienes inmuebles de la persona
	 * @return Double
	 */
	public Double getImoporteBienesInmuebles	()	{ return this.importeBienesInmuebles;}
	
	/**
	 * Recupera del Bean los bienes muebles de la persona
	 * @return String
	 */
	public String getBienesMuebles			()	{ return this.bienesMuebles;}
	
	/**
	 * Recupera del Bean el importe de los bienes muebles de la persona
	 * @return Double 
	 */
	public Double getImoporteBienesMuebles	()	{ return this.importeBienesMuebles;}
	
	/**
	 * Recupera del Bean otros bienes de la persona
	 * @return String 
	 */
	public String getOtrosBienes			()	{ return this.otrosBienes;}
	
	/**
	 * Recupera del Bean el importe de los bienes muebles de la persona
	 * @return Double 
	 */
	public Double getImporteOtrosBienes	()	{ return this.importeOtrosBienes;}
	/**
	 * Recupera del Bean la descripción de los ingresos anuales
	 * @return String
	 */
	public String getDescripcionIngresosAnuales ()	{ return this.descripcionIngresosAnuales;}
	
	
	/**
	 * @return Returns the tipoGrupoLab.
	 */
	public Integer getTipoGrupoLab() {
		return tipoGrupoLab;
	}
	/**
	 * @param tipoGrupoLab The tipoGrupoLab to set.
	 */
	public void setTipoGrupoLab(Integer tipoGrupoLab) {
		this.tipoGrupoLab = tipoGrupoLab;
	}
	/**
	 * @return Returns the idParentesco.
	 */
	public Integer getIdParentesco() {
		return idParentesco;
	}
	/**
	 * @param idParentesco The idParentesco to set.
	 */
	public void setIdParentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}
	
	/**
	 * Recupera del Bean el identificador del tipo de ingreso del EJG  
	 * @return Integer 
	 */
	public Integer getTipoIngreso			()	{ return this.tipoIngreso;}

	public ScsPersonaJGBean getPersonaJG() {
		return personaJG;
	}

	public void setPersonaJG(ScsPersonaJGBean personaJG) {
		this.personaJG = personaJG;
	}

	public ScsParentescoBean getParentesco() {
		return parentesco;
	}

	public void setParentesco(ScsParentescoBean parentesco) {
		this.parentesco = parentesco;
	}
	public DefinirUnidadFamiliarEJGForm getUnidadFamiliarEjgForm() throws ClsExceptions {
		DefinirUnidadFamiliarEJGForm unidadFamiliarForm = new DefinirUnidadFamiliarEJGForm();
		unidadFamiliarForm.setIdInstitucion(idInstitucion.toString());
		unidadFamiliarForm.setAnio(anio.toString());
		unidadFamiliarForm.setIdTipoEJG(idTipoEJG.toString());
		unidadFamiliarForm.setNumero(numero.toString());
		unidadFamiliarForm.setIdPersona(idPersona.toString());
		unidadFamiliarForm.setPersonaJG(personaJG);
		unidadFamiliarForm.setSolicitante(solicitante.toString());
		unidadFamiliarForm.setPeticionEejg(peticionEejg);
		if(bienesMuebles!=null && !bienesMuebles.equals(""))
			unidadFamiliarForm.setBienesMuebles(bienesMuebles);
		if(bienesInmuebles!=null && !bienesInmuebles.equals(""))
			unidadFamiliarForm.setBienesInmuebles(bienesInmuebles);
		if(otrosBienes!=null && !otrosBienes.equals(""))
			unidadFamiliarForm.setOtrosBienes(otrosBienes);
		if(descripcionIngresosAnuales!=null && !descripcionIngresosAnuales.equals(""))
			unidadFamiliarForm.setDescripcionIngresosAnuales(descripcionIngresosAnuales);
		
		if(importeBienesInmuebles!=null )
			unidadFamiliarForm.setImporteBienesInmuebles(UtilidadesString.formatoImporte(importeBienesInmuebles));
		else
			unidadFamiliarForm.setImporteBienesInmuebles("");
		if(importeBienesMuebles!=null)
			unidadFamiliarForm.setImporteBienesMuebles(UtilidadesString.formatoImporte(importeBienesMuebles));
		else
			unidadFamiliarForm.setImporteBienesMuebles("");
		if(importeOtrosBienes!=null)
			unidadFamiliarForm.setImporteOtrosBienes(UtilidadesString.formatoImporte(importeOtrosBienes));
		else
			unidadFamiliarForm.setImporteOtrosBienes("");
			
		if(ingresosAnuales!=null)
			unidadFamiliarForm.setImporteIngresosAnuales(UtilidadesString.formatoImporte(ingresosAnuales));
		else
			unidadFamiliarForm.setImporteIngresosAnuales("");
		
		if(enCalidadDe!=null && !enCalidadDe.equals(""))
			unidadFamiliarForm.setEnCalidadDe(enCalidadDe);
		if(parentesco!=null ){
			if(parentesco.getIdParentesco()!=null)
				unidadFamiliarForm.setIdParentesco(parentesco.getIdParentesco().toString());
			unidadFamiliarForm.setParentesco(parentesco);
			
		}
		
		if(observaciones!=null && !observaciones.equals(""))
			unidadFamiliarForm.setObservaciones(observaciones);
		
		if(tipoGrupoLab!=null)
			unidadFamiliarForm.setIdTipoGrupoLab(tipoGrupoLab.toString());
		
		if(tipoIngreso!=null)
			unidadFamiliarForm.setIdTipoIngreso(tipoIngreso.toString());
		
		return unidadFamiliarForm;
	}

	public ScsEejgPeticionesBean getPeticionEejg() {
		return peticionEejg;
	}

	public void setPeticionEejg(ScsEejgPeticionesBean peticionEejg) {
		this.peticionEejg = peticionEejg;
	}

	
	
	
	
	
	
	
}