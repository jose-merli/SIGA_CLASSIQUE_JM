package com.siga.envios.form;

import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisional;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;


public class DesignaProvisionalForm extends IntercambioTelematicoForm
{
	/**
	 * 
	 */
	private String designaCodigo;
	private String designaFecha;
	private String designaTurno;
	private String designaInteresados;
	
	private String ejgCodigo;
	private String ejgFecha;
	private String ejgSolicitantes;
	private String ejgEstado;
	
	private String abogadoDesignado;
	
	private String procuradorDesignado;

	public DesignaProvisionalForm(EcomDesignaprovisional ecomDesignaProvisional) {
		if(ecomDesignaProvisional.getFechapeticion()!=null){
			try {
				this.setFechaPeticion(GstDate.getFormatedDateLong("", ecomDesignaProvisional.getFechapeticion()));
			} catch (ClsExceptions e) {
				e.printStackTrace();
			}
		}
	}

	public String getDesignaCodigo() {
		return designaCodigo;
	}

	public void setDesignaCodigo(String designaCodigo) {
		this.designaCodigo = designaCodigo;
	}

	public String getDesignaFecha() {
		return designaFecha;
	}

	public void setDesignaFecha(String designaFecha) {
		this.designaFecha = designaFecha;
	}

	public String getDesignaTurno() {
		return designaTurno;
	}

	public void setDesignaTurno(String designaTurno) {
		this.designaTurno = designaTurno;
	}

	public String getDesignaInteresados() {
		return designaInteresados;
	}

	public void setDesignaInteresados(String designaInteresados) {
		this.designaInteresados = designaInteresados;
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

	public String getProcuradorDesignado() {
		return procuradorDesignado;
	}

	public void setProcuradorDesignado(String procuradorDesignado) {
		this.procuradorDesignado = procuradorDesignado;
	}

	public String getAbogadoDesignado() {
		return abogadoDesignado;
	}

	public void setAbogadoDesignado(String abogadoDesignado) {
		this.abogadoDesignado = abogadoDesignado;
	}
	
	
	
	
	
	
	

	
	
}