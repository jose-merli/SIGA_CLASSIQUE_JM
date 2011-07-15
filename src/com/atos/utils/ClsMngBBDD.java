package com.atos.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
//import com.atos.utils.*;


public final class ClsMngBBDD {

  private static Hashtable poolHashtab = null;
  private static String poolName="none";
  private static ReadProperties dsProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.POOL);
//private static ReadProperties dsProperties=new ReadProperties("pool.properties");
  public static String POOLWR = dsProperties.returnProperty("POOL.WRITE");
  public static String POOLRD = dsProperties.returnProperty("POOL.READ");
  public static String POOLNLS = dsProperties.returnProperty("POOL.NLS");
  
  private static DataSource ds=null;
  private static DataSource dsread=null;
  private static DataSource dsnls=null;
  
  //Sólo una instancia de esta clase: Patrón Singleton
  private static ClsMngBBDD bd = new ClsMngBBDD();

  protected ClsMngBBDD() {
	Context ctx=null;

	//ClsLogging.writeFileLogWithoutSession("****************************************************", 3);
	//ClsLogging.writeFileLogWithoutSession("**               Objeto BBDD creado               **", 3);
	//ClsLogging.writeFileLogWithoutSession("****************************************************", 3);
	ClsLogging.writeFileLogWithoutSession("   > Levantando Pools de Conexión a Base de Datos.", 3);

	try{
	  ctx = new InitialContext();
	  ds = (DataSource) javax.rmi.PortableRemoteObject.narrow(ctx.lookup(POOLWR), javax.sql.DataSource.class);
	  dsread = (DataSource) javax.rmi.PortableRemoteObject.narrow(ctx.lookup(POOLRD), javax.sql.DataSource.class);
	  dsnls = (DataSource) javax.rmi.PortableRemoteObject.narrow(ctx.lookup(POOLNLS), javax.sql.DataSource.class);
	  ctx.close();
	  ctx=null;
      
	}
	catch(NamingException e){
		ClsLogging.writeFileLogError("En el Constructor de BBDD, "+e.toString(), e, 3);
	   
	}
	ClsLogging.writeFileLogWithoutSession("   > Pools de Conexión a Base de Datos levantados: ", 3);
	ClsLogging.writeFileLogWithoutSession("        - Lectura/Escritura (R/W): " + POOLWR, 3);
	ClsLogging.writeFileLogWithoutSession("        - Lectura (R): " + POOLRD, 3);
	ClsLogging.writeFileLogWithoutSession("        - Lectura/Escritura (R/W) con parametros NLS: " + POOLNLS, 3);
  }

  public static Connection getConnection() throws ClsExceptions {
    Connection con = null;
    
	  try{
		con = ds.getConnection();
	  }
	  catch(SQLException es){
		throw new ClsExceptions(es, es.getMessage().substring(0,es.getMessage().length() - 1));
	  }
	  
    return con;
  }

  public static Connection getNLSConnection() throws ClsExceptions {
    Connection con = null;
    
	try{
			con = dsnls.getConnection();
		  }
		  catch(SQLException es){
			throw new ClsExceptions(es,es.getMessage().substring(0,es.getMessage().length() - 1));
		  }
	//com.atos.utils.ClsLogging.writeFileLogWithoutSession("GET pool WR: "+con.toString(), 3);
    return con;
  }

  /**
   *
   * @return Read Connection pool
   * @throws ClsExceptions
   */

  public static synchronized Connection getReadConnection() throws ClsExceptions {
    Connection con = null;
	int status=Status.STATUS_NO_TRANSACTION;
	UserTransaction tx = null;
	try {
		Context ctx = new InitialContext();
		tx = (UserTransaction) ctx.lookup("javax.transaction.UserTransaction");
		status=tx.getStatus();
	} catch (Exception ex) {
		ex.printStackTrace();
	}
    
      try{
      	if (status==Status.STATUS_ACTIVE) {
      		//System.out.println("Obteniendo pool de R/W cuando se pedia R. TRANSACCION ACTIVA");
      		con = ds.getConnection();
      	} else {
      		con = dsread.getConnection();
      	}
      }
      catch(SQLException es){
      	throw new ClsExceptions(es,es.getMessage().substring(0,
            es.getMessage().length() - 1));
      }
      
	//com.atos.utils.ClsLogging.writeFileLogWithoutSession("GET pool RD: "+con.toString(), 3);
		
    return con;
  }

  public static void closeConnection(Connection con) throws ClsExceptions {
  	
	//com.atos.utils.ClsLogging.writeFileLogWithoutSession("CLOSE con: "+con.toString(), 3);
    try {
      if (con!=null)
        con.close();
      con = null;
    }
    catch (SQLException ex) {
      con = null;
      throw new ClsExceptions(ex, ex.getMessage().substring(0,
          ex.getMessage().length() - 1));
    }
	
  }

  public static void setDataSource(String DSname, DataSource ds){
    if(poolHashtab == null){
      init();
    }
    poolHashtab.put(DSname, ds);

  }

  public static void setDataSource(String poolname){
    poolName=poolname;
  }

  public static void init(){
    poolHashtab = new Hashtable();
  }


  public static int nextSequenceValue(String secuencia) throws ClsExceptions {
    int num = 0;

    Connection conn = null;
    Statement st = null;

    try {

      String query = "SELECT " + secuencia + ".NEXTVAL FROM DUAL ";

      conn = ClsMngBBDD.getConnection();
      st = conn.createStatement();

      ResultSet rs = st.executeQuery(query);

      rs.next();

      String valor = rs.getString(1);

      num = new Integer(valor).intValue();

      st.close();

    }
    catch (Exception e) {
      if (st != null) {
        try {
          st.close();
        }
        catch (Exception er) {
          throw new ClsExceptions(er,"Error cerrando estamento, " + er.toString());
        }
      }

      throw new ClsExceptions(e,"Error pidiendo el siguiente valor de la secuencia " + secuencia +", " + e.toString());

    } finally {
      if (conn != null)
        closeConnection(conn);
    }
    return num;
  }

  public static void writeError(String iduser, 
  								String idstream,
                                String description,
                                String category,
                                String coderrortype, 
                                String webuser,
                                String institucion) throws ClsExceptions {

    Connection conn = null;
    Statement st = null;
      /*
      ID_ERROR           SEQUENCE
      ID_STREAM          COD STREAM
      DATE_ERROR         DATE
      DESC_ERROR         DESCRIPTION
      ID_ERROR_CATEGORY  COD CATEGORY
      ID_ERROR_TYPE      COD TYPE
      ID_USER            COD USER,
      WEB_USER
      */

    /* Chk Decription */
    if ((description==null)||(description.equals("")))
      description="NO DESCRIPTION FOUND";

    /******************/

    ClsLogging.writeFileLogWithoutSession("ERROR: " + description, 1);
    int sec = nextSequenceValue(ExceptionManager.SN_ERROR_SEQ);
    try {
      String query = "INSERT INTO "+ExceptionManager.TN_ERRORS+
                     " ( "+ExceptionManager.FN_ID_ERROR+", "+ExceptionManager.FN_ID_STREAM+
					" , "+ExceptionManager.FN_DATE_ERROR+", "+ExceptionManager.FN_DESC_ERROR+
					", "+ExceptionManager.FN_ID_ERROR_CATEGORY+", "+ExceptionManager.FN_ID_ERROR_TYPE+
					", "+ExceptionManager.FN_ID_USER+/*ExceptionManager.FN_WEB_USER+*/
					", "+ExceptionManager.FN_DATE_UPDATE+", "+ExceptionManager.FN_ID_USER_UPDATE+
					", "+ExceptionManager.FN_ID_INSTITUCION+
                     ") VALUES (" + sec + ", '" + idstream + "', SYSDATE, '" +
                     ((description.length() > 200) ? description.substring(0, 199) : description) + "', '" + category + "'" +
                     ", '" + coderrortype + "', "+iduser+", SYSDATE, "+ iduser+", " + institucion + ")"; //+webuser+"')";

      ClsLogging.writeFileLogWithoutSession("query: " + query, 1);

      conn = ClsMngBBDD.getConnection();
      st = conn.createStatement();

      st.executeQuery(query);
      st.close();

    }
    catch (SQLException e) {
      if (st != null) {
        try {
          st.close();
        }
        catch (Exception er) {
          throw new ClsExceptions(er,"Error cerrando estamento, " + er.toString());
        }
      }
      throw new ClsExceptions(e,"Error SQL guardando el error, " + e.toString());
    } catch (Exception e) {
      if (st != null) {
        try {
          st.close();
        }
        catch (Exception er) {
          throw new ClsExceptions(er,"Error cerrando estamento, " + er.toString());
        }
      }
      throw new ClsExceptions(e,"Error Genérico guardando el error, " + e.toString());
    } finally {
      if (conn != null)
        ClsMngBBDD.closeConnection(conn);
    }

  }


  /**
   *  Devuelve pares Clave(nombre de campo)/Valor(Short SQLType del campo)
   *  para todos los campos de la tabla.
   */
  public static Hashtable tableDataTypes(Connection con, String tableName) throws ClsExceptions{
    DatabaseMetaData meta = null;
    ResultSet rs = null;
    Hashtable ht = null;
    try {
      meta = con.getMetaData();
      rs = meta.getColumns(null, null, tableName, null);
      if (rs.next()) {
        ht = new Hashtable();
        do {
          ht.put(rs.getString("COLUMN_NAME"), new Short(rs.getShort("DATA_TYPE")));
        } while (rs.next());
      } else {
        ClsLogging.writeFileLog("MetaData.getColumns para la tabla " + tableName + " no ha devuelto ningún valor", 1);
      }
      return ht;
    } catch (SQLException ex ) {
      throw new ClsExceptions(ex,ex.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (meta != null)
          meta = null;
      } catch (SQLException ex ) {
        throw new ClsExceptions(ex,ex.getMessage());
      }
    }
  } // tiposCampo

  /**
   * Returns a Hastable with the data Types for the columns of the table.
   * Returns As Strings:
   *     NUMBER
   *     DATE
   *     STRING
   *     OTHERS
   * @param con
   * @param tableName
   * @return
   * @throws ClsExceptions
   */
  public static Hashtable tableDataTypesAsString(Connection con, String tableName) throws ClsExceptions {
    Hashtable ht = ClsMngBBDD.tableDataTypes(con, tableName);
    Enumeration en = ht.keys();
    while (en.hasMoreElements()) {
      String key = (String)en.nextElement();
            /*
            if (((Short)ht.get(key)).intValue() == Types.TIME ||
                  ((Short)ht.get(key)).intValue() == Types.TIMESTAMP) {
                   ht.remove(key);
                   ht.put(key, "TIMESTAMP");
                   continue;
     }
           if (((Short)ht.get(key)).intValue() == Types.DATE) {
            ht.remove(key);
            ht.put(key, "DATE");
            continue;
     }
            */
      if (((Short)ht.get(key)).intValue() == Types.DATE ||
          ((Short)ht.get(key)).intValue() == Types.TIME ||
          ((Short)ht.get(key)).intValue() == Types.TIMESTAMP) {
        ht.remove(key);
        ht.put(key, "DATE");
        continue;
      }
      if (((Short)ht.get(key)).intValue() == Types.NUMERIC ||
          ((Short)ht.get(key)).intValue() == Types.DECIMAL ||
          ((Short)ht.get(key)).intValue() == Types.INTEGER ||
          ((Short)ht.get(key)).intValue() == Types.TINYINT ||
          ((Short)ht.get(key)).intValue() == Types.BIGINT ||
          ((Short)ht.get(key)).intValue() == Types.DOUBLE ||
          ((Short)ht.get(key)).intValue() == Types.FLOAT) {
        ht.remove(key);
        ht.put(key, "NUMBER");
        continue;
      }
      if (((Short)ht.get(key)).intValue() == Types.CHAR ||
         ((Short)ht.get(key)).intValue() == Types.VARCHAR ||
          ((Short)ht.get(key)).intValue() == Types.LONGVARCHAR) {
        ht.remove(key);
        ht.put(key, "STRING");
        continue;
      }
      // resto de los casos
      ht.remove(key);
      ht.put(key, "OTHERS");
      continue;
    }
    return ht;
  }

  /**
   * Calls a PL Funtion
   * @author CSD
   * @param functionDefinition string that defines the function
   * @param inParameters input parameters
   * @return error code, '0' if ok
   * @throws ClsExceptions  type Exception
   */
  public static String callPLFunction(String functionDefinition, Object[] inParameters) throws ClsExceptions {
    return callPLFunction(functionDefinition,0,inParameters)[0];
  }

  /**
   * Calls a PL Funtion
   * @author CSD
   * @param functionDefinition string that defines the function
   * @param inParameters input parameters
   * @param outParameters number of output parameters
   * @return error code, '0' if ok
   * @throws ClsExceptions  type Exception
   */
  public static String[] callPLFunction(String functionDefinition, int outParameters, Object[] inParameters) throws ClsExceptions {
    ClsLogging.writeFileLogWithoutSession("CALL: "+functionDefinition, 3);
    Connection con=ClsMngBBDD.getConnection();
    try{
      CallableStatement cs=con.prepareCall(functionDefinition);
      int size=inParameters.length;
      //error code
      cs.registerOutParameter(1,Types.VARCHAR);
      //output Parameters
      for(int i=0;i<outParameters;i++){
        cs.registerOutParameter(i+2,Types.VARCHAR);
      }
      //input Parameters
      for(int i=0;i<size;i++){
        ClsLogging.writeFileLogWithoutSession("PARAM_"+i+": "+inParameters[i], 3);
        cs.setString(i+outParameters+2,(String)inParameters[i]);
      }
      cs.execute();
      String[] result=new String[outParameters+1];
      for(int i=0;i<outParameters+1;i++){
        result[i]=cs.getString(i+1);
      }
      cs.close();
      return result;
    }catch(SQLException ex){
      String msg=ex.getMessage().substring(0,ex.getMessage().length() - 1);
      String p=functionDefinition;
      p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
      throw new ClsExceptions(ex,msg,p,"0","GEN00","20");
    }catch(Exception e){
        String msg=e.getMessage();
        String p=functionDefinition;
        p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
        throw new ClsExceptions(e,msg,p,"0","GEN00","20");
    }finally{      
      ClsMngBBDD.closeConnection(con);
     }
  }
  /**
   * Calls a PL Procedure that returns a RowsContainer
   * @author JER
   * @param functionDefinition string that defines the function
   * @param inParameters input parameters
   * @return a Row container with the result of the sql sentence
   * @throws ClsExceptions  type Exception
   */
  public static RowsContainer callPLProcedureRC(String functionDefinition, Object[] inParameters) throws ClsExceptions {
    ResultSet result = null;
    RowsContainer rc = new RowsContainer();

    ClsLogging.writeFileLogWithoutSession("CALL: "+functionDefinition, 3);
    Connection con=ClsMngBBDD.getConnection();
    try{
      CallableStatement cs=con.prepareCall(functionDefinition);
      int size=inParameters.length;
      //output Parameters (only 1)
      //cs.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);
      cs.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);
      //input Parameters
      for(int i=0;i<size;i++){
        ClsLogging.writeFileLogWithoutSession("PARAM_"+i+": "+inParameters[i], 3);
        cs.setString(i+2,(String)inParameters[i]);
      }
      cs.execute();

      result=(ResultSet)cs.getObject(1);
      // Transform ResultSet to RowsContainer
      while (result.next()) {
        Row row = new Row();
        row.load(result);
        rc.addRecord(row);
      } // while (result.next())
      result.close();
      cs.close();
      return rc;
    }catch(SQLException ex){
      String msg=ex.getMessage().substring(0,ex.getMessage().length() - 1);
      String p=functionDefinition;
      p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
      throw new ClsExceptions(ex,msg,p,"0","GEN00","20");
    }catch(Exception e){
        String msg=e.getMessage();
        String p=functionDefinition;
        p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
        throw new ClsExceptions(e,msg,p,"0","GEN00","20");
    }finally{
      ClsMngBBDD.closeConnection(con);
    }
  } // callPLProcedureRC


  /**
   * Calls a PL Funtion
   * @author CSD
   * @param functionDefinition string that defines the function
   * @param inParameters input parameters
   * @param outParameters number of output parameters
   * @return error code, '0' if ok
   * @throws ClsExceptions  type Exception
   */
  public static String[] callPLProcedure(String functionDefinition, int outParameters, Object[] inParameters) throws ClsExceptions {
    String result[] = null;

    if (outParameters>0) result= new String[outParameters];
    ClsLogging.writeFileLogWithoutSession("CALL: "+functionDefinition, 3);
    Connection con=ClsMngBBDD.getConnection();
    try{
      CallableStatement cs=con.prepareCall(functionDefinition);
      int size=inParameters.length;
      
      //input Parameters
      for(int i=0;i<size;i++){
        ClsLogging.writeFileLogWithoutSession("PARAM_IN_"+(i+1)+": "+inParameters[i], 3);
        cs.setString(i+1,(String)inParameters[i]);
      }
      //output Parameters
      for(int i=0;i<outParameters;i++){
        cs.registerOutParameter(i+size+1,Types.VARCHAR);
      }
      cs.execute();

      for(int i=0;i<outParameters;i++){
        result[i]=cs.getString(i+size+1);
        ClsLogging.writeFileLogWithoutSession("PARAM_OUT_"+(i+1)+": "+result[i], 3);
      }
      cs.close();
      return result;
    }catch(SQLException ex){
        String msg=ex.getMessage().substring(0,ex.getMessage().length() - 1);
        String p=functionDefinition;
        p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
        throw new ClsExceptions(ex,msg,p,"0","GEN00","20");
    }catch(Exception e){
        String msg=e.getMessage();
        String p=functionDefinition;
        p=p.substring(p.indexOf("=")+1,p.indexOf("(?"));
        throw new ClsExceptions(e,msg,p,"0","GEN00","20");
    }finally{
      ClsMngBBDD.closeConnection(con);
    }
  }

  /**
   * Calls a PL Funtion
   * @author CSD
   * @param functionDefinition string that defines the function
   * @param inParameters input parameters
   * @param outParameters number of output parameters
   * @return error code, '0' if ok
   * @throws ClsExceptions  type Exception
   */
  public static int executeUpdate(String sentence) throws ClsExceptions {
    int result=0;
    Connection con=ClsMngBBDD.getConnection();
    try{
      Statement stmt=con.createStatement();
      ClsLogging.writeFileLogWithoutSession("SQL CLSMNGBBDD: "+sentence, 3);
      result=stmt.executeUpdate(sentence);
      stmt.close();
    }catch(SQLException ex){
      ClsExceptions psscEx = new ClsExceptions(ex,ex.getMessage().substring(0, ex.getMessage().length() - 1));
      psscEx.setErrorType("9");
      throw psscEx;
      
      //psscEx.setParam(tableName);
    }finally{
      ClsMngBBDD.closeConnection(con);
    }
    return result;
  }

  public static String getDriverInformation(Connection connection)
  throws SQLException
          {
          String infodriver = new String();
          java.sql.DatabaseMetaData md = connection.getMetaData();
          infodriver = "Driver Name: "+md.getDriverName();
          infodriver = infodriver + "Driver Version: "+md.getDriverVersion();
          infodriver = infodriver + "Driver Major Version:"+md.getDriverMajorVersion();
          infodriver = infodriver + "Driver Minor Version: "+md.getDriverMinorVersion();
          infodriver = infodriver + "Database Product Name: " 	   +md.getDatabaseProductName();
          infodriver = infodriver + "Database Product Version: " 	   +md.getDatabaseProductVersion();
          return infodriver;
      }


  // Antes:
