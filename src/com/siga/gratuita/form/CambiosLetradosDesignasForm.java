package com.siga.gratuita.form;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.general.MasterForm;

public class CambiosLetradosDesignasForm extends MasterForm {
	
	protected String anio = ScsDesignasLetradoBean.C_ANIO;
	protected String numero = ScsDesignasLetradoBean.C_NUMERO;
	protected String idTurno = ScsDesignasLetradoBean.C_IDTURNO;
	protected String idPersona = ScsDesignasLetradoBean.C_IDPERSONA;
	protected String nColegiadoActual =CenColegiadoBean.C_NCOLEGIADO;
	protected String nombreActual =CenPersonaBean.C_NOMBRE;
	protected String apellido1Actual =CenPersonaBean.C_APELLIDOS1;
	protected String apellido2Actual =CenPersonaBean.C_APELLIDOS2;
	protected String fechaDesigna =ScsDesignasLetradoBean.C_FECHADESIGNA;
	protected String fechaRenuncia =ScsDesignasLetradoBean.C_FECHARENUNCIA;
	protected String fechaRenunciaSolicita =ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA;
	protected String idTipoMotivo=ScsDesignasLetradoBean.C_IDTIPOMOTIVO;
	protected String observaciones =ScsDesignasLetradoBean.C_OBSERVACIONES;
	protected String codigo ="CODIGO";
	protected String sufijo ="SUFIJO";
	protected String nColegiadoOrigen ="NCOLEGIADOORIGEN";	
	protected String nInstitucionOrigen =ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN;	
	
	
	
	
	public String getnInstitucionOrigen() {
		return UtilidadesHash.getString(datos,nInstitucionOrigen);
	}
	//Metodos get de los campos del formulario
	public String getsufijo() {
		return UtilidadesHash.getString(datos,sufijo);
	}
	public String getCodigo() {
		return UtilidadesHash.getString(datos,codigo);
	}
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
	public String getIdPersona() {
		return UtilidadesHash.getString(datos,idPersona);
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
	public String getnColegiadoOrigen() {
		return UtilidadesHash.getString(datos,nColegiadoOrigen);
	}
	
	//Metodos set de los campos del formulario
	
	public void setnInstitucionOrigen(String nInstitucionOrigen) {
		UtilidadesHash.set(datos,this.nInstitucionOrigen,nInstitucionOrigen);
	}
	public void setnColegiadoOrigen(String nColegiadoOrigen) {
		UtilidadesHash.set(datos,this.nColegiadoOrigen,nColegiadoOrigen);		
	}
	public void setsufijo(String sufijo) {
		UtilidadesHash.set(datos,this.sufijo,sufijo);
	}	
	public void setCodigo(String codigo) {
		UtilidadesHash.set(datos,this.codigo,codigo);
	}
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
	public void setIdPersona(String idPersona) {
		UtilidadesHash.set(datos,this.idPersona,idPersona);
	}
	public void setIdTipoMotivo(String idTipoMotivo) {
		UtilidadesHash.set(datos,this.idTipoMotivo,idTipoMotivo);
	}
	public void setIdTurno(String idTurno) {
		UtilidadesHash.set(datos,this.idTurno,idTurno);
	}
	public void setNumero(String numero) {
		UtilidadesHash.set(datos,this.numero,numero);
	}
	public void setObservaciones(String observaciones) {
		UtilidadesHash.set(datos,this.observaciones,observaciones);
	}

}