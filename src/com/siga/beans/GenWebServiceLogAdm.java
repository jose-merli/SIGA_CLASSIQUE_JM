package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class GenWebServiceLogAdm extends MasterBeanAdministrador {

	
	public GenWebServiceLogAdm(UsrBean usuario) {
		super(GenWebServiceLogBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {
							GenWebServiceLogBean.C_IDWEBSERVICELOG,
							GenWebServiceLogBean.C_IDINSTITUCION,							
							GenWebServiceLogBean.C_RQ_RS,
							GenWebServiceLogBean.C_XML_SOAP,
							GenWebServiceLogBean.C_DESCRIPCION,
							
							GenWebServiceLogBean.C_FECHAMODIFICACION,
							GenWebServiceLogBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {GenWebServiceLogBean.C_IDWEBSERVICELOG};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenWebServiceLogBean bean = null;
		
		try {
			bean = new GenWebServiceLogBean();
			bean.setIdWebServiceLog(UtilidadesHash.getSigaSequence(bean.getIdWebServiceLog(), hash, GenWebServiceLogBean.C_IDWEBSERVICELOG));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, GenWebServiceLogBean.C_IDINSTITUCION));			
			
			bean.setRqRs(UtilidadesHash.getString(hash, GenWebServiceLogBean.C_RQ_RS));
			bean.setXmlSoap(UtilidadesHash.getString(hash, GenWebServiceLogBean.C_XML_SOAP));			
			bean.setDescripcion(UtilidadesHash.getString(hash, GenWebServiceLogBean.C_DESCRIPCION));
					
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenWebServiceLogBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			GenWebServiceLogBean b = (GenWebServiceLogBean) bean;
			
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_IDWEBSERVICELOG, b.getIdWebServiceLog());			
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_IDINSTITUCION, b.getIdInstitucion());
			
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_RQ_RS, b.getRqRs());
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_XML_SOAP, b.getXmlSoap());
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_DESCRIPCION, b.getDescripcion());
			
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, GenWebServiceLogBean.C_USUMODIFICACION, b.getUsuMod());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	
}
