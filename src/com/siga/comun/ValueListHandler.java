package com.siga.comun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.siga.comun.daos.PagedDao;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;

public class ValueListHandler implements ValueListIterator {
	private PagedDao dao;
	private PagedVo pagedVo;
	private SortedVo sortedVo;
	private Vo dataVo;
	private Map<Object,Object> dataMap;

	public ValueListHandler(PagedDao dao, Vo dataVo, SortedVo sortedVo, PagedVo pagedVo){
		this(dao, dataVo, sortedVo, pagedVo, new HashMap<Object,Object>());
	}

	public ValueListHandler(PagedDao dao, Vo dataVo, SortedVo sortedVo, PagedVo pagedVo, Map<Object,Object> dataMap){
		this.dao=dao;
		if (pagedVo==null)
			this.pagedVo=new PagedVo();
		else
			this.pagedVo=pagedVo;
		this.dataVo=dataVo;
		this.sortedVo=sortedVo;
		dao.setPager(pagedVo);
		this.dataMap = dataMap;
	}
	
	public int getTotalTableSize() throws DaoException{
		pagedVo.setTotalListSize(dao.getTotalSize(dataVo,dataMap));

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
		
		return dao.getPage(dataVo, sortedVo, dataMap);
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
