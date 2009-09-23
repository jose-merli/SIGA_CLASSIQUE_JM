package com.atos.utils;


import java.io.*;
import java.sql.*;
//import java.util.*;


/**
 * <p> Herramienta Documental</p>
 * <p>Descripción: Clase que gestiona el tratamiento de los campos Blob en la aplicación</p>
 * @author CSD 
 * @version RGG 16/04/2007 Adaptación de la clase a la capa base 
 */


public class MngBlob {


  /**
   * Obtiene un OutputStream a partir de un campo BLOB
   * @param con Objeto Connection con el que se controla la transacción
   * @param fieldName Nombre del campo en base de datos
   * @param select Sentencia sql para obtener el campo
   * @return El OutputStream solicitado
   * @throws GenericException Excepción genérica controlada por la aplicación
   */
  public static ByteArrayOutputStream getBlobToStream(Connection con, String fieldName, String select)
      throws ClsExceptions {

    Statement st=null;
    ResultSet rs=null;
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    Blob dbBlob=null;
    try {
      st= con.createStatement();
      rs=st.executeQuery(select);
      if( rs.next() ) {

        dbBlob=rs.getBlob(fieldName);
        readBlob(dbBlob, os);
        os.close();//??aqui?
      } else {
        throw new ClsExceptions("BLOB ("+fieldName+") no existe en tabla ");
      }
    }catch (Exception ex) {
      throw new ClsExceptions(ex,"Exception extracting OutputStream from blob field");
    } finally {
      try {if (rs!=null) rs.close(); }catch (SQLException ex) {}
      try {if (st!=null) st.close(); }catch (SQLException ex) {}
    }
    return os;
  }

  /**
   * Escribe el contenido de un InputStream en un campo BLOB
   * @param con Objeto Connection con el que se controla la transacción
   * @param tableName Nombre de la tabla en base de datos
   * @param keys Lista de campos clave
   * @param fieldValues Lista de valores de los campos
   * @param blobNameField Nombre del campo BLOB
   * @param is objeto InputStream
   * @param length longitud del InputStream
   * @throws GenericException Excepción genérica controlada por la aplicación
   */

  public static void updateBlobField(Connection con,
                                     String tableName,
                                     String whereSentence,
                                     String blobNameField,
                                     InputStream is) throws ClsExceptions{

    PreparedStatement psLock=null;
    PreparedStatement psUpdate=null;
    ResultSet rsLock=null;
    String sqlLockRow=null;
    String sqlSetRow=null;
    Blob dbBlob=null;

    try {

      sqlLockRow="select "+blobNameField+
                 " from "+tableName+
                 whereSentence+" for update";

      psLock =con.prepareStatement(sqlLockRow);
      rsLock= psLock.executeQuery();

      if( rsLock.next() ) {
        sqlSetRow="update "+tableName+
                  " set "+blobNameField+"=?" +
                  whereSentence;

        dbBlob= rsLock.getBlob(1);

        writeBlob(dbBlob,is);
        is.close();

        psUpdate =con.prepareStatement(sqlSetRow);
        psUpdate.setBlob(1,dbBlob);
        psUpdate.executeUpdate();

      }
    }catch (Exception ex) {
      throw new ClsExceptions(ex,"Exception updating blob field");
    }finally {
      try {if (rsLock!=null) rsLock.close();}catch (SQLException ex) {}
      try {if (psLock!=null) psLock.close();}catch (SQLException ex) {}
      try {if (psUpdate!=null) psUpdate.close();}catch (SQLException ex) {}
    }
  }

  protected static void writeBlob(Blob blob, InputStream is) throws IOException, SQLException {
    OutputStream os=((oracle.sql.BLOB)blob).getBinaryOutputStream();
    loopStream(is, os);
    os.close();
  }


  protected static void readBlob(Blob blob, OutputStream os) throws IOException, SQLException {
    InputStream is=blob.getBinaryStream();
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