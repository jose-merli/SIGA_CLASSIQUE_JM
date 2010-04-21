package com.siga.censo.service;

import java.util.List;

import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;


public interface CensoService extends BusinessService{

	public List<InstitucionVO> getColegiosDeConsejo(String idInstitucion) throws SIGAException;
	public List<InstitucionVO> getColegiosNoConsejo(String idInstitucion) throws SIGAException;
	public List<ValueKeyVO> getGruposFijos(String idioma, String idInstitucion) throws SIGAException;
	public List<ValueKeyVO> getTipoColegiacion(String idioma) throws SIGAException;
	public List<ValueKeyVO> getTiposCV(String idioma)throws SIGAException;
	public List<ValueKeyVO> getComisiones(String idioma, String idInstitucion, String idTipoCV) throws SIGAException;
	public List<ValueKeyVO> getCargos(String idioma, String idInstitucion, String idTipoCV) throws SIGAException;
	
}
