/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.censo.form.BajasTemporalesForm;

public class CenBajasTemporalesBean extends MasterBean {

	//Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = -2532876089544539337L;
	private Integer idInstitucion;
	private Long idPersona;
	private String tipo;
	private String fechaBT;
	private String fechaDesde;
	private String fechaHasta;
	private String fechaAlta;
	private String descripcion;
	
	private String fechaEstado;
	private String validado;
	
	UsrBean usrBean;
	CenPersonaBean persona;
	
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_FECHABT = "FECHABT";
	static public final String C_TIPO = "TIPO";
	static public final String C_FECHADESDE = "FECHADESDE";
	static public final String C_FECHAHASTA = "FECHAHASTA";
	static public final String C_FECHAALTA = "FECHAALTA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	
	static public final String C_FECHAESTADO = "FECHAESTADO";
	static public final String C_VALIDADO = "VALIDADO";

	static public final String T_NOMBRETABLA = "CEN_BAJASTEMPORALES";
	public static final String TIPO_COD_VACACION = "V";
	public static final String TIPO_COD_MATERNIDAD = "M";
	public static final String TIPO_COD_BAJA = "B";
	public static final String TIPO_DESC_VACACION = "censo.bajastemporales.tipo.vacaciones";
	public static final String TIPO_DESC_BAJA = "censo.bajastemporales.tipo.baja";
	public static final String TIPO_DESC_MATERNIDAD = "censo.bajastemporales.tipo.maternidad";
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaBT() {
		return fechaBT;
	}

	public void setFechaBT(String fechaBT) {
		this.fechaBT = fechaBT;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	public BajasTemporalesForm getBajaTemporalForm(){
		BajasTemporalesForm form = new BajasTemporalesForm();
		form.setIdPersona(idPersona.toString());
		form.setIdInstitucion(idInstitucion.toString());
		if(persona!=null){
			StringBuffer nombre = new StringBuffer();
			nombre.append(persona.getNombre());
			nombre.append(" ");
			nombre.append(persona.getApellido1());
			if(persona.getApellido2()!=null){
				nombre.append(" ");
				nombre.append(persona.getApellido2());
			}
			form.setColegiadoNombre(nombre.toString());
			form.setColegiadoNumero(persona.getColegiado().getNColegiado());
		}
		form.setDescripcion(descripcion);
		form.setTipo(tipo);
		form.setValidado(validado);
		form.setUsrBean(usrBean);
		
		
		try {
			form.setFechaEstado(fechaEstado);
			form.setFechaAlta( fechaAlta);
			form.setFechaDesde(GstDate.getFormatedDateShort("", fechaDesde));
			form.setFechaHasta(GstDate.getFormatedDateShort("", fechaHasta));
			
			
		} catch (Exception e) {
		}
		return form;
		
	}

	public CenPersonaBean getPersona() {
		return persona;
	}

	public void setPersona(CenPersonaBean persona) {
		this.persona = persona;
	}

	public String getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getValidado() {
		return validado;
	}

	public void setValidado(String validado) {
		this.validado = validado;
	}

	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}



}
