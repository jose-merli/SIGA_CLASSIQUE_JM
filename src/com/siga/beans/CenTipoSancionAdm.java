package com.siga.beans;

import java.util.Hashtable;
//import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
//import com.atos.utils.Row;
//import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla CEN_TIPOSANCION
 * 
 * @author RGG
 * @since 16/03/2007
 */
public class CenTipoSancionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public CenTipoSancionAdm (UsrBean usuario) {
		super( CenTipoSancionBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	CenTipoSancionBean.C_IDTIPOSANCION, CenTipoSancionBean.C_DESCRIPCION, CenTipoSancionBean.C_CODIGOEXT,
							CenTipoSancionBean.C_USUMODIFICACION, CenTipoSancionBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	CenTipoSancionBean.C_IDTIPOSANCION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTipoSancionBean bean = null;
		try{
			bean = new CenTipoSancionBean();
			bean.setIdTipoSancion(UtilidadesHash.getInteger(hash,CenTipoSancionBean.C_IDTIPOSANCION));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTipoSancionBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,CenTipoSancionBean.C_CODIGOEXT));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, CenTipoSancionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTipoSancionBean.C_USUMODIFICACION));
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
			CenTipoSancionBean b = (CenTipoSancionBean) bean;
			hash.put(CenTipoSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
			hash.put(CenTipoSancionBean.C_DESCRIPCION, b.getDescripcion());
			hash.put(CenTipoSancionBean.C_CODIGOEXT, b.getCodigoExt());

			hash.put(CenTipoSancionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(CenTipoSancionBean.C_FECHAMODIFICACION, b.getFechaMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	public String[] getOrdenCampos() {
		String[] vector = {CenTipoSancionBean.C_DESCRIPCION};
		return vector;
	}


	
}