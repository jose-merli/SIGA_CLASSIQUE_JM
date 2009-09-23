package com.siga.gratuita.form;

/**
 * @author carlos.vidal
 */
import com.siga.general.MasterForm;

public class AltaTurnosGuardiasForm extends MasterForm {
	//Campos para la tabla de turnos
	Integer IDINSTITUCION = null;
	Integer IDPERSONA = null;
	Integer IDTURNO = null;
	Integer GUARDIAS = null; //0--> todas, 1--> todas o ninguna, 2 --> elegir
	String  FECHASOLICITUD = null;
	String  OSBSERVACIONESSOLICITUD = null;
	String  FECHAVALIDACION = null;
	String  OSBSERVACIONESVALIDACION = null;
	String  FECHASOLICITUDBAJA = null;
	String  OSBSERVACIONESBAJA = null;
	String  VALIDARINSCRIPCIONES = null;
	String  FECHABAJA = null;
	String GUARDIASSEL="";
	//Otros campos
	String PASO = null;
	String RETENCION = null;
	String IDRETENCION = null;
	String telefono1 = null;
	String telefono2 = null;
	String movil = null;
	String fax1 = null;
	String fax2 = null;
	
	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion			(Integer idinstitucion)	{ IDINSTITUCION = idinstitucion;	}	
	public void setIdPersona				(Integer idpersona)		{ IDPERSONA = idpersona;	}	
	public void setIdTurno		 			(Integer idturno)	 	{ IDTURNO = idturno;	}
	public void setGuardias		 			(Integer guardias)	 	{ GUARDIAS = guardias;	}
	public void setFechaSolicitud			(String fechasolicitud) { FECHASOLICITUD = fechasolicitud;	}
	public void setObservacionesSolicitud	(String observacionessolicitud) { OSBSERVACIONESSOLICITUD = observacionessolicitud;	}
	public void setFechaValidacion			(String fechavalidacion) { FECHAVALIDACION = fechavalidacion;	}
	public void setObservacionesValidacion	(String observacionesvalidacion) { OSBSERVACIONESVALIDACION = observacionesvalidacion;	}
	public void setFechaSolicitudBaja		(String fechasolicitudbaja) { FECHASOLICITUDBAJA = fechasolicitudbaja;	}
	public void setFechaBaja		        (String fechabaja) { FECHABAJA = fechabaja;	}
	public void setObservacionesBaja		(String observacionesbaja) { OSBSERVACIONESBAJA = observacionesbaja;	}
	public void setPaso						(String paso)		{ PASO = paso;	}	
	public void setRetencion				(String retencion)		{ RETENCION = retencion;	}	
	public void setIdRetencion				(String idretencion)		{ IDRETENCION = idretencion;	}	
	public void setValidarInscripciones		(String validar)		{ VALIDARINSCRIPCIONES = validar;	}
	public void setGuardiasSel         		(String guardiasSel)		{ GUARDIASSEL = guardiasSel;	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	public Integer	  getIdInstitucion() 		{ return IDINSTITUCION; }
	public Integer 	  getIdPersona() 			{ return IDPERSONA; }	
	public Integer    getIdTurno() 				{ return IDTURNO; }	
	public Integer    getGuardias() 			{ return GUARDIAS; }	
	public String 	  getFechaSolicitud() 		{ return FECHASOLICITUD; }
	public String 	  getObservacionesSolicitud() 	{ return OSBSERVACIONESSOLICITUD; }
	public String 	  getFechaValidacion() 		{ return FECHAVALIDACION; }
	public String 	  getObservacionesValidacion() 	{ return OSBSERVACIONESVALIDACION; }
	public String 	  getFechaSolicitudBaja() 		{ return FECHASOLICITUDBAJA; }
	public String 	  getFechaBaja() 		{ return FECHABAJA; }
	public String 	  getObservacionesBaja() 	{ return OSBSERVACIONESBAJA; }
	public String     getPaso() 				{ return PASO; }	
	public String     getRetencion() 				{ return RETENCION; }	
	public String     getIdRetencion() 				{ return IDRETENCION; }	
	public String     getValidarInscripciones()		{ return VALIDARINSCRIPCIONES; }
	public String     getGuardiasSel()		{ return GUARDIASSEL; }
	public String getTelefono1() {
		return telefono1;
	}
	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}
	public String getTelefono2() {
		return telefono2;
	}
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	public String getFax1() {
		return fax1;
	}
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	
	public String getAbreviatura() 	{ 
 		return (String)this.datos.get("ABREVIATURA");
 	} 	
 	public String getNombre(){
		return (String)this.datos.get("NOMBRE");
	}
	public String getArea(){
		return (String)this.datos.get("IDAREA");
	}
	
	public String getMateria(){
		return (String)this.datos.get("IDMATERIA");
	}
	
	public String getZona(){
		return (String)this.datos.get("IDZONA");
	}
	public String getSubzona(){
		return (String)this.datos.get("IDSUBZONA");
	}
	public void setAbreviatura (String valor){ 
		this.datos.put("ABREVIATURA", valor.toUpperCase());
	}
	
	public void setNombre (String valor){ 
		this.datos.put("NOMBRE", valor.toUpperCase());
	}

	public void setArea (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDAREA", aux);
		}
		else this.datos.put("IDAREA",valor);
	}
	
	public void setMateria (String valor){ 
		this.datos.put("IDMATERIA", valor);
	}
	
	public void setZona (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDZONA", aux);
		}
		else this.datos.put("IDZONA",valor);
	}
	public void setSubzona (String valor){ 
		this.datos.put("IDSUBZONA", valor);
	}
	
}