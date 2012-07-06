/**
 * <p>Title: RowsContainer </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
//import java.io.StringBufferInputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;



public class RowsContainer implements Serializable {
	/**
	 * To keep Table Name, needed in batch methods
	 */
	private String tableName;

	/**
	 * To keep the table fields, needed in batch methods
	 */
	private String[] fieldNames;

	/**
	 * To keep the format we want for date fields
	 */
	private String dateFormat = null;
    /**
     *  Will contain the Row Objects
     */
    protected Vector rows;
    /**
     *  Container size
     */
    protected int size = -1;
    /**
     * First row index at the SQL query
     */
    protected int firstRowIndex = 0;
    /**
     *  Vector inside positioning
     */
    protected int index = 0;

    /**
     *  MbRequest of the petition
     */
    private HttpServletRequest req = null;
    /**
     *  Constructor
     */
    public RowsContainer() {
    }

	/**
	 * Data obtaining
	 * @return the next object
	 */
    public Object get() {
      if (this.index < this.rows.size()){
          Object row = this.rows.get(this.index);
          this.index++;
          return row;
      } else
          return null;
    }

    public Object get(int idx) {
      if(idx < this.rows.size())
          return this.rows.get(idx);
      else
          return null;
    }

    public Vector getAll() {
        return this.rows;
    }

    public void first() {
      this.setIndex(0);
    }

