package com.siga.envios.form;

import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisional;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimiento;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;


public class SolSusProcedimientoForm extends IntercambioTelematicoForm
{
	/**
	 * 
	 */
	
	
	private String ejgCodigo;
	private String ejgFecha;
	private String ejgSolicitantes;
	private String ejgEstado;
	
	

	public SolSusProcedimientoForm(EcomSolsusprocedimiento ecomSolsusprocedimiento) {
		if(ecomSolsusprocedimiento.getFechapeticion()!=null){
			try {
				this.setFechaPeticion(GstDate.getFormatedDateLong("", ecomSolsusprocedimiento.getFechapeticion()));
			} catch (ClsExceptions e) {
				e.printStackTrace();
			}
		}
	}

	

	
	public String getEjgCodigo() {
		return ejgCodigo;
	}

	public void setEjgCodigo(String ejgCodigo) {
		this.ejgCodigo = ejgCodigo;
	}

	public String getEjgFecha() {
		return ejgFecha;
	}

	public void setEjgFecha(String ejgFecha) {
		this.ejgFecha = ejgFecha;
	}

	public String getEjgSolicitantes() {
		return ejgSolicitantes;
	}

	public void setEjgSolicitantes(String ejgSolicitantes) {
		this.ejgSolicitantes = ejgSolicitantes;
	}

	public String getEjgEstado() {
		return ejgEstado;
	}

	public void setEjgEstado(String ejgEstado) {
		this.ejgEstado = ejgEstado;
	}

	
	
	
	

	
	
}