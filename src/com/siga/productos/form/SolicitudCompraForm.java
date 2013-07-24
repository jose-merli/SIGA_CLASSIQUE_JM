/*
 * Created on Jan 28, 2005 
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.productos.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysServiciosInstitucionBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 * Modificado por david.sanchezp el 9/03/2005 para incluir los set y get del PAN y la Fecha de Caducidad de la Tarjeta.
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SolicitudCompraForm extends MasterForm{
	
	Integer idInstitucionPresentador;

	public Integer getIdInstitucionPresentador() {
		return idInstitucionPresentador;
	}
	public void setIdInstitucionPresentador(Integer idInstitucionPresentador) {
		this.idInstitucionPresentador = idInstitucionPresentador;
	}
	public void setIdPeticion(String dato) {
		UtilidadesHash.set(this.datos, "IDPETICION", dato);		
	}
	public void setTarjeta1(String dato) {
		UtilidadesHash.set(this.datos, "TARJETA1", dato);		
	}	
	public void setFechaEfectivaCompra(String fechaEfectivaCompra) {
		UtilidadesHash.set(this.datos, "FECHAEFECTIVA", fechaEfectivaCompra);		
	}
	
	public void setOperacionOK(String dato) {
		UtilidadesHash.set(this.datos, "OPERACIONOK", dato);		
	}
	public void setNombreProducto(String dato) {
		UtilidadesHash.set(this.datos, "NOMBREPRODUCTO", dato);		
	}
	public void setCategoriaAux(String dato) {
		UtilidadesHash.set(this.datos, "CATEGORIA1", dato);		
	}
	public void setTipoAux(String dato) {
		UtilidadesHash.set(this.datos, "TIPO1", dato);		
	}
	public void setProductoAux(String dato) {
		UtilidadesHash.set(this.datos, "PRODUCTO1", dato);		
	}

	public void setNumOperacion(String dato) {
		UtilidadesHash.set(this.datos, "NUMOPERACION", dato);		
	}

	public void setTarjeta2(String dato) {
		UtilidadesHash.set(this.datos, "TARJETA2", dato);		
	}
	
	public void setTarjeta3(String dato) {
		UtilidadesHash.set(this.datos, "TARJETA3", dato);		
	}

	public void setTarjeta4(String dato) {
		UtilidadesHash.set(this.datos, "TARJETA4", dato);		
	}

	public void setTarjetaMes(String dato) {
		UtilidadesHash.set(this.datos, "TARJETAMES", dato);		
	}

	public void setTarjetaAnho(String dato) {
		UtilidadesHash.set(this.datos, "TARJETAAÑO", dato);		
	}
	
	public void setFechaCaducidad(String dato) {
		UtilidadesHash.set(this.datos, "FECHACADUCIDAD", dato);		
	}
	
	public void setPan(String dato) {
		UtilidadesHash.set(this.datos, "PAN", dato);		
	}
	
	public void setVolver(String dato) {
		UtilidadesHash.set(this.datos, "VOLVER", dato);		
	}
	
	public void setConcepto(String dato) {
		UtilidadesHash.set(this.datos, "CONCEPTO", dato);		
	}
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, "IDPERSONA", dato);
	}
	
	public void setNumeroColegiado(String dato) {
		UtilidadesHash.set(this.datos, "NUMEROCOLEGIADO", dato);
	}
	
	public void setNombrePersona(String dato) {
		UtilidadesHash.set(this.datos, "NOMBREPERSONA", dato);
	}
	
	public void setNif(String dato) {
		UtilidadesHash.set(this.datos, "NIF", dato);
	}
	
	public void setTipoProducto(Integer dato) {	
		UtilidadesHash.set(this.datos, PysProductosInstitucionBean.C_IDTIPOPRODUCTO, dato);
	}		
	
	public void setCategoriaProducto(String dato) {
		if (dato.contains(",")){
			String[] lista=UtilidadesString.split(dato,",");
			if ((lista == null) || (lista.length < 1)) return;
			UtilidadesHash.set(this.datos, PysProductosInstitucionBean.C_IDPRODUCTO, lista[1]);
		} else {
			UtilidadesHash.set(this.datos, PysProductosInstitucionBean.C_IDPRODUCTO, dato);
		}
	}
	
	public void setProducto(Long dato) {	
		UtilidadesHash.set(this.datos, PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, dato);
	}
	
	public void setTipoServicio(Integer dato) {	
		UtilidadesHash.set(this.datos, PysServiciosInstitucionBean.C_IDTIPOSERVICIOS, dato);
	}
	
	public void setCategoriaServicio(String dato) {
		if (dato.contains(",")){
			String[] lista=UtilidadesString.split(dato,",");
			if ((lista == null) || (lista.length < 1)) return;
			UtilidadesHash.set(this.datos, PysServiciosInstitucionBean.C_IDSERVICIO, lista[1]);
		} else {
			UtilidadesHash.set(this.datos, PysServiciosInstitucionBean.C_IDSERVICIO, dato);
		}
	}
	
	public void setServicio(Long dato) {	
		UtilidadesHash.set(this.datos, PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION, dato);
	}
	
	
	public void setIdInstitucion (Integer dato) {
		UtilidadesHash.set(this.datos, "_IDINSTITUCION_", dato);
	}
	public Integer getIdInstitucion () {
		return UtilidadesHash.getInteger(this.datos, "_IDINSTITUCION_");
	}
/*
	public void setIdInstitucionPresentador (Integer dato) {
		UtilidadesHash.set(this.datos, "_IDINSTITUCIONPRESENTADOR_", dato);
	}
	public Integer getIdInstitucionPresentador () {
		return UtilidadesHash.getInteger(this.datos, "_IDINSTITUCIONPRESENTADOR_");
	}
*/
	
	
	public String getFechaEfectivaCompra() {
		return UtilidadesHash.getString(this.datos, "FECHAEFECTIVA");		
	}
	public String getIdPeticion() {
		return UtilidadesHash.getString(this.datos, "IDPETICION");		
	}
	public String getVolver() {
		return UtilidadesHash.getString(this.datos, "VOLVER");		
	}
	
	public String getConcepto() {
		return UtilidadesHash.getString(this.datos, "CONCEPTO");
	}
	
	public Long getIdPersona() {
		return UtilidadesHash.getLong(this.datos, "IDPERSONA");
	}
	
	public String getNumeroColegiado() {
		return UtilidadesHash.getString(this.datos, "NUMEROCOLEGIADO");
	}
		
	public String getNombrePersona() {
		return UtilidadesHash.getString(this.datos, "NOMBREPERSONA");
	}
	
	public String getNif() {
		return UtilidadesHash.getString(this.datos, "NIF");
	}
		
	public Integer getTipoProducto() {
		return UtilidadesHash.getInteger(this.datos, PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
	}	
	
	public String getCategoriaProducto() {
		return UtilidadesHash.getString(this.datos, PysProductosInstitucionBean.C_IDPRODUCTO);
	}
	
	public Long getProducto() {
		return UtilidadesHash.getLong(this.datos, PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
	}
	
	public Integer getTipoServicio() {
		return UtilidadesHash.getInteger(this.datos, PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
	}
	public String getCatalogo() {
		return UtilidadesHash.getString(this.datos, "CATALOGO");
	}
	public void setCatalogo(String dato) {
		UtilidadesHash.set(this.datos, "CATALOGO", dato);		
	}
	
	public String getCategoriaServicio() {
		return UtilidadesHash.getString(this.datos, PysServiciosInstitucionBean.C_IDSERVICIO);
	}
	
	public Long getServicio() {
		return UtilidadesHash.getLong(this.datos, PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
	}
	
	public String getFechaCaducidad() {
		return UtilidadesHash.getString(this.datos, "FECHACADUCIDAD");
	}
	
	public String getPan() {
		return UtilidadesHash.getString(this.datos, "PAN");
	}
	
	public String getTarjeta1() {
		return UtilidadesHash.getString(this.datos, "TARJETA1");
	}
	
	public String getTarjeta2() {
		return UtilidadesHash.getString(this.datos, "TARJETA2");
	}
	
	public String getTarjeta3() {
		return UtilidadesHash.getString(this.datos, "TARJETA3");
	}

	public String getTarjeta4() {
		return UtilidadesHash.getString(this.datos, "TARJETA4");
	}

	public String getTarjetaMes() {
		return UtilidadesHash.getString(this.datos, "TARJETAMES");
	}

	public String getTarjetaAnho() {
		return UtilidadesHash.getString(this.datos, "TARJETAAÑO");
	}
	
	public String getOperacionOK() {
		return UtilidadesHash.getString(this.datos, "OPERACIONOK");
	}

	public String getNumOperacion() {
		return UtilidadesHash.getString(this.datos, "NUMOPERACION");
	}
	

	public String getNombreProducto() {
		return UtilidadesHash.getString(this.datos, "NOMBREPRODUCTO");
	}
	public String getCategoriaAux() {
		return UtilidadesHash.getString(this.datos, "CATEGORIA1");
	}
	public String getTipoAux() {
		return UtilidadesHash.getString(this.datos, "TIPO1");
	}
	public String getProductoAux() {
		return UtilidadesHash.getString(this.datos, "PRODUCTO1");
	}
	

}
