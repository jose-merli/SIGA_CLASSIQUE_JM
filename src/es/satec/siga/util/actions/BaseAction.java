package es.satec.siga.util.actions;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class BaseAction extends MasterAction {
	private static final String DEFAULT_ACTION="inicio";
	private static DaoManager dm=null;
	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario,
		      HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino="";
		MasterForm miForm = null;
		
		try {
			dm=DaoManagerBuilder.buildDaoManager(Resources.getResourceAsReader("DaoConfig.xml"));

			miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion=miForm.getModo();
				if (StringUtils.isEmpty(accion)){
					accion=DEFAULT_ACTION;
				}
				Method metodo=this.getClass().getMethod(accion, new Class[]{ActionMapping.class, MasterForm.class, HttpServletRequest.class, HttpServletResponse.class});
				if (metodo!=null)
					destino=(String)metodo.invoke(this, new Object[]{mapping, miForm, request, response});
				else
				    throw new ClsExceptions("La accion "+miForm.getModo()+" a ejectura debe existir.");
			}
			
			if (StringUtils.isEmpty(destino)){ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			
			return mapping.findForward(destino);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	public DaoManager getDaoManager(){
		return dm;
	}
}
