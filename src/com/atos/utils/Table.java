package com.atos.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;


public class Table {

  private static final String LOAD_METHOD_NAME = "setData";

  private String tableName = null;
  private String rowClassName = null;
  private Hashtable htFilters = null;
  private HttpServletRequest req= null;

  public Table() {
    this.tableName="";
    this.rowClassName="";
    this.htFilters=new Hashtable();
    this.req=null;
  }


  public Table(HttpServletRequest request, String table,String row, Hashtable ht) {
    this.tableName= table;
    this.rowClassName= row;
    this.htFilters=ht;
    this.req = request;
  }


  public Table(HttpServletRequest request, String table,String rowClass) {
    this.tableName= table;
    this.rowClassName= rowClass;
    this.htFilters= new Hashtable();
    this.req = request;
    this.tableName= table;
    this.rowClassName= rowClass;
    this.htFilters= new Hashtable();
    this.req = request;
  }

  public void setReq(HttpServletRequest request){
    this.req= request;
  }

  public HttpServletRequest getReq(){
    return this.req;
  }

  public void setTableName(String table){
    this.tableName= table;
  }

  public String getTableName(){
    return this.tableName;
  }

  public void setRowClassName(String rowClass){
    this.rowClassName= rowClass;
  }

  public String getRowClassName(){
    return this.rowClassName;
  }

  public void setHtFilters(Hashtable ht){
    this.htFilters=ht;
  }

  public Hashtable getHtFilters(){
    return this.htFilters;
  }

  public void addFilter(String key, Object value){
    this.htFilters.put(key,value);
  }

  private void eraseBlankValues(){
    Hashtable htFiltersFiltered=new Hashtable();
    Enumeration e=this.htFilters.keys();

    while(e.hasMoreElements()){
      String key=(String) e.nextElement();
          Object obj = this.htFilters.get(key);
          String value=obj.toString().trim();

      if (!value.equals("")){
        htFiltersFiltered.put(key,value);
      }
    }
    htFilters=htFiltersFiltered;
  }

  /**
   * Each element of the vector must be a String with the next sintax: "columnName asc".
   * The whitespace between the columnName and the type-of-order (asc or desc) is mandatory
   * @param params
   * @return Vector
   * @throws ClsExceptions
   */

