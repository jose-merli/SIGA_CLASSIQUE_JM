//VERSIONES
//emilio.grau 27-12-2004 Creacion
//

package com.siga.expedientes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso BUSCAR EXPEDIENTE
 * @author AtosOrigin 27-12-2004
 */
public class BusquedaExpedientesForm extends MasterForm {
    
	String orden;
	String tipoOrden;
	

	
	// Metodos Set (Formulario (*.jsp))	

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 

	//Para los combos que devuelven más de un dato separados por comas---
	public void setComboTipoExpediente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"ComboTipoExpediente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public void setComboFases (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"ComboFases", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public void setComboEstados (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"ComboEstados", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
	public void setComboMaterias (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"materiaarea", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
	public void setComboJuzgados (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"comboJuzgadosMateria", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	//-------- Fin combos
	
	public void setAvanzada (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Avanzada", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
	public void setEsGeneral (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"EsGeneral", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
	public void setTipoExpediente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"TipoExpediente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setInstitucion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Institucion", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setNumeroExpediente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"NumeroExpediente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setAnioExpediente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"AnioExpediente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setNumeroExpDisciplinario (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"NumeroExpDisciplinario", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setAnioExpDisciplinario (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"AnioExpDisciplinario", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setFecha (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Fecha", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setNombre (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Nombre", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setPrimerApellido (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"PrimerApellido", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setSegundoApellido (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"SegundoApellido", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setNombreDenunciante  (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"nombreDenunciante", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setPrimerApellidoDenunciante  (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"primerApellidoDenunciante", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setSegundoApellidoDenunciante (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"segundoApellidoDenunciante", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA AVANZADA 

 	public void setFase (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Fase", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setEstado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Estado", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	} 
 	
 	public void setMateriaSel (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"materiaSel", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setJuzgadoSel (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"juzgadoSel", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setNombreParte (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"NombreParte", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setPrimerApellidoParte (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"PrimerApellidoParte", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setSegundoApellidoParte (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"SegundoApellidoParte", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setRol (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Rol", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
	// Metodos Get 1 por campo Formulario (*.jsp)
 	
	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 

 	public String getAvanzada	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Avanzada");		
 	}
 	
 	public String getEsGeneral	() 	{ 
 		return UtilidadesHash.getString(this.datos, "EsGeneral");		
 	}
 	
 	public String getTipoExpediente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "TipoExpediente");		
 	}

 	public String getInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Institucion");		
 	}
 	
 	public String getNumeroExpediente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NumeroExpediente");		
 	}
 	
 	public String getAnioExpediente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "AnioExpediente");		
 	}

 	public String getNumeroExpDisciplinario	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NumeroExpDisciplinario");		
 	}
 	
 	public String getAnioExpDisciplinario	() 	{ 
 		return UtilidadesHash.getString(this.datos, "AnioExpDisciplinario");		
 	}
 	
 	public String getFecha () 	{ 
 		return UtilidadesHash.getString(this.datos, "Fecha");		
 	}

 	public String getNombre () 	{ 
 		return UtilidadesHash.getString(this.datos, "Nombre");		
 	}

 	public String getPrimerApellido	() 	{ 
 		return UtilidadesHash.getString(this.datos, "PrimerApellido");		
 	}

 	public String getSegundoApellido	() 	{ 
 		return UtilidadesHash.getString(this.datos, "SegundoApellido");		
 	}
 	public String getNombreDenunciante () 	{ 
 		return UtilidadesHash.getString(this.datos, "nombreDenunciante");		
 	}

 	public String getPrimerApellidoDenunciante	() 	{ 
 		return UtilidadesHash.getString(this.datos, "primerApellidoDenunciante");		
 	}

 	public String getSegundoApellidoDenunciante	() 	{ 
 		return UtilidadesHash.getString(this.datos, "segundoApellidoDenunciante");		
 	}
 	
 	public String getComboTipoExpediente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "ComboTipoExpediente");		
 	}

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA AVANZADA 

 	public String getFase() 	{ 
 		return UtilidadesHash.getString(this.datos, "Fase");		
 	}

 	public String getEstado() 	{ 
 		return UtilidadesHash.getString(this.datos, "Estado");		
 	}
 	
 	public String getMateriaSel() 	{ 
 		return UtilidadesHash.getString(this.datos, "materiaSel");		
 	}
 	
 	public String getJuzgadoSel() 	{ 
 		return UtilidadesHash.getString(this.datos, "juzgadoSel");		
 	}
 	
 	public String getComboFases() 	{ 
 		return UtilidadesHash.getString(this.datos, "ComboFases");		
 	}
 	public String getComboEstados() 	{ 
 		return UtilidadesHash.getString(this.datos, "ComboEstados");		
 	}
 	
 	public String getComboMaterias() 	{ 
 		return UtilidadesHash.getString(this.datos, "materiaarea");		
 	}
 	
 	public String getComboJuzgados() 	{ 
 		return UtilidadesHash.getString(this.datos, "comboJuzgadosMateria");		
 	}
 	
 	public String getNombreParte() 	{ 
 		return UtilidadesHash.getString(this.datos, "NombreParte");		
 	}
 	
 	public String getPrimerApellidoParte() 	{ 
 		return UtilidadesHash.getString(this.datos, "PrimerApellidoParte");		
 	}
 	
 	public String getSegundoApellidoParte() 	{ 
 		return UtilidadesHash.getString(this.datos, "SegundoApellidoParte");		
 	}
 	
 	public String getRol() 	{ 
 		return UtilidadesHash.getString(this.datos, "Rol");		
 	}
 	public void setAsunto (String asunto) { 
 		
 			UtilidadesHash.set(this.datos,"asunto", asunto);
 		
 	}
 	public void setObservaciones (String observaciones) { 
 		
			UtilidadesHash.set(this.datos,"observaciones", observaciones);
		
	}
 	public String getAsunto() 	{ 
 		return UtilidadesHash.getString(this.datos, "asunto");		
 	}
 	public String getObservaciones() 	{ 
 		return UtilidadesHash.getString(this.datos, "observaciones");		
 	}
 	public void setCampoConfigurado (String campoConfigurado) { 
 		
			UtilidadesHash.set(this.datos,"campoConfigurado", campoConfigurado);
		
	}
	public String getCampoConfigurado() 	{ 
		return UtilidadesHash.getString(this.datos, "campoConfigurado");		
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getNumAsunto() {
		return UtilidadesHash.getString(this.datos, "numAsunto");	
	}
	public void setNumAsunto(String numAsunto) {
		UtilidadesHash.set(this.datos,"numAsunto", numAsunto);
	}
	
	public String getJuzgado() {
		return UtilidadesHash.getString(this.datos, "Juzgado");	
	}
	public void setJuzgado(String juzgado) {
		UtilidadesHash.set(this.datos,"Juzgado", juzgado);
	}

 	
	public String getIdArea() {
		return UtilidadesHash.getString(this.datos, "idArea");	
	}
	public void setIdArea(String idArea) {
		UtilidadesHash.set(this.datos,"idArea", idArea);
	}
	
	public String getIdMateria() {
		return UtilidadesHash.getString(this.datos, "idMateria");	
	}
	public void setIdMateria(String idMateria) {
		UtilidadesHash.set(this.datos,"idMateria", idMateria);
	}
	
	// OTRAS FUNCIONES 

}
