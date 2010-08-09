package com.siga.censo.service;

import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;

public interface BajasTemporalesService extends BusinessService{
	
	public Object executeService(CenBajasTemporalesBean bajasTemporalesVo  ) throws SIGAException,ClsExceptions;
	public List<CenBajasTemporalesBean> getBajasTemporales(BajasTemporalesForm bajaTemporal,UsrBean usrBean)throws ClsExceptions;
	public void insertaBajasTemporales(BajasTemporalesForm bajasTemporalesForm,boolean isComprobacion,UsrBean usrBean)throws ClsExceptions;
	public void validarSolicitudesBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions;
	public void denegarSolicitudesBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions;
	public void borrarSolicitudBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions;
	public CenBajasTemporalesBean getBajaTemporal(	BajasTemporalesForm bajaTemporalForm,UsrBean usrBean)throws ClsExceptions ;
	public void modificarSolicitudBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions;
	public void setColegiado(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions;

	
}
