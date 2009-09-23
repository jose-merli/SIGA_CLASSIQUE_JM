//VERSIONES
//emilio.grau 27-12-2004 Creacion
//

package com.siga.expedientes.form;

import java.util.StringTokenizer;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso BUSCAR EXPEDIENTE
* @author AtosOrigin 27-12-2004
*/
public class BusquedaAlertaForm extends MasterForm {
  
	// Metodos Set (Formulario (*.jsp))	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6893980964327158378L;

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE
	//	Propiedad que recojo del combo de Fases y parseo en las propiedades correspondientes
	private String comboFases;
	private String comboEstados="";

	String fechaDesde;
	String fechaHasta;
	String idEstado;
	String idFase;
	String idInstitucion;

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getIdFase() {
		return idFase;
	}

	public void setIdFase(String idFase) {
		this.idFase = idFase;
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
			this.setIdEstado((st.nextToken()));        	
		}else{        	
			this.setIdEstado("");  
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
			this.setIdFase(st.nextToken());        	
		}else{        	
			this.setIdFase("");  
		}
	}

	//Para los combos que devuelven más de un dato separados por comas---
	public void setComboTipoExpediente (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"ComboTipoExpediente", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
		}
	}

	//-------- Fin combos

	public void setTipoExpediente (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"TipoExpediente", dato);
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

	// Metodos Get 1 por campo Formulario (*.jsp)

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 
	public String getTipoExpediente	() 	{ 
		return UtilidadesHash.getString(this.datos, "TipoExpediente");		
	}

	public String getNumeroExpediente	() 	{ 
		return UtilidadesHash.getString(this.datos, "NumeroExpediente");		
	}

	public String getAnioExpediente	() 	{ 
		return UtilidadesHash.getString(this.datos, "AnioExpediente");		
	}

	public String getComboTipoExpediente	() 	{ 
		return UtilidadesHash.getString(this.datos, "ComboTipoExpediente");		
	}

	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
}
