package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsJuzgadoProcedimientoBean;
import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoJuzgadoForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdInstitucionJuzgado(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG , dato);
	}
	public void setIdProcedimiento(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO , dato);
	}
	public void setIdInstitucionProcedimiento(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoProcedimientoBean.C_IDINSTITUCION , dato);
	}
	public void setIdJuzgado(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_IDJUZGADO , dato);
	}
	public void setProcedimiento(String dato) {
		UtilidadesHash.set(this.datos, "PROCEDIMIENTO" , dato);
	}
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_NOMBRE , dato);
	}
	public void setNombreBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "buscarNombre" , dato);
	}
	public void setCodigoExt(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_CODIGOEXT , dato);
	}
	public void setCodProcurador(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_CODPROCURADOR , dato);
	}
	public void setCodigoExtBusqueda(String dato) {
		UtilidadesHash.set(this.datos, "buscarCodigoExt" , dato);
	}
	public void setDireccion(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_DIRECCION , dato);
	}
	public void setCodigoPostal(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_CODIGOPOSTAL , dato);
	}
	public void setIdPoblacion(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_IDPOBLACION , dato);
	}
	public void setIdProvincia(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_IDPROVINCIA , dato);
	}
	public void setTelefono1(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_TELEFONO1 , dato);
	}
	public void setTelefono2(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_TELEFONO2 , dato);
	}
	public void setFax1(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_FAX1 , dato);
	}
	public void setPoblacion(String dato) {
		UtilidadesHash.set(this.datos, "POBLACION", dato);
	}
	public void setProvincia(String dato) {
		UtilidadesHash.set(this.datos, "PROVINCIA", dato);
	}
	public void setVisible(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_VISIBLE , dato);
	}
	public void setNombreObjetoDestino(String dato) {
		UtilidadesHash.set(this.datos, "NOMBRE_OBJETO_DESTINOP" , dato);
	}
	public void setMovil(String dato) {
		UtilidadesHash.set(this.datos, ScsJuzgadoBean.C_MOVIL, dato);
	}
	
	
	// METODOS GET	
	public String getIdProcedimiento() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO);
	}
	public String getIdInstitucionProcedimiento() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoProcedimientoBean.C_IDINSTITUCION);
	}
	public String getIdInstitucionJuzgado() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoProcedimientoBean.C_IDINSTITUCION_JUZG);
	}
	public String getNombre() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_NOMBRE);
	}
	public String getNombreBusqueda() {
		return UtilidadesHash.getString(this.datos, "buscarNombre");
	}
	public String getCodigoExt() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_CODIGOEXT);
	}
	public String getCodProcurador() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_CODPROCURADOR);
	}
	public String getCodigoExtBusqueda() {
		return UtilidadesHash.getString(this.datos, "buscarCodigoExt");
	}
	public String getProcedimiento() {
		return UtilidadesHash.getString(this.datos, "PROCEDIMIENTO");
	}	
	public String getDireccion() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_DIRECCION);
	}	
	public String getCodigoPostal() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_CODIGOPOSTAL);
	}
	public String getIdPoblacion() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_IDPOBLACION);		
	}
	public String getIdProvincia() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_IDPROVINCIA);
	}
	public String getTelefono1() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_TELEFONO1);
	}	
	public String getTelefono2() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_TELEFONO2);
	}
	public String getFax1() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_FAX1);
	}
	public String getPoblacion() {
		return UtilidadesHash.getString(this.datos, "POBLACION");		
	}
	public String getProvincia() {
		return UtilidadesHash.getString(this.datos, "PROVINCIA");
	}
	public String getIdJuzgado() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_IDJUZGADO);
	}
	public String getVisible() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_VISIBLE);
	}
	public String getNombreObjetoDestino() {
		return UtilidadesHash.getString(this.datos, "NOMBRE_OBJETO_DESTINOP");
	}
	public String getMovil() {
		return UtilidadesHash.getString(this.datos, ScsJuzgadoBean.C_MOVIL);
	}
}