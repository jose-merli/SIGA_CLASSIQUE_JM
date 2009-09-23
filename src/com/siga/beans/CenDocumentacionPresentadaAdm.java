/*
 * Created on 14-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author daniel.campos
 *
 */
public class CenDocumentacionPresentadaAdm extends MasterBeanAdministrador 
{
	public CenDocumentacionPresentadaAdm(UsrBean usuario) {
		super(CenDocumentacionPresentadaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	CenDocumentacionPresentadaBean.C_FECHAMODIFICACION,	CenDocumentacionPresentadaBean.C_IDDOCUMENTACION,
							//CenDocumentacionPresentadaBean.C_IDINSTITUCION,
							CenDocumentacionPresentadaBean.C_IDSOLICITUD,		
							CenDocumentacionPresentadaBean.C_USUMODIFICACION};
							
		return campos;
	}

	protected String[] getClavesBean() {
		String[] campos = {	CenDocumentacionPresentadaBean.C_IDDOCUMENTACION, CenDocumentacionPresentadaBean.C_IDSOLICITUD};
		return campos;
	}

	protected String[] getOrdenCampos() {
		String[] campos = {	CenDocumentacionPresentadaBean.C_IDDOCUMENTACION, CenDocumentacionPresentadaBean.C_IDSOLICITUD};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenDocumentacionPresentadaBean bean = null;
		try {
			bean = new CenDocumentacionPresentadaBean();
			bean.setIdDocumentacion(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudBean.C_IDDOCUMENTACION));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash, CenDocumentacionPresentadaBean.C_IDSOLICITUD));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDocumentacionPresentadaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDocumentacionPresentadaBean.C_USUMODIFICACION));
			//bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenDocumentacionPresentadaBean.C_IDINSTITUCION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenDocumentacionPresentadaBean b = (CenDocumentacionPresentadaBean) bean;
			UtilidadesHash.set(htData, CenDocumentacionPresentadaBean.C_IDDOCUMENTACION, 	b.getIdDocumentacion());
			UtilidadesHash.set(htData, CenDocumentacionPresentadaBean.C_IDSOLICITUD, 		b.getIdSolicitud());
			UtilidadesHash.set(htData, CenDocumentacionPresentadaBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenDocumentacionPresentadaBean.C_USUMODIFICACION, 	b.getUsuMod());
			//UtilidadesHash.set(htData, CenDocumentacionPresentadaBean.C_IDINSTITUCION, 		b.getIdInstitucion());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
}