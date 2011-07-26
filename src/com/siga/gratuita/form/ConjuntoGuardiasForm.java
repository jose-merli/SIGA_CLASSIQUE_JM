package com.siga.gratuita.form;

import java.util.List;

import com.siga.general.MasterForm;
import com.siga.gratuita.beans.ScsConjuntoGuardiasBean;

/**
 * 
 * @author jorgeta
 *
 */
public class ConjuntoGuardiasForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idConjuntoGuardia;
	private String idInstitucion;
	private String descripcion;
	
	private String guardiasInsertar;
	private List<ConfConjuntoGuardiasForm> confConjuntoGuardia;
	public String getIdConjuntoGuardia() {
		return idConjuntoGuardia;
	}
	public void setIdConjuntoGuardia(String idConjuntoGuardia) {
		this.idConjuntoGuardia = idConjuntoGuardia;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public ScsConjuntoGuardiasBean getConjuntoGuardiaVO() {
		ScsConjuntoGuardiasBean conjuntoGuardiasBean = new ScsConjuntoGuardiasBean();
		if(idConjuntoGuardia!=null&&!idConjuntoGuardia.equals(""))
			conjuntoGuardiasBean.setIdConjuntoGuardia(new Long(idConjuntoGuardia));
		conjuntoGuardiasBean.setDescripcion(descripcion);
		if(idInstitucion!=null&&!idInstitucion.equals(""))
			conjuntoGuardiasBean.setIdInstitucion(new Integer(idInstitucion));
		return conjuntoGuardiasBean;
	}
	public List<ConfConjuntoGuardiasForm> getConfConjuntoGuardia() {
		return confConjuntoGuardia;
	}
	public void setConfConjuntoGuardia(List<ConfConjuntoGuardiasForm> confConjuntoGuardia) {
		this.confConjuntoGuardia = confConjuntoGuardia;
	}
	
	public String getGuardiasInsertar() {
		return guardiasInsertar;
	}
	public void setGuardiasInsertar(String guardiasInsertar) {
		this.guardiasInsertar = guardiasInsertar;
	}
	
}
