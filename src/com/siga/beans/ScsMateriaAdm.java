/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_MATERIA
 * 
 * Autor: ruben.fernandez 1/11/2004
 * Ultima modificación: 29/12/2004 - Anhadidos métodos insertar, modificar, borrar (julio.vicente)
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;



public class ScsMateriaAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsMateriaAdm (UsrBean usuario) {
		super( ScsMateriaBean.T_NOMBRETABLA, usuario);
	}
	
	
	/** Funcion selectByPK (Hashtable hash)
	 * @param hashTable con las parejas campo-valor para realizar un where en el select por todas las claves primarias
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectByPK(Hashtable miHash) throws ClsExceptions
	{
		Vector datos = new Vector();		
		
		try {			
			String where = "where ( mate." + ScsMateriaBean.C_IDINSTITUCION + " = " + miHash.get(ScsMateriaBean.C_IDINSTITUCION) + " AND mate." + ScsMateriaBean.C_IDAREA + " = " + miHash.get(ScsMateriaBean.C_IDAREA) + " AND mate." + ScsMateriaBean.C_IDMATERIA + " = " + miHash.get(ScsMateriaBean.C_IDMATERIA) + ")";
			datos = this.select(where);
		}		
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR");
		}	
		return datos;	
	}	
	
	/**
	 * Efectúa un SELECT en la tabla SCS_MATERIA con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT 
	 */
	public Vector select(Hashtable miHash) throws ClsExceptions {

		Vector datos = new Vector();		
		
		try {			
			String where = "where ( mate." + ScsMateriaBean.C_IDINSTITUCION + " = " + miHash.get(ScsMateriaBean.C_IDINSTITUCION) + " AND mate." + ScsMateriaBean.C_IDAREA + " = " + miHash.get(ScsMateriaBean.C_IDAREA) + ")";
			datos = this.select(where);
		}		
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR");
		}	
		return datos;	
	}
	
	/** Funcion select (String where). Devuelve los campos: IDAREA, IDINSTITUCION, NOMBRE y CONTENIDO
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
			String sql = "select " + ScsMateriaBean.C_IDAREA  + ", " + ScsMateriaBean.C_IDINSTITUCION + ", " + ScsMateriaBean.C_IDMATERIA + ", " + ScsMateriaBean.C_NOMBRE + ", " + ScsMateriaBean.C_CONTENIDO + " FROM " + ScsMateriaBean.T_NOMBRETABLA + " mate";			
			sql += " " + where;
			sql += this.getOrdenPorNombre()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenPorNombre()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

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
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "select " + ScsMateriaBean.C_IDAREA  + ", " + ScsMateriaBean.C_IDINSTITUCION + ", " + ScsMateriaBean.C_IDMATERIA + ", " + ScsMateriaBean.C_NOMBRE + ", " + ScsMateriaBean.C_CONTENIDO + " FROM " + ScsMateriaBean.T_NOMBRETABLA + " mate";			
			sql += " " + where;
			sql += this.getOrdenPorNombre()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenPorNombre()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
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
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la zona que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsMateriaBean.C_IDMATERIA + ") + 1) AS IDMATERIA FROM " + nombreTabla;			 
			sql += " WHERE " + ScsMateriaBean.C_IDINSTITUCION + " = " + entrada.get(ScsMateriaBean.C_IDINSTITUCION) + " AND " + ScsMateriaBean.C_IDAREA + " = " + entrada.get(ScsMateriaBean.C_IDAREA);
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable miHash = fila.getRow();			
				if (miHash.get("IDMATERIA").equals("")) {
					entrada.put(ScsMateriaBean.C_IDMATERIA,"1");
				}
				else entrada.put(ScsMateriaBean.C_IDMATERIA,(String)miHash.get("IDMATERIA"));				
			}
			entrada.put("NOMBRE",(String)entrada.get("NOMBREMATERIA"));
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDMATERIA");
		};
		
		return entrada;
	}	
	

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsMateriaBean.C_IDAREA,		ScsMateriaBean.C_IDMATERIA,
							ScsMateriaBean.C_NOMBRE,		ScsMateriaBean.C_CONTENIDO,
							ScsMateriaBean.C_IDINSTITUCION,	ScsMateriaBean.C_FECHAMODIFICACION,
							ScsMateriaBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsMateriaBean.C_IDINSTITUCION,	ScsMateriaBean.C_IDAREA, ScsMateriaBean.C_IDMATERIA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsMateriaBean bean = null;
		try{
			bean = new ScsMateriaBean();
			bean.setIdArea(Integer.valueOf((String)hash.get(ScsMateriaBean.C_IDAREA)));
			bean.setIdMateria(Integer.valueOf((String)hash.get(ScsMateriaBean.C_IDMATERIA)));
			bean.setNombre((String)hash.get(ScsMateriaBean.C_NOMBRE));
			bean.setContenido((String)hash.get(ScsMateriaBean.C_CONTENIDO));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsMateriaBean.C_IDINSTITUCION)));
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsMateriaBean b = (ScsMateriaBean) bean;
			hash.put(ScsMateriaBean.C_IDAREA, String.valueOf(b.getIdArea()));
			hash.put(ScsMateriaBean.C_IDMATERIA, String.valueOf(b.getIdMateria()));
			hash.put(ScsMateriaBean.C_NOMBRE, b.getNombre());
			hash.put(ScsMateriaBean.C_CONTENIDO,b.getContenido());
			hash.put(ScsMateriaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
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
	protected String[] getOrdenCampos() {
		return null;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {
		String[] vector = {"UPPER (mate." + ScsMateriaBean.C_NOMBRE + ")"};
		return vector;
	}
	
}