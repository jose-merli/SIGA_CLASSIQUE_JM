package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DESIGNALETRADO
 * 
 * @since 20/1/2005
 * @version 26/01/2006 (david.sanchezp) nuevos campos.
 */

public class ScsDesignaBean extends MasterBean {
	
	ScsTurnoBean turno;
	CenPersonaBean colegiadoDesignado;
	// Variables 
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer	anio;
	private Long numero;
	private String	fechaEntrada;
	private String	fechaFin;
	private String	fechaAnulacion;
	private String  estado;
	private String	resumenAsunto;
	private String	delitos;
	private String	defensaJuridica;
	private String	observaciones;
	private String	procurador;
	private Integer idTipoDesignaColegio;
	private Long idProcurador;
	private Integer idInstitucionProcurador;
	private Long idJuzgado;
	private Integer idInstitucionJuzgado;
	private String	codigo;
	private String	numProcedimiento;
	private String	fechaJuicio;
	private String	procedimiento;
	private String	fechaEstado;
	private Integer  idPretension; 
	private String  sufijo;
	private String  fechaRecepcionColegio; 
	private String  fechaOficioJuzgado;
	private String  fechaAlta;
	private String art27;
	private String NIG;
	private Integer anioProcedimiento;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_DESIGNA";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_IDTURNO =					"IDTURNO";
	static public final String	C_ANIO =					"ANIO";
	static public final String	C_NUMERO =					"NUMERO";
	static public final String	C_FECHAENTRADA	=			"FECHAENTRADA";
	static public final String	C_FECHAFIN	=				"FECHAFIN";
	static public final String	C_ESTADO	=				"ESTADO";
	static public final String	C_RESUMENASUNTO	=			"RESUMENASUNTO";
	static public final String	C_DELITOS	=				"DELITOS";
	static public final String	C_DEFENSAJURIDICA	=		"DEFENSAJURIDICA";
	static public final String	C_PROCURADOR	=			"PROCURADOR";
	static public final String	C_OBSERVACIONES			=	"OBSERVACIONES";
	static public final String	C_IDTIPODESIGNACOLEGIO	=	"IDTIPODESIGNACOLEGIO";
	static public final String	C_IDPROCURADOR	=			"IDPROCURADOR";
	static public final String	C_IDINSTITUCIONPROCURADOR = "IDINSTITUCION_PROCUR";
	static public final String	C_IDJUZGADO		=			"IDJUZGADO";
	static public final String	C_IDINSTITUCIONJUZGADO	=	"IDINSTITUCION_JUZG";
	static public final String	C_FECHA_ANULACION		=	"FECHAANULACION";
	static public final String	C_CODIGO         		=	"CODIGO";
	static public final String	C_NUMPROCEDIMIENTO		=	"NUMPROCEDIMIENTO";
	static public final String	C_FECHAJUICIO		    =	"FECHAJUICIO";
	static public final String	C_IDPROCEDIMIENTO		=	"IDPROCEDIMIENTO";
	static public final String	C_FECHAESTADO		    =	"FECHAESTADO";
	static public final String	C_IDPRETENSION		    =	"IDPRETENSION";
	static public final String	C_SUFIJO		        =	"SUFIJO";
	static public final String	C_FECHARECEPCIONCOLEGIO = 	"FECHARECEPCIONCOLEGIO";
	static public final String	C_FECHAOFICIOJUZGADO 	= 	"FECHAOFICIOJUZGADO";
	static public final String	C_FECHAALTA 			= 	"FECHAALTA";
	static public final String	C_ART27 				= 	"ART27";
	static public final String  C_NIG   				= 	"NIG";
	static public final String  C_ANIOPROCEDIMIENTO		= 	"ANIOPROCEDIMIENTO";
	
	

	
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	/**
	 * @return Returns the anio.
	 */
	public String getProcedimiento() {
		return procedimiento;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	/**
	 * @return Returns the defensaJuridica.
	 */
	public String getDefensaJuridica() {
		return defensaJuridica;
	}
	/**
	 * @param defensaJuridica The defensaJuridica to set.
	 */
	public void setDefensaJuridica(String defensaJuridica) {
		this.defensaJuridica = defensaJuridica;
	}
	/**
	 * @return Returns the delitos.
	 */
	public String getDelitos() {
		return delitos;
	}
	/**
	 * @param delitos The delitos to set.
	 */
	public void setDelitos(String delitos) {
		this.delitos = delitos;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	/**
	 * @return Returns the fechaAnulacion.
	 */
	public String getFechaAnulacion() {
		return fechaAnulacion;
	}
	/**
	 * @param fechaAnulacion The fechaAnulacion to set.
	 */
	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	/**
	 * @return Returns the fechaEntrada.
	 */
	public String getFechaEntrada() {
		return fechaEntrada;
	}
	
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @return Returns the codigo.
	 */
	public String getSufijo() {
		return sufijo;
	}
	/**
	 * @param fechaEntrada The fechaEntrada to set.
	 */
	public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	/**
	 * @return Returns the fechaFin.
	 */
	public String getFechaFin() {
		return fechaFin;
	}
	/**
	 * @param fechaFin The fechaFin to set.
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idInstitucionJuzgado.
	 */
	public Integer getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	/**
	 * @param idInstitucionJuzgado The idInstitucionJuzgado to set.
	 */
	public void setIdInstitucionJuzgado(Integer idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	/**
	 * @return Returns the idInstitucionProcurador.
	 */
	public Integer getIdInstitucionProcurador() {
		return idInstitucionProcurador;
	}
	/**
	 * @param idInstitucionProcurador The idInstitucionProcurador to set.
	 */
	public void setIdInstitucionProcurador(Integer idInstitucionProcurador) {
		this.idInstitucionProcurador = idInstitucionProcurador;
	}
	/**
	 * @return Returns the idJuzgado.
	 */
	public Long getIdJuzgado() {
		return idJuzgado;
	}
	/**
	 * @param idJuzgado The idJuzgado to set.
	 */
	public void setIdJuzgado(Long idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	/**
	 * @return Returns the idProcurador.
	 */
	public Long getIdProcurador() {
		return idProcurador;
	}
	/**
	 * @param idProcurador The idProcurador to set.
	 */
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
/**
 * @return Returns the idTipoDesignaColegio.
 */
public Integer getIdTipoDesignaColegio() {
	return idTipoDesignaColegio;
}
/**
 * @param idTipoDesignaColegio The idTipoDesignaColegio to set.
 */
public void setIdTipoDesignaColegio(Integer idTipoDesignaColegio) {
	this.idTipoDesignaColegio = idTipoDesignaColegio;
}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	/**
	 * @return Returns the numero.
	 */
	public Long getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return Returns the procurador.
	 */
	public String getProcurador() {
		return procurador;
	}
	/**
	 * @param procurador The procurador to set.
	 */
	public void setProcurador(String procurador) {
		this.procurador = procurador;
	}
	/**
	 * @return Returns the resumenAsunto.
	 */
	public String getResumenAsunto() {
		return resumenAsunto;
	}
	/**
	 * @param resumenAsunto The resumenAsunto to set.
	 */
	public void setResumenAsunto(String resumenAsunto) {
		this.resumenAsunto = resumenAsunto;
	}
	/**
	 * @param resumenAsunto The resumenAsunto to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
    /**
     * @return Returns the numProcedimiento.
     */
    public String getNumProcedimiento() {
        return numProcedimiento;
    }
    /**
     * @param numProcedimiento The numProcedimiento to set.
     */
    public void setNumProcedimiento(String numProcedimiento) {
        this.numProcedimiento = numProcedimiento;
    }

    /**
     * @return Returns the fechaJuicio.
     */
    public String getFechaJuicio() {
        return fechaJuicio;
    }
    /**
     * @param fechaJuicio 
     */
    public void setFechaJuicio(String valor) {
        this.fechaJuicio = valor;
    }
	/**
	 * @return Returns the fechaEstado.
	 */
	public String getFechaEstado() {
		return fechaEstado;
	}
	/**
	 * @param fechaEstado The fechaEstado to set.
	 */
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	/**
	 * @return El idPretension
	 */
	public Integer getIdPretension() {
		return idPretension;
	}
	/**
	 * @param idPretension El idPretension para almacenar
	 */
	public void setIdPretension(Integer idPretension) {
		this.idPretension = idPretension;
	}
	public ScsTurnoBean getTurno() {
		return turno;
	}
	public void setTurno(ScsTurnoBean turno) {
		this.turno = turno;
	}
	public CenPersonaBean getColegiadoDesignado() {
		return colegiadoDesignado;
	}
	public void setColegiadoDesignado(CenPersonaBean colegiadoDesignado) {
		this.colegiadoDesignado = colegiadoDesignado;
	}
	public String getFechaRecepcionColegio() {
		return fechaRecepcionColegio;
	}
	public void setFechaRecepcionColegio(String fechaRecepcionColegio) {
		this.fechaRecepcionColegio = fechaRecepcionColegio;
	}
	public String getFechaOficioJuzgado() {
		return fechaOficioJuzgado;
	}
	public void setFechaOficioJuzgado(String fechaOficioJuzgado) {
		this.fechaOficioJuzgado = fechaOficioJuzgado;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public String getArt27() {
		return art27;
	}
	public void setArt27(String art27) {
		this.art27 = art27;
	}
	public String getNIG() {
		return NIG;
	}
	public void setNIG(String nIG) {
		NIG = nIG;
	}
	public Integer getAnioProcedimiento() {
		return anioProcedimiento;
	}
	public void setAnioProcedimiento(Integer anioProcedimiento) {
		this.anioProcedimiento = anioProcedimiento;
	}
	
}