/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentesJuridicosForm extends MasterForm{

//	metodos set de los campos del formulario
	public void setModo(String modo) {
		UtilidadesHash.set(this.datos, "MODO", modo);		
	}
	
	public void setNifcif(String nifcif) {
		UtilidadesHash.set(this.datos, CenPersonaBean.C_NIFCIF, nifcif);
	}
	
	public void setNombre(String nombre) {
		UtilidadesHash.set(this.datos, CenPersonaBean.C_NOMBRE, nombre);		
	}
	public void setApellidos1(String apellidos1) {
		UtilidadesHash.set(this.datos, CenPersonaBean.C_APELLIDOS1, apellidos1);		
	}
	public void setApellidos2(String apellidos2) {
		UtilidadesHash.set(this.datos, CenPersonaBean.C_APELLIDOS2, apellidos2);		
	}
	
	public void setCargo(String cargo) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_CARGO, cargo);	
	}
	
	public void setFechaCargo(String fechaCargo) {		
		try {
			fechaCargo = GstDate.getApplicationFormatDate("",fechaCargo);
			UtilidadesHash.set(this.datos,CenComponentesBean.C_FECHACARGO, fechaCargo);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSociedad(Boolean sociedad) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_SOCIEDAD, sociedad);
	}
	public void setprofesional(String profesional) {
		UtilidadesHash.set(this.datos, "profesional", profesional);
	}
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDPERSONA, dato);
	}

	public void setIdInstitucion(Integer dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDINSTITUCION, dato);
	}

	public void setIdComponente(Integer dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDCOMPONENTE, dato);
	}
	
	public void setClienteIdPersona(String dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, dato);
	}

	public void setClienteIdInstitucion(Integer dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION, dato);
	}
	
	public void setMotivo(String motivo) {
		UtilidadesHash.set(this.datos, "_MOTIVO_", motivo);
	}

	public void setIdCuenta(Integer dato) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDCUENTA, dato);
	}
	public void setIdTipoColegio(String idtipocolegio) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDTIPOCOLEGIO, idtipocolegio);
	}
	public void setNumColegiado(String numcolegiado) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_NUMCOLEGIADO, numcolegiado);
	}
	public void setIdCargo(String idcargo) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDCARGO, idcargo);
	}
	public void setIdProvincia(String idprovincia) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_IDPROVINCIA, idprovincia);
	}
	public void setCapitalSocial(Float capitalsocial) {
		UtilidadesHash.set(this.datos, CenComponentesBean.C_CAPITALSOCIAL, capitalsocial);
	}
	


//	metodos get de los campos del formulario
	
	public String getModo() {	
		return (String)this.datos.get("MODO");
	}	
	public String getNifcif() {
		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NIFCIF);
	}
	
	public String getNombre() {	
		return (String)this.datos.get(CenPersonaBean.C_NOMBRE);
	}	
	public String getApellidos1() {	
		return (String)this.datos.get(CenPersonaBean.C_APELLIDOS1);
	}
	public String getApellidos2() {	
		return (String)this.datos.get(CenPersonaBean.C_APELLIDOS2);
	}	

	public String getCargo() {
		return (String)this.datos.get(CenComponentesBean.C_CARGO);
	}
	
	public String getFechaCargo() {
		return (String)this.datos.get(CenComponentesBean.C_FECHACARGO);
	}
			
	public Boolean getSociedad() {
		return UtilidadesHash.getBoolean(this.datos, CenComponentesBean.C_SOCIEDAD);
	}
	public String getprofesional() {
		return UtilidadesHash.getString(this.datos, "profesional");
	}
	
	public Long getIdPersona() {
		return UtilidadesHash.getLong(this.datos, CenComponentesBean.C_IDPERSONA);
	}

	public Integer getIdInstitucion() {
		return UtilidadesHash.getInteger(this.datos, CenComponentesBean.C_IDINSTITUCION);
	}

	public Integer getIdComponente() {
		return UtilidadesHash.getInteger(this.datos, CenComponentesBean.C_IDCOMPONENTE);
	}

	public String getClienteIdPersona() {
		return UtilidadesHash.getString(this.datos, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA);
	}

	public Integer getClienteIdInstitucion() {
		return UtilidadesHash.getInteger(this.datos, CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION);
	}

	public String getMotivo() {
		return UtilidadesHash.getString(this.datos, "_MOTIVO_");
	}

	public Integer getIdCuenta() {
		return UtilidadesHash.getInteger(this.datos, CenComponentesBean.C_IDCUENTA);
	}
	
	public String getIdTipoColegio() {
		return UtilidadesHash.getString(this.datos, CenComponentesBean.C_IDTIPOCOLEGIO);
	}
	public String getNumColegiado() {
		return UtilidadesHash.getString(this.datos, CenComponentesBean.C_NUMCOLEGIADO);
	}
	public String getIdCargo() {
		return UtilidadesHash.getString(this.datos, CenComponentesBean.C_IDCARGO);
	}
	public String getIdProvincia() {
		return UtilidadesHash.getString(this.datos, CenComponentesBean.C_IDPROVINCIA);
	}
	
	public Float getCapitalSocial() {
		return UtilidadesHash.getFloat(this.datos, CenComponentesBean.C_CAPITALSOCIAL);
	}

}	