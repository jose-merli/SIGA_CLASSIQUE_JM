/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_SUBZONA
 * 
 * Autor: ruben.fernandez - 01/11/2004
 * Modificaicones: julio.vicente - 14/01/2004 (Anhadidos metodos select) 
 * 
 * */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;

public class ScsSubzonaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsSubzonaAdm (UsrBean usuario) {
		super( ScsSubzonaBean.T_NOMBRETABLA, usuario);
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_SUBZONA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsSubzonaBean.C_IDZONA + " = " + hash.get(ScsSubzonaBean.C_IDZONA) + " AND " + ScsSubzonaBean.C_IDINSTITUCION + " = " + hash.get(ScsZonaBean.C_IDINSTITUCION);
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
			String sql = "SELECT * FROM " + ScsSubzonaBean.T_NOMBRETABLA + where;			
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
			String sql ="SELECT (MAX("+ ScsSubzonaBean.C_IDSUBZONA + ") + 1) AS IDSUBZONA FROM " + nombreTabla + " WHERE " + ScsSubzonaBean.C_IDINSTITUCION + " = " + entrada.get(ScsSubzonaBean.C_IDINSTITUCION);			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDSUBZONA").equals("")) {
					entrada.put(ScsSubzonaBean.C_IDSUBZONA,"1");
				}
				else entrada.put(ScsSubzonaBean.C_IDSUBZONA,(String)prueba.get("IDSUBZONA"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDSUBZONA");
		};		
		entrada.put(ScsSubzonaBean.C_NOMBRE,(String)entrada.get("NOMBRESUBZONA"));
		entrada.put(ScsSubzonaBean.C_IDINSTITUCION,(String)entrada.get("INSTITUCIONSUBZONA"));
		
		// Si el IDPARTIDO es 0 quiere decir que no se ha seleccionado nada en el combo de partidos 
		// Por ello en la hash no metemos nada:
		if (entrada.get(ScsSubzonaBean.C_IDPARTIDO)==null || ((String)entrada.get(ScsSubzonaBean.C_IDPARTIDO)).equals("0")) {
			entrada.remove(ScsSubzonaBean.C_IDPARTIDO);
		}
		
		return entrada;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsSubzonaBean.C_IDSUBZONA,		 ScsSubzonaBean.C_IDZONA,
							ScsSubzonaBean.C_NOMBRE,		 ScsSubzonaBean.C_MUNICIPIOS,
							ScsSubzonaBean.C_IDINSTITUCION,	 ScsSubzonaBean.C_IDPARTIDO,
							ScsSubzonaBean.C_USUMODIFICACION,ScsSubzonaBean.C_FECHAMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsSubzonaBean.C_IDINSTITUCION, ScsSubzonaBean.C_IDZONA, ScsSubzonaBean.C_IDSUBZONA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsSubzonaBean bean = null;
		try{
			bean = new ScsSubzonaBean();
			bean.setIdZona(Integer.valueOf((String)hash.get(ScsSubzonaBean.C_IDZONA)));
			bean.setIdSubzona(Integer.valueOf((String)hash.get(ScsSubzonaBean.C_IDSUBZONA)));
			bean.setNombre((String)hash.get(ScsSubzonaBean.C_NOMBRE));
			bean.setMunicipios((String)hash.get(ScsSubzonaBean.C_MUNICIPIOS));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsSubzonaBean.C_IDINSTITUCION)));
			if (!((String)hash.get(ScsSubzonaBean.C_IDPARTIDO)).equalsIgnoreCase("")){
				bean.setIdPartido(Integer.valueOf((String)hash.get(ScsSubzonaBean.C_IDPARTIDO)));
			}
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
			ScsSubzonaBean b = (ScsSubzonaBean) bean;
			hash.put(ScsSubzonaBean.C_IDZONA, String.valueOf(b.getIdZona()));
			hash.put(ScsSubzonaBean.C_IDSUBZONA, String.valueOf(b.getIdSubzona()));
			hash.put(ScsSubzonaBean.C_NOMBRE, b.getNombre());
			hash.put(ScsSubzonaBean.C_MUNICIPIOS, b.getMunicipios());
			hash.put(ScsSubzonaBean.C_CONTENIDO, String.valueOf(b.getContenido()));
			hash.put(ScsSubzonaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsSubzonaBean.C_IDPARTIDO, String.valueOf(b.getIdPartido()));
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
		String[] vector = {"UPPER (" + ScsSubzonaBean.C_NOMBRE + ")"};
		return vector;
	}
	
	public Vector getPartidosJudiciales(Hashtable datos) throws ClsExceptions 
	{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			String sql = "select IDPARTIDO from scs_subzonapartido " +
			" where idzona=" + (String)datos.get("IDZONA") +
			" and idsubzona=" + (String)datos.get("IDSUBZONA")  +
			" and idInstitucion=" + (String)datos.get("IDINSTITUCION") ;
			
			// RGG cambio para visibilidad
            rc = this.find(sql);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
//		catch (SIGAException e) { 
//			throw e; 	
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getPartidosJudiciales");
		}
		return v;
	}
	
		
}