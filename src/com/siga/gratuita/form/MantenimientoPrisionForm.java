package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsPrisionBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 17/01/2006
 *
 */
public class MantenimientoPrisionForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdInstitucionPrision(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_IDINSTITUCION , dato);
	}
	public void setIdPrision(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_IDPRISION , dato);
	}
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_NOMBRE , dato);
	}
	public void setNombreBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "buscarNombre" , dato);
	}
	public void setDireccion(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_DIRECCION , dato);
	}
	public void setCodigoPostal(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_CODIGOPOSTAL , dato);
	}
	public void setCodigoExt(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_CODIGOEXT, dato);
	}
	public void setCodigoExtBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "codigoExtBusqueda" , dato);
	}
	public void setIdPoblacion(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_IDPOBLACION , dato);
	}
	public void setIdProvincia(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_IDPROVINCIA , dato);
	}
	public void setTelefono1(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_TELEFONO1 , dato);
	}
	public void setTelefono2(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_TELEFONO2 , dato);
	}
	public void setFax1(String dato) {
		UtilidadesHash.set(this.datos, ScsPrisionBean.C_FAX1 , dato);
	}
	public void setPoblacion(String dato) {
		UtilidadesHash.set(this.datos, "POBLACION", dato);
	}
	public void setProvincia(String dato) {
		UtilidadesHash.set(this.datos, "PROVINCIA", dato);
	}
	
	
	// METODOS GET
	public String getNombre() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_NOMBRE);
	}
	public String getNombreBusqueda() {
		return UtilidadesHash.getString(this.datos, "buscarNombre");
	}
	public String getDireccion() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_DIRECCION);
	}	
	public String getCodigoPostal() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_CODIGOPOSTAL);
	}
	public String getCodigoExt() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_CODIGOEXT);
	}
	public String getCodigoExtBusqueda() {
		return UtilidadesHash.getString(this.datos,"codigoExtBusqueda");
	}
	public String getIdPoblacion() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_IDPOBLACION);		
	}
	public String getIdProvincia() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_IDPROVINCIA);
	}
	public String getTelefono1() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_TELEFONO1);
	}	
	public String getTelefono2() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_TELEFONO2);
	}
	public String getFax1() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_FAX1);
	}
	public String getPoblacion() {
		return UtilidadesHash.getString(this.datos, "POBLACION");		
	}
	public String getProvincia() {
		return UtilidadesHash.getString(this.datos, "PROVINCIA");
	}
	public String getIdInstitucionPrision() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_IDINSTITUCION);
	}
	public String getIdPrision() {
		return UtilidadesHash.getString(this.datos, ScsPrisionBean.C_IDPRISION);
	}

	
}