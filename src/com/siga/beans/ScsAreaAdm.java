
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;

//Clase: ScsAreaAdm
//Autor: ruben.fernandez
//Ultima modificación: 28/12/2004 - Anhadidos métodos insertar, modificar, borrar (julio.vicente)

public class ScsAreaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsAreaAdm (UsrBean usuario) {
		super( ScsAreaBean.T_NOMBRETABLA, usuario);
	}
  
	/**
	 * Efectúa un SELECT en la tabla SCS_AREA con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT 
	 */
	public Vector select(Hashtable miHash) throws ClsExceptions 
	{
	    return this.select(miHash, false);
	}
	
	public Vector select(Hashtable miHash, boolean bConMaterias) throws ClsExceptions 
	{

		Vector datos = new Vector();		
		
		try {
			String where ="where (";
			// En función de los criterios de búsqueda se construye el where
			if (((String)miHash.get("NOMBREMATERIA") != null) &&
				((String)miHash.get("NOMBREMATERIA")).length() > 0)
			{				
				where += "mate." + ScsMateriaBean.C_IDINSTITUCION + " = area." + ScsAreaBean.C_IDINSTITUCION + " AND " + 
						 "mate." + ScsMateriaBean.C_IDAREA + " = area." + ScsAreaBean.C_IDAREA + " AND " +
						 ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get("NOMBREMATERIA")).trim(),"mate." + ScsMateriaBean.C_NOMBRE );
 			}
			if (((String)miHash.get("NOMBREAREA") != null) && 
					((String)miHash.get("NOMBREAREA")).length() > 0)
			{
				/* Si el campo de búsqueda del nombre de la materia se rellenó, antes de rellenar el select con el nombre de área
				 * habrá que anhadir un AND delante
				 */ 
				if (((String)miHash.get("NOMBREMATERIA") != null) &&
						((String)miHash.get("NOMBREMATERIA")).length() > 0)
				{
					where += " AND ";
				}
				
				where +=ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get("NOMBREAREA")).trim(),"area." + ScsAreaBean.C_NOMBRE );
			}
			if ((((String)miHash.get("NOMBREMATERIA") != null) &&
			    ((String)miHash.get("NOMBREMATERIA")).length() > 0) ||
				(((String)miHash.get("NOMBREAREA") != null) && 
				((String)miHash.get("NOMBREAREA")).length() > 0)) {
					where += " AND ";
			}
			where += "area." + ScsAreaBean.C_IDINSTITUCION + " = " + miHash.get(ScsAreaBean.C_IDINSTITUCION) + ")";					
			datos = this.select(where, bConMaterias);
		} 
		catch (ClsExceptions e) {
			throw e;			
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
	    return this.select (where, false);
	}
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		boolean bConMaterias = false;
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT DISTINCT area." + ScsAreaBean.C_IDAREA  + ", area." + ScsAreaBean.C_IDINSTITUCION + ", area." + ScsAreaBean.C_NOMBRE + ", area." + ScsAreaBean.C_CONTENIDO;
			if (bConMaterias) {
			    sql += ", F_SIGA_NOMBRE_MATERIAS(area." + ScsAreaBean.C_IDAREA  + ", area." + ScsAreaBean.C_IDINSTITUCION + ") as MATERIAS ";
			}
			sql += " FROM " + ScsAreaBean.T_NOMBRETABLA + " area";
			
			if (where.indexOf("mate") != -1) {
				sql += ", " + ScsMateriaBean.T_NOMBRETABLA + " mate";
			}
			sql += " " + where;
			sql += this.getOrdenPorNombre()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenPorNombre()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryNLSBind(sql,data)) {
			    if (bConMaterias) {
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						Hashtable h = (Hashtable)fila.getRow();
						if (h != null)
						    datos.add(h);
					}
			    }
			    else {
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
						if (registro != null) 
							datos.add(registro);
					}
			    }
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	public Vector select(String where, boolean bConMaterias) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT DISTINCT area." + ScsAreaBean.C_IDAREA  + ", area." + ScsAreaBean.C_IDINSTITUCION + ", area." + ScsAreaBean.C_NOMBRE + ", area." + ScsAreaBean.C_CONTENIDO;
			
			if (bConMaterias) {
			    sql += ", F_SIGA_NOMBRE_MATERIAS(area." + ScsAreaBean.C_IDAREA  + ", area." + ScsAreaBean.C_IDINSTITUCION + ") as MATERIAS ";
			}
			sql += " FROM " + ScsAreaBean.T_NOMBRETABLA + " area";
			
			if (where.indexOf("mate") != -1) {
				sql += ", " + ScsMateriaBean.T_NOMBRETABLA + " mate";
			}
			sql += " " + where;
			sql += this.getOrdenPorNombre()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenPorNombre()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.queryNLS(sql)) {
			    
			    if (bConMaterias) {
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						Hashtable h = (Hashtable)fila.getRow();
						if (h != null)
						    datos.add(h);
					}
			    }
			    else {
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
						if (registro != null) 
							datos.add(registro);
					}
			    }
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
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
			String sql = "SELECT * FROM " + ScsAreaBean.T_NOMBRETABLA + " area " + where;			
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
	
	/**
	 * Efectúa un SELECT en la tabla SCS_AREA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsAreaBean.C_IDAREA + " = " + hash.get(ScsAreaBean.C_IDAREA) + " AND " + ScsAreaBean.C_IDINSTITUCION + " = " + hash.get(ScsAreaBean.C_IDINSTITUCION);		
			datos = this.selectAll(where);
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
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del área que se va a insertar.
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
			String sql ="SELECT (MAX("+ ScsAreaBean.C_IDAREA + ") + 1) AS IDAREA FROM " + nombreTabla;			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDAREA").equals("")) {
					entrada.put(ScsAreaBean.C_IDAREA,"1");
				}
				else entrada.put(ScsAreaBean.C_IDAREA,(String)prueba.get("IDAREA"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDAREA");
		};		
		entrada.put("NOMBRE",(String)entrada.get("NOMBREAREA"));
		
		return entrada;
	}	
	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsAreaBean.C_IDAREA, 			ScsAreaBean.C_NOMBRE,	
							ScsAreaBean.C_CONTENIDO, 		ScsAreaBean.C_IDINSTITUCION,
							ScsAreaBean.C_USUMODIFICACION,	ScsAreaBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsAreaBean.C_IDINSTITUCION, ScsAreaBean.C_IDAREA};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsAreaBean bean = null;
		try{
			bean = new ScsAreaBean();
			bean.setIdArea(UtilidadesHash.getInteger(hash,ScsAreaBean.C_IDAREA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsAreaBean.C_IDINSTITUCION));
			bean.setNombre(UtilidadesHash.getString(hash,ScsAreaBean.C_NOMBRE));
			bean.setContenido(UtilidadesHash.getString(hash,ScsAreaBean.C_CONTENIDO));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsAreaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsAreaBean.C_USUMODIFICACION));
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
			ScsAreaBean b = (ScsAreaBean) bean;
			hash.put(ScsAreaBean.C_CONTENIDO,b.getContenido());
			hash.put(ScsAreaBean.C_IDAREA, String.valueOf(b.getIdArea()));
			hash.put(ScsAreaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsAreaBean.C_NOMBRE, b.getNombre());
			hash.put(ScsAreaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsAreaBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] vector = {ScsAreaBean.C_IDAREA,ScsAreaBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER (area." + ScsAreaBean.C_NOMBRE + ")"};
		return vector;
	}
		
}