package com.siga.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlAuthenticate extends SIGAServletAdapter {
	private static final long serialVersionUID = 2092588302399699149L;

	private static final String CONTENT_TYPE = "text/html";

	private String INITCTX = "";
	private String MY_HOST[] = {""};
	private String MGR_DN = "";
	private String MGR_PW = "";
	private  String MY_SEARCHBASE = "";

  //Inicializar variables globales
  //Procesar una petición HTTP Get
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
  }

  public void init() throws javax.servlet.ServletException {
    super.init();
    ReadProperties ldapProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.LDAP);
    //String LDAPPropertiesFile = cfg.getInitParameter("LDAPPROPERTIESFILE");
//    ReadProperties ldapProperties=new ReadProperties(LDAPPropertiesFile);
    INITCTX = ldapProperties.returnProperty("LDAP.INITCTX");
    String hosts = ldapProperties.returnProperty("LDAP.MY_HOST");
    StringTokenizer tok=new StringTokenizer(hosts,";");
    int i=0;
    MY_HOST=new String[tok.countTokens()];
    while (tok.hasMoreTokens()) MY_HOST[i++]=tok.nextToken();
    MGR_DN = ldapProperties.returnProperty("LDAP.MGR_DN");
    MGR_PW = ldapProperties.returnProperty("LDAP.MGR_PW");
    MY_SEARCHBASE = ldapProperties.returnProperty("LDAP.MY_SEARCHBASE");
  }


  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType(CONTENT_TYPE);

    try {
      String ldapName = request.getParameter("LDAP_NAME");
      String ldapPassword = request.getParameter("LDAP_PWD");

      ClsLogging.writeFileLog("ldapName: "+ldapName,request,2);
      ClsLogging.writeFileLog("ldapPassword: "+ldapPassword,request,2);
      /******** RELEVANTE ********/
      com.atos.utils.Mngjndi jnd = new com.atos.utils.Mngjndi();
      com.atos.utils.LdapUserEJHome home = jnd.lookupHome(request);
      Object obje = home.create(ldapName,ldapPassword,
                                request.getHeader("user-agent"),
                                request.getRemoteAddr());
      com.atos.utils.LdapUserEJ usrEJB = (com.atos.utils.LdapUserEJ)
          jnd.narrow(obje, com.atos.utils.LdapUserEJ.class,request);
      /******** RELEVANTE ********/
      usrEJB.serLDAPConf(INITCTX,MY_HOST,MGR_DN,MGR_PW,MY_SEARCHBASE);
      com.atos.utils.UsrBean usr=usrEJB.createUserSession();
      javax.servlet.http.HttpSession ses = request.getSession();
      ses.setAttribute("USRBEAN",usr);
      ClsLogging.writeFileLog("User "+ldapName+ " Authenticated! Going to SIGA Site...",request,1);
      

      String url="/Dispatch?process=-1";
      RequestDispatcher rd = request.getRequestDispatcher(url);
      ClsLogging.writeFileLog("Forward to: "+url,request,3);
      rd.forward(request, response);
    } catch (Exception e) {
      ClsLogging.writeFileLogError("Authentication error : "+e.toString(),request,1);
      String url="";
      if (e.getMessage()==null)
                url="/jsp/login.jsp?error=2";
      else
      {
      if (e.getMessage().indexOf("Invalid Password")!=-1) {
        url="/jsp/login.jsp?error=1";
      } else {
        url="/jsp/login.jsp?error=2";
      }
      }
      ClsLogging.writeFileLogError("Forwarding to: "+url,request,1);
      RequestDispatcher rd = request.getRequestDispatcher(url);
      rd.forward(request, response);
    }
  }
  
  private String chkLocation(String uid, HttpServletRequest request)
  {
    String queryAccess="select ID_LOCATION from SIGA_USER WHERE ID_USER='"+uid.toUpperCase()+"'";

    String loc="";
	Statement stmtAccess=null;
    Connection con= null;
	ResultSet rsLoc=null;

    try{
      con= ClsMngBBDD.getConnection();
      stmtAccess=con.createStatement();
      rsLoc=stmtAccess.executeQuery(queryAccess);

      while(rsLoc.next()){
        loc=rsLoc.getString("ID_LOCATION");
        ClsLogging.writeFileLog("Location: "+loc,request,3);
      }
      
      rsLoc.close();
      stmtAccess.close();


    }catch (Exception e){
      //ORA-00942: tabla o vista no encontrada
      //ORA-00923: palabra clave FROM no encontrada donde se esperaba
      //ORA-00933: comando SQL no terminado correctamente
//                 throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
      ClsLogging.writeFileLogError("ERROR: "+e.toString(),request,1);

    }finally{
    	
		try {
			if (rsLoc!=null)       rsLoc.close();
			if (stmtAccess!=null)     stmtAccess.close();
            ClsMngBBDD.closeConnection(con);
			 }
			 catch (Exception ex)
    		 {
      ClsLogging.writeFileLogError("ERROR closing connection: "+ex.toString(),request,1);
     }
    }

    return loc;
  }


  //Limpiar recursos
  public void destroy() {
	ClsLogging.writeFileLogWithoutSession("Destroy servlet Authenticate",1);
  }
}