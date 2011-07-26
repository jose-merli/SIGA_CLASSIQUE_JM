package com.siga.beans;

import java.util.Vector;

import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_CALENDARIOGUARDIAS
 * @since 06/12/2004
 */
public class ScsCalendarioGuardiasBean extends MasterBean
{
	static public final String estadoProgramado = "0";
	static public final String estadoProcesando = "1";
	static public final String estadoError = "2";
	static public final String estadoGenerado = "3";
    static public final String estadoCancelado = "4";
    static public final String estadoPteManual = "5";
	
	// Atributos
	private Integer	idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer	idCalendarioGuardias;
	private String	fechaInicio;
	private String	fechaFin;
	private String  observaciones;
	private Long    idPersonaUltimoAnterior;
	private String	fechaSuscUltimoAnterior;
	private Long	idGrupoGuardiaColegiadoAnterior;
	private Integer idTurnoPrincipal;
	private Integer idGuardiaPrincipal;
	private Integer	idCalendarioGuardiasPrincipal;	
	ScsGuardiasTurnoBean guardiaTurno;
	
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_CALENDARIOGUARDIAS";
	
	// Nombre de campos de la tabla
	static public final String C_IDINSTITUCION				        = "IDINSTITUCION";
	static public final String C_IDTURNO					        = "IDTURNO";
	static public final String C_IDGUARDIA					        = "IDGUARDIA";
	static public final String C_IDCALENDARIOGUARDIAS		        = "IDCALENDARIOGUARDIAS";
	static public final String C_FECHAINICIO				        = "FECHAINICIO";
	static public final String C_FECHAFIN					        = "FECHAFIN";
	static public final String C_OBSERVACIONES				        = "OBSERVACIONES";
	static public final String C_IDPERSONA_ULTIMOANTERIOR	        = "IDPERSONA_ULTIMOANTERIOR";
	static public final String C_IDGRUPOGUARDIACOLEGIADO_ULTIMOANTERIOR	    = "IDGRUPOGUARDIA_ULTIMOANTERIOR";
	static public final String C_FECHASUSC_ULTIMOANTERIOR	        = "FECHASUSC_ULTIMOANTERIOR";
	static public final String C_IDTURNO_PRINCIPAL  		        = "IDTURNOPRINCIPAL";
	static public final String C_IDGUARDIA_PRINCIPAL		        = "IDGUARDIAPRINCIPAL";
	static public final String C_IDCALENDARIOGUARDIAS_PRINCIPAL		= "IDCALENDARIOGUARDIASPRINCIPAL";
	
	
	/** Constructor */
	public ScsCalendarioGuardiasBean() {
		
	}
	public ScsCalendarioGuardiasBean(Integer idInstitucion, Integer idTurno, Integer idGuardia,
			Integer idCalendarioGuardias, String fechaInicio, String fechaFin, String observaciones, 
			Integer idTurnoPrincipal, Integer idGuardiaPrincipal, Integer idCalendarioGuardiasPrincipal)
	{
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idCalendarioGuardias = idCalendarioGuardias;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.observaciones = observaciones;
		this.idTurnoPrincipal = idTurnoPrincipal;
		this.idGuardiaPrincipal = idGuardiaPrincipal;
		this.idCalendarioGuardiasPrincipal = idCalendarioGuardiasPrincipal;
	}	
	
	
	// Setters
	public void setIdInstitucion			          (Integer idInstitucion)				    {this.idInstitucion				    = idInstitucion;}
	public void setIdTurno					          (Integer idTurno)					        {this.idTurno					    = idTurno;}
	public void setIdGuardia				          (Integer idGuardia)					    {this.idGuardia					    = idGuardia;}
	public void setIdCalendarioGuardias		          (Integer idCalendarioGuardias)		    {this.idCalendarioGuardias		    = idCalendarioGuardias;}
	public void setFechaInicio				          (String  fechaInicio)				        {this.fechaInicio				    = fechaInicio;}
	public void setFechaFin					          (String  fechaFin)					    {this.fechaFin					    = fechaFin;}
	public void setObservaciones			          (String  observaciones)				    {this.observaciones				    = observaciones;}
	public void setIdPersonaUltimoAnterior	          (Long    idPersonaUltimoAnterior)	        {this.idPersonaUltimoAnterior	    = idPersonaUltimoAnterior;}
	public void setFechaSuscUltimoAnterior	          (String  fechaSuscUltimoAnterior)	        {this.fechaSuscUltimoAnterior	    = fechaSuscUltimoAnterior;}
	public void setIdTurnoPrincipal                   (Integer idTurnoPrincipal)                {this.idTurnoPrincipal              = idTurnoPrincipal;}
	public void setIdGuardiaPrincipal                 (Integer idGuardiaPrincipal)              {this.idGuardiaPrincipal            = idGuardiaPrincipal;}
	public void setIdCalendarioGuardiasPrincipal      (Integer idCalendarioGuardiasPrincipal)   {this.idCalendarioGuardiasPrincipal = idCalendarioGuardiasPrincipal;}
	public void setIdGrupoGuardiaColegiadoAnterior   		  (Long idGrupoGuardiaColegiadoAnterior) 		{this.idGrupoGuardiaColegiadoAnterior 		= idGrupoGuardiaColegiadoAnterior;}


