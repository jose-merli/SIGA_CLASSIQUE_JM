package com.siga.beans;

import java.util.ArrayList;

import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_SALTOCOMPENSACIONGRUPO
 */
public class ScsSaltoCompensacionGrupoBean extends MasterBean
{
	// Campos
	private Long	idSaltoCompensacionGrupo;
	private Long	idGrupoGuardia;
	private String	saltoCompensacion;
	private String	fecha;
	private String  fechaCumplimiento;
	private String  motivo;
	private String  motivoCumplimiento;
	private Integer	idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer idCalendarioGuardias;
	private Integer	idInstitucion_Cumpli;
	private Integer idTurno_Cumpli;
	private Integer idGuardia_Cumpli;
	private Integer idCalendarioGuardias_Cumpli;
	private String	fechaCreacion;
	private Integer	usuCreacion;
	
	private ArrayList<LetradoGuardia> letrados;
	
	
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_SALTOCOMPENSACIONGRUPO";
	
	// Nombre de campos de la tabla
	static public final String 	C_IDSALTOCOMPENSACIONGRUPO		= "IDSALTOCOMPENSACIONGRUPO";
	static public final String 	C_IDGRUPOGUARDIA				= "IDGRUPOGUARDIA";
	static public final String 	C_SALTOCOMPENSACION				= "SALTOOCOMPENSACION";
	static public final String 	C_FECHA							= "FECHA";
	static public final String  C_FECHACUMPLIMIENTO				= "FECHACUMPLIMIENTO";
	static public final String 	C_MOTIVO						= "MOTIVO";
	static public final String 	C_MOTIVOCUMPLIMIENTO			= "MOTIVOCUMPLIMIENTO";
	static public final String	C_IDINSTITUCION					= "IDINSTITUCION";
	static public final String 	C_IDTURNO						= "IDTURNO";
	static public final String 	C_IDGUARDIA						= "IDGUARDIA";
	static public final String  C_IDCALENDARIOGUARDIAS			= "IDCALENDARIOGUARDIAS";
	static public final String	C_IDINSTITUCION_CUMPLI			= "IDINSTITUCION_CUMPLI";
	static public final String 	C_IDTURNO_CUMPLI				= "IDTURNO_CUMPLI";
	static public final String 	C_IDGUARDIA_CUMPLI				= "IDGUARDIA_CUMPLI";
	static public final String  C_IDCALENDARIOGUARDIAS_CUMPLI	= "IDCALENDARIOGUARDIAS_CUMPLI";
	static public final String 	C_FECHACREACION					= "FECHACREACION";
	static public final String 	C_USUCREACION					= "USUCREACION";
	
	
	// SETTERS
	public void setIdSaltoCompensacionGrupo		(Long valor)	{this.idSaltoCompensacionGrupo		= valor;}
	public void setIdGrupoGuardia				(Long valor)	{this.idGrupoGuardia				= valor;}
	public void setSaltoCompensacion			(String  valor)	{this.saltoCompensacion				= valor;}
	public void setFecha						(String  valor)	{this.fecha							= valor;}
	public void setFechaCumplimiento			(String  valor)	{this.fechaCumplimiento				= valor;}
	public void setMotivo						(String  valor)	{this.motivo						= valor;}
	public void setMotivoCumplimiento			(String  valor)	{this.motivoCumplimiento			= valor;}
	public void setIdInstitucion				(Integer valor)	{this.idInstitucion					= valor;}
	public void setIdTurno						(Integer valor)	{this.idTurno						= valor;}
	public void setIdGuardia					(Integer valor)	{this.idGuardia						= valor;}
	public void setIdCalendarioGuardias			(Integer valor) {this.idCalendarioGuardias			= valor;}
	public void setIdInstitucion_Cumpli			(Integer valor)	{this.idInstitucion_Cumpli			= valor;}
	public void setIdTurno_Cumpli				(Integer valor)	{this.idTurno_Cumpli				= valor;}
	public void setIdGuardia_Cumpli				(Integer valor)	{this.idGuardia_Cumpli				= valor;}
	public void setIdCalendarioGuardias_Cumpli	(Integer valor)	{this.idCalendarioGuardias_Cumpli	= valor;}
	public void setFechaCreacion				(String  valor)	{this.fechaCreacion					= valor;}
	public void setUsuCreacion					(Integer valor)	{this.usuCreacion					= valor;}
	
	public void setLetrados(ArrayList<LetradoGuardia> letrados) {this.letrados = letrados;}
	
	
	// GETTERS
	public Long		getIdSaltoCompensacionGrupo		() {return idSaltoCompensacionGrupo;}
	public Long		getIdGrupoGuardia				() {return idGrupoGuardia;}
	public String	getSaltoCompensacion			() {return saltoCompensacion;}
	public String	getFecha						() {return fecha;}
	public String	getFechaCumplimiento			() {return fechaCumplimiento;}
	public String	getMotivo						() {return motivo;}
	public String	getMotivoCumplimiento			() {return motivoCumplimiento;}
	public Integer	getIdInstitucion				() {return idInstitucion;}
	public Integer	getIdTurno						() {return idTurno;}
	public Integer	getIdGuardia					() {return idGuardia;}
	public Integer	getIdCalendarioGuardias			() {return idCalendarioGuardias;}
	public Integer	getIdInstitucion_Cumpli			() {return idInstitucion_Cumpli;}
	public Integer	getIdTurno_Cumpli				() {return idTurno_Cumpli;}
	public Integer	getIdGuardia_Cumpli				() {return idGuardia_Cumpli;}
	public Integer	getIdCalendarioGuardias_Cumpli	() {return idCalendarioGuardias_Cumpli;}
	public String	getFechaCreacion				() {return fechaCreacion;}
	public Integer	getUsuCreacion					() {return usuCreacion;}
	
	public ArrayList<LetradoGuardia> getLetrados() {return letrados;}
	
	
	public String toString() {
		return this.saltoCompensacion + " de grupo: " + this.getLetrados();
	}

}