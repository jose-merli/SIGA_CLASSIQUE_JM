
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;

//Clase: ScsRetencionesAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 21/12/2004
/**
* Implementa las operaciones sobre la base de datos, es decir: select, insert, update...  
*/
public class ScsPartidaPresupuestariaAdm extends MasterBeanAdministrador {

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsPartidaPresupuestariaAdm(UsrBean usuario) {
		super(ScsPartidaPresupuestariaBean.T_NOMBRETABLA, usuario);		
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_PARTIDAPRESUPUESTARIA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try {		
			String where = " WHERE " + ScsPartidaPresupuestariaBean.C_IDINSTITUCION + " = " + hash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION) + " and " + ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA + " = " + hash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA);			
			datos = this.select(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_PARTIDAPRESUPUESTARIA con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT 
	 */
	public Vector select(Hashtable miHash) throws ClsExceptions {

		Vector datos = new Vector();		
		
		try {
			String where = " where (";
			// En función de los criterios de búsqueda se construye el where
			if ((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA) != null &&
				((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA)).length() > 0){
				where += ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA + " = " + Integer.parseInt((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA));
				
				if ((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)!= null && 
					((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)).length() > 0){				
					where += " and " + ScsPartidaPresupuestariaBean.C_IDINSTITUCION + " = " + Integer.parseInt((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)) + ")";			
				}
			}
			else {
				if (((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION) != null) &&
					((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)).length() > 0) {				 
					where += ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)).trim(),ScsPartidaPresupuestariaBean.C_DESCRIPCION );
				}
				if (((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION) != null) &&
					((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)).length() > 0)
				{
						if (((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA) != null) &&
							((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)).length() > 0) 
						{
								where += " and ";
						}
				}
				if (((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA) != null) &&
						((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)).length() > 0) 
					{
						 where += ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)).trim(),ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA);
				}
				
				if 	((((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)).length() > 0) || 
					 (((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)).length() > 0)) {
						where += " and ( ";
				}
				// En todas las consultas debe cumplirse que el idinstitucion sea igual al del usuario o a null (para mantener la consistencia de la base de datos)
				if ((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION) != null && 
					((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)).length() > 0){				
					where += ScsPartidaPresupuestariaBean.C_IDINSTITUCION + " = " + Integer.parseInt((String)miHash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)) + " or " + ScsPartidaPresupuestariaBean.C_IDINSTITUCION + " IS NULL) ";			
				}		
				if ((((String)miHash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION)).length()>0) || (((String)miHash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA)).length()>0)){
					where +=")";
				}		
			}		
			datos = this.select(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR");
		}	
		return datos;	
	}
	
	
	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryNLSBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + ScsPartidaPresupuestariaBean.T_NOMBRETABLA + where;			
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la partida presupuestaria que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		String sql="";
		
		try {			
			rc = new RowsContainer();
			/* Se realiza una consulta en la base de datos para calcular el identificador de la nueva retención. */		
			sql ="SELECT (MAX(IDPARTIDAPRESUPUESTARIA) + 1) AS IDPARTIDAPRESUPUESTARIA FROM " + nombreTabla + " WHERE IDINSTITUCION = " + entrada.get("IDINSTITUCION") ;	
					
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPARTIDAPRESUPUESTARIA").equals("")) {
					entrada.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,"1");
				}
				else entrada.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,(String)prueba.get("IDPARTIDAPRESUPUESTARIA"));								
			}
		}				
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDPARTIDAPRESUPUESTARIA");
		};
		return entrada;
	}
	
	/**
	 * Efectúa un DELETE en la tabla SCS_MAESTRORETENCIONES del registro seleccionado 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean delete(Hashtable hash) throws ClsExceptions{

		try {
			Row row = new Row();	
			row.load(hash);

			String [] campos = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, campos) == 1) {
				return true;
			}		
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BORRADO");
		}
		return false;
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	protected String[] getCamposBean() {
		String[] campos = {	ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,	ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA,
							ScsPartidaPresupuestariaBean.C_DESCRIPCION,				ScsPartidaPresupuestariaBean.C_IDINSTITUCION,
							ScsPartidaPresupuestariaBean.C_USUMODIFICACION,			ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION,
							ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		String[] campos = {ScsPartidaPresupuestariaBean.C_IDINSTITUCION	,ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPartidaPresupuestariaBean bean = null;
		try{
			bean = new ScsPartidaPresupuestariaBean();
			bean.setIdPartidaPresupuestaria(Integer.valueOf((String)hash.get(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA)));			
			bean.setImportePartida(Float.valueOf(hash.get(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA).toString()));			
			bean.setNombrePartida((String)hash.get(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA));
			bean.setDescripcion((String)hash.get(ScsPartidaPresupuestariaBean.C_DESCRIPCION));
			if (!((String)hash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)).equalsIgnoreCase("")){
				bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsPartidaPresupuestariaBean.C_IDINSTITUCION)));
			}			
			bean.setFechaMod((String)hash.get(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(Integer.valueOf((String)hash.get(ScsPartidaPresupuestariaBean.C_USUMODIFICACION)));
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsPartidaPresupuestariaBean b = (ScsPartidaPresupuestariaBean) bean;
			hash.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,b.getIdPartidaPresupuestaria().toString());
			hash.put(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA,b.getImportePartida().toString());
			hash.put(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA, b.getNombrePartida());
			hash.put(ScsPartidaPresupuestariaBean.C_DESCRIPCION, b.getDescripcion());
			if (b.getIdInstitucion() != null) hash.put(ScsPartidaPresupuestariaBean.C_IDINSTITUCION,b.getIdInstitucion().toString());
			hash.put(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(ScsPartidaPresupuestariaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return hash;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */	
	protected String[] getOrdenCampos() {
		return null;
	}
	
}