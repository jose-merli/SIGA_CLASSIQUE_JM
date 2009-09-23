/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_ZONA
 * 
 * Autor: ruben.fernandez - 01/11/2004
 * Modificaicones: julio.vicente - 14/01/2004 (Anhadidos metodos select) 
 * s
 * */


package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;


public class ScsZonaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsZonaAdm (UsrBean usuario) {
		super( ScsZonaBean.T_NOMBRETABLA,usuario);
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_ZONA con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT 
	 */
	public Vector select(Hashtable miHash) throws ClsExceptions {

		Vector datos = new Vector();		
		
		try {
			// En primer lugar hacemosl a join con las tablas que necesitamos
			String where =" WHERE subz." + ScsSubzonaBean.C_IDZONA + "(+)= zona." + ScsZonaBean.C_IDZONA + " and subz." + ScsSubzonaBean.C_IDINSTITUCION + " (+)= zona." + ScsZonaBean.C_IDINSTITUCION + " and parti." + CenPartidoJudicialBean.C_IDPARTIDO + " (+) = subz." + ScsSubzonaBean.C_IDPARTIDO + " and zona." + ScsZonaBean.C_IDINSTITUCION + " = " + miHash.get("INSTITUCIONZONA");
			// Si se introdució el nombre de zona como criterio
			if (((String)miHash.get("NOMBREZONA") != null) &&
			(((String)miHash.get("NOMBREZONA")).length() > 0)) 
			{
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get("NOMBREZONA")).trim(),"zona." + ScsZonaBean.C_NOMBRE );
			}
			// Si se introdució el nombre de subzona como criterio
			if (((String)miHash.get("NOMBRESUBZONA") != null) &&
			(((String)miHash.get("NOMBRESUBZONA")).length() > 0))
			{
				 
				where += "AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get("NOMBRESUBZONA")).trim(),"subz." + ScsSubzonaBean.C_NOMBRE );
			}
			// Si se introdució el nombre de partido judicial como criterio
			if (((String)miHash.get(CenPartidoJudicialBean.C_NOMBRE) != null) &&
			(((String)miHash.get(CenPartidoJudicialBean.C_NOMBRE)).length() > 0)) 
			{
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)miHash.get(CenPartidoJudicialBean.C_NOMBRE)).trim(),"parti." + CenPartidoJudicialBean.C_NOMBRE  );
			}
			
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
			String sql = "SELECT DISTINCT zona." + ScsZonaBean.C_IDZONA + ", zona." + ScsZonaBean.C_NOMBRE + ", zona." + ScsZonaBean.C_IDINSTITUCION  + " FROM " + ScsZonaBean.T_NOMBRETABLA + " zona";
			if ((where.indexOf("subz") != -1) || (where.indexOf("parti")) != -1) {
				sql += ", " + ScsSubzonaBean.T_NOMBRETABLA + " subz";				
			} 
			if (where.indexOf("parti") != -1) {
				sql += ", " + CenPartidoJudicialBean.T_NOMBRETABLA + " parti";
			}			
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
			String sql = "SELECT DISTINCT zona." + ScsZonaBean.C_IDZONA + ", zona." + ScsZonaBean.C_NOMBRE + ", zona." + ScsZonaBean.C_IDINSTITUCION  + " FROM " + ScsZonaBean.T_NOMBRETABLA + " zona";
			if ((where.indexOf("subz") != -1) || (where.indexOf("parti")) != -1) {
				sql += ", " + ScsSubzonaBean.T_NOMBRETABLA + " subz";				
			} 
			if (where.indexOf("parti") != -1) {
				sql += ", " + CenPartidoJudicialBean.T_NOMBRETABLA + " parti";
			}			
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
	/** Busqueda de zonas por nombre zona, subzona o partido judicial que contienen.
	 *  @param criteros para filtrar el select 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector buscarZonas(Hashtable criterios) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			String idInstitucionZona=(String)criterios.get("idInstitucionZona");
			String zona=(String)criterios.get("NOMBREZONA");
			String subZona=(String)criterios.get("NOMBRESUBZONA");
			String partidoJudicial=(String)criterios.get("NOMBRE");
			
			rc = new RowsContainer(); 
			String sql=	"select z.* from scs_zona z where z.idinstitucion ="+idInstitucionZona;
			
			if(zona!=null && !zona.trim().equals("")){
				 
				sql+=" and "+ComodinBusquedas.prepararSentenciaCompleta(zona.trim(),"z.nombre");
			}
			
			if(subZona!=null && !subZona.trim().equals("")){
				 sql+=
					 " and z.idzona in " +
					 "( select sz.idzona" +
					 "    from scs_subzona sz"+
					 "   where sz.idinstitucion = z.idinstitucion"+
				
					 "     and "+ComodinBusquedas.prepararSentenciaCompleta(subZona.trim(),"sz.nombre")+
					 " )";
			}
			
			if(partidoJudicial!=null && !partidoJudicial.trim().equals("")){
				 sql+=
					 " and z.idzona in " +
					 "( select szp.idzona" +
					 "    from scs_subzonapartido szp, cen_partidojudicial pj"+
					 "   where szp.idinstitucion = z.idinstitucion" +
					 "     and szp.idpartido = pj.idpartido"+
				
					 "     and "+ComodinBusquedas.prepararSentenciaCompleta(partidoJudicial.trim(),"pj.nombre")+
					 " )";
			}
			sql += " order by z.nombre";
			
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

	/**
	 * Efectúa un SELECT en la tabla SCS_ZONA con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsZonaBean.C_IDZONA + " = " + hash.get(ScsZonaBean.C_IDZONA) + " AND " + ScsZonaBean.C_IDINSTITUCION + " = " + hash.get(ScsZonaBean.C_IDINSTITUCION);		
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
			String sql = "SELECT * FROM " + ScsZonaBean.T_NOMBRETABLA + " zona " + where;			
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
		RowsContainer rc = null;		
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsZonaBean.C_IDZONA + ") + 1) AS IDZONA FROM " + nombreTabla + " WHERE " + ScsZonaBean.C_IDINSTITUCION + " = " + entrada.get("INSTITUCIONZONA");			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDZONA").equals("")) {
					entrada.put(ScsZonaBean.C_IDZONA,"1");
				}
				else entrada.put(ScsZonaBean.C_IDZONA,(String)prueba.get("IDZONA"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDZONA");
		};		
		entrada.put(ScsZonaBean.C_NOMBRE,(String)entrada.get("NOMBREZONA"));
		entrada.put(ScsZonaBean.C_IDINSTITUCION,(String)entrada.get("INSTITUCIONZONA"));
		
		return entrada;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsZonaBean.C_IDZONA,			ScsZonaBean.C_NOMBRE,		
							ScsZonaBean.C_MUNICIPIOS,		ScsZonaBean.C_IDINSTITUCION,
							ScsZonaBean.C_USUMODIFICACION,	ScsZonaBean.C_FECHAMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsZonaBean.C_IDINSTITUCION, ScsZonaBean.C_IDZONA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsZonaBean bean = null;
		try{
			bean = new ScsZonaBean();
			bean.setIdZona(Integer.valueOf((String)hash.get(ScsZonaBean.C_IDZONA)));
			bean.setNombre((String)hash.get(ScsZonaBean.C_NOMBRE));
			bean.setMunicipios((String)hash.get(ScsZonaBean.C_MUNICIPIOS));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsZonaBean.C_IDINSTITUCION)));
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
			ScsZonaBean b = (ScsZonaBean) bean;
			hash.put(ScsZonaBean.C_IDZONA, String.valueOf(b.getIdZona()));
			hash.put(ScsZonaBean.C_NOMBRE, b.getNombre());
			hash.put(ScsZonaBean.C_MUNICIPIOS, b.getMunicipios());
			hash.put(ScsZonaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
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
		String[] vector = {"UPPER (zona." + ScsZonaBean.C_NOMBRE + ")"};
		return vector;
	}	
	
}