
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

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
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
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
	 *  @return bean con la informaci�n de la hashtable
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
	 *  @return hashtable con la informaci�n del bean
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
	 *  @return String[] conjunto de valores con los campos por los que se deber� ordenar la select
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
	/**
	 * 
	 * @param idInstitucion
	 * @param idTipoAsistenciaColegio
	 * @param isObligatorio
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsTipoActuacionBean> getTipoActuaciones(Integer idInstitucion, Integer idTipoAsistenciaColegio,boolean isObligatorio)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT IDINSTITUCION,IDTIPOACTUACION, substr(F_SIGA_GETRECURSO(DESCRIPCION,:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),this.usrbean.getLanguage());
		 
		sql.append(" ),0,60)   AS DESCRIPCION ");
		sql.append(" FROM SCS_TIPOACTUACION WHERE IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" AND IDTIPOASISTENCIA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTipoAsistenciaColegio);
		sql.append(" ORDER BY DESCRIPCION ");
		
		
		
		
		List<ScsTipoActuacionBean> tipoActuacionBeans = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	tipoActuacionBeans = new ArrayList<ScsTipoActuacionBean>();
            	ScsTipoActuacionBean tipoActuacionBean = new ScsTipoActuacionBean();
            	if(isObligatorio){
	            	tipoActuacionBean.setDescripcion(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
	            	tipoActuacionBean.setIdTipoActuacion(new Integer(-1));
            	}else{
            		tipoActuacionBean.setDescripcion("");
	            	
            	}
            	tipoActuacionBeans.add(tipoActuacionBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		tipoActuacionBean = new ScsTipoActuacionBean();
            		tipoActuacionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTipoActuacionBean.C_IDINSTITUCION));
            		tipoActuacionBean.setIdTipoActuacion(UtilidadesHash.getInteger(htFila,ScsTipoActuacionBean.C_IDTIPOACTUACION));
            		tipoActuacionBean.setDescripcion(UtilidadesHash.getString(htFila,ScsTipoActuacionBean.C_DESCRIPCION));
            		tipoActuacionBeans.add(tipoActuacionBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta getTipoActuaciones ");
       }
       return tipoActuacionBeans;
		
	}
	
	/**
	 * Devuelve un vector con los tipos de asistencias/actuaciones disponibles 
	 * para configurar un coste fijo:
	 * - Si estamos editando/consultando una relaci�n coste fijo - tipo asistencia se muestran
	 * solamente los tipos de actuaciones disponibles para la asistencia pasada como par�metro 
	 * (incluidas las que est�n ya relacionadas con el coste fijo).
	 * - Si estamos a�adiendo una nueva relaci�n coste fijo - tipo asistencia se recuperan 
	 * solamente los tipos de asistencias (y sus tipos de actuaciones) que no est�n relacionadas
	 * con el coste fijo
	 * Los registros se ordenan por descripci�n
	 * 
	 * @param idInstitucion
	 * @param idCosteFijo
	 * @param idTipoAsistencia
	 * @param idioma
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> getTiposAsistTiposActDispCosteFijo (String idInstitucion, String idCosteFijo, String idTipoAsistencia, String idioma) throws ClsExceptions {
		Vector<Hashtable<String,Object>> resultado = new Vector<Hashtable<String,Object>>();
		
		try {
			String sql = "SELECT SCS_TIPOACTUACION.IDINSTITUCION, " + 
							" SCS_TIPOACTUACION.IDTIPOASISTENCIA, " + 
							" SCS_TIPOACTUACION.IDTIPOACTUACION, " +
							" F_SIGA_GETRECURSO(SCS_TIPOACTUACION.DESCRIPCION, " + idioma + ") AS DSTIPOACTUACION, " +
							" ( " + 
								" SELECT F_SIGA_GETRECURSO(SCS_TIPOASISTENCIACOLEGIO.DESCRIPCION, " + idioma + ") " +
								" FROM SCS_TIPOASISTENCIACOLEGIO " + 
								" WHERE SCS_TIPOASISTENCIACOLEGIO.IDINSTITUCION = SCS_TIPOACTUACION.IDINSTITUCION " +
									" AND SCS_TIPOASISTENCIACOLEGIO.IDTIPOASISTENCIACOLEGIO = SCS_TIPOACTUACION.IDTIPOASISTENCIA " +
							" ) AS DSTIPOASISTENCIA, " +
							" ( " +
								" SELECT REPLACE(NVL(SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE,0),'.',',') " +
								" FROM SCS_TIPOACTUACIONCOSTEFIJO " +
								" WHERE SCS_TIPOACTUACIONCOSTEFIJO.IDINSTITUCION = SCS_TIPOACTUACION.IDINSTITUCION " +
									" AND SCS_TIPOACTUACIONCOSTEFIJO.IDTIPOASISTENCIA = SCS_TIPOACTUACION.IDTIPOASISTENCIA " +
									" AND SCS_TIPOACTUACIONCOSTEFIJO.IDTIPOACTUACION = SCS_TIPOACTUACION.IDTIPOACTUACION " +
									" AND SCS_TIPOACTUACIONCOSTEFIJO.IDCOSTEFIJO = " + idCosteFijo + 
							" ) AS IMPORTECOSTE " +
						" FROM SCS_TIPOACTUACION " +
						" WHERE SCS_TIPOACTUACION.IDINSTITUCION = " + idInstitucion;
			
			if (idTipoAsistencia!=null && !idTipoAsistencia.equals("")) {
				sql += " AND SCS_TIPOACTUACION.IDTIPOASISTENCIA = " + idTipoAsistencia;
			
			} else { //Estamos relacionando un nuevo tipo de asistencia con el coste fijo
				sql += " AND NOT EXISTS ( " +
							" SELECT 1 " +
							" FROM SCS_TIPOACTUACIONCOSTEFIJO " +
							" WHERE SCS_TIPOACTUACIONCOSTEFIJO.IDINSTITUCION = SCS_TIPOACTUACION.IDINSTITUCION " +
								" AND SCS_TIPOACTUACIONCOSTEFIJO.IDTIPOASISTENCIA = SCS_TIPOACTUACION.IDTIPOASISTENCIA " +
								" AND SCS_TIPOACTUACIONCOSTEFIJO.IDTIPOACTUACION = SCS_TIPOACTUACION.IDTIPOACTUACION " +
								" AND SCS_TIPOACTUACIONCOSTEFIJO.IDCOSTEFIJO = " + idCosteFijo +
						" ) ";
			}
			
			sql += " ORDER BY DSTIPOASISTENCIA, DSTIPOACTUACION ASC";
			
			resultado = this.selectGenerico(sql);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error en ScsTipoActuacionAdm.getTiposAsistTiposActDispCosteFijo()");
		}	
		
		return resultado;
	}
}