
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_PRECIOHITO
 * 
 * @author pdm
 * @since 05/10/2007
 */
public class CenTiposCVSubtipo2Adm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public CenTiposCVSubtipo2Adm (UsrBean usuario) {
		super( CenTiposCVSubtipo2Bean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenTiposCVSubtipo2Bean.C_IDTIPOCV,
							CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2,
							CenTiposCVSubtipo2Bean.C_IDINSTITUCION,
							CenTiposCVSubtipo2Bean.C_CODIGOEXT,
							CenTiposCVSubtipo2Bean.C_DESCRIPCION,
							CenTiposCVSubtipo2Bean.C_FECHAMODIFICACION,
							CenTiposCVSubtipo2Bean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenTiposCVSubtipo2Bean.C_IDTIPOCV,
				            CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2,
							CenTiposCVSubtipo2Bean.C_IDINSTITUCION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTiposCVSubtipo2Bean bean = null;
		try{
			bean = new CenTiposCVSubtipo2Bean();
			bean.setIdTipoCV(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo2Bean.C_IDTIPOCV));
			bean.setIdTipoCVSubtipo2(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo2Bean.C_IDINSTITUCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,CenTiposCVSubtipo2Bean.C_CODIGOEXT));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTiposCVSubtipo2Bean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenTiposCVSubtipo2Bean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo2Bean.C_USUMODIFICACION));
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
			CenTiposCVSubtipo2Bean b = (CenTiposCVSubtipo2Bean) bean;
			hash.put(CenTiposCVSubtipo2Bean.C_IDTIPOCV,b.getIdTipoCV());
			hash.put(CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2, String.valueOf(b.getIdTipoCVSubtipo2()));
			hash.put(CenTiposCVSubtipo2Bean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenTiposCVSubtipo2Bean.C_CODIGOEXT, b.getCodigoExt());
			hash.put(CenTiposCVSubtipo2Bean.C_DESCRIPCION, b.getDescripcion());
			hash.put(CenTiposCVSubtipo2Bean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(CenTiposCVSubtipo2Bean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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