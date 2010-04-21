package com.siga.comun;

import java.util.List;

import com.ibatis.dao.client.DaoException;

public interface ValueListIterator {
	public int getTotalTableSize() throws DaoException;
	
	public List getPreviousPage() throws DaoException;
	public List getPage(int page) throws DaoException;
	public List getNextPage() throws DaoException;

	public boolean hasPreviousPage() throws DaoException ;
	public boolean hasNextPage() throws DaoException ;
}
