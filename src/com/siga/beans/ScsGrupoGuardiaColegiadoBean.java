package com.siga.beans;

import java.util.ArrayList;

import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

/**
 * Bean de la tabla SCS_GRUPOGUARDIACOLEGIADO
 * @since 25/11/2010
 */
public class ScsGrupoGuardiaColegiadoBean extends MasterBean
{
	/**
	 *  Nombre de Tabla
	 */
	static public String T_NOMBRETABLA = "SCS_GRUPOGUARDIACOLEGIADO";
	
	
	// Nombre de campos de la tabla
	static public final String C_IDGRUPOGUARDIACOLEGIADO	= "IDGRUPOGUARDIACOLEGIADO";
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDTURNO					= "IDTURNO";
	static public final String C_IDGUARDIA					= "IDGUARDIA";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_FECHASUSCRIPCION			= "FECHASUSCRIPCION";
	static public final String C_IDGRUPO					= "IDGRUPOGUARDIA";
	static public final String C_ORDEN						= "ORDEN";
	static public final String C_FECHACREACION				= "FECHACREACION";
	static public final String C_USUCREACION				= "USUCREACION";
	
	static public final String S_SECUENCIA					= "SQ_SCS_GRUPOGUARDIACOLEGIADO";
	
	
	// Atributos
	private Long	idGrupoGuardiaColegiado;
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Long 	idPersona;
	private String	fechaSuscripcion;
	private Integer	idGrupo;
	private Integer	orden;
	private String	fechaCreacion;
	private Integer	usuCreacion;
	
	
	// Setters
	public void setIdGrupoGuardiaColegiado	(Long valor)	{this.idGrupoGuardiaColegiado	= valor;}
	public void setIdInstitucion 			(Integer valor)	{this.idInstitucion				= valor;}
	public void setIdTurno					(Integer valor)	{this.idTurno					= valor;}
	public void setIdGuardia				(Integer valor)	{this.idGuardia					= valor;}
	public void setIdPersona	 			(Long valor)	{this.idPersona					= valor;}
	public void setFechaSuscripcion			(String valor)	{this.fechaSuscripcion			= valor;}
	public void setIdGrupo					(Integer valor)	{this.idGrupo					= valor;}
	public void setOrden					(Integer valor)	{this.orden						= valor;}
	public void setFechaCreacion			(String valor)	{this.fechaCreacion				= valor;}
	public void setUsuCreacion				(Integer valor)	{this.usuCreacion				= valor;}
	
	
	// Getters
	public Long getIdGrupoGuardiaColegiado	() {return idGrupoGuardiaColegiado;}
	public Integer getIdInstitucion 		() {return idInstitucion;}
	public Integer getIdTurno				() {return idTurno;}
	public Integer getIdGuardia				() {return idGuardia;}
	public Long getIdPersona		 		() {return idPersona;}
	public String getFechaSuscripcion		() {return fechaSuscripcion;}
	public Integer getIdGrupo				() {return idGrupo;}
	public Integer getOrden					() {return orden;}
	public String getFechaCreacion			() {return fechaCreacion;}
	public Integer getUsuCreacion			() {return usuCreacion;}
	
}