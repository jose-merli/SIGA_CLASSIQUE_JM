package com.siga.gratuita.beans;

import com.atos.utils.UsrBean;
import com.siga.beans.MasterBean;
import com.siga.gratuita.form.ConjuntoGuardiasForm;

/**
 * 
 * @author jorgeta
 *
 */
public class ScsConjuntoGuardiasBean extends MasterBean{

	  
	
	static public String T_NOMBRETABLA = "SCS_CONJUNTOGUARDIAS";
	static public final String SEQ_SCS_CONJUNTO_GUARDIAS = "SEQ_SCSCONJUNTOGUARDIAS";
	UsrBean usrBean;

	// Nombre campos de la tabla
	static public final String C_IDCONJUNTOGUARDIA = "IDCONJUNTOGUARDIA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	
	private Long idConjuntoGuardia;
	private Integer idInstitucion;
	private String descripcion;
	public Long getIdConjuntoGuardia() {
		return idConjuntoGuardia;
	}
	public void setIdConjuntoGuardia(Long idConjuntoGuardia) {
		this.idConjuntoGuardia = idConjuntoGuardia;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public ConjuntoGuardiasForm getConjuntoGuardiaForm() {
		ConjuntoGuardiasForm conjuntoGuardiasForm = new ConjuntoGuardiasForm();
		if(idConjuntoGuardia!=null)
			conjuntoGuardiasForm.setIdConjuntoGuardia(idConjuntoGuardia.toString());
		conjuntoGuardiasForm.setDescripcion(descripcion);
		if(idInstitucion!=null)
			conjuntoGuardiasForm.setIdInstitucion(idInstitucion.toString());
		return conjuntoGuardiasForm;
	}
	
	
	

	
}
