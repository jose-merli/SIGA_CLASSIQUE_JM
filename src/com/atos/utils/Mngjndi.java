package com.atos.utils;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;

import com.siga.Utilidades.SIGAReferences;


/**
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: SLB</p>
 * @author ASD
 * @version 1.0
 */

public class Mngjndi {

  public Mngjndi() {
  }

  /**
   *
   * @return LdapUserEJHome
   * @throws NamingException
   */

  public LdapUserEJHome lookupHome()
    throws NamingException
{
//    ReadProperties prop= new ReadProperties("jndi.properties" , null);
    ReadProperties prop= new ReadProperties(SIGAReferences.RESOURCE_FILES.JNDI, null);
    String pUrl = prop.returnProperty("JNDI.DEFAULT_URL");
    String pName = prop.returnProperty("JNDI.EJB_LDAP_NAME");
    String pFact = prop.returnProperty("JNDI.IIOP_FACTORY");
    String user = prop.returnProperty("JNDI.IIOP_USER");
    String pwd = prop.returnProperty("JNDI.IIOP_PASSWORD");

    Context ctx = getInitialContext(pUrl,pFact,user,pwd);
    Object home = ctx.lookup(pName);
    Object obj = this.narrow(home, LdapUserEJHome.class);
    return (LdapUserEJHome) obj;
  }

  /**
   *
   * @param pUrl
   * @param fact
   * @param usr
   * @param pwd
   * @return Context
   * @throws NamingException
   */

  public Context getInitialContext(String pUrl, String fact, String usr, String pwd)
       throws NamingException {
       Hashtable h = new Hashtable();
       h.put(Context.INITIAL_CONTEXT_FACTORY,
             fact);
       h.put(Context.PROVIDER_URL, pUrl);
       h.put(Context.SECURITY_PRINCIPAL, usr);
       h.put(Context.SECURITY_CREDENTIALS, pwd);
       return new InitialContext(h);
  }

  /**
   *
   * @param ref
   * @param c
   * @return Object
   */

  public Object narrow(Object ref, Class c) {
    return PortableRemoteObject.narrow(ref, c);
  }

  /**
   *
   * @param req
   * @return LdapUserEJHome
   * @throws NamingException
   */

  public LdapUserEJHome lookupHome(HttpServletRequest req)
     throws NamingException
 {
     // Lookup the beans home using JNDI
    ReadProperties prop= new ReadProperties(SIGAReferences.RESOURCE_FILES.JNDI, req);
//     ReadProperties prop= new ReadProperties("jndi.properties" , req);
     String pUrl = prop.returnProperty("JNDI.DEFAULT_URL");
     String pName = prop.returnProperty("JNDI.EJB_LDAP_NAME");
     String pFact = prop.returnProperty("JNDI.IIOP_FACTORY");
     String user = prop.returnProperty("JNDI.IIOP_USER");
     String pwd = prop.returnProperty("JNDI.IIOP_PASSWORD");

     ClsLogging.writeFileLog("JNDI.EJB_LDAP_NAME: "+pName,req,3);
     ClsLogging.writeFileLog("JNDI.FACTORY: "+pFact,req,3);
     Context ctx = getInitialContext(pUrl,pFact,user,pwd,req);
     Object home = ctx.lookup(pName);
     Object obj = this.narrow(home, LdapUserEJHome.class,req);
     return (LdapUserEJHome) obj;
 }

 /**
  *
  * @param pUrl
  * @param fact
  * @param usr
  * @param pwd
  * @param req
  * @return Context
  * @throws NamingException
  */

 public Context getInitialContext(String pUrl, String fact, String usr, String pwd, HttpServletRequest req)
     throws NamingException {
     Hashtable h = new Hashtable();
     h.put(Context.INITIAL_CONTEXT_FACTORY,
           fact);
     h.put(Context.PROVIDER_URL, pUrl);
     h.put(Context.SECURITY_PRINCIPAL, usr);
     h.put(Context.SECURITY_CREDENTIALS, pwd);
     return new InitialContext(h);
 }

 /**
  *
  * @param ref
  * @param c
  * @param req
  * @return Object
  */

 public Object narrow(Object ref, Class c,HttpServletRequest req) {
   return PortableRemoteObject.narrow(ref, c);
  }
}