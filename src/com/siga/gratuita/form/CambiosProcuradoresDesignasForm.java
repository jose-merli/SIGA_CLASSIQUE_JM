package com.siga.gratuita.form;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDesignasProcuradorBean;
import com.siga.beans.ScsProcuradorBean;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 3/2/2005
 */

 

public class CambiosProcuradoresDesignasForm extends MasterForm {
	
	protected String anio = ScsDesignasProcuradorBean.C_ANIO;
	protected String numero = ScsDesignasProcuradorBean.C_NUMERO;
	protected String idTurno = ScsDesignasProcuradorBean.C_IDTURNO;
	protected String nColegiadoActual =ScsProcuradorBean.C_NCOLEGIADO;
	protected String nombreActual =ScsProcuradorBean.C_NOMBRE;
	protected String apellido1Actual =ScsProcuradorBean.C_APELLIDO1;
	protected String apellido2Actual =ScsProcuradorBean.C_APELLIDO2;
	protected String fechaDesigna =ScsDesignasProcuradorBean.C_FECHADESIGNA;
	protected String fechaRenuncia =ScsDesignasProcuradorBean.C_FECHARENUNCIA;
	protected String fechaRenunciaSolicita =ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA;
	protected String idTipoMotivo=ScsDesignasProcuradorBean.C_IDTIPOMOTIVO;
	protected String observaciones =ScsDesignasProcuradorBean.C_OBSERVACIONES;
	protected String idProcurador = ScsDesignasProcuradorBean.C_IDPROCURADOR;
	protected String institProcurador=ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC;
	protected String numeroDesigna=ScsDesignasProcuradorBean.C_NUMERODESIGNACION;
	
	//Metodos get de los campos del formulario
	public String getAnio() {
		return UtilidadesHash.getString(datos,anio);
	}
	public String getAplFechaDesigna() {
		return UtilidadesHash.getString(datos,fechaDesigna);
	}
	public String getFechaDesigna() {
		return UtilidadesHash.getShortDate(datos,fechaDesigna);
	}
	public String getAplFechaRenuncia() {
		return UtilidadesHash.getString(datos,fechaRenuncia);
	}
	public String getFechaRenuncia() {
		return UtilidadesHash.getShortDate(datos,fechaRenuncia);
	}
	public String getAplFechaRenunciaSolicita() {
		return UtilidadesHash.getString(datos,fechaRenunciaSolicita);
	}
	public String getFechaRenunciaSolicita() {
		return UtilidadesHash.getShortDate(datos,fechaRenunciaSolicita);
	}
	public String getAplInstitProcurador() {
		return UtilidadesHash.getString(datos,institProcurador);
	}
	public String getAplIdProcurador() {
		return UtilidadesHash.getString(datos,idProcurador);
	}
	public String getIdProcurador() {
		return 
		UtilidadesHash.getString(datos,idProcurador)+","+
		UtilidadesHash.getString(datos,institProcurador);
	}
	public String getIdTipoMotivo() {
		return UtilidadesHash.getString(datos,idTipoMotivo);
	}
	public String getIdTurno() {
		return UtilidadesHash.getString(datos,idTurno);
	}
	public String getNColegiadoActual() {
		return UtilidadesHash.getString(datos,nColegiadoActual);
	}
	public String getNumeroDesigna() {
		return UtilidadesHash.getString(datos,numeroDesigna);
	}
/*
	public String getNombreActual() {
		String n=null; 
		if(datos.containsKey(nombreActual)||datos.containsKey(apellido1Actual)||datos.containsKey(apellido2Actual)){
			n=
				UtilidadesHash.getString(datos,nombreActual)+" "+
				UtilidadesHash.getString(datos,apellido1Actual)+" "+
				UtilidadesHash.getString(datos,apellido2Actual);
		}
		return n;
	}
  */	
	public String getNombreActual() {
		return	UtilidadesHash.getString(datos,nombreActual);
	}
	public String getApellido1Actual() {
		return	UtilidadesHash.getString(datos,apellido1Actual);
	}
	public String getApellido2Actual() {
		return	UtilidadesHash.getString(datos,apellido2Actual);
	}

	public String getNumero() {
		return UtilidadesHash.getString(datos,numero);
	}
	public String getObservaciones() {
		return UtilidadesHash.getString(datos,observaciones);
	}
	
	
	//Metodos set de los campos del formulario
	public void setAnio(String anio) {
		UtilidadesHash.set(datos,this.anio,anio);
	}
	public void setAplFechaDesigna(String fechaDesigna) {
		UtilidadesHash.set(datos,this.fechaDesigna,fechaDesigna);
	}
	public void setFechaDesigna(String fechaDesigna) {
		UtilidadesHash.setShortDate(datos,this.fechaDesigna,fechaDesigna);
	}
	public void setAplFechaRenuncia(String fechaRenuncia) {
		UtilidadesHash.set(datos,this.fechaRenuncia,fechaRenuncia);
	}
	public void setFechaRenuncia(String fechaRenuncia) {
		UtilidadesHash.setShortDate(datos,this.fechaRenuncia,fechaRenuncia);
	}
	public void setAplFechaRenunciaSolicita(String fechaRenunciaSolicita) {
		UtilidadesHash.set(datos,this.fechaRenunciaSolicita,fechaRenunciaSolicita);
	}
	public void setFechaRenunciaSolicita(String fechaRenunciaSolicita) {
		UtilidadesHash.setShortDate(datos,this.fechaRenunciaSolicita,fechaRenunciaSolicita);
	}
	public void setAplInstitProcurador(String institProcurador) {
		UtilidadesHash.set(datos,this.institProcurador,institProcurador);
	}
	public void setAplIdProcurador(String idProcurador) {
		UtilidadesHash.set(datos,this.idProcurador,idProcurador);
	}
	public void setIdProcurador(String idProcurador) {
		int i=-1;
		if(idProcurador!=null && (i=idProcurador.indexOf(","))!=-1){
			UtilidadesHash.set(datos,this.idProcurador,idProcurador.substring(0,i));
			UtilidadesHash.set(datos,this.institProcurador,idProcurador.substring(i+1));
		}
	}
	public void setIdTipoMotivo(String idTipoMotivo) {
		UtilidadesHash.set(datos,this.idTipoMotivo,idTipoMotivo);
	}
	public void setIdTurno(String idTurno) {
		UtilidadesHash.set(datos,this.idTurno,idTurno);
	}
	public void setNumeroDesigna(String numeroDesigna) {
		UtilidadesHash.set(datos,this.numeroDesigna,numeroDesigna);
	}
	public void setNumero(String numero) {
		UtilidadesHash.set(datos,this.numero,numero);
	}
	public void setObservaciones(String observaciones) {
		UtilidadesHash.set(datos,this.observaciones,observaciones);
	}

}