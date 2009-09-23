
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPOFUNDAMENTOS
 */
public class ScsTipoFundamentosAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTipoFundamentosAdm (UsrBean usuario) {
		super( ScsTipoFundamentosBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsTipoFundamentosBean.C_IDFUNDAMENTO,
				ScsTipoFundamentosBean.C_CODIGO,
				ScsTipoFundamentosBean.C_DESCRIPCION,
				ScsTipoFundamentosBean.C_FECHAMODIFICACION,
				ScsTipoFundamentosBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTipoFundamentosBean.C_IDFUNDAMENTO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTipoFundamentosBean bean = null;
		try{
			bean = new ScsTipoFundamentosBean();
			bean.setIdFundamento(UtilidadesHash.getInteger(hash,ScsTipoFundamentosBean.C_IDFUNDAMENTO));
			bean.setCodigo(UtilidadesHash.getString(hash,ScsTipoFundamentosBean.C_CODIGO));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsTipoFundamentosBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsTipoFundamentosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsTipoFundamentosBean.C_USUMODIFICACION));
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
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsTipoFundamentosBean b = (ScsTipoFundamentosBean) bean;
			UtilidadesHash.set(hash, ScsTipoFundamentosBean.C_IDFUNDAMENTO,String.valueOf(b.getIdFundamento()));
			UtilidadesHash.set(hash, ScsTipoFundamentosBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(hash, ScsTipoFundamentosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsTipoFundamentosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsTipoFundamentosBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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