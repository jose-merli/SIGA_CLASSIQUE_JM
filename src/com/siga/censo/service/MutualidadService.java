package com.siga.censo.service;

import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.censo.form.MutualidadForm;
import com.siga.general.SIGAException;
import com.siga.ws.mutualidad.RespuestaMutualidad;

import es.satec.businessManager.BusinessService;

public interface MutualidadService extends BusinessService{
	public void insertarSolicitudMutualidad(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception;
	public List<CenSolicitudMutualidadBean> getSolicitudesMutualidad(CenSolicitudIncorporacionBean solicitudIncorporacionBean, UsrBean usrBean)throws ClsExceptions ;
	public MutualidadForm getSolicitudMutualidad(MutualidadForm mutualidadForm, String idPersona,String idTipoSolicitud,UsrBean usrBean) throws ClsExceptions, SIGAException ;
	public MutualidadForm setMutualidadForm(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception;
//	public MutualidadForm setMutualidadFormDefecto(MutualidadForm mutualidadForm) throws Exception;
	public MutualidadForm setCobertura(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception;
	public RespuestaMutualidad isPosibilidadSolicitudAlta(String numeroIdentificacion,String fechaNacimiento, UsrBean usrBean) throws SIGAException, Exception;
	public void actualizaEstadoSolicitud(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception;
	public void actualizaEstadoMutualista(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception;
	public MutualidadForm getDatosPersonaFicha(MutualidadForm mutualidadForm, UsrBean usr)throws Exception;
}
