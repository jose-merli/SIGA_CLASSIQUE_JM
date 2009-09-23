package com.siga.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlDispatcher extends HttpServlet {
  //static final private String CONTENT_TYPE = "text/html";
  //Global vars
  public void init() throws ServletException {
  }
  //Process a HTTP GET request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ClsLogging.writeFileLog("GET METHOD Dispatcher",request,3);
    response.setHeader("Cache-Control","no-store"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", -1); //prevents caching at the proxy server

    doPost(request,response);
  }
  //Process a HTTP POST request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ClsLogging.writeFileLog("POST METHOD Dispatcher",request,3);

    HttpSession ses = request.getSession();
    if (ses.isNew())
    {
      String url1="/jsp/index.jsp?error=3";
      ClsLogging.writeFileLogError("SESION CADUCADA!!!",request,3);
      RequestDispatcher rd = request.getRequestDispatcher(url1);
      ClsLogging.writeFileLog("Redireccionando a: "+url1,request,3);
      rd.forward(request, response);
      return;
    }

    ClsLogging.writeFileLog("Tiempo Máximo de Inactividad: "+ses.getMaxInactiveInterval(),request,3);

    boolean auth=true;
    boolean forward=false;
    String[] prof=null;
    String url="";

    UsrBean usrbean=(UsrBean)ses.getAttribute("USRBEAN");

    if (usrbean!=null) {
		String lan=usrbean.getLanguage();
      	String country="ES";
      	try{
        	Object obj = ses.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
        	if (obj != null)
          		country = ( (java.util.Locale) obj).getCountry();
      	}catch(Exception e) {
        	ClsLogging.writeFileLogError("Error obteniendo LOCALE, "+e.toString(),request,3);
      	}

      	ses.setAttribute( org.apache.struts.Globals.LOCALE_KEY, new java.util.Locale(lan,country));
      	ClsLogging.writeFileLog("LOCALE:  Lenguaje/Pais:"+lan+"/"+country,request,3);
    } else  {
    	ClsLogging.writeFileLog("Nueva instancia de USRBEAN!",request,1);
      	usrbean=new UsrBean();
    }
    String proc = request.getParameter("process");
//    ClsLogging.writeFileLog("Proceso SIGA: "+proc,request,3);
    if (proc == null) {
    	proc = "";
		auth=false;
    } else if (proc.equals("-2")) {
      	url="/Auth";
      	ClsLogging.writeFileLog("Redireccionando a Servlet de Autenticación",request,3);
      	RequestDispatcher rd = request.getRequestDispatcher(url);
      	rd.forward(request, response);
      	return;
    }
    ClsLogging.writeFileLog("SESION VALIDA!!!",request,3);

    String id = request.getParameter("id");
    if (id == null) {
      	id = "NO_ID";
    }

    String type = request.getParameter("type");
    if (type == null) {
      	type = "NO_TYPE";
    }

//    ClsLogging.writeFileLog("ID Session: "+ses.getId(),request,3);

    if(usrbean!=null){
      	prof=usrbean.getProfile();
      	ClsLogging.writeFileLog("Perfil SIGA: "+prof+", Proceso SIGA: "+proc+", id SIGA: "+id+", type SIGA: "+type,request,3);
    } else {
      	url="/jsp/index.jsp?error=3";
      	ClsLogging.writeFileLogError("SESION DE USUARIO NO ENCONTRADA!!!",request,3);

      	RequestDispatcher rd = request.getRequestDispatcher(url);
      	ClsLogging.writeFileLog("Redireccionando a: "+url,request,3);
      	rd.forward(request, response);
      	return;
    }

    int access_type=0;
    String transaction="";
    String desc="";
    String transaction_aux="";

    /******************************/
    /** CHK RIGHTS & TRANSACTION **/
    /******************************/
    access_type=chkAccessType(proc,request,prof,access_type);
    transaction=chkTransaction(proc,request);
    
    int n=transaction.indexOf("@@");
	transaction_aux=transaction.substring(0,n);
    desc=transaction.substring(transaction_aux.length()+2,transaction.length());

    ClsLogging.writeFileLog("SIGA Access_type: "+access_type+", Transaccion: "+transaction_aux+", Descripcion: "+desc,request,3);
    /***************/

    String access="";

    if (auth==false) {
      url="/jsp/login.jsp?error=3";
      ClsLogging.writeFileLogError("Perfil '"+prof+"' no está en la sesión",request,3);
    } else if ((auth==true) && (forward==false)) {
        switch(access_type) {
          case 1: {
            access="DENIED";
            break;
          }
          case 2: {
            access="READ";
            break;
          }
          case 3: {
            access="FULL";
            break;
          }
          default: {
			access="DENIED"; 
			break;
          }
            
         }

         url="/"+transaction_aux;

		 usrbean.setDescTrans(desc);
		 usrbean.setStrutsTrans(transaction_aux);
         usrbean.setAccessType(access);
      }

      boolean processnotvalid=false;
      boolean processdenied=false;
      if (!access.equals("DENIED")) {
        /* Chk if DENIED */
        ClsLogging.writeFileLog("Redireccionando a: "+url,request,3);
      } else {
        //url="/jsp/PSSCDeneidPage.jsp?msg='Access Denied!'";
        ClsLogging.writeFileLogError("ACCESO DENEGADO!!",request,3);
      }
      
      if (processnotvalid){
	      	PrintWriter out = response.getWriter();
			String contextPath=request.getContextPath();
	      	response.setContentType("text/html"); // Servlet Staff
	      	//out.println("<HTML><HEAD><SCRIPT>alert(\"Process not valid!\");window.close();</SCRIPT></HEAD></HTML>");
	      	out.println("<HTML><HEAD><SCRIPT>alert(\"Acceso denegado!\");document.location='"+contextPath+"/jsp/common/blank.jsp';</SCRIPT></HEAD></HTML>");
	      	out.flush();
	      	out.close();
      } else if (processdenied) {
	      	PrintWriter outd = response.getWriter();
			String contextPath=request.getContextPath();
	      	response.setContentType("text/html"); // Servlet Staff
	      	outd.println("<HTML><HEAD><SCRIPT>alert(\"Acceso denegado!\");document.location='"+contextPath+"/jsp/common/blank.jsp';</SCRIPT></HEAD></HTML>");
	      	outd.flush();
	      	outd.close();
      } else {
	        RequestDispatcher rd = request.getRequestDispatcher(url);
	        rd.forward(request, response);
      }
      return;
  }

  private int chkAccessType(String proc, HttpServletRequest request, String[] prof, int access_type) {
  	
  	String perfiles="";
  	for (int i=0;i<prof.length;i++) {
  		perfiles+=",'"+prof[i]+"'";
  	}
  	if (perfiles.length()>0) perfiles=perfiles.substring(1);
  	
    String queryAccess="select "+ColumnConstants.FN_ACCESS_RIGHT_VALUE+
						" from "+TableConstants.TABLE_ACCESS_RIGHT+
						" where "+ColumnConstants.FN_ACCESS_RIGHT_PROCESS+"="+proc+
    					" and "+ColumnConstants.FN_ACCESS_RIGHT_PROFILE+" in ("+perfiles+")";
    

    String profileLevel="select P1."+ColumnConstants.FN_PROFILE_ID_PROFILE+
						" from "+TableConstants.TABLE_PROFILE +" P1, "+
								 TableConstants.TABLE_PROFILE +" P2 "+
						" where P1."+ColumnConstants.FN_PROFILE_PROFILE_LEVEL+" < P2."+ColumnConstants.FN_PROFILE_PROFILE_LEVEL+
						" AND P2."+ColumnConstants.FN_PROFILE_ID_PROFILE+"='"+prof+"'";

    //ClsLogging.writeFileLog("queryAccess: "+queryAccess,request,3);

    Connection con= null;
    Statement stmtAccess=null;
    ResultSet rsAccess=null;
    try{
      con= ClsMngBBDD.getReadConnection();
      stmtAccess=con.createStatement();
      //ClsLogging.writeFileLog("SQL ACCESO -->"+queryAccess,request, 3);
      rsAccess=stmtAccess.executeQuery(queryAccess);

      while(rsAccess.next()){
        access_type=rsAccess.getInt(ColumnConstants.FN_ACCESS_RIGHT_VALUE);
        //ClsLogging.writeFileLog("Access_right: "+access_type,request,3);
      }
      rsAccess.close();
      rsAccess=null;

	  //ClsLogging.writeFileLog("SQL PERFIL -->"+profileLevel,request, 3);
      rsAccess=stmtAccess.executeQuery(profileLevel);
      if (!rsAccess.next()) {
        access_type = 3;
      }
    }catch (Exception e){
      //ORA-00942: tabla o vista no encontrada
      //ORA-00923: palabra clave FROM no encontrada donde se esperaba
      //ORA-00933: comando SQL no terminado correctamente
//                 throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
      ClsLogging.writeFileLogError("ERROR: "+e.toString(),request,1);

    }finally{

     try {
	   if (rsAccess!=null)       rsAccess.close();	
       if (stmtAccess!=null)     stmtAccess.close();
       ClsMngBBDD.closeConnection(con);
     }
     catch (Exception ex) {
      ClsLogging.writeFileLogError("ERROR cerrando la conexion: "+ex.toString(),request,1);
     }
    }

    return access_type;
  }


  private String chkTransaction(String proc, HttpServletRequest request)
  {
    String transaction="";
    String desc="";
	Statement stmtTran=null;
	ResultSet rsTran=null;

    String queryTran="select "+ColumnConstants.FN_PROCESS_TRANS+ ", " + ColumnConstants.FN_PROCESS_DESC +
					 " from "+TableConstants.TABLE_PROCESS+
					 " where "+ColumnConstants.FN_PROCESS_ID+"="+proc;

    Connection con= null;

    try{
      con= ClsMngBBDD.getReadConnection();
      stmtTran=con.createStatement();
	  //ClsLogging.writeFileLog("SQL TRANSACCION -->"+queryTran, 3);
      rsTran=stmtTran.executeQuery(queryTran);

      while(rsTran.next()){
        transaction=rsTran.getString(ColumnConstants.FN_PROCESS_TRANS);
        desc=rsTran.getString(ColumnConstants.FN_PROCESS_DESC);
        //ClsLogging.writeFileLog(ColumnConstants.FN_PROCESS_TRANS+" : "+transaction,request,3);
		//ClsLogging.writeFileLog(ColumnConstants.FN_PROCESS_DESC+" : "+desc,request,3);
      }
      rsTran.close();
      stmtTran.close();


    }catch (Exception e){
      //ORA-00942: tabla o vista no encontrada
      //ORA-00923: palabra clave FROM no encontrada donde se esperaba
      //ORA-00933: comando SQL no terminado correctamente
//                 throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
      ClsLogging.writeFileLogError("ERROR: "+e.toString(),request,1);

    }finally{

		try {
		  if (rsTran!=null)       rsTran.close();	
		  if (stmtTran!=null)     stmtTran.close();
		  ClsMngBBDD.closeConnection(con);
		}
		catch (Exception ex) {
      ClsLogging.writeFileLogError("ERROR cerrando la conexion: "+ex.toString(),request,1);
     }
    }

    return transaction+"@@"+desc;

  }

  //Clean resources
  public void destroy() {
  }
}