  public Vector search(Vector params) throws ClsExceptions{
    String sql=null;
    Vector vRows=null;
    RowsContainer rowsContainer=new RowsContainer();
    String element="";

      for(int i=0;i<(params.size()-1);i++) {
        element = element + (String)params.elementAt(i) + ",";
      }
      element = element + params.lastElement();
      String sqlStatementOrder = " ORDER BY "+ element;

    Connection con = null;

    sql="select * from "+ this.tableName;

    /* The hashtable htFilters must be filtered firstly
       to erase key fields which has a or empty value
     */
    eraseBlankValues();


    try {
    /*We apply filters if there are filters to be applied*/
    if(!this.htFilters.isEmpty()){
      con = ClsMngBBDD.getReadConnection();
      sql += buildSQLWhere(con, tableName, htFilters.keySet().toArray());
      ClsMngBBDD.closeConnection(con);
      con=null;
    }

    //Compose with "ORDER BY" clause
    sql=sql + sqlStatementOrder;

      ClsLogging.writeFileLog(sql,req,3);
      if (rowsContainer.query(sql)) {
        vRows = new Vector();
        Enumeration result= rowsContainer.getAll().elements();
        while (result.hasMoreElements()){
          Row data = (Row) result.nextElement();
          // An array with the class constructor parameter types is filled
          Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
          // The class constructor is obtained.
          Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
          /* The class becomes an object and its constructor is used, filled with
             the appropiate values */
          Object classObject = classConstructor.newInstance(new Object[] {req});
          // An array with a class method parameter types is filled
          Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
          // The method is filled with the appropiate values
          Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
          /* The method is now ready to be used and it is invoked. This method must be able
             to fill a hashtable for the class referenced by the variable "rowClassName"*/
          m.invoke(classObject, new Object[] {data.getRow()});
          // The filled object is added to a vector
          vRows.addElement(classObject);
        }
      }
    }catch(Exception sqlE){
      ClsLogging.writeFileLog("Table.search(params): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
      throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
    }
    finally{
      //close connection
      try {
          if (con != null){
            ClsMngBBDD.closeConnection(con);
            con = null;
          }
      } catch (ClsExceptions ex) {
          con = null;
          throw new ClsExceptions(ex,ex.getMessage(),"param","Table.search()","errorCategory","errorType");
      }
    }

     return vRows;
  }

  /**
   * Searching with filters
   * @return Vector
   * @throws ClsExceptions
   */

  public Vector search() throws ClsExceptions{
    String sql=null;
    Vector vRows=null;
    RowsContainer rowsContainer=new RowsContainer();
    Connection con = null;

    sql="select * from "+ this.tableName;

    /* The hashtable htFilters must be filtered firstly
       to erase key fields which has a or empty value
     */
    eraseBlankValues();


    try {
    /*We apply filters if there are filters to be applied*/
    if(!this.htFilters.isEmpty()){
      con = ClsMngBBDD.getReadConnection();
      sql += buildSQLWhere(con, tableName, htFilters.keySet().toArray());
      ClsMngBBDD.closeConnection(con);
      con=null;
    }

      ClsLogging.writeFileLog(sql,req,3);
      if (rowsContainer.query(sql)) {
        vRows = new Vector();
        Enumeration result= rowsContainer.getAll().elements();
        while (result.hasMoreElements()){
          Row data = (Row) result.nextElement();
          // An array with the class constructor parameter types is filled
          Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
          // The class constructor is obtained.
          Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
          /* The class becomes an object and its constructor is used, filled with
             the appropiate values */
          Object classObject = classConstructor.newInstance(new Object[] {req});
          // An array with a class method parameter types is filled
          Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
          // The method is filled with the appropiate values
          Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
          /* The method is now ready to be used and it is invoked. This method must be able
             to fill a hashtable for the class referenced by the variable "rowClassName"*/
          m.invoke(classObject, new Object[] {data.getRow()});
          // The filled object is added to a vector
          vRows.addElement(classObject);
        }
      }
    }catch(Exception sqlE){
      ClsLogging.writeFileLog("Table.search(): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
      throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
    }
    finally{
      //close connection
      try {
          if (con != null){
            ClsMngBBDD.closeConnection(con);
            con = null;
          }
      } catch (ClsExceptions ex) {
          con = null;
          throw new ClsExceptions(ex,ex.getMessage(),"param","Table.search()","errorCategory","errorType");
      }
    }

     return vRows;
  }

  /**
	 * Searching with filters
	 * @param boolean pool: true the write pool will be used
	 * 						false the reader poll will be used
	 * @return Vector
	 * @throws ClsExceptions
	 */

	public Vector search(boolean pool) throws ClsExceptions{
	  String sql=null;
	  Vector vRows=null;
	  RowsContainer rowsContainer=new RowsContainer();
	  Connection con = null;

	  sql="select * from "+ this.tableName;

	  /* The hashtable htFilters must be filtered firstly
		 to erase key fields which has a or empty value
	   */
	  eraseBlankValues();


	  try {
	  /*We apply filters if there are filters to be applied*/
	  if(!this.htFilters.isEmpty()){
		if (pool)
			con = ClsMngBBDD.getConnection();
		else
			con = ClsMngBBDD.getReadConnection();

		sql += buildSQLWhere(con, tableName, htFilters.keySet().toArray());
		ClsMngBBDD.closeConnection(con);
		con=null;
	  }

		ClsLogging.writeFileLog(sql,req,3);

		boolean isThereElements=false;
		if (pool)
				isThereElements=rowsContainer.findForUpdate(sql);
		else
				isThereElements=rowsContainer.query(sql);
		if (isThereElements) {
		  vRows = new Vector();
		  Enumeration result= rowsContainer.getAll().elements();
		  while (result.hasMoreElements()){
			Row data = (Row) result.nextElement();
			// An array with the class constructor parameter types is filled
			Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
			// The class constructor is obtained.
			Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
			/* The class becomes an object and its constructor is used, filled with
			   the appropiate values */
			Object classObject = classConstructor.newInstance(new Object[] {req});
			// An array with a class method parameter types is filled
			Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
			// The method is filled with the appropiate values
			Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
			/* The method is now ready to be used and it is invoked. This method must be able
			   to fill a hashtable for the class referenced by the variable "rowClassName"*/
			m.invoke(classObject, new Object[] {data.getRow()});
			// The filled object is added to a vector
			vRows.addElement(classObject);
		  }
		}
	  }catch(Exception sqlE){
	  	ClsLogging.writeFileLog("Table.search(): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
	  	throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
	  }
	  finally{
		//close connection
		try {
			if (con != null){
			  ClsMngBBDD.closeConnection(con);
			  con = null;
			}
		} catch (ClsExceptions ex) {
			con = null;
			throw new ClsExceptions(ex,ex.getMessage(),"param","Table.search()","errorCategory","errorType");
		}
	  }

	   return vRows;
  }

  /**
   * Searching with query
   * @param sqlStatement
   * @return Vector
   * @throws ClsExceptions
   */

  public Vector search(String sqlStatement) throws ClsExceptions{
    Vector vRows=null;
    RowsContainer rowsContainer=new RowsContainer();

    try {
      if (rowsContainer.query(sqlStatement)) {
        vRows = new Vector();
        Enumeration result= rowsContainer.getAll().elements();
        while (result.hasMoreElements()){
          Row data = (Row) result.nextElement();
          // An array with the class constructor parameter types is filled
          Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
          // The class constructor is obtained.
          Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
          /* The class becomes an object and its constructor is used, filled with
             the appropiate values */
          Object classObject = classConstructor.newInstance(new Object[] {req});
          // An array with a class method parameter types is filled
          Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
          // The method is filled with the appropiate values
          Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
          /* The method is now ready to be used and it is invoked. This method must be able
             to fill a hashtable for the class referenced by the variable "rowClassName"*/
          m.invoke(classObject, new Object[] {data.getRow()});
          // The filled object is added to a vector
          vRows.addElement(classObject);
        }
      }
    }catch(Exception sqlE){
      ClsLogging.writeFileLog("Table.search(sqlStatement): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
      throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
    }
     return vRows;
  }

  public Vector searchForUpdate(String sqlStatement) throws ClsExceptions{
    Vector vRows=null;
    RowsContainer rowsContainer=new RowsContainer();

    try {
      if (rowsContainer.findForUpdate(sqlStatement)) {
        vRows = new Vector();
        Enumeration result= rowsContainer.getAll().elements();
        while (result.hasMoreElements()){
          Row data = (Row) result.nextElement();
          // An array with the class constructor parameter types is filled
          Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
          // The class constructor is obtained.
          Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
          /* The class becomes an object and its constructor is used, filled with
             the appropiate values */
          Object classObject = classConstructor.newInstance(new Object[] {req});
          // An array with a class method parameter types is filled
          Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
          // The method is filled with the appropiate values
          Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
          /* The method is now ready to be used and it is invoked. This method must be able
             to fill a hashtable for the class referenced by the variable "rowClassName"*/
          m.invoke(classObject, new Object[] {data.getRow()});
          // The filled object is added to a vector
          vRows.addElement(classObject);
        }
      }
    }catch(Exception sqlE){
        ClsLogging.writeFileLog("Table.search(): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
        throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
    }

     return vRows;
  }

  public Vector searchForUpdateNLS(String sqlStatement) throws ClsExceptions{
    Vector vRows=null;
    RowsContainer rowsContainer=new RowsContainer();

    try {
      if (rowsContainer.findForUpdateNLS(sqlStatement)) {
        vRows = new Vector();
        Enumeration result= rowsContainer.getAll().elements();
        while (result.hasMoreElements()){
          Row data = (Row) result.nextElement();
          // An array with the class constructor parameter types is filled
          Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
          // The class constructor is obtained.
          Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
          /* The class becomes an object and its constructor is used, filled with
             the appropiate values */
          Object classObject = classConstructor.newInstance(new Object[] {req});
          // An array with a class method parameter types is filled
          Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
          // The method is filled with the appropiate values
          Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
          /* The method is now ready to be used and it is invoked. This method must be able
             to fill a hashtable for the class referenced by the variable "rowClassName"*/
          m.invoke(classObject, new Object[] {data.getRow()});
          // The filled object is added to a vector
          vRows.addElement(classObject);
        }
      }
    }catch(Exception sqlE){
        ClsLogging.writeFileLog("Table.search(): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
        throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
    }

     return vRows;
  }
  
  /**
   * Each element of the vector must be a String with the next sintax: "columnName asc".
   * The whitespace between the columnName and the type-of-order (asc or desc) is mandatory
   * @param sqlStatement
   * @param params
   * @return Vector
   * @throws ClsExceptions
   */
  public Vector search(String sqlStatement, Vector params) throws ClsExceptions{
      Vector vRows=null;
      String element="";
      RowsContainer rowsContainer=new RowsContainer();

      for(int i=0;i<(params.size()-1);i++) {
        element = element + (String)params.elementAt(i) + ",";
      }
      element = element + params.lastElement();
      sqlStatement = sqlStatement +" ORDER BY "+ element;


      try {
        if (rowsContainer.query(sqlStatement)) {
          vRows = new Vector();
          Enumeration result= rowsContainer.getAll().elements();

          while (result.hasMoreElements()){
            Row data = (Row) result.nextElement();
            // An array with the class constructor parameter types is filled
            Class[] constructorParams = new Class[] {Class.forName("javax.servlet.http.HttpServletRequest")};
            // The class constructor is obtained.
            Constructor classConstructor = Class.forName(rowClassName).getConstructor(constructorParams);
            /* The class becomes an object and its constructor is used, filled with
               the appropiate values */
            Object classObject = classConstructor.newInstance(new Object[] {req});
            // An array with a class method parameter types is filled
            Class[] methodParams = new Class[] {Class.forName("java.util.Hashtable")};
            // The method is filled with the appropiate values
            Method m = Class.forName(rowClassName).getMethod(LOAD_METHOD_NAME, methodParams);
            /* The method is now ready to be used and it is invoked. This method must be able
               to fill a hashtable for the class referenced by the variable "rowClassName"*/
            m.invoke(classObject, new Object[] {data.getRow()});
            // The filled object is added to a vector
            vRows.addElement(classObject);
          }
        }
      }catch(Exception sqlE){
        ClsLogging.writeFileLog("Table.search(): ERROR in sql statement at"+ this.tableName + ", Desc: "+sqlE.toString(),req,1);
        throw new ClsExceptions(sqlE,sqlE.getMessage(),"param","Table.search()","errorCategory","errorType");
      }
      return vRows;
    }

  public String replacePattern(String str, String pattern, String replace) {
          int s = 0;
          int e = 0;
          StringBuffer result = new StringBuffer();

          while ((e = str.indexOf(pattern, s)) >= 0) {
              result.append(str.substring(s, e));
              result.append(replace);
              s = e+pattern.length();
          }
          result.append(str.substring(s));
          return result.toString();
      }


   private String validateChars(Object cad) {
     String aux=""+cad;
     String cadaux="";

     cadaux=replacePattern(aux,"'","''");

     return cadaux;
    }


  public String buildSQLWhere(Connection con, String tableName, Object[] keyfields) throws ClsExceptions {
    StringBuffer sqlWhere = new StringBuffer("");
    Hashtable datatypes = ClsMngBBDD.tableDataTypesAsString(con, tableName);
    if (keyfields != null) {
        String aux = " WHERE ";
        for (int i = 0; i < keyfields.length; i++) {
            if (htFilters.get(keyfields[i]) == null) {
                sqlWhere.append(aux + keyfields[i] + " IS NULL ");
            } else {
                if (datatypes.get(keyfields[i]).equals("STRING")) {
                    //sqlWhere.append(aux + keyfields[i] + " LIKE ");
                    sqlWhere.append(aux + keyfields[i] + " = ");
                    sqlWhere.append("'" + validateChars(((String)htFilters.get(keyfields[i])).replace('*', '%')) + "' ");
                } else if (datatypes.get(keyfields[i]).equals("NUMBER")) {
                    //sqlWhere.append(aux + " TO_CHAR(" + keyfields[i] + ") LIKE ");
                    sqlWhere.append(aux + " TO_CHAR(" + keyfields[i] + ") = ");
                    sqlWhere.append("'" + ((String)htFilters.get(keyfields[i])).replace('*', '%') + "' ");
                } else if (datatypes.get(keyfields[i]).equals("DATE")) {
                    sqlWhere.append(aux + keyfields[i] + " = ");
                    formatDate(htFilters.get(keyfields[i]));
                    sqlWhere.append(" TO_DATE('" + htFilters.get(keyfields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
                } else {
                    //sqlWhere.append(aux + " TO_CHAR(" + keyfields[i] + ") LIKE ");
                    sqlWhere.append(aux + " TO_CHAR(" + keyfields[i] + ") = ");
                    sqlWhere.append("'" + htFilters.get(keyfields[i]).toString().replace('*', '%') + "' ");
                }
            }
            aux = " AND ";
        }
    }
    return sqlWhere.toString();
}

private String formatDate(Object date) throws ClsExceptions {
  String val = "";
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
  java.util.Date d = null;
  if (date instanceof java.sql.Date) {
          d = new java.util.Date(((java.sql.Date) date).getTime());
  }
  if (date instanceof java.sql.Timestamp) {
          d = new java.util.Date(((java.sql.Timestamp) date).getTime());
  }
  if (date instanceof java.sql.Time) {
          d = new java.util.Date(((java.sql.Time) date).getTime());
  }
  try {
          if (date instanceof java.lang.String) {
                  d = sdf.parse((String)date);
          }

          val = sdf.format(d);

  } catch (Exception e) {
  		ClsExceptions el=new ClsExceptions(e,"DATE FORMAT MUST BE LIKE '" +
  				ClsConstants.DATE_FORMAT_SQL + "'");
  		el.setErrorCode("DATEFORMAT");
        throw el;
  }
  return val;
}


}