package com.siga.beans;

import org.redabogacia.sigaservices.app.AppConstants.TiposEnvioEnum;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_REMESA
 * 
 * @author fernando.gómez
 * @since 17/09/2008
 */

public class CajgRemesaBean extends MasterBean{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 666367542747957849L;
	/*
	 *  Variables */ 
	private Integer idInstitucion;
	private Integer idRemesa;
	private String	prefijo;
	private String numero;
	private String sufijo;
	private String descripcion;
	private Integer idIntercambio;
	private Long idecomcola;
	private Short idTipoRemesa;  
	
	
	
	static public String T_NOMBRETABLA = "CAJG_REMESA";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_IDREMESA =					"IDREMESA";
	static public final String	C_PREFIJO	=					"PREFIJO";
	static public final String	C_NUMERO=						"NUMERO";
	static public final String	C_SUFIJO	=					"SUFIJO";
	static public final String	C_DESCRIPCION	=				"DESCRIPCION";
	static public final String	C_IDINTERCAMBIO	=				"IDINTERCAMBIO";
	static public final String	C_IDECOMCOLA	=				"IDECOMCOLA";
	static public final String	C_IDTIPOREMESA	=				"IDTIPOREMESA";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdRemesa() {
		return idRemesa;
	}
	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}
	public String getPrefijo() {
		return prefijo;
	}
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSufijo() {
		return sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getIdIntercambio() {
		return idIntercambio;
	}
	public void setIdIntercambio(Integer idIntercambio) {
		this.idIntercambio = idIntercambio;
	}
	public Long getIdecomcola() {
		return idecomcola;
	}
	public void setIdecomcola(Long idecomcola) {
		this.idecomcola = idecomcola;
	}
	/*
	 *  Nombre de Tabla*/
	
	public Short getIdTipoRemesa() {
		return idTipoRemesa;
	}
	public void setIdTipoRemesa(Short idTipoRemesa) {
		this.idTipoRemesa = idTipoRemesa;
	}
	
	public static enum TIPOREMESA {
		
		REMESA_EJGS ("0","REMESA"),
		REMESA_ECONOMICA ("1", "REMESAECONOMICA");
		
		


	    private final String idTipo;   
		private final String contador;   
	    
	    
		TIPOREMESA(String idTipo, String contador) {
	    	this.idTipo = idTipo;
	        this.contador = contador;
	    }
		
		public String getIdTipo() {
			return idTipo;
		}

		public String getContador() {
			return contador;
		}
		public static TIPOREMESA getEnum(String idTipo){
			for(TIPOREMESA sc : values()){
				if (sc.getIdTipo().equalsIgnoreCase(idTipo)){
					return sc;
				}
			}
			return null;
		}
		
		
		

	}
	
	
}