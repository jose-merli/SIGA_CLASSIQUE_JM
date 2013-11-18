package com.atos.utils;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//import oracle.jdbc.OraclePreparedStatement;

//import org.apache.log4j.*;



/**
 * <p> Herramienta Documental</p>
 * <p>Descripción: Clase que gestiona el tratamiento de los campos Clob en la aplicación</p>
 * @author CSD
 * @version RGG 16/04/2007 Adaptación de la clase a la capa base 
 */


public class MngClob {

  public static final int EURO = 164;
  public static final int StringEURO = 8364;
  
  /**
   * Obtiene un String a partir de un campo CLOB
   * @param con Objeto Connection con el que se controla la transacción
   * @param fieldName Nombre del campo en base de datos
   * @param select Sentencia sql para obtener el campo
   * @return El OutputStream solicitado
   * @throws ClsExceptions Excepción genérica controlada por la aplicación
   */
  
  public static String getClobToSring(Connection con, String fieldName, String select)
	      throws ClsExceptions {

	    ClsExceptions gEx=null;
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    Statement st=null;
	    ResultSet rs=null;
	    String  dbClobString=null;

	    try {
	      st= con.createStatement();
	      rs=st.executeQuery(select);
	      if( rs.next() ) {
	    	  dbClobString = rs.getString(fieldName);
	      } else {
	        throw new ClsExceptions("BLOB ("+fieldName+") no existe en tabla ");

	      }
	    }catch (Exception ex) {
	      throw new ClsExceptions(ex,"Exception extracting OutputStream from clob field");
	    } finally {
	      try {if (rs!=null) rs.close(); }catch (SQLException ex) {}
	      try {if (st!=null) st.close(); }catch (SQLException ex) {}
	      if(gEx!=null){
	        throw gEx;
	      }
	    }
	    return dbClobString==null?"":dbClobString;
	  }
 
  
 


  /**
   * Escribe el contenido de un String en un campo CLOB
   * @param con Objeto Connection con el que se controla la transacción
   * @param tableName Nombre de la tabla en base de datos
   * @param keys Lista de campos clave
   * @param fieldValues Lista de valores de los campos
   * @param clobNameField Nombre del campo CLOB
   * @throws ClsExceptions Excepción genérica controlada por la aplicación
   */
  public static void updateClobField(Connection con,
                                     String tableName,
                                     String whereSentence,
                                     String clobNameField, 
									 String clobString) throws ClsExceptions{

    ClsExceptions gEx=null;
    PreparedStatement psLock=null;
    // OPCION ORIGINAL
    //PreparedStatement psUpdate=null;
    // OPCION 2
    //OraclePreparedStatement psUpdate=null;
    // OPCION 3
    PreparedStatement psUpdate=null;
    ResultSet rsLock=null;
    String sqlLockRow=null;
    String sqlSetRow=null;
    Clob dbClob=null;

    try {
      if(clobString!=null && !clobString.equals("")){
        sqlLockRow="select "+clobNameField+
                   " from "+tableName+
                   whereSentence+" for update";

        psLock =con.prepareStatement(sqlLockRow);
        rsLock= psLock.executeQuery();

        if( rsLock.next() ) {
          sqlSetRow="update "+tableName+
                    " set "+clobNameField+"=?" +
                    whereSentence;

          dbClob= rsLock.getClob(1);
          
          // IOPCION ORIGINAL
          //writeClob(dbClob,clobString);
          //psUpdate = con.prepareStatement(sqlSetRow);
          //psUpdate.setClob(1,dbClob);
          
          // OPCION 2
          //psUpdate =(OraclePreparedStatement)con.prepareStatement(sqlSetRow);
          //psUpdate.setClob(1,dbClob);

          // OPCION 3
          psUpdate = con.prepareStatement(sqlSetRow);
          psUpdate.setString(1,clobString);

          psUpdate.executeUpdate();

        }
      }
    }catch (Exception ex) {
      throw new ClsExceptions(ex,"Exception updating clob field");
    }finally {
      try {if (rsLock!=null) rsLock.close();}catch (SQLException ex) {}
      try {if (psLock!=null) psLock.close();}catch (SQLException ex) {}
      try {if (psUpdate!=null) psUpdate.close();}catch (SQLException ex) {}
    }
  }



  /**
   * Convierte un objeto Clob en una cadena con el mismo contenido
   * @param clob Object Clob
   * @return Cadena resultante
   */
  public static String _readClobAsString (Clob clob) {
    if (clob==null) return null;
    BufferedReader bf=null;
    StringBuffer out = new StringBuffer(100);
    try {
      bf = new BufferedReader(new InputStreamReader(clob.getAsciiStream()));
      String line;
      while ( (line = bf.readLine()) != null) {
        out.append(line);
        out.append("\n");
      }
      bf.close();

    } catch (Exception ex) {
      out.setLength(0);

    } finally {
      try { bf.close();}catch (IOException ioe){}
    }

    return (out.toString());
  }

  public static String readClobAsString (Clob clob) {
      if (clob==null) {
        return "";
      }
      BufferedReader br = null;
      StringBuffer buffer = null;
      try {
        Reader cr = ((oracle.sql.CLOB)clob).getCharacterStream();
        br = new BufferedReader(cr);
        String line = "";
        while (line!=null){
          line = br.readLine();
          if (line!=null) {
            line = line.replace((char)EURO,(char)StringEURO);
            if (buffer==null) {
              buffer = new StringBuffer();
            }
            buffer.append(line);
          }
        }
      }catch (Exception ex) {
      } finally {
        try {
          if (br!=null) {
            br.close();
          }
        }catch (IOException ioe) {
        }
      }
      //logger.info("readClobAsString: "+buffer.toString());
      return (buffer==null)?"":buffer.toString();
    }

  public static boolean isClob(Object val){
    return ((val!=null)&&( val instanceof oracle.sql.CLOB));
  }

  protected static void _writeClob(Clob clob, String clobString) throws IOException, SQLException {
    Writer osw= ((oracle.sql.CLOB)clob).getCharacterOutputStream();
    osw.write(clobString);
    osw.flush();
    osw.close();
  }

  protected static void writeClob(Clob clob, String clobString) throws IOException, SQLException {
    if(clob!=null && clobString!=null){
      Writer osw= ((oracle.sql.CLOB)clob).getCharacterOutputStream();
      //Writer osw= ((oracle.sql.CLOB)((weblogic.jdbc.wrapper.Clob)clob).getVendorObj()).getCharacterOutputStream();
	  // Writer osw= clob.setCharacterStream(1);
      clobString = clobString.replace((char)StringEURO,(char)EURO);
      osw.write(clobString.toCharArray());
      osw.flush();
      osw.close();
    }
  }

  protected static void readClob(Clob clob, OutputStream os) throws IOException, SQLException {
	  
    InputStream is=clob.getAsciiStream();
    loopStream(is, os);
    is.close();
  }

  protected static void loopStream(InputStream is, OutputStream os) throws IOException {
    byte[] buffer = new byte[10* 1024];
    int nread = 0;
    while( (nread= is.read(buffer)) != -1 )
      os.write(buffer, 0, nread);
  }


}