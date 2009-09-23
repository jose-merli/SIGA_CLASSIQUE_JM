
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla GEN_RECURSOS_CATALOGOS
 */
public class GenCatalogosMultiidiomaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public GenCatalogosMultiidiomaAdm (UsrBean usuario) {
		super( GenCatalogosMultiidiomaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposBean(){
		String[] campos = { GenCatalogosMultiidiomaBean.C_CAMPOTABLA,	GenCatalogosMultiidiomaBean.C_CODIGO,
							GenCatalogosMultiidiomaBean.C_CODIGOTABLA,	GenCatalogosMultiidiomaBean.C_FECHAMODIFICACION,
							GenCatalogosMultiidiomaBean.C_LOCAL,		GenCatalogosMultiidiomaBean.C_MIGRADO,
							GenCatalogosMultiidiomaBean.C_NOMBRETABLA,	GenCatalogosMultiidiomaBean.C_USUMODIFICACION};
		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	GenCatalogosMultiidiomaBean.C_CODIGO };
		return campos;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return this.getClavesBean();
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		GenCatalogosMultiidiomaBean bean = null;
		try {
			bean = new GenCatalogosMultiidiomaBean();
			bean.setCampoTabla(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_CAMPOTABLA));
			bean.setCodigo(UtilidadesHash.getInteger(hash, GenCatalogosMultiidiomaBean.C_CODIGO));
			bean.setCodigoTabla(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_CODIGOTABLA));
			bean.setFechaMod(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_FECHAMODIFICACION));
			bean.setLocal(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_LOCAL));
			bean.setMigrado(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_MIGRADO));
			bean.setNombreTabla(UtilidadesHash.getString(hash, GenCatalogosMultiidiomaBean.C_NOMBRETABLA));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenCatalogosMultiidiomaBean.C_USUMODIFICACION));
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
		try
		{
			hash = new Hashtable();
			GenCatalogosMultiidiomaBean b = (GenCatalogosMultiidiomaBean) bean;
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_CAMPOTABLA,			b.getCampoTabla());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_CODIGO,				b.getCodigo());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_CODIGOTABLA,			b.getCodigoTabla());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_FECHAMODIFICACION,	b.getFechaMod());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_LOCAL,				b.getLocal());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_MIGRADO,				b.getMigrado());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_NOMBRETABLA,			b.getNombreTabla());
			UtilidadesHash.set(hash, GenCatalogosMultiidiomaBean.C_USUMODIFICACION,		b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
}