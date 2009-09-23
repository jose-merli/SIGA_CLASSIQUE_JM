package com.atos.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Descripción: Generador de Colecciones de objetos código/descripción
 * <code>CodeDescHandler</code>.</p>
 * @author Sin atribuir
 * @version 1.0
 * @see CodeDescHandler
 */

public class CodeDescCollectionHandler {

  /**
   * <p>Carga una colección de objetos CodeDescHandler a partir de una sentencia
   * SQL donde los campos se renombraran AS CODIGO y AS DESCRIPCION.</p>
   * <p>Para ello se usarán el primer campo de la SQL como código, y el segundo
   * como descripción.</p>
   * @param sqlStatement Sentencia SQL
   * @param firstBlank indicador de si se debe o no crear un elemento vacío al
   * principio de la colección
   * @return Colección de objetos CodeDescHandler
   * @throws GenericException Si ocurre algún error
   */
  public static Vector loadCollectionFromDB(String sqlStatement, boolean firstBlank)
      throws Exception{

    RowsContainer rc = new RowsContainer();
    Vector v = new Vector();

    try {
        if (rc.find(sqlStatement)) {
           for (int i = 0; i < rc.size(); i++){
              Row fila = (Row) rc.get(i);
              v.add(new CodeDescHandler(fila.getString("CODIGO"),fila.getString("DESCRIPCION")));
           }
        }

      }
      catch (Exception e)
  	{
  		throw e;
  	}	
      finally {
        if(firstBlank){
          v.add(0,new CodeDescHandler("",""));
        }
      }
      return v;
  }

  /**
   * Carga una colección de objetos CodeDescHandler a partir de los valores
   * recogidos en un fichero de propiedades.
   * @param fileName Ruta completa al fichero
   * @param firstBlank indicador de si se debe o no crear un elemento vacío al
   * principio de la colección
   * @return Colección de objetos CodeDescHandler
   */
  public static Vector loadCollectionFromFile(String fileName, boolean firstBlank) {
    Vector v = new Vector();
    FileInputStream inputStream=null;
    InputStreamReader inputStreamReader=null;
    try {
      inputStream=new FileInputStream(fileName);
      inputStreamReader=new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String s = bufferedReader.readLine();
      String key=null;
      String value=null;
      int i=-1;
      while(s!=null){
        i=s.indexOf("=");
        if(i!=-1){
          key=s.substring(0,i);
          value=s.substring(i+1,s.length());
          v.add(new CodeDescHandler(key,value));
          s = bufferedReader.readLine();
        }
      }
      inputStream.close();
      inputStreamReader.close();

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
    	try {
	        inputStream.close();
	        inputStreamReader.close();
    	}
    	catch (Exception e) {}
      if(firstBlank){
        v.add(0,new CodeDescHandler("",""));
      }
    }
    return v;
  }

  /**
   * <p>Carga una colección de objetos CodeDescHandler a partir de los datos de una
   * tabla en la base de datos.</p>
   * <p>Los resultados se ordenan por el campo Descripción.</p>
   * @param tableName Nombre de la tabla
   * @param codeFieldName Nombre del campo cuyo valor se usará como código
   * @param descFieldName Nombre del campo cuyo valor se usará como descripción
   * @param firstBlank indicador de si se debe o no crear un elemento vacío al
   * principio de la colección
   * @return Colección de objetos CodeDescHandler
   * @throws GenericException Si ocurre algún error
   */
  public static Vector loadCollectionFromDB(String tableName, String codeFieldName,
                                            String descFieldName, boolean firstBlank)
                                                                                throws Exception{
    Vector v = new Vector();
    RowsContainer rc = new RowsContainer();
    String sql = " SELECT " + codeFieldName + " AS CODIGO, " + descFieldName + " AS DESCRIPCION " +
                 " FROM " + tableName +
                 " ORDER BY " + descFieldName;

    try {
      if (rc.find(sql)) {
         for (int i = 0; i < rc.size(); i++){
            Row fila = (Row) rc.get(i);
            v.add(new CodeDescHandler(fila.getString("CODIGO"),fila.getString("DESCRIPCION")));
         }
      }
    }
    catch (Exception e)
	{
		throw e;
	}	
    finally {
      if(firstBlank){
        v.add(0,new CodeDescHandler("",""));
      }
    }
    return v;
  }

