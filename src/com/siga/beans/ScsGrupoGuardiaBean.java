package com.siga.beans;

import java.util.ArrayList;

import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * Bean de la tabla SCS_GRUPOGUARDIACOLEGIADO
 * @since 14/12/2011
 */
public class ScsGrupoGuardiaBean extends MasterBean
{
	/**
	 *  Nombre de Tabla
	 */
	static public String T_NOMBRETABLA = "SCS_GRUPOGUARDIA";
	
	static public String S_SECUENCIA = "SQ_SCS_GRUPOGUARDIA";
	
	
	/** 
	 * 	Nombre de campos de la tabla
	 */
	static public final String C_IDGRUPOGUARDIA				= "IDGRUPOGUARDIA";
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDTURNO					= "IDTURNO";
	static public final String C_IDGUARDIA					= "IDGUARDIA";
	static public final String C_NUMEROGRUPO				= "NUMEROGRUPO";
	static public final String C_FECHACREACION				= "FECHACREACION";
	static public final String C_USUCREACION				= "USUCREACION";
	
	/**
	 * Atributos
	 */
	private Long	idGrupoGuardia;
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private String 	numeroGrupo;
	private String	fechaCreacion;
	private Integer	usuCreacion;

	/**
	 * Getters
	 */
	public Long 	getIdGrupoGuardia() { return idGrupoGuardia;}
	public Integer 	getIdInstitucion() 	{ return idInstitucion;}
	public Integer 	getIdTurno() 		{ return idTurno;}
	public Integer 	getIdGuardia() 		{ return idGuardia;}
	public String 	getNumeroGrupo() 	{ return numeroGrupo;}
	public String 	getFechaCreacion() 	{ return fechaCreacion;}
	public Integer 	getUsuCreacion() 	{ return usuCreacion;}
	
	/**
	 * Setters
	 */
	public void setIdGrupoGuardia(Long idGrupoGuardia) 		{this.idGrupoGuardia = idGrupoGuardia;}
	public void setIdInstitucion(Integer idInstitucion) 	{this.idInstitucion = idInstitucion;}
	public void setIdTurno(Integer idTurno) 				{this.idTurno = idTurno;}
	public void setIdGuardia(Integer idGuardia) 			{this.idGuardia = idGuardia;}
	public void setNumeroGrupo(String numeroGrupo) 			{this.numeroGrupo = numeroGrupo;}
	public void setFechaCreacion(String fechaCreacion) 		{this.fechaCreacion = fechaCreacion;}
	public void setUsuCreacion(Integer usuCreacion) 		{this.usuCreacion = usuCreacion;}
	
	
}