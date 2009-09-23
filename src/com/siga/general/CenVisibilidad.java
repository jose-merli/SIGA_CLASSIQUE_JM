// VERSIONES:
// raul.ggonzalez 28-12-2004 Creacion
//

package com.siga.general;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

/**
 * Clase que carga la visibilidad de las diferentes instituciones
 * para la comprobación de visibilidad de instituciones desde otros 
 * puntos de la aplicación. Esta clase es un Singleton.
 * @author AtosOrigin  
 */

public class CenVisibilidad
{
  // hastable con la visibilidad de instituciones
  private static Hashtable hasInstituciones;
  private static Hashtable hasNombres;
  private static Hashtable hasAbreviaturas;
  private static Hashtable visibilidaCampos=new Hashtable();
  
  static {
  	try {
  		CenVisibilidad.rellenaHashVisibilidadCampos();
  		
/*		String sql = " SELECT "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NIFCIF+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NATURALDE+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDESTADOCIVIL+" , " + 
		 "  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARACTER+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FOTOGRAFIA+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_SEXO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_PUBLICIDAD+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_GUIAJUDICIAL+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ABONOSBANCO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_CARGOSBANCO+" , " +
		 "  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_ASIENTOCONTABLE+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_COMISIONES+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDTRATAMIENTO+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_FECHANACIMIENTO+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDLENGUAJE+" ,  "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDTIPOIDENTIFICACION+" ,  "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_FECHAALTA+"  " +
		 " FROM   " +CenClienteBean.T_NOMBRETABLA + ",  "+CenPersonaBean.T_NOMBRETABLA +
		 " WHERE   "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+"  = "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA+"  " +
		 " AND    "+CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION +" = " + 2032 +
		 " AND   "+CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_IDPERSONA+" = " + 2222;		  
		parseSql(sql,"S");  */
  		
/*  		String sql = " SELECT "+CenPersonaBean.C_NOMBRE+" ,  "+CenPersonaBean.C_APELLIDOS1+" ,  "+
		                        CenPersonaBean.C_APELLIDOS2+" ,  "+CenPersonaBean.C_NIFCIF+" ,  "+
								CenPersonaBean.C_NATURALDE+" ,  "+CenPersonaBean.C_IDESTADOCIVIL+" , " + 
		 " FROM   " +CenPersonaBean.T_NOMBRETABLA +
		 " WHERE   " + CenPersonaBean.C_IDPERSONA+" = " + 2222;
  		parseSql(sql,"S");  */
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  }
  
  /**
   * Carga en una hastable la visibilidad de instituciones para cada 
   * institución dada de alta en el sistema y el nivel de profundidad
   * de la institución dentro de la jerarquia:
   * 1.- Si es CGAE
   * 2.- Institución que cualga directamente de CGAE.
   * 3.- Institución que cuelga de un consejo autónomo
   */
  public static void init() throws ClsExceptions
  {
  	String sqlInstituciones = "";
  	String sqlVisibilidad = "";
  	
  	// no se ha cargado. Lo cargamos una sola vez
  	hasInstituciones = new Hashtable();
  	hasNombres = new Hashtable();
  	hasAbreviaturas = new Hashtable();
	
  	// Acceso a BBDD
	RowsContainer rcInstituciones = null;
	RowsContainer rcVisibilidad = null;
	try { 
		// consulta de insituciones
		sqlInstituciones = "SELECT LEVEL,IDINSTITUCION, NOMBRE, ABREVIATURA " + 
		" FROM CEN_INSTITUCION " + 
		" START WITH CEN_INST_IDINSTITUCION is null " +
		" CONNECT BY PRIOR IDINSTITUCION = CEN_INST_IDINSTITUCION";

		rcInstituciones = new RowsContainer(); 
		if (rcInstituciones.query(sqlInstituciones)) {
			for (int i = 0; i < rcInstituciones.size(); i++)	{
				Row filaInstituciones = (Row) rcInstituciones.get(i);
				String id = (String)filaInstituciones.getValue("IDINSTITUCION");
				String level = (String)filaInstituciones.getValue("LEVEL");
				String nombre = (String)filaInstituciones.getValue("NOMBRE");
				String abreviatura = (String)filaInstituciones.getValue("ABREVIATURA");
				if (id!=null) {
				    Hashtable codigos = new Hashtable();
				    codigos.put(new Integer(1),id.toString());

					sqlVisibilidad = "SELECT LEVEL,IDINSTITUCION " + 
					" FROM CEN_INSTITUCION " + 
					" START WITH IDINSTITUCION=:1" +
					" CONNECT BY PRIOR IDINSTITUCION = CEN_INST_IDINSTITUCION";
					rcVisibilidad = new RowsContainer(); 
					if (rcVisibilidad.queryBind(sqlVisibilidad, codigos)) {
						String instituciones = "";
						for (int j = 0; j < rcVisibilidad.size(); j++)	{
							Row filaVisibilidad = (Row) rcVisibilidad.get(j);
							String inst = (String)filaVisibilidad.getValue("IDINSTITUCION");
							instituciones += inst + ",";
						}
						// quito la ultima coma
						if (instituciones.length()>0) {
							instituciones = instituciones.substring(0,instituciones.length()-1);
						}
						ArrayList valor = new ArrayList();
						valor.add(level);
						valor.add(instituciones);
						hasInstituciones.put(id,valor);
					}
					hasNombres.put(id,nombre);
					hasAbreviaturas.put(id,abreviatura);
				}
			}
		}
	} 
	catch (Exception e) { 	
		throw new ClsExceptions (e, "Error al cargar la visibilidad de instituciones."); 
	}
  }
  
  /**
   * Obtiene el conjunto de instituciones visibles para una determinada
   * institucion.
   *  
   * @param idInstitucion Institucion para ver su visibilidad
   * @return java.lang.String Conjunto de identificadores de
   * institucion que puede ver la institucion introducida como parametro.
   * Se presentan separados por comas para formar parte de una clausula IN
   * dentro de una consulta de base de datos.  
   * @throws com.atos.utils.ClsExceptions Caso de error general 
   */
  public static String getVisibilidadInstitucion (String idInstitucion) throws ClsExceptions
  {
  	try {
  		ArrayList valor = (ArrayList) hasInstituciones.get(idInstitucion);
  		if (valor!=null) {
  			return (String) valor.get(1);
  		} else {
  			return null;
  		}
	} 
	catch (Exception e) { 	
		throw new ClsExceptions (e, "Error al obtener la visibilidad de una institución."); 
	}
  }

  /**
   * Obtiene el nivel de la institución pasada como parámetro dentro
   * de la jerarquía de instituciones.
   *  
   * @param idInstitucion Institucion para ver su visibilidad.
   * @return String Código de nivel de la institución:
   * <br>1.- Si es CGAE.
   * <br>2.- Institución que cualga directamente de CGAE.
   * <br>3.- Institución que cuelga de un consejo autónomo
   * @throws com.atos.utils.ClsExceptions Caso de error general 
   */
  public static String getNivelInstitucion (String idInstitucion) throws ClsExceptions  {
  	try {
  		ArrayList valor = (ArrayList) hasInstituciones.get(idInstitucion);
  		if (valor!=null) {
  			return (String) valor.get(0);
  		} else {
  			return null;
  		}
	} 
	catch (Exception e) { 	
		throw new ClsExceptions (e, "Error al obtener el nivel de una institución."); 
	}
  }

  public static void rellenaHashVisibilidadCampos() throws ClsExceptions {
  	visibilidaCampos=new Hashtable();	
  	// Acceso a BBDD
	RowsContainer rcVisibilidad = null;
	try { 
		// consulta de insituciones
		String sql="select c.idcampos IDCAMPO, adc.caracter CARACTER, c.descripcion CAMPO, c.tabla TABLA"+ 
						" from cen_campos c, cen_accesodatos_campos adc where c.idcampos=adc.idcampos";
		
		rcVisibilidad = new RowsContainer(); 
		if (rcVisibilidad.query(sql)) {
			for (int i = 0; i < rcVisibilidad.size(); i++)	{
				Row row = (Row) rcVisibilidad.get(i);
				if (row!=null) {
					String id = ((String)row.getValue("IDCAMPO")).toUpperCase();
					String caracter = ((String)(String)row.getValue("CARACTER")).toUpperCase();
					String campo = (String)row.getValue("CAMPO");
					String tabla = ((String)row.getValue("TABLA")).toUpperCase();
					Hashtable hashTablas=(Hashtable)visibilidaCampos.get(caracter);
					if (hashTablas==null) {
						hashTablas=new Hashtable();
						visibilidaCampos.put(caracter,hashTablas);
					}
					Hashtable campos=(Hashtable)hashTablas.get(tabla);
					if (campos==null) {
						campos=new Hashtable();
						hashTablas.put(tabla,campos);
					}
					if (campo!=null)
						campos.put(campo.toUpperCase(),campo.toUpperCase());
				}
			}
		}
	} catch (Exception e) { 	
		throw new ClsExceptions (e, "Error al cargar la visibilidad de instituciones."); 
	}
  }
  
  public static String toStringVisibilidadCampos() {
  	String toRet="Visibilida de campos obtenida de BBDD\n";
  	Enumeration e=visibilidaCampos.keys();
  	while (e.hasMoreElements()) {
  		String caracter=(String)e.nextElement();
  		toRet+="CARACTER " + caracter + "\n";
  		Hashtable tablas=(Hashtable)visibilidaCampos.get(caracter);
  		if (tablas!=null) {
  			Enumeration tabla=tablas.keys();
  		  	while (tabla.hasMoreElements()) {
  		  		String tab=(String)tabla.nextElement();
  		  		toRet+="  TABLA " + tab + "\n";
  		  		Hashtable campos=(Hashtable)tablas.get(tab);
  		  		Enumeration cam=campos.keys(); 
  		  		while(cam.hasMoreElements()) {
  		  			toRet+="    Campo " + cam.nextElement() + "\n";
  		  		}
  		  	}
  		}
  	}
  	toRet+="*************************************";
  	return toRet;
  }

  public static String parseSql(String sql, String caracter) throws ClsExceptions {
  	if (sql==null || sql.length()==0)
  		throw new ClsExceptions("CenVisibilidad::parseSQL -> SQL NO VALIDA ("+sql+")");

    Hashtable tablasDDBB=(Hashtable)visibilidaCampos.get(caracter);
    if (tablasDDBB==null) {
    	return sql; 
    }
  	String sqlUpper=sql.toUpperCase().trim();
    int indexFrom = sqlUpper.indexOf(" FROM ");
    if (indexFrom==-1) 
    	throw new ClsExceptions("CenVisibilidad::parseSQL -> SQL NO VALIDA ("+sqlUpper+")");
    
    int indexWhere = sqlUpper.indexOf(" WHERE ");
    if (indexWhere==-1)
    	indexWhere=sqlUpper.length()-1;
    
    String tablasQuery=sqlUpper.substring(indexFrom+6,indexWhere);

    
    // CAMBIO: String[] tablas=getTokens(tablasQuery,",");
    // RGG 17-02-2005 En lugar de obtener los nombres de tabla mediante
    // tokens de comas los vamos a parsear pensando que pueden estar definidos
    // como JOIN (LEFT, INNER, OUTER y RIGTH) para cubrir los diferentes 
    // tipos de consultas.
    String[] tablas = getNombresTablas(tablasQuery);

    
    
    String camposQuery=sqlUpper.substring(6,indexFrom);
    String[] campos=getTokens(camposQuery,",");
    Vector quitados=new Vector();
    for (int h=0;h<tablas.length;h++) {
    	Hashtable camposDDBB=(Hashtable)tablasDDBB.get(tablas[h]);
    	if (camposDDBB!=null)
    		quitaCampos(quitados, campos, camposDDBB, tablas[h]);  	
    }

    String newQuery="SELECT ";
    newQuery+=campos[0];
    for (int i=1;i<campos.length;i++) {
    	newQuery+=","+campos[i];
    }
    
    newQuery+=sqlUpper.substring(indexFrom);
  	return newQuery;
  }
  
  private static String[] getTokens(String cadena,String sep) {
    StringTokenizer tok=new StringTokenizer(cadena,sep);
    int tablasCount=tok.countTokens();
    String[] tablas=new String[tablasCount];
    int i=0;
    while (tok.hasMoreTokens()) {
    	tablas[i++]=tok.nextToken().trim();
    }
    return tablas;
  }
  
  private static void quitaCampos(Vector camposQuitados, String[] queryCampos, Hashtable camposDDBB, String tabla) {

  	// RGG 16-02-2005 MODIFICACION PARA QUE NO SE LLAME EL SINONIMO COMO TABLA.CAMPO SI NO COMO CAMPO A SECAS 
  	for (int i=0;i<queryCampos.length;i++) {
  		if (camposDDBB.get(queryCampos[i])!=null) {
  			// RGG cambio para qu el sinonimo nuevo se llame solo como
  			// el nombre del campo y no como tabla.campo
  			String tablaCampo=null;
  			String campoCampo=null;
  			int indexPunto=queryCampos[i].indexOf(".");
  			if (indexPunto==-1) continue;
  			tablaCampo=queryCampos[i].substring(0,indexPunto);
  			campoCampo=queryCampos[i].substring(indexPunto+1);
  			String campo=queryCampos[i];
  			camposQuitados.add(queryCampos[i]);
  			//queryCampos[i]=" ' ' AS " + campo;
  			queryCampos[i]=" ' ' AS " + campoCampo;

  		} else {
  			String tablaCampo=null;
  			String campoCampo=null;
  			int indexPunto=queryCampos[i].indexOf(".");
  			if (indexPunto==-1) continue;
  			tablaCampo=queryCampos[i].substring(0,indexPunto);
  			campoCampo=queryCampos[i].substring(indexPunto+1);
  			if (!tabla.equalsIgnoreCase(tabla)) continue;
  			if (camposDDBB.get(campoCampo)!=null) {
  	  			camposQuitados.add(queryCampos[i]);
  	  			// RGG cambio para qu el sinonimo nuevo se llame solo como
  	  			// el nombre del campo y no como tabla.campo
  	  			//queryCampos[i]=" ' ' AS \"" + queryCampos[i] + "\"";
  	  			queryCampos[i]=" ' ' AS \"" + campoCampo + "\"";

  			}
  		}
    }
  }
 
  /**
   * Funcion que obtiene los nombres de las tablas desde la parte FROM de una sentencia 
   * Las sentencias pueden venir con join mediante comas, o mediante las clausulas JOIN
   * o no tener ninguna join implementada
   * @param sql String con la sentencia 
   * @return String[] con los nombres delas tablas 
   */
  private static String[] getNombresTablas(String sql) {
  	String salida[] = null;
  	
  	
  	// primero miro si tiene comas
  	if (sql.indexOf(",")!=-1) {
  		// utilizo la funcion anterior
  		return getTokens(sql,",");
  	} else {
  	  	// miro a ver si tiene JOIN
  		if (sql.indexOf(" JOIN ")!=-1) {
  			// tratamiento para JOIN
  			//salida = getTokens(sql,"JOIN"); 
	  	    int indexToken = sql.indexOf(" JOIN ");
	  	    String valores = "";
	  	    while (indexToken!=-1) {
	  	    	valores += sql.substring(0,indexToken) + ",";
	  	    	sql = sql.substring(indexToken + 6,sql.length());
	  	    	indexToken = sql.indexOf(" JOIN ");
	  	    }
	  	    valores += sql;
	  	    salida =  getTokens(valores,",");
  			// recorro los token para limpiarlos
  			for (int i=0;i<salida.length;i++) {
  				// quito los LEFT, RIGHT, INNER y OUTER
  				salida[i] = salida[i].replaceAll("INNER","");
  				salida[i] = salida[i].replaceAll("OUTER","");
  				salida[i] = salida[i].replaceAll("LEFT","");
  				salida[i] = salida[i].replaceAll("RIGHT","");
  				// quito los parentesis
  				salida[i] = salida[i].replaceAll("\\c(","");
  				salida[i] = salida[i].replaceAll("\\c)","");
  				// quito los ON y lo que hay a su derecha
  				int indexOn = salida[i].indexOf(" ON ");
  				if (indexOn!=-1) {
  					salida[i] = salida[i].substring(0,indexOn);
  				}
  				salida[i] = salida[i].trim();
  			}
  			return salida;
  		} else {
  			// no tiene joins de ningun tipo
  			salida = new String[1];
  			salida[0] = sql.trim(); // ???
  			return salida;
  		}
  	}
  }

  /**
   * Devuelve el nombre de la institucion
   * @param idInstitucion a buscar
   * @return Nombre de la institucion
   */
  public static String getNombreInstitucion(String idInstitucion) {
  	return (String) hasNombres.get(idInstitucion);
  }
  /**
   * Devuelve la abreviatura de la institucion
   * @param idInstitucion a buscar
   * @return Abreviatura de la institucion
   */
  public static String getAbreviaturaInstitucion(String idInstitucion) {
  	return (String) hasAbreviaturas.get(idInstitucion);
  }
 
  /**
   * comprueba la visibilidad de un campo concreto
   * @param tabla
   * @param campo 
   * @param caracter
   * @return true en caso de ser visible
   */
  public static boolean esVisibleCampo(String tabla, String campo, String caracter) {
  	boolean salida = true;
  	Hashtable tablas = null;
  	Hashtable campos = null;
  	if (visibilidaCampos.containsKey(caracter)) {
  		tablas = (Hashtable) visibilidaCampos.get(caracter);
  		if (tablas.containsKey(tabla)) {
  			campos = (Hashtable) tablas.get(tabla);
  			if (campos.containsKey(campo)) {
  				salida = false;
  			}
  		}
  	}
  	return salida;
  }
    
  /**
   * Comprueba si una institucion tiene hijas. 
   * @param idInstitucion Institucion para comprobar su nivel
   * @return boolean - true en caso afirmativo
   * @throws com.atos.utils.ClsExceptions Caso de error general 
   */
  public static boolean tieneHijos (String idInstitucion) throws ClsExceptions
  {
  	boolean salida = false;
  	// Acceso a BBDD
	RowsContainer rc = null;
	try { 
		// consulta de insituciones
		String sql = "select IDINSTITUCION from CEN_INSTITUCION where CEN_INST_IDINSTITUCION = " + idInstitucion; 

		rc = new RowsContainer(); 
		if (rc.query(sql)) {
			if (rc.size()>0) {
				salida = true;
			}
		}
		return salida;
	}
	catch (Exception e) { 	
		throw new ClsExceptions (e, "Error de acceso a B.D."); 
	}
  }


}