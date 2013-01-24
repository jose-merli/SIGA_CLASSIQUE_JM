package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsProcuradorBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoProcuradorForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdInstitucionProcurador(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_IDINSTITUCION , dato);
	}
	public void setIdProcurador(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_IDPROCURADOR , dato);
	}
	public void setNColegiado(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_NCOLEGIADO , dato);
	}

	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_NOMBRE , dato);
	}
	public void setApellido1(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_APELLIDO1 , dato);
	}
	public void setApellido2(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_APELLIDO2 , dato);
	}
	public void setDireccion(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_DIRECCION , dato);
	}
	public void setEmail(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_EMAIL , dato);
	}
	public void setCodigoPostal(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_CODIGOPOSTAL , dato);
	}
	public void setIdPoblacion(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_IDPOBLACION , dato);
	}
	public void setIdProvincia(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_IDPROVINCIA , dato);
	}
	public void setTelefono1(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_TELEFONO1 , dato);
	}
	public void setTelefono2(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_TELEFONO2 , dato);
	}
	public void setFax1(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_FAX1 , dato);
	}
	public void setPoblacion(String dato) {
		UtilidadesHash.set(this.datos, "POBLACION", dato);
	}
	public void setProvincia(String dato) {
		UtilidadesHash.set(this.datos, "PROVINCIA", dato);
	}
	public void setCodProcurador(String dato) {
		UtilidadesHash.set(this.datos, ScsProcuradorBean.C_CODPROCURADOR , dato);
	
	}
	
	
	// METODOS GET
	public String getIdInstitucionProcurador() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_IDINSTITUCION);
	}
	public String getIdProcurador() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_IDPROCURADOR);
	}
	public String getNColegiado() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_NCOLEGIADO);
	}

	public String getNombre() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_NOMBRE);
	}
	public String getApellido1() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_APELLIDO1);
	}
	public String getApellido2() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_APELLIDO2);
	}
	public String getDireccion() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_DIRECCION);
	}
	public String getEmail() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_EMAIL);
	}
	public String getCodigoPostal() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_CODIGOPOSTAL);
	}
	public String getIdPoblacion() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_IDPOBLACION);		
	}
	public String getIdProvincia() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_IDPROVINCIA);
	}
	public String getTelefono1() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_TELEFONO1);
	}	
	public String getTelefono2() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_TELEFONO2);
	}
	public String getFax1() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_FAX1);
	}
	public String getPoblacion() {
		return UtilidadesHash.getString(this.datos, "POBLACION");		
	}
	public String getProvincia() {
		return UtilidadesHash.getString(this.datos, "PROVINCIA");
	}

	public String getCodProcurador() {
		return UtilidadesHash.getString(this.datos, ScsProcuradorBean.C_CODPROCURADOR);
	}
}