
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPOFUNDAMENTOCALIF
 */
public class ScsTipoFundamentosCalifAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTipoFundamentosCalifAdm (UsrBean usuario) {
		super( ScsTipoFundamentosCalifBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsTipoFundamentosCalifBean.C_IDFUNDAMENTOCALIF,
				ScsTipoFundamentosCalifBean.C_IDINSTITUCION,
				ScsTipoFundamentosCalifBean.C_CODIGO,
				ScsTipoFundamentosCalifBean.C_DESCRIPCION,
				ScsTipoFundamentosCalifBean.C_IDTIPODICTAMENEJG,
				ScsTipoFundamentosCalifBean.C_FECHAMODIFICACION,
				ScsTipoFundamentosCalifBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTipoFundamentosCalifBean.C_IDFUNDAMENTOCALIF, ScsTipoFundamentosCalifBean.C_IDINSTITUCION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTipoFundamentosCalifBean bean = null;
		try{
			bean = new ScsTipoFundamentosCalifBean();
			bean.setIdFundamentoCalif(UtilidadesHash.getInteger(hash,ScsTipoFundamentosCalifBean.C_IDFUNDAMENTOCALIF));
			bean.setCodigo(UtilidadesHash.getString(hash,ScsTipoFundamentosCalifBean.C_CODIGO));
			bean.setIdInstitucion(UtilidadesHash.getString(hash,ScsTipoFundamentosCalifBean.C_IDINSTITUCION));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsTipoFundamentosCalifBean.C_DESCRIPCION));
			bean.setIdTipoDictamenEJG(UtilidadesHash.getInteger(hash,ScsTipoFundamentosCalifBean.C_IDTIPODICTAMENEJG));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsTipoFundamentosCalifBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsTipoFundamentosCalifBean.C_USUMODIFICACION));
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
			ScsTipoFundamentosCalifBean b = (ScsTipoFundamentosCalifBean) bean;
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_IDFUNDAMENTOCALIF,String.valueOf(b.getIdFundamentoCalif()));
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_IDTIPODICTAMENEJG, b.getIdTipoDictamenEJG());
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsTipoFundamentosCalifBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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