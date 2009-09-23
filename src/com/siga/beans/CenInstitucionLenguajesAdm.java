
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class CenInstitucionLenguajesAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param request
	 */
	public CenInstitucionLenguajesAdm (UsrBean usuario) {
		super( CenInstitucionLenguajesBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	CenInstitucionLenguajesBean.C_IDINSTITUCION,		
							CenInstitucionLenguajesBean.C_IDLENGUAJE,		
							CenInstitucionLenguajesBean.C_USUMODIFICACION,
							CenInstitucionLenguajesBean.C_FECHAMODIFICACION};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {	CenInstitucionLenguajesBean.C_IDINSTITUCION, CenInstitucionLenguajesBean.C_IDLENGUAJE};
		return campos;
	}
	
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenInstitucionLenguajesBean bean = null;
		try{
			bean = new CenInstitucionLenguajesBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenInstitucionLenguajesBean.C_IDINSTITUCION));
			bean.setIdLenguaje(UtilidadesHash.getString(hash,CenInstitucionLenguajesBean.C_IDLENGUAJE));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenInstitucionLenguajesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenInstitucionLenguajesBean.C_USUMODIFICACION));
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
			CenInstitucionLenguajesBean b = (CenInstitucionLenguajesBean) bean;
			hash.put(CenInstitucionLenguajesBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenInstitucionLenguajesBean.C_IDLENGUAJE, String.valueOf(b.getIdLenguaje()));
			hash.put(CenInstitucionLenguajesBean.C_FECHAMODIFICACION, String.valueOf(b.getFechaMod()));
			hash.put(CenInstitucionLenguajesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

}