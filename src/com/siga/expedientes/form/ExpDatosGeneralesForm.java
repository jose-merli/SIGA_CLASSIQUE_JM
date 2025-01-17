/*
 * Created on Jan 17, 2005
 * @author emilio.grau
 */
package com.siga.expedientes.form;

import java.util.StringTokenizer;

import org.json.JSONObject;

import com.siga.general.MasterForm;

/**
 * Formulario para la gesti�n de los datos generales de un expediente
 */
public class ExpDatosGeneralesForm extends MasterForm {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1332047063575043937L;
	//    private String modo="";
    private String modal="";
    private String tipoExpediente="";
    private String idTipoExpediente="";    
    private String idturnoDesignado="";    

	private String numExpediente="";
    private String anioExpediente="";
    private String numExpDisciplinario="";
    private String numEJGDisciplinario;
    
	private String anioExpDisciplinario="";
    private String numExpDisciplinarioCalc="";
    
    private String tipoExpDisciplinario="";
	private String fecha="";
    private String institucion="";
    private String asunto="";
    private String clasificacion="";
    //private String nombre="";
    //private String primerApellido="";
    //private String segundoApellido="";
    //private String nif="";
    private String numColegiado="";
    private String nombreDenunciado="";
    private String primerApellidoDenunciado="";
    private String segundoApellidoDenunciado="";
    private String nifDenunciado=""; 
    private String nColDenunciado=""; 
    private String nombreDenunciante="";
    private String primerApellidoDenunciante="";
    private String segundoApellidoDenunciante="";
    private String nifDenunciante=""; 
    private String idPersonaDenunciante="";
    private String idPersonaDenunciado=""; 
    private String nColDenunciante=""; 
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
    //private String idPersona="";
    private String colegioOrigen="";
    private String colegioOrigenDenunciante="";
    private String idPretension="";
    private String otrasPretensiones="";
     
    private String fechaCaducidad="";
    private String observaciones="";
    private String minuta="";
    private String importeIVA="";
    private String importeTotal="";
    private String porcentajeIVA="";
    private String porcentajeIVAFinal="";
    private String idDireccionDenunciante;
    private String idDireccionDenunciado;
    private String idInstitucionOrigenDenunciante;
    private String idInstitucionOrigenDenunciado;
    private String minutaFinal="";
    private String importeIVAFinal="";
    private String importeTotalFinal="";
    private String derechosColegiales="";
    private String solicitanteEjgNif;
    private String solicitanteEjgApellido1;
    private String solicitanteEjgApellido2;
    private String solicitanteEjgNombre;
    private String relacionExpediente;
    private String nombreRelacionExpediente;    
    private String copia;
    private String campo;
    private String idTipoIdentificacionDenunciado="";
    private String idTipoIdentificacionDenunciante="";
    
