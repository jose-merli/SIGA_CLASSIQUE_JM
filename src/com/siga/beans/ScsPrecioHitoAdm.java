
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_PRECIOHITO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsPrecioHitoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsPrecioHitoAdm (UsrBean usuario) {
		super( ScsPrecioHitoBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsPrecioHitoBean.C_IDHITOFACTURABLE,
				 			ScsPrecioHitoBean.C_FECHAENTRADAVIGOR,
				 			ScsPrecioHitoBean.C_PRECIO,
							ScsPrecioHitoBean.C_USUMODIFICACION,
							ScsPrecioHitoBean.C_FECHAMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsPrecioHitoBean.C_IDHITOFACTURABLE,
	 						ScsPrecioHitoBean.C_FECHAENTRADAVIGOR};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPrecioHitoBean bean = null;
		try{
			bean = new ScsPrecioHitoBean();
			bean.setIdHitoFacturable(UtilidadesHash.getInteger(hash,ScsPrecioHitoBean.C_IDHITOFACTURABLE));
			bean.setFechaEntradaVigor(UtilidadesHash.getString(hash,ScsPrecioHitoBean.C_FECHAENTRADAVIGOR));
			bean.setPrecio(UtilidadesHash.getInteger(hash,ScsPrecioHitoBean.C_PRECIO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsPrecioHitoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsPrecioHitoBean.C_USUMODIFICACION));
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
			ScsPrecioHitoBean b = (ScsPrecioHitoBean) bean;
			hash.put(ScsPrecioHitoBean.C_IDHITOFACTURABLE,b.getIdHitoFacturable());
			hash.put(ScsPrecioHitoBean.C_FECHAENTRADAVIGOR, String.valueOf(b.getFechaEntradaVigor()));
			hash.put(ScsPrecioHitoBean.C_PRECIO, String.valueOf(b.getPrecio()));
			hash.put(ScsPrecioHitoBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(ScsPrecioHitoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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