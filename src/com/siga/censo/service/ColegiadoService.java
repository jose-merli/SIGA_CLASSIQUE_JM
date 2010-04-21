package com.siga.censo.service;

import java.util.List;

import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;
import com.siga.general.SIGAException;

public interface ColegiadoService extends ClientesService{

	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo, 
			String idioma, String idInstitucion) throws SIGAException;
	
}
