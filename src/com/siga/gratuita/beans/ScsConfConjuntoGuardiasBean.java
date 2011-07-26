package com.siga.gratuita.beans;

import com.atos.utils.UsrBean;
import com.siga.beans.MasterBean;
import com.siga.gratuita.form.ConfConjuntoGuardiasForm;
/**
 * 
 * @author jorgeta
 *
 */
public class ScsConfConjuntoGuardiasBean  extends MasterBean{


	static public String T_NOMBRETABLA = "SCS_CONF_CONJUNTO_GUARDIAS";
	UsrBean usrBean;

	// Nombre campos de la tabla
	static public final String C_IDCONJUNTOGUARDIA = "IDCONJUNTOGUARDIA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTURNO = "IDTURNO";
	static public final String C_IDGUARDIA = "IDGUARDIA";
	static public final String C_ORDEN = "ORDEN";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	
	private Long idConjuntoGuardia;
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Short orden;
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
	
	
	
	public Integer getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	public Integer getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	public Short getOrden() {
		return orden;
	}
	public void setOrden(Short orden) {
		this.orden = orden;
	}
	
	public ConfConjuntoGuardiasForm getConfConjuntoGuardiasFormForm() {
		ConfConjuntoGuardiasForm confConjuntoGuardiasForm = new ConfConjuntoGuardiasForm();
		if(idConjuntoGuardia!=null)
			confConjuntoGuardiasForm.setIdConjuntoGuardia(idConjuntoGuardia.toString());
		if(idInstitucion!=null)
			confConjuntoGuardiasForm.setIdInstitucion(idInstitucion.toString());
		if(idTurno!=null)
			confConjuntoGuardiasForm.setIdTurno(idTurno.toString());
		if(idGuardia!=null)
			confConjuntoGuardiasForm.setIdGuardia(idGuardia.toString());
		if(orden!=null)
			confConjuntoGuardiasForm.setOrden(orden.toString());
		
		return confConjuntoGuardiasForm;
	}
	

	
}
