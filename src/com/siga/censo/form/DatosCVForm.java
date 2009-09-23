/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;


import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenDatosCVBean;
import com.siga.beans.CenHistoricoBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 */
public class DatosCVForm extends MasterForm
{
	String incluirRegistrosConBajaLogica;
	
	public void setTipoApunte(Integer dato) {
		UtilidadesHash.set(this.datos, "TIPOAPUNTE", dato);
	}
	
	public void setFechaInicio(String fechaInicio) {
		try {
			fechaInicio = GstDate.getApplicationFormatDate("",fechaInicio);
			this.datos.put(CenDatosCVBean.C_FECHAINICIO, fechaInicio);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		this.datos.put(CenDatosCVBean.C_FECHAINICIO, fechaInicio);
	}
	
	public void setFechaFin(String fechaFin) {
		try {
		 if (fechaFin!=null && !fechaFin.equals("")){		
			fechaFin = GstDate.getApplicationFormatDate("",fechaFin);
			this.datos.put(CenDatosCVBean.C_FECHAFIN, fechaFin);
		 }	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		this.datos.put(CenDatosCVBean.C_FECHAFIN, fechaFin);
	}
	
	public void setCertificado(Boolean certificado) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_CERTIFICADO, certificado);
	}
	
	public void setFechaMovimiento(String fechaMovimiento) {
		try {
		  if (fechaMovimiento!=null && !fechaMovimiento.equals("")){	 
			fechaMovimiento = GstDate.getApplicationFormatDate("",fechaMovimiento);
			this.datos.put(CenDatosCVBean.C_FECHAMOVIMIENTO, fechaMovimiento);
		  }	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		this.datos.put(CenDatosCVBean.C_FECHAMOVIMIENTO, fechaMovimiento);
	}
	
	public void setCreditos(Long creditos) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_CREDITOS, creditos);
	}
	
	public void setDescripcion(String descripcion) {
		this.datos.put(CenDatosCVBean.C_DESCRIPCION, descripcion);
	}
	
	public void setMotivo(String motivo) {
		UtilidadesHash.set(this.datos, CenHistoricoBean.C_MOTIVO, motivo);
	}
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDPERSONA, dato);
	}
	public void setIdInstitucion(Integer dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDINSTITUCION, dato);
	}
	
	public void setIdCV(Integer dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDCV, dato);
	}
	public void setIdTipoCVSubtipo1(String dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDTIPOCVSUBTIPO1, dato);
	}
	public void setIdTipoCVSubtipo2(String dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDTIPOCVSUBTIPO2, dato);
	}
	public void setIdInstitucion_Subtipo1(Integer dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDINSTITUCION_SUBT1, dato);
	}
	public void setIdInstitucion_Subtipo2(Integer dato) {
		UtilidadesHash.set(this.datos, CenDatosCVBean.C_IDINSTITUCION_SUBT2, dato);
	}
	
	//	metodos get de los campos del formulario
	public Integer getTipoApunte() {
		return UtilidadesHash.getInteger(this.datos,"TIPOAPUNTE");
	}
	
	public String getFechaInicio() {
		return (String)this.datos.get(CenDatosCVBean.C_FECHAINICIO);
	}	

	public String getFechaFin() {
		return (String)this.datos.get(CenDatosCVBean.C_FECHAFIN);
	}
			
	public Boolean getCertificado() {
		return UtilidadesHash.getBoolean(this.datos, CenDatosCVBean.C_CERTIFICADO);
	}
	
	public String getFechaMovimiento() {
		return (String)this.datos.get(CenDatosCVBean.C_FECHAMOVIMIENTO);
	}	
	
	public Long getCreditos() {
		return UtilidadesHash.getLong(this.datos, CenDatosCVBean.C_CREDITOS);
	}
	
	public String getDescripcion() {
		return (String)this.datos.get(CenDatosCVBean.C_DESCRIPCION);
	}
	
	public String getMotivo() {
		return UtilidadesHash.getString(this.datos, CenHistoricoBean.C_MOTIVO);
	}

	public Long getIDPersona() {
		return UtilidadesHash.getLong(this.datos, CenDatosCVBean.C_IDPERSONA);
	}

	public Integer getIDInstitucion() {
		return UtilidadesHash.getInteger(this.datos, CenDatosCVBean.C_IDINSTITUCION);
	}
	
	public Integer getIDCV() {
		return UtilidadesHash.getInteger(this.datos, CenDatosCVBean.C_IDCV);
	}
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	public String getIdTipoCVSubtipo1() {
		return UtilidadesHash.getString(this.datos, CenDatosCVBean.C_IDTIPOCVSUBTIPO1);
	}
	public String getIdTipoCVSubtipo2() {
		return UtilidadesHash.getString(this.datos, CenDatosCVBean.C_IDTIPOCVSUBTIPO2);
	}
	public Integer getIdInstitucion_Subtipo1() {
		return UtilidadesHash.getInteger(this.datos, CenDatosCVBean.C_IDINSTITUCION_SUBT1);
	}
	public Integer getIdInstitucion_Subtipo2() {
		return UtilidadesHash.getInteger(this.datos, CenDatosCVBean.C_IDINSTITUCION_SUBT2);
	}

}
