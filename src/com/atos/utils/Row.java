package com.atos.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;



/**
 * <p>Clase a bajo nivel que gestiona <b>todas</b> las operaciones
 * realizadas sobre la Base de Datos. Incluye insercción, modificación, borrado y consulta.</p>
 * @author Luis Miguel Sánchez PIÑA
 * @version 1.8
 */

public class Row implements Serializable 
{
	static protected String schemasAvailables[]=null;
	
	/**
	 *  Contiene parejas de clave/valor.
	 */
	protected Hashtable row = null;
	
	/**
	 * Nombre de la tabla.
	 */
	private String table;
	
	/**
	 * Contiene los tipos de datos de la tabla.
	 */
	private Hashtable datatypes;
	
	/**
	 * Usado para comparar.
	 */
	private Hashtable htorigin;
	
	/**
	 * Devuelve los registros que han cambiado.
	 */
	private Vector vectEnd;
	
	/**
	 * Objeto HttpServletRequest que representa la petición.
	 */
	private HttpServletRequest request;
	
	static 
	{
		if (schemasAvailables==null) 
		{
			try 
			{
				Connection con = null;
				boolean found = false;
				Statement st = null;
				ResultSet rs = null;
				String sqlStatement = "select VALOR from "+TableConstants.TABLE_PARAMETERS+" where "+
				" MODULO='GEN' and PARAMETRO='BBDD_SCHEMAS'";
				try 
				{
					con = ClsMngBBDD.getReadConnection();
					st = con.createStatement();
					rs = st.executeQuery(sqlStatement);
					
					if (rs.next())
					{
						String schemas = rs.getString("VALOR");
						StringTokenizer tok=new StringTokenizer(schemas,",");
						schemasAvailables=new String[tok.countTokens()];
						int i=0;
						
						while (tok.hasMoreTokens()) 
						{
							String sql="select VALOR from " + TableConstants.TABLE_PARAMETERS + " where " +
							" MODULO='GEN' and PARAMETRO='" + tok.nextToken().trim() + "'";
							Statement st2=null;
							ResultSet rs2=null;
							
							try 
							{
								st2=con.createStatement();
								rs2=st2.executeQuery(sql);
								
								if (rs2.next())
								{
									schemasAvailables[i++]=rs2.getString("VALOR");
								}
							} 
							
							catch (Exception ev) 
							{
								ev.printStackTrace();
							} 
							
							finally 
							{
								if (rs2!=null) rs2.close();
								if (st2!=null) st2.close();
							}
						}
					}
				} 
				
				catch (SQLException sqe) 
				{
					sqe.printStackTrace();
				} 
				
				finally 
				{
					try 
					{
						if (rs != null)
						{
							rs.close();
						}
						
						if (st != null)
						{
							st.close();
						}
						
						if (con != null)
						{
							ClsMngBBDD.closeConnection(con);
						}
					} 
					
					catch (SQLException exc) 
					{
						exc.printStackTrace();
					}
				}
				
				String toTrace="";
				
				for (int v=0; v<schemasAvailables.length; v++)
				{
					toTrace+=(schemasAvailables[v]+" ");
				}
				
				ClsLogging.writeFileLogWithoutSession("   > Esquema BD: " + toTrace, 7);
			}
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  Constructor
	 */
	public Row() { }
	
	public static Row create(Hashtable reg) 
	{
		Row newRow = new Row();
		newRow.load(reg);
		
		return newRow;
	}
	
	public static Row create(ResultSet rs) throws ClsExceptions 
	{
		Row newRow = new Row();
		newRow.load(rs);
		
		return newRow;
	}
	
	/**
	 * Ejecuta una sentencia SELECT en la Base de Datos.
	 * Carga en una Hashtable el resultado de la consulta (cuando se encuentra más
	 * de un registro, devuelve <b>únicamente</b> el primero encontrado.
	 * La conexión se obtiene del Pool de 'Sólo Lectura'.
	 * @param con Objeto Connection.
	 * @param sqlStatement Sentencia SELECT a ejecutar.
	 * @return true Si se encuentra algún registro. En caso contrario, se lanza una excepción.
	 * @throws ClsExceptions
	 */
	private boolean find (Connection con, String sqlStatement) throws ClsExceptions 
	{
		boolean found = false;
		Statement st = null;
		ResultSet rs = null;
		boolean excThrow=false;
		
		try 
		{
			st = con.createStatement();
			
			try 
			{
				rs = st.executeQuery(sqlStatement);
			} 
			
			catch (SQLException ex) 
			{
				rs = st.executeQuery(validateWhereClause(sqlStatement));
			}
			
			if (rs.next())
			{
				this.load(rs);
				found = true;
			}
			
			return found;
		} 
		catch (SQLException ex)  {
			ClsExceptions psscExrow = new ClsExceptions(ex,ex.toString() + " SQL: "+sqlStatement, "", "0", "SQL0", "1");
			excThrow=true;
			throw psscExrow;
		}	finally	{
			try {
				if (rs != null)	{
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException exc) {
				if (!excThrow)
					throw new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
			}
		}
	}
	
	/**
	 * Ejecuta una sentencia SELECT en la Base de Datos.
	 * Carga en una Hashtable el resultado de la consulta (cuando se encuentra más
	 * de un registro, devuelve <b>únicamente</b> el primero encontrado.
	 * La conexión se obtiene del Pool de 'Sólo Lectura'.
	 * @param sqlStatement Sentencia SELECT a ejecutar.
	 * @return true Si se encuentra algún registro. En caso contrario, se lanza una excepción.
	 * @throws ClsExceptions
	 */
	public boolean find (String sqlStatement) throws ClsExceptions 
	{
		Connection con = null;
		
		try	{
			con = ClsMngBBDD.getReadConnection();
			return find(con, sqlStatement);
		} catch (ClsExceptions ex)	{
			throw ex;
		} finally {
			if (con != null) {
				ClsMngBBDD.closeConnection(con);
			}
		}
	}
	
	
	/**
	 * Ejecuta una sentencia SELECT en la Base de Datos.
	 * Carga en una Hashtable el resultado de la consulta (cuando se encuentra más
	 * de un registro, devuelve <b>únicamente</b> el primero encontrado.
	 * La conexión se obtiene del Pool de 'Lectura-Escritura'.
	 * @param sqlStatement Sentencia SELECT a ejecutar.
	 * @return true Si se encuentra algún registro. En caso contrario, se lanza una excepción.
	 * @throws ClsExceptions
	 */
	public boolean findForUpdate (String sqlStatement) throws ClsExceptions 
	{
		Connection con = null;
		try	{
			con = ClsMngBBDD.getConnection();
			return find(con,sqlStatement);
		} catch (ClsExceptions ex)	{
			throw ex;
		} finally	{
			if (con != null) {
				ClsMngBBDD.closeConnection(con);
			}
		}
	}
	
	/**
	 * Carga una Hashtable en la propia Hashtable de Row.
	 * @param reg Hashtable a cargar.
	 */
	public void load(Hashtable reg) 
	{
		this.row = reg;
	}
	
	/**
	 * Carga un Resultset en la Hashtable de Row.
	 * @param rs Resultset a cargar.
	 * @throws ClsExceptions
	 */
	public void load(ResultSet rs) throws ClsExceptions 
	{
		this.row = new Hashtable();
		ArrayList columns = this.getColumnNames(rs);
		int puntero = 0;
		if (columns!=null){
		for(;puntero < columns.size(); puntero++) 
		{
			String key = (String) columns.get(puntero);
			try 
			{
				String val = "";
				if (rs.getObject(key) != null)	{
					if (rs.getObject(key) instanceof java.sql.Date ||
						rs.getObject(key) instanceof java.sql.Timestamp ||
						rs.getObject(key) instanceof java.sql.Time) 
					{
						val = formatDate(rs.getTimestamp(key));
					} else	{
						val = rs.getObject(key).toString();
					}
				}
				this.row.put(key, val);
			}	catch (SQLException ex)	{
				continue;
			}
		}
		}
	}	
	
	/**
	 * Devuelve un ArrayList con los nombres de las columnas del Resultset.
	 * @param rs Resultset para obtener los nombre de sus columnas.
	 */
	public ArrayList getColumnNames(ResultSet rs) 
	{
		int counter = 1;
		ArrayList list = new ArrayList();
		try		{
			ResultSetMetaData meta = rs.getMetaData();
			for (;counter <= meta.getColumnCount(); counter++)	{
				list.add(counter -1, meta.getColumnName(counter));
			}
			return list;
		} catch (SQLException ex)	{
			return null;
			
		}
	}
	
	/**
	 * Devuelve una cadena con el valor de la clave especificada como parámetro.
	 * @param key Clave del elemento a buscar.
	 * @return Cadena con el valor de la clave especificada como parámetro.
	 * En caso de que la Hashtable de Row no exista, devuelve null.
	 */
	public String getString(Object key) 
	{
		if (this.row == null)
		{
			return null;
		}
		
		else
		{ 
			// RGG prueba para ver el String que obtiene
			//ClsLogging.writeFileLog("PRUEBA >>> TRAZA ROW.GETSTRING() OBTIENE: '"+this.row.get(key)+"' Y TRAS EL STRING.VALUEOF: '"+String.valueOf(this.row.get(key))+"'",10);
			return String.valueOf(this.row.get(key));
		}
	}
	
	/**
	 * Devuelve el objeto con el valor de la clave especificada como parámetro.
	 * @param key Clave del elemento a buscar.
	 * @return Objeto con el valor de la clave especificada como parámetro.
	 * En caso de que la Hashtable de Row no exista, devuelve null.
	 */
	public Object getValue(Object key) 
	{
		if (this.row == null)
		{
			return null;
		}
		
		else
		{
			return this.row.get(key);
		}
	}
	
	/**
	 * Establece un objeto en la Hashtable de Row.
	 * @param key Clave del elemento a almacenar.
	 * @param value Objeto a almacenar.
	 */
	public void setValue(String key, Object value) 
	{
		if (this.row == null)
		{
			this.row = new Hashtable();
		}
		
		this.row.put(key, value);
	}
	
	/**
	 * Devuelve la Hashtable de Row.
	 * @return Objeto Hashtable de Row.
	 */
	public Hashtable getRow() 
	{
		return this.row;
	}
	
	/**
	 * Estable la Hashtable de Row.
	 * @return Objeto Hashtable de Row.
	 */
	public void setRow(Hashtable reg) 
	{
		this.row = reg;
	}
	
	/**
	 * Ejecuta una sentencia DELETE en la Base de Datos.
	 * Borrará registro de la tabla especificada en el parámetro tableName, usando como filtro
	 * los datos contenidos en la Hashtable de Row que correspondan con las claves especificadas
	 * en el parámetro keyfields.
	 * Si el tipo de datos de un campo sea DATE, el formato será aplicado.
	 * Si se establece a null, el campo fecha no será utilizado en la cláusula WHERE.
	 * En cualquier caso, cuando el campo fecha se establece al valor "SYSDATE", ese será el valor
	 * utilizado en la sentencia.
	 * @param tableName Nombre de la tabla sobre la que borrar registros.
	 * @param keyfields Lista con los nombres de los campos a usar en la cláusula WHERE. 
	 * @return Número de registros afectados (Borrados).
	 * @throws ClsExceptions
	 */
	public int delete(String tableName, Object[] keyfields) throws ClsExceptions 
	{
	    return deleteBind(tableName, keyfields);
	    
	    /*
		boolean thowsExc=false;
		Connection con = null;
		int deletedRecords = 0;
		Statement st = null;
		String sql = "";
		try 
		{
			con = ClsMngBBDD.getConnection();
			// No es necesario comprobar la Integridad Referencial, puesto que si ocurre algún error
			// al borrar el registro, ya el SGBDR lanzará el error.
			//checkDDBBIntegrity(con, tableName, keyfields, false, schemasAvailables[0]);
			sql = " DELETE " + tableName + buildSQLWhere(con, tableName, keyfields);
			st = con.createStatement();
            ClsLogging.writeFileLog("SQL DELETE: "+sql,10);
			deletedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
            ClsLogging.writeFileLogError("Error en Insert SQL: "+sql,ex,3);
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("12");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("12");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}
		
		return deletedRecords;
		*/
	}
	
	
	public int deleteBind(String tableName, Object[] keyfields) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int deletedRecords = 0;
		PreparedStatement st = null;
		String sql = "";
		Hashtable cods = new Hashtable();
		try 
		{
			con = ClsMngBBDD.getConnection();
			// No es necesario comprobar la Integridad Referencial, puesto que si ocurre algún error
			// al borrar el registro, ya el SGBDR lanzará el error.
			//checkDDBBIntegrity(con, tableName, keyfields, false, schemasAvailables[0]);
			//sql = " DELETE " + tableName + buildSQLWhere(con, tableName, keyfields);
			
			Vector v = buildSQLWhereBind(con, tableName, keyfields, new Hashtable(),1);
			sql = " DELETE " + tableName + (String) v.get(0);
			cods = (Hashtable) v.get(1);
			//st = con.createStatement();
			ClsLogging.writeFileLog("SQL BIND DELETE: "+ClsMngBBDD.getSQLBindInformation(sql,cods),10);
			st = con.prepareStatement(sql);
			
			Enumeration e = cods.keys();
	   	     while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         st.setString(key.intValue(), (String)cods.get(key));
	   	     }
           try {
           		deletedRecords = st.executeUpdate();
           } 
           catch (SQLException exce) {
	           	ClsLogging.writeFileLog("Error DELETE BIND: "+ exce.getMessage() + " SQL:"+ClsMngBBDD.getSQLBindInformation(sql,cods),3);
	           	throw exce;
           }
			
			//deletedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("12");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("12");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}
		
		return deletedRecords;
	}
	
	public int deleteSQL(String sql) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int deletedRecords = 0;
		Statement st = null;
		try 
		{
			con = ClsMngBBDD.getConnection();
			st = con.createStatement();
            ClsLogging.writeFileLog("SQL DELETE SQL: "+sql,10);
			deletedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
            ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1) + "  SQL: "+sql);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					throw psscEx;
				}
			}
		}
		
		return deletedRecords;
	}
	public int updateSQL(String sql) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int updatedRecords = 0;
		Statement st = null;
		try 
		{
			con = ClsMngBBDD.getConnection();
			st = con.createStatement();
            ClsLogging.writeFileLog("SQL UPDATE SQL: "+sql,10);
            updatedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
            ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1) + "  SQL: "+sql);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					throw psscEx;
				}
			}
		}
		
		return updatedRecords;
	}
	
	public void insertSQL(String sql) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int deletedRecords = 0;
		Statement st = null;
		try 
		{
			con = ClsMngBBDD.getConnection();
			st = con.createStatement();
            ClsLogging.writeFileLog("SQL INSERT SQL: "+sql,10);
			st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
         	ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1) + " SQL: "+sql);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					throw psscEx;
				}
			}
		}
		
	}
	
	public void insertSQLBind(String sql, Hashtable codigos) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int deletedRecords = 0;
		PreparedStatement st = null;
		try 
		{
			con = ClsMngBBDD.getConnection();
			st = con.prepareStatement(sql);
			
			Enumeration e = codigos.keys();
	   	    while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         st.setString(key.intValue(), (String)codigos.get(key));
	   	    }
	   	     
            ClsLogging.writeFileLog("SQL INSERT SQL BIND: "+ClsMngBBDD.getSQLBindInformation(sql,codigos),10);
			st.executeUpdate();
		} 
		
		catch (SQLException ex) 
		{
            ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1)+" SQL BIND: "+ClsMngBBDD.getSQLBindInformation(sql,codigos));
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					throw psscEx;
				}
			}
		}
		
	}
	
	/**
	 * Ejecuta una sentencia INSERT en la Base de Datos.
	 * Actúa sobre la tabla especificada en el parámetro tableName.
	 * Usa los valores establecidos en la Hashtable de Row que correspondan con
	 * los campos especificados en el parémtro fieldNames. 
	 * @param tableName Nombre de la tabla sobre la que insertar registros.
	 * @param fieldNames Lista de los campos a incluir en la sentencia INSERT.
	 * @return Número de registros afectados (Insertados).
	 * @throws ClsExceptions
	 */
	public int add(String tableName, Object[] fieldNames) throws ClsExceptions 
	{
	    return addBind(tableName, fieldNames);
	    /*
		boolean thowsExc=false;
		Connection con = null;
		int insertedRecords = 0;
		Statement st = null;
		
		try 
		{
			con = ClsMngBBDD.getConnection();
			st = con.createStatement();
			String sql = buildInsertStatement(con, tableName, fieldNames);
			ClsLogging.writeFileLogWithoutSession("SQL INSERT: "+sql, 10);
			insertedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("7");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("7");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}
		
		return insertedRecords;
		*/
	}
	
	public int addBind(String tableName, Object[] fieldNames) throws ClsExceptions 
	{
		boolean thowsExc=false;
		Connection con = null;
		int insertedRecords = 0;
		PreparedStatement st = null;
		
		try 
		{
			con = ClsMngBBDD.getConnection();
			Vector v = buildInsertStatementBind(con, tableName, fieldNames);
			String sql = (String) v.get(0);
			Hashtable codigos = (Hashtable) v.get(1);
			st = con.prepareStatement(sql);
			ClsLogging.writeFileLogWithoutSession("SQL BIND INSERT: "+ClsMngBBDD.getSQLBindInformation(sql,codigos), 10);
			
			Enumeration e = codigos.keys();
	   	     while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         st.setString(key.intValue(), (String)codigos.get(key));
	   	     }
	         try {
	             insertedRecords  = st.executeUpdate();
	         } 
	         catch (SQLException exce) {
	         	 ClsLogging.writeFileLog("Error INSERT BIND: "+ exce.getMessage() + " SQL:"+ClsMngBBDD.getSQLBindInformation(sql,codigos),3);
	         	 throw exce;
	         }
//			 insertedRecords = st.executeUpdate();
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("7");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("7");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}
		
		return insertedRecords;
	}
	
	
	/**
	 * Ejecuta una sentencia UPDATE en la Base de Datos.
	 * Actúa sobre la tabla especificada en el parámetro tableName.
	 * Los campos contenidos en el parámetro keyfields serán usados para la cláusula WHERE.
	 * Los campos que se modificarán serán aquellos cuyo valor haya cambiado.
	 * En cualquier caso, los valores que se tomarán serán aquellos que estén almacenados en el
	 * objeto Hashtable de Row.
	 * @param tableName Nombre de la tabla sobre la que modificar registros.
	 * @param keyfields Lista de los campos a incluir en la sentencia UPDATE.
	 * @param updatableFields Lista de los campos que serán actualizados.
	 * @return Número de registros afectados (Modificados).
	 * @throws ClsExceptions
	 */
	public int update(String tableName, Object[] keyfields, Object[] updatableFields) throws ClsExceptions 
	{
	    return updateBind(tableName,keyfields,updatableFields);
	    /*
		Connection con = null;
		int updatedRecords = 0;
		try {
		boolean thowsExc=false;
		Statement st1 = null;
		con = ClsMngBBDD.getConnection();
		
		Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		String sql = " UPDATE " + tableName + " SET ";
		
		// SENTENCIA UPDATE
		String aux = " ";
		
		if (updatableFields != null) 
		{
			for (int i = 0; i < updatableFields.length; i++) 
			{
				if (!row.containsKey(updatableFields[i])) continue;
				sql += aux + updatableFields[i] + " = ";
				
				if (row.get(updatableFields[i]) == null || row.get(updatableFields[i]).equals("")) {
					sql += " NULL ";
				} else {
					if (dataTypes.get(updatableFields[i]).equals("STRING")) {
						sql += "'" + validateChars(row.get(updatableFields[i])) + "' ";
					} else if (dataTypes.get(updatableFields[i]).equals("NUMBER")) {
						sql += " " + row.get(updatableFields[i]) + " ";
					} else if (dataTypes.get(updatableFields[i]).equals("DATE")) {
						if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) {
							//sql += " " + row.get(updatableFields[i]).toString() + " ";
							sql += " SYSTIMESTAMP ";
						} else {
							//TEMPORAL: test the passed date is in standard format
							formatDate(row.get(updatableFields[i]));
							sql += " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						}
					} else {
						sql += row.get(updatableFields[i]).toString();
					}
				}
				aux = ", ";
			}
		}
		
		// Sentencia Condicional
		sql += buildSQLWhere(con, tableName, keyfields);
		
		Hashtable htend=new Hashtable();
		
		
		if (this.htorigin==null)
		{
			ClsLogging.writeFileLogError("(COMPARE) HASH ORIGIN IS EMPTY",this.request,3);
			ClsExceptions psscExorigin = new ClsExceptions("(COMPARE) HASH ORIGIN IS EMPTY");
			psscExorigin.setErrorType("0");
			psscExorigin.setMsg("(COMPARE) HASH ORIGIN IS EMPTY");
			psscExorigin.setParam("Comparing integrity data");
			psscExorigin.setErrorCode("UPDCOMPARE");
			throw psscExorigin;
		}
		
		Enumeration enumer =this.htorigin.keys();
		Hashtable hash2=(Hashtable)htorigin.clone();
		String ele="";
		String auxst="";
		
		while (enumer.hasMoreElements())  {
			ele=(String)enumer.nextElement();
			if (dataTypes.get(ele)==null) {
				hash2.remove(ele);
				continue;
			}
			if (dataTypes.get(ele).equals("DATE"))   {
				ele = "TO_CHAR(" + ele + ", '" + ClsConstants.DATE_FORMAT_SQL +  "') " + ele;
			}
			if (auxst.equals("")) {
				auxst=auxst+ele;
			} else {
				auxst=auxst+", "+ele;
			}
		}
		
		String queryPrev = "SELECT "+auxst+" FROM "+tableName+" "+buildSQLWhere(con, tableName, keyfields);
		ClsLogging.writeFileLog(queryPrev, request, 10);
		Connection conne = null;
		
		Statement st2 = null;
		
		try  {
			conne = ClsMngBBDD.getConnection();
			st2=conne.createStatement();
			ResultSet rs= st2.executeQuery(queryPrev);
			
			if (rs.next())   {
				Enumeration enumm =hash2.keys();
				String elem="";
				String val="";
				
				while (enumm.hasMoreElements())
				{
					elem=(String)enumm.nextElement();
					//if (dataTypes.get(ele)==null) continue;
					val=(String)rs.getString(elem);
					
					if (val==null)
					{
						val="";
					}
					
					htend.put(elem,val);
				}
			} else {
				ClsLogging.writeFileLogError("(COMPARACIÓN) LA CONSULTA NO HA DEVUELTO REGISTROS", this.request, 3);
				ClsExceptions psscExrow = new ClsExceptions("(COMPARACIÓN) LA CONSULTA NO HA DEVUELTO REGISTROS", "", "0", "GEN00", "21");
				psscExrow.setErrorCode("UPDCOMPARE");
				thowsExc=true;
				throw psscExrow;
			}
		} catch (SQLException ex) {
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("9");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} catch (ClsExceptions ex)  {
			thowsExc=true;
			throw ex;
		} catch (Exception exx)   {
			ClsExceptions psscExx = new ClsExceptions(exx,exx.getMessage().substring(0, exx.getMessage().length() - 1));
			psscExx.setErrorType("9");
			psscExx.setParam(tableName);
			thowsExc=true;
			throw psscExx;
		} 
		
		finally {
			try  {
				if (st2 != null) {
					st2.close();
				}
				if (conne != null) {
					ClsMngBBDD.closeConnection(conne);
				}
			} catch (SQLException exc) {
				if (conne != null) {
					ClsMngBBDD.closeConnection(conne);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("9");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}

		//  Chequeo de la integridad de los datos 
		
		Vector vect=null;
		
		if (htend==null) {
			ClsLogging.writeFileLogError("(COMPARACIÓN) La Hashtable a insertar está vacía", this.request, 3);
			ClsExceptions psscExrow = new ClsExceptions("(COMPARACIÓN) La Hashtable a insertar está vacía");
			psscExrow.setErrorType("0");
			psscExrow.setMsg("(COMPARACIÓN) La Hashtable a insertar está vacía");
			psscExrow.setParam("Comparando Integridad de Datos");
			psscExrow.setErrorCode("UPDCOMPARE");
			throw psscExrow;
		}
		try {
			ClsMngData mngData = new ClsMngData();
			//vect=mngData.compareHashtables(this.htorigin,htend,this.request);
			vect=mngData.compareHashtablesMinimum(this.htorigin,htend,this.request);
		} catch (ClsExceptions ex)  {
			int numError= new Integer(ex.getMsg()).intValue();
			switch (numError) {
			case 0:
			{
				ClsLogging.writeFileLogError("(COMPARACIÓN) Tamanhos de Hashtables es erróneos", this.request, 3);
				ClsExceptions psscEx = new ClsExceptions("(COMPARACIÓN) Tamanhos de Hashtables es erróneos");
				psscEx.setErrorType("0");
				psscEx.setMsg("(COMPARACIÓN) Tamanhos de Hashtables es erróneos");
				psscEx.setParam("Comparando Integridad de Datos");
				psscEx.setErrorCode("UPDCOMPARE");
				throw psscEx;
			}
			
			case 1:
			{
				ClsLogging.writeFileLogError("(COMPARACIÓN) Claves de Hashtables erróneas", this.request, 3);
				ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Claves de Hashtables erróneas");
				psscExaux.setErrorType("0");
				psscExaux.setMsg("(COMPARACIÓN) Claves de Hashtables erróneas");
				psscExaux.setParam("Comparing integrity data");
				psscExaux.setErrorCode("UPDCOMPARE");
				throw psscExaux;
			}
			}
		}
		
		if (vect.size()>0)
		{
			Enumeration en=vect.elements();
			ClsLogging.writeFileLog("*****************", this.request, 7);
			ClsLogging.writeFileLog("Elementos cambiados:", this.request, 7);
			
			String values="";
			String val="";
			String key="";
			int num=0;
			
			while (en.hasMoreElements())
			{
				PairsKeys obj = (PairsKeys)en.nextElement();
				val=(String)obj.getValueObj();
				key=(String)obj.getIdObj();
				ClsLogging.writeFileLog("Clave: " + key, this.request, 7);
				ClsLogging.writeFileLog("Valor: " + val, this.request, 7);
				ClsLogging.writeFileLog("*****************", this.request, 7);
				
				if (num==0)
				{
					values= key+": "+val;
				}
				
				else
				{
					values = values + ", " + key + ": " + val;
				}
				
				num++;
			}
			
			this.vectEnd=vect;
			
			// Prepare Exception
			ClsLogging.writeFileLogError("(COMPARACIÓN) Los registros han cambiado: " + values, this.request, 3);
			ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setErrorType("17");
			psscExaux.setMsg("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setParam(values);
			psscExaux.setErrorCode("UPDCOMPARE");
			throw psscExaux;
		}
		//  Chequeo de la integridad de los datos 
		
		else
		{
			
			
			//ClsLogging.writeFileLog("Actualizando.....", this.request, 7);
			Connection connec = null;
			
			try
			{
				ClsLogging.writeFileLog("SQL UPDATE: " + sql, this.request, 10);
				connec = ClsMngBBDD.getConnection();
				st1 = connec.createStatement();
				updatedRecords = st1.executeUpdate(sql);
			} 
			
			catch (SQLException ex) 
			{
				ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
				ClsLogging.writeFileLogError(ex.toString(), this.request, 3);
				psscEx.setErrorType("9");
				psscEx.setParam(tableName);
				
				throw psscEx;
			} 
			
			finally 
			{
				try 
				{
					if (st1 != null)
					{
						st1.close();
					}
					
					if (connec != null)
					{
						ClsMngBBDD.closeConnection(connec);
					}
				} 
				
				catch (SQLException exc) 
				{
					if (connec != null)
					{
						ClsMngBBDD.closeConnection(connec);
					}
					
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("9");
					psscEx.setParam(tableName);
					
					throw psscEx;
				}
			}
		}
		} finally {
			if (con!=null)
				ClsMngBBDD.closeConnection(con);
				
		}
		return updatedRecords;
		*/
	}

	public int updateBind(String tableName, Object[] keyfields, Object[] updatableFields) throws ClsExceptions 
	{
		Connection con = null;
		int updatedRecords = 0;
		try {
		boolean thowsExc=false;
		PreparedStatement st1 = null;
		con = ClsMngBBDD.getConnection();
		
		Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		String sql = " UPDATE " + tableName + " SET ";
		
		Hashtable codigos = new Hashtable();
  	  	int contador=1;
  	  	
		// SENTENCIA UPDATE
		String aux = " ";
		
		if (updatableFields != null) 
		{
			for (int i = 0; i < updatableFields.length; i++) 
			{
				if (!row.containsKey(updatableFields[i])) continue;
				sql += aux + updatableFields[i] + " = ";
				
				if (row.get(updatableFields[i]) == null || row.get(updatableFields[i]).equals("") || row.get(updatableFields[i]).equals("null") || row.get(updatableFields[i]).equals("NULL")) {
					sql += " NULL ";
				} else {
					if (dataTypes.get(updatableFields[i]).equals("STRING")) {
						//sql += "'" + validateChars(row.get(updatableFields[i])) + "' ";
						sql +=":"+new Integer(contador).toString()+" ";
						//codigos.put(new Integer(contador),validateChars(""+row.get(updatableFields[i])));
						codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;
					} else if (dataTypes.get(updatableFields[i]).equals("NUMBER")) {
						//sql += " " + row.get(updatableFields[i]) + " ";
						sql +=":"+new Integer(contador).toString()+" ";
  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;
					} else if (dataTypes.get(updatableFields[i]).equals("DATE")) {
						if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) {
							//sql += " " + row.get(updatableFields[i]).toString() + " ";
							sql += " SYSTIMESTAMP ";
						} else {
							//TEMPORAL: test the passed date is in standard format
							// esto no hace nada porque no recoge la respuesta 
						    formatDate(row.get(updatableFields[i]));
							//sql += " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
							sql += " TO_DATE(" + ":"+new Integer(contador).toString() + ", '" + ClsConstants.DATE_FORMAT_SQL + "') ";
	  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
	  	  				    contador++;
						}
					} else {
						//sql += row.get(updatableFields[i]).toString();
						sql += ":"+new Integer(contador).toString()+" ";
  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;
					}
				}
				aux = ", ";
			}
		}
		
		// Sentencia Condicional
		Vector v = buildSQLWhereBind(con, tableName, keyfields, codigos, contador);
		sql += (String) v.get(0);
		codigos = (Hashtable) v.get(1);
		
		// recalculo el where para nuevas variables (para consulta de comparacion)
		Hashtable codigosCompare = new Hashtable();
		Vector v2 = buildSQLWhereBind(con, tableName, keyfields, codigosCompare, 1);
		String sqlWhere = (String) v2.get(0);
		codigosCompare = (Hashtable) v2.get(1);
		
		
		Hashtable htend=new Hashtable();
		
		/********** FOR UPDATE ***********/
		if (this.htorigin==null)
		{
			ClsLogging.writeFileLog("(COMPARE) HASH ORIGIN IS EMPTY",this.request,3);
			ClsExceptions psscExorigin = new ClsExceptions("(COMPARE) HASH ORIGIN IS EMPTY");
			psscExorigin.setErrorType("0");
			psscExorigin.setMsg("(COMPARE) HASH ORIGIN IS EMPTY");
			psscExorigin.setParam("Comparing integrity data");
			psscExorigin.setErrorCode("UPDCOMPARE");
			throw psscExorigin;
		}
		
		Enumeration enumer =this.htorigin.keys();
		Hashtable hash2=(Hashtable)htorigin.clone();
		String ele="";
		String auxst="";
		
		while (enumer.hasMoreElements())  {
			ele=(String)enumer.nextElement();
			if (dataTypes.get(ele)==null) {
				hash2.remove(ele);
				continue;
			}
			if (dataTypes.get(ele).equals("DATE"))   {
				ele = "TO_CHAR(" + ele + ", '" + ClsConstants.DATE_FORMAT_SQL +  "') " + ele;
			}
			if (auxst.equals("")) {
				auxst=auxst+ele;
			} else {
				auxst=auxst+", "+ele;
			}
		}


		String queryPrev = "SELECT "+auxst+" FROM "+tableName+" "+sqlWhere;
		ClsLogging.writeFileLog("query de compare: "+ClsMngBBDD.getSQLBindInformation(queryPrev,codigosCompare), request, 10);
		Connection conne = null;
		
		PreparedStatement st2= null;
		ResultSet rs = null;
		try  {
			 conne = ClsMngBBDD.getConnection();
             st2 = conne.prepareStatement(queryPrev);
	   	     Enumeration e = codigosCompare.keys();
	   	     while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         st2.setString(key.intValue(), (String)codigosCompare.get(key));
	   	     }
            try {
                rs = st2.executeQuery();
            } 
            catch (SQLException exce) {
            	throw exce;
            }
//            rs = st2.executeQuery();

            /*
			st2=conne.createStatement();
			ResultSet rs= st2.executeQuery(queryPrev);
			*/
            
			if (rs.next())   {
				Enumeration enumm =hash2.keys();
				String elem="";
				String val="";
				
				while (enumm.hasMoreElements())
				{
					elem=(String)enumm.nextElement();
					//if (dataTypes.get(ele)==null) continue;
					val=(String)rs.getString(elem);
					
					if (val==null)
					{
						val="";
					}
					
					htend.put(elem,val);
				}
			} else {
				ClsLogging.writeFileLog("(COMPARACIÓN) LA CONSULTA NO HA DEVUELTO REGISTROS", this.request, 3);
				ClsExceptions psscExrow = new ClsExceptions("(COMPARACIÓN) LA CONSULTA NO HA DEVUELTO REGISTROS", "", "0", "GEN00", "21");
				psscExrow.setErrorCode("UPDCOMPARE");
				thowsExc=true;
				throw psscExrow;
			}
		} catch (SQLException ex) {
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("9");
			psscEx.setParam(tableName);
			thowsExc=true;
			throw psscEx;
		} catch (ClsExceptions ex)  {
			thowsExc=true;
			throw ex;
		} catch (Exception exx)   {
			ClsExceptions psscExx = new ClsExceptions(exx,exx.getMessage().substring(0, exx.getMessage().length() - 1));
			psscExx.setErrorType("9");
			psscExx.setParam(tableName);
			thowsExc=true;
			throw psscExx;
		} 
		
		finally {
			try  {
				if (st2 != null) {
					st2.close();
				}
				if (conne != null) {
					ClsMngBBDD.closeConnection(conne);
				}
			} catch (SQLException exc) {
				if (conne != null) {
					ClsMngBBDD.closeConnection(conne);
				}
				if (!thowsExc) {
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("9");
					psscEx.setParam(tableName);
					throw psscEx;
				}
			}
		}
		/********** FOR UPDATE ***********/
		
		/******************************************/
		/*  Chequeo de la integridad de los datos */
		/******************************************/
		
		Vector vect=null;
		
		if (htend==null) {
			ClsLogging.writeFileLog("(COMPARACIÓN) La Hashtable a insertar está vacía", this.request, 3);
			ClsExceptions psscExrow = new ClsExceptions("(COMPARACIÓN) La Hashtable a insertar está vacía");
			psscExrow.setErrorType("0");
			psscExrow.setMsg("(COMPARACIÓN) La Hashtable a insertar está vacía");
			psscExrow.setParam("Comparando Integridad de Datos");
			psscExrow.setErrorCode("UPDCOMPARE");
			throw psscExrow;
		}
		try {
			ClsMngData mngData = new ClsMngData();
			//vect=mngData.compareHashtables(this.htorigin,htend,this.request);
			vect=mngData.compareHashtablesMinimum(this.htorigin,htend,this.request);
		} catch (ClsExceptions ex)  {
			int numError= new Integer(ex.getMsg()).intValue();
			switch (numError) {
			case 0:
			{
				ClsLogging.writeFileLog("(COMPARACIÓN) Tamanhos de Hashtables es erróneos", this.request, 3);
				ClsExceptions psscEx = new ClsExceptions("(COMPARACIÓN) Tamanhos de Hashtables es erróneos");
				psscEx.setErrorType("0");
				psscEx.setMsg("(COMPARACIÓN) Tamanhos de Hashtables es erróneos");
				psscEx.setParam("Comparando Integridad de Datos");
				psscEx.setErrorCode("UPDCOMPARE");
				throw psscEx;
			}
			
			case 1:
			{
				ClsLogging.writeFileLog("(COMPARACIÓN) Claves de Hashtables erróneas", this.request, 3);
				ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Claves de Hashtables erróneas");
				psscExaux.setErrorType("0");
				psscExaux.setMsg("(COMPARACIÓN) Claves de Hashtables erróneas");
				psscExaux.setParam("Comparing integrity data");
				psscExaux.setErrorCode("UPDCOMPARE");
				throw psscExaux;
			}
			}
		}
		
		if (vect.size()>0)
		{
			Enumeration en=vect.elements();
			ClsLogging.writeFileLog("*****************", this.request, 7);
			ClsLogging.writeFileLog("Elementos cambiados:", this.request, 7);
			
			String values="";
			String val="";
			String key="";
			int num=0;
			
			while (en.hasMoreElements())
			{
				PairsKeys obj = (PairsKeys)en.nextElement();
				val=(String)obj.getValueObj();
				key=(String)obj.getIdObj();
				ClsLogging.writeFileLog("Clave: " + key, this.request, 7);
				ClsLogging.writeFileLog("Valor: " + val, this.request, 7);
				ClsLogging.writeFileLog("*****************", this.request, 7);
				
				if (num==0)
				{
					values= key+": "+val;
				}
				
				else
				{
					values = values + ", " + key + ": " + val;
				}
				
				num++;
			}
			
			this.vectEnd=vect;
			
			// Prepare Exception
			ClsLogging.writeFileLog("(COMPARACIÓN) Los registros han cambiado: " + values, this.request, 3);
			ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setErrorType("17");
			psscExaux.setMsg("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setParam(values);
			psscExaux.setErrorCode("UPDCOMPARE");
			throw psscExaux;
		}
		/*  Chequeo de la integridad de los datos */
		
		else
		{
			/********** ********** ***********/
			/********** FOR UPDATE ***********/
			/********** ********** ***********/
			
			//ClsLogging.writeFileLog("Actualizando.....", this.request, 7);
			Connection connec = null;
			
			try
			{
				ClsLogging.writeFileLog("SQL BIND UPDATE: " + ClsMngBBDD.getSQLBindInformation(sql,codigos), this.request, 10);
				connec = ClsMngBBDD.getConnection();
				/*
				 st1 = connec.createStatement();
				 updatedRecords = st1.executeUpdate(sql);
				*/
				
				st1 = connec.prepareStatement(sql);
		   	    Enumeration e2 = codigos.keys();
		   	    while (e2.hasMoreElements()) {
		   	         Integer key = (Integer)e2.nextElement();
		   	         st1.setString(key.intValue(), (String)codigos.get(key));
		   	     }
	            try {
	                updatedRecords = st1.executeUpdate();
	            } catch (SQLException exce) {
	           	 ClsLogging.writeFileLog("Error en UPDATE BIND: "+ exce.getMessage() + " SQL:"+ClsMngBBDD.getSQLBindInformation(sql,codigos),3);
	           	 throw exce;
	            }				
				
			} 
			
			catch (SQLException ex) 
			{
				ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
				ClsLogging.writeFileLog(ex.toString(), this.request, 3);
				psscEx.setErrorType("9");
				psscEx.setParam(tableName);
				
				throw psscEx;
			} 
			
			finally 
			{
				try 
				{
					if (st1 != null)
					{
						st1.close();
					}
					
					if (connec != null)
					{
						ClsMngBBDD.closeConnection(connec);
					}
				} 
				
				catch (SQLException exc) 
				{
					if (connec != null)
					{
						ClsMngBBDD.closeConnection(connec);
					}
					
					ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
					psscEx.setErrorType("9");
					psscEx.setParam(tableName);
					
					throw psscEx;
				}
			}
		}
		} finally {
			if (con!=null)
				ClsMngBBDD.closeConnection(con);
				
		}
		return updatedRecords;
	}

	/**
	 * Ejecuta una sentencia UPDATE en la Base de Datos.
	 * Actúa sobre la tabla especificada en el parámetro tableName.
	 * Los campos contenidos en el parámetro keyfields serán usados para la cláusula WHERE.
	 * Los campos que se modificarán serán aquellos cuyo valor haya cambiado.
	 * En cualquier caso, los valores que se tomarán serán aquellos que estén almacenados en el
	 * objeto Hashtable de Row.
	 * @param tableName Nombre de la tabla sobre la que modificar registros.
	 * @param keyfields Lista de los campos a incluir en la sentencia UPDATE.
	 * @param updatableFields Lista de los campos que serán actualizados.
	 * @return Número de registros afectados (Modificados).
	 * @throws ClsExceptions
	 */
	public int updateDirect(String tableName, Object[] keyfields, Object[] updatableFields) throws ClsExceptions 
	{
	    return updateDirectBind(tableName, keyfields, updatableFields);
	    /*
		int updatedRecords = 0;
		
		Connection con = null;
		try {
		Statement st = null;
		con = ClsMngBBDD.getConnection();
		
		Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		String sql = " UPDATE " + tableName + " SET ";
		
		// UPDATE SENTENCE!
		String aux = " ";
		if (updatableFields != null) 
		{
			for (int i = 0; i < updatableFields.length; i++) 
			{
				sql += aux + updatableFields[i] + " = ";
				
				if (row.get(updatableFields[i]) == null || row.get(updatableFields[i]).equals("")) 
				{
					sql += " NULL ";
				} 
				
				else 
				{
					if (dataTypes.get(updatableFields[i]).equals("STRING")) 
					{
						sql += "'" + validateChars(row.get(updatableFields[i])) + "' ";
					} 
					
					else if (dataTypes.get(updatableFields[i]).equals("NUMBER")) 
					{
						sql += " " + row.get(updatableFields[i]) + " ";
					} 
					
					else if (dataTypes.get(updatableFields[i]).equals("DATE")) 
					{
						if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) 
						{
							//sql += " " + row.get(updatableFields[i]).toString() + " ";
							sql += " SYSTIMESTAMP ";
						} 
						
						else 
						{
							//TEMPORAL: test the passed date is in standard format
							formatDate(row.get(updatableFields[i]));
							sql += " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						}
					} 
					
					else 
					{
						sql += row.get(updatableFields[i]).toString();
					}
				}
				
				aux = ", ";
			}
		}
		
		// Conditional Sentence!
		sql += buildSQLWhere(con, tableName, keyfields);
		
		Hashtable htend=new Hashtable();
		
		Connection connec = null;
		
		try
		{
			ClsLogging.writeFileLog("SQL UPDATE: " + sql, 10);
			connec = ClsMngBBDD.getConnection();
			st = connec.createStatement();
			updatedRecords = st.executeUpdate(sql);
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			ClsLogging.writeFileLogError(ex.toString(), this.request, 3);
			psscEx.setErrorType("9");
			psscEx.setParam(tableName);
			
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (connec != null)
				{
					ClsMngBBDD.closeConnection(connec);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (connec != null)
				{
					ClsMngBBDD.closeConnection(connec);
				}
				
				ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
				psscEx.setErrorType("9");
				psscEx.setParam(tableName);
				
				throw psscEx;
			}
		}
		} finally {
			if (con!=null)
				ClsMngBBDD.closeConnection(con);
		}
		return updatedRecords;
		*/
	}
	
	public int updateDirectBind(String tableName, Object[] keyfields, Object[] updatableFields) throws ClsExceptions 
	{
		int updatedRecords = 0;
		String aux = " ";
		Hashtable codigos= new Hashtable();
		
		Connection con = null;
		try {
		PreparedStatement st = null;
		con = ClsMngBBDD.getConnection();
		
		Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		String sql = " UPDATE " + tableName + " SET ";
		int contador=1;
		// UPDATE SENTENCE!
		if (updatableFields != null) 
		{
			for (int i = 0; i < updatableFields.length; i++) 
			{
				sql += aux + updatableFields[i] + " = ";
				
				if (row.get(updatableFields[i]) == null || row.get(updatableFields[i]).equals("") || row.get(updatableFields[i]).equals("null") || row.get(updatableFields[i]).equals("NULL")) 
				{
					sql += " NULL ";
				} 
				else 
				{
					if (dataTypes.get(updatableFields[i]).equals("STRING")) 
					{
						//sql += "'" + validateChars(row.get(updatableFields[i])) + "' ";
						sql +=":"+new Integer(contador).toString()+" ";
						//codigos.put(new Integer(contador),validateChars(""+row.get(updatableFields[i])));
						codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;
					} 
					
					else if (dataTypes.get(updatableFields[i]).equals("NUMBER")) 
					{
						//sql += " " + row.get(updatableFields[i]) + " ";
						sql +=":"+new Integer(contador).toString()+" ";
  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;
					} 
					
					else if (dataTypes.get(updatableFields[i]).equals("DATE")) 
					{
						if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) 
						{
							//sql += " " + row.get(updatableFields[i]).toString() + " ";
							sql += " SYSTIMESTAMP ";
							/*
							sql +=":"+new Integer(contador).toString()+" ";
							codigos.put(new Integer(contador)," SYSTIMESTAMP ");
		  				    contador++;
		  				    */
						} 
						
						else 
						{
							//TEMPORAL: test the passed date is in standard format
							formatDate(row.get(updatableFields[i]));
							//sql += " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
							sql +=" TO_DATE(:"+new Integer(contador).toString() + ", '" + ClsConstants.DATE_FORMAT_SQL + "') ";
	  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
	  	  				    contador++;

						}
					} 
					
					else 
					{
						//sql += row.get(updatableFields[i]).toString();
						sql +=":"+new Integer(contador).toString()+" ";
  	  				    codigos.put(new Integer(contador),""+row.get(updatableFields[i]));
  	  				    contador++;

					}
				}
				
				aux = ", ";
			}
		}
		
		// Conditional Sentence!
		//sql += buildSQLWhere(con, tableName, keyfields);
		Vector v = buildSQLWhereBind(con, tableName, keyfields, codigos, contador);
		String sqlWhere = (String) v.get(0);
		sql += sqlWhere;
		codigos = (Hashtable) v.get(1);
		
		Hashtable htend=new Hashtable();
		
		Connection connec = null;
		
		try
		{
			ClsLogging.writeFileLog("SQL BIND UPDATE: " + ClsMngBBDD.getSQLBindInformation(sql,codigos), 10);
			connec = ClsMngBBDD.getConnection();
			/*
			st = connec.createStatement();
			updatedRecords = st.executeUpdate(sql);
			*/
			st = connec.prepareStatement(sql);
	   	     Enumeration e = codigos.keys();
	   	     while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         st.setString(key.intValue(), (String)codigos.get(key));
	   	     }
           try {
               updatedRecords = st.executeUpdate();
           } catch (SQLException exce) {
          	 throw exce;
           }
			
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			ClsLogging.writeFileLog(ex.toString(), this.request, 3);
			psscEx.setErrorType("9");
			psscEx.setParam(tableName);
			
			throw psscEx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (connec != null)
				{
					ClsMngBBDD.closeConnection(connec);
				}
			} 
			
			catch (SQLException exc) 
			{
				if (connec != null)
				{
					ClsMngBBDD.closeConnection(connec);
				}
				
				ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
				psscEx.setErrorType("9");
				psscEx.setParam(tableName);
				
				throw psscEx;
			}
		}
		} finally {
			if (con!=null)
				ClsMngBBDD.closeConnection(con);
		}
		return updatedRecords;
	}
	
	/**
	 * Reemplaza un patrón por otro dentro de una cadena.
	 * @param str Cadena en la que buscar.
	 * @param pattern Patrón a buscar.
	 * @param replace Nueva cadena.
	 * @return Cadena reemplazada.
	 */
	
	public String replacePattern(String str, String pattern, String replace) 
	{
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		
		while ((e = str.indexOf(pattern, s)) >= 0) 
		{
			result.append(str.substring(s, e));
			result.append(replace);
			s = e+pattern.length();
		}
		
		result.append(str.substring(s));
		
		return result.toString();
	}
	
	/**
	 * Reemplaza una comilla simpre por dos comillas simples.
	 * @param cad Cadena a tratar.
	 * @return Cadena reemplazada.
	 */
	private String validateChars(Object cad) 
	{
		return replacePattern(replaceBlanks(cad.toString()), "'", "''");
	}
	
	/**
	 * Borra los espacios blancos sobrantes de una cadena.
	 * @param str Cadena a tratar.
	 * @return Cadena reemplazada.
	 */
	private String replaceBlanks(String str) 
	{
		byte[] aux = str.trim().getBytes();
		int j=0;
		
		for(int i=0; i<aux.length;)
		{
			aux[j++]=aux[i];
			
			if(aux[i++]==' ')
			{
				while(aux[i]==' ')i++;
			}
		}
		
		return new String(aux,0,j);
	}
	
	/**
	 * Devuelve una cláusula WHERE en formato válido.
	 * @param str Cadena a formatear.
	 * @return Cadena formateada.
	 */
	public static String validateWhereClause(String str) 
	{
		str = str.trim();
		StringBuffer sb = null;
		String aux = str.toUpperCase();
		int pos = aux.indexOf("WHERE");
		
		if(pos!=-1)
		{
			sb = new StringBuffer(str.substring(0, pos+=5));
			int pos2 = aux.indexOf("'", pos);
			
			if(pos2!=-1)
			{
				sb.append(str.substring(pos, pos2+=1));
				StringTokenizer st = new StringTokenizer(str.substring(pos2), "'", true);
				
				if(st.countTokens()>0)
				{
					boolean isComm = false;
					boolean isElem = true;
					
					while(st.hasMoreTokens())
					{
						String tok = st.nextToken();
						
						if(isComm)
						{
							
							if(isElem && !(tok.startsWith(" ") || 
									tok.startsWith(",") || 
									tok.startsWith(")") || 
									tok.startsWith("||") || 
									tok.toUpperCase().startsWith("AND") || 
									tok.toUpperCase().startsWith("OR")))
							{
								sb.append("'");
							}
							
							else if(isElem && tok.startsWith(" ") && st.countTokens()>0)
							{
								if(checkToken(tok.toUpperCase().trim()))
								{
									isElem=false;
								}
								
								else
								{
									sb.append("'");
								}
							} 
							
							else
							{
								isElem=!isElem;
							}
						}
						
						sb.append(tok);
						isComm = tok.equals("'");
					}
					
					if(isElem ^ isComm)
					{
						ClsLogging.writeFileLogWithoutSession("Wrong Sequence in WHERE clause", 3);
						ClsLogging.writeFileLogWithoutSession("Receive: "+str, 3);
						ClsLogging.writeFileLogWithoutSession("Process: "+sb.toString(), 3);
						sb = new StringBuffer(str);
					}
				}
			} 
			
			else
			{
				sb.append(str.substring(pos));
			}
		} 
		
		else
		{
			sb = new StringBuffer(str);
		}
		
		return sb.toString();
	}
	
	/**
	 * Comprueba si una cadena contiene alguna de las palabras "AND ", "OR ", "ORDER BY", "GROUP BY", "START WITH".
	 * @param tok Cadena en la que buscar.
	 * @return true si la cadena contiene alguna de las palabras especificadas. false en caso contrario.
	 */
	private static boolean checkToken(String tok)
	{
		String[] sReserve = {"AND ", "OR ", "ORDER BY", "GROUP BY", "START WITH"};
		
		for(int i=0; i<sReserve.length; i++)
		{
			if(tok.indexOf(sReserve[i])!=-1)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Construye una sentencia INSERT.
	 * @param con Objeto Connection a la Base de Datos.
	 * @param tableName Tabla sobre la que se realizará el INSERT.
	 * @param fieldNames Nombres de los campos para la cláusula INSERT.
	 * @return Cadena formateada.
	 */
	public String buildInsertStatement(Connection con, String tableName, Object[] fieldNames) throws ClsExceptions 
	{
		if (table == null || !table.equals(tableName)) 
		{
			table = tableName;
			this.datatypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		}
		
		StringBuffer sqlFields = new StringBuffer(" INSERT INTO " + tableName + "(");
		StringBuffer sqlValues = new StringBuffer(" ) VALUES (");
		
		if (fieldNames != null) 
		{
			String aux = " ";
			
			for (int i = 0; i < fieldNames.length; i++) 
			{
				if (row.get(fieldNames[i]) == null || row.get(fieldNames[i]).equals("") ||
						"NULL".equalsIgnoreCase(""+fieldNames[i]))
					//  	            		((String)row.get(fieldNames[i])).equalsIgnoreCase("NULL")) 
				{
					sqlValues.append(aux + " NULL ");
				} 
				
				else 
				{
					if (datatypes.get(fieldNames[i]).equals("STRING")) 
					{
						sqlValues.append(aux + " '" + validateChars(row.get(fieldNames[i])) + "' ");
					} 
					
					else if (datatypes.get(fieldNames[i]).equals("NUMBER")) 
					{
						sqlValues.append(aux + row.get(fieldNames[i]) + " ");
					} 
					
					else if (datatypes.get(fieldNames[i]).equals("DATE")) 
					{
						if (row.get(fieldNames[i]).toString().equalsIgnoreCase("SYSDATE")) 
						{
							sqlValues.append(aux + " " + row.get(fieldNames[i]).toString() + " ");
						} 
						
						else 
						{
							//TEMPORAL: test the passed date is in standard format
							formatDate(row.get(fieldNames[i]));
							sqlValues.append(aux + " TO_DATE('" + row.get(fieldNames[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
						}
					} 
					
					else 
					{
						sqlValues.append(aux + " " + row.get(fieldNames[i]).toString() + " ");
					}
				}
				
				sqlFields.append(aux + fieldNames[i]);
				aux = ", ";
			}
			
			sqlValues.append(") ");
		}
		
		sqlFields.append(sqlValues.toString());
		
		return sqlFields.toString();
	}
	
	public Vector buildInsertStatementBind(Connection con, String tableName, Object[] fieldNames) throws ClsExceptions 
	{
	    Vector salida = new Vector();
	    Hashtable codigos = new Hashtable();
	    int contador=1;
	    
		if (table == null || !table.equals(tableName)) 
		{
			table = tableName;
			this.datatypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		}
		
		StringBuffer sqlFields = new StringBuffer(" INSERT INTO " + tableName + "(");
		StringBuffer sqlValues = new StringBuffer(" ) VALUES (");
		
		if (fieldNames != null) 
		{
			String aux = " ";
			
			for (int i = 0; i < fieldNames.length; i++) 
			{
				if (row.get(fieldNames[i]) == null || row.get(fieldNames[i]).equals("") ||
					"NULL".equalsIgnoreCase(""+fieldNames[i]) ||
				    row.get(fieldNames[i]).equals("NULL") || 
				    row.get(fieldNames[i]).equals("null")) 
				{
					sqlValues.append(aux + " NULL ");
				} 
				
				else 
				{
					if (datatypes.get(fieldNames[i]).equals("STRING")) 
					{
						//sqlValues.append(aux + " '" + validateChars(row.get(fieldNames[i])) + "' ");
						sqlValues.append(aux + ":"+new Integer(contador).toString()+" ");
						//codigos.put(new Integer(contador),""+validateChars(row.get(fieldNames[i])));
						codigos.put(new Integer(contador),""+row.get(fieldNames[i]));
  	  				    contador++;
					} 
					
					else if (datatypes.get(fieldNames[i]).equals("NUMBER")) 
					{
						//sqlValues.append(aux + row.get(fieldNames[i]) + " ");
						sqlValues.append(aux + ":"+new Integer(contador).toString()+" ");
						codigos.put(new Integer(contador),""+row.get(fieldNames[i]));
  	  				    contador++;

					} 
					
					else if (datatypes.get(fieldNames[i]).equals("DATE")) 
					{
						if (row.get(fieldNames[i]).toString().equalsIgnoreCase("SYSDATE")) 
						{
							sqlValues.append(aux + " SYSTIMESTAMP ");
						} 
						
						else 
						{
							//TEMPORAL: test the passed date is in standard format
							formatDate(row.get(fieldNames[i]));
							//sqlValues.append(aux + " TO_DATE('" + row.get(fieldNames[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
							sqlValues.append(aux + " TO_DATE(:"+new Integer(contador).toString()+", '" + ClsConstants.DATE_FORMAT_SQL + "')");
							codigos.put(new Integer(contador),""+row.get(fieldNames[i]).toString());
	  	  				    contador++;
						}
					} 
					
					else 
					{
						//sqlValues.append(aux + " " + row.get(fieldNames[i]).toString() + " ");
						sqlValues.append(aux + ":"+new Integer(contador).toString()+" ");
						codigos.put(new Integer(contador),""+row.get(fieldNames[i]).toString());
  	  				    contador++;
					}
				}
				
				sqlFields.append(aux + fieldNames[i]);
				aux = ", ";
			}
			
			sqlValues.append(") ");
		}
		
		sqlFields.append(sqlValues.toString());
		
		salida.add(sqlFields.toString());
		salida.add(codigos);
		return salida;
	}
	
	/**
	 * Construye una sentencia SELECT con cláusula WHERE.
	 * @param con Objeto Connection a la Base de Datos.
	 * @param tableName Tabla sobre la que se realizará el SELECT.
	 * @param keyfields Nombres de los campos para la cláusula SELECT.
	 * @return Cadena formateada.
	 */
	public String buildSQLWhere(Connection con, String tableName, Object[] keyfields) throws ClsExceptions 
	{
		if (table == null || !table.equals(tableName)) 
		{
			table = tableName;
			this.datatypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		}
		
		StringBuffer sqlWhere = new StringBuffer("");
		
		if (keyfields != null) 
		{
			String aux = " WHERE ";
			
			for (int i = 0; i < keyfields.length; i++) 
			{
				if (row.get(keyfields[i]) == null) 
				{
					sqlWhere.append(aux + keyfields[i] + " IS NULL ");
					
				} 
				
				else if (row.get(keyfields[i]).toString().trim().equals("")) 
				{
					sqlWhere.append(aux + keyfields[i] + " IS NULL ");
				}
				
				else 
				{
					sqlWhere.append(aux + keyfields[i] + " = ");
					
					if (datatypes.get(keyfields[i]).equals("STRING")) 
					{
						sqlWhere.append("'" + validateChars(row.get(keyfields[i])) + "' ");
					} 
					
					else if (datatypes.get(keyfields[i]).equals("NUMBER")) 
					{
						sqlWhere.append(" " + row.get(keyfields[i]) + " ");
					} 
					
					else if (datatypes.get(keyfields[i]).equals("DATE")) 
					{
						//TEMPORAL: test the passed date is in standard format
						formatDate(row.get(keyfields[i]));
						sqlWhere.append(" TO_DATE('" + row.get(keyfields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
					} 
					
					else 
					{
						sqlWhere.append(row.get(keyfields[i]).toString());
					}
				}
				
				aux = " AND ";
			}
		}
		
		return sqlWhere.toString();
	}
	
	/**
	 * Construye una sentencia SELECT con cláusula WHERE.
	 * @param con Objeto Connection a la Base de Datos.
	 * @param tableName Tabla sobre la que se realizará el SELECT.
	 * @param keyfields Nombres de los campos para la cláusula SELECT.
	 * @return Cadena formateada.
	 */
	public Vector buildSQLWhereBind(Connection con, String tableName, Object[] keyfields, Hashtable codigos, int cont) throws ClsExceptions 
	{
	    Vector salida = new Vector();
	    int contador = cont;
	    
		if (table == null || !table.equals(tableName)) 
		{
			table = tableName;
			this.datatypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		}
		
		StringBuffer sqlWhere = new StringBuffer("");
		
		if (keyfields != null) 
		{
			String aux = " WHERE ";
			
			for (int i = 0; i < keyfields.length; i++) 
			{
				if (row.get(keyfields[i]) == null) 
				{
					sqlWhere.append(aux + keyfields[i] + " IS NULL ");
				} 
				else if (row.get(keyfields[i]).toString().trim().equals("") || row.get(keyfields[i]).equals("null") || row.get(keyfields[i]).equals("NULL")) 
				{
					sqlWhere.append(aux + keyfields[i] + " IS NULL ");
				}
				else 
				{
					sqlWhere.append(aux + keyfields[i] + " = ");
					
					if (datatypes.get(keyfields[i]).equals("STRING")) 
					{
					    //sqlWhere.append("'" + validateChars(row.get(keyfields[i])) + "' ");
					    sqlWhere.append(":"+new Integer(contador).toString()+" ");
					    //codigos.put(new Integer(contador),validateChars(""+row.get(keyfields[i])));
					    codigos.put(new Integer(contador),""+row.get(keyfields[i]));
  	  				    contador++;
					} 
					else if (datatypes.get(keyfields[i]).equals("NUMBER")) 
					{
						//sqlWhere.append(" " + row.get(keyfields[i]) + " ");
					    sqlWhere.append(":"+new Integer(contador).toString()+" ");
						codigos.put(new Integer(contador),""+row.get(keyfields[i]));
  	  				    contador++;
					} 
					
					else if (datatypes.get(keyfields[i]).equals("DATE")) 
					{
						//TEMPORAL: test the passed date is in standard format
						formatDate(row.get(keyfields[i]));
						//sqlWhere.append(" TO_DATE('" + row.get(keyfields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
					    sqlWhere.append(" TO_DATE(:" + new Integer(contador).toString() + ", '" + ClsConstants.DATE_FORMAT_SQL + "') ");
						codigos.put(new Integer(contador),""+row.get(keyfields[i]));
  	  				    contador++;
					} 
					
					else 
					{
						//sqlWhere.append(row.get(keyfields[i]).toString());
					    sqlWhere.append(":"+new Integer(contador).toString()+" ");
						codigos.put(new Integer(contador),""+row.get(keyfields[i]));
  	  				    contador++;

					}
				}
				
				aux = " AND ";
			}
		}
		salida.add(sqlWhere.toString());
		salida.add(codigos);
		return salida;
	}
	
	/**
	 * Devuelve una Hashtable con los tipos de datos de las columnas.
	 * @param tableName Nombre de la tabla.
	 * @return Hashtable con los tipos de datos de las columnas de la tabla especificada como parámetro.
	 */
	public Hashtable getDataTypes(String tableName) 
	{
		Hashtable ht = null;
		
		if (datatypes != null && table.equals(tableName)) 
		{
			ht = datatypes;
		}
		
		return ht;
	}
	
	/**
	 * Establece los tipos de datos de las columnas.
	 * @param tableName Nombre de la tabla.
	 * @param dTypes Hashtable con los tipos de datos.
	 */
	public void setDataTypes(String tableName, Hashtable dTypes) 
	{
		table = tableName;
		datatypes = dTypes;
	}
	
	/**
	 * Guarda una Hashtable con los datos originales.
	 * @param htor Hashtable con los datos originales.
	 */
	public void setCompareData(Hashtable htor)
	{
		this.htorigin=htor;
	}
	
	/**
	 * Guarda una Hashtable con los datos originales.
	 * @param htor Hashtable con los datos originales.
	 * @param req Petición.
	 */
	public void setCompareData(Hashtable htor, HttpServletRequest req)
	{
		this.htorigin=htor;
		this.request=req;
	}
	
	/**
	 * Devuelve un Vector con los registros modificados.
	 */
	public Vector getRecordsChanged()
	{
		return this.vectEnd;
	}
	
	/**
	 * Devuelve una cadena de una fecha a partir de un objeto.
	 * @param date Objeto con la fecha.
	 */
	private String formatDate(Object date) throws ClsExceptions 
	{
		String val = "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		java.util.Date d = null;
		
		if (date instanceof java.sql.Date) 
		{
			d = new java.util.Date(((java.sql.Date) date).getTime());
		}
		
		if (date instanceof java.util.Date) 
		{
			d = (java.util.Date)date;
		}
		
		if (date instanceof java.sql.Timestamp) 
		{
			d = new java.util.Date(((java.sql.Timestamp) date).getTime());
		}
		
		if (date instanceof java.sql.Time) 
		{
			d = new java.util.Date(((java.sql.Time) date).getTime());
		}
		
		try 
		{
			if (date instanceof java.lang.String) 
			{
				d = sdf.parse((String)date);
			}
			
			val = sdf.format(d);
			
		} 
		
		catch (Exception e) 
		{
			ClsExceptions el=new ClsExceptions(e,"El Formato de Fecha debe ser '" + ClsConstants.DATE_FORMAT_SQL + "'");
			el.setErrorCode("DATEFORMAT");
			throw el;
		}
		
		return val;
	}
	
	/**
	 * Chequea la Integridad Referencial de los campos de la tabla especificados como parámetros.
	 * @param con Objeto Connection con la Base de Datos.
	 * @param tableName Nombre de la tabla para comprobar la Integridad Referencial.
	 * @param keyfield Lista de campos componen la Integridad Referencial.
	 * @param logicalDelete Indica si la tabla acepta borrado lógico o no.
	 * @param schema Nombre del Schema de la Base de Datos.
	 */
	protected void checkDDBBIntegrity(Connection con, String tableName, Object[] keyfields, boolean logicalDelete, String schema) throws ClsExceptions 
	{
		Hashtable pkfor=new Hashtable();
		String hijos="";
		String paramexc="";
		String tablenameexc="";
		ResultSet ki=null;
		String where="";
		DatabaseMetaData dt=null;
		Statement stm=null;
		ResultSet rs0=null;
		
		try 
		{
			tablenameexc=tableName;
			
			dt=con.getMetaData();
			ki=dt.getExportedKeys("", schema, tableName);
			
			String sql="SELECT * ";
			String aux="";
			sql+=" FROM " + tableName + " ";
			where=buildSQLWhere(con, tableName, keyfields);
			sql+=where;
			
			stm=con.createStatement();
			rs0=stm.executeQuery(sql);
			/*if (!rs0.next()) {
			 ClsExceptions psscExaux = new ClsExceptions(tableName);
			 psscExaux.setErrorType("12");
			 psscExaux.setMsg("The record cannot be deleted. The record has been erased");
			 psscExaux.setParam(tablenameexc);
			 throw psscExaux;
			 }*/
			
			while (ki.next()) 
			{
				String name = (String)ki.getObject("FK_NAME");
				String tablaDes=ki.getObject("FKTABLE_NAME").toString();
				
				// esto no debería pasar, pero ...
				if (name==null) 
				{
					ClsLogging.writeFileLogWithoutSession("Integridad Referencial " + "Forein Key es null. Usando parámetro tablename. Chequear errores", 3);
					
					name = tablaDes;
				}
				
				key tkey=(key)pkfor.get(name);
				
				if (tkey==null)
				{
					tkey=new key();
				}
				
				pkfor.put(name,tkey);
				tkey.tableDestino=tablaDes;
				tkey.campoOrigen.add(rs0.getObject(ki.getObject("PKCOLUMN_NAME").toString()));
				tkey.campoDestino.add(ki.getObject("FKCOLUMN_NAME").toString());
				int order=ki.getInt("KEY_SEQ");
				
				if (tkey.campoOrigen.size()!=order) 
				{
					ClsLogging.writeFileLogWithoutSession("Integridad Referencial " + " order != vector.size", 3);
				}
			}
			
			ki.close();
		} 
		
		catch (Exception e) 
		{
			if (request==null) 
			{
				ClsLogging.writeFileLogWithoutSession("Violación de Integridad Referencial " + e.getMessage(),3);
			} 
			
			else 
			{
				ClsLogging.writeFileLog(e.getMessage(), this.request,3);
			}
			
			if (e instanceof ClsExceptions)
			{
				throw (ClsExceptions)e;
			}
			
			ClsExceptions psscExaux = new ClsExceptions(e, tableName);
			psscExaux.setErrorType("0");
			psscExaux.setMsg(e.getMessage());
			psscExaux.setParam(tablenameexc);
			
			throw psscExaux;
		} 
		
		finally 
		{
			try 
			{
				if (ki!=null)
				{
					ki.close();
				}
				
				if (rs0!=null)
				{
					rs0.close();
				}
				
				if (stm!=null)
				{
					stm.close();
				}
			} 
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		if (pkfor.size()==0)
		{
			return;
		}
		
		PreparedStatement pst=null;
		ResultSet rs1=null;
		
		try 
		{
			Enumeration ep = pkfor.keys();
			
			while (ep.hasMoreElements()) 
			{
				String name = (String)ep.nextElement();
				key tkey=(key)pkfor.get(name);
				boolean putDeletedField=false;
				
				if (logicalDelete) 
				{
					ResultSet rsLd = null;
					
					try 
					{
						rsLd = dt.getColumns("", schema, tkey.tableDestino, "DELETED");
						putDeletedField=rsLd.next();
					} 
					
					catch (Exception el) 
					{
						if (request==null) 
						{
							ClsLogging.writeFileLogWithoutSession("Violación de Integridad Referencial " + el.getMessage(), 3);
						} 
						
						else 
						{
							ClsLogging.writeFileLog(el.getMessage(), this.request, 3);
						}
						
						ClsExceptions psscExaux = new ClsExceptions(el, tableName);
						psscExaux.setErrorType("0");
						psscExaux.setMsg(el.getMessage());
						psscExaux.setParam(tableName);
						
						throw psscExaux;
					} 
					
					finally 
					{
						try 
						{
							if (rsLd!=null)
							{
								rsLd.close();
							}
						} 
						
						catch (Exception su) 
						{
							su.printStackTrace();
						}
					}
				}
				
				String sql2="SELECT COUNT(*) FROM "+ tkey.tableDestino + " WHERE ";
				String aux=" ";
				
				for (int h=0;h<tkey.campoDestino.size();h++) 
				{
					sql2+=(aux + tkey.campoDestino.elementAt(h) + " =?");
					aux=" AND ";
				}
				
				if (putDeletedField)
				{
					sql2+=" AND DELETED=0";
				}
				
				pst=con.prepareStatement(sql2);
				
				for (int h=0;h<tkey.campoDestino.size();h++) 
				{
					pst.setObject(h+1,tkey.campoOrigen.elementAt(h));
				}
				
				rs1=pst.executeQuery();
				
				if (rs1.next()) 
				{
					if (rs1.getInt(1)!=0) 
					{
						hijos+=("["+tkey.tableDestino+"("+tkey.campoDestino.toString()+")]");
						int indice = tkey.tableDestino.indexOf("_");
						
						if (!paramexc.equals(""))
						{
							paramexc+=" , ";
						}
						
						if (indice==-1)
						{
							paramexc+=tkey.tableDestino + " ";
						}
						
						else
						{
							paramexc+=tkey.tableDestino.substring(indice+5);
						}
					}
				}
			}
		} 
		
		catch (SQLException ev) 
		{
			if (request!=null) 
			{
				ClsLogging.writeFileLog(ev.getMessage(), this.request,3);
			} 
			
			else 
			{
				ClsLogging.writeFileLogWithoutSession("Violación de Integridad Referencial " + ev.getMessage(), 3);
			}
			
			ClsExceptions psscExaux = new ClsExceptions(ev,tableName);
			psscExaux.setErrorType("0");
			psscExaux.setMsg("El registro no puede ser alterado. Violación de Integridad Referencial");
			psscExaux.setParam(ev.getMessage());
			
			throw psscExaux;
		} 
		
		finally 
		{
			try 
			{
				if (rs1!=null)
				{
					rs1.close();
				}
				
				if (pst!=null)
				{
					pst.close();
				}
			} 
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		if (hijos.length()!=0) 
		{
			String errMsg="checkDDBBIntegrity: el registro("+where+") de la tabla("+tableName+") no puede ser alterado. " + "Foreing key(s) "+hijos;
			
			if (request!=null) 
			{
				ClsLogging.writeFileLog(errMsg, this.request,3);
			} 
			
			else 
			{
				ClsLogging.writeFileLogWithoutSession(errMsg,3);
			}
			
			ClsExceptions psscExaux = new ClsExceptions(tableName);
			psscExaux.setErrorType(logicalDelete?"16":"17");
			psscExaux.setMsg("El registro no puede ser alterado. Violación de Integridad Referencial");
			psscExaux.setParam(tablenameexc + " : " + paramexc);
			
			throw psscExaux;
		}
	}
	
	/**
	 * Clase protegida de la clase Row.
	 */
	protected class key 
	{
		public String tableDestino;
		public Vector campoOrigen=new Vector();
		public Vector campoDestino=new Vector();
		
		/*public void setNewCampo(String origen, String destino) 
		 {
		 campoOrigen.add(origen);
		 campoDestino.add(destino);
		 }*/
	}
	
	/**
	 * Devuelve un Vector con las claves de la tabla especificada como parámetro.
	 * @param tableName Nombre de la tabla para comprobar la Integridad Referencial.
	 * @throws ClsExceptions
	 */
	public Vector getKeys(String tablename) throws ClsExceptions 
	{
		Vector ret=new Vector();
		Connection con=null;
		ResultSet ki=null;
		
		try 
		{
			DatabaseMetaData dt = null;
			con = ClsMngBBDD.getConnection();
			dt = con.getMetaData();
			ki = dt.getPrimaryKeys("", schemasAvailables[0], tablename);
			Hashtable pkfor=new Hashtable();
			
			while (ki.next()) 
			{
				String name = (String)ki.getObject("PK_NAME");
				String field=ki.getObject("COLUMN_NAME").toString();
				
				//esto no debería pasar, pero ...
				if (name==null) 
				{
					ClsLogging.writeFileLogWithoutSession("Primary key no encontrada " + "Usando parámetro tablename. Chequear errores", 3);
					name = tablename;
				}
				
				key tkey=(key)pkfor.get(name);
				
				if (tkey==null) 
				{
					tkey = new key();
					pkfor.put(name, tkey);
				}
				
				tkey.campoOrigen.add(field);
				int order=ki.getInt("KEY_SEQ");
				
				if (tkey.campoOrigen.size()!=order) 
				{
					ClsLogging.writeFileLogWithoutSession("Integridad de Primary key " + " order != vector.size", 3);
				}
			}
			
			ki.close();
			ki=null;
			Enumeration e=pkfor.elements();
			
			while (e.hasMoreElements()) 
			{
				ret.add(((key)e.nextElement()).campoOrigen.toArray());
			}
		} 
		
		catch (Exception e) 
		{
			throw new ClsExceptions(e,e.getMessage());
		} 
		
		finally 
		{
			try 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				
				if (ki != null)
				{
					ki.close();
				}
			} 
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	/**
	 * Compara una Hashtable con una consulta formada con la tabla y los campos especificados como parámetros.
	 * @param tableName Nombre de la tabla.
	 * @param keyfields Lista con los campos clave.
	 * @param request Petición Request.
	 * @param htoriginal Hashtable a comparar.
	 * @return Vector Nulo si los registros son iguales. En otro caso, devuelve los elementos que diferencian.
	 * @throws ClsExceptions
	 */
	public Vector compareBeforeOperation(String tableName, Object[] keyfields, HttpServletRequest request, Hashtable htoriginal) throws ClsExceptions
	{
		Vector vectOut=null;
		Hashtable htend = new Hashtable();
		Connection con = null;
		Statement st = null;
		
		
		if (htoriginal==null)
		{
			ClsLogging.writeFileLog("(COMPARACIÓN) La Hashtable original está vacía", this.request, 3);
			ClsExceptions psscExorigin = new ClsExceptions("(COMPARACIÓN) La Hashtable original está vacía");
			psscExorigin.setErrorType("0");
			psscExorigin.setMsg("(COMPARACIÓN) La Hashtable original está vacía");
			psscExorigin.setParam("Comparando integridad de datos");
			psscExorigin.setErrorCode("UPDCOMPARE");
			throw psscExorigin;
		}
		
		try {
		con = ClsMngBBDD.getReadConnection();
		Enumeration enumer =htoriginal.keys();
		String ele="";
		String auxst="";
		
		Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
		
		while (enumer.hasMoreElements())
		{
			ele=(String)enumer.nextElement();
			
			if (dataTypes.get(ele).equals("DATE")) 
			{
				ele = "TO_CHAR(" + ele + ", '" + ClsConstants.DATE_FORMAT_SQL +  "') " + ele;
			}
			
			if (auxst.equals(""))
			{
				auxst=auxst+ele;
			}
			
			else
			{
				auxst=auxst+", "+ele;
			}
		}
		
		String queryPrev = "SELECT "+auxst+" FROM "+tableName+" "+buildSQLWhere(con, tableName, keyfields);
		ClsLogging.writeFileLog(queryPrev, request, 10);
		
		Statement sta = null;
		
		try
		{
			sta=con.createStatement();
			ResultSet rs= sta.executeQuery(queryPrev);
			
			if (rs.next())
			{
				Enumeration enumm =htoriginal.keys();
				String elem="";
				String val="";
				
				while (enumm.hasMoreElements())
				{
					elem=(String)enumm.nextElement();
					val=(String)rs.getString(elem);
					
					if (val==null)
					{
						val="";
					}
					
					htend.put(elem,val);
				}
			}
			
			else
			{
				ClsLogging.writeFileLog("(COMPARACIÓN) La consulta no ha devuelto registros", request, 3);
				Vector v = new Vector();
				v.addElement("");
				//psscExrow = new ClsExceptions("(COMPARE) NO RECORDS RETURNED BY QUERY","","0","GEN00","21");        psscExrow.setErrorType("0");
				// throw psscExrow;
				
				return v;
			}
		} 
		
		catch (SQLException ex) 
		{
			ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
			psscEx.setErrorType("9");
			psscEx.setParam(tableName);
			psscEx.setErrorCode("UPDCOMPARE");
			throw psscEx;
		} /*
		catch (ClsExceptions ex) {
		throw ex;
		}*/
		catch (Exception exx) 
		{
			ClsExceptions psscExx = new ClsExceptions(exx,exx.getMessage().substring(0, exx.getMessage().length() - 1));
			psscExx.setErrorType("9");
			psscExx.setParam(tableName);
			psscExx.setErrorCode("UPDCOMPARE");
			throw psscExx;
		} 
		
		finally 
		{
			try 
			{
				if (st != null)
				{
					st.close();
				}
				
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
					con=null;
				}
			} 
			
			catch (SQLException exc) 
			{
				if (con != null)
				{
					ClsMngBBDD.closeConnection(con);
				}
				
				ClsExceptions psscEx = new ClsExceptions(exc,exc.getMessage().substring(0, exc.getMessage().length() - 1));
				psscEx.setErrorType("9");
				psscEx.setParam(tableName);
				
				throw psscEx;
			}
		}
		} finally {
			if (con != null) {
				ClsMngBBDD.closeConnection(con);
				con=null;
			}
		}
		/**************************/
		/*  Check Data Integrity  */
		/**************************/
		
		Vector vect=null;
		
		if (htend==null)
		{
			ClsLogging.writeFileLog("(COMPARACIÓN) La Hashtable final está vacía", this.request, 3);
			ClsExceptions psscExrow = new ClsExceptions("(COMPARE) La Hashtable final está vacía");
			psscExrow.setErrorType("0");
			psscExrow.setMsg("(COMPARACIÓN) La Hashtable final está vacía");
			psscExrow.setParam("Comparando integridad de datos");
			psscExrow.setErrorCode("UPDCOMPARE");
			throw psscExrow;
		}
		
		try
		{
			ClsMngData mngData = new ClsMngData();
			vect=mngData.compareHashtables(htoriginal,htend,this.request);
		}
		
		catch (ClsExceptions ex) 
		{
			int numError= new Integer(ex.getMsg()).intValue();
			
			switch (numError)
			{
			case 0:
			{
				ClsLogging.writeFileLog("(COMPARACIÓN) Error de tamanhos de las Hashtables", this.request, 3);
				ClsExceptions psscEx = new ClsExceptions("(COMPARACIÓN) Error de tamanhos de las Hashtables");
				psscEx.setErrorType("0");
				psscEx.setMsg("(COMPARACIÓN) Error de tamanhos de las Hashtables");
				psscEx.setParam("Comparando integridad de datos");
				psscEx.setErrorCode("UPDCOMPARE");	
				throw psscEx;
			}
			
			case 1:
			{
				ClsLogging.writeFileLog("(COMPARACIÓN) Error de claves de las Hashtables", this.request, 3);
				ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Error de claves de las Hashtables");
				psscExaux.setErrorType("0");
				psscExaux.setMsg("(COMPARACIÓN) Error de claves de las Hashtables");
				psscExaux.setParam("Comparando integridad de datos");
				psscExaux.setErrorCode("UPDCOMPARE");
				throw psscExaux;
			}
			}
		}
		
		if (vect.size()>0)
		{
			Enumeration en=vect.elements();
			ClsLogging.writeFileLog("*******************", this.request, 7);
			ClsLogging.writeFileLog("Elementos cambiados:", this.request, 7);
			
			String values="";
			String val="";
			String key="";
			int num=0;
			
			while (en.hasMoreElements())
			{
				PairsKeys obj = (PairsKeys)en.nextElement();
				val=(String)obj.getValueObj();
				key=(String)obj.getIdObj();
				ClsLogging.writeFileLog("Clave: " + key,this.request, 7);
				ClsLogging.writeFileLog("Valor: " + val, this.request, 7);
				ClsLogging.writeFileLog("*******************", this.request, 7);
				
				if (num==0)
				{
					values= key+": "+val;
				}
				
				else
				{
					values = values + ", " + key + ": " + val;
				}
				
				num++;
			}
			
			vectOut=vect;
			
			// Prepare Exception
			ClsLogging.writeFileLog("(COMPARACIÓN) Los registros han cambiado: " + values, this.request, 3);
			ClsExceptions psscExaux = new ClsExceptions("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setErrorType("17");
			psscExaux.setMsg("(COMPARACIÓN) Los registros han cambiado: " + values);
			psscExaux.setParam(values);
			psscExaux.setErrorCode("UPDCOMPARE");
			throw psscExaux;
		}
		
		return vectOut;
	}
}