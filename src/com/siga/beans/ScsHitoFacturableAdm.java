
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_HITOFACTURABLE
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsHitoFacturableAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsHitoFacturableAdm (UsrBean usuario) {
		super( ScsHitoFacturableBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsHitoFacturableBean.C_APLICABLEA,			ScsHitoFacturableBean.C_DESCRIPCION,
							ScsHitoFacturableBean.C_FECHAMODIFICACION,	ScsHitoFacturableBean.C_IDHITO,
							ScsHitoFacturableBean.C_NOMBRE,				ScsHitoFacturableBean.C_SQLSELECCION,
							ScsHitoFacturableBean.C_SQLUPDATE,			ScsHitoFacturableBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsHitoFacturableBean.C_IDHITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsHitoFacturableBean bean = null;
		try{
			bean = new ScsHitoFacturableBean();
			bean.setAplicableA(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_APLICABLEA));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_DESCRIPCION));
			bean.setIdHito(UtilidadesHash.getInteger(hash,ScsHitoFacturableBean.C_IDHITO));
			bean.setNombre(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_NOMBRE));
			bean.setSqlSeleccion(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_SQLSELECCION));
			bean.setSqlUpdate(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_SQLUPDATE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsHitoFacturableBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_FECHAMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsHitoFacturableBean b = (ScsHitoFacturableBean) bean;
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_APLICABLEA, b.getAplicableA());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_IDHITO, b.getIdHito());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_SQLSELECCION, b.getSqlSeleccion());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_SQLUPDATE, b.getSqlUpdate());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_FECHAMODIFICACION, b.getFechaMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
		
}