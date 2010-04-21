package com.siga.censo.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.ValueKeyVO;


public interface CensoDao extends Dao {
	
	public List<InstitucionVO> getColegiosNoConsejo(String idinstitucion);
	public List<InstitucionVO> getColegiosDeConsejo(String idinstitucion);
	public List<ValueKeyVO> getGruposFijos(String idioma, String idInstitucion);
	public List<ValueKeyVO> getTipoColegiacion(String idioma);
	public List<ValueKeyVO> getTiposCV(String idioma);
	public List<ValueKeyVO> getComisiones(String idioma, String idInstitucion, String idTipoCV);
	public List<ValueKeyVO> getCargos(String idioma, String idInstitucion, String idTipoCV);
	
}
