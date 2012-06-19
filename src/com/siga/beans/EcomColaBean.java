package com.siga.beans;


public class EcomColaBean extends MasterBean {
	
	public static final String SEQ_ECOM_COLA = "SEQ_ECOM_COLA";
	
	public static enum EstadosCola {		
		INICIAL (1),
		EJECUTANDOSE (2),
		REINTENTANDO (3),
		ERROR (4),
		FINAL (5),
		ERROR_VALIDACION (6);
		
		private int id = -1;
		
		EstadosCola(int id) {
			this.id = id;
		}
		public int getId() {
			return this.id;
		}
	}
	
	public static enum OPERACION {
		ASIGNA_OBTENER_PROCURADOR (1)
		, ASIGNA_ENVIO_DOCUMENTO (2)
		, EJIS_OBTENER_DESTINATARIOS (3)
		, EJIS_COMUNICACION_DESIGNA_ABOGADO_PROCURADOR (4)
		, ASIGNA_CONSULTA_NUMERO (5);	
		
		private int id = -1;
		
		private OPERACION(int id) {
			this.id = id;
		}
		public int getId() {
			return this.id;
		}
	}
	
	private Integer idEcomCola;
	private Integer idInstitucion;
	private Integer idEstadoCola;
	private Integer idOperacion;
	private Integer reintento;
	private String fechaCreacion;
		
		
	static public String T_NOMBRETABLA = "ECOM_COLA";
	
	static public final String C_IDECOMCOLA = "IDECOMCOLA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDESTADOCOLA = "IDESTADOCOLA";
	static public final String C_IDOPERACION = "IDOPERACION";
	static public final String C_REINTENTO = "REINTENTO";
	static public final String C_FECHACREACION = "FECHACREACION";
	
	
	public Integer getIdEcomCola() {
		return idEcomCola;
	}
	public void setIdEcomCola(Integer idEcomCola) {
		this.idEcomCola = idEcomCola;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdEstadoCola() {
		return idEstadoCola;
	}
	public void setIdEstadoCola(Integer idEstadoCola) {
		this.idEstadoCola = idEstadoCola;
	}
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public Integer getReintento() {
		return reintento;
	}
	public void setReintento(Integer reintento) {
		this.reintento = reintento;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}	
	
}
