/*
 * Created on Jan 17, 2005
 * @author emilio.grau
 */
package com.siga.expedientes.form;

import java.util.StringTokenizer;

import com.siga.general.MasterForm;

/**
 * Formulario para la gestión de los datos generales de un expediente
 */
public class ExpDatosGeneralesForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
    private String tipoExpediente="";
    private String numExpediente="";
    private String anioExpediente="";
    private String numExpDisciplinario="";
    private String anioExpDisciplinario="";
    private String fecha="";
    private String institucion="";
    private String asunto="";
    private String clasificacion="";
    private String nombre="";
    private String primerApellido="";
    private String segundoApellido="";
    private String nif="";
    private String numColegiado="";
    private String juzgado="";
    private String idMateria="";
    private String idArea="";
    private String procedimiento="";
    private String idInstitucionJuzgado="";
    private String idInstitucionProcedimiento="";
    private String numAsunto="";
    private String fase="";
    private String estado="";
    private String fechaInicial="";
    private String fechaFinal="";
    private String fechaProrroga="";
    private String clasificacionSel="";
    private String faseSel="";
    private String estadoSel="";
    private String comboFases="";
    private String comboEstados="";
    private String idPersona="";
    private String idPretension="";
    private String otrasPretensiones="";
    

    
    
    
    
    private String fechaCaducidad="";
    private String observaciones="";
    private String minuta="";
    private String importeIVA="";
    private String importeTotal="";
    private String porcentajeIVA="";
    private String idTipoIVA="";
        
    public String getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getMinuta() {
		return minuta;
	}
	public void setMinuta(String minuta) {
		this.minuta = minuta;
	}
	
	public String getImporteIVA() {
		return importeIVA;
	}
	public void setImporteIVA(String valor) {
		this.importeIVA = valor;
	}

	public String getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(String valor) {
		this.importeTotal = valor;
	}

	public String getPorcentajeIVA() {
		return porcentajeIVA;
	}
	public void setPorcentajeIVA(String valor) {
		this.porcentajeIVA = valor;
	}

	public String getIdTipoIVA() {
		return idTipoIVA;
	}
	public void setIdTipoIVA(String idTipoIVA) {
		this.idTipoIVA = idTipoIVA;
	}
    
	public String getIdPretension() {
		return idPretension;
	}
	public void setIdPretension(String valor) {
		this.idPretension = valor;
	}
    
	public String getOtrasPretensiones() {
		return otrasPretensiones;
	}
	public void setOtrasPretensiones(String valor) {
		this.otrasPretensiones = valor;
	}
    
    
    
	
	
	
	
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getAnioExpDisciplinario() {
		return anioExpDisciplinario;
	}
	public void setAnioExpDisciplinario(String anioExpDisciplinario) {
		this.anioExpDisciplinario = anioExpDisciplinario;
	}
	public String getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(String anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFase() {
		return fase;
	}
	public void setFase(String fase) {
		this.fase = fase;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public String getFechaProrroga() {
		return fechaProrroga;
	}
	public void setFechaProrroga(String fechaProrroga) {
		this.fechaProrroga = fechaProrroga;
	}
	public String getInstitucion() {
		return institucion;
	}
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	public String getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(String juzgado) {
		StringTokenizer st = new StringTokenizer(juzgado, ",");
		
		if (st.hasMoreTokens())
		{
			this.juzgado = st.nextToken();

			if (st.hasMoreTokens())
			{
				this.idInstitucionJuzgado = st.nextToken();
			}
			
			else
			{
				this.idInstitucionJuzgado="";
			}
		}
		
		else
		{
			this.juzgado="";
			this.idInstitucionJuzgado="";
		}
	}
	public String getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}

	public void setIdMateriaSolo(String valor) {
		this.idMateria=valor;
	}

	public void setIdAreaSolo(String valor) {
		this.idArea=valor;
	}


	public void setIdMateria(String valor) {
		StringTokenizer st = new StringTokenizer(valor, ",");
		
		if (st.hasMoreTokens())
		{
			st.nextToken();
			if (st.hasMoreTokens())
			{
				this.idArea = st.nextToken();
				if (st.hasMoreTokens())
				{
					this.idMateria = st.nextToken();
					
				} else {
					this.idMateria="";
				}
				
			} else {
				this.idArea="";
				this.idMateria="";
			}
		} else {
			this.idArea="";
			this.idMateria="";
		}
	}
	public String getIdMateria() {
		return idMateria;
	}

	
	public void setIdInstitucionJuzgado(String idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}

	public void setIdArea(String valor) {
		this.idArea = valor;
	}
	public String getIdArea() {
		return idArea;
	}
	public String getModal() {
		return modal;
	}
	public void setModal(String modal) {
		this.modal = modal;
	}
//	public String getModo() {
//		return modo;
//	}
//	public void setModo(String modo) {
//		this.modo = modo;
//	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNumAsunto() {
		return numAsunto;
	}
	public void setNumAsunto(String numAsunto) {
		this.numAsunto = numAsunto;
	}
	public String getNumColegiado() {
		return numColegiado;
	}
	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}
	public String getNumExpDisciplinario() {
		return numExpDisciplinario;
	}
	public void setNumExpDisciplinario(String numExpDisciplinario) {
		this.numExpDisciplinario = numExpDisciplinario;
	}
	public String getNumExpediente() {
		return numExpediente;
	}
	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		StringTokenizer st = new StringTokenizer(procedimiento, ",");

		if (st.hasMoreTokens())
		{
			this.procedimiento = st.nextToken();
			
			if (st.hasMoreTokens())
			{
				this.idInstitucionProcedimiento = st.nextToken();
			}
			
			else
			{
				this.idInstitucionProcedimiento="";
			}
		}
		
		else
		{
			this.procedimiento="";
			this.idInstitucionProcedimiento="";
		}
	}
	public String getIdInstitucionProcedimiento() {
		return idInstitucionProcedimiento;
	}
	public void setIdInstitucionProcedimiento(String idInstitucionProcedimiento) {
		this.idInstitucionProcedimiento = idInstitucionProcedimiento;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getTipoExpediente() {
		return tipoExpediente;
	}
	public void setTipoExpediente(String tipoExpediente) {
		this.tipoExpediente = tipoExpediente;
	}
	public String getClasificacionSel() {
		return clasificacionSel;
	}
	public void setClasificacionSel(String clasificacionSel) {
		this.clasificacionSel = clasificacionSel;
	}
	public String getComboEstados() {
		return comboEstados;
	}
	public void setComboEstados(String comboEstados) {
		this.comboEstados = comboEstados;
		if (comboEstados!=null && !comboEstados.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboEstados,",");
	    	st.nextToken();//idinstitucion_tipoexpediente
	    	st.nextToken();//idtipoexpediente
	    	st.nextToken();//idfase
	    	this.setEstado(st.nextToken());        	
	    }else{        	
	    	this.setEstado("");  
	    }
	}
	public String getComboFases() {
		return comboFases;
	}
	public void setComboFases(String comboFases) {
		this.comboFases = comboFases;
	    if (comboFases!=null && !comboFases.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboFases,",");
	    	st.nextToken();//idinstitucion_tipoexpediente
	    	st.nextToken();//idtipoexpediente
	    	this.setFase(st.nextToken());        	
	    }else{        	
	    	this.setFase("");  
	    }
	}
	public String getEstadoSel() {
		return estadoSel;
	}
	public void setEstadoSel(String estadoSel) {
		this.estadoSel = estadoSel;
	}
	public String getFaseSel() {
		return faseSel;
	}
	public void setFaseSel(String faseSel) {
		this.faseSel = faseSel;
	}	
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	 
	
}
