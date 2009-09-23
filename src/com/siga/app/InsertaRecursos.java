/*
 * Created on Dec 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


//import javax.servlet.http.HttpServletRequest;

//import com.atos.utils.ClsConstants;
//import com.atos.utils.ClsLogging;

/**
 * @author raul.ggonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsertaRecursos {

    private static Connection con = null;
    private static ResultSet rs = null;
    private static Statement st = null;
    
	public static void main(String[] args) {
     
	try {
     	
	    conecta();
     	
     	if (args==null || args[0]==null) {
			System.out.println("Debe introducir el nombre del fichero de recursos y el nombre de fichero de salida");
		} else {
			
			Properties properties = new Properties();
		    try {
		        properties.load(new FileInputStream(args[0]));
		    } catch (IOException e) {
		    	System.out.println(e.toString());
		    	throw e;
            }
		    Enumeration enumer = properties.keys();

		    StringBuffer trace = new StringBuffer();

		    while (enumer.hasMoreElements()) {
		    	String clave = (String) enumer.nextElement();
		    	String valor = (String)properties.getProperty(clave);
		    	if (valor!=null) {
		    	    if (!existeIdRecurso(clave)) {
			    		//trace.append("\n delete GEN_RECURSOS where IDRECURSO='"+clave+"';");
			    		trace.append("\n insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('"+clave+"', '"+valor+"', 0, '1', sysdate, 0, '19');");
			    		trace.append("\n insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('"+clave+"', '"+valor+"#GL', 0, '4', sysdate, 0, '19');");
			    		trace.append("\n insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('"+clave+"', '"+valor+"#CA', 0, '2', sysdate, 0, '19');");
			    		trace.append("\n insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('"+clave+"', '"+valor+"#EU', 0, '3', sysdate, 0, '19');");
			    		trace.append("\n");
		    	    } else {
		    	        // caso de pregunta o update
		    		    InputStreamReader isr=new InputStreamReader(System.in);
		    	        BufferedReader br=new BufferedReader(isr);
		    	        System.out.println("Se ha encontrado la clave "+clave);
		    	        System.out.println("Es una modificación de recurso? (s/n)");
		    		    String texto = br.readLine();
		    	        if (texto.equals("s")) {
				    		trace.append("\n update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='"+valor+"' where idrecurso='"+clave+"' and idlenguaje='1';"); 
				    		trace.append("\n update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='"+valor+"#GL' where idrecurso='"+clave+"' and idlenguaje='4';"); 
				    		trace.append("\n update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='"+valor+"#CA' where idrecurso='"+clave+"' and idlenguaje='2';"); 
				    		trace.append("\n update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='"+valor+"#EU' where idrecurso='"+clave+"' and idlenguaje='3';"); 
				    		trace.append("\n");
		    	        } else {
		    	            System.out.println("ATENCIÓN!!! Estas usando un idrecurso que ya existe. TE LO VAS A CARGAR!!");
		    	            System.out.println("Anda, empieza de nuevo. La clave duplicada es "+clave);
		    	            System.out.println("Adiós");
		    	            break;
		    	            
		    	        }

		    	    }
		    	}
		    }

		    try {
			    if (args[1]!=null) {
				    PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));
			        printer.println(trace.toString());
			        printer.flush();
			        printer.close();
			    } else {
			    	System.out.println("NO EXISTE FICHERO DE SALIDA");
			    	System.out.println(trace.toString());
			    }
			
		    } catch (IOException e) {
		    	System.out.println(e.toString());
		    	throw e;
	        }
			
		    System.out.println("fin correcto");

		}
	} catch (Exception ex) {
		ex.printStackTrace(System.out);
	}
	}
	

	private static void conecta () throws Exception {
	 	ResourceBundle rp=ResourceBundle.getBundle("SIGA");
	 	String claseConexion = rp.getString("directorios.bd.clase");
	 	String cadenaConexion = rp.getString("directorios.bd.cadenaConexion");
	 	String usuario = rp.getString("directorios.bd.usu");
	 	String clave = rp.getString("directorios.bd.pwd");

        con = null;
        Class.forName(claseConexion);
        con = DriverManager.getConnection(cadenaConexion, usuario, clave);
	}	
	
	private static ResultSet consulta (String query) throws Exception {
		st=null;
        st = con.createStatement();
        rs = st.executeQuery(query);
		return rs;
        
	}
	private static boolean existeIdRecurso(String id) throws Exception 
	{
	  try{
		
      	Vector salida = new Vector();
		String query = "SELECT IDRECURSO FROM GEN_RECURSOS WHERE upper(IDRECURSO)=UPPER('"+id+"')"; 
		rs = consulta(query);
		if (rs.next()) {
	 		return true; 
		} else {
		    return false;
		}
      
      } finally 
	  {
      	try {
      	rs.close();
      	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
      }
	}

	
}
