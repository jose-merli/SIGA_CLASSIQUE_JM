/*
 * Created on Apr 06, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la definición de envios.
 */
public class ProgramacionForm extends MasterForm {
	
//    private String modo="";
    private String idEnvio;
	private String fechaProgramada;
	private String horas;
	private String minutos;
	private String generarDocumento="N";
	private String imprimirEtiquetas="N";
	private String idImpresora;
	private String automatico;
	
	private String fechaCargoUnica;
	private String fechaPresentacion;
	private String fechaRecibosPrimeros;
	private String fechaRecibosRecurrentes;
	private String fechaRecibosCOR1;
	private String fechaRecibosB2B;

    public String getFechaProgramada() {
        return fechaProgramada;
    }
    public void setFechaProgramada(String fecha) {
        this.fechaProgramada = fecha;
    }
    public String getHoras() {
        return horas;
    }
    public void setHoras(String horas) {
        this.horas = horas;
    }
    public String getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }
    public String getImprimirEtiquetas() {
        return imprimirEtiquetas;
    }
    public void setImprimirEtiquetas(String imprimirEtiquetas) {
        this.imprimirEtiquetas = imprimirEtiquetas;
    }
    public String getMinutos() {
        return minutos;
    }
    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }
    public String getGenerarDocumento() {
        return generarDocumento;
    }
    public void setGenerarDocumento(String generarDocumento) {
        this.generarDocumento = generarDocumento;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }
    public String getIdImpresora() {
        return idImpresora;
    }
    public void setIdImpresora(String idImpresora) {
        this.idImpresora = idImpresora;
    }
    public String getAutomatico() {
        return automatico;
    }
    public void setAutomatico(String automatico) {
        this.automatico = automatico;
    }
	public String getFechaCargoUnica() {
		return fechaCargoUnica;
	}
	public void setFechaCargoUnica(String fechaCargoUnica) {
		this.fechaCargoUnica = fechaCargoUnica;
	}
	public String getFechaPresentacion() {
		return fechaPresentacion;
	}
	public void setFechaPresentacion(String fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}
	public String getFechaRecibosPrimeros() {
		return fechaRecibosPrimeros;
	}
	public void setFechaRecibosPrimeros(String fechaRecibosPrimeros) {
		this.fechaRecibosPrimeros = fechaRecibosPrimeros;
	}
	public String getFechaRecibosRecurrentes() {
		return fechaRecibosRecurrentes;
	}
	public void setFechaRecibosRecurrentes(String fechaRecibosRecurrentes) {
		this.fechaRecibosRecurrentes = fechaRecibosRecurrentes;
	}
	public String getFechaRecibosCOR1() {
		return fechaRecibosCOR1;
	}
	public void setFechaRecibosCOR1(String fechaRecibosCOR1) {
		this.fechaRecibosCOR1 = fechaRecibosCOR1;
	}
	public String getFechaRecibosB2B() {
		return fechaRecibosB2B;
	}
	public void setFechaRecibosB2B(String fechaRecibosB2B) {
		this.fechaRecibosB2B = fechaRecibosB2B;
	}
    
}
