package com.siga.beans;

import java.util.Date;

/**
 * Bean para manejar el historico de estados por los que pasa un envio
 * Mapea contra la tabla ENV_HISTORICOESTADOENVIO
 * @author josebd
 *
 */
public class EnvHistoricoEstadoEnvioBean extends MasterBean {
	
    // Variables
	private Integer idInstitucion;
	private Integer idEnvio;
	private Integer idHistorico;
	private Integer idEstado;
	private Date 	fechaEstado;
	
	
	// Nombre campos de la tabla
	static public final String T_NOMBRETABLA = "ENV_HISTORICOESTADOENVIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDHISTORICO = "IDHISTORICO";
	static public final String C_IDESTADO = "IDESTADO";	
	static public final String C_FECHAESTADO = "FECHAESTADO";
	
	public static String[] getCamposBean() {
        String[] campos = {
                C_IDINSTITUCION,
                C_IDENVIO,
                C_IDHISTORICO,
                C_IDESTADO,
                C_FECHAESTADO, 
                C_FECHAMODIFICACION, 
                C_USUMODIFICACION
				};

		return campos;
    }
	
	public static String[] getOrdenCampos() {
		String[] campos = {
				C_FECHAESTADO
		};
		
		return campos;
	}
	
	public static String[] getPK() {
        String[] pk = {
                C_IDINSTITUCION,
                C_IDENVIO,
                C_IDHISTORICO
				};

		return pk;
    }
	
	// Getters y Setters de la tabla ENV_HISTORICOESTADOENVIO
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdEnvio() {
		return idEnvio;
	}
	public void setIdEnvio(Integer idEnvio) {
		this.idEnvio = idEnvio;
	}
	public Integer getIdHistorico() {
		return idHistorico;
	}
	public void setIdHistorico(Integer idHistorico) {
		this.idHistorico = idHistorico;
	}
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public Date getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Date date) {
		this.fechaEstado = date;
	}	
	
	
	

}
