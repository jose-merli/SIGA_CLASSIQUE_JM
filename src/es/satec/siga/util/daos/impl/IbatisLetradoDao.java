package es.satec.siga.util.daos.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

import es.satec.siga.util.daos.LetradoDao;
import es.satec.siga.util.vos.PagedVo;
import es.satec.siga.util.vos.SortedVo;
import es.satec.siga.util.vos.Vo;

public class IbatisLetradoDao extends SqlMapDaoTemplate implements LetradoDao {
	private PagedVo paginador=new PagedVo();
	
	public IbatisLetradoDao(DaoManager daoManager) {
		super(daoManager);
	}
	
	public void setPager(PagedVo paginador){
		this.paginador=paginador;
	}

	public List getPage(Vo letrado) throws DaoException {
		return getPage(letrado, new SortedVo());
	}
	
	public List getPage(Vo letrado, SortedVo ordenacion) throws DaoException {
		Map params=new HashMap();
		params.put("letrado", letrado);
		params.put("paginador", paginador);
		params.put("ordenacion", ordenacion);

		try {
			return getSqlMapExecutor().queryForList("Pruebas.paginaLetrados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener la pagina "+paginador.getPage()+" de la busqueda de letrados.", se);
		}
	}

	public Integer getTotalSize(Vo letrado) throws DaoException {
		Map params=new HashMap();
		params.put("letrado", letrado);
		params.put("paginador", paginador);

		try {
			return (Integer)getSqlMapExecutor().queryForObject("Pruebas.numeroLetrados", params);
		} catch (SQLException se) {
			throw new DaoException ("Error al obtener el numero de letrados.", se);
		}
	}
}
