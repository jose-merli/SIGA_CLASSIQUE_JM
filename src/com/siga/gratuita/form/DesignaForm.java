package com.siga.gratuita.form;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.siga.general.MasterForm;

/**
 * @author jorgeta
 *
 * @version 05/10/2010
 */

public class DesignaForm extends MasterForm
{
		
	private String idPersona;
	private String idInstitucion;
	private String anio;
	private String numero;
	private String idTurno;
	private String codigoDesigna;
//	private String ejgs;
	private List<DefinirEJGForm> expedientes;
	private String juzgado;
	private String fecha;
	private String asunto;
	private String clientes;
	private String categoria;
	private String descripcionProcedimiento;
	private String baja;
	private Map<String, List<ActuacionDesignaForm>> actuaciones;
	private Map<String,List<AcreditacionForm>> acreditaciones;
	private String actuacionValidarJustificaciones;
	private String actuacionPermitidaLetrado;
	private String actuacionRestriccionesActiva;
	private int rowSpan;
	private String idProcedimiento;
	private String idJurisdiccion;
	
	private String idJuzgado;
	private String multiplesComplementos;
	private String estado;
	
	private String cambioLetrado;
	private String articulo27;
	boolean permitidoJustificar = false;
	String tipoResolucionDesigna;
	
	private int numEjgResueltosFavorables; 
	
	

	public int getNumEjgResueltosFavorables() {
		return numEjgResueltosFavorables;
	}

	public void setNumEjgResueltosFavorables(int numEjgResueltosFavorables) {
		this.numEjgResueltosFavorables = numEjgResueltosFavorables;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMultiplesComplementos() {
		return multiplesComplementos;
	}

	public void setMultiplesComplementos(String multiplesComplementos) {
		this.multiplesComplementos = multiplesComplementos;
	}

	public int getRowSpan() {
		
			
		
		return rowSpan;
	}
	
	public void setRowSpan() {
		
		rowSpan = 0;
		if((cambioLetrado==null || cambioLetrado.equals("N"))&&(articulo27==null || articulo27.equals("N")) ){
			if(actuaciones!=null&&actuaciones.size()>0){
				Iterator<String> itActuacion = actuaciones.keySet().iterator();
				while (itActuacion.hasNext()) {
					String codProcedimineto = (String) itActuacion.next();
					rowSpan += ((List<ActuacionDesignaForm>)actuaciones.get(codProcedimineto)).size();
				}
				
			}
			if(acreditaciones!=null&&acreditaciones.size()>0){
				Iterator<String> itAcreditacion = acreditaciones.keySet().iterator();
				while (itAcreditacion.hasNext()) {
					String codProcedimineto = (String) itAcreditacion.next();
					rowSpan += ((List<AcreditacionForm>)acreditaciones.get(codProcedimineto)).size();
				}
			}
		}
		
		if(rowSpan==0)rowSpan=1;
		
		
	}

	public String getActuacionValidarJustificaciones() {
		return actuacionValidarJustificaciones;
	}
	public void setActuacionValidarJustificaciones(
			String actuacionValidarJustificaciones) {
		this.actuacionValidarJustificaciones = actuacionValidarJustificaciones;
	}
	public String getActuacionPermitidaLetrado() {
		return actuacionPermitidaLetrado;
	}
	public void setActuacionPermitidaLetrado(String actuacionPermitidaLetrado) {
		this.actuacionPermitidaLetrado = actuacionPermitidaLetrado;
	}
	
	public String getActuacionRestriccionesActiva() {
		return actuacionRestriccionesActiva;
	}
	public void setActuacionRestriccionesActiva(String actuacionRestriccionesActiva) {
		this.actuacionRestriccionesActiva = actuacionRestriccionesActiva;
	}
	
	public Map<String, List<AcreditacionForm>> getAcreditaciones() {
		return acreditaciones;
	}
	public void setAcreditaciones(Map<String, List<AcreditacionForm>> acreditaciones) {
		this.acreditaciones = acreditaciones;
	}
	public String getCodigoDesigna() {
		return codigoDesigna;
	}
	public void setCodigoDesigna(String codigoDesigna) {
		this.codigoDesigna = codigoDesigna;
	}
	/*
	public String getEjgs() {
		return ejgs;
	}*/
	/*public void setEjgs(String ejgs) {
		this.ejgs = ejgs;
	}*/
	public String getJuzgado() {
		if(juzgado!=null)
			juzgado=juzgado.trim();
		return juzgado;
	}
	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getClientes() {
		return clientes;
	}
	public void setClientes(String clientes) {
		this.clientes = clientes;
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getBaja() {
		return baja;
	}
	public void setBaja(String baja) {
		this.baja = baja;
	}
	
	
	public Map<String, List<ActuacionDesignaForm>> getActuaciones() {
		return actuaciones;
	}

	public void setActuaciones(Map<String, List<ActuacionDesignaForm>> actuaciones) {
		this.actuaciones = actuaciones;
	}

	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}

	public String getIdProcedimiento() {
		return idProcedimiento;
	}

	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}

	public String getIdJuzgado() {
		return idJuzgado;
	}

	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}

	public String getIdJurisdiccion() {
		return idJurisdiccion;
	}

	public void setIdJurisdiccion(String idJurisdiccion) {
		this.idJurisdiccion = idJurisdiccion;
	}

	public String getDescripcionProcedimiento() {
		return descripcionProcedimiento;
	}

	public void setDescripcionProcedimiento(String descripcionProcedimiento) {
		this.descripcionProcedimiento = descripcionProcedimiento;
	}

	public String getCambioLetrado() {
		return cambioLetrado;
	}

	public void setCambioLetrado(String cambioLetrado) {
		this.cambioLetrado = cambioLetrado;
	}

	public boolean getPermitidoJustificar() {
		return permitidoJustificar;
	}

	public void setPermitidoJustificar(boolean permitidoJustificar) {
		this.permitidoJustificar = permitidoJustificar;
	}

	public String getTipoResolucionDesigna() {
		return tipoResolucionDesigna;
	}

	public void setTipoResolucionDesigna(String tipoResolucionDesigna) {
		this.tipoResolucionDesigna = tipoResolucionDesigna;
	}

	public List<DefinirEJGForm> getExpedientes() {
		return expedientes;
	}

	public void setExpedientes(List<DefinirEJGForm> expedientes) {
		this.expedientes = expedientes;
	}

	public String getArticulo27() {
		return articulo27;
	}

	public void setArticulo27(String articulo27) {
		this.articulo27 = articulo27;
	}

	

	
	
}