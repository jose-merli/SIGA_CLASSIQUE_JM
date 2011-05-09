package com.siga.beans;

import com.atos.utils.GstDate;
import com.siga.gratuita.form.InscripcionTGForm;


/**
 * Implementa las operaciones sobre el bean de la tabla SCS_INSCRIPCIONGUARDIA
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */
public class ScsInscripcionGuardiaBean extends MasterBean
{
	/**
	 *  Nombre de Tabla
	 */
	static public String T_NOMBRETABLA = "SCS_INSCRIPCIONGUARDIA";
	
	
	// Nombre de campos de la tabla
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDTURNO					= "IDTURNO";
	static public final String C_IDGUARDIA					= "IDGUARDIA";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_FECHASUSCRIPCION			= "FECHASUSCRIPCION";
	
	static public final String C_FECHAVALIDACION			= "FECHAVALIDACION";
	static public final String C_FECHASOLICITUDBAJA			= "FECHASOLICITUDBAJA";
	static public final String C_FECHABAJA					= "FECHABAJA";
	static public final String C_FECHADENEGACION			= "FECHADENEGACION";
	
	static public final String C_OBSERVACIONESSUSCRIPCION	= "OBSERVACIONESSUSCRIPCION";
	static public final String C_OBSERVACIONESVALIDACION	= "OBSERVACIONESVALIDACION";
	static public final String C_OBSERVACIONESBAJA			= "OBSERVACIONESBAJA";
	static public final String C_OBSERVACIONESDENEGACION	= "OBSERVACIONESDENEGACION";
	static public final String C_OBSERVACIONESVALBAJA		= "OBSERVACIONESVALBAJA";
	
	// Campos calculados
	static public final String C_IDGRUPOGUARDIACOLEGIADO	= "IDGRUPOGUARDIACOLEGIADO";
	static public final String C_GRUPO						= "GRUPO";
	static public final String C_ORDENGRUPO					= "ORDENGRUPO";
	
	
	// Atributos
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Long 	idPersona;
	private String	fechaSuscripcion;
	
	private String	fechaValidacion;
	private String	fechaSolicitudBaja;
	private String	fechaBaja;
	private String	fechaDenegacion;
	
	private String  observacionesSuscripcion;
	private String	observacionesValidacion;
	private String  observacionesBaja;
	private String	observacionesValBaja;
	private String  observacionesDenegacion;
	
	// Atributos calculados
	private String	estado;
	private String	fechaValorAlta;
	private String	fechaValorBaja;
	private Long 	idGrupoGuardiaColegiado;
	private Integer	grupo;
	private Integer	ordenGrupo;
	
	// Otros atributos
	ScsTurnoBean turno;
	ScsGuardiasTurnoBean guardia;
	CenPersonaBean persona;
	String numeroGrupo;
	
	
	/**
	 * Constructor
	 */
	public ScsInscripcionGuardiaBean() {
		
	}
	public ScsInscripcionGuardiaBean(Integer idInstitucion, Integer idTurno, Integer idGuardia, Long idPersona,
			String fechaSuscripcion, Long idGrupoGuardiaColegiado)
	{
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idPersona = idPersona;
		this.fechaSuscripcion = fechaSuscripcion;
		this.idGrupoGuardiaColegiado = idGrupoGuardiaColegiado;
	}
	
	
	// Setters
	public void setIdInstitucion 			(Integer valor)	{this.idInstitucion				= valor;}
	public void setIdTurno					(Integer valor)	{this.idTurno					= valor;}
	public void setIdGuardia				(Integer valor)	{this.idGuardia					= valor;}
	public void setIdPersona	 			(Long valor)	{this.idPersona					= valor;}
	public void setFechaSuscripcion			(String valor)	{this.fechaSuscripcion			= valor;}
	
	public void setFechaValidacion			(String valor)	{this.fechaValidacion			= valor;}
	public void setFechaSolicitudBaja		(String valor)	{this.fechaSolicitudBaja		= valor;}
	public void setFechaBaja				(String valor)	{this.fechaBaja					= valor;}
	public void setFechaDenegacion			(String valor)	{this.fechaDenegacion			= valor;}
	
	public void setObservacionesSuscripcion	(String valor)	{this.observacionesSuscripcion	= valor;}
	public void setObservacionesValidacion	(String valor)	{this.observacionesValidacion	= valor;}
	public void setObservacionesBaja		(String valor)	{this.observacionesBaja			= valor;}
	public void setObservacionesValBaja		(String valor)	{this.observacionesValBaja		= valor;}
	public void setObservacionesDenegacion	(String valor)	{this.observacionesDenegacion	= valor;}
	
	public void setEstado					(String valor)	{this.estado					= valor;}
	public void setFechaValorAlta			(String valor)	{this.fechaValorAlta			= valor;}
	public void setFechaValorBaja			(String valor)	{this.fechaValorBaja			= valor;}
	public void setIdGrupoGuardiaColegiado	(Long valor)	{this.idGrupoGuardiaColegiado	= valor;}
	public void setGrupo					(Integer valor)	{this.grupo						= valor;}
	public void setOrdenGrupo				(Integer valor)	{this.ordenGrupo				= valor;}
	
