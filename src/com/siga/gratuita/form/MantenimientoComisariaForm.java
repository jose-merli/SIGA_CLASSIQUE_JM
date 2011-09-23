package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsComisariaBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoComisariaForm extends MasterForm {
	
	String ponerBaja;
	
	
	// METODOS SET
	public void setIdInstitucionComisaria(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_IDINSTITUCION , dato);
	}
	public void setIdComisaria(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_IDCOMISARIA , dato);
	}
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_NOMBRE , dato);
	}
	public void setNombreBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "buscarNombre" , dato);
	}
	public void setDireccion(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_DIRECCION , dato);
	}
	public void setCodigoPostal(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_CODIGOPOSTAL , dato);
	}
	public void setCodigoExt(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_CODIGOEXT , dato);
	}
	public void setCodigoExtBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "buscarCodigoExt" , dato);
	}
	public void setIdPoblacion(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_IDPOBLACION , dato);
	}
	public void setIdProvincia(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_IDPROVINCIA , dato);
	}
	public void setTelefono1(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_TELEFONO1 , dato);
	}
	public void setTelefono2(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_TELEFONO2 , dato);
	}
	public void setFax1(String dato) {
		UtilidadesHash.set(this.datos, ScsComisariaBean.C_FAX1 , dato);
	}
	public void setPoblacion(String dato) {
		UtilidadesHash.set(this.datos, "POBLACION", dato);
	}
	public void setProvincia(String dato) {
		UtilidadesHash.set(this.datos, "PROVINCIA", dato);
	}
	public void setNombreObjetoDestino(String dato) {
		UtilidadesHash.set(this.datos, "NOMBRE_OBJETO_DESTINOP" , dato);
	}
	
	
	// METODOS GET
	public String getNombre() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_NOMBRE);
	}
	public String getNombreBusqueda() {
		return UtilidadesHash.getString(this.datos, "buscarNombre");
	}
	public String getDireccion() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_DIRECCION);
	}	
	public String getCodigoPostal() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_CODIGOPOSTAL);
	}
	public String getCodigoExtBusqueda() {
		return UtilidadesHash.getString(this.datos, "buscarCodigoExt");
	}
	public String getCodigoExt() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_CODIGOEXT);
	}
	public String getIdPoblacion() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_IDPOBLACION);		
	}
	public String getIdProvincia() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_IDPROVINCIA);
	}
	public String getTelefono1() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_TELEFONO1);
	}	
	public String getTelefono2() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_TELEFONO2);
	}
	public String getFax1() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_FAX1);
	}
	public String getPoblacion() {
		return UtilidadesHash.getString(this.datos, "POBLACION");		
	}
	public String getProvincia() {
		return UtilidadesHash.getString(this.datos, "PROVINCIA");
	}
	public String getIdInstitucionComisaria() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_IDINSTITUCION);
	}
	public String getIdComisaria() {
		return UtilidadesHash.getString(this.datos, ScsComisariaBean.C_IDCOMISARIA);
	}
	
	public String getNombreObjetoDestino() {
		return UtilidadesHash.getString(this.datos, "NOMBRE_OBJETO_DESTINOP");
	}
	public String getPonerBaja() {
		return ponerBaja;
	}
	public void setPonerBaja(String ponerBaja) {
		this.ponerBaja = ponerBaja;
	}

}