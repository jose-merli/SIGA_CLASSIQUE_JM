package com.siga.censo.dao;

import java.util.List;

import com.siga.comun.daos.PagedDao;
import com.siga.comun.vos.Vo;

public interface LetradoDao extends PagedDao {
	public List getAll(Vo letrado, String idioma, String idInstitucion);
}
