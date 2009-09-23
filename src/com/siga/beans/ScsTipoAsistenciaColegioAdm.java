
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

public class ScsTipoAsistenciaColegioAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTipoAsistenciaColegioAdm (UsrBean usuario) {
		super( ScsTipoAsistenciaColegioBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsTipoAsistenciaColegioBean.C_FECHAMODIFICACION,	    ScsTipoAsistenciaColegioBean.C_IDINSTITUCION,
				            ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO,	ScsTipoAsistenciaColegioBean.C_IMPORTE,
				            ScsTipoAsistenciaColegioBean.C_IMPORTEMAXIMO,           ScsTipoAsistenciaColegioBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTipoAsistenciaColegioBean.C_IDINSTITUCION,		ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTipoAsistenciaColegioBean bean = null;
		try{
			bean = new ScsTipoAsistenciaColegioBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsTipoAsistenciaColegioBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsTipoAsistenciaColegioBean.C_IDINSTITUCION));
			bean.setIdTipoAsistenciaColegio(UtilidadesHash.getInteger(hash,ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO));
			bean.setImporte(Float.parseFloat((String)hash.get(ScsTipoAsistenciaColegioBean.C_IMPORTE)));
			bean.setImporteMaximo(Float.parseFloat((String)hash.get(ScsTipoAsistenciaColegioBean.C_IMPORTEMAXIMO)));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsTipoAsistenciaColegioBean.C_USUMODIFICACION));
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
			ScsTipoAsistenciaColegioBean b = (ScsTipoAsistenciaColegioBean) bean;
			hash.put(ScsTipoAsistenciaColegioBean.C_FECHAMODIFICACION,	b.getFechaMod());
			hash.put(ScsTipoAsistenciaColegioBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO,	String.valueOf(b.getIdTipoAsistenciaColegio()));
			hash.put(ScsTipoAsistenciaColegioBean.C_IMPORTE, String.valueOf(b.getImporte()));
			hash.put(ScsTipoAsistenciaColegioBean.C_IMPORTEMAXIMO, String.valueOf(b.getImporteMaximo()));
			hash.put(ScsHitoFacturableGuardiaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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