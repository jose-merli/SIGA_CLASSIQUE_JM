package es.satec.siga.util;

import java.util.List;

import com.ibatis.dao.client.DaoException;

import es.satec.siga.util.daos.PagedDao;
import es.satec.siga.util.vos.PagedVo;
import es.satec.siga.util.vos.SortedVo;
import es.satec.siga.util.vos.Vo;

public class ValueListHandler implements ValueListIterator {
	private PagedDao dao;
	private PagedVo pagedVo;
	private SortedVo sortedVo;
	private Vo dataVo;

	public ValueListHandler(PagedDao dao, PagedVo pagedVo, Vo dataVo, SortedVo sortedVo){
		this.dao=dao;
		if (pagedVo==null)
			this.pagedVo=new PagedVo();
		else
			this.pagedVo=pagedVo;
		this.dataVo=dataVo;
		this.sortedVo=sortedVo;
		dao.setPager(pagedVo);
	}
	
	public int getTotalTableSize() throws DaoException{
		pagedVo.setTotalListSize(dao.getTotalSize(dataVo));

		return pagedVo.getTotalListSize().intValue();
	}

	public List getNextPage() throws DaoException{
		if (this.hasNextPage()) {
			try {
				pagedVo.getNextPage();
				return this.getPage(pagedVo.getPage().intValue());
			} catch (DaoException de){
				pagedVo.getPreviousPage();
				throw de;
			}
		} else
			return null;
	}

	public List getPage(int page) throws DaoException {
		pagedVo.setPage(page);
		
		return dao.getPage(dataVo, sortedVo);
	}
	public List getPage(Integer page) throws DaoException {
		return getPage(page.intValue());
	}

	public List getPreviousPage() throws DaoException{
		if (this.hasNextPage()) {
			try {
				pagedVo.getPreviousPage();
				return this.getPage(pagedVo.getPage());
			} catch (DaoException de){
				pagedVo.getNextPage();
				throw de;
			}
		} else
			return null;
	}

	public boolean hasNextPage() throws DaoException {
		return ((getTotalTableSize()-(pagedVo.getPage().intValue()*pagedVo.getPageSize().intValue()))>0);
	}

	public boolean hasPreviousPage() throws DaoException {
		return (pagedVo.getPage().intValue()>0);
	}
}
