package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FacFormaPagoSerieAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacFormaPagoSerieAdm(UsrBean usu) {
		super(FacFormaPagoSerieBean.T_NOMBRETABLA, usu);
	}

	/** 
	 * Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {FacFormaPagoSerieBean.C_IDINSTITUCION,
							FacFormaPagoSerieBean.C_IDSERIEFACTURACION ,
							FacFormaPagoSerieBean.C_IDFORMAPAGO,
							FacFormaPagoSerieBean.C_FECHAMODIFICACION,
							FacFormaPagoSerieBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {FacFormaPagoSerieBean.C_IDINSTITUCION,
							FacFormaPagoSerieBean.C_IDSERIEFACTURACION ,
							FacFormaPagoSerieBean.C_IDFORMAPAGO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFormaPagoSerieBean bean = null;
		
		try {
			bean = new FacFormaPagoSerieBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacFormaPagoSerieBean.C_IDINSTITUCION));
			bean.setIdSerieFacturacion(UtilidadesHash.getInteger(hash,FacFormaPagoSerieBean.C_IDSERIEFACTURACION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_IDFORMAPAGO));
			bean.setFechaMod(UtilidadesHash.getString (hash,PysFormaPagoProductoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_USUMODIFICACION));	
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
			FacFormaPagoSerieBean b = (FacFormaPagoSerieBean) bean;
			UtilidadesHash.set(htData,FacFormaPagoSerieBean.C_IDINSTITUCION,b.getIdInstitucion()); 
			UtilidadesHash.set(htData,FacFormaPagoSerieBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData,FacFormaPagoSerieBean.C_IDFORMAPAGO,b.getIdFormaPago());
			UtilidadesHash.set(htData,FacFormaPagoSerieBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,FacFormaPagoSerieBean.C_USUMODIFICACION, b.getUsuMod());
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
