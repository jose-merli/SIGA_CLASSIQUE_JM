/*
 * VERSIONES:
 * 
 * miguel.villegas - 21-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento del historico <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;
import com.siga.beans.*;


public class HistoricoForm extends MasterForm{
	
	private String fechaInicio=""; 	// Inicio fecha efectiva en busqueda
	private String fechaFin="";		// Fin fecha efectiva en busqueda
	
	// Metodos set
	
	// Formulario Busqueda Historico y Datos Historico	
	
	public void setCmbCambioHistorico(String v){
		datos.put(CenHistoricoBean.C_IDTIPOCAMBIO ,v);
	}
	
	// Formulario Busqueda Historico 	
	
	public void setFechaInicio(String v){
		this.fechaInicio=v;
	}

	public void setFechaFin(String v){
		this.fechaFin=v;
	}
		
	// Formulario Datos Historico	
	
	public void setMotivo(String mot){
		datos.put(CenHistoricoBean.C_MOTIVO,mot);
	}

	public void setFechaEntrada(String fechaE){
		datos.put(CenHistoricoBean.C_FECHAENTRADA,fechaE);
	}

	public void setFechaEfectiva(String fechaE){
		datos.put(CenHistoricoBean.C_FECHAEFECTIVA,fechaE);
	}

	
	// Metodos get	
	
	// Formulario Busqueda Historico y Datos Historico
	
	public String getCmbCambioHistorico(){
		return (String)datos.get(CenHistoricoBean.C_IDTIPOCAMBIO);
	}

	// Formulario Busqueda Historico	
	
	public String getFechaInicio(){
		return this.fechaInicio;
	}

	public String getFechaFin(){
		return this.fechaFin;
	}
	
	// Formulario Datos Historico	
	
	public String getMotivo(){
		return (String)datos.get(CenHistoricoBean.C_MOTIVO);
	}

	public String getFechaEntrada(){
		return (String)datos.get(CenHistoricoBean.C_FECHAENTRADA);
	}

	public String getFechaEfectiva(){
		return (String)datos.get(CenHistoricoBean.C_FECHAEFECTIVA);
	}
	

}