/*
 * VERSIONES:
 * miguel.villegas - 4-2-2005 - Creacion 
 *	
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
*
* Clase que gestiona la tabla PYS_FORMAPAGOSERVICIOS de la BBDD
* 
*/

public class PysFormaPagoServiciosAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysFormaPagoServiciosAdm(UsrBean usu) {
		super(PysFormaPagoServiciosBean.T_NOMBRETABLA, usu);
	}

	/** 
	 * Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {PysFormaPagoServiciosBean.C_IDINSTITUCION,
							PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS ,
							PysFormaPagoServiciosBean.C_IDSERVICIO,
							PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,
							PysFormaPagoServiciosBean.C_IDFORMAPAGO,
							PysFormaPagoServiciosBean.C_INTERNET,
							PysFormaPagoServiciosBean.C_FECHAMODIFICACION,
							PysFormaPagoServiciosBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {PysFormaPagoServiciosBean.C_IDINSTITUCION,
							PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS ,
							PysFormaPagoServiciosBean.C_IDSERVICIO,
							PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,
							PysFormaPagoServiciosBean.C_IDFORMAPAGO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysFormaPagoServiciosBean bean = null;
		
		try {
			bean = new PysFormaPagoServiciosBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysFormaPagoServiciosBean.C_IDINSTITUCION));
			bean.setIdTipoServicios(UtilidadesHash.getInteger(hash,PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS));
			bean.setIdServicio(UtilidadesHash.getLong(hash,PysFormaPagoServiciosBean.C_IDSERVICIO));
			bean.setIdServiciosInstitucion(UtilidadesHash.getLong(hash,PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysFormaPagoServiciosBean.C_IDFORMAPAGO));
			bean.setInternet(UtilidadesHash.getString (hash,PysFormaPagoServiciosBean.C_INTERNET));
			bean.setFechaMod(UtilidadesHash.getString (hash,PysFormaPagoServiciosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysFormaPagoServiciosBean.C_USUMODIFICACION));			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysFormaPagoServiciosBean b = (PysFormaPagoServiciosBean) bean;
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_IDINSTITUCION,b.getIdInstitucion()); 
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_IDSERVICIO,b.getIdServicio());
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_IDSERVICIOSINSTITUCION,b.getIdServiciosInstitucion());
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_IDFORMAPAGO,b.getIdFormaPago());
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_INTERNET,b.getInternet()); 
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysFormaPagoServiciosBean.C_USUMODIFICACION, b.getUsuMod());
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


	
}
