package com.siga.gratuita.form;

/**
 * @author adrian.ayala
 */
import com.siga.general.MasterForm;

public class BusquedaIncompatibilidadesGuardiasForm extends MasterForm 
{
	////////// Atributos //////////
	//Propios del Bean
	private String idInstitucion			= "idInstitucion";
	private String idTurno					= "idTurno";
	private String idGuardia				= "idGuardia";
	private String fechaModificacion		= "fechaModificacion";
	private String usuModificacion			= "usuModificacion";
	private String idTurno_incompatible		= "idTurno_incompatible";
	private String idGuardia_incompatible	= "idGuardia_incompatible";
	private String motivos					= "motivos";
	private String diasseparacionguardias	= "diasseparacionguardias";
	
	private String incompParaAnyadir = "incompParaAnyadir";
	private String incompParaQuitar = "incompParaQuitar";
	private String soloIncompatibilidades = "soloIncompatibilidades";
	
	//Datos auxiliares
	private String nombreTurno					= "nombreTurno";
	private String nombreGuardia				= "nombreGuardia";
	private String nombreTurno_incompatible		= "nombreTurno_incompatible";
	private String nombreGuardia_incompatible	= "nombreGuardia_incompatible";
	
	//Sobre la pestanha
	private String modoPestanha = null;
	
	
	////////// GETTERS //////////
	//Propios del Bean
	public String getIdInstitucion			() {return (String) this.datos.get (this.idInstitucion);}
	public String getIdTurno				() {return (String) this.datos.get (this.idTurno);}
	public String getIdGuardia				() {return (String) this.datos.get (this.idGuardia);}
	public String getFechaModificacion		() {return (String) this.datos.get (this.fechaModificacion);}
	public String getUsuModificacion		() {return (String) this.datos.get (this.usuModificacion);}
	public String getIdTurno_incompatible	() {return (String) this.datos.get (this.idTurno_incompatible);}
	public String getIdGuardia_incompatible	() {return (String) this.datos.get (this.idGuardia_incompatible);}
	public String getMotivos				() {return (String) this.datos.get (this.motivos);}
	public String getDiasseparacionguardias	() {return (String) this.datos.get (this.diasseparacionguardias);}
	
	public String getIncompParaAnyadir		() {return (String) this.datos.get (this.incompParaAnyadir);}
	public String getIncompParaQuitar		() {return (String) this.datos.get (this.incompParaQuitar);}
	public String getSoloIncompatibilidades	() {return (String) this.datos.get (this.soloIncompatibilidades);}
	
	//Datos auxiliares
	public String getNombreTurno				() {return nombreTurno;}
	public String getNombreGuardia				() {return nombreGuardia;}
	public String getNombreTurno_incompatible	() {return nombreTurno_incompatible;}
	public String getNombreGuardia_incompatible	() {return nombreGuardia_incompatible;}
	
	/** @return Returns the modoPestanha */
	public String getModoPestanha () {return modoPestanha;}
	
	
	////////// SETTERS //////////
	//Propios del Bean
	public void setIdInstitucion			(String	valor) {this.datos.put (this.idInstitucion, valor);}
	public void setIdTurno					(String valor) {this.datos.put (this.idTurno, valor);}
	public void setIdGuardia				(String valor) {this.datos.put (this.idGuardia, valor);}
	public void setFechaModificacion		(String	valor) {this.datos.put (this.fechaModificacion, valor);}
	public void setUsuModificacion			(String	valor) {this.datos.put (this.usuModificacion, valor);}
	public void setIdTurno_incompatible		(String	valor) {this.datos.put (this.idTurno_incompatible, valor);}
	public void setIdGuardia_incompatible	(String	valor) {this.datos.put (this.idGuardia_incompatible, valor);}
	public void setMotivos					(String	valor) {this.datos.put (this.motivos, valor);}
	public void setDiasseparacionguardias	(String	valor) {this.datos.put (this.diasseparacionguardias, valor);}

	public void setIncompParaAnyadir		(String	valor) {this.datos.put (this.incompParaAnyadir, valor);}
	public void setIncompParaQuitar			(String	valor) {this.datos.put (this.incompParaQuitar, valor);}
	public void setSoloIncompatibilidades	(String	valor) {this.datos.put (this.soloIncompatibilidades, valor);}
	
	//Datos auxiliares
	public void setNombreTurno					(String valor) {this.nombreTurno = valor;}
	public void setNombreGuardia				(String valor) {this.nombreGuardia = valor;}
	public void setNombreTurno_incompatible		(String valor) {this.nombreTurno_incompatible = valor;}
	public void setNombreGuardia_incompatible	(String valor) {this.nombreGuardia_incompatible = valor;}
	
	/** @param modoPestanha The modoPestanha to set */
	public void setModoPestanha (String modoPestanha) {this.modoPestanha = modoPestanha;}

}