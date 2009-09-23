/*
 * Created on Nov 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenSoliModiDireccionesBean;
import com.siga.beans.CenTipoDireccionBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DireccionesForm extends MasterForm{

	String incluirRegistrosConBajaLogica;
	
	//	metodos set de los campos del formulario
	public void setCodigoPostal(String codigoPostal) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_CODIGOPOSTAL, codigoPostal);
	}
	
	public void setCorreoElectronico(String correoElectronico) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_CORREOELECTRONICO, correoElectronico);
	}
	
	public void setDomicilio(String domicilio) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_DOMICILIO, domicilio);
	}
	
	public void setFax1(String fax1) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_FAX1, fax1);
	}
	
	public void setFax2(String fax2) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_FAX2, fax2);
	}
	
	public void setFechaBaja(String fechaBaja) {
		try {
			fechaBaja = GstDate.getApplicationFormatDate("",fechaBaja);
			this.datos.put(CenDireccionesBean.C_FECHABAJA, fechaBaja);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void setIdTipoDireccion(Integer dato[]) {
		this.datos.put(CenTipoDireccionBean.C_IDTIPODIRECCION, dato);
	}
	
	public void setIdTipoDireccionAntes(Integer dato) {
		UtilidadesHash.set(this.datos, "IDTIPODIRECCION_ANTES", dato);
	}
	
	public void setIdDireccion(Long idDireccion) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDDIRECCION, idDireccion);
	}
	
	public void setPais(String idPais) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDPAIS, idPais);
	}
	
	public void setPoblacion(String idPoblacion) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDPOBLACION, idPoblacion);
	}
	
	public void setPoblacionExt(String idPoblacion) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_POBLACIONEXTRANJERA, idPoblacion);
	}
	
	public void setProvincia(String idProvincia) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDPROVINCIA, idProvincia);
	}
	
	public void setMotivo(String motivo) {
		UtilidadesHash.set(this.datos, CenSoliModiDireccionesBean.C_MOTIVO, motivo);
	}
	
	public void setMovil(String movil) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_MOVIL, movil);
	}
	
	public void setPaginaWeb(String paginaWeb) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_PAGINAWEB, paginaWeb);
	}
	
	public void setPreferente(String preferente) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_PREFERENTE, preferente);
	}
	
	public void setTelefono1(String telefono1) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_TELEFONO1, telefono1);
	}
	
	public void setTelefono2(String telefono2) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_TELEFONO2, telefono2);
	}	
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDPERSONA, dato);
	}
	public void setIdInstitucion(Integer dato) {
		UtilidadesHash.set(this.datos, CenDireccionesBean.C_IDINSTITUCION, dato);
	}
	
	public void setPreferenteFax(Boolean dato) {
		UtilidadesHash.set(this.datos, "PREFERENTE_FAX", dato);
	}
	
	public void setPreferenteCorreo(Boolean dato) {
		UtilidadesHash.set(this.datos, "PREFERENTE_CORREO", dato);
	}
	
	public void setPreferenteMail(Boolean dato) {
		UtilidadesHash.set(this.datos, "PREFERENTE_MAIL", dato);
	}
	
	public void setPreferenteSms(Boolean dato) {
		UtilidadesHash.set(this.datos, "PREFERENTE_SMS", dato);
	}
	
	//	metodos get de los campos del formulario
	public String getCodigoPostal() {
		return (String)this.datos.get(CenDireccionesBean.C_CODIGOPOSTAL);
	}
	
	public String getCorreoElectronico() {
		return (String)this.datos.get(CenDireccionesBean.C_CORREOELECTRONICO);
	}	

	public String getDomicilio() {
		return (String)this.datos.get(CenDireccionesBean.C_DOMICILIO);
	}
	
	public String getFax1() {
		return (String)this.datos.get(CenDireccionesBean.C_FAX1);
	}
	
	public String getFax2() {
		return (String)this.datos.get(CenDireccionesBean.C_FAX2);
	}
	
	public String getFechaBaja() {
		return (String)this.datos.get(CenDireccionesBean.C_FECHABAJA);
	}
	
	public Long getIdDireccion() {
		return UtilidadesHash.getLong(this.datos, CenDireccionesBean.C_IDDIRECCION);
	}
	
	public String getPais() {
		return (String)this.datos.get(CenDireccionesBean.C_IDPAIS);
	}
	
	public String getPoblacion() {
		return (String)this.datos.get(CenDireccionesBean.C_IDPOBLACION);
	}
	
	public String getPoblacionExt() {
		return (String)this.datos.get(CenDireccionesBean.C_POBLACIONEXTRANJERA);
	}
	
	public String getProvincia() {
		return (String)this.datos.get(CenDireccionesBean.C_IDPROVINCIA);
	}
	
	public String getMotivo() {
		return UtilidadesHash.getString(this.datos, CenSoliModiDireccionesBean.C_MOTIVO);
	}
	
	public String getMovil() {
		return (String)this.datos.get(CenDireccionesBean.C_MOVIL);
	}
	
	public String getPaginaWeb() {
		return (String)this.datos.get(CenDireccionesBean.C_PAGINAWEB);
	}
	
	public String getPreferente() {
		return (String)this.datos.get(CenDireccionesBean.C_PREFERENTE);
	}
	
	public String getTelefono1() {
		return (String)this.datos.get(CenDireccionesBean.C_TELEFONO1);
	}
	
	public String getTelefono2() {
		return (String)this.datos.get(CenDireccionesBean.C_TELEFONO2);
	}
	
	public Integer[] getIDTipoDireccion() {
		return (Integer[])this.datos.get(CenTipoDireccionBean.C_IDTIPODIRECCION);
	}

	public Integer getIdTipoDireccionAntes() {
		return UtilidadesHash.getInteger(this.datos, "IDTIPODIRECCION_ANTES");
	}

	public Long getIDPersona() {
		return UtilidadesHash.getLong(this.datos, CenDireccionesBean.C_IDPERSONA);
	}

	public Integer getIDInstitucion() {
		return UtilidadesHash.getInteger(this.datos, CenDireccionesBean.C_IDINSTITUCION);
	}

	public Boolean getPreferenteFax() {
		return UtilidadesHash.getBoolean(this.datos, "PREFERENTE_FAX");
	}

	public Boolean getPreferenteCorreo() {
		return UtilidadesHash.getBoolean(this.datos, "PREFERENTE_CORREO");
	}

	public Boolean getPreferenteMail() {
		return UtilidadesHash.getBoolean(this.datos, "PREFERENTE_MAIL");
	}
	
	public Boolean getPreferenteSms() {
		return UtilidadesHash.getBoolean(this.datos, "PREFERENTE_SMS");
	}
	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
}
