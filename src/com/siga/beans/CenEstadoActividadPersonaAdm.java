/*
 * Created on 16-03-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
//import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
//import com.siga.censo.form.BusquedaClientesForm;
//import com.siga.general.SIGAException;

/**
 * @author pilar.duran
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenEstadoActividadPersonaAdm extends MasterBeanAdministrador {

	public CenEstadoActividadPersonaAdm(UsrBean usu) {
		super(CenEstadoActividadPersonaBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenEstadoActividadPersonaBean.C_MOTIVO, 	CenEstadoActividadPersonaBean.C_FECHAMODIFICACION, 
				            CenEstadoActividadPersonaBean.C_IDPERSONA,	CenEstadoActividadPersonaBean.C_IDESTADO, 
							CenEstadoActividadPersonaBean.C_FECHAESTADO, CenEstadoActividadPersonaBean.C_USUMODIFICACION,
							CenEstadoActividadPersonaBean.C_IDCODIGO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenEstadoActividadPersonaBean.C_IDPERSONA+" desc", CenEstadoActividadPersonaBean.C_IDCODIGO+" desc"};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenEstadoActividadPersonaBean bean = null;
		
		try {
			bean = new CenEstadoActividadPersonaBean();
			bean.setMotivo((String)hash.get(CenEstadoActividadPersonaBean.C_MOTIVO));
			bean.setFechaEstado((String)hash.get(CenEstadoActividadPersonaBean.C_FECHAESTADO));
			bean.setFechaMod((String)hash.get(CenEstadoActividadBean.C_FECHAMODIFICACION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, CenEstadoActividadPersonaBean.C_IDPERSONA));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, CenEstadoActividadPersonaBean.C_IDESTADO));
			bean.setUsuMod((UtilidadesHash.getInteger(hash, CenEstadoActividadPersonaBean.C_USUMODIFICACION)));
			bean.setIdCodigo((UtilidadesHash.getInteger(hash, CenEstadoActividadPersonaBean.C_IDCODIGO)));
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
			CenEstadoActividadPersonaBean b = (CenEstadoActividadPersonaBean) bean;
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_MOTIVO, b.getMotivo());
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_FECHAESTADO, b.getfechaEstado());
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_IDESTADO, String.valueOf(b.getIdEstado()));
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(htData, CenEstadoActividadPersonaBean.C_IDCODIGO, String.valueOf(b.getIdCodigo()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	public String getIdCodigo(String idpersona) throws ClsExceptions {
		String select="";
		RowsContainer rc=null;
		String idCodigo="";
		try{
			select=" SELECT NVL(MAX("+CenEstadoActividadPersonaBean.C_IDCODIGO+"),0)+1 CODIGO"+
			       " FROM "+CenEstadoActividadPersonaBean.T_NOMBRETABLA+
				   " WHERE "+CenEstadoActividadPersonaBean.C_IDPERSONA+" = "+idpersona;
			
			rc = new RowsContainer();
			if (rc.find(select)) {
				
					Hashtable htRow=((Row)rc.get(0)).getRow();
					idCodigo=(String)htRow.get("CODIGO");
			
			}else{
				idCodigo="1";
			}
		
		}
		
	    catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
	    return idCodigo;
	}
}
