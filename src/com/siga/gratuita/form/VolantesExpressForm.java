package com.siga.gratuita.form;


import java.util.Collection;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsComisariaBean;
import com.siga.beans.ScsDelitoBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsTipoAsistenciaColegioBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;
import com.siga.gratuita.vos.VolantesExpressVo;


public class VolantesExpressForm extends MasterForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 130392229372142332L;
	String idInstitucion;
	String fechaGuardia;
	String lugar;
	String idTurno;
	String idGuardia;
	String idColegiadoGuardia;
	String idColegiado;
	String nombreColegiado;
	String numeroColegiado;
	Boolean sustituto;
	String idColegiadoSustituido;
	String idTipoAsistencia;
	String idTipoAsistenciaColegio;
	List<CenPersonaBean> colegiadosGuardia;
	List<CenPersonaBean> colegiadosSustituidos;
	List<ScsTurnoBean> turnos;
	List<ScsGuardiasTurnoBean> guardias;
	List<ScsComisariaBean> comisarias;
	List<ScsJuzgadoBean> juzgados;
	List<ScsDelitoBean> delitos;
	
	List<ScsTipoAsistenciaColegioBean> tiposAsistenciaColegio;
	
	List<ScsAsistenciasBean> asistencias;
	
	String datosAsistencias;
	String tipoPcajg;

	UsrBean usrBean;
	String fechaJustificacion;
	
	Boolean delito;
	
	String msgError;
	String msgAviso;
	
	
	public String getTipoPcajg() {
		return tipoPcajg;
	}
	public void setTipoPcajg(String tipoPcajg) {
		this.tipoPcajg = tipoPcajg;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public String getFechaGuardia() {
		return fechaGuardia;
	}
	public void setFechaGuardia(String fechaGuardia) {
		this.fechaGuardia = fechaGuardia;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	
	
	public String getNombreColegiado() {
		return nombreColegiado;
	}
	public void setNombreColegiado(String nombreColegiado) {
		this.nombreColegiado = nombreColegiado;
	}
	public Boolean getSustituto() {
		return sustituto;
	}
	public void setSustituto(Boolean sustituto) {
		this.sustituto = sustituto;
	}
	
	
	
	public Collection<CenPersonaBean> getColegiadosGuardia() {
		return colegiadosGuardia;
	}
	public void setColegiadosGuardia(List<CenPersonaBean> colegiadosGuardia) {
		this.colegiadosGuardia = colegiadosGuardia;
	}
	public Collection<ScsTipoAsistenciaColegioBean> getTiposAsistenciaColegio() {
		return tiposAsistenciaColegio;
	}
	public void setTiposAsistenciaColegio(
			List<ScsTipoAsistenciaColegioBean> tiposAsistenciaColegio) {
		this.tiposAsistenciaColegio = tiposAsistenciaColegio;
	}
	public List<ScsTurnoBean> getTurnos() {
		return turnos;
	}
	public void setTurnos(List<ScsTurnoBean> turnos) {
		this.turnos = turnos;
	}

	public List<ScsGuardiasTurnoBean> getGuardias() {
		return guardias;
	}
	public void setGuardias(List<ScsGuardiasTurnoBean> guardias) {
		this.guardias = guardias;
	}
	
	public UsrBean getUsrBean() {
		return usrBean;
	}
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	} 
	
	public VolantesExpressVo getVolanteExpressVo() throws ClsExceptions {
		VolantesExpressVo volanteExpressVo=new VolantesExpressVo();
		volanteExpressVo.setUsrBean(usrBean);
		
		if(idInstitucion!=null && !idInstitucion.equals(""))
			volanteExpressVo.setIdInstitucion(new Integer(idInstitucion));
		
		if(fechaGuardia!=null && !fechaGuardia.equals(""))
			volanteExpressVo.setFechaGuardia(GstDate.getApplicationFormatDate("", fechaGuardia));
		
		if(idTurno!=null && !idTurno.equals("") && !idTurno.equals("-1") && !idTurno.equals("undefined"))
			volanteExpressVo.setIdTurno(Integer.parseInt(idTurno));
		
		if(idGuardia!=null && !idGuardia.equals("")&& !idGuardia.equals("-1"))
			volanteExpressVo.setIdGuardia(Integer.parseInt(idGuardia));
		
		if(idColegiado!=null && !idColegiado.equals("") && !idColegiado.equals("-1"))
			volanteExpressVo.setIdColegiado(Long.parseLong(idColegiado));
		
		if(idColegiadoGuardia!=null && !idColegiadoGuardia.equals("") && !idColegiadoGuardia.equals("-1"))
			volanteExpressVo.setIdColegiadoGuardia(Long.parseLong(idColegiadoGuardia));
		
		if(idColegiadoSustituido!=null && !idColegiadoSustituido.equals("-1")&& !idColegiadoSustituido.equals(""))
			volanteExpressVo.setIdColegiadoSustituido(Long.parseLong( idColegiadoSustituido));
		
		if(idTipoAsistenciaColegio!=null && !idTipoAsistenciaColegio.equals("") && !idTipoAsistenciaColegio.equals("-1"))
			volanteExpressVo.setIdTipoAsistenciaColegio(Integer.parseInt(idTipoAsistenciaColegio));
		
		if(idTipoAsistencia!=null && !idTipoAsistencia.equals("") && !idTipoAsistencia.equals("-1"))
			volanteExpressVo.setIdTipoAsistencia(Integer.parseInt(idTipoAsistencia));
		
		if(lugar!=null && !lugar.equals(""))
			volanteExpressVo.setLugar(lugar);
		
		if(datosAsistencias!=null && !datosAsistencias.equals(""))
			volanteExpressVo.setDatosAsistencias(datosAsistencias);
		
		if(asistencias!=null && asistencias.size()>0)
			volanteExpressVo.setAsistencias(asistencias);
		
		if(fechaJustificacion!=null && !fechaJustificacion.equals("")){			
			volanteExpressVo.setFechaJustificacion(GstDate.getApplicationFormatDate("", fechaJustificacion));
		}
		
		return volanteExpressVo;
	}
	
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void clear(){
		idInstitucion = null;
		fechaGuardia = null;
		lugar = null;
		idTurno = null;
		idGuardia = null;
		idColegiadoGuardia = null;
		idColegiado = null;
		nombreColegiado = null;
		numeroColegiado = null;
		nombreColegiado = null;
		sustituto = null;
		idColegiadoSustituido = null;
		idTipoAsistencia = null;
		idTipoAsistenciaColegio = null;
		colegiadosGuardia = null;
		turnos = null;
		guardias = null;
		fechaJustificacion = null;
		datosAsistencias = null;
		asistencias = null;
		
		
	}
	public String getIdColegiadoGuardia() {
		return idColegiadoGuardia;
	}
	public void setIdColegiadoGuardia(String idColegiadoGuardia) {
		this.idColegiadoGuardia = idColegiadoGuardia;
	}
	public String getIdColegiado() {
		return idColegiado;
	}
	public void setIdColegiado(String idColegiado) {
		this.idColegiado = idColegiado;
	}
	public String getIdColegiadoSustituido() {
		return idColegiadoSustituido;
	}
	public void setIdColegiadoSustituido(String idColegiadoSustituido) {
		this.idColegiadoSustituido = idColegiadoSustituido;
	}
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	public String getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}
	public String getIdTipoAsistencia() {
		return idTipoAsistencia;
	}
	public void setIdTipoAsistencia(String idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}
	public String getIdTipoAsistenciaColegio() {
		return idTipoAsistenciaColegio;
	}
	public void setIdTipoAsistenciaColegio(String idTipoAsistenciaColegio) {
		this.idTipoAsistenciaColegio = idTipoAsistenciaColegio;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getNumeroColegiado() {
		return numeroColegiado;
	}
	public void setNumeroColegiado(String numeroColegiado) {
		this.numeroColegiado = numeroColegiado;
	}
	public Collection<ScsAsistenciasBean> getAsistencias() {
		return asistencias;
	}
	public void setAsistencias(List<ScsAsistenciasBean> asistencias) {
		this.asistencias = asistencias;
	}
	
	public String getDatosAsistencias() {
		return datosAsistencias;
	}
	public void setDatosAsistencias(String datosAsistencias) {
		this.datosAsistencias = datosAsistencias;
	}
	public Boolean getDelito() {
		return delito;
	}
	public void setDelito(Boolean delito) {
		this.delito = delito;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	public String getMsgAviso() {
		return msgAviso;
	}
	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}
	public List<ScsComisariaBean> getComisarias() {
		return comisarias;
	}
	public void setComisarias(List<ScsComisariaBean> comisarias) {
		this.comisarias = comisarias;
	}
	public List<ScsJuzgadoBean> getJuzgados() {
		return juzgados;
	}
	public void setJuzgados(List<ScsJuzgadoBean> juzgados) {
		this.juzgados = juzgados;
	}
	public void setDelitos(List<ScsDelitoBean> delitos) {
		this.delitos = delitos;
	}
	public List<ScsDelitoBean> getDelitos() {
		return delitos;
	}
	public List<CenPersonaBean> getColegiadosSustituidos() {
		return colegiadosSustituidos;
	}
	public void setColegiadosSustituidos(List<CenPersonaBean> colegiadosSustituidos) {
		this.colegiadosSustituidos = colegiadosSustituidos;
	}
}