  /**
   * <p>Carga una colección de objetos CodeDescHandler a partir de los datos de una
   * tabla en la base de datos.</p>
   * <p>En este caso se usará un sólo campo como código y descripción.</p>
   * <p>Los resultados se ordenan por el campo a recuperar.</p>
   * @param tableName Nombre de la tabla
   * @param codeFieldName Nombre del campo cuyo valor se usará como código
   * @param firstBlank indicador de si se debe o no crear un elemento vacío al
   * principio de la colección
   * @return Colección de objetos CodeDescHandler
   * @throws GenericException Si ocurre algún error
   */
  public static Vector loadCollectionFromDB(String tableName, String codeFieldName,
      boolean firstBlank)
      throws Exception{
  	
    Vector v = new Vector();
    RowsContainer rc = new RowsContainer();
    
    String sql = " SELECT " + codeFieldName  + " AS CODIGO " +
                 " FROM " + tableName +
                 " ORDER BY " + codeFieldName;
 
    try {
        if (rc.find(sql)) {
           for (int i = 0; i < rc.size(); i++){
              Row fila = (Row) rc.get(i);
              v.add(new CodeDescHandler(fila.getString("CODIGO"),fila.getString("CODIGO")));
           }
        }

    }
    catch (Exception e){
  		throw e;
  	}	
    finally {
        if(firstBlank){
          v.add(0,new CodeDescHandler("",""));
        }
    }
    return v;
  }


  /**
   * <p>Carga una colección de objetos CodeDescHandler a partir de los datos de una
   * tabla en la base de datos.</p>
   * <p>En este caso, se indica un campo distinto a la descripción para ordenar
   * los resultados.</p>
   * @param tableName Nombre de la tabla
   * @param keyFieldName Campo por el que se ordenarán los resultados CODIGO1.
   * @param codeFieldName Nombre del campo cuyo valor se usará como código. CODIGO2
   * @param descFieldName Nombre del campo cuyo valor se usará como descripción DESCRIPCION
   * @return Colección de objetos CodeDescHandler
   * @throws GenericException Si ocurre algún error
   */
  public static Hashtable loadCollectionsSetFromDB(String tableName, String keyFieldName,
      String codeFieldName, String descFieldName)
      throws Exception{

    Hashtable h = new Hashtable();
    Vector v = null;
    String key="";

    String sql = " SELECT " + keyFieldName + " AS CODIGO1, "+ codeFieldName + " AS CODIGO2, " + descFieldName + " AS DESCRIPCION " +
                 " FROM " + tableName +
                 " ORDER BY CODIGO1, DESCRIPCION";

    try {
      RowsContainer rc = new RowsContainer();  
      if (rc.find(sql)){
      	for (int i = 0; i < rc.size(); i++){
      		Row fila = (Row) rc.get(i);
      		String k="Collection_"+fila.getString("CODIGO1");
      		if(!k.equalsIgnoreCase(key)){
      			v=new Vector();
      			key=k;
      			h.put(key,v);
      		}
      		v.add(new CodeDescHandler(fila.getString("CODIGO2"),fila.getString("CODIGO3")));
      	}
      }
    } catch (Exception e) {
    	throw e;
    }
    return h;
  }

  /**
   * <p>Carga una Hashtable de colecciones de objetos CodeDescHandler a partir
   * de una sentencia SQL.</p>
   * <p>La sentencia debe tener al menos tres campos:</p>
   * <ul>
   *     <li>El primer campo será el que agrupará las colecciones, bajo claves
   * compuestas por la cadena "Collection_" y el valor del campo.</li>
   *     <li>El segundo campo será el que se use como clave de los objetos
   * CodeDescHandler</li>
   *     <li>El tercer campo será el que se use como descripción de los objetos
   * CodeDescHandler</li>
   * </ul>
   * <p>El Hashtable tendrá como claves las compuestas por "Collection_" y el
   * valor del primer campo, y como valor, las colecciones de objetos
   * CodeDescHandler.</p>
   * @param sqlStatement Sentencia SQL
   * @return Colección de objetos CodeDescHandler
   * @throws GenericException Si ocurre algún error
   */
  public static Hashtable loadCollectionsSetFromDB(String sqlStatement) throws Exception{
    return CodeDescCollectionHandler.loadCollectionsSetFromDB(sqlStatement, false);
  }