/* 
 * 				RGG esto no se usa y confunde. Lo quito
 * 
    public boolean query(String sqlStatement, Vector params) throws ClsExceptions{
        Vector vRows=null;
        String element="";

        for(int i=0;i<(params.size()-1);i++) {
          element = element + (String)params.elementAt(i) + ",";
        }
        element = element + params.lastElement();
        sqlStatement = sqlStatement +" ORDER BY "+ element;

        return this.find(sqlStatement);

      }

    public boolean queryNLS(String sqlStatement, Vector params) throws ClsExceptions{
        Vector vRows=null;
        String element="";

        for(int i=0;i<(params.size()-1);i++) {
          element = element + (String)params.elementAt(i) + ",";
        }
        element = element + params.lastElement();
        sqlStatement = sqlStatement +" ORDER BY "+ element;

        return this.findNLS(sqlStatement);

      }
*/

	/**
	 * Executes the query
	 * Set the InfoBean object parameters.
     * Loads on Vector as many Row Objects as size allows
	 * @param sqlStatement The SELECT query to execute
	 * @return true if any row found
	 * @throws ClsExceptions
	 */
    public boolean query(String sqlStatement) throws ClsExceptions {
    	return this.find(sqlStatement);
    }

    public boolean queryBind(String sqlStatement, Hashtable data) throws ClsExceptions {
    	return this.findBind(sqlStatement,data);
    }

    public boolean queryForUpdate(String sqlStatement) throws ClsExceptions {
    	return this.findForUpdate(sqlStatement);
    }

    public boolean queryForUpdateBind(String sqlStatement, Hashtable data) throws ClsExceptions {
    	return this.findForUpdateBind(sqlStatement,data);
    }

    public boolean queryNLS(String sqlStatement) throws ClsExceptions {
    	return this.findNLS(sqlStatement);
    }

    public boolean queryNLSBind(String sqlStatement, Hashtable data) throws ClsExceptions {
    	return this.findNLSBind(sqlStatement, data);
    }

    /**
     * Executes the query
     * Set the InfoBean object parameters.
     * Loads on Vector as many Row Objects as size allows
     * @param sqlStatement The SELECT query to execute
     * @return true if any row found
     * @throws ClsExceptions
     */
     private boolean find(Connection con, String sqlStatement) throws ClsExceptions {
      	  ClsLogging.writeFileLog("SQL SELECT: "+ sqlStatement,req,10);
         getReady();
         Statement st = null;
         java.sql.ResultSet rs= null;
         int position = 0;
         boolean ok = false;
         boolean moreResults = true;
         Exception exc=null;
         try {
            st = con.createStatement();
            try {
               Date ini = new Date();
				rs = st.executeQuery(sqlStatement);

               Date fin = new Date();
				// Control de transacciones largas
				if ((fin.getTime()-ini.getTime())>3000) {
				    Date dat = Calendar.getInstance().getTime();
				    SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
				    String fecha = sdfLong.format(dat);
				    ClsLogging.writeFileLog(fecha + ",==> SIGA: Control de querys largas (>3 seg.),FIND,"+new Long((fin.getTime()-ini.getTime())).toString()+","+sqlStatement+","+new Long((fin.getTime()-ini.getTime())).toString(),10);
				}
            } catch (SQLException exce) {
            	rs = st.executeQuery(Row.validateWhereClause(sqlStatement));
            }

		   	if (rs != null) {
		   		Row auxiliar=Row.create(rs);
		   		ArrayList columAux=auxiliar.getColumnNames(rs);
		 	    String[] columnas=new String[columAux.size()];
		 	    for (int i=0;i<columAux.size();i++){
		 	    	columnas[i]=(String)columAux.get(i);
		 	    }
		 	    this.setFieldNames(columnas);
		 	    
  		   	    while (position < this.getFirstIndex()) {
			   	    if (rs.next()) {
			   	      position++;
			   	    } else {
			   	      moreResults = false;
			   	    }
			   	}
  		   	    if (moreResults) {
  		   	        while ((moreResults=rs.next()) && ((this.size == -1) ? true : this.rows.size() < this.size)) {
  		   	            ok = true;
  		   	            this.rows.add(Row.create(rs));
   	      
  		   	        }
  		   	    }
		   	} else {
		   	  ok = false;
		   	}
		   	
         } catch (Exception il) {
             exc=il;
             
         }finally {
		   	try {
		   	  if (rs != null) rs.close();
		   	  if (st != null) st.close();
		   	} catch (SQLException exce) {
		   	  throw new ClsExceptions(exce,exce.getMessage());
		   	}
		   	if (exc!=null) throw new ClsExceptions(exc,exc.getClass() + " Exception " + ": " + exc.getMessage() + " SQL:"+sqlStatement);
         }
         return ok;
      }
     
     /**
      * ATENCION: HAY QUE PONERLOS EN ORDEN Y SIN REPERTIR LOS INDICES!!!
      * 
      * @param con
      * @param sqlStatement
      * @param data
      * @return
      * @throws ClsExceptions
      */
     private boolean findBind(Connection con, String sqlStatement, Hashtable data) throws ClsExceptions {
     	  ClsLogging.writeFileLog("SQL BIND: "+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data),req,10);
        getReady();
        PreparedStatement pst = null;
        java.sql.ResultSet rs= null;
        int position = 0;
        boolean ok = false;
        boolean moreResults = true;
        Exception exc=null;
        try {
        	ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
        	String timeOut = properties.returnProperty("general.sql.timeout");

            pst = con.prepareStatement(sqlStatement);
            pst.setQueryTimeout(Integer.parseInt(timeOut));
	   	     Enumeration e = data.keys();
	   	     while (e.hasMoreElements()) {
	   	         Integer key = (Integer)e.nextElement();
	   	         Object objeto = (Object)data.get(key);
	   	         if(objeto instanceof String)
	   	        	 pst.setString(key.intValue(), (String)objeto);
	   	         else{
	   	        	objeto = String.valueOf(objeto);
	   	        	 pst.setString(key.intValue(), (String)objeto);
	   	         }
	   	         
	   	     }
            try {
                rs = pst.executeQuery();
                
            } catch (SQLException exce) {

            	if(exce.getMessage().contains("ORA-01013")){
            		throw new ClsExceptions(exce, "Error en find BIND: La consulta ha superado los " + pst.getQueryTimeout() + " segundos de ejecucion y ha sido cancelada. SQL:"+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data));
            	}else{
	           	 	// RGG control de errores
	           	 	throw new ClsExceptions(exce, "Error en find BIND: "+ exce.getMessage() + " SQL:"+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data));
            	}
            }

		  	if (rs != null) {
		  	  while (position < this.getFirstIndex()) {
		  	    if (rs.next()) {
		  	      position++;
		  	    } else {
		  	      moreResults = false;
		  	    }
		  	  }
		  	  if (moreResults) {
		  	    while ((moreResults=rs.next()) && ((this.size == -1) ? true : this.rows.size() < this.size)) {
		  	      ok = true;
		  	      this.rows.add(Row.create(rs));
		  	    }
		  	  }
		  	} else {
		  	  ok = false;
		  	}
		  	
        } catch (Exception il) {
            exc=il;
        }finally {
		  	try {
		  	  if (rs != null) rs.close();
		  	  if (pst != null) pst.close();
		  	} catch (SQLException exce) {
		  	  throw new ClsExceptions(exce,exce.getMessage());
		  	}
		  	
		  	if (exc!=null)
		  		throw new ClsExceptions(exc,exc.getMessage());
        }
        return ok;
      }

     private boolean findBindHashVacio(Connection con, String sqlStatement, Hashtable data) throws ClsExceptions {
     	  ClsLogging.writeFileLog("SQL BIND Hash Vacio: "+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data),req,10);
        getReady();
        PreparedStatement pst = null;
        java.sql.ResultSet rs= null;
        int position = 0;
        boolean ok = false;
        boolean moreResults = true;
        Exception exc=null;
        Hashtable auxiliar = null; 
        try {
        	ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
        	String timeOut = properties.returnProperty("general.sql.timeout");

            pst = con.prepareStatement(sqlStatement);
            pst.setQueryTimeout(Integer.parseInt(timeOut));        	
            Enumeration e = data.keys();
	   	    while (e.hasMoreElements()) {
	   	        Integer key = (Integer)e.nextElement();
	   	        pst.setString(key.intValue(), (String)data.get(key));
	   	    }
	   	    
            try {
                rs = pst.executeQuery();
            } catch (SQLException exce) {
            	if(exce.getMessage().contains("ORA-01013")){
            		throw new ClsExceptions(exce, "Error en find BIND: La consulta ha superado los " + pst.getQueryTimeout() + " segundos de ejecucion y ha sido cancelada. SQL:"+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data));
            	}else{
            		// RGG control de errores
           	 		throw new ClsExceptions(exce, "Error en find BIND: "+ exce.getMessage() + " SQL:"+ ClsMngBBDD.getSQLBindInformation(sqlStatement,data));
            	}
            }

		  	if (rs != null) {
		      // RGG
		      auxiliar = this.getHashVacio(rs);
		  	  
		      while (position < this.getFirstIndex()) {
		  	    if (rs.next()) {
		  	      position++;
		  	    } else {
		  	      moreResults = false;
		  	    }
		  	  }
		  	  
		      if (!rs.next()) {
		  	    moreResults = false;
		   	  }
		  	  
		  	  if (moreResults) {
		  	    while (((this.size == -1) ? true : this.rows.size() < this.size)) {
		  	      ok = true;
		  	      this.rows.add(Row.create(rs));
		  	      if (!rs.next()) {
		  	          break;
		  	      }
		  	    }
		  	  } else {
		  	      // RGG en lugar de devolver false, devuelvo el hash vacío
		  	  	  this.rows.add(Row.create(auxiliar));
		  	  	  ok=true;
		  	  }
		  	} else {
		  	  // RGG en lugar de devolver false, devuelvo el hash vacío
		  	  //ok = false;
		  	  this.rows.add(Row.create(auxiliar));
		  	  ok=true;
		  	}

        } catch (Exception il) {
        	exc=il;
        }finally {
		  	try {
		  	  if (rs != null) rs.close();
		  	  if (pst != null) pst.close();
		  	} catch (SQLException exce) {
		  	  throw new ClsExceptions(exce,exce.getMessage());
		  	}
		  	if (exc!=null)
		  		throw new ClsExceptions(exc,exc.getMessage());
        }
        return ok;
      }

  /**
   * Executes a SELECT statement, returning true if any record found.
   * Loads in Hashtable row the result of the query (when more than one
   * records, loads the first found)<br>
   * It gets the connection from the 'readOnly' connections pool.
   * @param sqlStatement The SELECT statement
   * @return true if any record found
   * @throws ClsExceptions
   */
    public boolean find (String sqlStatement) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getReadConnection();
          return find(con,sqlStatement);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
      }

    public boolean findBind (String sqlStatement, Hashtable data) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getReadConnection();
          return findBind(con,sqlStatement, data);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
      }

    public boolean findBindHashVacio (String sqlStatement, Hashtable data) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getReadConnection();
          return findBindHashVacio(con,sqlStatement, data);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
      }

    public boolean findForUpdateBind (String sqlStatement, Hashtable data) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getConnection();
          return findBind(con,sqlStatement, data);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
      }

    public boolean findNLS (String sqlStatement) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getNLSConnection();
          return find(con,sqlStatement);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
     }
    
    public boolean findNLSBind (String sqlStatement, Hashtable data) throws ClsExceptions {
        Connection con = null;
        try{
          con = ClsMngBBDD.getNLSConnection();
          return findBind(con,sqlStatement, data);
        }catch (ClsExceptions ex){
          throw ex;
        }finally{
          if (con != null) 
          {
              ClsMngBBDD.closeConnection(con);
              con=null;
          }
        }
      }
  /**
   * Executes a SELECT statement, returning true if any record found.
   * Loads in Hashtable row the result of the query (when more than one
   * records, loads the first found)<br>
   * It gets the connection from the 'modify' connections pool.
   * @param sqlStatement The SELECT statement
   * @return true if any record found
   * @throws ClsExceptions
   */
  public boolean findForUpdate (String sqlStatement) throws ClsExceptions {
    Connection con = null;
   try{
      con = ClsMngBBDD.getConnection();
      return find(con,sqlStatement);
    }catch (ClsExceptions ex){
      throw ex;
    }finally{
      if (con != null)
      {
          ClsMngBBDD.closeConnection(con);
          con=null;
      }
    }
  }
  
  /**
   * Executes a SELECT statement, returning true if any record found.
   * Loads in Hashtable row the result of the query (when more than one
   * records, loads the first found)<br>
   * It gets the connection from the 'modify' connections pool.
   * @param sqlStatement The SELECT statement
   * @return true if any record found
   * @throws ClsExceptions
   */
  public boolean findForUpdateNLS (String sqlStatement) throws ClsExceptions {
    Connection con = null;
   try{
      con = ClsMngBBDD.getNLSConnection();
      return find(con,sqlStatement);
    }catch (ClsExceptions ex){
      throw ex;
    }finally{
      if (con != null)
      {
          ClsMngBBDD.closeConnection(con);
          con=null;
      }
    }
  }
    /**
     *  Get an String with the name of the table where the
     *  rows are going to be inserted.
     *  @return String with table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     *  Set an String with the name of the table where the
     *  rows are going to be inserted.
     *  @param s String with table name
     */
    public void setTableName(String s) {
        this.tableName = s;
    }

	/**
     *  Get an String array containing the field names of the table where the
     *  rows are going to be inserted.
     *  @return array of Strings with field names
     */
    public String[] getFieldNames() {
        return this.fieldNames;
    }

    /**
     *  Set an String array containing the fields names of the table where the
     *  rows are going to be inserted.
     *  @param s array of Strings with field names
     */
    public void setFieldNames(String[] s) {
        this.fieldNames = s;
    }


    /**
     *  Insert all rows into the database in using a batch statement.
     * @param tableName String with the table name.
     * @param fieldNames String[] containing the names of the fields to be inserted
     *                   in database.
     * @return Total number of inserted records
     * @throws ClsExceptions
     */
    public int insertAllBatch(String tableName, String[] fieldNames) throws ClsExceptions {
		this.setTableName(tableName);
		this.setFieldNames(fieldNames);
		return insertAllBatch();
    }

	/**
     *  Insert all rows into the database in using a batch statement.
	 * Before use this method, it's needed having set attributes tableName,
	 * fieldNames, and dateFormat
	 * @return Total number of inserted records
	 * @throws ClsExceptions
	 */
    public int insertAllBatch() throws ClsExceptions {
    	int insertedRecords = 0;
		Connection con = null;
        Statement stmt = null;
        String sql = null;
        try {
			con = ClsMngBBDD.getConnection();
			stmt = con.createStatement();

            Row referenceRow = null;
            int batchCount = 0;

            Enumeration en = rows.elements();
            while (en.hasMoreElements()) {
				Row r = (Row) en.nextElement();
                if (r == null) {
                    continue;
                }
                if (referenceRow == null) {
                    referenceRow = r;
                } else {
                    //first time, loads datatypes from database: later, it's not necessary
                    if (referenceRow.getDataTypes(tableName) != null) {
                        r.setDataTypes(tableName, referenceRow.getDataTypes(tableName));
                    }
                }

				sql = r.buildInsertStatement(con, tableName, fieldNames);
                ClsLogging.writeFileLog("SQL INSERT: "+sql,10);
				stmt.addBatch(sql);
                batchCount++;
            }

            int [] updateCounts = stmt.executeBatch();
			for (int i = 0; i < updateCounts.length; i ++) {
				if (updateCounts[i] >= 0) {
					insertedRecords += updateCounts[i];
				} else {
                    //A value of -2 -- indicates that the command was processed
                    //successfully but that the number of rows affected is unknown
                    //otherwise, an error produced!
                    if (updateCounts[i] == -2) {
                        insertedRecords += 1; //AT LEAST ONE RECORD INSERTED PER STATEMENT
                    } else {
                        insertedRecords = updateCounts[i];
                        break;
                    }
				}
			}
        } catch(BatchUpdateException b) {
       	  ClsLogging.writeFileLogError("Error en insert: "+ b.getMessage()+ " SQL:"+sql,b,3);

        	String msg = getClass()+" Batch update failed: " + b.getMessage();
/*            int [] updateCounts = b.getUpdateCounts();
            for (int  i = 0; i < updateCounts.length; i ++) {
                msg = msg + updateCounts[i] + "  ";
            }*/
			ClsExceptions psscEx = new ClsExceptions(msg);
			psscEx.setErrorType("7");
			psscEx.setParam(tableName);
            throw psscEx;
        } catch (SQLException ex) {
         	  ClsLogging.writeFileLogError("Error en insert: "+ ex.getMessage() + " SQL:"+sql,ex,3);
			ClsExceptions psscEx = new ClsExceptions(ex.getMessage());
			psscEx.setErrorType("7");
			psscEx.setParam(tableName);
            ClsLogging.writeFileLog(ex.toString(), req, 1);
            throw psscEx;
        } finally {
			try {
				if (stmt != null) stmt.close();
			} catch (SQLException ex) {
				stmt = null;
			}
			if (con != null) {
				ClsMngBBDD.closeConnection(con);
				con=null;
			}
        }
        return insertedRecords;
    }



   //***************************************************
   //**************** Access methods ******************
   //**************************************************/

    public int getMaxSize() {
      return this.size;
    }
    public void setMaxSize(int size) {
       this.size = size;
    }

    public int size(){
      if (this.rows == null)
          return 0;
      else
          return this.rows.size();
    }

    public int getIndex () {
       return this.index;
    }
    public void setIndex(int index) {
       this.index = index;
    }

    public int getFirstIndex () {
       return this.firstRowIndex;
    }

    public void setFirstIndex(int firstRow) {
        this.firstRowIndex = firstRow;
    }

    public int getLastIndex () {
      return this.firstRowIndex + ((this.size == -1) ? 0 : this.size);
    }

    //***************************************************
    //****************  Accesory methods ****************
    //***************************************************
    private Object loadRow (Class clase, ResultSet rs) {
      try {
         Object row = clase.newInstance();
         java.lang.reflect.Method m = clase.getMethod("load", new Class[] {Class.forName("java.sql.ResultSet")});
         m.invoke(row, new Object[] {rs});
         return row;
      } catch (Exception ex) {
          return null;
      }
    }

    public void initialize(HttpServletRequest req) {
       this.getReady();
       this.req = req;
    }

    public void getReady() {
		if (this.rows == null) {
			this.rows = (getMaxSize() == -1) ? new Vector() : new Vector(getMaxSize());
		}
    }

	public void addRecord(Row r) {
		if (rows == null) {
			rows = new Vector();
		}
		rows.add(r);
	}

	public void addRecord(int index, Row r) {
		if (rows == null) {
			rows = new Vector();
		}
		if (index > rows.size()) {
			rows.setSize(index + 1);
		}
		rows.add(index, r);
	}

	public void removeRecord(int index) {
		if (rows != null) {
			if (index < rows.size()) {
				rows.remove(index);
			}
		}
	}

	public void setSize(int size) {
		if (rows == null) {
			rows = new Vector();
		}
		rows.setSize(size);
	}

	/** 
     * Actualiza el valor de un campo blob
	 * @param tableName
	 * @param whereSentence
	 * @param nombreCampo
	 * @param texto
	 * @throws ClsExceptions
	 */
	public void updateBlob (String tableName, String whereSentence, String nombreCampo, InputStream is) throws ClsExceptions {
	    Connection con = null;
	   try{

	   	  con = ClsMngBBDD.getConnection();
	      MngBlob.updateBlobField(con,tableName,whereSentence,nombreCampo,is);
		  
	    }catch (ClsExceptions ex){
	      throw ex;
	    }finally{
	      if (con != null)
	      {
	          ClsMngBBDD.closeConnection(con);
	          con=null;
	      }
	    }
	  }
	
	/**
     * Obtiene el valor de un campo blob
	 * @param tableName
	 * @param nombreCampo
	 * @param select
	 * @return
	 * @throws ClsExceptions
	 */
	public OutputStream getBlob (String tableName, String nombreCampo, String select) throws ClsExceptions {
	    Connection con = null;
	    OutputStream os = null;
	    
	   try{
	   	  con = ClsMngBBDD.getConnection();
	      os = MngBlob.getBlobToStream(con,nombreCampo,select);
		  
	    }catch (ClsExceptions ex){
	      throw ex;
	    }finally{
	      if (con!=null)
	      {
	          ClsMngBBDD.closeConnection(con);
	          con=null;
	      }
	    }
	    return os;
	}
	/** 
     * Actualiza el valor de un campo clob
	 * @param tableName
	 * @param whereSentence
	 * @param nombreCampo
	 * @param texto
	 * @throws ClsExceptions
	 */
	public void updateClob (String tableName, String whereSentence, String nombreCampo, String texto) throws ClsExceptions {
	    Connection con = null;
	   try{

	   	  con = ClsMngBBDD.getConnection();

	      MngClob.updateClobField(con,tableName,whereSentence,nombreCampo,texto);
		  
	    }catch (ClsExceptions ex){
	      throw ex;
	    }finally{
	      if (con != null)
	      {
	          ClsMngBBDD.closeConnection(con);
	          con=null;
	      }
	    }
	  }
	
    /** 
     * Obtiene el valor de un campo clob
	 * @param tableName
	 * @param whereSentence
	 * @param nombreCampo
	 * @param texto
	 * @throws ClsExceptions
     * @throws UnsupportedEncodingException 
	 */
	public String getClob (String tableName, String nombreCampo, String select) throws ClsExceptions, UnsupportedEncodingException {
	    Connection con = null;
	    ByteArrayOutputStream os = null;
	    String salida ="";
	   try{
	   	  con = ClsMngBBDD.getConnection();
	      os = MngClob.getClobToStream(con,nombreCampo,select);
		  try {
			salida = os.toString("ISO-8859-15");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			throw e;
		}
	    }catch (ClsExceptions ex){
	      throw ex;
	    }finally{
	      if (con!=null)
	      {
	          ClsMngBBDD.closeConnection(con);
	          con=null;
	      }
	    }
	    return salida;
	}

    public Hashtable getHashVacio (ResultSet rs) throws ClsExceptions {
        Hashtable ht = new Hashtable();
        ResultSetMetaData rsmd = null; 
        try{
          rsmd = rs.getMetaData();
          for (int f=1;f<=rsmd.getColumnCount();f++) {
        	  String label = rsmd.getColumnLabel(f);
        	  ht.put(label, " ");
          }
        }catch (Exception ex){
          throw new ClsExceptions(ex,"Error obteniendo metadata");
        }
        return ht;
      }


}