	public String getCopia() {
		return copia;
	}
	public void setCopia(String copia) {
		this.copia = copia;
	}
	public String getRelacionExpediente() {
		return relacionExpediente;
	}
	public void setRelacionExpediente(String relacionExpediente) {
		this.relacionExpediente = relacionExpediente;
	}
	public String getNombreRelacionExpediente() {
		return nombreRelacionExpediente;
	}
	public void setNombreRelacionExpediente(String nombreRelacionExpediente) {
		this.nombreRelacionExpediente = nombreRelacionExpediente;
	}
	public String getIdturnoDesignado() {
		return idturnoDesignado;
	}
	public void setIdturnoDesignado(String idturnoDesignado) {
		this.idturnoDesignado = idturnoDesignado;
	}
    public String getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(String idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	
	public String getNumExpDisciplinarioCalc() {
		return numExpDisciplinarioCalc;
	}
	public void setNumExpDisciplinarioCalc(String numExpDisciplinarioCalc) {
		this.numExpDisciplinarioCalc = numExpDisciplinarioCalc;
	}

    public String getTipoExpDisciplinario() {
		return tipoExpDisciplinario;
	}
	public void setTipoExpDisciplinario(String tipoExpDisciplinario) {
		this.tipoExpDisciplinario = tipoExpDisciplinario;
	}
    public String getDerechosColegiales() {
		return derechosColegiales;
	}
	public void setDerechosColegiales(String derechosColegiales) {
		this.derechosColegiales = derechosColegiales;
	}
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
	
	public String getMinutaFinal() {
		return minutaFinal;
	}
	public void setMinutaFinal(String minutaFinal) {
		this.minutaFinal = minutaFinal;
	}
	public String getImporteIVAFinal() {
		return importeIVAFinal;
	}
	public void setImporteIVAFinal(String importeIVAFinal) {
		this.importeIVAFinal = importeIVAFinal;
	}
	public String getImporteTotalFinal() {
		return importeTotalFinal;
	}
	public void setImporteTotalFinal(String importeTotalFinal) {
		this.importeTotalFinal = importeTotalFinal;
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
	
	public String getPorcentajeIVAFinal() {
		return porcentajeIVAFinal;
	}
	public void setPorcentajeIVAFinal(String valor) {
		this.porcentajeIVAFinal = valor;
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
    
    
    
	
	
	
	/*
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	*/
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
		if (!juzgado.startsWith("{")){
			StringTokenizer st = new StringTokenizer(juzgado, ",");
			if (st.hasMoreTokens()) {
				this.juzgado = st.nextToken();
				if (st.hasMoreTokens()) {
					this.idInstitucionJuzgado = st.nextToken();
				} else {
					this.idInstitucionJuzgado="";
				}
			} else {
				this.juzgado="";
				this.idInstitucionJuzgado="";
			}
		} else {
			// JSON
			try {
				final JSONObject juzgadoJSON = new JSONObject(juzgado);
				this.juzgado=(String) juzgadoJSON.get("idjuzgado");
				this.idInstitucionJuzgado=(String) juzgadoJSON.get("idinstitucion");
			} catch (Exception e) {				
			}
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
		if (valor.startsWith("{")){
			try {
				final JSONObject idmateriaJSON = new JSONObject(valor);
				this.idMateria=(String) idmateriaJSON.get("idmateria");
				this.idArea=(String) idmateriaJSON.get("idarea");
			} catch (Exception e) {		
			}
		} else {
			StringTokenizer st = new StringTokenizer(valor, ",");
			if (st.hasMoreTokens()){
				st.nextToken();
				if (st.hasMoreTokens()){
					this.idArea = st.nextToken();
					if (st.hasMoreTokens()) {
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
	/*
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
	*/
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
	/*
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	*/
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		if (procedimiento.startsWith("{")){
			try {
				final JSONObject procedimientoJSON = new JSONObject(procedimiento);
				this.procedimiento=(String) procedimientoJSON.get("idprocedimiento");
				this.idInstitucionProcedimiento=(String) procedimientoJSON.get("idinstitucion");
			} catch (Exception e) {		
			}
		} else {
			StringTokenizer st = new StringTokenizer(procedimiento, ",");
			if (st.hasMoreTokens()) {
				this.procedimiento = st.nextToken();
				if (st.hasMoreTokens()) {
					this.idInstitucionProcedimiento = st.nextToken();
				} else {
					this.idInstitucionProcedimiento="";
				}
			} else {
				this.procedimiento="";
				this.idInstitucionProcedimiento="";
			}
		}
	}
	public String getIdInstitucionProcedimiento() {
		return idInstitucionProcedimiento;
	}
	public void setIdInstitucionProcedimiento(String idInstitucionProcedimiento) {
		this.idInstitucionProcedimiento = idInstitucionProcedimiento;
	}
	/*
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	*/
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
			if (comboEstados.startsWith("{")){
				try {
					final JSONObject comboEstadosJSON = new JSONObject(comboEstados);
					this.setEstado((String) comboEstadosJSON.get("idestado"));
				} catch (Exception e) {				
				}
			} else {
		    	StringTokenizer st = new StringTokenizer(comboEstados,",");
		    	st.nextToken();//idinstitucion_tipoexpediente
		    	st.nextToken();//idtipoexpediente
		    	st.nextToken();//idfase
		    	this.setEstado(st.nextToken()); 
			}
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
	    	if (comboFases.startsWith("{")){
				try {
					final JSONObject comboFasesJSON = new JSONObject(comboFases);
					this.setFase((String) comboFasesJSON.get("idfase"));
				} catch (Exception e) {		
				}
			} else {
		    	StringTokenizer st = new StringTokenizer(comboFases,",");
		    	st.nextToken();//idinstitucion_tipoexpediente
		    	st.nextToken();//idtipoexpediente
		    	this.setFase(st.nextToken());
			}
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
	/*
	public String getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}
	*/
	public String getNombreDenunciante() {
		return nombreDenunciante;
	}
	public void setNombreDenunciante(String nombreDenunciante) {
		this.nombreDenunciante = nombreDenunciante;
	}
	public String getPrimerApellidoDenunciante() {
		return primerApellidoDenunciante;
	}
	public void setPrimerApellidoDenunciante(String primerApellidoDenunciante) {
		this.primerApellidoDenunciante = primerApellidoDenunciante;
	}
	public String getSegundoApellidoDenunciante() {
		return segundoApellidoDenunciante;
	}
	public void setSegundoApellidoDenunciante(String segundoApellidoDenunciante) {
		this.segundoApellidoDenunciante = segundoApellidoDenunciante;
	}
	public String getNifDenunciante() {
		return nifDenunciante;
	}
	public void setNifDenunciante(String nifDenunciante) {
		this.nifDenunciante = nifDenunciante;
	}
	public String getIdPersonaDenunciante() {
		return idPersonaDenunciante;
	}
	public void setIdPersonaDenunciante(String idPersonaDenunciante) {
		this.idPersonaDenunciante = idPersonaDenunciante;
	}
	public String getnColDenunciante() {
		return nColDenunciante;
	}
	public void setnColDenunciante(String nColDenunciante) {
		this.nColDenunciante = nColDenunciante;
	}
	public String getSolicitanteEjgNif() {
		return solicitanteEjgNif;
	}
	public void setSolicitanteEjgNif(String solicitanteEjgNif) {
		this.solicitanteEjgNif = solicitanteEjgNif;
	}
	public String getSolicitanteEjgApellido1() {
		return solicitanteEjgApellido1;
	}
	public void setSolicitanteEjgApellido1(String solicitanteEjgApellido1) {
		this.solicitanteEjgApellido1 = solicitanteEjgApellido1;
	}
	public String getSolicitanteEjgApellido2() {
		return solicitanteEjgApellido2;
	}
	public void setSolicitanteEjgApellido2(String solicitanteEjgApellido2) {
		this.solicitanteEjgApellido2 = solicitanteEjgApellido2;
	}
	public String getSolicitanteEjgNombre() {
		return solicitanteEjgNombre;
	}
	public void setSolicitanteEjgNombre(String solicitanteEjgNombre) {
		this.solicitanteEjgNombre = solicitanteEjgNombre;
	}
	public String getSolicitanteEjgDescripcion() {
		StringBuffer solicitanteEjgDescripcion =  new StringBuffer("");
		if(solicitanteEjgNombre!=null && !solicitanteEjgNombre.equals("")){
			if(solicitanteEjgNif!=null){
				solicitanteEjgDescripcion.append(solicitanteEjgNif);
				solicitanteEjgDescripcion.append(" ");
			}
			solicitanteEjgDescripcion.append(solicitanteEjgNombre);
			solicitanteEjgDescripcion.append(" ");
			solicitanteEjgDescripcion.append(solicitanteEjgApellido1);
			if(solicitanteEjgApellido2!=null){
				solicitanteEjgDescripcion.append(" ");
				solicitanteEjgDescripcion.append(solicitanteEjgApellido2);
			}
			
		}
		return solicitanteEjgDescripcion.toString();
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getIdDireccionDenunciante() {
		return idDireccionDenunciante;
	}
	public void setIdDireccionDenunciante(String idDireccionDenunciante) {
		this.idDireccionDenunciante = idDireccionDenunciante;
	}
	public String getColegioOrigen() {
		return colegioOrigen;
	}
	public void setColegioOrigen(String colegioOrigen) {
		this.colegioOrigen = colegioOrigen;
	}
	public String getColegioOrigenDenunciante() {
		return colegioOrigenDenunciante;
	}
	public void setColegioOrigenDenunciante(String colegioOrigenDenunciante) {
		this.colegioOrigenDenunciante = colegioOrigenDenunciante;
	}
	public String getIdPersonaDenunciado() {
		return idPersonaDenunciado;
	}
	public void setIdPersonaDenunciado(String idPersonaDenunciado) {
		this.idPersonaDenunciado = idPersonaDenunciado;
	}
	public String getIdDireccionDenunciado() {
		return idDireccionDenunciado;
	}
	public void setIdDireccionDenunciado(String idDireccionDenunciado) {
		this.idDireccionDenunciado = idDireccionDenunciado;
	}
	public String getIdInstitucionOrigenDenunciante() {
		return idInstitucionOrigenDenunciante;
	}
	public void setIdInstitucionOrigenDenunciante(
			String idInstitucionOrigenDenunciante) {
		this.idInstitucionOrigenDenunciante = idInstitucionOrigenDenunciante;
	}
	public String getIdInstitucionOrigenDenunciado() {
		return idInstitucionOrigenDenunciado;
	}
	public void setIdInstitucionOrigenDenunciado(
			String idInstitucionOrigenDenunciado) {
		this.idInstitucionOrigenDenunciado = idInstitucionOrigenDenunciado;
	}
	public String getNombreDenunciado() {
		return nombreDenunciado;
	}
	public void setNombreDenunciado(String nombreDenunciado) {
		this.nombreDenunciado = nombreDenunciado;
	}
	public String getPrimerApellidoDenunciado() {
		return primerApellidoDenunciado;
	}
	public void setPrimerApellidoDenunciado(String primerApellidoDenunciado) {
		this.primerApellidoDenunciado = primerApellidoDenunciado;
	}
	public String getSegundoApellidoDenunciado() {
		return segundoApellidoDenunciado;
	}
	public void setSegundoApellidoDenunciado(String segundoApellidoDenunciado) {
		this.segundoApellidoDenunciado = segundoApellidoDenunciado;
	}
	public String getNifDenunciado() {
		return nifDenunciado;
	}
	public void setNifDenunciado(String nifDenunciado) {
		this.nifDenunciado = nifDenunciado;
	}
	public String getnColDenunciado() {
		return nColDenunciado;
	}
	public void setnColDenunciado(String nColDenunciado) {
		this.nColDenunciado = nColDenunciado;
	}
	public String getIdTipoIdentificacionDenunciado() {
		return idTipoIdentificacionDenunciado;
	}
	public void setIdTipoIdentificacionDenunciado(String idTipoIdentificacionDenunciado) {
		this.idTipoIdentificacionDenunciado = idTipoIdentificacionDenunciado;
	}
	public String getIdTipoIdentificacionDenunciante() {
		return idTipoIdentificacionDenunciante;
	}
	public void setIdTipoIdentificacionDenunciante(String idTipoIdentificacionDenunciante) {
		this.idTipoIdentificacionDenunciante = idTipoIdentificacionDenunciante;
	}
	/**
	 * @return the numEJGDisciplinario
	 */
	public String getNumEJGDisciplinario() {
		return numEJGDisciplinario;
	}
	/**
	 * @param numEJGDisciplinario the numEJGDisciplinario to set
	 */
	public void setNumEJGDisciplinario(String numEJGDisciplinario) {
		this.numEJGDisciplinario = numEJGDisciplinario;
	}
	
}
