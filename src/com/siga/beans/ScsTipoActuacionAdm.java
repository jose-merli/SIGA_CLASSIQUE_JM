
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_HITOFACTURABLEGUARDIA
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsTipoActuacionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTipoActuacionAdm (UsrBean usuario) {
		super( ScsTipoActuacionBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsTipoActuacionBean.C_FECHAMODIFICACION,	    ScsTipoActuacionBean.C_IDINSTITUCION,
							ScsTipoActuacionBean.C_IDTIPOASISTENCIA,        ScsTipoActuacionBean.C_IDTIPOACTUACION,
							ScsTipoActuacionBean.C_IMPORTE,  ScsTipoActuacionBean.C_IMPORTEMAXIMO,           
							ScsTipoActuacionBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTipoActuacionBean.C_IDINSTITUCION,		ScsTipoActuacionBean.C_IDTIPOASISTENCIA, ScsTipoActuacionBean.C_IDTIPOACTUACION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTipoActuacionBean bean = null;
		try{
			bean = new ScsTipoActuacionBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsTipoActuacionBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsTipoActuacionBean.C_IDINSTITUCION));
			bean.setIdTipoAsistencia(UtilidadesHash.getInteger(hash,ScsTipoActuacionBean.C_IDTIPOASISTENCIA));
			bean.setIdTipoActuacion(UtilidadesHash.getInteger(hash,ScsTipoActuacionBean.C_IDTIPOACTUACION));
			bean.setImporte(Float.parseFloat((String)hash.get(ScsTipoActuacionBean.C_IMPORTE)));
			bean.setImporteMaximo(Float.parseFloat((String)hash.get(ScsTipoActuacionBean.C_IMPORTEMAXIMO)));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsTipoActuacionBean.C_USUMODIFICACION));
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
			ScsTipoActuacionBean b = (ScsTipoActuacionBean) bean;
			hash.put(ScsTipoActuacionBean.C_FECHAMODIFICACION,	b.getFechaMod());
			hash.put(ScsTipoActuacionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsTipoActuacionBean.C_IDTIPOASISTENCIA,	String.valueOf(b.getIdTipoAsistencia()));
			hash.put(ScsTipoActuacionBean.C_IDTIPOACTUACION,	String.valueOf(b.getIdTipoActuacion()));
			hash.put(ScsTipoActuacionBean.C_IMPORTE, String.valueOf(b.getImporte()));
			hash.put(ScsTipoActuacionBean.C_IMPORTEMAXIMO, String.valueOf(b.getImporteMaximo()));
			hash.put(ScsTipoActuacionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
		
}