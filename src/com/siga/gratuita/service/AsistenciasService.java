package com.siga.gratuita.service;

import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsComisariaBean;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsPrisionBean;
import com.siga.beans.ScsTipoActuacionBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;

import es.satec.businessManager.BusinessService;

public interface AsistenciasService extends BusinessService{
	
	public List<ActuacionAsistenciaForm> getActuacionesAsistencia(AsistenciaForm asistenciaForm,UsrBean usrBean) throws ClsExceptions ;
	public ActuacionAsistenciaForm getActuacionAsistencia(ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)throws ClsExceptions;
	public void borrarActuacionAsistencia(	ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)throws ClsExceptions;
	public AsistenciaForm getDatosAsistencia(AsistenciaForm asistenciaForm, UsrBean usrBean)throws ClsExceptions;
	public List<ScsComisariaBean> getComisarias(AsistenciaForm asistenciaForm, UsrBean usrBean) throws ClsExceptions;
	public List<ScsJuzgadoBean> getJuzgados(AsistenciaForm asistenciaForm,UsrBean usrBean) throws ClsExceptions ;
	public List<ValueKeyVO> getTipoCosteFijoActuaciones(ActuacionAsistenciaForm actuacionAsistenciaForm,String idTipoActuacion, UsrBean usrBean)throws ClsExceptions ;
	public List<ScsTipoActuacionBean> getTiposActuacion(AsistenciaForm asistenciaForm, UsrBean usrBean)	throws ClsExceptions ;
	public List<ScsPrisionBean> getPrisiones(AsistenciaForm asistenciaForm,	UsrBean usrBean) throws ClsExceptions;
	public void modificarActuacionAsistencia(ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)throws ClsExceptions, SIGAException ;
	public void insertarActuacionAsistencia(ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)throws ClsExceptions, SIGAException ;
	public Integer getNuevaActuacionAsistencia(AsistenciaForm asistencia,UsrBean usrBean) throws ClsExceptions;
	
	
	




	

	
}
