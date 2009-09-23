package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author david.sanchezp
 * Bean de Administracion de la tabla CEN_INSTITUCION_POBLACION.
 * 
 */
public class CenInstitucionPoblacionAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param request
	 */
	public CenInstitucionPoblacionAdm (UsrBean usuario) {
		super( CenInstitucionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	CenInstitucionPoblacionBean.C_IDINSTITUCION, CenInstitucionPoblacionBean.C_IDPOBLACION,		
							CenInstitucionPoblacionBean.C_USUMODIFICACION, CenInstitucionPoblacionBean.C_FECHAMODIFICACION};
		return campos;
	}
	protected String[] getClavesBean() {
		String[] campos = {	CenInstitucionPoblacionBean.C_IDINSTITUCION, CenInstitucionPoblacionBean.C_IDPOBLACION};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenInstitucionPoblacionBean bean = null;
		try{
			bean = new CenInstitucionPoblacionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenInstitucionPoblacionBean.C_IDINSTITUCION));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,CenInstitucionPoblacionBean.C_IDPOBLACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenInstitucionPoblacionBean b = (CenInstitucionPoblacionBean) bean;
			hash.put(CenInstitucionPoblacionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenInstitucionPoblacionBean.C_IDPOBLACION, b.getIdPoblacion());			
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}


}