//	public static String getSQLBindInformation(String sql, Hashtable codigos) throws ClsExceptions   {
//	    String salida = sql + "[";
//	    Enumeration e = codigos.keys();
//  	     while (e.hasMoreElements()) {
//  	         Integer key = (Integer)e.nextElement();
//  	         salida += key.toString() + "-" + (String)codigos.get(key) + " | ";
//  	     }
//	    return salida + "]";
//	}

	public static String getSQLBindInformation(String sql, Hashtable codigos) throws ClsExceptions 
	{
	    String salida = sql + " ";
		
		try {
		    Enumeration e = codigos.keys();
	 	    while (e.hasMoreElements()) {
	 	    	Integer key = (Integer)e.nextElement();

	 	    	boolean esCadena = true;
	 	    	try {
	 	    		if (((String)codigos.get(key)).equalsIgnoreCase("SYSDATE") || ((String)codigos.get(key)).equalsIgnoreCase("SYSTIMESTAMP"))
	 	    			esCadena = false;
	    		
	 	    		Long.parseLong((String)codigos.get(key));
	 	    		esCadena = false;
	 	    	}
	 	    	catch (Exception e1) {
	 	    		try {
	 	    			Double.parseDouble((String)codigos.get(key));
		 	    		esCadena = false;
	 	    		}
	 	    		catch (Exception e3) {
	 	    			try {
		 	    			Integer.parseInt((String)codigos.get(key));
			 	    		esCadena = false;
		 	    		}
		 	    		catch (Exception e4) {
		 	    			
		 	    			
		 	    		}
	 	    			
	 	    		}
	 	    	}
	 	    	
	 	    	
	 	    	
				salida = UtilidadesString.replaceFirstIgnoreCase(salida, ":" + key.toString() + "'", (esCadena?"'":"") + String.valueOf(codigos.get(key)) + (esCadena?"'":"") + "'");
	 	    	salida = UtilidadesString.replaceFirstIgnoreCase(salida, ":" + key.toString() + ",", (esCadena?"'":"") + String.valueOf(codigos.get(key)) + (esCadena?"'":"") + ",");
	 	    	salida = UtilidadesString.replaceFirstIgnoreCase(salida, ":" + key.toString() + " ", (esCadena?"'":"") + String.valueOf(codigos.get(key)) + (esCadena?"'":"") + " ");
	 	    	salida = UtilidadesString.replaceFirstIgnoreCase(salida, ":" + key.toString() + "", (esCadena?"'":"") + String.valueOf(codigos.get(key)) + (esCadena?"'":"") + "");
	  	    }
		}
		catch (Exception e) {
			return salida;
		}
	    return salida;
	}
}