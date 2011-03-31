package com.siga.beans;

import com.atos.utils.GstDate;
import com.siga.gratuita.form.InscripcionTGForm;
import com.siga.tlds.FilaExtElement;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_INSCRIPCIONTURNO
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */



public class ScsInscripcionTurnoBean extends MasterBean{
	
	/* Variables */ 
	
	private Long 	idPersona;
	private Integer idInstitucion;
	private Integer idTurno;
	private String	fechaSolicitud;
	private String	fechaValidacion;
	private String	fechaSolicitudBaja;
	private String	fechaBaja;
	private String  observacionesSolicitud;
	private String  observacionesValidacion;
	private String  observacionesBaja;
	private String	fechaDenegacion;
	private String  observacionesDenegacion;
	InscripcionTGForm inscripcion;
	
	ScsTurnoBean turno;
	CenPersonaBean persona;
	String estado = null;
	private String	fechaValorAlta;
	private String	fechaValorBaja;
	private String  observacionesValBaja;
	
	/* Nombre de Tabla*/
	
	
	static public String T_NOMBRETABLA = "SCS_INSCRIPCIONTURNO";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDTURNO = 				"IDTURNO";
	static public final String 	C_FECHASOLICITUD = 			"FECHASOLICITUD";
	static public final String 	C_FECHAVALIDACION = 		"FECHAVALIDACION";
	static public final String 	C_FECHABAJA = 				"FECHABAJA";
	static public final String 	C_FECHASOLICITUDBAJA =		"FECHASOLICITUDBAJA";
	static public final String 	C_OBSERVACIONESSOLICITUD = 	"OBSERVACIONESSOLICITUD";
	static public final String 	C_OBSERVACIONESVALIDACION = "OBSERVACIONESVALIDACION";
	static public final String 	C_OBSERVACIONESBAJA = 		"OBSERVACIONESBAJA";
	static public final String 	C_OBSERVACIONESDENEGACION = "OBSERVACIONESDENEGACION";
	static public final String 	C_FECHADENEGACION	=	"FECHADENEGACION";
	static public final String 	C_OBSERVACIONESVALBAJA =		"OBSERVACIONESVALBAJA";
	
	
	/*Metodos SET*/
	
	public ScsInscripcionTurnoBean() {

	}
	
	public ScsInscripcionTurnoBean(int idInstitucion, int idTurno, Long idPersonaUltimo, String fechaSolicitud) {
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idPersona = idPersonaUltimo;
		this.fechaSolicitud = fechaSolicitud;

	}
	/**
	 * Almacena en el Bean el identificador de persona 
	 * 
	 * @param valor Identificador de la persona apuntada al turno. De tipo "Long". 
	 * @return void 
	 */
	public void setIdPersona	 			(Long valor) 	{ this.idPersona = valor;}
	/**
	 * Almacena en el Bean el identificador de la institucion 
	 * 
	 * @param valor Identificador de la institucion a la que pertenece el turno. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = valor;}
	/**
	 * Almacena en el Bean el identificador del turno
	 * 
	 * @param valor Identificador del turno. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)		{ this.idTurno = valor;}
	/**
	 * Almacena en el Bean la fecha de solicitud
	 * 
	 * @param valor Fecha de solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaSolicitud			(String valor)	{ this.fechaSolicitud = valor;}
	/**
	 * Almacena en el Bean la fecha de validacion
	 * 
	 * @param valor Fecha de validacion de la solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaValidacion			(String valor)	{ this.fechaValidacion = valor;}
	/**
	 * Almacena en el Bean la fecha de baja
	 * 
	 * @param valor Fecha de validacion de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaBaja				(String valor)	{ this.fechaBaja = valor;}
	/**
	 * Almacena en el Bean la fecha de solicitud de la baja
	 * 
	 * @param valor Fecha de solicitud de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaSolicitudBaja				(String valor)	{ this.fechaSolicitudBaja = valor;}
	/**
	 * Almacena en el Bean las observaciones de solicitud
	 * 
	 * @param valor Observaciones de solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesSolicitud	(String valor)	{ this.observacionesSolicitud = valor;}
	/**
	 * Almacena en el Bean las observaciones de validacion
	 * 
	 * @param valor Observaciones de validacion de la solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesValidacion	(String valor)	{ this.observacionesValidacion = valor;}
	/**
	 * Almacena en el Bean las observaciones de baja
	 * 
	 * @param valor Observaciones de validacion de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesBaja		(String valor)	{ this.observacionesBaja = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la persona
	 * 
	 * @return Identificador de la persona
	 */
	public Long getIdPersona		 			() 	{ return this.idPersona;}
	/**
	 * Recupera del Bean el identificador de la institucion
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion 			() 	{ return this.idInstitucion;}
	/**
	 * Recupera del Bean el identificador del turno
	 * 
	 * @return Identificador del turno
	 */
	public Integer getIdTurno					()	{ return this.idTurno;}
	/**
	 * Recupera del Bean la fecha de solicitud
	 * 
	 * @return Fecha de solicitud del turno
	 */
	public String getFechaSolicitud			()	{ return this.fechaSolicitud;}
	/**
	 * Recupera del Bean la fecha de validacion
	 * 
	 * @return Fecha de validacion del turno
	 */
	public String getFechaValidacion		()	{ return this.fechaValidacion;}
	/**
	 * Recupera del Bean la fecha de baja
	 * 
	 * @return Fecha de solicitud baja del turno
	 */
	public String getFechaSolicitudBaja				()	{ return this.fechaSolicitudBaja;}
	/**
	 * Recupera del Bean la fecha de baja
	 * 
	 * @return Fecha de baja del turno
	 */
	public String getFechaBaja				()	{ return this.fechaBaja;}
	/**
	 * Recupera del Bean las observaciones de solicitud
	 * 
	 * @return Observaciones de solicitud del turno
	 */
	public String getObservacionesSolicitud	()	{ return this.observacionesSolicitud;}
	/**
	 * Recupera del Bean las observaciones de validacion
	 * 
	 * @return Observaciones de validacion del turno
	 */
	public String getObservacionesValidacion()	{ return this.observacionesValidacion;}
	/**
	 * Recupera del Bean las observaciones de baja
	 * 
	 * @return Observaciones de baja del turno
	 */
	public String getObservacionesBaja		()	{ return this.observacionesBaja;}
	public ScsTurnoBean getTurno() {
		return turno;
	}
	public void setTurno(ScsTurnoBean turno) {
		this.turno = turno;
	}
	public CenPersonaBean getPersona() {
		return persona;
	}
	public void setPersona(CenPersonaBean persona) {
		this.persona = persona;
	}
	
