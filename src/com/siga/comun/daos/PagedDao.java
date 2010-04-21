package com.siga.comun.daos;

import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.Dao;
import com.ibatis.dao.client.DaoException;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;


public interface PagedDao extends Dao {
	public void setPager(PagedVo vo);
	public Integer getTotalSize(Vo vo) throws DaoException;
	public Integer getTotalSize(Vo vo, Map<Object,Object> map) throws DaoException;
	public List getPage(Vo vo) throws DaoException;
	public List getPage(Vo vo, SortedVo ordenacion) throws DaoException;
	public List getPage(Vo vo, SortedVo ordenacion, Map<Object,Object> dataMap) throws DaoException;

}