	// Getters
	public Integer getIdInstitucion				     () {return idInstitucion;}
	public Integer getIdTurno					     () {return idTurno;}
	public Integer getIdGuardia					     () {return idGuardia;}
	public Integer getIdCalendarioGuardias		     () {return idCalendarioGuardias;}
	public String  getFechaInicio				     () {return fechaInicio;}
	public String  getFechaFin					     () {return fechaFin;}
	public String  getObservaciones				     () {return observaciones;}
	public Long	   getIdPersonaUltimoAnterior	     () {return idPersonaUltimoAnterior;}
	public String  getFechaSuscUltimoAnterior	     () {return fechaSuscUltimoAnterior;}
	public Integer getIdTurnoPrincipal               () {return idTurnoPrincipal;}
	public Integer getIdGuardiaPrincipal             () {return idGuardiaPrincipal;}
	public Integer getIdCalendarioGuardiasPrincipal  () {return idCalendarioGuardiasPrincipal;}
	public Long getIdGrupoGuardiaColegiadoAnterior			 () {return idGrupoGuardiaColegiadoAnterior;	}
	
	public String toString() {
		return "Cal. " + this.idTurno + "." + this.idGuardia + "." + this.idCalendarioGuardias + "-" + this.fechaInicio + "-" + this.fechaFin + "-" + this.idTurnoPrincipal + "-"  + this.idGuardiaPrincipal + this.idCalendarioGuardiasPrincipal;
	}
	public ScsGuardiasTurnoBean getGuardiaTurno() {
		return guardiaTurno;
	}
	public void setGuardiaTurno(ScsGuardiasTurnoBean guardiaTurno) {
		this.guardiaTurno = guardiaTurno;
	}
		Vector<ScsCalendarioGuardiasBean> calendariosVinculados;


		public Vector<ScsCalendarioGuardiasBean> getCalendariosVinculados() {
			return calendariosVinculados;
		}
		public void setCalendariosVinculados(
				Vector<ScsCalendarioGuardiasBean> calendariosVinculados) {
			this.calendariosVinculados = calendariosVinculados;
		} 
	public DefinirCalendarioGuardiaForm getDefinirCalendarioGuardiaForm() {
		DefinirCalendarioGuardiaForm  definirCalendarioGuardiaForm = new DefinirCalendarioGuardiaForm();
		definirCalendarioGuardiaForm.setObservaciones(observaciones);
		definirCalendarioGuardiaForm.setFechaInicio(fechaInicio);
		definirCalendarioGuardiaForm.setFechaFin(fechaFin);
		if(idInstitucion!=null)
			definirCalendarioGuardiaForm.setIdInstitucion(idInstitucion.toString());
		if(idTurno!=null)
			definirCalendarioGuardiaForm.setIdTurno(idTurno.toString());
		if(idGuardia!=null)
			definirCalendarioGuardiaForm.setIdGuardia(idGuardia.toString());
		if(idCalendarioGuardias!=null)
			definirCalendarioGuardiaForm.setIdCalendarioGuardias(idCalendarioGuardias.toString());
		return definirCalendarioGuardiaForm;
	}
		
	
	
}