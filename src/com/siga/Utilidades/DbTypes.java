package com.siga.Utilidades;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;


/**
 * <p> Herramienta Documental</p>
 * <p>Descripción: Clase que implementa constantes y métodos para facilitar
 * el manejo de tipos relacionados con la base de datos</p>
 * @author CSD
 * @author TNF
 */

public class DbTypes implements Serializable {

  public static final String DATE			= "DATE";
  public static final String NUMBER			= "NUMBER";
  public static final String STRING			= "STRING";
  public static final String BLOB			= "BLOB";
  public static final String CLOB			= "CLOB";
  public static final String LONGVARBINARY	= "LONGVARBINARY";
  public static final String LONGVARCHAR	= "LONGVARCHAR";
  public static final String OTHERS			= "OTHERS";

  protected static Hashtable typesPerTable = new Hashtable();

  public static synchronized Hashtable getDataTypes(String tableName) throws ClsExceptions {
  	
  	Hashtable hash = null;
  	Connection con = null;
  	
    try{
      con = ClsMngBBDD.getConnection();
      hash = getDataTypes(con, tableName);
    }
    catch (Exception ex){
    	throw new ClsExceptions (ex, "Error al recuperar los tipos de dato");
    }
    finally {
    	if (con != null) {
            ClsMngBBDD.closeConnection(con);
            con = null;
        }
    }
    return hash;
  }

  /**
   * Recupera los tipos de datos de cada uno de los campos de la tabla dada
   * @param con Objeto Connection con el que se controla la transacción
   * @param tableName Nombre de la tabla en base de datos
   * @return Conjunto de tipos de datos identificados por nombre de campo
   * @throws GenericException Excepción genérica controlada por la aplicación
   */
  public static synchronized Hashtable getDataTypes(Connection con, String tableName) throws ClsExceptions {

    Hashtable hash = null;
    tableName=tableName.toUpperCase();
    try {
      if(typesPerTable.containsKey(tableName)){
        hash=(Hashtable) typesPerTable.get(tableName);

      }else{
        DatabaseMetaData meta= con.getMetaData();
        ResultSet rs = meta.getColumns(null, null, tableName, null);
        Hashtable ht=new Hashtable();
        while(rs.next()) {
          ht.put(rs.getString("COLUMN_NAME"), getTypeAsString(rs.getInt("DATA_TYPE")));
        }
        rs.close();
        if(!ht.isEmpty()){
          typesPerTable.put(tableName,ht);
          hash=ht;
        }
      }
    }
    catch (SQLException ex) {
      throw new ClsExceptions(ex,"Exception checking SQL data types");
    }

    return hash;
  }


  /**
   * <p>Devuelve una representación en String para el codigo Oracle Data Type
   * pasado como parámetro.</p>
   * <p>Las cadenas que retorna son:<ul>
   *     <li>NUMBER</li>
   *     <li>DATE</li>
   *     <li>STRING</li>
   *     <li>CLOB</li>
   *     <li>BLOB</li>
   *     <li>LONGVARBINARY</li>
   *     <li>LONGVARCHAR</li>
   *     <li>OTHERS</li>
   * </ul></p>
   * @param type código de tipo de dato
   * @return representación legible del tipo de dato
   * @see java.sql.Types
   */
  public static String getTypeAsString(int type){
    String result = null;

    switch (type){

      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP:	result= DbTypes.DATE;
        					break;

      case Types.NUMERIC:
      case Types.DECIMAL:
      case Types.INTEGER:
      case Types.TINYINT:
      case Types.BIGINT:
      case Types.DOUBLE:
      case Types.FLOAT:		result= DbTypes.NUMBER;
        					break;

      case Types.CHAR:
      case Types.VARCHAR:	result= DbTypes.STRING;
        					break;

      case Types.BLOB:		result=DbTypes.BLOB;
        					break;

      case Types.CLOB:		result=DbTypes.CLOB;
        					break;

      case Types.LONGVARBINARY:	result=DbTypes.LONGVARBINARY;
        						break;

      case Types.LONGVARCHAR:	result=DbTypes.LONGVARCHAR;
        						break;

      default:	result= DbTypes.OTHERS;
      			break;
    }
    return result;
  }

}