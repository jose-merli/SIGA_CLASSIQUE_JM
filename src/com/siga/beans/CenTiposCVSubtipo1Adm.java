
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
public class CenTiposCVSubtipo1Adm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public CenTiposCVSubtipo1Adm (UsrBean usuario) {
		super( CenTiposCVSubtipo1Bean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenTiposCVSubtipo1Bean.C_IDTIPOCV,
				            CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1,
				            CenTiposCVSubtipo1Bean.C_IDINSTITUCION,
				            CenTiposCVSubtipo1Bean.C_CODIGOEXT,
				            CenTiposCVSubtipo1Bean.C_DESCRIPCION,
							CenTiposCVSubtipo1Bean.C_FECHAMODIFICACION,
							CenTiposCVSubtipo1Bean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenTiposCVSubtipo1Bean.C_IDTIPOCV,
				            CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1,
							CenTiposCVSubtipo1Bean.C_IDINSTITUCION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTiposCVSubtipo1Bean bean = null;
		try{
			bean = new CenTiposCVSubtipo1Bean();
			bean.setIdTipoCV(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo1Bean.C_IDTIPOCV));
			bean.setIdTipoCVSubtipo1(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo1Bean.C_IDINSTITUCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,CenTiposCVSubtipo1Bean.C_CODIGOEXT));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTiposCVSubtipo1Bean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenTiposCVSubtipo1Bean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTiposCVSubtipo1Bean.C_USUMODIFICACION));
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
			CenTiposCVSubtipo1Bean b = (CenTiposCVSubtipo1Bean) bean;
			hash.put(CenTiposCVSubtipo1Bean.C_IDTIPOCV,b.getIdTipoCV());
			hash.put(CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1, String.valueOf(b.getIdTipoCVSubtipo1()));
			hash.put(CenTiposCVSubtipo1Bean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenTiposCVSubtipo1Bean.C_CODIGOEXT, b.getCodigoExt());
			hash.put(CenTiposCVSubtipo1Bean.C_DESCRIPCION, b.getDescripcion());
			hash.put(CenTiposCVSubtipo1Bean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(CenTiposCVSubtipo1Bean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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