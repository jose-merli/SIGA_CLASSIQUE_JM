package com.siga.beans;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.facturacionSJCS.form.TramosRetencionForm;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_TRAMOSRETENCION
* 
* @author jtacosta
* @since 29/04/2011 
*/

public class FcsTramosRetencionBean extends MasterBean{
	
	/**
	 *  FCS_TRAMOSRETENCION
	 *  (
	 *    IDINSTITUCION     NUMBER(4) not null,
	 *      NTRAMO            NUMBER(2) not null,
	 *        DESCRIPCION       VARCHAR2(250) not null,
	 *          PORCENTAJE        NUMBER(5,2) not null,
	 *            NSMI              NUMBER(3),
	 *              FECHAMODIFICACION DATE not null,
	 *                USUMODIFICACION   NUMBER(5) not null 

	 * */ 
	
	private Double 	porcentaje;
	private Integer		numeroTramo;
	private Integer		idInstitucion;
	private String		descripcion;
	private Integer 	numerosSmi;
	public Integer getNumeroTramo() {
		return numeroTramo;
	}
	public void setNumeroTramo(Integer numeroTramo) {
		this.numeroTramo = numeroTramo;
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
	public Integer getNumerosSmi() {
		return numerosSmi;
	}
	public void setNumerosSmi(Integer numerosSmi) {
		this.numerosSmi = numerosSmi;
	}
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	
	
	
	static public String T_NOMBRETABLA = "FCS_TRAMOSRETENCION";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_NTRAMO = 	"NTRAMO";
	static public final String 	C_DESCRIPCION = "DESCRIPCION";
	static public final String 	C_PORCENTAJE = "PORCENTAJE";
	static public final String 	C_NSMI = "NSMI";
	static public final String 	C_IDINSTITUCION = "IDINSTITUCION";
	 
	public TramosRetencionForm getTramosRetencionForm(){
		TramosRetencionForm form = new TramosRetencionForm();
		form.setIdInstitucion(idInstitucion.toString());
		form.setDescripcion(descripcion);
		form.setNumeroTramo(numeroTramo.toString());
		if(numerosSmi!=null)
			form.setNumerosSmi(numerosSmi.toString());
		else
			form.setNumerosSmi("");
		form.setPorcentaje(UtilidadesNumero.formato(porcentaje));
		return form;
		
	}

	
	
}