
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
import com.siga.Utilidades.paginadores.PaginadorBind;

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
	 * @param idInstitucion
	 * @param idCosteFijo
	 * @return
	 */
	public PaginadorBind getTiposAsistTiposActDispCosteFijo (String idInstitucion, String idCosteFijo, String idTipoAsistencia,boolean regBajaLog, String lang) throws ClsExceptions{

		String select="";	
		String from ="";	
		String where="";	
		String orderBy ="";	
		String selectContar="";	
		Hashtable codigosBind = new Hashtable();
		int contador = 0;
		try {
			select+="SELECT TAS.IDTIPOASISTENCIA, ";	 
			select+="       TAS.IDTIPOACTUACION, ";	 
			select+="F_SIGA_GETRECURSO(TAS.DESCRIPCION,"+lang+") AS DSTIPOACTUACION,"; 
			select+="(SELECT F_SIGA_GETRECURSO(DESCRIPCION,"+lang+") ";
			select+="   FROM SCS_TIPOASISTENCIACOLEGIO ";
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			select+="  WHERE IDINSTITUCION=:"+contador;
			select+="    AND IDTIPOASISTENCIACOLEGIO=TAS.IDTIPOASISTENCIA) AS DSTIPOASISTENCIA,";
			select+="(SELECT COUNT(*) ";
			select+="   FROM SCS_TIPOACTUACIONCOSTEFIJO ";
			select+="  WHERE IDINSTITUCION=TAS.IDINSTITUCION ";
			select+="    AND IDTIPOASISTENCIA=TAS.IDTIPOASISTENCIA ";
			select+="    AND IDTIPOACTUACION=TAS.IDTIPOACTUACION ";
			contador++;
			codigosBind.put(new Integer(contador),idCosteFijo.toString());
			select+="    AND IDCOSTEFIJO=:"+contador+") AS SELECCIONADO, ";	
			select+="(SELECT REPLACE(TO_CHAR(NVL(IMPORTE,0),'9999999999.99'),'.',',') ";
			select+="   FROM SCS_TIPOACTUACIONCOSTEFIJO ";
			select+="  WHERE IDINSTITUCION=TAS.IDINSTITUCION ";
			select+="    AND IDTIPOASISTENCIA=TAS.IDTIPOASISTENCIA ";
			select+="    AND IDTIPOACTUACION=TAS.IDTIPOACTUACION ";
			contador++;
			codigosBind.put(new Integer(contador),idCosteFijo.toString());
			select+="    AND IDCOSTEFIJO=:"+contador+") AS IMPORTECOSTE, ";
			select+="    TAS.IDINSTITUCION, ";
			select+="    TAS.IMPORTE, ";
			select+="    TAS.IMPORTEMAXIMO ";
			from+="  FROM SCS_TIPOACTUACION TAS ";
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			where+=" WHERE TAS.IDINSTITUCION=:"+contador;
		
			//Estamos editando o consultando un tipo de asistencia relacionada con el coste fijo
			if((idTipoAsistencia!=null)&&(!idTipoAsistencia.equals(""))){
				contador++;
				codigosBind.put(new Integer(contador),idTipoAsistencia.toString());
				where+="   AND TAS.IDTIPOASISTENCIA=:"+contador;
			//Estamos relacionando un nuevo tipo de asistencia con el coste fijo
			}else{
			    where+="   AND NOT EXISTS (SELECT 1 ";
			    where+="                     FROM SCS_TIPOACTUACIONCOSTEFIJO ACF ";
			    where+="                    WHERE ACF.IDINSTITUCION=TAS.IDINSTITUCION ";
			    where+="                      AND ACF.IDTIPOASISTENCIA=TAS.IDTIPOASISTENCIA ";
			    where+="                      AND ACF.IDTIPOACTUACION=TAS.IDTIPOACTUACION ";
			    contador++;
				codigosBind.put(new Integer(contador),idCosteFijo.toString());
			    where+="                      AND ACF.IDCOSTEFIJO=:"+contador+")";

			}
			
			//Las relaciones que est�n de baja no las mostramos. Si tienen fecha de baja es porque 
			//si se elimina la relaci�n en SCS_TIPOACTUACIONCOSTEFIJO entre tipo asistencia-tipo actuaci�n-coste fijo 
			//si existe un registro en SCS_ACTUACIONASISTCOSTEFIJO se da de baja l�gica en vez de realizar el borrado f�sico  
			where+="   AND EXISTS (SELECT 1 ";
		    where+="                     FROM SCS_TIPOACTUACIONCOSTEFIJO ACF ";
		    where+="                    WHERE ACF.IDINSTITUCION=TAS.IDINSTITUCION ";
		    where+="                      AND ACF.IDTIPOASISTENCIA=TAS.IDTIPOASISTENCIA ";
		    where+="                      AND ACF.IDTIPOACTUACION=TAS.IDTIPOACTUACION ";
		   
		    if(regBajaLog)
		    	where+="                      AND ACF.FECHABAJA IS NOT NULL) ";
		    else
		    	where+="                      AND ACF.FECHABAJA IS NULL) ";
			
			orderBy+=" ORDER BY DSTIPOASISTENCIA,DSTIPOACTUACION ASC";

			selectContar+=" SELECT 1 "+from+where;
			select+=from+where+orderBy;
	
			PaginadorBind resultado = new PaginadorBind(select,selectContar,codigosBind);				
			int totalRegistros = resultado.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				resultado =null;
			}
			
			return resultado;
						
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error en ScsActuacionAsistCosteFijo.getTiposAsistTiposActDispCosteFijo()" + select);
		}		

	}
		
}