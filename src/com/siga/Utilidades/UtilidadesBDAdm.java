/*
 * Created on 05-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

/**
 * @author daniel.campos
 *
 * UtilidadesBDAdm
 * Clase que contiene utilidades para el acceso a base de datos
 */
public class UtilidadesBDAdm 
{
	/** Funcion getFechaCompletaBD
	 *  Funcion que obtiene la fecha completa a partir de la base de datos
	 *  @param idioma, para el formato de salida. Si es "" el formato sera DD/MM/YYYY HH24:MI:SS, en caso contario sera YYYY/MM/DD HH24:MI:SS 
	 *  @return String con la fecha completa actual 
	 * */
	static public String getFechaCompletaBD(String lang) throws ClsExceptions 
	{
		if (esLenguajeDeEspana(lang)) {
			return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS') AS FECHA FROM DUAL");
		}
		else {
			return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS') AS FECHA FROM DUAL");
		}
	}
	
	/** Funcion getFechaBD
	 *  Funcion que obtiene la fecha a partir de la base de datos
	 *  @param idioma, para el formato de salida. Si es "" el formato sera DD/MM/YYYY, en caso contario sera YYYY/MM/DD 
	 *  @return String con la fecha actual 
	 * */
	static public String getFechaBD(String lang) throws ClsExceptions 
	{
		if (esLenguajeDeEspana(lang)) {
			return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'DD/MM/YYYY') AS FECHA FROM DUAL");
		}
		else {
			return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'YYYY/MM/DD') AS FECHA FROM DUAL");
		}
	}
	
	/** Funcion getFechaBD
	 *  Funcion que obtiene la fecha a partir de la base de datos
	 *  @param idioma, para el formato de salida. Si es "" el formato sera DD/MM/YYYY, en caso contario sera YYYY/MM/DD 
	 *  @return String con la fecha actual 
	 * */
	static public String getFechaEscritaBD(String lang) throws ClsExceptions {
		return UtilidadesBDAdm.getDateBD("SELECT PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(sysdate,'m',"+lang+") AS FECHA FROM DUAL");
	}
	
	/** Funcion getHoraBD
	 *  Funcion que obtiene la hora a partir de la base de datos
	 *  @return String con la hora actual 
	 * */
	static public String getHoraBD() throws ClsExceptions {
		return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'HH24:MI:SS') AS FECHA FROM DUAL");
	}

	static public String getYearBD(String lang) throws ClsExceptions 
	{
		return UtilidadesBDAdm.getDateBD("SELECT TO_CHAR(SYSDATE, 'YYYY') AS FECHA FROM DUAL");
	}

	static private String getDateBD(String sql) throws ClsExceptions{
		
		String fecha = "";
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				fecha = (String)(fila.getRow().get("FECHA"));
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al recuperar la fecha de BD"); 
		}
		return fecha;
	}
	
	static public Hashtable ejecutaQuery(String sql) throws ClsExceptions{
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				return (Hashtable)(fila.getRow());
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al recuperar al ejecutar la sentencia de BD"); 
		}
		return null;
	}
	
	/**
	 * @param campo
	 * @param fechaDesde  YYYY/MM/DD
	 * @param fechaHasta  YYYY/MM/DD
	 * @return
	 */
	static public String dateBetweenDesdeHasta (String campo, String fechaDesde, String fechaHasta) 
	{
		try {
			return GstDate.dateBetweenDesdeAndHasta(campo, fechaDesde, fechaHasta);
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
			return "";
		}
	}
	
	static private boolean esLenguajeDeEspana (String len) 
	{
		if (len == null)                return true;	// Por defecto es espanol
		if (len.equals(""))             return true;	// Por defecto es espanol
		if (len.equals("1"))            return true;	// castellano
		if (len.equals("2"))            return true;	// catalan
		if (len.equals("3"))            return true;	// euskera
		if (len.equals("4"))            return true;	// gallego
		if (len.equalsIgnoreCase("es")) return true;	// castellano
		if (len.equalsIgnoreCase("ca")) return true;	// catalan
		if (len.equalsIgnoreCase("eu")) return true;	// euskera
		if (len.equalsIgnoreCase("gl")) return true;	// gallego
		return false;
	}

	  /**
	   * Construye una clausula select con la lista de campos y el nombre de la tabla
	   * @param tableName Nombre de la tabla en base de datos
	   * @param fields Lista de campos de base de datos
	   * @return La clausula select
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlSelect (String tableName, String[] fields) throws ClsExceptions 
	  {
	      	return sqlSelect(tableName, null, fields);
	  }

	  /**
	   * Construye una clausula select con la lista de campos, un nombre para la tabla y un alias para la misma.
	   * @param tableName Nombre de la tabla en base de datos
	   * @param aliasTabla Alias para poner a la tabla.
	   * @param fields Lista de campos de base de datos
	   * @return La clausula select
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlSelect (String tableName, String aliasTabla, String[] fields) throws ClsExceptions 
	  {
	    StringBuffer sql=new StringBuffer("SELECT ");
	    for(int i=0;i<fields.length; i++)
	    {
	        if (aliasTabla==null)
	        {
	  		  	sql.append(fields[i]);
	  		  	sql.append(", ");
	        }
	        
	        else
	        {
		        if (!fields[i].startsWith("F_SIGA"))
		        {
		            sql.append(aliasTabla + ".");
		  		  	sql.append(fields[i]);
		  		  	sql.append(", ");
		        }
		        
		        else
		        {
		            String cadena = fields[i].substring(fields[i].indexOf("(")+1, fields[i].indexOf(")")); 
		            StringTokenizer st = new StringTokenizer(cadena, ",");
		            String tokens = "";
		             
		            while (st.hasMoreTokens())
		            {
		                String token = st.nextToken();
		                token = "," + aliasTabla + "." + token.trim();
		                tokens += token;
		            }
		            
		            tokens = tokens.substring(1);
		            
		            sql.append(fields[i].substring(0, fields[i].indexOf("(")+1));
		            sql.append(tokens);
		            sql.append(fields[i].substring(fields[i].indexOf(")")));
		            
		            //sql.append(fields[i].substring(0, fields[i].lastIndexOf("(")+1) + aliasTabla + "." + fields[i].substring((fields[i]).lastIndexOf("(")+1,fields[i].length())+2));	            
		        }
	        }
	      //sql.append(aliasTabla!=null && !fields[i].startsWith("F_SIGA") ? aliasTabla + "." : fields[i].substring(0, fields[i].lastIndexOf("(")+1) + aliasTabla + "." + fields[i].substring((fields[i]).lastIndexOf("(")+1,fields[i].length())+2);                                                                 
		}
	    
	    if (aliasTabla==null)
	    {
	        sql.setCharAt(sql.length()-2,' ');
	    }
	    
		sql.append(" FROM ");
	    sql.append(tableName);
	    sql.append(aliasTabla!=null ? " " + aliasTabla + " " : "");
	    return sql.toString();
	  }
	
	  /**
	   * Construye una clausula where con los valores de los campos
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param row Conjunto de valores de los campos
	   * @param keyfields Lista de claves
	   * @return La clausula where
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlWhere (String tableName, Hashtable row, Object[] keyfields) throws ClsExceptions {
	  	
	  	  	StringBuffer sqlWhere = new StringBuffer("");
	  	  	Hashtable dataTypes = DbTypes.getDataTypes(tableName);
	  	  	if (keyfields != null) {
	  		  String aux = " WHERE ";
	  		  for (int i = 0; i < keyfields.length; i++) {
	  		    if (row.get(keyfields[i]) == null) {
	  		      sqlWhere.append(aux + keyfields[i] + " IS NULL ");
	  		    } 
	  		    else 
	  		    	if (row.get(keyfields[i]).toString().trim().equals("")) {
	  		    		sqlWhere.append(aux + keyfields[i] + " IS NULL ");
	  		    	}
	  				else {
	  				  sqlWhere.append(aux + keyfields[i] + " = ");
	  				  if (dataTypes.get(keyfields[i]).equals(DbTypes.STRING)) {
	  				    sqlWhere.append("'" + validateChars(row.get(keyfields[i])) + "' ");
	  				  } else if (dataTypes.get(keyfields[i]).equals(DbTypes.NUMBER)) {
	  				    sqlWhere.append(" " + row.get(keyfields[i]) + " ");
	  				  } else if (dataTypes.get(keyfields[i]).equals(DbTypes.DATE)) {
	  					formatDate(row.get(keyfields[i]));
	  					sqlWhere.append(" TO_DATE('" + row.get(keyfields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
	  				  } else {
	  				    sqlWhere.append(row.get(keyfields[i]).toString());
	  				  }
	  				}
	  				aux = " AND ";
	  		  }
	  	    }
	  	    return sqlWhere.toString();
	  	  }
	  	
	  /**
	   * Construye una clausula 'order by' con los campos que se le pasan
	   * @param fields Lista de campos de base de datos
	   * @return La clausula 'order by'
	   */
	  public static String sqlOrderBy(Object[] fields) {
	
	    StringBuffer sqlOrderBy = null;
	
	    if(fields.length>0){
	      sqlOrderBy=new StringBuffer(" ORDER BY ");
	      for(int i=0;i<fields.length;i++) {
		    sqlOrderBy.append(fields[i]);
		    sqlOrderBy.append(", ");
		  }
	      sqlOrderBy.setCharAt(sqlOrderBy.length()-2,' ');
	    }
	
	    return sqlOrderBy.toString();
	  }
	
	  /**
	   * Construye una clausula insert con los valores de los campos que se le pasan
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param row Conjunto de valores de los campos
	   * @param fieldNames Lista de campos de base de datos
	   * @return La clausula insert
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlInsert(String tableName, Hashtable row, Object[] fieldNames) throws ClsExceptions {
	
	    Hashtable dataTypes = DbTypes.getDataTypes(tableName);
	
	    StringBuffer sqlFields = new StringBuffer(" INSERT INTO " + tableName + "(");
	    StringBuffer sqlValues = new StringBuffer(" ) VALUES (");
	
	    if (fieldNames != null) {
		  String aux = " ";
		  String type=null;
		  for (int i = 0; i < fieldNames.length; i++) {
		  	type=(String)dataTypes.get(fieldNames[i]);
	
		  	if (type.equals(DbTypes.BLOB)) { //para los blob
			  sqlValues.append(aux + " EMPTY_BLOB() ");
			  sqlFields.append(aux + fieldNames[i]);
			  aux = ", ";
			
			}
		  	else 
		  		if (type.equals(DbTypes.CLOB)) { //para los clob
				  sqlValues.append(aux + " EMPTY_CLOB() ");
				  sqlFields.append(aux + fieldNames[i]);
				  aux = ", ";
				
				}
		  		else {
				  if (row.get(fieldNames[i]) == null || row.get(fieldNames[i]).equals("") || ((String)row.get(fieldNames[i])).equalsIgnoreCase("NULL")) {
				  	sqlValues.append(aux + " NULL ");
				  } 
				  else {
				    if (type.equals(DbTypes.STRING)) {
				      sqlValues.append(aux + " '" + validateChars(row.get(fieldNames[i])) + "' ");
				    } 
				    else 
				    	if (type.equals(DbTypes.NUMBER)) {
				    		sqlValues.append(aux + row.get(fieldNames[i]) + " ");
				    	} 
				    	else 
				    		if (type.equals(DbTypes.DATE)) {
				    			if (row.get(fieldNames[i]).toString().equalsIgnoreCase("SYSDATE")) {
				    				sqlValues.append(aux + " " + row.get(fieldNames[i]).toString() + " ");
				    			} 
				    			else {
				    				formatDate(row.get(fieldNames[i]));
				    				sqlValues.append(aux + " TO_DATE('" + row.get(fieldNames[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ");
				    			}
				    		} 
				    		else {
				    			sqlValues.append(aux + " " + row.get(fieldNames[i]).toString() + " ");
				    		}
				  }
				  sqlFields.append(aux + fieldNames[i]);
				  aux = ", ";
		  		}
		  }
		  sqlValues.append(") ");
	    }
	
	    sqlFields.append(sqlValues.toString());
	    return sqlFields.toString();
	  }
	
	  /**
	   * Construye un valor para utilizar en una sentencia de insercion o modificacion
	   * en funcion del tipo de campo
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param fieldName Nombre del campo de base de datos
	   * @param fieldValue Valor del campo
	   * @return El valor del campo transformado
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlField(String tableName, String fieldName, String fieldValue) throws ClsExceptions {
	
	    String sqlValue=fieldValue;
	
		if (fieldName != null) {
		  Hashtable dataTypes = DbTypes.getDataTypes(tableName);
		  String type=(String)dataTypes.get(fieldName);
		
		  if (type.equals(DbTypes.BLOB)) { //para los blob
		  	sqlValue="EMPTY_BLOB()";
		  }
		  else 
		  	if (type.equals(DbTypes.CLOB)) { //para los clob
		  		sqlValue="EMPTY_CLOB()";
		  	}
		  	else{
		  		if (fieldValue == null || fieldValue.equals("") || fieldValue.equalsIgnoreCase("NULL")) {
		  			sqlValue="NULL";
		  		} 
		  		else {
		  			if (type.equals(DbTypes.STRING)) {
		  				sqlValue=" '" + validateChars(fieldValue) + "' ";
		  			} 
		  			else 
		  				if (type.equals(DbTypes.NUMBER)) {
		  					sqlValue=fieldValue;
		  				} 
		  				else 
		  					if (type.equals(DbTypes.DATE)) {
		  						if (fieldValue.equalsIgnoreCase("SYSDATE")) {
		  							sqlValue=fieldValue;
		  						} 
		  						else {
		  							sqlValue="TO_DATE('" + formatDate(fieldValue)+ "', '" + ClsConstants.DATE_FORMAT_SQL + "')";
		  						}
		  					}
		  		}
		  	}
		}
	    return sqlValue;
	  }
	
	  /**
	   * Construye una clausula update con los valores de los campos que se le pasan
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param row Conjunto de valores de los campos
	   * @param updatableFields Lista de campos de base de datos
	   * @return La clusula update
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlUpdate (String tableName, Hashtable row, Object[] updatableFields) throws ClsExceptions {
	
	    Hashtable dataTypes = DbTypes.getDataTypes(tableName);
	    String sql = " UPDATE " + tableName + " SET ";
		String aux = " ";
		String type=null;
	
		if (updatableFields != null) {
			for (int i = 0; i < updatableFields.length; i++) {
				//sql += aux + updatableFields[i] + " = ";
				type=(String)dataTypes.get(updatableFields[i]);
				if (type.equals(DbTypes.CLOB)) { //para los clob
					sql+= aux + updatableFields[i] + "= EMPTY_CLOB() ";
				}
				else
				if (row.get(updatableFields[i]) == null) {
					sql += aux + updatableFields[i] + " = NULL ";
				}
				else 
					if (row.get(updatableFields[i]).equals("")) {
						sql += aux + updatableFields[i] + " = NULL ";
					}
					else 
						if( ((String)row.get(updatableFields[i])).equalsIgnoreCase("NULL")) {
							sql += aux + updatableFields[i] + " = NULL ";
						}
						else {
							if (dataTypes.get(updatableFields[i]).equals(DbTypes.STRING)) {
								sql += aux + updatableFields[i] + " = " + "'" + validateChars(row.get(updatableFields[i])) + "' ";
							}
							else 
								if (dataTypes.get(updatableFields[i]).equals(DbTypes.NUMBER)) {
									sql += aux + updatableFields[i] + " = " + " " + row.get(updatableFields[i]) + " ";
								}
								else 
									if (dataTypes.get(updatableFields[i]).equals(DbTypes.DATE)) {
										if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) {
											sql += aux + updatableFields[i] + " = "+
											row.get(updatableFields[i]).toString() + " ";
											//" SYSTIMESTAMP ";
										} 
										else {
											//TEMPORAL: test the passed date is in standard format
											formatDate(row.get(updatableFields[i]));
											sql += aux + updatableFields[i] + " = " + " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
										}
									} 
									else {
										sql += row.get(updatableFields[i]).toString();
									}
						}
				aux = ", ";
			}
	    }
	    return sql;
	  }
	
	  /**
	   * Construye una clausula update con los valores de los campos que se le pasan (no se
	   * incluirán los valores que lleguen a nulo o a blanco).
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param row Conjunto de valores de los campos
	   * @param updatableFields Lista de campos de base de datos
	   * @return La clusula update
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlUpdateSelective(String tableName, Hashtable row, Object[] updatableFields) throws ClsExceptions {
	
	    Hashtable dataTypes = DbTypes.getDataTypes(tableName);
	    String sql = " UPDATE " + tableName + " SET ";
	    // UPDATE SENTENCE!
	    String aux = " ";
	
	    if (updatableFields != null) {
	    	for (int i = 0; i < updatableFields.length; i++) {
	    		if (row.get(updatableFields[i]) == null) {
	    		}
	    		else 
	    			if (row.get(updatableFields[i]).equals("")) {
	    			}
	    			else 
	    				if( ((String)row.get(updatableFields[i])).equalsIgnoreCase("NULL")) {
	    					sql += aux + updatableFields[i] + " = NULL ";
	    				}
	    				else {
	    					if (dataTypes.get(updatableFields[i]).equals(DbTypes.STRING)) {
	    						sql += aux + updatableFields[i] + " = " + "'" + validateChars(row.get(updatableFields[i])) + "' ";
	    					}
	    					else 
	    						if (dataTypes.get(updatableFields[i]).equals(DbTypes.NUMBER)) {
	    							sql += aux + updatableFields[i] + " = " + " " + row.get(updatableFields[i]) + " ";
	    						}
	    						else 
	    							if (dataTypes.get(updatableFields[i]).equals(DbTypes.DATE)) {
	    								if (row.get(updatableFields[i]).toString().equalsIgnoreCase("SYSDATE")) {
	    									sql += aux + updatableFields[i] + " = "+
											row.get(updatableFields[i]).toString() + " ";
	    									//" SYSTIMESTAMP ";
	    								}
	    								else {
	    									formatDate(row.get(updatableFields[i]));
	    									sql += aux + updatableFields[i] + " = " + " TO_DATE('" + row.get(updatableFields[i]) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
	    								}
	    							} 
	    							else {
	    								sql += row.get(updatableFields[i]).toString();
	    							}
	    				}
	    		aux = ", ";
	    	}
	    }
	    return sql;
	  }
	
	
	  /**
	   * Construye una clausula 'connect by prior' dada una lista de valores iniciales
	   * @param con Objeto Connection con el que se controla la transacción
	   * @param tableName Nombre de la tabla en base de datos
	   * @param start Nombre del campo que filtra el inicio
	   * @param values Conjunto de valores posibles del campo de inicio
	   * @param leftField Nombre del campo hijo
	   * @param rightField Nombre del campo padre
	   * @return La clausula 'connect by prior'
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  public static String sqlConnectByPriorIn(String tableName, String start, String values, String leftField, String rightField) throws ClsExceptions {
	    StringBuffer sqlWhere = new StringBuffer("");
		String aux = " START WITH ";
		sqlWhere.append(aux + start + " IN ( " + values + ")");
		sqlWhere.append( " CONNECT BY PRIOR " + leftField +  "=" + rightField ) ;
	    return sqlWhere.toString();
	  }
	
	  /**
	   * Convierte en cadena un objeto cuyo tipo original representa una fecha en base de datos
	   * @param date objeto de tipo Date,Timestamp,Time...
	   * @return Cadena fecha con el formato por defecto para la aplicación
	   * @throws GenericException Excepción genérica controlada por la aplicación
	   */
	  private static String formatDate(Object date) throws ClsExceptions {
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
	
		} 
		catch (Exception e) {
			ClsExceptions es=new ClsExceptions(e, "DATE FORMAT MUST BE LIKE '" +  ClsConstants.DATE_FORMAT_SQL + "'");
			es.setErrorCode("DATEFORMAT");
			throw es;
	    }
	    return val;
	  }

	  //**************************************************************************//
	  //***********************  PRIVADAS   **************************************//
	  //**************************************************************************//
	  private static String replaceBlanks(String str) {
	    byte[] aux = str.trim().getBytes();
	    int j=0;
	    for(int i=0; i<aux.length;){
	      aux[j++]=aux[i];
	      if(aux[i++]==' ')
	      	while(aux[i]==' ')i++;
	    }
	    return new String(aux,0,j);
	  }

	  public static String validateChars(Object cad) {
		  cad = replacePattern(replaceBlanks(cad.toString()),"'","''");
		  cad = replacePattern(replaceBlanks(cad.toString()),"(","\\(");
		  cad = replacePattern(replaceBlanks(cad.toString()),")","\\)");
	    return cad.toString();
	  }

	  private static String replacePattern(String str, String pattern, String replace) {
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
	  /**
	   * 
	   * @param idInstitucion
	   * @param valorClaveUnica Valor del campo que es clave unica
	   * @param campoClaveUnica Campo de la clave unica
	   * @param htPktabla Hashtable donde la clave son los campos de las tablas y el elemento su valor
	   * @param htPkSignos Hashtable donde la metemos el =,<>,>,> son signos para aplcair a los campos de la tabla con su valor
	   * @param tabla
	   * @param tipoMantenimiento 1 matenimiento tipo 1.Solo para la institucion
	   * 		tipoMantenimiento 2 matenimiento tipo 2.Solo para el CGAE
	   * 		tipoMantenimiento 3 matenimiento tipo 3.Para CGAE e Institucion
	   *        tipoMantenimiento 4 matenimiento tipo 4.Para la Institucion y todos sus padres
	   * @param lenguaje
	   * @return
	   * @throws ClsExceptions
	   */
	  
	  public static boolean isClaveUnicaMultiIdioma(String idInstitucion, String valorClaveUnica, String campoClaveUnica,
			  Hashtable htPktabla, Hashtable htPkSignos, String tabla, int tipoMantenimiento, String lenguaje) throws ClsExceptions{
		  StringBuffer sSQL = new StringBuffer();
		  boolean isClaveUnica = true;
		  Hashtable htCodigos = new Hashtable();
		  int contador = 0;
		  sSQL.append(" SELECT count(1) AS CLAVEUNICA");   
		  sSQL.append("  FROM ");
		  sSQL.append(tabla);
		  sSQL.append("  WHERE TRIM(upper (f_siga_getrecurso(");
		  sSQL.append(campoClaveUnica);
		  sSQL.append(",");
		  contador ++;
		  htCodigos.put(new Integer(contador), lenguaje);
		  sSQL.append(":");
		  sSQL.append(contador);
		  sSQL.append("))) = ");
		  contador ++;
		  htCodigos.put(new Integer(contador), valorClaveUnica.toUpperCase().trim());
		  sSQL.append(":");
		  sSQL.append(contador);
		  Iterator itPkTabla = htPktabla.keySet().iterator();
		  while (itPkTabla.hasNext()) {
			  String campoPkTabla = (String) itPkTabla.next();
			  Object valorPkTabla = htPktabla.get(campoPkTabla);
			  String signo = (String)htPkSignos.get(campoPkTabla);
			  sSQL.append(" AND ");
			  sSQL.append(campoPkTabla);
			  sSQL.append(signo);
			  contador ++;
			  htCodigos.put(new Integer(contador), valorPkTabla);
			  sSQL.append(":");
			  sSQL.append(contador);

			  /*if(valorPkTabla instanceof Number){
					sSQL += " AND   "+ campoPkTabla +" <> "+valorPkTabla+" ";
				}else{
					sSQL += " AND   "+ campoPkTabla +" <> '"+valorPkTabla+"' ";

				}*/


		  }
		  switch (tipoMantenimiento) {
		  case 1:
			  sSQL.append(" AND IDINSTITUCION =");
			  contador ++;
			  htCodigos.put(new Integer(contador), idInstitucion);
			  sSQL.append(":");
			  sSQL.append(contador);

			  break;
		  case 2:
			  sSQL.append(" AND IDINSTITUCION = ");
			  contador ++;
			  htCodigos.put(new Integer(contador), "2000");
			  sSQL.append(":");
			  sSQL.append(contador);

			  break;
		  case 3:
			  sSQL.append(" AND IDINSTITUCION IN (");
			  contador ++;
			  htCodigos.put(new Integer(contador), "2000");
			  sSQL.append(":");
			  sSQL.append(contador);
			  sSQL.append(",");
			  contador ++;
			  htCodigos.put(new Integer(contador), idInstitucion);
			  sSQL.append(":");
			  sSQL.append(contador);
			  sSQL.append(")");
			  break;
		  case 4:
			  sSQL.append(" AND IDINSTITUCION IN (SELECT IDINSTITUCION ");
			  sSQL.append(" FROM CEN_INSTITUCION START WITH IDINSTITUCION = ");
			  contador ++;
			  htCodigos.put(new Integer(contador), idInstitucion);
			  sSQL.append(":");
			  sSQL.append(contador);
			  sSQL.append(" CONNECT BY PRIOR  CEN_INST_IDINSTITUCION = IDINSTITUCION)");
			  
			  
			  break;
		  default:
			  break;
		  }

		  RowsContainer rc = new RowsContainer();
		  try {
			  rc.findBind(sSQL.toString(),htCodigos);
			  Row row = (Row)rc.get(0);
			  if (row.getString("CLAVEUNICA")!=null && !row.getString("CLAVEUNICA").equals("0"))
				  isClaveUnica = false;	
		  } catch (Exception e) {
			  isClaveUnica = true;
		  }


		  return isClaveUnica;
	  }
	  public static boolean isClaveUnica(String idInstitucion, String valorClaveUnica, String campoClaveUnica,
			  Hashtable htPktabla, Hashtable htPkSignos, String tabla, int tipoMantenimiento) throws ClsExceptions{
		  StringBuffer sSQL = new StringBuffer();
		  boolean isClaveUnica = true;
		  Hashtable htCodigos = new Hashtable();
		  int contador = 0;
		  sSQL.append(" SELECT count(1) AS CLAVEUNICA");   
		  sSQL.append("  FROM ");
		  sSQL.append(tabla);
		  sSQL.append("  WHERE TRIM(UPPER(");
		 
		  sSQL.append(campoClaveUnica);
		  sSQL.append(")) = ");
		  contador ++;
		  htCodigos.put(new Integer(contador), valorClaveUnica.toUpperCase().trim());
		  sSQL.append(":");
		  sSQL.append(contador);
		  
		  Iterator itPkTabla = htPktabla.keySet().iterator();
		  while (itPkTabla.hasNext()) {
			  String campoPkTabla = (String) itPkTabla.next();
			  Object valorPkTabla = htPktabla.get(campoPkTabla);
			  String signo = (String)htPkSignos.get(campoPkTabla);
			  sSQL.append(" AND ");
			  sSQL.append(campoPkTabla);
			  sSQL.append(signo);
			  contador ++;
			  htCodigos.put(new Integer(contador), valorPkTabla);
			  sSQL.append(":");
			  sSQL.append(contador);

			  /*if(valorPkTabla instanceof Number){
					sSQL += " AND   "+ campoPkTabla +" <> "+valorPkTabla+" ";
				}else{
					sSQL += " AND   "+ campoPkTabla +" <> '"+valorPkTabla+"' ";

				}*/


		  }
		  switch (tipoMantenimiento) {
		  case 1:
			  sSQL.append(" AND IDINSTITUCION =");
			  contador ++;
			  htCodigos.put(new Integer(contador), idInstitucion);
			  sSQL.append(":");
			  sSQL.append(contador);

			  break;
		  case 2:
			  sSQL.append(" AND IDINSTITUCION = ");
			  contador ++;
			  htCodigos.put(new Integer(contador), "2000");
			  sSQL.append(":");
			  sSQL.append(contador);

			  break;
		  case 3:
			  sSQL.append(" AND IDINSTITUCION IN (");
			  contador ++;
			  htCodigos.put(new Integer(contador), "2000");
			  sSQL.append(":");
			  sSQL.append(contador);
			  sSQL.append(",");
			  contador ++;
			  htCodigos.put(new Integer(contador), idInstitucion);
			  sSQL.append(":");
			  sSQL.append(contador);
			  sSQL.append(")");
			  break;
		  default:
			  break;
		  }

		  RowsContainer rc = new RowsContainer();
		  try {
			  rc.findBind(sSQL.toString(),htCodigos);
			  Row row = (Row)rc.get(0);
			  if (row.getString("CLAVEUNICA")!=null && !row.getString("CLAVEUNICA").equals("0"))
				  isClaveUnica = false;	
		  } catch (Exception e) {
			  isClaveUnica = true;
		  }


		  return isClaveUnica;
	  }
	  public static ArrayList quitaCamposQuery(String queryInicial, Hashtable codigos) {
			String marcaInicial = "SELECT";
			String marcaFinal = "FROM";
			String marcaUNION = "UNION";
			String marcaGROUP = "GROUP";
			String marcaDISTINCT = "DISTINCT";
			String marcaBind = ":";
			
			Hashtable codigosNuevo = new Hashtable();
			ArrayList retorno = new ArrayList();
			
			boolean continuar = true;
			String texto = queryInicial.toUpperCase();

			if (texto.indexOf(marcaUNION)!=-1 || texto.indexOf(marcaGROUP)!=-1 || texto.indexOf(marcaDISTINCT)!=-1) {
			    ClsLogging.writeFileLog("WARNING paginador: Quitar campos de query: No se puede simplificar porque contiene clausulas DISTINCT, UNION o GROUP BY.",7);
				retorno.add(queryInicial);
				retorno.add(comprobarCodigosQuery(queryInicial,codigos));
			    return retorno;
			}

			String textoTratar = "";
			int inicio=texto.indexOf(marcaInicial);
			int fin=texto.indexOf(marcaFinal);
			if(inicio!=-1 && fin!=-1){
			    textoTratar=texto.substring(inicio+marcaInicial.length(),fin);
			}
			
			while(continuar){
				int abrir = textoTratar.split("\\(").length-1;
			    int cerrar = textoTratar.split("\\)").length-1;
			    
				if (abrir<cerrar) {
				    ClsLogging.writeFileLog("WARNING paginador: Quitar campos de query: No se puede simplificar porque contiene más paréntesis cerrados que abiertos.",7);
					retorno.add(queryInicial);
					retorno.add(comprobarCodigosQuery(queryInicial,codigos));
				    return retorno;
				    
				} else if (abrir>cerrar) {
				    // hay que seguir buscando
				    continuar=true;
				    fin=texto.indexOf(marcaFinal,fin+marcaFinal.length());
					if(inicio!=-1 && fin!=-1){
					    textoTratar=texto.substring(inicio+marcaInicial.length(),fin);
					} else {	
					    ClsLogging.writeFileLog("WARNING paginador: Quitar campos de query: No se puede simplificar porque contiene más paréntesis abiertos que cerrados.",7);
						retorno.add(queryInicial);
						retorno.add(comprobarCodigosQuery(queryInicial,codigos));
					    return retorno;
					}
					
				} else {
				    continuar=false;
				}
			
			}
			
			String campos = queryInicial.substring(inicio+marcaInicial.length(),fin);
			texto = UtilidadesString.replaceFirstIgnoreCase(queryInicial,campos," 1 ");

			// Revisión de variables bind 
			if (codigos!=null) {
			    int posVbleBind=-1;
			    boolean salir=false;
			    int inicial = 0;
			    String vbleBind = "";
			    //obtengo la primera variable bind
			    posVbleBind = texto.indexOf(marcaBind);
			    int indice = 1;
			    while (posVbleBind!=-1) {
			        int posfin = posVbleBind+3;
			        try {
			            vbleBind = texto.substring(posVbleBind+1,posfin);
			        } catch (StringIndexOutOfBoundsException siabe) {
			            posfin = posVbleBind+2;
			            vbleBind = texto.substring(posVbleBind+1,posfin);
			        }
			        while (!esNumerico(vbleBind) && (vbleBind.length()>1)) {
			            posfin--;
			            vbleBind = texto.substring(posVbleBind+1,posfin);
			        }
			        if (esNumerico(vbleBind) && !vbleBind.trim().equals("00") && !vbleBind.trim().equals("0") && !vbleBind.trim().equals("59")) {
			            Integer test = new Integer(vbleBind);
			            inicial = test.intValue();
			            // cambio la variable en la consulta
			            texto = UtilidadesString.replaceFirstIgnoreCase(texto,marcaBind+vbleBind,marcaBind+indice);
			            // grabo el valor en el nuevo hashtable de codigos
			            codigosNuevo.put(new Integer(indice), codigos.get(new Integer(test.intValue())));
			            indice++;
			        }
			        posVbleBind = texto.indexOf(marcaBind, posfin);
			    }
			} else {
			    codigosNuevo = codigos;
			}
			
			retorno.add(texto);
			retorno.add(codigosNuevo);
			return retorno;

		}
	  
	  public static boolean esNumerico(String numero) {
	      try {
	          Integer test = new Integer(numero);
	          return true;
	      }catch (NumberFormatException nfe) {
	          return false;
	      }
	  }
	  
	  /**
	   * 
	   * @param query
	   * @param codigos
	   * @return
	   */
	  private static Hashtable comprobarCodigosQuery(String query, Hashtable codigos) {
		  Hashtable codigosNuevo = new Hashtable();
		  int indice = 1;
		  
		  if (codigos!=null) {
			  Enumeration e = codigos.keys();
			  while (e.hasMoreElements()) {
				  Integer key = (Integer)e.nextElement();
				  if (query.indexOf(":"+key) >= 0) {	
					   codigosNuevo.put(key, codigos.get(key));
					   indice++;
				  }
			  }
			  
			  return codigosNuevo;
			  
		  } else {
			  return null;
		  }
		  
	  }
}
