package es.satec.siga.util.daos;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.ibatis.dao.client.DaoException;

import es.satec.siga.util.vos.PagedVo;
import es.satec.siga.util.vos.SortedVo;
import es.satec.siga.util.vos.Vo;

public interface PagedDao extends Dao {
	public void setPager(PagedVo vo);
	public Integer getTotalSize(Vo vo) throws DaoException;
	public List getPage(Vo vo) throws DaoException;
	public List getPage(Vo vo, SortedVo sortedVo) throws DaoException;
}