    /**
     * <p>Carga una Hashtable de colecciones de objetos CodeDescHandler a partir
     * de una sentencia SQL.</p>
     * <p>La sentencia debe tener al menos tres campos:
     * 		nombrados como AS CODIGO1, AS CODIGO2, AS DESCRIPCION</p>
     * <ul>
     *     <li>El primer campo será el que agrupará las colecciones, bajo claves
     * compuestas por la cadena "Collection_" y el valor del campo.</li>
     *     <li>El segundo campo será el que se use como clave de los objetos
     * CodeDescHandler</li>
     *     <li>El tercer campo será el que se use como descripción de los objetos
     * CodeDescHandler</li>
     * </ul>
     * <p>El Hashtable tendrá como claves las compuestas por "Collection_" y el
     * valor del primer campo, y como valor, las colecciones de objetos
     * CodeDescHandler.</p>
     * @param sqlStatement Sentencia SQL
     * @param firstBlank indicador de si se debe o no crear un elemento vacío al
     * principio de cada colección
     * @return Colección de objetos CodeDescHandler
     * @throws GenericException Si ocurre algún error
     */
    public static Hashtable loadCollectionsSetFromDB(String sqlStatement,  boolean firstBlank) throws Exception{
      
      Hashtable h = new Hashtable();
      Vector v = null;
      String key="";

      try {
        RowsContainer rc = new RowsContainer();  
        if (rc.find(sqlStatement)){
        	for (int i = 0; i < rc.size(); i++){
        		Row fila = (Row) rc.get(i);
        		String k="Collection_"+fila.getString("CODIGO1");
        		if(!k.equalsIgnoreCase(key)){
        			v=new Vector();
        			key=k;
        			h.put(key,v);        			
        		}
        		v.add(new CodeDescHandler(fila.getString("CODIGO2"),fila.getString("CODIGO3")));
        	}
        }
      } catch (Exception e) {
      	throw e;
      }
      finally {
	      if(firstBlank){
	        v.add(0,new CodeDescHandler("",""));
	      }
      }
      return h;
    }


  /**
   * Anhade una cadena a un Array
   * @param array Array original
   * @param o Cadena a anhadir
   * @return Array con la cadena anhadida (al final del mismo)
   */
  public static String[] _addStringToArray(String[] array, String o) {
    String[] a = new String[array.length + 1];
    System.arraycopy(array, 0, a, 0, array.length);
    a[a.length -1] = o;
    return a;
  }

  /**
   * <p>Rellena una cadena con un carácter hasta alcanzar un tamanho
   * determinado.</p>
   * <p>Los caracteres pueden ser anhadidos al principio o al final de la
   * cadena.</p>
   * @param s La cadena a completar
   * @param c El caracter a anhadir
   * @param finalLength Longitud final de la cadena
   * @param appendAtEnd indicador de si los caracteres deben ser anhadidos al final
   * si el valor es <code>true</code>, o al principio si este es <code>false</code>
   * @return la cadena con los caracteres anhadidos
   */
  public static String _addCharacterToString(String s, char c, int finalLength, boolean appendAtEnd) {
    while (s.length() < finalLength) {
      s = (appendAtEnd) ? s + c : c + s;
    }
    return s;
  }

  /**
   * Anhade un elemento a una colección
   * @param v Colección a la que anhadiremos un nuevo objeto CodeDescHandler.
   * @param code Código del nuevo objeto CodeDescHandler.
   * @param desc Descripción del nuevo objeto CodeDescHandler.
   */
  public static void _addStringToCollection(Vector v, String code, String desc){
    if (v==null){
      v = new Vector();
    }
    v.add(new CodeDescHandler(code,desc));
  }


}