	public void setTurno	(ScsTurnoBean valor)			{this.turno		= valor;}
	public void setGuardia	(ScsGuardiasTurnoBean valor)	{this.guardia	= valor;}
	public void setPersona	(CenPersonaBean valor)			{this.persona	= valor;}
	public void setNumeroGrupo	(String valor)			{this.numeroGrupo	= valor;}
	
	
	// Getters
	public Integer getIdInstitucion 			() {return this.idInstitucion;}
	public Integer getIdTurno					() {return this.idTurno;}
	public Integer getIdGuardia					() {return this.idGuardia;}
	public Long getIdPersona		 			() {return this.idPersona;}
	public String getFechaSuscripcion			() {return this.fechaSuscripcion;}
	
	public String getFechaValidacion			() {return this.fechaValidacion;}
	public String getFechaSolicitudBaja			() {return this.fechaSolicitudBaja;}
	public String getFechaBaja					() {return this.fechaBaja;}
	public String getFechaDenegacion			() {return this.fechaDenegacion;}
	
	public String getObservacionesSuscripcion	() {return this.observacionesSuscripcion;}
	public String getObservacionesValidacion	() {return this.observacionesValidacion;}
	public String getObservacionesBaja			() {return this.observacionesBaja;}
	public String getObservacionesValBaja		() {return this.observacionesValBaja;}
	public String getObservacionesDenegacion	() {return this.observacionesDenegacion;}
	
	public String getEstado						() {return this.estado;}
	public String getFechaValorAlta				() {return this.fechaValorAlta;}
	public String getFechaValorBaja				() {return this.fechaValorBaja;}
	public Long getIdGrupoGuardiaColegiado		() {return this.idGrupoGuardiaColegiado;}
	public Integer getGrupo						() {return this.grupo;}
	public Integer getOrdenGrupo				() {return this.ordenGrupo;}
	
	public ScsTurnoBean getTurno			() {return turno;}
	public ScsGuardiasTurnoBean getGuardia	() {return guardia;}
	public CenPersonaBean getPersona		() {return persona;}
	public String getNumeroGrupo() {return numeroGrupo;}

	
	// Otros metodos
	public InscripcionTGForm getInscripcion()
	{
		InscripcionTGForm inscripcion = new InscripcionTGForm();

		inscripcion.setIdTurno(idTurno.toString());
		inscripcion.setIdPersona(idPersona.toString());
		inscripcion.setIdInstitucion(idInstitucion.toString());
		inscripcion.setIdGuardia(idGuardia.toString());
		StringBuffer nombre = new StringBuffer();
		nombre.append(persona.getNombre());
		nombre.append(" ");
		nombre.append(persona.getApellido1());
		if (persona.getApellido2() != null) {
			nombre.append(" ");
			nombre.append(persona.getApellido2());
		}
		if (turno != null && turno.getGuardias() != null)
			inscripcion.setTipoGuardias(turno.getGuardias().toString());
		if (turno != null && turno.getValidarInscripciones() != null)
			inscripcion.setValidarInscripciones(turno.getValidarInscripciones());
		inscripcion.setColegiadoNombre(nombre.toString());
		inscripcion.setColegiadoNumero(persona.getColegiado().getNColegiado());
		inscripcion.setObservacionesSolicitud(observacionesSuscripcion);
		try {
			inscripcion.setFechaSolicitud(GstDate.getFormatedDateShort("", fechaSuscripcion));
			inscripcion.setFechaValidacion(GstDate.getFormatedDateShort("", fechaValidacion));
			inscripcion.setFechaSolicitudBaja(GstDate.getFormatedDateShort("", fechaSolicitudBaja));
			inscripcion.setFechaDenegacion(GstDate.getFormatedDateShort("", fechaDenegacion));
			inscripcion.setFechaBaja(GstDate.getFormatedDateShort("", fechaBaja));
			inscripcion.setFechaValorAlta(fechaValorAlta);
			inscripcion.setFechaValorBaja(fechaValorBaja);
		} catch (Exception e) {
		}
		inscripcion.setObservacionesValidacion(observacionesValidacion);
		inscripcion.setObservacionesDenegacion(observacionesDenegacion);
		inscripcion.setObservacionesBaja(observacionesBaja);
		inscripcion.setObservacionesValBaja(observacionesValBaja);
		inscripcion.setEstado(estado);
		return inscripcion;
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
	public boolean equals(ScsInscripcionGuardiaBean bean) {
		boolean result = true;
		
		result &= idInstitucion.intValue() == bean.getIdInstitucion().intValue();
		result &= idTurno.intValue() == bean.getIdTurno().intValue();
		result &= idGuardia.intValue() == bean.getIdGuardia().intValue();
		result &= idPersona.longValue() == bean.getIdPersona().longValue();
		result &= fechaSuscripcion.equalsIgnoreCase(bean.getFechaSuscripcion());
		//Miramos si la guardia es de grupos
		if(bean.getIdGrupoGuardiaColegiado() != null){
			result &= idGrupoGuardiaColegiado.longValue() == bean.getIdGrupoGuardiaColegiado().longValue();
		}
		
		return result;
	}
	
}