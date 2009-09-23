/*
 * Created on 07-nov-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.app;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
//import java.util.ResourceBundle;
import java.util.Vector;

import com.atos.utils.ClsLogging;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesString;
//import com.siga.beans.CenInstitucionBean;
import com.siga.beans.GenParametrosBean;

/**
 * @author raulg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class testRaul {

    
	
	/** Path origen de las plantillas */
	private static String pathOrigenPlantillas = "";
	/** vector con el conjunto de instituciones a tratar */
	private static Vector instituciones = new Vector();
	/** booleano que dice si hay que hacerlo para todas las instituciones */
	private static boolean todasInstituciones = false;
	/** Conexion a base de datos */
	private static Connection con = null;
	/** Statement para base de datos */
	private static Statement st = null;
	/** clase de conexion */
	private static String claseConexion = null;
	/** cadena de conexion */
	private static String cadenaConexion = null;
	/** clave de conexion */
	private static String clave = null;
	/** Institucion usada por defecto cuando no exista la tratada */
	private static String institucionDefecto = null;
	
	/** si es el usuario weblogic */
	private static boolean bWeblogic = false;
	/** si es el usuario oracle */
	private static boolean bOracle = false;
	private static ResultSet rs = null; 	
	
	private static String path=null;
	private static String tablespace=null;
	private static String usuario=null;
	private static String entorno=null;
	private static String idInstitucionMantener=null;
	
	public static void main(String[] args)  {
		try {
		    
		    //configuración (No olvidar modificar la cadena de conexión)
		    idInstitucionMantener="2010";
		    path="C:\\Documents and Settings\\raulg.ITCGAE\\Escritorio\\20090716\\borradoMurcia\\";
		    entorno = "PRETEST";
		    usuario= "uscgaetest";
		    scriptBorradoInstitucion();
		    
		} catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}
		
	}


	
	private static void scriptIndices() throws Exception {
	    
	    File fichero = new File(path+usuario.toLowerCase()+"."+entorno+".correccion.creaIndicesNLSSORT.sql");
	    BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
	    
	  
	  try {

	    conecta();
	  	String a = "select i.index_name, i.table_name, c.column_name, (select 1 from user_tab_columns ct where ct.TABLE_NAME = c.table_name and ct.COLUMN_NAME = c.column_name and ct.DATA_TYPE = 'VARCHAR2') campos_varchar2 " +
	  	    	   	" from user_indexes i, user_ind_columns c " +
	    	   		"  where i.table_name = c.table_name " +
	    	   		"    and i.index_name = c.index_name " +
	    	   		//"    and i.table_owner = 'uscgae' " +
	    	   		"  order by i.index_name, i.table_name, c.column_position ";
	  	
	  	rs = consulta(a);
	  	bw.write("spool "+usuario.toLowerCase()+"."+entorno+".correccion.creaIndicesNLSSORT.LOG; "+"\n");
  		bw.write("prompt SCRIPT DE CREACION DE ÍNDICES CON ORDENACIÓN NLSSORT"+"\n");
  		bw.write(""+"\n");
  		
	  	String indiceAnterior = "";
	  	int cuenta = 0;
	  	int contador = 0;
	  	String campos = "";
	  	int varchar2 = 0;
	  	String tablaAnterior="";
	  	String campoAnterior="";
	  	String varcharAnterior="";
	  	
	  	ArrayList errores= new ArrayList();
	  	ArrayList pks= new ArrayList();
	  	ArrayList erroresPks= new ArrayList();
	  	
  		while (rs.next()) {
  		    cuenta++;
  		    String indice = rs.getString("index_name");
  	        String tabla = rs.getString("table_name");
  	        String campo = rs.getString("column_name");
  	        String varchar = rs.getString("campos_varchar2");
  	        
  	        
  	        if (contador==0 || indice.equals(indiceAnterior)) {
	  	        // leo y acumulo
	  	        
	  	        
	  	    } else {
	  	        
	  	        campos = campos.substring(0,campos.length()-1);

	  	        if (campos.indexOf("SYS")!=-1) {
	  	            if (esConstraintPK(indiceAnterior)) {
	  	                // ES UNA PK, HAY QUE CREAR UN NUEVO INDICE 
	  	                String nuevoNombre = "";
	  	                if (indiceAnterior.length()>20) {
	  	                    nuevoNombre=indiceAnterior.substring(0,indiceAnterior.length()-8) + "_" +  UtilidadesString.formateaFicheros(new Integer(cuenta),3,true) + "_NLS "; 
	  	                } else {
	  	                    nuevoNombre=indiceAnterior + "_" + UtilidadesString.formateaFicheros(new Integer(cuenta),3,true) + "_NLS ";
	  	                }
	  	                String sentencia = "CREATE INDEX "+nuevoNombre +  
		  	            " on "+tablaAnterior+" ("+campos+") " + 
		  	            " tablespace "+tablespace+" " + 
		  	            "   pctfree 10 " + 
		  	            "   initrans 2 " + 
		  	            "   maxtrans 255 " + 
		  	            "   storage " + 
		  	            "   ( " + 
		  	            "     initial 64K " + 
		  	            "     minextents 1 " + 
		  	            "     maxextents unlimited " + 
		  	            "   );                   ";
			  	        erroresPks.add(sentencia);
			  	        
	  	                
	  	            } else {
	  	                // NO ES UNA PK, borramos el indice y lo creamos nuevo. 
			  	        //	  	            bw.write("prompt recreacion del indice "+indiceAnterior);
		  	            errores.add("DROP INDEX "+indiceAnterior+";");
			  	        String sentencia = "CREATE INDEX "+indiceAnterior+" " +  
			  	            " on "+tablaAnterior+" ("+campos+") " + 
			  	            " tablespace "+tablespace+" " + 
			  	            "   pctfree 10 " + 
			  	            "   initrans 2 " + 
			  	            "   maxtrans 255 " + 
			  	            "   storage " + 
			  	            "   ( " + 
			  	            "     initial 64K " + 
			  	            "     minextents 1 " + 
			  	            "     maxextents unlimited " + 
			  	            "   );                   ";
			  	        errores.add(sentencia);
	  	            }
	  	        } else {
			  	    
		  	        if (varchar2>0)  {
		  	            if (esConstraintPK(indiceAnterior)) {
		  	                //	ES UNA PK, HAY QUE CREAR UN NUEVO INDICE 
		  	                String nuevoNombre = "";
		  	                if (indiceAnterior.length()>20) {
		  	                    nuevoNombre=indiceAnterior.substring(0,indiceAnterior.length()-8) + "_" +  UtilidadesString.formateaFicheros(new Integer(cuenta),3,true) + "_NLS "; 
		  	                } else {
		  	                    nuevoNombre=indiceAnterior  + "_" +  UtilidadesString.formateaFicheros(new Integer(cuenta),3,true) + "_NLS ";
		  	                }
				  	        String sentencia = "CREATE INDEX "+nuevoNombre +
				  	            " on "+tablaAnterior+" ("+campos+") " + 
				  	            " tablespace "+tablespace+" " + 
				  	            "   pctfree 10 " + 
				  	            "   initrans 2 " + 
				  	            "   maxtrans 255 " + 
				  	            "   storage " + 
				  	            "   ( " + 
				  	            "     initial 64K " + 
				  	            "     minextents 1 " + 
				  	            "     maxextents unlimited " + 
				  	            "   );                   ";
				  	        pks.add(sentencia);
		  	            } else {
		  	                // NO ES UNA PK, borramos el indice y lo creamos nuevo. 
			  	            //bw.write("prompt recreacion del indice "+indiceAnterior);
			  	            bw.write("DROP INDEX "+indiceAnterior+";"+"\n");
				  	        String sentencia = "CREATE INDEX "+indiceAnterior+" " +  
				  	            " on "+tablaAnterior+" ("+campos+") " + 
				  	            " tablespace "+tablespace+" " + 
				  	            "   pctfree 10 " + 
				  	            "   initrans 2 " + 
				  	            "   maxtrans 255 " + 
				  	            "   storage " + 
				  	            "   ( " + 
				  	            "     initial 64K " + 
				  	            "     minextents 1 " + 
				  	            "     maxextents unlimited " + 
				  	            "   );                   ";
				  	        bw.write(sentencia+"\n");		  	                
		  	            }
		  	        }
	  	        }
  	            

	  	        varchar2 = 0;
	  	        campos = "";
	  	        
	  	    }
  	        
  	        varchar2 += (varchar!=null || campo.indexOf("SYS_")!=-1)?1:0; 
  	        
  	        if (varchar!=null || campo.indexOf("SYS_")!=-1) {
  	  	        campos += "NLSSORT(("+campo+"),'nls_sort=''GENERIC_BASELETTER'''),";
  	        } else {
  	  	        campos += campo + ",";
  	        }

  	        tablaAnterior = tabla;
  	        campoAnterior = campo;
  	        varcharAnterior = varchar;
	  	    indiceAnterior = indice;
	  	    contador++;

	  	}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- indices PK"+"\n");
  		for (int i=0;i<pks.size();i++) {
  		  bw.write((String)pks.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- indices PK pendientes de revisión (CAMPOS COMPUESTOS)"+"\n");
  		for (int i=0;i<erroresPks.size();i++) {
  		  bw.write((String)erroresPks.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- pendientes de revisión (CAMPOS COMPUESTOS)"+"\n");
  		for (int i=0;i<errores.size();i++) {
  		  bw.write((String)errores.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write("prompt HECHO."+"\n");
  		bw.write("spool off; "+"\n");
  		bw.flush();
	  	
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
    	try {
        	con.close();
       	} catch (Exception e) {}
       	try {
        	bw.close();
       	} catch (Exception e) {}
      	
      }
	}
		
	private static void scriptIndices2() throws Exception {
	    
	    File fichero = new File(path+usuario.toLowerCase()+"."+entorno+".correccion.borraIndicesNLSSORT.sql");
	    BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
	    
	  
	  try {

	    conecta();
	  	String a = "select i.index_name, i.table_name, c.column_name, (select 1 from user_tab_columns ct where ct.TABLE_NAME = c.table_name and ct.COLUMN_NAME = c.column_name and ct.DATA_TYPE = 'VARCHAR2') campos_varchar2 " +
	  	    	   	" from user_indexes i, user_ind_columns c " +
	    	   		"  where i.table_name = c.table_name " +
	    	   		"    and i.index_name = c.index_name " +
	    	   		"    and i.table_owner = '"+usuario+"' " +
	    	   		"  order by i.index_name, i.table_name, c.column_position ";
	  	
	  	rs = consulta(a);
	  	bw.write("spool "+usuario.toLowerCase()+"."+entorno+".correccion.borraIndicesNLSSORT.LOG; "+"\n");
  		bw.write("prompt SCRIPT DE BORRADO DE ÍNDICES CON ORDENACIÓN NLSSORT"+"\n");
  		bw.write(""+"\n");
  		
	  	String indiceAnterior = "";
	  	int cuenta = 0;
	  	int contador = 0;
	  	String campos = "";
	  	int varchar2 = 0;
	  	String tablaAnterior="";
	  	String campoAnterior="";
	  	String varcharAnterior="";
	  	
	  	ArrayList errores= new ArrayList();
	  	ArrayList pks= new ArrayList();
	  	ArrayList erroresPks= new ArrayList();
	  	
  		while (rs.next()) {
  		    cuenta++;
  		    String indice = rs.getString("index_name");
  	        String tabla = rs.getString("table_name");
  	        String campo = rs.getString("column_name");
  	        String varchar = rs.getString("campos_varchar2");
  	        
  	        if (contador==0 || indice.equals(indiceAnterior)) {
	  	        // leo y acumulo
	  	        
	  	        
	  	    } else {
	  	        
	  	        campos = campos.substring(0,campos.length()-1);

	  	        
	  	        if (indiceAnterior.endsWith("NLS")) {
	  	            bw.write("DROP INDEX "+indiceAnterior+";"+"\n");
	  	    	}
	  	     

	  	        varchar2 = 0;
	  	        campos = "";
	  	        
	  	    }
  	        
  	        varchar2 += (varchar!=null || campo.indexOf("SYS_")!=-1)?1:0; 
  	        
  	        if (varchar!=null || campo.indexOf("SYS_")!=-1) {
  	  	        campos += "NLSSORT(("+campo+"),'nls_sort=''GENERIC_BASELETTER'''),";
  	        } else {
  	  	        campos += campo + ",";
  	        }

  	        tablaAnterior = tabla;
  	        campoAnterior = campo;
  	        varcharAnterior = varchar;
	  	    indiceAnterior = indice;
	  	    contador++;

	  	}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- indices PK"+"\n");
  		for (int i=0;i<pks.size();i++) {
  		  bw.write((String)pks.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- indices PK pendientes de revisión (CAMPOS COMPUESTOS)"+"\n");
  		for (int i=0;i<erroresPks.size();i++) {
  		  bw.write((String)erroresPks.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		bw.write("-- pendientes de revisión (CAMPOS COMPUESTOS)"+"\n");
  		for (int i=0;i<errores.size();i++) {
  		  bw.write((String)errores.get(i)+"\n");
  		}
  		bw.write(""+"\n");
  		bw.write("prompt HECHO."+"\n");
  		bw.write("spool off; "+"\n");
  		bw.flush();
	  	
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
    	try {
        	con.close();
       	} catch (Exception e) {}
       	try {
        	bw.close();
       	} catch (Exception e) {}
      	
      }
	}
		
	private static boolean esConstraintPK(String nombreConstraint)throws Exception {
	  Statement st2=null;
	  ResultSet rs2=null;
	  try{
	    String consultaSQL = " select 1 from user_constraints where owner='"+usuario+"' and constraint_name='"+nombreConstraint+"' and (constraint_type='P' OR constraint_type='U')";
	    st2=null;
        st2 = con.createStatement();
        rs2 = st2.executeQuery(consultaSQL);

	    if (rs2.next()) {
  		    return true;
	    } else {
	        return false;
	    }
	  } finally 
 	  {
	    	try {
	        	rs2.close();
	       	} catch (Exception e) {}
	    	try {
	        	st2.close();
	       	} catch (Exception e) {}
      }	    
	}
	
	private static  ResultSet consulta (String query) throws Exception {
		st=null;
        st = con.createStatement();
        rs = st.executeQuery(query);
		return rs;
        
	}



	private static void conecta () throws Exception {
        con = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        if (entorno.equals("DESA2"))
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.11.55:1521:SIGADES", "uscgae2", "uscgae2");
        if (entorno.equals("DESA"))
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.11.55:1521:SIGADES", "uscgae", "uscgae");
        if (entorno.equals("PRE"))
            con = DriverManager.getConnection("jdbc:oracle:thin:@10.60.3.96:1526:SIGAPRE", "uscgae", "uscgae");
        if (entorno.equals("PRETEST"))
            con = DriverManager.getConnection("jdbc:oracle:thin:@10.60.3.96:1526:SIGAPRE", "uscgaetest", "uscgae");
        if (entorno.equals("PRO"))
            con = DriverManager.getConnection("jdbc:oracle:thin:@10.60.3.82:1527:SIGAPRO", "uscgae", "uscgae");
        
	}


	private static void hacer3() {
		SIGALogging logger = new SIGALogging("C:\\fichero_de_prueba.log.xls");
		logger.writeLogGestorColaSincronizarDatos(SIGALogging.ERROR,new Integer(2000),new Long(2000000001),"Raul","Esto es un ERROR"); 
		logger.writeLogGestorColaSincronizarDatos(SIGALogging.INFO,new Integer(2000),new Long(2000000001),"Raul","Esto es un INFO"); 
	}
	
	private  static String valorParam (String institucion,String modulo,String nombre,String valorDefecto) throws Exception 
	{
      try{
        String salida = valorDefecto;
		String query = "SELECT " + GenParametrosBean.C_VALOR + " FROM " + GenParametrosBean.T_NOMBRETABLA + " WHERE " + GenParametrosBean.C_MODULO + "='" +modulo+"' AND " + GenParametrosBean.C_PARAMETRO + "='" + nombre  +"' AND " +GenParametrosBean.C_IDINSTITUCION +"=" + institucion; 
		rs = consulta(query);
		if (rs.next()) {
			salida = rs.getString(GenParametrosBean.C_VALOR);
		} else {
	      	try {
	          	rs.close();
	        } catch (Exception e) {}
	    	try {
	        	st.close();
	       	} catch (Exception e) {}
			// si no existe para mi institucion lo busco para la 0
			query = "SELECT " + GenParametrosBean.C_VALOR + " FROM " + GenParametrosBean.T_NOMBRETABLA + " WHERE " + GenParametrosBean.C_MODULO + "='" +modulo+"' AND " + GenParametrosBean.C_PARAMETRO + "='" + nombre  +"' AND " +GenParametrosBean.C_IDINSTITUCION +"=0"; 
			rs = null;
			rs = consulta(query);
			if (rs.next()) {
				salida = rs.getString(GenParametrosBean.C_VALOR);
			}			
		}
		return salida;
      
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

	private static void hacer() throws Exception {
		
	  
	  try {

	  	conecta();
	  	String kk = "";
	  	String a = "SELECT   '0' salto from dual";
	  	rs = consulta(a);
  		System.out.println("SALTO:");
  		System.out.println("---------------------------------------------------");
	  	while (rs.next()) {
	  		kk = rs.getString("SALTO");
	  		Hashtable ht = new Hashtable();
	  		ht.put("kk",kk);
	  		System.out.println("salto: |"+ht.get("kk")+"|");
	  		System.out.println("equals? "+kk.equalsIgnoreCase("0"));
	  		System.out.println("equals2? "+(kk!=null && kk.equalsIgnoreCase("0")));
	  		
	  		if (kk!=null && kk.equalsIgnoreCase("0")) {
	  			System.out.println("La bola entro");
	  		}
	  		
	  		//System.out.print(" | ");
	  		//System.out.print(rs.getString(2));
	  		
	  		System.out.println(" ");
	  	}

  		System.out.println("=====================");

	  	
	  	
/*	  	
		Vector salida = new Vector();
		ResourceBundle rp=ResourceBundle.getBundle("SIGA");

		if (bWeblogic) {
		
			//informes descarga , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"INF","PATH_INFORMES_DESCARGA",null);
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
		
		}

		if (bWeblogic) {
		
			// SJCS
			String path = rp.getString("sjcs.directorioFisicoSJCSJava");
			path += File.separator + rp.getString("sjcs.directorioSJCSJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
		}

		
	*/
    
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
    	try {
        	con.close();
       	} catch (Exception e) {}
      }
	}
		
	private static void script() throws Exception {
		
	  
	  try {

	  	conecta();
	  	String a = "select constrain.table_name, constrain.constraint_name, constrain.column_name from "+
	  	    " (select ind.index_name, ind.table_name, icol.column_name, icol.column_position from user_ind_columns icol,  user_indexes ind "+ 
	  	    " where icol.index_name = ind.index_name "+
	  	    " and ind.index_name not like 'PK%' "+
	  	    " order by ind.index_name, icol.column_position) indices, "+
	  	    " (select con.constraint_name, con.table_name, ccol.column_name from User_Cons_Columns ccol,  USER_CONSTRAINTS con "+ 
	  	    " where ccol.constraint_name = con.constraint_name "+
	  	    " and con.constraint_type='R' "+
	  	    " order by con.constraint_name, ccol.position) constrain "+
	  	    " where indices.table_name (+) = constrain.table_name "+
	  	    " and  indices.column_name (+) = constrain.column_name "+ 
	  	    " and indices.index_name is null "+
	  	    " order by constrain.table_name, constrain.constraint_name, indices.index_name, indices.column_position ";
	  	
	  	rs = consulta(a);
  		System.out.println("spool uscgaetest.PRE.correccion.creaIndicesFK.LOG; ");
  		System.out.println("prompt SCRIPT DE CREACION DE ÍNDICES EN FOREIGN KEYS ");
	  	String constraintAnterior = "";
	  	String tablaAnterior = "";
	  	int cuenta = 0;
	  	int contador = 0;
	  	String campos = "";
  		while (rs.next()) {
  		    cuenta++;
	  	    String tabla = rs.getString("TABLE_NAME");
	  	    String constraint = rs.getString("CONSTRAINT_NAME");
	  	    String campo = rs.getString("COLUMN_NAME");
	  	    if (contador==0 || constraint.equals(constraintAnterior)) {
	  	        campos += campo + ",";
	  	    } else {
	  	        campos = campos.substring(0,campos.length()-1);
	  	        String indice = "SI_" + UtilidadesString.formateaFicheros(new Integer(cuenta),3,true) + "_" + constraintAnterior.substring(0,constraintAnterior.length()-7);
	  	        String sentencia = "create index "+indice+" " +  
	  	            " on "+tablaAnterior+" ("+campos+") " + 
	  	            " tablespace TS_SIGAINT_IDX " + 
	  	            "   pctfree 10 " + 
	  	            "   initrans 2 " + 
	  	            "   maxtrans 255 " + 
	  	            "   storage " + 
	  	            "   ( " + 
	  	            "     initial 64K " + 
	  	            "     minextents 1 " + 
	  	            "     maxextents unlimited " + 
	  	            "   );                   ";
	  	        System.out.println(sentencia);
	  	        campos = campo + ",";
	  	        contador=0;
	  	    }
	  	    contador++;
	  	    constraintAnterior = constraint;
	  	    tablaAnterior = tabla;

	  	}

  		System.out.println("prompt HECHO.");
  		System.out.println("spool off; ");

	  	
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
    	try {
        	con.close();
       	} catch (Exception e) {}
      }
	}
		
	private static void hacer2() throws Exception {
		
	  
		try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Documents and Settings\\raulg.ITCGAE\\Escritorio\\20090612\\A127m.n34"));
            PrintWriter bw = new PrintWriter(new FileWriter("C:\\Documents and Settings\\raulg.ITCGAE\\Escritorio\\20090612\\A127m2.n34")); 
            
            String read = "";
            while((read = br.readLine()) != null) {

                read = read.toUpperCase();
                read = UtilidadesString.quitarAcentos((read));
                read = UtilidadesString.validarModelo34(read);
                bw.println(read);
            }
            
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally 
		{
	  	
 	  }
	}

	private static void scriptBorradoInstitucion() throws Exception {
	    
	    File fichero = new File(path+usuario.toLowerCase()+"."+entorno+".borrado.inicializacionMurcia.sql");
	    BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
	    
	  
	  try {

	    conecta();
	  	String a = "select distinct table_name from user_tab_columns where column_name='IDINSTITUCION' and table_name not like 'V_SIGA%' and table_name not like 'BIN%' order by table_name ";
	  	
	  	rs = consulta(a);
	  	bw.write("spool "+usuario.toLowerCase()+"."+entorno+".borrado.inicializacion."+idInstitucionMantener+".LOG; "+"\n");
  		bw.write("prompt SCRIPT DE INICIALIZACION DE DATOS PARA MURCIA "+"\n");
  		bw.write(""+"\n");
  		
	  	int cuenta = 0;
	  	
  		while (rs.next()) {
  		    cuenta++;
  		    String tabla = rs.getString("table_name");
  	        
  	        
            String sentencia = "DELETE FROM  "+ tabla + " WHERE IDINSTITUCION not in ("+idInstitucionMantener+",2000,0); ";  
  	        bw.write(sentencia+"\n");		  	                
	  	        

	  	}
  		bw.write(""+"\n");
  		bw.write(""+"\n");
  		

  		bw.write("prompt HECHO."+"\n");
  		bw.write("spool off; "+"\n");
  		bw.flush();
	  	
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
    	try {
        	con.close();
       	} catch (Exception e) {}
       	try {
        	bw.close();
       	} catch (Exception e) {}
      	
      }
	}
		

	
}
