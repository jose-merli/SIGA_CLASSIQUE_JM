package es.satec.siga.util.actions.censo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.ibatis.dao.client.DaoException;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.siga.util.ValueListHandler;
import es.satec.siga.util.ValueListIterator;
import es.satec.siga.util.actions.BaseAction;
import es.satec.siga.util.daos.LetradoDao;
import es.satec.siga.util.daos.PagedDao;
import es.satec.siga.util.formbeans.censo.BusquedaBean;

public class BusquedaLetradosAction extends BaseAction{
	private static final String INICIO_FORWARD="inicio";
	private static final String TABLE_NAME="tablaLetrados";

	public String busqueda(ActionMapping mapping, MasterForm formulario,
		      HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		BusquedaBean busquedaBean=(BusquedaBean)formulario;
		busquedaBean.setTableName(TABLE_NAME);
		busquedaBean.setRequest(request);
		
		PagedDao dao = (PagedDao)getDaoManager().getDao(LetradoDao.class);
		ValueListIterator vli=new ValueListHandler(dao, busquedaBean.getPagedVo(), busquedaBean.getLetradoVo(), busquedaBean.getSortedVo());
		try {
			busquedaBean.setTotalTableSize(vli.getTotalTableSize());
			busquedaBean.setTable(vli.getPage(busquedaBean.getPage().intValue()));
		} catch (DaoException de){
			throw new SIGAException (de);
		}

		return INICIO_FORWARD;
	}

	public String inicio(ActionMapping mapping, MasterForm formulario,
		      HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		BusquedaBean busquedaBean=(BusquedaBean)formulario;
		busquedaBean.setTableName(TABLE_NAME);
		return INICIO_FORWARD;
	}
}
