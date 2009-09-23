
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPODESIGNACOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsTipoDesignaColegioAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTipoDesignaColegioAdm (UsrBean usuario) {
		super( ScsTipoDesignaColegioBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsTipoDesignaColegioBean.C_DESCRIPCION,
				ScsTipoDesignaColegioBean.C_FECHAMODIFICACION,
				ScsTipoDesignaColegioBean.C_CODIGOEXT,
							ScsTipoDesignaColegioBean.C_IDINSTITUCION,
							ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO,
							ScsTipoDesignaColegioBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTipoDesignaColegioBean.C_IDINSTITUCION,
							ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTipoDesignaColegioBean bean = null;
		try{
			bean = new ScsTipoDesignaColegioBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsTipoDesignaColegioBean.C_IDINSTITUCION));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsTipoDesignaColegioBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsTipoDesignaColegioBean.C_CODIGOEXT));
			bean.setIdTipoDesignaColegiado(UtilidadesHash.getInteger(hash,ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsTipoDesignaColegioBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsTipoDesignaColegioBean.C_USUMODIFICACION));
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
			ScsTipoDesignaColegioBean b = (ScsTipoDesignaColegioBean) bean;
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO, String.valueOf(b.getIdTipoDesignaColegiado()));
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsTipoDesignaColegioBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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