	public InscripcionTGForm getInscripcion() {
		inscripcion = new InscripcionTGForm();
		inscripcion.setIdTurno(idTurno.toString());
		inscripcion.setIdPersona(idPersona.toString());
		inscripcion.setIdInstitucion(idInstitucion.toString());
		StringBuffer nombre = new StringBuffer();
		nombre.append(persona.getNombre());
		nombre.append(" ");
		nombre.append(persona.getApellido1());
		if(persona.getApellido2()!=null){
			nombre.append(" ");
			nombre.append(persona.getApellido2());
		}
		inscripcion.setColegiadoNombre(nombre.toString());
		inscripcion.setColegiadoNumero(persona.getColegiado().getNColegiado());
		inscripcion.setObservacionesSolicitud(observacionesSolicitud);
		try {
			inscripcion.setFechaSolicitud(GstDate.getFormatedDateShort("", fechaSolicitud));
			inscripcion.setFechaValidacion(GstDate.getFormatedDateShort("", fechaValidacion));
			inscripcion.setFechaSolicitudBaja(GstDate.getFormatedDateShort("", fechaSolicitudBaja));
			inscripcion.setFechaDenegacion(GstDate.getFormatedDateShort("", fechaDenegacion));
			inscripcion.setFechaValorAlta(fechaValorAlta);
			inscripcion.setFechaValorBaja(fechaValorBaja);
			
			inscripcion.setFechaBaja(GstDate.getFormatedDateShort("", fechaBaja));	
		} catch (Exception e) {
		}
		inscripcion.setObservacionesValidacion(observacionesValidacion);
		inscripcion.setObservacionesDenegacion(observacionesDenegacion);
		inscripcion.setObservacionesBaja(observacionesBaja);
		inscripcion.setObservacionesValBaja(observacionesValBaja);
		inscripcion.setEstado(estado);
		return inscripcion;
		
		
	}
	public void setInscripcion(InscripcionTGForm inscripcion) {
		this.inscripcion = inscripcion;
		
		
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFechaDenegacion() {
		return fechaDenegacion;
	}
	public void setFechaDenegacion(String fechaDenegacion) {
		this.fechaDenegacion = fechaDenegacion;
	}
	public String getObservacionesDenegacion() {
		return observacionesDenegacion;
	}
	public void setObservacionesDenegacion(String observacionesDenegacion) {
		this.observacionesDenegacion = observacionesDenegacion;
	}
	public String getFechaValorAlta() {
		return fechaValorAlta;
	}
	public void setFechaValorAlta(String fechaValorAlta) {
		this.fechaValorAlta = fechaValorAlta;
	}
	public String getFechaValorBaja() {
		return fechaValorBaja;
	}
	public void setFechaValorBaja(String fechaValorBaja) {
		this.fechaValorBaja = fechaValorBaja;
	}
	public String getObservacionesValBaja() {
		return observacionesValBaja;
	}
	public void setObservacionesValBaja(String observacionesValBaja) {
		this.observacionesValBaja = observacionesValBaja;
	}	
	
		/**
	 * Este metodo comprueba si este bean de inscripcion corresponde a la misma inscripcion 
	 * que el bean pasado como parametro.
	 * Es decir, comprueba la igualdad de los siguientes atributos:
	 * idInstitucion, idTurno, idGuardia, idPersona, fechaSuscripcion
	 * 
	 * @param bean
	 * @return
	 */
	public boolean equals(ScsInscripcionTurnoBean bean) {
		boolean result = true;
		
		result &= idInstitucion.intValue() == bean.getIdInstitucion().intValue();
		result &= idTurno.intValue() == bean.getIdTurno().intValue();
		result &= idPersona.longValue() == bean.getIdPersona().longValue();
		result &= fechaSolicitud.equalsIgnoreCase(bean.getFechaSolicitud());
		
		return result;
	}
	
}