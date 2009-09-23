/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacEstadoAbonoAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacEstadoAbonoAdm(UsrBean usu) {
		super(FacEstadoAbonoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacEstadoAbonoBean.C_IDESTADO,
				FacEstadoAbonoBean.C_DESCRIPCION,
				FacEstadoAbonoBean.C_LENGUAJE};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacEstadoAbonoBean.C_IDESTADO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = {FacEstadoAbonoBean.C_IDESTADO};
		return orden;
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacEstadoAbonoBean bean = null;
		
		try {
			bean = new FacEstadoAbonoBean();
			bean.setIdEstado(UtilidadesHash.getInteger(hash,FacEstadoAbonoBean.C_IDESTADO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,FacEstadoAbonoBean.C_DESCRIPCION));
			bean.setLenguaje(UtilidadesHash.getString(hash,FacEstadoAbonoBean.C_LENGUAJE));
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
			FacEstadoAbonoBean b = (FacEstadoAbonoBean) bean;
			UtilidadesHash.set(htData,FacEstadoAbonoBean.C_IDESTADO,b.getIdEstado());
			UtilidadesHash.set(htData,FacEstadoAbonoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData,FacEstadoAbonoBean.C_LENGUAJE,b.getLenguaje());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}


}
