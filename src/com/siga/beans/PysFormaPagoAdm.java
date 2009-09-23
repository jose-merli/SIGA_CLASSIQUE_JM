/*
 * VERSIONES:
 * 
 * miguel.villegas - 05-12-2004 - Creacion
 *	
 */

package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 *
 * Clase que gestiona la tabla PYS_FORMAPAGO de la BBDD
 * 
 */
public class PysFormaPagoAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysFormaPagoAdm(UsrBean usu) {
		super(PysFormaPagoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysFormaPagoBean.C_IDFORMAPAGO,
							PysFormaPagoBean.C_DESCRIPCION,
							PysFormaPagoBean.C_INTERNET};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysFormaPagoBean.C_IDFORMAPAGO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysFormaPagoBean bean = null;
		
		try {
			bean = new PysFormaPagoBean();
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysFormaPagoBean.C_IDFORMAPAGO));
			bean.setDescripcion(UtilidadesHash.getString(hash,PysFormaPagoBean.C_DESCRIPCION));			
			bean.setInternet(UtilidadesHash.getString(hash,PysFormaPagoBean.C_INTERNET));	
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
			PysFormaPagoBean b = (PysFormaPagoBean) bean; 
			UtilidadesHash.set(htData,PysFormaPagoBean.C_IDFORMAPAGO, b.getIdFormaPago());
			UtilidadesHash.set(htData,PysFormaPagoBean.C_DESCRIPCION, b.getDescripcion());			
			UtilidadesHash.set(htData,PysFormaPagoBean.C_INTERNET,b.getInternet());
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
