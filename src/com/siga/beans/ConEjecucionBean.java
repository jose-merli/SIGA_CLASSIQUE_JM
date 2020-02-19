package com.siga.beans;

public class ConEjecucionBean extends MasterBean {
	
	private static final long serialVersionUID = 8945336879851278327L;

	// Campos de la tabla 
	static public final String C_IDEJECUCION ="IDEJECUCION";
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDUSUARIO ="IDUSUARIO";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_IDINSTITUCION_CONSULTA ="IDINSTITUCION_CONSULTA";
	static public final String C_FECHAEJECUCION ="FECHAEJECUCION";
	static public final String C_TIEMPOEJECUCION ="TIEMPOEJECUCION";
	static public final String C_MENSAJE ="MENSAJE";
	static public final String C_SENTENCIA ="SENTENCIA";
	
	static public final String T_NOMBRETABLA = "CON_EJECUCION";
	
	// Variables
	private Long idEjecucion;
	private Integer idInstitucion;
	private Integer IdUsuario;
	private Long idConsulta;
	private Integer idInstitucion_Consulta;
	private String fechaEjecucion;
	private Long tiempoEjecucion;
	private String mensaje;
	private String sentencia;
	
	public Integer getIdUsuario() {
		return IdUsuario;
	}

	public void setIdUsuario(Integer IdUsuario) {
		this.IdUsuario = IdUsuario;
	}

	public Long getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Long idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Integer getIdInstitucion_Consulta() {
		return idInstitucion_Consulta;
	}

	public void setIdInstitucion_Consulta(Integer idInstitucion_Consulta) {
		this.idInstitucion_Consulta = idInstitucion_Consulta;
	}

	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public Long getTiempoEjecucion() {
		return tiempoEjecucion;
	}

	public void setTiempoEjecucion(Long tiempoEjecucion) {
		this.tiempoEjecucion = tiempoEjecucion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getIdEjecucion() {
		return idEjecucion;
	}

	public void setIdEjecucion(Long idEjecucion) {
		this.idEjecucion = idEjecucion;
	}

	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getSentencia() {
		return sentencia;
	}

	public void setSentencia(String sentencia) {
		this.sentencia = sentencia;
	}
	
}
