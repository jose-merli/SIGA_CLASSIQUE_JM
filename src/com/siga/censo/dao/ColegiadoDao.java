package com.siga.censo.dao;

import java.util.List;

import com.siga.comun.daos.PagedDao;
import com.siga.comun.vos.Vo;

public interface ColegiadoDao extends PagedDao {

	public String getEstadoColegial(String idInstitucion, String idPersona, String idioma);
	public List getAll(Vo colegiado, String idioma, String idInstitucion);
	
}
